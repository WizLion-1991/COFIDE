<%-- 
    Document   : COFIDE_MonitoreoRegistro
    Created on : 30/08/2017, 10:21:18 AM
    Author     : Desarrollo_COFIDE
--%>

<%@page import="comSIWeb.ContextoApt.atrJSP"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page import="comSIWeb.Utilerias.UtilXml"%>
<%@page import="comSIWeb.ContextoApt.Seguridad"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<%@page import="comSIWeb.ContextoApt.VariableSession"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%


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
    String strSql = "";
    ResultSet rs = null;

    if (varSesiones.getIntNoUser() != 0 && seg.ValidaURL(request)) {
        String strid = request.getParameter("ID");
        if (strid != null) {
            //Cuenta el numero de exparticipantes y prospectos de acuerdo al filtro
            if (strid.equals("1")) {
                String strBusqueda = request.getParameter("strTextoBusqueda");
                String strFiltro = request.getParameter("filtro");
                String strColumn = "";
                if (strFiltro.equals("1")) {
                    strColumn = "CT_GIRO";
                } else {
                    if (strFiltro.equals("2")) {
                        strColumn = "CT_SEDE";
                    } else {
                        if (strFiltro.equals("3")) {
                            strColumn = "CT_AREA";
                        }
                    }
                }
                String strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                strXML += "<MonitoreoRegistros>";
                strSql = "select nombre_usuario,COFIDE_CODIGO from usuarios where IS_TMK = 1 AND UsuarioActivo = 1 order by COFIDE_CODIGO";
                try {

                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        strXML += "<datos ";
                        strXML += " nombre_usuario = \"" + util.Sustituye(rs.getString("nombre_usuario")) + "\"  "
                                + " COFIDE_CODIGO = \"" + util.Sustituye(rs.getString("COFIDE_CODIGO")) + "\"  "
                                + " PROSPECTOS = \"" + intGetMonitoreoContador(oConn, rs.getString("COFIDE_CODIGO"), strBusqueda, 1, strColumn) + "\" "
                                + " EX_PARTICIPANTES = \"" + intGetMonitoreoContador(oConn, rs.getString("COFIDE_CODIGO"), strBusqueda, 0, strColumn) + "\" "
                                + " />";
                    }
                    rs.next();
                } catch (SQLException ex) {
                    System.out.println("ERROR COFIDE_MonitoreoRegistro_1 [" + ex.getLocalizedMessage() + "}");
                }
                strXML += "</MonitoreoRegistros>";
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            }//Fin ID 1

            if (strid.equals("2")) {
                String strBusqueda = request.getParameter("tipo_filtro");
                strSql = "";
                String strColumn = "";
                if (strBusqueda.equals("1")) {
                    strSql = "select CT_GIRO from vta_cliente group by CT_GIRO;";
                    strColumn = "CT_GIRO";
                } else {
                    if (strBusqueda.equals("2")) {
                        strSql = "select CT_SEDE from vta_cliente group by CT_SEDE;";
                        strColumn = "CT_SEDE";
                    } else {
                        if (strBusqueda.equals("3")) {
                            strSql = "select CT_AREA from vta_cliente group by CT_AREA;";
                            strColumn = "CT_AREA";
                        }
                    }
                }

                String strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                strXML += "<tipo_filtro>";

                try {

                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        strXML += "<datos ";
                        strXML += " TEXT_FILTRO = \"" + util.Sustituye(rs.getString(strColumn)) + "\"  "
                                + " />";
                    }
                    rs.next();
                } catch (SQLException ex) {
                    System.out.println("ERROR COFIDE_MonitoreoRegistro_2 [" + ex.getLocalizedMessage() + "}");
                }
                strXML += "</tipo_filtro>";
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            }//Fin ID 2
        }
    }
    oConn.close();
%>
<%!
    public int intGetMonitoreoContador(Conexion oConn, String strBase, String strBusqueda, int intEsProspecto, String strClasificacion) {
        int intCountMonitoreo = 0;
        String strComplete = "";
        if (strClasificacion.equals("CT_AREA")) {
//            strComplete = " AND " + strClasificacion + " = '" + strBusqueda + "'";
            strComplete = " AND (CT_AREA like '" + strBusqueda + "' OR CT_ID in (select CT_ID from cofide_contactos where CCO_AREA like '" + strBusqueda + "'))";
        } else {
            strComplete = " AND " + strClasificacion + " = '" + strBusqueda + "'";
        }
        try {
            String strSql = "select " + strClasificacion
                    + " from vta_cliente "
                    + " where CT_ES_PROSPECTO = " + intEsProspecto + " "
                    + " and vta_cliente.CT_CLAVE_DDBB = '" + strBase + "' "
                    + strComplete + " AND CT_ACTIVO = 1";
//                    + " AND " + strClasificacion + " = '" + strBusqueda + "'";
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                intCountMonitoreo++;
            }
            rs.next();
        } catch (SQLException ex) {
            System.out.println("ERROR intGetMonitoreoContador:[" + ex.getLocalizedMessage() + "]");
        }
        return intCountMonitoreo;
    }//Fin intGetMonitoreoContador

%>