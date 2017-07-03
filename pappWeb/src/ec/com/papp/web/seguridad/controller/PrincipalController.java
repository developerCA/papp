package ec.com.papp.web.seguridad.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ec.com.papp.seguridad.to.UsuarioTO;
import ec.com.papp.web.comun.util.UtilSession;
import ec.com.papp.web.seguridad.util.RolesUsuario;
import ec.com.xcelsa.utilitario.exception.MyException;

@RestController
@RequestMapping("/user")
public class PrincipalController {

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody RolesUsuario user(HttpServletRequest request,Authentication principal) {
	System.out.println("principal: " + principal);
	System.out.println("nombre: " + principal.getName());
	Collection collection=principal.getAuthorities();
	UtilSession.setUsuario(request, principal.getName());
	UsuarioTO usuarioTO=new UsuarioTO();
	usuarioTO.setUsuario(principal.getName());
	RolesUsuario rolesUsuario=new RolesUsuario();
	try {
		Collection<UsuarioTO> usuarioTOs=UtilSession.seguridadServicio.transObtenerusuario(usuarioTO);
		usuarioTO=(UsuarioTO)usuarioTOs.iterator().next();
		rolesUsuario.setCollection(collection);
		rolesUsuario.setPerfil(usuarioTO.getPerfilid());
	} catch (MyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return rolesUsuario;
	}
	
}
