app.factory("ItemsFactory", ["Restangular", function (Restangular) {

    var service = Restangular.service("/administrar");

    return {

        traerItems: function (pagina) {

            return Restangular.allUrl("administrar/consultar/item/pagina=" + pagina).getList();

        },

        traerItemsFiltro: function (pagina, unidad, estado, nombregrupo) {

            var url = "administrar/consultar/item/pagina=" + pagina;

            if (unidad != null && unidad != "") url += "&nombre=" + unidad.toUpperCase();
            if (nombregrupo != null && nombregrupo != "") url += "&nombregrupo=" + nombregrupo;
            if (estado != null && estado != "") url += "&estado=" + estado;
            console.clear();
            console.log(url);
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