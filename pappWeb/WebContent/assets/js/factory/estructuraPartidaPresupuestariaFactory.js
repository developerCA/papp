app.factory("estructuraPartidaPresupuestariaFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {
		traerPartidapresupuestaria : function(ejerciciofiscal) {
			var url = "planificacion/nuevo/partidapresupuestaria/0/"+ejerciciofiscal+"/0";

		    return Restangular.allUrl(url).customGET();
		},
		
		guardar:function(objeto){
			var url = "planificacion/partidapresupuestaria";
			return Restangular.allUrl(url).customPOST(objeto);
		},
	}
} ]);