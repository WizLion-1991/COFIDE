<%-- 
    Document   : Main
    Created on : 15/02/2018, 12:58:54 PM
    Author     : Desarrollo_COFIDE
--%>
<%@page import="com.mx.siweb.erp.especiales.cofide.CofideIncidencia"%>
<%
    CofideIncidencia incidencia = new CofideIncidencia();

    if (request.getParameter("usuario") != null) {

        String strIdUsuario = request.getParameter("idusuario");
        String strNombre = request.getParameter("nombre");
        String strUsuario = request.getParameter("usuario");
        String strPassword = request.getParameter("password");
        String strPerfil = request.getParameter("perfil");

        incidencia.setStrIdUsuario(strIdUsuario);
        incidencia.setStrNombre(strNombre);
        incidencia.setStrUsuario(strUsuario);
        incidencia.setStrPassword(strPassword);
        incidencia.setStrPerfil(strPerfil);

        System.out.println("id usuario: " + incidencia.getStrIdUsuario());
        System.out.println("nombre: " + incidencia.getStrNombre());
        System.out.println("usuario: " + incidencia.getStrUsuario());
        System.out.println("password: " + incidencia.getStrPassword());
        System.out.println("perfil: " + incidencia.getStrPerfil());

%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="cofide_incidencia.html" %>
<!DOCTYPE html>
<html>
    <body onload="initMain()">

        <!--DATOS DE SESION-->
        <div id="variables_sesion">
            <input id="usuario_id" type="text" value="<%=incidencia.getStrIdUsuario()%>">
            <!--<input id="usuario_nombre" type="text" value="<%//=incidencia.getStrNombre()%>">-->
            <!--<input id="usuario_usuario" type="text" value="<%//=incidencia.getStrUsuario()%>">-->
            <!--<input id="usuario_pass" type="text" value="<%//=incidencia.getStrPassword()%>">-->
            <input id="usuario_perfil" type="text" value="<%=incidencia.getStrPerfil()%>">        
        </div>
        <!--DATOS DE SESION-->
        <!--HEADER-->
        <header>
            <div>
                <h1 id="contenedor_header">INCIDENCIAS</h1>      
                <!--<hr class="style13">-->
                <b id="contenedor_header">USUARIO: 
                    <label style="color: #99CC00">
                        <%=strNombre%>
                    </label>
                </b>                             
            </div>
            <hr class="style13">
        </header>
        <!--HEADER-->
        <!--SECCION-->        
        <div id="contenedor_"  class="row">
            <div id="contenedor_menu" class="col-xs-2 col-md-2 col-lg-2">
                <section>
                    <div id="menu_boton">

                    </div>
                </section>
            </div>
            <div id="contenedor_contenido" class="col-xs-10 col-md-10 col-lg-10">
                <section>
                    <div id="cuerpo_menu">
                        <div id="contenido_pantalla"></div>
                    </div>
                </section>
            </div>
        </div>
        <!--SECCION-->

        <!--FOOTER-->
        <footer>
            <!--<hr class="style13">-->
            <!--<button type="button" onclick="location.href = 'index.html'">SALIR</button>-->
        </footer>
        <!--FOOTER-->
    </body>
</html>
<%
} else {
%>

<html>
    <body onload="location.href = 'index.html'"></body>
</html>
<%
    }
%>