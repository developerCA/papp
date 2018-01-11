'use strict';

app.controller('ModalTipoRegimenController', ["$scope", "$rootScope", "$uibModalInstance","$filter", "ngTableParams","ComunTipoRegimenFactory",
	function($scope, $rootScope, $uibModalInstance,$filter, ngTableParams,ComunTipoRegimenFactory) {
	
    $scope.data=[];

    $scope.consultar = function () {
        ComunTipoRegimenFactory.traer().then(function (resp) {
            //console.log(resp);
        	$scope.data = resp.tiporegimen;
        	console.log($scope.data);
        });
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

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);
	};

	$scope.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};
}]);
