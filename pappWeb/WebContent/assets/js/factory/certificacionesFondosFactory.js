app.factory("certificacionesFondosFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/ejecucion");

	return {
		
		
		traerCertificacionesFondos : function(pagina) {
			  return Restangular.allUrl("ejecucion/consultar/certificacion/pagina="+pagina).getList();
		},
		
		traerCertificacionesFondosFiltro : function(pagina,codigo,precompromiso,valorinicial,valorfinal,fechainicial,fechafinal,estado) {
			  
			var url = "ejecucion/consultar/certificacion/pagina="+pagina;

			if(codigo != null && codigo != "") url += "&codigo=" + codigo;	
			if(precompromiso != null && precompromiso != "") url += "&numprecompromiso=" + precompromiso;	
			if(valorinicial!= null && valorinicial != "") url += "&valorinicial=" + valorinicial;	
			if(valorfinal != null && valorfinal != "") url += "&valorfinal=" + valorfinal;	
			if(fechainicial != null && fechainicial != "") url += "&fechainicial=" + fechainicial;	
			if(fechafinal != null && fechafinal != "") url += "&fechafinal=" + fechafinal;	
			if(estado != null && estado != "") url += "&estado=" + estado;	
			 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerCertificacionesFondosEditar : function(id) {
			  
			var url = "ejecucion/certificacion/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "ejecucion/certificacion/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

	}
} ]);