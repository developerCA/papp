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

		traerSubActividad : function(
			npNivelid,
			ejercicio
		) {
			var url = "planificacion/consultar/subactividad/" +
				"padre=" + npNivelid  +
				"&subactividadejerfiscalid=" + ejercicio;
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

		traerSubActividadEditar : function(id) {
			var url = "planificacion/subactividad/"+id+"/0";

		    return Restangular.allUrl(url).customGET();
		},

		traerProgramaNuevo : function(padreid, ejerciciofiscalid) {
			var url = "planificacion/nuevo/programa/"+padreid+"/"+ejerciciofiscalid+"/0";
			//console.log(url);
		    return Restangular.allUrl(url).customGET();
		},

		traerSubProgramaNuevo : function(padreid, ejerciciofiscalid) {
			var url = "planificacion/nuevo/subprograma/"+padreid+"/"+ejerciciofiscalid+"/0";
			//console.log(url);
		    return Restangular.allUrl(url).customGET();
		},

		traerProyectoNuevo : function(padreid, ejerciciofiscalid) {
			var url = "planificacion/nuevo/proyecto/"+padreid+"/"+ejerciciofiscalid+"/0";
			//console.log(url);
		    return Restangular.allUrl(url).customGET();
		},

		traerActividadNuevo : function(padreid, ejerciciofiscalid) {
			var url = "planificacion/nuevo/actividad/"+padreid+"/"+ejerciciofiscalid+"/0";
			//console.log(url);
		    return Restangular.allUrl(url).customGET();
		},

		traerSubActividadNuevo : function(padreid, ejerciciofiscalid) {
			var url = "planificacion/nuevo/subactividad/"+padreid+"/"+ejerciciofiscalid+"/0";
			//console.log(url);
		    return Restangular.allUrl(url).customGET();
		},

		guardar:function(tipo, objeto){
			var url = "planificacion/" + this.obtenerTipo(tipo) + "/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
		
		obtenerTipo: function(tipo) {
        	switch (tipo) {
				case "PR":
		        	return "programa";
				case "SP":
		        	return "subprograma";
				case "PY":
		        	return "proyectometa";
				case "AC":
		        	return "actividad";
				case "SA":
		        	return "subactividad";
				default:
			}
		}
	}
} ]);
