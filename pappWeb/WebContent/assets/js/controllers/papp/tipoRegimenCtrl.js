'use strict';

app.controller('TipoRegimenController', [ "$scope","$rootScope","SweetAlert","$filter", "ngTableParams","tipoRegimenFactory",  function($scope,$rootScope,SweetAlert,$filter, ngTableParams,tipoRegimenFactory) {
    
	
	$scope.nombreFiltro=null;
	$scope.codigoFiltro=null;
	$scope.estadoFiltro=null;
	$scope.edicion=false;
	$scope.objeto={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		
		$scope.data=[];
		tipoRegimenFactory.traerTiposRegimen(pagina).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
				console.log($scope.data);
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
		tipoRegimenFactory.traerTiposRegimenFiltro(pagina,$scope.nombreFiltro,$scope.estadoFiltro).then(function(resp){
			
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
		tipoRegimenFactory.traerTipo(id).then(function(resp){
			;
			if (resp.estado)
			   $scope.objeto=resp.json.tiporegimen;
			console.log($scope.objeto);
			   $scope.edicion=true;

		})
		
	};
	
	 $scope.eliminar = function (id) {

	        SweetAlert.swal({
	            title: "Tipo de r√©fimen",
	            text: "Confirma eliminar el registro?",
	            type: "warning",
	            showCancelButton: true,
	            confirmButtonText: "Eliminar",
	            cancelButtonText: "Cancelar",
	            closeOnConfirm: false
	        },
	            function (isconfirm) {
	        	 if (isconfirm)
	        		 tipoRegimenFactory.eliminar(id).then(function (resp) {
	                    if (resp.estado) {
	                        SweetAlert.swal("Tipo de RÈgimen", "Registro eliminado!", "success");
	                        $scope.limpiar();
	                        
	                    } else {
	                        SweetAlert.swal("Tipo de RÈgimen", resp.mensajes.msg, "error");
	                    }
	                })
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
		                
		            	tipoRegimenFactory.guardar($scope.objeto).then(function(resp){
		        			 if (resp.estado){
		        				 form.$setPristine(true);
			 		             $scope.edicion=false;
			 		             $scope.objeto={};
			 		             $scope.limpiar();
			 		             SweetAlert.swal("Tipo RÈgimen!", "Registro guardado satisfactoriamente!", "success");
	 
		        			 }else{
			 		             SweetAlert.swal("Tipo RÈgimen", resp.mensajes.msg, "error");
		        				 
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