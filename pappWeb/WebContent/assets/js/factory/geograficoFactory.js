app.factory("geograficoFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/administrar");

	return {
		traerFiltro : function(
			pagina,
			ejerciciofiscal,
			codigo,
			nombre,
			estado
		) {
			var url = "administrar/consultar/divisiongeografica/" +
				"pagina="+pagina+
				"&filas=10";

			if(ejerciciofiscal!=null && ejerciciofiscal != "") url += "&institucionejerciciofiscalid=" + ejerciciofiscal;	
			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;
			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;
			if(estado!=null && estado != "") url += "&estado=" + estado;
			return Restangular.allUrl(url).getList();
		},
	}
} ]);