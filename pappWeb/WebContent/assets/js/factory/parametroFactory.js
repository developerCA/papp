app.factory("parametroFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/administrar");

	return {
		
		
		traerParametros : function(pagina) {
			  
			  return Restangular.allUrl("administrar/consultar/parametros/pagina="+pagina).getList();
			  
		},
		
		traerParametrosFiltro : function(pagina,nombre,estado) {
			  
			var url = "administrar/consultar/parametros/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;			
			if(estado!=null && estado != "" ) url += "&estado=" + estado;
			 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerParametro : function(id) {
			  
			var url = "administrar/parametro/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardarParametro:function(objeto){
			var url = "administrar/parametro/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
		
		
	
	}
} ]);