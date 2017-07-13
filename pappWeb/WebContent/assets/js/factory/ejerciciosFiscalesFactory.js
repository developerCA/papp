app.factory("ejercicioFiscalFactory", [ "Restangular", function(Restangular) {

		
	var service = Restangular.service("/administrar");

	return {
		
		
		traerEjercicios : function(pagina) {
			  
			  return Restangular.allUrl("administrar/consultar/ejerciciofiscal/pagina="+pagina).getList();
		},
	
	}
} ]);