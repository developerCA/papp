app.factory("ObjetivoOperacionalReporteFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {
		traerFiltro : function(
			pagina,
			ejerfiscalid,
			padre
		) {
			///planificacion/consultar/objetivo/objetivoejeerciciofisca={ejerciciofiscal}&estado=A&tipo=O
			var url = "planificacion/consultar/objetivo/estado=A&tipo=O&pagina="+pagina;

			if(ejerfiscalid!=null && ejerfiscalid != "") url += "&objetivoejeerciciofisca=" + ejerfiscalid;	
			if(padre!=null && padre != "") url += "&id=" + padre;

			return Restangular.allUrl(url).getList();
		},
	}
} ]);