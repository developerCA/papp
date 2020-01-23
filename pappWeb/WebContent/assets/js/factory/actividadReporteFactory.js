app.factory("actividadReporteFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {
		traerActividadFiltro : function(
			pagina,
			ejerciciofiscal,
			nombre,
			npunidad,
			npprogramaid,
			npproyectoid
		) {
			var url = "planificacion/consultar/nivelactividad/tipo=AC&estado=A";

			if(ejerciciofiscal!=null && ejerciciofiscal != "") url += "&nivelactividadejerfiscalid=" + ejerciciofiscal;	
			if(npunidad!=null && npunidad != "") url += "&nivelactividadunidadid=" + npunidad;	
			return Restangular.allUrl(url).getList();
		},
//		traerActividadFiltro : function(
//				pagina,
//				ejerciciofiscal,
//				nombre,
//				npunidad,
//				npprogramaid,
//				npproyectoid
//			) {
//				var url = "planificacion/consultar/actividadreporte/pagina="+pagina;
//
//				if(ejerciciofiscal!=null && ejerciciofiscal != "") url += "&ejerciciofiscal=" + ejerciciofiscal;	
//				if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
//				if(npunidad!=null && npunidad != "") url += "&npunidad=" + npunidad;	
//				if(npprogramaid!=null && npprogramaid != "") url += "&npprogramaid=" + npprogramaid;	
//				if(npproyectoid!=null && npproyectoid != "") url += "&npproyectoid=" + npproyectoid;	
//				return Restangular.allUrl(url).getList();
//			},
		
		traerActividadEditar : function(id) {
			var url = "planificacion/actividadreporte/"+id+"/-1";

			return Restangular.allUrl(url).customGET();
		},

		guardar:function(objeto){
			var url = "planificacion/actividadreporte/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
	}
} ]);