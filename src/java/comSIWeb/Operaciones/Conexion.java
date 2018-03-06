package comSIWeb.Operaciones;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletContext;
import comSIWeb.Utilerias.Fechas;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Construimos el objeto conexion
 */
public class Conexion {

   /**
    * Objeto para el manejo de errores
    */
   protected Bitacora bitacora;
   /**
    * URL de la conexion
    */
   protected String strURLConexion;
   /**
    * Usuario de la conexion
    */
   protected String strUserBD;
   /**
    * Password de la conexion
    */
   private String strPasswordBD;
   /**
    * Tipo de manejador de bd
    */
   protected String strManejadorBD;
   /**
    * Tipo de manejador de bd
    */
   protected String strNomPool;
   /**
    * Declaramos el objeto conecion de JDBC
    */
   protected Connection conexion;
   /**
    * Es el tipo de error
    */
   /**
    * Nos muestra los querys ejecutados
    */
   protected boolean bolMostrarQuerys = false;
   protected boolean bolEsError = false;
   protected String strMsgError;
   protected Fechas fecha;
   protected String strUsuario;
   protected boolean bolCleanError;
   protected boolean bolUsingPool;
   private static final Logger log = LogManager.getLogger(Conexion.class.getName());
   protected String strBaseDatos;
   protected String strDriverName;

   public String getStrNomPool() {
      return strNomPool;
   }

   public void setStrNomPool(String strNomPool) {
      this.strNomPool = strNomPool;
   }

   /**
    * Es el constructor de una conexion, abre directamente la conexion
    *
    * @param strUsuario Es el usuario que esta usando la aplicacion
    * @param context Es el objeto que contiene las variables de contexto
    * @throws java.lang.Exception Excepcion
    */
   public Conexion(String strUsuario, ServletContext context) throws Exception {

      conexion = null;
      strBaseDatos = null;
      strMsgError = "";
      //20/12/2012 Quitamos la obtencion del driver porque usaremos Pool
      //Eliminamos
      //Class.forName("com.mysql.jdbc.Driver").newInstance();

      //Instanciamos objetos de fecha y bitacroa
      this.bitacora = new Bitacora();
      this.fecha = new Fechas();
      this.bolCleanError = true;
      //Obtenemos la configuracion para la bd
      this.strURLConexion = context.getInitParameter("URLConexion");
      this.strUserBD = context.getInitParameter("UserBd");
      this.strPasswordBD = context.getInitParameter("PasswordBd");
      this.strManejadorBD = context.getInitParameter("ManejadorBd");
      this.strUsuario = strUsuario;
      this.strMsgError = "";
      if (context.getInitParameter("debug") == null) {
         bolMostrarQuerys = false;
      } else if (context.getInitParameter("debug").equals("SI")) {
         bolMostrarQuerys = true;
      } else {
         bolMostrarQuerys = false;
      }
      bolUsingPool = true;
      this.strNomPool = "jdbc/SIWEBDB";
   }

   /**
    * Es el constructor de una conexion Esta vacio y no realiza ninguna
    * operacion
    */
   public Conexion() {
      conexion = null;
      strBaseDatos = null;
      strMsgError = "";
      //Instanciamos objetos de fecha y bitacroa
      this.bitacora = new Bitacora();
      this.fecha = new Fechas();
      this.bolCleanError = true;
      this.strMsgError = "";
      bolUsingPool = true;
      this.strNomPool = "jdbc/SIWEBDB";
   }

   /**
    * Es el constructor de una conexion, abre directamente la conexion
    *
    * @param ConexionURL Es un arreglo de String con la cadena de conexion:
    * URLConexion, UserBd, PasswordBd,ManejadorBd
    * @param strUsuario Es el usuario que esta usando la base de datos
    * @param strDriver Es el driver para la conexion
    * @throws java.lang.Exception Excepcion
    */
   public Conexion(String[] ConexionURL, String strUsuario, String strDriver) throws Exception {
      conexion = null;
      strBaseDatos = null;
      strMsgError = "";
      Class.forName(strDriver).newInstance();
      this.strUsuario = strUsuario;
      //Instanciamos el objeto que manipula los errores
      //Instanciamos objetos de fecha y bitacroa
      this.bitacora = new Bitacora();
      this.fecha = new Fechas();
      this.bolCleanError = true;
      //Obtenemos la configuracion para la bd
      this.strURLConexion = ConexionURL[0];
      this.strUserBD = ConexionURL[1];
      this.strPasswordBD = ConexionURL[2];
      this.strManejadorBD = ConexionURL[3];
      this.strMsgError = "";
      bolUsingPool = false;
      this.strNomPool = "jdbc/SIWEBDB";
   }

