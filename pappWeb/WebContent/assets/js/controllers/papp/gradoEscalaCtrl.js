'use strict';

app.controller('GradoEscalaController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","GradoEscalaFactory",  function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams,gradoEscalaFactory) {
    	
	$scope.codigo=null;
	$scope.nombregrado=null;
	$scope.nombrefuerza=null;
	$scope.grupoocupacional=null;
	$scope.estado=null;
	$scope.edicion=false;
	$scope.objeto={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		
		$scope.data=[];
		gradoEscalaFactory.traer(pagina).then(function(resp){
			console.log(resp);
			if (resp.meta)
				$scope.data=resp;				
		})
	
	};
	
	$scope.$watch('data', function() {
		
		$scope.tableParams = new ngTableParams({
			page : 1, // show first page
			count : 10, // count per page
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
				
		gradoEscalaFactory.traerFiltro(pagina,$scope.codigo,$scope.nombregrado,$scope.nombrefuerza, $scope.grupoocupacional).then(function(resp){
			
			if (resp.meta)
				$scope.data=resp;
		})
	};
	
	$scope.limpiar=function(){

		$scope.codigo=null;
		$scope.nombregrado=null;
		$scope.nombrefuerza=null;
		$scope.grupoocupacional=null;
		$scope.estado=null;
		$scope.consultar();
		
	};
	
	$scope.nuevo=function(){
		
		$scope.objeto={id:null,estado:'A'};		
		$scope.edicion=true;
	};
	
	$scope.editar=function(id){
		
		gradoEscalaFactory.traerObj(id).then(function(resp){
			
			if (resp.estado){
				$scope.objeto=resp.json.gradoescala;
				console.log("==========//==========");
				console.log($scope.objeto);
				$scope.edicion=true;
				console.log($scope.objeto);
			}
		})
		
	};
	
	$scope.buscarEscala=function(){
		var modalInstance = $uibModal.open({
			templateUrl : 'modalEscala.html',
			controller : 'ModalEscalaController',
			size : 'md',
			resolve : {
				tipo : function() {
					return $scope.objeto.tipo;
				}
			}
		});

		modalInstance.result.then(function(obj) {
			console.log("==========()==========");
			console.log(obj);
			console.log("==========()==========");
			$scope.escala = obj;
			$scope.objeto.geescalarmuid = obj.id;
			$scope.objeto.npcodigoescalarmu = obj.id;			
		}, function() {
			
		});

	};
	
	$scope.buscarGrado=function(){
		var modalInstance = $uibModal.open({
			templateUrl : 'modalGradoFuerza.html',
			controller : 'ModalGradoFuerzaController',
			size : 'md',
			resolve : {
				tipo : function() {
					return $scope.objeto.tipo;
				}
			}
		});

		modalInstance.result.then(function(obj) {
			console.log("==========**===========");
			console.log(obj);
			console.log("==========**===========");
			$scope.objeto.npcodigofuerza = obj.npcodigofuerza; 
			$scope.objeto.npcodigogrado = obj.npcodigogrado;
			$scope.objeto.npnombregrado = obj.npnombregrado;
			$scope.objeto.npnombrefuerza = obj.npnombrefuerza;
			$scope.objeto.npsiglafuerza = obj.npsiglafuerza;
			$scope.objeto.npsiglagrado = obj.npsiglagrado;	
			$scope.objeto.gegradofuerzaid = obj.id;
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
		            	console.log("===========<>============");
	            		console.log($scope.objeto);
	            		console.log("===========<>=============");
	            		gradoEscalaFactory.guardar($scope.objeto).then(function(resp){
		            		
		        			 if (resp.estado){
		        				 form.$setPristine(true);
			 		             $scope.edicion=false;
			 		             $scope.objeto={};
			 		             $scope.limpiar();
			 		             SweetAlert.swal("Grado Escala!", "Registro guardado satisfactoriamente!", "success");
	 
		        			 }else{
			 		             SweetAlert.swal("Grado Escala!", resp.mensajes.msg, "error");		        				 
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