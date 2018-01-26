app.factory("MetasFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {

		traerMetas : function(pagina, ejercicio) {
			return Restangular.allUrl("planificacion/consultar/metas/pagina="+pagina+"&tipo=P&plannacionalejerfiscalid="+ejercicio).getList();
		},

		traerMetasFiltro : function(pagina, ejercicio, codigo, descripcion, codigop, descripcionp, estado) {
			var url = "planificacion/consultar/metas/pagina="+pagina+"&tipo=P&plannacionalejerfiscalid="+ejercicio;

			if (codigo!=null && codigo != "") url += "&codigo=" + codigo;
			if (descripcion!=null && descripcion != "") url += "&descripcion=" + descripcion;
			if (codigop!=null && codigop != "") url += "&codigopadre=" + codigop;
			if (descripcionp!=null && descripcionp != "") url += "&nombrepadre=" + descripcionp;
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