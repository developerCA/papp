app.factory("cargoFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/administrar");

	return {
		
		
		traerCargos : function(pagina) {
			  
			  return Restangular.allUrl("administrar/consultar/cargo/pagina="+pagina).getList();
			  
		},
		
		traerCargosFiltro : function(pagina,nombre,codigo,estado) {
			  
			var url = "administrar/consultar/cargo/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;	
			if(estado!=null && estado != "" ) url += "&estado=" + estado;
			
			 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerCargo : function(id) {
			  
			var url = "administrar/cargo/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "administrar/cargo/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
		
		
	
	}
} ]);