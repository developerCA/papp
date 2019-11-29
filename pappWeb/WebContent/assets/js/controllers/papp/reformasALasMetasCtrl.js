'use strict';

app.controller('ReformasALasMetasController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","reformasALasMetasFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, reformasALasMetasFactory) {

	$scope.rol=function(nombre) {
		return ifRollPermiso(nombre);
	}

	var index = 0;
	// Metas
	$scope.metasLista = false;
	$scope.metasLinea = false;
	$scope.metasSubtarea = false;
	$scope.metasDistribucion = false;

	// Reformas
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
	$scope.detalles={};
	
    $scope.pagina = 1;
    $scope.aplicafiltro=false;
	
	$scope.consultar=function(){
		$scope.data = [];
		reformasALasMetasFactory.traer(
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
		reformasALasMetasFactory.traerFiltro(
			$scope.pagina,
			$rootScope.ejefiscal,
			$scope.codigoFiltro,
			toStringDate($scope.fechainicialFiltro),
			toStringDate($scope.fechafinalFiltro),
			$scope.estadoFiltro
		).then(function(resp){
        	$scope.data = resp.result;
            $scope.total = resp.total.valor;
		})
	}

	$scope.limpiar=function(){
    	$scope.pagina=1;
		$scope.codigoFiltro=null;
		$scope.fechainicialFiltro=null;
		$scope.fechafinalFiltro=null;
		$scope.estadoFiltro=null;
		$scope.aplicafiltro=false;

		$scope.consultar();
	};
	
	$scope.nuevo=function(){
		reformasALasMetasFactory.traerNuevo(
			$rootScope.ejefiscal
		).then(function(resp){
			//console.log(resp);
			if (!resp.estado) return;
			$scope.objeto=resp.json.reformameta;
			$scope.detalles={};
			//$scope.agregarDetalles();
			$scope.edicion=true;
			$scope.nuevoar=true;
			$scope.guardar=true;
			$scope.noeditar=false;
			$scope.objeto.incluyemeta=$scope.objeto.incluyemeta==1;
		})
	}

	$scope.editar=function(index){
		index = $scope.calcularIndex(index);
		//console.log($scope.data[index]);
		if ($scope.rol('ROLE_APROBADOR')) {
			$scope.noeditar = true;
		} else if ($scope.rol('ROLE_CONSULTA')) {
			$scope.noeditar = true;
		} else {
			$scope.noeditar = ($scope.data[index].npestado == "Registrado"? false: true);
		}
		reformasALasMetasFactory.traerEditar($scope.data[index].id).then(function(resp){
			//console.log(resp.json);
			if (resp.estado) {
			    $scope.objeto=resp.json.reformameta;
				//$scope.objeto.incluyemeta=$scope.objeto.incluyemeta==1;
			    $scope.detalles=resp.json.reformametalineas;
			}
			$scope.edicion=true;
			$scope.nuevoar=false;
			$scope.guardar=true;
		})
	};

	$scope.solicitar=function(index) {
		index = $scope.calcularIndex(index);
		if ($scope.data[index].estado != "RE") {
			SweetAlert.swal(
					"Reformas Metas!",
					"Solo se puede solicitar si esta en estado registrar.",
					"error"
			);
			return;
		}
		reformasALasMetasFactory.solicitar(
			$scope.data[index].id,
			"SO",
			null,
			null
		).then(function(resp){
			if (resp.estado) {
				$scope.data[index].estado = "SO";
				$scope.data[index].npestado = "Solicitando";
			}
			SweetAlert.swal(
					"Reformas Metas!",
					resp.mensajes.msg,
					resp.mensajes.type
			);
		});
	}

	$scope.aprobar = function(index) {
		index = $scope.calcularIndex(index);
		if ($scope.data[index].estado != "SO") {
			SweetAlert.swal(
					"Reformas Metas!",
					"Solo se puede aprobar si esta en estado solicitado.",
					"error"
			);
			return;
		}
		SweetAlert.swal({
			title: "Reformas Metas?",
			text: "Seguro de aprobar la reforma..?",
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
				reformasALasMetasFactory.solicitar(
					$scope.data[index].id,
					"AP",
					null,
					null
				).then(function(resp){
					if (resp.estado) {
						$scope.data[index].estado = "AP";
						$scope.data[index].npestado = "Aprobando";
					}
					SweetAlert.swal(
							"Reformas Meta!",
							resp.mensajes.msg,
							resp.mensajes.type
					);
				});
			}
		});
	}

	$scope.negar = function(index) {
		index = $scope.calcularIndex(index);
		if ($scope.data[index].estado != "SO") {
			SweetAlert.swal(
					"Reformas Meta!",
					"Solo se puede negar si esta en estado solicitado.",
					"error"
			);
			return;
		}
		SweetAlert.swal({
			title: "Reformas Meta?",
			text: "Seguro de negar la reforma..?",
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
				$scope.data[index].estado = "NE";
				$scope.data[index].npestado = "Negando";
				reformasALasMetasFactory.solicitar(
					$scope.data[index].id,
					"NE",
					null,
					null
				).then(function(resp){
					SweetAlert.swal(
							"Reformas Meta!",
							resp.mensajes.msg,
							resp.mensajes.type
					);
				});
			}
		});
	}

	$scope.eliminar = function(index) {
		index = $scope.calcularIndex(index);
		if ($scope.data[index].estado != "RE") {
			SweetAlert.swal(
					"Reformas Meta!",
					"No se permite eliminar este registro, solo los que estan 'Registrados'.",
					"error"
			);
			return;
		}
		SweetAlert.swal({
			title: "Reformas Meta?",
			text: "Seguro que desea eliminar la reforma..?",
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
				$scope.data[index].estado = "EL";
				$scope.data[index].npestado = "Eliminando";
				reformasALasMetasFactory.solicitar(
					$scope.data[index].id,
					"EL",
					null,
					null
				).then(function(resp){
					SweetAlert.swal(
							"Reformas Meta!",
							resp.mensajes.msg,
							resp.mensajes.type
					);
				});
			}
		});
	}

	$scope.agregarDetalles=function(){
		$scope.detalles={id: null};
	};

	$scope.agregarLinea = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalReformasALasMetasLineas.html',
			controller : 'ModalReformasALasMetasLineasController',
			size : 'lg',
			resolve : {
				ID : function() {
					return $scope.objeto.id;
				},
				unidadID : function() {
					return $scope.objeto.metaunidadid;
				},
				unidadcodigo : function() {
					return $scope.objeto.npunidadcodigo;
				},
				unidadnombre : function() {
					return $scope.objeto.npunidadnombre;
				},
				editar : function() {
					return null;
				}
			}
		});
		modalInstance.result.then(function(obj) {
		    $scope.detalles = obj.reformametalineas;
		    $scope.objeto.valorincremento = obj.valorincremento;
		    $scope.objeto.valordecremento = obj.valordecremento;
		    $scope.nuevoar = true;
		    $scope.form.submit(Form);
            SweetAlert.swal(
            		"Reformas! - Lineas",
            		"Registro guardado satisfactoriamente!",
            		"success"
    		);
		}, function() {
		});
	};

	$scope.editarLinea = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalReformasALasMetasLineas.html',
			controller : 'ModalReformasALasMetasLineasController',
			size : 'lg',
			resolve : {
				ID : function() {
					return $scope.objeto.id;
				},
				unidadID : function() {
					return $scope.objeto.metaunidadid;
				},
				unidadcodigo : function() {
					return $scope.objeto.npunidadcodigo;
				},
				unidadnombre : function() {
					return $scope.objeto.npunidadnombre;
				},
				editar : function() {
					return $scope.detalles[index].id
				}
			}
		});
		modalInstance.result.then(function(obj) {
		    $scope.detalles = obj.reformametalineas;
		    //$scope.objeto.valortotal = obj.valortotal;
		    $scope.nuevoar = true;
		    $scope.form.submit(Form);
            SweetAlert.swal(
            		"Reformas Meta! - Lineas",
            		"Registro guardado satisfactoriamente!",
            		"success"
    		);
		}, function() {
		});
	};

	$scope.eliminarLinea = function(index) {
		$scope.index = index;
		SweetAlert.swal({
			title: "Reformas?",
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
			reformasALasMetasFactory.eliminarLinea(
				$scope.detalles[index].id.id,
				$scope.detalles[index].id.lineaid
			).then(function(resp){
				if (resp.estado){
					SweetAlert.swal(
							"Reformas!",
							"Eliminado satisfactoriamente!",
							"success"
					);
					$scope.objeto.valortotal -= $scope.detalles[index].npvalor;
					$scope.objeto.valorincremento -= $scope.detalles[index].valorincremento;
					$scope.objeto.valordecremento -= $scope.detalles[index].valordecremento;
				    $scope.detalles.splice(index, 1);
					$scope.detallesDP.splice($scope.index, 1);
				    //$scope.form.submit(Form);
	   			}else{
		            SweetAlert.swal(
		            		"Reformas!",
		            		resp.mensajes.msg,
		            		"error"
            		);
	   			}
          	})
		});
	};

	$scope.vMetaActual = 0;
	$scope.vReprogramada = 0;
	$scope.editarLineaMeta = function(index) {
		//$scope.detalles[index]
		var tObjLinea = Object.assign({}, $scope.detalles[index]);
		tObjLinea.npfechacreacion = $scope.objeto.npfechacreacion;
		reformasALasMetasFactory.editarLineaMeta(
				$rootScope.ejefiscalobj.anio,
				tObjLinea
		).then(function(resp){
			//console.log(resp.json);
			if (!resp.estado) {
	            SweetAlert.swal(
	            		"Reformas Meta! - Distribucion de Meta - Editar Presupuesto",
	            		resp.mensajes.msg,
	            		"error"
	    		);
				return;
			}
		    $scope.objetoP=resp.json;
			$scope.detallesP=resp.json.cronogramalinea;
			$scope.vReprogramada = $scope.objetoP.reformalinea.valorincremento - $scope.objetoP.reformalinea.valordecremento;
			$scope.vMetaActual = $scope.objetoP.reformalinea.npvalorinicial + $scope.vReprogramada;

			$scope.metasDistribucionLinea=true;
			$scope.edicion=false;
			$scope.guardar=true;
		})
	};

	$scope.editarLineaDistMeta = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalReformasLineasDistMeta.html',
			controller : 'ModalReformasLineasDistMetaController',
			size : 'lg',
			resolve : {
				ID : function() {
					return $scope.objetoM.id;
				},
				editar : function() {
					return $scope.detallesM[index].id
				}
			}
		});
		modalInstance.result.then(function(obj) {
		    $scope.detallesM = obj.reformametalineas;
		    //$scope.objeto.valortotal = obj.valortotal;
		    $scope.nuevoar = true;
		    $scope.form.submit(Form);
            SweetAlert.swal(
            		"Reformas! - Lineas",
            		"Registro guardado satisfactoriamente!",
            		"success"
    		);
		}, function() {
		});
	};

	$scope.editarLineaDistMetaSubtareaMeta = function(index) {
		var tObjLinea = Object.assign({}, $scope.detallesM[index]);
		//tObjLinea.npfechacreacion = $scope.objeto.npfechacreacion;
		reformasALasMetasFactory.editarLineaMetaSubtareaMeta(
				$rootScope.ejefiscalobj.anio,
				tObjLinea
		).then(function(resp){
			//console.log(resp.json);
			if (!resp.estado) {
	            SweetAlert.swal(
	            		"Reformas! - Distribucion de Meta - Editar Presupuesto",
	            		resp.mensajes.msg,
	            		"error"
	    		);
				return;
			}
		    $scope.objetoP=resp.json;
			$scope.detallesP=resp.json.cronogramalinea;

			$scope.metasDistribucionSubtareaMeta=true;
			$scope.metasDistribucion=false;
			$scope.guardar=true;
		})
	};

