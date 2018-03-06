
import Tablas.CofideLlamadas;
import Tablas.vta_facturas;
import com.mx.siweb.crm.mailing.EnvioMasivoMail;
import com.mx.siweb.erp.especiales.cofide.COFIDE_Calendario;
import com.mx.siweb.erp.especiales.cofide.COFIDE_EvCurso;
import com.mx.siweb.erp.especiales.cofide.COFIDE_LeerXlsDDBB;
import com.mx.siweb.erp.especiales.cofide.COFIDE_Mail_cursos;
import com.mx.siweb.erp.especiales.cofide.COFIDE_ReadXls;
import com.mx.siweb.erp.especiales.cofide.CRM_Envio_Template;
import com.mx.siweb.erp.especiales.cofide.CRM_MailMasivo;
import com.mx.siweb.erp.especiales.cofide.CofideIncidencia;
import com.mx.siweb.erp.especiales.cofide.Cofide_LeerXLSXMeta;
import com.mx.siweb.erp.especiales.cofide.Cofide_PBX;
import com.mx.siweb.erp.especiales.cofide.LlamadaHistorial;
import com.mx.siweb.erp.especiales.cofide.LlamadasPBX;
import com.mx.siweb.erp.especiales.cofide.SincronizarPaginaWeb;
import com.mx.siweb.erp.especiales.cofide.Telemarketing;
import com.mx.siweb.erp.especiales.cofide.entidades.CofideCursoInteres;
import com.mx.siweb.erp.especiales.cofide.entidades.CofideLlamada;
import com.mx.siweb.erp.especiales.cofide.restful.LlamadaResource;
import com.mx.siweb.ui.web.ventas.Vta_Facturas;
import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.CIP_GeneraCodigo;
import comSIWeb.Utilerias.Fechas;
import comSIWeb.Utilerias.Mail;
import comSIWeb.Utilerias.Texto_UppLow;
import comSIWeb.Utilerias.UtilXml;
import comSIWeb.Utilerias.Util_CopiaTablas;
import comSIWeb.Utilerias.generateData;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import static jdk.nashorn.internal.objects.NativeArray.reverse;
import static org.joda.time.format.ISODateTimeFormat.date;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author zeus
 */
public class testClasses1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //boolean bolRes = ConvertUTFtoBOM.convertUTF8toBOM("C:/Zeus", "XmlSAT61 .xml");
        //System.out.println("bolRes:" + bolRes);
/*Abrimos conexion*/
        String[] ConexionURL = new String[4];
//servidor local
//        ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_cofide_new_vta";//nombre de la base de datos
//        ConexionURL[0] = "jdbc:mysql://localhost:3306/vta_cofide";//nombre de la base de datos
//        ConexionURL[1] = "root";
//        ConexionURL[2] = "";
//        ConexionURL[3] = "mysql";
//servidor local

//servidor produccion
//        ConexionURL[0] = "jdbc:mysql://192.168.2.246:3306/vta_cofide_prueba";//nombre de la base de datos
        ConexionURL[0] = "jdbc:mysql://192.168.2.246:3306/vta_cofide";//nombre de la base de datos
        ConexionURL[1] = "root";
        ConexionURL[2] = "Adm1n.01.C0f1d3";
        ConexionURL[3] = "mysql";
