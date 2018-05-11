app.factory("sociosNegocioFactory", ["Restangular", function (Restangular) {

    var service = Restangular.service("/administrar");

    return {

        traer: function (pagina, ejercicio) {
            return Restangular.allUrl("administrar/consultar/socionegocio/pagina=" + pagina + "&filas=10&ejerciciofiscalid=" + ejercicio).customGET();
        },

        traerFiltro: function (pagina, codigo, nombre, estado) {
            var url = "administrar/consultar/socionegocio/pagina=" + pagina + "&filas=10";

            if (nombre != null && nombre != "") url += "&nombre=" + nombre.toUpperCase();
            if (codigo != null && codigo != "") url += "&codigo=" + codigo.toUpperCase();
            if (estado != null && estado != "") url += "&estado=" + estado;

            return Restangular.allUrl(url).customGET();
        },

        traerFiltroEmpleados: function (pagina, ejercicio, codigo, nombre, estado) {
            var url = "administrar/consultar/socionegocio/pagina=" + pagina + "&filas=10&esempleado=1&ejerciciofiscalid=" + ejercicio;

            if (nombre != null && nombre != "") url += "&nombre=" + nombre.toUpperCase();
            if (codigo != null && codigo != "") url += "&codigo=" + codigo.toUpperCase();
            if (estado != null && estado != "") url += "&estado=" + estado;

            return Restangular.allUrl(url).customGET();

        },

        traerFiltroEmpleados2: function (pagina, ejercicio, codigo, nombre, estado) {
            var url = "administrar/consultar/socionegocio/pagina=" + pagina + "&filas=10&esempleado=1&ejerciciofiscalid=" + ejercicio;

            if (nombre != null && nombre != "") url += "&nombremostrado=" + nombre.toUpperCase();
            if (codigo != null && codigo != "") url += "&codigo=" + codigo.toUpperCase();
            if (estado != null && estado != "") url += "&estado=" + estado;

            return Restangular.allUrl(url).customGET();

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