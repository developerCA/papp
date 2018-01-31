'use strict';

app.controller('OrdenGastoController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","ordenGastoFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, ordenGastoFactory) {

	$scope.codigoFiltro = null;
	$scope.compromisoFiltro = null;
	$scope.certificacionFiltro = null;
	$scope.valorinicialFiltro = null;
	$scope.valorfinalFiltro = null;
	$scope.fechainicialFiltro = null;
	$scope.fechafinalFiltro = null;
	$scope.estadoFiltro = null;

	$scope.edicion=false;
	$scope.nuevoar=false;
	$scope.guardar=false;
	$scope.objeto={estado:null};
	$scope.objetodetalles={};
	
    $scope.pagina = 1;
    $scope.aplicafiltro=false;
	
	$scope.consultar=function(){
		ordenGastoFactory.traer(
			$scope.pagina,
			$rootScope.ejefiscal
		).then(function(resp){
			//console.log(resp);
        	$scope.data = resp.result;
            $scope.total = resp.total.valor;
		})
	};

    $scope.pageChanged = function() {
        if ($scope.aplicafiltro){
        	$scope.filtrar();
        }else{
        	$scope.consultar();	
        }
    };  
    
    $scope.filtrarUnico=function(){
    	$scope.pagina=1;
    	$scope.aplicafiltro=true;
    	$scope.filtrar();
    }  

	$scope.filtrar=function(){
		ordenGastoFactory.traerFiltro(
			$scope.pagina,
			$rootScope.ejefiscal,
			$scope.codigoFiltro,
			$scope.compromisoFiltro,
			$scope.certificacionFiltro,
			$scope.valorinicialFiltro,
			$scope.valorfinalFiltro,
			toStringDate($scope.fechainicialFiltro),
			toStringDate($scope.fechafinalFiltro),
			$scope.estadoFiltro
		).then(function(resp){
        	$scope.data = resp.result;
            $scope.total = resp.total.valor;
		})
	}

	$scope.limpiar=function(){
		$scope.codigoFiltro = null;
		$scope.compromisoFiltro = null;
		$scope.certificacionFiltro = null;
		$scope.valorinicialFiltro = null;
		$scope.valorfinalFiltro = null;
		$scope.fechainicialFiltro = null;
		$scope.fechafinalFiltro = null;
		$scope.estadoFiltro = null;

		$scope.consultar();
	};
	
	$scope.nuevo=function(){
		ordenGastoFactory.nuevo(
			$rootScope.ejefiscal
		).then(function(resp){
			console.log(resp);
			if (resp.estado) {
			    $scope.objeto=resp.json.ordengasto;
			}
			$scope.noeditar=false;
			$scope.edicion=true;
			$scope.nuevoar=true;
			$scope.guardar=true;
		})
	}
	
	$scope.editar=function(index) {
		$scope.noeditar = ($scope.data[index].npestado == 'Registrado'? false: true);
		ordenGastoFactory.editar(
			$scope.data[index].id
		).then(function(resp){
			console.log(resp);
			if (resp.estado) {
			    $scope.objeto=resp.json.ordengasto;
			}
			$scope.noeditar=false;
			$scope.edicion=true;
			$scope.nuevoar=false;
			$scope.guardar=true;
		})
	};

	$scope.abrirProveedorCodigo = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/js/controllers/papp/modales/modalProveedor.html',
			controller : 'ModalProveedorController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			return;
			$scope.objeto.certificacionunidadid = obj.id;
			$scope.objeto.npunidadcodigo = obj.codigopresup;
			$scope.objeto.npunidadnombre = obj.nombre;
		}, function() {
			console.log("close modal");
		});
	};

	$scope.abrirUnidadCodigo = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalUnidades.html',
			controller : 'ModalUnidadController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.certificacionunidadid = obj.id;
			$scope.objeto.npunidadcodigo = obj.codigopresup;
			$scope.objeto.npunidadnombre = obj.nombre;
		}, function() {
			console.log("close modal");
		});
	};

	$scope.abrirClaseRegistroCodigo = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalClaseRegistro.html',
			controller : 'ModalClaseRegistroController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.certificacionclaseregid = obj.id;
			$scope.objeto.npcodigoregistro = obj.codigo;
			$scope.objeto.npnombreregistro = obj.nombre;
		}, function() {
			console.log("close modal");
		});
	};

	$scope.abrirTipoDocumentoCodigo = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalTipoDocumento.html',
			controller : 'ModalTipoDocumentoController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.certificaciontipodocid = obj.id;
			$scope.objeto.npcodigotipodocumento = obj.codigo;
			$scope.objeto.npnombretipodocumento = obj.nombre;
		}, function() {
			console.log("close modal");
		});
	};

	$scope.liquidarManualMente = function(id) {
		console.log("ok");
		var modalInstance = $uibModal.open({
			templateUrl : 'modalLiquidacionManua.html',
			controller : 'ModalCertificacionesFondoLiquidacionManuaController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
/*
			$scope.objeto.certificaciontipodocid = obj.id;
			$scope.objeto.npcodigotipodocumento = obj.codigo;
			$scope.objeto.npnombretipodocumento = obj.nombre;
*/
		}, function() {
			console.log("close modal");
		});
	};

	$scope.eliminar = function(id) {
/*
		SweetAlert.swal({
			title: "ok",
			text: "Seguro que desea eliminar la orden?",
			buttons: {
				cancel: true,
				confirm: "No",
			    roll: {
			        text: "Si",
			        value: "roll",
			    }
			}
		});
*/
        SweetAlert.swal("Orden de Gastos!", "No se pudo eliminar porque falta REST para eliminar", "error");
/*
		ordenGastoFactory.eliminar(id).then(function(resp){
			if (resp.estado){
	             $scope.objeto={};
	             $scope.limpiar();
	             SweetAlert.swal("Orden de Gastos!", "Registro eliminado satisfactoriamente!", "success");
			}else{
	             SweetAlert.swal("Orden de Gastos!", resp.mensajes.msg, "error");
			}
		});
*/
	}

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
            	ordenGastoFactory.guardar($scope.objeto).then(function(resp){
        			 if (resp.estado){
        				 form.$setPristine(true);
	 		             $scope.edicion=false;
	 		             $scope.objeto={};
	 		             $scope.limpiar();
	 		             SweetAlert.swal("Orden de Gastos!", "Registro registrado satisfactoriamente!", "success");
        			 }else{
	 		             SweetAlert.swal("Orden de Gastos!", resp.mensajes.msg, "error");
        			 }
        		})
            }
        },
        reset: function (form) {
            $scope.myModel = angular.copy($scope.master);
            form.$setPristine(true);
            $scope.edicion=false;
            $scope.objeto={};
        }
    };

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
	$scope.popupnpFechafin = {
	    opened: false
	};
	$scope.opennpFechafin = function() {
	    $scope.popupnpFechafin.opened = true;
	}
} ]);