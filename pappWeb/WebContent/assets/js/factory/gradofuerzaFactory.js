app.factory("gradofuerzaFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/administrar");

	return {
		
		
		traerGradoFuerza : function(pagina) {
			  
			  return Restangular.allUrl("administrar/consultar/gradofuerza/pagina="+pagina).getList();
			  
		},

		traerGradoFuerzaFiltroIdFuerza : function(pagina,idfuerza,codigo,fuerza,grado,padre,estado) {
			  
			var url = "administrar/consultar/gradofuerza/pagina="+pagina;

			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;	
			if(fuerza!=null && fuerza != "") url += "&npnombrefuerza=" + fuerza;	
			if(grado!=null && grado != "") url += "&npnombregrado=" + grado;	
			if(padre!=null && padre != "") url += "&npgrupo=" + padre;	
			if(estado!=null && estado != "") url += "&estado=" + estado;	
			if(idfuerza!=null && idfuerza != "") url += "&idfuerza=" + idfuerza;	

			return Restangular.allUrl(url).getList();
			  
		},

		traerGradoFuerzaFiltro : function(pagina,codigo,fuerza,grado,padre,estado) {
			  
			var url = "administrar/consultar/gradofuerza/pagina="+pagina;

			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;	
			if(fuerza!=null && fuerza != "") url += "&npnombrefuerza=" + fuerza;	
			if(grado!=null && grado != "") url += "&npnombregrado=" + grado;	
			if(padre!=null && padre != "") url += "&npgrupo=" + padre;	
			if(estado!=null && estado != "") url += "&estado=" + estado;	
			 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerGradoFuerzaEditar : function(id) {
			  
			var url = "administrar/gradofuerza/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "administrar/gradofuerza/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

	}
} ]);