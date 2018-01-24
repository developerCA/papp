app.factory("TipoProductoFactory", ["Restangular", function (Restangular) {

    var service = Restangular.service("/administrar");

    return {

        traerTipos: function (pagina) {

            return Restangular.allUrl("administrar/consultar/tipoproducto/pagina=" + pagina).getList();

        },

        traer: function (pagina, ejercicio) {
        	var url = "comun/tipoproducto";
        	return Restangular.allUrl(url).customGET();
        },

        traerTiposFiltro: function (pagina, nombre, estado) {
            var url = "administrar/consultar/tipoproducto/pagina=" + pagina;
            if (nombre != null && nombre != "") url += "&nombre=" + nombre.toUpperCase();
            url += "&estado=" + estado;
            return Restangular.allUrl(url).getList();

        },

        traerTipo: function (id) {

            var url = "administrar/tipoproducto/" + id + "/-1";
            return Restangular.allUrl(url).customGET();

        },

        guardar: function (objeto) {
            var url = "administrar/tipoproducto/";
            return Restangular.allUrl(url).customPOST(objeto);
        },

        eliminar: function (id) {
            var url = "administrar/tipoproducto/"+id+"/-1";
            return Restangular.allUrl(url).remove();
        }

    }

}]);