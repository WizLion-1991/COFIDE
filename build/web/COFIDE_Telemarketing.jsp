<%-- 
    Document   : COFIDE_Telemarketing
    Created on : 8/12/2015, 04:59:36 PM
    Author     : COFIDE
--%>

<%@page import="com.mx.siweb.erp.especiales.cofide.entidades.CofideCursoInteres"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.SincronizarPaginaWeb"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.entidades.VtaRazonsocialTMP"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.entidades.VtaContactoTMP"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.entidades.VtaCteTMP"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.cofide_pausa"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.entidades.CofideCampaniasLlamadas"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.entidades.CofideCampanias"%>
<%@page import="javax.swing.JOptionPane"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.COFIDE_Mail_cursos"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.LlamadaHistorial"%>
<%@page import="Tablas.CofideLlamadas"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.LlamadasPBX"%>
<%@page import="com.siweb.utilerias.json.JSONObject"%>
<%@page import="com.siweb.utilerias.json.JSONArray"%>
<%@page import="com.google.common.base.Ascii"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.Telemarketing"%>
<%@page import="java.io.Console"%>
<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="Tablas.cofide_contactos"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="Tablas.crm_eventos"%>
<%@page import="Tablas.vta_cliente"%>
<%@page import="comSIWeb.Operaciones.CIP_Tabla"%>
<%@page import="comSIWeb.Utilerias.UtilXml"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="comSIWeb.ContextoApt.atrJSP"%>
<%@page import="comSIWeb.ContextoApt.Seguridad"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="comSIWeb.ContextoApt.VariableSession"%>

