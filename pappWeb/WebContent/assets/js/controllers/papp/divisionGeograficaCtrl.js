'use strict';

app.controller('DivisionGeograficaController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","divisionGeograficaFactory",  function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams,divisionGeograficaFactory) {
    
	
	$scope.nombreFiltro=null;
	$scope.codigoFiltro=null;
	$scope.estadoFiltro=null;
	$scope.edicion=false;
	$scope.objeto={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		
		$scope.data=[];
		divisionGeograficaFactory.traerDivisiones(pagina).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
			
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
	
	
	$scope.filtrar=function(){
		
		$scope.data=[];
		divisionGeograficaFactory.traerDivisionesFiltro(pagina,$scope.nombreFiltro,$scope.codigoFiltro,$scope.estadoFiltro).then(function(resp){
			
			if (resp.meta)
				$scope.data=resp;
		})
	}
	
	$scope.limpiar=function(){
		$scope.nombreFiltro=null;
		$scope.codigoFiltro=null;
		$scope.estadoFiltro=null;
		
		$scope.consultar();
		
	};
	
	$scope.nuevo=function(){
		$scope.objeto={id:null,estado:'A'};
		
		$scope.edicion=true;
	}
	
	$scope.editar=function(id){
		divisionGeograficaFactory.traerDivision(id).then(function(resp){
			
			if (resp.estado)
			   $scope.objeto=resp.json.divisiongeografica;
			   $scope.edicion=true;
			   console.log($scope.objeto);

		})
		
	};
	
	$scope.buscarDivision=function(){
		var modalInstance = $uibModal.open({
			templateUrl : 'modalDiviviones.html',
			controller : 'ModalDivisionController',
			size : 'md',
			resolve : {
				tipo : function() {
					return $scope.objeto.tipo;
				}
			}
		});

		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.npcodigopadre = obj.codigo;
			$scope.objeto.npnombrepadre = obj.nombre;
			$scope.objeto.nptipopadrenombre = obj.nptipopadrenombre;
			$scope.objeto.divisiongeograficapadreid=obj.id;
			
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
		                console.log($scope.objeto);
		            	divisionGeograficaFactory.guardar($scope.objeto).then(function(resp){
		        			 if (resp.estado){
		        				 form.$setPristine(true);
			 		             $scope.edicion=false;
			 		             $scope.objeto={};
			 		             $scope.limpiar();
			 		             SweetAlert.swal("Division Geográfica!", "Registro guardado satisfactoriamente!", "success");
	 
		        			 }else{
			 		             SweetAlert.swal("Division Geográfica!", resp.mensajes.msg, "error");
		        				 
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

