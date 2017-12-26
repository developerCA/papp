app.factory("claseGastoFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/administracion");

	return {
		traerClases: function(
			pagina,
			ejercicio
		) {
			var url = "administracion/consultar/claseregistrocmcgasto/" +
				"pagina=" + pagina +"&" +
				"ejerciciofiscalid=" + ejercicio;

			return Restangular.allUrl(url).getList();
		},

		traerClasesFiltro: function(
			pagina,
			ejercicio,
			registrocodigo,
			registronombre,
			modificacioncodigo,
			modificacionnombre,
			gastocodigo,
			gastonombre
		) {
			var url = "administracion/consultar/claseregistrocmcgasto/" +
				"pagina=" + pagina +"&" +
				"ejerciciofiscalid=" + ejercicio;

			if (registrocodigo!=null && registrocodigo != "") url += "&registrocodigo=" + registrocodigo;	
			if (registronombre!=null && registronombre != "") url += "&registronombre=" + registronombre;	
			if (modificacioncodigo!=null && modificacioncodigo != "" ) url += "&modificacioncodigo=" + modificacioncodigo;
			if (modificacionnombre!=null && modificacionnombre != "") url += "&modificacionnombre=" + modificacionnombre;	
			if (gastocodigo!=null && gastocodigo != "") url += "&gastocodigo=" + gastocodigo;	
			if (gastonombre!=null && gastonombre != "" ) url += "&gastonombre=" + gastonombre;

			return Restangular.allUrl(url).getList();
		}
	}
} ]);