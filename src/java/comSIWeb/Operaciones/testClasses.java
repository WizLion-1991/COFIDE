/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comSIWeb.Operaciones;

import Core.FirmasElectronicas.Opalina;
import Core.FirmasElectronicas.SATXml;
import ERP.PolizasContables;
import ERP.Precios;
import Tablas.vta_facturas;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Formatos.Formateador;
import comSIWeb.Operaciones.Formatos.FormateadorMasivo;
import comSIWeb.Operaciones.Reportes.CIP_Formato;
import comSIWeb.Operaciones.Reportes.CIP_ReportPDF;
import comSIWeb.Operaciones.Reportes.CIP_ReporteColum;
import comSIWeb.Operaciones.Reportes.CIP_ReporteValor;
import comSIWeb.Scripting.scriptReport;
import comSIWeb.Utilerias.CIP_GeneraCodigo;
import comSIWeb.Utilerias.Fechas;
import comSIWeb.Utilerias.Mail;
import comSIWeb.Utilerias.Util_CopiaTablas;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zeus
 */
public class testClasses {

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {

      /*Abrimos conexion*/
      String[] ConexionURL = new String[4];
      ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_cenyg";

     ConexionURL[1] = "root";
      ConexionURL[2] = "solsticio";
      ConexionURL[3] = "mysql";

      Conexion oConn;
      try {
         oConn = new Conexion(ConexionURL, null);
         oConn.open();
         oConn.setBolMostrarQuerys(true);

         VariableSession sesion = new VariableSession(null);
         Util_CopiaTablas copia = new Util_CopiaTablas();
         ArrayList<String> lstNo = new ArrayList<String>();


         //copia.CopiaTabla("permisos_sistema", " where ps_id in (98)", lstNo, oConn);

         lstNo.add("FMD_ID");
            copia.CopiaTabla("formatodeta", " where fmd_id in (3013,3014)", lstNo, oConn);
         
         //copia.CopiaTabla("formularios", " where frm_id in (79)", lstNo, oConn);
         
//         copia.CopiaTabla("formularios_deta", " where frmd_id in (2399)", lstNo, oConn);
         //copia.CopiaTabla("formatos", " where fm_id in (2)", lstNo, oConn);
//Generamos password para contabilidad

         /*
         System.out.println("MMORELOS");
         String strUserReal = op.Encripta("Mm0ReL0$", "ceZH/37VIaXBywBsF1ek2A==");
         System.out.println("strUserReal " + strUserReal);
         String strPassReal = op.Encripta("E4r0P34$t", "ceZH/37VIaXBywBsF1ek2A==");
         System.out.println("strPassReal " + strPassReal);
         //
         System.out.println("SIWEB");
         strUserReal = op.Encripta("s0l4C!0N3s", "ceZH/37VIaXBywBsF1ek2A==");
         System.out.println("strUserReal " + strUserReal);
         strPassReal = op.Encripta("M4$Pr4cT1c0", "ceZH/37VIaXBywBsF1ek2A==");
         System.out.println("strPassReal " + strPassReal);
         //
         System.out.println("PRUEBAS");
         strUserReal = op.Encripta("pR0$b4nD01", "ceZH/37VIaXBywBsF1ek2A==");
         System.out.println("strUserReal " + strUserReal);
         strPassReal = op.Encripta("B4s36r4Uu", "ceZH/37VIaXBywBsF1ek2A==");
         System.out.println("strPassReal " + strPassReal);
          *
          */
         //De una lista encriptamos la info
         /*String strlstUsers = "BUHO|ObV0YBmuUx,INACOSA|Of@^>Mmk{7,GERMANIA|rN8*+nr?L9,RAMA|Q2M:L6f#n6,JOMY|EOTF3dqVQg,MALINALCO|qC@L*J=tv[,MATZINGA|^0S/swF(>^,VALLE|@#)t;7{yFp,VERVATIM|dkPTu9%oVq";
         String[] lstUser = strlstUsers.split(",");
         for (int i = 0; i < lstUser.length; i++) {
         StringTokenizer tokens = new StringTokenizer(lstUser[i], "|");
         int nDatos = tokens.countTokens();
         String[] datos = new String[nDatos];
         int y = 0;
         while (tokens.hasMoreTokens()) {
         String str = tokens.nextToken();
         datos[y] = str;
         y++;
         }
         System.out.println(datos[0]);
         String strUserReal = op.Encripta(datos[0], "ceZH/37VIaXBywBsF1ek2A==");
         System.out.println("Usuario: " + strUserReal);
         String strPassReal = op.Encripta(datos[1], "ceZH/37VIaXBywBsF1ek2A==");
         System.out.println("Password:" + strPassReal);

         }*/
         //Mail 1
         /*
         Mail mail = new Mail();
         mail.setBolDepuracion(true);
         mail.setUsuario("soporte@solucionesinformaticasweb.com.mx");
         mail.setContrasenia("UH)la0U@D8(9");
         mail.setHost("mail.solucionesinformaticasweb.com.mx");
         //mail.setHost("super.servidoresdedicadosdemexico.com");
         //mail.setPuerto("465");
         mail.setPuerto("28");
         mail.setAsunto("test mensaje");
         mail.setDestino("aleph_79@hotmail.com");
         mail.setMensaje("<b>Esta es una prueba</b> de envio de mail(1)");
         boolean bol = mail.sendMail();

         System.out.println(bol);*/

         /*mail.sendMail("mail.solucionesinformaticasweb.com.mx", "zgalindo@solucionesinformaticasweb.com.mx", "Q!bAY<=q-tF,",
         "zgalindo@solucionesinformaticasweb.com.mx",
         "aleph_79@hotmail.com", "", "", true, "test", "test", "465", "");*/
         /*
         mail.setBolUsaStartTls(true);
         mail.sendMail("smtp.live.com", "aleph_79@hotmail.com", "minotauro",
         "aleph_79@hotmail.com",
         "aleph_79@hotmail.com", "", "", false, "test", "test", "587", "587");*/
         //Mail 2
         /*
         mail = new Mail();
         mail.setBolDepuracion(true);
         //HOTMAIL
         mail.setUsuario("aleph_79@hotmail.com");
         mail.setContrasenia("minotauro");
         mail.setHost("smtp.live.com");
         mail.setPuerto("587");
         mail.setAsunto("test mensaje");
         mail.setDestino("aleph_79@hotmail.com");
         mail.setMensaje("<b>Esta es una prueba</b> de envio de mail(2)");
         mail.setBolUsaStartTls(true);
         bol = mail.sendMail();

         System.out.println(bol);
         //Mail 3
         mail = new Mail();
         mail.setBolDepuracion(true);*/
         //mail.setBolUsaTls(true);
         //mail.setBolUsaStartTls(true);
         //mail.setBolUsaSSL(true);
         /*
         mail.setUsuario("soporte@solucionesinformaticasweb.com.mx");
         mail.setContrasenia("UH)la0U@D8(9");
         mail.setHost("mail.solucionesinformaticasweb.com.mx");
         mail.setBolAcuseRecibo(true);

         //mail.setHost("super.servidoresdedicadosdemexico.com");
         //mail.setPuerto("465");
         mail.setPuerto("28");
         mail.setAsunto("test mensaje");
         mail.setDestino("aleph_79@hotmail.com");
         mail.setMensaje("<b>Esta es una prueba</b> de envio de mail(3)");
         bol = mail.sendMail();*/

         //System.out.println(bol);
         //MIS FINANZAS
         /*mail.setUsuario("zgalindo@misfinanzas.com.mx");
         mail.setContrasenia("minotauro");
         mail.setHost("mail.misfinanzas.com.mx");
         mail.setPuerto("28");
         mail.setAsunto("test mensaje");
         mail.setDestino("aleph_79@hotmail.com");
         mail.setMensaje("<b>Esta es una prueba</b> de envio de mail");
         mail.setBolUsaStartTls(true);*/
         //CONTROLPOINT
         /*mail.setUsuario("system@controlpoint.com.mx");
         mail.setContrasenia("solsticio");
         mail.setHost("mail.controlpoint.com.mx");
         mail.setPuerto("25");
         mail.setAsunto("test mensaje");
         mail.setDestino("aleph_79@hotmail.com");
         mail.setMensaje("<b>Esta es una prueba</b> de envio de mail");*/
         /*boolean bol = mail.sendMail();
         System.out.println(bol);*/
/*
int intMaximo = 112;
String strFrase = "miometriales están formados por haces de células fusiformes, con núcleos alargados y citoplasma eosinófilo";
//Ubicamos ultima palabra
System.out.println(" strFrase:" + strFrase);
System.out.println(" strFrase(length):" + strFrase.length());
int intDiff = intMaximo - strFrase.length();
System.out.println(" intDiff:" + intDiff);
int intLastOf =strFrase.lastIndexOf(" ");
System.out.println("intLastOf:" + intLastOf);
String strSubFrase = strFrase.substring(intLastOf, strFrase.length());
strFrase = strFrase.substring(0, intLastOf);
System.out.println("substr:" + strSubFrase);
System.out.println("strFrase:" + strFrase);
String strSpace = "";
for(int i=0;i<intDiff;i++){
   strSpace += " ";
}
strFrase = strFrase + strSpace + strSubFrase;
System.out.println("strFrase:" + strFrase);
System.out.println(" strFrase(length):" + strFrase.length());
*/
         //*********************************TEST FORMATO*******************************

//         Document document = new Document();
//         PdfWriter writer = PdfWriter.getInstance(document,
//                 new FileOutputStream("C:/Zeus/Biopsia.pdf"));
//         document.open();
//         //document.setPageSize(PageSize.A6);
//         System.out.println(document.getPageSize().getWidth());
//         System.out.println(document.getPageSize().getHeight());
//         Formateador format = new Formateador();
//         //format.setStrPath(this.getServletContext().getRealPath("/"));
//         format.setStrPath("c:/zeus/");
//         format.InitFormat(oConn, "Biopsias");
//         String strRes = format.DoFormat(oConn, 2);
//         System.out.println("strRes: " + strRes);
//         if (strRes.equals("OK")) {
//            CIP_Formato fPDF = new CIP_Formato();
//            fPDF.setDocument(document);
//            fPDF.setWriter(writer);
//
//            String strSep = System.getProperty("file.separator");
//            fPDF.setStrPathFonts("C:" + strSep + "Zeus" + strSep + "Netbeans" + strSep + "ERPWEB_Ventas" + strSep + "build" + strSep + "web" + strSep + "fonts");
//            fPDF.EmiteFormato(format.getFmXML());
//            document.close();
//            //Abrimos el acrobat
//            Process p =
//                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler c:/Zeus/Biopsia.pdf");
//            p.waitFor();
//
//         } else {
//         }
//         System.out.println("Done.");

         //*********************************TEST FORMATO*******************************

         //Ejemplo del objeto reporte
         /*Document document = new Document();
         PdfWriter writer = PdfWriter.getInstance(document,
         new FileOutputStream("C:/Zeus/Report.pdf"));
         document.open();
         CIP_ReportPDF reportPdf = new CIP_ReportPDF("", "");
         reportPdf.setIntTipoReporte(CIP_ReportPDF.PDF);
         //Asignamos el documento el reporte
         reportPdf.setDocument(document);
         reportPdf.setWriter(writer);
         //Reportes por base de datos
         scriptReport sRep = new scriptReport(null, null,reportPdf,oConn,null);
         sRep.setStrAbrev("LST_XFAC");
         sRep.Execute();

         reportPdf.EmiteReportePDF(oConn);
         document.close();
         //Abrimos el acrobat
         Process p =
         Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler c:/Zeus/Report.pdf");
         p.waitFor();*/
         /*CIP_Form form = new CIP_Form();
         String strXML = form.DameCamposSc("CLIENTES", "", oConn, sesion);
         System.out.println(strXML);//Pintamos el resultado*/
         /*
         Bitacoraquerys bitacora = new Bitacoraquerys();
         String strRes = bitacora.ObtenDatos(1, oConn);
         System.out.println("hi " + strRes);
         System.out.println("idbitacoraQuerys " + bitacora.getFieldInt("idbitacoraQuerys"));
         System.out.println("FECHA " + bitacora.getFieldString("FECHA"));
         System.out.println("HORA " + bitacora.getFieldString("HORA"));

         Formularios form = new Formularios();
         strRes = form.ObtenDatos(1, oConn);
         System.out.println("hi " + strRes);
         System.out.println("frm_id " + form.getFieldInt("frm_id"));
         System.out.println("frm_title " + form.getFieldString("frm_title"));
         System.out.println("frm_javascript " + form.getFieldString("frm_javascript"));
         System.out.println("frm_script " + form.getFieldString("frm_script"));

         FormulariosDeta form_deta = new FormulariosDeta();
         ArrayList<TableMaster> lst = form_deta.ObtenDatosVarios(" frm_id = 3 order by frmd_orden", oConn);
         Iterator<TableMaster> it = lst.iterator();
         int conta = 0;
         while(it.hasNext()){
         TableMaster table = it.next();
         conta++;
         System.out.println(conta + " " + table.getFieldString("frmd_nombre"));
         }
         System.out.println("size..." + lst.size());
         
         System.out.println("test...Pantalla");
         CIP_Form Pantalla =  new CIP_Form();
         String strResult = Pantalla.DameCamposSc("CUENTA", "", oConn, null);
         System.out.println("strResult:" + strResult);*/

         //CIP_Tabla objTabla = new CIP_Tabla("","","","",null);
         //objTabla.Init("ESTACION", oConn);
         //objTabla.ObtenParams(false,true,false,false,request);
         //objTabla.ValorKey = "1";
         //objTabla.ObtenDatos( oConn);
         //Obtenemos el XML
         /*String strXML = objTabla.ConsultaXML(oConn);
         System.out.println( "sql: " + strXML);*/

         //CIP_Menu menu = new CIP_Menu();
         //menu.DrawMenu(oConn, null, null,null);

         /*Generamosd objetos*/

         //System.out.println("generamos objetos de manera dinamica...");
         //CIP_GeneraCodigo.GeneraEntidades(oConn);
         ArrayList<String> lst = new ArrayList<String>();
         /*lst.add("vta_empresas");
         lst.add("vta_estacionamiento");
         lst.add("vta_cliente");
         lst.add("vta_conceptofactura");
         lst.add("vtas_tarifas");
         lst.add("vtas_formaspago");
         lst.add("vtas_pensiones");*/
         //CIP_GeneraCodigo.GeneraPantallasAuto(lst, oConn);
         //**************************TEST SAT***********************
         /*SATXml SAT = new SATXml(33, "C:/Zeus/sat/SIWEB/",
         "288178", "20101210", "00001000000102324829",
         "C:/Zeus/sat/SIWEB/Cer_Sello/key_private1.key", "SOINWEB061210", sesion,
         oConn);
         String strRes = SAT.GeneraXml();
         System.out.println(" strRes " + strRes);*/
         //**********************************************************
         oConn.close();
      } catch (Exception ex) {
         Logger.getLogger(testClasses.class.getName()).log(Level.SEVERE, null, ex);
      }


   }

