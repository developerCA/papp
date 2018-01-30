app.controller('PUEController', [ "$scope","$rootScope","$aside","$uibModalInstance","SweetAlert","$filter", "ngTableParams",
	function($scope,$rootScope,$aside,$uibModalInstance,SweetAlert,$filter, ngTableParams) {

	$scope.init = function() {
		$scope.objetoVista=$rootScope.objetoVista;
		console.log($scope.objetoVista);
	} 
	
    $scope.ok = function(e) {
    	console.log("sc:", $scope);
        $uibModalInstance.close();
        e.stopPropagation();
    };
    
    $scope.cancel = function(e) {
        $uibModalInstance.dismiss();
        e.stopPropagation();
    };

} ]);
