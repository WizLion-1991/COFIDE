<%-- 
    Document   : COFIDE_InBound
    Created on : 9/02/2018, 11:20:44 AM
    Author     : Desarrollo_COFIDE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>

        <%

            String strNombreCurso = request.getParameter("curso");
            String strHoras = request.getParameter("horas");
            String strInstructor = request.getParameter("instructor");
            String strLigaXLS = request.getParameter("xls");

        %>

        nombre del curso: <%=strNombreCurso%>
        <br>
        duración: <%=strHoras%>
        <br>
        Instructor: <%=strInstructor%>
        <br>
        liga xls: <%=strLigaXLS%>
        <br>
        <input type="button" value="confirmar información" onclick="alert('confirma')">
        <input type="button" value="Exportar Constancias" onclick="alert('PDF')">

    </body>
</html>
