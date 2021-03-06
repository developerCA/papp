function Roles() {
//https://www.youtube.com/watch?v=Yw3xxAUJ0Ns
//app.value('usuario', seccion.usuario);
// https://www.oscarlijo.com/blog/servicios-en-angularjs/
  this.init=function() {
	seccion.rols = JSON.parse(seccion.rols);
	seccion.rols = seccion.rols.permisos;
	//console.log(seccion.rols);
  }

  this.ifRollId=function(id) {
  	if (!isNaN(id)) {
  		id = parseInt(id);
  		if (id === 0) return true;
  		for (var obj in seccion.rols) {
  			if (seccion.rols[obj].id.permisoid == id) {
  				return true;
  			}
  		}
  	}
  	return false;
  }

  this.ifRollPermiso=function(nombre) {
  	if (nombre !== undefined) {
  		if (nombre.trim() === '') return true;
  		for (var obj in seccion.rols) {
  			if (seccion.rols[obj].nppermiso == nombre) {
  				return true;
  			}
  		}
  	}
  	return false;
  }
}
 
app.service("roles", Roles);