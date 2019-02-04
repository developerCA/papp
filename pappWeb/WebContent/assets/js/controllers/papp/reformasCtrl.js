'use strict';

app.controller('ReformasController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","reformasFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, reformasFactory) {

	$scope.rol=function(nombre) {
		return ifRollPermiso(nombre);
	}

	var index = 0;
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
			//$scope.agregarDetalles();
			$scope.edicion=true;
			$scope.nuevoar=true;
			$scope.guardar=true;
			$scope.noeditar=false;
		})
	}

	$scope.editar=function(index){
		index = $scope.calcularIndex(index);
		//console.log($scope.data[index]);
		$scope.noeditar = ($scope.data[index].npestado == "Registrado"? false: true);
		reformasFactory.traerEditar($scope.data[index].id).then(function(resp){
			//console.log(resp.json);
			if (resp.estado) {
			    $scope.objeto=resp.json.reforma;
			    $scope.detalles=resp.json.reformalineas;
			}
			$scope.edicion=true;
			$scope.nuevoar=false;
			$scope.guardar=true;
		})
	};

	$scope.solicitar=function(index) {
		index = $scope.calcularIndex(index);
		if ($scope.data[index].estado != "RE") {
			SweetAlert.swal("Reformas!", "Solo se puede solicitar si esta en estado registrar.", "error");
			return;
		}
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
			SweetAlert.swal("Reformas!", resp.mensajes.msg, resp.mensajes.type);
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
					SweetAlert.swal("Reformas!", resp.mensajes.msg, resp.mensajes.type);
				});
			}
		});
	}

	$scope.negar = function(index) {
		index = $scope.calcularIndex(index);
		if ($scope.data[index].estado != "SO") {
			SweetAlert.swal("Reformas!", "Solo se puede negar si esta en estado solicitado.", "error");
			return;
		}
		SweetAlert.swal({
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
					SweetAlert.swal("Reformas!", resp.mensajes.msg, resp.mensajes.type);
				});
			}
		});
	}

	$scope.eliminar = function(index) {
		index = $scope.calcularIndex(index);
		if ($scope.data[index].estado != "RE") {
			SweetAlert.swal(
					"Reformas!",
					"No se permite eliminar este articulo, solo los que estan 'Registrados'.",
					"error"
			);
			return;
		}
		SweetAlert.swal({
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
					SweetAlert.swal("Reformas!", resp.mensajes.msg, resp.mensajes.type);
				});
			}
		});
	}

	$scope.agregarDetalles=function(){
		$scope.detalles={id: null};
	};

	$scope.agregarLinea = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalReformasLineas.html',
			controller : 'ModalReformasLineasController',
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
				}
			}
		});
		modalInstance.result.then(function(obj) {
		    $scope.detalles = obj.lineas;
		    $scope.objeto.valorincremento = obj.valorincremento;
		    $scope.objeto.valordecremento = obj.valordecremento;
		    $scope.form.submit(Form);
            SweetAlert.swal("Reformas! - Lineas", "Registro guardado satisfactoriamente!", "success");
		}, function() {
		});
	};

	$scope.editarLinea = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalReformasLineas.html',
			controller : 'ModalReformasLineasController',
			size : 'lg',
			resolve : {
				ID : function() {
					return $scope.objeto.id;
				},
				unidadID : function() {
					return $scope.objeto.reformaunidadid;
				},
				editar : function() {
					return $scope.detalles[index].id
				}
			}
		});
		modalInstance.result.then(function(obj) {
		    $scope.detalles = obj.lineas;
		    //$scope.objeto.valortotal = obj.valortotal;
		    $scope.form.submit(Form);
            SweetAlert.swal("Reformas! - Lineas", "Registro guardado satisfactoriamente!", "success");
		}, function() {
		});
	};

	$scope.eliminarLinea = function(index) {
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
					SweetAlert.swal("Reformas!", "Eliminado satisfactoriamente!", "success");
					$scope.objeto.valortotal -= $scope.detalles[index].npvalor;
				    $scope.detalles.splice(index, 1);
				    $scope.form.submit(Form);
	   			}else{
		            SweetAlert.swal("Reformas!", resp.mensajes.msg, "error");
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
            	reformasFactory.guardar(tObj).then(function(resp){
        			 if (resp.estado){
        				 if ($scope.nuevoar) {
	      					 $scope.noeditar = false;
	      					 $scope.nuevoar=false;
	      				     $scope.objeto=resp.json.reforma;
        				 } else {
          		             form.$setPristine(true);
          		             $scope.edicion=false;
          		             $scope.objeto={};
        				 }
        				 //$scope.pageChanged();
      					 SweetAlert.swal("Reformas!", "Registro guardado satisfactoriamente!", "success");
        			 }else{
	 		             SweetAlert.swal("Reformas!", resp.mensajes.msg, "error");
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
