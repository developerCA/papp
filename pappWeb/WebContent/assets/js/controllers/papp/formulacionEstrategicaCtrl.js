'use strict';

app.controller('FormulacionEstrategicaController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","formulacionEstrategicaFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, formulacionEstrategicaFactory) {

	$scope.dateOptions = {
	    changeYear: true,
	    changeMonth: true,
	    yearRange: '2000:-0',    
    };
	$scope.arbol={};
	$scope.edicion=false;
	$scope.guardar=false;
	$scope.objeto={};
	$scope.objetolista={};
	$scope.editarId=null;
	
	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.data=[];
		formulacionEstrategicaFactory.traerFormulacionEstrategica(
			pagina,
			$rootScope.ejefiscal
		).then(function(resp){
			//console.log(resp);
			if (resp.meta) {
				$scope.data = resp;
				for (var i = 0; i < $scope.data.length; i++) {
					$scope.data[i].nodeTipo = "PR";
				}
				$scope.arbol = JSON.parse(JSON.stringify($scope.data).split('"nombre":').join('"title":'));
				
			}
		})
	};

	$scope.cargarHijos=function(node){
		//console.log(node);
		if (!node.iscargado)
		    node.iscargado=true;
		else
			return;

		if (node.nodeTipo == "PR") { // Carga Subprograma
		    formulacionEstrategicaFactory.traerSubPrograma(
	    		node.npNivelid,
	    		$rootScope.ejefiscal
			).then(function(resp){
				//console.log(resp);
				for (var i = 0; i < resp.length; i++) {
					resp[i].nodeTipo = "SP";
				}
				var nodes=JSON.parse(JSON.stringify(resp).split('"nombre":').join('"title":'));
				node.nodes=nodes;
			});
		}

		if (node.nodeTipo == "SP") { // Carga Proyecto
		    formulacionEstrategicaFactory.traerProyecto(
	    		node.npNivelid,
	    		$rootScope.ejefiscal
			).then(function(resp){
				//console.log(resp);
				for (var i = 0; i < resp.length; i++) {
					resp[i].nodeTipo = "PY";
				}
				var nodes=JSON.parse(JSON.stringify(resp).split('"nombre":').join('"title":'));
				node.nodes=nodes;
			});
		}

		if (node.nodeTipo == "PY") { // Carga Actividad
		    formulacionEstrategicaFactory.traerActividad(
	    		node.npNivelid,
	    		$rootScope.ejefiscal
			).then(function(resp){
				//console.log(resp);
				for (var i = 0; i < resp.length; i++) {
					resp[i].nodeTipo = "AC";
				}
				var nodes=JSON.parse(JSON.stringify(resp).split('"nombre":').join('"title":'));
				node.nodes=nodes;
			});
		}
	}

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
	
	$scope.nuevo=function(node){
		$scope.editarId=null;
		formulacionEstrategicaFactory.traerFormulacionEstrategicaNuevoEstructura(
			node.id,
			$rootScope.ejefiscal,
			node.tipo
		).then(function(resp){
			$scope.objeto=resp.json.objetivo;

			console.log($scope.objeto);
			$scope.guardar=true;
			switch ($scope.objeto.tipo) {
				case "P": //Plan nacional
					$scope.edicion=true;
					break;
				case "E": //Estrategico
					$scope.edicionEstrategico=true;
					break;
				case "O": //Operativo
				case "F": //Fuerzas
				default:
					$scope.edicionOperativo=true;
					break;
			}
		})
	}
	
	$scope.editar=function(node){
		console.log("Fuente:");
		console.log(node);
		if (node.nodeTipo == "PR") { // Programa
			formulacionEstrategicaFactory.traerProgramaEditar(node).then(function(resp){
				//console.log("Respuesta:");
				//console.log(resp.json);
				if (resp.estado) {
					$scope.objeto=resp.json.programa;
					$scope.objeto.nodeTipo="PR";
				}
				$scope.guardar=true;
				$scope.edicion=true;
			});
		}
		if (node.nodeTipo == "SP") { // SubPrograma
			formulacionEstrategicaFactory.traerSubProgramaEditar(node.id).then(function(resp){
				//console.log("Respuesta:");
				//console.log(resp.json);
				if (resp.estado) {
					$scope.objeto=resp.json.subprograma;
					$scope.objeto.nodeTipo="SP";
				}
				$scope.guardar=true;
				$scope.edicionSubPrograma=true;
			});
		}
		if (node.nodeTipo == "PY") { // Proyecto
			formulacionEstrategicaFactory.traerProyectoEditar(node.id).then(function(resp){
				console.log("Respuesta:");
				console.log(resp.json);
				if (resp.estado) {
					$scope.objeto=resp.json.proyecto;
					$scope.objeto.npFechainicio = toDate($scope.objeto.npFechainicio);
					$scope.objeto.npFechafin = toDate($scope.objeto.npFechafin);

					$scope.editarId=$scope.objeto.id;
					$scope.objetolista=resp.json.proyectometa;
					if ($scope.objetolista == []) {
						$scope.agregarDetalle();
					}
					$scope.objeto.nodeTipo="PY";
				}
				$scope.guardar=true;
				$scope.edicionProyecto=true;
			});
		}
	};

	function toDate(fuente) {
		var parts = fuente.split('/');
		//please put attention to the month (parts[0]), Javascript counts months from 0:
		// January - 0, February - 1, etc
		return new Date(parts[2],parts[0]-1,parts[1]); 
	}

	$scope.agregarDetalle=function(){
		var obj={
			proyectoid: $scope.editarId,
			id: {
				id: null,
				metaejerciciofiscalid: $rootScope.ejefiscal
			}
		};
		$scope.objetolista.push(obj);
	}

	$scope.removerDetalle=function(index){
		$scope.objetolista.splice(index,1);
	}

	$scope.abrirObjetivoFuerzas = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalObjetivos.html',
			controller : 'ModalObjetivosController',
			size : 'lg',
			resolve : {
				ejefiscal : function() {
					return $rootScope.ejefiscal;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.programaobjetivofuersasid = obj.id;
			$scope.objeto.npObjetivocodigo = obj.codigo4;
			$scope.objeto.npObjetivodescripcion = obj.descripcion;
		}, function() {
		});
	};

	$scope.abrirFuerza = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalFuerza.html',
			controller : 'ModalFuerzaController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.proyectofuerzaid = obj.id;
			$scope.objeto.npFuerzacodigo = obj.codigo;
			$scope.objeto.npFuerzanombre = obj.nombre;
		}, function() {
		});
	};

	$scope.abrirLider = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalSocioNegocio.html',
			controller : 'ModalSocioNegocioController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.proyectoliderid = obj.id;
			$scope.objeto.npSocionegociocodigo = obj.codigo;
			$scope.objeto.npSocionegocionombre = obj.nombremostrado;
		}, function() {
		});
	};

	$scope.abrirIndicador = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalIndicadores.html',
			controller : 'ModalIndicadoresController',
			size : 'lg',
			resolve : {
				ejefiscal : function() {
					return $rootScope.ejefiscal;
				}
			}
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.proyectoindicadorid = obj.id;
			$scope.objeto.npIndicadorcodigo = obj.codigo;
			$scope.objeto.npIndicadornombre = obj.nombre;
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
            	$scope.newobj = $scope.objeto;
            	if ($scope.newobj.nodeTipo == "PY") {
            		$scope.newobj.proyectometaTOs = $scope.objetolista;
            		console.log($scope.newobj);
            	}
            	formulacionEstrategicaFactory.guardar($scope.newobj).then(function(resp){
        			 if (resp.estado){
        				 form.$setPristine(true);
	 		             $scope.edicion=false;
	 		             $scope.edicionSubPrograma=false;
	 		             $scope.edicionProyecto=false;
	 		             $scope.objeto={};
	 		             SweetAlert.swal("Formulaci&oacute;n Estrat&eacute;gica!", "Registro grabado satisfactoriamente!", "success");
        			 }else{
	 		             SweetAlert.swal("Formulaci&oacute;n Estrat&eacute;gica!", resp.mensajes.msg, "error");
        			 }
        		})
            }
        },
        reset: function (form) {
            $scope.myModel = angular.copy($scope.master);
            form.$setPristine(true);
            $scope.edicion=false;
            $scope.edicionSubPrograma=false;
            $scope.edicionProyecto=false;
            $scope.objeto={};
        }
    };

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