//	$scope.eliminarLinea = function(index) {
//		SweetAlert.swal({
//			title: "Reformas?",
//			text: "Seguro que desea eliminar esta linea?",
//			type: "warning",
//			showCancelButton: true,
//			confirmButtonClass: "btn-danger",
//			confirmButtonText: "SI!",
//			cancelButtonText: "NO",
//			closeOnConfirm: false,
//			closeOnCancel: true
//		},
//		function(isConfirm) {
//			if (!isConfirm) return;
//			reformasALasMetasFactory.eliminarLinea(
//				$scope.detalles[index].id.id,
//				$scope.detalles[index].id.lineaid
//			).then(function(resp){
//				if (resp.estado){
//					SweetAlert.swal(
//							"Reformas!",
//							"Eliminado satisfactoriamente!",
//							"success"
//					);
//					$scope.objeto.valortotal -= $scope.detalles[index].npvalor;
//				    $scope.detalles.splice(index, 1);
//				    $scope.form.submit(Form);
//	   			}else{
//		            SweetAlert.swal(
//		            		"Reformas!",
//		            		resp.mensajes.msg,
//		            		"error"
//            		);
//	   			}
//          	})
//		});
//	};

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
			$scope.objeto.metaunidadid = obj.id;
			$scope.objeto.npunidadcodigo = obj.codigopresup;
			$scope.objeto.npunidadnombre = obj.nombre;
		}, function() {
		});
	};

	$scope.abrirItem = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalItems.html',
			controller : 'ModalItemController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			$scope.objeto.reformaitemid = obj.id;
			$scope.objeto.npitemcodigo = obj.codigo;			
			$scope.objeto.npitemnombre = obj.nombre;			
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
                var tObj = Object.assign({}, $scope.objeto);
                tObj.incluyemeta = (tObj.incluyemeta? 1: 0);
            	reformasALasMetasFactory.guardar(tObj).then(function(resp){
        			 if (resp.estado){
        				 if ($scope.nuevoar) {
	      					 $scope.noeditar = false;
	      					 $scope.nuevoar=false;
	      				     $scope.objeto=resp.json.reformameta;
        				 } else {
          		             //form.$setPristine(true);
          		             $scope.edicion=false;
          		             $scope.objeto={};
        				 }
        				 //$scope.pageChanged();
      					 SweetAlert.swal(
      							 "Reformas Meta!",
      							 "Registro guardado satisfactoriamente!",
      							 "success"
						 );
        			 }else{
	 		             SweetAlert.swal(
	 		            		 "Reformas Meta!",
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
            $scope.objeto={};
            //$scope.pageChanged();
        }
    };

	$scope.formMetasDistribucionLinea = {
        submit: function (formMetasDistribucionLinea) {
            var firstError = null;
            if (formMetasDistribucionLinea.$invalid) {
                var field = null, firstError = null;
                for (field in formMetasDistribucionLinea) {
                    if (field[0] != '$') {
                        if (firstError === null && !formMetasDistribucionLinea[field].$valid) {
                            firstError = formMetasDistribucionLinea[field].$name;
                        }
                        if (formMetasDistribucionLinea[field].$pristine) {
                        	formMetasDistribucionLinea[field].$dirty = true;
                        }
                    }
                }
                angular.element('.ng-invalid[name=' + firstError + ']').focus();
                return;
            } else {
            	var diferencia = $scope.vMetaActual;
            	for (var i = 0; i < $scope.detallesP.length; i++) {
            		diferencia -= $scope.detallesP[i].npejecutado.toFixed(2);
            		diferencia = +diferencia.toFixed(2)
				}
            	for (var i = 0; i < $scope.detallesP.length; i++) {
            		diferencia -= $scope.detallesP[i].metacantidad.toFixed(2);
            		diferencia = +diferencia.toFixed(2)
				}
            	if (diferencia != 0) {
                    SweetAlert.swal(
                		"Reformas a las Metas",
                		"Tiene una diferencia de: " + diferencia + ", con la Meta Actual",
                		"error"
            		);
        			return;
            	}
                var tObj = Object.assign({}, $scope.objetoP.cronograma);
                var tDet = Object.assign([], $scope.detallesP);
                tObj.cronogramalineaTOs = tDet;
            	reformasALasMetasFactory.guardarLineaMeta(tObj).then(function(resp){
        			 if (!resp.estado){
 	 		             SweetAlert.swal(
	 		            		 "Reformas Meta!",
	 		            		 resp.mensajes.msg,
	 		            		 "error"
	            		 );
 	 		             return;
        			 }
					 $scope.metasDistribucionLinea = false;
					 $scope.edicion = true;
					 //$scope.objetoP = {};
  					 SweetAlert.swal(
  							 "Reformas Meta!",
  							 "Registro guardado satisfactoriamente!",
  							 "success"
					 );
        		})
            }
        },
        reset: function (formMetasDistribucionLinea) {
            $scope.myModel = angular.copy($scope.master);
            formMetasDistribucionLinea.$setPristine(true);
			$scope.metasDistribucionLinea = false;
			$scope.edicion = true;
            $scope.objetoP = {};
            $scope.detallesP = {};
        }
    };

	$scope.formMetasDistribucionReset = function() {
	    delete $scope.objetoM;
	    delete $scope.detallesM;
        $scope.metasDistribucion = false;
		$scope.edicion = true;
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
	$scope.popupnpFechafin = {
	    opened: false
	};
	$scope.opennpFechafin = function() {
	    $scope.popupnpFechafin.opened = true;
	}

	$scope.calcularIndex = function(index) {
		return (($scope.tableParams.page() - 1) * 5) + index;
	}

//	$scope.metaEditar=function(){
//        var tObj = Object.assign({}, $scope.objeto);
//        delete tObj.incluyemeta;
//        var tDet = Object.assign([], $scope.detalles);
//        tObj.details = tDet;
//		reformasALasMetasFactory.traerEditarMeta(
//				tObj
//		).then(function(resp){
//			if (!resp.estado){
//	             SweetAlert.swal(
//	            		 "Reformas Meta!",
//	            		 resp.mensajes.msg,
//	            		 "error"
//        		 );
//	             return;
//			}
//		    $scope.objetoM=resp.json.reforma;
//		    $scope.detallesM=resp.json.reformametasubtarea;
//			$scope.edicion = false;
//			$scope.metasDistribucion = true;
//		})
//	};

	$scope.metaActualizar = function(index) {
//		var modalInstance = $uibModal.open({
//			templateUrl : 'assets/views/papp/modal/modalReformasLineas.html',
//			controller : 'ModalReformasLineasController',
//			size : 'lg',
//			resolve : {
//				ID : function() {
//					return $scope.objeto.id;
//				},
//				unidadID : function() {
//					return $scope.objeto.metaunidadid;
//				},
//				editar : function() {
//					return $scope.detalles[index].id
//				}
//			}
//		});
//		modalInstance.result.then(function(obj) {
//		    $scope.detalles = obj.lineas;
//		    //$scope.objeto.valortotal = obj.valortotal;
//		    $scope.form.submit(Form);
//            SweetAlert.swal("Reformas! - Lineas", "Registro guardado satisfactoriamente!", "success");
//		}, function() {
//		});
	};

	$scope.metaDistribucion = function(index) {
		
	}

	$scope.metaEliminar = function(index) {
//		SweetAlert.swal({
//			title: "Reformas?",
//			text: "Seguro que desea eliminar esta linea?",
//			type: "warning",
//			showCancelButton: true,
//			confirmButtonClass: "btn-danger",
//			confirmButtonText: "SI!",
//			cancelButtonText: "NO",
//			closeOnConfirm: false,
//			closeOnCancel: true
//		},
//		function(isConfirm) {
//			if (!isConfirm) return;
//			reformasALasMetasFactory.eliminarLinea(
//				$scope.detalles[index].id.id,
//				$scope.detalles[index].id.lineaid
//			).then(function(resp){
//				if (resp.estado){
//					SweetAlert.swal("Reformas!", "Eliminado satisfactoriamente!", "success");
//					$scope.objeto.valortotal -= $scope.detalles[index].npvalor;
//				    $scope.detalles.splice(index, 1);
//				    $scope.form.submit(Form);
//	   			}else{
//		            SweetAlert.swal("Reformas!", resp.mensajes.msg, "error");
//	   			}
//          	})
//		});
	};
} ]);
