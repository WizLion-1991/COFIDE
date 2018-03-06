<%-- 
    Document   : COFIDE_GetSetPlantilla
    Created on : 13/09/2017, 11:48:54 AM
    Author     : Desarrollo_COFIDE
--%>

<%@page import="com.mx.siweb.erp.especiales.cofide.CRM_Envio_Template"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.DataInputStream"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="java.io.File"%>
<%@page import="comSIWeb.ContextoApt.VariableSession" %>
<%@page import="comSIWeb.ContextoApt.atrJSP" %>
<%@page import="comSIWeb.ContextoApt.Seguridad" %>
<%@page import="comSIWeb.Operaciones.Conexion" %>
<%
    /*Obtenemos las variables de sesion*/
    VariableSession varSesiones = new VariableSession(request);
    varSesiones.getVars();
    Fechas fec = new Fechas();

    String strNomDoc = request.getParameter("name");

    //Instanciamos objeto de seguridad para no permitir el acceso externo a ver los XML
    Seguridad seg = new Seguridad();//Valida que la peticion se halla hecho desde el mismo sitio
    String strmsg = "";
    String strerror = "";
    String strPathUsado = "";
    if (varSesiones.getIntNoUser() != 0 && seg.ValidaURL(request)) {
        String strPathBase = this.getServletContext().getRealPath("/");
        String strSeparator = System.getProperty("file.separator");
        if (strSeparator.equals("\\")) {
            strSeparator = "/";
            strPathBase = strPathBase.replace("\\", "/");
        }
        Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
        oConn.open();
        //Si la peticion no fue nula proseguimos
        atrJSP.atrJSP(request, response, true, false);
        out.clearBuffer();
        Fechas fecha = new Fechas();
        String strFecha = fecha.getFechaActual().substring(0, 6);

        //Instanciamos objeto para subir archivos....
        //Validamos si la peticion se genero con enctype
        if (ServletFileUpload.isMultipartContent(request)) {
            ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
            List fileItemsList = servletFileUpload.parseRequest(request);
            Iterator it = fileItemsList.iterator();
            //Procesamos los archivos subidos
            while (it.hasNext()) {
                FileItem fileItem = (FileItem) it.next();
                if (fileItem.isFormField()) {
                } else {
                    /* The file item contains an uploaded file */
 /* Get the name attribute value of the <input type="file"> element. */
                    String fieldName = fileItem.getFieldName();
                    /* Get the size of the uploaded file in bytes. */
                    long fileSize = fileItem.getSize();
                    /* Get the name of the uploaded file at the client-side. Some browsers such as IE 6 include the whole path here (e.g. e:\files\myFile.txt), so you may need to extract the file name from the path. This information is provided by the client browser, which means you should be cautious since it may be a wrong value provided by a malicious user. */
                    String fileName = fileItem.getName();
                    String strFileExtension = "";

                    if (fileName.toLowerCase().endsWith(".txt")) {
                        strFileExtension = fileName.substring(fileName.length() - 3, fileName.length());
                    } else {
                        strFileExtension = fileName.substring(fileName.length() - 4, fileName.length());
                    }
                    /* Get the content type (MIME type) of the uploaded file. This information is provided by the client browser, which means you should be cautious since it may be a wrong value provided by a malicious user. */
                    String contentType = fileItem.getContentType();
                    //Validamos que suban solo archivos pdf
                    if (fileName.toLowerCase().endsWith(".txt") || fileName.toLowerCase().endsWith(".html")) {
                        //Separamos el nombre del archivo                        
                        if (fileName.contains("\\") && request.getHeader("User-Agent").contains("MSIE")) {
                            fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
                        }
                        //Asignamos el nombre del archivo                        
                        String strNombre = "Plantilla_" + strNomDoc;
                        strPathUsado = strPathBase + "document" + strSeparator + "Plantilla" + strSeparator + strNombre + "." + strFileExtension;

//                        System.out.println("################## nombre del archivo: " + strNombre);
                        //Guardamos el archivoa
                        File saveTo = new File(strPathUsado);
                        fileItem.write(saveTo);
                        if (saveTo.exists()) {
                            strmsg = "OK";
                        }
                        if (strmsg.equals("OK")) {
//                            strmsg = strNombre + "." + strFileExtension;
                            if (SetPlantilla(oConn, strPathUsado)) {
                                strmsg = strNombre + "." + strFileExtension;
                            } else {
                                strmsg = "ocurrio un problema al actualizar la plantilla";
                            }
                            System.out.println("###############\n" + strmsg);
                        }
                    }
                }
            }
        } else {
            strerror = "La petición no viene de  manera enctype";
        }
    }
    out.clear();
    out.println("{");
    out.println("error: '" + strerror + "',\n");
    out.println("msg: '" + strmsg + "'\n");
    out.println("}\n");
%>

<%!
    /**
     * OBTENEMOS EL CONTENIDO DEL ARCHIVO
     */
//    public String getContainTemplate(String stPathUsado) {
//        String strContenido = "";
//        if (!stPathUsado.equals("")) {
//            try {
//                // Abrimos el archivo
////                FileInputStream fstream = new FileInputStream("C:/Users/Desarrollo_COFIDE/Documents/NetBeansProjects/COFIDE/build/web/document/Plantilla/Plantilla_cuacu.html");
//                FileInputStream fstream = new FileInputStream(stPathUsado);
////                FileInputStream fstream = new FileInputStream("C:\\Users\\Desarrollo_COFIDE\\Desktop\\HTML\\archivo.txt");
//                // Creamos el objeto de entrada
//                DataInputStream entrada = new DataInputStream(fstream);
//                // Creamos el Buffer de Lectura
//                BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));
//                String strLinea;
//                // Leer el archivo linea por linea
//                while ((strLinea = buffer.readLine()) != null) {
//                    // Imprimimos la línea por pantalla
//                    strContenido += strLinea + "\n";
//                }
//                // Cerramos el archivo
//                entrada.close();
//            } catch (Exception e) { //Catch de excepciones
//                System.err.println("Ocurrio un error: " + e.getMessage());
//            }
//        } else {
//            strContenido = "No se encontro el contenido...\nFavor de subirlo nuevamente.";
//        }
//        System.out.println("##plantilla desde la funcion:;\n" + strContenido);
//        return strContenido;
//    }
    /**
     * guarda la plantilla en la BD
     */
    CRM_Envio_Template template = new CRM_Envio_Template();

    public boolean SetPlantilla(Conexion oConn, String strPathusado) {
        boolean bolResp = false;
        //guardado en la BD
        String strresp = "";
        String strPlantilla = "";
//        strPlantilla = getContainTemplate(strPathusado);        
        strPlantilla = template.getContainTemplate(strPathusado);
        System.out.println("###plantilla con el getHTML;\n" + strPlantilla);
        String strSql = "UPDATE mailtemplates SET MT_CONTENIDO='" + strPlantilla + "' WHERE MT_ID=61";
        oConn.runQueryLMD(strSql);
        if (!oConn.isBolEsError()) {
            strresp = "OK";
            bolResp = true;
        } else {
            strresp = "Hubó un error al actualizar la plantilla: [ " + oConn.getStrMsgError() + " ]";
            System.out.println(strresp);
        }
        oConn.close();
        //guardado en la BD
        return bolResp;
    }
%>