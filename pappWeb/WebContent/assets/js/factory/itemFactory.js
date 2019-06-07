app.factory("ItemsFactory", ["Restangular", function (Restangular) {

    var service = Restangular.service("/administrar");

    return {

        traerItems: function (pagina, ejercicio) {
        	return Restangular.allUrl("administrar/consultar/item/pagina=" + pagina + "&ejerciciofiscalid=" + ejercicio).getList();
        },

        traerItemsCustom: function (pagina, ejercicio) {
        	 var url = "administrar/consultar/item/pagina=" + pagina + "&filas=10&ejerciciofiscalid=" + ejercicio;
             return Restangular.allUrl(url).customGET();
        },
        
        traerItemsFiltro: function (pagina, ejercicio, codigo, nombre, estado, tipo, codigopadre, nombrepadre) {
            var url = "administrar/consultar/item/pagina=" + pagina+ "&ejerciciofiscalid=" + ejercicio;
            if (codigo != null && codigo != "") url += "&codigo=" + codigo.toUpperCase();
            if (nombre != null && nombre != "") url += "&nombre=" + nombre;
            if (tipo != null && tipo != "") url += "&tipo=" + tipo;
            if (estado != null && estado != "") url += "&estado=" + estado;
            if (codigopadre != null && codigopadre != "") url += "&codigopadre=" + codigopadre;
            if (nombrepadre != null && nombrepadre != "") url += "&nombrepadre=" + nombrepadre;
            //console.log("FILTRAR: " + url);
            return Restangular.allUrl(url).getList();
        },

        traerItemsFiltroCustom: function (pagina, ejercicio, codigo, nombre, estado, tipo, codigopadre, nombrepadre) {
            var url = "administrar/consultar/item/pagina=" + pagina+ "&filas=10&ejerciciofiscalid=" + ejercicio;
            if (codigo != null && codigo != "") url += "&codigo=" + codigo.toUpperCase();
            if (nombre != null && nombre != "") url += "&nombre=" + nombre;
            if (tipo != null && tipo != "") url += "&tipo=" + tipo;
            if (estado != null && estado != "") url += "&estado=" + estado;
            if (codigopadre != null && codigopadre != "") url += "&codigopadre=" + codigopadre;
            if (nombrepadre != null && nombrepadre != "") url += "&nombrepadre=" + nombrepadre;
            //console.log("FILTRAR: " + url);
            return Restangular.allUrl(url).customGET();
        },

        traerItem: function (id) {
            var url = "administrar/item/" + id + "/-1";
            return Restangular.allUrl(url).customGET();
        },

        guardar: function (objeto) {
            var url = "administrar/item/";
            return Restangular.allUrl(url).customPOST(objeto);
        },

        traerItemsNivelActividadFiltroCustom: function (pagina, ejercicio, nivelactividadunidadid, codigo, nombre, estado, tipo, codigopadre, nombrepadre) {
            var url = "planificacion/consultar/nivelactividad/" +
            		"pagina=" + pagina+ "&" +
    				"filas=10&" +
    				"nivelactividadejerfiscalid=" + ejercicio + "&" +
    				"nivelactividadunidadid=" + nivelactividadunidadid + "&" +
    				"tipo=IT&" +
    				"estado=A";
            if (codigo != null && codigo != "") url += "&codigo=" + codigo.toUpperCase();
            if (nombre != null && nombre != "") url += "&nombre=" + nombre;
            if (tipo != null && tipo != "") url += "&tipo=" + tipo;
            //if (estado != null && estado != "") url += "&estado=" + estado;
            if (codigopadre != null && codigopadre != "") url += "&codigopadre=" + codigopadre;
            if (nombrepadre != null && nombrepadre != "") url += "&nombrepadre=" + nombrepadre;
            //console.log("FILTRAR: " + url);
            return Restangular.allUrl(url).customGET();
        },
    }
}]);