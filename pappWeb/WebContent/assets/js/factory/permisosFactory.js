app.factory("permisosFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/seguridad");

	return {
		traerPermisos : function(pagina) {
			  return Restangular.allUrl("seguridad/consultar/permiso/pagina="+pagina+"&filas=10").customGET();
		},
		
		traerPermisosFiltro : function(pagina,nombre) {
			var url = "seguridad/consultar/permiso/pagina="+pagina+"&filas=10";

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			 
			return Restangular.allUrl(url).customGET();
		},
		
		traerPermisosFiltro2 : function(pagina,nombre,id) {
			var url = "seguridad/consultar/permiso/pagina="+pagina+"&filas=10";

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if(id!=null && id != "") url += "&id=" + id;	
			 
			return Restangular.allUrl(url).customGET();
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