<%-- 
    Document   : COFIDE_GrupoTrabajo
    Created on : Feb 9, 2016, 12:07:47 PM
    Author     : CasaJosefa
--%>

<%@page import="java.sql.SQLException"%>
<%@page import="comSIWeb.ContextoApt.atrJSP"%>
<%@page import="comSIWeb.Utilerias.UtilXml"%>
<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="comSIWeb.ContextoApt.Seguridad"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<%@page import="comSIWeb.ContextoApt.VariableSession"%>
<%
    /*Obtenemos las variables de sesion*/
    VariableSession varSesiones = new VariableSession(request);
    varSesiones.getVars();
    String strerror = "";
    String strmsg = "";
    //Abrimos la conexion
    Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
    oConn.open();
    //Instanciamos objeto de seguridad para no permitir el acceso externo a ver los XML
    Seguridad seg = new Seguridad();//Valida que la peticion se halla hecho desde el mismo sitio
    if (varSesiones.getIntNoUser() != 0 && seg.ValidaURL(request)) {
        /*Definimos parametros de la aplicacion*/
        String strid = request.getParameter("ID");
        if (strid == null) {
            strid = "0";
        }
        String strSql = "";
        ResultSet rs;
        String strRes = "";
        Fechas fec = new Fechas();
        UtilXml util = new UtilXml();

        if (strid.equals("1")) {
            String strAnio = request.getParameter("strAnio");
            String strMes = request.getParameter("strMes");
            StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
            strXML.append("<ReporteMetasMes>");

            strSql = "";
            strSql = "select *, "
                    + "(select nombre_usuario from usuarios where id_usuarios = cofide_metas_usuario.id_usuarios) as EJECUTIVO, "
                    + "(select TMK_CLAVE from usuarios where id_usuarios = cofide_metas_usuario.id_usuarios) as TMK_CLAVE, "
                    + "(select COFIDE_CODIGO from usuarios where id_usuarios = cofide_metas_usuario.id_usuarios) as COFIDE_CODIGO, "
                    + "(select count(*) from vta_cliente where CT_ES_PROSPECTO = 0 and CT_ACTIVO = 1 and CT_CLAVE_DDBB = COFIDE_CODIGO) as ExParticipantes "
                    + "from cofide_metas_usuario where CME_ANIO = " + strAnio + " and CME_MES = " + strMes + ";";
            String strEjecutivo = "";
            String strTmkClave = "";
            try {
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strEjecutivo = "";
                    strTmkClave = "";

                    if (rs.getString("EJECUTIVO") != null) {
                        strEjecutivo = rs.getString("EJECUTIVO");
                    }
                    if (rs.getString("TMK_CLAVE") != null) {
                        strTmkClave = rs.getString("TMK_CLAVE");
                    }
                    strXML.append("<datos");
                    strXML.append(" TMK_CLAVE = \"").append(util.Sustituye(strTmkClave)).append("\"");
                    strXML.append(" EJECUTIVO = \"").append(util.Sustituye(strEjecutivo)).append("\"");
                    strXML.append(" CMU_NUEVO_CTE = \"").append(rs.getInt("CMU_NUEVO_CTE")).append("\"");
                    strXML.append(" ExParticipantes = \"").append(rs.getInt("ExParticipantes")).append("\"");
                    strXML.append(" CMU_IMPORTE = \"").append(rs.getDouble("CMU_IMPORTE")).append("\"");
                    strXML.append(" CMU_ID = \"").append(rs.getInt("CMU_ID")).append("\"");
                    strXML.append(" id_usuarios = \"").append(rs.getInt("id_usuarios")).append("\"");
                    strXML.append(" />");
                }
                rs.close();
            } catch (SQLException ex) {
                System.out.println("GetMetasMes COFIDE(1) : [ " + ex.getLocalizedMessage() + "]");
            }

            strXML.append("</ReporteMetasMes>");

            strRes = strXML.toString();

            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
            out.println(strRes);//Pintamos el resultado  
        }//Fin ID 1

        //Agrega, Edita o Elimina Partidas de los Ejecutivos
        if (strid.equals("2")) {
            String strAnio = request.getParameter("MET_ANIO");
            String strMes = request.getParameter("MET_MES");
            String strUsuario = request.getParameter("nombre_usuario");
            String strImporte = request.getParameter("CMU_IMPORTE");
            String strNuevosCt = request.getParameter("CMU_NUEVO_CTE");
            String strCMU_ID = "";
            String strElimina = "";

            //Si mandan el ID diferente de NULL o 0
            if (request.getParameter("CMU_ID") != null && request.getParameter("CMU_ID") != "" && request.getParameter("CMU_ID") != "0") {
                strCMU_ID = request.getParameter("CMU_ID");
                //Query para Eliminar una partida
                /*if (request.getParameter("ELIMINA") != null) {
                    strElimina = request.getParameter("ELIMINA");
                    if (strElimina.equals("1")) {
                        strSql = "delete from cofide_metas_usuario where CMU_ID = " + strCMU_ID;
                    }
                } else {*/
                    strSql = "update cofide_metas_usuario set CMU_NUEVO_CTE = " + strNuevosCt + ", CMU_IMPORTE = " + strImporte + " where CMU_ID = " + strCMU_ID;
                //}
            } else {
                strSql = "insert into cofide_metas_usuario ( CMU_NUEVO_CTE, id_usuarios, CME_ANIO, CME_MES, CMU_IMPORTE) "
                        + "values ( " + strNuevosCt + ", " + strUsuario + ", '" + strAnio + "', '" + strMes + "', " + strImporte + ")";
            }

            oConn.runQueryLMD(strSql);

            if (oConn.getStrMsgError().equals("")) {
                strRes = "OK";
            } else {
                strRes = oConn.getStrMsgError();
            }
            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
            out.println(strRes);//Pintamos el resultado

        }//Fin ID 2

        //Consulta los Datos de una Meta en especifico
        if (strid.equals("3")) {
            String strIdMeta = request.getParameter("ID_REGISTRO");
            StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
            strXML.append("<ReporteMetasMesEdita>");
            strSql = "";
            strSql = "select * from cofide_metas_usuario where CMU_ID = " + strIdMeta;

            try {
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strXML.append("<datos");
                    strXML.append(" CMU_NUEVO_CTE = \"").append(rs.getInt("CMU_NUEVO_CTE")).append("\"");
                    strXML.append(" CMU_IMPORTE = \"").append(rs.getDouble("CMU_IMPORTE")).append("\"");
                    strXML.append(" CMU_ID = \"").append(rs.getInt("CMU_ID")).append("\"");
                    strXML.append(" id_usuarios = \"").append(rs.getInt("id_usuarios")).append("\"");
                    strXML.append(" />");
                }
                rs.close();
            } catch (SQLException ex) {
                System.out.println("GetMetasMes COFIDE(3) : [ " + ex.getLocalizedMessage() + "]");
            }
            strXML.append("</ReporteMetasMesEdita>");
            strRes = strXML.toString();

            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
            out.println(strRes);//Pintamos el resultado
        }//Fin ID 3

        //Consulta las metas de los supervisores
        if (strid.equals("4")) {

            String strAnio = request.getParameter("strAnio");
            String strMes = request.getParameter("strMes");
            StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
            strXML.append("<ReporteMetasMesSupervisor>");

            strSql = "select * from cofide_grupo_trabajo;";
            String strRes1 = "";
            try {
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strRes1 = limpiaMetaSupervisor(oConn, strAnio, strMes, rs.getString("CG_ID"));

                    if (strRes1.equals("OK")) {
                        strRes1 = calculaMetaEquipo(oConn, strAnio, strMes, rs.getString("CG_ID"));
                    }
                }
                rs.close();
            } catch (SQLException ex) {
                System.out.println("GetMetasMes COFIDE[4] : [ " + ex.getLocalizedMessage() + "]");
            }

            strSql = "";
            strSql = "select *,"
                    + "(select CG_DESCRIPCION from cofide_grupo_trabajo where cofide_grupo_trabajo.CG_ID = cofide_meta_global.CG_ID)as EJ_BASE "
                    + "from cofide_meta_global where MG_ANIO = " + strAnio + " and MG_MES = " + strMes;
            try {
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strXML.append("<datos");
                    strXML.append(" MG_IMPORTE = \"").append(rs.getDouble("MG_IMPORTE")).append("\"");
                    strXML.append(" EJ_BASE = \"").append(rs.getString("EJ_BASE")).append("\"");
                    strXML.append(" MG_NUEVO_CT = \"").append(rs.getInt("MG_NUEVO_CT")).append("\"");
                    strXML.append(" />");

                }
                rs.close();
            } catch (SQLException ex) {
                System.out.println("GetMetasMes COFIDE(4) : [ " + ex.getLocalizedMessage() + "]");
            }

            strXML.append("</ReporteMetasMesSupervisor>");

            strRes = strXML.toString();

            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
            out.println(strRes);//Pintamos el resultado  
        }//Fin Id 4

        //Elimina una meta asignada a un usuario
        if (strid.equals("5")) {
            String strCMU_ID = request.getParameter("CMU_ID");
            strSql = "delete from cofide_metas_usuario where CMU_ID = " + strCMU_ID;
            oConn.runQueryLMD(strSql);

            if (oConn.getStrMsgError().equals("")) {
                strRes = "OK";
            } else {
                strRes = oConn.getStrMsgError();
    }
            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
            out.println(strRes);//Pintamos el resultado
        }//Fin ID 5

    }
    oConn.close();
