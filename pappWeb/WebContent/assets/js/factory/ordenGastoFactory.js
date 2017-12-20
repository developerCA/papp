app.factory("ordenGastoFactory", [ "Restangular",
function(Restangular) {

	var service = Restangular.service("/ejecucion");

	return {
		traer : function(
			pagina,
			ejefisca
		) {
			var url = "ejecucion/consultar/ordengasto/" +
				"pagina=" + pagina +
				"&ordengastoejerfiscalid=" + ejefisca;

			return Restangular.allUrl(url).getList();
		},
		
		traerFiltro : function(
			pagina,
			ejefisca,
			codigo,
			compromiso,
			certificacion,
			valorinicial,
			valorfinal,
			fechainicial,
			fechafinal,
			estado
		) {
			var url = "ejecucion/consultar/ordengasto/" +
				"pagina=" + pagina +
				"&ordengastoejerfiscalid=" + ejefisca;

			if(codigo != null && codigo != "") url += "&codigo=" + codigo;	
			if(compromiso != null && compromiso != "") url += "&compromiso=" + compromiso;	
			if(certificacion != null && certificacion != "") url += "&certificacion=" + certificacion;	
			if(valorinicial!= null && valorinicial != "") url += "&valorinicial=" + valorinicial;	
			if(valorfinal != null && valorfinal != "") url += "&valorfinal=" + valorfinal;	
			if(fechainicial != null && fechainicial != "") url += "&fechainicial=" + fechainicial;	
			if(fechafinal != null && fechafinal != "") url += "&fechafinal=" + fechafinal;	
			if(estado != null && estado != "") url += "&estado=" + estado;	

			//console.log(url);
			return Restangular.allUrl(url).getList();
		},

		nuevo : function(ejefisca) {
			var url = "ejecucion/nuevo/ordengasto/" + ejefisca;

		    return Restangular.allUrl(url).customGET();
		},

		editar : function(id) {
			var url = "ejecucion/ordengasto/"+id+"/0";

		    return Restangular.allUrl(url).customGET();
		},
		
		guardar:function(objeto){
			var url = "ejecucion/ordengasto/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

		eliminar:function(id){
			//var url = "ejecucion/ordengasto/";
			//return Restangular.allUrl(url).customPOST(objeto);
		},
	}
} ]);