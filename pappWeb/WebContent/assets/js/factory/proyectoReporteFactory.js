app.factory("ProyectoReporteFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {
		traerProyectoFiltro : function(
			pagina,
			proyectoejerfiscalid,
			codigo,
			nombre,
			npcodigoprograma,
			npnombreprograma,
			npcodigosubprograma,
			npunidad,
			npprogramaid,
			padre
		) {
			var url = "planificacion/consultar/proyectoreporte/pagina="+pagina;

			if(proyectoejerfiscalid!=null && proyectoejerfiscalid != "") url += "&proyectoejerfiscalid=" + proyectoejerfiscalid;	
			if(codigo!=null && codigo != "") url += "&codigo=" + codigo;	
			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	
			if(npcodigoprograma!=null && npcodigoprograma != "") url += "&npcodigoprograma=" + npcodigoprograma;	
			if(npnombreprograma!=null && npnombreprograma != "") url += "&npnombreprograma=" + npnombreprograma;	
			if(npcodigosubprograma!=null && npcodigosubprograma != "") url += "&npcodigosubprograma=" + npcodigosubprograma;	
			if(npunidad!=null && npunidad != "") url += "&npunidad=" + npunidad;	
			if(npprogramaid!=null && npprogramaid != "") url += "&npprogramaid=" + npprogramaid;	
			if(padre!=null && padre != "") url += "&padre=" + padre;	

			return Restangular.allUrl(url).getList();
		},
		
		traerProyectoEditar : function(id) {
			var url = "planificacion/proyecto/"+id+"/-1";

			return Restangular.allUrl(url).customGET();
		},

		guardar:function(objeto){
			var url = "planificacion/proyecto/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
	}
} ]);