app.factory("MetasFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {

		traerMetas : function(pagina, ejercicio) {
			return Restangular.allUrl("planificacion/consultar/metas/pagina="+pagina+"&plannacionalejerfiscalid="+ejercicio).getList();
		},

		traerMetasFiltro : function(pagina, ejercicio, codigo, nombre, estado) {
			var url = "planificacion/consultar/metas/pagina="+pagina+"&plannacionalejerfiscalid="+ejercicio;

			if (codigo!=null && codigo != "") url += "&codigo=" + codigo;
			if (nombre!=null && nombre != "") url += "&descripcion=" + nombre;
			if (estado!=null && estado != "") url += "&estado=" + estado;

			return Restangular.allUrl(url).getList();
		},
		
		traerMetasEditar : function(id) {
			var url = "planificacion/metas/"+id+"/-1";

			return Restangular.allUrl(url).customGET();
		},

		guardar:function(objeto){
			var url = "planificacion/metas/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

	}
} ]);