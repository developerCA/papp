'use strict';

app.controller('PlanNacionalController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","plannacionalFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, plannacionalFactory) {

	$scope.arbol={};
	$scope.edicion=false;
	$scope.guardar=false;
	$scope.objeto={};
	$scope.objetolista={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.data=[];
		plannacionalFactory.traer(
			pagina,
			$rootScope.ejefiscal
		).then(function(resp){
			console.log(resp);
			if (resp.meta) {
				$scope.data = resp;
				$scope.arbol = JSON.parse(JSON.stringify(resp).split('"descripcion":').join('"title":'));
			}
		})
	};

	$scope.cargarHijos=function(node){
		if (!node.iscargado)
			//console.log(node);
		    node.iscargado=true;

		    plannacionalFactory.traerHijos(
	    		pagina,
	    		$rootScope.ejefiscal,
	    		node.id +
	    		(node.tipo == "P"? "&tipo=P": "")
    		).then(function(resp){
				var nodes=JSON.parse(JSON.stringify(resp).split('"descripcion":').join('"title":'));
				node.nodes=nodes;
			})
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

	$scope.filtrar=function(){
		$scope.data=[];
		plannacionalFactory.traerFiltro(pagina,$scope.nombreFiltro).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		})
	}

	$scope.limpiar=function(){
		$scope.nombreFiltro=null;
		$scope.ordenFiltro=null;

		$scope.consultar();
	};
	
	$scope.nuevo=function(node){
		//console.log("objeto nuevo");
		$scope.objeto={
			id: null,
			tipo: null,
			npTipo: null,
			estado: "A",
			plannacionalejerfiscalid: $rootScope.ejefiscal,
			plannacionalpadreid: null
		};
		if (node==null) {
			$scope.objeto.tipo = "O";
			$scope.objeto.npTipo = "Objeto";
		} else {
			$scope.objeto.plannacionalpadreid = node.id;
			switch (node.tipo) {
				case "O":
					$scope.objeto.tipo = "P";
					$scope.objeto.npTipo = "Politica";
					break;
				default:
					$scope.objeto.tipo = "M";
					$scope.objeto.npTipo = "Meta";
					break;
			}
			if (node.tipo=="M") {
				plannacionalFactory.traerEditar(node.id).then(function(resp){
					if (resp.estado) {
						$scope.objeto.plannacionalpadreid = resp.json.plannacional.plannacionalpadreid;
					}
					//console.log("fuente");
					//console.log(node);
				})
			}
		}

		//console.log($scope.objeto);
		$scope.edicion=true;
		$scope.guardar=true;
	}
	
	$scope.editar=function(node){
		plannacionalFactory.traerEditar(node.id).then(function(resp){
			console.log(resp.json.plannacional);
			if (resp.estado) {
			   $scope.objeto=resp.json.plannacional;
			}
			$scope.edicion=true;
			$scope.guardar=true;
		})
	};

	$scope.agregarDetalle=function(){
		var obj={id:{perfilid:$scope.objeto,permisoid:null},nppermiso:null};
		$scope.objetolista.push(obj);
	}

	$scope.removerDetalle=function(index){
		$scope.objetolista.splice(index,1);
	}

	$scope.abrirPerfilesPermisos = function(index) {
		//console.log("aqui");
		var modalInstance = $uibModal.open({
			templateUrl : 'modalPerfilesPermisos.html',
			controller : 'PerfilesPermisosController',
			size : 'lg'
		});
		/* modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objetolista[index].id.permisoid = obj.id;
			$scope.objetolista[index].nppermiso=obj.nombre;
		}, function() {
			console.log("close modal");
		}); */
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
		                
		            	plannacionalFactory.guardar($scope.objeto).then(function(resp){
		        			 if (resp.estado){
		        				 form.$setPristine(true);
			 		             $scope.edicion=false;
			 		             $scope.objeto={};
			 		             $scope.limpiar();
			 		             SweetAlert.swal("Plan Nacional!", "Registro grabado satisfactoriamente!", "success");
	 
		        			 }else{
			 		             SweetAlert.swal("Plan Nacional!", resp.mensajes.msg, "error");
		        				 
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
} ]);