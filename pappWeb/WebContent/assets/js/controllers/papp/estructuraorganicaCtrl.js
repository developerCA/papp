'use strict';

app.controller('EstructuraOrganicaController', [ "$scope","$rootScope","$uibModal","SweetAlert","$filter", "ngTableParams","estructuraorganicaFactory","unidadFactory",
	function($scope,$rootScope,$uibModal,SweetAlert,$filter, ngTableParams, estructuraorganicaFactory,unidadFactory) {

	$scope.codigo=null;
	$scope.fuerza=null;
	$scope.grado=null;
	$scope.padre=null;
	$scope.estado=null;

	$scope.edicion=false;
	$scope.nuevoar=false;
	$scope.guardar=false;
	$scope.objeto={estado:null};
	$scope.tabactivo=0;
	$scope.arbol={};
	
	var pagina = 1;
	
	$scope.consultar=function(){
		$scope.data=[];
		//console.log('aqi');
		estructuraorganicaFactory.traerEstructuraOrganica(pagina).then(function(resp){
			console.log(resp);
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
		estructuraorganicaFactory.traerEstructuraOrganicaFiltro(pagina,$scope.codigo,$scope.fuerza,$scope.grado,$scope.padre,$scope.estado).then(function(resp){
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
	
	$scope.nuevo=function(){
		$scope.objeto={id:null,estado:null};
		$scope.edicion=true;
		$scope.nuevoar=true;
		$scope.guardar=true;
	}
	
	$scope.editar=function(id){
		estructuraorganicaFactory.traerEstructuraOrganicaEditar(id).then(function(resp){
			if (resp.estado) {
			    $scope.objeto=resp.json.estructuraorganica;
			}
			$scope.edicion=true;
			$scope.nuevoar=false;
			$scope.guardar=true;
			console.log($scope.objeto);
			$scope.tabactivo=0;
		})
	};

	$scope.editarUnidad=function(id){
		
		$scope.tabactivo=1;
		$scope.edicion=true;
		
		unidadFactory.traerUnidadesArbol(pagina,id).then(function(resp){
			$scope.arbol=resp;
			
			console.log($scope.arbol);
		})
		
		
	};
	
	$scope.arbol2 = [{
        'id': 1,
        'title': 'node1',
        'nodes': [
          {
            'id': 11,
            'title': 'node1.1',
            'nodes': [
              {
                'id': 111,
                'title': 'node1.1.1',
                'nodes': []
              }
            ]
          },
          {
            'id': 12,
            'title': 'node1.2',
            'nodes': []
          }
        ]
      }, {
        'id': 2,
        'title': 'node2',
        'nodrop': true, // An arbitrary property to check in custom template for nodrop-enabled
        'nodes': [
          {
            'id': 21,
            'title': 'node2.1',
            'nodes': []
          },
          {
            'id': 22,
            'title': 'node2.2',
            'nodes': []
          }
        ]
      }, {
        'id': 3,
        'title': 'node3',
        'nodes': [
          {
            'id': 31,
            'title': 'node3.1',
            'nodes': []
          }
        ]
      }];
	
	
	$scope.abrirInstitucion = function() {
		var modalInstance = $uibModal.open({
			templateUrl : 'modalInstitucion.html',
			controller : 'ModalInstitucionController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			console.log(obj);
			$scope.objeto.eorganicainstitucionid = obj.id;
			$scope.objeto.npcodigoinstitucion = obj.codigo;
			$scope.objeto.npnombreinstitucion = obj.nombre;
		}, function() {
			console.log("close modal");
		});
	};

	$scope.abrirGrado = function(index) {
		//console.log("aqui");
		var modalInstance = $uibModal.open({
			templateUrl : 'modalGrado.html',
			controller : 'ModalGradoController',
			size : 'lg'
		});
		modalInstance.result.then(function(obj) {
			//console.log(obj);
			$scope.objeto.estructuraorganicagradoid = obj.id;
			$scope.objeto.npnombregrado = obj.nombre;
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
		                
		            	estructuraorganicaFactory.guardar($scope.objeto).then(function(resp){
		        			 if (resp.estado){
		        				 form.$setPristine(true);
			 		             $scope.edicion=false;
			 		             $scope.objeto={};
			 		             $scope.limpiar();
			 		             SweetAlert.swal("Grado - Fuerza!", "Registro registrado satisfactoriamente!", "success");
	 
		        			 }else{
			 		             SweetAlert.swal("Grado - Fuerza!", resp.mensajes.msg, "error");
		        				 
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