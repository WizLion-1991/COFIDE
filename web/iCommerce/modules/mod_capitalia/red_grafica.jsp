<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.mx.siweb.mlm.utilerias.Redes"%>
<%@page import="Tablas.vta_cliente"%>
<%@page import="com.mx.siweb.mlm.compensacion.Periodos"%>
<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<%@page import="comSIWeb.ContextoApt.Seguridad"%>
<%@page import="comSIWeb.ContextoApt.VariableSession"%>
<%
    /*Inicializamos las variables de sesion limpias*/
    VariableSession varSesiones = new VariableSession(request);
    varSesiones.getVars();

    //Instanciamos objeto de seguridad para no permitir el acceso externo a ver los XML
    Seguridad seg = new Seguridad();//Valida que la peticion se halla hecho desde el mismo sitio
    if (varSesiones.getIntNoUser() != 0) {

        //Abrimos la conexion
        Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
        oConn.open();

        //Parametros para visualizar acotado
        boolean bolAcotado = false;
        int intMaxNiveles = 3;
        String strCampoFiltro = "CT_UPLINE";
        String strTituloTipo = "Descendencia";

        //Cargamos datos iniciales
        Fechas fecha = new Fechas();
        Periodos periodo = new Periodos();
        String strPeriodo = periodo.getPeriodoActualNom(oConn);
        //Obtenemos datos del cliente
        vta_cliente cliente = new vta_cliente();
        cliente.ObtenDatos(varSesiones.getintIdCliente(), oConn);

        String strModo = request.getParameter("Modo");

        if (strModo != null) {
            if (strModo.equals("Unilevel")) {
                strCampoFiltro = "CT_SPONZOR";
                strTituloTipo = " - Mis recomendados";
            }
            if (strModo.equals("Binario")) {
                strCampoFiltro = "CT_UPLINE";
                strTituloTipo = " - Mi red binaria";
            }
        }

      //Obtenemos el nombre del cliente

%>
<link rel="stylesheet" href="../jqGrid/jOrgChart/css/bootstrap.min.css"/>
<link rel="stylesheet" href="../jqGrid/jOrgChart/css/jquery.jOrgChart.css"/>
<link rel="stylesheet" href="../jqGrid/jOrgChart/css/custom.css"/>
<link href="../jqGrid/jOrgChart/css/prettify.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="../jqGrid/jOrgChart/prettify.js"></script>

<!-- jQuery includes -->
<script src="../jqGrid/jOrgChart/jquery.jOrgChart.js"></script>

<script>
    jQuery(document).ready(function () {
        $("#org").jOrgChart({
            chartElement: '#chart',
            dragAndDrop: true
        });
        $("#dialogDeta").dialog({autoOpen: false, draggable: true, modal: true, resizable: true, show: 'slide', position: 'top', width: "auto"});
    });
    /**Muestra la información del nodo seleccionado*/
    function ShowDetails(id) {
        $.ajax({
            type: "POST",
            data: "CT_ID=" + id,
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "modules/mod_capitalia/mlm_red_grid.jsp?ID=2",
            success: function (contenido) {
                document.getElementById("dialogDeta_inside").innerHTML = contenido;
                $("#dialogDeta").dialog("open");
            },
            error: function (objeto, quepaso, otroobj) {
                alert("detalle distribuidor" + objeto + " " + quepaso + " " + otroobj);
            }
        });

    }
</script>

<div class="well ">
    <h3 class="page-header">Mi red <%=cliente.getFieldString("CT_NOMBRE")%>&nbsp;<%=cliente.getFieldString("CT_APATERNO")%>&nbsp;<%=cliente.getFieldString("CT_AMATERNO")%>&nbsp<a href="index.jsp?mod=red_grafica">Ir al inicio&nbsp;-<a href="index.jsp?mod=FZWebRed">Otro tipo de red</a></h3>
    <div class="userdata">       
        <div id="form-new-submit" class="control-group">
            <div class="controls">
                <span class="required"></span>
            </div>

            <!--Lista para el organigrama-->
            <ul id="org" style="display:none">
                <!--Inicia lista-->
                <%
                    //Aqui generamos la lista con los 3 primeros niveles de la red
                    String strNodoHijo = request.getParameter("NodoHijo");
                    int intNodoHijo = 0;
                    try {
                        intNodoHijo = Integer.valueOf(strNodoHijo);
                    } catch (NumberFormatException ex) {
                    }
                    //Evaluamos si consultamos un nodo hijo o el nodo inicial
                    int intNodoPintar = 0;
                    if (intNodoHijo == 0) {
                        intNodoPintar = varSesiones.getintIdCliente();
                    } else {
                      //Validamos que el nodo hijo por pintar este en su red
                      /*boolean bolValido = Redes.esPartedelaRed(oConn, "vta_cliente", "CT_UPLINE", "CT_ID", varSesiones.getintIdCliente(), intNodoHijo);
                         if (bolValido) {*/
                        intNodoPintar = intNodoHijo;
                        /*} else {
                         intNodoPintar = varSesiones.getintIdCliente();
                         }*/
                    }

                    //Obtenemos datos del cliente por pintar
                    vta_cliente clientePintar = new vta_cliente();
                    clientePintar.ObtenDatos(intNodoPintar, oConn);
                %>
                <li>
                    <%=clientePintar.getFieldString("CT_ID")%>
                    <img src="../images/mlm/nodo_principal.png" border="0" alt="" title="" /><a href="javascript:ShowDetails(<%=clientePintar.getFieldString("CT_ID")%>)">(+)</a>
                    <div class="nom_ini"><%=clientePintar.getFieldString("CT_RAZONSOCIAL")%></div>
                    <ul>
                        <%
                            //Consulta de hijos
                            String strSql = "select CT_ID,CT_RAZONSOCIAL,CT_PPUNTOS from vta_cliente where " + strCampoFiltro + " = " + intNodoPintar;
                            ResultSet rs = oConn.runQuery(strSql, true);
                            String strColorImg = "";
                            while (rs.next()) {
                                strColorImg = getColorCTE(oConn, rs.getInt("CT_ID"));
                                String strNomImg = strColorImg;
                                if (rs.getDouble("CT_PPUNTOS") == 0) {
                                    strNomImg = strColorImg;
                                }
                        %><li id="n1_<%=rs.getInt("CT_ID")%>">
                            <%=rs.getInt("CT_ID")%>
                            <a><img src="<%=strColorImg%>" border="0" alt="<%=rs.getString("CT_RAZONSOCIAL")%>" title="<%=rs.getString("CT_RAZONSOCIAL")%>" /></a><a href="javascript:ShowDetails(<%=rs.getInt("CT_ID")%>)">(+)</a>
                            <div class="nom_small"><%=rs.getString("CT_RAZONSOCIAL")%>
                            </div>
                            <%
                                //Consultamos los nietos
                                boolean bolDrawUl = true;
                                String strSql2 = "select CT_ID,CT_RAZONSOCIAL,CT_PPUNTOS from vta_cliente where " + strCampoFiltro + " = " + rs.getInt("CT_ID");
                                ResultSet rs2 = oConn.runQuery(strSql2, true);
                                while (rs2.next()) {
                                    strColorImg = getColorCTE(oConn, rs2.getInt("CT_ID"));
                                    String strNomImg2 = strColorImg;
                                    if (rs2.getDouble("CT_PPUNTOS") == 0) {
                                        strNomImg2 = strColorImg;
                                    }
                                    if (bolDrawUl) {
                                        out.println("<ul>");
                                        bolDrawUl = false;
                                    }
                            %><li id="n2_<%=rs2.getInt("CT_ID")%>">
                            <% out.print(rs2.getInt("CT_ID") + "<a><img src=\"" + strColorImg + "\" border=\"0\" alt=\"" + rs2.getString("CT_RAZONSOCIAL") + "\" title=\"" + rs2.getString("CT_RAZONSOCIAL") + "\" /></a><a href=\"javascript:ShowDetails(" + rs2.getInt("CT_ID") + ")\">(+)</a>" 
                                        + "<div class=\"nom_small\">" + rs2.getString("CT_RAZONSOCIAL") + "</div>");

                                //Consultamos los Bisnietos
                                boolean bolDrawUl3 = true;
                                String strSql3 = "select CT_ID,CT_RAZONSOCIAL,CT_PPUNTOS from vta_cliente where " + strCampoFiltro + " = " + rs2.getInt("CT_ID");
                                ResultSet rs3 = oConn.runQuery(strSql3, true);
                                while (rs3.next()) {
                                    strColorImg = getColorCTE(oConn, rs3.getInt("CT_ID"));
                                    String strNomImg3 = strColorImg;
                                    if (rs3.getDouble("CT_PPUNTOS") == 0) {
                                        strNomImg3 = strColorImg;
                                    }
                                    if (bolDrawUl3) {
                                        out.println("<ul>");
                                        bolDrawUl3 = false;
                                    }
                            %><li id="n2_<%=rs3.getInt("CT_ID")%>">
                            <% out.print(rs3.getInt("CT_ID") + "<a><img src=\"" + strColorImg + "\" border=\"0\" alt=\"" + rs3.getString("CT_RAZONSOCIAL") + "\" title=\"" + rs3.getString("CT_RAZONSOCIAL") + "\" /></a><a href=\"javascript:ShowDetails(" + rs3.getInt("CT_ID") + ")\">(+)</a><div class=\"nom_small\">" + rs3.getString("CT_RAZONSOCIAL") + "</div>");


                            %></li><%                         }
                                //Si se pinto el ul inicial pintamos el cierre del mismo
                                if (!bolDrawUl3) {
                                    out.println("</ul>");
                                }
                                rs3.close();
                               //Consultamos los nietos

                            %></li><%                         }
                                //Si se pinto el ul inicial pintamos el cierre del mismo
                                if (!bolDrawUl) {
                                    out.println("</ul>");
                                }
                                rs2.close();
                    %>
                </li><%
                    }
                    rs.getStatement().close();
                    rs.close();
                %>>
            </ul>
            </li>
            <!--Termina lista-->
            </ul>   
            <!--Lista para el organigrama-->
            <div id="chart" class="orgChart" style=" OVERFLOW: auto; WIDTH: auto; TOP: 18px; HEIGHT: auto"></div>

        </div>   

    </div>
</div>
<div id="dialogDeta" title="Detalle del distribuidor">
    <div id="dialogDeta_inside"></div>
</div>    
<%
        oConn.close();
    }
    
