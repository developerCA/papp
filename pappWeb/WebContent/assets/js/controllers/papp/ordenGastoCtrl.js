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

	$scope.cProveedorCodigoFiltro = null;
	$scope.cProveedorNombreMostradoFiltro = null;
	$scope.cFechainicialFiltro = null;
	$scope.cEstadoFiltro = null;
	$scope.tmpData=[];

	$scope.edicion=false;
	$scope.divContrato=false;
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
	    $scope.aplicafiltro=false;

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

	$scope.aprobar = function(index) {
		if ($scope.data[index].estado != "SO") {
			SweetAlert.swal("Orden de Devengo!", "Solo se puede negar si esta en estado solicitado.", "error");
			return;
		}
		var modalInstance = $uibModal.open({
			templateUrl : 'modalLiquidacionManua.html',
			controller : 'ModalCertificacionesFondoLiquidacionManuaController',
			size : 'lg',
			resolve: {
				titulo: function() {
					return "Aprobar";
				},
				subtitulo : function() {
					return "Numero de CUR";
				}
			}
		});
		modalInstance.result.then(function(cur) {
			//console.log(obj);
			if (cur === undefined) {
				cur = 0;
			}
			$scope.data[index].npestado = "Aprobando";
			ordenDevengoFactory.solicitar(
				$scope.data[index].id,
				"AP",
				cur,
				null
			).then(function(resp){
				console.log(resp);
				$scope.pageChanged();
				SweetAlert.swal("Orden de Devengo!", resp.mensajes.msg, resp.mensajes.type);
			});
		}, function() {
		});
	}

	$scope.negar = function(index) {
		if ($scope.data[index].estado != "SO") {
			SweetAlert.swal("Orden de Devengo!", "Solo se puede negar si esta en estado solicitado.", "error");
			return;
		}
		var modalInstance = $uibModal.open({
			templateUrl : 'modalLiquidacionManua.html',
			controller : 'ModalCertificacionesFondoLiquidacionManuaController',
			size : 'lg',
			resolve: {
				titulo: function() {
					return "Negar";
				},
				subtitulo : function() {
					return "Observaci&oacute;n";
				}
			}
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			if (obj === undefined) {
				obj = "";
			}
			var cur = 0;
			$scope.data[index].npestado = "Negando";
			ordenDevengoFactory.solicitar(
				$scope.data[index].id,
				"NE",
				null,
				obj
			).then(function(resp){
				console.log(resp);
				$scope.pageChanged();
				SweetAlert.swal("Orden de Devengo!", resp.mensajes.msg, resp.mensajes.type);
			});
		}, function() {
		});
	}

	$scope.abrirUnidadCodigo = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalUnidades.html',
			controller : 'ModalUnidadController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.ordengastounidadid = obj.id;
			$scope.objeto.npunidadcodigo = obj.codigopresup;
			$scope.objeto.npunidadnombre = obj.nombre;
		}, function() {
		});
	};

	$scope.abrirProveedorCodigo = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalProveedor.html',
			controller : 'ModalProveedorController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.ordengastoproveedorid = obj.id;
			$scope.objeto.npproveedorcodigo = obj.codigo;
			$scope.objeto.npproveedornombre = obj.nombremostrado;
		}, function() {
		});
	};

	$scope.abrirCertificacionCodigo = function() {
		if ($scope.objeto.ordengastounidadid == 0) {
	        SweetAlert.swal(
        		"Orden de Gastos!",
        		"Tiene que seleccionar primero una Unidad",
        		"warning"
    		);
			return;
		}
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalCertificacionesFondos.html',
			controller : 'ModalCertificacionesFondosController',
			size : 'lg',
			resolve: {
				unidadid: function() {
					return $scope.objeto.ordengastounidadid;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.ordengastocertificacionid = obj.id;
			$scope.objeto.npcertificacioncodigo = obj.codigo;
			$scope.objeto.valortotal = obj.valortotal;
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
			$scope.objeto.ordengastoclaseregid = obj.id.id;
			$scope.objeto.ordengastoclasemodid = obj.id.cmid;
			$scope.objeto.ordengastocgastoid = obj.id.cmcgastoid;

			$scope.objeto.npcodigoregistro = obj.npcodigoregistro;
			$scope.objeto.npnombreregistro = obj.npnombreregistro;
			$scope.objeto.npcodigomodificacion = obj.npcodigomodificacion;
			$scope.objeto.npnombremodificacion = obj.npnombremodificacion;
			$scope.objeto.npcodigoregcmcgasto = obj.codigo;
			$scope.objeto.npnombreregcmcgasto = obj.nombre;
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
			$scope.objeto.ordengastotipodocid = obj.id.id;
			$scope.objeto.ordengastotpclasedocid = obj.id.clasedocid;
			$scope.objeto.npcodigotipodocumento = obj.codigo;
			$scope.objeto.npnombretipodocumento = obj.nombre;
			$scope.objeto.npcodigodocumento = obj.npcodigodocumento;
			$scope.objeto.npnombredocumento = obj.npnombredocumento;
		}, function() {
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

	$scope.contrato = function() {
		angular.copy($scope.data, $scope.tmpData);
		$scope.data=[];
		$scope.cLimpiar();
		$scope.edicion = false;
		$scope.divContrato = true;
	}

	var pagina = 1;
	$scope.cFiltrar = function(){
		ordenGastoFactory.traerContratoFiltro(
			pagina,
			$scope.cProveedorCodigoFiltro,
			$scope.cProveedorNombreMostradoFiltro,
			$scope.cFechainicialFiltro,
			$scope.cEstadoFiltro
		).then(function(resp){
			console.log(resp);
			if (resp.meta)
				$scope.data=resp;
		})
	}

	$scope.cLimpiar = function(){
		$scope.cProveedorCodigoFiltro = null;
		$scope.cProveedorNombreMostradoFiltro = null;
		$scope.cFechainicialFiltro = null;
		$scope.cEstadoFiltro = null;

		$scope.cFiltrar();
	};

	$scope.$watch('data', function() {
		if ($scope.data == undefined || !$scope.divContrato) {
			return;
		}
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
	$scope.popupnpcFechainicio = {
	    opened: false
	};
	$scope.opennpcFechainicio = function() {
	    $scope.popupnpcFechainicio.opened = true;
	}
} ]);