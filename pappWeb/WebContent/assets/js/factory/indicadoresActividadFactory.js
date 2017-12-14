app.factory("indicadoresActividadFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {
		traerIndicadoresActividad : function(pagina, ejercicio) {
			var url = "planificacion/consultar/consultaBusquedaIndicadoractividad/pagina=" + pagina + "&indicadorejerciciofiscalid=" + ejercicio;
			return Restangular.allUrl(url).getList();
		},

		traerIndicadoresActividadFiltro : function(pagina, ejercicio, codigo, nombre) {
			var url = "planificacion/consultar/consultaBusquedaIndicadoractividad/pagina="+pagina + "&indicadorejerciciofiscalid=" + ejercicio;

			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;	
			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	

			return Restangular.allUrl(url).getList();
		},
	}
} ]);
