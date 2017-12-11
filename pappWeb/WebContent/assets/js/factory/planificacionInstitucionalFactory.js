app.factory("planificacionInstitucionalFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {
		traerPlanificacionInstitucional : function(pagina, ejercicio) {
			var url = "planificacion/consultar/objetivo/pagina=" + pagina + "&objetivoejerciciofiscalid=" + ejercicio;
			return Restangular.allUrl(url).getList();
		},

		traerPlanificacionInstitucionalHijos : function(pagina, ejercicio, padre) {
			var url = "planificacion/consultar/objetivo/" +
					"pagina=" + pagina +
					"&objetivoejerciciofiscalid=" + ejercicio +
					"&id=" + padre;
			return Restangular.allUrl(url).getList();
		},

		traerPlanificacionInstitucionalFiltro : function(pagina,nombre) {
			var url = "planificacion/consultar/objetivo/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	

			return Restangular.allUrl(url).getList();
		},

		traerPlanificacionInstitucionalEditar : function(id) {
			var url = "planificacion/objetivo/"+id+"/0";

		    return Restangular.allUrl(url).customGET();
		},

		guardar:function(objeto){
			var url = "planificacion/objetivo/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
	}
} ]);
