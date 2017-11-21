app.factory("tipoRegimenFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/administrar");

	return {
		
		
		traerTiposRegimen : function(pagina) {
			  
			  return Restangular.allUrl("administrar/consultar/tiporegimen/pagina="+pagina).getList();
			  
		},
		
		traerTiposRegimenFiltro : function(pagina,nombre,estado) {
			  
			var url = "administrar/consultar/tiporegimen/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if(estado!=null && estado != "" ) url += "&estado=" + estado;
			 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerTipo : function(id) {
			  
			var url = "administrar/tiporegimen/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "administrar/tiporegimen/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
		
		eliminar: function (id) {
	            var url = "administrar/tiporegimen/"+id+"/-1";
	            return Restangular.allUrl(url).remove();
	    }
		
	
	}
} ]);