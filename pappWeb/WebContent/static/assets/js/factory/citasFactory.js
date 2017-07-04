app.factory("citasFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/citas");

	return {

		listaDisponibilidad: function(colaborador,sucursal,fecha) {
            return service.getList({colaborador:colaborador,sucursal:sucursal,fecha:fecha});
        },   
        
        listaDisponibilidadSucursal : function(sucursal,fecha) {
			return Restangular.allUrl("citas/disponibilidadSucursal").getList({
				sucursal : sucursal,fecha:fecha
			});

		},
		
        listaCitas : function(sucursal,anio) {
			return Restangular.allUrl("citas/citasSucursal").getList({
				sucursal : sucursal,anio:anio
			});

		},
      
        crearCita: function(eventoDto){
            return service.post(eventoDto);
        },
        
        buscar: function(codigo) {
        	
            return Restangular.allUrl("citas/buscar").customPUT(codigo);
        },
        
        cancelar: function(codigo) {
        	
            return Restangular.allUrl("citas/cancelar").customPUT(codigo);
        },
     
        aprobar: function(codigo) {
        	
            return Restangular.allUrl("citas/aprobar").customPUT(codigo);
        },
     
	}
} ]);