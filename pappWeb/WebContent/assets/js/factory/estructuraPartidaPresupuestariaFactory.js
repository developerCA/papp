app.factory("estructuraPartidaPresupuestariaFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/estructuraorganica");

	return {
		traerPartidapresupuestaria : function(ejerciciofiscal) {
			  return Restangular.allUrl("planificacion/nuevo/partidapresupuestaria/0/"+ejerciciofiscal+"/0").getList();
		},
		
		guardar:function(objeto){
			var url = "estructuraorganica/estructuraorganica/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
	}
} ]);