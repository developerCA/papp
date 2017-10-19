app.factory("estructuraorganicaFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/estructuraorganica");

	return {
		
		
		traerEstructuraOrganica : function(pagina) {
			  
			  return Restangular.allUrl("estructuraorganica/consultar/estructuraorganica/pagina="+pagina).getList();
			  
		},
		
		traerEstructuraOrganicaFiltro : function(pagina,codigo,fuerza,grado,padre,estado) {
			  
			var url = "estructuraorganica/consultar/estructuraorganica/pagina="+pagina;

			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;	
			if(fuerza!=null && fuerza != "") url += "&npnombrefuerza=" + fuerza;	
			if(grado!=null && grado != "") url += "&npnombregrado=" + grado;	
			if(padre!=null && padre != "") url += "&npgrupo=" + padre;	
			if(estado!=null && estado != "") url += "&estado=" + estado;	
			 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerEstructuraOrganicaEditar : function(id) {
			  
			var url = "estructuraorganica/estructuraorganica/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "estructuraorganica/estructuraorganica/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

	}
} ]);