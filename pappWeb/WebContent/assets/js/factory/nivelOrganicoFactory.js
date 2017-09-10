app.factory("nivelOrganicoFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/administrar");

	return {
		
		
		traerNiveles : function(pagina) {
			  
			  return Restangular.allUrl("administrar/consultar/nivelorganico/pagina="+pagina).getList();
			  
		},
		
		traerNivelesFiltro : function(pagina,nombre,codigo,estado) {
			  
			var url = "administrar/consultar/nivelorganico/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;	
			if(estado!=null && estado != "" ) url += "&estado=" + estado;
			 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerNivel : function(id) {
			  
			var url = "administrar/nivelorganico/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "administrar/nivelorganico/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
		
		
	
	}
} ]);