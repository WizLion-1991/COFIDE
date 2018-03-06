<%-- 
    Document   : COFIDE_RepoComisiones
    Created on : 31-oct-2016, 10:53:36
    Author     : casajosefa
--%>

<%@page import="comSIWeb.ContextoApt.atrJSP"%>
<%@page import="java.sql.SQLException"%>
<%@page import="comSIWeb.ContextoApt.Seguridad"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page import="comSIWeb.Utilerias.UtilXml"%>
<%@page import="comSIWeb.Utilerias.UtilXml"%>
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
    String strRes = "";
    String strSql = "";
    ResultSet rs = null;

    if (varSesiones.getIntNoUser() != 0 && seg.ValidaURL(request)) {
        String strid = request.getParameter("id");
        if (strid != null) {

            //Consulta el reporte de comisiones por mes y en base a la base de los Ejecutivos
            if (strid.equals("1")) {
                String strMes = request.getParameter("strMes");
                String strAnio = request.getParameter("strAnio");
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<ReporteComision>");
                if (strMes.equals("0")) {
                    strMes = fec.getFechaActual().substring(4, 6);
                }
                strSql = "select FAC_US_ALTA,"
                        + "view_ventasglobales.FAC_TOTAL,"
                        + "FAC_DESCUENTO, "
                        + "TMK_CLAVE, "
                        + "(COFIDE_CODIGO)as CT_BASE, "
                        + "(CG_DESCRIPCION) as EQUIPO, "
                        + "(nombre_usuario)as US_NOMBRE, "
                        + "(select getDblCofideComisionCofide(FAC_US_ALTA,'" + strAnio + strMes + "')) as TOTAL_VENDIDO, "
                        + "(select getDblCofideComisionNvo(FAC_US_ALTA,'" + strAnio + strMes + "',CT_BASE,'1')) as TOTAL_VENDIDO_NVO, "
                        + "(select getDblCofideComisionExp(FAC_US_ALTA,'" + strAnio + strMes + "',CT_BASE,'1')) as TOTAL_VENDIDO_EXP, "
                        + "(select getDblCofideComisionNvo(FAC_US_ALTA,'" + strAnio + strMes + "',CT_BASE,'2')) as TOTAL_VENDIDO_COMISION_NVO, "
                        + "(select getDblCofideComisionExp(FAC_US_ALTA,'" + strAnio + strMes + "',CT_BASE,'2')) as TOTAL_VENDIDO_COMISION_EXP, "
                        + "(select getDblCofideComisionExpMayor(FAC_US_ALTA,'" + strAnio + strMes + "',CT_BASE)) as TOTAL_VENDIDO_EXP_MAYOR "
                        + "from view_ventasglobales, usuarios, cofide_grupo_trabajo "
                        + "where FAC_US_ALTA = id_usuarios and CG_ID = US_GRUPO and "
                        + "left(FAC_FECHA,6) = '" + strAnio + strMes + "' "
                        + "and FAC_ANULADA = 0 "
                        + "and COFIDE_CARRITO = 0 "
                        + "and FAC_COFIDE_VALIDA = 1 "
                        + "group by FAC_US_ALTA "
                        + "order by EQUIPO";
                try {
                    int intTamaño = 0;
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        intTamaño++;
                        //System.out.println();
                        double dblDescuento = 0;
                        double dblTotalVendido = 0;
                        double dblTotalNvo = 0;
                        double dblTotalExp = 0;
                        double dblTotalComisionNvo = 0;
                        double dblTotalComisionExp = 0;
                        double dblTotalComisionExpMayor = 0;

                        if (rs.getString("FAC_DESCUENTO") != null) {
                            dblDescuento = rs.getDouble("FAC_DESCUENTO");
                        }
                        if (rs.getString("TOTAL_VENDIDO") != null) {
                            dblTotalVendido = rs.getDouble("TOTAL_VENDIDO");
                        }
                        if (rs.getString("TOTAL_VENDIDO_NVO") != null) {
                            dblTotalNvo = rs.getDouble("TOTAL_VENDIDO_NVO");
                        }
                        if (rs.getString("TOTAL_VENDIDO_EXP") != null) {
                            dblTotalExp = rs.getDouble("TOTAL_VENDIDO_EXP");
                        }
                        if (rs.getString("TOTAL_VENDIDO_COMISION_NVO") != null) {
                            dblTotalComisionNvo = rs.getDouble("TOTAL_VENDIDO_COMISION_NVO");
                        }
                        if (rs.getString("TOTAL_VENDIDO_COMISION_EXP") != null) {
                            dblTotalComisionExp = rs.getDouble("TOTAL_VENDIDO_COMISION_EXP");
                        }
                        if (rs.getString("TOTAL_VENDIDO_EXP_MAYOR") != null) {
                            dblTotalComisionExpMayor = rs.getDouble("TOTAL_VENDIDO_EXP_MAYOR");
                        }
                        strXML.append("<datos");
                        strXML.append(" CT_BASE = \"").append((rs.getString("CT_BASE") != null ? rs.getString("CT_BASE") : "")).append("\"");
                        strXML.append(" TMK_CLAVE = \"").append((rs.getString("TMK_CLAVE") != null ? rs.getString("TMK_CLAVE") : "")).append("\"");
                        strXML.append(" EQUIPO = \"").append((rs.getString("EQUIPO") != null ? rs.getString("EQUIPO") : "")).append("\"");
                        strXML.append(" US_NOMBRE = \"").append((rs.getString("US_NOMBRE") != null ? util.Sustituye(rs.getString("US_NOMBRE")) : "")).append("\"");
                        strXML.append(" TOTAL_VENDIDO = \"").append(dblTotalVendido).append("\"");
                        strXML.append(" TOTAL_VENDIDO_NVO = \"").append(dblTotalNvo).append("\"");
                        strXML.append(" TOTAL_VENDIDO_EXP = \"").append(dblTotalExp).append("\"");
                        strXML.append(" TOTAL_VENDIDO_COMISION_NVO = \"").append(dblTotalComisionNvo).append("\"");
                        strXML.append(" TOTAL_VENDIDO_COMISION_EXP = \"").append(dblTotalComisionExp).append("\"");
                        strXML.append(" TOTAL_VENDIDO_EXP_MAYOR = \"").append(dblTotalComisionExpMayor).append("\"");
                        strXML.append(" FAC_DESCUENTO = \"").append(dblDescuento).append("\"");
                        strXML.append(" IS_SUPERVISOR = \"").append(0).append("\"");
                        strXML.append(" />");
                    }
                    rs.close();
                    //strXML.append(getStrSupervisorCom(oConn, strMes, strAnio));
                    strXML.append("</ReporteComision>");
                } catch (SQLException ex) {
                    System.out.println("Error GetConsulta Comisiones: " + ex.getLocalizedMessage());
                }
                strXML.toString();

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado

            }//Fin ID 1

            //Consulta el reporte de comisiones por mes y en base a la base de los Supervisores
            if (strid.equals("2")) {
                String strMes = request.getParameter("strMes");
                String strAnio = request.getParameter("strAnio");
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<ReporteComision>");
                if (strMes.equals("0")) {
                    strMes = fec.getFechaActual().substring(4, 6);
                }
                strXML.append(getStrSupervisorCom(oConn, strMes, strAnio));
                strXML.append("</ReporteComision>");
                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado

            }//Fin ID 2
        }
        oConn.close();
    } else {
        out.print("Sin Acceso");
    }


