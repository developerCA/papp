app.factory("gruposMedidaFactory", ["Restangular", function (Restangular) {

    var service = Restangular.service("/administrar");

    return {

        traerGrupos: function (pagina) {

            return Restangular.allUrl("administrar/consultar/grupomedida/pagina=" + pagina).getList();

        },

        traerGruposFiltro: function (pagina, nombre, estado) {

            var url = "administrar/consultar/grupomedida/pagina=" + pagina;

            if (nombre != null && nombre != "") url += "&nombre=" + nombre.toUpperCase();
            if (estado != null && estado != "") url += "&estado=" + estado;

            return Restangular.allUrl(url).getList();

        },

        traerGrupo: function (id) {

            var url = "administrar/grupomedida/" + id + "/-1";
            return Restangular.allUrl(url).customGET();

        },

        guardar: function (objeto) {
            var url = "administrar/grupomedida/";
            return Restangular.allUrl(url).customPOST(objeto);
        },

    }
}]);