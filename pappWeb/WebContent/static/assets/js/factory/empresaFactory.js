app.factory("empresaFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/empresa");

	return {

		lista: function() {
            return service.getList();
        },
        nuevo: function() {
        	
            return Restangular.allUrl("empresa/nuevo").customPUT();
        },
        registrar: function(empresa) {
            return Restangular.allUrl("empresa/registro").customPOST(empresa);
        },
        buscar: function(codigoEmpresa) {
        	
            return Restangular.allUrl("empresa/buscar").customPUT(codigoEmpresa);
        },
        eliminar: function(codigoEmpresa) {
        	
            return Restangular.allUrl("empresa/eliminar").customPUT(codigoEmpresa);
        },
       
	}
} ]);