'use strict';

app.controller('CargoEscalaController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","CargoEscalaFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams,cargoEscalaFactory) {
    	
	$scope.codigo=null;
	$scope.nombrecargo=null;
	$scope.grupoocupacional=null;
	$scope.estado=null;
	$scope.edicion=false;
	$scope.objeto={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		
		$scope.data=[];
		cargoEscalaFactory.traer(pagina).then(function(resp){
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
				
		cargoEscalaFactory.traerFiltro(pagina,$scope.codigo,$scope.nombrecargo, $scope.grupoocupacional, $scope.estado).then(function(resp){
			
			if (resp.meta)
				$scope.data=resp;
		})
	};
	
	$scope.limpiar=function(){

		$scope.codigo=null;
		$scope.nombrecargo=null;
		$scope.grupoocupacional=null;
		$scope.estado=null;
		$scope.consultar();
		
	};
	
	$scope.nuevo=function(){
		
		$scope.objeto={id:null,estado:'A'};		
		$scope.edicion=true;
	};
	
	$scope.editar=function(id){
		
		cargoEscalaFactory.traerObj(id).then(function(resp){
			
			console.log(resp.json.cargoescala);
			if (resp.estado){
				$scope.objeto=resp.json.cargoescala;
				$scope.edicion=true;
				console.log($scope.objeto);
				$scope.objeto.npremuneracion=$scope.convertirDecimal($scope.objeto.npremuneracion);
				
			}
		})
		
	};
	
	$scope.convertirDecimal=function (dato) {
		
	    return  parseFloat(dato).toFixed(5);
	};
	
	$scope.buscarEscala=function(){
		var modalInstance = $uibModal.open({
			templateUrl : 'modalEscala.html',
			controller : 'ModalEscalaController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.npcodigoescalarmu = obj.codigo;
			$scope.objeto.npgrupoocupacional = obj.grupoocupacional;
			$scope.objeto.npremuneracion= obj.remuneracion;
			$scope.objeto.ceescalarmuid=obj.id;
			
		}, function() {
			
		});

	};
	
	$scope.buscarCargo = function(){
		var modalInstance = $uibModal.open({
			templateUrl : 'modalCargo.html',
			controller : 'ModalCargoController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.npcodigocargo = obj.codigo;
			$scope.objeto.npnombrecargo = obj.nombre;
			$scope.objeto.cecargoid = obj.id;
			
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
		                
		            	cargoEscalaFactory.guardar($scope.objeto).then(function(resp){
		        			 if (resp.estado){
		        				 form.$setPristine(true);
			 		             $scope.edicion=false;
			 		             $scope.objeto={};
			 		             $scope.limpiar();
			 		             SweetAlert.swal("Cargo Escala!", "Registro guardado satisfactoriamente!", "success");
	 
		        			 }else{
			 		             SweetAlert.swal("Cargo Escala!", resp.mensajes.msg, "error");
		        				 
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