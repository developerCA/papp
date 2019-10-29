'use strict';

app.controller('ReporteO02Controller', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","reporteP01Factory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, reporteP01Factory) {

	$scope.consultar = function() {
	}

	$scope.form = {
        submit: function (form) {
        	var url = "/birt/frameset?__report=O02-LEOD.rptdesign" +
					"&ef=" + $rootScope.ejefiscal;
        	window.open(url, '_blank');
        },
        reset: function (form) {
            $scope.myModel = angular.copy($scope.master);
            form.$setPristine(true);
        }
    };
} ]);
