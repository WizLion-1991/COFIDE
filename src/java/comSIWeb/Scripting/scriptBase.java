
package comSIWeb.Scripting;

import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Bitacora;
import comSIWeb.Operaciones.CIP_Tabla;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import comSIWeb.Utilerias.Mail;
import java.sql.ResultSet;
import java.util.StringTokenizer;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 *Es la clase principal para ejecutar scripting
 * @author zeus
 */
public class scriptBase {
   
   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   protected HttpServletRequest request = null;
   protected Bitacora bitacora;
   private CIP_Tabla tabla;
   protected String strMsgERROR;
   protected VariableSession varSesiones;
   protected HSSFSheet sheet;

   /**
    * Regresa el objeto con las variables de sesion
    * @return Es el objeto con las varibles de sesion
    */
   public VariableSession getVarSesiones() {
      return varSesiones;
   }

   /**
    * Define las variables de sesion
    * @param varSesiones  Es el objeto con las varibles de sesion
    */
   public void setVarSesiones(VariableSession varSesiones) {
      this.varSesiones = varSesiones;
   }
   /**
    * Nos regresa el objeto request
    * @return Nos regresa un objeto HttpServletRequest
    */
   public HttpServletRequest getRequest() {
      return request;
   }
   /**
    * Definimos el objeto HttpServletRequest
    * @param request es un objeto HttpServletRequest
    */
   public void setRequest(HttpServletRequest request) {
      this.request = request;
   }

   /**
    * Regresa la tabla Master
    * @return Regresa el objeto de tabla master
    */
   public CIP_Tabla getTabla() {
      return tabla;
   }

   /**
    * Define la tabla Master
    * @param tabla Es el objeto tabla master
    */
   public void setTabla(CIP_Tabla tabla) {
      this.tabla = tabla;
   }

   /**
    * Nos regresa el mensaje de error del scripting
    * @return Nos regresa el mensaje de error del scripting
    */
   public String getStrMsgERROR() {
      return strMsgERROR;
   }

   /**
    *Regresa el objeto sheet del XLS en caso de las importaciones
    * @return Regresa un objeto HSSFSheet
    */
   public HSSFSheet getSheet() {
      return sheet;
   }

   /**
    *Define el objeto sheet del XLS en caso de las importaciones
    * @param sheet Es un objeto HSSFSheet
    */
   public void setSheet(HSSFSheet sheet) {
      this.sheet = sheet;
   }
   
   // </editor-fold>
   
   // <editor-fold defaultstate="collapsed" desc="Constructor">
   /***
    * Constructor default
    */
   public scriptBase() {
      this.bitacora = new Bitacora();
      this.strMsgERROR = "";
   }
   // </editor-fold>
   
   // <editor-fold defaultstate="collapsed" desc="Metodos">
    /**
    * Este metodo se encarga de parsear y ejecutar formulas mediante la tecnica de scripting
    * @param strFormulaAplicar Es el valor de la formula por aplicar, pueden haber varios valores
    * separados por #
    * @param oConn Es la conexion a usar
    * @return Nos regresa un dato tipo boolean con true en caso de cumplirse las condiciones de las sentencias
    */
   public boolean EjecutarFormula(String strFormulaAplicar, Conexion oConn) {
      //Nos indica si la ejecucion de la formula fue exitosa
      boolean bolResultFormula = true;
      //Parseamos la formula por la instruccion |
      //Recorremos la lista de tokens
      StringTokenizer tokens1 = new StringTokenizer(strFormulaAplicar, "#");
      //Barremos la lista de campos solicitados
      while (tokens1.hasMoreTokens()) {
         String strLstDeta = tokens1.nextToken();
         bolResultFormula = EjecutaSentencia(strLstDeta, oConn);
         //Si el resultado es falso nos salimos de la ejecucion
         if (bolResultFormula == false) {
            if (oConn.isBolMostrarQuerys() == true) {
               System.out.println("**Script Exit**");
            }
            break;
         }
      }
      return bolResultFormula;
   }

