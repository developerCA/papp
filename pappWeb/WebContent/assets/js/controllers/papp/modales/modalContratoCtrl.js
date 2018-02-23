'use strict';

app.controller('ModalContratoController', [ "$scope","$rootScope","$uibModalInstance","objetoFuente","SweetAlert","$filter","ngTableParams","contratoFactory",
	function($scope,$rootScope,$uibModalInstance,objetoFuente,SweetAlert,$filter,ngTableParams,contratoFactory) {

	$scope.anticipo=0;

	$scope.editar=function() {
		contratoFactory.editar(
			objetoFuente
		).then(function(resp){
			//console.log(resp);
			if (!resp.estado) {
				SweetAlert.swal("Contrato!", resp.mensajes.msg, "error");
				$uibModalInstance.dismiss('cancel');
				return;
			}
			$scope.objeto=resp.json.contrato;
			$scope.objeto.npfechainicio = toDate($scope.objeto.npfechainicio);
			$scope.anticipo=0;
			$scope.noeditar=false;
			$scope.edicion=true;
			$scope.nuevoar=false;
			$scope.guardar=true;
		})
	};

	function toDate(fuente) {
		try {
			var parts = fuente.split('/');
		} catch (err) {
			return new Date();
		}
		return new Date(parts[2]*1,parts[1]-1,parts[0]*1, 0, 0, 0, 0); 
	}

	function toStringDate(fuente) {
		if (fuente == null) {
			return null;
		}
		try {
			var parts = fuente.toISOString();
			parts = parts.split('T');
			parts = parts[0].split('-');
		} catch (err) {
			return null;
		}
		return parts[2] + "/" + parts[1] + "/" + parts[0]; 
	}

	$scope.popupnpFechainicio = {
	    opened: false
	};

	$scope.opennpFechainicio = function() {
	    $scope.popupnpFechainicio.opened = true;
	}

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);
	};

	$scope.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};

	$scope.calcuarFechaFin = function() {
		$scope.objeto.npfechafin = $scope.toStringDate(
			$scope.sumaDias(
				$scope.objeto.npfechainicio,
				$scope.objeto.duraciondias
			)
		);
	};

	$scope.sumaDias = function(fecha, numDias) {
		if (numDias == undefined || numDias.toString().trim() == "") {
			numDias = 0;
		}
		var nfecha = new Date(fecha);
		nfecha.setDate(fecha.getDate() + parseInt(numDias));
		return nfecha;
	}

	$scope.toStringDate = function(fuente) {
		if (fuente == null) {
			return null;
		}
		try {
			var parts = fuente.toISOString();
			parts = parts.split('T');
			parts = parts[0].split('-');
		} catch (err) {
			return null;
		}
		return parts[2] + "/" + parts[1] + "/" + parts[0]; 
	}
}]);
