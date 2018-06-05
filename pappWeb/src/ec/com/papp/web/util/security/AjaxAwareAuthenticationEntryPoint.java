
package ec.com.papp.web.util.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class AjaxAwareAuthenticationEntryPoint 
             extends LoginUrlAuthenticationEntryPoint {

    public AjaxAwareAuthenticationEntryPoint(String loginUrl) {
        super(loginUrl);
    }

    @Override
    public void commence(
        HttpServletRequest request, 
        HttpServletResponse response, 
        AuthenticationException authException) 
            throws IOException, ServletException {

    	//System.out.println("url: " + request.getRequestURI());
    	boolean isLogin = request.getRequestURI().startsWith("/papp-Desarrollo/login");
        boolean isAjax = request.getRequestURI().startsWith("/papp-Desarrollo/");

        if (isAjax && !isLogin) {
            response.sendError(302, "Pagina no encontrada");
        } else {
        	//System.out.println("entro otro");
            super.commence(request, response, authException);
        }
    }
}