%>
<%!
    public String getColorCTE(Conexion oConn, int intCT_ID){
        String strQuery = "select CT_ACTIVO from vta_cliente where CT_ID = " + intCT_ID;
        int intTipo = 0;
        int intActivo = 0;
        String strRes = "";
        try{
            ResultSet rs = oConn.runQuery(strQuery, true);
            while(rs.next()){
                intActivo = rs.getInt("CT_ACTIVO");
            }
        if(intActivo == 1){
            strQuery = "select CT_ID,CT_RAZONSOCIAL,vta_cliente.CT_ARMADOINI,vta_cliente.CT_ARMADOFIN ,"
                + " (select COUNT(g.CT_ID) from  vta_cliente g where  g.CT_ARMADONUM >= vta_cliente.CT_ARMADOINI "
                + " and g.CT_ARMADONUM <= vta_cliente.CT_ARMADOFIN) as cuantos from vta_cliente where CT_ID = " + intCT_ID;
            rs = oConn.runQuery(strQuery, true);
            while(rs.next()){
                intTipo = rs.getInt("cuantos");
            }
            
            if(intTipo >= 36 && intTipo < 126){
                strRes = "../images/mlm/36_personas_activas.png";
            }else{
                if(intTipo >= 216 && intTipo < 1296){
                    strRes = "../images/mlm/216_personas_activas.png";
                }else{
                    if(intTipo >= 1296){
                        strRes = "../images/mlm/1296_personas.png";
                    }else{
                        strRes = "../images/mlm/activos.png";
                    }
                }
            }
            
        }else{
            strRes = "../images/mlm/inactivos.png";
        }    
            
        rs.getStatement().close();
        rs.close();
        }catch(SQLException e){
            
        }
        return strRes;
    }
%>