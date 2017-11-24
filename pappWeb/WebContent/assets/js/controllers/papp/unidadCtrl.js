'use strict';

app.controller('UnidadController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","unidadFactory",  function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams,unidadFactory) {
    
	
	$scope.nombreFiltro=null;
	$scope.codigoFiltro=null;
	$scope.estadoFiltro=null;
	$scope.edicion=false;
	$scope.objeto={};
	$scope.detalles=[];
	
	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.data=[];
		unidadFactory.traerUnidades(pagina).then(function(resp){
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
		unidadFactory.traerUnidadesFiltro(pagina,$scope.nombreFiltro,$scope.codigoFiltro,$scope.estadoFiltro).then(function(resp){
			
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
		$scope.detalles=[];
		$scope.edicion=true;
	}
	
	$scope.editar=function(id){
		
		unidadFactory.traerUnidad(id).then(function(resp){
			
			if (resp.estado)
			   $scope.objeto=resp.json.unidad;
			   $scope.detalles=resp.json.details;
			   $scope.edicion=true;
			  console.log(resp.json);
		})
		
	};
	
	$scope.agregarDetalle=function(){
		var obj={id: {id: null}, codigo: null, estado: "A", nombre: null};
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
		            	
		            	unidadFactory.guardar($scope.objeto).then(function(resp){
		        			 if (resp.estado){
		        				 form.$setPristine(true);
			 		             $scope.edicion=false;
			 		             $scope.objeto={};
			 		             $scope.detalles=[];
			 		             $scope.limpiar();
			 		             SweetAlert.swal("Clase de Registro!", "Registro registrado satisfactoriamente!", "success");
	 
		        			 }else{
			 		             SweetAlert.swal("Clase de Registro!", resp.mensajes.msg, "error");
		        				 
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


		$scope.abrirInstitutoEntidad = function(index) {
			//console.log("aqui");
			var modalInstance = $uibModal.open({
				templateUrl : 'modalInstitutoEntidad.html',
				controller : 'ModalInstitutoEntidadController',
				size : 'lg'
			});
			modalInstance.result.then(function(obj) {
				console.log(obj);
				$scope.detalles[index].id.id = obj.institucionid;
				$scope.detalles[index].unidadinstitucionid = obj.institucionid;
				$scope.detalles[index].id.unidad = obj.institucionentid;
				$scope.detalles[index].unidadinstitucionentid = obj.institucionentid;
				$scope.detalles[index].npcodigoinstitucion = obj.npcodigoinstitucion;
				$scope.detalles[index].npnombreinstitucion = obj.npnombreinstitucion;
				$scope.detalles[index].npcodigoentidad = obj.codigo;
				$scope.detalles[index].npnombreentidad = obj.nombre;
			}, function() {
				console.log("close modal");
			});
		};
} ]);