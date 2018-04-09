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
			console.log(resp);
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
			console.log(resp);
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
			    $scope.detalles=[];
			}
			//$scope.
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
			    $scope.detalles=resp.json.ordengastolineas;
			}
			//agregarLinea$scope.noeditar=false;
			$scope.edicion=true;
			$scope.nuevoar=false;
			$scope.guardar=true;
		})
	};

	$scope.solicitar = function(index) {
		//console.log($scope.data[index]);
		if ($scope.data[index].estado != "RE") {
			SweetAlert.swal("Orden de Gasto!", "Solo se puede solicitar si esta en estado registrar.", "error");
			return;
		}
		if ($scope.data[index].valortotal <= 0) {
			SweetAlert.swal("Orden de Gasto!", "El valor total tiene que ser mayor que cero.", "error");
			return;
		}
		SweetAlert.swal({ 
				title: "Orden de Gasto?",
				text: "Seguro que desea hacer la solicitud!",
				type: "warning",
				showCancelButton: true,
				confirmButtonText: "Si!",
				cancelButtonText: "No",
				closeOnConfirm: true,
				closeOnCancel: true 
			}, 
			function(isConfirm) { 
				if (!isConfirm) return;
				$scope.data[index].npestado = "Solicitando";
				ordenGastoFactory.solicitar(
					$scope.data[index].id,
					"SO",
					null,
					null
				).then(function(resp){
					//console.log(resp);
					$scope.pageChanged();
					SweetAlert.swal("Orden de Gasto!", resp.mensajes.msg, resp.mensajes.type);
				});
			}
		); 
	}

	$scope.aprobar = function(index) {
		if ($scope.data[index].estado != "SO") {
			SweetAlert.swal("Orden de Gasto!", "Solo se puede aprobar si esta en estado solicitado.", "error");
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
					return "CUR";
				}
			}
		});
		modalInstance.result.then(function(cur) {
			//console.log(obj);
			if (cur === undefined) {
				cur = 0;
			}
			$scope.data[index].npestado = "Aprobando";
			ordenGastoFactory.solicitar(
				$scope.data[index].id,
				"AP",
				cur,
				null
			).then(function(resp){
				console.log(resp);
				$scope.pageChanged();
				SweetAlert.swal("Orden de Gasto!", resp.mensajes.msg, resp.mensajes.type);
			});
		}, function() {
		});
	}

	$scope.negar = function(index) {
		if ($scope.data[index].estado != "SO") {
			SweetAlert.swal("Orden de Gasto!", "Solo se puede negar si esta en estado solicitado.", "error");
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
					return "Observacion";
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
			ordenGastoFactory.solicitar(
				$scope.data[index].id,
				"NE",
				null,
				obj
			).then(function(resp){
				console.log(resp);
				$scope.pageChanged();
				SweetAlert.swal("Orden de Gasto!", resp.mensajes.msg, resp.mensajes.type);
			});
		}, function() {
		});
	}

	$scope.abrirUnidadCodigo = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalUnidadCorto.html',
			controller : 'ModalUnidadCortoController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
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
			$scope.objeto.ordengastocertificacionid = obj.certificacionid;
			$scope.objeto.npcertificacioncodigo = obj.codigo;
			//$scope.objeto.valortotal = obj.valortotal;
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

	$scope.anular = function(index) {
		if ($scope.data[index].estado != "AP") {
			SweetAlert.swal(
	    		"Orden de Gastos!",
				"No se permite anular este articulo, solo los que estan 'Aprobado'.",
				"error"
			);
			return;
		}
		var modalInstance = $uibModal.open({
			templateUrl : 'modalLiquidacionManua.html',
			controller : 'ModalCertificacionesFondoLiquidacionManuaController',
			size : 'lg',
			resolve: {
				titulo: function() {
					return "Anular";
				},
				subtitulo : function() {
					return "Observacion";
				}
			}
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			if (obj === undefined) {
				obj = "";
			}
			var cur = 0;
			$scope.data[index].npestado = "Anulando";
			ordenGastoFactory.solicitar(
				$scope.data[index].id,
				"AN",
				null,
				obj
			).then(function(resp){
				if (resp.estado) {
					$scope.pageChanged();
		            SweetAlert.swal(
	            		"Orden de Gastos!",
	            		"Registro anulado satisfactoriamente!",
	            		"success"
            		);
				} else {
		            SweetAlert.swal(
	            		"Orden de Gastos!",
	            		resp.mensajes.msg,
	            		"error"
            		);
				}
			});
		}, function() {
		});
	}

	$scope.eliminar = function(index) {
		if ($scope.data[index].estado != "RE") {
			SweetAlert.swal(
	    		"Orden de Gastos!",
				"No se permite eliminar este articulo, solo los que estan 'Registrados'.",
				"error"
			);
			return;
		}
		var modalInstance = $uibModal.open({
			templateUrl : 'modalLiquidacionManua.html',
			controller : 'ModalCertificacionesFondoLiquidacionManuaController',
			size : 'lg',
			resolve: {
				titulo: function() {
					return "Eliminar";
				},
				subtitulo : function() {
					return "Observacion";
				}
			}
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			if (obj === undefined) {
				obj = "";
			}
			var cur = 0;
			$scope.data[index].npestado = "Eliminando";
			ordenGastoFactory.solicitar(
				$scope.data[index].id,
				"EL",
				null,
				obj
			).then(function(resp){
				if (resp.estado) {
					$scope.pageChanged();
		            SweetAlert.swal(
	            		"Orden de Gastos!",
	            		"Registro eliminado satisfactoriamente!",
	            		"success"
            		);
				} else {
		            SweetAlert.swal(
	            		"Orden de Gastos!",
	            		resp.mensajes.msg,
	            		"error"
            		);
				}
			});
		}, function() {
		});
	}

	$scope.contrato = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalContrato.html',
			controller : 'ModalContratoController',
			size : 'lg',
			resolve: {
				objetoFuente: function() {
					return $scope.objeto;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			if (obj != null) {
				$scope.objeto.ordengastocontratoid = obj.id;
				$scope.form.submit(Form);
			}
		}, function() {
		});
	}

	$scope.agregarLinea = function() {
		//console.log($scope.objeto);
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalOrdenGastoLineas.html',
			controller : 'ModalOrdenGastoLineasController',
			size : 'lg',
			resolve : {
				ID : function() {
					return $scope.objeto.id;
				},
				certificacionID : function() {
					return $scope.objeto.ordengastocertificacionid;
				},
				editar : function() {
					return null;
				},
				npcertificacionvalor : function() {
					return null;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);//130
		    $scope.detalles = obj.lineas;
		    $scope.objeto.valortotal = obj.valortotal;
			$scope.form.submit(Form);
            SweetAlert.swal("Orden Gasto! - Lineas", "Registro guardado satisfactoriamente!", "success");
		}, function() {
		});
	};

	$scope.editarLinea = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalOrdenGastoLineas.html',
			controller : 'ModalOrdenGastoLineasController',
			size : 'lg',
			resolve : {
				ID : function() {
					return $scope.objeto.ordengastocertificacionid;
				},
				certificacionID : function() {
					return $scope.objeto.id;
				},
				editar : function() {
					return $scope.detalles[index].id;
				},
				npcertificacionvalor : function() {
					return $scope.objeto.npcertificacionvalor;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
		    $scope.detalles = obj.lineas;
		    $scope.objeto.valortotal = obj.valortotal;
			$scope.form.submit(Form);
            SweetAlert.swal("Orden Gasto! - Lineas", "Registro guardado satisfactoriamente!", "success");
		}, function() {
		});
	};

	$scope.eliminarLinea = function(index) {
		SweetAlert.swal({
			title: "Orden Gasto! - Lineas",
			text: "Seguro que desea eliminar esta linea?",
			type: "warning",
			showCancelButton: true,
			confirmButtonClass: "btn-danger",
			confirmButtonText: "SI!",
			cancelButtonText: "NO",
			closeOnConfirm: false,
			closeOnCancel: true
		},
		function(isConfirm) {
			if (!isConfirm) return;
			ordenGastoFactory.eliminarLinea(
				$scope.detalles[index].id.id,
				$scope.detalles[index].id.lineaid
			).then(function(resp){
				if (resp.estado){
					SweetAlert.swal("Orden Gasto! - Lineas!", "Eliminado satisfactoriamente!", "success");
					$scope.objeto.valortotal -= $scope.detalles[index].npvalor;
				    $scope.detalles.splice(index, 1);
				    $scope.form.submit(Form);
	   			}else{
		            SweetAlert.swal("Orden Gasto! - Lineas!", resp.mensajes.msg, "error");
	   			}
          	})
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
            	ordenGastoFactory.guardar($scope.objeto).then(function(resp){
        			 if (resp.estado){
        				 if ($scope.nuevoar) {
	      					 $scope.noeditar = false;
	      					 $scope.nuevoar=false;
	      				     $scope.objeto=resp.json.ordengasto;
        				 } else {
            				 form.$setPristine(true);
    	 		             $scope.edicion=false;
    	 		             $scope.objeto={};
    	 		             //$scope.limpiar();
        				 }
        		         $scope.pageChanged();
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
            $scope.pageChanged();
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