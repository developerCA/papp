'use strict';

app.controller('ModalUnidadArbolController', [ "$scope","$rootScope","$uibModalInstance","instituicionFuente","institucionentidad","SweetAlert","$filter", "ngTableParams","unidadFactory",
	function($scope,$rootScope,$uibModalInstance,instituicionFuente,institucionentidad,SweetAlert,$filter, ngTableParams,unidadFactory) {

	$scope.id=null;
	$scope.institucion=null;
	$scope.estado='A';
	$scope.arbol=[];

	$scope.consultar=function(){
		unidadFactory.traerUnidadArbolFiltro(
			1,
			$scope.id,
			instituicionFuente,
			institucionentidad,
			$scope.estado
		).then(function(resp){
			if (resp.meta)
				$scope.arbol=resp;
		})
	}
	
	$scope.limpiar=function(){
		$scope.id=null;
		$scope.institucion=null;
		$scope.estado=null;
		
		$scope.consultar();
	};

	$scope.seleccionar=function(obj){
		$uibModalInstance.close(obj);		
	};
	
	$scope.cancelar = function() {
		$uibModalInstance.dismiss('cancel');
	};

	$scope.cargarHijos=function(node){
		//console.log(node);
		if (!node.iscargado)
		    node.iscargado=true;
		else
			return;

		unidadFactory.traerUnidadArbolFiltro(
			1,
    		node.id,
			instituicionFuente,
			institucionentidad,
			$scope.estado
		).then(function(resp){
//			//console.log(resp);
//			for (var i = 0; i < resp.length; i++) {
//				resp[i].nodeTipo = tipo;
//				resp[i].padreID = node.id;
//			}
			var nodes=resp;
			//console.log(nodes);
			for (var i = 0; i < nodes.length; i++) {
				nodes[i].nodePadre = node;
			}
			node.nodes=nodes;
		});
	}
}]);
