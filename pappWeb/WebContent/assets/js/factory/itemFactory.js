app.factory("ItemsFactory", ["Restangular", function (Restangular) {

    var service = Restangular.service("/administrar");

    return {

        traerItems: function (pagina, ejercicio) {
        	return Restangular.allUrl("administrar/consultar/item/pagina=" + pagina + "&ejerciciofiscalid=" + ejercicio).getList();

        },

        traerItemsFiltro: function (pagina, codigo, nombre, estado, tipo, ejerciciofiscalid) {

            var url = "administrar/consultar/item/pagina=" + pagina;
            if (codigo != null && codigo != "") url += "&codigo=" + codigo.toUpperCase();
            if (nombre != null && nombre != "") url += "&nombre=" + nombre;
            if (tipo != null && tipo != "") url += "&tipo=" + tipo;
            if (estado != null && estado != "") url += "&estado=" + estado;
            return Restangular.allUrl(url).getList();

        },

        traerItem: function (id) {

            var url = "administrar/item/" + id + "/-1";
            return Restangular.allUrl(url).customGET();

        },

        guardar: function (objeto) {
            var url = "administrar/item/";
            return Restangular.allUrl(url).customPOST(objeto);
        },

    }
}]);