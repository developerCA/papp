app.factory("indicadoresFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {
		traerIndicadores : function(pagina, ejercicio) {
			var url = "planificacion/consultar/consultaBusquedaIndicador/pagina=" + pagina + "&indicadorejerciciofiscalid=" + ejercicio;
			return Restangular.allUrl(url).getList();
		},

		traerIndicadoresFiltro : function(pagina, ejercicio, codigo, nombre) {
			var url = "planificacion/consultar/consultaBusquedaIndicador/pagina="+pagina + "&indicadorejerciciofiscalid=" + ejercicio;

			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;	
			if(nombre!=null && nombre != "") url += "&descripcion=" + nombre;	

			return Restangular.allUrl(url).getList();
		},

		traerIndicadoresEditar : function(id) {
			var url = "planificacion/consultaBusquedaIndicador/"+id+"/0";

		    return Restangular.allUrl(url).customGET();
		},

		guardar:function(objeto){
			var url = "planificacion/consultaBusquedaIndicador/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
	}
} ]);
