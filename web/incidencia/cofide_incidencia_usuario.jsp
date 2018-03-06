<%-- 
    Document   : cofide_incidencia_usuario
    Created on : 22/02/2018, 02:19:04 PM
    Author     : Desarrollo_COFIDE
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.SQLException"%>
<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<%@page import="comSIWeb.ContextoApt.VariableSession"%>
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
                <h3><b>USUARIOS</b></h3>
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
                    <th style="text-align: center" class="col-xs-2 col-md-2 col-lg-2">ID USUARIO</th>
                    <th style="text-align: center" class="col-xs-3 col-md-3 col-lg-3">NOMBRE</th>
                    <!--<th style="text-align: center" class="col-xs-2 col-md-2 col-lg-2">USUARIO</th>-->
                    <!--<th style="text-align: center" class="col-xs-2 col-md-2 col-lg-2">CONTRASEÑA</th>-->
                    <th style="text-align: center" class="col-xs-2 col-md-2 col-lg-2">ESTATUS</th>
                    <th style="text-align: center" class="col-xs-2 col-md-2 col-lg-2">PERFIL</th>
                    <th colspan="3" style="text-align: center" class="col-xs-3 col-md-3 col-lg-3">ACCIONES</th>                
                    </thead>
                    <tbody class="contenido_tbody">
                        <%
                            strSql = "select INC_US_ID, INC_US_NOMBRE, INC_US_USUARIO, INC_US_PASSWORD, if(INC_US_ESTATUS = 0,'INACTIVO','ACTIVO') as INC_US_ESTATUS, if(INC_US_PERFIL = 0, 'SIN PERFIL',(select INC_PF_PERFIL from cfd_incidencia_perfil where INC_PF_ID = INC_US_PERFIL)) AS INC_US_PERFIL "
                                    + "from cfd_incidencia_usuario "
                                    + "order by INC_US_NOMBRE;";
                            try {
                                rs = oConn.runQuery(strSql, true);
                                while (rs.next()) {

                                    String strColorEstatusB = "";
                                    String strColorEstatusL = "";
                                    if (rs.getString("INC_US_ESTATUS").equals("INACTIVO")) {
                                        strColorEstatusB = "RED";
                                        strColorEstatusL = "white";
                                    }

                        %>
                        <tr>
                            <td style="text-align: center" class="col-xs-2 col-md-2 col-lg-2"><%=rs.getString("INC_US_ID")%></td>
                            <td style="text-align: center" class="col-xs-3 col-md-3 col-lg-3"><%=rs.getString("INC_US_NOMBRE")%></td>
<!--                            <td style="text-align: center" class="col-xs-2 col-md-2 col-lg-2"><%//=rs.getString("INC_US_USUARIO")%></td>
                            <td style="text-align: center" class="col-xs-2 col-md-2 col-lg-2"><%//=rs.getString("INC_US_PASSWORD")%></td>-->
                            <td style="text-align: center; color:<%=strColorEstatusL%>;  background-color: <%=strColorEstatusB%>;" class="col-xs-2 col-md-2 col-lg-2"><%=rs.getString("INC_US_ESTATUS")%></td>
                            <td style="text-align: center" class="col-xs-2 col-md-2 col-lg-2"><%=rs.getString("INC_US_PERFIL")%></td>                            
                            <td class="col-xs-1 col-md-1 col-lg-1"><div><button onclick="OpnDetalleusr(<%=rs.getString("INC_US_ID")%>)" class="btn btn-info">DETALLES</button></div></td>                        
                            <td class="col-xs-1 col-md-1 col-lg-1"><div><button onclick="setEstatusUsr(1, <%=rs.getString("INC_US_ID")%>)" class="btn btn-success">ACTIVAR</button></div></td>                        
                            <td class="col-xs-1 col-md-1 col-lg-1"><div><button onclick="setEstatusUsr(0, <%=rs.getString("INC_US_ID")%>)" class="btn btn-warning">BLOQUEAR</button></div></td>                        
                        </tr>     
                        <%                                }
                                rs.close();
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
                            <div style="text-align: center;" id="id_usuario"></div>
                            <input type="hidden" id="id_usuario_ed">
                        </div>                        
                        <div class="container">

                            <div style="width: 100%; height: 550px; overflow: auto">
                                <%--<%@include  file = "cofide_incidencia_incidencia.jsp" %>--%>
                                <!--EDICIÓN DE ENCIDENCIAS-->
                                <div id="container" style="font-size: 12px;">
                                    <div id="contenedor_incidencia">
                                        <div>
                                            <label>NOMBRE</label>
                                            <input type="text" class="form-control" id="nombre">
                                        </div>
                                        <div>
                                            <label>USUARIO</label>
                                            <input type="text" class="form-control" id="usuario">
                                        </div>
                                        <div>
                                            <label>CONTRASEÑA</label>
                                            <input type="text" class="form-control" id="contrasenia">
                                        </div>
                                        <div>
                                            <label>PERFIL</label>
                                            <select id="perfil" class="form-control"></select>
                                        </div>
                                        <div>
                                            <label>ESTATUS</label>                                            
                                            <select id="estatus" class="form-control">
                                                <option value="0">SELECCIONE UNA OPCIÓN</option>
                                                <option value="1">ACTIVO</option>                                                
                                                <option value="0">INACTIVO</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <!--EDICIÓN DE ENCIDENCIAS-->

                            </div>

                            <button type="button" onclick="updateUsuario()">ACTUALIZAR</button>        
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
                    <h1>ES NECESARIO INICIAS SESIÓN.</h1>
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
