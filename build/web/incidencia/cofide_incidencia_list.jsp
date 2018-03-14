<%-- 
    Document   : cofide_incidencia_list
    Created on : 21/02/2018, 11:49:37 AM
    Author     : Desarrollo_COFIDE
--%>

<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<%@page import="comSIWeb.ContextoApt.VariableSession"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="cofide_incidencia.html" %>
<%
    VariableSession varSesiones = new VariableSession(request);
    varSesiones.getVars();
    Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
    Fechas fec = new Fechas();
    oConn.open();
%>
<!DOCTYPE html>
<html>   
    <body>
        <div>
            <div style="text-align: center">
                <h3>INCIDENCIAS</h3>
            </div>
            <div id="container">
                <%
                    if (request.getParameter("idusuario") != null) {
                        String strIdUsuario = request.getParameter("idusuario");
                        String strSql = "";
                        ResultSet rs;
                %>
                <table class="table2 table-striped" id="tabla_incidencia">
                    <!--<table id="tabla_incidencia" class="table2 table-hover">-->                
                    <thead class="contenido_thead">
                    <th style="text-align: center">FECHA DE ALTA</th>
                    <th style="text-align: center">HORA DE ALTA</th>
                    <th style="text-align: center">CLIENTE</th>
                    <th style="text-align: center">TIPO DE PROBLEMA</th>
                    <th style="text-align: center">CRM(MODULO)</th>
                    <th style="text-align: center">ESTATUS DE INCIDENCIA</th>
                    <th style="text-align: center">FECHA TERMINO</th>
                    <th style="text-align: center">HORA TERMINO</th>
                    <th style="text-align: center">TIEMPO DE RESPUESTA</th>                
                    <th colspan="3" style="text-align: center">ACCIONES</th>                
                    </thead>
                    <tbody class="contenido_tbody">
                        <%
                            strSql = "select cfd_incidencia_incidencia.*, "
                                    + "CIP_DESCRIPCION, "
                                    + "IFNULL(CMO_DESCRIPCION, '') as CMO_DESCRIPCION,"
                                    + "ELT(CCI_ESTATUS,'COMPLETO','PENDIENTE','SIN ATENDER') AS ESTATUS,"
                                    + "setDiffHrsIncidencia(CCI_FECHA_ALTA, CCI_FECHA_TERMINO, CCI_HORA_ALTA, CCI_HORA_TERMINO) as TOTAL_TIEMPO  "
                                    + "from cfd_incidencia_incidencia "
                                    + "LEFT JOIN cfd_incidencia_problema on CCI_TIPO_PROBLEMA = CIP_ID "
                                    + "LEFT JOIN cfd_incidencia_modulo on CCI_MODULO_CRM = CMO_ID "
                                    + "where CCI_US_ALTA =  " + strIdUsuario
                                    + " order by CCI_FECHA_ALTA desc , CCI_HORA_ALTA desc;";
                            String strFechaIni = "";
                            String strFechaFin = "";
                            String strIdIncidencia = "";
                            String strcolorEstatus = "";
                            boolean bolResultado = false;
                            try {
                                rs = oConn.runQuery(strSql, true);
                                while (rs.next()) {
                                    bolResultado = true;
                                    strFechaIni = rs.getString("CCI_FECHA_ALTA");
                                    strFechaFin = rs.getString("CCI_FECHA_TERMINO");
                                    if (!strFechaIni.equals("")) {
                                        strFechaIni = fec.FormateaDDMMAAAA(strFechaIni, "/");
                                    }
                                    if (!strFechaFin.equals("-")) {
                                        strFechaFin = fec.FormateaDDMMAAAA(strFechaFin, "/");
                                    }
                                    strIdIncidencia = rs.getString("CII_ID");

                                    if (rs.getInt("CCI_ESTATUS") == 1) {
                                        strcolorEstatus = "#28a745";
                                    }
                                    if (rs.getInt("CCI_ESTATUS") == 2) {
                                        strcolorEstatus = "#e0a800";
                                    }
                                    if (rs.getInt("CCI_ESTATUS") == 3) {
                                        strcolorEstatus = "RED";
                                    }
                        %>
                        <tr>
                            <td style="text-align: center"><%=strFechaIni%></td>
                            <td style="text-align: center"><%=rs.getString("CCI_HORA_ALTA")%></td>
                            <td ><%=rs.getString("CCI_CLIENTE")%></td>
                            <td style="text-align: center"><%=rs.getString("CIP_DESCRIPCION")%></td>
                            <td style="text-align: center"><%=rs.getString("CMO_DESCRIPCION")%></td>
                            <td style="text-align: center; background-color: <%=strcolorEstatus%>; color: white;"><b><%=rs.getString("ESTATUS")%></b></td>                            
                            <td style="text-align: center"><%=strFechaFin%></td>
                            <td style="text-align: center"><%=rs.getString("CCI_HORA_TERMINO")%></td>
                            <td style="text-align: center"><%=rs.getString("TOTAL_TIEMPO")%></td>
                            <td><div><button onclick="OpnDetalle(<%=strIdIncidencia%>)" class="btn btn-info">DETALLES</button></div></td>                        
                            <td><div><button onclick="setEstatus(2,<%=strIdIncidencia%>)" class="btn btn-warning">PENDIENTE</button></div></td>                        
                            <td><div><button onclick="setEstatus(1,<%=strIdIncidencia%>)" class="btn btn-success">COMPLETO</button></div></td>                        
                        </tr>     
                        <% }
                            rs.close();
                            if (!bolResultado) {

                        %>
                    <div style="text-align: center">
                        <hr class="style13">
                        <img src="http://201.161.14.206:9001/cofide/images/lupa.png">
                        <h1>NO SE ENCONTRARÓN INCIDENCIAS REGISTRADAS EN TU PERFIL</h1>
                        <hr class="style13">
                        <script>
                            document.getElementById("tabla_incidencia").style.display = "none";
                        </script>
                    </div>
                    <%        }
                        } catch (SQLException sql) {
                            System.out.println("Error al recuperar las incidencias del usuario: " + strIdUsuario + " [ " + sql.getMessage() + " ]");
                        }

                    %>
                    </tbody>
                </table>

                <!--MODAL DE REGISTRO DE USUARIO-->
                <div id="id04" class="modal2">                    
                    <form class="modal-content2 animate" >            
                        <div class="imgcontainer">
                            <span onclick="CloseModal(4)" class="close" title="CERRAR">&times;</span>                            
                            <div style="text-align: center;" id="id_incidencia_ed"></div>
                            <input type="hidden" id="id_incidencia_edicion">
                        </div>                        
                        <div class="container">

                            <div style="width: 100%; height: 550px; overflow: auto">
                                <%--<%@include  file = "cofide_incidencia_incidencia.jsp" %>--%>
                                <!--EDICIÓN DE ENCIDENCIAS-->
                                <div id="container" style="font-size: 12px;">
                                    <div id="contenedor_incidencia">
                                        <div>
                                            <label>CLIENTE</label>
                                            <input type="text" placeholder="INFORMACIÓN DEL CLIENTE" id="cliente" class="form-control">
                                        </div>
                                        <div>
                                            <label>CORREO</label>
                                            <input type="email" placeholder="CORREO@DOMINIO.ORG" id="correo" class="form-control">
                                        </div>                                        
                                        <br>
                                        <div>
                                            <label>TIPO DE PROBLEMA</label>
                                            <br>
                                            <select id="tipoproblema" class="form-control" onchange="hidemodulo()">
                                                <option value="0">SELECCIONE UNA OPCIÓN</option>
                                            </select>
                                        </div>
                                        <br>
                                        <div>
                                            <label>CURSO DE REFERENCIA</label>
                                            <br>
                                            <select id="cursoref" class="form-control">
                                                <option value="0">SELECCIONE UNA OPCIÓN</option>
                                            </select>
                                        </div>
                                        <br>
                                        <div>
                                            <label>ORIGEN DEL PROBLEMA</label>
                                            <br>
                                            <select id="origenproblema" class="form-control">
                                                <option value="">SELECCIONE UNA OPCIÓN</option>
                                                <option value="CLIENTE EXT.">CLIENTE EXTERNO</option>
                                                <option value="CLIENTE INT.">CLIENTE INTERNO</option>
                                                <option value="SISTEMA">SISTEMA</option>
                                            </select>
                                        </div>                                        
                                        <br>
                                        <div>
                                            <label>MODULO</label>
                                            <br>
                                            <select id="modulocrm" class="form-control">
                                                <option value="0">SELECCIONE UNA OPCIÓN</option>
                                            </select>
                                        </div>
                                        <br>                                                                   
                                        <div>
                                            <label>COMENTARIO</label>
                                            <textarea placeholder="COMENTARIOS DEL CLIENTE" id="comentario" class="form-control"></textarea>                    
                                        </div>                
                                        <div>
                                            <label>OBSERVACIÓN</label>
                                            <textarea placeholder="OBSERVACIONES SOBRE EL PROBLEMA" id="observacion" class="form-control"></textarea>                    
                                        </div>

                                    </div>
                                </div>
                                <!--EDICIÓN DE ENCIDENCIAS-->

                            </div>

                            <button type="button" onclick="UpdateInc()">ACTUALIZAR</button>        
                        </div>
                        <div class="container" style="background-color:#f1f1f1">
                            <button type="button" onclick="CloseModal(4)" class="cancelbtn">CANCELAR</button>                    
                        </div>
                    </form>
                </div>
                <!--MODAL DE REGISTRO DE USUARIO-->

                <%                } else {
                %>
                <div style="text-align: center">
                    <hr class="style13">
                    <img src="http://201.161.14.206:9001/cofide/images/lupa.png">
                    <h1>NO SE ENCONTRARÓN INCIDENCIAS REGISTRADAS EN TU PERFIL</h1>
                    <hr class="style13">
                </div>
                <%
                    }
                %>
            </div>
        </div>
    </div>
</body>
</html>
<%
    oConn.close();
%>