<%
    /**
     * atrJSP.atrJSP(request, response, true, false);//Definimos atributos para
     * el HTML atrJSP.atrJSP(request, response, true, true);//Definimos
     * atributos para el XML
     */

    /*Obtenemos las variables de sesion*/
    VariableSession varSesiones = new VariableSession(request);
    varSesiones.getVars();
    //Abrimos la conexion
    Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
    oConn.open();
    //Instanciamos objeto de seguridad para no permitir el acceso externo a ver los XML
    Seguridad seg = new Seguridad();//Valida que la peticion se halla hecho desde el mismo sitio
    UtilXml util = new UtilXml();
    Fechas fec = new Fechas();
    LlamadasPBX llama = new LlamadasPBX();
    LlamadaHistorial lh = new LlamadaHistorial();
    Telemarketing tele = new Telemarketing();
    int intTimeOut = 15000; // tiempo de espera para que la llamada hacia el cliente sea atendida
    String strRes = "";

    if (varSesiones.getIntNoUser() != 0 && seg.ValidaURL(request)) {
        String strid = request.getParameter("ID");
        if (strid != null) {
            if (strid.equals("1")) { //llenado de vta_cliente
                String strCteManual = request.getParameter("cte_manual");//Cliente buscado manualmente
                String strCodigoBase = "";
                if (request.getParameter("strCodigoBase") != null) {
                    if (!request.getParameter("strCodigoBase").isEmpty()) {
                        strCodigoBase = request.getParameter("strCodigoBase");
                    }
                }
                System.out.println("BASE EJECUTIVO ...... " + strCodigoBase);
                boolean bolCteManual = false; // si es falso, es el flujo de la agenda, en caso contrario mostrara el registor citado con el ID
                String strSql = "", strSqlVta = "";
                ResultSet rs, rscrm;
                String strFiltro = "";

                int intCT_ID = 0;
                String strCT_RAZONSOCIAL = "";
                String strCT_NUMERO = "";
                String strCT_RFC = "";
                String strCT_TELEFONO1 = "";
                String strCT_TELEFONO2 = "";
                String strCT_EMAIL1 = "";
                String strCT_EMAIL2 = "";
                String strCT_CP = "";
                String strCT_COL = "";
                String strCT_CALLE = "";
                String strCT_EDO = "";
                String strCT_MUNI = "";
                String strCT_NUM = "";
                String strCT_AREA = "";
                String strCT_GIRO = "";
                String strCT_SEDE = "";
                String strCT_COMENTARIO = "";
                String strCT_CONMUTADOR = "";
                String strCT_CONTACTO = "";
                String strCT_CLAVEDDBB = "";
                String strCT_AAA = "";
                String strIdMedioPublicidad = "";
                String strXML_test = "";
                int intIdCampaniaUso = 0;
                int intCte_Prosp = 0;
                int intEnvioMail = 0;

                boolean bolHrExacta = false; //registro de la hr exacta
                boolean bolResagadoHoy = false; //resagados de dia de hoy
                boolean bolResagado = false; //resagados de dias pasados
                boolean bolProsp = false; //prospecto
                boolean bolDesdeCampania = false; // entra a campaña
                boolean bolCampania = false; //obtuvo resultados la campaña

                String strExtencion = "";
                boolean bolRes = false; //respuesta del pbx
                String strIdLlamadaPBX = ""; //id de la lamada del pbx
                String strHora = fec.getHoraActual();
                String strFecha = fec.getFechaActual();
                if (!strCodigoBase.equals("")) { //si no tienes ninguna base asignada para poder abrir el registro
                    if (!isUserInBound(oConn, varSesiones.getIntNoUser())) { //obtenemos si es con base InBound para acompletar el filtro
                        strFiltro = " and CT_CLAVE_DDBB = '" + strCodigoBase + "'";
                    }
                }
                //obtiene la extension del ejecutivo
                strSql = "select NUM_EXT, COFIDE_CODIGO from usuarios where id_usuarios = " + varSesiones.getIntNoUser();
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strExtencion = rs.getString("NUM_EXT"); //obtenemos la extencion del usuario
                }
                rs.close();
                //valida si es manual o viene de la agenda
                if (strCteManual != null) { // es una busqueda manual
                    System.out.println("agenda manual");
                    bolCteManual = true; // es manual y no hara la llamada
                    strSql = "select * from vta_cliente where  CT_ID = " + strCteManual + strFiltro;
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        intCT_ID = rs.getInt("CT_ID"); //id cliente de vta_clientes
                        strCT_RAZONSOCIAL = rs.getString("CT_RAZONSOCIAL");
                        strCT_EMAIL1 = rs.getString("CT_EMAIL1");
                        strCT_EMAIL2 = rs.getString("CT_EMAIL2");
                        strCT_NUMERO = rs.getString("CT_NUMERO");
                        strCT_RFC = rs.getString("CT_RFC");
                        strCT_TELEFONO1 = rs.getString("CT_TELEFONO1");
                        strCT_TELEFONO2 = rs.getString("CT_TELEFONO2");
                        strCT_CP = rs.getString("CT_CP");
                        strCT_CALLE = rs.getString("CT_CALLE");
                        strCT_EDO = rs.getString("CT_ESTADO");
                        strCT_MUNI = rs.getString("CT_MUNICIPIO");
                        strCT_COL = rs.getString("CT_COLONIA");
                        strCT_SEDE = rs.getString("CT_SEDE");
                        strCT_GIRO = rs.getString("CT_GIRO");
                        strCT_AREA = rs.getString("CT_AREA");
                        intEnvioMail = rs.getInt("CT_MAILMES");
                        intCte_Prosp = rs.getInt("CT_ES_PROSPECTO");
                        strCT_CONMUTADOR = rs.getString("CT_CONMUTADOR");
                        strCT_CONTACTO = rs.getString("CT_CONTACTO1");
                        strCT_CLAVEDDBB = rs.getString("CT_CLAVE_DDBB");
                        strCT_AAA = rs.getString("CT_AAA");
                        strIdMedioPublicidad = rs.getString("CT_MKT");

                        String StrSqlComent = "select EV_ASUNTO from crm_eventos where EV_ESTADO = 1 and EV_CT_ID = " + strCteManual + " limit 1";
                        ResultSet rsComent = oConn.runQuery(StrSqlComent, true);
                        while (rsComent.next()) {
                            strCT_COMENTARIO = rsComent.getString("EV_ASUNTO");
                        }
                        rsComent.close();
                    }
                    rs.close();
                    System.out.println("----------------------------------------------------------------------------comentario del cliente " + strCT_COMENTARIO);
                } else { //  agenda progresiva
                    System.out.println("agenda progresiva");
                    //agenda de hr exacta
                    strSql = "select EV_ID, EV_CT_ID, EV_ASUNTO from crm_eventos where EV_FECHA_INICIO = '" + strFecha + "' "
                            + "and EV_HORA_INICIO = '" + strHora + "' and EV_ESTADO = 1 and EV_ASIGNADO_A = " + varSesiones.getIntNoUser() + " "
                            + "order by EV_HORA_INICIO desc limit 1";
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) { //crm eventos de la hora exacta
                        System.out.println("agenda de hr actual");
//                        bolHrExacta = true; // si tomo registro de hr exacta
                        intCT_ID = rs.getInt("EV_CT_ID");
                        //Obtenemos datos del cliente
                        strSqlVta = "select * from vta_cliente where  CT_ID = " + intCT_ID + " and CT_CLAVE_DDBB = '" + strCodigoBase + "' and CT_ACTIVO = 1";
                        rscrm = oConn.runQuery(strSqlVta, true);
                        while (rscrm.next()) { //informacion del cliente de la agenda exacta
                            bolHrExacta = true; // si tomo registro de hr exacta
                            System.out.println("si hay registro de la agenda de hr actual");
                            strCT_COMENTARIO = rs.getString("EV_ASUNTO"); //se llena si viene resultado de la consulta
                            intCT_ID = rscrm.getInt("CT_ID"); //id cliente de vta_clientes
                            strCT_RAZONSOCIAL = rscrm.getString("CT_RAZONSOCIAL");
                            strCT_EMAIL1 = rscrm.getString("CT_EMAIL1");
                            strCT_EMAIL2 = rscrm.getString("CT_EMAIL2");
                            strCT_NUMERO = rscrm.getString("CT_NUMERO");
                            strCT_RFC = rscrm.getString("CT_RFC");
                            strCT_TELEFONO1 = rscrm.getString("CT_TELEFONO1");
                            strCT_TELEFONO2 = rscrm.getString("CT_TELEFONO2");
                            strCT_CP = rscrm.getString("CT_CP");
                            strCT_CALLE = rscrm.getString("CT_CALLE");
                            strCT_EDO = rscrm.getString("CT_ESTADO");
                            strCT_MUNI = rscrm.getString("CT_MUNICIPIO");
                            strCT_COL = rscrm.getString("CT_COLONIA");
                            strCT_SEDE = rscrm.getString("CT_SEDE");
                            strCT_GIRO = rscrm.getString("CT_GIRO");
                            strCT_AREA = rscrm.getString("CT_AREA");
                            intEnvioMail = rscrm.getInt("CT_MAILMES");
                            intCte_Prosp = rscrm.getInt("CT_ES_PROSPECTO");
                            strCT_CONMUTADOR = rscrm.getString("CT_CONMUTADOR");
                            strCT_CONTACTO = rscrm.getString("CT_CONTACTO1");
                            strCT_AAA = rscrm.getString("CT_AAA");
                            strIdMedioPublicidad = rscrm.getString("CT_MKT");
                        }
                        rscrm.close();
                        System.out.println("----------------------------------------------------------------------------comentario del cliente " + strCT_COMENTARIO);
                    }
                    rs.close(); //termina la consulta del registro exatco, de la hora y dia exacta
                    if (!bolHrExacta) { //si es falsa la hr exacta, entra para ver campaña
                        //campaña
                        CofideCampanias campania = getCampaniaFiltro(oConn); //Revisa si hay campanias activas
                        if (campania != null) {
                            System.out.println("encontro una campaña");
                            bolDesdeCampania = true; // si hay campaña
                            String strFiltro_1 = "";
                            strSql = "select * from vta_cliente where CT_CLAVE_DDBB = '" + strCodigoBase + "' and CT_ACTIVO = 1"
                                    + " and (select COUNT(*) from cofide_campanias_llamadas where cofide_campanias_llamadas.CT_ID = vta_cliente.CT_ID "
                                    + " AND cofide_campanias_llamadas.CAMP_ID = " + campania.getIntIdCampania() + ") = 0 ";
                            if (campania.getStrArea() != null) {
//                                strSql += " and CT_AREA = '" + campania.getStrArea() + "' ";
                                strFiltro_1 += " and (CT_AREA = '" + campania.getStrArea() + "' OR CT_ID in (select CT_ID from cofide_contactos where CCO_AREA = '" + campania.getStrArea() + "'))";
                            }
                            if (campania.getStrGiro() != null) {
                                strFiltro_1 += " and (CT_GIRO = '" + campania.getStrGiro() + "' )";
                            }
                            if (campania.getStrSede() != null) {
                                strFiltro_1 += " and (CT_SEDE = '" + campania.getStrSede() + "' )";
                            }
                            if (campania.getIntProspecto() == 0 && campania.getIntExParticipante() == 1) {
                                strFiltro_1 += " and (CT_ES_PROSPECTO = 0 )";
                            }
                            if (campania.getIntProspecto() == 1 && campania.getIntExParticipante() == 0) {
                                strFiltro_1 += " and (CT_ES_PROSPECTO = 1 )";
                            }

                            if (campania.getIntPuntosLealtad() != 0) {
                                strFiltro_1 = " and (CT_ID in (" + getStrCtIdsPuntos(oConn, campania.getIntPuntosLealtad(), strCodigoBase) + " ))";
                            }
                            strSql = strSql + strFiltro_1;

                            strSql += " order by CT_ES_PROSPECTO DESC, CT_ID ASC limit 1";

                            rs = oConn.runQuery(strSql, true);
                            while (rs.next()) {
                                System.out.println("entro a la campaña y tomo registro de la campaña");
                                if (bolDesdeCampania) {
                                    //Guardar bitacora de llamada...
                                    intIdCampaniaUso = campania.getIntIdCampania();
                                    saveCampaniaLlamada(oConn, campania, varSesiones.getIntNoUser(), rs.getInt("CT_ID"));// en agenda
                                }
                                bolCampania = true; //la campaña esta activa
                                intCT_ID = rs.getInt("CT_ID"); //id cliente de vta_clientes
                                strCT_RAZONSOCIAL = rs.getString("CT_RAZONSOCIAL");
                                strCT_EMAIL1 = rs.getString("CT_EMAIL1");
                                strCT_EMAIL2 = rs.getString("CT_EMAIL2");
                                strCT_NUMERO = rs.getString("CT_NUMERO");
                                strCT_RFC = rs.getString("CT_RFC");
                                strCT_TELEFONO1 = rs.getString("CT_TELEFONO1");
                                strCT_TELEFONO2 = rs.getString("CT_TELEFONO2");
                                strCT_CP = rs.getString("CT_CP");
                                strCT_CALLE = rs.getString("CT_CALLE");
                                strCT_EDO = rs.getString("CT_ESTADO");
                                strCT_MUNI = rs.getString("CT_MUNICIPIO");
                                strCT_COL = rs.getString("CT_COLONIA");
                                strCT_SEDE = rs.getString("CT_SEDE");
                                strCT_GIRO = rs.getString("CT_GIRO");
                                strCT_AREA = rs.getString("CT_AREA");
                                intEnvioMail = rs.getInt("CT_MAILMES");
                                intCte_Prosp = rs.getInt("CT_ES_PROSPECTO");
                                strCT_CONMUTADOR = rs.getString("CT_CONMUTADOR");
                                strCT_CONTACTO = rs.getString("CT_CONTACTO1");
                                strCT_AAA = rs.getString("CT_AAA");
                                strIdMedioPublicidad = rs.getString("CT_MKT");
                            }
                            rs.close();
                        }
                        if (!bolCampania) { //si la campaña es falsa, entra a ver la agenda rezagada dle dia de hoy
                            //agenda rezagada hoy
                            strSql = "select EV_ID, EV_CT_ID, EV_ASUNTO from crm_eventos where EV_FECHA_INICIO = '" + strFecha + "' "
                                    + "and EV_HORA_INICIO <= '" + strHora + "' and EV_ESTADO = 1 and EV_ASIGNADO_A = " + varSesiones.getIntNoUser() + " "
                                    + "order by EV_FECHA_INICIO DESC ,EV_HORA_INICIO ASC limit 1";
//                                    + "order by ev_fecha_inicio desc limit 1";
                            rscrm = oConn.runQuery(strSql, true);
                            while (rscrm.next()) { //crm_evento
                                System.out.println("entro a rezagados del dia de hoy");
//                                bolResagadoHoy = true;
                                intCT_ID = rscrm.getInt("EV_CT_ID");
                                //Obtenemos datos del cliente
                                strSqlVta = "select * from vta_cliente where  CT_ID = " + intCT_ID + " and CT_CLAVE_DDBB = '" + strCodigoBase + "' and CT_ACTIVO = 1";
                                rs = oConn.runQuery(strSqlVta, true);
                                while (rs.next()) {
                                    bolResagadoHoy = true;
                                    System.out.println("encontro registro de agenda rezagada le dia de hoy");
                                    strCT_COMENTARIO = rscrm.getString("EV_ASUNTO"); //lo trae siempre y cuando venga la consulta desde el segundo query
                                    intCT_ID = rs.getInt("CT_ID"); //id cliente de vta_clientes
                                    strCT_RAZONSOCIAL = rs.getString("CT_RAZONSOCIAL");
                                    strCT_EMAIL1 = rs.getString("CT_EMAIL1");
                                    strCT_EMAIL2 = rs.getString("CT_EMAIL2");
                                    strCT_NUMERO = rs.getString("CT_NUMERO");
                                    strCT_RFC = rs.getString("CT_RFC");
                                    strCT_TELEFONO1 = rs.getString("CT_TELEFONO1");
                                    strCT_TELEFONO2 = rs.getString("CT_TELEFONO2");
                                    strCT_CP = rs.getString("CT_CP");
                                    strCT_CALLE = rs.getString("CT_CALLE");
                                    strCT_EDO = rs.getString("CT_ESTADO");
                                    strCT_MUNI = rs.getString("CT_MUNICIPIO");
                                    strCT_COL = rs.getString("CT_COLONIA");
                                    strCT_SEDE = rs.getString("CT_SEDE");
                                    strCT_GIRO = rs.getString("CT_GIRO");
                                    strCT_AREA = rs.getString("CT_AREA");
                                    intEnvioMail = rs.getInt("CT_MAILMES");
                                    intCte_Prosp = rs.getInt("CT_ES_PROSPECTO");
                                    strCT_CONMUTADOR = rs.getString("CT_CONMUTADOR");
                                    strCT_CONTACTO = rs.getString("CT_CONTACTO1");
                                    strCT_AAA = rs.getString("CT_AAA");
                                    strIdMedioPublicidad = rs.getString("CT_MKT");
                                }
                                rs.close();
                                System.out.println("----------------------------------------------------------------------------comentario del cliente " + strCT_COMENTARIO);
                            }
                            rscrm.close();
                            if (!bolResagadoHoy) {
                                //agenda rezagada dias pasados
                                strSql = "select EV_ID, EV_CT_ID, EV_ASUNTO from crm_eventos where EV_FECHA_INICIO < '" + strFecha + "' "
                                        + " and EV_ESTADO = 1 and EV_ASIGNADO_A = " + varSesiones.getIntNoUser() + " "
                                        + "order by EV_FECHA_INICIO desc, EV_HORA_INICIO desc limit 1";
                                rscrm = oConn.runQuery(strSql, true);
                                while (rscrm.next()) { //crm_evento
                                    System.out.println("entro a rezagados de dias pasados");
//                                    bolResagado = true;
                                    intCT_ID = rscrm.getInt("EV_CT_ID");
                                    //Obtenemos datos del cliente
                                    strSqlVta = "select * from vta_cliente where  CT_ID = " + intCT_ID + " and CT_CLAVE_DDBB = '" + strCodigoBase + "' and CT_ACTIVO = 1";
                                    rs = oConn.runQuery(strSqlVta, true);
                                    while (rs.next()) {
                                        bolResagado = true;
                                        System.out.println("encontro registro de rezagados de dias pasados");
                                        bolProsp = false;
                                        strCT_COMENTARIO = rscrm.getString("EV_ASUNTO"); //lo trae siempre y cuando venga la consulta desde el segundo query
                                        intCT_ID = rs.getInt("CT_ID"); //id cliente de vta_clientes
                                        strCT_RAZONSOCIAL = rs.getString("CT_RAZONSOCIAL");
                                        strCT_EMAIL1 = rs.getString("CT_EMAIL1");
                                        strCT_EMAIL2 = rs.getString("CT_EMAIL2");
                                        strCT_NUMERO = rs.getString("CT_NUMERO");
                                        strCT_RFC = rs.getString("CT_RFC");
                                        strCT_TELEFONO1 = rs.getString("CT_TELEFONO1");
                                        strCT_TELEFONO2 = rs.getString("CT_TELEFONO2");
                                        strCT_CP = rs.getString("CT_CP");
                                        strCT_CALLE = rs.getString("CT_CALLE");
                                        strCT_EDO = rs.getString("CT_ESTADO");
                                        strCT_MUNI = rs.getString("CT_MUNICIPIO");
                                        strCT_COL = rs.getString("CT_COLONIA");
                                        strCT_SEDE = rs.getString("CT_SEDE");
                                        strCT_GIRO = rs.getString("CT_GIRO");
                                        strCT_AREA = rs.getString("CT_AREA");
                                        intEnvioMail = rs.getInt("CT_MAILMES");
                                        intCte_Prosp = rs.getInt("CT_ES_PROSPECTO");
                                        strCT_CONMUTADOR = rs.getString("CT_CONMUTADOR");
                                        strCT_CONTACTO = rs.getString("CT_CONTACTO1");
                                        strCT_AAA = rs.getString("CT_AAA");
                                        strIdMedioPublicidad = rs.getString("CT_MKT");
                                    }
                                    rs.close();
                                    System.out.println("----------------------------------------------------------------------------comentario del cliente " + strCT_COMENTARIO);
                                }
                                rscrm.close();
                                if (!bolResagado) {
                                    System.out.println("entro a prospectos");
                                    //prospectos
                                    strSql = "select * from vta_cliente where CT_ES_PROSPECTO = 1 and CT_CLAVE_DDBB = '" + strCodigoBase + "' and CT_EXITOSOS = 0 and CT_ACTIVO = 1 "
                                            + "order by CT_ID limit 1";
                                    rs = oConn.runQuery(strSql, true);
                                    while (rs.next()) {
                                        System.out.println("encontro prospectos");
                                        intCT_ID = rs.getInt("CT_ID"); //id cliente de vta_clientes
                                        strCT_RAZONSOCIAL = rs.getString("CT_RAZONSOCIAL");
                                        strCT_EMAIL1 = rs.getString("CT_EMAIL1");
                                        strCT_EMAIL2 = rs.getString("CT_EMAIL2");
                                        strCT_NUMERO = rs.getString("CT_NUMERO");
                                        strCT_RFC = rs.getString("CT_RFC");
                                        strCT_TELEFONO1 = rs.getString("CT_TELEFONO1");
                                        strCT_TELEFONO2 = rs.getString("CT_TELEFONO2");
                                        strCT_CP = rs.getString("CT_CP");
                                        strCT_CALLE = rs.getString("CT_CALLE");
                                        strCT_EDO = rs.getString("CT_ESTADO");
                                        strCT_MUNI = rs.getString("CT_MUNICIPIO");
                                        strCT_COL = rs.getString("CT_COLONIA");
                                        strCT_SEDE = rs.getString("CT_SEDE");
                                        strCT_GIRO = rs.getString("CT_GIRO");
                                        strCT_AREA = rs.getString("CT_AREA");
                                        intEnvioMail = rs.getInt("CT_MAILMES");
                                        intCte_Prosp = rs.getInt("CT_ES_PROSPECTO");
                                        strCT_CONMUTADOR = rs.getString("CT_CONMUTADOR");
                                        strCT_CONTACTO = rs.getString("CT_CONTACTO1");
                                        strCT_AAA = rs.getString("CT_AAA");
                                        strIdMedioPublicidad = rs.getString("CT_MKT");
                                    }
                                    rs.close();
                                } // fin de prospectos
                            } //fin de rezagados de dias anteriroes
                        } // fin de rezagados del dia actual
                    } else { // fin de campaña
                        System.out.println("ya no hay registros en su DDBB: " + varSesiones.getIntNoUser() + " - " + varSesiones.getStrUser());
                    } //fin si ya no hay registros en la DDBB
                } // fin agenda de hr exacta y agenda progresiva
                if (!bolCteManual) { //si es falso, viene la llamada progesiva y si genera la llamada desde la agenda
                    //insert llamada en el registro para el historial
                    if (!strCT_TELEFONO1.equals("")) { // sí esta vacio el telefono, que no mande la llamada;
                        if (!strExtencion.equals("")) { //si la extencion esta vacia no realiza la llamada!
                            strIdLlamadaPBX = lh.GuardaLlamada(oConn, intCT_ID, fec.getFechaActual(), fec.getHoraActual(), varSesiones.getIntNoUser(), strExtencion, strCT_TELEFONO1);
                            bolRes = llama.generaNvaLlamada(oConn, strCT_TELEFONO1, strExtencion, intTimeOut, varSesiones.getIntSucursalDefault());
                        }
                    }
                }
                //actualiza al usuario de libre a ocupado, par retomar la siguiente llamada
                strSql = "update usuarios set US_LIBRE = 0 where IS_SUPERVISOR = 0 and id_usuarios = " + varSesiones.getIntNoUser();
                oConn.runQueryLMD(strSql); // ya no esta libre  y ya no le avisa al sueprvisor

                if (strIdMedioPublicidad.equals("")) {
                    strIdMedioPublicidad = "0";
                }

                //Enviamos el xml con los datos
                UtilXml utilXML = new UtilXml();
                String strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                strXML += "<vta>";
                strXML += "<datos "
                        + " CT_ID = \"" + intCT_ID + "\"  "
                        + " CT_RAZONSOCIAL = \"" + utilXML.Sustituye(strCT_RAZONSOCIAL) + "\"  "
                        + " CT_EMAIL1 = \"" + strCT_EMAIL1 + "\" "
                        + " CT_EMAIL2 = \"" + strCT_EMAIL2 + "\" "
                        + " CT_NUMERO = \"" + strCT_NUMERO + "\" "
                        + " CT_RFC = \"" + utilXML.Sustituye(strCT_RFC) + "\" "
                        + " CT_TELEFONO1 = \"" + strCT_TELEFONO1 + "\" "
                        + " CT_TELEFONO2 = \"" + strCT_TELEFONO2 + "\" "
                        + " bolBase = \"" + false + "\" "
                        + " CT_CP = \"" + strCT_CP + "\" "
                        + " CT_COL = \"" + strCT_COL + "\" "
                        + " CT_CALLE = \"" + utilXML.Sustituye(strCT_CALLE) + "\" "
                        + " CT_SEDE = \"" + strCT_SEDE + "\" "
                        + " CT_GIRO = \"" + strCT_GIRO + "\" "
                        + " CT_AREA = \"" + strCT_AREA + "\" "
                        + " CT_MUNI = \"" + strCT_MUNI + "\" "
                        + " CT_EDO = \"" + strCT_EDO + "\" "
                        + " HoraInicial = \"" + strHora + "\" "
                        + " Respuesta = \"" + bolRes + "\" "
                        + " Mensaje = \"" + llama.getStrRespuesta() + "\" "
                        + " id_llamada = \"" + strIdLlamadaPBX + "\" "
                        + " EV_ASUNTO = \"" + utilXML.Sustituye(strCT_COMENTARIO) + "\" "
                        + " envio_mail = \"" + intEnvioMail + "\" "
                        + " cte_prosp = \"" + intCte_Prosp + "\" "
                        + " CT_CONMUTADOR = \"" + strCT_CONMUTADOR + "\" "
                        + " CT_CONTACTO = \"" + utilXML.Sustituye(strCT_CONTACTO) + "\" "
                        + " CT_CLAVEDDBB = \"" + strCT_CLAVEDDBB + "\" "
                        + " Campania = \"" + intIdCampaniaUso + "\" "
                        + " CT_AAA = \"" + strCT_AAA + "\" "
                        + " CT_MKT = \"" + strIdMedioPublicidad + "\" "
                        + " />";
                strXML += "</vta>";
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } //fin de la primera opcion
            if (strid.equals("2")) { //valida los contactos con los correos de la lista negra
                String strResultado = "OK";
                cofide_contactos cco = new cofide_contactos();
                //recuperamos el lenght del arreglo para recuperar las cadenas del grid de contactos
                int intlength = Integer.parseInt(request.getParameter("length"));
                for (int i = 0; i < intlength; i++) {
                    //recuperamos el correso que esta en la fila i del grid
                    String strCorreo = request.getParameter("CCO_CORREO" + i);
                    //consulta si existe el correo en la lista negra
                    String strSql = "select CB_CT_CORREO from cofide_lista_negra where CB_CT_CORREO = '" + strCorreo + "'";
                    ResultSet rs = oConn.runQuery(strSql, true);
                    //sí, si existe el correo, va a regresar el correo que esta en la lista negra, si no, el resutado continua con su valor OK
                    while (rs.next()) { //valida correo en lista negra
                        if (strCorreo != null) {
                            strResultado = strCorreo;
                        }
                    }
                    rs.close();
                    String strSql1 = "select CCO_CORREO from cofide_contactos where CCO_CORREO = '" + strCorreo + "'";
                    ResultSet rs1 = oConn.runQuery(strSql1, true);
                    while (rs1.next()) { //valida correo que no este repetido
                        if (strCorreo != null) {
                            strResultado = strCorreo;
                        }
                    }
                    rs1.close();
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                strResultado = "OK," + cco.getValorKey();
                out.println(strResultado);//Pintamos el resultado
            }
            if (strid.equals("3")) { //guardar llamada y generar llamada al PBX

                int intCT_ID = Integer.valueOf(request.getParameter("CT_ID"));
                String strExt = ""; //extension del ejecutivo
                String strCT_CONTACTO = request.getParameter("CT_CONTACTO");

                //obtener extension
                String strSqlExt = "select NUM_EXT from usuarios where id_usuarios = " + varSesiones.getIntNoUser();
                ResultSet rs = oConn.runQuery(strSqlExt, true);
                while (rs.next()) {
                    strExt = rs.getString("NUM_EXT");
                }
                rs.close();
//PBX
                boolean bolRes = false;
                String strIdLlamadaPBX = "";
                //insert llamada en el registro para el historial
                if (!strCT_CONTACTO.equals("")) { // sí esta vacio el telefono, que no mande la llamada;
                    strIdLlamadaPBX = lh.GuardaLlamada(oConn, intCT_ID, fec.getFechaActual(), fec.getHoraActual(), varSesiones.getIntNoUser(), strExt, strCT_CONTACTO);
                    bolRes = llama.generaNvaLlamada(oConn, strCT_CONTACTO, strExt, intTimeOut, varSesiones.getIntSucursalDefault());
                }
                //mandamos a la pantalla los datos de la llamada realizada
                UtilXml utilXML = new UtilXml();
                String strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                strXML += "<vta>";
                strXML += "<datos "
                        + " Respuesta = \"" + bolRes + "\" "
                        + " Mensaje = \"" + llama.getStrRespuesta() + "\" "
                        + " id_llamada = \"" + strIdLlamadaPBX + "\" "
                        + " />";
                strXML += "</vta>";
                //PBX
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } //fin de la tercer opcionﬁ
            //Guardado del telemarketing
            if (strid.equals("4")) { //guarda lso datos de la pantalla de telemarketing
                String strResultado = "";
                //Recuperamos campos del post
                int intCT_ID = Integer.valueOf(request.getParameter("CT_ID"));
                String strCT_NO_CLIENTE = request.getParameter("CT_NO_CLIENTE");
                final String strRazonsocial = URLDecoder.decode(new String(request.getParameter("CT_RAZONSOCIAL").getBytes("iso-8859-1")), "UTF-8");
                final String strRfc = URLDecoder.decode(new String(request.getParameter("CT_RFC").getBytes("iso-8859-1")), "UTF-8");
                String strSede = request.getParameter("CT_SEDE");
                String strContacto = request.getParameter("CT_CONTACTO");
                String strContacto2 = request.getParameter("CT_CONTACTO2");
                String strCorreo = request.getParameter("CT_CORREO");
                String strCorreo2 = request.getParameter("CT_CORREO2");
                String strBolBase = request.getParameter("CT_BOLBASE");
                String strFecha = request.getParameter("CT_FECHA");
                String strHora = request.getParameter("CT_HORA");
                String strComent = request.getParameter("CT_COMENTARIOS");
                String strGiro = request.getParameter("CT_GIRO");
                String strArea = request.getParameter("CT_AREA");
                String strCP = request.getParameter("CT_CP");
                final String strCalle = URLDecoder.decode(new String(request.getParameter("CT_CALLE").getBytes("iso-8859-1")), "UTF-8");
                String strNumEx = request.getParameter("CT_NUM");
                String strCol = request.getParameter("CT_COL");
                String strNombre = request.getParameter("CT_NOMBRE");
                String strConmutador = request.getParameter("CT_CONMUTADOR");
                int intMailMes = Integer.parseInt(request.getParameter("CT_MAILMES"));
                int intAAA = Integer.parseInt(request.getParameter("CT_AAA"));
                String strBase = request.getParameter("strCodigoBase");
                String strIdMedioPublicidad = request.getParameter("mediopublicidad");

                //motivos porque no regreso
                String strMotivo = "";
                if (request.getParameter("motivo") != null) {
                    strMotivo = request.getParameter("motivo");
                }

                //Guardamos el prospecto
                strResultado = tele.doSaveProspectoBase(oConn, strBolBase, intCT_ID, strCT_NO_CLIENTE, strRazonsocial, strRfc,
                        strContacto, strContacto2, strCorreo, strCorreo2, strFecha, strHora, strComent, strSede, strGiro, strArea, strCP, strCalle,
                        strNumEx, strCol, varSesiones, intMailMes, strNombre, strConmutador, intAAA, strIdMedioPublicidad, strMotivo);
//                String strSqlProsp = "update vta_cliente set CT_EXITOSOS = 1, CT_CLAVE_DDBB = '" + strBase + "' where CT_ID = " + intCT_ID;
                String strSqlProsp = "update vta_cliente set CT_EXITOSOS = 1 where CT_ID = " + intCT_ID;
                oConn.runQueryLMD(strSqlProsp);
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strResultado);//Pintamos el resultado
            }
            //caso de agregar contactos del grid
            if (strid.equals("5")) {
                String strResultado = "";
                int intCT_ID = Integer.valueOf(request.getParameter("CT_ID"));
                //Clase de guardado
                //Telemarketing tele = new Telemarketing();
                strResultado = tele.guardaContactos(oConn, varSesiones, request, intCT_ID);
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strResultado);//Pintamos el resultado
            }
            //Caso para borrar
            if (strid.equals("6")) {
                //Recuperamso campos del post
                int intCT_ID = Integer.valueOf(request.getParameter("CT_ID"));
                //Marcar el CLIENTE COMO descartado
                String strUpdate = "update cofide_base set cofide_base.CB_DESCARTADO = 1 where cofide_base.CB_CT_ID=" + intCT_ID;
                oConn.runQueryLMD(strUpdate);
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println("OK");//Pintamos el resultado
            }
            //Obtiene las horas para agendar citas
            if (strid.equals("7")) {
                Fechas fecha = new Fechas();
                String strFecha = request.getParameter("CT_FECHA");
                String strUserAct = varSesiones.getIntNoUser() + "";
                String strFechaIni = "";
                String strHoraActual = fec.getHoraActual(); //hora actual
                String strMin = strHoraActual.substring(3, 5); //tomamos los minutos de la hora
                String strMinOk = ""; //donde se guardaran los minutos que corresponde de 3 en 3 hasta el 60
                int intMIn = Integer.parseInt(strMin);
                if (fec.FormateaBD(strFecha, "/").equals(fec.getFechaActual())) { // si es el dia actual, solo podra dar hora actual en adelante
                    for (int i = 3; i <= intMIn + 3; i += 3) { //minuto actual menor igual al valor de 3 en 3 
                        if (intMIn > 0 && intMIn <= i) { //si es mayor a cero y menor al ultimo valor de 3 en 3 
                            if (i < 10) { //si es menor a 10 le agrega un cero
                                strMinOk = "0" + i;
                            } else { //si es mayor a 10 lo deja completo
                                strMinOk = "" + i;
                            } //fin if
                        } //fin if 
                    } //fin for
                    strFechaIni = strFecha + " " + strHoraActual.replace(strMin, strMinOk); //hora de la que partira
                } else {
                    strFechaIni = strFecha + " 09:00";
                }
                //String strFechaFin = strFecha + " 18:00";
                String strFechaFin = strFecha + " 17:57";
                StringBuilder strXmlHoras = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
                strXmlHoras.append("<horas>");
                SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy kk:mm");
                SimpleDateFormat formatHora = new SimpleDateFormat("kk:mm");
                final long ONE_MINUTE_IN_MILLIS = 60000;
                Date dteSolicitadoIni = formateador.parse(strFechaIni);
                Date dteSolicitadoFin = formateador.parse(strFechaFin);
                //Objeto calendario
                Calendar dateIni = Calendar.getInstance();
                dateIni.setTime(dteSolicitadoIni);
                Calendar dateFin = Calendar.getInstance();
                dateFin.setTime(dteSolicitadoFin);
                //Ciclo para recorrer todos los minutos programables
                while (dateIni.before(dateFin)) {
                    long t = dateIni.getTimeInMillis();
                    Date afterAddingTenMins = new Date(t + (3 * ONE_MINUTE_IN_MILLIS));
                    String strHoraProb = formatHora.format(afterAddingTenMins);
                    //Evaluamos si hay un evento en este horario
                    boolean bolExiste = false;
                    String strSql = "select EV_ID from crm_eventos "
                            + " where EV_FECHA_INICIO = '" + fecha.FormateaBD(strFecha, "/") + "' "
                            + " and EV_HORA_INICIO = '" + strHoraProb + "' "
                            + " and EV_ESTADO = 1 "
                            + "and EV_ASIGNADO_A = " + strUserAct;
                    ResultSet rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        bolExiste = true;
                    }
                    rs.close();
                    if (!bolExiste) {
                        strXmlHoras.append("<hora valor=\"" + strHoraProb + "\"  />");
                    }
                    dateIni.setTime(afterAddingTenMins);
                }
                strXmlHoras.append("</horas>");
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXmlHoras.toString());//Pintamos el resultado

            }
            //Obtiene los indicadores por dibujar
            if (strid.equals("8")) {
                //int intCuantasVentas = 0;
                int intCuantosNvos = 0;
                double dblMontoVentas = 0;
                double dblMontoCobrado = 0;
                int intCuantosNvosMeta = 0;
                double dblMontoVentasMeta = 0;
                int intConteoLlamada = 0;
                double dblFactura = 0.0;
                int intNuevos = 0;
                String strPendiente = "Ninguna";
                String strSql = "";
                ResultSet rs;
                //ventas
                strSql = "select (FAC_TOTAL - FAC_IMPUESTO1) as TOT "
                        + "from view_ventasglobales "
                        + "where FAC_ANULADA = 0 and left(FAC_FECHA,6) = '" + fec.getFechaActual().substring(0, 6) + "' and FAC_US_ALTA = " + varSesiones.getIntNoUser()
                        + " and FAC_PROMO = 0 and CANCEL = 0";
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    dblMontoVentas += rs.getDouble("TOT");

                }
                rs.close();//               
                //Cuantos Cobrado
                strSql = "select (FAC_TOTAL - FAC_IMPUESTO1) as COBRADO "
                        + "from view_ventasglobales "
                        + "where FAC_ANULADA = 0 and FAC_PAGADO = 1 AND FAC_COFIDE_VALIDA = 1 AND left(FAC_FECHA_COBRO,6) = '" + fec.getFechaActual().substring(0, 6) + "' "
                        + "and FAC_US_ALTA = " + varSesiones.getIntNoUser() + " and FAC_PROMO = 0 AND CANCEL = 0";
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {

                    dblMontoCobrado += rs.getDouble("COBRADO");

                }
                rs.close();
                //Metas por grupo
                strSql = "select cofide_gtrabajo.* from cofide_gtrabajo,cofide_grupo_trabajo,usuarios "
                        + " where cofide_grupo_trabajo.CG_ID = cofide_gtrabajo.CG_ID "
                        + " and usuarios.US_GRUPO = cofide_grupo_trabajo.CG_ID"
                        + " and CGD_MES = " + (fec.getMesActual() + 1) + " and CGD_ANIO = " + fec.getAnioActual() + " and usuarios.id_usuarios = " + varSesiones.getIntNoUser();
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    intCuantosNvosMeta = rs.getInt("CGD_IMPMETA");
                }
                rs.close();
                //meta montos, nuevos participantes
                String strSqlMetas = "select * from cofide_metas_usuario where id_usuarios = " + varSesiones.getIntNoUser();
                rs = oConn.runQuery(strSqlMetas, true);
                while (rs.next()) {
                    dblMontoVentasMeta = rs.getDouble("CMU_IMPORTE");
                    intCuantosNvos = rs.getInt("CMU_NUEVO_CTE");
                }
                rs.close();
                //llamadas
                int intCuantasLlamadas = 0;
                strSql = "select CL_ID from cofide_llamada where CL_FECHA = '" + fec.getFechaActual() + "' and CL_COMPLETO = 1 and CL_USUARIO = '" + varSesiones.getIntNoUser() + "'";
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
//                    intConteoLlamada = rs.getInt("Llamadas");
                    intConteoLlamada++;
                }
                rs.close();
                dblFactura = getFacturado(oConn, varSesiones.getIntNoUser());
                strPendiente = getPendiente(oConn, varSesiones.getIntNoUser());
                // nuevos clientes CAMBIAR
                strSql = "select C_ID from cofide_clientes "
                        + "where C_CT_ID in (select ct_id from vta_cliente, usuarios where CT_CLAVE_DDBB = COFIDE_CODIGO and "
                        + "id_usuarios = " + varSesiones.getIntNoUser() + ") and left(C_FECHA,6) = '" + fec.getFechaActual().substring(0, 6) + "'";
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    intNuevos++;
                }
                rs.close();
                //Generamos el xml
                StringBuilder strXmlDash = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
                strXmlDash.append("<indicadores>");
                strXmlDash.append("<Indicador "
                        + " llamadas=\"" + intConteoLlamada + "\""
                        + " nuevo=\"" + intNuevos + "\""
                        + " monto=\"" + dblMontoVentas + "\""
                        + " cobrado=\"" + dblMontoCobrado + "\""
                        + " nuevo_meta=\"" + intCuantosNvos + "\""
                        + " monto_meta=\"" + dblMontoVentasMeta + "\""
                        + " monto_factura=\"" + dblFactura + "\""
                        + " pendiente=\"" + strPendiente + "\""
                        + " />");

                strXmlDash.append("</indicadores>");
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXmlDash.toString());//Pintamos el resultado
            }
            if (strid.equals("11")) {
                int intMax = 0;
                String strSql = "SELECT MAX(ct_id) AS CT_ID FROM vta_cliente ";
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    intMax = rs.getInt("CT_ID"); //recuperamos el ultimo registro guardado
                }
                rs.close();
                intMax += 1;
                String strRazon = request.getParameter("NCT_RAZONSOCIAL");
                String strMail = request.getParameter("NCT_EMAIL1");
                String strNumero = request.getParameter("NCT_NUMERO");
                String strMedio = request.getParameter("NCT_MEDIO");
                int intEmpresa = varSesiones.getIntIdEmpresa();
                String strBase = "";
                String strSqlNumBase = "select COFIDE_CODIGO from usuarios where id_usuarios = " + varSesiones.getIntNoUser();
                ResultSet rsBase = oConn.runQuery(strSqlNumBase, true);
                while (rsBase.next()) {
                    strBase = rsBase.getNString("COFIDE_CODIGO");
                }
                rsBase.close();
                String strInsert = "insert into vta_cliente (CT_ID, CT_RAZONSOCIAL, CT_EMAIL1, CT_TELEFONO1, EMP_ID, CT_CLAVE_DDBB, CT_ES_PROSPECTO, CT_MKT) values "
                        + "(" + intMax + ",'" + strRazon + "','" + strMail + "','" + strNumero + "'," + intEmpresa + ",'" + strBase + "',1,'" + strMedio + "')";
                oConn.runQueryLMD(strInsert);
                if (!strMedio.equals("0")) {
                    String strInsertMedios = "insert into cofide_repoinbound (CR_RAZONSOCIAL,CR_NUMCLIENTE,CR_MEDIO) values "
                            + "('" + strRazon + "'," + intMax + ",'" + strMedio + "')";
                    oConn.runQueryLMD(strInsertMedios); //guarda el registro de medios
                }
                //enviamos el ID
                UtilXml utilXML = new UtilXml();
                String strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                strXML += "<vta>";
                strXML += "<datos "
                        + " NCT_ID = \"" + intMax + "\"  "
                        + " />";
                strXML += "</vta>";
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } // fin 11
            if (strid.equals("12")) { //autocompletar
                String strValorBuscar = request.getParameter("term");
                String strCurso = request.getParameter("Clasifica");
                String strPromosion = request.getParameter("promosion");
                String strCompleteSql = "";
                if (strPromosion != null) {
                    if (strPromosion.equals("1")) {
                        strCompleteSql = " and CC_IS_DIPLOMADO = 0 and CC_IS_SEMINARIO = 0";
                    }
                }
                if (strCurso == null) {
                    strCurso = "1";
                } //si viene vacio la variable, asignarle el 1
                int intCurso = Integer.parseInt(strCurso);
                if (strValorBuscar == null) {
                    strValorBuscar = "";
                }
                //Declaramos objeto json
                JSONArray jsonChild = new JSONArray();
                if (strValorBuscar != "") {
                    String strSql = "select concat(CC_CURSO_ID,' / ',"
                            + "CC_HR_EVENTO_INI,' / ',"
                            + "substring(CC_FECHA_INICIAL,7,2),'-',"
                            + "substring(CC_FECHA_INICIAL,5,2),'-',"
                            + "substring(CC_FECHA_INICIAL,1,4),' / ',"
                            + "CSH_SEDE,' / ',"
                            + "CC_NOMBRE_CURSO) as CC_NOMBRE_CURSO "
                            + "from cofide_cursos LEFT JOIN cofide_sede_hotel on CC_SEDE_ID = CSH_ID "
                            + "where CC_NOMBRE_CURSO like '%" + strValorBuscar + "%' "
                            + "and CC_ACTIVO = 1 and CC_ESTATUS = 1 ";
                    if (intCurso == 1) {
                        strSql += " and CC_IS_PRESENCIAL = 1 and CC_FECHA_INICIAL >= REPLACE(DATE_SUB(curdate(),INTERVAL 10 day),'-','')";
                    }
                    if (intCurso == 2) {
                        strSql += " and CC_IS_ONLINE = 1 and CC_FECHA_INICIAL >= REPLACE(DATE_SUB(curdate(),INTERVAL 10 day),'-','')";
                    }
                    if (intCurso == 3) {
                        strSql += " and CC_IS_VIDEO = 1 ";
                    }
                    strSql += strCompleteSql;
                    strSql += " order by CC_FECHA_INICIAL, CC_HR_EVENTO_INI";
                    ResultSet rsCombo;
                    try {
                        rsCombo = oConn.runQuery(strSql, true);
                        while (rsCombo.next()) {
                            String strCurso1 = rsCombo.getString("CC_NOMBRE_CURSO");
                            JSONObject objJson = new JSONObject();
                            objJson.put("value", strCurso1);
                            objJson.put("label", strCurso1);
                            jsonChild.put(objJson);
                        }
                        rsCombo.close();
                    } catch (SQLException ex) {
                        ex.fillInStackTrace();
                    }
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(jsonChild.toString());//Pintamos el resultado
            } //fin 12
            if (strid.equals("13")) { //obtenerHr
                String strFecha = request.getParameter("FECHA");
                strFecha = fec.FormateaBD(strFecha, "/");
                String strOk = "";
                String strFechaActual = fec.getFechaActual();
                int intFecha = Integer.parseInt(strFecha);
                int intFechaActual = Integer.parseInt(strFechaActual);
                String strFechaDiagonal = fec.getFechaActualDDMMAAAADiagonal();
                if (intFecha >= intFechaActual) {
                    strOk = "ok";
                }
                UtilXml utilXML = new UtilXml();
                String strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                strXML += "<vta>";
                strXML += "<datos "
                        + " ok = \"" + strOk + "\"  "
                        + " FechaDiagonal = \"" + strFechaDiagonal + "\"  "
                        + " />";
                strXML += "</vta>";
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } //fin 13
            if (strid.equals("14")) { //CP
                String strCp = request.getParameter("CT_CP");
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<Sepomex>");
                String strColonia = "";
                String strMunicipio = "";
                String strEstado = "";
                int cont = 0;
//                String strSql = "select CMX_COLONIA, CMX_MUNICIPIO, CMX_ESTADO from cofide_sepomex where CMX_CP = " + strCp;
                String strSql = "select UPPER(CMX_COLONIA) as CMX_COLONIA, UPPER(CMX_MUNICIPIO) as CMX_MUNICIPIO,UPPER(CMX_ESTADO) as CMX_ESTADO from cofide_sepomex where CMX_CP = " + strCp;
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strColonia = rs.getString("CMX_COLONIA");
                    strMunicipio = rs.getString("CMX_MUNICIPIO");
                    strEstado = rs.getString("CMX_ESTADO");
                    if (cont == 0) {
                        strXML.append("<General ");
                        strXML.append(" CMX_MUNICIPIO = \"").append(util.Sustituye(strMunicipio)).append("\" ");
                        strXML.append(" CMX_ESTADO = \"").append(util.Sustituye(strEstado)).append("\" ");
                        strXML.append(" >");
                    }
                    strXML.append("<Colonia ");
                    strXML.append(" CMX_COLONIA = \"").append(util.Sustituye(strColonia)).append("\"");
                    strXML.append(" />");
                    cont++;
                }
                strXML.append("</General>");
                strXML.append("</Sepomex>");
                rs.close();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML.toString());//Pintamos el resultado
            } //fin 14
            if (strid.equals("15")) { //pbx
                String IdPBX = "";
                String strId_Llamada = request.getParameter("id_llamada");
                String strSql = "select uniqueid from cofide_llamada where cl_id = " + strId_Llamada;
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    IdPBX = rs.getString("uniqueid");
                }
                rs.close();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(IdPBX);//Pintamos el resultado
            } //15
            if (strid.equals("16")) { //correo al supervissor
                strRes = "OK";
                String strSqlUp = "update usuarios set US_LIBRE = 1 where IS_SUPERVISOR = 0 and id_usuarios = " + varSesiones.getIntNoUser();
                oConn.runQueryLMD(strSqlUp); // pone en disponible al ejecutovo para poderle avisar al supervisor
                if (!oConn.getStrMsgError().equals("")) {
                    strRes = oConn.getStrMsgError();
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado
            } //16
            if (strid.equals("17")) { //monitoreo a los agentes
                String strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                strXML += "<vta>";
                //CONSULTA SI ES SUPERVISOR
                String strGpo = "";
                int intIsSupervisor = 0;
                String strRespuesta = "";
                String strAgentes = "";
                String strCompleteGrupo = "";
                String strSQLSupr = "select IS_SUPERVISOR, US_GRUPO from usuarios where id_usuarios = " + varSesiones.getIntNoUser();
                ResultSet rsSupr = oConn.runQuery(strSQLSupr, true);
                while (rsSupr.next()) {
                    strRespuesta = "OK";
                    strGpo = rsSupr.getString("US_GRUPO");
                    if (!strGpo.equals("")) {
                        strCompleteGrupo = " US_GRUPO = " + strGpo + " and ";
                    }
                    intIsSupervisor = rsSupr.getInt("IS_SUPERVISOR");
                    if (intIsSupervisor == 1) { //si es supervisor, consulta sus agentes
                        String strSqlAgentes = "select nombre_usuario from usuarios where " + strCompleteGrupo + " US_LIBRE = 1 and IS_LOGGED = 1 and IS_TMK = 1";
                        ResultSet rsAgent = oConn.runQuery(strSqlAgentes, true);
                        while (rsAgent.next()) {
                            strAgentes += rsAgent.getString("nombre_usuario") + ", ";
                        } // while agentes
                        rsAgent.close();
                    } // if
                    strXML += "<datos "
                            + " agente = \"" + strAgentes + "\"  "
                            + " libres = \"" + strRespuesta + "\"  "
                            + " supervisor = \"" + intIsSupervisor + "\"  "
                            + " />";
                } // fin while supervisor
                rsSupr.close(); // obtiene el grupo y si es supervisor
                strXML += "</vta>";
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } // FIN 17
            if (strid.equals("18")) { //monitoreo a los agentes     

                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<vta>");
                String strIdCTE = request.getParameter("CT_ID");
                String strCCO_ID = "";
                String strCCO_NOMBRE = "";
                String strCCO_APPAT = "";
                String strCCO_APMAT = "";
                String strCCO_TITULO = "";
                String strCCO_NOSOCIO = "";
                String strCCO_AREA = "";
                String strCCO_ASOC = "";
                String strCCO_CORREO = "";
                String strCT_MailMes = "";
                String strCT_MailMes2 = "";
                String strCCO_CORREO2 = "";
                String strCCO_TELEFONO = "";
                String strCCO_EXT = "";
                String strCCO_ALTERNO = "";
                double dblTotalPtos = 0.0;
                int intIdContacto = 0;
                String strEnvio = "0"; //revisa si ya se le envio o no su cortesia
                String strSqlCTE = "select * from cofide_contactos where CT_ID = " + strIdCTE;
                try {
                    ResultSet rsCTE = oConn.runQuery(strSqlCTE, true);
                    while (rsCTE.next()) {
                        strCCO_ID = rsCTE.getString("CCO_ID");
                        strCCO_NOMBRE = rsCTE.getString("CCO_NOMBRE");
                        strCCO_APPAT = rsCTE.getString("CCO_APPATERNO");
                        strCCO_APMAT = rsCTE.getString("CCO_APMATERNO");
                        strCCO_TITULO = rsCTE.getString("CCO_TITULO");
                        strCCO_NOSOCIO = rsCTE.getString("CCO_NOSOCIO");
                        strCCO_AREA = rsCTE.getString("CCO_AREA");
                        strCCO_ASOC = rsCTE.getString("CCO_ASOCIACION");
                        strCCO_CORREO = rsCTE.getString("CCO_CORREO");
                        strCT_MailMes = rsCTE.getString("CT_MAILMES");
                        strCT_MailMes2 = rsCTE.getString("CT_MAILMES2");
                        strCCO_CORREO2 = rsCTE.getString("CCO_CORREO2");
                        strCCO_TELEFONO = rsCTE.getString("CCO_TELEFONO");
                        strCCO_EXT = rsCTE.getString("CCO_EXTENCION");
                        strCCO_ALTERNO = rsCTE.getString("CCO_ALTERNO");
                        intIdContacto = rsCTE.getInt("CONTACTO_ID");
                        dblTotalPtos = dblGetPuntosCofideContacto(oConn, intIdContacto);
                        strEnvio = getEnvio(oConn, intIdContacto); // revisa si ya se le envio su tarjeta

                        strXML.append("<datos ");
                        strXML.append(" CCO_ID = \"").append(strCCO_ID).append("\"");
                        strXML.append(" CCO_NOMBRE = \"").append(strCCO_NOMBRE).append("\"");
                        strXML.append(" CCO_APPATERNO = \"").append(strCCO_APPAT).append("\"");
                        strXML.append(" CCO_APMATERNO = \"").append(strCCO_APMAT).append("\"");
                        strXML.append(" CCO_TITULO = \"").append(strCCO_TITULO).append("\"");
                        strXML.append(" CCO_NOSOCIO = \"").append(strCCO_NOSOCIO).append("\"");
                        strXML.append(" CCO_AREA = \"").append(strCCO_AREA).append("\"");
                        strXML.append(" CCO_ASOCIACION = \"").append(strCCO_ASOC).append("\"");
                        strXML.append(" CCO_CORREO = \"").append(strCCO_CORREO).append("\"");
                        strXML.append(" CT_MAILMES1 = \"").append((strCT_MailMes.equals("1") ? "SI" : "NO")).append("\"");
                        strXML.append(" CT_MAILMES2 = \"").append((strCT_MailMes2.equals("1") ? "SI" : "NO")).append("\"");
                        strXML.append(" CCO_CORREO2 = \"").append(strCCO_CORREO2).append("\"");
                        strXML.append(" CCO_TELEFONO = \"").append(util.Sustituye(strCCO_TELEFONO)).append("\"");
                        strXML.append(" CCO_EXTENCION = \"").append(strCCO_EXT).append("\"");
                        strXML.append(" CCO_ALTERNO = \"").append(util.Sustituye(strCCO_ALTERNO)).append("\"");
                        strXML.append(" CCO_PUNTOS = \"").append(dblTotalPtos).append("\"");
                        strXML.append(" CONTACTO_ID = \"").append(intIdContacto).append("\"");
                        strXML.append(" CT_ID = \"").append(strIdCTE).append("\"");
                        strXML.append(" enviado = \"").append(strEnvio).append("\"");
                        strXML.append(" />");
                    }
                    rsCTE.close();
                } catch (SQLException e) {
                    System.out.println("ERROR AL OBTENER INFORMACIÓN DEL APRTICIPANTE/CLIENTE " + e.getMessage());
                }
                strXML.append("</vta>");
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML.toString());//Pintamos el resultado
            } //18
            if (strid.equals("19")) {  //limpia contactos para actualizarlos
                String strCT_ID = request.getParameter("CT_ID");
                String strSqlDelContacto = "delete from cofide_contactos where CT_ID = " + strCT_ID;
                oConn.runQueryLMD(strSqlDelContacto);
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println("OK");//Pintamos el resultado
            } //fin 19
            if (strid.equals("20")) {  //hacer llamada individual
                String strTelefono = request.getParameter("CT_TELEFONO");
                int intCT_ID = Integer.parseInt(request.getParameter("CT_ID"));
                String strExt = "";
                String strSqlExt = "select NUM_EXT from usuarios where id_usuarios = " + varSesiones.getIntNoUser();
                ResultSet rsExt = oConn.runQuery(strSqlExt, true);
                while (rsExt.next()) {
                    strExt = rsExt.getString("NUM_EXT"); //obtenemos la extencion del usuario
                }
                rsExt.close();
                System.out.println(strTelefono + " telefono a marcar");
                //int intTimeOut = 15000; // tiempo de espera para que la llamada hacia el cliente sea atendida
                //LlamadasPBX llama = new LlamadasPBX();
                boolean bolRes = false;
                String strIdLlamadaPBX = "";
                //insert llamada en el registro para el historial
                if (!strTelefono.equals("")) { // sí esta vacio el telefono, que no mande la llamada;
                    //LlamadaHistorial lh = new LlamadaHistorial();
                    strIdLlamadaPBX = lh.GuardaLlamada(oConn, intCT_ID, fec.getFechaActual(), fec.getHoraActual(), varSesiones.getIntNoUser(), strExt, strTelefono);
                    bolRes = llama.generaNvaLlamada(oConn, strTelefono, strExt, intTimeOut, varSesiones.getIntSucursalDefault());
                }
                //
                //mandamos a la pantalla los datos de la llamada realizada
                UtilXml utilXML = new UtilXml();
                String strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                strXML += "<vta>";
                strXML += "<datos "
                        + " Respuesta = \"" + bolRes + "\" "
                        + " Mensaje = \"" + llama.getStrRespuesta() + "\" "
                        + " id_llamada = \"" + strIdLlamadaPBX + "\" "
                        + " />";
                strXML += "</vta>";
                //

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } //fin 20
            if (strid.equals("21")) {
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<vta>");
                int intCT_ID = Integer.parseInt(request.getParameter("CT_ID"));
                String strSql = "select * from cofide_razonsocial where CT_ID = " + intCT_ID;
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {

                    strXML.append("<datos");
                    strXML.append(" RZN_ID = \"").append(rs.getInt("CR_ID")).append("\"");
                    strXML.append(" RZN_CTE = \"").append(rs.getString("CT_ID")).append("\"");
                    strXML.append(" RZN_NOMBRE = \"").append(rs.getString("CR_RAZONSOCIAL")).append("\"");
                    strXML.append(" />");
                }
                strXML.append("</vta>");
                strXML.toString();
                rs.close();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } //fin 21
            if (strid.equals("22")) {
                String ct_id = request.getParameter("ct_id");
                String strIdGrupo = "";
                String strSql = "update vta_cliente set CT_ACTIVO = 0 where CT_ID = " + ct_id;
                oConn.runQueryLMD(strSql);
                String strSql2 = "update crm_eventos set EV_ESTADO = 0 where EV_CT_ID = " + ct_id;
                oConn.runQueryLMD(strSql2);
                String strSqlSelect = "select CT_RAZONSOCIAL, CT_CLAVE_DDBB, CT_TELEFONO1 from vta_cliente where CT_ID = " + ct_id;
                ResultSet rs = oConn.runQuery(strSqlSelect, true);
                while (rs.next()) {
                    String strSqlGrupo = "select us_grupo from usuarios where id_usuarios = " + varSesiones.getIntNoUser();
                    ResultSet rsGrupo = oConn.runQuery(strSqlGrupo, true);
                    while (rsGrupo.next()) {
                        strIdGrupo = rsGrupo.getString("US_GRUPO");
                    }
                    rsGrupo.close();
                    String strSql3 = "insert into cofide_err_base (CT_ID, ERR_RAZONSOCIAL, ERR_BASE, ERR_FECHA, ERR_USUARIO, ERR_GRUPO)values"
                            + "(" + ct_id + ",'" + rs.getString("CT_RAZONSOCIAL") + "','" + rs.getString("CT_CLAVE_DDBB") + "','"
                            + fec.getFechaActual() + "'," + varSesiones.getIntNoUser() + "," + strIdGrupo + ")";
                    oConn.runQueryLMD(strSql3);
                }
                rs.close();
                out.clearBuffer();//Limpiamos buffer
            } //22
            if (strid.equals("23")) { //actualiza lso datos de la pantalla de telemarketing
                //Recuperamos campos del post
                int intCT_ID = Integer.valueOf(request.getParameter("CT_ID"));
                //String strRazonsocial = request.getParameter("CT_RAZONSOCIAL");
                final String strRazonsocial = URLDecoder.decode(new String(request.getParameter("CT_RAZONSOCIAL").getBytes("iso-8859-1")), "UTF-8");
                final String strRfc = URLDecoder.decode(new String(request.getParameter("CT_RFC").getBytes("iso-8859-1")), "UTF-8");
                String strSede = request.getParameter("CT_SEDE");
                String strContacto = request.getParameter("CT_CONTACTO");
                String strContacto2 = request.getParameter("CT_CONTACTO2");
                String strCorreo = request.getParameter("CT_CORREO");
                String strCorreo2 = request.getParameter("CT_CORREO2");
                String strComent = request.getParameter("CT_COMENTARIOS");
                String strGiro = request.getParameter("CT_GIRO");
                String strArea = request.getParameter("CT_AREA");
                String strCP = request.getParameter("CT_CP");
                final String strCalle = URLDecoder.decode(new String(request.getParameter("CT_CALLE").getBytes("iso-8859-1")), "UTF-8");
                String strNumEx = request.getParameter("CT_NUM");
                String strCol = request.getParameter("CT_COL");
                String strContactoNOmbre = request.getParameter("CT_NOMBRE"); //
                String strConmutador = request.getParameter("CT_CONMUTADOR"); //
                int intAAA = Integer.parseInt(request.getParameter("CT_AAA"));
                String strIdMedioPublicidad = request.getParameter("mediopublicidad");
                String strUpdateComent = "";
                int intMailMes = Integer.parseInt(request.getParameter("CT_MAILMES"));
                String strIdLlamadaPBX = "";
                String strExtencon = "";
                String strCodigoBase = "";
                String strSqlExt = "select NUM_EXT, COFIDE_CODIGO from usuarios where id_usuarios = " + varSesiones.getIntNoUser();
                ResultSet rsExt = oConn.runQuery(strSqlExt, true);
                while (rsExt.next()) {
                    strExtencon = rsExt.getString("NUM_EXT"); //obtenemos la extencion del usuario
                    strCodigoBase = rsExt.getString("COFIDE_CODIGO"); //obtener la base del ejecutivo
                }
                rsExt.close();

                String strMotivo = "";
                if (request.getParameter("motivo") != null) {
                    strMotivo = request.getParameter("motivo");
                }

                //hacer llamada
                String strUpdateCte = "UPDATE vta_cliente SET CT_RAZONSOCIAL = '" + strRazonsocial + "',CT_RFC = '" + strRfc + "', "
                        + "CT_SEDE = '" + strSede + "', CT_CONTACTO1 = '" + strContactoNOmbre + "', CT_TELEFONO1 = '" + strContacto + "', CT_TELEFONO2 = '" + strContacto2 + "', "
                        + "CT_EMAIL1 = '" + strCorreo + "',CT_EMAIL2 = '" + strCorreo2 + "', CT_GIRO = '" + strGiro + "', CT_AREA = '" + strArea + "', CT_CP = '" + strCP + "', "
                        + "CT_CALLE = '" + strCalle + "', CT_NUMERO = '" + strNumEx + "', CT_COLONIA = '" + strCol + "', CT_CONMUTADOR = '" + strConmutador + "', CT_AAA = '" + intAAA + "', CT_MKT = '" + strIdMedioPublicidad + "',"
                        + "CT_NOMBRE_INVITO = '" + strMotivo + "' "
                        + "where CT_ID = " + intCT_ID;
                if (!strComent.equals("")) {
                    strUpdateComent = "UPDATE crm_eventos set EV_ASUNTO = '" + strComent + "' where EV_ESTADO = 1 and EV_CT_ID = " + intCT_ID; //actualiza el ultimo agregado 
                } else {
                    strUpdateComent = "insert into crm_eventos (EV_ASUNTO, EV_CT_ID, EV_ESTADO) values ('" + strComent + "', " + intCT_ID + ",1)";
                }
                oConn.runQueryLMD(strUpdateCte);
                oConn.runQueryLMD(strUpdateComent);
                out.clearBuffer();//Limpiamos buffer
            } //23
            if (strid.equals("24")) {
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<vta>");
                String strCorreo = request.getParameter("correo");
                String strIDCte = request.getParameter("idcte");
                String strBase = request.getParameter("ct_ddbb");
                int intDuplicado = 0;
                int IntDuplicadoBase = 0;
                int intConteo = 0;
                int intLibre = 0;
                String strBASE = "";
                String strSQL = "select DISTINCT CT_CLAVE_DDBB from vta_cliente where (CT_EMAIL1 = '" + strCorreo + "' "
                        + "OR CT_ID in (select DISTINCT CT_ID from cofide_contactos where CCO_CORREO = '" + strCorreo + "' "
                        + "OR CCO_CORREO2 = '" + strCorreo + "')) "
                        + "and CT_ACTIVO = 1 and CT_ID <>  " + strIDCte;
                try {
                    ResultSet rs = oConn.runQuery(strSQL, true);
                    while (rs.next()) {
                        intConteo++;
                        strBASE = rs.getString("CT_CLAVE_DDBB");
                    }
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println("Error al buscar Correos duplicados: " + ex);
                }
                System.out.println("conteo: " + intConteo);
                if (intConteo == 0) {
                    intLibre = 1;
                } else {
                    if (intConteo > 1) {
                        IntDuplicadoBase = 1;
                    }
                    if (intConteo == 1) {
                        if (strBASE.equals(strBase)) { //si son iguales manda duplicado de la misma base
                            intDuplicado = 1;
                        } else { // si no son iguales, manda duplicado de bases
                            IntDuplicadoBase = 1;
                        }
                    }
                }
                //si los dos van en cero, es porque no existe el registro en ningun lado
                strXML.append("<datos");
                strXML.append(" duplicado = \"").append(intDuplicado).append("\"");
                strXML.append(" duplicadoBase = \"").append(IntDuplicadoBase).append("\"");
                strXML.append(" libre = \"").append(intLibre).append("\"");
                strXML.append(" />");
                strXML.append("</vta>");

                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } //24
            if (strid.equals("25")) {
                String strCT_ID = request.getParameter("CT_ID");
                String strSQLRZNDel = "delete from cofide_razonsocial where ct_id = " + strCT_ID;
                oConn.runQueryLMD(strSQLRZNDel); //limpiar razones sociales
                int intLength = Integer.parseInt(request.getParameter("length"));
                for (int i = 0; i < intLength; i++) {
                    String strRazonSocial = request.getParameter("RAZONSOCIAL" + i);
                    String strSqlRznIns = "insert into cofide_razonsocial (ct_id, cr_razonsocial) value (" + strCT_ID + ",'" + strRazonSocial + "'  )";
                    oConn.runQueryLMD(strSqlRznIns);
                }
            } //25
            if (strid.equals("26")) {
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<vta>");
                String strTelefono = request.getParameter("telefono");
                String strIDCte = request.getParameter("idcte");
                String strBase = request.getParameter("ct_ddbb");
                int intDuplicado = 0;
                int IntDuplicadoBase = 0;
                int intConteo = 0;
                int intLibre = 0;
                String strBASE = "";
                String strSQL = "select DISTINCT CT_CLAVE_DDBB from vta_cliente where (CT_TELEFONO1 = '" + strTelefono + "' "
                        + "OR CT_ID in (select DISTINCT CT_ID from cofide_contactos where CCO_TELEFONO = '" + strTelefono + "' "
                        + "OR CCO_ALTERNO = '" + strTelefono + "')) "
                        + "and CT_ACTIVO = 1 and CT_ID <>  " + strIDCte;
                try {
                    ResultSet rs = oConn.runQuery(strSQL, true);
                    while (rs.next()) {
                        intConteo++;
                        strBASE = rs.getString("CT_CLAVE_DDBB");
                    }
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println("Error al buscar Correos duplicados: " + ex);
                }
                System.out.println("conteo: " + intConteo);
                if (intConteo == 0) {
                    intLibre = 1;
                } else {
                    if (intConteo > 1) {
                        IntDuplicadoBase = 1;
                    }
                    if (intConteo == 1) {
                        if (strBASE.equals(strBase)) { //si son iguales manda duplicado de la misma base
                            intDuplicado = 1;
                        } else { // si no son iguales, manda duplicado de bases
                            IntDuplicadoBase = 1;
                        }
                    }
                }
                //si los dos van en cero, es porque no existe el registro en ningun lado
                strXML.append("<datos");
                strXML.append(" duplicado = \"").append(intDuplicado).append("\"");
                strXML.append(" duplicadoBase = \"").append(IntDuplicadoBase).append("\"");
                strXML.append(" libre = \"").append(intLibre).append("\"");
                strXML.append(" />");
                strXML.append("</vta>");

                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } //26
            if (strid.equals("27")) {
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<vta>");

                String strDia = request.getParameter("dia");
                String strNumeroDia = fec.FormateaBD(strDia, "/");
                String strLunes = "Monday";
                String strMartes = "Tuesday";
                String strMiercoles = "Wednesday";
                String strJueves = "Thursday";
                String strViernes = "Friday";
                String strSabado = "Saturday";
                String strDomingo = "Sunday";
                int intDía = 0;
                String strNombreDia = "";

                String strSQLDay = "select DAYNAME('" + strNumeroDia + "') as day";
                ResultSet rs = oConn.runQuery(strSQLDay, true);
                while (rs.next()) {
                    strNombreDia = rs.getString("day");
                }
                rs.close();
                if (strNombreDia.equals(strLunes)) {
                    intDía = 1;
                }
                if (strNombreDia.equals(strMartes)) {
                    intDía = 2;
                }
                if (strNombreDia.equals(strMiercoles)) {
                    intDía = 3;
                }
                if (strNombreDia.equals(strJueves)) {
                    intDía = 4;
                }
                if (strNombreDia.equals(strViernes)) {
                    intDía = 5;
                }
                if (strNombreDia.equals(strSabado)) {
                    intDía = 6;
                }
                if (strNombreDia.equals(strDomingo)) {
                    intDía = 7;
                }
                //System.out.println("el dia de la semana es: " + intDía);

                strXML.append("<datos");
                strXML.append(" dia = \"").append(intDía).append("\"");
                strXML.append(" />");
                strXML.append("</vta>");
                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } //27

            if (strid.equals("28")) {
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<vta>");
//                String strBusqueda = request.getParameter("buscar");
//                final String strBusqueda = URLDecoder.decode(new String(request.getParameter("buscar").getBytes("iso-8859-1")), "UTF-8");
                final String strBusqueda = URLDecoder.decode(new String(request.getParameter("buscar").getBytes("iso-8859-1")), "UTF-8");
                String strFiltro = request.getParameter("filtro");
                String strCodigoUser = request.getParameter("BaseUser");
                String strSqlBusqueda = "";
                String strDDBB = "";

                strSqlBusqueda = "select *,"
                        + "(select nombre_usuario from usuarios where usuarios.COFIDE_CODIGO = vta_cliente.CT_CLAVE_DDBB limit 1)as CT_EJECUTIVO "
                        + "from vta_cliente ";

                if (strFiltro.equals("1")) { //CT_ID

                    strSqlBusqueda += " where CT_ID = " + strBusqueda;

                }

                if (strFiltro.equals("2")) { //RAZON SOCIAL

                    strSqlBusqueda += " where (CT_ID in (select CT_ID from vta_cliente_facturacion where DFA_RAZONSOCIAL like '%" + strBusqueda + "%') "
                            + "or CT_RAZONSOCIAL like '%" + strBusqueda + "%') ";

                }

                if (strFiltro.equals("3")) { //RFC

                    strSqlBusqueda += " where (CT_RFC like '%" + strBusqueda + "%' "
                            + "OR CT_ID IN (SELECT CT_ID FROM vta_cliente_facturacion WHERE DFA_RFC LIKE '%" + strBusqueda + "%')) ";

                }

                if (strFiltro.equals("4")) { //CORREO

                    strSqlBusqueda = "select CT_ID, CT_RAZONSOCIAL, CT_RFC, CT_ES_PROSPECTO, CT_ACTIVO, CT_CLAVE_DDBB,"
                            + "(select nombre_usuario from usuarios where usuarios.COFIDE_CODIGO = CT_CLAVE_DDBB limit 1)as CT_EJECUTIVO "
                            + "from vta_cliente "
                            + "where ((CT_EMAIL1 LIKE '%" + strBusqueda + "%' or CT_EMAIL2 like '%" + strBusqueda + "%') "
                            + "or CT_ID in (select CT_ID from cofide_contactos where CCO_CORREO like '%" + strBusqueda + "%' or CCO_CORREO2 like '%" + strBusqueda + "%')) ";

                }

                if (strFiltro.equals("5")) { // TELEFONO

                    strSqlBusqueda = "select CT_ID, CT_RAZONSOCIAL, CT_RFC, CT_ES_PROSPECTO, CT_ACTIVO, CT_CLAVE_DDBB,"
                            + "(select nombre_usuario from usuarios where usuarios.COFIDE_CODIGO = CT_CLAVE_DDBB limit 1)as CT_EJECUTIVO "
                            + "from vta_cliente "
                            + "where ((CT_TELEFONO1 LIKE '%" + strBusqueda + "%' or CT_TELEFONO2 like '%" + strBusqueda + "%') "
                            + "or CT_ID in (select CT_ID from cofide_contactos where CCO_TELEFONO like '%" + strBusqueda + "%' "
                            + "or CCO_ALTERNO like '%" + strBusqueda + "%')) ";

                }

                if (strFiltro.equals("6")) { //SEDE

                    strSqlBusqueda += " where CT_SEDE like '%" + strBusqueda + "%'";

                }

                if (strFiltro.equals("7")) { //GIRO

                    strSqlBusqueda += " where CT_GIRO like '%" + strBusqueda + "%'";

                }

                if (strFiltro.equals("8")) { //AREA

                    strSqlBusqueda += " where (CT_AREA like '%" + strBusqueda + "%' OR CT_ID in (select CT_ID from cofide_contactos where CCO_AREA like '%" + strBusqueda + "%'))";

                }

                if (strFiltro.equals("9")) { // CONTACTO

                    strSqlBusqueda += "where CT_ID in "
                            + "(select CT_ID from cofide_contactos where CONCAT(CCO_NOMBRE,' ', CCO_APPATERNO,' ', CCO_APMATERNO) "
                            + "like '%" + strBusqueda + "%') ";

                }
                boolean bolExterno = false;

                if (strCodigoUser.equals("BE-INBOUND") || strCodigoUser.equals("BE-85") || strCodigoUser.equals("")) {

                    bolExterno = true;

                }

                if (!bolExterno) {

                    strSqlBusqueda += " and CT_CLAVE_DDBB = '" + strCodigoUser + "'";

                }

                strSqlBusqueda += " and CT_ACTIVO = 1 order by CT_ES_PROSPECTO, CT_ID";

                ResultSet rs = oConn.runQuery(strSqlBusqueda, true);

                while (rs.next()) {

                    strXML.append("<datos");
                    strXML.append(" CT_ID = \"").append(rs.getString("CT_ID")).append("\"");
                    strXML.append(" CT_RAZONSOCIAL = \"").append(util.Sustituye(rs.getString("CT_RAZONSOCIAL"))).append("\"");
                    strXML.append(" CT_RFC = \"").append(util.Sustituye(rs.getString("CT_RFC"))).append("\"");
                    strXML.append(" CT_CLAVE_DDBB = \"").append(rs.getString("CT_CLAVE_DDBB")).append("\"");
                    strXML.append(" nombre_usuario = \"").append(rs.getString("CT_EJECUTIVO")).append("\"");
                    strXML.append(" CT_ES_PROSPECTO = \"").append(rs.getString("CT_ES_PROSPECTO")).append("\"");
                    strXML.append(" />");

                }
                rs.close();

                strXML.append("</vta>");
                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado

            } //28

            if (strid.equals("29")) {
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<vta>");
                String strEstatus = request.getParameter("estatus");
                String strTipoPausa = "";
                String strIdPausa = "";
                String strResp = "";
                String strSql = "";
                cofide_pausa cp = new cofide_pausa();
                if (strEstatus.equals("0")) { //inicia la pausa
                    strTipoPausa = request.getParameter("tipopausa");
                    cp.setFieldInt("CP_USUARIO", varSesiones.getIntNoUser());
                    cp.setFieldString("CP_HORA_INICIO", fec.getHoraActual());
                    cp.setFieldString("CP_FECHA", fec.getFechaActual());
                    cp.setFieldString("CP_TIPO", strTipoPausa);
                    cp.setBolGetAutonumeric(true);
                    cp.Agrega(oConn);
                    strResp = cp.getValorKey();
                    // usuario ocupado en pausa
                    strSql = "Update usuarios set US_LIBRE = 0 where id_usuarios = " + varSesiones.getIntNoUser();
                    oConn.runQueryLMD(strSql);
                    // usuario ocupado en pausa
                }
                if (strEstatus.equals("1")) {
                    strIdPausa = request.getParameter("idpausa");
                    strSql = "Update cofide_pausa set CP_HORA_FIN = '" + fec.getHoraActual() + "' where CP_ID = " + strIdPausa;
                    oConn.runQueryLMD(strSql);
                    // usuario libre
                    strSql = "Update usuarios set US_LIBRE = 1 where id_usuarios = " + varSesiones.getIntNoUser();
                    oConn.runQueryLMD(strSql);
                    // usuario libre
                }
                strXML.append("<datos");
                strXML.append(" idpausa = \"").append(strResp).append("\"");
                strXML.append(" />");
                strXML.append("</vta>");
                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } //29
            if (strid.equals("30")) {
                //Telemarketing tele = new Telemarketing();
                String strResultado = "OK";
                String strCte = request.getParameter("CT_ID");
                tele.GuardarCteTmp(oConn, strCte, request, varSesiones.getIntNoUser());
                tele.GuardarContactoTmp(oConn, strCte, request);
                tele.GuardaRazonSocialTmp(oConn, strCte, request);
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strResultado);//Pintamos el resultado
            } //30
            if (strid.equals("31")) {
                VtaCteTMP CTE = new VtaCteTMP();
                VtaContactoTMP CCOTmp = new VtaContactoTMP();
                VtaRazonsocialTMP RZNTmp = new VtaRazonsocialTMP();
                //llenado de vta_cliente
                String strCteManual = request.getParameter("cte_manual");//Cliente buscado manualmente
                int intCT_ID = 0;
                String strCT_RAZONSOCIAL = "";
                String strCT_NUMERO = "";
                String strCT_RFC = "";
                String strCT_TELEFONO1 = "";
                String strCT_TELEFONO2 = "";
                String strCT_EMAIL1 = "";
                String strCT_EMAIL2 = "";
                String strCT_CP = "";
                String strCT_COL = "";
                String strCT_CALLE = "";
                String strCT_EDO = "";
                String strCT_MUNI = "";
                String strCT_NUM = "";
                String strCT_AREA = "";
                String strCT_GIRO = "";
                String strCT_SEDE = "";
                String strCT_COMENTARIO = "";
                String strCT_COMENTARIOS = "";
                String strCT_CONMUTADOR = "";
                String strCT_CONTACTO = "";
                String strCT_FECHA = "";
                String strCT_HORA = "";
                int intCte_Prosp = 0;
                int intEnvioMail = 0;
                String strExtencion = "";
                String strMotivo = "";
                String strCodigoBase = "";
                String strSqlDelTemp = ""; //eliminar temporales
                String strSqlExt = "select NUM_EXT, COFIDE_CODIGO from usuarios where id_usuarios = " + varSesiones.getIntNoUser();
                ResultSet rsExt = oConn.runQuery(strSqlExt, true);
                while (rsExt.next()) {
                    strExtencion = rsExt.getString("NUM_EXT"); //obtenemos la extencion del usuario
                    strCodigoBase = rsExt.getString("COFIDE_CODIGO"); //obtener la base del ejecutivo
                }
                rsExt.close();
                //Buscamos al cliente seleccionado
                String strSqlVta = "select CT_ID,CT_RAZONSOCIAL,CT_EMAIL1,CT_EMAIL2,CT_NUMERO,CT_RFC,CT_TELEFONO1, CT_TELEFONO2 ,"
                        + "CT_CP,CT_CALLE, CT_ES_PROSPECTO,"
                        + "CT_ESTADO, CT_MUNICIPIO, CT_COLONIA, CT_NUMERO, CT_SEDE, CT_GIRO, "
                        + "CT_AREA, CT_MAILMES, CT_CONMUTADOR, CT_CONTACTO1, CT_FECHA, CT_HORA, CT_COMENTARIO,CT_NOMBRE_INVITO "
                        + "from vta_cliente_tmp where  CT_ID = " + strCteManual;
                ResultSet rs = oConn.runQuery(strSqlVta, true);
                while (rs.next()) {
                    intCT_ID = rs.getInt("CT_ID"); //id cliente de vta_clientes
                    strCT_RAZONSOCIAL = rs.getString("CT_RAZONSOCIAL");
                    strCT_EMAIL1 = rs.getString("CT_EMAIL1");
                    strCT_EMAIL2 = rs.getString("CT_EMAIL2");
                    strCT_NUMERO = rs.getString("CT_NUMERO");
                    strCT_RFC = rs.getString("CT_RFC");
                    strCT_TELEFONO1 = rs.getString("CT_TELEFONO1");
                    strCT_TELEFONO2 = rs.getString("CT_TELEFONO2");
                    strCT_CP = rs.getString("CT_CP");
                    strCT_CALLE = rs.getString("CT_CALLE");
                    strCT_EDO = rs.getString("CT_ESTADO");
                    strCT_MUNI = rs.getString("CT_MUNICIPIO");
                    strCT_COL = rs.getString("CT_COLONIA");
                    strCT_SEDE = rs.getString("CT_SEDE");
                    strCT_GIRO = rs.getString("CT_GIRO");
                    strCT_AREA = rs.getString("CT_AREA");
                    intEnvioMail = rs.getInt("CT_MAILMES");
                    intCte_Prosp = rs.getInt("CT_ES_PROSPECTO");
                    strCT_CONMUTADOR = rs.getString("CT_CONMUTADOR");
                    strCT_CONTACTO = rs.getString("CT_CONTACTO1");
                    strCT_FECHA = rs.getString("CT_FECHA");
                    strCT_HORA = rs.getString("CT_HORA");
                    strCT_COMENTARIOS = rs.getString("CT_COMENTARIO");
                    strMotivo = rs.getString("CT_NOMBRE_INVITO");
                    String StrSqlComent = "select EV_ASUNTO from crm_eventos where EV_ESTADO = 1 and EV_CT_ID = " + strCteManual;
                    ResultSet rsComent = oConn.runQuery(StrSqlComent, true);
                    while (rsComent.next()) {
                        strCT_COMENTARIO = rsComent.getString("EV_ASUNTO");
                    }
                    rsComent.close();
                }
                rs.close();

                //eliminamos temporales
                //cte_temp
                strSqlDelTemp = "delete from vta_cliente_tmp where CT_ID = " + strCteManual;
                oConn.runQueryLMD(strSqlDelTemp);
                //eliminamos temporales

                //Enviamos el xml con los datos
                UtilXml utilXML = new UtilXml();
                String strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                strXML += "<vta>";
                strXML += "<datos "
                        + " CT_ID = \"" + intCT_ID + "\"  "
                        + " CT_RAZONSOCIAL = \"" + utilXML.Sustituye(strCT_RAZONSOCIAL) + "\"  "
                        + " CT_EMAIL1 = \"" + strCT_EMAIL1 + "\" "
                        + " CT_EMAIL2 = \"" + strCT_EMAIL2 + "\" "
                        + " CT_NUMERO = \"" + strCT_NUMERO + "\" "
                        + " CT_RFC = \"" + utilXML.Sustituye(strCT_RFC) + "\" "
                        + " CT_TELEFONO1 = \"" + strCT_TELEFONO1 + "\" "
                        + " CT_TELEFONO2 = \"" + strCT_TELEFONO2 + "\" "
                        + " CT_CP = \"" + strCT_CP + "\" "
                        + " CT_COL = \"" + strCT_COL + "\" "
                        + " CT_CALLE = \"" + utilXML.Sustituye(strCT_CALLE) + "\" "
                        + " CT_SEDE = \"" + strCT_SEDE + "\" "
                        + " CT_GIRO = \"" + strCT_GIRO + "\" "
                        + " CT_AREA = \"" + strCT_AREA + "\" "
                        + " CT_MUNI = \"" + strCT_MUNI + "\" "
                        + " CT_EDO = \"" + strCT_EDO + "\" "
                        + " Mensaje = \"" + llama.getStrRespuesta() + "\" "
                        + " EV_ASUNTO = \"" + utilXML.Sustituye(strCT_COMENTARIO) + "\" "
                        + " envio_mail = \"" + intEnvioMail + "\" "
                        + " cte_prosp = \"" + intCte_Prosp + "\" "
                        + " CT_CONMUTADOR = \"" + strCT_CONMUTADOR + "\" "
                        + " CT_CONTACTO = \"" + strCT_CONTACTO + "\" "
                        + " CT_COMENTARIOS = \"" + strCT_COMENTARIOS + "\" "
                        + " CT_FECHA = \"" + strCT_FECHA + "\" "
                        + " CT_HORA = \"" + strCT_HORA + "\" "
                        + " motivo = \"" + strMotivo + "\" "
                        + " />";
                strXML += "</vta>";
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado

            } //31
            if (strid.equals("32")) {
                //monitoreo a los agentes
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<vta>");
                String strIdCTE = request.getParameter("CT_ID");
                String strCCO_ID = "";
                String strCCO_NOMBRE = "";
                String strCCO_APPAT = "";
                String strCCO_APMAT = "";
                String strCCO_TITULO = "";
                String strCCO_NOSOCIO = "";
                String strCCO_AREA = "";
                String strCCO_ASOC = "";
                String strCCO_CORREO = "";
                String strCCO_CORREO2 = "";
                String strCCO_TELEFONO = "";
                String strCCO_EXT = "";
                String strCCO_ALTERNO = "";
                int intIdContacto = 0;
                String strSqlCTE = "select * from cofide_contactos_tmp where CT_ID = " + strIdCTE;
                try {
                    ResultSet rsCTE = oConn.runQuery(strSqlCTE, true);
                    while (rsCTE.next()) {
                        strCCO_ID = rsCTE.getString("CCO_ID");
                        strCCO_NOMBRE = rsCTE.getString("CCO_NOMBRE");
                        strCCO_APPAT = rsCTE.getString("CCO_APPATERNO");
                        strCCO_APMAT = rsCTE.getString("CCO_APMATERNO");
                        strCCO_TITULO = rsCTE.getString("CCO_TITULO");
                        strCCO_NOSOCIO = rsCTE.getString("CCO_NOSOCIO");
                        strCCO_AREA = rsCTE.getString("CCO_AREA");
                        strCCO_ASOC = rsCTE.getString("CCO_ASOCIACION");
                        strCCO_CORREO = rsCTE.getString("CCO_CORREO");
                        strCCO_CORREO2 = rsCTE.getString("CCO_CORREO2");
                        strCCO_TELEFONO = rsCTE.getString("CCO_TELEFONO");
                        strCCO_EXT = rsCTE.getString("CCO_EXTENCION");
                        strCCO_ALTERNO = rsCTE.getString("CCO_ALTERNO");
                        intIdContacto = rsCTE.getInt("CONTACTO_ID");
                        double dblPuntos = dblGetPuntosCofideContacto(oConn, intIdContacto);

                        strXML.append("<datos ");
                        strXML.append(" CCO_ID = \"").append(strCCO_ID).append("\"");
                        strXML.append(" CCO_NOMBRE = \"").append(strCCO_NOMBRE).append("\"");
                        strXML.append(" CCO_APPATERNO = \"").append(strCCO_APPAT).append("\"");
                        strXML.append(" CCO_APMATERNO = \"").append(strCCO_APMAT).append("\"");
                        strXML.append(" CCO_TITULO = \"").append(strCCO_TITULO).append("\"");
                        strXML.append(" CCO_NOSOCIO = \"").append(strCCO_NOSOCIO).append("\"");
                        strXML.append(" CCO_AREA = \"").append(strCCO_AREA).append("\"");
                        strXML.append(" CCO_ASOCIACION = \"").append(strCCO_ASOC).append("\"");
                        strXML.append(" CCO_CORREO = \"").append(strCCO_CORREO).append("\"");
                        strXML.append(" CCO_CORREO2 = \"").append(strCCO_CORREO2).append("\"");
                        strXML.append(" CCO_TELEFONO = \"").append(strCCO_TELEFONO).append("\"");
                        strXML.append(" CCO_EXTENCION = \"").append(strCCO_EXT).append("\"");
                        strXML.append(" CCO_ALTERNO = \"").append(strCCO_ALTERNO).append("\"");
                        strXML.append(" CONTACTO_ID = \"").append(intIdContacto).append("\"");
                        strXML.append(" CCO_PUNTOS = \"").append(dblPuntos).append("\"");
                        strXML.append(" />");
                    }
                    rsCTE.close();
                    //contacto_tmp
                    String strSqlDelTemp = "delete from cofide_contactos_tmp where CT_ID = " + strIdCTE;
                    oConn.runQueryLMD(strSqlDelTemp);
                } catch (SQLException e) {
                    System.out.println("error " + e);
                }
                strXML.append("</vta>");
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML.toString());//Pintamos el resultado
            } //32
            if (strid.equals("33")) {
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<vta>");
                int intCT_ID = Integer.parseInt(request.getParameter("CT_ID"));
                String strSql = "select * from cofide_razonsocial_tmp where CT_ID = " + intCT_ID;
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {

                    strXML.append("<datos");
                    strXML.append(" RZN_ID = \"").append(rs.getInt("CR_ID")).append("\"");
                    strXML.append(" RZN_CTE = \"").append(rs.getString("CT_ID")).append("\"");
                    strXML.append(" RZN_NOMBRE = \"").append(rs.getString("CR_RAZONSOCIAL")).append("\"");
                    strXML.append(" />");
                }
                strXML.append("</vta>");
                strXML.toString();
                rs.close();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
                //razon_temp
                String strSqlDelTemp = "delete from cofide_razonsocial_tmp where CT_ID = " + intCT_ID;
                oConn.runQueryLMD(strSqlDelTemp);
            } //33

            //Regresa las bases del grupo de trabajo del usuario
            if (strid.equals("34")) {
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<BasesCt>");
                String strSql = "select COFIDE_CODIGO "
                        + "from usuarios "
                        + "where if("
                        + "(select US_GRUPO from usuarios where id_usuarios = " + varSesiones.getIntNoUser() + ")<> '',"
                        + "US_GRUPO = (select US_GRUPO from usuarios where id_usuarios = " + varSesiones.getIntNoUser() + "),"
                        + "1=1) and COFIDE_CODIGO <> '' "
                        + "group by COFIDE_CODIGO;";
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {

                    strXML.append("<datos");
                    strXML.append(" COFIDE_CODIGO = \"").append(rs.getString("COFIDE_CODIGO")).append("\"");
                    strXML.append(" />");
                }
                strXML.append("</BasesCt>");
                strXML.toString();
                rs.close();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } //34

            //Regresa los cursos asignados al instructor
            if (strid.equals("35")) {
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<Cursos>");
                String strInstructor = request.getParameter("ID_INSTRC");
                String strSql = "select *,"
                        + "(select CCU_CURSO from cofide_catalogo_curso where cofide_catalogo_curso.CCU_ID_M = cofide_cursos_instructor.CCU_ID_M )as CURSO_DESC "
                        + "from cofide_cursos_instructor where CI_INSTRUCTOR_ID = " + strInstructor + " order by CURSO_DESC";
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {

                    strXML.append("<datos");
                    strXML.append(" CCD_ID = \"").append(rs.getString("CCD_ID")).append("\"");
                    strXML.append(" CURSO_DESC = \"").append(util.Sustituye(rs.getString("CURSO_DESC"))).append("\"");
                    strXML.append(" />");
                }
                strXML.append("</Cursos>");
                strXML.toString();
                rs.close();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } //35

            //Agrega Cursos Disponibles para el Instructor
            if (strid.equals("36")) {
                //Telemarketing tele = new Telemarketing();
                String strResultado = "OK";
                String strInstructor = request.getParameter("ID_INSTRC");
                String strCurso = request.getParameter("ID_CURSO");
                String strQuery = "insert into cofide_cursos_instructor (CCU_ID_M, CI_INSTRUCTOR_ID) values (" + strCurso + "," + strInstructor + ")";
                oConn.runQueryLMD(strQuery);
                if (!oConn.getStrMsgError().equals("")) {
                    strResultado = oConn.getStrMsgError();
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strResultado);//Pintamos el resultado
            } //36

            //Elimina Cursos Disponibles para el Instructor
            if (strid.equals("37")) {
                //Telemarketing tele = new Telemarketing();
                String strResultado = "OK";
                String strIdRg = request.getParameter("ID_REGISTRO");
                String strQuery = "delete from cofide_cursos_instructor where CCD_ID = " + strIdRg;
                oConn.runQueryLMD(strQuery);
                if (!oConn.getStrMsgError().equals("")) {
                    strResultado = oConn.getStrMsgError();
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strResultado);//Pintamos el resultado
            } //37

            //Regresa los giros
            if (strid.equals("38")) {
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<C_GIROS>");
                String strSql = "select * from cofide_giro";
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strXML.append("<datosGiro");
                    strXML.append(" CG_ID_M = \"").append(rs.getInt("CG_ID_M")).append("\"");
                    strXML.append(" CG_GIRO = \"").append(rs.getString("CG_GIRO")).append("\"");
                    strXML.append(" />");
                }

                strSql = "select * from cofide_segmento";
                rs = oConn.runQuery(strSql, true);
                strXML.append("<C_SEGMENTOS>");
                while (rs.next()) {
                    strXML.append("<datosSeg");
                    strXML.append(" CS_ID_M = \"").append(rs.getInt("CS_ID_M")).append("\"");
                    strXML.append(" CS_AREA = \"").append(rs.getString("CS_AREA")).append("\"");
                    strXML.append(" />");
                }
                strXML.append("</C_SEGMENTOS>");

                strSql = "select * from cofide_sede";
                rs = oConn.runQuery(strSql, true);
                strXML.append("<C_SEDE>");
                while (rs.next()) {
                    strXML.append("<datosSede");
                    strXML.append(" CS_SEDE_ID = \"").append(rs.getInt("CS_SEDE_ID")).append("\"");
                    strXML.append(" CS_SEDE = \"").append(rs.getString("CS_SEDE")).append("\"");
                    strXML.append(" />");
                }
                strXML.append("</C_SEDE>");
                strXML.append("</C_GIROS>");
                strXML.toString();
                rs.close();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } //38
            if (strid.equals("39")) {
                String strBaseCT = request.getParameter("CT_BASE");
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<datos>");
                String strSql = "select "
                        + "(select count(*) from vta_cliente where CT_ES_PROSPECTO = 1 and CT_ACTIVO = 1 and CT_CLAVE_DDBB = '" + strBaseCT + "') as prospecto,"
                        + "(select count(*) from vta_cliente where CT_ES_PROSPECTO = 0 and CT_ACTIVO = 1 and CT_CLAVE_DDBB = '" + strBaseCT + "') as cliente "
                        + "from usuarios limit 1";
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strXML.append("<cte");
                    strXML.append(" cliente = \"").append(rs.getString("cliente")).append("\"");
                    strXML.append(" prospecto = \"").append(rs.getString("prospecto")).append("\"");
                    strXML.append(" />");
                }
                strXML.append("</datos>");
                strXML.toString();
                rs.close();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado            
            } //39
            if (strid.equals("40")) { //autocompletar
                String strValorBuscar = request.getParameter("term");
                String strCurso = request.getParameter("Clasifica");
                if (strCurso == null) {
                    strCurso = "1";
                } //si viene vacio la variable, asignarle el 1
                int intCurso = Integer.parseInt(strCurso);
                if (strValorBuscar == null) {
                    strValorBuscar = "";
                }
                //Declaramos objeto json
                JSONArray jsonChild = new JSONArray();
                if (strValorBuscar != "") {
                    String strSql = "select concat(CC_CURSO_ID,' / ',"
                            + "CC_HR_EVENTO_INI,' / ',"
                            + "substring(CC_FECHA_INICIAL,7,2),'-',"
                            + "substring(CC_FECHA_INICIAL,5,2),'-',"
                            + "substring(CC_FECHA_INICIAL,1,4),' / ',"
                            + "CSH_SEDE,' / ',"
                            + "CC_NOMBRE_CURSO) as CC_NOMBRE_CURSO "
                            + "from cofide_cursos LEFT JOIN cofide_sede_hotel on CC_SEDE_ID = CSH_ID "
                            + "where CC_NOMBRE_CURSO like '%" + strValorBuscar + "%' "
                            + "and CC_ACTIVO = 1";
                    if (intCurso == 1) {
                        strSql += " and cc_is_presencial = 1 ";
                    }
                    if (intCurso == 2) {
                        strSql += " and cc_is_online = 1 ";
                    }
                    if (intCurso == 3) {
                        strSql += " and cc_is_video = 1 ";
                    }
                    strSql += "and CC_FECHA_INICIAL >= DATE_SUB(curdate(),INTERVAL 5 day) order by CC_FECHA_INICIAL";
                    ResultSet rsCombo;
                    try {
                        rsCombo = oConn.runQuery(strSql, true);
                        while (rsCombo.next()) {
                            String strCurso1 = rsCombo.getString("cc_nombre_curso");
                            JSONObject objJson = new JSONObject();
                            objJson.put("value", strCurso1);
                            objJson.put("label", strCurso1);
                            jsonChild.put(objJson);
                        }
                        rsCombo.close();
                    } catch (SQLException ex) {
                        ex.fillInStackTrace();
                    }
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(jsonChild.toString());//Pintamos el resultado
            }  //40
            if (strid.equals("41")) { //contabilidad
                boolean bolContabilidad = false;
                String strResp = "OK.";
                String strSql = "select PERF_ID from usuarios where id_usuarios = " + varSesiones.getIntNoUser();
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    if (rs.getInt("PERF_ID") == 33) {
                        bolContabilidad = true;
                    }
                }
                rs.close();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strResp + bolContabilidad);//Pintamos el resultado
            }//41

            if (strid.equals("42")) {

                StringBuilder strXmlHoras = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
                strXmlHoras.append("<tipos>");
                String strTipoMail = request.getParameter("tipo_mail");
                String strSql = "";

                if (!strTipoMail.equals("0")) {
                    if (strTipoMail.equals("1")) { //mensual
                        strSql = "SELECT CC_FECHA_INICIAL, "
                                + "SUBSTRING(CC_FECHA_INICIAL,1,4) as anio, " //anio
                                + "SUBSTRING(CC_FECHA_INICIAL,5,2) as mes," //mes
                                + "(select ME_DESCRIPCION from vta_meses where ME_NUM_1 = mes) as NameMes " //MesName
                                + "from cofide_cursos "
                                + "where CC_ACTIVO = 1 and CC_FECHA_INICIAL >= '" + fec.getFechaActual() + "' "
                                + "group by anio, mes order by anio, mes";
                    }

                    if (strTipoMail.equals("2")) { //sede
                        strSql = "select distinct CC_ALIAS "
                                + "from cofide_cursos "
                                + "where CC_ACTIVO = 1 and CC_FECHA_INICIAL >= '" + fec.getFechaActual() + "' "
                                + "and CC_ALIAS <> '' order by CC_ALIAS";

                    }

                    String strId_tipo = "";
                    String strNameTipo = "";
                    ResultSet rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        if (strTipoMail.equals("1")) { //mensual
                            strId_tipo = rs.getString("mes");
                            strNameTipo = rs.getString("NameMes") + " " + rs.getString("anio");
                        }
                        if (strTipoMail.equals("2")) { //mensual
                            strId_tipo = rs.getString("CC_ALIAS");
                            strNameTipo = rs.getString("CC_ALIAS");
                        }
                        strXmlHoras.append("<tipo ");
                        strXmlHoras.append(" id_tipo = \"").append(strId_tipo).append("\"");
                        strXmlHoras.append(" NameTipo = \"").append(strNameTipo).append("\"");
                        strXmlHoras.append(" />");
                    }
                    rs.close();
                    strXmlHoras.append("</tipos>");
                    out.clearBuffer();//Limpiamos buffer
                    atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                    out.println(strXmlHoras.toString());//Pintamos el resultado
                }
            } //42
            if (strid.equals("43")) {
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<vta>");
                String stridContacto = request.getParameter("CONTACTO_ID");
                String strCCO_NOMBRE = "";
                String strCCO_APPAT = "";
                String strCCO_APMAT = "";
                String strCCO_TITULO = "";
                String strCCO_NOSOCIO = "";
                String strCCO_AREA = "";
                String strCCO_ASOC = "";
                String strCCO_CORREO = "";
                String strCCO_CORREO2 = "";
                String strCCO_TELEFONO = "";
                String strCCO_EXT = "";
                String strCCO_ALTERNO = "";
                double dblTotalPtos = 0.0;
                int intIdContacto = 0;
                String strSqlCTE = "select * from cofide_contactos where CONTACTO_ID = " + stridContacto;
                try {
                    ResultSet rsCTE = oConn.runQuery(strSqlCTE, true);
                    while (rsCTE.next()) {
                        strCCO_NOMBRE = rsCTE.getString("CCO_NOMBRE");
                        strCCO_APPAT = rsCTE.getString("CCO_APPATERNO");
                        strCCO_APMAT = rsCTE.getString("CCO_APMATERNO");
                        strCCO_TITULO = rsCTE.getString("CCO_TITULO");
                        strCCO_NOSOCIO = rsCTE.getString("CCO_NOSOCIO");
                        strCCO_AREA = rsCTE.getString("CCO_AREA");
                        strCCO_ASOC = rsCTE.getString("CCO_ASOCIACION");
                        strCCO_CORREO = rsCTE.getString("CCO_CORREO");
                        strCCO_CORREO2 = rsCTE.getString("CCO_CORREO2");
                        strCCO_TELEFONO = rsCTE.getString("CCO_TELEFONO");
                        strCCO_EXT = rsCTE.getString("CCO_EXTENCION");
                        strCCO_ALTERNO = rsCTE.getString("CCO_ALTERNO");
                        intIdContacto = rsCTE.getInt("CONTACTO_ID");

                        strXML.append("<datos ");
                        strXML.append(" CCO_NOMBRE = \"").append(strCCO_NOMBRE).append("\"");
                        strXML.append(" CCO_APPATERNO = \"").append(strCCO_APPAT).append("\"");
                        strXML.append(" CCO_APMATERNO = \"").append(strCCO_APMAT).append("\"");
                        strXML.append(" CCO_TITULO = \"").append(strCCO_TITULO).append("\"");
                        strXML.append(" CCO_NOSOCIO = \"").append(strCCO_NOSOCIO).append("\"");
                        strXML.append(" CCO_AREA = \"").append(strCCO_AREA).append("\"");
                        strXML.append(" CCO_ASOCIACION = \"").append(strCCO_ASOC).append("\"");
                        strXML.append(" CCO_CORREO = \"").append(strCCO_CORREO).append("\"");
                        strXML.append(" CCO_CORREO2 = \"").append(strCCO_CORREO2).append("\"");
                        strXML.append(" CCO_TELEFONO = \"").append(strCCO_TELEFONO).append("\"");
                        strXML.append(" CCO_EXTENCION = \"").append(strCCO_EXT).append("\"");
                        strXML.append(" CCO_ALTERNO = \"").append(strCCO_ALTERNO).append("\"");
                        strXML.append(" CONTACTO_ID = \"").append(intIdContacto).append("\"");
                        strXML.append(" />");
                    }
                    rsCTE.close();
                } catch (SQLException e) {
                    System.out.println("ERROR: al obtener información del participante/contacto: " + e.getMessage());
                }
                strXML.append("</vta>");
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML.toString());//Pintamos el resultado
            }
            if (strid.equals("44")) {
                // obtiene id maximo para el contacto
                String strRespuesta = "";
                String strComplete = "";
                String strIdContacto = request.getParameter("idcontacto").trim();

                String strNombre = request.getParameter("nombre");
                String strAppat = request.getParameter("appat").trim();
                String strApmat = request.getParameter("apmat").trim();
                String strTitulo = request.getParameter("titulo");
                String strNumSoc = request.getParameter("numero").trim();
                String strAsoc = request.getParameter("asoc").trim();
                String strCorreo = request.getParameter("correo").trim();
                String strMaterial = request.getParameter("material");
                String strSql = "select * from cofide_contactos where CONTACTO_ID = " + strIdContacto;
                ResultSet rs = oConn.runQuery(strSql, true);

                while (rs.next()) {

                    strComplete = "Update cofide_contactos set ";
                    // nombre
                    if (rs.getString("CCO_NOMBRE").equals(strNombre)) {

                        strComplete += "CCO_NOMBRE = '" + rs.getString("CCO_NOMBRE") + "'";

                    } else {

                        strComplete += "CCO_NOMBRE = '" + strNombre + "'";

                    }
                    // appat
                    if (rs.getString("CCO_APPATERNO").equals(strAppat)) {

                        strComplete += ",CCO_APPATERNO = '" + rs.getString("CCO_APPATERNO") + "'";

                    } else {

                        strComplete += ",CCO_APPATERNO = '" + strAppat + "'";

                    }
                    // apmat
                    if (rs.getString("CCO_APMATERNO").equals(strApmat)) {

                        strComplete += ",CCO_APMATERNO = '" + rs.getString("CCO_APMATERNO") + "'";

                    } else {

                        strComplete += ",CCO_APMATERNO = '" + strApmat + "'";

                    }
                    // correo
                    if (rs.getString("CCO_CORREO").equals(strCorreo)) {

                        strComplete += ",CCO_CORREO = '" + rs.getString("CCO_CORREO") + "'";

                    } else {

                        strComplete += ",CCO_CORREO = '" + strCorreo + "'";

                    }
                    // titulo
                    if (rs.getString("CCO_TITULO").equals(strTitulo)) {

                        strComplete += ",CCO_TITULO = '" + rs.getString("CCO_TITULO") + "'";

                    } else {

                        strComplete += ",CCO_TITULO = '" + strTitulo + "'";

                    }
                    // numero
                    if (rs.getString("CCO_NOSOCIO").equals(strNumSoc)) {

                        strComplete += ",CCO_NOSOCIO = '" + rs.getString("CCO_NOSOCIO") + "'";

                    } else {

                        strComplete += ",CCO_NOSOCIO = '" + strNumSoc + "'";

                    }
                    // asociacion
                    if (rs.getString("CCO_ASOCIACION").equals(strAsoc)) {

                        strComplete += ",CCO_ASOCIACION = '" + rs.getString("CCO_ASOCIACION") + "'";

                    } else {

                        strComplete += ",CCO_ASOCIACION = '" + strAsoc + "'";

                    }

                    strSql = strComplete + " where CONTACTO_ID = " + strIdContacto;

                    oConn.runQueryLMD(strSql);

                    if (!oConn.isBolEsError()) {

                        strSql = "UPDATE cofide_participantes SET "
                                + "CP_NOMBRE='" + strNombre + "', CP_APPAT='" + strAppat + "', CP_APMAT='" + strApmat + "', "
                                + "CP_TITULO='" + strTitulo + "', CCO_NOSOCIO='" + strNumSoc + "', CP_ASCOC='" + strAsoc + "', "
                                + "CP_CORREO='" + strCorreo + "' WHERE CONTACTO_ID = " + strIdContacto;

                        oConn.runQueryLMD(strSql);

                        if (!oConn.isBolEsError()) {

                            strRespuesta = "OK";

                        }

                    }

                }

                rs.close();

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRespuesta);//Pintamos el resultado

            } //44
            if (strid.equals("45")) { //ACTUALIZA CONTACTO DIRECTO

                String strResultado = "";
                String strEdicion = request.getParameter("edicion").trim();
                String strIdContacto = request.getParameter("id_contacto").trim();
                String strNombre = request.getParameter("nombre").trim();
                String strAppat = request.getParameter("appat").trim();
                String strApmat = request.getParameter("apmat").trim();
                String strTitulo = request.getParameter("titulo").trim();
                String strSocio = request.getParameter("socio").trim();
                String strArea = request.getParameter("area").trim();
                String strAsoc = request.getParameter("asoc").trim();
                String strCorreo = request.getParameter("correo").trim();
                String strCorreo2 = request.getParameter("correo2").trim();
                String strTelefono = request.getParameter("telefono").trim();
                String strExt = request.getParameter("ext").trim();
                String strAlterno = request.getParameter("alterno").trim();
                String strMail = request.getParameter("mail1").trim();
                String strMail2 = request.getParameter("mail2").trim();
                String strCT_ID = request.getParameter("ct_id").trim();
                String strSql = "";
                if (strEdicion.equals("C")) { //actualizar
                    strSql = "UPDATE cofide_contactos SET "
                            + "CCO_NOMBRE='" + strNombre + "', "
                            + "CCO_APPATERNO='" + strAppat + "', "
                            + "CCO_APMATERNO='" + strApmat + "', "
                            + "CCO_TITULO='" + strTitulo + "', "
                            + "CCO_NOSOCIO='" + strSocio + "', "
                            + "CCO_AREA='" + strArea + "', "
                            + "CCO_ASOCIACION='" + strAsoc + "', "
                            + "CCO_CORREO='" + strCorreo + "', "
                            + "CCO_TELEFONO='" + strTelefono + "', "
                            + "CCO_EXTENCION='" + strExt + "', "
                            + "CCO_ALTERNO='" + strAlterno + "', "
                            + "CT_MAILMES='" + (strMail.equals("SI") ? 1 : 0) + "', "
                            + "CCO_CORREO2='" + strCorreo2 + "', "
                            + "CT_MAILMES2='" + (strMail2.equals("SI") ? 1 : 0) + "' "
                            + "WHERE CONTACTO_ID='" + strIdContacto + "' "
                            + "and CT_ID = " + strCT_ID;
                    oConn.runQueryLMD(strSql);
                    if (!oConn.isBolEsError()) {
                        strResultado = "OK";
                    }
                } else { //insertar
                    if (strIdContacto.equals("0")) {
                        strIdContacto = String.valueOf(tele.getNewIdContact(oConn, strIdContacto));
                    }
                    strSql = "INSERT INTO cofide_contactos "
                            + "(CCO_NOMBRE, CCO_APPATERNO, CCO_APMATERNO, CCO_TITULO, CCO_NOSOCIO, CCO_AREA, "
                            + "CCO_ASOCIACION, CCO_CORREO, CCO_TELEFONO, CCO_EXTENCION, CCO_ALTERNO, CT_ID, "
                            + "CT_MAILMES, CCO_CORREO2, CT_MAILMES2, CONTACTO_ID) "
                            + "VALUES "
                            + "('" + strNombre + "', '" + strAppat + "', '" + strApmat + "', '" + strTitulo + "', '" + strSocio + "', '" + strArea + "', "
                            + "'" + strAsoc + "', '" + strCorreo + "', '" + strTelefono + "', '" + strExt + "', '" + strAlterno + "', '" + strCT_ID + "', "
                            + "'" + strMail + "', '" + strCorreo2 + "', '" + strMail2 + "', '" + strIdContacto + "')";
                    oConn.runQueryLMD(strSql);
                    if (!oConn.isBolEsError()) {
                        strResultado = "OK";
                    }
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strResultado);//Pintamos el resultado
            } //45
            if (strid.equals("46")) { //elimina al registro CONTACTO
                String strIdContacto = request.getParameter("idcontacto");
                String strResp = "";
                String strSql = "delete from cofide_contactos where CONTACTO_ID = " + strIdContacto;
                oConn.runQueryLMD(strSql);
                if (!oConn.isBolEsError()) {
                    strSql = "delete from cofide_puntos_contacto where CONTACTO_ID = " + strIdContacto;
                    oConn.runQueryLMD(strSql);
                    if (!oConn.isBolEsError()) {
                        strResp = "OK";
                    } else {
                        strResp = "Hubó un problema al remover el registro [puntos de lealtad]: " + oConn.getStrMsgError();
                    }
                } else {
                    strResp = "Hubó un problema al remover el registro [contacto]: " + oConn.getStrMsgError();
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strResp);//Pintamos el resultado
            } //46
            if (strid.equals("47")) { //cursos de interes
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
                strXML.append("<vta>");
                String strIdCte = request.getParameter("idCte");
                final String strCurso = URLDecoder.decode(new String(request.getParameter("curso").getBytes("iso-8859-1")), "UTF-8");
                if (!strCurso.equals("")) {
                    //si contiene informaicón, se va a guardar
                    CofideCursoInteres interes = new CofideCursoInteres();
                    int intIdCursoInteres = 0;

                    if (intIdCursoInteres != 0) {
                        interes.ObtenDatos(intIdCursoInteres, oConn);
                    }

                    interes.setFieldString("CCI_FECHA", fec.getFechaActual());
                    interes.setFieldString("CCI_DESCRIPCION", strCurso);
                    interes.setFieldInt("CCI_CT_ID", Integer.parseInt(strIdCte));
                    interes.setFieldInt("CCI_US_ALTA", varSesiones.getIntNoUser());

                    String strRespuesta = "";

                    if (intIdCursoInteres == 0) {
                        strRespuesta = "Agrega: " + interes.Agrega(oConn);
                    } else {
                        strRespuesta = "Modifica: " + interes.Modifica(oConn);
                    }

                }
                if (!strIdCte.equals("0")) {
                    //consulta información
                    String strSql = "select * "
                            + "from cofide_cursos_interes "
                            + "where CCI_CT_ID = " + strIdCte + " "
                            + "order by CCI_DESCRIPCION";
                    try {
                        ResultSet rs = oConn.runQuery(strSql, true);
                        while (rs.next()) {
                            strXML.append("<datos ");
                            strXML.append(" CCI_ID = \"").append(rs.getString("CCI_ID")).append("\"");
                            strXML.append(" CCI_CT_ID = \"").append(rs.getString("CCI_CT_ID")).append("\"");
                            strXML.append(" CCI_FECHA = \"").append(rs.getString("CCI_FECHA")).append("\"");
                            strXML.append(" CCI_US_ALTA = \"").append(rs.getString("CCI_US_ALTA")).append("\"");
                            strXML.append(" CCI_DESCRIPCION = \"").append(util.Sustituye(rs.getString("CCI_DESCRIPCION"))).append("\"");
                            strXML.append(" />");
                        }
                        rs.close();
                    } catch (SQLException sql) {
                        System.out.println("Error al traer información de los cursos sugeridos: " + sql.getMessage());
                    }

                } else {

                    strXML.append("<datos ");
                    strXML.append(" />");

                }

                strXML.append("</vta>");
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML.toString());//Pintamos el resultado
            }
            if (strid.equals("48")) { //autocompletar
                String strValorBuscar = request.getParameter("term");

                if (strValorBuscar == null) {
                    strValorBuscar = "";
                }
                //Declaramos objeto json
                JSONArray jsonChild = new JSONArray();
                if (strValorBuscar != "") {
//                    String strSql = "select DISTINCT CCI_DESCRIPCION from cofide_cursos_interes  where CCI_DESCRIPCION like '%" + strValorBuscar + "%'";
                    String strSql = "select DISTINCT NombreCurso from view_cursos where NombreCurso LIKE '%" + strValorBuscar + "%' order by NombreCurso";

                    ResultSet rsCombo;
                    try {
                        rsCombo = oConn.runQuery(strSql, true);
                        while (rsCombo.next()) {
                            String strCurso1 = rsCombo.getString("NombreCurso");
                            JSONObject objJson = new JSONObject();
                            objJson.put("value", strCurso1);
                            objJson.put("label", strCurso1);
                            jsonChild.put(objJson);
                        }
                        rsCombo.close();
                    } catch (SQLException ex) {
                        ex.fillInStackTrace();
                    }
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(jsonChild.toString());//Pintamos el resultado
            }
        } //fin if strid != null
    }

    oConn.close();