   /**
    * Es el constructor de una conexion, abre directamente la conexion
    *
    * @param ConexionURL Es un arreglo de String con la cadena de conexion:
    * URLConexion, UserBd, PasswordBd,ManejadorBd
    * @param strUsuario Es el usuario que esta usando la base de datos
    * @throws java.lang.Exception Excepcion
    */
   public Conexion(String[] ConexionURL, String strUsuario) throws Exception {
      conexion = null;
      strBaseDatos = null;
      strMsgError = "";
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      this.strUsuario = strUsuario;
      //Instanciamos el objeto que manipula los errores
      //Instanciamos objetos de fecha y bitacroa
      this.bitacora = new Bitacora();
      this.fecha = new Fechas();
      this.bolCleanError = true;
      //Obtenemos la configuracion para la bd
      this.strURLConexion = ConexionURL[0];
      this.strUserBD = ConexionURL[1];
      this.strPasswordBD = ConexionURL[2];
      this.strManejadorBD = ConexionURL[3];
      this.strMsgError = "";
      bolUsingPool = false;
      this.strNomPool = "jdbc/SIWEBDB";
   }

   public String getStrBaseDatos() {
      return strBaseDatos;
   }

   public void setStrBaseDatos(String strBaseDatos) {
      this.strBaseDatos = strBaseDatos;
   }

   /**
    * Abre la coneccion a la base de datos.
    *
    * @return regresamos un objeto conexiÃ³n ya conectado
    */
   public Connection open() {
      //Validamos si usamos el pool de conexiones
      if (bolUsingPool) {
         openPool();
      } else {
         if (this.bolCleanError) {
            strMsgError = "";
            bolEsError = false;
         }
         /*Liberamos memoria*/
         Runtime.getRuntime().gc();
         Runtime.getRuntime().freeMemory();
         try {
            /*
             String URL_bd="jdbc:mysql://localhost:3306/mc";
             String usuario ="root";
             String contrasena = "solsticio";
             */
            //Conectar con la BD
            conexion = DriverManager.getConnection(strURLConexion, this.strUserBD, this.strPasswordBD);

//            if (bolMostrarQuerys == true) {
//               System.out.println(this.fecha.getFechaActual() + " "
//                       + this.fecha.getHoraActual() + " " + " Conexion realizada con Exito" + this.conexion.toString());
//            }
            log.debug("Conexion realizada con Exito");
            //Identifica el driver
            DatabaseMetaData dmd = this.getConexion().getMetaData();
            this.strDriverName = dmd.getDriverName();
            log.debug("url: " + dmd.getDriverName());
         } catch (SQLException ex) {
            bolEsError = true;
            strMsgError = "ERROR" + ex.getMessage();
            log.error(ex.getSQLState() + " " + ex.getLocalizedMessage() + " " + ex.getMessage() + " " + strURLConexion);
//            System.out.println(this.fecha.getFechaActual() + " "
//                    + this.fecha.getHoraActual() + " " + "Error en conexion: " + strMsgError);
//            //cError.RegistraErrorTxt
//            System.out.println(this.getClass().getCanonicalName() + ex.getSQLState() + " " + ex.getLocalizedMessage() + " " + strURLConexion);
            return null;
         } catch (Exception ex) {
            strMsgError = "ERROR: " + ex.getMessage();
            log.error(" " + ex.getLocalizedMessage() + " " + ex.getMessage() + " " + strURLConexion);
//            System.out.println(this.fecha.getFechaActual() + " "
//                    + this.fecha.getHoraActual() + " " + " Error en conexion 2 " + strMsgError + ex.getMessage());
//            System.out.println(this.getClass().getCanonicalName() + " " + ex.getLocalizedMessage() + " " + strURLConexion);
            return null;
         }
      }

      return conexion;
   }

   /**
    * Nos conectamos usan un Pool de conexiones
    *
    * @return
    */
   public Connection openPool() {
      try {
         Context initCtx = new InitialContext();
         Context envCtx = (Context) initCtx.lookup("java:comp/env");
         DataSource ds = (DataSource) envCtx.lookup(this.strNomPool);
         this.conexion = ds.getConnection();
         //Identifica el driver
         DatabaseMetaData dmd = this.getConexion().getMetaData();
         this.strDriverName = dmd.getDriverName();
      } catch (SQLException ex) {
         log.error(ex.getSQLState() + " " + ex.getLocalizedMessage() + " " + ex.getMessage() + " " + strURLConexion);
      } catch (NamingException ex) {
         log.error(" " + ex.getLocalizedMessage() + " " + ex.getMessage() + " " + strURLConexion);
      }
      return this.conexion;
   }

