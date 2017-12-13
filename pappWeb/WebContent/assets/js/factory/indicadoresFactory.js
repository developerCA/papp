app.factory("indicadoresFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {
		traerIndicadores : function(pagina, ejercicio) {
			var url = "planificacion/consultar/consultaBusquedaIndicador/pagina=" + pagina + "&consultaBusquedaIndicador=" + ejercicio;
			return Restangular.allUrl(url).getList();
		},

		traerIndicadoresFiltro : function(pagina, ejercicio, codigo) {
			var url = "planificacion/consultar/consultaBusquedaIndicador/pagina="+pagina + "&consultaBusquedaIndicador=" + ejercicio;

			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;	

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
