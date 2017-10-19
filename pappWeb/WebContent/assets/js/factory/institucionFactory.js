app.factory("institucionFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/administrar");

	return {
		
		
		traerInstitucion : function(pagina) {
			  
			  return Restangular.allUrl("administrar/consultar/institucion/pagina="+pagina).getList();
			  
		},
		
		traerInstitucionFiltro : function(pagina,nombre) {
			  
			var url = "administrar/consultar/institucion/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerInstitucion : function(id) {
			  
			var url = "administrar/institucion/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "administrar/institucion/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

	}
} ]);