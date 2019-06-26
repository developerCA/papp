'use strict';

app.controller('ModalCertificacionesFondoLiquidacionManuaController', [ "$scope","$rootScope","$uibModal","titulo","subtitulo","$uibModalInstance","SweetAlert","$filter", "ngTableParams","certificacionesFondosFactory",
	function($scope,$rootScope,$uibModal,titulo,subtitulo,$uibModalInstance,SweetAlert,$filter, ngTableParams, certificacionesFondosFactory) {

	$scope.data={};
	$scope.nptitulo="";
	$scope.npsubtitulo="";

	$scope.consultar=function(){
		$scope.nptitulo=titulo;
		$scope.npsubtitulo=subtitulo;
	}

	$scope.aceptar=function(dat){
		$uibModalInstance.close(dat);
	};
	
	$scope.cancelar=function(){
		$uibModalInstance.dismiss();
	};
} ]);