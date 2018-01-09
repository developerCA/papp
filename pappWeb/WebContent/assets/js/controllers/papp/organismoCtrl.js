'use strict';

app.controller('OrganismoController', [ "$scope","$rootScope","SweetAlert","$filter", "ngTableParams","organismoFactory",
	function($scope,$rootScope,SweetAlert,$filter, ngTableParams,organismoFactory) {

	$scope.nombreFiltro = null;
	$scope.codigoFiltro = null;
	$scope.estadoFiltro = null;
    $scope.edicion = false;
    $scope.objeto = {};
    $scope.data = [];

    $scope.pagina = 1;
    $scope.aplicafiltro=false;

    $scope.consultar = function () {
        organismoFactory.traer(
    		$scope.pagina,
    		$rootScope.ejefiscal
		).then(function (resp) {
        	$scope.data = resp.json.result;
            $scope.total = resp.json.total.valor;
        })
    };

    $scope.pageChanged = function() {
        if ($scope.aplicafiltro){
        	$scope.filtrarUnico();
        }else{
        	$scope.consultar();	
        }
    };  
    
    $scope.filtrar=function(){
    	$scope.pagina=1;
    	$scope.aplicafiltro=true;
    	$scope.filtrarUnico();
    }  

	$scope.filtrarUnico=function(){
        organismoFactory.traerFiltro(
    		$scope.pagina,
    		$rootScope.ejefiscal,
    		$scope.codigoFiltro,
    		$scope.nombreFiltro,
    		$scope.estadoFiltro
		).then(function (resp) {
        	$scope.data = resp.json.result;
            $scope.total = resp.json.total.valor;
        })
    };

    $scope.mayusculas = function () {
        $scope.nombre = $scope.nombre.toUpperCase();
    };

    $scope.limpiar = function () {
    	$scope.nombreFiltro = null;
    	$scope.codigoFiltro = null;
    	$scope.estadoFiltro = null;
        $scope.consultar();
    };

	$scope.blurCodigo=function(index) {
		for (var i = 0; i < $scope.detalles.length; i++) {
			if ((i != index) && ($scope.detalles[i].codigo == $scope.detalles[index].codigo)) {
	             SweetAlert.swal("Organismo!", "YA EXISTE CLASE DE REGISTRO " + $scope.detalles[i].codigo + ",  CLASE MODIFICACION CODIGO", "error");
	             $scope.detalles[index].codigo = null;
	             return false;
			}
		}
	}
/*
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
*/
	
	$scope.nuevo=function(){
		$scope.objeto={id:null,estado:'A',organismoejerciciofiscalid:$rootScope.ejefiscal};
		$scope.detalles=[];
		$scope.edicion=true;
	}
	
	$scope.editar=function(id){
		organismoFactory.editar(id).then(function(resp){
			if (resp.estado)
				$scope.objeto=resp.json.organismo;
				//console.log($scope.objeto);
				$scope.detalles=resp.json.details;
				$scope.edicion=true;
		})
	};

	$scope.agregarDetalle=function(){
		var obj={codigo:null,estado:"A",nombre:null};
		$scope.detalles.push(obj);
	}
	
	$scope.removerDetalle=function(index){
		$scope.detalles.splice(index,1);
	}

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
                
            	$scope.objeto.details=$scope.detalles;
            	
            	organismoFactory.guardar($scope.objeto).then(function(resp){
        			 if (resp.estado){
        				 form.$setPristine(true);
	 		             $scope.edicion=false;
	 		             $scope.objeto={};
	 		             $scope.detalles=[];
	 		             $scope.limpiar();
	 		             SweetAlert.swal("Organismo!", "Registro guardado satisfactoriamente!", "success");
 
	        			 }else{
		 		             SweetAlert.swal("Organismo!", resp.mensajes.msg, "error");
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