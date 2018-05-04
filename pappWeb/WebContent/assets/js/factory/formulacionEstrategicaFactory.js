app.factory("formulacionEstrategicaFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {
		traer : function(pagina, ejercicio) {
			var url = "planificacion/consultar/programa/" +
				"pagina=" + pagina +
				"&programaejerciciofiscalid=" + ejercicio +
				"&estado=A";
			return Restangular.allUrl(url).getList();
		},

		traerSubPrograma : function(
			npNivelid,
			ejercicio
		) {
			var url = "planificacion/consultar/subprograma/" +
				"padre=" + npNivelid  +
				"&subprogramaejerciciofiscalid=" + ejercicio +
				"&estado=A";
			return Restangular.allUrl(url).getList();
		},

		traerProyecto : function(
			npNivelid,
			ejercicio
		) {
			var url = "planificacion/consultar/proyecto/" +
				"padre=" + npNivelid  +
				"&proyectoejerciciofiscalid=" + ejercicio +
				"&estado=A";
			return Restangular.allUrl(url).getList();
		},

		traerActividad : function(
			npNivelid,
			ejercicio
		) {
			var url = "planificacion/consultar/actividad/" +
				"padre=" + npNivelid  +
				"&actividadeejerciciofiscalid=" + ejercicio +
				"&estado=A";
			return Restangular.allUrl(url).getList();
		},

		traerSubActividad : function(
			npNivelid,
			ejercicio
		) {
			var url = "planificacion/consultar/subactividad/" +
				"padre=" + npNivelid  +
				"&subactividadejerfiscalid=" + ejercicio +
				"&estado=A";
			return Restangular.allUrl(url).getList();
		},

		traerFiltro : function(pagina,nombre) {
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

		traerNuevo: function(tipo, padreid, ejerciciofiscalid) {
			if (padreid === undefined) {
				padreid = 0;
			}
			var url = "planificacion/nuevo/" + this.obtenerTipo(tipo) + "/"+padreid+"/"+ejerciciofiscalid+"/0";
			//console.log(url);
		    return Restangular.allUrl(url).customGET();
		},

		eliminarActividadUnidad: function(id){
			var url = "planificacion/nivelactividad/" + id + "/0";
			return Restangular.allUrl(url).customDELETE();
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
		        	return "proyecto";
				case "AC":
		        	return "actividad";
				case "SA":
		        	return "subactividad";
				default:
			}
		}
	}
} ]);
