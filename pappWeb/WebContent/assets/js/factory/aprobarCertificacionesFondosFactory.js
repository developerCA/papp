app.factory("aprobarCertificacionesFondosFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/ejecucion");

	return {
		traer: function(
			pagina,
			ejefiscal
		) {
			var url = "ejecucion/consultar/ordengasto/" +
				"pagina=" + pagina +
				"&ordengastoejerfiscalid=" + ejefiscal;
			//console.log(ejefiscal);
			//console.log(url);
			return Restangular.allUrl(url).getList();
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
			var url = "ejecucion/consultar/ordengasto/" +
				"pagina=" + pagina +
				"&ordengastoejerfiscalid=" + ejefiscal;

			if(codigo != null && codigo != "") url += "&codigo=" + codigo;	
			if(precompromiso != null && precompromiso != "") url += "&numprecompromiso=" + precompromiso;	
			if(valorinicial!= null && valorinicial != "") url += "&valorinicial=" + valorinicial;	
			if(valorfinal != null && valorfinal != "") url += "&valorfinal=" + valorfinal;	
			if(fechainicial != null && fechainicial != "") url += "&fechainicial=" + fechainicial;	
			if(fechafinal != null && fechafinal != "") url += "&fechafinal=" + fechafinal;	
			if(estado != null && estado != "") url += "&estado=" + estado;	

			return Restangular.allUrl(url).getList();
		},

		traerNuevo: function(
			ejefiscal
		) {
			var url = "ejecucion/nuevo/ordengasto/" + ejefiscal;
		    return Restangular.allUrl(url).customGET();
		},

		traerEditar: function(
			id
		) {
			var url = "ejecucion/ordengasto/"+id+"/-1";
		    return Restangular.allUrl(url).customGET();
		},

		guardar:function(
			objeto
		){
			var url = "ejecucion/ordengasto/";
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