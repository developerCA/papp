app.factory("ObrasFactory", ["Restangular", function (Restangular) {

    var service = Restangular.service("/administrar");

    return {

        traerObras: function (pagina) {

            return Restangular.allUrl("administrar/consultar/obra/pagina=" + pagina).getList();

        },

        traerObrasFiltro: function (pagina, codigo, nombre, estado) {

            var url = "administrar/consultar/obra/pagina=" + pagina;
            if (codigo != null && codigo != "") url += "&codigo=" + codigo;
            if (nombre != null && nombre != "") url += "&nombre=" + nombre;
            if (estado != null && estado != "") url += "&estado=" + estado;
            return Restangular.allUrl(url).getList();

        },

        traerObra: function (id) {

            var url = "administrar/obra/" + id + "/-1";
            return Restangular.allUrl(url).customGET();

        },

        guardar: function (objeto) {
            var url = "administrar/obra/";
            return Restangular.allUrl(url).customPOST(objeto);
        },

    }
}]);