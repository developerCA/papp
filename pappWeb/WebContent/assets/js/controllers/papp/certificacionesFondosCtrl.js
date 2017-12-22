'use strict';

app.controller('CertificacionesFondosController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","certificacionesFondosFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, certificacionesFondosFactory) {

	$scope.dateOptions = {
	    changeYear: true,
	    changeMonth: true,
	    yearRange: '2000:-0',    
    };
	$scope.codigoFiltro=null;
	$scope.precompromisoFiltro=null;
	$scope.valorinicialFiltro=null;
	$scope.valorfinalFiltro=null;
	$scope.fechainicialFiltro=null;
	$scope.fechafinalFiltro=null;
	$scope.estadoFiltro=null;

	$scope.data=[];
	$scope.edicion=false;
	$scope.nuevoar=false;
	$scope.guardar=false;
	$scope.objeto={estado:null};
	$scope.objetodetalles={};
	
    $scope.pagina = 1;
	
	$scope.consultar=function(){
		certificacionesFondosFactory.traer(
			$scope.pagina,
			$rootScope.ejefiscal
		).then(function(resp){
			//console.log(resp);
        	$scope.data = resp.json.result;
            $scope.total = resp.json.total.valor;
			console.log($scope.data);
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
    	$scope.filtrar();
    }  

	$scope.filtrar=function(){
		//console.log($rootScope.ejefiscal);
		//console.log($rootScope);
		$scope.data=[];
		certificacionesFondosFactory.traerFiltro(
			$scope.pagina,
			$rootScope.ejefiscal,
			$scope.codigoFiltro,
			$scope.precompromisoFiltro,
			$scope.valorinicialFiltro,
			$scope.valorfinalFiltro,
			toStringDate($scope.fechainicialFiltro),
			toStringDate($scope.fechafinalFiltro),
			$scope.estadoFiltro
		).then(function(resp){
        	$scope.data = resp.json.result;
            $scope.total = resp.json.total.valor;
		})
	}
/*
	$scope.$watch('data', function() {
		$scope.tableParams = new ngTableParams({
			page : 1, // show first page
			count : 5, // count per page
			filter: {} 	
		}, {
			total : $scope.data.length, // length of data
			getData : function($defer, params) {
				var orderedData = params.filter() ? $filter('filter')(
						$scope.data, params.filter()) : $scope.data;
				$scope.lista = orderedData.slice(
						(params.page() - 1) * params.count(), params
								.page()
								* params.count());
				params.total(orderedData.length);
				$defer.resolve($scope.lista);
			}
		});
	});
*/
	$scope.limpiar=function(){
		$scope.codigoFiltro=null;
		$scope.precompromisoFiltro=null;
		$scope.valorinicialFiltro=null;
		$scope.valorfinalFiltro=null;
		$scope.fechainicialFiltro=null;
		$scope.fechafinalFiltro=null;
		$scope.estadoFiltro=null;

		$scope.consultar();
	};
	
	$scope.nuevo=function(){
		//console.log($rootScope.ejefiscal);
		//console.log($rootScope);
		certificacionesFondosFactory.traerCertificacionesFondosNuevo(
			$rootScope.ejefiscal
		).then(function(resp){
			//console.log(resp);
			if (!resp.estado) return;
			$scope.objeto=resp.json.certificacion;
			$scope.objetodetalles={};
			$scope.agregarDetalles();
			$scope.edicion=true;
			$scope.nuevoar=true;
			$scope.guardar=true;
		})
	}

	$scope.editar=function(id){
		certificacionesFondosFactory.traerCertificacionesFondosEditar(id).then(function(resp){
			console.log(resp.json);
			if (resp.estado) {
			    $scope.objeto=resp.json.certificacion;
			    $scope.objetodetalles=resp.json.certificacionlineas;
			}
			$scope.edicion=true;
			$scope.nuevoar=false;
			$scope.guardar=true;
		})
	};

	$scope.solicitar=function(id) {
		certificacionesFondosFactory.solicitar(id).then(function(resp){
	        SweetAlert.swal("Certificaciones de Fondos!", "Solicitud enviada", "success");
		});
	}

	$scope.liquidarManualMente = function(id) {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalLiquidacionManua.html',
			controller : 'ModalCertificacionesFondoLiquidacionManuaController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
/*			certificacionesFondosFactory.liquidarManualMente(id, obj).then(function(resp){
		        SweetAlert.swal("Certificaciones de Fondos!", "Solicitud enviada", "success");
			});
*/		}, function() {
		});
	};

	$scope.eliminar = function(id) {
		SweetAlert.swal({
			title: "Certificaciones de Fondos?",
			text: "Seguro que desea eliminar la orden?",
			type: "warning",
			showCancelButton: true,
			confirmButtonClass: "btn-danger",
			confirmButtonText: "SI!",
			cancelButtonText: "NO",
			closeOnConfirm: false,
			closeOnCancel: true
		},
		function(isConfirm) {
			if (isConfirm) {
				certificacionesFondosFactory.eliminar(id).then(function(resp){
					SweetAlert.swal("Certificaciones de Fondos!", "Se solicito la eliminacion", "success");
				});
			}
		});
	}

	$scope.agregarDetalles=function(){
		$scope.objetodetalles={id: null};
	};

	$scope.abrirUnidad = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalUnidadCorto.html',
			controller : 'ModalUnidadCortoController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.certificacionunidadid = obj.id;
			$scope.objeto.npunidadcodigo = obj.codigopresup;
			$scope.objeto.npunidadnombre = obj.nombre;
		}, function() {
		});
	};

	$scope.abrirClaseRegistroCodigo = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalClaseGasto.html',
			controller : 'ModalClaseGastoController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.certificacionclaseregid = obj.id.id;
			$scope.objeto.certificacionclasemoid = obj.id.cmid;
			$scope.objeto.certificaciongastoid = obj.id.cmcgastoid;
			$scope.objeto.npcodigoregcmcgasto = obj.codigo;
			$scope.objeto.npnombreregcmcgasto = obj.nombre;
			$scope.objeto.npcodigoregistro = obj.npcodigoregistro;
			$scope.objeto.npnombreregistro = obj.npnombreregistro;
			$scope.objeto.npcodigomodificacion = obj.npcodigomodificacion;
			$scope.objeto.npnombremodificacion = obj.npnombremodificacion;
		}, function() {
		});
	};

	$scope.abrirTipoDocumentoCodigo = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalClaseDocumento.html',
			controller : 'ModalClaseDocumentoController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.certificaciontipodocid = obj.id.id;
			$scope.objeto.certificaciontpclasedocid = obj.id.clasedocid;
			$scope.objeto.npcodigotipodocumento = obj.codigo;
			$scope.objeto.npnombretipodocumento = obj.nombre;
			$scope.objeto.npcodigodocumento = obj.npcodigodocumento;
			$scope.objeto.npnombredocumento = obj.npnombredocumento;
		}, function() {
		});
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
            	certificacionesFondosFactory.guardar($scope.objeto).then(function(resp){
        			 if (resp.estado){
        				 form.$setPristine(true);
	 		             $scope.edicion=false;
	 		             $scope.objeto={};
	 		             $scope.limpiar();
	 		             SweetAlert.swal("Certificaciones de Fondos!", "Registro registrado satisfactoriamente!", "success");
        			 }else{
	 		             SweetAlert.swal("Certificaciones de Fondos!", resp.mensajes.msg, "error");
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
			//please put attention to the month (parts[0]), Javascript counts months from 0:
			// January - 0, February - 1, etc
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