   /**
    * Cierra la coneccion a la base de datos.
    */
   public void close() {
      if (this.bolCleanError) {
         strMsgError = "";
         bolEsError = false;
      }
      try {
         if (conexion != null) {
            conexion.close();
         }
//         if (bolMostrarQuerys == true) {
//            System.out.println(this.fecha.getFechaActual() + " "
//                    + this.fecha.getHoraActual() + " " + " Conexion cerrada con exito");
//         }
         log.debug("Conexion cerrada con exito");
      } catch (Exception ex) {
         this.bolEsError = true;
         strMsgError = "ERROR: " + ex.toString();
         log.error(ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
//         System.out.println(this.fecha.getFechaActual() + " "
//                 + this.fecha.getHoraActual() + " " + "error al cerrar la conexion " + strMsgError);
//         e.fillInStackTrace();
      }
   }

   /**
    * Ejecuta un query que no regresa un ResultSet aplica para queries INSERT,
    * UPDATE y DELETE. Devuelve el numero de filas se vieron afectadas. Si desea
    * ejecutar una consulta SELECT utilizar el metodo correrQuery.
    *
    * @param query del query
    * @return La cantidad de filas afectadas.
    */
   public int runQueryLMD(String query) {
      int rowsAffected = 0;
      if (this.bolCleanError) {
         strMsgError = "";
         bolEsError = false;
      }
      if (conexion != null) {
         //Tratamos de ejecutar el query
         try {
            Statement s = conexion.createStatement();
//            if (this.bolMostrarQuerys == true) {
//               System.out.println(this.fecha.getFechaActual() + " "
//                       + this.fecha.getHoraActual() + " " + query);
//            }
            log.debug(query);
            try {
               rowsAffected = s.executeUpdate(query);
            } catch (Exception e) {
               this.bolEsError = true;
               strMsgError = "ERROR: " + e.getLocalizedMessage();
//               System.out.println(this.fecha.getFechaActual() + " "
//                       + this.fecha.getHoraActual() + " " + "error al ejecutar query " + e.getMessage() + " QUERY:" + query);
               log.error(e.getLocalizedMessage() + " " + e.getMessage() + " ");
               if (this.bolMostrarQuerys == true) {
                  this.bitacora.GeneraBitacora(strMsgError, this.strUsuario, "", this);
               }
            }
            s.close();
         } catch (SQLException ex) {
            strMsgError = "ERROR: " + ex.getLocalizedMessage();
            this.bolEsError = true;
            log.error(ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
         }
      }
      return rowsAffected;
   }

   /**
    * Ejecuta un query devuelve un ResultSet. Utilice este metodo para ejecutar
    * queries SELECT.
    *
    * @return el ResultSet.
    * @param query del query.
    * @param bolSoloFordward Indicamos que se genere un recordset de solo
    * adelante
    * @throws java.sql.SQLException Nos regresa una excepcion de Sql
    */
   public ResultSet runQuery(String query, boolean bolSoloFordward) throws SQLException {
      Statement s = null;
//      

      if (this.bolCleanError) {
         strMsgError = "";
         bolEsError = false;
      }
      if (conexion != null) {
         if (strBaseDatos == null) {
            if (this.strDriverName.contains("MySQL")) {
               if (bolSoloFordward == true) {
                  s = conexion.createStatement(ResultSet.FETCH_FORWARD, ResultSet.CONCUR_READ_ONLY);
               } else {
                  s = conexion.createStatement(ResultSet.FETCH_REVERSE, ResultSet.CONCUR_READ_ONLY);
               }
            } else {
               s = conexion.createStatement();
            }
         } else {
            s = conexion.createStatement();
         }

      }
      ResultSet rs = null;
      if (conexion != null) {
//         if (this.bolMostrarQuerys == true) {
//            System.out.println(this.fecha.getFechaActual() + " "
//                    + this.fecha.getHoraActual() + " " + query);
//         }
         log.debug(query);
         try {
            rs = s.executeQuery(query);
         } catch (SQLException ex) {
            strMsgError = "ERROR: " + ex.toString();
            this.bolEsError = true;
            log.error(ex.getLocalizedMessage() + " " + ex.getMessage() + " ");

         } catch (Exception e) {
            this.bitacora.GeneraBitacora(query + this.getClass().getCanonicalName(), this.strUsuario, "", this);
            strMsgError = "ERROR:" + e.toString();
            bolEsError = true;
//            System.out.println(this.fecha.getFechaActual() + " " + this.fecha.getHoraActual() + " " + "error al obtener resulset " + e.getMessage() + " QUERY:" + query);
            log.error(e.getLocalizedMessage() + " " + e.getMessage() + " ");
         }
      }
      return rs;
   }

   /**
    * Ejecuta un query devuelve un ResultSet. Utilice este metodo para ejecutar
    * queries SELECT.
    *
    * @return el ResultSet.
    * @param query del query.
    * @throws java.sql.SQLException Nos regresa una excepcion de Sql
    */
   public ResultSet runQuery(String query) throws SQLException {
      ResultSet rs = this.runQuery(query, false);
      return rs;
   }

   /**
    * Nos regresa el nombre de la base de datos de la conexion actual
    *
    * @return Nos regresa una cadena con el nombre de la base de datos
    */
   public String RegresaBaseDatos() {
      if (this.bolCleanError) {
         strMsgError = "";
         bolEsError = false;
      }
      /**
       * Es el nombre de la base de datos
       */
      String strNombreBase = "";
      try {
         //Obtenemos el nombre de la base de datos
         ResultSet rs = null;
         rs = this.runQuery("SELECT DATABASE();");
         while (rs.next()) {
            strNombreBase = rs.getString(1);
         }
         if (rs.getStatement() != null) {
            //rs.getStatement().close();
         }
         rs.close();
      } catch (SQLException ex) {
         strMsgError = "ERROR: " + ex.toString();
         bolEsError = true;
         this.bitacora.GeneraBitacora(ex.getMessage(), this.strUsuario, "", this);
         log.error(ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
//         System.out.println(this.fecha.getFechaActual() + " "
//                 + this.fecha.getHoraActual() + " " + "error al obtener resulset " + ex.getMessage());
         ex.printStackTrace();
      }
      return strNombreBase;
   }

   /**
    * Nos regresa el valor de la propiedad mostrar querys
    *
    * @return un valor boolean con true en caso de mostrar los querys
    */
   public boolean isBolMostrarQuerys() {
      return bolMostrarQuerys;
   }

   /**
    * Definimos el valor de la propiedad mostrar querys
    *
    * @param bolMostrarQuerys un valor boolean con true en caso de querer
    * mostrar los querys
    */
   public void setBolMostrarQuerys(boolean bolMostrarQuerys) {
      this.bolMostrarQuerys = bolMostrarQuerys;
   }

   /**
    * Cierra la conexion en caso de quedar abierta
    *
    * @throws java.lang.Throwable Genera un error
    */
   @Override
   protected void finalize() throws Throwable {
      try {
         if (conexion != null) {
            if (conexion.isClosed() == false) {
               conexion.close();
               log.debug("Conexion cerrada con exito en metodo finalizar");
//               System.out.println(this.fecha.getFechaActual() + " "
//                       + this.fecha.getHoraActual() + " " + " Conexion cerrada con exito en metodo finalizar");
            }
         }
      } catch (Exception e) {
         bolEsError = true;
         strMsgError = e.toString();
         log.error(e.getLocalizedMessage() + " " + e.getMessage() + " ");
//         System.out.println(this.fecha.getFechaActual() + " "
//                 + this.fecha.getHoraActual() + " " + "error al cerrar la conexion en metodo finalizar " + strMsgError);
         this.bitacora.GeneraBitacora(strMsgError, this.strUsuario, "", this);
      }
      //do finalization here
      super.finalize(); //not necessary if extending Object.
   }

   /**
    * Regresa el mensaje de error del ultimo query ejecutado
    *
    * @return Regresa una cadena con el mensaje de error del ultimo query
    * ejecutado
    */
   public String getStrMsgError() {
      return strMsgError;
   }

   /**
    * Nos indica si se genero un error en la ultima consulta
    *
    * @return Nos regres TRUE/FALSE
    */
   public boolean isBolEsError() {
      return bolEsError;
   }

   /**
    * Nos regresa el usuario que esta usando el sistema
    *
    * @return Regresa una cadena con el nombre del usuario
    */
   public String getStrUsuario() {
      return strUsuario;
   }

   /**
    * Nos dice si limpia el error al ejecutar un query
    *
    * @return es un valor boolean
    */
   public boolean isBolCleanError() {
      return bolCleanError;
   }

   /**
    * Definimos si Limpiamos los errores
    *
    * @param bolCleanError es un valor boolean
    */
   public void setBolCleanError(boolean bolCleanError) {
      this.bolCleanError = bolCleanError;
   }

   /**
    * Recupera el objeto conexion
    *
    * @return Regresa el objeto conexion
    */
   public Connection getConexion() {
      return conexion;
   }

   /**
    * Nos regresa el manejador de base de datos que estemos usando
    *
    * @return Regresa el manejador de base de datos
    */
   public String getStrManejadorBD() {
      return strManejadorBD;
   }

   public String getStrDriverName() {
      return strDriverName;
   }

   public void setStrDriverName(String strDriverName) {
      this.strDriverName = strDriverName;
   }

}
