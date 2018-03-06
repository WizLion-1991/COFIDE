<%-- 
    Document   : newjsp
    Created on : 28/02/2018, 10:24:26 AM
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
        se conecta el sr.

        <%

            out.println(session.getAttribute("valor"));
        %>
    </body>
</html>
