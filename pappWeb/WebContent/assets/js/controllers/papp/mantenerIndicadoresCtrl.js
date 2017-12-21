'use strict';

app.controller('MantenerIndicadoresController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","mantenerIndicadoresFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, mantenerIndicadoresFactory) {

	$scope.arbol={};
	$scope.edicion=false;
	$scope.guardar=false;
	$scope.nuevoar=false;
	$scope.padreid=0;
	$scope.objeto={};
	$scope.objetolista={};
	$scope.nodeActivo=null;
	
	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.data=[];
		mantenerIndicadoresFactory.traerMantenerIndicadores(
			pagina,
			$rootScope.ejefiscal
		).then(function(resp){
			//console.log(resp);
			if (resp.meta) {
				$scope.data = resp;
				$scope.arbol = JSON.parse(JSON.stringify($scope.data).split('"nombre":').join('"title":'));
				//console.log($scope.arbol);
			}
		})
	};

	$scope.cargarHijos=function(node){
		if (!node.iscargado)
			console.log(node);
			console.log(node.id);
		    node.iscargado=true;

		    mantenerIndicadoresFactory.traerMantenerIndicadoresHijos(
	    		pagina,
	    		$rootScope.ejefiscal,
	    		node.id
    		).then(function(resp){
				var nodes=JSON.parse(JSON.stringify(resp).split('"nombre":').join('"title":'));
				for (var i = 0; i < nodes.length; i++) {
					nodes[i].indicadorpadreid = node.id;
				}
    			node.nodes=nodes;
			})
	}

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
		mantenerIndicadoresFactory.traerMantenerIndicadoresFiltro(pagina,$scope.nombreFiltro).then(function(resp){
			if (resp.meta)
				$scope.data=resp;
		})
	}

	$scope.limpiar=function(){
		$scope.nombreFiltro=null;
		$scope.ordenFiltro=null;

		$scope.consultar();
	};
	
	$scope.nuevo=function(node){
		$scope.padreid = 0;
		$scope.nodeActivo = node;
		if (node != null) {
			$scope.padreid = node.id;
			/*if (node.indicadorpadreid == undefined) {
				$scope.padreid = node.id;
			} else {
				$scope.padreid = node.indicadorpadreid;
			}*/
		}
		//console.log($scope.padreid);
		//console.log(node);
		$scope.objeto = {
			id: null,
			indicadorejerciciofiscalid: $rootScope.ejefiscal,
			indicadorpadreid: $scope.padreid,
			indicadorpadreid: null,
			eriodicidad: null,
			estado: "A"
		};
		$scope.objeto.indicadorpadreid = $scope.padreid;
		//console.log($scope.objeto);
		$scope.objetolista = [];
		$scope.agregarDetalle();

		$scope.guardar=true;
		$scope.edicion=true;
		$scope.nuevoar=true;
	}

	$scope.agregarDetalle=function(){
		var obj = {
			id: {
				id: null,
				metodoid: null
			},
			estado: null
		};
		$scope.objetolista.push(obj);
	}

	$scope.removerDetalle=function(index){
		$scope.objetolista.splice(index,1);
	}

	$scope.editar=function(node){
		console.log(node);
		$scope.nodeActivo = node;
		mantenerIndicadoresFactory.traerMantenerIndicadoresEditar(node.id).then(function(resp){
			console.log(resp.json);
			if (resp.estado) {
			   $scope.objeto=resp.json.indicador;
			   $scope.objetolista=resp.json.indicadormetodo;
			}
			$scope.guardar=true;
			$scope.edicion=true;
			$scope.nuevoar=false;
		})
	};

	$scope.abrirUnidadMedida = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'assets/views/papp/modal/modalUnidadMedida.html',
			controller : 'ModalUnidadMedidaController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.indicadorunidadmedidaid = obj.id;
			$scope.objeto.npCodigounidad = obj.codigo;
			$scope.objeto.npNombreunidad = obj.nombre;
			$scope.objeto.npCodigogrupo = obj.npCodigogrupo;
			$scope.objeto.npNombregrupo = obj.npNombregrupo;
		}, function() {
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
            	$scope.obj = $scope.objeto;
            	$scope.obj.indicadormetodosTOs = $scope.objetolista;
            	mantenerIndicadoresFactory.guardar($scope.obj).then(function(resp){
        			 if (resp.estado){
        				 $scope.nodeActivo.iscargado = false;
        				 form.$setPristine(true);
	 		             $scope.edicion=false;
	 		             $scope.objeto={};
	 		             //$scope.limpiar();
	 		             SweetAlert.swal("Mantener Indicadores!", "Registro grabado satisfactoriamente!", "success");
        			 }else{
	 		             SweetAlert.swal("Mantener Indicadores!", resp.mensajes.msg, "error");
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