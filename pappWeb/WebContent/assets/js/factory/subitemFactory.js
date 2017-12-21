app.factory("SubItemsFactory", ["Restangular", function (Restangular) {

    var service = Restangular.service("/administrar");

    return {

        traerItems: function (pagina, ejercicio) {
        	var url = "administrar/consultar/subitem/pagina=" + pagina + "&ejerciciofiscalid=" + ejercicio;
        	return Restangular.allUrl(url).getList();
        },
        
        traerItemsCustom: function (pagina, ejercicio) {
        	 var url = "administrar/consultar/subitem/pagina=" + pagina + "&ejerciciofiscalid=" + ejercicio;
             return Restangular.allUrl(url).customGET();
        	
        },

        traerItemsFiltro: function (pagina, codigo, nombre, estado, tipo, ejercicio,incop,itemnombre) {
            var url = "administrar/consultar/subitem/pagina=" + pagina + "&ejerciciofiscalid=" + ejercicio;

            if (codigo != null && codigo != "") url += "&codigo=" + codigo.toUpperCase();
            if (nombre != null && nombre != "") url += "&nombre=" + nombre;
            if (tipo != null && tipo != "") url += "&tipo=" + tipo;
            if (estado != null && estado != "") url += "&estado=" + estado;
            if (incop != null && incop != "") url += "&codigoIncop=" + incop;
            if (itemnombre != null && itemnombre != "") url += "&itemnombre=" + itemnombre;

            console.log(url);
            return Restangular.allUrl(url).getList();

        },

        traerItemsFiltroCustom: function (pagina, codigo, nombre, estado, tipo, ejercicio,incop,itemnombre) {
            var url = "administrar/consultar/subitem/pagina=" + pagina + "&ejerciciofiscalid=" + ejercicio;

            if (codigo != null && codigo != "") url += "&codigo=" + codigo.toUpperCase();
            if (nombre != null && nombre != "") url += "&nombre=" + nombre;
            if (tipo != null && tipo != "") url += "&tipo=" + tipo;
            if (estado != null && estado != "") url += "&estado=" + estado;
            if (incop != null && incop != "") url += "&codigoIncop=" + incop;
            if (itemnombre != null && itemnombre != "") url += "&itemnombre=" + itemnombre;
            
            return Restangular.allUrl(url).customGET();


        },

        traerItem: function (id) {

            var url = "administrar/subitem/" + id + "/-1";
            return Restangular.allUrl(url).customGET();

        },

        guardar: function (objeto) {
            var url = "administrar/subitem/";
            return Restangular.allUrl(url).customPOST(objeto);
        },

    }
}]);