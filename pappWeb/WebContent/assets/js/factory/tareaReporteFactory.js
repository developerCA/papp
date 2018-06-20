app.factory("tareaReporteFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {
		traerFiltro : function(
			pagina,
			ejerciciofiscal,
			tipo,
			nivelactividadpadreid,
			nivelactividadunidadid,
			estado
		) {
			var url = "planificacion/consultar/nivelactividadid/" +
				"pagina="+pagina+
				"&tipo=TA" +
				"&filas=10";

			if(ejerciciofiscal!=null && ejerciciofiscal != "") url += "&nivelactividadejerfiscalid=" + ejerciciofiscal;	
			if(tipo!=null && tipo != "") url += "&tipo=" + tipo;
			if(nivelactividadpadreid!=null && nivelactividadpadreid != "") url += "&nivelactividadpadreid=" + nivelactividadpadreid;
			if(nivelactividadunidadid!=null && nivelactividadunidadid != "") url += "&nivelactividadunidadid=" + nivelactividadunidadid;
			if(estado!=null && estado != "") url += "&estado=" + estado;
			return Restangular.allUrl(url).getList();
		},
	}
} ]);