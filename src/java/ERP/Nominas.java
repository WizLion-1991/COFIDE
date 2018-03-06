/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import Core.FirmasElectronicas.SATXml3_0;
import Core.FirmasElectronicas.SatCancelaCFDI;
import Core.FirmasElectronicas.Utils.UtilCert;
import ERP.BusinessEntities.PercepcionesDeduccionesE;
import Tablas.RhhIncidencias;
import Tablas.Rhh_Nominas_Master;
import Tablas.rhh_empleados;
import Tablas.rhh_nominas;
import Tablas.rhh_nominas_deta;
import com.mx.siweb.erp.nominas.Entidades.ItemConcepto;
import com.mx.siweb.erp.nominas.NominasFormulas;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.Mail;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.cert.CertificateEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Clase que realiza todas las operaciones de nominas
 *
 * @author ZeusGalindo
 */
public class Nominas extends ProcesoMaster implements ProcesoInterfaz {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    protected TableMaster document;
    protected ArrayList<TableMaster> lstConceptos;
    protected String strFechaAnul;
    protected int intEMP_ID = 0;
    protected String strPATHXml;
    protected String strPATHKeys;
    protected String strMyPassSecret;
    protected String strPATHFonts;
    private static final Logger log = LogManager.getLogger(Nominas.class.getName());
    protected boolean bolUsoLugarExpEmp = false;
    protected boolean bolSendMailMasivo;
    protected boolean bolEsFolioSucursal = false;

    /**
     * Nos regresa que el lugar de expedicion lo saque de la empresa
     *
     * @return Indica true si se usa la empresa como lugar de expedicion
     */
    public boolean isBolUsoLugarExpEmp() {
        return bolUsoLugarExpEmp;
    }

    /**
     * Indica que el lugar de expedicion lo saque de la empresa
     *
     * @param bolUsoLugarExpEmp Indica true si se usa la empresa como lugar de
     * expedicion
     */
    public void setBolUsoLugarExpEmp(boolean bolUsoLugarExpEmp) {
        this.bolUsoLugarExpEmp = bolUsoLugarExpEmp;
    }

    /**
     * Nos dice si se envia el mail del formato
     *
     * @return Regresa true si se envia el mail
     */
    public boolean isBolSendMailMasivo() {
        return bolSendMailMasivo;
    }

    /**
     * Indica si se envia el mail masivo
     *
     * @param bolSendMailMasivo Nos regresa true si se envia el mail del formato
     */
    public void setBolSendMailMasivo(boolean bolSendMailMasivo) {
        this.bolSendMailMasivo = bolSendMailMasivo;
    }

    public TableMaster getDocument() {
        return document;
    }

    public void setDocument(TableMaster document) {
        this.document = document;
    }

    public ArrayList<TableMaster> getLstConceptos() {
        return lstConceptos;
    }

    public void setLstConceptos(ArrayList<TableMaster> lstConceptos) {
        this.lstConceptos = lstConceptos;
    }

    public int getIntEMP_ID() {
        return intEMP_ID;
    }

    public void setIntEMP_ID(int intEMP_ID) {
        this.intEMP_ID = intEMP_ID;
    }

    /**
     * Es el path donde guardamos las llaves privadas
     *
     * @return Regresa una cadena
     */
    public String getStrPATHKeys() {
        return strPATHKeys;
    }

    /**
     * Es el path donde guardamos las llaves privadas
     *
     * @param strPATHKeys Regresa una cadena
     */
    public void setStrPATHKeys(String strPATHKeys) {
        this.strPATHKeys = strPATHKeys;
    }

    /**
     * Es el path donde guardamos los xml de las facturas electronica
     *
     * @return Regresa una cadena
     */
    public String getStrPATHXml() {
        return strPATHXml;
    }

    /**
     * Es el path donde guardamos los xml de las facturas electronica
     *
     * @param strPATHXml Regresa una cadena
     */
    public void setStrPATHXml(String strPATHXml) {
        this.strPATHXml = strPATHXml;
    }

    /**
     * Regresa el path donde se encuentran las fuentes
     *
     * @return Es una cadena de texto con un path
     */
    public String getStrPATHFonts() {
        return strPATHFonts;
    }

    /**
     * Define el path donde se encuentran las fuentes
     *
     * @param strPATHFonts Es una cadena de texto con un path
     */
    public void setStrPATHFonts(String strPATHFonts) {
        this.strPATHFonts = strPATHFonts;
    }

    public boolean isBolEsFolioSucursal() {
        return bolEsFolioSucursal;
    }

    /**
     * Indica que se generara un folio por sucursal
     *
     * @param bolEsFolioSucursal
     */
    public void setBolEsFolioSucursal(boolean bolEsFolioSucursal) {
        this.bolEsFolioSucursal = bolEsFolioSucursal;
    }

    /**
     * Definimos la palabra secreta
     *
     * @param strMyPassSecret Es un cadena
     */
    public void setStrMyPassSecret(String strMyPassSecret) {
        this.strMyPassSecret = strMyPassSecret;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">

    public Nominas(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
        super(oConn, varSesiones, request);
        this.document = new rhh_nominas();
        this.lstConceptos = new ArrayList<TableMaster>();
    }

    public Nominas(Conexion oConn, VariableSession varSesiones) {
        super(oConn, varSesiones);
        this.document = new rhh_nominas();
        this.lstConceptos = new ArrayList<TableMaster>();
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    @Override
    public void Init() {
        this.strResultLast = "OK";

        //Si nos pasan el id del movimiento recuperamos todos los datos
        if (this.document.getFieldInt("NOM_ID") != 0) {
            this.strFechaAnul = this.document.getFieldString("NOM_FECHAANUL");
            //¿Validamos o asignamos la tabla?
            this.document.ObtenDatos(this.document.getFieldInt("NOM_ID"), oConn);
        }
    }

    @Override
    public void doTrx() {

        // <editor-fold defaultstate="collapsed" desc="Inicializamos valores ">
        this.strResultLast = "OK";
        this.document.setFieldString("NOM_FECHACREATE", this.fecha.getFechaActual());
        this.document.setFieldString("NOM_HORA", this.fecha.getHoraActualHHMMSS());
        this.document.setFieldInt("NOM_US_ALTA", varSesiones.getIntNoUser());
      // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Validaciones previas al guardado">
        if (this.document.getFieldInt("SC_ID") == 0) {
            this.strResultLast = "ERROR:Falta definir la sucursal";
        }
        if (this.document.getFieldString("NOM_FECHA").equals("")) {
            this.strResultLast = "ERROR:Falta definir la fecha de pago";
        }
        if (this.document.getFieldString("NOM_FECHA_INICIAL_PAGO").equals("")) {
            this.strResultLast = "ERROR:Falta definir la fecha inicial de pago";
        }

        if (this.document.getFieldString("NOM_FECHA_FINAL_PAGO").equals("")) {
            this.strResultLast = "ERROR:Falta definir la fecha final de pago";
        }
        if (this.document.getFieldInt("EMP_NUM") == 0) {
            this.strResultLast = "ERROR:Falta definir el empleado";
        }
//      if (this.document.getFieldDouble("NOM_NUM_DIAS_PAGADOS") == 0) {
//         this.strResultLast = "ERROR:Falta definir el numero de dias pagados";
//      }

//      if (this.lstConceptos.isEmpty()) {
//         this.strResultLast = "ERROR:Debe tener por lo menos un item en la nomina";
//      }
        //Asignamos empresa...
        if (this.document.getFieldInt("EMP_ID") == 0) {
            this.document.setFieldInt("EMP_ID", this.intEMP_ID);
        }
        //Evaluamos la fecha de cierre
        if (!this.document.getFieldString("NOM_FECHA").isEmpty()) {
//         boolean bolEvalCierre = evaluaFechaCierre(this.document.getFieldString("NOM_FECHA"), this.document.getFieldInt("EMP_ID"), this.document.getFieldInt("SC_ID"));
//         if (!bolEvalCierre) {
//            this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
//         }
        }
      // </editor-fold>

        //Validamos si pasamos las validaciones
        if (this.strResultLast.equals("OK")) {
            // <editor-fold defaultstate="collapsed" desc="Copiar datos del empleado">
            boolean bolFindEmp = false;
            String strEmp_Reg_Patronal = "";
            String strSql = "SELECT EMP_NOMBRE,EMP_RFC,EMP_CALLE,EMP_COLONIA,EMP_LOCALIDAD,"
                    + "EMP_SALARIO_DIARIO, EMP_SALARIO_INTEGRADO,"
                    + " EMP_MUNICIPIO,EMP_ESTADO,EMP_NUMERO,EMP_NUMINT,"
                    + " EMP_CP,RC_ID,DP_ID,EMP_CURP,RC_ID,EMP_PERIODICIDAD_PAGO,EMP_REGISTRO_PATRONAL"
                    + " FROM rhh_empleados "
                    + " where EMP_NUM=" + this.document.getFieldInt("EMP_NUM") + "";
            try {
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    this.document.setFieldString("NOM_RAZONSOCIAL", rs.getString("EMP_NOMBRE"));
                    this.document.setFieldString("NOM_RFC", rs.getString("EMP_RFC"));
                    this.document.setFieldString("NOM_CALLE", rs.getString("EMP_CALLE"));
                    this.document.setFieldString("NOM_COLONIA", rs.getString("EMP_COLONIA"));
                    this.document.setFieldString("NOM_LOCALIDAD", rs.getString("EMP_LOCALIDAD"));
                    this.document.setFieldString("NOM_MUNICIPIO", rs.getString("EMP_MUNICIPIO"));
                    this.document.setFieldString("NOM_ESTADO", rs.getString("EMP_ESTADO"));
                    this.document.setFieldString("NOM_NUMERO", rs.getString("EMP_NUMERO"));
                    this.document.setFieldString("NOM_NUMINT", rs.getString("EMP_NUMINT"));
                    this.document.setFieldString("NOM_CP", rs.getString("EMP_CP"));
                    this.document.setFieldDouble("NOM_SALARIO_DIARIO", rs.getDouble("EMP_SALARIO_DIARIO"));
                    this.document.setFieldDouble("NOM_SALARIO_INTEGRADO", rs.getDouble("EMP_SALARIO_INTEGRADO"));
                    this.document.setFieldInt("RC_ID", rs.getInt("RC_ID"));
                    strEmp_Reg_Patronal = rs.getString("EMP_REGISTRO_PATRONAL");
                    if (rs.getInt("RC_ID") == 0) {
                        this.strResultLast = "ERROR:El empleado no tiene capturado el tipo de regimen de contratacion";
                        break;
                    }
                    this.document.setFieldInt("DP_ID", rs.getInt("DP_ID"));
                    if (rs.getString("EMP_CURP").isEmpty()) {
                        this.strResultLast = "ERROR:El empleado no tiene capturada la CURP";
                        break;
                    }
                    if (rs.getString("EMP_PERIODICIDAD_PAGO").isEmpty()) {
                        this.strResultLast = "ERROR:El empleado no tiene capturada la periodicidad del pago";
                        break;
                    }
                    bolFindEmp = true;
                }
                rs.close();
            } catch (SQLException ex) {
                this.strResultLast = "ERROR:" + ex.getMessage();
                ex.fillInStackTrace();
                log.error("!!!!!!...." + ex.getMessage());
            }
         // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Si el empleado se encontro">
            if (bolFindEmp) {
                // <editor-fold defaultstate="collapsed" desc="Obtener id de empresa que hara el recibo">
                strSql = "SELECT EMP_ID,SC_TASA1,SC_TASA2,SC_TASA3,"
                        + "SC_SOBRIMP1_2,SC_SOBRIMP1_3,SC_SOBRIMP2_3 "
                        + "FROM vta_sucursal WHERE SC_ID = " + this.document.getFieldInt("SC_ID") + "";
                try {
                    ResultSet rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        this.document.setFieldInt("EMP_ID", rs.getInt("EMP_ID"));
                    }
                    rs.close();
                } catch (SQLException ex) {
                    this.strResultLast = "ERROR:" + ex.getMessage();
                    ex.fillInStackTrace();
                }
                //Si se definio el id de la empresa independiente de la sucursal
                if (this.intEMP_ID != 0) {
                    this.document.setFieldInt("EMP_ID", this.intEMP_ID);
                    this.bolFolioGlobal = false;
                }

                // <editor-fold defaultstate="collapsed" desc="Validamos regimen fiscal">
                int intCuantos = 0;
                String strSqlRegFis = "select count(REGF_DESCRIPCION) as cuantos from vta_empregfiscal,vta_regimenfiscal "
                        + " where vta_empregfiscal.REGF_ID = vta_regimenfiscal.REGF_ID "
                        + " AND vta_empregfiscal.EMP_ID = " + this.document.getFieldInt("EMP_ID");
                try {
                    ResultSet rs = oConn.runQuery(strSqlRegFis, true);
                    while (rs.next()) {
                        intCuantos = rs.getInt("cuantos");
                    }
                    rs.close();
                } catch (SQLException ex) {
                    this.strResultLast = "ERROR:" + ex.getMessage();
                    ex.fillInStackTrace();
                }
                //Validamos que el registro patronal del empleado no sea vacio
                if (!strEmp_Reg_Patronal.isEmpty()) {
                    this.document.setFieldString("NOM_REGISTRO_PATRONAL", strEmp_Reg_Patronal);
                } else {
                    strSql = "SELECT EMP_REGISTRO_PATRONAL,EMP_FOLIO_SUCURSAL_NOMINA "
                            + " FROM vta_empresas "
                            + " WHERE EMP_ID = " + this.document.getFieldInt("EMP_ID") + "";
                    try {
                        ResultSet rs = oConn.runQuery(strSql, true);
                        while (rs.next()) {
                            this.document.setFieldString("NOM_REGISTRO_PATRONAL", rs.getString("EMP_REGISTRO_PATRONAL"));
                            if (rs.getInt("EMP_FOLIO_SUCURSAL_NOMINA") == 1) {
                                this.bolEsFolioSucursal = true;
                            }
                        }
                        rs.close();
                        //Evaluamos si la sucursal tiene registro patronal
                        strSql = "SELECT CT_REG_PAT "
                                + " FROM vta_sucursal "
                                + " WHERE SC_ID = " + this.document.getFieldInt("SC_ID") + "";
                        rs = oConn.runQuery(strSql, true);
                        while (rs.next()) {
                            if (rs.getString("CT_REG_PAT") != null) {
                                if (!rs.getString("CT_REG_PAT").isEmpty()) {
                                    this.document.setFieldString("NOM_REGISTRO_PATRONAL", rs.getString("CT_REG_PAT"));
                                }
                            }

                        }
                        rs.close();
                    } catch (SQLException ex) {
                        this.strResultLast = "ERROR:" + ex.getMessage();
                        ex.fillInStackTrace();
                    }
                    //Version 2.2
                    if (intCuantos == 0) {
                        this.strResultLast = "ERROR:FALTA ASIGNAR UN REGIMEN FISCAL A LA EMPRESA";
                    }
                }
            // </editor-fold>

                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="Calculamos los totales de percepciones">
                double dblTotPercepciones = 0;
                double dblTotDeducciones = 0;
                Iterator<TableMaster> itI = this.lstConceptos.iterator();
                while (itI.hasNext()) {
                    TableMaster deta = itI.next();
                    if (deta.getFieldInt("TP_ID") > 0) {
                        dblTotPercepciones += deta.getFieldDouble("NOMD_UNITARIO");
                    } else {
                        dblTotDeducciones += deta.getFieldDouble("NOMD_UNITARIO");
                    }
                }
                double dblTotal = dblTotPercepciones - dblTotDeducciones /*- this.document.getFieldDouble("NOM_ISR_RETENIDO")*/;//el isr viene en las partidas
                log.debug("dblTotPercepciones:" + dblTotPercepciones);
                log.debug("dblTotDeducciones:" + dblTotDeducciones);
                log.debug("dblTotal:" + dblTotal);
                this.document.setFieldDouble("NOM_PERCEPCIONES", dblTotPercepciones);
                this.document.setFieldDouble("NOM_DEDUCCIONES", dblTotDeducciones);
                this.document.setFieldDouble("NOM_PERCEPCION_TOTAL", dblTotal);

                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="Proceso de guardado del documento">
                if (this.strResultLast.equals("OK")) {
                    // <editor-fold defaultstate="collapsed" desc="Iniciar transaccion">
                    if (this.bolTransaccionalidad) {
                        this.oConn.runQueryLMD("BEGIN");
                    }
               // </editor-fold>

                    // <editor-fold defaultstate="collapsed" desc="Folios para el documento">
                    this.document.setBolGetAutonumeric(true);
                    Folios folio = new Folios();
                    String strFolio = "";
                    if (this.bolEsFolioSucursal) {
                        folio.setBolEsFolioSucursal(true);
                        folio.setIntSucId(this.document.getFieldInt("SC_ID"));
                    }
                    strFolio = folio.doFolio(oConn, Folios.RECIBO_NOMINA, this.bolFolioGlobal, this.document.getFieldInt("EMP_ID"));
                    this.document.setFieldString("NOM_FOLIO", strFolio);
               // </editor-fold>

                    // <editor-fold defaultstate="collapsed" desc="Si todo esta ok guardamos el documento">
                    if (this.strResultLast.equals("OK")) {
                        String strRes1 = this.document.Agrega(oConn);
                        if (!strRes1.equals("OK")) {
                            this.strResultLast = strRes1;
                        }
                    }
               // </editor-fold>

                    //Continuamos con el resto 
                    if (this.strResultLast.equals("OK")) {

                        // <editor-fold defaultstate="collapsed" desc="Guardar detalle">
                        int intId = Integer.valueOf(this.document.getValorKey());
                        Iterator<TableMaster> it = this.lstConceptos.iterator();
                        while (it.hasNext()) {
                            TableMaster deta = it.next();
                            deta.setFieldInt("NOM_ID", intId);
                            deta.setBolGetAutonumeric(true);
                            String strRes2 = deta.Agrega(oConn);
                            //Validamos si todo fue satisfactorio
                            if (!strRes2.equals("OK")) {
                                this.strResultLast = strRes2;
                                break;
                            }
                        }
                  // </editor-fold>

                        // <editor-fold defaultstate="collapsed" desc="Bitacora">
                        this.saveBitacora("NOMINA", "NUEVA", intId);
                        // </editor-fold>
                    }
                    // <editor-fold defaultstate="collapsed" desc="Termina la transaccion">
                    if (this.bolTransaccionalidad) {
                        if (this.strResultLast.equals("OK")) {
                            this.oConn.runQueryLMD("COMMIT");
                        } else {
                            this.oConn.runQueryLMD("ROLLBACK");
                        }
                    }
                    // </editor-fold>  
                }
                // </editor-fold>
            } else {
                this.strResultLast = "ERROR:El empleado seleccionado no existe" + this.document.getFieldInt("EMP_NUM");
            }
            // </editor-fold>
        }
    }

