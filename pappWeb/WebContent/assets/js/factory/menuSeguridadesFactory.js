app.factory("menuSeguridadesFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/seguridad");

	return {
		
		
		traerMenus : function(pagina) {
			  
			  return Restangular.allUrl("seguridad/consultar/menu/pagina="+pagina).getList();
			  
		},
		
		traerMenusFiltro : function(pagina,nombre,orden) {
			  
			var url = "seguridad/consultar/menu/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if(orden!=null && orden != "") url += "&orden=" + orden;	
			 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerMenu : function(id) {
			  
			var url = "seguridad/menu/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "seguridad/menu/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
		
		eliminar:function(id){
			var url = "seguridad/menu/"+id+"/-1";
			console.log(url);
			return Restangular.allUrl(url).remove();
		},
		
	
	}
} ]);