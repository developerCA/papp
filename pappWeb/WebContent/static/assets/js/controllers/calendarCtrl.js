'use strict';
/**
 * Controller of the angularBootstrapCalendarApp
*/

app.filter('propsFilter', function () {
    return function (items, props) {
        var out = [];

        if (angular.isArray(items)) {
        	
            items.forEach(function (item) {
                var itemMatches = false;

                var keys = Object.keys(props);
                for (var i = 0; i < keys.length; i++) {
                    var prop = keys[i];
                    var text = props[prop].toLowerCase();
                    if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
                        itemMatches = true;
                        break;
                    }
                }

                if (itemMatches) {
                    out.push(item);
                }
            });
        } else {
            // Let the output be the input untouched
            out = items;
        }

        return out;
    };
});


app.controller('CalendarCtrl', ["$scope", "$aside", "moment", "SweetAlert","uiCalendarConfig","$window","$compile","sucursalesFactory","citasFactory","$timeout", function ($scope, $aside, moment, SweetAlert,uiCalendarConfig,$window,$compile,sucursalesFactory,citasFactory,$timeout) {

    var vm = this;
    var date = new Date();
    var d = date.getDate();
    var m = date.getMonth();
    var anio = date.getFullYear();
    var y = date.getFullYear();
    
    
       

    $scope.cargarSucursales=function(){
    	sucursalesFactory.listaSucursales().then(function(r){
			$scope.sucursales=r;
			
		})
    	
    };
    
    $scope.seleccionarSucursal=function(sucursal){
		
		marcarSucursal(sucursal);
	};
	
	function marcarSucursal(sucursal){
		
		angular.forEach($scope.sucursales, function(value, key) {
			  value.active="";
		});
		
		sucursal.active="active";
		$scope.selectedSucursal=sucursal;
		
	}      
    $scope.selectedSucursal=null;
    
    $scope.eventSource = {
            url: "http://www.google.com/calendar/feeds/usa__en%40holiday.calendar.google.com/public/basic",
            className: 'gcal-event',           // an option!
            currentTimezone: 'America/Chicago' // an option!
    };
    
    
    /* alert on eventClick */
    $scope.alertOnEventClick = function( date, jsEvent, view){
    	console.log(date);
        citasFactory.buscar(date.id).then(function(resp){
 			console.log(resp.objeto);
 			showCita(resp.objeto);
 		})
    };
 
    /* alert on Drop */
     $scope.alertOnDrop = function(event, delta, revertFunc, jsEvent, ui, view){
       $scope.alertMessage = ('Event Dropped to make dayDelta ' + delta);
    };
    /* alert on Resize */
    $scope.alertOnResize = function(event, delta, revertFunc, jsEvent, ui, view ){
       $scope.alertMessage = ('Event Resized to make dayDelta ' + delta);
    };
    
    $scope.eventRender = function( event, element, view ) {
        element.attr({'tooltip': event.title,
                      'tooltip-append-to-body': true});
        $compile(element)($scope);
    };
    
    $scope.uiConfig = {
    	      calendar:{
    	    	height: '100%',
    	    	editable: true,
    	        header:{
    	          left: '',
    	          center: 'title',
    	          right: 'today prev,next'
    	        },
    	        eventClick: $scope.alertOnEventClick,
    	        eventRender: $scope.eventRender
    	      }
    };
    
    $scope.events = [];
    
       
 
    $scope.changeView = function(view,calendar) {
    	
        uiCalendarConfig.calendars[calendar].fullCalendar('changeView',view);
      };
      
    
    $scope.eventsF = function (start, end, timezone, callback) {
        var s = new Date(start).getTime() / 1000;
        var e = new Date(end).getTime() / 1000;
        var m = new Date(start).getMonth();
        callback($scope.events);
        
    };
      
   
    
    
    $scope.eventSources = [$scope.events, $scope.eventSource, $scope.eventsF];
    
    $scope.$watch('selectedSucursal', function() {
        
          if ($scope.selectedSucursal!=null){
        	  $scope.liberarCitas();
          }
    });
    
    $scope.liberarCitas=function(){
    	citasFactory.listaCitas($scope.selectedSucursal.codigo,anio).then(function(resp){
  			
  			$scope.events.splice(0,$scope.events.length);
  			
  			angular.forEach(resp, function(value, key) {
  				var event={color: value.color,textColor: 'white',id:value.codigo,title: value.title,start: new Date(value.anioInicio, value.mesInicio -1,value.diaInicio,value.horaInicio,value.minutoInicio)};
      	      	$scope.events.push(event);  
      	      	
  			});
  		})
    }
    
    function formatDate(date) {
        var d = new Date(date),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();

        if (month.length < 2) month = '0' + month;
        if (day.length < 2) day = '0' + day;

        return [year, month, day].join('-');
    }
    
    $scope.cargarEventos=function(){
    	console.log($scope.calendarDate);
    	console.log($scope.calendarView);
    	
    }

    $scope.addEvent = function () {
       
   	 var event={     title: '',
   	      type: 'home',
   	      endsAt: new Date(y, m, d)
   	  };
   	 
        $scope.eventEdited(event);
    };

    $scope.eventEdited = function (event) {
    	
        showModal('Edited', event,$scope.selectedSucursal);
    };

  

    $scope.toggle = function ($event, field, event) {
        $event.preventDefault();
        $event.stopPropagation();

        event[field] = !event[field];
    };

       
    function showCita(cita) {
    	var modalInstance = $aside.open({
            templateUrl: 'citaEvent.html',
            placement: 'right',
            size: 'sm',
            backdrop: true,
            controller: function ($scope, $uibModal,$uibModalInstance,toaster,citasFactory) {
            	
            	
            	$scope.cargarCita=function(){
            		$scope.cita=cita;
            	}
            	$scope.cerrarCita=function(){
            		$uibModalInstance.dismiss();
            	}
            	
            	$scope.cancelarCita=function(){
            		citasFactory.cancelar($scope.cita.codigo).then(function(resp){
            			if (resp.estado);
            			$uibModalInstance.close();
            			
            		})
            		
            	}
            	
            	$scope.confirmarCita=function(){
            		citasFactory.aprobar($scope.cita.codigo).then(function(resp){
            			if (resp.estado);
            			$uibModalInstance.close();
            			
            		})	
            	}
            }
        });
                	
        modalInstance.result.then(function () {
        	console.log("cancela");
        	$scope.liberarCitas();
        });
        
    }
    
    function showModal(action, event,sucursal) {
        var modalInstance = $aside.open({
            templateUrl: 'calendarEvent.html',
            placement: 'right',
            size: 'sm',
            backdrop: true,
            controller: function ($scope, $uibModal,$uibModalInstance,toaster,clientesFactory,serviciosFactory,colaboradoresFactory,citasFactory) {
            	$scope.format = 'yyyy-MM-dd';
            	$scope.clientes=[];
            	$scope.servicio={selected:null};
            	$scope.cliente={selected:null};
            	$scope.colaborador={selected:null};
            	$scope.colaboradores=[];
            	$scope.cita={fecha:null};
            	$scope.editaCita=true;
            	$scope.clienteEdit=null;
            	$scope.disponibilidades=[];
            	$scope.eventoDto={codigoServicio:null,codigoHorarioColaborador:null,fechaCita:null};
            	$scope.$modalInstance = $uibModalInstance;
                $scope.action = action;
                $scope.event = event;
                $scope.sucursalEdit=sucursal;
              
                
                $scope.nuevoCliente=function(){
                	
                	clientesFactory.nuevo().then(function(resp){
            			$scope.clienteEdit=resp.objeto;
            			$scope.editaCita=false;
            			console.log($scope.clienteEdit);	
            		})
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
    			                //SweetAlert.swal("The form cannot be submitted because it contains validation errors!", "Errors are marked with a red, dashed border!", "error");
    			                return;

    			            } else {
    			            	$scope.guardarCliente(form);
    			            	
    			            }

    			        },
    			        resetear: function (form) {
    			        	$scope.editaCita=true;
    			            $scope.clienteEdit = {};
    			            form.$setPristine(true);
    			            
    						
    						

    			        }
    			    };

                $scope.guardarCliente=function(form){
                	clientesFactory.registrar($scope.clienteEdit).then(function(resp){
               		 if (resp.estado){
               			 $scope.cargarClientes();
               			 toaster.pop("sucess", "Cliente" ,"El cliente se registró satisfactoriamente" );
                  		 $scope.editaCita=true;
                  		 $scope.clienteEdit = {};
                  		 form.$setPristine(true); 
               		 }else{
               			toaster.pop("error", "Cliente" ,resp.mensaje );
                  			 
               		 } 
                	 
                		
            		})
                	
                }
                
                $scope.cerrarCliente=function(){
                	$scope.editaCita=true;
                }
                
            	$scope.cargarClientes=function(){
            		
            		clientesFactory.listaClientes().then(function(r){
            			$scope.clientes=r;
            			$scope.cargarServicios();
            			
            		})
                	
                };
                
                $scope.cargarServicios=function(){
                	
                	serviciosFactory.listaServicios().then(function(r){
            			$scope.servicios=r;
            			
            		})
                	
                };

                $scope.$watch('servicio.selected', function() {
                
                	
                	$scope.cargarDatosColaborador();
                	
                	
                });
                
                $scope.cargarDatosColaborador=function(){
                	$scope.colaborador={selected:null};
                	$scope.colaboradores=[];
                	if ($scope.servicio.selected!=null){
                		$scope.cargarColaboradores($scope.servicio.selected)
                	}
                }
                
                $scope.cargarDatosColaboradorSucursal=function(){
                	$scope.colaborador={selected:null};
                	$scope.colaboradores=[];
                	$scope.cargarColaboradoresSucursal();
                }
                
                
                $scope.cargarColaboradores=function(servicio){
                	if (servicio!=null)
                	colaboradoresFactory.listaColaboradoresPorServicio(servicio.codigo).then(function(r){
            			$scope.colaboradores=r;
            			
            		})
                	
                };
                
                $scope.cargarColaboradoresSucursal=function(){
                	
                	colaboradoresFactory.listaColaboradores().then(function(r){
            			$scope.colaboradores=r;
            			
            		})
                	
                };

                $scope.$watch('colaborador.selected', function() {
                    
                    	
                      	
                });
                
                $scope.datepickerOptions = {
                		showWeeks : false,
                		startingDay : 1
                };
                
                $scope.abrirCalendario = function() {
                	console.log($scope.opened);
            		$scope.opened = !$scope.opened;
            		console.log($scope.opened);
            		
            	};
                
                $scope.$watch('cita.fecha', function() {
                	$scope.cargarDisponibilidad();
                });
                
                $scope.cargarDisponibilidad=function(){
                	$scope.cita.codigoDiaSucursalHora=undefined;
                    
                    if ($scope.cita.fecha!=null && $scope.colaborador.selected!=null){
                    	console.log("buscando Horario...");
                    	$scope.cargarCitas($scope.sucursalEdit.codigo,$scope.colaborador.selected.codigo,$scope.cita.fecha);
                    }
                };
                
                $scope.cargarCitas=function(sucursal,colaborador,fecha){
                	
                	if (fecha!=null && sucursal!=null)
                		citasFactory.listaDisponibilidad(colaborador,sucursal,formatDate(fecha)).then(function(r){
                		$scope.disponibilidades=r;
            		
                		})
                	
                };
                
                $scope.todasHorasSucursal=function(){
                	$scope.cita.codigoDiaSucursalHora=undefined;
                	
                	if ($scope.cita.fecha!=null ){
                    	console.log("buscando Horario...");
                    	$scope.cargarDisponibilidadSucursal($scope.sucursalEdit.codigo,$scope.cita.fecha);
                    }
                }
                
                $scope.cargarDisponibilidadSucursal=function(sucursal,fecha){
                	
                	if (fecha!=null && sucursal!=null)
                		citasFactory.listaDisponibilidadSucursal(sucursal,formatDate(fecha)).then(function(r){
                		$scope.disponibilidades=r;
            			            			
                		})
                	
                };
                
                
                function formatDate(date) {
                    var d = new Date(date),
                        month = '' + (d.getMonth() + 1),
                        day = '' + d.getDate(),
                        year = d.getFullYear();

                    if (month.length < 2) month = '0' + month;
                    if (day.length < 2) day = '0' + day;

                    return [year, month, day].join('-');
                }
                	
                
                $scope.crearCita=function(){
                
                	if ($scope.validarCita()){
                		var fecha=$scope.cita.fecha;
                			
                    	$scope.eventoDto={codigoServicio:$scope.servicio.selected.codigo,
                    			          codigoDiaSucursalHora:$scope.cita.codigoDiaSucursalHora.codigo,
                    			          fechaCita:formatDate(fecha),
                    			          codigoCliente:$scope.cliente.selected.codigo,
                    			          codigoColaborador:$scope.colaborador.selected.codigo,
                    			          notas:$scope.cita.notas
                    			       	};
                    	
                    	
                    	citasFactory.crearCita($scope.eventoDto).then(function(resp){
                    		
                    		
                    		if (resp.estado){
                    			$uibModalInstance.close();
                    			 toaster.pop("success", "Cita" ,"La cita se registro correctamente, se le notificó al cliente por SMS y EMAIL" );
                    		}else{
                    			 toaster.pop("error", "Cita" ,resp.mensaje );
                                 
                    		}
                    		
                			            			
                    	})
                	}
                }
               
               
                
                $scope.validarCita=function(){
                	
                	if ($scope.cliente.selected==null){
                
                		 toaster.pop("error", "Cita" ,"No ha seleccionado ningún cliente" );
                         return false;
                	}
                	if ($scope.servicio.selected==null){
                        
               		 toaster.pop("error", "Cita" ,"No ha seleccionado ningún servicio" );
                        return false;
                	}
                	if ($scope.colaborador.selected==null){
                        
                  		 toaster.pop("error", "Cita" ,"No ha seleccionado ningún especialista/colaborador" );
                           return false;
                   	}
                	if ($scope.cita.fecha==null){
                        
                  		 toaster.pop("error", "Cita" ,"No ha seleccionado ninguna fecha" );
                           return false;
                   	}
                	
                	if ($scope.cita.codigoDiaSucursalHora==null){
                        
                 		 toaster.pop("error", "Cita" ,"No ha seleccionado el horario de la cita" );
                          return false;
                  	}
               	
                	return true;
                }
                
               
                $scope.cancelarCita = function () {
                    $uibModalInstance.dismiss('cancelar');
                };
                
               
			
				

            }
        });
                	
        modalInstance.result.then(function (selectedEvent, action) {
        	$scope.liberarCitas();
        });
    }

    

}]);

app.controller('ModalInstanceCtrl', ["$scope", "$uibModalInstance", "items", function ($scope, $uibModalInstance, items) {

    $scope.items = items;
   

    $scope.ok = function () {
        //$uibModalInstance.close($scope.selected.item);
        $uibModalInstance.close();
        
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}]);
