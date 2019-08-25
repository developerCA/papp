'use strict';

app.controller('ReformasController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","reformasFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, reformasFactory) {

	$scope.rol=function(nombre) {
		return ifRollPermiso(nombre);
	}

	var index = 0;
	var noSalir = false;
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
	$scope.detallesDP=[];
	$scope.detallesDPmeta=[];
	
    $scope.pagina = 1;
    $scope.aplicafiltro=false;
	
	$scope.consultar=function(){
		$scope.data = [];
		reformasFactory.traer(
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
		reformasFactory.traerFiltro(
			$scope.pagina,
			$rootScope.ejefiscal,
			$scope.codigoFiltro,
			$scope.tipoFiltro,
			toStringDate($scope.fechainicialFiltro),
			toStringDate($scope.fechafinalFiltro),
			$scope.estadoFiltro,
			null
		).then(function(resp){
        	$scope.data = resp.result;
            $scope.total = resp.total.valor;
		})
	}

	$scope.limpiar=function(){
    	$scope.pagina=1;
		$scope.codigoFiltro=null;
		$scope.tipoFiltro=null;
		$scope.fechainicialFiltro=null;
		$scope.fechafinalFiltro=null;
		$scope.estadoFiltro=null;
		$scope.aplicafiltro=false;

		$scope.consultar();
	};
	
	$scope.nuevo=function(){
		reformasFactory.traerNuevo(
			$rootScope.ejefiscal
		).then(function(resp){
			//console.log(resp);
			if (!resp.estado) return;
			$scope.objeto=resp.json.reforma;
			$scope.detalles={};
			$scope.detallesDP=[];
			//$scope.agregarDetalles();
			$scope.edicion=true;
			$scope.nuevoar=true;
			$scope.guardar=true;
			$scope.noeditar=false;
			$scope.objeto.incluyemeta=$scope.objeto.incluyemeta==1;
		})
	}

	$scope.editar = function(index){
		index = $scope.calcularIndex(index);
		//console.log($scope.data[index]);
		if ($scope.rol('ROLE_APROBADOR')) {
			$scope.noeditar = true;
		} else {
			$scope.noeditar = ($scope.data[index].npestado == "Registrado"? false: true);
		}
		reformasFactory.traerEditar($scope.data[index].id).then(function(resp){
			//console.log(resp.json);
			if (resp.estado) {
			    $scope.objeto=resp.json.reforma;
				$scope.objeto.incluyemeta=$scope.objeto.incluyemeta==1;
			    $scope.detalles=resp.json.reformalineas;
			    $scope.detallesDP=[];
			    for (var i = 0; i < $scope.detalles.length; i++) {
			    	$scope.detallesDP.push(true);
				}
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
					"Reformas!",
					"Solo se puede solicitar si esta en estado registrar.",
					"error"
			);
			return;
		}
		SweetAlert.swal({
			title: "Reformas!",
			text: "Esta seguro que desea solicitar?",
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
			reformasFactory.solicitar(
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
						"Reformas!",
						resp.mensajes.msg,
						resp.mensajes.type
				);
			});
		});
	}

	$scope.aprobar = function(index) {
		index = $scope.calcularIndex(index);
		if ($scope.data[index].estado != "SO") {
			SweetAlert.swal(
					"Reformas!",
					"Solo se puede aprobar si esta en estado solicitado.",
					"error"
			);
			return;
		}
		SweetAlert.swal({
			title: "Reformas?",
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
				reformasFactory.solicitar(
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
							"Reformas!",
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
					"Reformas!",
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
			if (obj === undefined) {
				obj = "";
			}
			var cur = 0;
			reformasFactory.solicitar(
				$scope.data[index].id,
				"NE",
				null,
				obj
			).then(function(resp){
				$scope.data[index].estado = "NE";
				$scope.data[index].npestado = "Negando";
				reformasFactory.solicitar(
					$scope.data[index].id,
					"NE",
					null,
					null
				).then(function(resp){
					SweetAlert.swal(
							"Reformas!",
							resp.mensajes.msg,
							resp.mensajes.type
					);
					$scope.filtrar();
				});
			});
		}, function() {
		});
/*		SweetAlert.swal({
			title: "Reformas?",
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
				reformasFactory.solicitar(
					$scope.data[index].id,
					"NE",
					null,
					null
				).then(function(resp){
					SweetAlert.swal(
							"Reformas!",
							resp.mensajes.msg,
							resp.mensajes.type
					);
				});
			}
		}); */
	}

	$scope.eliminar = function(index) {
		index = $scope.calcularIndex(index);
		if ($scope.data[index].estado != "RE") {
			SweetAlert.swal(
					"Reformas!",
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
			reformasFactory.solicitar(
				$scope.data[index].id,
				"EL",
				null,
				obj
			).then(function(resp){
				$scope.data[index].estado = "EL";
				$scope.data[index].npestado = "Eliminando";
				reformasFactory.solicitar(
					$scope.data[index].id,
					"EL",
					null,
					null
				).then(function(resp){
					SweetAlert.swal(
							"Reformas!",
							resp.mensajes.msg,
							resp.mensajes.type
					);
					$scope.filtrar();
				});
			});
		}, function() {
		});
/*		SweetAlert.swal({
			title: "Reformas?",
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
				reformasFactory.solicitar(
					$scope.data[index].id,
					"EL",
					null,
					null
				).then(function(resp){
					SweetAlert.swal(
							"Reformas!",
							resp.mensajes.msg,
							resp.mensajes.type
					);
				});
			}
		});*/
	}

//	$scope.agregarDetalles=function(){
//		$scope.detalles={id: null};
//	};

	$scope.agregarLinea = function() {
		if ($scope.objeto.tipo == 'ES') {
			$scope.agregarLineaSubItem();
			return;
		}
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalReformasLineas1.html',
			controller : 'ModalReformasLineasController1',
			size : 'lg',
			resolve : {
				ID : function() {
					return $scope.objeto.id;
				},
				unidadID : function() {
					return $scope.objeto.reformaunidadid;
				},
				unidadcodigo : function() {
					return $scope.objeto.npunidadcodigo;
				},
				unidadnombre : function() {
					return $scope.objeto.npunidadnombre;
				},
				editar : function() {
					return null;
				},
				noeditar : function() {
					return $scope.noeditar;
				}
			}
		});
		modalInstance.result.then(function(obj) {
		    $scope.detalles = obj.reformalineas;
		    $scope.detallesDP.push(false);
			$scope.objeto.valorincremento = 0;
			$scope.objeto.valordecremento = 0;
		    for (var i = 0; i < $scope.detalles.length; i++) {
				$scope.objeto.valorincremento += $scope.detalles[i].valorincremento;
				$scope.objeto.valordecremento += $scope.detalles[i].valordecremento;
			}
		    noSalir = true;
		    $scope.form.submit(Form);
            SweetAlert.swal(
            		"Reformas! - Lineas",
            		"Registro guardado satisfactoriamente!",
            		"success"
    		);
		}, function() {
		});
	};

	$scope.agregarLineaSubItem = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalReformasLineasSubItem.html',
			controller : 'ModalReformasLineasSubItemController',
			size : 'lg',
			resolve : {
				ID : function() {
					return $scope.objeto.id;
				},
				unidadID : function() {
					return $scope.objeto.reformaunidadid;
				},
				unidadcodigo : function() {
					return $scope.objeto.npunidadcodigo;
				},
				unidadnombre : function() {
					return $scope.objeto.npunidadnombre;
				},
				editar : function() {
					return null;
				},
				noeditar : function() {
					return $scope.noeditar;
				},
				itemId : function() {
					return $scope.objeto.nivelactividadid;
				},
				subItemId : function() {
					return null;
				},
				subItemTablarelacionid : function() {
					return null;
				}
			}
		});
		modalInstance.result.then(function(obj) {
		    $scope.detalles = obj.reformalineas;
		    $scope.detallesDP.push(false);
			$scope.objeto.valorincremento = 0;
			$scope.objeto.valordecremento = 0;
		    for (var i = 0; i < $scope.detalles.length; i++) {
				$scope.objeto.valorincremento += $scope.detalles[i].valorincremento;
				$scope.objeto.valordecremento += $scope.detalles[i].valordecremento;
			}
		    noSalir = true;
		    $scope.form.submit(Form);
            SweetAlert.swal(
            		"Reformas! - Lineas",
            		"Registro guardado satisfactoriamente!",
            		"success"
    		);
//		    $scope.detallesM = obj.reformalineas;
//		    $scope.detallesDP.push(false);
//			$scope.objeto.valorincremento = 0;
//			$scope.objeto.valordecremento = 0;
//		    for (var i = 0; i < $scope.detallesM.length; i++) {
//				$scope.objeto.valorincremento += $scope.detallesM[i].valorincremento;
//				$scope.objeto.valordecremento += $scope.detallesM[i].valordecremento;
//			}
//		    noSalir = true;
//		    $scope.form.submit(Form);
//            SweetAlert.swal(
//            		"Reformas! - Lineas",
//            		"Registro guardado satisfactoriamente!",
//            		"success"
//    		);
		}, function() {
		});
	};

	var indexLinea = 0;
	$scope.editarLinea = function(index) {
		indexLinea = index;
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalReformasLineas.html',
			controller : 'ModalReformasLineasController1',
			size : 'lg',
			resolve : {
				ID : function() {
					return $scope.objeto.id;
				},
				unidadID : function() {
					return $scope.objeto.reformaunidadid;
				},
				unidadcodigo : function() {
					return $scope.objeto.npunidadcodigo;
				},
				unidadnombre : function() {
					return $scope.objeto.npunidadnombre;
				},
				editar : function() {
					return $scope.detalles[index].id
				},
				noeditar : function() {
					return $scope.noeditar;
				}
			}
		});
		modalInstance.result.then(function(obj) {
		    $scope.detalles = obj.reformalineas;
		    //$scope.objeto.valortotal = obj.valortotal;
			$scope.objeto.valorincremento = 0;
			$scope.objeto.valordecremento = 0;
		    for (var i = 0; i < $scope.detalles.length; i++) {
				$scope.objeto.valorincremento += $scope.detalles[i].valorincremento;
				$scope.objeto.valordecremento += $scope.detalles[i].valordecremento;
			}
		    noSalir = true;
		    $scope.form.submit(Form);
            SweetAlert.swal(
            		"Reformas! - Lineas",
            		"Registro guardado satisfactoriamente!",
            		"success"
    		);
            $scope.detallesDP[indexLinea] = false;
		}, function() {
		});
	};

	$scope.index = null;
	$scope.editarLineaMeta = function(index) {
		//$scope.detalles[index]
		$scope.index = index;
		var tObjLinea = Object.assign({}, $scope.detalles[index]);
		tObjLinea.npfechacreacion = $scope.objeto.npfechacreacion;
		reformasFactory.editarLineaMeta(
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

			$scope.metasDistribucionLinea=true;
			$scope.edicion=false;
			$scope.guardar=true;
		})
	};

	var mIncremento = 0;
	var mDecremento = 0;
	var mIndex = 0;
	$scope.editarLineaDistMeta = function(index) {
		mIndex = index;
		mIncremento = $scope.detallesM[index].valorincremento;
		mDecremento = $scope.detallesM[index].valordecremento;
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
				},
				noeditar : function() {
					return $scope.noeditar
				}
			}
		});
		modalInstance.result.then(function(obj) {
		    $scope.detallesM = obj.reformametasubtarea;
		    if (mIncremento != $scope.detallesM[mIndex].valorincremento
		    		|| mDecremento != $scope.detallesM[mIndex].valordecremento) {
		    	$scope.detallesDPmeta[mIndex] = false;
		    }
            SweetAlert.swal(
            		"Reformas! - Lineas",
            		"Registro guardado satisfactoriamente!",
            		"success"
    		);
		}, function() {
		});
	};

	$scope.editarLineaDistMetaSubtareaMeta = function(index) {
		mIndex = index;
		var tObjLinea = Object.assign({}, $scope.detallesM[index]);
		//tObjLinea.npfechacreacion = $scope.objeto.npfechacreacion;
		reformasFactory.editarLineaMetaSubtareaMeta(
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

			if (typeof($scope.objetoP.reformalinea.codificado) != 'number') {
				$scope.objetoP.reformalinea.codificado = 0;
			}
			if (typeof($scope.objetoP.reformalinea.valordecremento) != 'number') {
				$scope.objetoP.reformalinea.valordecremento = 0;
			}
			if (typeof($scope.objetoP.reformalinea.valorincremento) != 'number') {
				$scope.objetoP.reformalinea.valorincremento = 0;
			}
			var i = $scope.objetoP.reformalinea.codificado - $scope.objetoP.reformalinea.valordecremento + $scope.objetoP.reformalinea.valorincremento;
			$scope.objetoP.reformalinea.metaReprogramada = i;
			$scope.metasDistribucionSubtareaMeta=true;
			$scope.metasDistribucion=false;
			$scope.guardar=true;
		})
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
			reformasFactory.eliminarLinea(
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
			$scope.objeto.reformaunidadid = obj.id;
			npacitividadunidad = obj.npacitividadunidad;
			$scope.objeto.npunidadcodigo = obj.codigopresup;
			$scope.objeto.npunidadnombre = obj.nombre;
		}, function() {
		});
	};
	var npacitividadunidad = 0;

	$scope.abrirItem = function() {
		if ($scope.objeto.reformaunidadid == 0) {
			SweetAlert.swal(
        		"Reformas",
        		"Tiene que seleccionar primero una unidad",
        		"error"
    		);
			return;
		}
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalReformasLineasItem.html',
			controller : 'ModalReformasLineasItemController',
			size : 'lg',
			resolve : {
				ID : function() {
					return $scope.objeto.id;
				},
				unidadID : function() {
					return $scope.objeto.reformaunidadid;
				},
				unidadcodigo : function() {
					return $scope.objeto.npunidadcodigo;
				},
				unidadnombre : function() {
					return $scope.objeto.npunidadnombre;
				},
				editar : function() {
					return null; //$scope.detalles[index].id
				},
				noeditar : function() {
					return $scope.noeditar;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.reformaitemid = obj.id;
			$scope.objeto.nivelactividadid = obj.id;
			$scope.objeto.npitemcodigo = obj.npcodigo + ' - ' + obj.npcodigocanton + ' - ' + obj.npcodigofuente;			
			$scope.objeto.npitemnombre = obj.npdescripcion;
//		    $scope.detalles = obj.reformalineas;
//		    //$scope.objeto.valortotal = obj.valortotal;
//			$scope.objeto.valorincremento = 0;
//			$scope.objeto.valordecremento = 0;
//		    for (var i = 0; i < $scope.detalles.length; i++) {
//				$scope.objeto.valorincremento += $scope.detalles[i].valorincremento;
//				$scope.objeto.valordecremento += $scope.detalles[i].valordecremento;
//			}
//		    noSalir = true;
//		    $scope.form.submit(Form);
//            SweetAlert.swal(
//            		"Reformas! - Lineas",
//            		"Registro guardado satisfactoriamente!",
//            		"success"
//    		);
//            $scope.detallesDP[indexLinea] = false;
		}, function() {
		});
//		var modalInstance = $uibModal.open({
//			templateUrl : 'assets/views/papp/modal/modalItemsNivelActividad.html',
//			controller : 'ModalItemNivelActividadController',
//			size : 'lg',
//			resolve : {
//				nivelactividadunidadid : function() {
//					return $scope.objeto.reformaunidadid; // npacitividadunidad;
//				}
//			}
//		});
//		modalInstance.result.then(function(obj) {
//			$scope.objeto.reformaitemid = obj.id;
//			$scope.objeto.nivelactividadid = obj.id;
//			$scope.objeto.npitemcodigo = obj.npcodigo + ' - ' + obj.npcodigocanton + ' - ' + obj.npcodigofuente;			
//			$scope.objeto.npitemnombre = obj.npdescripcion;
//		}, function() {
//		});
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
            	if ($scope.objeto.tipo == "MU" || $scope.objeto.tipo == "ES") {
            		if ($scope.objeto.valorincremento != $scope.objeto.valordecremento) {
	                    SweetAlert.swal(
	                		"Reformas",
	                		"El valor incrementado es diferente del valor decrementado",
	                		"error"
	            		);
	        			return;
            		}
            	}
            	if (!noSalir) {
            		var terminar = false;
            		var listaCodigos = '';
	            	for (var i = 0; i < $scope.detallesDP.length; i++) {
						if (!$scope.detallesDP[i]) {
							terminar = true;
							listaCodigos += '\n\r - ' + $scope.detalles[i].npSubitemcodigo + ': ' + $scope.detalles[i].npSubitem;
						}
					}
	            	if (terminar) {
	                    SweetAlert.swal(
	                		"Reformas",
	                		"Todas las lineas nuevas tienen que tener echa la Distribucion del Presupuesto." + listaCodigos,
	                		"error"
	            		);
	        			return;
	            	}
            	}
                var tObj = Object.assign({}, $scope.objeto);
                tObj.incluyemeta = (tObj.incluyemeta? 1: 0);
            	reformasFactory.guardar(tObj).then(function(resp){
        			 if (resp.estado){
        				 if (noSalir == true) {
        					 noSalir = false;
	      				     $scope.objeto = resp.json.reforma;
	      				     $scope.detalles = resp.json.reformalineas;
        					 return;
        				 }
        				 if ($scope.nuevoar) {
	      					 $scope.noeditar = false;
	      					 $scope.nuevoar = false;
	      				     $scope.objeto = resp.json.reforma;
        				 } else {
          		             $scope.edicion = false;
          		             $scope.objeto = {};
        				 }
        				 //$scope.pageChanged();
      					 SweetAlert.swal(
      							 "Reformas!",
      							 "Registro guardado satisfactoriamente!",
      							 "success"
						 );
        			 }else{
	 		             SweetAlert.swal(
	 		            		 "Reformas!",
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

	$scope.formMetasDistribucion = {
        submit: function (formMetasDistribucion) {
            var firstError = null;
            if (formMetasDistribucion.$invalid) {
                var field = null, firstError = null;
                for (field in formMetasDistribucion) {
                    if (field[0] != '$') {
                        if (firstError === null && !formMetasDistribucion[field].$valid) {
                            firstError = form[field].$name;
                        }
                        if (formMetasDistribucion[field].$pristine) {
                        	formMetasDistribucion[field].$dirty = true;
                        }
                    }
                }
                angular.element('.ng-invalid[name=' + firstError + ']').focus();
                return;
            } else {
            	if ($scope.objeto.tipo == "MU" || $scope.objeto.tipo == "ES") {
            		if ($scope.objeto.valorincremento != $scope.objeto.valordecremento) {
	                    SweetAlert.swal(
	                		"Reformas - Meta",
	                		"El valor incrementado es diferente del valor decrementado",
	                		"error"
	            		);
	        			return;
            		}
            	}
            	if (!noSalir) {
            		var terminar = false;
            		var listaCodigos = '';
	            	for (var i = 0; i < $scope.detallesDPmeta.length; i++) {
						if (!$scope.detallesDPmeta[i]) {
							terminar = true;
							listaCodigos += '\n\r - ' + $scope.detallesM[i].npSubtarea + ' (' + $scope.detallesM[i].npunidadmedida + ')';
						}
					}
	            	if (terminar) {
	                    SweetAlert.swal(
	                		"Reformas - Metas",
	                		"Todas las lineas nuevas tienen que tener echa la Distribucion del Presupuesto." + listaCodigos,
	                		"error"
	            		);
	        			return;
	            	}
            	}
            	$scope.formMetasDistribucionReset();
            }
        },
        reset: function (form) {
        	$scope.formMetasDistribucionReset();
        }
    };

	$scope.formMetasDistribucionReset = function() {
	    delete $scope.objetoM;
	    delete $scope.detallesM;
        $scope.metasDistribucion = false;
		$scope.edicion = true;
	}

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
            	var diferencia = $scope.objetoP.reformalinea.npSubitemvalor;
            	for (var i = 0; i < $scope.detallesP.length; i++) {
            		diferencia -= $scope.detallesP[i].valor;
				}
            	if (diferencia != 0) {
                    SweetAlert.swal(
                		"Reformas",
                		"Tiene una diferencia de: " + diferencia + ", con el Presupuesto",
                		"error"
            		);
        			return;
            	}
                var tObj = Object.assign({}, $scope.objetoP.cronograma);
                var tDet = Object.assign([], $scope.detallesP);
                tObj.cronogramalineaTOs = tDet;
            	reformasFactory.guardarLineaMeta(tObj).then(function(resp){
        			 if (!resp.estado){
 	 		             SweetAlert.swal(
	 		            		 "Reformas!",
	 		            		 resp.mensajes.msg,
	 		            		 "error"
	            		 );
 	 		             return;
        			 }
					 $scope.metasDistribucionLinea = false;
					 $scope.edicion = true;
					 $scope.detallesDP[$scope.index] = true;
					 //$scope.objetoP = {};
  					 SweetAlert.swal(
  							 "Reformas!",
  							 "Registro guardado satisfactoriamente!",
  							 "success"
					 );
        		})
            }
        },
        reset: function (formMetasDistribucionLinea) {
            $scope.myModel = angular.copy($scope.master);
            $scope.metasDistribucionLinea = false;
			$scope.edicion = true;
            $scope.objetoP = {};
        }
    };

	$scope.formMetasDistribucionSubtariaLinea = {
        submit: function (formMetasDistribucionSubtariaLinea) {
            var firstError = null;
            if (formMetasDistribucionSubtariaLinea.$invalid) {
                var field = null, firstError = null;
                for (field in formMetasDistribucionSubtariaLinea) {
                    if (field[0] != '$') {
                        if (firstError === null && !formMetasDistribucionSubtariaLinea[field].$valid) {
                            firstError = formMetasDistribucionSubtariaLinea[field].$name;
                        }
                        if (formMetasDistribucionSubtariaLinea[field].$pristine) {
                        	formMetasDistribucionSubtariaLinea[field].$dirty = true;
                        }
                    }
                }
                angular.element('.ng-invalid[name=' + firstError + ']').focus();
                return;
            } else {
            	var diferencia = $scope.objetoP.reformalinea.metaReprogramada;
            	for (var i = 0; i < $scope.detallesP.length; i++) {
            		diferencia -= $scope.detallesP[i].metacantidad;
				}
            	if (diferencia != 0) {
                    SweetAlert.swal(
                		"Reformas",
                		"Tiene una diferencia de: " + diferencia + ", con la Meta Reprogramada",
                		"error"
            		);
        			return;
            	}
                var tObj = Object.assign({}, $scope.objetoP.cronograma);
                var tDet = Object.assign([], $scope.detallesP);
                tObj.cronogramalineaTOs = tDet;
            	reformasFactory.guardarLineaMeta(tObj).then(function(resp){
        			 if (!resp.estado){
 	 		             SweetAlert.swal(
	 		            		 "Reformas!",
	 		            		 resp.mensajes.msg,
	 		            		 "error"
	            		 );
 	 		             return;
        			 }
        			 $scope.detallesDPmeta[mIndex] = true;
					 $scope.metasDistribucionSubtareaMeta = false;
					 $scope.metasDistribucion = true;
					 //$scope.objetoP = {};
  					 SweetAlert.swal(
  							 "Reformas!",
  							 "Registro guardado satisfactoriamente!",
  							 "success"
					 );
        		})
            }
        },
        reset: function (formMetasDistribucionSubtariaLinea) {
            $scope.myModel = angular.copy($scope.master);
            $scope.metasDistribucionSubtareaMeta = false;
			$scope.metasDistribucion = true;
            $scope.objetoP = {};
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

	$scope.metaEditar = function(){
        var tObj = Object.assign({}, $scope.objeto);
        delete tObj.incluyemeta;
        var tDet = Object.assign([], $scope.detalles);
        tObj.details = tDet;
		reformasFactory.traerEditarMeta(
				tObj
		).then(function(resp){
			if (!resp.estado){
	             SweetAlert.swal(
	            		 "Reformas Meta!",
	            		 resp.mensajes.msg,
	            		 "error"
        		 );
	             return;
			}
		    $scope.objetoM = resp.json.reforma;
		    $scope.detallesM = resp.json.reformametasubtarea;
		    $scope.detallesDPmeta = [];
		    for (var i = 0; i < $scope.detallesM.length; i++) {
		    	$scope.detallesDPmeta.push(
		    			$scope.detallesM[i].valorincremento == 0 && $scope.detallesM[i].valordecremento == 0
		    			? false
		    			: true
		    	);
			}
			$scope.edicion = false;
			$scope.metasDistribucion = true;
		})
	};

	$scope.metaActualizar = function(index) {
//		var modalInstance = $uibModal.open({
//			templateUrl : 'assets/views/papp/modal/modalReformasLineas.html',
//			controller : 'ModalReformasLineasController1',
//			size : 'lg',
//			resolve : {
//				ID : function() {
//					return $scope.objeto.id;
//				},
//				unidadID : function() {
//					return $scope.objeto.reformaunidadid;
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
//			reformasFactory.eliminarLinea(
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

	$scope.imprimirsolicitud=function(id){
    	var url = "/birt/frameset?__report=E03-R.rptdesign" +
		"&reforma=" + id;
	    window.open(url, '_blank');
		return;
	};

	$scope.imprimiroficio=function(id){
    	var url = "/birt/frameset?__report=E03-RO.rptdesign" +
		"&reforma=" + id;
	    window.open(url, '_blank');
		return;
	};
} ]);