    @Override
    public void doTrxAnul() {

        // <editor-fold defaultstate="collapsed" desc="Valores iniciales">
        this.strResultLast = "OK";
        //Validamos que todos los campos basico se encuentren
        if (this.document.getFieldInt("NOM_ID") == 0) {
            this.strResultLast = "ERROR:Falta definir la operacion por anular";
        }
        //Evaluamos la fecha de cierre
        boolean bolEvalCierre = evaluaFechaCierre(this.document.getFieldString("NOM_FECHA"), this.document.getFieldInt("EMP_ID"), this.document.getFieldInt("SC_ID"));
        if (!bolEvalCierre) {
            this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
        }
      // </editor-fold>

        //Validamos que halla pasado las validaciones
        if (this.strResultLast.equals("OK")) {
            // <editor-fold defaultstate="collapsed" desc="Validamos que no este anulada">
            if (this.document.getFieldInt("NOM_ANULADA") == 0) {

                // <editor-fold defaultstate="collapsed" desc="Iniciamos transaccion">
                if (this.bolTransaccionalidad) {
                    this.oConn.runQueryLMD("BEGIN");
                }
            // </editor-fold>

                // <editor-fold defaultstate="collapsed" desc="Guardamos el documento principal">
                this.document.setFieldInt("NOM_US_ANUL", this.varSesiones.getIntNoUser());
                this.document.setFieldInt("NOM_ANULADA", 1);
                this.document.setFieldString("NOM_HORANUL", this.fecha.getHoraActual());
                if (strFechaAnul.equals("")) {
                    this.document.setFieldString("NOM_FECHAANUL", this.fecha.getFechaActual());
                } else {
                    this.document.setFieldString("NOM_FECHAANUL", strFechaAnul);
                }
                String strResp1 = this.document.Modifica(oConn);
                // </editor-fold>

                if (!strResp1.equals("OK")) {
                    this.strResultLast = strResp1;
                } else {

                    //Evaluamos si se timbro el recibo
                    if (this.document.getFieldInt("NOM_SE_TIMBRO") == 1) {

                        // <editor-fold defaultstate="collapsed" desc="Obtenemos banderas de la empresa">
                        int intEMP_USECONTA = 0;
                        int intEMP_AVISOCANCEL_NOM = 0;
                        /*String strCtaVtasGlobal = "";
                         String strCtaVtasIVAGlobal = "";
                         String strCtaVtasIVATasa = "";
                         String strCtaVtasCteGlobal = "";
                         String strEMP_PASSCP = "";
                         String strEMP_USERCP = "";
                         String strEMP_URLCP = "";
                         int intEMP_VTA_DETA = 0;
                         int intEMP_CFD_CFDI = 0;*/
                        //Consultamos la info de la empresa
                        String strSql = "SELECT EMP_NOAPROB,EMP_FECHAPROB,EMP_NOSERIECERT,EMP_TIPOCOMP,EMP_VTA_DETA,EMP_URLCP,"
                                + " EMP_USECONTA,EMP_CTAVTA,EMP_CTAIVA,EMP_CTACTE,EMP_PASSCP,EMP_USERCP,EMP_AVISOCANCEL_NOM,EMP_CFD_CFDI "
                                + " FROM vta_empresas "
                                + " WHERE EMP_ID = " + this.document.getFieldInt("EMP_ID") + "";
                        try {
                            ResultSet rs = oConn.runQuery(strSql, true);
                            while (rs.next()) {
                                intEMP_USECONTA = rs.getInt("EMP_USECONTA");
                                intEMP_AVISOCANCEL_NOM = rs.getInt("EMP_AVISOCANCEL_NOM");
                                /*strCtaVtasGlobal = rs.getString("EMP_CTAVTA");
                                 strCtaVtasIVAGlobal = rs.getString("EMP_CTAIVA");
                                 strCtaVtasCteGlobal = rs.getString("EMP_CTACTE");
                                 strEMP_PASSCP = rs.getString("EMP_PASSCP");
                                 strEMP_USERCP = rs.getString("EMP_USERCP");
                                 intEMP_VTA_DETA = rs.getInt("EMP_VTA_DETA");
                                 intEMP_CFD_CFDI = rs.getInt("EMP_CFD_CFDI");
                                 strEMP_URLCP = rs.getString("EMP_URLCP");*/
                            }
                            rs.close();
                        } catch (SQLException ex) {
                            this.strResultLast = "ERROR:" + ex.getMessage();
                            ex.fillInStackTrace();
                        }
                  // </editor-fold>

                        // <editor-fold defaultstate="collapsed" desc="Enviamos aviso de cancelacion al SAT si es CDFI">
                        String strResp = CancelaComprobante();
                        if (!strResp.equals("OK")) {
                            this.strResultLast = strResp;
                        }
                        // </editor-fold>
                        //<editor-fold defaultstate="collapsed" desc="Contabilidad">
                        if (intEMP_USECONTA == 1) {
                            //Instanciamos objetos para calcular la poliz contable
                            ContabilidadUtil contaUtil = new ContabilidadUtil(oConn, varSesiones);
//                     //Calcula la poliza para recibos de nomina
//                     contaUtil.CalculaPolizaContableNominas(this.document.getFieldInt("EMP_ID"),
//                             strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP, strCtaVtasGlobal,
//                             strCtaVtasIVAGlobal, strCtaVtasIVATasa, strCtaVtasCteGlobal,
//                             this.document.getFieldInt("NOM_MONEDA"), document, 0,
//                             this.document.getFieldInt("NOM_ID"),
//                             "rhh_nominas", "NOM_", "CANCEL");
                        }
                        // </editor-fold>
                        // <editor-fold defaultstate="collapsed" desc="Mail de aviso de cancelacion">
                        //Validamos si se envia mail de aviso
                        if (intEMP_AVISOCANCEL_NOM == 1) {
                            envioMailCancel();
                        }
                        // </editor-fold>   
                    }
                }
                // <editor-fold defaultstate="collapsed" desc="Guardamos la bitacora">
                this.saveBitacora("NOMINA", "ANULAR", this.document.getFieldInt("NOM_ID"));
            // </editor-fold>

                // <editor-fold defaultstate="collapsed" desc="Terminamos la transaccion">
                if (this.bolTransaccionalidad) {
                    if (this.strResultLast.equals("OK")) {
                        this.oConn.runQueryLMD("COMMIT");
                    } else {
                        this.oConn.runQueryLMD("ROLLBACK");
                    }
                }
                // </editor-fold>
            } else {
                this.strResultLast = "ERROR:La operacion ya fue anulada";
            }
            // </editor-fold>
        }
    }

    @Override
    public void doTrxRevive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doTrxSaldo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doTrxMod() {
        this.strResultLast = "OK";
        //Validamos que todos los campos basico se encuentren
        if (this.document.getFieldInt("NOM_ID") == 0) {
            this.strResultLast = "ERROR:Falta definir la operacion por modificar";
        }
        //Validamos que no este timbrada
        if (this.document.getFieldInt("NOM_SE_TIMBRO") == 1) {
            this.strResultLast = "ERROR:el recibo de nomina ya se timbro";
        }

        //Si paso las validaciones proseguimos
        if (this.strResultLast.equals("OK")) {
            // <editor-fold defaultstate="collapsed" desc="Validamos que no este anulado">
            if (this.document.getFieldInt("NOM_ANULADA") == 0) {
                // <editor-fold defaultstate="collapsed" desc="Iniciar transaccion">
                if (this.bolTransaccionalidad) {
                    this.oConn.runQueryLMD("BEGIN");
                }
            // </editor-fold>

                //Definimos campos
                this.document.setFieldInt("NOM_US_MOD", this.varSesiones.getIntNoUser());
                String strResp1 = this.document.Modifica(oConn);
                if (!strResp1.equals("OK")) {
                    this.strResultLast = strResp1;
                } else {

                    // <editor-fold defaultstate="collapsed" desc="Borrar el detalle">
                    String strNomTable = "";
                    strNomTable = "rhh_nominas_deta";

                    String strDelete = "delete from " + strNomTable + " where " + "NOM_ID" + " = " + this.document.getFieldInt("NOM_ID");
                    oConn.runQueryLMD(strDelete);
               // </editor-fold>

                    // <editor-fold defaultstate="collapsed" desc="guardar detalle">
                    int intId = Integer.valueOf(this.document.getValorKey());
                    Iterator<TableMaster> it = this.lstConceptos.iterator();
                    while (it.hasNext()) {
                        TableMaster deta = it.next();
                        deta.setFieldInt("NOM_ID", intId);
                        deta.setBolGetAutonumeric(true);
                        String strRes2 = deta.Agrega(oConn);
                        //Validamos si todo fue satisfactorio
                        if (!strRes2.equals("OK")) {
                            this.strResultLast = strRes2;
                            break;
                        }
                    }
                    // </editor-fold>
                    if (this.strResultLast.equals("OK")) {
                        //Actualizamos la poliza contable
                    }
                }
                //Guardamos la bitacora
                this.saveBitacora("NOMINA", "MODIFICAR", this.document.getFieldInt("NOM_ID"));

                // <editor-fold defaultstate="collapsed" desc="Termina la transaccion">
                if (this.bolTransaccionalidad) {
                    if (this.strResultLast.equals("OK")) {
                        this.oConn.runQueryLMD("COMMIT");
                    } else {
                        this.oConn.runQueryLMD("ROLLBACK");
                    }
                }
                // </editor-fold>  
            } else {
                this.strResultLast = "ERROR:La operacion ya fue anulada";
            }
            // </editor-fold>
        }

    }

