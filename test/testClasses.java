
import Core.FirmasElectronicas.Addendas.DateFormatterISO8601;
import Core.FirmasElectronicas.SATXml;
import Core.FirmasElectronicas.SATXml3_0;
import Core.FirmasElectronicas.Utils.UtilCert;
import ERP.Importa_Empleados;
import ERP.Importa_Nominas;
import ERP.Importar;
import com.mx.siweb.erp.especiales.cofide.SincronizarPaginaWeb;
import com.siweb.test.TestPagos;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.CIP_GeneraCodigo;
import comSIWeb.Utilerias.Mail;
import comSIWeb.Utilerias.Util_CopiaTablas;
import java.security.cert.CertificateEncodingException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author zeus
 */
public class testClasses {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //boolean bolRes = ConvertUTFtoBOM.convertUTF8toBOM("C:/Zeus", "XmlSAT61 .xml");
        //System.out.println("bolRes:" + bolRes);
/*Abrimos conexion*/
        String[] ConexionURL = new String[4];
        ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_cofide";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/erpweb_ventas";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_grupomak";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_firm";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_perezcolmenares";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_prosefi";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_innte";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_tasarel";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/cp_mmorelos";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/cp_iemssa";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_8_22";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_gonzaloroque";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_pasteleriasangel";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/gpo_sarquis";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_casasnovasroberto";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_escuadron201";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_small";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_comptelg";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_mmorelos";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_xocobenefit";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_siweb";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_azteca_personas_fisicas";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_aztecabienes";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/ct_intertrade";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_ccsa";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_rcti";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_truchas";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_grupomak";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_casa_josefa";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_gascare";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_alitex";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_seducao";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_borrame";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_wppg";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_we_now";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_klensy";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_moriah_vasti";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_park_inc";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_capitalia";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_lydia_dainow";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/hd_rcti";
//      ConexionURL[0] = "jdbc:mysql://localhost:3306/ct_control_accesos";
        ConexionURL[1] = "root";
        ConexionURL[2] = "";
        ConexionURL[3] = "mysql";

