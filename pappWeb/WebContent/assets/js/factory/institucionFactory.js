app.factory("institucionFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/administrar");

	return {

		traerInstitucion : function(pagina, ejercicio) {
			return Restangular.allUrl(
				"administrar/consultar/institucion/" +
				"pagina="+pagina+
				"&institucionejerciciofiscalid="+ejercicio
				//"&estado=A"
			).getList();
		},

		traerInstitucionFiltro : function(pagina, ejercicio, codigo, nombre, estado) {
			var url = "administrar/consultar/institucion/pagina="+pagina+"&institucionejerciciofiscalid="+ejercicio;

			if (codigo!=null && codigo != "") url += "&codigo=" + codigo;	
			if (nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if (estado!=null && estado != "") url += "&estado=" + estado;
			//else url += "&estado=A"

			return Restangular.allUrl(url).getList();
		},
		
		traerInstitucionEditar : function(id) {
			var url = "administrar/institucion/"+id+"/-1";

			return Restangular.allUrl(url).customGET();
		},

		guardar:function(objeto){
			var url = "administrar/institucion/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

	}
} ]);