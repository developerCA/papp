app.factory("certificacionesFondosFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/ejecucion");

	return {
		traer: function(
			pagina,
			ejefiscal
		) {
			var url = "ejecucion/consultar/certificacion/" +
				"filas=10" +
				"&pagina=" + pagina +
				"&certificacionejerfiscalid=" + ejefiscal;
			return Restangular.allUrl(url).customGET();
		},

		traerFiltro: function(
			pagina,
			ejefiscal,
			codigo,
			precompromiso,
			valorinicial,
			valorfinal,
			fechainicial,
			fechafinal,
			estado
		) {
			var url = "ejecucion/consultar/certificacion/" +
				"filas=10" +
				"&pagina=" + pagina +
				"&certificacionejerfiscalid=" + ejefiscal;

			if(codigo != null && codigo != "") url += "&codigo=" + codigo;	
			if(precompromiso != null && precompromiso != "") url += "&numprecompromiso=" + precompromiso;	
			if(valorinicial!= null && valorinicial != "") url += "&valorinicial=" + valorinicial;	
			if(valorfinal != null && valorfinal != "") url += "&valorfinal=" + valorfinal;	
			if(fechainicial != null && fechainicial != "") url += "&fechainicial=" + encodeURIComponent(fechainicial);	
			if(fechafinal != null && fechafinal != "") url += "&fechafinal=" + encodeURIComponent(fechafinal);	
			if(estado != null && estado != "") url += "&estado=" + estado;	

			return Restangular.allUrl(url).customGET();
		},

		traerCertificacionesFondosNuevo: function(
			ejefiscal
		) {
			var url = "ejecucion/nuevo/certificacion/" + ejefiscal;
		    return Restangular.allUrl(url).customGET();
		},

		traerCertificacionesFondosEditar: function(
			id
		) {
			var url = "ejecucion/certificacion/"+id+"/-1";
		    return Restangular.allUrl(url).customGET();
		},

		guardar:function(
			objeto
		){
			var url = "ejecucion/certificacion/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

		solicitar:function(
			id
		){
			var url = "ejecucion/flujo/" + id + "/SO";
			return Restangular.allUrl(url).customGET();
		},

		liquidarManualMente:function(
			id,
			motivo
		){
			var url = "ejecucion/flujo/" + id + "/SO";
			return Restangular.allUrl(url).customGET();
		},

		eliminar:function(
			id
		){
			var url = "ejecucion/flujo/" + id + "/EL";
			return Restangular.allUrl(url).customGET();
		},
	}
} ]);