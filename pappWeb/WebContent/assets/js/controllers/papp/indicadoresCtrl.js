'use strict';

app.controller('IndicadoresController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","indicadoresFactory",  function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, indicadoresFactory) {

	$scope.codigo=null;
	$scope.nombre=null;

	$scope.edicion=false;
	$scope.nuevoar=false;
	$scope.guardar=false;
	$scope.objeto={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.data=[];
		indicadoresFactory.traerIndicadores(pagina,$rootScope.ejefiscal).then(function(resp){
//			console.log(resp);
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
		indicadoresFactory.traerIndicadoresFiltro(pagina,$rootScope.ejefiscal,$scope.codigo,$scope.nombre).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		})
	}
	
	$scope.limpiar=function(){
		$scope.codigo=null;
		$scope.fuerza=null;
		$scope.grado=null;
		$scope.padre=null;
		$scope.estado=null;

		$scope.consultar();
		
	};

	$scope.agregarDetalle=function(){
		var obj={id:{id:$scope.objeto.id,metodoid:$scope.metodocount},estado:'I'};
		$scope.objetodetalle.push(obj);
	}

	$scope.removerDetalle=function(index){
		$scope.objetodetalle.splice(index,1);
	}

	$scope.nuevo=function(){
		$scope.objeto={id:null};
		$scope.objetodetalle=[];
		$scope.edicion=true;
		$scope.nuevoar=true;
		$scope.guardar=true;
		$scope.metodocount=1;
	}
	
	$scope.editar=function(id){
		indicadoresFactory.traerIndicadoresEditar(id).then(function(resp){
//console.clear();
//console.log('AQUIIII-111');
console.log(resp);
			if (resp.estado) {
			    $scope.objeto=resp.json.indicador;
			    $scope.objetodetalle=resp.json.indicadormetodo;
			    $scope.metodocount=$scope.objetodetalle.length+1;
			    console.log($scope.objeto);
			    //console.log($scope.objetodetalle);
			}
			$scope.edicion=true;
			$scope.nuevoar=false;
			$scope.guardar=true;
		})
	};

	$scope.abrirUnidadMedida = function(index) {
		//console.log("aqui");
		var modalInstance = $uibModal.open({
			templateUrl : 'modalUnidadMedida.html',
			controller : 'ModalUnidadMedidaController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.indicadorunidadmedidaid = obj.id;
			$scope.objeto.npNombreunidad = obj.nombre;
			$scope.objeto.npCodigounidad = obj.codigo;
			$scope.objeto.npNombregrupo = obj.npNombregrupo;
			$scope.objeto.npCodigogrupo = obj.npCodigogrupo;
		}, function() {
			console.log("close modal");
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
		                $scope.objeto.indicarmetodo=$scope.objetodetalle;
		            	indicadoresFactory.guardar($scope.objeto).then(function(resp){
		        			 if (resp.estado){
		        				 form.$setPristine(true);
			 		             $scope.edicion=false;
			 		             $scope.objeto={};
			 		             $scope.limpiar();
			 		             SweetAlert.swal("Indicadores!", "Registro registrado satisfactoriamente!", "success");
	 
		        			 }else{
			 		             SweetAlert.swal("Indicadores!", resp.mensajes.msg, "error");
		        				 
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