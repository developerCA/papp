app.factory("usuariosFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/usuarios");

	return {
		
		listaUsuarios : function() {
			return service.getList();
		},
		traerUsuarioAuntenticado : function() {
			return Restangular.allUrl("usuarios/info").customGET();
		},
		traerPerfiles : function() {
			return Restangular.allUrl("usuarios/perfiles").getList();
		},
        nuevo: function() {
        	
            return Restangular.allUrl("usuarios/nuevo").customPUT();
        },
        registrar: function(usuario) {
            return Restangular.allUrl("usuarios/registro").customPOST(usuario);
        },
        buscar: function(codigoUsuario) {
            return Restangular.allUrl("usuarios/buscar").customPUT(codigoUsuario);
        },
        cambiarClave: function(usuario) {
            return Restangular.allUrl("usuarios/clave").customPUT(usuario);
        },
        eliminar: function(codigoUsuario) {
        	
            return Restangular.allUrl("usuarios/eliminar").customPUT(codigoUsuario);
        },

	}
} ]);