////servidor produccion
//servidor pruebas
//        ConexionURL[0] = "jdbc:mysql://192.168.2.138:3306/vta_cofide_prueba";//nombre de la base de datos        
//        ConexionURL[1] = "root";
//        ConexionURL[2] = "C0f1d3";
//        ConexionURL[3] = "mysql";
//servidor pruebas
        Conexion oConn;
        try {
            oConn = new Conexion(ConexionURL, null);
            oConn.open();
            oConn.setBolMostrarQuerys(true);

            VariableSession sesion = new VariableSession(null);
            Util_CopiaTablas copia = new Util_CopiaTablas();
            ArrayList<String> lstNo = new ArrayList<String>();

            lstNo.add("fmd_id");
            lstNo.add("repp_id");
            lstNo.add("frmd_id");
            lstNo.add("pr_id");
            lstNo.add("CCO_ID");
            lstNo.add("CR_ID");
            lstNo.add("EV_ID");
            lstNo.add("CL_ID");
            lstNo.add("PFP_ID");

            lstNo.add("KL_CICLO");
            lstNo.add("CC1_ID");
            lstNo.add("CAMP_ID");
            lstNo.add("FAC_COFIDE_PAGADO");
            lstNo.add("CC_CURSO_ID");
            lstNo.add("COFIDE_NVO");
            lstNo.add("FAC_IMP_PAGADO");
            lstNo.add("FAC_ES_RESERVACION");
            lstNo.add("APC_ID");
            lstNo.add("FAC_INBOUND");
            lstNo.add("MP_ID");
            lstNo.add("FAC_COFIDE_VALIDA");
            lstNo.add("FAC_NOMPAGO");
            lstNo.add("FAC_ID_OLD");
            lstNo.add("COFIDE_DUPLICIDAD");
            lstNo.add("COFIDE_DUPLICIDAD_ID");
            lstNo.add("FAC_TIPO_CURSO");
            lstNo.add("FAC_US_MOD");
            lstNo.add("COFIDE_CARRITO");
            lstNo.add("FAC_MOTIVO_DENEGADA");
            lstNo.add("FAC_CANCEL");
            lstNo.add("FAC_PROMO");
            lstNo.add("FACD_TIPO_CURSO");
            lstNo.add("FACD_CANCEL");
            /**
             * UTIL SIWEB
             */
            //Clase para generar las entidades
//                        CIP_GeneraCodigo.GeneraEntidades(oConn);
            //            copia.CopiaTabla("vta_facturas", " where fac_id in (3767,3768,3769,3770)", lstNo, oConn);
            //            copia.CopiaTabla("vta_facturasdeta", " where fac_id in (3767,3768,3769,3770);", lstNo, oConn);
            /**
             * UTIL SIWEB
             */
            //
            //
            
       
            

//            String strCadena = "anita lava la tina";
//            String strCadenaResultado = "", strTmp = "";
//            for (int i = 0; i < strCadena.length(); i++) {
//                strCadenaResultado += strCadena.substring((strCadena.length() - i) - 1, strCadena.length() - i);
//            }
//            System.out.println("cadena: " + strCadenaResultado);

//            String strCadenaReverse = "";
//            StringBuilder buildCadena = new StringBuilder(strCadena);
//            strCadenaReverse = buildCadena.reverse().toString();
//            System.out.println("cadena al revesada: " + strCadenaReverse);

//            String strgetHIstorialCte = new CRM_Envio_Template().strGetHistorialContactoVta(oConn, "1660597");
//            System.out.println("cuadro: \n" + strgetHIstorialCte);
//            new CRM_Envio_Template().Cofide3Cursos(oConn, 162, "g-unit_jccm@live.com.mx", "jaja saludos", 1, "PERSONALIZADO", 786);
//
//            SimpleDateFormat dateFormat = new SimpleDateFormat("H:m");
//            Date fechaInicial = dateFormat.parse("10:00");
//            Date fechaFinal = dateFormat.parse("19:10");
//            String strDiferencia = "";
//            int diferencia = (int) ((fechaFinal.getTime() - fechaInicial.getTime()) / 1000);
//            int horas = 0;
//            int minutos = 0;
//            if (diferencia > 3600) {
//                horas = (int) Math.floor(diferencia / 3600);
//                diferencia = diferencia - (horas * 3600);
//            }
//            if (diferencia > 60) {
//                minutos = (int) Math.floor(diferencia / 60);
//                diferencia = diferencia - (minutos * 60);
//            }
//            if (horas < 8) {
//                if (horas < 10) {
////                    System.out.println("Hay 0" + horas + ":" + minutos + " de diferencia");
//                    strDiferencia = "0" + horas + ":" + minutos;
//                } else {
////                    System.out.println("Hay " + horas + ":" + minutos + " de diferencia");
//                    strDiferencia = horas + ":" + minutos;
//                }
//            } else {
//                strDiferencia = "Mayor a un día";
//            }
//
//            System.out.println("diferencia: " + strDiferencia);
//            
//            Fechas fec = new Fechas();
//            String strFechaini = "";
//            String strFechaFin = "";
//            String strDiferenciaTiempo = "";
//
//            strFechaini = fec.FormateaAAAAMMDD("20180221", "-");
//            strFechaFin = fec.FormateaAAAAMMDD("20180222", "-");
//
//            strFechaini += " 11:27:00";
//            strFechaFin += " 13:48:00";
//
//            System.out.println("date inicial: " + strFechaini);
//            System.out.println("date final: " + strFechaFin);
//
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
//            Date fechaInicial = dateFormat.parse(strFechaini);
//            Date fechaFinal = dateFormat.parse(strFechaFin);
//            int diferencia = (int) ((fechaFinal.getTime() - fechaInicial.getTime()) / 1000);
//            int dias = 0;
//            int horas = 0;
//            int minutos = 0;
//            if (diferencia > 86400) {
//                dias = (int) Math.floor(diferencia / 86400);
//                diferencia = diferencia - (dias * 86400);
//            }
//            if (diferencia > 3600) {
//                horas = (int) Math.floor(diferencia / 3600);
//                diferencia = diferencia - (horas * 3600);
//            }
//            if (diferencia > 60) {
//                minutos = (int) Math.floor(diferencia / 60);
//                diferencia = diferencia - (minutos * 60);
//            }
//            System.out.println("Hay " + dias + " dias, " + horas + " horas, " + minutos + " minutos y " + diferencia + " segundos de diferencia");
//            strDiferenciaTiempo = dias + " DÍAS, " + horas + ":" + minutos + ":" + diferencia + " HRS";
//            System.out.println("diferencia: " + strDiferenciaTiempo);
//            CofideIncidencia incidencia = new CofideIncidencia();
//            ArrayList<String> arrSetBtn = new ArrayList<String>();
//            arrSetBtn.add("admin user");
//            arrSetBtn.add("admin profile");
//            arrSetBtn.add("add incidencia");
//            arrSetBtn.add("show incidencia");
//            arrSetBtn.add("reporte");
//            arrSetBtn.add("salir");
//            incidencia.setArrMenuBtn(arrSetBtn);
//
//            ArrayList<String> arrGetBtn = new ArrayList<String>();
//            arrGetBtn = incidencia.getArrMenuBtn();
//            System.out.println("vamos a imprimir los botones");
//            Iterator<String> it = arrGetBtn.iterator();
//            while (it.hasNext()) {
//                System.out.println("boton : " + it.next());
//            }
            /**
             * PUNTOS DE LEALTAD
             */
//            
            //QUITAR PUNTOS POR PROMOSIÓN
//            getPuntosConsumidosPromocion(oConn);            
//
            /**
             * PUNTOS DE LEALTAD
             */
//            Fechas fec = new Fechas();
//            String strFechamocha = "20180201";
//            String strFechamocha_tmp = "";
//
//            for(int i = 1; i <= 24 ; i++){
////                System.out.println("i: " + i);
//                strFechamocha_tmp = fec.addFecha(strFechamocha, 2, i);
//                System.out.println("fecha: " + strFechamocha_tmp);  
//                strFechamocha_tmp = fec.addFecha(strFechamocha, 2, -i);
//                System.out.println("fecha: " + strFechamocha_tmp);                
//            }
//            strFechamocha_tmp = "";
//            System.out.println(":v");
//            
//            for(int i = 1; i <= 12 ; i++){
////                System.out.println("i: " + i);
//                strFechamocha_tmp = fec.addFecha(strFechamocha, 2, -i);
//                System.out.println("fecha: " + strFechamocha_tmp);                
//            }
//            String strFechOK1 = fec.addFecha(strFechamocha, 2, 3);
//            String strFechOK2 = fec.addFecha(strFechamocha, 2, -1);
//            System.out.println(" fecha " + strFechOK1);
//            System.out.println(" fecha " + strFechOK2);
            /**
             * ex participantes
             */
//            
            ArrayList<String> listaCTID = new ArrayList<String>();
            int intmeh = 0;
            String strsql = "select DISTINCT CT_ID from view_ventasglobales where FAC_ANULADA = 0 and CANCEL = 0 and FAC_PAGADO = 1 and FAC_VALIDA = 1 and FAC_FECHA >= '20171101';";
            ResultSet rs = oConn.runQuery(strsql, true);
//
            while (rs.next()) {
                intmeh++;
//                System.out.println("CT_ID: " + rs.getString("CT_ID"));
                String strSql2 = "select CT_ID, CT_ES_PROSPECTO, CT_RAZONSOCIAL from vta_cliente where  CT_ACTIVO = 1 and CT_ES_PROSPECTO = 1 and CT_ID = " + rs.getString("CT_ID");
                ResultSet rs2 = oConn.runQuery(strSql2, true);
                while (rs2.next()) {
                    System.out.println(" es prospecto y debe de ser ex participante: " + rs2.getString("CT_ID"));
                    listaCTID.add(rs2.getString("CT_ID"));
                }
                rs2.close();

            }
            rs.close();
//
            System.out.println(" cuantos: " + intmeh);
            System.out.println(" cuantosArr: " + listaCTID.size());
            System.out.println(" cuales: " + listaCTID);
//            int intCiclo = 0;
//            String strCadena = "";
//            String strSql = "select CT_ID, CT_RAZONSOCIAL, CT_CLAVE_DDBB from vta_cliente where CT_ACTIVO = 1 and CT_ES_PROSPECTO = 0 limit 100";
//            boolean bolExiste = false;
//            ResultSet rs = oConn.runQuery(strSql, true);
//            while (rs.next()) {
//                if(intCiclo >= 1){
//                    strCadena += ",";
//                }
//                strCadena += rs.getString("CT_ID");
//                intCiclo++;
//            }
//            rs.close();
//
//            String strSql2 = "select * from view_ventasglobales where CT_ID in (" + strCadena + ") and left(FAC_FECHA,4) = '2017'";
//            ResultSet rs2 = oConn.runQuery(strSql2, true);
//            while (rs2.next()) {
//                bolExiste = true;
//            }
//            rs2.close();
//
//            System.out.println("Clientes sin ventas: " + strCadena);
//            Cofide_LeerXLSXMeta xls = new Cofide_LeerXLSXMeta(oConn);
//            xls.importaXLSXMetas("C:\\Users\\Desarrollo_COFIDE\\Desktop\\test.xls");
//            COFIDE_ReadXls xls = new COFIDE_ReadXls(oConn);
//            xls.readXLS("C:\\Users\\Desarrollo_COFIDE\\Desktop\\test.xls");
            /**
             * ex participantes
             */
//            System.out.println(new Telemarketing().getParticipantes(oConn, "1412"));
//            ArrayList<String> arreglotime = new ArrayList<String>();
//            arreglotime.add("julio");
//            arreglotime.add("cesar");
//            arreglotime.add("chavez");
//            arreglotime.add("mondragon");
//
//            System.out.println("general : " + arreglotime);
//            System.out.println("tamaño : " + arreglotime.size());
//            for (int i = 0; i < arreglotime.size(); i++) {
//                System.out.println(":v : " + arreglotime.get(i));
//            }
////            System.out.println(":v : "  + arreglotime.get(1));
////            System.out.println(":v : "  + arreglotime.get(2));
////            System.out.println(":v : "  + arreglotime.get(3));
//            System.out.println("posicipon : " + arreglotime.indexOf("chavez"));
//           
//            
//            System.out.println("iterando ando");
//            Iterator<String> it = arreglotime.iterator();
//            while(it.hasNext()){
//                System.out.println("iterando : " + it.next());
//            }
//            Scanner sc = new Scanner(System.in);
//            String strNombre = "";
//            String strTelefono = "";
//            String strCorreo = "";
//            ArrayList<String> persona = new ArrayList<String>();
//            ArrayList<ArrayList<String>> personas = new ArrayList<ArrayList<String>>();
//            System.out.println("Ingresa los valores: ");
//            System.out.println("nombre: ");
//            strNombre = sc.next();
//            System.out.println("telefono: ");
//            strTelefono = sc.next();
//            System.out.println("correo: ");
//            strCorreo = sc.next();
//            
//            persona.add(strNombre);
//            persona.add(strTelefono);
//            persona.add(strCorreo);
//            
//            personas.add(persona);
//            
//            System.out.println("hay " + persona.size() + " datos registradas");
//            System.out.println("hay " + personas.size() + " personas registradas");
//            
//            System.out.println("persona " + personas.get(0).get(0)+ " " + personas.get(0).get(1)+ " " + personas.get(0).get(2));
//            
//            
//            String strConsulta = "select * from calcula_PuntosLealtad;";
//            try {
//                ResultSet rs = oConn.runQuery(strConsulta, true);
//                while (rs.next()) {
//                    setSumaCorrectaPuntos(oConn, rs.getString("CONTACTO_ID"));
////                    setPuntosPromocion(oConn, rs.getString("CONTACTO_ID"));
//                }
//                rs.close();
//            } catch (SQLException ex) {
//                System.out.println("EXCEPTION SQL: " + ex.getLocalizedMessage());
//            }
//            setPuntosLealtadCofide(oConn); //*1 llenar tabla calcula_PuntosLealtad*/
//setPuntosIniciales(oConn); //ultimo paso
//
            oConn.close();
        } catch (Exception ex) {
            Logger.getLogger(testClasses1.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

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

//    public boolean EsPromo(Conexion oConn, String strIdVta) {
//        boolean bolResp = false;
//        String strSql = "";
//        String strTipoVta = "";
//        String strPromo = "";
//        String strIdCurso = "";
//        try {
//            strSql = "select * from view_ventasglobales where fac_id = " + strIdVta;
//            ResultSet rs = oConn.runQuery(strSql, true);
//            while (rs.next()) {
//                strTipoVta = rs.getString("TIPO_DOC");
//                strPromo = rs.getString("FAC_PROMO");
//                strIdCurso = rs.getString("CC_CURSO_ID");
//                bolResp = true;
//                System.out.println("################### es promoción?");
//            }
//            rs.close();
//            if (bolResp && strPromo.equals("1")) {
//                System.out.println("################### va a recuperar los puntos de la promoción");
//                bolResp = RestartPointsPromo(oConn, strIdVta, strTipoVta, strIdCurso);
//            }
//        } catch (SQLException sql) {
//            System.out.println("ERROR: al recuperar la información de la venta... " + sql.getMessage());
//        }
//        return bolResp;
//    }
//
//    public boolean RestartPointsPromo(Conexion oConn, String strIdVta, String strTipoVta, String strIdCurso) {
//        boolean bolResp = false;
//        String strSql = "select CONTACTO_ID from cofide_participantes where ";
//        String strContactoID = "";
//
//        if (strTipoVta.equals("1")) { //factura
//            strSql += " CP_FAC_ID = " + strIdVta;
//        } else { //ticket
//            strSql += " CP_TKT_ID = " + strIdVta;
//        }
//        try {
//            ResultSet rs = oConn.runQuery(strSql, true);
//            while (rs.next()) {
//                strContactoID = rs.getString("CONTACTO_ID");
//                System.out.println("################### recupera al participante");
//            }
//            rs.close();
//            strSql = "update cofide_puntos_contacto set CPC_ACTIVO = 1, CC_CURSO_ID = 0 where CONTACTO_ID = " + strContactoID + " and CC_CURSO_ID = " + strIdCurso;
//            oConn.runQueryLMD(strSql);
//            if (!oConn.isBolEsError()) {
//                System.out.println("################### recupero sus puntos con exito");
//                bolResp = true;
//            }
//        } catch (SQLException sql) {
//            System.out.println("ERROR: al recuperar puntos consumidos en la promoción... " + sql.getMessage());
//        }
//        return bolResp;
//    }
    public static String quitSigns(String strCadena) {
        strCadena = strCadena.trim();
        strCadena = strCadena.toLowerCase();
        strCadena = strCadena.replace(")", "").replace("(", "").replace(" de ", "-").replace(" y ", "-").replace(" para ", "-").replace(" con ", "-").replace(" el ", "-");
        strCadena = strCadena.replace(" ", "-").replace(".", "-").replace(",", "-");
        strCadena = strCadena.trim();
        // Normalizar texto para eliminar acentos, dieresis, codillas, y tildes

        strCadena = strCadena.replace("--", "-");
        strCadena = Normalizer.normalize(strCadena, Normalizer.Form.NFD);
        // Quitamos elementos ASCII
        strCadena = strCadena.replaceAll("[^\\p{ASCII}(N\u0303)(n\u0303)(\u00A1)(\u00BF)(\u00B0)(U\u0308)(u\u0308)]", "");
        // Normalizar nuevamente para evitar simbolos
        strCadena = Normalizer.normalize(strCadena, Normalizer.Form.NFD);

        return strCadena;
    }

    /*
    Metodo para calcular los puntos de acuerdo a las ventas registradas
     */
    public static void setPuntosLealtadCofide(Conexion oConn) {
        boolean blSeminario;
        String strSql = "";

        strSql = "select CONTACTO_ID,CP_ID_CURSO,CP_NOMBRE,CP_FAC_ID,CP_TKT_ID,"
                + "(IFNULL((select COUNT(*) from cofide_modulo_curso where cofide_modulo_curso.CC_CURSO_IDD = cofide_participantes.CP_ID_CURSO group by CC_CURSO_IDD),0)) as NumModulos,"
                + "IF((select CC_CURSO_ID from cofide_cursos where (CC_IS_DIPLOMADO = 1 or CC_IS_SEMINARIO = 1) and CC_CURSO_ID = cofide_participantes.CP_ID_CURSO limit 1),\"SI\",\"NO\") as ES_DIPSEM,"
                + "fac_anulada, cancel "
                + "from cofide_participantes  "
                + "LEFT JOIN view_ventasglobales on "
                + "IF(CP_TKT_ID = 0,view_ventasglobales.FAC_ID = cofide_participantes.CP_FAC_ID,view_ventasglobales.FAC_ID = cofide_participantes.CP_TKT_ID) "
                + "where view_ventasglobales.FAC_ANULADA = 0 "
                + "and view_ventasglobales.CANCEL = 0 "
                + "and CP_TIPO_CURSO = 1 "
                + "and view_ventasglobales.FAC_PROMO = 0 "
                + "and view_ventasglobales.FAC_ES_RESERVACION = 0 " //no contar las reservaciones
                + "and (CP_TKT_ID <> 0 or CP_FAC_ID <> 0) ";

        try {

            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                if (rs.getString("ES_DIPSEM").equals("SI")) {
                    blSeminario = true;
                } else {
                    blSeminario = false;
                }
                /*1*/
                sumPuntosLealtadCofide(oConn, rs.getString("CONTACTO_ID"), blSeminario, rs.getInt("NumModulos"), "PUNTOS_CALCULADOS");
                /*2*/
                setPuntosPromocion(oConn, rs.getString("CONTACTO_ID"));
            }
            rs.close();
            //AGREGA LOS 200 PUNTOS INICIALES
            strSql = "update calcula_PuntosLealtad set PUNTOS_CALCULADOS = (PUNTOS_CALCULADOS + 200);";
            oConn.runQueryLMD(strSql);
            if (oConn.isBolEsError()) {
                System.out.println("OCURRIO UN PROBLEMA AL ACTUALIZAR LOS PUNTOS CALCULADOS CON BASE A LAS VENTAS: [ " + oConn.getStrMsgError() + " ]");
            }
        } catch (SQLException ex) {
            System.out.println("Error SQL[setPuntosLealtadCofide]: " + ex.getLocalizedMessage());
        }

    }//Fin setPuntosLealtadCofide

    /*
    2.- Itera en todas las ventas y va sumando los puntos de acuerdo al numero de ventas que se obtuvieron
     */
    public static void sumPuntosLealtadCofide(Conexion oConn, String strContactoId, boolean blSeminario, int intNumMod, String strCampoSuma) {
        String strSql = "select " + strCampoSuma + " from calcula_PuntosLealtad where CONTACTO_ID = " + strContactoId;
        int intTotalPuntos = 0;
        boolean blExistenPtos = false;
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                intTotalPuntos = rs.getInt(strCampoSuma);
                blExistenPtos = true;
            }
            rs.close();
            int intPuntos = 0;
            if (blSeminario) {
                intPuntos = intTotalPuntos + (200 * intNumMod);
            } else {
                intPuntos = intTotalPuntos + 200;
            }
            String strTrans = "";
            if (blExistenPtos) {
                strTrans = "update calcula_PuntosLealtad set " + strCampoSuma + " = " + intPuntos + " where CONTACTO_ID = " + strContactoId;
            } else {
                strTrans = "insert into calcula_PuntosLealtad "
                        + "(CONTACTO_ID, PUNTOS_CALCULADOS, PUNTOS_HISTORIAL) "
                        + "values (" + strContactoId + "," + intPuntos + ", 0)";
            }

            oConn.runQueryLMD(strTrans);

//            setPuntosPromocion(oConn, strContactoId);
        } catch (SQLException ex) {
            System.out.println("Error SQL[setPuntosLealtadCofide]: " + ex.getLocalizedMessage());
        }

    }//Fin sumPuntosLealtadCofide

    /*
    Suma los puntos que se han registrado en la tabla de puntos de COFIDE
     */
    public static void setPuntosPromocion(Conexion oConn, String strContactoId) {
        int intPuntos = 0;
        String strSql = "select SUM(CPC_PUNTOS) as SUMA from cofide_puntos_contacto where CPC_ACTIVO = 1 and CONTACTO_ID = " + strContactoId;
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                intPuntos = rs.getInt("SUMA");
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("ERROR[setPuntosPromocion]: " + ex.getLocalizedMessage());
        }
        String strUpdate = "update calcula_PuntosLealtad set PUNTOS_HISTORIAL = " + intPuntos + " where CONTACTO_ID = " + strContactoId;

        oConn.runQueryLMD(strUpdate);
    }//Fin setPuntosPromocion

    /*
    Inserta en la tabla de puntos, los puntos correctos de acuerdo al primer calculo con base en las ventas obtenidas por el cliente
     */
    public static void setSumaCorrectaPuntos(Conexion oConn, String strContactoId) {
        int intPtos = 0;
        String strConsulta = "select PUNTOS_CALCULADOS from calcula_PuntosLealtad where CONTACTO_ID = " + strContactoId;
        try {
            ResultSet rs = oConn.runQuery(strConsulta, true);
            while (rs.next()) {
                intPtos = rs.getInt("PUNTOS_CALCULADOS");

                int intCont = 0;
                intCont = intPtos / 200;

                for (int i = 0; i < intCont; i++) {
                    String strinst = "insert into cofide_puntos_contacto (CPC_PUNTOS,CC_CURSO_ID,CPC_ACTIVO,CONTACTO_ID) values (200,0,1," + strContactoId + ");";
                    oConn.runQueryLMD(strinst);
                }
            }
            rs.close();

        } catch (SQLException ex) {
            System.out.println("ERROR [setSumaCorrectaPuntos]: " + ex.getLocalizedMessage());
        }
    }//Fin setSumaCorrectaPuntos

    //Asigna 200 puntos iniciales
    public static void setPuntosIniciales(Conexion oConn) {
        String strSql = "", strSqlLDM = "";
        ResultSet rs;
        //obtiene los CONTACTO_ID que no tienen puntos asignados
        strSql = "select * from cofide_contactos where (select count(cofide_puntos_contacto.CONTACTO_ID)  from cofide_puntos_contacto where cofide_puntos_contacto.CONTACTO_ID = cofide_contactos.CONTACTO_ID)  = 0";
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strSqlLDM = "INSERT INTO cofide_puntos_contacto (CPC_PUNTOS, CC_CURSO_ID, CPC_ACTIVO, CONTACTO_ID) VALUES (200,0,1," + rs.getString("CONTACTO_ID") + ")";
                oConn.runQueryLMD(strSqlLDM);
                if (oConn.isBolEsError()) {
                    System.out.println("Ocurrio un problema al agregar puntos iniciales :( " + oConn.getStrMsgError() + " / " + rs.getString("CONTACTO_ID"));
                }
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println("error: " + sql.getMessage());
        }

    }//Asigna 200 puntos iniciales

    //QUITAR PUNTOS POR CURSOS TOMADOS POR PROMOSIÓN
    public static void getPuntosConsumidosPromocion(Conexion oConn) {

        ArrayList<String> arrIdTKT = new ArrayList<String>();
        String strIdTKT = "";

        String strSql = "select * from view_ventasglobales v, "
                + "view_ventasglobalesdeta vd "
                + "where v.FAC_ID = vd.FAC_ID "
                + "and v.TIPO_DOC = vd.TIPO_DOC "
                + "and FACD_TIPO_CURSO = 1 "
                + "and FAC_ANULADA = 0 "
                + "and CANCEL = 0 "
                + "and FAC_FECHA >= '20170501' "
                + "and FAC_PROMO = 1;";
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                arrIdTKT.add(rs.getString("FAC_ID"));
            }
            rs.close();

            Iterator<String> it = arrIdTKT.iterator();
            while (it.hasNext()) {

                strIdTKT += it.next();
                strIdTKT += ",";
            }

            strIdTKT = strIdTKT.substring(0, strIdTKT.length() - 1);

            strSql = "select CONTACTO_ID, CP_ID_CURSO from cofide_participantes where CP_TKT_ID in (" + strIdTKT + ")";
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {

                String strSQLUpdate = "update cofide_puntos_contacto set CPC_ACTIVO = 0, CC_CURSO_ID = " + rs.getString("CP_ID_CURSO") + " where CONTACTO_ID = " + rs.getString("CONTACTO_ID") + " limit 5";
//                System.out.println("query: " + strSQLUpdate);
                oConn.runQueryLMD(strSql);
                if (oConn.isBolEsError()) {
                    System.out.println("ERROR: [ " + oConn.getStrMsgError() + " ]");
                }

            }
            rs.close();

        } catch (SQLException sql) {
            System.out.println("Error [ " + sql.getMessage() + " ]");
        }

    }//QUITAR PUNTOS POR CURSOS TOMADOS POR PROMOSIÓN

}
