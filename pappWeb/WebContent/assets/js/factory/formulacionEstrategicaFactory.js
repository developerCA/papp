app.factory("formulacionEstrategicaFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {
		traerFormulacionEstrategica : function(pagina, ejercicio) {
			var url = "planificacion/consultar/programa/pagina=" + pagina + "&programaejerciciofiscalid=" + ejercicio;
			return Restangular.allUrl(url).getList();
		},

		traerSubPrograma : function(
			npNivelid,
			ejercicio
		) {
			var url = "planificacion/consultar/subprograma/" +
				"padre=" + npNivelid  +
				"&subprogramaejerciciofiscalid=" + ejercicio;
			return Restangular.allUrl(url).getList();
		},

		traerProyecto : function(
			npNivelid,
			ejercicio
		) {
			var url = "planificacion/consultar/proyecto/" +
				"padre=" + npNivelid  +
				"&proyectoejerciciofiscalid =" + ejercicio;
			return Restangular.allUrl(url).getList();
		},

		traerActividad : function(
			npNivelid,
			ejercicio
		) {
			var url = "planificacion/consultar/actividad/" +
				"padre=" + npNivelid  +
				"&actividadeejerciciofiscalid =" + ejercicio;
			return Restangular.allUrl(url).getList();
		},

		traerFormulacionEstrategicaFiltro : function(pagina,nombre) {
			var url = "planificacion/consultar/programa/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	

			return Restangular.allUrl(url).getList();
		},

		traerProgramaEditar : function(node) {
			var url = "planificacion/programa/"+node.id+"/npnivelid="+node.npNivelid;

		    return Restangular.allUrl(url).customGET();
		},

		traerSubProgramaEditar : function(id) {
			var url = "planificacion/subprograma/"+id+"/0";

		    return Restangular.allUrl(url).customGET();
		},

		traerProyectoEditar : function(id) {
			var url = "planificacion/proyecto/"+id+"/0";

		    return Restangular.allUrl(url).customGET();
		},

		traerActividadEditar : function(id) {
			var url = "planificacion/actividad/"+id+"/0";

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
