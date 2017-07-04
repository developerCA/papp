'use strict';
/**
 * controller for angular-menu
 * 
 */
app.controller('ColaboradorCtrl', [
		"$scope",
		"$filter",
		"ngTableParams",
		"SweetAlert",
		"colaboradoresFactory",
		"sucursalesFactory",
		function($scope, $filter, ngTableParams,SweetAlert, colaboradoresFactory,sucursalesFactory) {

			$scope.data = [];
			$scope.dataServicios = [];
			$scope.dataSucursales=[];
			
			$scope.edit = true;
			$scope.colaborador={};
			$scope.dto={colaborador:null};
			$scope.active=0;
			$scope.succolaborador=null;
			$scope.horas=null;
			$scope.activaPanel=true;
	
			$scope.cargarData = function(datos) {

				$scope.data = datos;

			}
			$scope.traerColaboradores = function() {
				colaboradoresFactory.listaColaboradores().then(function(r) {

					$scope.cargarData(r);

				})
			};
			$scope.nuevo=function(){
				
				colaboradoresFactory.nuevo().then(function(resp) {

				
					$scope.edit = false;
					$scope.active=0
					$scope.colaborador=resp.objeto.colaborador;
					$scope.dataServicios = [];
					$scope.dataSucursales=resp.objeto.sucursalesColaborador;
					$scope.succolaborador=null;
					$scope.horas=null;
					$scope.diasSucursal=null;
					$scope.sucursalColaborador=null;
					
				})
				
							
			};
			
			function editar(){
				$scope.edit = false;
				$scope.succolaborador=null;
				$scope.horas=null;
				$scope.diasSucursal=null;
				$scope.activaPanel=false;
					
			
			};
			

			$scope.$watch('data', function() {
				
				$scope.tableParams = new ngTableParams({
					page : 1, // show first page
					count : 5, // count per page
					filter : {
						nombre : '',
						apellido : ''// initial filter
					}
				}, {
					total : $scope.data.length, // length of data
					getData : function($defer, params) {
						// use build-in angular filter
						var orderedData = params.filter() ? $filter('filter')(
								$scope.data, params.filter()) : $scope.data;
						$scope.colaboradores = orderedData.slice(
								(params.page() - 1) * params.count(), params
										.page()
										* params.count());
						params.total(orderedData.length);
						// set total for recalc pagination
						$defer.resolve($scope.colaboradores);
					}
				});
			});
			
			$scope.$watch('dataServicios', function() {
			
				$scope.tableServicios = new ngTableParams({
			        page: 1, 
			        count: 20 ,
			        filter : {
						nombre : ''
					}
			    }, {
			        total: $scope.dataServicios.length, 
			        getData : function($defer, params) {
						// use build-in angular filter
						var orderedData = params.filter() ? $filter('filter')(
								$scope.dataServicios, params.filter()) : $scope.dataServicios;
						$scope.servicios = orderedData.slice(
								(params.page() - 1) * params.count(), params
										.page()
										* params.count());
						params.total(orderedData.length);
						// set total for recalc pagination
						console.log($scope.servicios);
						$defer.resolve($scope.servicios);
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
			        	
			            $scope.colaborador = {};
			            form.$setPristine(true);
			            $scope.edit=true;
			            $scope.active=0;
						$scope.succolaborador=null;
						$scope.horas=null;
						$scope.diasSucursal=null;
						$scope.sucursalColaborador=null;
						$scope.activaPanel=true;
						
						

			        }
			    };


			$scope.registrar=function(form,next){
				
			    $scope.dto.colaborador=$scope.colaborador;
				$scope.dto.sucursalesColaborador=$scope.dataSucursales;
				
                colaboradoresFactory.registrar($scope.dto).then(function(r) {

                	if (r.estado){
                		SweetAlert.swal("Listo!", "La informacion se registro satisfactoriamente!", "success");
    					$scope.colaborador=r.objeto.colaborador;
    					$scope.traerColaboradores();
    				
    					if (next){
    						
    						$scope.buscarColaborador($scope.colaborador.codigo,false);
    						$scope.buscarSucursales($scope.colaborador.codigo);
    						$scope.active=1;
    						$scope.activaPanel=false;
    						
    					}else{
    						$scope.colaborador={};
    						form.$setPristine(true);
        					$scope.edit = true;
        					$scope.activaPanel=false;
    						
    	    			}
    					
    					
    		    	}else{
                		SweetAlert.swal("Error!", r.mensaje, "error");
        	          
                		
                	}
				    
				})
				
				
			};
			
			$scope.eliminarColaborador=function(codigoColaborador){
				SweetAlert.swal({
		            title: "Esta seguro de eliminar el colobarador/especialista ?",
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
		            	
		            	colaboradoresFactory.eliminar(codigoColaborador).then(function(r) {
		            		$scope.traerColaboradores();
		            		
		            		SweetAlert.swal({
			                	title: "Eliminado!", 
			                	text: "El registro se eliminó; satisfactoriamente", 
			                	type: "success",
			                	confirmButtonColor: "#007AFF"
			                });	
		            	});
		                
		            } else {
		            	
		            	 SweetAlert.swal({title:"", timer:1});
		            }
		        });
				
				

			};
			
			$scope.editarColaborador=function(codigoColaborador){
				$scope.buscarColaborador(codigoColaborador,true);
				$scope.buscarSucursales(codigoColaborador);
				$scope.active=0;
			};
			
			$scope.editarServicio=function(codigoColaborador){
				
				$scope.buscarColaborador(codigoColaborador,true);
				$scope.buscarSucursales(codigoColaborador);
				$scope.active=1;
			};
			
			$scope.editarHoras=function(codigoColaborador){
				$scope.buscarColaborador(codigoColaborador,true);
				$scope.buscarSucursales(codigoColaborador);
				$scope.active=2;
			};
			
			$scope.cambiarMarca=function (servicio){
				
				servicio.cambio=true;
			};
			$scope.cambiarMarcaSucursal=function (sucursal){
				
				sucursal.cambio=true;
				
			};
			
			$scope.cambiarMarcaHorario=function(horario){
				horario.cambio=true;
				
			};
			
			$scope.registrarServicios=function(form,next){
				
				
				$scope.dto.colaborador=$scope.colaborador;
				$scope.dto.serviciosColaborador=$scope.dataServicios;
				$scope.dto.sucursalesColaborador=$scope.dataSucursales;
				
				
				 colaboradoresFactory.registrar($scope.dto).then(function(r) {

	                	if (r.estado){
	                		SweetAlert.swal("Listo!", "La informacion se registro satisfactoriamente!", "success");
	    					$scope.colaborador=r.objeto.colaborador;
	    					$scope.traerColaboradores();
	    				
	    					if (next){
	    						
	    						$scope.buscarColaborador($scope.colaborador.codigo,false);
	    						$scope.buscarSucursales($scope.colaborador.codigo);
	    						
	    						$scope.active=2;
	    						$scope.activaPanel=false;
	    							
	    					}else{
	    						$scope.colaborador={};
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
				var dtoHoras={sucursalColaborador:$scope.succolaborador,listaDiaSucursalHora:$scope.horas};
				
				colaboradoresFactory.registarHoras(dtoHoras).then(function(resp) {
						
					if (resp.estado){
						$scope.horas=resp.objeto.listaDiaSucursalHora;
						SweetAlert.swal("Listo!", "La informacion se registro satisfactoriamente!", "success");
    					
					}else{
						SweetAlert.swal("Error!", resp.mensaje, "error");
					}
					
					
				})
				
			}
			
			$scope.buscarSucursales=function(codigoColaborador){
				
				colaboradoresFactory.listaSucursalesPorColaborador(codigoColaborador).then(function(resp) {
					$scope.sucursalColaborador=resp;	
				})
			};
			
			$scope.buscarColaborador=function(codigoColaborador,edit){
				 
				colaboradoresFactory.buscar(codigoColaborador).then(function(resp) {
					
					$scope.dataServicios=resp.objeto.serviciosColaborador;
					$scope.colaborador=resp.objeto.colaborador;
					$scope.dataSucursales=resp.objeto.sucursalesColaborador;
					
					if (edit){
						editar();
								
					}
				
				});
			};
			
			$scope.seleccionarSucursal=function(sucursalColaborador){
				
				sucursalesFactory.listaDias(sucursalColaborador.sucursal.codigo).then(function(resp) {
					
					$scope.diasSucursal=resp;
				});

				$scope.succolaborador=sucursalColaborador;
				marcarSucursal(sucursalColaborador);
				$scope.horas=null;
				
			};
			
			$scope.seleccionarDia=function(diaSucursal){
				
				colaboradoresFactory.listaHorasPorColaborador(diaSucursal.codigo,$scope.succolaborador.codigo).then(function(resp) {
					$scope.horas=resp;
				});
				marcarDia(diaSucursal);
				
			};
			
			function marcarSucursal(sucursalColaborador){
				
				angular.forEach($scope.sucursalColaborador, function(value, key) {
					  value.active="";
				});
				
				sucursalColaborador.active="active";
				
			}
			
			function marcarDia(diaSucursal){
				
				angular.forEach($scope.diasSucursal, function(value, key) {
					  value.active="";
				});
				
				diaSucursal.active="active";
				
			}
			
			
			

		} ]);