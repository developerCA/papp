app.factory("formulacionEstrategicaFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {
		traerFormulacionEstrategica : function(pagina, ejercicio) {
			var url = "planificacion/consultar/programa/pagina=" + pagina + "&programaejerciciofiscalid=" + ejercicio;
			return Restangular.allUrl(url).getList();
		},

		traerFormulacionEstrategicaHijos : function(pagina, ejercicio, padre) {
			var url = "planificacion/consultar/programa/" +
					"pagina=" + pagina +
					"&programaejerciciofiscalid=" + ejercicio +
					"&id=" + padre;
			return Restangular.allUrl(url).getList();
		},

		traerFormulacionEstrategicaFiltro : function(pagina,nombre) {
			var url = "planificacion/consultar/programa/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	

			return Restangular.allUrl(url).getList();
		},

		traerFormulacionEstrategicaEditar : function(id) {
			var url = "planificacion/programa/"+id+"/0";

		    return Restangular.allUrl(url).customGET();
		},

		traerFormulacionEstrategicaNuevoEstructura : function(padreid, ejerciciofiscalid, tipopadre) {
			var url = "planificacion/nuevo/programa/"+padreid+"/"+ejerciciofiscalid+"/"+tipopadre;

		    return Restangular.allUrl(url).customGET();
		},

		guardar:function(objeto){
			var url = "planificacion/programa/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
	}
} ]);
