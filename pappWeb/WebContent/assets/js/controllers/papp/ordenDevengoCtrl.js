'use strict';

app.controller('OrdenDevengoController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","ordenDevengoFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, ordenDevengoFactory) {

	$scope.rol=function(nombre) {
		return ifRollPermiso(nombre);
	}

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
	$scope.estadoFiltro="AP";

	$scope.data=[];
	$scope.edicion=false;
	$scope.nuevoar=false;
	$scope.guardar=false;
	$scope.objeto={estado:null};
	$scope.detalles={};
	$scope.divVista=false;
	
    $scope.pagina = 1;
    $scope.aplicafiltro=false;
	
	$scope.consultar=function(){
		$scope.data = [];
		ordenDevengoFactory.traer(
			$scope.pagina,
			$rootScope.ejefiscal,
			$scope.estadoFiltro
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
		ordenDevengoFactory.traerFiltro(
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
		$scope.estadoFiltro="AP";
		$scope.aplicafiltro=false;

		$scope.consultar();
	};
	
	$scope.nuevo=function(){
		$scope.dataIndex = 0;
		ordenDevengoFactory.traerNuevo(
			$rootScope.ejefiscal
		).then(function(resp){
			//console.log(resp);
			if (!resp.estado) return;
			$scope.objeto=resp.json.ordendevengo;
			$scope.objeto.tipo='L';
			$scope.detalles={};
			$scope.edicion=true;
			$scope.nuevoar=true;
			$scope.guardar=true;
			$scope.noeditar=false;
		})
	}

	$scope.editar=function(index){
		//console.log($scope.data[index]);
		index = $scope.calcularIndex(index);
		$scope.noeditar = ($scope.data[index].npestado == "Registrado"? false: true);
		$scope.dataIndex = index;
		ordenDevengoFactory.traerEditar(
			$scope.data[index].id
		).then(function(resp){
			//console.log(resp.json);
			if (resp.estado) {
			    $scope.objeto=resp.json.ordendevengo;
			    $scope.detalles=resp.json.ordendevengolineas;
			}
			//console.log($scope.objeto.codigo);
			$scope.edicion=true;
			$scope.nuevoar=false;
			$scope.guardar=true;
		})
	};

	$scope.visualizar=function(index){
		//console.log($scope.data[index]);
		//return;
		index = $scope.calcularIndex(index);
		ordenDevengoFactory.traerEditar(
			$scope.data[index].id
		).then(function(resp){
			//console.log(resp.json);
			if (resp.estado) {
			    $scope.objeto=resp.json.ordendevengo;
			    $scope.detalles=resp.json.ordendevengolineas;
			}
			//console.log($scope.objeto.codigo);
			//$scope.edicion=true;
			$scope.divVista=true;
			$scope.nuevoar=false;
			$scope.guardar=true;
		})
	};

	$scope.solicitar = function(index) {
		index = $scope.calcularIndex(index);
		if ($scope.data[index].estado != "RE") {
			SweetAlert.swal(
					"Orden de Devengo!",
					"Solo se puede solicitar si esta en estado registrar.",
					"error"
			);
			return;
		}
		if ($scope.data[index].valortotal <= 0) {
			SweetAlert.swal(
					"Orden de Devengo!",
					"El valor total tiene que ser mayor que cero.",
					"error"
			);
			return;
		}
		SweetAlert.swal({ 
				title: "Orden de Devengo?",
				text: "Seguro que desea hacer la solicitud?",
				type: "warning",
				showCancelButton: true,
				confirmButtonText: "SI!",
				cancelButtonText: "NO",
				closeOnConfirm: true,
				closeOnCancel: true 
			}, 
			function(isConfirm) { 
				if (!isConfirm) return;
				$scope.data[index].npestado = "Solicitando";
				ordenDevengoFactory.solicitar(
					$scope.data[index].id,
					"SO",
					null,
					null
				).then(function(resp){
					//console.log(resp);
					//$scope.pageChanged();
					SweetAlert.swal(
						"Orden de Devengo!",
						resp.mensajes.msg,
						resp.mensajes.type
					);
				});
			}
		); 
	}

	$scope.aprobar = function(index) {
		index = $scope.calcularIndex(index);
		if ($scope.data[index].estado != "SO") {
			SweetAlert.swal(
				"Orden de Devengo!",
				"Solo se puede aprobar si esta en estado solicitado.",
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
				if (resp.estado) {
					//$scope.pageChanged();
					SweetAlert.swal(
						"Orden de Devengo!",
		        		"Registro guardado satisfactoriamente!",
		        		"success"
					);
				} else {
					SweetAlert.swal(
						"Orden de Devengo!",
		        		"No se pudo eliminar",
		        		"error"
					);
				}
			});
		}, function() {
		});
	}

	$scope.anular = function(index) {
		index = $scope.calcularIndex(index);
		if ($scope.data[index].estado != "SO") {
			SweetAlert.swal(
					"Orden de Devengo!",
					"Solo se puede anular si esta en estado solicitado.",
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
			if (obj === undefined) {
				obj = "";
			}
			var cur = 0;
			$scope.data[index].npestado = "Anular";
			ordenDevengoFactory.solicitar(
				$scope.data[index].id,
				"AN",
				null,
				obj
			).then(function(resp){
				SweetAlert.swal(
					"Orden de Devengo!",
					resp.mensajes.msg,
					resp.mensajes.type
				);
			});
		}, function() {
		});
	}

	$scope.negar = function(index) {
		index = $scope.calcularIndex(index);
		if ($scope.data[index].estado != "SO") {
			SweetAlert.swal(
					"Orden de Devengo!",
					"Solo se puede negar si esta en estado solicitado.",
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
			ordenDevengoFactory.solicitar(
				$scope.data[index].id,
				"NE",
				null,
				obj
			).then(function(resp){
				SweetAlert.swal(
					"Orden de Devengo!",
					resp.mensajes.msg,
					resp.mensajes.type
				);
			});
		}, function() {
		});
	}

	$scope.eliminar = function(index) {
		index = $scope.calcularIndex(index);
		if ($scope.data[index].estado != "RE") {
			SweetAlert.swal(
				"Orden de Devengo!",
				"No se permite eliminar este registro, solo los que estan 'Registrados'.",
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
					return "Motivo de eliminacion";
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
			ordenDevengoFactory.solicitar(
				$scope.data[index].id,
				"EL",
				null,
				obj
			).then(function(resp){
				if (resp.estado) {
					//$scope.pageChanged();
					SweetAlert.swal(
						"Orden de Devengo!",
		        		"Registro guardado satisfactoriamente!",
		        		"success"
					);
				} else {
					SweetAlert.swal(
						"Orden de Devengo!",
		        		"No se pudo eliminar",
		        		"error"
					);
				}
			});
		}, function() {
		});
	}

	$scope.agregarDetalles=function(){
		$scope.detalles={id: null};
	};

	$scope.agregarLinea = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalOrdenDevengoLineas.html',
			controller : 'ModalOrdenDevengoLineasController',
			size : 'lg',
			resolve : {
				ordenDevengoID : function() {
					return $scope.objeto.id;
				},
				unidadID : function() {
					return $scope.objeto.id;
				},
				editar : function() {
					return null;
				},
				ordenGastoID : function() {
					return $scope.objeto.ordendevengoordengastoid;
				},
				ordenGastoValor : function() {
					return $scope.objeto.tipo;
					//return ($scope.objeto.tipo == 'L'? $scope.objeto.npordengastovalor: 0);
				},
				valorTotal : function() {
					return $scope.objeto.valortotal;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);//130
		    $scope.detalles = obj.ordendevengolineas;
			$scope.objeto.valortotal = obj.ordendevengo.valortotal;
			if ($scope.dataIndex != 0) {
				$scope.data[$scope.dataIndex].valortotal = $scope.objeto.valortotal;
			}
            SweetAlert.swal(
        		"Orden Devengo! - Lineas",
        		"Registro guardado satisfactoriamente!",
        		"success"
    		);
		}, function() {
		});
	};

	$scope.editarLinea = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalOrdenDevengoLineas.html',
			controller : 'ModalOrdenDevengoLineasController',
			size : 'lg',
			resolve : {
				ordenDevengoID : function() {
					return $scope.objeto.id;
				},
				unidadID : function() {
					return $scope.objeto.certificacionunidadid;
				},
				editar : function() {
					return $scope.detalles[index].id
				},
				ordenGastoID : function() {
					return $scope.objeto.ordendevengoordengastoid;
				},
				ordenGastoValor : function() {
					return ($scope.objeto.tipo == 'L'? $scope.objeto.npordengastovalor: 0);
				},
				valorTotal : function() {
					return $scope.objeto.valortotal;
				}
			}
		});
		modalInstance.result.then(function(obj) {
		    $scope.detalles = obj.ordendevengolineas;
			$scope.objeto.valortotal = obj.ordendevengo.valortotal;
			$scope.data[$scope.dataIndex].valortotal = $scope.objeto.valortotal;
            SweetAlert.swal(
            		"Orden Devengo! - Lineas",
            		"Registro guardado satisfactoriamente!",
            		"success"
    		);
		}, function() {
		});
	};

	$scope.eliminarLinea = function(index) {
		SweetAlert.swal({
			title: "Orden Devengo - Linea?",
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
			ordenDevengoFactory.eliminarLinea(
				$scope.detalles[index].id.id,
				$scope.detalles[index].id.lineaid
			).then(function(resp){
				if (resp.estado){
					SweetAlert.swal("Orden Devengo!", "Eliminado satisfactoriamente!", "success");
					$scope.objeto.valortotal -= $scope.detalles[index].valor;
				    $scope.detalles.splice(index, 1);
					$scope.data[$scope.dataIndex].valortotal = $scope.objeto.valortotal;
	   			}else{
		            SweetAlert.swal("Orden Devengo!", resp.mensajes.msg, "error");
	   			}
          	});
		});
	};

	$scope.abrirOrdenGasto = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalOrdenGasto.html',
			controller : 'ModalOrdenGastoController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.ordendevengoordengastoid = obj.ordengastoid;
			$scope.objeto.npordengasto = obj.codigo;
			$scope.objeto.npordengastovalor = obj.valortotal;
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
            	if ($scope.objeto.ordendevengoordengastoid === undefined) {
 					SweetAlert.swal(
  							 "Orden de Devengo!",
  							 "No selecciono la Orden de Gasto!",
  							 "error"
					);
            		return;
            	}
            	ordenDevengoFactory.guardar($scope.objeto).then(function(resp){
        			 if (resp.estado){
      					 SweetAlert.swal(
      							 "Orden de Devengo!",
      							 "Registro guardado satisfactoriamente!",
      							 "success"
						 );
        				 if ($scope.nuevoar) {
        					 $scope.objeto = resp.json.ordendevengo;
	      					 $scope.noeditar=false;
	      					 $scope.nuevoar=false;
	      					 return;
        				 }
      				     //$scope.objeto=resp.json.ordendevengo;
      		             form.$setPristine(true);
      		             $scope.edicion=false;
      		             $scope.objeto={};
      		             //$scope.pageChanged();
        			 }else{
	 		             SweetAlert.swal(
	 		            		 "Orden de Devengo!",
	 		            		 resp.mensajes.msg,
	 		            		 "error"
	            		 );
        			 }
        		})
            }
        },
        reset: function (form) {
            $scope.myModel = angular.copy($scope.master);
            form.$setPristine(true);
            $scope.edicion=false;
			$scope.divVista=false;
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

	$scope.calcularIndex = function(index) {
		return (($scope.tableParams.page() - 1) * 5) + index;
	}
} ]);
