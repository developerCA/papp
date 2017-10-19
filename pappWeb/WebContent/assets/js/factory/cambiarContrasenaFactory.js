app.factory("CambiarContrasenaFactory", [ "Restangular", function(Restangular) {

//	var service = Restangular.service("/seguridad");

	return {

		traerUsuario : function() {
//			var url = "seguridad/usuario/-1/-1";
			var url = "seguridad/";

			return Restangular.allUrl(url).customGET();
			  
		},

//		traerUsuario : function(id) {
//			  
//			var url = "seguridad/usuario/"+id+"/-1";
//		   
//		    return Restangular.allUrl(url).customGET();
//			  
//		},

		guardar:function(objeto){
			var url = "seguridad/cambiarClave/";
			return Restangular.allUrl(url).customPOST(objeto);
		}

	}
} ]);