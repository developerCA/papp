app.factory("ObjetivoEstrategicoReporteFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {
		traerFiltro : function(
			pagina,
			ejerfiscalid,
			padre
		) {
//			planificacion/consultar/objetivo/objetivoejeerciciofisca={ejerciciofiscal}&id={idpadre}&estado=A&tipo=E
			var url = "planificacion/consultar/objetivo/estado=A&tipo=E&pagina="+pagina;

			if(ejerfiscalid!=null && ejerfiscalid != "") url += "&objetivoejeerciciofisca=" + ejerfiscalid;
			if(padre!=null && padre != "") url += "&id=" + padre;

			return Restangular.allUrl(url).getList();
		},
	}
} ]);