app.factory("sociosNegocioFactory", ["Restangular", function (Restangular) {

    var service = Restangular.service("/administrar");

    return {

        traer: function (pagina, ejercicio) {

            return Restangular.allUrl("administrar/consultar/socionegocio/pagina=" + pagina + "&ejerciciofiscalid=" + ejercicio).getList();

        },

        traerFiltro: function (pagina, codigo, nombre, estado) {

            var url = "administrar/consultar/socionegocio/pagina=" + pagina;

            if (nombre != null && nombre != "") url += "&nombre=" + nombre.toUpperCase();
            if (codigo != null && codigo != "") url += "&codigo=" + codigo.toUpperCase();
            if (estado != null && estado != "") url += "&estado=" + estado;

            return Restangular.allUrl(url).getList();

        },

        traerObj: function (id) {

            var url = "administrar/socionegocio/" + id + "/-1";
            return Restangular.allUrl(url).customGET();

        },

        guardar: function (objeto) {
            var url = "administrar/socionegocio/";
            return Restangular.allUrl(url).customPOST(objeto);
        },

    }
}]);