<%@page import="com.mx.siweb.erp.especiales.cofide.COFIDE_Mail_cursos"%>
<%
    String strNombre = request.getParameter("nombre");            
    strNombre = strNombre.replace("_", " ");
    String strTotal = request.getParameter("total");
    String strEnviados = request.getParameter("enviado");
    String strError = request.getParameter("error");
    String strLeidos = request.getParameter("leido");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
        <!--Version 1.9.0.min -->
        <script type="text/javascript" src="jqGrid/jquery-1.9.0.min.js" ></script>
        <script type="text/javascript" src="jqGrid/grid.locale-es4.7.0.js" ></script>
        <script src="https://code.highcharts.com/highcharts.js"></script>
        <script src="https://code.highcharts.com/modules/exporting.js"></script>
        <script type="text/javascript" src="javascript/cofide_monitoreo_mail.js"></script> <!-- importar archivo javascript--> 
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">           
        <!-- script de grafica -->       
    </head>
    <style>
        #cofide_grafica{
            float:left;
            width:50%;
        }
        #cofide_grafica2{
            float:right;
            width:50%;
        }
        #contenedor{
            padding-top: 5%;
        }
    </style>

    <body onload="printGraphic(<%=strTotal%>, <%=strEnviados%>, <%=strError%>,<%=strLeidos%>)">
    <center>
        <!--mail masivo-->        
        <!--graficas-->
        <div>
            <h1>
                <%=strNombre%>
            </h1>
        </div>
        <div id="contenedor" style="width:80%">
            <div id="cofide_grafica"></div>
            <div id="cofide_grafica2"></div>
        </div>
        <div>
            <input class="btn btn-primary btn-lg btn-block" type="button" value="Salir" onclick="window.close()" title="Regresar al CRM / Cerrar ventana emergente"/>
        </div>
    </center>
</body>
</html>