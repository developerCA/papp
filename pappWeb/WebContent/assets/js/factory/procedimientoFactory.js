app.factory("ProcedimientoFactory", ["Restangular", function (Restangular) {

    var service = Restangular.service("/administrar");

    return {

        traerProcedimientos: function (pagina) {

            return Restangular.allUrl("administrar/consultar/procedimiento/pagina=" + pagina).getList();

        },

        traerProcedimientosFiltro: function (pagina, nombre, activo) {

            var url = "administrar/consultar/procedimiento/pagina=" + pagina;

            if (nombre != null && nombre != "") url += "&nombre=" + nombre.toUpperCase();
            //if (descripcion != null && descripcion != "") url += "&descripcion=" + descripcion;
            //if (activo != null && activo != "")
            url += "&estado=" + activo;

            return Restangular.allUrl(url).getList();

        },

        traerProcedimiento: function (id) {

            var url = "administrar/procedimiento/" + id + "/-1";
            return Restangular.allUrl(url).customGET();

        },

        guardar: function (objeto) {
            var url = "administrar/procedimiento/";
            return Restangular.allUrl(url).customPOST(objeto);
        },

        eliminar: function (id) {
            var url = "administrar/procedimiento/";
            return Restangular.allUrl(url).remove(id);
        }

    }

}]);