%>
<%!
    ResultSet rs = null;
    String strSql = "";
    UtilXml util = new UtilXml();

    public String getStrSupervisorCom(Conexion oConn, String strMes, String strAnio) {
        StringBuilder strSupervisor = new StringBuilder("");
        strSql = "select US_GRUPO,TMK_CLAVE,nombre_usuario,(CG_DESCRIPCION)as EQUIPO"
                + " from usuarios, cofide_grupo_trabajo  "
                + "where CG_ID = US_GRUPO and IS_SUPERVISOR = 1 and IS_DISABLED = 0";
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                if (!rs.getString("US_GRUPO").equals("")) {
                    double dblComision = getDblImporteVentaequipo(oConn, strMes, strAnio, rs.getString("US_GRUPO"), "0");
                    double dblImporteVenta = getDblImporteVentaequipo(oConn, strMes, strAnio, rs.getString("US_GRUPO"), "1");
                    double dblNuevo = 0.0; //funsion
                    double dblExp = 0.0; //funsion
                    double dblNuevoCom = 0.0; //funsion
                    double dblComisionTotal = dblComision + dblNuevoCom;
                    strSupervisor.append("<datos");
                    strSupervisor.append(" TMK_CLAVE = \"").append(rs.getString("TMK_CLAVE")).append("\"");
                    strSupervisor.append(" EQUIPO = \"").append((rs.getString("EQUIPO") != null ? rs.getString("EQUIPO") : "")).append("\"");
                    strSupervisor.append(" US_NOMBRE = \"").append(util.Sustituye(rs.getString("nombre_usuario"))).append("\"");
                    strSupervisor.append(" TOTAL_VENDIDO = \"").append(dblImporteVenta).append("\"");
                    strSupervisor.append(" TOTAL_VENDIDO_NVO = \"").append(dblNuevo).append("\"");
                    strSupervisor.append(" TOTAL_VENDIDO_EXP = \"").append(dblExp).append("\"");
                    strSupervisor.append(" COMISION_NVO = \"").append(dblNuevoCom).append("\"");
                    strSupervisor.append(" COM_HASTA = \"").append(dblComision).append("\"");
                    strSupervisor.append(" COM_MASDE = \"").append(dblComision).append("\"");
                    strSupervisor.append(" COM_TOT = \"").append(dblComisionTotal).append("\"");
                    strSupervisor.append(" />");
                }
            }
            rs.close();

        } catch (SQLException ex) {
            System.out.println("Error GetConsulta Comisiones Supervisor: " + ex.getLocalizedMessage());
        }
        return strSupervisor.toString();
    }//Fin getStrSupervisorCom

    public double getDblImporteVentaequipo(Conexion oConn, String strMes, String strAnio, String strGrupo, String strConsulta) {
        ResultSet rs2 = null;
        double dblImporteComision = 0;
        strSql = "select round(sum(FAC_IMPORTE-FAC_DESCUENTO),2) as ImporteEquipo "
                + "from view_ventasglobales,usuarios "
                + "where FAC_US_ALTA = id_usuarios and US_GRUPO = '" + strGrupo + "' "
                + "and left(FAC_FECHA,6) = '" + strAnio + strMes + "' "
                + "and FAC_ANULADA = 0 and COFIDE_CARRITO = 0 and FAC_COFIDE_VALIDA = 1";
        try {
            rs2 = oConn.runQuery(strSql, true);
            while (rs2.next()) {
                dblImporteComision = rs2.getDouble("ImporteEquipo");
            }
            rs2.close();
        } catch (SQLException ex) {
            System.out.println("Error GetComisiones Supervisor Importe: " + ex.getLocalizedMessage());
        }
        double dblTmpComision = 0;
        if (dblImporteComision < 700000) {
            dblTmpComision = (dblImporteComision / 100) * .05;
        } else {
            dblTmpComision = (dblImporteComision / 100) * 1;
        }
        /*Si la consulta es 1 devuelve el importe vendido por el equipo
        En caso contrario devuelve el importe de la comision*/
        if (strConsulta.equals("1")) {
            dblTmpComision = dblImporteComision;
        }

        return dblTmpComision;
    }
%>