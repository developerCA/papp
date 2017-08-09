app.factory("UnidadesMedidaFactory", ["Restangular", function (Restangular) {

    var service = Restangular.service("/administrar");

    return {

        traerUnidades: function (pagina) {

            return Restangular.allUrl("administrar/consultar/unidadmedida/pagina=" + pagina).getList();

        },

        traerUnidadesFiltro: function (pagina, unidad, estado, nombregrupo) {

            var url = "administrar/consultar/unidadmedida/pagina=" + pagina;

            if (unidad != null && unidad != "") url += "&nombre=" + unidad.toUpperCase();
            if (nombregrupo != null && nombregrupo != "") url += "&nombregrupo=" + nombregrupo;
            if (estado != null && estado != "") url += "&estado=" + estado;
            console.clear();
            console.log(url);
            return Restangular.allUrl(url).getList();

        },

        traerUnidad: function (id) {

            var url = "administrar/unidadmedida/" + id + "/-1";
            return Restangular.allUrl(url).customGET();

        },

        traerGrupos : function(pagina){
        
            return Restangular.allUrl("administrar/consultar/grupomedida/pagina=" + pagina).getList();

        },

        guardar: function (objeto) {
            var url = "administrar/unidadmedida/";
            return Restangular.allUrl(url).customPOST(objeto);
        },

    }
}]);