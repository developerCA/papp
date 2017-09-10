app.factory("tipoDocumentoFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/administrar");

	return {
		
		
		traerTipos : function(pagina,ejercicio) {
			 
			  return Restangular.allUrl("administrar/consultar/tipodocumento/pagina="+pagina+"&ejerciciofiscalid="+ejercicio).getList();
			  
		},
		
		traerTiposFiltro : function(pagina,ejercicio,nombre,codigo,estado) {
			  
			var url = "administrar/consultar/tipodocumento/pagina="+pagina+"&ejerciciofiscalid="+ejercicio;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;	
			if(estado!=null && estado != "" ) url += "&estado=" + estado;
			 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerTipo : function(id) {
			  
			var url = "administrar/tipodocumento/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "administrar/tipodocumento/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
		
		
	
	}
} ]);