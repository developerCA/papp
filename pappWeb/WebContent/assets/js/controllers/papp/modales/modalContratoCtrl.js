'use strict';

app.controller('ModalContratoController', [ "$scope","$rootScope","$uibModalInstance","objetoFuente","SweetAlert","$filter","ngTableParams","contratoFactory",
	function($scope,$rootScope,$uibModalInstance,objetoFuente,SweetAlert,$filter,ngTableParams,contratoFactory) {
	
	$scope.editar=function() {
		contratoFactory.editar(
			objetoFuente
		).then(function(resp){
			console.log(resp);
			return;
			if (resp.estado) {
			    $scope.objeto=resp.json.ordengasto;
			}
			$scope.noeditar=false;
			$scope.edicion=true;
			$scope.nuevoar=false;
			$scope.guardar=true;
		})
	};

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);
	};

	$scope.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};
}]);
