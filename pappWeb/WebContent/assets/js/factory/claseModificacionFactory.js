app.factory("claseModificacionFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/administrar");

	return {
		
		
		traerClases : function(pagina,ejercicio) {
			 
			  return Restangular.allUrl("administrar/consultar/clasemodificacion/pagina="+pagina+"&ejerciciofiscalid="+ejercicio).getList();
			  
		},
		
		traerClasesFiltro : function(pagina,ejercicio,nombre,codigo,estado) {
			  
			var url = "administrar/consultar/clasemodificacion/pagina="+pagina+"&ejerciciofiscalid="+ejercicio;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;	
			if(estado!=null && estado != "" ) url += "&estado=" + estado;
			 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerClase : function(id) {
			  
			var url = "administrar/clasemodificacion/"+id.registrocmid+"/"+id.registroid;
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "administrar/clasemodificacion/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
		
		
	
	}
} ]);