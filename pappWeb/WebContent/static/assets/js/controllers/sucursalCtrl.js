'use strict';
/**
 * controller for angular-menu
 * 
 */
app.controller('SucursalCtrl', [
		"$scope",
		"$filter",
		"ngTableParams",
		"SweetAlert",
		"sucursalesFactory",
		function($scope, $filter, ngTableParams,SweetAlert, sucursalesFactory) {

			$scope.data = [];
			$scope.dataDias = [];
			
			
			$scope.edit = true;
			$scope.sucursal={};
			$scope.dto={sucursal:null};
			$scope.active=0;
			$scope.succolaborador=null;
			$scope.horas=null;
			$scope.activaPanel=true;
	
		
			$scope.traerSucursales = function() {
				sucursalesFactory.listaSucursales().then(function(resp) {

					$scope.data = resp;
					

				})
			};
			$scope.nuevo=function(){
				
				sucursalesFactory.nuevo().then(function(resp) {
					$scope.edit = false;
					$scope.active=0
					$scope.sucursal=resp.objeto.sucursal;
				})
				
							
			};
			
			function editar(){
				$scope.edit = false;
				$scope.horas=null;
				$scope.activaPanel=false;
			};
			

			$scope.$watch('data', function() {
				
				$scope.tableParams = new ngTableParams({
					page : 1, // show first page
					count : 5, // count per page
					filter : {
						nombre : ''
						
					}
				}, {
					total : $scope.data.length, // length of data
					getData : function($defer, params) {
						var orderedData = params.filter() ? $filter('filter')(
								$scope.data, params.filter()) : $scope.data;
						$scope.sucursales = orderedData.slice(
								(params.page() - 1) * params.count(), params
										.page()
										* params.count());
						params.total(orderedData.length);
						$defer.resolve($scope.sucursales);
					}
				});
			});
			
			$scope.$watch('dataDias', function() {
			
				 $scope.tableDias = new ngTableParams({
				        page: 1, // show first page
				        count: 7 // count per page
				    }, {
				        total: $scope.dataDias.length, // length of data
				        getData: function ($defer, params) {
				            $defer.resolve($scope.dataDias.slice((params.page() - 1) * params.count(), params.page() * params.count()));
				        }
				    });
				 
				
			
			});
			
			$scope.$watch('dataSucursales', function() {
				
				
			});

			$scope.form = {

			        submit: function (form,next) {
			        	
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
			                //SweetAlert.swal("The form cannot be submitted because it contains validation errors!", "Errors are marked with a red, dashed border!", "error");
			                return;

			            } else {
			            	$scope.registrar(form,next);
			            	
			            }

			        },
			        resetear: function (form) {
			        	
			            $scope.sucursal = {};
			            form.$setPristine(true);
			            $scope.edit=true;
			            $scope.active=0;
						$scope.horas=null;
						$scope.dataDias = [];
						$scope.activaPanel=true;
						
						

			        }
			    };


			$scope.registrar=function(form,next){
				
			    $scope.dto.sucursal=$scope.sucursal;
				
			    sucursalesFactory.registrar($scope.dto).then(function(r) {

                	if (r.estado){
                		SweetAlert.swal("Listo!", "La informacion se registro satisfactoriamente!", "success");
    					$scope.sucursal=r.objeto.sucursal;
    					$scope.traerSucursales();
    				
    					if (next){
    						
    						$scope.buscarSucursal($scope.sucursal.codigo,false);
    						$scope.active=1;
    						$scope.activaPanel=false;
    						
    					}else{
    						$scope.sucursal={};
    						form.$setPristine(true);
        					$scope.edit = true;
        					$scope.activaPanel=false;
    						
    	    			}
    					
    					
    		    	}else{
                		SweetAlert.swal("Error!", r.mensaje, "error");
        	          
                		
                	}
				    
				})
				
				
			};
			
			$scope.eliminarSucursal=function(codigoSucursal){
				SweetAlert.swal({
		            title: "Esta seguro de eliminar la sucursal ?",
		            text: "Es posible que la información que va a eliminar no se visualice en otros procesos del sistema!",
		            type: "warning",
		            showCancelButton: true,
		            confirmButtonColor: "#DD6B55",
		            confirmButtonText: "Si,estoy seguro",
		            cancelButtonText: "No",
		            closeOnConfirm: false,
		            closeOnCancel: false
		        }, function (isConfirm) {
		            if (isConfirm) {
		            	
		            	sucursalesFactory.eliminar(codigoSucursal).then(function(r) {
		            		$scope.traerSucursales();
		            		
		            		SweetAlert.swal({
			                	title: "Eliminado!", 
			                	text: "El registro se eliminó satisfactoriamente", 
			                	type: "success",
			                	confirmButtonColor: "#007AFF"
			                });	
		            	});
		                
		            } else {
		            	
		            	 SweetAlert.swal({title:"", timer:1});
		            }
		        });
				
				

			};
			
			$scope.editarSucursal=function(codigoSucursal){
				$scope.buscarSucursal(codigoSucursal,true);
				$scope.buscarDias(codigoSucursal);
				$scope.active=0;
			};
			
			$scope.editarDias=function(codigoSucursal){
				
				$scope.buscarSucursal(codigoSucursal,true);
				$scope.buscarDias(codigoSucursal);
				$scope.active=1;
			};
			
			$scope.editarHoras=function(codigoSucursal){
				$scope.buscarSucursal(codigoSucursal,true);
				$scope.buscarDias(codigoSucursal);
				$scope.active=2;
			};
			
			$scope.cambiarMarca=function (dia){
				
				dia.cambio=true;
			};
			$scope.cambiarMarcaSucursal=function (sucursal){
				
				sucursal.cambio=true;
				
			};
			
			$scope.cambiarMarcaHorario=function(horario){
				horario.cambio=true;
				
			};
			
			$scope.registrarDias=function(form,next){
				
				
				$scope.dto.sucursal=$scope.sucursal;
				$scope.dto.diasSucursal=$scope.dataDias;
				
				
				sucursalesFactory.registrar($scope.dto).then(function(r) {

	                	if (r.estado){
	                		SweetAlert.swal("Listo!", "La informacion se registro satisfactoriamente!", "success");
	    					$scope.sucursal=r.objeto.sucursal;
	    					$scope.traerSucursales();
	    				
	    					if (next){
	    						
	    						$scope.buscarSucursal($scope.sucursal.codigo,false);
	    						$scope.buscarDias($scope.sucursal.codigo);
	    						
	    						$scope.active=2;
	    						$scope.activaPanel=false;
	    							
	    					}else{
	    						$scope.sucursal={};
	    						form.$setPristine(true);
	        					$scope.edit = true;
	        					$scope.activaPanel=false;
	    						
	    	    			}
	    					
	    					
	    		    	}else{
	                		SweetAlert.swal("Error!", r.mensaje, "error");
	        	          
	                		
	                	}
					    
					})

					
			};
			
			$scope.registrarHorario=function(){
				
				sucursalesFactory.registarHoras($scope.horas).then(function(resp) {
						
					if (resp.estado){
						$scope.horas=resp.objeto;
						SweetAlert.swal("Listo!", "La informacion se registro satisfactoriamente!", "success");
    					
					}else{
						SweetAlert.swal("Error!", resp.mensaje, "error");
					}
					
					
				})
				
			}
			
			
			
			$scope.buscarSucursal=function(codigoSucursal,edit){
				 
				sucursalesFactory.buscar(codigoSucursal).then(function(resp) {
					console.log(resp.objeto);
					$scope.sucursal=resp.objeto.sucursal;
					$scope.dataDias=resp.objeto.diasSucursal;
					
					if (edit){
						editar();
								
					}
				
				});
			};
			
            $scope.buscarDias=function(codigoSucursal){
				
            	sucursalesFactory.listaDias(codigoSucursal).then(function(resp) {
					$scope.sucursaldias=resp;
					
				})
			};
			
			
			$scope.seleccionarDia=function(sucursalDia){
				
				
				sucursalesFactory.listaHoras(sucursalDia.codigo).then(function(resp) {
					$scope.horas=resp;
					
				});
				
				
				marcarSucursalDia(sucursalDia);
				
			};
			
			
			
			function marcarSucursalDia(sucursalDia){
				
				angular.forEach($scope.sucursaldias, function(value, key) {
					  value.active="";
				});
				
				sucursalDia.active="active";
				
			}
			
			function marcarDia(diaSucursal){
				
				angular.forEach($scope.diasSucursal, function(value, key) {
					  value.active="";
				});
				
				diaSucursal.active="active";
				
			}
			
			
			

		} ]);