%>
<%!
    String strRes = "";
    String strSql = "";
    ResultSet rs = null;

    //Actualiza las metas por equipo
    public String limpiaMetaSupervisor(Conexion oConn, String strAnio, String strMes, String strEquipo) {
        strSql = "delete from cofide_meta_global where MG_ANIO = '" + strAnio + "' and MG_MES = '" + strMes + "' and CG_ID = " + strEquipo;

        oConn.runQueryLMD(strSql);

        strRes = oConn.getStrMsgError();

        if (strRes.equals("")) {
            strRes = "OK";
        }
        return strRes;
    }//Fin limpiaMetaSupervisor

    //Calcula las metas por Equipo
    public String calculaMetaEquipo(Conexion oConn, String strAnio, String strMes, String strEquipo) {
        strSql = "";
        strSql = "select GROUP_CONCAT(CONCAT_WS(',',id_usuarios)) as IDS from usuarios where IS_TMK = 1 and US_GRUPO = " + strEquipo;
        String strUsers = "";
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                if (rs.getString("IDS") != null) {
                    strUsers = rs.getString("IDS");
                }
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("GetMetasMes COFIDE(calculaMetaEquipo) : [ " + ex.getLocalizedMessage() + "]");
        }

        double dblImporteMeta = 0;
        int intNuevosCt = 0;
        if (!strUsers.equals("")) {
            strSql = "";
            strSql = "select sum(CMU_IMPORTE) as ImporteMetaTotal, sum(CMU_NUEVO_CTE) as NuevosCtMeta from cofide_metas_usuario where id_usuarios in (" + strUsers + ");";

            try {
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    dblImporteMeta = rs.getDouble("ImporteMetaTotal");
                    intNuevosCt = rs.getInt("NuevosCtMeta");
                }
                rs.close();
            } catch (SQLException ex) {
                System.out.println("GetMetasMes COFIDE(calculaMetaEquipo_2) : [ " + ex.getLocalizedMessage() + "]");
            }

            strSql = "";
            strSql = "insert into cofide_meta_global ( MG_MES, MG_IMPORTE, CG_ID, MG_NUEVO_CT, MG_ANIO) "
                    + "values ( '" + strMes + "', " + dblImporteMeta + ", " + strEquipo + ", " + intNuevosCt + ", '" + strAnio + "')";

            oConn.runQueryLMD(strSql);

            strRes = oConn.getStrMsgError();

            if (strRes.equals("")) {
                strRes = "";
            }
        }
        return strRes;
    }//Fin calculaMetaEquipo

%>