    public boolean existHorasExtra(RhhIncidencias incidencias) {
        boolean blExiste = false;
        int intContador = 0;
        String strHorasExtra = "select RHIN_ID from rhh_horas_extra where RHIN_ID = " + incidencias.getFieldInt("RHIN_ID");
        try {
            ResultSet Rs = oConn.runQuery(strHorasExtra, true);
            while (Rs.next()) {
                intContador = +1;
            }
            Rs.close();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(Nominas.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (intContador == 0) {
            blExiste = false;
        } else {
            blExiste = true;
        }
        return blExiste;
    }//Fin existHorasExtra

    // </editor-fold>
    /**
     * Timbra los recibos de nomina del periodo solicitado
     *
     * @param intEmpId
     * @param strPeriodo
     * @param intIdNomina
     * @param strFolioIni
     * @param strFolioFin
     */
    public void doTimbrado(int intEmpId, String strPeriodo, int intIdNomina, String strFolioIni, String strFolioFin) {
        this.strResultLast = "OK";
        // <editor-fold defaultstate="collapsed" desc="Valores iniciales">
        int intCT_TIPOPERS = 0;
        int intEMP_TIPOPERS = 0;
        int intEMP_AVISOFOLIO = 0;
        //Valores para contabilidad
        int intEMP_USECONTA = 0;
        String strEMP_URLCP = "";
        String strEMP_PASSCP = "";
        String strEMP_USERCP = "";
        String strNoSerieCert = "";
        String strNomKey = "";
        String strNomCert = "";
        String strPassKey = "";
      // </editor-fold>

        //Paso 1 obtenemos datos de la empresa
        // <editor-fold defaultstate="collapsed" desc="Obtenemos datos de la empresa(WEB)">
        String strSql = "SELECT EMP_NOAPROB,EMP_FECHAPROB,EMP_NOSERIECERT,EMP_TIPOCOMP,"
                + "EMP_NOMKEY,EMP_NOMCERT,EMP_TIPOPERS,EMP_AVISOFOLIO,EMP_USECONTA,EMP_CTAVTA,EMP_CTAIVA,EMP_CTACTE,"
                + "AES_DECRYPT(EMP_PASSKEY, '" + strMyPassSecret + "') AS unencrypted,EMP_CFD_CFDI,"
                + "EMP_FIRMA,EMP_PASSCP,EMP_USERCP,EMP_URLCP,EMP_USACODBARR,EMP_VTA_DETA "
                + "FROM vta_empresas "
                + "WHERE EMP_ID = " + intEmpId;
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                intEMP_TIPOPERS = rs.getInt("EMP_TIPOPERS");
                strNoSerieCert = rs.getString("EMP_NOSERIECERT");
                strNomKey = rs.getString("EMP_NOMKEY");
                strNomCert = rs.getString("EMP_NOMCERT");
                strPassKey = rs.getString("unencrypted");
                intEMP_USECONTA = rs.getInt("EMP_USECONTA");
//            intEMP_VTA_DETA = rs.getInt("EMP_VTA_DETA");
//            strCtaVtasGlobal = rs.getString("EMP_CTAVTA");
//            strCtaVtasIVAGlobal = rs.getString("EMP_CTAIVA");
//            strCtaVtasCteGlobal = rs.getString("EMP_CTACTE");
                strEMP_PASSCP = rs.getString("EMP_PASSCP");
                strEMP_USERCP = rs.getString("EMP_USERCP");
                strEMP_URLCP = rs.getString("EMP_URLCP");
                intEMP_AVISOFOLIO = rs.getInt("EMP_AVISOFOLIO");
            }
            rs.close();
        } catch (SQLException ex) {
            this.strResultLast = "ERROR:" + ex.getMessage();
            ex.fillInStackTrace();
        }
      // </editor-fold>

        //Paso 2 buscamos los recibos por timbrar
        // <editor-fold defaultstate="collapsed" desc="Busqueda de recibos por timbrar">
        strSql = "select NOM_ID from rhh_nominas where NOM_ANULADA = 0 and NOM_SE_TIMBRO = 0 AND EMP_ID = " + intEmpId;
        //solo se timbrara un recibo
        if (intIdNomina != 0) {
            strSql = "select NOM_ID from rhh_nominas where NOM_ANULADA = 0 and NOM_SE_TIMBRO = 0 and NOM_ID = " + intIdNomina + " AND EMP_ID = " + intEmpId;
        } else {
            if (!strFolioIni.isEmpty() && !strFolioFin.isEmpty()) {
                strSql = "select NOM_ID from rhh_nominas where NOM_ANULADA = 0 and NOM_SE_TIMBRO = 0 and NOM_FOLIO >= '" + strFolioIni + "' AND NOM_FOLIO <= '" + strFolioFin + "'  AND EMP_ID = " + intEmpId;
            } else {
                if (!strPeriodo.isEmpty()) {
                    strSql = "select NOM_ID from rhh_nominas where NOM_ANULADA = 0 and NOM_SE_TIMBRO = 0 and LEFT(NOM_FECHA,6) = '" + strPeriodo + "'  AND EMP_ID = " + intEmpId;
                }
            }
        }
        ResultSet rs;
        try {
            rs = this.oConn.runQuery(strSql, true);
            while (rs.next()) {
                //instanciamos objeto de CFDI
                SATXml3_0 sat3 = new SATXml3_0(rs.getInt("NOM_ID"), this.strPATHXml,
                        "", "", strNoSerieCert,
                        this.strPATHKeys + strNomKey, strPassKey, this.varSesiones,
                        oConn);
                sat3.setStrPathConfigPAC(this.strPATHKeys);
                //Validamos que el path del certificado no este vacio
                if (!strNomCert.isEmpty()) {
                    sat3.setStrPathCert(this.strPATHKeys + strNomCert);
                }
                sat3.setStrPATHFonts(strPATHFonts);
                sat3.setBolSendMailMasivo(bolSendMailMasivo);
                if (this.bolUsoLugarExpEmp) {
                    sat3.setBolUsoLugarExpEmp(bolUsoLugarExpEmp);
                }
                sat3.setBolNominas(true);
                sat3.setBolSendMailMasivo(false);

                String strRes = sat3.GeneraXml();
                if (!strRes.equals("OK")) {
                    this.strResultLast = strRes;
                } else {
                    //Guardamos la bandera de que fue timbrado
                    String strUpdate = "update rhh_nominas set NOM_SE_TIMBRO =1 where NOM_ID = " + rs.getInt("NOM_ID");
                    this.oConn.runQueryLMD(strUpdate);
                }

            }
            rs.close();

        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        // </editor-fold>
    }

    protected String CancelaComprobante() {

        // <editor-fold defaultstate="collapsed" desc="Obtenemos datos del sello">
        String strNomKey = null;
        String strNomCert = null;
        String strPassKey = null;

        // <editor-fold defaultstate="collapsed" desc="Obtenemos datos de la empresa(WEB)">
        String strSql = "SELECT "
                + "EMP_NOMKEY,EMP_NOMCERT,"
                + "AES_DECRYPT(EMP_PASSKEY, '" + strMyPassSecret + "') AS unencrypted "
                + "FROM vta_empresas "
                + "WHERE EMP_ID = " + this.document.getFieldInt("EMP_ID") + "";
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strNomKey = rs.getString("EMP_NOMKEY");
                strNomCert = rs.getString("EMP_NOMCERT");
                strPassKey = rs.getString("unencrypted");
            }
            rs.close();
        } catch (SQLException ex) {
            this.strResultLast = "ERROR:" + ex.getMessage();
            ex.fillInStackTrace();
        }
      // </editor-fold>

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Preparamos y ejecutamos objeto de cancelacion">
        byte[] certificadoEmisor = PreparaCertificado(this.strPATHKeys + System.getProperty("file.separator") + strNomCert);
        //byte[] llavePrivadaEmisor = PreparaLlave(this.strPATHKeys + System.getProperty("file.separator") + strNomKey, strPassKey);
        byte[] llavePrivadaEmisor = getPrivateKey(this.strPATHKeys + System.getProperty("file.separator") + strNomKey);
        StringBuilder strNomFile = new StringBuilder("");
        //Instanciamos objeto que cancela
        SatCancelaCFDI cancela = new SatCancelaCFDI(this.strPATHKeys);

        cancela.setStrTablaDoc("rhh_nominas");
        cancela.setStrPrefijoDoc("NOM_");
        cancela.setIntIdDoc(this.document.getFieldInt("NOM_ID"));
        cancela.setStrPathXml(this.strPATHXml);
        cancela.setoConn(oConn);
        cancela.setLlavePrivadaEmisor(llavePrivadaEmisor);
        cancela.setCertificadoEmisor(certificadoEmisor);
        cancela.setStrPasswordEmisor(strPassKey);

        strNomFile.append("Nomina_").append(this.document.getFieldInt("NOM_ID")).append(".xml");
        //Validamos si existe en la versión 1.0
        File fileXml = new File(strNomFile.toString());
        if (!fileXml.exists()) {
            //Version 2
            strNomFile = new StringBuilder("");
            ERP_MapeoFormato mapeoXml = new ERP_MapeoFormato(7);
            strNomFile.append(getNombreFileXml(mapeoXml, this.document.getFieldInt("NOM_ID"), this.document.getFieldString("NOM_RAZONSOCIAL"), this.document.getFieldString("NOM_FECHA"), this.document.getFieldString("NOM_UUID")));
            //getNombreFileXml(ERP_MapeoFormato mapeo, int intTransaccion, String strNombreReceptor, String strFechaCFDI, String UUID) 
        }

        String strResp = cancela.timbra_Factura(strNomFile.toString());
        // </editor-fold>