%>


<%!
    public CofideCampanias getCampaniaFiltro(Conexion oConn) {
        CofideCampanias campania = null;
        Fechas fecha = new Fechas();
        String strSql = "select *"
                + ",(select s.CS_AREA from cofide_segmento s where s.CS_ID_M = cofide_campanias.CS_ID_M) as area"
                + ",(select g.CG_GIRO from cofide_giro g where g.CG_ID_M = cofide_campanias.CG_ID_M) as giro "
                + ",(select s.CS_SEDE from cofide_sede s where s.CS_SEDE_ID = cofide_campanias.CS_SEDE_ID) as sede"
                + " from cofide_campanias where CAMP_ACTIVO = 1 "
                + " and '" + fecha.getFechaActual() + "' >= CAMP_FECHA_INI AND '" + fecha.getFechaActual() + "' <= CAMP_FECHA_FIN "
                + " and '" + fecha.getHoraActual() + "'>= CAMP_HORA_INI and '" + fecha.getHoraActual() + "'<=CAMP_HORA_FIN;";
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                campania = new CofideCampanias();
                campania.setIntIdCampania(rs.getInt("CAMP_ID"));
                campania.setIntIdArea(rs.getInt("CS_ID_M"));
                campania.setIntIdGiro(rs.getInt("CG_ID_M"));
                campania.setIntIdSede(rs.getInt("CS_SEDE_ID"));
                campania.setStrArea(rs.getString("area"));
                campania.setStrGiro(rs.getString("giro"));
                campania.setStrSede(rs.getString("sede"));
                campania.setIntProspecto(rs.getInt("CAMP_PROSPECTO"));
                campania.setIntExParticipante(rs.getInt("CAMP_EXPARTICIPANTE"));
                campania.setIntPuntosLealtad(rs.getInt("CAMP_PUNTOS_LEALTAD"));
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error en buscar campañas " + ex.getMessage());
        }
        return campania;
    }

    public boolean saveCampaniaLlamada(Conexion oConn, CofideCampanias campania, int intUser, int intCliente) {
        boolean bolGuardo = false;
        Fechas fecha = new Fechas();
        //Guarda la llamada realizada
        CofideCampaniasLlamadas llamada = new CofideCampaniasLlamadas();
        llamada.setFieldString("CAMR_FECHA", fecha.getFechaActual());
        llamada.setFieldString("CAMR_HORA", fecha.getHoraActual());
        llamada.setFieldInt("CAMR_USER", intUser);
        llamada.setFieldInt("CT_ID", intCliente);
        llamada.setFieldInt("CAMP_ID", campania.getIntIdCampania());
        String strRes = llamada.Agrega(oConn);
        if (strRes.equals("OK")) {
            bolGuardo = true;
        }
        return bolGuardo;
    }

    public boolean updateCampaniaLlamada(Conexion oConn, int intIdCampania, int intUser, int intCliente) {
        boolean bolGuardo = false;
        int intIdReg = 0;
        String strSql = "select * from cofide_campanias_llamadas where CT_ID = " + intCliente + "  AND CAMP_ID = " + intIdCampania;
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                intIdReg = rs.getInt("CAMR_ID");
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error en buscar campañas " + ex.getMessage());
        }
        if (intIdReg != 0) {
            CofideCampaniasLlamadas llamada = new CofideCampaniasLlamadas();
            llamada.ObtenDatos(intIdReg, oConn);
            llamada.setFieldInt("CAMP_REALIZADO", 1);
            String strRes = llamada.Modifica(oConn);
            if (strRes.equals("OK")) {
                bolGuardo = true;
            }
        }
        return bolGuardo;
    }//Fin updateCampaniaLlamada

    public boolean isUserInBound(Conexion oConn, int intNoUser) {
        String strCodigoUser = "";
        boolean isInBound = false;
        try {
            String strSql = "";
            strSql = "select COFIDE_CODIGO from usuarios where id_usuarios = " + intNoUser;
            ResultSet rsCod = oConn.runQuery(strSql, true);
            while (rsCod.next()) {
                strCodigoUser = rsCod.getString("COFIDE_CODIGO");
            }
            rsCod.close();
        } catch (SQLException ex) {
            System.out.println("Es usuario INBOUND: [ERROR:] " + ex.getLocalizedMessage());
        }
//        if (strCodigoUser.equals("BE-INBOUND")) {
        if (strCodigoUser.equals("BE-INBOUND") || strCodigoUser.equals("BE-85")) {
            isInBound = true; //PERMISOS DE INBOUND
        } else {
            isInBound = false;
        }
        return isInBound;
    }

    public double getFacturado(Conexion oConn, int intIdUser) {
        Fechas fec = new Fechas();
        double dblFactura = 0;
        String strSql = "";
        ResultSet rs;
        try {
            strSql = "select (FAC_TOTAL - FAC_IMPUESTO1) as FACTURADO "
                    + "from view_ventasglobales "
                    + "where TIPO_DOC = 'F' and FAC_ANULADA = 0  AND CANCEL = 0 "
                    + "AND left(FAC_FECHA,6) = '" + fec.getFechaActual().substring(0, 6) + "' AND FAC_US_ALTA = " + intIdUser;
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                dblFactura += rs.getDouble("FACTURADO");
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
        }
        return dblFactura;
    }

    public String getPendiente(Conexion oConn, int intIdUser) {
        String strPendiente = "Ninguna";
        String strSql = "";
        ResultSet rs;
        String strDDBB = "";
        strSql = "select COFIDE_CODIGO from usuarios where id_usuarios = " + intIdUser;
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strDDBB = rs.getString("COFIDE_CODIGO");
            }
            rs.close();
            strSql = "select count(*) as Pendiente from vta_cliente_tmp where CT_CLAVE_DDBB = '" + strDDBB + "'";
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strPendiente = rs.getString("Pendiente");
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
        }
        return strPendiente;
    }

