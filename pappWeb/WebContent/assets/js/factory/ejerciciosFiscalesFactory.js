app.factory("ejercicioFiscalFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/administrar");

	return {
		
		
		traerEjercicios : function(pagina) {
			  
			  return Restangular.allUrl("administrar/consultar/ejerciciofiscal/pagina="+pagina).getList();
			  
		},
		
		traerEjerciciosFiltro : function(pagina,anio,estado) {
			  
			var url = "administrar/consultar/ejerciciofiscal/pagina="+pagina;

			if(anio!=null && anio != "") url += "&anio=" + anio;			
			if(estado!=null && estado != "" ) url += "&estado=" + estado;
			 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerEjerciciosActivos : function(pagina,estado) {
			  
			var url = "administrar/consultar/ejerciciofiscal/pagina="+pagina;

			if(estado!=null && estado != "" ) url += "&estado=" + estado;
			 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerEjercicio : function(id) {
			  
			var url = "administrar/ejerciciofiscal/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardarEjercicio:function(objeto){
			var url = "administrar/ejerciciofiscal/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
		
		
	
	}
} ]);