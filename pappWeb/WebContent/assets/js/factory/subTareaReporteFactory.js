app.factory("subTareaReporteFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {
		traerFiltro : function(
			pagina,
			ejerciciofiscal,
			nivelactividadpadreid,
			nivelactividadunidadid,
			estado
		) {
			var url = "planificacion/consultar/nivelactividadid/" +
				"pagina="+pagina+
				"&tipo=ST" +
				"&filas=10";

			if(ejerciciofiscal!=null && ejerciciofiscal != "") url += "&nivelactividadejerfiscalid=" + ejerciciofiscal;	
			if(nivelactividadpadreid!=null && nivelactividadpadreid != "") url += "&nivelactividadpadreid=" + nivelactividadpadreid;
			if(nivelactividadunidadid!=null && nivelactividadunidadid != "") url += "&nivelactividadunidadid=" + nivelactividadunidadid;
			if(estado!=null && estado != "") url += "&estado=" + estado;
			return Restangular.allUrl(url).getList();
		},
	}
} ]);