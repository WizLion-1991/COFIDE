<%-- 
    Document   : COFIDE_PDF_Download
    Created on : 19/12/2017, 10:42:10 AM
    Author     : Desarrollo_COFIDE
--%>


<%@page import="ERP.Ticket"%>
<%@page import="ERP.ERP_MapeoFormato"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.io.BufferedOutputStream"%>
<%@page import="java.io.BufferedInputStream"%>
<%@ page import="comSIWeb.ContextoApt.VariableSession" %>
<%@ page import="comSIWeb.ContextoApt.atrJSP" %>
<%@ page import="comSIWeb.ContextoApt.Seguridad" %>
<%@ page import="comSIWeb.Operaciones.CIP_Form" %>
<%@ page import="Tablas.Usuarios" %>
<%@ page import="comSIWeb.Operaciones.Conexion" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@page contentType="application/octet-stream"%> 
<%@page pageEncoding="UTF-8"%> 
<%
    /*Obtenemos las variables de sesion*/
    VariableSession varSesiones = new VariableSession(request);
    varSesiones.getVars();
    //Abrimos la conexion
    Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
    oConn.open();
    //Instanciamos objeto de seguridad para no permitir el acceso externo a ver los XML
    Seguridad seg = new Seguridad();//Valida que la peticion se halla hecho desde el mismo sitio
    if (varSesiones.getIntNoUser() != 0 && seg.ValidaURL(request)) {
        int intNomId = 0;
        String strNomRazonSocial = "";
        String strNomFecha = "";
        String strNomUUID = "";
        String strFolio = "";
        String strSql = "";
        //Recuperamos el id de la factura
        String strPathXML = this.getServletContext().getInitParameter("PathXml");
        String strPathTKT = this.getServletContext().getInitParameter("PATHTKT");
        /*Definimos parametros de la aplicacion*/
        String strNombre = "";

        String strValorBuscar = "";

        String strPathFile = strPathXML + "XmlSAT" + strValorBuscar + " .xml";

        //no recibio el valor de FAC_ID
        if (request.getParameter("FAC_ID") != null) {
            strValorBuscar = request.getParameter("FAC_ID");
            strNombre = "factura_cfdi_{01}.pdf";

        }
        if (request.getParameter("TKT_ID") != null) {
            strValorBuscar = request.getParameter("TKT_ID");
            strNombre = "ticket_{01}.pdf";
        }

        if (strValorBuscar != null) {

            //Buscamos el folio del documento conforme corresponda
            if (strNombre.startsWith("factura_cfdi_")) {
                strSql = "select FAC_FOLIO,FAC_FOLIO_C, FAC_RAZONSOCIAL,FAC_ID,FAC_FECHA from vta_facturas where FAC_ID = " + strValorBuscar;
            } else if (strNombre.startsWith("ticket_")) {
                strSql = "select TKT_FOLIO from vta_tickets where TKT_ID = " + strValorBuscar;
            }

            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                if (strNombre.startsWith("factura_cfdi_")) {
                    strFolio = rs.getString("FAC_FOLIO");
                } else if (strNombre.startsWith("ticket_")) {
                    strFolio = rs.getString("TKT_FOLIO");
                }

            }
            rs.close();

            strNombre = strNombre.replace("{01}", strFolio);
            System.out.println("archivo a descargar: " + strNombre);
            //Limpiamos  el buffer
            out.clear();
            BufferedInputStream filein = null;
            BufferedOutputStream outputs = null;

            try {
                File file = new File(strPathXML);//specify the file path 

                if (strNombre.startsWith("factura_cfdi_")) {

                    file = new File(strPathXML + strNombre.toString());//specify the file path 
                    System.out.println("ARCHIVO A DESCARGAR: " + strPathXML + strNombre.toString());

                } else if (strNombre.startsWith("ticket_")) {

                    file = new File(strPathTKT + strNombre.toString());//specify the file path 
                    System.out.println("ARCHIVO A DESCARGAR: " + strPathTKT + strNombre.toString());

                }

                if (file.exists()) {
                    
                    byte b[] = new byte[2048];
                    int len = 0;
                    filein = new BufferedInputStream(new FileInputStream(file));
                    outputs = new BufferedOutputStream(response.getOutputStream());
                    response.setHeader("Content-Length", "" + file.length());
                    response.setContentType("application/force-download");
                    response.setHeader("Content-Disposition", "attachment;filename=" + strNombre);
                    response.setHeader("Content-Transfer-Encoding", "binary");
                    while ((len = filein.read(b)) > 0) {
                        outputs.write(b, 0, len);
                        outputs.flush();
                    }
                }

            } catch (Exception e) {
                out.println(e);
            }
        }

    } else {
    }
    oConn.close();
%>


