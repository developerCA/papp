'use strict';

app.controller('CertificacionesFondosController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","certificacionesFondosFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, certificacionesFondosFactory) {

	$scope.rol=function(nombre) {
		return ifRollPermiso(nombre);
	}

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
		$scope.data=[];
		certificacionesFondosFactory.traer(
			$scope.pagina,
			$rootScope.ejefiscal
		).then(function(resp){
			//if (resp.meta)
			$scope.data = resp.result;
//			console.log($scope.data);
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
        	$scope.data = resp.result;
            $scope.total = resp.total.valor;
		})
	}

	$scope.limpiar=function(){
		$scope.codigoFiltro=null;
		$scope.precompromisoFiltro=null;
		$scope.valorinicialFiltro=null;
		$scope.valorfinalFiltro=null;
		$scope.fechainicialFiltro=null;
		$scope.fechafinalFiltro=null;
		$scope.estadoFiltro=null;
		$scope.aplicafiltro=false;

		$scope.consultar();
	};
	
	$scope.nuevo=function(){
		certificacionesFondosFactory.traerCertificacionesFondosNuevo(
			$rootScope.ejefiscal
		).then(function(resp){
			//console.log(resp);
			if (!resp.estado) return;
			$scope.objeto=resp.json.certificacion;
			$scope.objeto.numerodocumento='';
			$scope.detalles={};
			//$scope.agregarDetalles();
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
		certificacionesFondosFactory.traerCertificacionesFondosEditar($scope.data[index].id).then(function(resp){
			//console.log(resp.json);
			if (resp.estado) {
			    $scope.objeto=resp.json.certificacion;
			    $scope.detalles=resp.json.certificacionlineas;
			}
			$scope.edicion=true;
			$scope.nuevoar=false;
			$scope.guardar=true;
		})
	};

	$scope.solicitar=function(index) {
		//console.log($scope.data[index]);
		index = $scope.calcularIndex(index);
		if ($scope.data[index].estado != "RE") {
			SweetAlert.swal("Certificaciones de Fondos!", "Solo se puede solicitar si esta en estado registrar.", "error");
			return;
		}
		if ($scope.data[index].valortotal <= 0) {
			SweetAlert.swal("Certificaciones de Fondos!", "El valor total tiene que ser mayor que cero.", "error");
			return;
		}
		$scope.data[index].npestado = "Solicitando";
		certificacionesFondosFactory.solicitar(
			$scope.data[index].id,
			"SO",
			null,
			null
		).then(function(resp){
			//console.log(resp);
			//$scope.pageChanged();
			SweetAlert.swal("Certificaciones de Fondos!", resp.mensajes.msg, resp.mensajes.type);
		});
	}

	$scope.aprobar = function(index) {
		index = $scope.calcularIndex(index);
		if ($scope.data[index].estado != "SO") {
			SweetAlert.swal("Certificaciones de Fondos!", "Solo se puede aprobar si esta en estado solicitado.", "error");
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
			certificacionesFondosFactory.solicitar(
				$scope.data[index].id,
				"AP",
				cur,
				null
			).then(function(resp){
				//console.log(resp);
				//$scope.pageChanged();
				SweetAlert.swal("Certificaciones de Fondos!", resp.mensajes.msg, resp.mensajes.type);
			});
		}, function() {
		});
	}

	$scope.negar = function(index) {
		index = $scope.calcularIndex(index);
		if ($scope.data[index].estado != "SO") {
			SweetAlert.swal("Certificaciones de Fondos!", "Solo se puede negar si esta en estado solicitado.", "error");
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
			certificacionesFondosFactory.solicitar(
				$scope.data[index].id,
				"NE",
				null,
				obj
			).then(function(resp){
				//console.log(resp);
				//$scope.pageChanged();
				SweetAlert.swal("Certificaciones de Fondos!", resp.mensajes.msg, resp.mensajes.type);
			});
		}, function() {
		});
	}

	$scope.LiquidacionTotal = function(index) {
		index = $scope.calcularIndex(index);
		if ($scope.data[index].estado != "AP") {
			SweetAlert.swal("Certificaciones de Fondos!", "Solo se puede liquidar si esta en estado aprobado.", "error");
			return;
		}
		var modalInstance = $uibModal.open({
			templateUrl : 'modalLiquidacionManua.html',
			controller : 'ModalCertificacionesFondoLiquidacionManuaController',
			size : 'lg',
			resolve: {
				titulo: function() {
					return "Liquidaci&oacute;n Total";
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
			$scope.data[index].npestado = "Liquidando";
			certificacionesFondosFactory.solicitar(
				$scope.data[index].id,
				"LT",
				null,
				obj
			).then(function(resp){
				//console.log(resp);
				//$scope.pageChanged();
				SweetAlert.swal("Certificaciones de Fondos!", resp.mensajes.msg, resp.mensajes.type);
			});
		}, function() {
		});
	}

	$scope.LiquidacionParcial = function(index) {
		index = $scope.calcularIndex(index);
		if ($scope.data[index].estado != "AP") {
			SweetAlert.swal("Certificaciones de Fondos!", "Solo se puede liquidar si esta en estado aprobado.", "error");
			return;
		}
		var modalInstance = $uibModal.open({
			templateUrl : 'modalLiquidacionManua.html',
			controller : 'ModalCertificacionesFondoLiquidacionManuaController',
			size : 'lg',
			resolve: {
				titulo: function() {
					return "Liquidaci&oacute;n Parcial";
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
			$scope.data[index].npestado = "Liquidando";
			certificacionesFondosFactory.solicitar(
				$scope.data[index].id,
				"LP",
				null,
				obj
			).then(function(resp){
				//console.log(resp);
				//$scope.pageChanged();
				SweetAlert.swal("Certificaciones de Fondos!", resp.mensajes.msg, resp.mensajes.type);
			});
		}, function() {
		});
	}

	$scope.eliminar = function(index) {
		index = $scope.calcularIndex(index);
		if ($scope.data[index].estado != "RE") {
			SweetAlert.swal("Certificaciones de Fondos!", "No se permite eliminar este articulo, solo los que estan 'Registrados'.", "error");
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
			certificacionesFondosFactory.solicitar(
				$scope.data[index].id,
				"EL",
				null,
				obj
			).then(function(resp){
				//console.log(resp);
				//$scope.pageChanged();
				SweetAlert.swal("Certificaciones de Fondos!", resp.mensajes.msg, resp.mensajes.type);
			});
		}, function() {
		});
	}

	$scope.agregarDetalles=function(){
		$scope.detalles={id: null};
	};

	$scope.agregarLinea = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalCertificacionesFondosLineas.html',
			controller : 'ModalCertificacionesFondosLineasController',
			size : 'lg',
			resolve : {
				ID : function() {
					return $scope.objeto.id;
				},
				unidadID : function() {
					return $scope.objeto.certificacionunidadid;
				},
				editar : function() {
					return null;
				}
			}
		});
		modalInstance.result.then(function(obj) {
		    $scope.detalles = obj.lineas;
		    $scope.objeto.valortotal = obj.valortotal;
		    $scope.form.submit(Form);
            SweetAlert.swal("Certificaciones de Fondos! - Lineas", "Registro guardado satisfactoriamente!", "success");
		}, function() {
		});
	};

	$scope.editarLinea = function(index) {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalCertificacionesFondosLineas.html',
			controller : 'ModalCertificacionesFondosLineasController',
			size : 'lg',
			resolve : {
				ID : function() {
					return $scope.objeto.id;
				},
				unidadID : function() {
					return $scope.objeto.certificacionunidadid;
				},
				editar : function() {
					return $scope.detalles[index].id
				}
			}
		});
		modalInstance.result.then(function(obj) {
		    $scope.detalles = obj.lineas;
		    $scope.objeto.valortotal = obj.valortotal;
		    $scope.form.submit(Form);
            SweetAlert.swal("Certificaciones de Fondos! - Lineas", "Registro guardado satisfactoriamente!", "success");
		}, function() {
		});
	};

	$scope.eliminarLinea = function(index) {
		SweetAlert.swal({
			title: "Certificaciones de Fondos?",
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
			certificacionesFondosFactory.eliminarLinea(
				$scope.detalles[index].id.id,
				$scope.detalles[index].id.lineaid
			).then(function(resp){
				if (resp.estado){
					SweetAlert.swal("Certificaciones de Fondos!", "Eliminado satisfactoriamente!", "success");
					$scope.objeto.valortotal -= $scope.detalles[index].npvalor;
				    $scope.detalles.splice(index, 1);
				    $scope.form.submit(Form);
	   			}else{
		            SweetAlert.swal("Certificaciones de Fondos!", resp.mensajes.msg, "error");
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
			//console.log(obj);
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
        				 if ($scope.nuevoar) {
	      					 $scope.noeditar = false;
	      					 $scope.nuevoar=false;
	      				     $scope.objeto=resp.json.certificacion;
        				 } else {
          		             form.$setPristine(true);
          		             $scope.edicion=false;
          		             $scope.objeto={};
        				 }
        				 //$scope.pageChanged();
      					 SweetAlert.swal(
      							 "Certificaciones de Fondos!",
      							 "Registro guardado satisfactoriamente!",
      							 "success"
						 );
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
	
	$scope.imprimirsolicitud=function(id){
    	var url = "/birt/frameset?__report=E01-CF.rptdesign" +
		"&certificacion=" + id;
	    window.open(url, '_blank');
		return;
	};
	
	$scope.imprimiroficio=function(id){
    	var url = "/birt/frameset?__report=E01-CFO.rptdesign" +
		"&certificacion=" + id;
	    window.open(url, '_blank');
		return;
	};

	$scope.calcularIndex = function(index) {
		return (($scope.tableParams.page() - 1) * 5) + index;
	}
} ]);
