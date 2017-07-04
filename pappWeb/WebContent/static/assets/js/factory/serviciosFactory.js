app.factory("serviciosFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/servicios");

	return {

		listaServicios: function() {
            return service.getList();
        },
        nuevo: function() {
        	
            return Restangular.allUrl("servicios/nuevo").customPUT();
        },
        registrar: function(servicio) {
            return Restangular.allUrl("servicios/registro").customPOST(servicio);
        },
        buscar: function(codigoServicio) {
        	
            return Restangular.allUrl("servicios/buscar").customPUT(codigoServicio);
        },
        eliminar: function(codigoServicio) {
        	
            return Restangular.allUrl("servicios/eliminar").customPUT(codigoServicio);
        },
       
	}
} ]);