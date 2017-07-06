package ec.com.papp.web.comun.util;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
 
 
//public class CorsFilter extends OncePerRequestFilter {
//	private static Log log = new Log(CorsFilter.class);
//	 @Override
//	    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//	            throws ServletException, IOException {
//		 	System.out.println("entro al filtro");
//	        response.addHeader("Access-Control-Allow-Origin", "*");
//	        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//	        response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with, sid, mycustom, smuser");
//	        response.addHeader("Access-Control-Max-Age", "1800");//30 min
//	        filterChain.doFilter(request, response);
//	    }
//	}



public class CorsFilter extends HttpServlet{

	public void init(ServletConfig cfg,HttpServletResponse response) throws ServletException {
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with, sid, mycustom, smuser");
            response.addHeader("Access-Control-Max-Age", "1800");//30 min
       // }
    }
}