        return strResp;

    }

    /**
     * Obtiene la llave privada
     *
     * @param strPath Es el path del archivo
     * @return Regresa la llave en binario
     */
    public byte[] getPrivateKey(String strPath) {
        RandomAccessFile f = null;
        byte[] b = null;
        try {
            f = new RandomAccessFile(strPath, "r");
            b = new byte[(int) f.length()];
            f.read(b);
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ticket.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Ticket.class.getName()).log(Level.SEVERE, null, ex);
        }

        return b;
    }

    //Prepara la llave a usar para cancelar el comprobante
    protected byte[] PreparaCertificado(String strPathCert) {
        byte[] certificado = null;
        UtilCert cert = new UtilCert();
        cert.OpenCert(strPathCert);
        if (!cert.getStrResult().startsWith("ERROR")) {
            try {
                certificado = cert.getCert().getEncoded();
            } catch (CertificateEncodingException ex) {
                log.error(ex.toString() + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
            }
        }
        return certificado;
    }

    /**
     * Envia aviso de que se cancelo la operacion al cliente
     */
    public void envioMailCancel() {
        //Buscamos mail del cliente
        String strCT_EMAIL1 = "";
        String strCT_EMAIL2 = "";
        String strSql = "SELECT EMP_EMAIL1,EMP_EMAIL2 FROM rhh_empleados "
                + " where EMP_NUM=" + this.document.getFieldInt("EMP_NUM") + "";
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strCT_EMAIL1 = rs.getString("EMP_EMAIL1");
                strCT_EMAIL2 = rs.getString("EMP_EMAIL2");
            }
            rs.close();
        } catch (SQLException ex) {
            this.strResultLast = "ERROR:" + ex.getMessage();
            ex.fillInStackTrace();
        }
        //validamos que hallan puesto el mail
        if (!strCT_EMAIL1.isEmpty() || !strCT_EMAIL2.isEmpty()) {
            Mail mail = new Mail();
            String strLstMail = "";
            //Validamos si el mail del cliente es valido
            if (mail.isEmail(strCT_EMAIL1)) {
                strLstMail += "," + strCT_EMAIL1;
            }
            if (mail.isEmail(strCT_EMAIL2)) {
                strLstMail += "," + strCT_EMAIL2;
            }
            //Intentamos mandar el mail
            mail.setBolDepuracion(false);
            mail.getTemplate("CANCEL_NOM", oConn);
            mail.getMensaje();
            mail.setReplaceContent(this.getDocument());
            strSql = "SELECT * FROM vta_empresas "
                    + " where EMP_ID=" + this.document.getFieldInt("EMP_ID") + "";
            try {
                ResultSet rs = oConn.runQuery(strSql);
                mail.setReplaceContent(rs);
                rs.close();
            } catch (SQLException ex) {
                this.strResultLast = "ERROR:" + ex.getMessage();
                ex.fillInStackTrace();
            }
            mail.setDestino(strLstMail);
            boolean bol = mail.sendMail();
            if (bol) {
                //strResp = "MAIL ENVIADO.";
            } else {
                //strResp = "FALLO EL ENVIO DEL MAIL.";
            }

        } else {
            //strResp = "ERROR: INGRESE UN MAIL";
        }
    }

    /**
     * Envio masivo de recibos, y facturas cfdi de nominas
     *
     * @param intEmpId
     * @param strFolioIni Es el folio inicial
     * @param strFolioFin Es el folio final
     * @param strCorreo
     * @param intCopia
     */
    public void doEnvioMasivo(int intEmpId, String strFolioIni, String strFolioFin, String strCorreo, int intCopia) {
        this.strResultLast = "OK";
        //Recuperamos valores para el envio del correo

        String strSql = "select NOM_ID,EMP_NUM from rhh_nominas where NOM_SE_TIMBRO <> 0 "
                + " AND NOM_FOLIO >= '" + strFolioIni + "' AND NOM_FOLIO <= '" + strFolioFin + "' "
                + " AND EMP_ID = " + intEmpId;
        ResultSet rs;
        try {
            rs = this.oConn.runQuery(strSql, true);
            while (rs.next()) {
                //Recuperamos el id
                int intNOM_ID = rs.getInt("NOM_ID");
                //Buscamos mail del empleado
                String strCT_EMAIL1 = "";
                String strCT_EMAIL2 = "";
                strSql = "SELECT EMP_EMAIL1,EMP_EMAIL2 FROM rhh_empleados "
                        + " where EMP_NUM=" + rs.getInt("EMP_NUM") + "";
                try {
                    ResultSet rs2 = oConn.runQuery(strSql, true);
                    while (rs2.next()) {
                        strCT_EMAIL1 = rs2.getString("EMP_EMAIL1");
                        strCT_EMAIL2 = rs2.getString("EMP_EMAIL2");
                    }
                    rs2.close();
                } catch (SQLException ex) {
                    this.strResultLast = "ERROR:" + ex.getMessage();
                    log.error(ex.getMessage());
                }
                //Evaluamos si hay correo donde enviar
                if (!strCT_EMAIL1.isEmpty() || !strCT_EMAIL1.isEmpty() || (!strCorreo.isEmpty() && intCopia == 1)) {
                    this.document.ObtenDatos(intNOM_ID, oConn);
                    //Objeto de mail
                    Mail mail = new Mail();
                    String strLstMail = "";
                    //Validamos si el mail del cliente es valido
                    if (mail.isEmail(strCT_EMAIL1)) {
                        strLstMail += "," + strCT_EMAIL1;
                    }
                    if (mail.isEmail(strCT_EMAIL2)) {
                        strLstMail += "," + strCT_EMAIL2;
                    }
                    if (mail.isEmail(strCorreo)) {
                        strLstMail += "," + strCorreo;
                    }
                    if (strLstMail.startsWith(",")) {
                        strLstMail = strLstMail.substring(1, strLstMail.length());
                    }
                    //Intentamos mandar el mail
                    mail.setBolDepuracion(false);
                    mail.getTemplate("MAIL_NOMINA", oConn);
                    mail.getMensaje();
                    mail.setReplaceContent(this.getDocument());
                    //Sacamos datos de la empresa
                    strSql = "SELECT * FROM vta_empresas "
                            + " where EMP_ID=" + this.document.getFieldInt("EMP_ID") + "";
                    try {
                        ResultSet rs2 = oConn.runQuery(strSql);
                        mail.setReplaceContent(rs2);
                        rs2.close();
                    } catch (SQLException ex) {
                        this.strResultLast = "ERROR:" + ex.getMessage();
                    }
                    mail.setDestino(strLstMail);
                    //Adjuntamos los archivos XML y PDF
                    //Versión 1.0
                    File fileXmlTmp = new File(this.strPATHXml + "Nomina_" + intNOM_ID + ".xml");
                    if (fileXmlTmp.exists()) {
                        log.debug("Versión 1.0 XML:" + this.strPATHXml + "Nomina_" + intNOM_ID + ".xml");
                        mail.setFichero(this.strPATHXml + "Nomina_" + intNOM_ID + ".xml");

                    } else {
                        //Version 2
                        StringBuilder strNomFile = new StringBuilder("");
                        ERP_MapeoFormato mapeoXml = new ERP_MapeoFormato(7);
                        strNomFile.append(getNombreFileXml(mapeoXml, this.document.getFieldInt("NOM_ID"), this.document.getFieldString("NOM_RAZONSOCIAL"), this.document.getFieldString("NOM_FECHA"), this.document.getFieldString("NOM_UUID")));

                        log.debug("Versión 2.0 XML:" + this.strPATHXml + strNomFile);
                        mail.setFichero(this.strPATHXml + strNomFile);
                    }
                    //Obtenemos el formato individual  de CFDI
                    String[] lstParamsName = {"nom_folio1", "nom_folio2", "emp_id", "sc_id"};
                    String[] lstParamsValue = {this.document.getFieldString("NOM_FOLIO"), this.document.getFieldString("NOM_FOLIO"), this.document.getFieldInt("EMP_ID") + "", this.document.getFieldInt("SC_ID") + ""};
                    String strNomFormatoCFDI = "NOM_CFDI";
                    String strNomFormatoRecibo = "NOM_RECIBO";
                    //Evaluamos si se usara un formato especial
                    if (!this.document.getFieldString("NOM_NOMFORMATO").trim().isEmpty()) {
                        strNomFormatoCFDI = this.document.getFieldString("NOM_NOMFORMATO");
                    }
                    if (!this.document.getFieldString("NOM_NOMFORMATO_RECIBO").trim().isEmpty()) {
                        strNomFormatoRecibo = this.document.getFieldString("NOM_NOMFORMATO_RECIBO");
                    }
                    //Obtenemos los formatos correspondientes
                    String strNomFile = this.doGeneraFormatoJasper(0, strNomFormatoCFDI, "PDF", this.getDocument(), lstParamsName, lstParamsValue, this.strPATHXml);
                    if (!strNomFile.isEmpty()) {
                        log.debug(strNomFile);
                        mail.setFichero(strNomFile);
                    }
                    //Obtenemos el formato individual  de CFDI
                    String strNomFile2 = this.doGeneraFormatoJasper(0, strNomFormatoRecibo, "PDF", this.getDocument(), lstParamsName, lstParamsValue, this.strPATHXml);
                    if (!strNomFile2.isEmpty()) {
                        log.debug(strNomFile2);
                        mail.setFichero(strNomFile2);
                    }

                    log.debug("Enviamos el correo....");

                    boolean bol = mail.sendMail();
                    if (bol) {
                        //Se envio el recibo de nomina
                    } else {
                        //No se envio...
                    }
                }

            }
            rs.close();

        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
    }

    /**
     * Generacion masiva de contabilidad
     *
     * @param intEmpId Es el id de la empresa
     * @param strFolioIni Es el folio inicial
     * @param strFolioFin Es el folio final
     */
    public void doGeneraContabilidad(int intEmpId, String strFolioIni, String strFolioFin) {
        this.strResultLast = "OK";
        //Recuperamos parametros de la empresa
        String strEMP_PASSCP = "";
        String strEMP_USERCP = "";
        String strEMP_URLCP = "";
        String strSql = "SELECT EMP_URLCP,EMP_USERCP,EMP_PASSCP FROM vta_empresas where EMP_ID=" + intEmpId + "";
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strEMP_URLCP = rs.getString("EMP_URLCP");
                strEMP_USERCP = rs.getString("EMP_USERCP");
                strEMP_PASSCP = rs.getString("EMP_PASSCP");
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        //Consulta a las nominas.
        strSql = "select NOM_ID,EMP_NUM from rhh_nominas where NOM_SE_TIMBRO <> 0 "
                + " AND NOM_FOLIO >= '" + strFolioIni + "' AND NOM_FOLIO <= '" + strFolioFin + "' "
                + " AND EMP_ID = " + intEmpId;
        ResultSet rs;
        try {
            rs = this.oConn.runQuery(strSql, true);
            while (rs.next()) {
                //Recuperamos el id
                int intNOM_ID = rs.getInt("NOM_ID");
                //Envio para la contabilidad
                ContabilidadUtil contaUtil = new ContabilidadUtil(oConn, varSesiones);
                TableMaster documentNom = new rhh_nominas();
                document.ObtenDatos(intNOM_ID, oConn);
                contaUtil.CalculaPolizaContablenNominas(intEmpId, strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP, documentNom.getFieldInt("NOM_MONEDA"), documentNom, intNOM_ID, 0, "NEW");
            }
            rs.close();

        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
    }

    /**
     * Realiza el cálculo de la nómina
     *
     * @param intNomina Es el id de la nómina por cálcular
     */
    public void CalculaNomina(int intNomina) {

        this.strResultLast = "OK";
        Rhh_Nominas_Master nominaMaster = new Rhh_Nominas_Master();
        ArrayList<TableMaster> lstEmpleados = null;
        ArrayList<TableMaster> lstIncidencias = null;
        boolean bolFondoAhorro = false;
        boolean bolDescInfonavit = false;
        boolean bolDescInfonacot = false;
        bolEsFolioSucursal = false;

        NominasFormulas formula = new NominasFormulas(oConn);
        formula.Init();
        // <editor-fold defaultstate="collapsed" desc="Obtenemos datos de la nomina(periodo de fecha)">
        nominaMaster.ObtenDatos(intNomina, oConn);
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Buscamos parametros de la Deducciones y Percepciones">
      /*Obtenemos los Id de  deduciones y percepciones */
        PercepcionesDeducciones PercDedu = new PercepcionesDeducciones(oConn, varSesiones.getIntIdEmpresa());
//      PercepcionesDeducciones PercDedu = new PercepcionesDeducciones(oConn, this.intEMP_ID);
        Map<String, PercepcionesDeduccionesE> HashMapPercepciones = PercDedu.ObtenPercepciones();
        Map<String, PercepcionesDeduccionesE> HashMapDeducciones = PercDedu.ObtenDeducciones();
        int intTP_ID = 0;
        int intIdPerc = 0;
        int intTD_ID = 0;
        int intIdDedu = 0;
        double dblSalarioMin = 0.0;
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Buscamos parametros de la empresa">
        String strSql = "select EMP_FONDO_AHORRO,EMP_REGISTRO_PATRONAL,EMP_FOLIO_SUCURSAL_NOMINA,EMP_SALARIO_MINIMO"
                + "  from vta_empresas where EMP_ID = " + nominaMaster.getFieldInt("EMP_ID");

        try {
            ResultSet rs;
            rs = oConn.runQuery(strSql);
            while (rs.next()) {
                if (rs.getInt("EMP_FONDO_AHORRO") == 1) {
                    bolFondoAhorro = true;
                }
                if (rs.getInt("EMP_FOLIO_SUCURSAL_NOMINA") == 1) {
                    bolEsFolioSucursal = true;
                }
                dblSalarioMin = rs.getDouble("EMP_SALARIO_MINIMO");
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Borramos las nominas">
        String strSqlDelete = "delete from rhh_nominas where NOM_SE_TIMBRO = 0 AND EMP_ID = " + nominaMaster.getFieldInt("EMP_ID");
        if (bolEsFolioSucursal) {
            strSqlDelete = "delete from rhh_nominas where NOM_SE_TIMBRO = 0 AND EMP_ID = " + nominaMaster.getFieldInt("EMP_ID")
                    + " AND SC_ID = " + nominaMaster.getFieldInt("SC_ID");
        }
        oConn.runQueryLMD(strSqlDelete);
        //Limpiamos el consecutivo de folios
        LimpiezaFolios(nominaMaster.getFieldInt("EMP_ID"), nominaMaster.getFieldInt("SC_ID"));
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Filtramos los empleados a los que se les hara el calculo">
        rhh_empleados empleadoTmp = new rhh_empleados();
        String strCondicion = "  emp_id = " + nominaMaster.getFieldInt("EMP_ID") + " and EMP_ACTIVO = 1 ";
        lstEmpleados = empleadoTmp.ObtenDatosVarios(strCondicion, oConn);
      // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Recorremos cada empleado para generar el calculo de su nomina">
        Iterator<TableMaster> it = lstEmpleados.iterator();
        while (it.hasNext()) {
            rhh_empleados empleado = (rhh_empleados) it.next();
            // <editor-fold defaultstate="collapsed" desc="Inicializamos el objeto de nominas">
            Nominas nominaEmpleado = new Nominas(oConn, this.varSesiones);
            nominaEmpleado.Init();
            nominaEmpleado.setIntEMP_ID(nominaMaster.getFieldInt("EMP_ID"));
            nominaEmpleado.getDocument().setFieldInt("EMP_ID", nominaMaster.getFieldInt("EMP_ID"));
            nominaEmpleado.getDocument().setFieldInt("SC_ID", nominaMaster.getFieldInt("SC_ID"));
            nominaEmpleado.getDocument().setFieldString("NOM_FECHA", fecha.getFechaActual());
            nominaEmpleado.getDocument().setFieldString("NOM_FECHA_INICIAL_PAGO", nominaMaster.getFieldString("RHN_FECHA_INICIAL"));
            nominaEmpleado.getDocument().setFieldString("NOM_FECHA_FINAL_PAGO", nominaMaster.getFieldString("RHN_FECHA_FINAL"));
            nominaEmpleado.getDocument().setFieldInt("EMP_NUM", empleado.getFieldInt("EMP_NUM"));
            nominaEmpleado.getDocument().setFieldInt("NOM_MONEDA", 1);
            nominaEmpleado.getDocument().setFieldString("NOM_CONCEPTO", nominaMaster.getFieldString("RHN_DESCRIPCION"));
            // <editor-fold defaultstate="collapsed" desc="Calculamos los dias Pagados">

            log.debug("Se Calculo dias pagados");
            double dblTotalFaltas = 0.0;
            double dblNumFaltas = 0;
            double dblNumFaltasInjust = 0;
            double dblDiasIncapacidad = 0;
            double dblDiasSancion = 0;
            double dblNumeroDiasTrabajados = 0.0;

            String strSqldp = "select RHIN_NUM_FALTAS_INJUSTIFICADAS, RHIN_NUM_FALTAS, RHIN_DIAS_INCAPACIDAD, RHIN_FALTAS_SANCION from rhh_incidencias where EMP_NUM = '" + empleado.getFieldInt("EMP_NUM") + "' and RHN_ID = '" + nominaMaster.getFieldInt("RHN_ID") + "';";
            try {
                ResultSet rs = oConn.runQuery(strSqldp, true);
                while (rs.next()) {
                    dblNumFaltas = rs.getInt("RHIN_NUM_FALTAS");
                    dblNumFaltasInjust = rs.getInt("RHIN_NUM_FALTAS_INJUSTIFICADAS");
                    dblDiasIncapacidad = rs.getInt("RHIN_DIAS_INCAPACIDAD");
                    dblDiasSancion = rs.getInt("RHIN_FALTAS_SANCION");
                }
                rs.close();
            } catch (SQLException ex) {
                log.error(ex.getMessage());
            }

            double falta = 0.0;
            double faltaJust = 0.0;
            double faltaIncap = 0.0;
            double faltaSancion = 0.0;

            String strSqlh = "select HR_FACTOR_FALTAS, HR_FACTOR_FALTAS_INJUS, HR_FACTOR_INCAPACIDAD,HR_FACTOR_FALTA_SANCION from rhh_horarios where HR_ID = '" + empleado.getFieldString("EMP_HORARIO") + "';";
            try {
                ResultSet rs = oConn.runQuery(strSqlh, true);
                while (rs.next()) {
                    falta = rs.getDouble("HR_FACTOR_FALTAS");
                    faltaJust = rs.getDouble("HR_FACTOR_FALTAS_INJUS");
                    faltaIncap = rs.getDouble("HR_FACTOR_INCAPACIDAD");
                    faltaSancion = rs.getDouble("HR_FACTOR_FALTA_SANCION");
                }
                rs.close();
            } catch (SQLException ex) {
                log.error(ex.getMessage());
            }

            log.debug("Faltas justificadas: " + dblNumFaltas + " factor falta:" + falta);
            log.debug("Faltas injustificadas: " + dblNumFaltasInjust + " factor falta:" + faltaJust);
            log.debug("Faltas Incapacidad: " + dblDiasIncapacidad + " factor falta:" + faltaIncap);
            log.debug("Faltas por Sanción: " + dblDiasSancion + " factor falta:" + faltaSancion);

            dblNumFaltas = dblNumFaltas * falta;
            dblNumFaltasInjust = dblNumFaltasInjust * faltaJust;
            dblDiasIncapacidad = dblDiasIncapacidad * faltaIncap;
            dblDiasSancion = dblDiasSancion * faltaSancion;

            dblTotalFaltas = dblNumFaltas + dblNumFaltasInjust + dblDiasIncapacidad + dblDiasSancion;
            dblNumeroDiasTrabajados = nominaMaster.getFieldInt("RHN_NUMERO_DIAS") - dblTotalFaltas;

            log.debug("Faltas justificadas: " + dblNumFaltas);
            log.debug("Faltas injustificadas: " + dblNumFaltasInjust);
            log.debug("Faltas Incapacidad: " + dblDiasIncapacidad);
            log.debug("Faltas Sanción: " + dblDiasSancion);

            log.debug("Días trabajados: " + dblNumeroDiasTrabajados);

            nominaEmpleado.getDocument().setFieldDouble("NOM_NUM_DIAS_PAGADOS", dblNumeroDiasTrabajados);
            // </editor-fold>
            nominaEmpleado.getDocument().setFieldDouble("NOM_DESCUENTO", 0.0);
            //Datos que copiamos de los empleados
            nominaEmpleado.getDocument().setFieldString("NOM_METODODEPAGO", empleado.getFieldString("EMP_METODO_PAGO"));
            nominaEmpleado.getDocument().setFieldString("NOM_NUMCUENTA", empleado.getFieldString("EMP_NUM_CTA"));
            nominaEmpleado.getDocument().setFieldString("NOM_CONDPAGO", empleado.getFieldString("EMP_CONDICIONES"));
            nominaEmpleado.getDocument().setFieldString("NOM_FORMADEPAGO", empleado.getFieldString("EMP_FORMA_DE_PAGO"));
            nominaEmpleado.getDocument().setFieldInt("RHN_ID", intNomina);

            // </editor-fold>  
            // <editor-fold defaultstate="collapsed" desc="Recorremos cada empleado para generar el calculo de sus percepciones principales">
            //Si NO es aguinaldo es sueldo normal buscamos percepcion 
            intTP_ID = HashMapPercepciones.get("1").getIntIdTipo();
            intIdPerc = HashMapPercepciones.get("1").getIntIdPercDedu();
            if (nominaMaster.getFieldInt("RHN_ES_AGUINALDO") == 1) {
                intTP_ID = HashMapPercepciones.get("2").getIntIdTipo();
                intIdPerc = HashMapPercepciones.get("2").getIntIdPercDedu();
                log.debug("La bandera de AGUINALDO esta activada = 1");
            } else {
                log.debug("La bandera de AGUINALDO esta activada = 0 es Sueldo");
            }

            //Cálculamos el concepto....
            formula.setEmpleadoActual(empleado);
            formula.setNominaActual(nominaMaster);
            formula.setNomina(nominaEmpleado);
            ItemConcepto concepto = formula.CalculaImporte(intIdPerc, true, empleado, null);
            if (concepto != null) {
                log.debug("El importe calculado es..." + concepto.getStrCve() + " " + concepto.getDblImporte());
                if (nominaMaster.getFieldInt("RHN_ES_AGUINALDO") == 1) {
                    /*Verificamos si tiene gravado o exento Hasta 30 días de salario minimo estan exentos*/
                    double dblAguinaldo = dblSalarioMin * 30;
                    if (concepto.getDblImporte() > dblAguinaldo) {
                        log.debug("Aguinaldo tiene Gravado y Excento = " + concepto.getDblImporte() + " > " + dblAguinaldo);
                        /*Gravado*/
                        double dblGravado = (concepto.getDblImporte()) - (dblAguinaldo);
                        /*Excento*/
                        double dblExcento = concepto.getDblImporte() - dblGravado;
                        rhh_nominas_deta detalle1 = new rhh_nominas_deta();
                        detalle1.setFieldInt("TP_ID", intTP_ID);
                        detalle1.setFieldInt("PERC_ID", intIdPerc);
                        detalle1.setFieldInt("TD_ID", 0);
                        detalle1.setFieldInt("DEDU_ID", 0);
                        detalle1.setFieldInt("NOMD_CANTIDAD", 1);
                        detalle1.setFieldDouble("NOMD_UNITARIO", (dblGravado + dblExcento));
                        detalle1.setFieldDouble("NOMD_GRAVADO", 1);
                        detalle1.setFieldDouble("NOMD_IMPORTE_GRAVADO", dblGravado);
                        detalle1.setFieldDouble("NOMD_IMPORTE_EXENTO", dblExcento);
                        nominaEmpleado.getLstConceptos().add(detalle1);
                        log.debug("Operacion Aguinaldo Gravado = " + dblGravado + " = " + concepto.getDblImporte() + " - " + dblAguinaldo);

                    } else {
                        log.debug("Aguinaldo NOO tiene Gravado y Excento = " + concepto.getDblImporte() + " > " + dblAguinaldo);
                        rhh_nominas_deta detalle = new rhh_nominas_deta();
                        detalle.setFieldInt("TP_ID", intTP_ID);
                        detalle.setFieldInt("PERC_ID", intIdPerc);
                        detalle.setFieldInt("TD_ID", 0);
                        detalle.setFieldInt("DEDU_ID", 0);
                        detalle.setFieldInt("NOMD_CANTIDAD", 1);
                        detalle.setFieldDouble("NOMD_UNITARIO", concepto.getDblImporte());
                        detalle.setFieldDouble("NOMD_IMPORTE_EXENTO", concepto.getDblImporte());
                        detalle.setFieldDouble("NOMD_GRAVADO", 0);
                        nominaEmpleado.getLstConceptos().add(detalle);

                    }
                } else {
                    rhh_nominas_deta detalle = new rhh_nominas_deta();
                    detalle.setFieldInt("TP_ID", intTP_ID);
                    detalle.setFieldInt("PERC_ID", intIdPerc);
                    detalle.setFieldInt("TD_ID", 0);
                    detalle.setFieldInt("DEDU_ID", 0);
                    detalle.setFieldInt("NOMD_CANTIDAD", 1);
                    detalle.setFieldDouble("NOMD_UNITARIO", concepto.getDblImporte());
                    detalle.setFieldDouble("NOMD_IMPORTE_EXENTO", 0);
                    detalle.setFieldDouble("NOMD_IMPORTE_GRAVADO", concepto.getDblImporte());
                    nominaEmpleado.getLstConceptos().add(detalle);
                }
            } else {
                log.debug("No se encontro el concepto...");
            }
         //nomina.getLstConceptos().add(document)

            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Consultamos incidencias">
            RhhIncidencias incidenciasTmp = new RhhIncidencias();
            String strCondicion1 = "  EMP_NUM = " + empleado.getFieldInt("EMP_NUM") + " and RHN_ID = " + nominaMaster.getFieldInt("RHN_ID");
            lstIncidencias = incidenciasTmp.ObtenDatosVarios(strCondicion1, oConn);

////            // <editor-fold defaultstate="collapsed" desc="Excepciones">
//            try {
//                intTP_ID = HashMapPercepciones.get("33").getIntIdTipo();
//                intIdPerc = HashMapPercepciones.get("33").getIntIdPercDedu();
//                calculaExcepciones(empleado, nominaMaster, nominaEmpleado, intTP_ID, intIdPerc);
//            } catch (SQLException ex) {
//                java.util.logging.Logger.getLogger(Nominas.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//            // </editor-fold>    
            // <editor-fold defaultstate="collapsed" desc="Recorremos cada Incidencia">
            Iterator<TableMaster> it1 = lstIncidencias.iterator();
            while (it1.hasNext()) {
                RhhIncidencias incidencias = (RhhIncidencias) it1.next();

                // <editor-fold defaultstate="collapsed" desc="Premio de asistencia">            
                int intPremioAsistencia = incidencias.getFieldInt("RHIN_APLICA_PREMIO_ASISTENCIA");
                log.debug("La bandera de Premio de Asistencia esta activada =" + intPremioAsistencia);
                if (intPremioAsistencia == 1) {
                    formula.setEmpleadoActual(empleado);
                    formula.setNominaActual(nominaMaster);
                    formula.setIncidenciaActual(incidencias);
                    formula.setNomina(nominaEmpleado);

                    intTP_ID = HashMapPercepciones.get("32").getIntIdTipo();
                    intIdPerc = HashMapPercepciones.get("32").getIntIdPercDedu();

                    concepto = formula.CalculaImporte(intIdPerc, true, empleado, incidencias);
                    if (concepto != null) {
                        log.debug("El importe calculado es..." + concepto.getStrCve() + " " + concepto.getDblImporte());
                        if (concepto.getDblImporte() > 0) {
                        rhh_nominas_deta detalle = new rhh_nominas_deta();
                        detalle.setFieldInt("TP_ID", intTP_ID);
                        detalle.setFieldInt("PERC_ID", intIdPerc);
                        detalle.setFieldInt("TD_ID", 0);
                        detalle.setFieldInt("DEDU_ID", 0);
                        detalle.setFieldInt("NOMD_CANTIDAD", 1);
                        detalle.setFieldDouble("NOMD_UNITARIO", concepto.getDblImporte());
                        detalle.setFieldDouble("NOMD_IMPORTE_GRAVADO", concepto.getDblImporte());
                        detalle.setFieldDouble("NOMD_IMPORTE_EXENTO", 0);
                        detalle.setFieldDouble("NOMD_GRAVADO", 1);
                        nominaEmpleado.getLstConceptos().add(detalle);
                        }
                    } else {
                        log.debug("No se encontro el concepto...");
                    }
                }
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="Premio de Puntualidad">            
                int intPremioPuntualidad = incidencias.getFieldInt("RHIN_APLICA_PREMIO_PUNTUALIDAD");
                log.debug("La bandera de Premio de Puntualidad esta activada =" + intPremioPuntualidad);
                if (intPremioPuntualidad == 1) {
                    formula.setEmpleadoActual(empleado);
                    formula.setNominaActual(nominaMaster);
                    formula.setIncidenciaActual(incidencias);
                    formula.setNomina(nominaEmpleado);

                    intTP_ID = HashMapPercepciones.get("31").getIntIdTipo();
                    intIdPerc = HashMapPercepciones.get("31").getIntIdPercDedu();

                    concepto = formula.CalculaImporte(intIdPerc, true, empleado, incidencias);
                    if (concepto != null) {
                        log.debug("El importe calculado es..." + concepto.getStrCve() + " " + concepto.getDblImporte());
                        if (concepto.getDblImporte() > 0) {
                        rhh_nominas_deta detalle = new rhh_nominas_deta();
                        detalle.setFieldInt("TP_ID", intTP_ID);
                        detalle.setFieldInt("PERC_ID", intIdPerc);
                        detalle.setFieldInt("TD_ID", 0);
                        detalle.setFieldInt("DEDU_ID", 0);
                        detalle.setFieldInt("NOMD_CANTIDAD", 1);
                        detalle.setFieldDouble("NOMD_UNITARIO", concepto.getDblImporte());
                        detalle.setFieldDouble("NOMD_IMPORTE_GRAVADO", concepto.getDblImporte());
                        detalle.setFieldDouble("NOMD_IMPORTE_EXENTO", 0);
                        detalle.setFieldDouble("NOMD_GRAVADO", 1);
                        nominaEmpleado.getLstConceptos().add(detalle);
                        }

                    } else {
                        log.debug("No se encontro el concepto...");
                    }
                }
                // </editor-fold>  
                // <editor-fold defaultstate="collapsed" desc="Premio de Cumplimiento">            
                int intPremioCumplimiento = incidencias.getFieldInt("RHIN_APLICA_PREMIO_PRODUCTIVIDAD");
                log.debug("La bandera de Premio de Cumplimiento esta activada =" + intPremioCumplimiento);
                if (intPremioCumplimiento == 1) {
                    formula.setEmpleadoActual(empleado);
                    formula.setNominaActual(nominaMaster);
                    formula.setIncidenciaActual(incidencias);
                    formula.setNomina(nominaEmpleado);

                    intTP_ID = HashMapPercepciones.get("35").getIntIdTipo();
                    intIdPerc = HashMapPercepciones.get("35").getIntIdPercDedu();

                    concepto = formula.CalculaImporte(intIdPerc, true, empleado, incidencias);
                    if (concepto != null) {
                        log.debug("El importe calculado es..." + concepto.getStrCve() + " " + concepto.getDblImporte());
                        if (concepto.getDblImporte() > 0) {
                        rhh_nominas_deta detalle = new rhh_nominas_deta();
                        detalle.setFieldInt("TP_ID", intTP_ID);
                        detalle.setFieldInt("PERC_ID", intIdPerc);
                        detalle.setFieldInt("TD_ID", 0);
                        detalle.setFieldInt("DEDU_ID", 0);
                        detalle.setFieldInt("NOMD_CANTIDAD", 1);
                        detalle.setFieldDouble("NOMD_UNITARIO", concepto.getDblImporte());
                        detalle.setFieldDouble("NOMD_IMPORTE_GRAVADO", concepto.getDblImporte());
                        detalle.setFieldDouble("NOMD_IMPORTE_EXENTO", 0);
                        detalle.setFieldDouble("NOMD_GRAVADO", 1);
                        nominaEmpleado.getLstConceptos().add(detalle);
                        }
                    } else {
                        log.debug("No se encontro el concepto...");
                    }
                }
                // </editor-fold>  
                // <editor-fold defaultstate="collapsed" desc="Validamos si aplica prima vacacional">
                int intPrimaVacacional = incidencias.getFieldInt("RHIN_APLICA_VACACIONES");

                log.debug("La bandera de Prima Vacacional esta activada= " + intPrimaVacacional);
                if (intPrimaVacacional == 1) {
                    formula.setEmpleadoActual(empleado);
                    formula.setNominaActual(nominaMaster);
                    formula.setIncidenciaActual(incidencias);

                    intTP_ID = HashMapPercepciones.get("21").getIntIdTipo();
                    intIdPerc = HashMapPercepciones.get("21").getIntIdPercDedu();

                    concepto = formula.CalculaImporte(intIdPerc, true, empleado, incidencias);
                    if (concepto != null) {
                        log.debug("El importe calculado es..." + concepto.getStrCve() + " " + concepto.getDblImporte());
                        /*Verificamos si tiene gravado o exento Hasta 15 días de salario mínimo*/
                        double dblPrimaVacacional = dblSalarioMin * 15;
                        log.debug("Verificamos si tiene gravado o exento Hasta 15 días de salario mínimo");
                        log.debug("dblPrimaVacacional = " + dblSalarioMin + " * " + 15);
                        if (concepto.getDblImporte() > dblPrimaVacacional) {
                            log.debug("prima vacacional tiene Gravado y Excento = " + concepto.getDblImporte() + " > " + dblPrimaVacacional);
                            /*Gravado*/
                            double dblGravado = (concepto.getDblImporte()) - (dblPrimaVacacional);
                            double dblExcento = concepto.getDblImporte() - dblGravado;
                            rhh_nominas_deta detalle1 = new rhh_nominas_deta();
                            detalle1.setFieldInt("TP_ID", intTP_ID);
                            detalle1.setFieldInt("PERC_ID", intIdPerc);
                            detalle1.setFieldInt("TD_ID", 0);
                            detalle1.setFieldInt("DEDU_ID", 0);
                            detalle1.setFieldInt("NOMD_CANTIDAD", 1);
                            detalle1.setFieldDouble("NOMD_UNITARIO", concepto.getDblImporte());
                            detalle1.setFieldDouble("NOMD_IMPORTE_GRAVADO", dblGravado);
                            detalle1.setFieldDouble("NOMD_IMPORTE_EXENTO", dblExcento);
                            detalle1.setFieldDouble("NOMD_GRAVADO", 1);
                            nominaEmpleado.getLstConceptos().add(detalle1);
                            log.debug("Operacion prima vacacional Gravado = " + dblGravado + " = " + concepto.getDblImporte() + " - " + dblPrimaVacacional);
                            /*Excento*/
                            log.debug("Operacion prima vacacional Excento = " + dblExcento + " = " + concepto.getDblImporte() + " - " + dblGravado);
                        } else {
                            log.debug("prima vacacional NOO tiene Gravado y Excento = " + concepto.getDblImporte() + " > " + dblPrimaVacacional);
                            rhh_nominas_deta detalle = new rhh_nominas_deta();
                            detalle.setFieldInt("TP_ID", intTP_ID);
                            detalle.setFieldInt("PERC_ID", intIdPerc);
                            detalle.setFieldInt("TD_ID", 0);
                            detalle.setFieldInt("DEDU_ID", 0);
                            detalle.setFieldInt("NOMD_CANTIDAD", 1);
                            detalle.setFieldDouble("NOMD_UNITARIO", concepto.getDblImporte());
                            detalle.setFieldDouble("NOMD_IMPORTE_GRAVADO", 0);
                            detalle.setFieldDouble("NOMD_IMPORTE_EXENTO", concepto.getDblImporte());
                            detalle.setFieldDouble("NOMD_GRAVADO", 0);
                            nominaEmpleado.getLstConceptos().add(detalle);
                        }
                    } else {
                        log.debug("No se encontro el concepto...");
                    }
                }
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="Bono y Gratificaciones">            
                int intBonoGratifica = incidencias.getFieldInt("RHIN_APLICA_BONOS_GRATIFICACIONES");

                log.debug("La bandera de Bono y gratificaciones esta activada =" + intBonoGratifica);
                if (intBonoGratifica == 1) {
                    formula.setEmpleadoActual(empleado);
                    formula.setNominaActual(nominaMaster);
                    formula.setIncidenciaActual(incidencias);

                    intTP_ID = HashMapPercepciones.get("33").getIntIdTipo();
                    intIdPerc = HashMapPercepciones.get("33").getIntIdPercDedu();

                    concepto = formula.CalculaImporte(intIdPerc, true, empleado, incidencias);
                    if (concepto != null) {
                        log.debug("El importe calculado es..." + concepto.getStrCve() + " " + concepto.getDblImporte());
                        rhh_nominas_deta detalle = new rhh_nominas_deta();
                        detalle.setFieldInt("TP_ID", intTP_ID);
                        detalle.setFieldInt("PERC_ID", intIdPerc);
                        detalle.setFieldInt("TD_ID", 0);
                        detalle.setFieldInt("DEDU_ID", 0);
                        detalle.setFieldInt("NOMD_CANTIDAD", 1);
                        detalle.setFieldDouble("NOMD_UNITARIO", concepto.getDblImporte());
                        detalle.setFieldDouble("NOMD_IMPORTE_GRAVADO", concepto.getDblImporte());
                        detalle.setFieldDouble("NOMD_IMPORTE_EXENTO", 0);
                        detalle.setFieldDouble("NOMD_GRAVADO", 1);
                        nominaEmpleado.getLstConceptos().add(detalle);
                    } else {
                        log.debug("No se encontro el concepto...");
                    }
                }
            // </editor-fold>

                // <editor-fold defaultstate="collapsed" desc="Validamos si aplica la prima dominical">
                int intPrimaDominical = incidencias.getFieldInt("RHIN_APLICA_PRIMA_DOMINICAL");
                log.debug("La bandera de prima dominical esta activada= " + intPrimaDominical);
                if (intPrimaDominical == 1) {
                    formula.setEmpleadoActual(empleado);
                    formula.setNominaActual(nominaMaster);
                    formula.setIncidenciaActual(incidencias);

                    intTP_ID = HashMapPercepciones.get("20").getIntIdTipo();
                    intIdPerc = HashMapPercepciones.get("20").getIntIdPercDedu();

                    concepto = formula.CalculaImporte(intIdPerc, true, empleado, incidencias);
                    if (concepto != null) {
                        log.debug("El importe calculado es..." + concepto.getStrCve() + " " + concepto.getDblImporte());
                        /*Verificamos si tiene gravado o exento Hasta 1 salario minimo x cada domingo*/
                        int intDomingosTrabajados = 0;
                        String strSql1 = "select RHPD_FECHA from rhh_prima_dominical where RHIN_ID  = " + incidencias.getFieldInt("RHIN_ID");
                        try {
                            ResultSet rs = oConn.runQuery(strSql1, true);
                            while (rs.next()) {
                                intDomingosTrabajados = intDomingosTrabajados + 1;
                            }
                            rs.close();
                        } catch (SQLException ex) {
                            log.error(ex.getMessage());
                        }
                        double dblTopePrimDom = dblSalarioMin * intDomingosTrabajados;
                        rhh_nominas_deta detalle1 = new rhh_nominas_deta();
                        if (concepto.getDblImporte() > dblTopePrimDom) {
                            double dblGravado = concepto.getDblImporte() - dblTopePrimDom;
                            double dblExcento = concepto.getDblImporte() - dblGravado;
                            detalle1.setFieldInt("TP_ID", intTP_ID);
                            detalle1.setFieldInt("PERC_ID", intIdPerc);
                            detalle1.setFieldInt("TD_ID", 0);
                            detalle1.setFieldInt("DEDU_ID", 0);
                            detalle1.setFieldInt("NOMD_CANTIDAD", 1);
                            detalle1.setFieldDouble("NOMD_UNITARIO", concepto.getDblImporte());
                            detalle1.setFieldDouble("NOMD_IMPORTE_GRAVADO", dblGravado);
                            detalle1.setFieldDouble("NOMD_IMPORTE_EXENTO", dblExcento);
                            detalle1.setFieldDouble("NOMD_GRAVADO", 1);
                        } else {
                            double dblExcento = concepto.getDblImporte();
                            detalle1.setFieldInt("TP_ID", intTP_ID);
                            detalle1.setFieldInt("PERC_ID", intIdPerc);
                            detalle1.setFieldInt("TD_ID", 0);
                            detalle1.setFieldInt("DEDU_ID", 0);
                            detalle1.setFieldInt("NOMD_CANTIDAD", 1);
                            detalle1.setFieldDouble("NOMD_UNITARIO", concepto.getDblImporte());
                            detalle1.setFieldDouble("NOMD_IMPORTE_GRAVADO", 0);
                            detalle1.setFieldDouble("NOMD_IMPORTE_EXENTO", dblExcento);
                            detalle1.setFieldDouble("NOMD_GRAVADO", 1);
                        }
                        nominaEmpleado.getLstConceptos().add(detalle1);

                    } else {
                        log.debug("No se encontro el concepto...");
                    }
                }
                // </editor-fold>

                // <editor-fold defaultstate="collapsed" desc="Validamos si tiene horas extra">
                boolean horasExtra = existHorasExtra(incidencias);
                log.debug("Existen horas extra " + horasExtra);
                if (horasExtra == true) {
                    formula.setEmpleadoActual(empleado);
                    formula.setNominaActual(nominaMaster);
                    formula.setIncidenciaActual(incidencias);

                    intTP_ID = HashMapPercepciones.get("19").getIntIdTipo();
                    intIdPerc = HashMapPercepciones.get("19").getIntIdPercDedu();

                    concepto = formula.CalculaImporte(intIdPerc, true, empleado, incidencias);
                    if (concepto != null) {
                        log.debug("El importe calculado es..." + concepto.getStrCve() + " " + concepto.getDblImporte());
                        strSql = " select EMP_SALARIO_MINIMO from vta_empresas where EMP_ID = " + varSesiones.getIntIdEmpresa();
                        double dblTopeHR = 0.0;
                        dblTopeHR = dblSalarioMin * 5;
                        double dblImporteHR = concepto.getDblImporte() / 2;

                        rhh_nominas_deta detalle = new rhh_nominas_deta();
                        if (dblImporteHR > dblTopeHR) {
                            double dblGravado = dblImporteHR - dblTopeHR;
                            double dblExento = dblImporteHR - dblGravado;
                            detalle.setFieldInt("TP_ID", intTP_ID);
                            detalle.setFieldInt("PERC_ID", intIdPerc);
                            detalle.setFieldInt("TD_ID", 0);
                            detalle.setFieldInt("DEDU_ID", 0);
                            detalle.setFieldInt("NOMD_CANTIDAD", 1);
                            detalle.setFieldDouble("NOMD_UNITARIO", concepto.getDblImporte());
                            detalle.setFieldDouble("NOMD_GRAVADO", 1);
                            detalle.setFieldDouble("NOMD_IMPORTE_GRAVADO", dblGravado);
                            detalle.setFieldDouble("NOMD_IMPORTE_EXENTO", dblExento);
                        } else {
                            double dblExento = dblImporteHR;
                            double dblGravado = dblImporteHR;
                            detalle.setFieldInt("TP_ID", intTP_ID);
                            detalle.setFieldInt("PERC_ID", intIdPerc);
                            detalle.setFieldInt("TD_ID", 0);
                            detalle.setFieldInt("DEDU_ID", 0);
                            detalle.setFieldInt("NOMD_CANTIDAD", 1);
                            detalle.setFieldDouble("NOMD_UNITARIO", concepto.getDblImporte());
                            detalle.setFieldDouble("NOMD_GRAVADO", 1);
                            detalle.setFieldDouble("NOMD_IMPORTE_GRAVADO", dblGravado);
                            detalle.setFieldDouble("NOMD_IMPORTE_EXENTO", dblExento);
                        }

                        nominaEmpleado.getLstConceptos().add(detalle);
                    } else {
                        log.debug("No se encontro el concepto...");
                    }
                }
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="Retroactivo">
                log.debug("Retroactivo...");

                formula.setEmpleadoActual(empleado);
                formula.setNominaActual(nominaMaster);
                formula.setIncidenciaActual(incidencias);
                intTP_ID = HashMapPercepciones.get("36").getIntIdTipo();
                intIdPerc = HashMapPercepciones.get("36").getIntIdPercDedu();

                int intDiasRetroactivo = 0;
                String strSql2 = "select RHIN_DIAS_RETROACTIVO from rhh_incidencias where EMP_NUM = '" + empleado.getFieldInt("EMP_NUM") + "' and RHN_ID = " + nominaMaster.getFieldInt("RHN_ID");

                try {
                    ResultSet rs;
                    rs = oConn.runQuery(strSql2);
                    while (rs.next()) {
                        intDiasRetroactivo = rs.getInt("RHIN_DIAS_RETROACTIVO");
                    }
                    rs.close();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }

                if (intDiasRetroactivo != 0) {
                    concepto = formula.CalculaImporte(intIdPerc, true, empleado, incidencias);
                    if (concepto != null) {
                        log.debug("Los dias de Retroactivo son : " + intDiasRetroactivo);
                        log.debug("El importe calculado es..." + concepto.getStrCve() + " " + concepto.getDblImporte());
                        rhh_nominas_deta detalle = new rhh_nominas_deta();
                        detalle.setFieldInt("TP_ID", intTP_ID);
                        detalle.setFieldInt("PERC_ID", intIdPerc);
                        detalle.setFieldInt("TD_ID", 0);
                        detalle.setFieldInt("DEDU_ID", 0);
                        detalle.setFieldInt("NOMD_CANTIDAD", 1);
                        detalle.setFieldDouble("NOMD_UNITARIO", concepto.getDblImporte());
                        detalle.setFieldDouble("NOMD_GRAVADO", 1);
                        detalle.setFieldDouble("NOMD_IMPORTE_GRAVADO", concepto.getDblImporte());
                        detalle.setFieldDouble("NOMD_IMPORTE_EXENTO", 0);
                        nominaEmpleado.getLstConceptos().add(detalle);
                    } else {
                        log.debug("No se encontro el concepto...");
                    }
                } else {
                    log.debug("Los dias de Retroactivo son 0 No se calcula");
                }
            // </editor-fold>  

                //Revisar aportación de la empresa 26/09/2015
                //Es la misma que la deducción.
                // <editor-fold defaultstate="collapsed" desc="Retiro Fondo de ahorro">
// </editor-fold>      
            }
         // </editor-fold>      
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Consultamos prestamos">
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Descuento prestamos">

            // </editor-fold>  
            // <editor-fold defaultstate="collapsed" desc="Retiro Fondo de ahorro">
            // </editor-fold>         
            // <editor-fold defaultstate="collapsed" desc="Recorremos cada empleado para generar el calculo de sus deducciones">
            // <editor-fold defaultstate="collapsed" desc="Fondo de Ahorro">
            if (bolFondoAhorro) {
                log.debug("La bandera de Fondo Ahorro esta activada= " + bolFondoAhorro);
                formula.setEmpleadoActual(empleado);
                formula.setNominaActual(nominaMaster);

                intTD_ID = HashMapDeducciones.get("5").getIntIdTipo();
                intIdDedu = HashMapDeducciones.get("5").getIntIdPercDedu();

                concepto = formula.CalculaImporte(intIdDedu, false, empleado, null);
                if (concepto != null) {
                    log.debug("El importe calculado es..." + concepto.getStrCve() + " " + concepto.getDblImporte());
                    rhh_nominas_deta detalle = new rhh_nominas_deta();
                    detalle.setFieldInt("TP_ID", 0);
                    detalle.setFieldInt("PERC_ID", 0);
                    detalle.setFieldInt("TD_ID", intTD_ID);
                    detalle.setFieldInt("DEDU_ID", intIdDedu);
                    detalle.setFieldInt("NOMD_CANTIDAD", 1);
                    detalle.setFieldDouble("NOMD_UNITARIO", concepto.getDblImporte());
                    detalle.setFieldDouble("NOMD_GRAVADO", 0);
                    detalle.setFieldDouble("NOMD_IMPORTE_GRAVADO", 0);
                    detalle.setFieldDouble("NOMD_IMPORTE_EXENTO", concepto.getDblImporte());
                    nominaEmpleado.getLstConceptos().add(detalle);

                } else {
                    log.debug("No se encontro el concepto...");
                }

                formula.setEmpleadoActual(empleado);
                formula.setNominaActual(nominaMaster);

                intTP_ID = HashMapPercepciones.get("5").getIntIdTipo();
                intIdPerc = HashMapPercepciones.get("5").getIntIdPercDedu();

                concepto = formula.CalculaImporte(intIdPerc, true, empleado, null);
                if (concepto != null) {
                    log.debug("El importe calculado es..." + concepto.getStrCve() + " " + concepto.getDblImporte());
                    rhh_nominas_deta detalle = new rhh_nominas_deta();
                    detalle.setFieldInt("TP_ID", intTP_ID);
                    detalle.setFieldInt("PERC_ID", intIdPerc);
                    detalle.setFieldInt("TD_ID", 0);
                    detalle.setFieldInt("DEDU_ID", 0);
                    detalle.setFieldInt("NOMD_CANTIDAD", 1);
                    detalle.setFieldDouble("NOMD_UNITARIO", concepto.getDblImporte());
                    detalle.setFieldDouble("NOMD_GRAVADO", 0);
                    detalle.setFieldDouble("NOMD_IMPORTE_GRAVADO", 0);
                    detalle.setFieldDouble("NOMD_IMPORTE_EXENTO", concepto.getDblImporte());
                    nominaEmpleado.getLstConceptos().add(detalle);

                } else {
                    log.debug("No se encontro el concepto...");
                }
            }

            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Query Obtiene Activo INFONAVIT">   
            bolDescInfonavit = false;
            String strdescInfonavit = "select RHDI_ACTIVO from "
                    + "rhh_descuentos_infonavit where EMP_NUM = " + empleado.getFieldInt("EMP_NUM") + " ;";

            try {
                ResultSet rs;
                rs = oConn.runQuery(strdescInfonavit);
                while (rs.next()) {
                    if (rs.getInt("RHDI_ACTIVO") == 1) {
                        bolDescInfonavit = true;
                    }
                }
                rs.close();
            } catch (SQLException ex) {
                log.error(ex.getMessage());
            }
            // </editor-fold>
            //          <editor-fold defaultstate="collapsed" desc="Descuento Infonavit">
            if (bolDescInfonavit) {
                log.debug("La bandera de Desc Infonavit esta activada= " + bolDescInfonavit);
                formula.setEmpleadoActual(empleado);
                formula.setNominaActual(nominaMaster);

                intTD_ID = HashMapDeducciones.get("10").getIntIdTipo();
                intIdDedu = HashMapDeducciones.get("10").getIntIdTipo();

                concepto = formula.CalculaImporte(intIdDedu, false, empleado, null);
                if (concepto != null) {
                    log.debug("El importe calculado es..." + concepto.getStrCve() + " | " + concepto.getDblImporte());
                    rhh_nominas_deta detalle = new rhh_nominas_deta();
                    detalle.setFieldInt("TP_ID", 0);
                    detalle.setFieldInt("PERC_ID", 0);
                    detalle.setFieldInt("TD_ID", intTD_ID);
                    detalle.setFieldInt("DEDU_ID", intIdDedu);
                    detalle.setFieldInt("NOMD_CANTIDAD", 1);
                    detalle.setFieldDouble("NOMD_UNITARIO", concepto.getDblImporte());
                    detalle.setFieldDouble("NOMD_GRAVADO", 0);
                    detalle.setFieldDouble("NOMD_IMPORTE_GRAVADO", 0);
                    detalle.setFieldDouble("NOMD_IMPORTE_EXENTO", concepto.getDblImporte());
                    nominaEmpleado.getLstConceptos().add(detalle);
                } else {
                    log.debug("No se encontro el concepto...");
                }
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Descuento Infonacot">
            String strdescInfonacot = "select RHDF_ACTIVO from "
                    + "rhh_descuentos_infonacot where EMP_NUM = " + empleado.getFieldInt("EMP_NUM") + " ;";

            try {
                ResultSet rs;
                rs = oConn.runQuery(strdescInfonacot);
                while (rs.next()) {
                    if (rs.getInt("RHDF_ACTIVO") == 1) {
                        log.debug("La bandera de Descuento Infonacot esta activada= " + rs.getInt("RHDF_ACTIVO"));
                        bolDescInfonavit = true;
                        if (bolDescInfonacot) {
                            log.debug("La bandera de bolDescInfonacot esta activada= " + bolDescInfonacot);
                            formula.setEmpleadoActual(empleado);
                            formula.setNominaActual(nominaMaster);

                            intTD_ID = HashMapDeducciones.get("11").getIntIdTipo();
                            intIdDedu = HashMapDeducciones.get("11").getIntIdTipo();

                            concepto = formula.CalculaImporte(intIdDedu, false, empleado, null);
                            if (concepto != null) {
                                log.debug("El importe calculado es..." + concepto.getStrCve() + " | " + concepto.getDblImporte());
                                rhh_nominas_deta detalle = new rhh_nominas_deta();
                                detalle.setFieldInt("TP_ID", 0);
                                detalle.setFieldInt("PERC_ID", 0);
                                detalle.setFieldInt("TD_ID", intTD_ID);
                                detalle.setFieldInt("DEDU_ID", intIdDedu);
                                detalle.setFieldInt("NOMD_CANTIDAD", 1);
                                detalle.setFieldDouble("NOMD_UNITARIO", concepto.getDblImporte());
                                detalle.setFieldDouble("NOMD_GRAVADO", 0);
                                detalle.setFieldDouble("NOMD_IMPORTE_GRAVADO", 0);
                                detalle.setFieldDouble("NOMD_IMPORTE_EXENTO", concepto.getDblImporte());
                                nominaEmpleado.getLstConceptos().add(detalle);
                            } else {
                                log.debug("No se encontro el concepto...");
                            }
                        }
                    }
                }
                rs.close();
            } catch (SQLException ex) {
                log.error(ex.getMessage());
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Otros descuentos">
            String strOtrosDesc = "select RHOD_ESTA_ACTIVO,RHOD_ES_ANTICIPO,RHOD_IMPORTE_DESC,RHOD_IMPORTE_TOTAL from "
                    + "rhh_otros_descuentos where EMP_NUM= " + empleado.getFieldInt("EMP_NUM") + " ;";

            try {
                ResultSet rs;
                rs = oConn.runQuery(strOtrosDesc);
                while (rs.next()) {
                    if (rs.getInt("RHOD_ES_ANTICIPO") != 1) {
                        log.debug("La bandera de Otros descuentos esta activada= " + rs.getInt("RHOD_ES_ANTICIPO"));
                        if (rs.getInt("RHOD_ESTA_ACTIVO") == 1) {
                            log.debug("La bandera de RHOD_ESTA_ACTIVO esta activada= " + rs.getInt("RHOD_ESTA_ACTIVO"));
                            formula.setEmpleadoActual(empleado);
                            formula.setNominaActual(nominaMaster);

                            intTD_ID = HashMapDeducciones.get("22").getIntIdTipo();
                            intIdDedu = HashMapDeducciones.get("22").getIntIdTipo();

                            log.debug("El importe calculado es..." + rs.getDouble("RHOD_IMPORTE_DESC"));
                            rhh_nominas_deta detalle = new rhh_nominas_deta();
                            detalle.setFieldInt("TP_ID", 0);
                            detalle.setFieldInt("PERC_ID", 0);
                            detalle.setFieldInt("TD_ID", intTD_ID);
                            detalle.setFieldInt("DEDU_ID", intIdDedu);
                            detalle.setFieldInt("NOMD_CANTIDAD", 1);
                            detalle.setFieldDouble("NOMD_UNITARIO", rs.getDouble("RHOD_IMPORTE_DESC"));
                            detalle.setFieldDouble("NOMD_GRAVADO", 0);
                            detalle.setFieldDouble("NOMD_IMPORTE_GRAVADO", 0);
                            detalle.setFieldDouble("NOMD_IMPORTE_EXENTO", concepto.getDblImporte());
                            nominaEmpleado.getLstConceptos().add(detalle);

                        }
                    }
                }
                rs.close();
            } catch (SQLException ex) {
                log.error(ex.getMessage());
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Otros Anticipos">
            String strOtrosAnt = "select RHOD_ESTA_ACTIVO,RHOD_ES_ANTICIPO,RHOD_IMPORTE_TOTAL from "
                    + "rhh_otros_descuentos where EMP_NUM= " + empleado.getFieldInt("EMP_NUM") + " ;";

            try {
                ResultSet rs;
                rs = oConn.runQuery(strOtrosAnt);
                while (rs.next()) {
                    if (rs.getInt("RHOD_ES_ANTICIPO") == 1) {
                        log.debug("La bandera de Otros Anticipos esta activada= " + rs.getInt("RHOD_ES_ANTICIPO"));
                        if (rs.getInt("RHOD_ESTA_ACTIVO") == 1) {
                            log.debug("La bandera de RHOD_ESTA_ACTIVO esta activada= " + rs.getInt("RHOD_ESTA_ACTIVO"));

                            formula.setEmpleadoActual(empleado);
                            formula.setNominaActual(nominaMaster);

                            intTD_ID = HashMapDeducciones.get("23").getIntIdTipo();
                            intIdDedu = HashMapDeducciones.get("23").getIntIdTipo();

                            log.debug("El importe calculado es..." + rs.getDouble("RHOD_IMPORTE_TOTAL"));
                            rhh_nominas_deta detalle = new rhh_nominas_deta();
                            detalle.setFieldInt("TP_ID", 0);
                            detalle.setFieldInt("PERC_ID", 0);
                            detalle.setFieldInt("TD_ID", intTD_ID);
                            detalle.setFieldInt("DEDU_ID", intIdDedu);
                            detalle.setFieldInt("NOMD_CANTIDAD", 1);
                            detalle.setFieldDouble("NOMD_UNITARIO", rs.getDouble("RHOD_IMPORTE_TOTAL"));
                            detalle.setFieldDouble("NOMD_GRAVADO", 0);
                            nominaEmpleado.getLstConceptos().add(detalle);

                        }
                    }
                }
                rs.close();
            } catch (SQLException ex) {
                log.error(ex.getMessage());
            }

            // </editor-fold>         
            // <editor-fold defaultstate="collapsed" desc="Descuento seguro social">
            int intSeguroSocial = empleado.getFieldInt("RC_ID");
            if (intSeguroSocial != 1) {
                log.debug("seguro social");
                formula.setEmpleadoActual(empleado);
                formula.setNominaActual(nominaMaster);

                intTD_ID = HashMapDeducciones.get("1").getIntIdTipo();
                intIdDedu = HashMapDeducciones.get("1").getIntIdTipo();

                concepto = formula.CalculaImporte(intIdDedu, false, empleado, null);
                if (concepto != null) {
                    log.debug("El importe calculado es..." + concepto.getStrCve() + " " + concepto.getDblImporte());
                    rhh_nominas_deta detalle = new rhh_nominas_deta();
                    detalle.setFieldInt("TP_ID", 0);
                    detalle.setFieldInt("PERC_ID", 0);
                    detalle.setFieldInt("TD_ID", intTD_ID);
                    detalle.setFieldInt("DEDU_ID", intIdDedu);
                    detalle.setFieldInt("NOMD_CANTIDAD", 1);
                    detalle.setFieldDouble("NOMD_UNITARIO", concepto.getDblImporte());
                    detalle.setFieldDouble("NOMD_GRAVADO", 0);
                    nominaEmpleado.getLstConceptos().add(detalle);
                } else {
                    log.debug("No se encontro el concepto...");
                }
            }
            // </editor-fold> 

            // <editor-fold defaultstate="collapsed" desc="Descuento ISR">
            log.debug("Calculo ISR");
            formula.setEmpleadoActual(empleado);
            formula.setNominaActual(nominaMaster);

            if (isWWPG()) {
                log.debug("Calculo de la nomina WWPG.");
            CalculaISR calIsr = new CalculaISR(oConn, varSesiones);
            double dblIsr = 0.0;
            //Se paga el subsidio al empleado en la segunda nómina
            if (nominaMaster.getFieldInt("RHN_PAGA_SUBSIDIO") == 1) {
                    double intDiasPasados = getDiasLaborados(nominaMaster.getFieldString("RHN_FECHA_INICIAL"), empleado.getFieldInt("EMP_NUM"));
                    double intTotalDTrabajados = dblNumeroDiasTrabajados + intDiasPasados;
                    calIsr.setIsWWPG(isWWPG());
                    calIsr.setDblDiasMes(intTotalDTrabajados);
                intTP_ID = HashMapPercepciones.get("17").getIntIdTipo();
                intIdPerc = HashMapPercepciones.get("17").getIntIdPercDedu();
                concepto = formula.CalculaImporte(intIdDedu, false, empleado, null);
                if (concepto != null) {
                        //Si sale positivo en la primer quincena 
                    double dblSubsidio = getSubsidioAlEmpleo(nominaMaster.getFieldString("RHN_FECHA_INICIAL"), empleado.getFieldInt("EMP_NUM"));
                    double dblgravadosAnteriores = getGravadosNominaAnterior(nominaMaster.getFieldString("RHN_FECHA_INICIAL"), empleado.getFieldInt("EMP_NUM"));
                    double dblgravadosActuales = getSumGravados(nominaEmpleado);
                    double totalGravados = dblgravadosAnteriores + dblgravadosActuales;
                        String strPeriodicidad = nominaMaster.getFieldString("RHN_TIPO_SUBSIDIO");
                    calIsr.setDblGravados(totalGravados);
                        dblIsr = calIsr.calculaImporte(strPeriodicidad);
                        if (dblIsr < 0.0) {
                            dblIsr = dblIsr * (-1);
                            log.debug("El importe calculado es..." + concepto.getStrCve() + " " + concepto.getDblImporte());
                            rhh_nominas_deta detalle = new rhh_nominas_deta();
                            detalle.setFieldInt("TP_ID", intTP_ID);
                            detalle.setFieldInt("PERC_ID", intIdPerc);
                            detalle.setFieldInt("TD_ID", 0);
                            detalle.setFieldInt("DEDU_ID", 0);
                            detalle.setFieldInt("NOMD_CANTIDAD", 1);
                            detalle.setFieldDouble("NOMD_UNITARIO", dblIsr);
                            detalle.setFieldDouble("NOMD_GRAVADO", 0);
                            nominaEmpleado.getLstConceptos().add(detalle);
                        } else {
                            intTD_ID = HashMapDeducciones.get("2").getIntIdTipo();
                            intIdDedu = HashMapDeducciones.get("2").getIntIdTipo();
                            if (dblSubsidio > 0.0) {
                                dblIsr = dblIsr - dblSubsidio;
                            } else {
                                dblIsr = dblIsr;
                        }

                        log.debug("El importe calculado es..." + concepto.getStrCve() + " " + concepto.getDblImporte());
                        rhh_nominas_deta detalle = new rhh_nominas_deta();
                        detalle.setFieldInt("TP_ID", 0);
                        detalle.setFieldInt("PERC_ID", 0);
                        detalle.setFieldInt("TD_ID", intTD_ID);
                        detalle.setFieldInt("DEDU_ID", intIdDedu);
                        detalle.setFieldInt("NOMD_CANTIDAD", 1);
                            detalle.setFieldDouble("NOMD_UNITARIO", dblIsr);
                        detalle.setFieldDouble("NOMD_GRAVADO", 0);
                        nominaEmpleado.getLstConceptos().add(detalle);
                    }

                    //Termina concepto subsidio
                } else {
                    log.debug("No se encontro el concepto...");
                }
            } else {
                //Primer nomina se paga normal sin subsidio
                intTD_ID = HashMapDeducciones.get("2").getIntIdTipo();
                intIdDedu = HashMapDeducciones.get("2").getIntIdTipo();
                calIsr.setDblGravados(getSumGravados(nominaEmpleado));
                dblIsr = calIsr.CalculaISR(empleado, nominaMaster);
                String strIsr = Double.toString(dblIsr);
                if (strIsr != null) {
                    log.debug("El importe calculado es... " + dblIsr);
                    rhh_nominas_deta detalle = new rhh_nominas_deta();
                    detalle.setFieldInt("TP_ID", 0);
                    detalle.setFieldInt("PERC_ID", 0);
                    detalle.setFieldInt("TD_ID", intTD_ID);
                    detalle.setFieldInt("DEDU_ID", intIdDedu);
                    detalle.setFieldInt("NOMD_CANTIDAD", 1);
                    if (dblIsr < 0) {
                        detalle.setFieldDouble("NOMD_UNITARIO", 0);
                        detalle.setFieldDouble("NOMD_IMPORTE_ISR", dblIsr);
                    } else {
                        detalle.setFieldDouble("NOMD_UNITARIO", dblIsr);
                        detalle.setFieldDouble("NOMD_IMPORTE_ISR", dblIsr);
                    }
                    detalle.setFieldDouble("NOMD_GRAVADO", 0);
                    nominaEmpleado.getLstConceptos().add(detalle);
                } else {
                    log.debug("No se encontro el concepto...");
                }
            }
                //Fin nomina WWPG
            } else {
                log.debug("Calculo de la nomina Estandar.");
                CalculaISR calIsr = new CalculaISR(oConn, varSesiones);
                double dblIsr = 0.0;

                double dblSueldoNomina = dblNumeroDiasTrabajados * empleado.getFieldDouble("EMP_SALARIO_DIARIO");
                calIsr.setDblGravados(dblSueldoNomina);
                dblIsr = calIsr.CalculaISR(empleado, nominaMaster);
                if (dblIsr < 0.0) {               
                    intTP_ID = HashMapPercepciones.get("17").getIntIdTipo();
                    intIdPerc = HashMapPercepciones.get("17").getIntIdPercDedu();
                    dblIsr = dblIsr * (-1);
                    log.debug("El importe calculado es..." + concepto.getStrCve() + " " + concepto.getDblImporte());
                    rhh_nominas_deta detalle = new rhh_nominas_deta();
                    detalle.setFieldInt("TP_ID", intTP_ID);
                    detalle.setFieldInt("PERC_ID", intIdPerc);
                    detalle.setFieldInt("TD_ID", 0);
                    detalle.setFieldInt("DEDU_ID", 0);
                    detalle.setFieldInt("NOMD_CANTIDAD", 1);
                    detalle.setFieldDouble("NOMD_UNITARIO", dblIsr);
                    detalle.setFieldDouble("NOMD_GRAVADO", 0);
                    nominaEmpleado.getLstConceptos().add(detalle);
                } else {
                    intTD_ID = HashMapDeducciones.get("2").getIntIdTipo();
                    intIdDedu = HashMapDeducciones.get("2").getIntIdTipo();
                    log.debug("El importe calculado es..." + concepto.getStrCve() + " " + concepto.getDblImporte());
                    rhh_nominas_deta detalle = new rhh_nominas_deta();
                    detalle.setFieldInt("TP_ID", 0);
                    detalle.setFieldInt("PERC_ID", 0);
                    detalle.setFieldInt("TD_ID", intTD_ID);
                    detalle.setFieldInt("DEDU_ID", intIdDedu);
                    detalle.setFieldInt("NOMD_CANTIDAD", 1);
                    detalle.setFieldDouble("NOMD_UNITARIO", dblIsr);
                    detalle.setFieldDouble("NOMD_GRAVADO", 0);
                    nominaEmpleado.getLstConceptos().add(detalle);
                }

            }

            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Pension Alimenticia">
            int intPenAlimenticia = empleado.getFieldInt("EMP_PENSION_ALIMENTICIA");
            if (intPenAlimenticia == 1) {
                log.debug("Pension Alimenticia");
                formula.setEmpleadoActual(empleado);
                formula.setNominaActual(nominaMaster);
                formula.setNomina(nominaEmpleado);

                intTD_ID = HashMapDeducciones.get("7").getIntIdTipo();
                intIdDedu = HashMapDeducciones.get("7").getIntIdTipo();

                concepto = formula.CalculaImporte(intIdDedu, false, empleado, null);

                if (concepto != null) {
                    log.debug("El importe calculado es..." + concepto.getStrCve() + " " + concepto.getDblImporte());
                    rhh_nominas_deta detalle = new rhh_nominas_deta();
                    detalle.setFieldInt("TP_ID", 0);
                    detalle.setFieldInt("PERC_ID", 0);
                    detalle.setFieldInt("TD_ID", intTD_ID);
                    detalle.setFieldInt("DEDU_ID", intIdDedu);
                    detalle.setFieldInt("NOMD_CANTIDAD", 1);
                    detalle.setFieldDouble("NOMD_UNITARIO", concepto.getDblImporte());
                    detalle.setFieldDouble("NOMD_GRAVADO", 0);
                    nominaEmpleado.getLstConceptos().add(detalle);
                } else {
                    log.debug("No se encontro el concepto...");
                }
            }
            // </editor-fold> 

            // </editor-fold> 
            // <editor-fold defaultstate="collapsed" desc="Guardamos la nómina cálculada...">
            //         nominaEmpleado.getDocument().setFieldDouble("NOM_ISR_RETENIDO", cllISR_Retenido.getNumericCellValue());
//         nominaEmpleado.getDocument().setFieldDouble("NOM_TASA_ISR", cllTasaISR.getNumericCellValue());
//         nominaEmpleado.getDocument().setFieldDouble("NOM_RETISR", cllISR_Retenido.getNumericCellValue());
            //         nominaEmpleado.getDocument().setFieldString("NOM_REGIMENFISCAL", strRegimenFiscal);
            nominaEmpleado.doTrx();
            String strRes = nominaEmpleado.getStrResultLast();
            if (!strRes.equals("OK")) {
                this.strResultLast = strRes;
            }

            // </editor-fold>         
        }
         // </editor-fold>

        //this.lstConceptos.iterator();
    }

   /**
    *Calcula las excepciones.
    * @param empleado
    * @param nominaMaster
    * @param nominaEmpleado
    * @param intTP_ID
    * @param intIdPerc
    * @throws SQLException
    */
   public void calculaExcepciones(rhh_empleados empleado, Rhh_Nominas_Master nominaMaster, Nominas nominaEmpleado, int intTP_ID, int intIdPerc) throws SQLException {

        int id_Depto = empleado.getFieldInt("DP_ID");
        int id_Nomina = nominaMaster.getFieldInt("RHN_ID");
        double dblEmpSalDiario = empleado.getFieldDouble("EMP_SALARIO_INTEGRADO");
        int intDiasNomina = nominaMaster.getFieldInt("RHN_NUMERO_DIAS");
        double dblSueldoNomina = dblEmpSalDiario * intDiasNomina;
        String strEcepciones = "select * from rhh_excepciones where RHN_ID = '" + id_Nomina + "' and DP_ID = '" + id_Depto + "';";
        double dblImporte = 0.0;
        int intFijo = 0;
        double dblTmpPorc = 0.0;
        ResultSet rsExc = oConn.runQuery(strEcepciones);
        while (rsExc.next()) {
            intFijo = rsExc.getInt("RHE_FIJO");
            if (intFijo == 1) {
                dblImporte = rsExc.getDouble("RHE_IMPORTE");
            } else {
                dblTmpPorc = rsExc.getDouble("RHE_PORCENTAJE");
                dblImporte = dblSueldoNomina * (dblTmpPorc / 100);
            }
        }
        rsExc.close();
        log.debug("Sueldo DIARIO: " + dblEmpSalDiario);
        log.debug("DÍAS NOMINA: " + intDiasNomina);
        log.debug("IMPORTE EXCEPCION: " + dblImporte);
        log.debug("PORCENTAJE EXCEPCION: " + dblTmpPorc);

        rhh_nominas_deta detalle = new rhh_nominas_deta();
        detalle.setFieldInt("TP_ID", intTP_ID);
        detalle.setFieldInt("PERC_ID", intIdPerc);
        detalle.setFieldInt("TD_ID", 0);
        detalle.setFieldInt("DEDU_ID", 0);
        detalle.setFieldInt("NOMD_CANTIDAD", 1);
        detalle.setFieldDouble("NOMD_UNITARIO", dblImporte);
        detalle.setFieldDouble("NOMD_GRAVADO", 0);
        nominaEmpleado.getLstConceptos().add(detalle);
    }

    public void LimpiezaFolios(int intEmpresa, int intSucursal) {
        //Regeneramos el folio
        int intFolio = 1;
        String strSql = "select convert(max(nom_folio) ,unsigned integer) as num_folio from rhh_nominas where EMP_ID = " + intEmpresa;
        if (bolEsFolioSucursal) {
            strSql = "select convert(max(nom_folio) ,unsigned integer) as num_folio from rhh_nominas where EMP_ID = " + intEmpresa
                    + " AND SC_ID = " + intSucursal;
        }
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                intFolio = rs.getInt("num_folio");
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        //Especificamos el nombre de la tabla
        String strNomTablaFolios = "vta_folionomina" + intEmpresa;
        if (bolEsFolioSucursal) {
            strNomTablaFolios = "vta_folionomina" + intEmpresa + "_" + intSucursal;
        }
        //Borramos los items
        strSql = "delete from " + strNomTablaFolios + " where FOL_ID > " + intFolio;
        oConn.runQueryLMD(strSql);
        //Alteramos el autoincremental
        if (intFolio == 0) {
            strSql = "alter table " + strNomTablaFolios + " AUTO_INCREMENT = 1";
        } else {
            intFolio++;
            strSql = "alter table " + strNomTablaFolios + " AUTO_INCREMENT = " + intFolio;
        }
        oConn.runQueryLMD(strSql);
    }

    /**
     * Genera el nombre del xml
     */
    private String getNombreFileXml(ERP_MapeoFormato mapeo, int intTransaccion, String strNombreReceptor, String strFechaCFDI, String strFolioFiscalUUID) {
        String strNomFileXml = null;
        String strPatronNomXml = mapeo.getStrNomXML("NOMINA");
        log.debug("strNomFileXml:" + strNomFileXml);
        log.debug("strPatronNomXml:" + strPatronNomXml);
        log.debug("intTransaccion:" + intTransaccion);
        log.debug("strNombreReceptor:" + strNombreReceptor);
        strNomFileXml = strPatronNomXml.replace("%Transaccion%", intTransaccion + "").replace("", "").replace("%nombre_receptor%", strNombreReceptor).replace("%fecha%", strFechaCFDI).replace("%UUID%", strFolioFiscalUUID).replace(" ", "_");
        return strNomFileXml;
    }

    public double acumulaImporteNominas(String strfecha, int numPercDedu, int GravaExento, boolean isPercepcion, int idEmpleado) {
        String fecha = strfecha.substring(0, 6);
        String strIdNomina = "";
        double dblSumaImporte = 0.0;
        String strSql = "select NOM_ID from rhh_nominas where  left(NOM_FECHA,6) = '" + fecha + "' and EMP_NUM = " + idEmpleado + " ;";
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strIdNomina = rs.getString("NOM_ID");
                /*Si es true es Percepcion si no es Dedudccion*/
                if (isPercepcion) {
                    String strSql1 = "select NOMD_UNITARIO from rhh_nominas_deta where NOM_ID = " + strIdNomina + " and PERC_ID = " + numPercDedu;
                    try {
                        ResultSet rs1 = oConn.runQuery(strSql1, true);
                        while (rs1.next()) {
                            dblSumaImporte = dblSumaImporte + rs1.getDouble("NOMD_UNITARIO");
                        }
                        rs1.close();
                    } catch (SQLException ex) {
                        this.strResultLast = "ERROR:" + ex.getMessage();
                        ex.fillInStackTrace();
                        log.error("!!!!!!...." + ex.getMessage());
                    }
                } else {
                    String strSql1 = "select NOMD_UNITARIO from rhh_nominas_deta where NOM_ID = " + strIdNomina + " and DEDU_ID = " + numPercDedu;
                    try {
                        ResultSet rs1 = oConn.runQuery(strSql1, true);
                        while (rs1.next()) {
                            dblSumaImporte = dblSumaImporte + rs1.getDouble("NOMD_UNITARIO");
                        }
                        rs1.close();
                    } catch (SQLException ex) {
                        this.strResultLast = "ERROR:" + ex.getMessage();
                        ex.fillInStackTrace();
                        log.error("!!!!!!...." + ex.getMessage());
                    }
                }
            }
            rs.close();
        } catch (SQLException ex) {
            this.strResultLast = "ERROR:" + ex.getMessage();
            ex.fillInStackTrace();
            log.error("!!!!!!...." + ex.getMessage());
        }
        return dblSumaImporte;
    }

    public double getPercepcionActual(int idPercDedu) {
        double importeActualCalculado = 0;
        Iterator<TableMaster> ite = this.lstConceptos.iterator();
        while (ite.hasNext()) {
            rhh_nominas_deta detalle = (rhh_nominas_deta) ite.next();
            if (detalle.getFieldInt("PERC_ID") == idPercDedu) {
                importeActualCalculado = importeActualCalculado + detalle.getFieldDouble("NOMD_UNITARIO");
            }
        }
        return importeActualCalculado;
    }

    public double getDeduccionActual(int idDedu) {
        double importeActualCalculado = 0;
        Iterator<TableMaster> ite = this.lstConceptos.iterator();
        while (ite.hasNext()) {
            rhh_nominas_deta detalle = (rhh_nominas_deta) ite.next();
            if (detalle.getFieldInt("DEDU_ID") == idDedu) {
                importeActualCalculado = importeActualCalculado + detalle.getFieldDouble("NOMD_UNITARIO");
            }
        }
        return importeActualCalculado;
    }

    public double getGravados(int idPercDedu) {
        double importeActualCalculado = 0;
        Iterator<TableMaster> ite = this.lstConceptos.iterator();
        while (ite.hasNext()) {
            rhh_nominas_deta detalle = (rhh_nominas_deta) ite.next();
            if (detalle.getFieldInt("PERC_ID") == idPercDedu) {
                importeActualCalculado = importeActualCalculado + detalle.getFieldDouble("NOMD_IMPORTE_GRAVADO");
            }
        }
        return importeActualCalculado;
    }

    public double getSumGravados(Nominas nominaEmpleado) {
        double dblSalario = nominaEmpleado.getGravados(1);
        double dblPrimaDom = nominaEmpleado.getGravados(20);
        double dblHrExtra = nominaEmpleado.getGravados(19);
        double dblBonoAsistencia = nominaEmpleado.getGravados(31);
        double dblBonoPuntual = nominaEmpleado.getGravados(32);
        double dblBonoCumplimiento = nominaEmpleado.getGravados(35);
        double SumGravados = dblSalario + dblPrimaDom + dblHrExtra + dblBonoCumplimiento + dblBonoPuntual + dblBonoAsistencia;
        return SumGravados;
    }//Fin getSuma Gravados

    public double getSubsidioAlEmpleo(String strfecha, int intIdEmpleado) {
        String fecha = strfecha.substring(0, 6);
        int intIdNomina = 0;
        double dblImporte = 0.0;
        ResultSet rs;
        String strSql = "select NOM_ID from rhh_nominas where left(NOM_FECHA_INICIAL_PAGO,6) = '" + fecha + "' and EMP_NUM = " + intIdEmpleado;
        try {
            rs = oConn.runQuery(strSql, false);
            while (rs.next()) {
                intIdNomina = rs.getInt("NOM_ID");
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }

        strSql = "select * from rhh_nominas_deta where NOM_ID = " + intIdNomina + " and DEDU_ID = 2";
        try {
            rs = oConn.runQuery(strSql, false);
            while (rs.next()) {
                dblImporte = rs.getDouble("NOMD_IMPORTE_ISR");
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        return dblImporte;
    }//Fin get subsidio al empleo

    public double getGravadosNominaAnterior(String strfecha, int intIdEmpleado) {
        String fecha = strfecha.substring(0, 6);
        int intIdNomina = 0;
        double dblImporte = 0.0;
        ResultSet rs;
        String strSql = "select NOM_ID from rhh_nominas where left(NOM_FECHA_INICIAL_PAGO,6) = '" + fecha + "' and EMP_NUM = " + intIdEmpleado;
        try {
            rs = oConn.runQuery(strSql, false);
            while (rs.next()) {
                intIdNomina = rs.getInt("NOM_ID");
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }

        strSql = "select * from rhh_nominas_deta where NOM_ID = " + intIdNomina;
        try {
            rs = oConn.runQuery(strSql, false);
            while (rs.next()) {
                dblImporte = dblImporte + rs.getDouble("NOMD_IMPORTE_GRAVADO");
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        return dblImporte;
    }
    
    public double getSueldoAnterior(String strfecha, int intIdEmpleado) {
        String fecha = strfecha.substring(0, 6);
        int intIdNomina = 0;
        double dblImporte = 0.0;
        ResultSet rs;
        String strSql = "select NOM_ID from rhh_nominas where left(NOM_FECHA_INICIAL_PAGO,6) = '" + fecha + "' and EMP_NUM = " + intIdEmpleado;
        try {
            rs = oConn.runQuery(strSql, false);
            while (rs.next()) {
                intIdNomina = rs.getInt("NOM_ID");
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }

        strSql = "select NOMD_UNITARIO from rhh_nominas_deta where NOM_ID = " + intIdNomina + " and PERC_ID = 1";
        try {
            rs = oConn.runQuery(strSql, false);
            while (rs.next()) {
                dblImporte = rs.getDouble("NOMD_UNITARIO");
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        return dblImporte;
    }

    public double getDiasLaborados(String strfecha, int intIdEmpleado) {
        String fecha = strfecha.substring(0, 6);
        double dblDiasLaborados = 0;
        ResultSet rs;
        String strSql = "select NOM_ID,NOM_NUM_DIAS_PAGADOS from rhh_nominas where left(NOM_FECHA_INICIAL_PAGO,6) = '" + fecha + "' and EMP_NUM = " + intIdEmpleado;
        try {
            rs = oConn.runQuery(strSql, false);
            while (rs.next()) {
                dblDiasLaborados = rs.getDouble("NOM_NUM_DIAS_PAGADOS");
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        return dblDiasLaborados;
    }

    public boolean isWWPG() {
        boolean isWWPG = false;
        int isrWWPG = 0;
        String strQuery = "select EMP_ISR_WWPG from vta_empresas where EMP_ID = " + varSesiones.getIntIdEmpresa();
        ResultSet rs;
        try {
            rs = oConn.runQuery(strQuery, false);
            while (rs.next()) {
                isrWWPG = rs.getInt("EMP_ISR_WWPG");
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        if (isrWWPG == 1) {
            isWWPG = true;
        } else {
            isWWPG = false;
        }
        return isWWPG;
    }

    public int getDiferenciaNomina(String strFecIni, String strFecFinal) {
        int intDiferencia = 0;
        String strQuery = "select DATEDIFF('" + strFecIni + "','" + strFecFinal + "') as diferencia";
        ResultSet rs;
        try {
            rs = oConn.runQuery(strQuery, false);
            while (rs.next()) {
                intDiferencia = rs.getInt("diferencia");
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        intDiferencia = intDiferencia + 1;
        return intDiferencia;
    }//Fin get Diferencia días de nomina

   // </editor-fold>
}