        Conexion oConn;
        try {
            oConn = new Conexion(ConexionURL, null);
            oConn.open();
            oConn.setBolMostrarQuerys(true);

            VariableSession sesion = new VariableSession(null);
            Util_CopiaTablas copia = new Util_CopiaTablas();
            ArrayList<String> lstNo = new ArrayList<String>();
//         testFireBird fireBird = new testFireBird();

         //2015-11-27T08:34:52-0600
            //-06:00
//Acceso al pac        
//         TimbradoFacturaSegundos timbrado = new TimbradoFacturaSegundos("/Users/ZeusGalindo/Documents/Zeus/SAT/");
//         String strResultado = timbrado.OtorgaNuevoAcceso("","");
//         System.out.println("Resultado de la alta...." + strResultado);
            // <editor-fold defaultstate="collapsed" desc="Simulacion de red...">
//         SimulaRed simula = new SimulaRed();
//         simula.GeneraRed(1, 1,6, 9331, "", oConn);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Cálculo comisiones Capitalia">
//         com.mx.siweb.mlm.compensacion.capitalia.CalculoComision comis
//                 = new com.mx.siweb.mlm.compensacion.capitalia.CalculoComision(oConn, 1, false);
//         comis.doFase1();
//         System.out.println("Resultado de fases " + comis.getStrResultLast());
//         if (comis.getStrResultLast().equals("OK")) {
//            comis.doFase2();
//            if (comis.getStrResultLast().equals("OK")) {
//               comis.doFase3();
//               if (comis.getStrResultLast().equals("OK")) {
//                  comis.doFase4();
//                  if (comis.getStrResultLast().equals("OK")) {
//                     System.out.println("Concluyeron las comisiones....");
//                  } else {
//                     System.out.println("ERROR AL CALCULAR COMISIONES(3) : " + comis.getStrResultLast());
//                  }
//               } else {
//                  System.out.println("ERROR AL CALCULAR COMISIONES(3) : " + comis.getStrResultLast());
//               }
//            } else {
//               System.out.println("ERROR AL CALCULAR COMISIONES (2): " + comis.getStrResultLast());
//            }
//         } else {
//            System.out.println("ERROR AL CALCULAR COMISIONES(1): " + comis.getStrResultLast());
//         }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Instalador sistemas">
//         InstaladorSIWEB install = new InstaladorSIWEB(oConn, sesion);
//         install.setStrNombreSistema("Prueba1");
//         install.setStrNombreBaseDatos("vta_prueba1");
//         install.setStrTipoSistema("MLM");
//         install.setStrPathCarpetaXMLOrigen("/Users/ZeusGalindo/Documents/Zeus/SAT/cerSelloBase/");
//         install.setStrNombreCarpetaXML("PRUEBA1");
//         install.setStrPathRepositorioXML("/Users/ZeusGalindo/Desktop/tmp_Zeus/tmpXML/");
//         install.setStrPathBase("/Users/ZeusGalindo/Desktop/tmp_Zeus/tmpInstaller/");
//         install.setStrPathOrigen("/Users/ZeusGalindo/Documents/Fuentes/Netbeans/ERPWEB_Ventas/build/web/");
//         install.setStrPathFileBaseContext("/Users/ZeusGalindo/Desktop/tmp_Zeus/tmpInstaller/");
//         install.setStrPathFileContextOrigen("/Users/ZeusGalindo/Documents/Fuentes/Netbeans/ERPWEB_Ventas/web/META-INF/context.xml");
//         install.setStrPuertoBd("3309");
//         install.setBolCopiaSitio(false);
//         install.setBolGeneraTablas(false);
//         install.doTrx();
//         System.out.println("Resultado creacion: " + install.getStrResultLast());
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Cálculo de posición WenOW">
//         CobrosMasivosXLS masivos = new CobrosMasivosXLS();
//         masivos.setStrPathExcel("/Users/ZeusGalindo/Documents/Zeus/dbs/Distribuidores.xls");
//         masivos.setVarSesiones(sesion);
//         masivos.setoConn(oConn);
//         masivos.setIntUplineInicial(10001);
//         masivos.setIntUplineTemporal(3);
//         String strRespuesta = masivos.processXLS();
//         System.out.println("strRespuesta:" + strRespuesta);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="FACTURACION masiva Klensy">
//         int intCliente =0;
//         int intEMP_ID =0;
//         int intSC_ID =0;
//         int intPedidosPagados = 0;
//         double dblClientesSaldoMenor =0;
//         String strFechaFactura = "20150716";
//         String strFechaIni = "";
//         String strFechaFin = "";
//         String strFiltro = "";
//         //Filtro EMPRESA
//         if(intEMP_ID > 0 ){
//            strFiltro = " AND EMP_ID = " + intEMP_ID;
//         }
//         //Filtro BODEGA
//         if(intSC_ID > 0 ){
//            strFiltro = " AND SC_ID = " + intSC_ID;
//         }
//         //Filtro cliente
//         if(intCliente > 0 ){
//            strFiltro = " AND CT_ID = " + intCliente;
//         }
//         //Filtro Fecha inicial y final
//         if(!strFechaIni.isEmpty() && !strFechaFin.isEmpty()){
//            strFiltro = " AND PD_FECHA >= '" + strFechaIni + "' AND PD_FECHA <= '" + strFechaFin + "' ";
//         }
//         FacturaMasivaPedidos facturaPedidos = new FacturaMasivaPedidos( oConn,  sesion,  null);
//         facturaPedidos.setStrFiltro(strFiltro);
//         facturaPedidos.setIntPedidosPagados(intPedidosPagados);
//         facturaPedidos.setDblClientesSaldoMenor(dblClientesSaldoMenor);
//         facturaPedidos.setStrFechaFactura(strFechaFactura);
//         facturaPedidos.setStrTipoVta(Ticket.TICKET);
//         facturaPedidos.Init();
//         facturaPedidos.doTrx();
//         String strResp = facturaPedidos.getStrResultLast();
//         System.out.println("strRes " + strResp);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Modulo para compropago">
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Cálculo comisiones Klensy">
//         GeneraMailReferenciado(oConn, sesion, 10002);
//         com.mx.siweb.mlm.compensacion.klensy.CalculoComision comis
//                 = new com.mx.siweb.mlm.compensacion.klensy.CalculoComision(oConn, 3, false);
//         comis.doFase1();
//         System.out.println("Resultado de fases " + comis.getStrResultLast());
//         if (comis.getStrResultLast().equals("OK")) {
//            comis.doFase2();
//            if (comis.getStrResultLast().equals("OK")) {
//               comis.doFase3();
//               if (comis.getStrResultLast().equals("OK")) {
//                  comis.doFase4();
//                  if (comis.getStrResultLast().equals("OK")) {
//                     System.out.println("Concluyeron las comisiones....");
//                  } else {
//                     System.out.println("ERROR AL CALCULAR COMISIONES(3) : " + comis.getStrResultLast());
//                  }
//               } else {
//                  System.out.println("ERROR AL CALCULAR COMISIONES(3) : " + comis.getStrResultLast());
//               }
//            } else {
//               System.out.println("ERROR AL CALCULAR COMISIONES (2): " + comis.getStrResultLast());
//            }
//         } else {
//            System.out.println("ERROR AL CALCULAR COMISIONES(1): " + comis.getStrResultLast());
//         }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Cálculo comisiones Wenow">
//         com.mx.siweb.mlm.compensacion.wenow.CalculoComision comis
//                 = new com.mx.siweb.mlm.compensacion.wenow.CalculoComision(oConn, 3, false);
//         comis.doFase1();
//         System.out.println("Resultado de fases " + comis.getStrResultLast());
//         if (comis.getStrResultLast().equals("OK")) {
//            comis.doFase2();
//            if (comis.getStrResultLast().equals("OK")) {
//               comis.doFase3();
//               if (comis.getStrResultLast().equals("OK")) {
//                  comis.doFase4();
//                  if (comis.getStrResultLast().equals("OK")) {
//                     System.out.println("Concluyeron las comisiones....");
//                  } else {
//                     System.out.println("ERROR AL CALCULAR COMISIONES(3) : " + comis.getStrResultLast());
//                  }
//               } else {
//                  System.out.println("ERROR AL CALCULAR COMISIONES(3) : " + comis.getStrResultLast());
//               }
//            } else {
//               System.out.println("ERROR AL CALCULAR COMISIONES (2): " + comis.getStrResultLast());
//            }
//         } else {
//            System.out.println("ERROR AL CALCULAR COMISIONES(1): " + comis.getStrResultLast());
//         }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Cálculo comisiones MoriahVasti">
//         com.mx.siweb.mlm.compensacion.moriahvasti.CalculoComision comis
//                 = new com.mx.siweb.mlm.compensacion.moriahvasti.CalculoComision(oConn, 1, false);
//         comis.doFase1();
//         System.out.println("Resultado de fases " + comis.getStrResultLast());
//         if (comis.getStrResultLast().equals("OK")) {
//            comis.doFase2();
//            if (comis.getStrResultLast().equals("OK")) {
//               comis.doFase3();
//               if (comis.getStrResultLast().equals("OK")) {
//                  comis.doFase4();
//                  if (comis.getStrResultLast().equals("OK")) {
//                     System.out.println("Concluyeron las comisiones....");
//                  } else {
//                     System.out.println("ERROR AL CALCULAR COMISIONES(3) : " + comis.getStrResultLast());
//                  }
//               } else {
//                  System.out.println("ERROR AL CALCULAR COMISIONES(3) : " + comis.getStrResultLast());
//               }
//            } else {
//               System.out.println("ERROR AL CALCULAR COMISIONES (2): " + comis.getStrResultLast());
//            }
//         } else {
//            System.out.println("ERROR AL CALCULAR COMISIONES(1): " + comis.getStrResultLast());
//         }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Cálculo de nóminas">
// // Calculo de isr         
//            rhh_empleados empleado = new rhh_empleados();
//            empleado.ObtenDatos(1, oConn);
//            System.out.println(" " + empleado.getFieldString("EMP_NOMBRE"));
//         
//            Rhh_Nominas_Master nominaActual = new Rhh_Nominas_Master();
//            nominaActual.ObtenDatos(1, oConn);
//            System.out.println(" " + nominaActual.getFieldString("RHN_DESCRIPCION"));
//            
//            CalculaISR calc = new CalculaISR(oConn);
//            double dblISRMonto = calc.CalculaISR(empleado,nominaActual);
//            System.out.println("Calculo ISR:    " + dblISRMonto);
//         Rhh_Nominas_Master nominaMaster = new Rhh_Nominas_Master();
//         rhh_empleados empleado = new rhh_empleados();
//         empleado.ObtenDatos(1, oConn);
//         nominaMaster.ObtenDatos(1, oConn);
//                 
//         NominasFormulas formula = new NominasFormulas(oConn);
//         formula.Init();
//         
//         //Cálculamos el concepto....
//         formula.setEmpleadoActual(empleado);
//         formula.setNominaActual(nominaMaster);
//         ItemConcepto concepto = formula.CalculaImporte(1, true, empleado, null);
//         if (concepto != null) {
//            System.out.println("El importe calculado es..." + concepto.getStrCve() + " " + concepto.getDblImporte());
//         } else {
//            System.out.println("No se encontro el concepto...");
//         }
//            Nominas nomina = new Nominas(oConn, sesion);
//            nomina.CalculaNomina(1);
//         System.out.println("Resultado: " + nomina.getStrResultLast());
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Retenciones">
//         String strPassB64 = "9qT9yUhip1dsAi0FD6nYlw==";
//         Opalina opa = new Opalina();
//         String strMyPassSecret = opa.DesEncripta(strPassB64, "dWAM1YhbGAeu7CTULai4eQ==");
//
//         String strRex = SATRetenciones.TimbradoMasivo("0000006", "0000006",
//           2,  sesion,  strMyPassSecret,
//            "/Users/ZeusGalindo/Documents/Zeus/SAT/GrupoMak/",  "/Users/ZeusGalindo/Documents/Zeus/SAT/GrupoMak/Cer_Sello/", 
//           "/Users/ZeusGalindo/Documents/Zeus/SAT/GrupoMak/Cer_Sello/",  oConn);
//         String strRex = SATRetenciones.CancelaComprobanteRetencion(2, strMyPassSecret, 2, "/Users/ZeusGalindo/Documents/Zeus/SAT/GrupoMak/Cer_Sello/", "/Users/ZeusGalindo/Documents/Zeus/SAT/GrupoMak/", oConn);
//         SATRetenciones retenciones = new SATRetenciones(1, "/Users/ZeusGalindo/Documents/Zeus/SAT/GrupoMak",
//                  "/Users/ZeusGalindo/Documents/Zeus/SAT/GrupoMak/Cer_Sello/", strMyPassSecret,
//                 sesion, oConn);
//        retenciones.setStrPathConfigPAC("/Users/ZeusGalindo/Documents/Zeus/SAT/GrupoMak/Cer_Sello/");
//         String strResXML = retenciones.GeneraXml();
//         System.out.println("strRex:" + strRex);
            // </editor-fold>
//         DashboardGenerator dash = new DashboardGenerator(oConn);
//         dash.Init("TOP_VTA");
//         //Obtenemos los parametros
//         Iterator<TableMaster> itParams = dash.getLstParams().iterator();
//         while (itParams.hasNext()) {
//            TableMaster param = (Dashboard_Params) itParams.next();
//            if (param.getFieldString("DBP_NAME").equals("EMP_ID")) {
//               param.setFieldString("DBP_VALOR_DEFA", "2");
//            }
//            if (param.getFieldString("DBP_NAME").equals("MON_ID")) {
//               param.setFieldString("DBP_VALOR_DEFA", "1");
//            }
//            if (param.getFieldString("DBP_NAME").equals("CONVERTIDO")) {
//               param.setFieldString("DBP_VALOR_DEFA", "1");
//            }
//            if (param.getFieldString("DBP_NAME").equals("SC_ID")) {
//               param.setFieldString("DBP_VALOR_DEFA", "2");
//            }
//            if (param.getFieldString("DBP_NAME").equals("FECHA_INI")) {
//               param.setFieldString("DBP_VALOR_DEFA", "20150201");
//            }
//            if (param.getFieldString("DBP_NAME").equals("FECHA_FIN")) {
//               param.setFieldString("DBP_VALOR_DEFA", "20150201");
//            }
//         }
//         String strJson = dash.DoDashboard();
//         System.out.println("strJson:" + strJson);
//         
//         //Envio del mail 
//         Mail mail = new Mail();
//         String strLstMail
//                 = "aleph_79@hotmail.com"; //Intentamos mandar el mail
//         mail.setBolDepuracion(false);
//         mail.getTemplate("MSG_ING", oConn);
//         mail.getMensaje();
//         String strSqlEmp = "SELECT * FROM vta_cliente"
//                 + " where CT_ID=" + 2 + "";
//         try {
//            ResultSet rs
//                    = oConn.runQuery(strSqlEmp);
//            mail.setReplaceContent(rs);
//            rs.close();
//         } catch (SQLException ex) { //this.strResultLast = "ERROR:" +
//            ex.getMessage();
//
//         }
//         mail.setDestino(strLstMail);
//         boolean bol = mail.sendMail();
////         FacturacionMasiva mas = new FacturacionMasiva( oConn,  sesion,  null);
//////         Parametros del contexto
////         String strPassB64 = "9qT9yUhip1dsAi0FD6nYlw==";
////         Opalina opa = new Opalina();
////         String strMyPassSecret = opa.DesEncripta(strPassB64, "dWAM1YhbGAeu7CTULai4eQ==");
////         mas.setStrMyPassSecret(strMyPassSecret);
////         mas.setStrPATHBase("/Users/ZeusGalindo/Documents/Fuentes/Netbeans/ERPWEB_Ventas/build/web/");
////         mas.setStrPathPrivateKeys("/Users/ZeusGalindo/Documents/Zeus/SAT/SIWEB/Cer_Sello/");
////         mas.setStrPathXML("/Users/ZeusGalindo/Documents/Zeus/SAT/SIWEB/");
////         mas.setStrPathFonts("");
////         
////         mas.setStrAnio("2015");
////         mas.setIntMes(3);
////         mas.setIntSC_ID(1);
////         mas.setIntEMP_ID(1);
////         mas.doTrx();
//         mas.doInvoices();
//         System.out.println(" " + mas.toString());
         /*
             //Abre los certificados
             String filename = "/Users/ZeusGalindo/Documents/Zeus/SAT/GrupoMak/Cer_Sello/cert_file2.cer";
             FileInputStream fis = new FileInputStream(filename);
             BufferedInputStream bis = new BufferedInputStream(fis);

             CertificateFactory cf = CertificateFactory.getInstance("X.509");
             System.out.println("Vamos a leer el sello");
             while (bis.available() > 0) {
             Certificate cert = cf.generateCertificate(bis);
             X509Certificate certRSA = (X509Certificate) cert;
             System.out.println(cert.toString());

             try {
             certRSA.checkValidity();
             System.out.println(" " + certRSA.getSerialNumber());
             System.out.println("certificado valido.... ");
               
             javax.security.auth.x500.X500Principal subject = certRSA.getSubjectX500Principal();
             System.out.println("subject: " + subject.getName());
               
               
             javax.security.auth.x500.X500Principal issuer = certRSA.getIssuerX500Principal();
             System.out.println("issuer: " + issuer.getName());
               
               
             } catch (CertificateExpiredException ex) {
             System.out.println("Expirado... " + "");

             } catch (CertificateNotYetValidException ex) {
             System.out.println("No Valido " + "");
             }
             }
             System.out.println("Sello leído");
             */
//         //Path de los archivos
//         String strPath = "/Users/ZeusGalindo/Documents/Zeus/dbs/RespaldoXml20141017/";
//         String strRFCEmisor = "ALI790531PK6";
//         
//         RenombrarComprobantes renom = new RenombrarComprobantes();
//         renom.RenombrarXmlCFDI(oConn, strPath, strRFCEmisor);
         /*
             String strSql = "select * from vta_facturas where FAC_ES_CFD = 0";
             ResultSet rs = oConn.runQuery(strSql, true);
             while (rs.next()) {
             int intId = rs.getInt("FAC_ID");
             String strUUIDFind = rs.getString("FAC_FOLIO");
             String strFolioC = rs.getString("FAC_FOLIO_C");
             String strNomFileSupuesto = "XmlSAT" + intId + " .xml";
             double dblTotal = rs.getDouble("FAC_TOTAL");
             String strRFCReceptor = rs.getString("FAC_RFC");

             //Buscamos en todos los xml...
             System.out.println("Buscamos el archivo " + intId);
             String files;
             File folder = new File(strPath);
             File[] listOfFiles = folder.listFiles();
             boolean bolFoundXml = false;
             for (int i = 0; i < listOfFiles.length; i++) {
             if (listOfFiles[i].isFile()) {
             files = listOfFiles[i].getName();
             if (files.toUpperCase().startsWith("XMLSAT") && files.toUpperCase().endsWith(".XML")) {
             MovProveedor mov = new MovProveedor(oConn, null, null);
             mov.CargaDatosXML(listOfFiles[i].getAbsolutePath());
             if (mov.getStrUUID().equals(strUUIDFind)) {
             if (strNomFileSupuesto.equals(files)) {
             System.out.println("\u001B[32m Encontramos el archivo xml..." + files);
             } else {
             System.out.println("\u001B[31m Renombramos el archivo xml..." + files);
             }
             //Copiamos el archivo
             String strPathNuevo = strPath + "Modificados/" +  strNomFileSupuesto;
             File fileDestino = new File(strPathNuevo);
             listOfFiles[i].renameTo(fileDestino);
             //Generamos el QR de nuevo
             GeneracionQR.generaQR(strRFCEmisor, strRFCReceptor, new DecimalFormat("0000000000.000000").format(dblTotal), strUUIDFind, strPath + "Modificados/", intId, 1);
             //Marcamos que ya lo encontramos y cerramos el ciclo...
             bolFoundXml = true;
             break;

             }
             }
             }
             }
             //Si no encontro lo indicamos
             if (!bolFoundXml) {
             System.out.println("\u001B[36m No encontro el archivo..." + strNomFileSupuesto);
             }
             }
             rs.close();
             * */
            // Coordenadas google
         /*GoogleUtils goo = new GoogleUtils();

             try {
             int intContador = 0;
             int intContadorError = 0;
             String strSql = "select * from cp_sitio where ST_ID > 232 and ST_GOG_COORDENADAS NOT LIKE '%,%' \n"
             + "AND ST_GOG_COORDENADAS LIKE 'N%'  ";
             ResultSet rs = oConn.runQuery(strSql, true);
             while (rs.next()) {
             String strCoordenada = rs.getString("ST_GOG_COORDENADAS");
             intContador++;
             System.out.println("strCoordenada:" + strCoordenada);
             //N 30°53'15.84"  W 108°11'28.73"
             int intIdx1 = strCoordenada.indexOf("W");
             //Latitud
             String strLat = strCoordenada.substring(0, intIdx1);
             strLat = strLat.replace("N", "");
             int intIdx2 = strLat.indexOf("°");
             String strGrado = strLat.substring(0, intIdx2).replace(" ", "").replace(" ", "");
             int intIdx3 = strLat.indexOf("'");
             String strMinuto = strLat.substring(intIdx2 + 1, intIdx3).replace(" ", "");
             String strSegundo = strLat.substring(intIdx3).replace("'", "").replace(" ", "").replace(" ", "");
             //Longitud
             String strLong = strCoordenada.substring(intIdx1, strCoordenada.length());
             strLong = strLong.replace("W", "");
             intIdx2 = strLong.indexOf("°");
             String strGrado2 = strLong.substring(0, intIdx2).replace(" ", "");
             intIdx3 = strLong.indexOf("'");
             String strMinuto2 = strLong.substring(intIdx2 + 1, intIdx3).replace(" ", "");
             String strSegundo2 = strLong.substring(intIdx3).replace("'", "").replace(" ", "");
             //Longitud
             //         strLong = strLong.replace(" ", ",");
             //               System.out.println(intIdx1);

             System.out.println("Latitud:" + strLat + " grado:" + strGrado + " Minuto:" + strMinuto + " Segundo:" + strSegundo);
             System.out.println("Longitud" + strLong + " grado:" + strGrado2 + " Minuto:" + strMinuto2 + " Segundo:" + strSegundo2);
             try {
             int intGrado1 = Integer.valueOf(strGrado);
             int intMinuto1 = Integer.valueOf(strMinuto);
             double dblSegundo1 = Double.valueOf(strSegundo);
             int intGrado2 = Integer.valueOf(strGrado2);
             int intMinuto2 = Integer.valueOf(strMinuto2);
             double dblSegundo2 = Double.valueOf(strSegundo2);
             goo.setLagrados(intGrado1);
             goo.setLaminutos(intMinuto1);
             goo.setLasegundos(dblSegundo1);
             goo.setLaposicion("N");

             goo.setLogrados(intGrado2);
             goo.setLominutos(intMinuto2);
             goo.setLosegundos(dblSegundo2);
             goo.setLoposicion("W");

             goo.calculo();
             String strPosGoogle = goo.getdLatitud() + "," + goo.getdLongitu();
             System.out.println("gOOGLE pOS " + strPosGoogle);
                  
             String strUpdate = "update cp_sitio set ST_GOG_GOOGLE = '" + strPosGoogle + "' where st_id = " + rs.getInt("ST_ID");
             oConn.runQueryLMD(strUpdate);
             } catch (NumberFormatException ex) {
             System.out.println("********Fallo en la conversion****** " + ex.getMessage());
             intContadorError++;
             } catch (Exception ex) {

             }
             System.out.println("");
             }
             rs.close();

             System.out.println("Total vistos " + intContador);
             System.out.println("Total con error " + intContadorError);

             } catch (Exception ex) {
             System.out.println(" " + ex.getMessage());
             }*/
//int intNodo
//        = Redes.calculaUpline(98, 5, "", false, oConn, true);
//         System.out.println("intNodo:" + intNodo);
//         ComproPago cpago = new ComproPago();
//         ComproPagoRest resp = cpago.AgregarCargo("sk_test_5214b107977b3bd51",
//                 "100", "SAMSUNG GOLD CURL", "SMGCURL1", 
//                 "https://test.amazon.com/5f4373", "Alejandra Leyva", "aleph_79@hotmail.com", "OXXO");
//         System.out.println("resp.getPayment_id():" + resp.getPayment_id());
//         System.out.println("resp.getPayment_status():" + resp.getPayment_status());
//         System.out.println("resp.getShort_payment_id():" + resp.getShort_payment_id());
//         System.out.println("resp.getDescription():" + resp.getDescription());
//         System.out.println("resp.getStep_1():" + resp.getStep_1());
//         System.out.println("resp.getStep_2():" + resp.getStep_2());
//         System.out.println("resp.getStep_3():" + resp.getStep_3());
            //Recuperamos datos del ticket
         /*

             //Recuperamos paths
             String strIdTicket = "2";
             String strNomFormato = "TICKET1";
             String strNomId = "TKT_ID";
             String strNomIdParam = "tkt_id";

             int intIdTicket = 0;

             if (strIdTicket.isEmpty()) {
             strIdTicket = "0";
             }
             try {
             intIdTicket = Integer.valueOf(strIdTicket);
             } catch (NumberFormatException ex) {
             System.out.println("ex ticket print:" + ex.getMessage());
             }
             //Recuperamos datos del ticket
             TableMaster ticket = null;
             if (strNomFormato.equals("TICKET1")) {
             ticket = new vta_tickets();
             }
             ticket.ObtenDatos(intIdTicket, oConn);

             //Abrimos el formato correspondiente
             //copiar
             String strEncabezado = "";
             String strCuerpo = "";
             String strCuerpoAll = "";
             String strPie = ""; 
             //Datos de la empresa
             String strFor = "select * from formatos_tickets where FT_ID = '1'";
             ResultSet rsI = oConn.runQuery(strFor, true);
             while (rsI.next()) {
             strEncabezado = rsI.getString("FT_TXT_ENCABEZADO");
             strCuerpo = rsI.getString("FT_TXT_CUERPO");
             strPie = rsI.getString("FT_TXT_PIE");

             }
             rsI.close();
             //Datos de la empresa
             String strEmp = "select * from vta_empresas where EMP_ID =  " + ticket.getFieldInt("EMP_ID");
             ResultSet rs = oConn.runQuery(strEmp, true);
             while (rs.next()) {
             strEncabezado = strEncabezado.replace("{EMP_RAZONSOCIAL}", rs.getString("EMP_RAZONSOCIAL"));
             strEncabezado = strEncabezado.replace("{EMP_CALLE}", rs.getString("EMP_CALLE"));
             strEncabezado = strEncabezado.replace("{EMP_COLONIA}", rs.getString("EMP_COLONIA"));
             strEncabezado = strEncabezado.replace("{EMP_MUNICIPIO}", rs.getString("EMP_MUNICIPIO"));
             strEncabezado = strEncabezado.replace("{EMP_ESTADO}", rs.getString("EMP_ESTADO"));
             strEncabezado = strEncabezado.replace("{EMP_TELEFONO1}", rs.getString("EMP_TELEFONO1"));

             }
             rs.close();
             //Usuario que realizo la venta
             strEmp = "select u.nombre_usuario from usuarios u where  u.id_usuarios =  " + ticket.getFieldInt("TKT_US_ALTA");
             rs = oConn.runQuery(strEmp, true);
             while (rs.next()) {
             strEncabezado = strEncabezado.replace("{TKT_VENDEDOR}", rs.getString("nombre_usuario"));

             }
             rs.close();
             //Datos del ticket 
             strEncabezado = strEncabezado.replace("{TKT_FOLIO}", ticket.getFieldString("TKT_FOLIO"));
             strEncabezado = strEncabezado.replace("{TKT_NOTAS}", ticket.getFieldString("TKT_NOTAS"));
             strPie = strPie.replace("{TKT_TOTAL}", "$" + NumberString.FormatearDecimal(ticket.getFieldDouble("TKT_TOTAL"), 2));
             StringofNumber enLetras = new StringofNumber();
             strPie = strPie.replace("{TKT_TOTAL_EN_LETRA}", enLetras.getStringOfNumber(ticket.getFieldDouble("TKT_TOTAL")));
         
             String sqlCuerpo ="SELECT TKTD_CANTIDAD, TKTD_DESCRIPCION,TKTD_PRECIO,TKTD_IMPORTEREAL FROM vta_ticketsdeta WHERE TKT_ID= " + ticket.getFieldInt("TKT_ID") ;
             ResultSet rCuerpo = oConn.runQuery(sqlCuerpo, true);
             double cantidad =0;
             while(rCuerpo.next()){
             String strCuerpoTemp = new String(strCuerpo);
             double importeReal = rCuerpo.getDouble("TKTD_IMPORTEREAL");
             double precioUnit = rCuerpo.getDouble("TKTD_PRECIO");
             double cant= rCuerpo.getDouble("TKTD_CANTIDAD");
             strCuerpoTemp = strCuerpoTemp.replace("{TKT_CANTIDAD} ", NumberString.FormatearDecimal(cant, 2));
             strCuerpoTemp = strCuerpoTemp.replace("{TKT_DESCRIPCION} ", rCuerpo.getString("TKTD_DESCRIPCION").substring(0, 10));
             strCuerpoTemp = strCuerpoTemp.replace("{TKT_PRECIO_UNIT} ",NumberString.FormatearDecimal(precioUnit, 2) );
             strCuerpoTemp = strCuerpoTemp.replace("{TKT_TOTAL}",NumberString.FormatearDecimal(importeReal, 2));
             cantidad += rCuerpo.getDouble("TKTD_CANTIDAD");
             strCuerpoAll += strCuerpoTemp;
             }
         
             strPie = strPie.replace("{TKT_CANTIDAD}", ""+cantidad);
         
             String strCliente = "SELECT MCD_FORMAPAGO,MCD_IMPORTE,MCD_CAMBIO FROM vta_mov_cte_deta WHERE CT_ID="+ ticket.getFieldInt("CT_ID");
             ResultSet rCliente = oConn.runQuery(strCliente , true);
             while (rCliente.next()) {
             strPie = strPie.replace("{TKT_FORMA_PAGO}", rCliente.getString("MCD_FORMAPAGO"));
            
             if (rCliente.getString("MCD_FORMAPAGO").equals("EFECTIVO")) {
             double importe = rCliente.getDouble("MCD_IMPORTE");
             double cambio = rCliente.getDouble("MCD_CAMBIO");
             strPie = strPie.replace("{TKT_EFECTIVO}", NumberString.FormatearDecimal(importe, 2));
             strPie = strPie.replace("{TKT_CAMBIO}", NumberString.FormatearDecimal(cambio, 2));
             } else {
             strPie = strPie.replace("{TKT_EFECTIVO}", "0.0");
             strPie = strPie.replace("{TKT_CAMBIO}", "0.0");
             }
             if (rCliente.getString("MCD_FORMAPAGO").equals("TARJETA")) {
             double importe = rCliente.getDouble("MCD_IMPORTE");
             strPie = strPie.replace("{TKT_TARJETA}", NumberString.FormatearDecimal(importe, 2));
             strPie = strPie.replace("{TKT_CAMBIO}", "0.0");
             }
             else{
             strPie = strPie.replace("{TKT_TARJETA}", "0.0");
             strPie = strPie.replace("{TKT_CAMBIO}", "0.0");
             }
             if (rCliente.getString("MCD_FORMAPAGO").equals("VALES")) {
             double importe = rCliente.getDouble("MCD_IMPORTE");
             strPie = strPie.replace("{TKT_VALES}", NumberString.FormatearDecimal(importe, 2));
             strPie = strPie.replace("{TKT_CAMBIO}", "0.0");
             }
             else{
             strPie = strPie.replace("{TKT_VALES}", "0.0");
             strPie = strPie.replace("{TKT_CAMBIO}", "0.0");  
             }
             }
         
             String strRes = (char) 27 + (char) 7 + (char) 11 + (char) 55 + (char) 7 + strEncabezado + strCuerpoAll + strPie;
             //copiar

             System.out.println("strRes:" + strRes);
             System.out.println("::::.");
             System.out.println("::::.");*/

            /* Impresion con pdf
             * 
             * vta_tickets ticket = new vta_tickets();
             ticket.ObtenDatos(1, oConn);

             //Clase que genera el formato
             ProcesoMaster process = new ProcesoMaster(oConn, sesion, null);
             process.setStrPATHBase("/Users/ZeusGalindo/Documents/Fuentes/Netbeans/ERPWEB_Ventas/build/web/");

             String[] lstParamsName = {"tkt_id"};
             String[] lstParamsValue = {ticket.getFieldString("TKT_ID")};

             String strNomFile = process.doGeneraFormatoJasper(0, "TICKET1", "PDF", ticket, lstParamsName, lstParamsValue, "/Users/ZeusGalindo/Desktop/ticketTmp" + ticket.getFieldString("TKT_ID") + ".pdf");
             System.out.println("strNomFile:" + strNomFile);

             try {
             File file = new File(strNomFile);
             //Abrimos el archivo y lo comprimimos
             byte[] fileByte = org.apache.commons.io.FileUtils.readFileToByteArray(file);
             String strCompBase64 = compressString(fileByte);
             if (strCompBase64 != null) {
             System.out.println("strCompBase64:" + strCompBase64);
             }
             } catch (IOException ex) {
             System.out.println(ex.getMessage());
             }
             */
            /*
             //Pruebas opalinA
             Opalina op = new Opalina();
             try {
             String usuario = op.Encripta("zeus", "ceZH/37VIaXBywBsF1ek2A==");
             //String password = op.DesEncripta("solsticio", "ceZH/37VIaXBywBsF1ek2A==");
             System.out.println("usuario:" + usuario);

             byte[] b64Enc = Base64.encodeBase64("zeus".getBytes());
             byte[] raw = Base64.decodeBase64("ceZH/37VIaXBywBsF1ek2A==".getBytes());
             System.out.println("length " + raw.length);

             System.out.println("Comienza h...");
             for (int h = 0; h < raw.length; h++) {
             System.out.println(h + "|" + raw[h] + "|");
             }
             System.out.println("Comienza h...");

             String strBase64 = new String(b64Enc);
             System.out.println("strBase64:" + strBase64);
             } catch (NoSuchAlgorithmException ex) {
             Logger.getLogger(testClasses.class.getName()).log(Level.SEVERE, null, ex);
             } catch (NoSuchPaddingException ex) {
             Logger.getLogger(testClasses.class.getName()).log(Level.SEVERE, null, ex);
             } catch (InvalidKeyException ex) {
             Logger.getLogger(testClasses.class.getName()).log(Level.SEVERE, null, ex);
             } catch (IllegalBlockSizeException ex) {
             Logger.getLogger(testClasses.class.getName()).log(Level.SEVERE, null, ex);
             } catch (BadPaddingException ex) {
             Logger.getLogger(testClasses.class.getName()).log(Level.SEVERE, null, ex);
             }
             */
//         String strEtiqueta = "hola esto \n sdsdsd,[PR_DESCRIPCION_LEFT30]\nssk44,3443kl[PR_DESCRIPCION_RIGHT12]kl34,3438983";
//         String strDesc = "ESTA ES UNA DESCRIPCION DE UN PRODUCTO DE VARIOS CARTACTEFRES SON MUCHOS";
//         if (strEtiqueta.contains("[PR_DESCRIPCION_LEFT")) {
//            int intpos = strEtiqueta.indexOf("[PR_DESCRIPCION_LEFT");
//            int intpos2 = strEtiqueta.indexOf("]", intpos);
//            String strAncho = strEtiqueta.substring(intpos, intpos2).replace("[PR_DESCRIPCION_LEFT", "");
//            try {
//               int intAncho = Integer.valueOf(strAncho);
//               System.out.println("intpos:" + intpos);
//               System.out.println("intpos2:" + intpos2);
//               System.out.println("sub:" + intAncho);
//               System.out.println("strDesc:" + strDesc.substring(0, intAncho));
//               strEtiqueta = strEtiqueta.replace(strEtiqueta.substring(intpos, intpos2) + "]", strDesc.substring(0, intAncho));
//               
//            } catch (NumberFormatException ex) {
//            }
//
//         }
//         if (strEtiqueta.contains("[PR_DESCRIPCION_RIGHT")) {
//            int intpos = strEtiqueta.indexOf("[PR_DESCRIPCION_RIGHT");
//            int intpos2 = strEtiqueta.indexOf("]", intpos);
//            String strAncho = strEtiqueta.substring(intpos, intpos2).replace("[PR_DESCRIPCION_RIGHT", "");
//            try {
//               int intAncho = Integer.valueOf(strAncho);
//               System.out.println("intpos:" + intpos);
//               System.out.println("intpos2:" + intpos2);
//               System.out.println("sub:" + intAncho);
//               System.out.println("strDesc:" + strDesc.substring(strDesc.length() - intAncho, strDesc.length()));
//               strEtiqueta = strEtiqueta.replace(strEtiqueta.substring(intpos, intpos2) + "]", strDesc.substring(strDesc.length() - intAncho, strDesc.length()));
//            } catch (NumberFormatException ex) {
//            }
//
//         }
//         System.out.println("strEtiqueta:" + strEtiqueta);
//         //Llamar sp
//         CallableStatement cStmt = oConn.getConexion().prepareCall("{call sp_getProdControlInventario1(?,?,?,?)}");
//         cStmt.setInt(1, 1);
//         boolean hadResults = cStmt.execute();
//         while (hadResults) {
//            ResultSet rs = cStmt.getResultSet();
//
//            // process result set
//            while(rs.next()){
//               System.out.println("Id " + rs.getInt("PR_ID"));
//            }
//            rs.close();
//
//            hadResults = cStmt.getMoreResults();
//         }
         /*
             String targetFileName = "/Users/ZeusGalindo/Desktop/mireport.pdf";
         
             ArrayList<EdoMovCte> lstData = new ArrayList<EdoMovCte>();
             EdoMovCte edo= new EdoMovCte();
             edo.setTipoDocumento("Factura");
             edo.setFolio("1462");
             edo.setFecha("13/12/2013");
             lstData.add(edo);
             edo= new EdoMovCte();
             edo.setTipoDocumento("Factura");
             edo.setFolio("1463");
             edo.setFecha("15/12/2013");
             lstData.add(edo);

             //Parametros
             Map parametersMap = new HashMap();
             parametersMap.put("NumCliente", "12");
             parametersMap.put("NombreCliente", "Demo de reporte sin conexion");
             parametersMap.put("SaldoInicial", 1300.56);
             //Obtenemos el reporte
             InputStream reportStream = new FileInputStream("/Users/ZeusGalindo/Documents/Fuentes/Netbeans/ERPWEB_Ventas/web/WEB-INF/jreports/rep_edo_cta_cte_print.jrxml");
             // Bing the datasource with the collection
             JRDataSource datasource = new JRBeanCollectionDataSource(lstData, true);
             // Compile and print the jasper report
             JasperReport report = JasperCompileManager.compileReport(reportStream);
             //JasperPrint print = JasperFillManager.fillReport(report, parameters, datasource);
             JasperPrint print = JasperFillManager.fillReport(report, parametersMap, datasource);
         
             //jasperPrint = JasperFillManager.fillReport(jasperReport, parametersMap, oConn.getConexion());
             JasperExportManager.exportReportToPdfFile(print, targetFileName);
             */
//         FacturaContratos fContrato = new FacturaContratos(oConn, sesion);
//         fContrato.setIntEMP_ID(7);
//         fContrato.setIntCTE_ID(0);
//         fContrato.setIntSC_ID(7);
//         fContrato.setBolAplica(true);
//         fContrato.setStrFechaFactura("20140513");
//         fContrato.setStrFechaInicio("20140501");
//         fContrato.setStrFechaFinal("20140531");
//         nomina.setStrPATHXml("/Users/ZeusGalindo/Documents/Zeus/SAT/MMORELOS/");
//         nomina.setStrPATHBase("/Users/ZeusGalindo/Documents/Fuentes/Netbeans/ERPWEB_Ventas/build/web/");
//         String strPassB64 = "9qT9yUhip1dsAi0FD6nYlw==";
//         Opalina opa = new Opalina();
//         String strMyPassSecret = opa.DesEncripta(strPassB64, "dWAM1YhbGAeu7CTULai4eQ==");
//         fContrato.setStrMyPassSecret(strMyPassSecret);
//         fContrato.setStrPATHBase("/Users/ZeusGalindo/Documents/Fuentes/Netbeans/ERPWEB_Ventas/build/web/");
//         fContrato.setStrPathPrivateKeys("/Users/ZeusGalindo/Documents/Zeus/SAT/GrupoMak/Cer_Sello/");
//         fContrato.setStrPathXML("/Users/ZeusGalindo/Documents/Zeus/SAT/GrupoMak/");
//         fContrato.setStrPathFonts("");
//         fContrato.doTrx();
//         System.out.println("Msg:" + fContrato.getStrResultLast());
            // <editor-fold defaultstate="collapsed" desc="Reparar lotes">
//         ProductosLoteRepair lotesRepa = new ProductosLoteRepair();
//         lotesRepa.doAjuste(oConn);
            // </editor-fold>
//         double dblZero = 0.0;
//         BigDecimal BGTasa1 = new BigDecimal(dblZero); 
//         String numero = NumberString.FormatearDecimal(BGTasa1.doubleValue() , 2).replace(",", "");
//         
//         System.out.println("numero "+ numero);
            // <editor-fold defaultstate="collapsed" desc="Copiar productos de una sucursal a otra">
//         Producto producto = new Producto();
//         String strSqlXC = "select SC_ID from vta_sucursal where SC_ID <> 1";
//         ResultSet rsXC = oConn.runQuery(strSqlXC, true);
//         while (rsXC.next()) {
//            String strResp = producto.CopiaProductosSucursal(oConn, 1, rsXC.getInt("SC_ID"));
//            System.out.println("resp " + strResp);
//         }
//         rsXC.close();
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Generar folios">
//         GeneraFolios folio = new GeneraFolios();
//         folio.setoConn(oConn);
//         folio.UpdateFolio(2);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Importar empleados y nominas">
            Importa_Empleados importaEmpleados = new Importa_Empleados();
//         importaEmpleados.CargaEmpleados("/Users/ZeusGalindo/Desktop/tmp_Zeus/Empleados(1)(2).xls", oConn, sesion);
//         System.out.println(" " + importaEmpleados.getStrResultLast());
////         sesion
            Importa_Nominas nominas = new Importa_Nominas();
            nominas.setBolLimpiarInfo(true);
//         sesion.setIntIdEmpresa(2);
//         nominas.Carga_Nominas("20140101","Regimen de personas morales","/Users/ZeusGalindo/Desktop/tmp_Zeus/Formato CFDI Nominas 2014 Sueldos.xls", oConn, sesion);
//         System.out.println(" " + nominas.getStrResultLast());
            // </editor-fold>

         // <editor-fold defaultstate="collapsed" desc="GENERACION DE REGISTROS DE NOMINA">
//         Nominas nomina = new Nominas(oConn, sesion, null);
//         nomina.Init();
         /*
             //Llenamos datos del encabezado
             nomina.setIntEMP_ID(1);
             nomina.getDocument().setFieldInt("EMP_ID", 1);
             nomina.getDocument().setFieldInt("SC_ID", 1);
             nomina.getDocument().setFieldString("NOM_FECHA","20140105");
             nomina.getDocument().setFieldString("NOM_METODODEPAGO","TRASFERENCIA");
             nomina.getDocument().setFieldInt("EMP_NUM",1);
             nomina.getDocument().setFieldDouble("NOM_PERCEPCIONES", 1000);
             nomina.getDocument().setFieldDouble("NOM_DEDUCCIONES", 100);
             nomina.getDocument().setFieldDouble("NOM_ISR_RETENIDO", 100);
             nomina.getDocument().setFieldDouble("NOM_PERCEPCION_TOTAL", 800);
             nomina.getDocument().setFieldDouble("NOM_TASA_ISR", 10);
             nomina.getDocument().setFieldInt("NOM_MONEDA", 1);
             nomina.getDocument().setFieldDouble("NOM_DESCUENTO", 100);
             nomina.getDocument().setFieldDouble("NOM_RETISR", 100);
             nomina.getDocument().setFieldString("NOM_FECHA_INICIAL_PAGO", "20131221");
             nomina.getDocument().setFieldString("NOM_FECHA_FINAL_PAGO", "20140105");
             nomina.getDocument().setFieldString("NOM_REGIMENFISCAL", "REGIMEN GENERAL DE LEY PERSONAS MORALES");
             nomina.getDocument().setFieldInt("NOM_NUM_DIAS_PAGADOS", 15);
         
             //Partidas de la nomina
             rhh_nominas_deta detalle = new rhh_nominas_deta();
             detalle.setFieldInt("TP_ID", 1);
             detalle.setFieldInt("PERC_ID", 1);
             detalle.setFieldInt("TD_ID", 0);
             detalle.setFieldInt("DEDU_ID", 0);
             detalle.setFieldInt("NOMD_CANTIDAD", 1);
             detalle.setFieldDouble("NOMD_UNITARIO", 1000);
             detalle.setFieldDouble("NOMD_GRAVADO", 1);
             nomina.getLstConceptos().add(detalle);
         
             detalle = new rhh_nominas_deta();
             detalle.setFieldInt("TP_ID", 0);
             detalle.setFieldInt("PERC_ID", 0);
             detalle.setFieldInt("TD_ID", 1);
             detalle.setFieldInt("DEDU_ID", 1);
             detalle.setFieldInt("NOMD_CANTIDAD", 1);
             detalle.setFieldDouble("NOMD_UNITARIO", 100);
             detalle.setFieldDouble("NOMD_GRAVADO", 1);
             nomina.getLstConceptos().add(detalle);
         
             nomina.doTrx();
             String strResult = nomina.getStrResultLast();
         

         
             System.out.println("strResult:" + strResult);
             * */
            //Envio de mail de cancelacion
//         nomina.getDocument().setFieldInt("NOM_ID", 209);
//         nomina.Init();
////         //nomina.envioMailCancel();
//         nomina.setStrPATHXml("/Users/ZeusGalindo/Documents/Zeus/SAT/MMORELOS/");
//         nomina.setStrPATHBase("/Users/ZeusGalindo/Documents/Fuentes/Netbeans/ERPWEB_Ventas/build/web/");
//         nomina.doEnvioMasivo(1, "0000001", "0000002", "zgalindo@siwebmx.com", 1);
//         String[] lstParamsName = {"nom_folio1","nom_folio2","emp_id"};
//         String[] lstParamsValue = {"0000001","0000001","1"};
//         nomina.doGeneraFormatoJasper(0, "NOM_CFDI", "PDF",nomina.getDocument(), lstParamsName, lstParamsValue);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Timbrado de nominas">
//         nomina.setBolSendMailMasivo(false);
//         nomina.setIntEMP_ID(1);
//         String strPassB64 = "9qT9yUhip1dsAi0FD6nYlw==";
//         Opalina opa = new Opalina();
//         String strMyPassSecret = opa.DesEncripta(strPassB64, "dWAM1YhbGAeu7CTULai4eQ==");
//         nomina.setStrMyPassSecret(strMyPassSecret);
//         nomina.setStrPATHFonts("");
//         nomina.setStrPATHXml("/Users/ZeusGalindo/Documents/Zeus/SAT/MMORELOS/");
//         nomina.setStrPATHKeys("/Users/ZeusGalindo/Documents/Zeus/SAT/MMORELOS/Cer_Sello/");
//         nomina.doTimbrado(1, "", 1);
//         String strResult = nomina.getStrResultLast();
//         System.out.println("strResult:" + strResult);
//</editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Costeos">
//         InventarioCosteo costeos = new InventarioCosteo(oConn);
//         costeos.doRecalculoCalculoPromedio(2, "20140430");
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Cierre de periodos">
//         CierrePeriodos cierre = new CierrePeriodos(oConn);
//         boolean bolValido = cierre.esValido("20130908", 2, 2);
//         System.out.println("bolValido:" + bolValido);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Estados de cuenta">
//         String strXml1 = edo.getPeriodos();
//         System.out.println("strXml1:" + strXml1);
//         String strXml2 = edo.getInfoGral(30);
//         System.out.println("strXml2:" + strXml2);
////         String strXml3 = edo.getInfoSaldos(70, 0, "201311");
////         System.out.println("strXml2:" + strXml3);
//         String strXml4 = edo.getSaldosPeriodos(30, 0, "201312",0);
//         System.out.println("strXml4:" + strXml4);
//         String strXml5 = edo.getHistorialFactura(33, 0, 1393);
//         System.out.println("strXml4:" + strXml5);
////         
//         EstadoCuentaProveedor prov = new EstadoCuentaProveedor(oConn);
//         String strXml1 = prov.getInfoGral(16);
//         System.out.println("strXml1:" + strXml1);
//         String strXml2 = prov.getInfoSaldos(16, 0, "201310");
//         System.out.println("strXml2:" + strXml2);
//         String strXml3 = prov.getPeriodos();
//         System.out.println("strXml3:" + strXml3);
//         String strXml5A = prov.getSaldosPeriodos(70, 0, "201307");
//         System.out.println("strXml5A:" + strXml5A);
//         String strXml6A = prov.getHistorialCxp(70, 0, 581);
//         System.out.println("strXml5A:" + strXml6A);
//         EstadoCuentaBanco edo = new EstadoCuentaBanco(oConn);
//         String strXml1 = edo.getPeriodos();
//         System.out.println("strXml1:" + strXml1);
//         String strXml2 = edo.getInfoGral(1);
//         System.out.println("strXml2:" + strXml2);
//         String strXml3 = edo.getInfoSaldos(1, 0, "201311");
//         System.out.println("strXml2:" + strXml3);
//         String strXml4 = edo.getSaldosPeriodos(1, 0, "201311");
//         System.out.println("strXml4:" + strXml4);
//         String strXml5 = edo.getHistorialFactura(33, 0, 1393);
//         System.out.println("strXml4:" + strXml5);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Manejo de turnos  ">
//         Turnos turno = new Turnos();
//         turno.setoConn(oConn);
//         int intTurno = turno.getTurn(1);
//         System.out.println("intTurno:" + intTurno);
//         turno.closeTurn(intTurno, 1, 1, 1);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Addenda MABE">
//         SATAddendaMabe mabe = new SATAddendaMabe();
//         mabe.makeNameSpaceDeclaration("/Users/ZeusGalindo/Documents/Zeus/SAT/GASCARE/XmlSAT171 .xml",  171, oConn);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Addenda autozone">
            //Adenda autozone
//         SATAddendaAutoZone autozone = new SATAddendaAutoZone();
//         autozone.setStrVendorId("4396");
//         autozone.setStrDeptId("528068");
//         autozone.setBuyer("Susana Perez");
//         autozone.makeNameSpaceDeclaration("/Users/ZeusGalindo/Desktop/Xml  Autozone.xml", "/Users/ZeusGalindo/Desktop/Xml  Autozone2.xml", 1, oConn);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Adenda femsa">
//         SATAddendaFemsa femsa = new SATAddendaFemsa();
//         femsa.makeNameSpaceDeclaration("/Users/ZeusGalindo/Documents/Zeus/dbs/SIWEB/Backups/dbs/XmlSAT47 .xml",
//                 53, oConn);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Adenda femsa">
            SATXml3_0 satCFDI = new SATXml3_0();
//         satCFDI.QuitarAddenda("/Users/ZeusGalindo/Documents/Zeus/SAT/PEREZCOLMENARES/XmlSAT409 .xml");
//         SATAddendaSanofi sanofi = new SATAddendaSanofi();
//
//         sanofi.makeNameSpaceDeclaration("/Users/ZeusGalindo/Documents/Zeus/SAT/PEREZCOLMENARES/XmlSAT409 .xml",
//                 409, oConn);
            // </editor-fold>
//         Opalina op = new Opalina();
//         String strUser = op.Encripta("M1CH3LL3", "ceZH/37VIaXBywBsF1ek2A==");
//         String strPass = op.Encripta("M1M1cheliN", "ceZH/37VIaXBywBsF1ek2A==");
//         System.out.println("strUser:" + strUser);
//         System.out.println("strPass:" + strPass);
            // <editor-fold defaultstate="collapsed" desc="Contabilidad restful">
//         ContabilidadRestfulClient client = new ContabilidadRestfulClient();
//         client.setIntEmpresa(2);
//         client.setoConn(oConn);
//         String strCodigo = client.logIn();
//         System.out.println("strCodigo:" + strCodigo);
//         String strXml = client.getPolizas(30, "201406",10000) ;
//         System.out.println("strXml:" + strXml);
//         client.logOut();
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Consulta xml para el jqgrid">
//         CIP_Tabla objTabla = new CIP_Tabla("", "", "", "", sesion);
//         objTabla.Init("POLIZAS", oConn);
//         objTabla.ObtenParams(false, false, true, true, null, oConn);
//         objTabla.setRows(2000);
//         objTabla.setSearch("true");
//         objTabla.setFieldInt("PO_CLIENTE", 96);
//         objTabla.setFieldString("periodo", "201309");
//         //Obtenemos el XML para el GRID
//         String strResp = objTabla.ConsultaXML(oConn);
//         System.out.println("strResp:" + strResp);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Cancela CFDI">
         /*
             * 
             //VERSION WEB
             String strPassB64 = "";
             String strMyPassSecret = "";
             String strPassKey = "";
             try {
             strPassB64 = "9qT9yUhip1dsAi0FD6nYlw==";
             Opalina opa = new Opalina();
             strMyPassSecret = opa.DesEncripta(strPassB64, "dWAM1YhbGAeu7CTULai4eQ==");
             } catch (NoSuchAlgorithmException ex) {
             System.out.println("getPass:" + ex.getMessage());
             } catch (NoSuchPaddingException ex) {
             System.out.println("getPass:" + ex.getMessage());
             } catch (InvalidKeyException ex) {
             System.out.println("getPass:" + ex.getMessage());
             } catch (IllegalBlockSizeException ex) {
             System.out.println("getPass:" + ex.getMessage());
             } catch (BadPaddingException ex) {
             System.out.println("getPass:" + ex.getMessage());
             }
             String strSql = "SELECT EMP_NOAPROB,EMP_FECHAPROB,EMP_NOSERIECERT,EMP_TIPOCOMP,"
             + "EMP_NOMKEY,EMP_TIPOPERS,EMP_USECONTA,EMP_CTAVTA,EMP_CTAIVA,EMP_CTACTE,"
             + "AES_DECRYPT(EMP_PASSKEY, '" + strMyPassSecret + "') AS unencrypted,"
             + "EMP_FIRMA,EMP_PASSCP,EMP_USERCP,EMP_USACODBARR,EMP_NOMCERT "
             + "FROM vta_empresas "
             + "WHERE EMP_ID = " + 1 + "";
             try {
             ResultSet rs2 = oConn.runQuery(strSql, true);
             while (rs2.next()) {

             strPassKey = rs2.getString("unencrypted");

             }
             rs2.close();
             } catch (SQLException ex) {
             ex.fillInStackTrace();
             }



             System.setProperty("http.proxyHost", "localhost");
             System.setProperty("http.proxyPort", "8888");
             //          */
//         System.out.println("inicia proceso...");
//         SatCancelaCFDI satCancela = new SatCancelaCFDI("/Users/ZeusGalindo/Documents/Zeus/SAT/PEREZCOLMENARES/Cer_Sello/");
//         satCancela.setStrPATHKeys("/Users/ZeusGalindo/Documents/Zeus/SAT/PEREZCOLMENARES/Cer_Sello/key_private1.key");
//         satCancela.setStrTablaDoc("vta_facturas");
//         satCancela.setStrPrefijoDoc("FAC_");
//         satCancela.setStrPathXml("/Users/ZeusGalindo/Documents/Zeus/SAT/PEREZCOLMENARES/");
//         satCancela.setIntIdDoc(410);
////          satCancela.setStrPassKey("2012Colm");
////          satCancela.setStrRFC("PECA780201KP8");
////          satCancela.setStrUUID("C84B948F-5481-4C22-A4C1-CE8EE8D53891");
//         System.out.println("Obtenemos certificado......");
//         byte[] certificadoEmisor = PreparaCertificado("/Users/ZeusGalindo/Documents/Zeus/SAT/PEREZCOLMENARES/Cer_Sello/cert_file1.cer");
//         satCancela.setCertificadoEmisor(certificadoEmisor);
//         System.out.println("Cancela.. ");
//         String strResp = satCancela.timbra_Factura("XmlSAT410 .xml");
//         System.out.println("strResp : " + strResp);
//         System.out.println("termina proceso...");

            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Migracion contable masiva">
            //Analizamos los archivos para la contabilidad
            // Directory path here
            String path = "/Users/ZeusGalindo/Documents/Fuentes/Netbeans/ERPWEB_Ventas/build/web/document/CuentasXPagar/";
            //Vincula xml en base al UUID
//         MovProveedor mov = new MovProveedor(oConn, sesion, null);
//         mov.VinculaXML(path);
//         String files;
//         File folder = new File(path);
//         File[] listOfFiles = folder.listFiles();
//
//         for (int i = 0; i < listOfFiles.length; i++) {
//
//            if (listOfFiles[i].isFile()) {
//               files = listOfFiles[i].getName();
//               if (files.toUpperCase().endsWith(".XML") ) {
//                  System.out.println(files);
//                  MovProveedor mov = new MovProveedor( oConn,  sesion,  null);
//                  mov.CargaDatosXML(listOfFiles[i].getAbsolutePath());
//                  System.out.println("UUID:" + mov.getStrUUID());
//               }
//            }
//         }

//         ContabilidadUtil contabilidad = new ContabilidadUtil(oConn, sesion);
//         contabilidad.setStrPathBaseXML(path);
//
//         String strResp1 = contabilidad.doCleanPolizaAuto(1, "201507");
//         System.out.println("strResp1:" + strResp1);
//         contabilidad.setBolContaFacturas(false);
//         contabilidad.setBolContaCXP(false);
//         contabilidad.setBolContaPagos(true);
//         contabilidad.setBolContaCobros(false);
//         contabilidad.setBolContaBco(false);
//         contabilidad.setBolContaNominas(false);
//         contabilidad.setBolContaNCredito(false);
//         contabilidad.setStrFiltroCobros(" and MPm_ID in (  1583)");
//         contabilidad.setStrFiltroCobrosMasivos(" and MPm_ID in (1583)");
//
//         contabilidad.GeneraConta(1, "201507");
//         
////         //         Pintamos resultados
//         String strSeparador = "\n";
//         StringBuilder strResultado = new StringBuilder("Migración masiva contable resultados:" + strSeparador);
//
//         strResultado.append("FACTURAS:").append(strSeparador);
//         strResultado.append("Exitosos ").append(contabilidad.getIntSucessFacturas()).append(strSeparador);
//         strResultado.append("Fallidos ").append(contabilidad.getIntFailFacturas()).append(strSeparador);
//         Iterator<String> it = contabilidad.getLstFailsFacturas().iterator();
//         while (it.hasNext()) {
//            String strError = it.next();
//            strResultado.append(strError).append(strSeparador);
//         }
//
//         strResultado.append("Cuentas por PAGAR:").append(strSeparador);
//         strResultado.append("Exitosos ").append(contabilidad.getIntSucessCXP()).append(strSeparador);
//         strResultado.append("Fallidos ").append(contabilidad.getIntFailCXP()).append(strSeparador);
//         it = contabilidad.getLstFailsCXP().iterator();
//         while (it.hasNext()) {
//            String strError = it.next();
//            strResultado.append(strError).append(strSeparador);
//         }
//
//         strResultado.append("Cobros a clientes:").append(strSeparador);
//         strResultado.append("Exitosos ").append(contabilidad.getIntSucessCobros()).append(strSeparador);
//         strResultado.append("Fallidos ").append(contabilidad.getIntFailCobros()).append(strSeparador);
//         it = contabilidad.getLstFailsCobros().iterator();
//         while (it.hasNext()) {
//            String strError = it.next();
//            strResultado.append(strError).append(strSeparador);
//         }
//
//         strResultado.append("Pagos a proveedor:").append(strSeparador);
//         strResultado.append("Exitosos ").append(contabilidad.getIntSucessPagos()).append(strSeparador);
//         strResultado.append("Fallidos ").append(contabilidad.getIntFailPagos()).append(strSeparador);
//         it = contabilidad.getLstFailsPagos().iterator();
//         while (it.hasNext()) {
//            String strError = it.next();
//            strResultado.append(strError).append(strSeparador);
//         }
//
//         strResultado.append("Bancos:").append(strSeparador);
//         strResultado.append("Exitosos ").append(contabilidad.getIntSucessBcos()).append(strSeparador);
//         strResultado.append("Fallidos ").append(contabilidad.getIntFailBcos()).append(strSeparador);
//         it = contabilidad.getLstFailsBancos().iterator();
//         while (it.hasNext()) {
//            String strError = it.next();
//            strResultado.append(strError).append(strSeparador);
//         }
//         strResultado.append("Nóminas:").append(strSeparador);
//         strResultado.append("Exitosos ").append(contabilidad.getIntSucessNominas()).append(strSeparador);
//         strResultado.append("Fallidos ").append(contabilidad.getIntFailNominas()).append(strSeparador);
//         it = contabilidad.getLstFailsNominas().iterator();
//         while (it.hasNext()) {
//            String strError = it.next();
//            strResultado.append(strError).append(strSeparador);
//         }
//         strResultado.append("NCredito:").append(strSeparador);
//         strResultado.append("Exitosos ").append(contabilidad.getIntSucessNcredito()).append(strSeparador);
//         strResultado.append("Fallidos ").append(contabilidad.getIntFailNcredito()).append(strSeparador);
//         it = contabilidad.getLstFailsNcredito().iterator();
//         while (it.hasNext()) {
//            String strError = it.next();
//            strResultado.append(strError).append(strSeparador);
//         }
//
//         System.out.println(strResultado);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Numeros en letras">
//         StringofNumber stringnumber = new StringofNumber();
//         String strEnLetras = stringnumber.getStringOfNumber(-1526.68);
//         System.out.println("strEnLetras "+ strEnLetras);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Test de aplicación de cobranza a clientes y pago de proveedores">
            TestPagos pagos = new TestPagos();

//         Monedas moneda  = new Monedas(oConn);
//         double dblPar1 = moneda.GetFactorConversion("20130701", 4, 1, 2);
//         double dblPar2 = moneda.GetFactorConversion("20130701", 4, 2, 1);
//         double dblPar3 = moneda.GetFactorConversion("20130701", 4, 2, 3);
//         double dblPar4 = moneda.GetFactorConversion("20130701", 4, 3, 2);
//         double dblPar5 = moneda.GetFactorConversion("20130701", 4, 2, 3);
//         double dblPar6 = moneda.GetFactorConversion("20130701", 4, 1, 3);
//         System.out.println("De pesos a dolares: " + dblPar1);
//         System.out.println("De dolares a pesos: " + dblPar2);
//         System.out.println("De dolares a euros: " + dblPar3);
//         System.out.println("De euros a doalres: " + dblPar4);
//         System.out.println("De dolares a euros: " + dblPar5);
//         System.out.println("De pesos a euros: " + dblPar6);
            //Caso 1 Pagos en pesos con banco en pesos proveedor en pesos y documento en pesos
//         pagos.TestPagoProveedor(oConn, sesion, 2000, 1, "20130808", 1, 1, 2000, "", 0, "468,469", "0,0", "1000,1000", "1000,1000", "1,1");
            //Caso 2 todo en dolares
//         pagos.TestPagoProveedor(oConn, sesion, 100, 2, "20130808", 2, 1, 100, "", 0, "220,390", "0,0", "50,50", "50,50", "2,2");
            //Caso 3 banco en dolares, documento y proveedor en mn
//         pagos.TestPagoProveedor(oConn, sesion, 1200, 2, "20130808", 2, 12.13, 1200, "", 0, "468,469", "0,0", "600,600", "600,600", "2,2");
            //Caso 4 banco en pesos, documento y proveedor en dolares
//         pagos.TestPagoProveedor(oConn, sesion, 2500, 1, "20130808", 1, 0.07675834171278563, 2500, "", 0, "397,479", "0,0", "1250,1250", "1250,1250", "1,1");
            //Caso 5 banco en pesos, documento en dolares y proveedor en pesos
//         pagos.TestPagoProveedor(oConn, sesion, 2200, 1, "20130808", 1, 0.07675834171278563, 2200, "", 0, "411", "0,0", "2200", "2200", "1");
            //Caso 7 banco en pesos, documento en dolares y proveedor en EUROS
//         pagos.TestPagoProveedor(oConn, sesion, 2200, 1, "20130808", 1, 0.0767583417127856, 2200, "", 0, "288", "0,0", "2200", "2200", "1");
            //Caso 8 banco en USD, documento en usd y proveedor en EUROS y pago en pesos
//         pagos.TestPagoProveedor(oConn, sesion, 100, 2, "20130808", 1, 0.0767583417127856, 100, "", 0, "288", "0,0", "100", "100", "1");
            //Caso 9 banco en USD, documento en usd y proveedor en EUROS y pago en USD
//         pagos.TestPagoProveedor(oConn, sesion, 10, 2, "20130808", 2, 1, 10, "", 0, "288", "0,0", "10", "10", "2");
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Aplicación de prorrateo en pedimentos">
//////         //Buscamos los pedimentos aplicados...para cancelarlos
//         String strSql = "select PED_ID from vta_pedimentos where PED_APLICADO = 1 AND PED_ID = 50 order by PED_FECHA_APLIC";//
//         ResultSet rs = oConn.runQuery(strSql, true);
//         while(rs.next()){
//            Pedimentos pedimento = new Pedimentos( oConn,  sesion);
//            pedimento.setIntPED_ID(rs.getInt("PED_ID"));
//            pedimento.doCancelaProrrateo();
//            System.out.println("Pedimento con id " + rs.getInt("PED_ID") + "estatus:" + pedimento.getStrResultLast());
//            
//            //Aplicamos los pedimentos
//            pedimento.doGeneraProrrateo();
//            System.out.println("Pedimento con id " + rs.getInt("PED_ID") + " despues de aplicar el prorrateo " + pedimento.getStrResultLast());
//         }
//         rs.close();
            // </editor-fold>
//         Precios precio = new Precios();
//         String strRes = precio.CambioPreciosMasivo(oConn, "HP32", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, 0, 2, 2, 0);
//         System.out.println("strRes: " + strRes);
//         double dblXRedondear = 409.706901098901;
//         NumberString number =  new NumberString();
//         System.out.println("dblRedondeo: " + dblXRedondear);
//         double dblRedondeo = number.RedondeoDolares(dblXRedondear);
//         System.out.println("dblRedondeo: " + dblRedondeo);
//         double dblRedondeo2 = number.RedondeoPesos(dblXRedondear);
//         System.out.println("dblRedondeo2: " + dblRedondeo2);
//         ExportaTickets exportaTickets =  new ExportaTickets();
//         String strTXT = exportaTickets.GenerarTxt(oConn, "20130601", "20130731");
//         System.out.println(" strTXT: " + strTXT);
            //Envio masivo de mails
//         EnvioMasivoMailsAcceso mails = new EnvioMasivoMailsAcceso();
//         mails.EnvioMasivoAccesos(oConn);
            //Actualizar la tasa de paridad
//         Paridades paridad = new Paridades();
//         paridad.CorrigeVentas("20120101", "20130731", oConn, 2,true);
//         paridad.CorrigeCXPagar("20120101", "20130731", oConn, 2,true);
//         
            //Circulos
//         String strSql = "select CT_ID from vta_cliente order by ct_id";
//         ResultSet rs = oConn.runQuery(strSql, true);
//         while (rs.next()) {
//            try {
//               boolean bolCirculo = Redes.hayUnCirculo(oConn, "vta_cliente", "CT_UPLINE", "CT_ID", rs.getInt("CT_ID"), 1);
//               if (bolCirculo) {
//                  //System.out.println(" HAY CIRCULO");
//               } else {
//                  //System.out.println(" SI CUMPLE.....");
//               }
//            } catch (com.mx.siweb.mlm.utilerias.ExceptionRed ex) {
//               System.out.println(" HAY CIRCULO");
//            }
//
//         }
//         rs.close();
//////Carga inicial de existencias de productos
            Importar importa = new Importar();
            importa.setIntEMP_ID(1);
            importa.setIntSC_ID(1);
//         importa.SaldosInicialesProv(oConn, "/Users/ZeusGalindo/Desktop/Cxpagar_layoutmak.xls", sesion);
//         Importar importa = new Importar();
//         importa.setIntEMP_ID(1);
//         importa.setIntSC_ID(1);
////         importa.SaldosInicialesProv(oConn, "/Users/ZeusGalindo/Desktop/Layout-cxpagar190713_QA.xls", sesion);
//         importa.SaldosInicialesProv(oConn, "/Users/ZeusGalindo/Desktop/Layout-cxpagar190713_822.xls", sesion);
//         importa.CargarExistenciaProductos(oConn, "/Users/ZeusGalindo/Documents/Fuentes/Historial_Actualizaciones_clientes/CasaJosefa/LayoutProductosABRAHAM.xls", sesion);

         // <editor-fold defaultstate="collapsed" desc="*********************Test comision Prosefi****************">
//         com.mx.siweb.mlm.compensacion.prosefi.CalculoComision comis
//                 = new com.mx.siweb.mlm.compensacion.prosefi.CalculoComision(oConn, 249, false);
//         comis.doFase1();
//         System.out.println("Resultado de fases " + comis.getStrResultLast());
//         if (comis.getStrResultLast().equals("OK")) {
//            comis.doFase2();
//            if (comis.getStrResultLast().equals("OK")) {
//               comis.doFase3();
//               if (comis.getStrResultLast().equals("OK")) {
//                  comis.doFase4();
//                  if (comis.getStrResultLast().equals("OK")) {
//                     System.out.println("Concluyeron las comisiones....");
//                  } else {
//                     System.out.println("ERROR AL CALCULAR COMISIONES(3) : " + comis.getStrResultLast());
//                  }
//               } else {
//                  System.out.println("ERROR AL CALCULAR COMISIONES(3) : " + comis.getStrResultLast());
//               }
//            } else {
//               System.out.println("ERROR AL CALCULAR COMISIONES (2): " + comis.getStrResultLast());
//            }
//         } else {
//            System.out.println("ERROR AL CALCULAR COMISIONES(1): " + comis.getStrResultLast());
//         }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="*********************Test comision Prosefi****************">
//         //*********************Test comision Jonfilu****************
//         com.mx.siweb.mlm.compensacion.prosefi.CalculoComision comis =  
//                 new com.mx.siweb.mlm.compensacion.prosefi.CalculoComision(oConn,1,false);
//         comis.doFase1();
//         System.out.println("Resultado de fases " + comis.getStrResultLast());
//         if(comis.getStrResultLast().equals("OK")){
//            comis.doFase2();
//            if(comis.getStrResultLast().equals("OK")){
//               comis.doFase3();
////               if(comis.getStrResultLast().equals("OK")){
////                  System.out.println("Concluyeron las comisiones....");
////               }else{
////                  System.out.println("ERROR AL CALCULAR COMISIONES(3) : " + comis.getStrResultLast());
////               }
//            }else{
//                System.out.println("ERROR AL CALCULAR COMISIONES (2): " + comis.getStrResultLast());
//            }
//         }else{
//            System.out.println("ERROR AL CALCULAR COMISIONES(1): " + comis.getStrResultLast());
//         }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Test comision Jonfilu">
            //Prueba de cobranza con archivo masivo
//         CobranzaLayouts layout = new CobranzaLayouts(1,oConn,sesion);
//         layout.setBolLinkPedidos(true);
//         layout.Init();
//         String strRes = layout.ProcesaLayout("/Users/aleph_79/Desktop/CobranzaEjemplo.txt");
//         System.out.println(" strRes " + strRes);
// </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Test comision Xocobenefit">
            //* * * * * * * * * * * * * * * * * * * * * Test comision Jonfilu
//         com.mx.siweb.mlm.compensacion.xocobenefit.CalculoComision comis
//                 = new com.mx.siweb.mlm.compensacion.xocobenefit.CalculoComision(oConn, 1, false);
//         comis.doFase1();
//         System.out.println("Resultado de fases " + comis.getStrResultLast());
//         if (comis.getStrResultLast().equals("OK")) {
//            comis.doFase2();
//            if (comis.getStrResultLast().equals("OK")) {
//               comis.doFase3();
//               if(comis.getStrResultLast().equals("OK")){
//                  System.out.println("Concluyeron las comisiones....");
//               }else{
//                  System.out.println("ERROR AL CALCULAR COMISIONES(3) : " + comis.getStrResultLast());
//               }
//            } else {
//               System.out.println("ERROR AL CALCULAR COMISIONES (2): " + comis.getStrResultLast());
//            }
//         } else {
//            System.out.println("ERROR AL CALCULAR COMISIONES(1): " + comis.getStrResultLast());
//         }
// </editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Test comision Casa josefa">
            //Prueba de cobranza con archivo masivo
         /*com.mx.siweb.mlm.compensacion.casajosefa.CalculoComision comis =
             new com.mx.siweb.mlm.compensacion.casajosefa.CalculoComision(oConn, 2, false);
             comis.doFase1();
             System.out.println("Resultado de fases " + comis.getStrResultLast());
             if (comis.getStrResultLast().equals("OK")) {
             comis.doFase2();
             if (comis.getStrResultLast().equals("OK")) {
             comis.doFase3();
             if (comis.getStrResultLast().equals("OK")) {
             comis.doFase4();
             System.out.println("Concluyeron las comisiones....");
             } else {
             System.out.println("ERROR AL CALCULAR COMISIONES(3) : " + comis.getStrResultLast());
             }
             } else {
             System.out.println("ERROR AL CALCULAR COMISIONES (2): " + comis.getStrResultLast());
             }
             } else {
             System.out.println("ERROR AL CALCULAR COMISIONES(1): " + comis.getStrResultLast());
             }*/
            //</editor-fold>
            //Addenda LALA
//         SATAddendaLala lala = new SATAddendaLala();
//         lala.makeNameSpaceDeclaration("/Users/aleph_79/Documents/Zeus/SAT/PEREZCOLMENARES/XmlSAT138 .xml", 138, oConn);

            /*
             id: "node35",
             name: "3.5",
             data: {},
             children: [{
             id: "node46",
             name: "4.6",
             data: {},
             children: []
             */
//         VistaRed red = new VistaRed(oConn);
//         String strRes = red.doJsonJit(20340);
//         System.out.println(" jSON= " + strRes);
//
//         PromocionesGenerator promociones = new PromocionesGenerator(oConn);
//         promociones.cargaPromociones();
//         System.out.println("cuantas promocs... " + promociones.getLstPromociones().size());
//         System.out.println("XML " + promociones.getXml());
//         int intIdCliente = 346884;
//         DigitoVerificador digito = new DigitoVerificador();
////         int strDigito = digito.CalculaModulo97("7004", "1706471", intIdCliente + "");
////         String strReferencia1 = intIdCliente + "" + strDigito;
////         if (strDigito < 10) {
////            strReferencia1 = intIdCliente + "0" + strDigito;
////         }
////
////         System.out.println("strReferencia1:" + strReferencia1);
//         String strSql = "select CT_ID from vta_cliente ";//ent
//         ResultSet rs = oConn.runQuery(strSql, true);
//         while (rs.next()) {
//            int intIdCliente = rs.getInt("CT_ID");
//
//            int strDigito = digito.CalculaModulo97("7004", "1706471", intIdCliente + "");
//
//            String strReferencia1 = intIdCliente + "" + strDigito;
//            if (strDigito < 10) {
//               strReferencia1 = intIdCliente + "0" + strDigito;
//            }
//            String strUpdate = "update vta_cliente set CT_RBANCARIA1='" + strReferencia1 + "' where CT_ID = " + intIdCliente;
//            oConn.runQueryLMD(strUpdate);
//         }
////         rs.close();
//         lstNo.add("IMP_ID");
//                  copia.CopiaTabla("ait_importacion", " where imp_id in (2)", lstNo, oConn);
//         lstNo.add("DBP_ID");
//         copia.CopiaTabla("dashboard_params", " where DBP_ID in (10)", lstNo, oConn);
//                  copia.CopiaTabla("ait_importacion", " where imp_id in (2)", lstNo, oConn);
//////            //Copiar formatos
//         copia.CopiaTabla("formatos", " where fm_id in (24)", lstNo, oConn);
//         lstNo.add("fmd_id");
//         copia.CopiaTabla("formatosql", " where fm_id in (24)", lstNo, oConn);
//         copia.CopiaTabla("formatodeta", " where fm_id in (24) order by FMD_HEAD_FOOT_BOD,FMD_ORDEN ", lstNo, oConn);
//         copia.CopiaTabla("formatodeta", " where fm_id = 23 and fmd_head_foot_bod = 0 and FMD_POSY = 565", lstNo, oConn);
//         copia.CopiaTabla("formatodeta", " where fm_id = 1 and FMD_HEAD_FOOT_BOD = 1;", lstNo, oConn);
//         copia.CopiaTabla("formatodeta", " where fm_id = 23 and fmd_head_foot_bod = 1", lstNo, oConn);
//                  copia.CopiaTabla("formatosql", " where fm_id in (23) and fs_id = 72", lstNo, oConn);
//         copia.CopiaTabla("vta_mov_cte", " where fac_id in(	 3369,3373,3397,3412) and mc_cargo> 0", lstNo, oConn);
//         Copiar facturas
//         copia.CopiaTabla("vta_facturas", " where fac_id in (311)", lstNo, oConn);
//         lstNo.add("MC_ID");
//         copia.CopiaTabla("vta_mov_cte", " where fac_id in (311)", lstNo, oConn);
//         copia.CopiaTabla("vta_cliente", " where  ct_id > 349278", lstNo, oConn);
//         copia.CopiaTabla("polizas", " where PO_CLIENTE = 38 and left(PO_FECHA,6)='201401'", lstNo, oConn);
//         copia.CopiaTabla("polizasdeta", " where  polizasdeta.ID_CLIENTE = 38 and left(polizasdeta.PD_FECHA,6)='201401'", lstNo, oConn);
//         copia.CopiaTabla("polizasdetaprov", " where polizasdetaprov.PD_ID IN (select polizasdeta.PD_ID from polizas,polizasdeta where polizas.PO_ID= polizasdeta.PO_ID and polizasdeta.ID_CLIENTE = 38 and left(polizasdeta.PD_FECHA,6)='201401')", lstNo, oConn);
////                 copia.CopiaTabla("formatodeta", " where fmd_id in (4453)", lstNo, oConn);
//         
////////////         //Copiar pantallas
//         copia.CopiaTabla("formularios", " where frm_id in (5040)", lstNo, oConn);
//         lstNo.add("frmd_id");
            
            
            /**
             *
             * pagina wbe cofide
             *
             */
            // <editor-fold defaultstate="collapsed" desc="Migracion pagin COFIDE">
            SincronizarPaginaWeb sincroniza = new SincronizarPaginaWeb(oConn);
            sincroniza.setBolLocal(true);
            sincroniza.setStrUsuario("root");
            sincroniza.setStrPassword("");
            sincroniza.setStrUrl("jdbc:mysql://localhost:3306/cofide");
            sincroniza.actualizaPaginaWeb(2);
            // </editor-fold>
            /**
             *
             * pagin web cofide
             *
             */
            
            
            
            /**
             *
             *testclasses
             *
             */
//         copia.CopiaTabla("formularios_deta", " where  frm_id = 5300 ", lstNo, oConn);
//         copia.CopiaTabla("formularios", " where  frm_id = 5300 ", lstNo, oConn);
//         copia.CopiaTabla("permisos_sistema", " where  ps_id = 720 ", lstNo, oConn);
//         copia.CopiaTabla("repo_master", " where  rep_id = 720 ", lstNo, oConn);
//         copia.CopiaTabla("repo_params", " where  rep_id = 720 ", lstNo, oConn);
            /**
             *
             *testclasses
             *
             */
//         copia.CopiaTabla("formularios_deta", " where frmd_id in (11093)", lstNo, oConn);
//         copia.CopiaTabla("formularios_deta", " where frmd_id in (10387,10388)", lstNo, oConn);
//         copia.CopiaTabla("formularios_deta", " where frm_id in (16) and frmd_nombre = 'PR_GPO_MODI_PREC' ", lstNo, oConn);
//         copia.CopiaTabla("formularios_deta", " where frm_id in (29) and frmd_nombre = 'PD_FOLIO_C' ", lstNo, oConn);
//         copia.CopiaTabla("vta_cxpagar", " where cxp_id in (998)", lstNo, oConn);
//         lstNo.add("MP_ID");
//         copia.CopiaTabla("vta_mov_prov", " where MP_ESPAGO = 0 AND cxp_id in (998)", lstNo, oConn);
//         lstNo.add("CXPD_ID");
//         copia.CopiaTabla("vta_cxpagardetalle", " where cxp_id in (998)", lstNo, oConn);
//////         lstNo.add("MC_ID");
////         copia.CopiaTabla("vta_mov_cte", " limit 0,1", lstNo, oConn);
//         copia.CopiaTabla("permisos_sistema", " WHERE PS_SECCION = 22", lstNo, oConn);
//         copia.CopiaTabla("permisos_sistema", " WHERE PS_ID IN( 682,683)", lstNo, oConn);
//         lstNo.add("MP_ID");
//         copia.CopiaTabla("vta_mov_prov", " limit 0,1", lstNo, oConn);
            //Carga inicial de existencias de productos
//    Importar importa = new Importar();
//    importa.setIntEMP_ID(2);
//    importa.setIntSC_ID(2);
//    importa.SaldosInicialesProv(oConn, "/Users/ZeusGalindo/Desktop/CambiosMak_31-07-2013.xls", sesion);
//         lstNo.add("frmn_id");
//         copia.CopiaTabla("formularios_menuopt", " where frm_id in (27)", lstNo, oConn);
//         //Copiado de reportes
//         copia.CopiaTabla("repo_master", " WHERE REP_ID in( 79,80,81,82)  order by REP_ID ", lstNo, oConn);
//         lstNo.add("REPP_ID");
//         copia.CopiaTabla("repo_params", " WHERE REP_ID in( 76,78) ", lstNo, oConn);
////         copia.CopiaTabla("repo_tipo_control", " ", lstNo, oConn);
//         copia.CopiaTabla("repo_tipo_dato", " ", lstNo, oConn);
//         copia.CopiaTabla("mailtemplates", " Where MT_ID IN(35)  ", lstNo, oConn);
//         lstNo.add("PVAR_ID");
////         copia.CopiaTabla("vta_promo_variables", " where PVAR_ID in (27)", lstNo, oConn);
//         copia.CopiaTabla("mailtemplates", " where MT_ID in (21,22)", lstNo, oConn);
//         lstNo.add("PR_ID");
//         copia.CopiaTabla("vta_producto", " where SC_ID = 3", lstNo, oConn);
////   copia.CopiaTabla("reportbase", " where RP_ID in (17)", lstNo, oConn);
//         lstNo.add("frmd_id");
//         copia.CopiaTabla("formularios_deta", " where frm_id in (6640,6641)", lstNo, oConn);
//        copia.CopiaTabla("permisos_sistema", " where ps_id in(510)", lstNo, oConn);
//copiado de promociones
//         copia.CopiaTabla("vta_promo_consecuente", " where PCO_ID = 15", lstNo, oConn);
//         copia.CopiaTabla("vta_promo_link_consecuente", " where PROM_ID = 16 ", lstNo, oConn);
////////         copia.CopiaTabla("vta_promo_sucursales", " ", lstNo, oConn);
////         copia.CopiaTabla("vta_promo_variables", " where PVAR_ID = 43", lstNo, oConn);
//         copia.CopiaTabla("vta_promociones", " where PROM_ID = 16", lstNo, oConn);
            // <editor-fold defaultstate="collapsed" desc="Generar pantallas en automatico">
            ArrayList<String> lst = new ArrayList<String>();
//         lst.add("ait_forwarder_interno");
//         lst.add("ait_forwarder_locales");
            //lst.add("vta_nombcos");
//         lst.add("estadospais");&
//         lst.add("vta_provcat6");
//         lst.add("vta_provcat7");
//         lst.add("vta_provcat8");
//         lst.add("vta_provcat9");
////         lst.add("vta_provcat10");
//         lst.add("cp_gog_series");
//         lst.add("control_actividad");
//         lst.add("control_actividad_estatus");
//         lst.add("control_categoria1");
//         lst.add("control_categoria2");
//         lst.add("control_categoria3");
//         lst.add("control_categoria4");
//         lst.add("control_categoria5");
//         lst.add("control_tarea");
//         lst.add("control_tarea_estatus");
//         lst.add("cat_procedenciarecurso");
//            CIP_GeneraCodigo.GeneraPantallasAuto(lst, oConn);

//         CIP_GeneraCodigo.GeneraEntidades(oConn);
            // </editor-fold>
//         
//         long lngDiff = Fechas.difDiasEntre2fechas(2012,2,31,2012,3,1);
////         System.out.println(" lngDiff " + lngDiff);
//   copia.CopiaTabla("formularios", " where frm_id in (92)", lstNo, oConn);
//   lstNo.add("frmd_id");
//   copia.CopiaTabla("formularios_deta", " where frm_id = '92'  ", lstNo, oConn);
//            lstNo.add("frmn_id");
//         copia.CopiaTabla("formularios_menuopt", " where frm_id in (27)", lstNo, oConn);
//         
//         copia.CopiaTabla("permisos_sistema", " where PS_ID in (488)", lstNo, oConn);
//         Ventas vta = new Ventas(oConn, 2011, 1, 0);
//         vta.getClientesAnual();
//         vta.doReportAnul();
//         HashMap map = vta.getLstCte();
//         Iterator<Map.Entry> it = map.entrySet().iterator();
//         while (it.hasNext()) {
//            Map.Entry e = it.next();
//            ClienteAnual cte = (ClienteAnual)e.getValue();
//            System.out.println(e.getKey() + " " + cte.getStrNombre() + " " + cte.getMap1().get("timporte") + " " + cte.getMap2().get("timporte"));
//         }
            //
            //System.out.println("strLlave:" + strLlave);
            // <editor-fold defaultstate="collapsed" desc="Test para los CFDI">
//         SATXml3_0 satCFDI = new SATXml3_0();
//                              satCFDI.setIntTransaccion(80);
//                              satCFDI.setStrPath("c:/zeus/SAT/siweb");
//                              satCFDI.setNoAprobacion("288178");
//                              satCFDI.setFechaAprobacion("20101210");
//                              satCFDI.setNoSerieCert("00001000000102324829");
//                              satCFDI.setStrPathKey("c:/zeus/SAT/siweb/Cer_Sello/key_private1.key");
//                              satCFDI.setStrPassKey("SOINWEB061210");
//                              satCFDI.setVarSesiones(sesion);
//                              satCFDI.setoConn(oConn);
//                              satCFDI.setBolSendMailMasivo(false);
//                              satCFDI.setStrPathCert("c:/zeus/SAT/siweb/Cer_Sello/cert_file1.cer");
//                              satCFDI.setStrPathConfigPAC("c:/zeus/SAT/siweb/Cer_Sello/");
//         String strResp = satCFDI.GeneraXml();
//         System.out.println("strResp:" + strResp);
         /*
             Test para los CFD 2.2
             */
            SATXml satCFD = new SATXml();

         // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Addenda Mabe">
//         SATAddendaSantander santander = new SATAddendaSantander();
//         satCFD.setSatAddenda(santander, Core.FirmasElectronicas.Addendas.santander.ObjectFactory.class);
//         
//
//         satCFD.setIntTransaccion(186);
//         satCFD.setStrPath("/Users/aleph_79/Documents/Zeus/SAT/SIWEB/");
//         satCFD.setNoAprobacion("288178");
//         satCFD.setFechaAprobacion("20101210");
//         satCFD.setNoSerieCert("00001000000102324829");
//         satCFD.setStrPathKey("/Users/aleph_79/Documents/Zeus/SAT/SIWEB/Cer_Sello/key_private1.key");
//         satCFD.setStrPassKey("SOINWEB061210");
//         satCFD.setStrPathCert("/Users/aleph_79/Documents/Zeus/SAT/SIWEB/Cer_Sello/cert1.cer");
//         satCFD.setVarSesiones(sesion);
//         satCFD.setoConn(oConn);
//         
//         satCFD.setBolSendMailMasivo(false);
            //satCFD.setStrPathCert("c:/zeus/SAT/escuadron201/Cer_Sello/cert_file1.cer");
//         String strResp = satCFD.GeneraXml();
//         System.out.println("strResp:" + strResp);
//      try {
//         String strPassB64 = "9qT9yUhip1dsAi0FD6nYlw==";
//         Opalina opa = new Opalina();
//         String strMyPassSecret = opa.DesEncripta(strPassB64, "dWAM1YhbGAeu7CTULai4eQ==");
//         System.out.println("strMyPassSecret " + strMyPassSecret);
//      } catch (NoSuchAlgorithmException ex) {
//         System.out.println("getPass:" + ex.getMessage());
//      } catch (NoSuchPaddingException ex) {
//         System.out.println("getPass:" + ex.getMessage());
//      } catch (InvalidKeyException ex) {
//         System.out.println("getPass:" + ex.getMessage());
//      } catch (IllegalBlockSizeException ex) {
//         System.out.println("getPass:" + ex.getMessage());
//      } catch (BadPaddingException ex) {
//         System.out.println("getPass:" + ex.getMessage());
//      }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Graficas con freechart">
//         BarGraphics bar = new BarGraphics();
//         AcumuladosporPeriodo acum1 = new AcumuladosporPeriodo("2012");
//         AcumuladosporPeriodo acum2 = new AcumuladosporPeriodo("2011");
//         acum1.AgregaPeriodo(12389.98, 1, 2012);
//         acum1.AgregaPeriodo(10549.98, 2, 2012); 
//         acum1.AgregaPeriodo(3234.98, 1, 2011);
//         acum1.AgregaPeriodo(12456.98, 2, 2011);
//         bar.setBolLineaUsaFecha(true);
//         bar.AddTimeSerie(acum1.getTimeseries());
//         bar.AddTimeSerie(acum2.getTimeseries() );
//         bar.DrawGraphic("Mi grafica", null, null, null, "eje x", "eje y", "c:/Zeus/migraficas.jpg", bar.LINE, 350, 300, Color.white);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Test para cancelar Factura">
//         Ticket ticket = new Ticket(oConn, sesion, null);
//         ticket.setStrPATHKeys("c:/zeus/SAT/siweb/Cer_Sello/");
//         ticket.setStrPATHXml("c:/zeus/SAT/siweb/");
//         String strPassB64 = "9qT9yUhip1dsAi0FD6nYlw==";
//         Opalina opa = new Opalina();
//         String strMyPassSecret = opa.DesEncripta(strPassB64, "dWAM1YhbGAeu7CTULai4eQ==");
//
//         ticket.setStrMyPassSecret(strMyPassSecret);
//         //Recibimos parametros
//         String strPrefijoMaster = "TKT";
//         String strTipoVtaNom = Ticket.TICKET;
//         strPrefijoMaster = "FAC";
//         strTipoVtaNom = Ticket.FACTURA;
//         ticket.setStrTipoVta(strTipoVtaNom);
//         //Asignamos el id de la operacion por anular
//         String strIdAnul = "80";
//         int intId = 0;
//         if (strIdAnul == null) {
//            strIdAnul = "0";
//         }
//         intId = Integer.valueOf(strIdAnul);
//         ticket.getDocument().setFieldInt(strPrefijoMaster + "_ID", intId);
//         ticket.Init();
//         ticket.doTrxAnul();
//         String strRes = ticket.getStrResultLast();
//         System.out.println("strRes " + strRes);
            //Generacion QR
            //GeneracionQR.generaQR("SIWEB100302", "GAPZ790609", "1500.00", "EEEWE-WEEWEWEW-WEWEEW-WEWE", "C:/Users/zeus/Desktop/", 1, 1);
//
//         try {
//            File fXmlFile = new File("C:/Zeus/sat/gonzaloroque/XmlSAT7 .xml");
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            dbFactory.setNamespaceAware(true);
//            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            Document doc = dBuilder.parse(fXmlFile);
//            doc.setStrictErrorChecking(false);
//            NodeList nList = doc.getDocumentElement().getChildNodes();
//            if (nList != null) {
//               for (int temp = 0; temp < nList.getLength(); temp++) {
//                  Node nNode = nList.item(temp);
//                  System.out.println("nNode.getNodeName() " + nNode.getNodeName());
//                  if (nNode.getNodeName().equals("cfdi:Complemento")) {
//                     if (nNode.hasChildNodes()) {
//                        NodeList nList2 = nNode.getChildNodes();
//                        for (int temp2 = 0; temp2 < nList2.getLength(); temp2++) {
//                           Node nNode2 = nList2.item(temp2);
//                           if (nNode2.getNodeName().equals("tfd:TimbreFiscalDigital")) {
//                              System.out.println("nNode2.getNodeType() " + nNode2.getNodeType());
//                              NamedNodeMap atts = nNode2.getAttributes();
//                              if (atts != null) {
//                                 System.out.println("noCertificadoSAT " + atts.getNamedItem("noCertificadoSAT").getNodeValue());
//                                 //Element eElement = (Element) nNode2;
//                                 //eElement.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:schemaLocation", "http://www.itcomplements.com/printweb http://www.itcomplements.com/printweb/sonocoV1.xsd");
//                              }
//                           }
//                        }
//                     }
//                  }
//               }
//            }
//
//         } catch (Exception e) {
//            System.out.println("error " + e.getMessage());
//         }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Funciones para redes MLM">
//         //Valida nodo Red
//         boolean bolArmado = red.armarArbol("vta_cgastos", "GT_ID", "GT_UPLINE", 1, "GT_", "", " ORDER BY GT_UPLINE,GT_DESCRIPCION desc", false, true, oConn);
//         boolean bolValido = red.esValidoElNodoPadre(oConn, "vta_cliente", "CT_UPLINE", "CT_ID", 1);
//         System.out.println("bolValido " + bolValido);
//         boolean bolArmado = red.armarArbol("vta_cliente", "CT_ID", "CT_UPLINE", 1, "CT_", "", " ORDER BY CT_ID", false, true, oConn);
//         System.out.println("bolArmado " + bolArmado);
            //boolean bolMov = Redes.moverArbol("vta_cliente", "CT_ID", "CT_UPLINE", 500, 499, 2, false, oConn);
            //System.out.println("bolMov " + bolMov);
//         boolean bolMov = Redes.compactarArbol("vta_cliente", "CT_ID", "CT_UPLINE", 500, 0, false, oConn);
//         System.out.println("bolMov " + bolMov);
//         VistaRed vistaRed = new VistaRed(oConn);
//         String strXML = vistaRed.doXMLtreeGrid(1);
//         System.out.println("strXML:" + strXML);
            // </editor-fold>
//char chr92 = 92;
//JSONObject myString = new JSONObject();
//String[][] lst = new String[2][11];
//lst[0][0] = "hola";
//lst[0][1] = "mundo";
//lst[0][2] = "muchos";
//myString.put("events", lst);
//myString.put("issort", "true");
//myString.put("start", "04" + chr92 + "/23" + chr92 + "/2012 00:00");
//myString.put("end", "04" + chr92 + "/29" + chr92 + "/2012 23:59");
//myString.put("error", "null");
//         System.out.println(" myString " + myString.toString());
            // <editor-fold defaultstate="collapsed" desc="PRUEBA DE FORMATOS DE CONTRATO">
         /*
          
             //Filtro para el reporte
             String strPathFonts = "";
             String strPathFile = "/Users/ZeusGalindo/Desktop/";
             Fechas fecha = new Fechas();
             String strFiltro = " CON_ID = '" + 1 + "' ";

             //Obtenemos los datos del cliente
             String strFCT_ID = "9761";
             if (strFCT_ID == null) {
             strFCT_ID = "0";
             }

             //Documento donde guardaremos el formato
             com.itextpdf.text.Document document = new com.itextpdf.text.Document();
             PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(strPathFile + "contrato.pdf"));
             document.open();

             FormateadorMasivo format = new FormateadorMasivo();
             format.setIntTypeOut(Formateador.FILE);
             //format.setStrPath(this.getServletContext().getRealPath("/"));
             format.setStrPath("/Users/ZeusGalindo/Desktop/");
             format.InitFormat(oConn, "CONTRATO");
             format.addComodinPersonalizado("[LIC]", "LIC. JORGE LUIS MORENO LEON");

             //agregamos la clausula 2
             String strSqlcL2 = "select CLAU_2 from gs_contrato_c2 where CON_ID =1";
             ResultSet rsCl2 = oConn.runQuery(strSqlcL2);
             while (rsCl2.next()) {
             format.addComodinPersonalizado("[clausula2]", rsCl2.getString("CLAU_2"));
             }
             rsCl2.close();

             //agregamos la parte 2 de la culumna CLAU2 de la tabla gs_contratos
             String strSqlcL2_part2 = "select CLAU2_PART2 from gs_contrato_c2 where CON_ID =1";
             rsCl2 = oConn.runQuery(strSqlcL2_part2);
             while (rsCl2.next()) {
             format.addComodinPersonalizado("[clau2_part2]", rsCl2.getString("CLAU2_PART2"));
             }
             rsCl2.close();

             //Consultamos los datos del cliente
             String strSql = "select * from vta_cliente a, vta_empresas b where a.EMP_ID = b.EMP_ID AND CT_ID = " + strFCT_ID;
             ResultSet rs = oConn.runQuery(strSql);

             while (rs.next()) {
             format.addComodinPersonalizado("[EMP_RAZONSOCIAL]", rs.getString("EMP_RAZONSOCIAL"));
             format.addComodinPersonalizado("[EMP_RFC]", "RFC: " + rs.getString("EMP_RFC").substring(0, 3) + "-" + rs.getString("EMP_RFC").substring(3, 9) + "-" + rs.getString("EMP_RFC").substring(9, 12));
             format.addComodinPersonalizado("[CT_RAZONSOCIAL]", rs.getString("CT_RAZONSOCIAL"));
             format.addComodinPersonalizado("[vdirec]", rs.getString("GS_LOCALIZA"));
             format.addComodinPersonalizado("[super]", rs.getString("GS_AREA"));
             format.addComodinPersonalizado("[vgiro]", rs.getString("GS_USO"));
             String strFechaHoy = fecha.getFechaActual();
             String strFechaHo = fecha.DameFechaenLetra(strFechaHoy);
             format.addComodinPersonalizado("[vfecha]", strFechaHo);

             String strFechaIni = fecha.DameFechaenLetra(rs.getString("GS_INICIO"));
             String strFechaFin = fecha.DameFechaenLetra(rs.getString("GS_TERMINO"));
             String strPeriodoFechas = "";
             if (!rs.getString("GS_INICIO").isEmpty() && !rs.getString("GS_TERMINO").isEmpty()) {
             strPeriodoFechas = (fecha.difDiasEntre2fechasMesStr(rs.getString("GS_INICIO"), rs.getString("GS_TERMINO"))) + " MESES ";
             }
             format.addComodinPersonalizado("[Vinicio]", strFechaIni);
             format.addComodinPersonalizado("[vtermino]", strFechaFin);
             format.addComodinPersonalizado("[periodo]", strPeriodoFechas);
             format.addComodinPersonalizado("[GS_RENTA]", "$" + NumberString.FormatearDecimal(rs.getDouble("GS_RENTA"), 2));
             format.addComodinPersonalizado("[nomb]", rs.getString("CT_FIADOR"));
             format.addComodinPersonalizado("[vdomfi]", rs.getString("GS_DOMICILIO"));
             format.addComodinPersonalizado("[vdirecc]", rs.getString("CT_INMUEBLE"));
             format.addComodinPersonalizado("[vnomb]", rs.getString("CT_FIADOR"));
             //revisamos la longitud del rfc para separarlo por -
             if (rs.getString("EMP_RFC").length() == 13) {
             format.addComodinPersonalizado("[CT_RFC]", rs.getString("CT_RFC").substring(0, 4) + "-" + rs.getString("CT_RFC").substring(4, 10) + "-" + rs.getString("CT_RFC").substring(10, 13));
             } else {
             if (rs.getString("EMP_RFC").length() == 12) {
             format.addComodinPersonalizado("[CT_RFC]", rs.getString("CT_RFC").substring(0, 3) + "-" + rs.getString("CT_RFC").substring(3, 9) + "-" + rs.getString("CT_RFC").substring(9, 12));
             }
             }

             format.addComodinPersonalizado("[GS_RENTA]", rs.getString("GS_RENTA"));
             if (rs.getString("GS_FOLIO").isEmpty()) {
             format.addComodinPersonalizado("[vfolio]", "______________");

             } else {
             format.addComodinPersonalizado("[vfolio]", rs.getString("GS_FOLIO"));
             }

             format.addComodinPersonalizado("[GS_RENTA1]", "(" + NumberString.NumeroenTexto(rs.getDouble("GS_RENTA"), "PESOS") + ")");

             }
             rs.close();

             String strRes = format.DoFormat(oConn, strFiltro);

             //Definimos parametros para que el cliente sepa que es un PDF
             System.out.println("strRes: " + strRes);
             if (strRes.equals("OK")) {
             CIP_Formato fPDF = new CIP_Formato();
             fPDF.setDocument(document);
             fPDF.setWriter(writer);
             fPDF.setStrPathFonts(strPathFonts);
             fPDF.EmiteFormatoMasivo(format.getFmXML());
             document.close();
             writer.close();
             }
             */
            // </editor-fold>
            oConn.close();
        } catch (Exception ex) {
            Logger.getLogger(testClasses.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    protected static byte[] PreparaCertificado(String strPathCert) {
        byte[] certificado = null;
        UtilCert cert = new UtilCert();
        cert.OpenCert(strPathCert);
        if (!cert.getStrResult().startsWith("ERROR")) {
            try {
                certificado = cert.getCert().getEncoded();
            } catch (CertificateEncodingException ex) {
                System.out.println("Error...." + ex.toString() + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
            }
        }
        return certificado;
    }

    public static String GeneraMailReferenciado(Conexion oConn, VariableSession varSesiones, int intNvoKey) {
        String strRes = "";
        String strEmail1 = "";
        String strEmail2 = "";
        int intSponzor = 0;
        //Buscamos los datos del sponzor
        String strSqlUsuarios = "SELECT a.CT_SPONZOR,b.CT_RAZONSOCIAL"
                + " ,b.CT_EMAIL1,b.CT_EMAIL2"
                + " FROM vta_cliente a,vta_cliente b "
                + " WHERE "
                + " a.CT_SPONZOR = b.CT_ID "
                + " and a.CT_ID= " + intNvoKey;
        try {
            ResultSet rs = oConn.runQuery(strSqlUsuarios);

            while (rs.next()) {
                intSponzor = rs.getInt("CT_SPONZOR");
                strEmail1 = rs.getString("CT_EMAIL1");
                strEmail2 = rs.getString("CT_EMAIL2");
            }

            rs.close();
        } catch (SQLException ex) {
            System.out.println("ERROR:" + ex.getMessage());

        }

        //validamos que hallan puesto el mail
        Mail mail = new Mail();
        if (!strEmail1.isEmpty() || !strEmail2.isEmpty()) {
            String strLstMail = "";
            //Validamos si el mail del cliente es valido
            if (mail.isEmail(strEmail1)) {
                strLstMail += "," + strEmail1;
            }
            if (mail.isEmail(strEmail2)) {
                strLstMail += "," + strEmail2;
            }

            //Intentamos mandar el mail
            mail.setBolDepuracion(false);
            mail.getTemplate("MSG_UPLINE", oConn);
            mail.getMensaje();
            String strSqlEmp = "SELECT *,(select b.CT_RAZONSOCIAL from vta_cliente b where b.CT_ID =  vta_cliente.CT_SPONZOR) AS CT_RAZONSOCIAL_UPLINE"
                    + " FROM vta_cliente"
                    + " where CT_ID=" + intNvoKey + "";
            try {
                ResultSet rs = oConn.runQuery(strSqlEmp);
                mail.setReplaceContent(rs);
                rs.close();
            } catch (SQLException ex) {
                //this.strResultLast = "ERROR:" + ex.getMessage();
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
        return strRes;
    }

    protected String FormateaTextoXml(String strValor, boolean bolAmpersand) {
        /*
         En el caso del & se deber usar la secuencia &amp;
         En el caso del  se deber usar la secuencia &quot;
         En el caso del < se deber usar la secuencia &lt;
         En el caso del > se deber usar la secuencia &gt;
         En el caso del  se deber usar la secuencia &#36;
         */
        strValor = strValor.replace(Character.toString((char) 10), " ");
        strValor = strValor.replace(Character.toString((char) 13), " ");
        //Aqui iran todos los patrones
        ArrayList<Pattern> lstPatterns = new ArrayList<Pattern>();
        ArrayList<String> lstSustituye = new ArrayList<String>();
        // compilamos el patron
        Pattern patronVarios = Pattern.compile("[ ]+");
        Pattern patronIni = Pattern.compile("^ ");
        Pattern patronFin = Pattern.compile(" $");
        lstPatterns.add(patronVarios);
        lstSustituye.add(" ");
        lstPatterns.add(patronIni);
        lstSustituye.add("");
        lstPatterns.add(patronFin);
        lstSustituye.add("");
        Iterator<Pattern> it = lstPatterns.iterator();
        int intCont = -1;
        while (it.hasNext()) {
            Pattern patron = it.next();
            intCont++;
            // creamos el Matcher a partir del patron, la cadena como parametro
            Matcher encaja = patron.matcher(strValor);
            // invocamos el metodo replaceAll
            strValor = encaja.replaceAll(lstSustituye.get(intCont));
        }
        strValor = strValor.replace("*", " ");
        if (bolAmpersand) {
            strValor = strValor.replace("&", "&amp;");
        }
        strValor = strValor.replace("\"", "&quot;");
        strValor = strValor.replace("<", "&lt;");
        strValor = strValor.replace(">", "&gt;");
        strValor = strValor.replace("'", "&#36;");
        if (strValor.endsWith(" ")) {
            strValor = strValor.substring(0, strValor.length() - 1);
        }
        strValor = strValor.replace("  ", " ");
        return strValor;
    }

}
