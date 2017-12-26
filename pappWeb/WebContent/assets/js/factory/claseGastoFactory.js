app.factory("claseGastoFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/administrar");

	return {
		traerClases: function(
			pagina,
			ejercicio
		) {
			var url = "administrar/consultar/claseregistrocmcgasto/" +
				"pagina=" + pagina +"&" +
				"ejerciciofiscalid=" + ejercicio;

			return Restangular.allUrl(url).getList();
		},

		traerClasesFiltro: function(
			pagina,
			ejercicio,
			gastocodigo,
			gastonombre,
			registrocodigo,
			registronombre,
			modificacioncodigo,
			modificacionnombre
		) {
			var url = "administrar/consultar/claseregistrocmcgasto/" +
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