app.factory("plannacionalFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/planificacion");

	return {
		
		
		traerInstitucion : function(pagina) {
			  
			  return Restangular.allUrl("planificacion/consultar/plannacional/pagina="+pagina).getList();
			  
		},
		
		traerInstitucionFiltro : function(pagina,nombre) {
			  
			var url = "planificacion/consultar/plannacional/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			 
			return Restangular.allUrl(url).getList();
			  
		},
		
		/* traerInstitucion : function(id) {
			  
			var url = "planificacion/plannacional/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		}, */
		
		guardar:function(objeto){
			var url = "planificacion/plannacional/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

	}
} ]);