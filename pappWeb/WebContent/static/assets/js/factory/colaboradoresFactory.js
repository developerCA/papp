app.factory("colaboradoresFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/colaboradores");

	return {

		listaColaboradores: function() {
            return service.getList();
        },
        
        listaColaboradoresPorServicio: function(servicio) {
        	 return Restangular.allUrl("colaboradores/servicio").getList({servicio:servicio});
        	   
        },
        
        registrar: function(dto) {
            return Restangular.allUrl("colaboradores/registro").customPOST(dto);
        },
        
        buscar: function(codigoColaborador) {
        	
            return Restangular.allUrl("colaboradores/buscar").customPUT(codigoColaborador);
        },
        eliminar: function(codigoColaborador) {
        	
            return Restangular.allUrl("colaboradores/eliminar").customPUT(codigoColaborador);
        },
        registarHoras: function(registroHoras) {
	 
            return Restangular.allUrl("colaboradores/registrarHoras").customPUT(registroHoras);
        },
        nuevo: function() {
        	
            return Restangular.allUrl("colaboradores/nuevo").customPUT();
        },
        listaSucursalesPorColaborador: function(codigoColaborador) {
       	 return Restangular.allUrl("colaboradores/sucursales").getList({codigoColaborador:codigoColaborador});
        },
        listaHorasPorColaborador: function(codigoDiaSucursal,codigoSucursalColaborador) {
        		
          	 return Restangular.allUrl("colaboradores/horas").getList({codigoDiaSucursal:codigoDiaSucursal,codigoSucursalColaborador:codigoSucursalColaborador});
        },
           
        
	}
} ]);