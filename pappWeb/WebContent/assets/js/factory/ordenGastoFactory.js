app.factory("ordenGastoFactory", [ "Restangular",
function(Restangular) {

	var service = Restangular.service("/ejecucion");

	return {
		traerOrdenGasto : function(pagina) {
			  return Restangular.allUrl("ejecucion/consultar/ordengasto/pagina="+pagina).getList();
		},
		
		traerOrdenGastoFiltro : function(pagina,codigo,precompromiso,valorinicial,valorfinal,fechainicial,fechafinal,estado) {
			var url = "ejecucion/consultar/ordengasto/pagina="+pagina;

			if(codigo != null && codigo != "") url += "&codigo=" + codigo;	
			if(precompromiso != null && precompromiso != "") url += "&numprecompromiso=" + precompromiso;	
			if(valorinicial!= null && valorinicial != "") url += "&valorinicial=" + valorinicial;	
			if(valorfinal != null && valorfinal != "") url += "&valorfinal=" + valorfinal;	
			if(fechainicial != null && fechainicial != "") url += "&fechainicial=" + fechainicial;	
			if(fechafinal != null && fechafinal != "") url += "&fechafinal=" + fechafinal;	
			if(estado != null && estado != "") url += "&estado=" + estado;	
			 
			return Restangular.allUrl(url).getList();
		},
		
		traerOrdenGastoEditar : function(id) {
			var url = "ejecucion/ordengasto/"+id+"/-1";
		   
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