app.factory("clientesFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/clientes");

	return {

		listaClientes: function() {
            return service.getList();
        },
        nuevo: function() {
        	
            return Restangular.allUrl("clientes/nuevo").customPUT();
        },
        registrar: function(cliente) {
            return Restangular.allUrl("clientes/registro").customPOST(cliente);
        },
       
	}
} ]);