app.factory("PlanificacionUEFactory", ["Restangular", function (Restangular) {

    var service = Restangular.service("/planificacion");

    return {

        traerPlanificacionUE: function (pagina, ejercicio) {
        	return Restangular.allUrl("planificacion/consultar/planificacion/pagina=" + pagina + "&ejerciciofiscal=" + ejercicio).getList();
        },

        traerPlanificacionUECustom: function (pagina, ejercicio) {
        	 var url = "planificacion/consultar/planificacion/pagina=" + pagina + "&ejerciciofiscal=" + ejercicio;
             return Restangular.allUrl(url).customGET();
             
            
        },
        
        traerPlanificacionUEFiltro: function (pagina, ejercicio, codigo, nombre) {
            var url = "planificacion/consultar/planificacion/pagina=" + pagina+ "&ejerciciofiscal=" + ejercicio;

            if (codigo != null && codigo != "") url += "&codigopresup=" + codigo.toUpperCase();
            if (nombre != null && nombre != "") url += "&nombre=" + nombre;

            return Restangular.allUrl(url).getList();
        },

        traerPlanificacionUEFiltroCustom: function (pagina, ejercicio, codigo, nombre) {
            var url = "planificacion/consultar/planificacion/pagina=" + pagina+ "&ejerciciofiscal=" + ejercicio;

            if (codigo != null && codigo != "") url += "&codigopresup=" + codigo.toUpperCase();
            if (nombre != null && nombre != "") url += "&nombre=" + nombre;

            return Restangular.allUrl(url).customGET();
        },

        traerItem: function (id) {
            var url = "planificacion/planificacion/" + id + "/-1";
            return Restangular.allUrl(url).customGET();
        },

        guardar: function (objeto) {
            var url = "planificacion/planificacion/";
            return Restangular.allUrl(url).customPOST(objeto);
        },
    }
}]);