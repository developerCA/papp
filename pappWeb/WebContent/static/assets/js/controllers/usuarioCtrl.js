'use strict';
/**
 * controller for angular-menu
 * 
 */
app.controller('UsuarioCtrl', [
		"$scope",
		"$filter",
		"ngTableParams",
		"SweetAlert",
		"usuariosFactory",
		"empresaFactory",
		function($scope, $filter, ngTableParams,SweetAlert,usuariosFactory,empresaFactory) {

			$scope.data = [];
			$scope.edit = true;
			$scope.usuario={};
			$scope.active=0;
			$scope.activaPanel=true;
			$scope.activaPanelClave=true;
			$scope.empresas=[];
			
			$scope.init=function(){
				$scope.traerUsuarios();
				$scope.traerEmpresas();
				$scope.traerPerfiles();
			};
		
			$scope.traerUsuarios = function() {
				usuariosFactory.listaUsuarios().then(function(resp) {
					
					$scope.data = resp;
				})
			};

			$scope.nuevo=function(){
				
				usuariosFactory.nuevo().then(function(resp) {
					
					$scope.usuario=resp.objeto;
					$scope.edit = false;
					$scope.active=0
					
				})
				
							
			};
			
			$scope.traerEmpresas=function(){
				empresaFactory.lista().then(function(resp) {
					
					$scope.empresas=resp;
					
				})
				
			}
			$scope.traerPerfiles=function(){
				usuariosFactory.traerPerfiles().then(function(resp) {
					$scope.perfiles=resp;
				})
			};
			
			
			

			$scope.$watch('data', function() {
				
				$scope.tableParams = new ngTableParams({
					page : 1, // show first page
					count : 5, // count per page
					filter : {
						nombre : '',
						apellido:'',
						
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
			                //SweetAlert.swal("The form cannot be submitted because it contains validation errors!", "Errors are marked with a red, dashed border!", "error");
			                return;

			            } else {
			            	$scope.registrar(form);
			            	
			            }

			        },
			        submitClave: function (form) {
			        	
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
			            	$scope.cambiarClave(form);
			            	
			            }

			        },
			        
			        resetear: function (form) {
			        	
			            $scope.usuario = {};
			            form.$setPristine(true);
			            $scope.edit=true;
			            $scope.active=0;
						$scope.activaPanel=true;
						$scope.dato="";
						

			        }
			    };


			$scope.registrar=function(form){
				usuariosFactory.registrar($scope.usuario).then(function(r) {

                	if (r.estado){
                		SweetAlert.swal("Listo!", "La informacion se registro satisfactoriamente!", "success");
    					
    					$scope.traerUsuarios();
    					$scope.usuario={};
    					form.$setPristine(true);
        				$scope.edit = true;
        				$scope.activaPanel=false;
    						
                	}else{
                		SweetAlert.swal("Error!", r.mensaje, "error");
                	}
				    
				})
			};
			
			$scope.cambiarClave=function(form){
				console.log("cambia");
				
				usuariosFactory.cambiarClave($scope.usuario).then(function(r) {

                	if (r.estado){
                		SweetAlert.swal("Listo!", "La clave del usuario se registro satisfactoriamente!", "success");
    					
    					$scope.traerUsuarios();
    					$scope.usuario={};
    					form.$setPristine(true);
        				$scope.edit = true;
        				$scope.activaPanel=true;
        				$scope.activaPanelClave=true;

    						
                	}else{
                		SweetAlert.swal("Error!", r.mensaje, "error");
                	}
				    
				})
			};
			
			$scope.eliminarUsuario=function(codigoUsuario){
				SweetAlert.swal({
		            title: "Esta seguro de eliminar al usuario ?",
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
		            	
		            	usuariosFactory.eliminar(codigoUsuario).then(function(r) {
		            		$scope.traerUsuarios();
		            		
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
			
			$scope.editarUsuario=function(codigoUsuario){
				$scope.buscarUsuario(codigoUsuario);
				$scope.active=0;
			};
			
			$scope.resetClave=function(codigoUsuario){
				
				$scope.buscarUsuario(codigoUsuario,true);
				$scope.active=1;
				$scope.activaPanel=true;
				$scope.activaPanelClave=false;
			};
			
			
			
			$scope.buscarUsuario=function(codigoUsuario){
				 
				usuariosFactory.buscar(codigoUsuario).then(function(resp) {
					$scope.usuario=resp.objeto;
					$scope.edit = false;
				});
			};
			
            
	} ]);