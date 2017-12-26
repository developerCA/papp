app.factory("claseGastoFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/administrar");

	return {
		traerClases: function(
			pagina,
			ejercicio
		) {
			var url = "administrar/consultar/tipodocumentoclasedocumento/" +
				"pagina=" + pagina +"&" +
				"ejerciciofiscalid=" + ejercicio;

			return Restangular.allUrl(url).getList();
		},

		traerClasesFiltro: function(
			pagina,
			ejercicio,
			codigo,
			nombre,
			tipocodigo,
			tiponombre
		) {
			var url = "administrar/consultar/tipodocumentoclasedocumento/" +
				"pagina=" + pagina +"&" +
				"ejerciciofiscalid=" + ejercicio;

			if (codigo!=null && codigo != "") url += "&codigo=" + codigo;	
			if (nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if (tipocodigo!=null && tipocodigo != "") url += "&tipocodigo=" + tipocodigo;	
			if (tiponombre!=null && tiponombre != "" ) url += "&tiponombre=" + tiponombre;

			return Restangular.allUrl(url).getList();
		}
	}
} ]);