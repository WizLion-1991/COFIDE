
package comSIWeb.Utilerias;

import java.util.Arrays;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

/**
 *Esta clase nos ayuda a rastrear los metodos request
 * @author zeus
 */
public class CIP_RastroRequest {
   /**
    * Muestra la lista de parametros enviados por el formulario que nos mandan por POST o GET
    * @param request Es la peticion request
    */
   public static void MuestraListadeParametros(HttpServletRequest request){
       Enumeration paramNames = request.getParameterNames();
       int intContador = 0;
       while (paramNames.hasMoreElements()) {
          paramNames.nextElement();
          intContador++;
       }
       String[] a = new String[intContador];
       Enumeration paramNames2 = request.getParameterNames();
       intContador = -1;
       while (paramNames2.hasMoreElements()) {
          String paramName = (String) paramNames2.nextElement();
          intContador++;
          a[intContador] = paramName;
       }
       Arrays.sort(a);

       //Mostramos el arreglo ordenado
       for (int i = 0; i < a.length; i++) {
          String strTmp = a[i] + ":" + request.getParameter(a[i]) + "<br>";
          System.out.println(strTmp);
       }
       //Mostramos el arreglo ordenado con la sentencia para obtener los parametros
       for (int i = 0; i < a.length; i++) {
          System.out.println("String str" + a[i] + " = request.getParameter(\"" + a[i] + "\");");
       }
   }
}
