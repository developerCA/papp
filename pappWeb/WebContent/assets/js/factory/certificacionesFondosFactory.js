app.factory("certificacionesFondosFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/ejecucion");

	return {
		traerCertificacionesFondos: function(
			pagina,
			ejefiscal
		) {
			var url = "ejecucion/consultar/certificacion/" +
				"pagina=" + pagina +
				"&certificacionejerfiscalid=" + ejefiscal;
			//console.log(ejefiscal);
			//console.log(url);
			return Restangular.allUrl(url).getList();
		},

		traerCertificacionesFondosFiltro: function(
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
				"pagina=" + pagina +
				"&certificacionejerfiscalid=" + ejefiscal;

			if(codigo != null && codigo != "") url += "&codigo=" + codigo;	
			if(precompromiso != null && precompromiso != "") url += "&numprecompromiso=" + precompromiso;	
			if(valorinicial!= null && valorinicial != "") url += "&valorinicial=" + valorinicial;	
			if(valorfinal != null && valorfinal != "") url += "&valorfinal=" + valorfinal;	
			if(fechainicial != null && fechainicial != "") url += "&fechainicial=" + fechainicial;	
			if(fechafinal != null && fechafinal != "") url += "&fechafinal=" + fechafinal;	
			if(estado != null && estado != "") url += "&estado=" + estado;	

			return Restangular.allUrl(url).getList();
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
	}
} ]);