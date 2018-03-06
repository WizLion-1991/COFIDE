/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import Tablas.vta_datos_archivo;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.Fechas;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *Esta clase procesa los archivos de cobranza del layout
 * @author aleph_79
 */
public class CobranzaLayouts {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   public ArrayList<TableMaster> lstCols = null;
   public int intCPM_ID;
   public int intBc_Id;
   public Conexion oConn;
   public VariableSession varSesiones;
   public String strSeparador;
   private static final Logger log = LogManager.getLogger(CobranzaLayouts.class.getName());
   protected boolean bolLinkPedidos = false;

   /**
    * Regresa la conexion a la base de datos
    * @return Regresa un objeto conexion
    */
   public Conexion getoConn() {
      return oConn;
   }

   /**
    * Define la conexion a la base de datos
    * @param oConn Es un objeto conexion
    */
   public void setoConn(Conexion oConn) {
      this.oConn = oConn;
   }

   public int getCPM_ID() {
      return intCPM_ID;
   }

   public void setCPM_ID(int CPM_ID) {
      this.intCPM_ID = CPM_ID;
   }

   public String getStrSeparador() {
      return strSeparador;
   }

   public void setStrSeparador(String strSeparador) {
      this.strSeparador = strSeparador;
   }

   public ArrayList<TableMaster> getLstCols() {
      return lstCols;
   }

   public void setLstCols(ArrayList<TableMaster> lstCols) {
      this.lstCols = lstCols;
   }

   /**
    * Regresa el id del banco
    * @return Es el id del banco al que afectara el movimiento
    */
   public int getIntBc_Id() {
      return intBc_Id;
   }

   /**
    * Define el id del banco
    * @param intBc_Id Es el id del banco al que afectara el movimiento
    */
   public void setIntBc_Id(int intBc_Id) {
      this.intBc_Id = intBc_Id;
   }
   /**
    * Nos indica si se vincula el anticipo con los pedidos pendientes por facturar del cliente
    * @return Es un valor boolean
    */
   public boolean isBolLinkPedidos() {
      return bolLinkPedidos;
   }

