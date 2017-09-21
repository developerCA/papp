app.factory("gradoFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/administrar");

	return {
		
		
		traerGrados : function(pagina) {
			  
			  return Restangular.allUrl("administrar/consultar/grado/pagina="+pagina).getList();
			  
		},
		
		traerGradosFiltro : function(pagina,nombre,sigla,grupo,estado) {
			  
			var url = "administrar/consultar/grado/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if(sigla!=null && sigla != "") url += "&gruposigla=" + sigla;	
			if(grupo!=null && grupo != "") url += "&nombregrupo=" + grupo;	
			if(estado!=null && estado != "" ) url += "&estado=" + estado;			
			alert(url);
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerGrado : function(id) {
			  
			var url = "administrar/grado/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "administrar/grado/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
			
	}
} ]);