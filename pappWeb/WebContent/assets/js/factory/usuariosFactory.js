app.factory("usuariosFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/seguridad");

	return {

		traerUsuarios : function(pagina) {
			  
			  return Restangular.allUrl("seguridad/consultar/usuario/pagina="+pagina).getList();
			  
		},
		
		traerUsuariosFiltro : function(pagina,usuario,nombre,estado) {
			  
			var url = "seguridad/consultar/usuario/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;
			if(usuario!=null && usuario != "") url += "&usuario=" + usuario;
			if(estado!=null && estado != "") url += "&estado=" + estado;
			 
			return Restangular.allUrl(url).getList();
			  
		},
		
		traerUsuario : function(id) {
			  
			var url = "seguridad/usuario/"+id+"/-1";
		   
		    return Restangular.allUrl(url).customGET();
			  
		},
		
		guardar:function(objeto){
			var url = "seguridad/usuario/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
		
		eliminar:function(id){
			var url = "seguridad/usuario/";
			return Restangular.allUrl(url).remove(id);
		},
		
	
	}
} ]);