   /**
    * Este metodo ejecuta la sentencia a evaluar
    */
   private boolean EjecutaSentencia(String strSentencia, Conexion oConn) {
      //Nos indica si la ejecucion de la sentencia fue exitosa
      boolean bolResulSentencia = true;
      //Objeto del recordset
      ResultSet Rs = null;
      Fechas Fecha = new Fechas();
      Mail mail = new Mail();
      //Objetos para la ejecucion de scripting usamos el SCRIPT default
      ScriptEngineManager mgr = new ScriptEngineManager();
      ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
      //Anadimos objetos para que los tome en cuenta el scripting
      jsEngine.put("Fecha", Fecha);
      jsEngine.put("request", request);
      jsEngine.put("Rs", Rs);
      jsEngine.put("oConn", oConn);
      jsEngine.put("mail", mail);
      jsEngine.put("obj", this);
      jsEngine.put("strMsgERROR", this.strMsgERROR);
      if(this.tabla != null){
         jsEngine.put("tabla", this.tabla);
      }
      //Agregamos objeto sheet del XLS(hoja)
      if(this.sheet != null){
         jsEngine.put("sheet", this.sheet);
      }
      jsEngine.put("bolResulSentencia", bolResulSentencia);
      if (oConn.isBolMostrarQuerys() == true) {
         System.out.println("**Program to execute**" + strSentencia);
      }
      try {
         jsEngine.eval(strSentencia + ";");
      } catch (ScriptException ex) {
         this.bitacora.GeneraBitacora(ex.getMessage(), "System", "scriptBase", oConn);
      }
      //Recuperamos el valor booleano que se halla parseado
      String strValorBoolean = jsEngine.get("bolResulSentencia").toString();
      if (strValorBoolean.equals("true")) {
         bolResulSentencia = true;
      } else {
         bolResulSentencia = false;
      }
      if (oConn.isBolMostrarQuerys() == true) {
         System.out.println("**Script Solution**" + bolResulSentencia);
      }
      //Recuperamos el valor del campo de texto del error
      this.strMsgERROR = jsEngine.get("strMsgERROR").toString();

      return bolResulSentencia;
   }

   /**
    * Este metodo ejecuta la sentencia a evaluar y nos regresa su resultado
    * @param strSentencia Es la sentencia por ejecutar
    * @param oConn Es la conexion a usar
    * @return Nos regresa una cadena con el resultado de la sentencia
    */
   public String EjecutaSentenciaDevuelve(String strSentencia, Conexion oConn) {
      //Nos indica si la ejecucion de la sentencia fue exitosa
      String strResul = "";
      //Objeto que contiene las validaciones por realizar
      Fechas Fecha = new Fechas();
      ResultSet Rs = null;
      Mail mail = new Mail();
      //Objetos para la ejecucion de scripting usamos el SCRIPT default
      ScriptEngineManager mgr = new ScriptEngineManager();
      ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
      //Anadimos objetos para que los tome en cuenta el scripting
      jsEngine.put("Resul", strResul);
      jsEngine.put("Fecha", Fecha);
      jsEngine.put("request", request);
      jsEngine.put("Rs", Rs);
      jsEngine.put("oConn", oConn);
      jsEngine.put("mail", mail);
      jsEngine.put("obj", this);
      jsEngine.put("strMsgERROR", this.strMsgERROR);
      if(this.tabla != null){
         jsEngine.put("tabla", this.tabla);
      }
      jsEngine.put("obj", this);
      if (oConn.isBolMostrarQuerys() == true) {
         System.out.println("**Program to execute**" + strSentencia);
      }
      try {
         jsEngine.eval(strSentencia);
      } catch (ScriptException ex) {
         this.bitacora.GeneraBitacora(ex.getMessage(), "System", "scriptBase", null);
      }
      //Recuperamos el valor booleano que se halla parseado
      strResul = (String) jsEngine.get("Resul");
      //strResulSentencia = strResul;
      if (oConn.isBolMostrarQuerys() == true) {
         System.out.println("**Script Solution**" + strResul);
      }
      //Recuperamos el valor del campo de texto del error
      this.strMsgERROR = jsEngine.get("strMsgERROR").toString();

      return strResul;
   }
   
   /**
    * Crea un nuevo objeto Tabla generica
    * @param NomTabla Es el nombre de la tabla
    * @param NomKey Es el nombre del campo llave
    * @param NomKey2 Es el nombre del campo llave secundario
    * @param NomKey3 Es el nombre del campo llave tercero
    * @param varSesiones Es la variable de sesion
    * @return Nos regresa un nuevo objeto tabla generica
    */
   public CIP_Tabla getCIP_Tabla(String NomTabla,String  NomKey, String NomKey2, String NomKey3, VariableSession varSesiones){
      return new CIP_Tabla(NomTabla, NomKey, NomKey2, NomKey3, varSesiones);
   }
   // </editor-fold>
   
}
