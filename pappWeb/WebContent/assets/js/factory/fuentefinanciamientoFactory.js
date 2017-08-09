app.factory("fuenteFinanciamientoFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/administrar");

	return {
		
		
		traerFuentes : function(pagina) {
			  
			  return Restangular.allUrl("administrar/consultar/fuentefinanciamiento/pagina="+pagina).getList();
			  
		},
		
		traerFuentesFiltro : function(pagina,nombre,codigo,estado) {
			  
			var url = "administrar/consultar/fuentefinanciamiento/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if(codigo!=null && codigo != "") url += "&prefijo=" + codigo;	
			if(estado!=null && estado != "" ) url += "&estado=" + estado;
			 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerFuente : function(id) {
			  
			var url = "administrar/fuentefinanciamiento/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "administrar/fuentefinanciamiento/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
		
		
	
	}
} ]);