/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import Tablas.Factura_contratos;
import Tablas.vta_facturadeta;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.ServletContext;
import org.apache.logging.log4j.LogManager;
import org.apache.struts.util.MessageResources;

public class FacturaContratos extends ProcesoMaster implements ProcesoInterfaz {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   protected ArrayList<Factura_contratos> lstContratos;
   protected int intEMP_ID = 0;
   protected int intSC_ID = 0;
   protected int intCTE_ID = 0;
   protected boolean bolAplica = false;
   protected String strFechaInicio;
   protected String strFechaFinal;
   protected String strFechaFactura;
   protected ServletContext context;
   protected String strPathPrivateKeys;
   protected String strPathXML;
   protected String strPathFonts;
   protected String strMyPassSecret;
   protected String strSerie;
   private MessageResources msgRes;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(FacturaContratos.class.getName());

   public ArrayList<Factura_contratos> getLstContratos() {
      return lstContratos;
   }

   public void setLstContratos(ArrayList<Factura_contratos> lstContratos) {
      this.lstContratos = lstContratos;
   }

   public void setStrSerie(String strSerie) {
      this.strSerie = strSerie;
   }

   public int getIntEMP_ID() {
      return intEMP_ID;
   }

   public void setIntEMP_ID(int intEMP_ID) {
      this.intEMP_ID = intEMP_ID;
   }

   public int getIntSC_ID() {
      return intSC_ID;
   }

   public void setIntSC_ID(int intSC_ID) {
      this.intSC_ID = intSC_ID;
   }

   public int getIntCTE_ID() {
      return intCTE_ID;
   }

   public void setIntCTE_ID(int intCTE_ID) {
      this.intCTE_ID = intCTE_ID;
   }

   public String getStrFechaInicio() {
      return strFechaInicio;
   }

   public void setStrFechaInicio(String strFechaInicio) {
      this.strFechaInicio = strFechaInicio;
   }

   public String getStrFechaFinal() {
      return strFechaFinal;
   }

   public void setStrFechaFinal(String strFechaFinal) {
      this.strFechaFinal = strFechaFinal;
   }

   public String getStrFechaFactura() {
      return strFechaFactura;
   }

   public void setStrFechaFactura(String strFechaFactura) {
      this.strFechaFactura = strFechaFactura;
   }

   public boolean isBolAplica() {
      return bolAplica;
   }

   public void setBolAplica(boolean bolAplica) {
      this.bolAplica = bolAplica;
   }

   public ServletContext getContext() {
      return context;
   }

   public void setContext(ServletContext context) {
      this.context = context;
   }

   public String getStrPathPrivateKeys() {
      return strPathPrivateKeys;
   }

   public void setStrPathPrivateKeys(String strPathPrivateKeys) {
      this.strPathPrivateKeys = strPathPrivateKeys;
   }

   public String getStrPathXML() {
      return strPathXML;
   }

   public void setStrPathXML(String strPathXML) {
      this.strPathXML = strPathXML;
   }

   public String getStrPathFonts() {
      return strPathFonts;
   }

   public void setStrPathFonts(String strPathFonts) {
      this.strPathFonts = strPathFonts;
   }

   public String getStrMyPassSecret() {
      return strMyPassSecret;
   }

   public void setStrMyPassSecret(String strMyPassSecret) {
      this.strMyPassSecret = strMyPassSecret;
   }

   public MessageResources getMsgRes() {
      return msgRes;
   }

