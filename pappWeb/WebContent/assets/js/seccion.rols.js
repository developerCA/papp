// seccion
seccion.rols = JSON.parse(seccion.rols);
seccion.rols = seccion.rols.permisos;
//console.log(seccion.rols);

function ifRollId(id) {
	if (!isNaN(id)) {
		id = parseInt(id);
		if (id == 0) return true;
		for (var obj in seccion.rols) {
			if (seccion.rols[obj].id.permisoid == id) {
				return true;
			}
		}
	}
	return false;
}

function ifRollPermiso(nombre) {
	if (nombre != undefined) {
		if (nombre.trim() == "") return true;
		for (var obj in seccion.rols) {
			if (seccion.rols[obj].nppermiso == nombre) {
				return true;
			}
		}
	}
	return false;
}
