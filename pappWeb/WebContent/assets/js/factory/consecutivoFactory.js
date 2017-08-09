app.factory("consecutivoFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/administrar");

	return {
		
		
		traerConsecutivos : function(pagina) {
			  
			  return Restangular.allUrl("administrar/consultar/consecutivo/pagina="+pagina).getList();
			  
		},
		
		traerConsecutivoFiltro : function(pagina,nombre,prefijo,estado) {
			  
			var url = "administrar/consultar/consecutivo/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if(prefijo!=null && prefijo != "") url += "&prefijo=" + prefijo;	
			if(estado!=null && estado != "" ) url += "&estado=" + estado;
			 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerConsecutivo : function(id) {
			  
			var url = "administrar/consecutivo/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardarConsecutivo:function(objeto){
			var url = "administrar/consecutivo/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
		
		
	
	}
} ]);