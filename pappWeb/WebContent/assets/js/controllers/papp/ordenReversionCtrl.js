'use strict';

app.controller('OrdenReversionController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","ordenReversionFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, ordenReversionFactory) {

	$scope.dateOptions = {
	    changeYear: true,
	    changeMonth: true,
	    yearRange: '2000:-0',    
    };
	$scope.codigoFiltro=null;
	$scope.ordenGastoCodigoFiltro=null;
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
	$scope.detalles={};
	
    $scope.pagina = 1;
    $scope.aplicafiltro=false;
	
	$scope.consultar=function(){
		$scope.data = [];
		ordenReversionFactory.traer(
			$scope.pagina,
			$rootScope.ejefiscal
		).then(function(resp){
			$scope.data = resp.result;
		})
	};

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

    $scope.filtrarUnico=function(){
    	$scope.pagina=1;
    	$scope.aplicafiltro=true;
    	$scope.filtrar();
    }  

	$scope.filtrar=function(){
		ordenReversionFactory.traerFiltro(
			$scope.pagina,
			$rootScope.ejefiscal,
			$scope.codigoFiltro,
			$scope.ordenGastoCodigoFiltro,
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
		$scope.codigoFiltro=null;
		$scope.ordenGastoCodigoFiltro=null;
		$scope.valorinicialFiltro=null;
		$scope.valorfinalFiltro=null;
		$scope.fechainicialFiltro=null;
		$scope.fechafinalFiltro=null;
		$scope.estadoFiltro=null;
		$scope.aplicafiltro=false;

		$scope.consultar();
	};
	
	$scope.nuevo=function(){
		$scope.dataIndex = 0;
		ordenReversionFactory.traerNuevo(
			$rootScope.ejefiscal
		).then(function(resp){
			if (!resp.estado) return;
			$scope.objeto=resp.json.ordenreversion;
			$scope.detalles={};
			$scope.edicion=true;
			$scope.nuevoar=true;
			$scope.guardar=true;
			$scope.noeditar=false;
		})
	}

	$scope.editar=function(index){
		$scope.noeditar = ($scope.data[index].npestado == "Registrado"? false: true);
		$scope.dataIndex = index;
		ordenReversionFactory.traerEditar(
			$scope.data[index].id
		).then(function(resp){
			if (resp.estado) {
			    $scope.objeto=resp.json.ordenreversion;
			    $scope.detalles=resp.json.ordenreversionlineas;
			}
			$scope.edicion=true;
			$scope.nuevoar=false;
			$scope.guardar=true;
		})
	};

	$scope.solicitar=function(index) {
		if ($scope.data[index].estado != "RE") {
			SweetAlert.swal("Orden de Reversion!", "Solo se puede solicitar si esta en estado registrar.", "error");
			return;
		}
		if ($scope.data[index].valortotal <= 0) {
			SweetAlert.swal("Orden de Reversion!", "El valor total tiene que ser mayor que cero.", "error");
			return;
		}
		SweetAlert.swal({ 
				title: "Orden de Reversion?",
				text: "Seguro que desea hacer la solicitud!",
				type: "warning",
				showCancelButton: true,
				confirmButtonText: "Si!",
				cancelButtonText: "No",
				closeOnConfirm: false,
				closeOnCancel: false 
			}, 
			function(isConfirm) { 
				if (!isConfirm) return;
				$scope.data[index].npestado = "Solicitando";
				ordenReversionFactory.solicitar(
					$scope.data[index].id,
					"SO",
					null,
					null
				).then(function(resp){
					$scope.pageChanged();
					SweetAlert.swal("Orden de Reversion!", resp.mensajes.msg, resp.mensajes.type);
				});
			}
		); 
	}

	$scope.aprobar = function(index) {
		if ($scope.data[index].estado != "SO") {
			SweetAlert.swal("Orden de Reversion!", "Solo se puede negar si esta en estado solicitado.", "error");
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
			if (cur === undefined) {
				cur = 0;
			}
			$scope.data[index].npestado = "Aprobando";
			ordenReversionFactory.solicitar(
				$scope.data[index].id,
				"AP",
				cur,
				null
			).then(function(resp){
				$scope.pageChanged();
				SweetAlert.swal("Orden de Reversion!", resp.mensajes.msg, resp.mensajes.type);
			});
		}, function() {
		});
	}

	$scope.negar = function(index) {
		if ($scope.data[index].estado != "SO") {
			SweetAlert.swal("Orden de Reversion!", "Solo se puede negar si esta en estado solicitado.", "error");
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
			ordenReversionFactory.solicitar(
				$scope.data[index].id,
				"NE",
				null,
				obj
			).then(function(resp){
				//console.log(resp);
				$scope.pageChanged();
				SweetAlert.swal("Orden de Reversion!", resp.mensajes.msg, resp.mensajes.type);
			});
		}, function() {
		});
	}

	$scope.eliminar = function(index) {
		if ($scope.data[index].estado != "RE") {
			SweetAlert.swal("Orden de Reversion!", "No se permite eliminar este articulo, solo los que estan 'Registrados'.", "error");
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
			$scope.data[index].npestado = "Eliminando";
			ordenReversionFactory.solicitar(
				$scope.data[index].id,
				"EL",
				null,
				obj
			).then(function(resp){
				//console.log(resp);
				$scope.pageChanged();
				SweetAlert.swal("Orden de Reversion!", resp.mensajes.msg, resp.mensajes.type);
			});
		}, function() {
		});
	}

	$scope.agregarLinea = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalOrdenReversionLineas.html',
			controller : 'ModalOrdenReversionLineasController',
			size : 'lg',
			resolve : {
				ordenReversionID : function() {
					return $scope.objeto.id;
				},
				unidadID : function() {
					return $scope.objeto.id;
				},
				editar : function() {
					return null;
				},
				ordenGastoID : function() {
					return $scope.objeto.ordenreversionogastoid;
				},
				ordenGastoValor : function() {
					return 0;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			$scope.detalles = obj.ordenreversionlineas;
			$scope.objeto.valortotal = obj.ordenreversion.valortotal;
			if ($scope.dataIndex != 0) {
				$scope.data[$scope.dataIndex].valortotal = $scope.objeto.valortotal;
			}
            SweetAlert.swal(
        		"Orden Reversion! - Lineas",
        		"Registro satisfactoriamente!",
        		"success"
    		);
		}, function() {
		});
	};

	$scope.editarLinea = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalOrdenReversionLineas.html',
			controller : 'ModalOrdenReversionLineasController',
			size : 'lg',
			resolve : {
				ordenReversionID : function() {
					return $scope.objeto.id;
				},
				unidadID : function() {
					return $scope.objeto.id;
					//return $scope.objeto.certificacionunidadid;
				},
				editar : function() {
					return $scope.detalles[index].id
				},
				ordenGastoID : function() {
					return $scope.objeto.ordenreversionogastoid;
				},
				ordenGastoValor : function() {
					return 0;
				}
			}
		});
		modalInstance.result.then(function(obj) {
		    $scope.detalles = obj.ordendevengolineas;
			$scope.objeto.valortotal = obj.ordendevengo.valortotal;
			$scope.data[$scope.dataIndex].valortotal = $scope.objeto.valortotal;
            SweetAlert.swal("Orden Reversion! - Lineas", "Registro guardado satisfactoriamente!", "success");
		}, function() {
		});
	};

	$scope.eliminarLinea = function(index) {
		SweetAlert.swal({
			title: "Orden Reversion - Linea?",
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
			ordenReversionFactory.eliminarLinea(
				$scope.detalles[index].id.id,
				$scope.detalles[index].id.lineaid
			).then(function(resp){
				if (resp.estado){
					SweetAlert.swal("Orden Reversion!", "Eliminado satisfactoriamente!", "success");
					$scope.objeto.valortotal -= $scope.detalles[index].valor;
				    $scope.detalles.splice(index, 1);
					$scope.data[$scope.dataIndex].valortotal = $scope.objeto.valortotal;
	   			}else{
		            SweetAlert.swal("Orden Reversion!", resp.mensajes.msg, "error");
	   			}
          	});
		});
	};

	$scope.abrirUnidad = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalUnidadCorto.html',
			controller : 'ModalUnidadCortoController',
			size : 'lg',
			resolve : {
				estadoaprobado : function() {
					return "1";
				}
			}
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.ordenreversionunidadid = obj.id;
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
			//console.log(obj);
			$scope.objeto.ordenreversionclaseregid = obj.id.id;
			$scope.objeto.ordenreversionclasemoid = obj.id.cmid;
			$scope.objeto.ordenreversiongastoid = obj.id.cmcgastoid;
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
			$scope.objeto.ordenreversiontipodocid = obj.id.id;
			$scope.objeto.ordenreversiontpclasedocid = obj.id.clasedocid;
			$scope.objeto.npcodigotipodocumento = obj.codigo;
			$scope.objeto.npnombretipodocumento = obj.nombre;
			$scope.objeto.npcodigodocumento = obj.npcodigodocumento;
			$scope.objeto.npnombredocumento = obj.npnombredocumento;
		}, function() {
		});
	};

	$scope.abrirOrdenGasto = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalOrdenGasto.html',
			controller : 'ModalOrdenGastoController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			$scope.objeto.ordenreversionogastoid = obj.ordengastoid;
			$scope.objeto.npordengasto = obj.codigo;
			$scope.objeto.npordengastovalor = obj.valortotal;
			//$scope.objeto.valortotal = obj.ordenvalortotal;
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
            	ordenReversionFactory.guardar($scope.objeto).then(function(resp){
        			 if (resp.estado){
      				     $scope.objeto=resp.json.ordenreversion;
      					 SweetAlert.swal("Orden de Reversion!", "Registro guardado satisfactoriamente!", "success");
        				 if ($scope.nuevoar) {
	      					 $scope.noeditar = false;
	      					 $scope.nuevoar=false;
        				 } else {
    			            form.$setPristine(true);
    			            $scope.edicion=false;
    			            $scope.objeto={};
        				 }
        			 }else{
	 		             SweetAlert.swal("Orden de Reversion!", resp.mensajes.msg, "error");
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
