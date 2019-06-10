'use strict';

app.controller('ModalContratoController', [ "$scope","$rootScope","$uibModalInstance","objetoFuente","vTotal","noeditar","SweetAlert","$filter","ngTableParams","contratoFactory",
	function($scope,$rootScope,$uibModalInstance,objetoFuente,vTotal,noeditar,SweetAlert,$filter,ngTableParams,contratoFactory) {

	$scope.anticipo=0;
	$scope.noeditar=false;

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
			//console.log($scope.objeto);
			$scope.objeto.npfechainicio = toDate($scope.objeto.npfechainicio);
			$scope.objeto.npproveedorcodigo = objetoFuente.npproveedorcodigo;
			$scope.objeto.npproveedor = objetoFuente.npproveedornombre;
			$scope.objeto.valortotal = vTotal;
			$scope.anticipo = 0;
			$scope.noeditar = noeditar;
			$scope.edicion = true;
			$scope.nuevoar = false;
			$scope.guardar = true;
		})
	};

	$scope.calculaValor=function() {
		if ($scope.anticipo == 1) {
			// porciento
			$scope.objeto.anticipovalor = ($scope.objeto.valortotal / 100) * $scope.objeto.anticipoporcentaje;
		}
		if ($scope.anticipo == 2) {
			// valor
			$scope.objeto.anticipoporcentaje = ($scope.objeto.anticipovalor / $scope.objeto.valortotal) * 100;
		}
	}

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

	$scope.form = {
        submit: function (form) {
            var firstError = null;
            if (form.$invalid) {
                var field = null, firstError = null;
                for (field in form) {
                    if (field[0] != '$') {
                        if (firstError === null && !form[field].$valid) {
                            firstError = form[field].$name;
                        }
                        if (form[field].$pristine) {
                            form[field].$dirty = true;
                        }
                    }
                }
                angular.element('.ng-invalid[name=' + firstError + ']').focus();
                return;
            } else {
            	var tObj = {};
            	angular.copy($scope.objeto, tObj);
            	//objeto.valortotal
            	tObj.npfechainicio = $scope.toStringDate(tObj.npfechainicio);
            	contratoFactory.guardar(tObj).then(function(resp){
        			 if (resp.estado){
      					 SweetAlert.swal(
      							 "Contrato!",
      							 "Registro guardado satisfactoriamente!",
      							 "success"
						 );
      					 $uibModalInstance.close(resp.json.contrato);
        			 }else{
	 		             SweetAlert.swal(
	 		            		 "Contrato!",
	 		            		 resp.mensajes.msg,
	 		            		 "error"
	            		 );
	 		             $uibModalInstance.close(null);
        			 }
        		})
            }
        },
        reset: function (form) {
            $scope.myModel = angular.copy($scope.master);
            form.$setPristine(true);
            $scope.edicion=false;
            $scope.objeto={};
            $uibModalInstance.close(null);
        }
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
