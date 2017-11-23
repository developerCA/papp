app.factory("fuerzaFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/administrar");

	return {
		
		
		traerFuerzas : function(pagina) {
			  
			  return Restangular.allUrl("administrar/consultar/fuerza/pagina="+pagina).getList();
			  
		},
		
		traerFuerzasFiltro : function(pagina,nombre,codigo,sigla,estado) {
			  
			var url = "administrar/consultar/fuerza/pagina="+pagina;
          
			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;
			if(sigla!=null && sigla != "") url += "&sigla=" + sigla;
			if(estado!=null && estado != "" ) url += "&estado=" + estado;
					 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerFuerza : function(id) {
			  
			var url = "administrar/fuerza/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "administrar/fuerza/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
		
		
	
	}
} ]);