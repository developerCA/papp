app.factory("ejecucionMetasFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {
        
        traerFiltro: function (pagina, ejercicio, codigo, nombre, estado) {
            var url = "planificacion/consultar/planificacion/pagina=" + pagina+ "&ejerciciofiscal=" + ejercicio;

            if (codigo != null && codigo != "") url += "&codigopresup=" + codigo.toUpperCase();
            if (nombre != null && nombre != "") url += "&nombre=" + nombre;
            if (estado != null && estado != "") url += "&estado=" + estado;

            return Restangular.allUrl(url).getList();
        },
		
		traerEditar : function(id) {
			var url = "administrar/ejecucionMetas/"+id+"/-1";

			return Restangular.allUrl(url).customGET();
		},

		guardar:function(objeto){
			var url = "administrar/ejecucionMetas/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

	}
} ]);