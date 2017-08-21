app.factory("claseRegistroFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/administrar");

	return {
		
		
		traerClases : function(pagina) {
			  
			  return Restangular.allUrl("administrar/consultar/claseregistro/pagina="+pagina).getList();
			  
		},
		
		traerClasesFiltro : function(pagina,nombre,codigo,estado) {
			  
			var url = "administrar/consultar/claseregistro/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;	
			if(estado!=null && estado != "" ) url += "&estado=" + estado;
			 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerClase : function(id) {
			  
			var url = "administrar/claseregistro/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "administrar/claseregistro/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
		
		
	
	}
} ]);