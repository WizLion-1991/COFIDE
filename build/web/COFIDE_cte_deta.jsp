<%-- 
    Document   : COFIDE_cte_deta
    Created on : 22/04/2017, 02:43:23 PM
    Author     : Desarrollo_COFIDE
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <title>DETALLES DEL CLIENTE</title>
        <style>

            /* tabs */
            body {font-family: "Lato", sans-serif;}

            /* Style the tab */
            div.tab {
                overflow: hidden;
                border: 1px solid #fff;
                background-color: #252525;
            }

            /* Style the buttons inside the tab */
            div.tab button {
                background-color: inherit;
                float: left;
                border: none;
                outline: none;
                cursor: pointer;
                padding: 14px 16px;
                transition: 0.3s;
                font-size: 17px;
                color: #fff;
            }

            /* Change background color of buttons on hover */
            div.tab button:hover {
                background-color: #ddd;
                color: #000;
            }

            /* Create an active/current tablink class */
            div.tab button.active {
                background-color: #ccc;
                color: #000;
            }

            /* Style the tab content */
            .tabcontent {
                display: none;
                padding: 6px 12px;
                -webkit-animation: fadeEffect 1s;
                animation: fadeEffect 1s;
            }

            /* Fade in tabs */
            @-webkit-keyframes fadeEffect {
                from {opacity: 0;}
                to {opacity: 1;}
            }

            @keyframes fadeEffect {
                from {opacity: 0;}
                to {opacity: 1;}
            }
            .titulo{
                background-color: black;
                color: #99cc00;
                font-size: xx-large;
            }
            /* tabs */

            /* table */
            /* CONTACTOS */
            .table-fixed thead {
                width: 100%;
            }
            .table-fixed tbody {
                height: 350px;
                overflow-y: auto;
                width: 100%;
            }
            .table-fixed thead, .table-fixed tbody, .table-fixed tr, .table-fixed td, .table-fixed th {
                display: block;
            }
            .table-fixed tbody td, .table-fixed thead > tr> th {
                float: left;
                border-bottom-width: 0;
            }
            /*RAZON SOCIAL*/
            .table-fixed2 thead {
                width: 97%;
            }
            .table-fixed2 tbody {
                height: 150px;
                overflow-y: auto;
                width: 100%;
            }
            .table-fixed2 thead, .table-fixed2 tbody, .table-fixed2 tr, .table-fixed2 td, .table-fixed2 th {
                display: block;
            }
            .table-fixed2 tbody td, .table-fixed2 thead > tr> th {
                float: left;
                border-bottom-width: 0;
            }
            /*VENTAS*/
            .table-fixed3 thead {
                width: 97%;
            }
            .table-fixed3 tbody {
                height: 600px;
                overflow-y: auto;
                width: 100%;
            }
            .table-fixed3 thead, .table-fixed3 tbody, .table-fixed3 tr, .table-fixed3 td, .table-fixed3 th {
                display: block;
            }
            .table-fixed3 tbody td, .table-fixed3 thead > tr> th {
                float: left;
                border-bottom-width: 0;
            }
            /* table */
            .contactos{
                font-size: 10px;
            }
            .ventas{
                font-size: 8px;
            }
        </style>
    </head>
    <body>
        <%
            Conexion oConn = new Conexion();
            Fechas fec = new Fechas();
            oConn.open();
            if (request.getParameter("id_cte") != null) {
                if (!request.getParameter("id_cte").isEmpty()) {
                    String strIdCte = request.getParameter("id_cte");
                    String strSql = "";
                    ResultSet rs;
                    boolean bolContacto = false;
                    boolean bolRazonSocial = false;
                    boolean bolVentas = false;
                    //cliente
                    String strRazonSocial = "";
                    String strRFC = "";
                    String strSede = "";
                    String strGiro = "";
                    String strArea = "";
                    String strNombre = "";
                    String strTelefono = "";
                    String strTelefono2 = "";
                    String strConmutador = "";
                    String strCorreo = "";
                    String strCorreo2 = "";
                    String strCp = "";
                    String strColonia = "";
                    String strCalle = "";
                    String strEstado = "";
                    String strMunicipio = "";
                    String strNumExt = "";
                    String strNumInt = "";
                    String strProspecto = "";
                    //contactos
                    String strTitulo = "-";
                    String strNombreC = "-";
                    String strAppat = "-";
                    String strApmat = "-";
                    String strNum = "-";
                    String strAsoc = "-";
                    String strAreaC = "-";
                    String strCorreoC = "-";
                    String strCorreoC2 = "-";
                    String strTelefonoC = "-";
                    String strTelefonoC2 = "-";
                    String strExtC = "-";
                    //razonsocial
                    String strRazonSocialR = "";
                    //ventas
                    String strIdVta = "";
                    String strDocumento = "";
                    String strFecha = "";
                    String strHora = "";
                    String strRazonSocialV = "";
                    String strAgente = "";
                    String strTotal = "";
                    String strPagado = "";
                    String strEstatus = "";
                    // inicia la consulta de cliente
                    strSql = "select * from vta_cliente where CT_ID = " + strIdCte;
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        strRazonSocial = rs.getString("CT_RAZONSOCIAL");
                        strRFC = rs.getString("CT_RFC");
                        strSede = rs.getString("CT_SEDE");
                        strGiro = rs.getString("CT_GIRO");
                        strArea = rs.getString("CT_AREA");
                        strNombre = rs.getString("CT_CONTACTO1");
                        strTelefono = rs.getString("CT_TELEFONO1");
                        strTelefono2 = rs.getString("CT_TELEFONO2");
                        strConmutador = rs.getString("CT_CONMUTADOR");
                        strCorreo = rs.getString("CT_EMAIL1");
                        strCorreo2 = rs.getString("CT_EMAIL2");
                        strCp = rs.getString("CT_CP");
                        strColonia = rs.getString("CT_COLONIA");
                        strCalle = rs.getString("CT_CALLE");
                        strEstado = rs.getString("CT_ESTADO");
                        strMunicipio = rs.getString("CT_MUNICIPIO");
                        strNumExt = rs.getString("CT_NUMERO");
                        strNumInt = rs.getString("CT_NUMINT");
                        strProspecto = rs.getString("CT_ES_PROSPECTO");
                    }
                    rs.close();
                    // termina la ocnsulta de cliente  
                    String strColor = "#848484";
                    String strTipo = "Prospecto";
                    if (strProspecto.equals("0")) {
                        strColor = "#99CC00";
                        strTipo = "Ex-Participante";
                    }
        %>
        <script>
            function Historial() {
                var strCte = document.getElementById("id_cte").value;
                open('COFIDE_Historial_llamadas.jsp?CT_ID=' + strCte, '', 'top=100,left=300,width=1000,height=600');
            }
            function Salir() {
                window.close();
            }
        </script>
        <div class="titulo">
            INFORMACIÓN DEL CLIENTE: <%=strIdCte%>
        </div>
        <!-- botones historial y cerrar -->
        <div style="padding-left: 2%;"><table>
                <tr>
                    <td>
                        <div>
                            <a href="javascript:Historial()">
                                <i class="fa fa-clock-o" title="Historial de llamadas" style="font-size:55px; color: #00D827" aria-hidden="true" ></i>
                            </a>
                        </div>
                    </td>
                    <td>
                        &nbsp;
                        &nbsp;
                        &nbsp;
                        &nbsp;
                        &nbsp;
                    </td>
                    <td>
                        <div>
                            <a href="javascript:Salir()">
                                <i class="fa fa-sign-out" title="Salir" style="font-size:55px; color: #FF0000" aria-hidden="true" ></i>
                            </a>
                        </div>
                    </td>
                </tr>
            </table></div>
        <!-- fin botones historial y cerrar -->
        <div class="tab">
            <button class="tablinks" onclick="openCte(event, 'ClienteTab')" id="defaultOpen">LLAMADA ACTUAL</button>
            <button class="tablinks" onclick="openCte(event, 'ContactoTab')">CONTACTOS</button>
            <button class="tablinks" onclick="openCte(event, 'VentasTab')">HISTORIAL DE VENTAS</button>
        </div>

        <div id="ClienteTab" class="tabcontent">
            <!-- es cliente -->
            <div style="padding-left: 2%"><table><tr><td>
                            <div style="color: <%=strColor%>">
                                <i class="fa fa-user" title="<%=strTipo%>" style="font-size:55px;" aria-hidden="true"></i>
                            </div>
                        </td></tr></table></div>
            <!-- fin es cliente -->
            <!-- contenido del cliente -->
            <div  class="form-group">
                <div class="col-xs-1">
                    <label for="id_cte">ID CLIENTE</label>
                    <input type="text" class="form-control input-sm" id="id_cte" readonly value=<%=strIdCte%> />
                </div>
                <br><br><br>
                <div class="col-xs-4 col-md-4">
                    <label for="razonsocial">RAZÓN SOCIAL</label>
                    <input type="text" class="form-control input-sm" id="razonsocial" readonly value="<%=strRazonSocial%>" />
                </div>
                <div class="col-xs-2 col-md-2">
                    <label for="rfc">RFC</label>
                    <input type="text" class="form-control input-sm" id="rfc" readonly value="<%=strRFC%>" />
                </div>
            </div>
            <br><br><br>
            <div  class="form-group">
                <div class="col-xs-2">
                    <label for="sede">SEDE</label>
                    <input type="text" class="form-control input-sm" id="sede" readonly value="<%=strSede%>" />
                </div>
                <div class="col-xs-2">
                    <label for="giro">GIRO</label>
                    <input type="text" class="form-control input-sm" id="giro" readonly value="<%=strGiro%>" />
                </div>
                <div class="col-xs-2">
                    <label for="area">ÁREA</label>
                    <input type="text" class="form-control input-sm" id="area" readonly value="<%=strArea%>" />
                </div>
            </div>
            <br><br><br>
            <div  class="form-group">
                <div class="col-xs-2">
                    <label for="nombre">NOMBRE</label>
                    <input type="text" class="form-control input-sm" id="nombre" readonly value="<%=strNombre%>" />
                </div>
                <div class="col-xs-2">
                    <label for="contacto">TELÉFONO</label>
                    <input type="text" class="form-control input-sm" id="contacto" readonly value="<%=strTelefono%>" />
                </div>
                <div class="col-xs-2">
                    <label for="otro_cont">OTRO</label>
                    <input type="text" class="form-control input-sm" id="otro_cont" readonly value="<%=strTelefono2%>" />
                </div>
            </div>
            <br><br><br>
            <div  class="form-group">
                <div class="col-xs-1">
                    <label for="conmutador">CONMUTADOR</label>
                    <input type="text" class="form-control input-sm" id="conmutador" readonly value="<%=strConmutador%>" />
                </div>
                <div class="col-xs-2">
                    <label for="correo">CORREO</label>
                    <input type="text" class="form-control input-sm" id="correo" readonly value="<%=strCorreo%>" />
                </div>
                <div class="col-xs-2">
                    <label for="correo_alt">CORREO ALTERNO</label>
                    <input type="text" class="form-control input-sm" id="correo_alt" readonly value="<%=strCorreo2%>" />
                </div>
            </div>
            <br><br><br>
            <div  class="form-group">
                <div class="col-xs-1">
                    <label for="conmutador">CP</label>
                    <input type="text" class="form-control input-sm" id="conmutador" readonly value="<%=strCp%>" />
                </div>
                <div class="col-xs-2">
                    <label for="correo">COLONIA</label>
                    <input type="text" class="form-control input-sm" id="correo" readonly value="<%=strColonia%>" />
                </div>
                <div class="col-xs-2">
                    <label for="calle">CALLE</label>
                    <input type="text" class="form-control input-sm" id="calle" readonly value="<%=strCalle%>" />
                </div>
                <br><br><br>
                <div class="col-xs-1">
                    <label for="estado">ESTADO</label>
                    <input type="text" class="form-control input-sm" id="estado" readonly value="<%=strEstado%>" />
                </div>
                <div class="col-xs-2">
                    <label for="municipio">MUNICIPIO</label>
                    <input type="text" class="form-control input-sm" id="municipio" readonly value="<%=strMunicipio%>" />
                </div>
                <div class="col-xs-1">
                    <label for="numext">NUM. EXT.</label>
                    <input type="text" class="form-control input-sm" id="numext" readonly value="<%=strNumExt%>" />
                </div>
                <div class="col-xs-1">
                    <label for="numint">NUM. INT.</label>
                    <input type="text" class="form-control input-sm" id="numint" readonly value="<%=strNumInt%>" />
                </div>
            </div>
            <!-- fin contenido del cliente -->
        </div>

        <div id="ContactoTab" class="tabcontent">
            <!-- tabla -->
            <!-- tabla contactos -->
            <div class="container">
                <div class="row">
                    <div class="panel panel-default contactos">
                        <div class="panel-heading">
                            <h4>
                                CONTACTOS
                            </h4>
                        </div>
                        <table class="table table-fixed">
                            <thead>
                                <tr>
                                    <th class="col-xs-2">TITULO</th>
                                    <th class="col-xs-2">NOMBRE</th>
                                    <th class="col-xs-1">AP. PATERNO</th>
                                    <th class="col-xs-1">AP. MATERNO</th>
                                    <th class="col-xs-2">CORREO</th>
                                    <th class="col-xs-1">CORREO ALT.</th>
                                    <th class="col-xs-1">TELÉFONO</th>
                                    <th class="col-xs-1">ALTERNO</th>
                                    <th class="col-xs-1">EXT.</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    // inicia la consulta de contactos
                                    strSql = "select * from cofide_contactos where CT_ID = " + strIdCte;
                                    rs = oConn.runQuery(strSql, true);
                                    while (rs.next()) {
                                        bolContacto = true;
                                        strTitulo = rs.getString("CCO_TITULO");
                                        strNombreC = rs.getString("CCO_NOMBRE");
                                        strAppat = rs.getString("CCO_APPATERNO");
                                        strApmat = rs.getString("CCO_APMATERNO");
//                                        strNum = rs.getString("CCO_NOSOCIO");
//                                        strAsoc = rs.getString("CCO_ASOCIACION");
//                                        strAreaC = rs.getString("CCO_AREA");
                                        strCorreoC = rs.getString("CCO_CORREO");
                                        strCorreoC2 = rs.getString("CCO_CORREO2");
                                        strTelefonoC = rs.getString("CCO_TELEFONO");
                                        strTelefonoC2 = rs.getString("CCO_ALTERNO");
//                                        strExtC = rs.getString("CCO_EXTENCION");
%>
                                <tr>
                                    <td class="col-xs-2"><%=strTitulo%></td>
                                    <td class="col-xs-2"><%=strNombreC%></td>
                                    <td class="col-xs-1"><%=strAppat%></td>
                                    <td class="col-xs-1"><%=strApmat%></td>
                                    <td class="col-xs-2"><%=strCorreoC%></td>
                                    <td class="col-xs-1"><%=strCorreoC2%></td>
                                    <td class="col-xs-1"><%=strTelefonoC%></td>
                                    <td class="col-xs-1"><%=strTelefonoC2%></td>
                                    <td class="col-xs-1"><%=strExtC%></td>
                                </tr>      
                                <%
                                    }
                                    rs.close();
                                    if (!bolContacto) {
                                %>
                            <td class="col-xs-1"></td>
                            <%
                                }
                                // termina la ocnsulta de contactos
                            %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <!-- fin tabla contactos -->
            <!-- tabla razon social -->
            <div class="container">            
                <div class="row">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4>
                                RAZÓN SOCIAL
                            </h4>
                        </div>
                        <table class="table table-fixed2">
                            <thead>
                                <tr>
                                    <th class="col-xs-10">RAZÓN SOCIAL</th>			  
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    // inicia la consulta de razon social
                                    strSql = "select * from cofide_razonsocial where CT_ID = " + strIdCte;
                                    rs = oConn.runQuery(strSql, true);
                                    while (rs.next()) {
                                        strRazonSocialR = rs.getString("CR_RAZONSOCIAL");
                                        bolRazonSocial = true;
                                %>
                                <tr>
                                    <td class="col-xs-10"><%=strRazonSocialR%></td>			  
                                </tr>        
                                <%
                                    }
                                    rs.close();
                                    if (!bolRazonSocial) {
                                %>
                            <td class="col-xs-10"></td>
                            <%
                                }
                                // termina la ocnsulta de razon social
                            %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- tabla razon social 
            <!-- fin tabla -->
        </div>

        <div id="VentasTab" class="tabcontent">
            <!-- VENTAS -->
            <div class="container">
                <div class="row">
                    <div class="panel panel-default ventas">
                        <div class="panel-heading">
                            <h4>
                                HISTORIAL DE VENTAS
                            </h4>
                        </div>
                        <table class="table table-fixed3">
                            <thead>
                                <tr>
                                    <th class="col-xs-1">ID VENTA</th>
                                    <th class="col-xs-2">DOCUMENTO</th>
                                    <th class="col-xs-1">FECHA</th>
                                    <th class="col-xs-1">HORA</th>
                                    <th class="col-xs-2">RAZÓN SOCIAL</th>
                                    <th class="col-xs-2">AGENTE</th>
                                    <th class="col-xs-1">TOTAL</th>
                                    <th class="col-xs-1">PAGADO</th>
                                    <th class="col-xs-1">ESTATUS</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    // inicia la consulta de ventas
                                    strSql = "select *,"
                                            + "if(FAC_ANULADA = 1, 'Cancelada','Activa') as ESTATUS,if(TIPO_DOC = 'T', 'TICKET', 'FACTURA') as DOCUMENTO,"
                                            + "(select vta_cliente.CT_RAZONSOCIAL from vta_cliente where vta_cliente.CT_ID = view_ventasglobales.CT_ID) as RAZONSOCIAL, "
                                            + "(select CONCAT(usuarios.nombre_usuario,' ',usuarios.apellido_paterno) from usuarios where usuarios.id_usuarios = view_ventasglobales.FAC_US_ALTA) as AGENTE "
                                            + " from view_ventasglobales where CT_ID = " + strIdCte;
                                    rs = oConn.runQuery(strSql, true);
                                    while (rs.next()) {
                                        bolVentas = true;
                                        if (rs.getString("FAC_PAGADO").equals("0") && rs.getString("FAC_COFIDE_VALIDA").equals("0") && rs.getString("ARCHIVO").equals("")) {
                                            strPagado = "SIN PAGO";
                                        }
                                        if (rs.getString("FAC_PAGADO").equals("1") && rs.getString("FAC_COFIDE_VALIDA").equals("0") && !rs.getString("ARCHIVO").equals("")) {
                                            strPagado = "REVISIÓN";
                                        }
                                        if (rs.getString("FAC_PAGADO").equals("0") && rs.getString("FAC_COFIDE_VALIDA").equals("0") && !rs.getString("ARCHIVO").equals("")) {
                                            strPagado = "NEGADO";
                                        }
                                        if (rs.getString("FAC_PAGADO").equals("1") && rs.getString("FAC_COFIDE_VALIDA").equals("1") && !rs.getString("ARCHIVO").equals("")) {
                                            strPagado = "PAGADO";
                                        }
                                        strIdVta = rs.getString("FAC_ID");
                                        strDocumento = rs.getString("DOCUMENTO");
                                        strFecha = rs.getString("FAC_FECHA");
                                        strHora = rs.getString("FAC_HORA");
                                        strRazonSocialV = rs.getString("RAZONSOCIAL");
                                        strAgente = rs.getString("AGENTE");
                                        strTotal = rs.getString("FAC_TOTAL");
//                                        strPagado = rs.getString("FAC_PAGADO");
                                        strEstatus = rs.getString("ESTATUS");
                                        // termina la ocnsulta de ventas
%>
                                <tr>
                                    <td class="col-xs-1"><%=strIdVta%></td>
                                    <td class="col-xs-2"><%=strDocumento%></td>
                                    <td class="col-xs-1"><%=strFecha%></td>
                                    <td class="col-xs-1"><%=strHora%></td>
                                    <td class="col-xs-2"><%=strRazonSocialV%></td>
                                    <td class="col-xs-2"><%=strAgente%></td>
                                    <td class="col-xs-1"><%=strTotal%></td>
                                    <td class="col-xs-1"><%=strPagado%></td>
                                    <td class="col-xs-1"><%=strEstatus%></td>
                                </tr>
                                <%    }
                                    rs.close();
                                    if (bolVentas) {
                                %>
                            <td class="col-xs-1"></td>           
                            <%
                                }
                            %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <!-- FIN VENTAS -->
        </div>

        <script>
            function openCte(evt, pestania) {
                var i, tabcontent, tablinks;
                tabcontent = document.getElementsByClassName("tabcontent");
                for (i = 0; i < tabcontent.length; i++) {
                    tabcontent[i].style.display = "none";
                }
                tablinks = document.getElementsByClassName("tablinks");
                for (i = 0; i < tablinks.length; i++) {
                    tablinks[i].className = tablinks[i].className.replace(" active", "");
                }
                document.getElementById(pestania).style.display = "block";
                evt.currentTarget.className += " active";
            }
            document.getElementById("defaultOpen").click();
        </script>
        <%
        } else {
        %>
        <div style="height: 30%"></div>
        <div style="text-align: center; height: 40%; background: #000000; color: #bdff76">
            <br>
            <h2><p>NO<br><i>se ha encontrado el</i><br>ID del cliente <br><i>para mostrar sus detalles</i></p></h2>
        </div>
        <div style="height: 30%"></div>
        <%
            }
        } else {
        %>
        <div style="height: 30%"></div>
        <div style="text-align: center; height: 40%; background: #000000; color: #bdff76">
            <br>
            <h2><p>NO<br><i>se ha encontrado el</i><br>ID del cliente <br><i>para mostrar sus detalles</i></p></h2>
        </div>
        <div style="height: 30%"></div>
        <%
            }
        %>
    </body>
</html> 