   public static void GeneraConta(Conexion oConn, VariableSession sesion, int intEMP_ID) {

      try {
         //Consultamos tas las operac ion bd local
         String strsqlmaster = "SELECT FAC_ID,TI_ID FROM vta_facturas where left(FAC_FECHA,6)='201104' and EMP_ID = " + intEMP_ID + " limit 0,1 ";
         ResultSet rsM = oConn.runQuery(strsqlmaster);
         while (rsM.next()) {
            //Instanciamos el docto
            int intId = rsM.getInt("FAC_ID");
            int intTI_ID = rsM.getInt("TI_ID");
            TableMaster document = new vta_facturas();
            document.ObtenDatos(intId, oConn);
            //Valores para retencion
            int intCT_TIPOPERS = 0;
            int intEMP_TIPOPERS = 0;
            //Valores para contabilidad
            int intEMP_USECONTA = 0;
            String strCtaVtasGlobal = "";
            String strCtaVtasIVAGlobal = "";
            String strCtaVtasCteGlobal = "";
            String strCtaVtas = "";
            String strCtaVtasCte = "";
            String strEMP_PASSCP = "";
            String strEMP_USERCP = "";
            String strSql = "SELECT * FROM vta_empresas where EMP_ID=" + document.getFieldInt("EMP_ID") + "";
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  strEMP_USERCP = rs.getString("EMP_USERCP");
                  strEMP_PASSCP = rs.getString("EMP_PASSCP");
               }
               //if(rs.getStatement() != null )rs.getStatement().close(); rs.close();
            } catch (SQLException ex) {
               ex.fillInStackTrace();
            }
            String strPrefijoMaster = "FAC";
            //Copiar datos del cliente
            boolean bolFindCte = false;
            strSql = "SELECT CT_RAZONSOCIAL,CT_RFC,CT_CALLE,CT_COLONIA,CT_LOCALIDAD," + " CT_MUNICIPIO,CT_ESTADO,CT_NUMERO,CT_NUMINT,CT_CONTACTE,CT_CONTAVTA," + " CT_CP,CT_DIASCREDITO,CT_LPRECIOS,CT_TIPOPERS FROM vta_cliente " + " where CT_ID=" + document.getFieldInt("CT_ID") + "";
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  document.setFieldString(strPrefijoMaster + "_RAZONSOCIAL", rs.getString("CT_RAZONSOCIAL"));
                  document.setFieldString(strPrefijoMaster + "_RFC", rs.getString("CT_RFC"));
                  document.setFieldString(strPrefijoMaster + "_CALLE", rs.getString("CT_CALLE"));
                  document.setFieldString(strPrefijoMaster + "_COLONIA", rs.getString("CT_COLONIA"));
                  document.setFieldString(strPrefijoMaster + "_LOCALIDAD", rs.getString("CT_LOCALIDAD"));
                  document.setFieldString(strPrefijoMaster + "_MUNICIPIO", rs.getString("CT_MUNICIPIO"));
                  document.setFieldString(strPrefijoMaster + "_ESTADO", rs.getString("CT_ESTADO"));
                  document.setFieldString(strPrefijoMaster + "_NUMERO", rs.getString("CT_NUMERO"));
                  document.setFieldString(strPrefijoMaster + "_NUMINT", rs.getString("CT_NUMINT"));
                  document.setFieldString(strPrefijoMaster + "_CP", rs.getString("CT_CP"));
                  document.setFieldInt(strPrefijoMaster + "_DIASCREDITO", rs.getInt("CT_DIASCREDITO"));
                  document.setFieldInt(strPrefijoMaster + "_LPRECIOS", rs.getInt("CT_LPRECIOS"));
                  strCtaVtas = rs.getString("CT_CONTAVTA");
                  strCtaVtasCte = rs.getString("CT_CONTACTE");
                  intCT_TIPOPERS = rs.getInt("CT_TIPOPERS");
                  bolFindCte = true;
               }
               //if(rs.getStatement() != null )rs.getStatement().close(); rs.close();
            } catch (SQLException ex) {
               ex.fillInStackTrace();
            }
            //Buscamos cuentas para los ivas
            String strCtaVtasIVATasa = "";
            strSql = "SELECT TI_CTA_CONT,TI_CTA_CONT_COB FROM vta_tasaiva "
                    + " where TI_ID=" + intTI_ID + "";
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  strCtaVtasIVATasa = rs.getString("TI_CTA_CONT");
               }
               //if(rs.getStatement() != null )rs.getStatement().close(); rs.close();
            } catch (SQLException ex) {
               ex.fillInStackTrace();
            }
            PolizasContables poli = new PolizasContables(oConn, sesion, null);
            poli.setStrOper("NEW");
            int intValOper = PolizasContables.FACTURA;
            System.out.println("strEMP_USERCP " + strEMP_USERCP);
            poli.setStrUserCte(strEMP_USERCP);
            poli.setStrPassCte(strEMP_PASSCP);
            poli.setDocumentMaster(document);
            //Validamos las cuentas a usar
            //Ventas
            if (strCtaVtas.isEmpty()) {
               poli.getLstCuentas().add(strCtaVtasGlobal);
            } else {
               poli.getLstCuentas().add(strCtaVtas);
            }
            //IVA
            if (strCtaVtasIVATasa.isEmpty()) {
               poli.getLstCuentas().add(strCtaVtasIVAGlobal);
            } else {
               poli.getLstCuentas().add(strCtaVtasIVATasa);
            }
            //Cte
            if (strCtaVtasCte.isEmpty()) {
               poli.getLstCuentas().add(strCtaVtasCteGlobal);
            } else {
               poli.getLstCuentas().add(strCtaVtasCte);
            }
            System.out.println("*********strCtaVtasGlobal...." + strCtaVtasGlobal);
            poli.callRemote(intId, intValOper);
            System.out.println("*********ResultLast...." + poli.getStrResultLast());
            if (poli.getStrResultLast().startsWith("OK")) {
               //Marcamos la venta como procesada
               oConn.runQueryLMD("UPDATE vta_facturas " + " SET FAC_EXEC_INTER_CP =  " + poli.getStrResultLast().replace("OK.", "") + " WHERE FAC_ID = " + intId);
            }
         }
         rsM.close();
      } catch (SQLException ex) {
         Logger.getLogger(testClasses.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
}