   /**
    * Vincula el anticipo con los pedidos pendientes por facturar del cliente
    * @param bolLinkPedidos Es un valor boolean
    */
   public void setBolLinkPedidos(boolean bolLinkPedidos) {
      this.bolLinkPedidos = bolLinkPedidos;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   /**
    * Constructor default
    * @param intCPM_ID Es el id del archivo de cobranza
    * @param oConn Es la conexion a la base de datos
    * @param varSesiones Son las variables de sesion 
    */
   public CobranzaLayouts(int intCPM_ID, Conexion oConn, VariableSession varSesiones) {
      this.lstCols = new ArrayList<TableMaster>();
      this.intCPM_ID = intCPM_ID;
      this.oConn = oConn;
      this.varSesiones = varSesiones;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    * Inicializa la estructura de datos
    */
   public void Init() {
      String strSql = "select CPM_SEPARADOR from vta_conf_p_master where CPM_ID = " + this.intCPM_ID;
      ResultSet rs;
      try {
         rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            //this.strSeparador = rs.getString("CPM_SEPARADOR");
             this.strSeparador = "\\|";
         }
         rs.close();
      } catch (SQLException ex) {
         log.error("SQLException:" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getLocalizedMessage());
      }
      //Cargamos los campos a detalle
      vta_datos_archivo lstDatos = new vta_datos_archivo();
      this.lstCols = lstDatos.ObtenDatosVarios(" CPM_ID = " + this.intCPM_ID + " Order by CPMD_ORDEN", this.oConn);
   }

   /**
    * Procesa el archivo de cobranza indicado como parametro
    * @param strFilePath Es el nombre del archivo de cobranza
    * @return Regresa OK si fue correcto toda la operación
    */
   public String ProcesaLayout(String strFilePath) {
      String strResp = "OK";
      File file = new File(strFilePath);
      //Validamos que exista el archivo
      if (file.exists()) {
         //Lo recorremos
         try {
            // Open the file that is the first 
            // command line parameter
            FileInputStream fstream = new FileInputStream(file);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            //Read File Line By Line
            while ((strLine = br.readLine()) != null) {
               // Print the content on the console
               System.out.println(strLine);
               strResp = ParserLine(strLine);
//               if(!strResp.equals("OK")){
//                  break;
//               }
            }
            //Close the input stream
            in.close();
         } catch (Exception ex) {//Catch exception if any
            log.error(" Exception file:" + ex.getMessage() + " " + ex.getLocalizedMessage());
         }

      }
      return strResp;
   }

   /**
    * Parsea una fila del archivo txt
    * @param strLine Es la linea del archivo 
    * @return Regresa Ok si todo fue correcto
    */
   public String ParserLine(String strLine) {
      //Valor que usaremos para el archivo
      int intCliente = 0;
      String strReferencia1 = "";
      String strFecha = "";
      String strHora = "";
      double dblImporte = 0;
      //Procesamos la fila
      String strResp = "OK";
      String[] lstColsTxt = strLine.split(this.strSeparador);
      for (int i = 0; i < lstColsTxt.length; i++) {
         String strValor = lstColsTxt[i];
         try {
            //for(int t=0;t<this.lstCols.size();t++){
            TableMaster tbn = this.lstCols.get(i);
            String strNombreCampo = tbn.getFieldString("CPMD_NOMBRE_CAMPO");
            //Validamos cada tipo de campo
            if (strNombreCampo.equals("$Cliente")) {
               try {
                  intCliente = Integer.valueOf(strValor);
               } catch (NumberFormatException ex) {
                  strResp = "ERROR:" + ex.getMessage() + " " + ex.getLocalizedMessage() + " " + i;
                  log.error("NumberFormatException:" + ex.getMessage() + " " + ex.getLocalizedMessage());
               }
            }
            if (strNombreCampo.equals("$Importe")) {
               try {
                  dblImporte = Double.valueOf(strValor);
               } catch (NumberFormatException ex) {
                  strResp = "ERROR:" + ex.getMessage() + " " + ex.getLocalizedMessage() + " " + i;
                  log.error("NumberFormatException:" + ex.getMessage() + " " + ex.getLocalizedMessage());
               }
            }
            //Si es referencia buscamos el id del cliente
            if (strNombreCampo.equals("$Referencia1")) {
               strReferencia1 = strValor;
               String strSql = "select CT_ID from "
                       + " vta_cliente where "
                       + "    CT_RBANCARIA1 = '" + strReferencia1 + "' "
                       + " or CT_RBANCARIA1 = '" + strReferencia1 + "' "
                       + " or CT_RBANCARIA1 = '" + strReferencia1 + "' ";
               ResultSet rs;
               try {
                  rs = this.oConn.runQuery(strSql);
                  while (rs.next()) {
                     intCliente = rs.getInt("CT_ID");
                  }
                  rs.close();
               } catch (SQLException ex) {
                  log.error("SQLException:" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getLocalizedMessage());
               }
            }
            if (strNombreCampo.equals("$Fecha")) {
               strFecha = strValor;
               SimpleDateFormat formatoDeFecha = new SimpleDateFormat(tbn.getFieldString("CPMD_EXP_REG"));
               Date date1 = null;
               try {
                  date1 = formatoDeFecha.parse(strFecha);
                  Fechas fecha = new Fechas();
                  strFecha = fecha.getFechaDate(date1);

               } catch (ParseException ex) {
                  log.error("ParseException:" + tbn.getFieldString("CPMD_EXP_REG") + " " + ex.getMessage() + " " + ex.getLocalizedMessage());
               }
            }
            if (strNombreCampo.equals("$Hora")) {
               strHora = strValor;
            }
            //}
         } catch (IndexOutOfBoundsException ex) {
            strResp = "ERROR:" + ex.getMessage() + " " + ex.getLocalizedMessage() + " " + i;
            log.error("IndexOutOfBoundsException:" + ex.getMessage() + " " + ex.getLocalizedMessage());
         }
      }
      log.debug(intCliente + " " + strReferencia1 + " " + strFecha + " " + dblImporte);
      //Evaluamos si contamos con los campos minimos para registrar el pago
      if (intCliente == 0) {
         strResp = "ERROR:Requiere de colocar el numero de cliente o referencia" + strResp;
         return strResp;
      }
      if (strFecha.isEmpty()) {
         strResp = "ERROR:Requiere de colocar una fecha " + strResp;
         return strResp;
      }
      if (dblImporte == 0) {
         strResp = "ERROR:Requiere de colocar un monto " + strResp;
         return strResp;
      }
      //Paso todas las validaciones entonces aplicamos el pago
//      Instanciamos el objeto que nos trae las listas de precios
      movCliente movCte = new movCliente(oConn, this.varSesiones, null);
      //Inicializamos objeto
      movCte.Init();
      //Buscamos el banco donde aplicara
      if (intBc_Id != 0) {
         movCte.setIntBc_Id(intBc_Id);
      } else {
         movCte.setBolCaja(true);
      }
//      //Recibimos datos para el encabezado
      movCte.getCta_clie().setFieldString("MC_FECHA", strFecha);
      movCte.getCta_clie().setFieldString("MC_NOTAS", "Archivo de cobranza ");
//      movCte.getCta_clie().setFieldInt("MC_MONEDA", Integer.valueOf(request.getParameter("MONEDA")));
      movCte.getCta_clie().setFieldDouble("MC_TASAPESO", 1);
      movCte.getCta_clie().setFieldInt("CT_ID", intCliente);
      movCte.getCta_clie().setFieldInt("FAC_ID", 0);
      movCte.getCta_clie().setFieldInt("TKT_ID", 0);
      movCte.getCta_clie().setFieldInt("MC_ESPAGO", 1);
      movCte.getCta_clie().setFieldInt("MC_ANTICIPO", 1);
      movCte.getCta_clie().setFieldInt("EMP_ID", varSesiones.getIntIdEmpresa());
      movCte.getCta_clie().setFieldInt("SC_ID", varSesiones.getIntSucursalDefault());
      movCte.getCta_clie().setFieldDouble("MC_ABONO", dblImporte);
      if(this.bolLinkPedidos){
         movCte.setBolLinkPedidos(true);
      }

      //Generamos transaccion
      movCte.doTrx();
      if (movCte.getStrResultLast().equals("OK")) {
         strResp = "OK." + movCte.getCta_clie().getValorKey();
      } else {
         strResp = movCte.getStrResultLast();
      }
      return strResp;
   }
// </editor-fold>
}
