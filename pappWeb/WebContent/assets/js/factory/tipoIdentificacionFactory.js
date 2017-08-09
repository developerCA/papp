app.factory("TipoIdentificacionFactory", ["Restangular", function (Restangular) {

    var service = Restangular.service("/administrar");

    return {

        traerTipos: function (pagina) {

            return Restangular.allUrl("administrar/consultar/tipoidentificacion/pagina=" + pagina).getList();

        },

        traerTiposFiltro: function (pagina, nombre) {

            var url = "administrar/consultar/tipoidentificacion/pagina=" + pagina;

            if (nombre != null && nombre != "") url += "&nombre=" + nombre.toUpperCase();
            console.log(url);
            return Restangular.allUrl(url).getList();

        },

        traerTipo: function (id) {

            var url = "administrar/tipoidentificaciontipo/" + id + "/-1";
            return Restangular.allUrl(url).customGET();

        },

        guardar: function (objeto) {
            var url = "administrar/tipoidentificacion/";
            return Restangular.allUrl(url).customPOST(objeto);
        },

    }
}]);