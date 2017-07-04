app.factory("sucursalesFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/sucursales");

	return {

		listaSucursales : function() {
			return service.getList();
		},
		
		nuevo: function() {
        	
            return Restangular.allUrl("sucursales/nuevo").customPUT();
        },
        registrar: function(dto) {
            return Restangular.allUrl("sucursales/registro").customPOST(dto);
        },
        

		listaDias : function(codigoSucursal) {
			return Restangular.allUrl("sucursales/dias").getList({
				codigoSucursal : codigoSucursal
			});

		},
		
		listaHoras: function(codigoDiaSucursal) {
     		
          	 return Restangular.allUrl("sucursales/horas").getList({codigoDiaSucursal:codigoDiaSucursal});
        },
        
        registarHoras: function(registroHoras) {
       	 
            return Restangular.allUrl("sucursales/registrarHoras").customPUT(registroHoras);
        },
		
		buscar: function(codigoSucursal) {
	            return Restangular.allUrl("sucursales/buscar").customPUT(codigoSucursal);
	    },
	    eliminar: function(codigoSucursal) {
        	
            return Restangular.allUrl("sucursales/eliminar").customPUT(codigoSucursal);
        },
	        

	}
} ]);