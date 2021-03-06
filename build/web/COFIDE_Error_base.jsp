<%-- 
    Document   : COFIDE_Error_base
    Created on : 16/11/2016, 09:47:34 AM
    Author     : Administrador
--%>

<%@page import="java.sql.SQLException"%>
<%@page import="comSIWeb.ContextoApt.atrJSP"%>
<%@page import="comSIWeb.Utilerias.UtilXml"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page import="comSIWeb.ContextoApt.Seguridad"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<%@page import="comSIWeb.ContextoApt.VariableSession"%>
<%
    /*Obtenemos las variables de sesion*/
    VariableSession varSesiones = new VariableSession(request);
    varSesiones.getVars();
    //Abrimos la conexion
    Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
    oConn.open();
    //Instanciamos objeto de seguridad para no permitir el acceso externo a ver los XML
    Seguridad seg = new Seguridad(); //Valida que la peticion se halla hecho desde el mismo sitio
    Fechas fec = new Fechas();
    UtilXml util = new UtilXml();
    String strSQL;
    ResultSet rs;
    String strResult = "";
    StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
    strXML.append("<vta>");

    if (varSesiones.getIntNoUser() != 0 && seg.ValidaURL(request)) {
        //Obtenemos parametros
        String strid = request.getParameter("id");
        if (!strid.equals(null)) {
            if (strid.equals("1")) {
                String strMes = request.getParameter("mes");
//                String strEquipo = request.getParameter("grupo");
                String strAnio = request.getParameter("anio");
                boolean bolLista = false;
                int intErrorBase = 0;
                int intIDejecutivo = 0;
                double dblMuestra = 0.0;
                int intMuestra = 0;
                String strGrupo = "";
                int intGrupo = GetGrupoUsr(oConn, varSesiones.getIntNoUser());
                if (intGrupo != 0) {
                    strGrupo = " and ERR_GRUPO = " + intGrupo;
                }
                //vemos que no haya sido listado los registros del mes, si ya esta listado mandara una alerta
                strSQL = "select ERR_LISTA from cofide_err_base "
                        + "where LEFT(ERR_FECHA,6) = '" + strAnio + strMes + "' and ERR_LISTA = 1 "
                        + strGrupo;
//                        + "ERR_GRUPO = " + GetGrupoUsr(oConn, varSesiones.getIntNoUser());
                rs = oConn.runQuery(strSQL, true);
                while (rs.next()) {
                    bolLista = true;
                }
                rs.close();
                //obtenemos a los 3 ejecutivos con mas error en base
                strSQL = "select count(*) as cuantos, ERR_USUARIO "
                        + "from cofide_err_base where LEFT(ERR_FECHA,6) = '" + strAnio + strMes + "' "
                        //                        + "and ERR_GRUPO = " + GetGrupoUsr(oConn, varSesiones.getIntNoUser()) + " "
                        + strGrupo
                        + " group by ERR_USUARIO "
                        + "order by cuantos desc limit 3";
                rs = oConn.runQuery(strSQL, true);
                while (rs.next()) {
                    intErrorBase = rs.getInt("cuantos");
                    intIDejecutivo = rs.getInt("ERR_USUARIO");
                    if (intErrorBase > 1) {
                        dblMuestra = intErrorBase * .02;
                        if (dblMuestra > 1) {
                            intMuestra = (int) dblMuestra;
                        } else {
                            intMuestra = 1;
                        }
                    } else {
                        intMuestra = intErrorBase;
                    }
                    System.out.println(intErrorBase + " cuantos");
                    System.out.println(dblMuestra + " muestra en dbl");
                    System.out.println(intMuestra + " muestra en int");
                    //obtenemos la muestra de los error en base de cada ejecutvo
                    String strSql2 = "select cofide_err_base.*, usuarios.nombre_usuario, vta_cliente.CT_TELEFONO1 "
                            + "from cofide_err_base,usuarios,vta_cliente "
                            + "where LEFT(ERR_FECHA,6) = '" + strAnio + strMes + "' and ERR_USUARIO = " + intIDejecutivo + " "
                            + "and ERR_USUARIO = id_usuarios "
                            + "and cofide_err_base.CT_ID = vta_cliente.CT_ID "
                            + "limit " + intMuestra;
                    ResultSet rs2 = oConn.runQuery(strSql2, true);
                    while (rs2.next()) {
                        strXML.append("<datos");
                        strXML.append(" listado = \"").append(rs2.getString("ERR_LISTA")).append("\"");
                        strXML.append(" editado = \"").append(rs2.getString("ERR_EDITADO")).append("\"");
                        strXML.append(" ejecutivo = \"").append(rs2.getString("nombre_usuario")).append("\"");
                        strXML.append(" id_cte = \"").append(rs2.getString("CT_ID")).append("\"");
                        strXML.append(" razon = \"").append(rs2.getString("ERR_RAZONSOCIAL")).append("\"");
                        strXML.append(" telefono = \"").append(rs2.getString("CT_TELEFONO1")).append("\"");
                        strXML.append(" clasifica = \"").append(rs2.getString("ERR_CLASIFICA")).append("\"");
                        strXML.append(" observacion = \"").append(rs2.getString("ERR_OBSERVACION")).append("\"");
                        strXML.append(" />");
                    }
                    rs2.close();
                }
                rs.close();
                //terminando de listar,, los marca como ya procesados
                strSQL = "update cofide_err_base set ERR_LISTA = 1 "
                        + "where LEFT(ERR_FECHA,6) = '" + strAnio + strMes + "' and ERR_GRUPO = " + GetGrupoUsr(oConn, varSesiones.getIntNoUser());
                oConn.runQueryLMD(strSQL);
                strXML.append("</vta>");
                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado 
            } //1
            if (strid.equals("2")) {
                String strIdCte = request.getParameter("idCte");
                String strComent = request.getParameter("comentario");
                String strClass = request.getParameter("clasificacion");
                strSQL = "UPDATE cofide_err_base SET ERR_CLASIFICA = '" + strClass + "', ERR_OBSERVACION = '" + strComent + "' "
                        + "WHERE CT_ID = " + strIdCte;
                oConn.runQueryLMD(strSQL);
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
            }
            if (strid.equals("3")) {
                String strMes = request.getParameter("mes");
//                String strEquipo = request.getParameter("grupo");
                String strAnio = request.getParameter("anio");
                strSQL = "update cofide_err_base set ERR_EDITADO = 1 "
                        + "where LEFT(ERR_FECHA,6) = '" + strAnio + strMes + "' and ERR_GRUPO = " + GetGrupoUsr(oConn, varSesiones.getIntNoUser());
                oConn.runQueryLMD(strSQL);
            }
            if (strid.equals("4")) {
                //imprime reporte de error en base, obtenemos el id del grupo al que pertenece el sueprvisor
                int intEquipo = GetGrupoUsr(oConn, varSesiones.getIntNoUser());
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(intEquipo);//Pintamos el resultado 
            }

        }
    }
%>

<%!
    public int GetGrupoUsr(Conexion oConn, int intIdUsr) {
        int intGrupo = 0;
        String strSql = "select US_GRUPO from usuarios where id_usuarios = " + intIdUsr;
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                intGrupo = rs.getInt("US_GRUPO");
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("ERROR: al obtener el grupo del usuario " + ex);
        }
        return intGrupo;
    }
%>