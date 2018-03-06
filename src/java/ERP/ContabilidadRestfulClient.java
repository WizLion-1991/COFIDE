/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import com.siweb.utilerias.json.JSONException;
import com.siweb.utilerias.json.JSONObject;
import comSIWeb.Operaciones.Conexion;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import org.apache.logging.log4j.LogManager;

/**
 * Esta clase realiza las operaciones de cliente de restful para el sistema
 * contable
 *
 * @author ZeusGalindo
 */
public class ContabilidadRestfulClient {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private String strCodigoSesion;
   private String strIdCliente;
   private int intEmpresa;
   private Conexion oConn;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(ContabilidadRestfulClient.class.getName());

   public String getStrCodigoSesion() {
      return strCodigoSesion;
   }

   public void setStrCodigoSesion(String strCodigoSesion) {
      this.strCodigoSesion = strCodigoSesion;
   }

   public int getIntEmpresa() {
      return intEmpresa;
   }

   public void setIntEmpresa(int intEmpresa) {
      this.intEmpresa = intEmpresa;
   }

   public Conexion getoConn() {
      return oConn;
   }

   public void setoConn(Conexion oConn) {
      this.oConn = oConn;
   }

   public String getStrIdCliente() {
      return strIdCliente;
   }

   public void setStrIdCliente(String strIdCliente) {
      this.strIdCliente = strIdCliente;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public ContabilidadRestfulClient() {
      System.setProperty("https.protocols", "TLSv1");
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    * Inicia sesion en restful
    *
    * @return Regresa la sesion nueva
    */
   public String logIn() {
      String strResult = "";
      if (this.strCodigoSesion != null) {
         if (!this.strCodigoSesion.isEmpty() && !this.strCodigoSesion.equals("0")) {
            return this.strCodigoSesion;
         }
      }
      //Obtenemos parametros de url y usuario
      ResultSet rs = null;
      String strUrlRestFul = null;
      String strUser = null;
      String strPassword = null;
      try {
         rs = oConn.runQuery("select EMP_URL_RESTFUL_CP,EMP_USERCP,EMP_PASSCP"
                 + " from vta_empresas where EMP_ID = " + this.intEmpresa, true);
         while (rs.next()) {
            strUrlRestFul = rs.getString("EMP_URL_RESTFUL_CP");
            strUser = rs.getString("EMP_USERCP");
            strPassword = rs.getString("EMP_PASSCP");
         }
         rs.close();
      } catch (SQLException ex) {
         log.debug(ex.getMessage());
      }
      //Abrimos conexion con restful
      try {
         if (strUrlRestFul.isEmpty()) {
            strUrlRestFul = "http://ventas.siwebmx.com:8080/ContabilidadPlus/";
         }
         URL url = new URL(strUrlRestFul + "webresources/access?usuario=" + URLEncoder.encode(strUser, "UTF-8") + "&contrasenia=" + URLEncoder.encode(strPassword, "UTF-8"));
         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
         conn.setRequestMethod("GET");
         conn.setRequestProperty("Accept", "application/json");

         if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
         }
         BufferedReader br = new BufferedReader(new InputStreamReader(
                 (conn.getInputStream())));

         String output;
         StringBuilder strJsonTxt = new StringBuilder();
         while ((output = br.readLine()) != null) {
            strJsonTxt.append(output);
         }
         try {
            JSONObject jsonObj = new JSONObject(strJsonTxt.toString());
            this.strCodigoSesion = (String) jsonObj.get("Codigo");
            strResult = (String) jsonObj.get("Codigo");
            //Faltaria recuperar el id de cliente
         } catch (JSONException ex) {
            Logger.getLogger(ContabilidadRestfulClient.class.getName()).log(Level.SEVERE, null, ex);
         }

         conn.disconnect();
      } catch (MalformedURLException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
      return strResult;
   }

   /**
    * Nos salimos de la sesion
    */
   public void logOut() {
      if (this.strCodigoSesion != null) {
         if (!this.strCodigoSesion.isEmpty() && !this.strCodigoSesion.equals("0")) {
            //Obtenemos parametros de url y usuario
            ResultSet rs;
            String strUrlRestFul = null;
            try {
               rs = oConn.runQuery("select EMP_URL_RESTFUL_CP,EMP_USERCP,EMP_PASSCP"
                       + " from vta_empresas where EMP_ID = " + this.intEmpresa, true);
               while (rs.next()) {
                  strUrlRestFul = rs.getString("EMP_URL_RESTFUL_CP");
               }
               rs.close();
            } catch (SQLException ex) {
               log.debug(ex.getMessage());
            }
            //Abrimos conexion con restful
            try {
               if (strUrlRestFul.isEmpty()) {
                  strUrlRestFul = "http://ventas.siwebmx.com:8080/ContabilidadPlus/";
               }
               URL url = new URL(strUrlRestFul + "webresources/access/exit?Codigo=" + this.strCodigoSesion);
               HttpURLConnection conn = (HttpURLConnection) url.openConnection();
               conn.setRequestMethod("GET");
               conn.setRequestProperty("Accept", "application/json");

               if (conn.getResponseCode() != 200) {
                  throw new RuntimeException("Failed : HTTP error code : "
                          + conn.getResponseCode());
               }
               BufferedReader br = new BufferedReader(new InputStreamReader(
                       (conn.getInputStream())));
               conn.disconnect();
               this.strCodigoSesion = "";
            } catch (MalformedURLException e) {
               e.printStackTrace();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
   }

   /**
    * Regresa todas las polizas en base a rangos
    *
    * @return Regresa la sesion nueva
    */
   public String getPolizas(int intCliente, String strPeriodo, int intRows) {
      String strResult = "";
      if (this.strCodigoSesion != null) {
         if (!this.strCodigoSesion.isEmpty() && !this.strCodigoSesion.equals("0")) {
            //Obtenemos parametros de url y usuario
            ResultSet rs = null;
            String strUrlRestFul = null;
            try {
               rs = oConn.runQuery("select EMP_URL_RESTFUL_CP,EMP_USERCP,EMP_PASSCP"
                       + " from vta_empresas where EMP_ID = " + this.intEmpresa, true);
               while (rs.next()) {
                  strUrlRestFul = rs.getString("EMP_URL_RESTFUL_CP");
               }
               rs.close();
            } catch (SQLException ex) {
               log.debug(ex.getMessage());
            }
            //Abrimos conexion con restful
            try {
               if (strUrlRestFul.isEmpty()) {
                  strUrlRestFul = "http://ventas.siwebmx.com:8080/ContabilidadPlus/";
               }
               BufferedReader br = null;
               //Conexion para descargar el xml
               URL url = new URL(strUrlRestFul + "webresources/tablas?Opcion=1&Abrv=Polizas&Codigo=" + this.strCodigoSesion
                       + "&_search=true&PO_CLIENTE=" + intCliente
                       + "&rows=" + intRows + "&periodo=" + strPeriodo);
               
               HttpURLConnection conn = (HttpURLConnection) url.openConnection();
               if (conn instanceof HttpsURLConnection) {

                  HttpsURLConnection conn1 = (HttpsURLConnection) url.openConnection();

                  conn1.setHostnameVerifier(new HostnameVerifier() {
                     public boolean verify(String hostname, SSLSession session) {
                        return true;
                     }
                  });
                  conn1.setRequestMethod("GET");
                  conn1.setRequestProperty("Accept", "application/xml");

                  if (conn1.getResponseCode() != 200) {
                     throw new RuntimeException("Failed : HTTP error code : "
                             + conn1.getResponseCode());
                  }
                  br = new BufferedReader(new InputStreamReader(
                          (conn1.getInputStream())));
               } else {
                  conn.setRequestMethod("GET");
                  conn.setRequestProperty("Accept", "application/xml");

                  if (conn.getResponseCode() != 200) {
                     throw new RuntimeException("Failed : HTTP error code : "
                             + conn.getResponseCode());
                  }
                  br = new BufferedReader(new InputStreamReader(
                          (conn.getInputStream())));
               }



               String output;
               StringBuilder strJsonTxt = new StringBuilder();
               if (br != null) {
                  while ((output = br.readLine()) != null) {
                     strJsonTxt.append(output);
                  }
               }
               strResult = strJsonTxt.toString();

               conn.disconnect();
            } catch (MalformedURLException e) {
               e.printStackTrace();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
      return strResult;
   }
   // </editor-fold>
}