//Obtiene la suma de los puntos por paticipante activos
    public double dblGetPuntosCofideContacto(Conexion oConn, int intContactoId) {
        double dblTotalPuntos = 0;
        String strSql = "select sum(CPC_PUNTOS) as TotalPuntos from cofide_puntos_contacto where CPC_ACTIVO = 1 and CONTACTO_ID = " + intContactoId;
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                dblTotalPuntos = rs.getDouble("TotalPuntos");
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error [dblGetPuntosCofideContacto]:" + ex.getLocalizedMessage());
        }
        return dblTotalPuntos;
    }//Fin dblGetPuntosCofideContacto

    /**
     *
     * envio de tarjeta okis o no okis
     */
    public String getEnvio(Conexion oConn, int intContactoId) {
        String strResp = "0";
        String strSql = "select * from cofide_envio_preferente where CONTACTO_ID = " + intContactoId;
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strResp = "1";
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error [bolEnviado]:" + ex.getLocalizedMessage());
        }
        return strResp;
    }

//Obtiene los CT_ID de los contactos con puntos N de lealtad
    public String getStrCtIdsPuntos(Conexion oConn, int intPuntosLealtad, String strBaseUser) {
        String strPuntos = "";
        String strIdsContacto = "";
        String strIdsCliente = "";
        if (intPuntosLealtad == 1000) {
            strPuntos = " >= " + intPuntosLealtad;
        } else {
            strPuntos = " = " + intPuntosLealtad;
        }
        String strSql = "select CONTACTO_ID,sum(CPC_PUNTOS) as SUMA "
                + "from cofide_puntos_contacto "
                + "where CPC_ACTIVO = 1 "
                + "group by CONTACTO_ID "
                + "HAVING SUMA " + strPuntos
                + " order by SUMA DESC";

        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strIdsContacto += rs.getString("CONTACTO_ID") + ",";
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error [getStrCtIdsPuntos_1.0] " + ex.getLocalizedMessage());
        }
        if (strIdsContacto.endsWith(",")) {
            strIdsContacto = strIdsContacto.substring(0, strIdsContacto.length() - 1);
        }

        /*
         * Obtenemos los ID DE LOS CONTACTOS
         */
        if (strIdsContacto.length() > 0) {
            try {
                strSql = "select CT_ID from cofide_contactos where CONTACTO_ID in (" + strIdsContacto + ");";
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strIdsCliente += rs.getString("CT_ID") + ",";
                }
                rs.close();
            } catch (SQLException ex) {
                System.out.println("Error [getStrCtIdsPuntos_2.0] " + ex.getLocalizedMessage());
            }
        }
        if (strIdsCliente.endsWith(",")) {
            strIdsCliente = strIdsCliente.substring(0, strIdsCliente.length() - 1);
        }

        /*
         * Obtenemos los ID DE LOS CONTACTOS
         */
        if (strIdsCliente.length() > 0) {
            try {
                strSql = "select * from vta_cliente where CT_ID in (" + strIdsCliente + ") and CT_ACTIVO = 1;";
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strIdsCliente += rs.getString("CT_ID") + ",";
                }
                rs.close();
            } catch (SQLException ex) {
                System.out.println("Error [getStrCtIdsPuntos_3.0] " + ex.getLocalizedMessage());
            }
        }
        if (strIdsCliente.endsWith(",")) {
            strIdsCliente = strIdsCliente.substring(0, strIdsCliente.length() - 1);
        }

        return strIdsCliente;
    }//fin getStrCtIdsPuntos
%>