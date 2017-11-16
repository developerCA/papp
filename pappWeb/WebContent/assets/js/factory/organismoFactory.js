app.factory("organismoFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/administrar");

	return {
		
		
		traerOrganismos : function(pagina,ejercicio) {
			  
			  return Restangular.allUrl("administrar/consultar/organismo/pagina="+pagina+ "&ejerciciofiscalid=" + ejercicio).getList();
			  
		},
		
		traerOrganismosFiltro : function(pagina,ejercicio,nombre,codigo,estado) {
			  
			var url = "administrar/consultar/organismo/pagina="+pagina+ "&ejerciciofiscalid=" + ejercicio;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;	
			if(estado!=null && estado != "" ) url += "&estado=" + estado;
			 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerOrganismo : function(id) {
			  
			var url = "administrar/organismo/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "administrar/organismo/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
		
		
	
	}
} ]);