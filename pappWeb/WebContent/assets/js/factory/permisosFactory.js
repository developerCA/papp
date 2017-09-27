app.factory("permisosFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/seguridad");

	return {
		
		
		traerPermisos : function(pagina) {
			  
			  return Restangular.allUrl("seguridad/consultar/permiso/pagina="+pagina).getList();
			  
		},
		
		traerPermisosFiltro : function(pagina,nombre) {
			  
			var url = "seguridad/consultar/permiso/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerPermiso : function(id) {
			  
			var url = "seguridad/permiso/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "seguridad/permiso/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

	}
} ]);