   /**
    * Define el objeto para recuperar los mensajes de struts
    *
    * @param msgRes Es el mensaje de struts
    */
   public void setMsgRes(MessageResources msgRes) {
      this.msgRes = msgRes;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructor">
   public FacturaContratos(int intEMP_ID, int intSC_ID, int intCTE_ID, String strFechaInicio, String strFechaFinal, String strFechaFactura, Conexion oConn, VariableSession varSesiones, String strSerie) {
      super(oConn, varSesiones);
      this.intEMP_ID = intEMP_ID;
      this.intSC_ID = intSC_ID;
      this.intCTE_ID = intCTE_ID;
      this.strFechaInicio = strFechaInicio;
      this.strFechaFinal = strFechaFinal;
      this.strFechaFactura = strFechaFactura;
      this.strSerie = strSerie;
      this.lstContratos = new ArrayList<Factura_contratos>();
   }

   public FacturaContratos(Conexion oConn, VariableSession varSesiones) {
      super(oConn, varSesiones);
      this.lstContratos = new ArrayList<Factura_contratos>();
   }// </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   public void Init() {
   }

   @Override
   public void doTrx() {
      //Iniciamos valores
      this.strResultLast = "OK";

      //Consultamos datos
      double dblTasaxmesTIIE = 0.00;
      double dblTasaxmesCPP = 0.00;
      int intEMP_TIPOPERS = 0;
      int intEMP_NO_ISR = 0;
      int intEMP_NO_IVA = 0;
      double dblFacRetISR = 10;
      try {
         String strSql = "select CTOA_ARRENDAMIENTO AS descripcion, CTOA_MTO_ARRENDAMIENTO as rentamensual, CTOA_CPP as ccp"
            + " ,CTOA_TEXTO_CPP"
            + " ,vta_contrato_arrend.CTE_ID"
            + " ,CTOA_FOLIO"
            + " ,CTOA_INICIO"
            + " ,CTOA_VENCIMIENTO"
            + " ,CTOA_ID"
            + " ,vta_contrato_arrend.SC_ID"
            + " ,vta_contrato_arrend.EMP_ID"
            + " ,vta_contrato_arrend.MON_ID"
            + " ,CTE_IDF"
            + " ,CTOA_VCP"
            + " ,CTOA_TIPO_FACTOR_AJUSTE"
            + ",CTOA_MENSUALIDAD_ACTUAL "
            + ",CTOA_MES "
            + ",EMP_TIPOPERS"
            + ",EMP_NO_ISR "
            + ",EMP_NO_IVA  "
            + ",CTOA_CC1_ID  "
            + " FROM vta_empresas, vta_contrato_arrend  "
            + " where vta_contrato_arrend.SC_ID= '" + this.intSC_ID + "'"
            + " and vta_empresas.EMP_ID =  vta_contrato_arrend.EMP_ID "
            + " AND vta_contrato_arrend.EMP_ID= '" + this.intEMP_ID + "'"
            + " and  CTOA_VENCIMIENTO >= '" + this.strFechaInicio + "'  "
            + " and CTOA_ACTIVO = 1"
            + " and CTOA_MENSUALIDAD_ACTUAL < CTOA_MES  ";
         if (this.intCTE_ID > 0) {
            strSql += " AND CTE_ID= '" + this.intCTE_ID + "'";
         }
         log.debug("Query:" + strSql);
         ResultSet rs = oConn.runQuery(strSql);
         while (rs.next()) {
            intEMP_TIPOPERS = rs.getInt("EMP_TIPOPERS");
            intEMP_NO_ISR = rs.getInt("EMP_NO_ISR");
            intEMP_NO_IVA = rs.getInt("EMP_NO_IVA");
            //Evaluamos si hay una factura en este periodo relacionado con el contrato
            int intCuantos = 0;
            String strSqlHay = "select count(FAC_ID) as cuantos from vta_facturas "
               + " where CTOA_ID = " + rs.getInt("CTOA_ID") + ""
               + "  and left(FAC_FECHA,6) = '" + this.strFechaFactura.substring(0, 6) + "' "
               + "  AND FAC_ANULADA = 0";
            ResultSet rs2 = oConn.runQuery(strSqlHay);
            while (rs2.next()) {
               intCuantos = rs2.getInt("cuantos");
            }
            rs2.close();
            //Llenamos el arreglo con instancias de contratos que no tengan facturas emitidas
            if (intCuantos == 0) {
               Factura_contratos obj = new Factura_contratos();
               obj.setFieldInt("CTOA_ID", rs.getInt("CTOA_ID"));
               obj.setFieldInt("CTE_ID", rs.getInt("CTE_ID"));
               obj.setFieldInt("CTOA_MENSUALIDAD_ACTUAL", rs.getInt("CTOA_MENSUALIDAD_ACTUAL"));
               obj.setFieldInt("CTOA_MES", rs.getInt("CTOA_MES"));
               //Evaluamos si tiene asignado un cliente para facturacion
               //entonces tomamos en cuenta a ese cliente
               if (rs.getInt("CTE_IDF") != 0) {
                  obj.setFieldInt("CTE_ID", rs.getInt("CTE_IDF"));
               }
               obj.setFieldInt("EMP_ID", rs.getInt("EMP_ID"));
               obj.setFieldInt("SC_ID", rs.getInt("SC_ID"));
               obj.setFieldInt("MON_ID", rs.getInt("MON_ID"));
               obj.setFieldInt("CTOA_TIPO_FACTOR_AJUSTE", rs.getInt("CTOA_TIPO_FACTOR_AJUSTE"));
               obj.setFieldString("CTOA_INICIO", rs.getString("CTOA_INICIO"));
               obj.setFieldString("CTOA_VENCIMIENTO", rs.getString("CTOA_VENCIMIENTO"));
               obj.setFieldString("FECHA_FAC", this.strFechaFactura);
               obj.setFieldString("CTOA_FOLIO", rs.getString("CTOA_FOLIO"));
               obj.setFieldDouble("CTOA_CPP", rs.getDouble("ccp"));
               obj.setFieldDouble("CTOA_MTO_ARRENDAMIENTO", rs.getDouble("rentamensual"));
               obj.setFieldDouble("CTOA_VCP", rs.getDouble("CTOA_VCP"));
               obj.setFieldString("CTOA_ARRENDAMIENTO", rs.getString("descripcion"));
               //Evaluamos si tiene el comodin del anio y el mes
               if (obj.getFieldString("CTOA_ARRENDAMIENTO").contains("@Mes")) {
                  //Mes
                  obj.setFieldString("CTOA_ARRENDAMIENTO", obj.getFieldString("CTOA_ARRENDAMIENTO").replace("@Mes", strFechaFactura.substring(4, 6)));
               }
               //Evaluamos si tiene el comodin del numero de mensualidades
               if (obj.getFieldString("CTOA_ARRENDAMIENTO").contains("@MensualidadActual")) {
                  //Mensualidad actual
                  obj.setFieldString("CTOA_ARRENDAMIENTO", obj.getFieldString("CTOA_ARRENDAMIENTO").replace("@MensualidadActual", obj.getFieldString("CTOA_MENSUALIDAD_ACTUAL")));
               }
               //Evaluamos si tiene el comodin de la mensualidad actual
               if (obj.getFieldString("CTOA_ARRENDAMIENTO").contains("@MensualidadTotal")) {
                  //Mensualidad actual
                  obj.setFieldString("CTOA_ARRENDAMIENTO", obj.getFieldString("CTOA_ARRENDAMIENTO").replace("@MensualidadTotal", obj.getFieldString("CTOA_MES")));
               }
               if (obj.getFieldString("CTOA_ARRENDAMIENTO").contains("@NombreMes") || obj.getFieldString("CTOA_ARRENDAMIENTO").contains("@NOMBREMES")) {
                  log.debug("Encontro macro de nombre del mes....");
                  if (this.msgRes != null) {
                     log.debug("Si hay objeto de struts");
                     //Mes
                     try {
                        fecha.setMsgRes(msgRes);
                        String strNomMes = fecha.DameMesenLetra(Integer.valueOf(strFechaFactura.substring(4, 6)));
                        log.debug("strNomMes:" + strNomMes);
                        if (obj.getFieldString("CTOA_ARRENDAMIENTO").contains("@NombreMes")) {
                           obj.setFieldString("CTOA_ARRENDAMIENTO", obj.getFieldString("CTOA_ARRENDAMIENTO").replace("@NombreMes", strNomMes));
                        } else {
                           obj.setFieldString("CTOA_ARRENDAMIENTO", obj.getFieldString("CTOA_ARRENDAMIENTO").replace("@NOMBREMES", strNomMes.toUpperCase()));
                        }

                     } catch (NumberFormatException ex) {
                        log.debug("Error al obtener fechas " + ex.getMessage());
                     }
                  } else {
                     log.debug("Este objeto esta nulo..." + msgRes);
                     //Mes
                     try {
                        String strNomMes = fecha.DameMesenLetra(Integer.valueOf(strFechaFactura.substring(4, 6)));
                        log.debug("strNomMes:" + strNomMes);
                        if (obj.getFieldString("CTOA_ARRENDAMIENTO").contains("@NombreMes")) {
                           obj.setFieldString("CTOA_ARRENDAMIENTO", obj.getFieldString("CTOA_ARRENDAMIENTO").replace("@NombreMes", strNomMes));
                        } else {
                           obj.setFieldString("CTOA_ARRENDAMIENTO", obj.getFieldString("CTOA_ARRENDAMIENTO").replace("@NOMBREMES", strNomMes.toUpperCase()));
                        }

                     } catch (NumberFormatException ex) {
                        log.debug("Error al obtener fechas " + ex.getMessage());
                     }
                  }
               }
               if (obj.getFieldString("CTOA_ARRENDAMIENTO").contains("@Anio") || obj.getFieldString("CTOA_ARRENDAMIENTO").contains("@ANIO")) {
                  //Anio
                  obj.setFieldString("CTOA_ARRENDAMIENTO", obj.getFieldString("CTOA_ARRENDAMIENTO").replace("@Anio", strFechaFactura.substring(0, 4)));
                  obj.setFieldString("CTOA_ARRENDAMIENTO", obj.getFieldString("CTOA_ARRENDAMIENTO").replace("@ANIO", strFechaFactura.substring(0, 4)));
               }
               obj.setFieldString("CTOA_TEXTO_CPP", rs.getString("CTOA_TEXTO_CPP"));
               //Categoria del cliente
               obj.setFieldInt("CTOA_CC1_ID", rs.getInt("CTOA_CC1_ID"));

               lstContratos.add(obj);
               log.debug(obj.getFieldString("CTOA_FOLIO"));
            } else {
               log.debug("folio facturado:" + rs.getString("CTOA_FOLIO"));
            }

         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex);
      }
      //Obtenemos la tasa del mes de TIIE
      try {
         String strSql = "select TI_TASA AS tiiexmes "
            + " FROM vta_tiies  "
            + " where left(TI_PERIODO,6)= '" + this.strFechaInicio.substring(0, 6) + "'  ";
         ResultSet rs = oConn.runQuery(strSql);
         while (rs.next()) {
            dblTasaxmesTIIE = rs.getDouble("tiiexmes");
            break;
         }
         rs.close();
         log.debug("dblTasaxmesTIIE " + dblTasaxmesTIIE);
      } catch (SQLException ex) {
         log.error(ex);
      }
      //Obtenemos la tasa del mes de CPP
      try {
         String strSql = "select FCP_FACTOR AS factor "
            + " FROM vta_factor_cpp  "
            + " where left(FCP_FECHA,6)= '" + this.strFechaInicio.substring(0, 6) + "'  ";
         ResultSet rs = oConn.runQuery(strSql);
         while (rs.next()) {
            dblTasaxmesCPP = rs.getDouble("factor");
            break;
         }
         rs.close();
         log.debug("dblTasaxmesCPP " + dblTasaxmesCPP);
      } catch (SQLException ex) {
         log.error(ex);
      }
      //Calculamos el factor del cpp
      Iterator<Factura_contratos> it = lstContratos.iterator();
      while (it.hasNext()) {
         Factura_contratos obj = it.next();

         //Evaluamos la tasa por aplicar
         double dblTasaAplicar = 0;
         if (obj.getFieldInt("CTOA_TIPO_FACTOR_AJUSTE") == 1) {
            dblTasaAplicar = dblTasaxmesCPP;
         } else {
            dblTasaAplicar = dblTasaxmesTIIE;
         }

         log.debug(obj.getFieldString("CTOA_FOLIO") + " " + dblTasaAplicar);

         //Evaluamos si aplica el ajuste
         double dblFactorAplica = 0;
         double dblImporteAjuste = 0;
         if (obj.getFieldInt("CTOA_TIPO_FACTOR_AJUSTE") != 3) {
            dblFactorAplica = dblTasaAplicar - obj.getFieldDouble("CTOA_CPP");
            dblImporteAjuste = obj.getFieldDouble("CTOA_VCP") * (dblFactorAplica);
            obj.setFieldDouble("CTOA_FACTOR_AJUSTE", dblFactorAplica);
            obj.setFieldDouble("CTOA_IMPORTE_AJUSTE", dblImporteAjuste);

         }

         //buscamos la tasa del iva
         try {
            String strSql = "select vta_tasaiva.TI_ID,vta_tasaiva.TI_TASA,CT_TIPOPERS "
               + " from vta_cliente, vta_tasaiva"
               + " where vta_cliente.TI_ID =vta_tasaiva.TI_ID AND vta_cliente.CT_ID = " + obj.getFieldInt("CTE_ID");
            ResultSet rs = oConn.runQuery(strSql);
            while (rs.next()) {
               obj.setFieldDouble("CTOA_TASA_IVA", rs.getDouble("TI_TASA"));
               obj.setFieldInt("TI_ID", rs.getInt("TI_ID"));
               obj.setIntCT_TIPOPERS(rs.getInt("CT_TIPOPERS"));
            }
            rs.close();
            strSql = "select CT_METODODEPAGO,CT_FORMADEPAGO,CT_CTABANCO1 "
               + " from vta_cliente"
               + " where vta_cliente.CT_ID = " + obj.getFieldInt("CTE_ID");
            rs = oConn.runQuery(strSql);
            while (rs.next()) {
               obj.setFieldString("CT_METODODEPAGO", rs.getString("CT_METODODEPAGO"));//CT_METODODEPAGO
               obj.setFieldString("CT_FORMADEPAGO", rs.getString("CT_FORMADEPAGO"));//CT_FORMADEPAGO
               obj.setFieldString("CT_CTABANCO1", rs.getString("CT_CTABANCO1"));//CT_CTABANCO1
               log.debug("CT_METODODEPAGO: " + rs.getString("CT_METODODEPAGO"));
               log.debug("CT_CTABANCO1: " + rs.getString("CT_CTABANCO1"));
               log.debug("CT_FORMADEPAGO: " + rs.getString("CT_FORMADEPAGO"));
            }
            rs.close();
         } catch (SQLException ex) {
            log.error(ex);
         }
         //Calculamos impuestos
         double dblSubtotal = dblImporteAjuste + obj.getFieldDouble("CTOA_MTO_ARRENDAMIENTO");
         double dblImpuesto = (obj.getFieldDouble("CTOA_TASA_IVA") / 100) * dblSubtotal;
         double dblTotal = dblImpuesto + dblSubtotal;
         obj.setFieldDouble("CTOA_IMPUESTO", dblImpuesto);
         obj.setFieldDouble("CTOA_TOTAL", dblTotal);
         //Calculamos total

         log.debug(" TIIE Orig:" + obj.getFieldDouble("CTOA_CPP") + " dblFactorAplica " + dblFactorAplica);
         log.debug("CTOA_MTO_ARRENDAMIENTO " + obj.getFieldDouble("CTOA_MTO_ARRENDAMIENTO"));
         log.debug("CTOA_VCP: " + obj.getFieldDouble("CTOA_VCP"));
         log.debug("dblImporteAjuste " + dblImporteAjuste);
         log.debug("dblImpuesto " + dblImpuesto);
         log.debug("dblTotal " + dblTotal);

      }
      //Solamente si aplica generamos las factura
      if (this.bolAplica) {

         //Comenzamos proceso de generacion de facturas
         it = lstContratos.iterator();
         while (it.hasNext()) {
            Factura_contratos obj = it.next();

            log.debug("Nva factura " + obj.getFieldString("CTOA_FOLIO"));
            double dblSubTotal = (obj.getFieldDouble("CTOA_MTO_ARRENDAMIENTO") + obj.getFieldDouble("CTOA_IMPORTE_AJUSTE"));
            log.debug("dblSubTotal:" + dblSubTotal);
            //Nuevo ticket
            Ticket ticket = new Ticket(oConn, varSesiones, request);
            ticket.setBolAfectaInv(false);
            ticket.setStrPATHKeys(this.strPathPrivateKeys);
            ticket.setStrPATHXml(this.strPathXML);
            ticket.setStrPATHFonts(this.strPathFonts);
            //ticket.setBolUsoLugarExpEmp(true);
            if (this.context != null) {
               ticket.initMyPass(this.context);
            } else {
               //El password viene parametrizado
               ticket.setStrMyPassSecret(strMyPassSecret);
            }
            //Recibimos parametros
            String strPrefijoMaster = "FAC";
            String strPrefijoDeta = "FACD";
            String strTipoVtaNom = Ticket.FACTURA;
            ticket.setStrTipoVta(strTipoVtaNom);
            ticket.setBolTransaccionalidad(!this.bolTransaccionalidad);
            ticket.setIntEMP_ID(this.intEMP_ID);
            ticket.setBolFolioGlobal(false);
            ticket.getDocument().setFieldInt("SC_ID", obj.getFieldInt("SC_ID"));
            ticket.getDocument().setFieldInt("CT_ID", obj.getFieldInt("CTE_ID"));
            //ticket.getDocument().setFieldInt("TKT_ID", obj.getIntTKT_ID());
            ticket.getDocument().setFieldInt(strPrefijoMaster + "_MONEDA", obj.getFieldInt("MON_ID"));
            ticket.getDocument().setFieldInt("VE_ID", 0);
            ticket.getDocument().setFieldInt("TI_ID", obj.getFieldInt("TI_ID"));
            ticket.getDocument().setFieldInt("CTOA_ID", obj.getFieldInt("CTOA_ID"));
            ticket.getDocument().setFieldInt(strPrefijoMaster + "_ESSERV", 1);
            ticket.getDocument().setFieldString(strPrefijoMaster + "_FECHA", obj.getFieldString("FECHA_FAC"));
            ticket.getDocument().setFieldString(strPrefijoMaster + "_FOLIO", "");
            ticket.getDocument().setFieldString(strPrefijoMaster + "_NOTAS", "CONTRATO:" + obj.getFieldString("CTOA_FOLIO"));
            ticket.getDocument().setFieldString(strPrefijoMaster + "_NOTASPIE", "");
            ticket.getDocument().setFieldString(strPrefijoMaster + "_REFERENCIA", "");
            ticket.getDocument().setFieldString(strPrefijoMaster + "_CONDPAGO", "");

            if (strSerie != null) {
               ticket.getDocument().setFieldString(strPrefijoMaster + "_SERIE", strSerie);
            }
            ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPORTE", dblSubTotal);
            ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO1", obj.getFieldDouble("CTOA_IMPUESTO"));
            ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO2", 0);
            ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO3", 0);
            ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TOTAL", obj.getFieldDouble("CTOA_TOTAL"));
            ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA1", obj.getFieldDouble("CTOA_TASA_IVA"));
            ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA2", 0);
            ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA3", 0);
            ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASAPESO", 1);
            ticket.getDocument().setFieldDouble(strPrefijoMaster + "_NUM_MENS", obj.getFieldInt("CTOA_MENSUALIDAD_ACTUAL"));
            log.debug("CT_METODODEPAGO: " + obj.getFieldString("CT_METODODEPAGO"));
            log.debug("CT_CTABANCO1: " + obj.getFieldString("CT_CTABANCO1").trim());
            log.debug("CT_FORMADEPAGO: " + obj.getFieldString("CT_FORMADEPAGO"));
            ticket.getDocument().setFieldString(strPrefijoMaster + "_METODODEPAGO", obj.getFieldString("CT_METODODEPAGO"));
            ticket.getDocument().setFieldString(strPrefijoMaster + "_NUMCUENTA", obj.getFieldString("CT_CTABANCO1").trim());
            ticket.getDocument().setFieldString(strPrefijoMaster + "_FORMADEPAGO", obj.getFieldString("CT_FORMADEPAGO"));
            //Definimos el id de la clasificacion 1 de clientes
            if (obj.getFieldInt("CTOA_CC1_ID") != 0) {
               ticket.getDocument().setFieldInt("CC1_ID", obj.getFieldInt("CTOA_CC1_ID") );
            }
            //Revisamos el cálculo de las retenciones
            log.debug("intEMP_TIPOPERS:" + intEMP_TIPOPERS);
            log.debug("obj.getIntCT_TIPOPERS():" + obj.getIntCT_TIPOPERS());
            if ((intEMP_TIPOPERS) == 2) {
               if (obj.getIntCT_TIPOPERS() == 1) {
                  //
                  double dblSuma = obj.getFieldDouble("CTOA_TOTAL");
                  double dblRetIsr = dblSubTotal * (dblFacRetISR / 100);
                  double dblRetIVA = 0;
                  if (obj.getFieldDouble("CTOA_IMPUESTO") > 0) {
                     dblRetIVA = (obj.getFieldDouble("CTOA_IMPUESTO") / 3) * 2;
                  }

                  //Exento retencion ISR
                  if ((intEMP_NO_ISR) == 1) {
                     dblRetIsr = 0;
                  }
                  //Exento retencion IVA
                  if ((intEMP_NO_IVA) == 1) {
                     dblRetIVA = 0;
                  }
                  double dblImpNeto = dblSuma - dblRetIsr - dblRetIVA;
                  log.debug("dblRetIsr:" + dblRetIsr);
                  log.debug("dblRetIVA:" + dblRetIVA);
                  log.debug("dblImpNeto:" + dblImpNeto);
                  ticket.getDocument().setFieldDouble(strPrefijoMaster + "_RETISR", dblRetIsr);
                  ticket.getDocument().setFieldDouble(strPrefijoMaster + "_RETIVA", dblRetIVA);
                  ticket.getDocument().setFieldDouble(strPrefijoMaster + "_NETO", dblImpNeto);
               } else {
                  ticket.getDocument().setFieldDouble(strPrefijoMaster + "_NETO", obj.getFieldDouble("CTOA_TOTAL"));
               }

            }

            //Insertamos el detalle (RENTA)
            vta_facturadeta deta = new vta_facturadeta();
            double dblImporteRentamasIVA = obj.getFieldDouble("CTOA_MTO_ARRENDAMIENTO") * (1 + (obj.getFieldDouble("CTOA_TASA_IVA") / 100));
            double dblImporteRentaIVA = obj.getFieldDouble("CTOA_MTO_ARRENDAMIENTO") * ((obj.getFieldDouble("CTOA_TASA_IVA") / 100));
            deta.setFieldInt("SC_ID", obj.getFieldInt("SC_ID"));
            deta.setFieldInt("PR_ID", 0);
            deta.setFieldString(strPrefijoDeta + "_CVE", "...");
            deta.setFieldString(strPrefijoDeta + "_DESCRIPCION", "SERVICIO");
            deta.setFieldDouble(strPrefijoDeta + "_IMPORTE", dblImporteRentamasIVA);
            deta.setFieldDouble(strPrefijoDeta + "_CANTIDAD", 1);
            deta.setFieldDouble(strPrefijoDeta + "_TASAIVA1", obj.getFieldDouble("CTOA_TASA_IVA"));
            deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO1", dblImporteRentaIVA);
            deta.setFieldDouble(strPrefijoDeta + "_PRECIO", dblImporteRentamasIVA);
            deta.setFieldDouble(strPrefijoDeta + "_PRECREAL", dblImporteRentamasIVA);
            String strComentario = obj.getFieldString("CTOA_ARRENDAMIENTO");
            deta.setFieldString(strPrefijoDeta + "_COMENTARIO", strComentario);
            ticket.AddDetalle(deta);

            //Validamos si hacemos ajuste
            if (obj.getFieldDouble("CTOA_IMPORTE_AJUSTE") != 0) {
               double dblImportemasIVA = obj.getFieldDouble("CTOA_IMPORTE_AJUSTE") * (1 + (obj.getFieldDouble("CTOA_TASA_IVA") / 100));
               double dblImporteIVA = obj.getFieldDouble("CTOA_IMPORTE_AJUSTE") * ((obj.getFieldDouble("CTOA_TASA_IVA") / 100));
               vta_facturadeta deta2 = new vta_facturadeta();
               deta2.setFieldInt("SC_ID", obj.getFieldInt("SC_ID"));
               deta2.setFieldInt("PR_ID", 0);
               deta2.setFieldString(strPrefijoDeta + "_CVE", "...");
               deta2.setFieldString(strPrefijoDeta + "_DESCRIPCION", "SERVICIO");
               deta2.setFieldDouble(strPrefijoDeta + "_IMPORTE", dblImportemasIVA);
               deta2.setFieldDouble(strPrefijoDeta + "_CANTIDAD", 1);
               deta2.setFieldDouble(strPrefijoDeta + "_TASAIVA1", obj.getFieldDouble("CTOA_TASA_IVA"));
               deta2.setFieldDouble(strPrefijoDeta + "_IMPUESTO1", dblImporteIVA);
               deta2.setFieldDouble(strPrefijoDeta + "_PRECIO", dblImportemasIVA);
               deta2.setFieldDouble(strPrefijoDeta + "_PRECREAL", dblImportemasIVA);
               strComentario = obj.getFieldString("CTOA_TEXTO_CPP");
               deta2.setFieldString(strPrefijoDeta + "_COMENTARIO", strComentario);
               ticket.AddDetalle(deta2);
            }

            //Inicializamos objeto
            ticket.Init();
            //Generamos transaccion
            ticket.doTrx();
            if (!ticket.getStrResultLast().equals("OK")) {
               if (this.strResultLast.equals("OK")) {
                  this.strResultLast = "";
               }
               this.strResultLast += " " + ticket.getStrResultLast() + " CONTRATO:" + obj.getFieldString("CTOA_FOLIO") + "<br>";
               log.debug(ticket.getStrResultLast() + " CONTRATO:" + obj.getFieldString("CTOA_FOLIO"));
            } else {
               //Si funciono....
               this.strResultLast += " " + ticket.getStrResultLast() + " CONTRATO:" + obj.getFieldString("CTOA_FOLIO") + "<br>";
               //Actualizamos la mensualidad
               int intNewMens = obj.getFieldInt("CTOA_MENSUALIDAD_ACTUAL");
               intNewMens++;
               String strUpdateContrato = "update vta_contrato_arrend set CTOA_MENSUALIDAD_ACTUAL = " + intNewMens
                  + " WHERE CTOA_ID = " + obj.getFieldInt("CTOA_ID");
               oConn.runQueryLMD(strUpdateContrato);
            }
         }

      }
   }

   public void doTrxAnul() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   public void doTrxRevive() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   public void doTrxSaldo() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   public void doTrxMod() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }
   // </editor-fold>
}
