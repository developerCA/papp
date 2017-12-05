'use strict';
 
app.controller('ModalUnidadMedidaController', ["$scope", "$uibModalInstance", "SweetAlert", "$filter", "ngTableParams", "UnidadesMedidaFactory",
	function ($scope, $uibModalInstance, SweetAlert, $filter, ngTableParams, UnidadesMedidaFactory) {

		$scope.codigo = null;
		$scope.nombre = null;
	    $scope.estado = null;
	    $scope.nombregrupo = null;
	    $scope.edicion = false;
	    $scope.url = "";
	    $scope.objeto = { unidadmedidagrupomedidaid : null };
	    $scope.nombreFiltro=null;
	    $scope.codigoFiltro=null;
	    
	    
	    var pagina = 1;

	    $scope.init = function () {

	        $scope.grupos = [];

	        UnidadesMedidaFactory.traerGrupos(pagina).then(function (resp) {
	            if (resp.meta)
	                $scope.grupos = resp;
	            console.log($scope.grupos);
	        });

	        $scope.consultar();
	    };

	    $scope.consultar = function () {

	        $scope.data = [];

	        UnidadesMedidaFactory.traerUnidades(pagina).then(function (resp) {
	            if (resp.meta)
	                $scope.data = resp;
	        });        

	    };

	    $scope.$watch('data', function () {

	        $scope.tableParams = new ngTableParams({
	            page: 1, // show first page
	            count: 5, // count per page
	            filter: {}
	        }, {
	            total: $scope.data.length, // length of data
	            getData: function ($defer, params) {
	                var orderedData = params.filter() ? $filter('filter')(
							$scope.data, params.filter()) : $scope.data;
	                $scope.gruposMedida = orderedData.slice(
							(params.page() - 1) * params.count(), params
									.page()
									* params.count());
	                params.total(orderedData.length);
	                $defer.resolve($scope.gruposMedida);
	            }
	        });
	    });

	    $scope.filtrar = function () {
	        $scope.data = [];
	        UnidadesMedidaFactory.traerUnidadesFiltro(
	        		pagina,
	        		$scope.nombre,
	        		$scope.estado,
	        		$scope.nombregrupo,
	        		$scope.codigo
    		).then(function (resp) {
	            if (resp.meta)
	                $scope.data = resp;
	        })
	    }

	    $scope.mayusculas = function () {
	        $scope.nombre = $scope.nombre.toUpperCase();
	    }

	    $scope.limpiar = function () {
	    	$scope.codigo = null;
	        $scope.nombre = null;
	        $scope.estado = null;
	        $scope.nombregrupo = null;
		    
	        $scope.consultar();
	    };

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);

	};
	
	$scope.cancelar=function(){
		$uibModalInstance.dismiss();
	};
} ]);