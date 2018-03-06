<%-- 
    Document   : cofide_incidencia_reporte
    Created on : 23/02/2018, 05:27:19 PM
    Author     : Desarrollo_COFIDE
--%>
<%@page import="javax.servlet.http.HttpSession"%>
<%@page import="javax.servlet.http.HttpServletRequest"%>
<%@page import="javax.servlet.http.HttpServletResponse"%>


<%

    session.setAttribute("valor", "julio");
%>
<html>
    <head>

    </head>


    <body>
        <%
            out.println(session.getAttribute("valor"));
        %>
        <br>
        this is a test

    </body>
</html>