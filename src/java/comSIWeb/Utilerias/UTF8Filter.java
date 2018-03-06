package comSIWeb.Utilerias;

import java.io.IOException;
import javax.servlet.*;

/**
 *Filtro para que la aplicación acepte codificación en formato UTF-8
 * @author zeus
 */
public class UTF8Filter implements Filter {

   private String encoding;

   /**

    * Recogemos el tipo de codificación definido en el web.xml

    * Si no se hubiera especificado ninguno se toma "UTF-8" por defecto

    * @param filterConfig
    * @throws ServletException
    */
   public void init(FilterConfig filterConfig) throws ServletException {

      encoding = filterConfig.getInitParameter("requestEncoding");

      if (encoding == null) {

         encoding = "UTF-8";

      }

   }

   /**

    * Metemos en la request el formato de codificacion UTF-8

    * @param request
    * @param response
    * @param fc
    * @throws IOException
    * @throws ServletException
    */
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc)
           throws IOException, ServletException {

      request.setCharacterEncoding(encoding);

      fc.doFilter(request, response);


   }

   /**
    *
    */
   public void destroy() {
   }
}
