'use strict';

app.controller('ModalCertificacionesFondoLiquidacionManuaController', [ "$scope","$rootScope","$uibModal","$uibModalInstance","SweetAlert","$filter", "ngTableParams","certificacionesFondosFactory",
	function($scope,$rootScope,$uibModal,$uibModalInstance,SweetAlert,$filter, ngTableParams, certificacionesFondosFactory) {

	$scope.data={};

	$scope.consultar=function(){
		console.log("cargo");
	}

	$scope.aceptar=function(dat){
		$uibModalInstance.close(dat);
	};
	
	$scope.cancelar=function(){
		$uibModalInstance.dismiss();
	};
} ]);