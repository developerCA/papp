app.factory("clasificacionFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/administrar");

	return {
		
		
		traerClasificaciones : function(pagina) {
			  
			  return Restangular.allUrl("administrar/consultar/clasificacion/pagina="+pagina).getList();
			  
		},
		
		traerClasificacionesActivos : function(pagina,estado) {
			  
			var url = "administrar/consultar/clasificacion/pagina="+pagina;

			if(estado!=null && estado != "" ) url += "&estado=" + estado;
						 
			return Restangular.allUrl(url).getList();
			  
		},
		traerClasificacionesFiltro : function(pagina,codigo,nombre, sigla, estado) {
			  
			var url = "administrar/consultar/clasificacion/pagina="+pagina;

			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;
			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if(sigla!=null && sigla != "") url += "&sigla=" + sigla;
			if(estado!=null && estado != "" ) url += "&estado=" + estado;
						 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerClasificacion : function(id) {
			  
			var url = "administrar/clasificacion/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "administrar/clasificacion/";
			return Restangular.allUrl(url).customPOST(objeto);
		},			
	
	}
} ]);