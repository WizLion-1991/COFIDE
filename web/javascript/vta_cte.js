/* 
 * Esta libreria realiza las operaciones necesarias para la pantalla de catalogo de clientes
 */
var tabTitle = $("#tab_title"),
        tabContent = $("#tab_content"),
        tabTemplate = "<li><a href='#href'>#label</a> <span class='ui-icon ui-icon-close' role='presentation'>Remove Tab</span></li>",
        tabCounter = 3;
function vta_cte() {//Funcion necesaria para que pueda cargarse la libreria en automatico

}

var bolUsaOpnCte;
/**Realiza la operacion de seleccion del item ya sea para editar o copiarlo al cuadro de texto de una venta*/
function dblClickCte(id) {
    var strNomMain = objMap.getNomMain();
    var grid = jQuery("#CLIENTES");
    var lstVal = grid.getRowData(id);
    if (strNomMain == "VALIDA_FAC") {
        document.getElementById("COB_CTE").value = lstVal.CT_ID;
        PeticionMonedaProvCob();
        $("#dialogCte").dialog("close");
    } else {
        if (strNomMain == "CLIENTES") {
            //En caso de cliente abrimos la opcion de edicion
            OpnEdit(document.getElementById("Ed" + strNomMain));
        } else {
            if (strNomMain == "COBROS") {
                //En el caso de cobranza colocamos el numero de cliente
                document.getElementById("COB_CTE").value = lstVal.CT_ID;
                PeticionMonedaProvCob();
            } else {
                if (strNomMain == "FAC_TKT") {
                    //En caso de facturas de tickets devolvemos el valor en el cuadro de numero
                    //de cliente para facturar

                    if (opcPopupProvfac == 1) {
                        document.getElementById("FACT_CTE").value = lstVal.CT_ID;
                    } else {
                        document.getElementById("FACT_CTE1").value = lstVal.CT_ID;
                    }
                } else {
                    if (strNomMain == "VENTAS" || strNomMain == "PEDIDO") {
                        var boolUsaEnMain = true;
                        if (bolUsaOpnCte != null) {
                            if (!bolUsaOpnCte) {
                                boolUsaEnMain = false;
                            }
                        }
                        if (boolUsaEnMain) {
                            if (strNomMain == "VENTAS") {
                                document.getElementById("FCT_ID").value = lstVal.CT_ID;
                                document.getElementById("CT_NOM").value = lstVal.CT_RAZONSOCIAL;
                                var intOpPago = "1";
                                if (document.getElementById("FAC_SHOWPAGOS") != null) {
                                    intOpPago = document.getElementById("FAC_SHOWPAGOS").value;
                                }
                                if (intOpPago == "0") {
                                    verificaCreditoFact(lstVal.CT_ID);
                                }
                                ObtenNomCte();
                            } else {
                                //En caso de ventas colocamos el numero y nombre en los renglones correspondientes
                                document.getElementById("FCT_ID").value = lstVal.CT_ID;
                                document.getElementById("CT_NOM").value = lstVal.CT_RAZONSOCIAL;
                                ObtenNomCte();
                            }

                        } else {
                            //Colocamos el id en la pantalla de consultas
                            document.getElementById("VIEW_CLIENTE").value = lstVal.CT_ID;
                        }

                    } else {
                        if (strNomMain == "NCREDITO") {
                            //En caso de ventas colocamos el numero y nombre en los renglones correspondientes
                            document.getElementById("VIEW_CLIENTE").value = lstVal.CT_ID;
//                  document.getElementById("CT_NOM").value = lstVal.CT_RAZONSOCIAL;
//                  ObtenNomCteNC();
                        } else {
                            if (strNomMain == "NOTA_SRV") {
                                //En caso de ventas colocamos el numero y nombre en los renglones correspondientes
                                document.getElementById("VIEW_CLIENTE").value = lstVal.CT_ID;
//                     document.getElementById("CT_NOM").value = lstVal.CT_RAZONSOCIAL;
//                     ObtenNomCteServNC();
                            } else {
                                if (strNomMain == "VENTASLALA") {
                                    //En caso de ventas colocamos el numero y nombre en los renglones correspondientes
                                    document.getElementById("FCT_ID").value = lstVal.CT_ID;
                                    document.getElementById("CT_NOM").value = lstVal.CT_RAZONSOCIAL;
                                    ObtenNomCtelala();
                                } else {
                                    if (strNomMain == "VTAS_VIEW") {
                                        //En el caso de cobranza colocamos el numero de cliente
                                        document.getElementById("VIEW_CLIENTE").value = lstVal.CT_ID;
                                    } else {
                                        if (strNomMain == "NC_VIEW") {
                                            //En el caso de cobranza colocamos el numero de cliente
                                            document.getElementById("VIEW_CLIENTE").value = lstVal.CT_ID;
                                        } else {
                                            if (strNomMain == "IMP_MAIL") {
                                                //En el caso de cobranza colocamos el numero de cliente
                                                document.getElementById("VIEW_CLIENTE").value = lstVal.CT_ID;
                                            } else {
                                                if (strNomMain == "EDO_CTA1") {
                                                    //En el caso de cobranza colocamos el numero de cliente
                                                    document.getElementById("CT_ID_REPO").value = lstVal.CT_ID;
                                                } else {
                                                    if (strNomMain == "FACMASIVA") {
                                                        document.getElementById("MAS_CTEID").value = lstVal.CT_ID;
                                                        document.getElementById("MAS_CTENOMBRE").value = lstVal.CT_RAZONSOCIAL;
                                                    } else {
                                                        if (strNomMain == "CREDITO") {
                                                            document.getElementById("CT_ID").value = lstVal.CT_ID;
                                                            document.getElementById("CT_NOM").value = lstVal.CT_RAZONSOCIAL;
                                                        } else {
                                                            if (strNomMain == "SUGE") {
                                                                document.getElementById("CT_ID").value = lstVal.CT_ID;
                                                            } else {
                                                                if (strNomMain == "CONA") {
                                                                    if (intTipoConsulta == 1) {
                                                                        document.getElementById("CTE_IDF").value = lstVal.CT_ID;
                                                                        document.getElementById("CTE_NOMBREF").value = lstVal.CT_RAZONSOCIAL;
                                                                    } else
                                                                    {
                                                                        document.getElementById("CTE_ID").value = lstVal.CT_ID;
                                                                        document.getElementById("CTE_NOMBRE").value = lstVal.CT_RAZONSOCIAL;
                                                                        document.getElementById("CTE_FIADOR").value = lstVal.CT_FIADOR;
                                                                    }
                                                                } else {
                                                                    if (strNomMain == "ANTICTE") {
                                                                        document.getElementById("CTE_ID").value = lstVal.CT_ID;
                                                                        ObtenNomCteAnt();
                                                                    } else
                                                                    {
                                                                        if (strNomMain == "AJS_COBROC")
                                                                        {
                                                                            document.getElementById("COB_CTE").value = lstVal.CT_ID;
                                                                        } else {
                                                                            if (strNomMain == "BANK_VIEW") {

                                                                                setBancoClienteNoIdent(lstVal.CT_ID);
                                                                            } else {
                                                                                if (strNomMain == "EDO_CTA_CT") {
                                                                                    document.getElementById("ED_CLIENTE").value = lstVal.CT_ID;
                                                                                    document.getElementById("ED_NOMBRE").value = lstVal.CT_RAZONSOCIAL;
                                                                                    RefreshCteEdoCta();
                                                                                } else {
                                                                                    if (strNomMain == "CONA_CTE") {
                                                                                        document.getElementById("CTE_ID").value = lstVal.CT_ID;
                                                                                        document.getElementById("CTE_NOMBRE").value = lstVal.CT_RAZONSOCIAL;
                                                                                        document.getElementById("CAC_MONEDA").value = parseInt(lstVal.MON_ID);
                                                                                        RefreshCteConsAnt();
                                                                                    } else {


                                                                                        if (strNomMain == "EST_CTES") {
                                                                                            document.getElementById("EST_CLIENTE").value = lstVal.CT_ID;
                                                                                            document.getElementById("EST_NOMBRE").value = lstVal.CT_RAZONSOCIAL;
                                                                                            $("#dialogCte").dialog("close");
                                                                                        } else {
                                                                                            if (strNomMain == "USOTIMFIS") {
//                                                                     var selected = $("#tabsUSOTIMFIS").tabs("option", "active");

                                                                                                var selected = $("#tabsUSOTIMFIS").tabs("option", "active");
                                                                                                switch (selected) {
                                                                                                    case 2:
                                                                                                        document.getElementById("USOTIM_IDCTE_CTE").value = lstVal.CT_ID;
                                                                                                        break;
                                                                                                    case 4:
                                                                                                        document.getElementById("USOTIM_IDCTE_CLMES").value = lstVal.CT_ID;
                                                                                                        break;
                                                                                                }
                                                                                            } else {
                                                                                                if (strNomMain == "FAC_CONTRATOS") {
                                                                                                    document.getElementById("FC_CLI").value = lstVal.CT_ID;
                                                                                                } else {
                                                                                                    if (strNomMain == "PUNTOS") {
                                                                                                        document.getElementById("CT_ID").value = lstVal.CT_ID;
                                                                                                    } else {
                                                                                                        //Aqui va la nueva pantalla
                                                                                                        if (strNomMain == "CON_VENTAS") {
                                                                                                            document.getElementById("VIEW_CLIENTE").value = lstVal.CT_ID;
                                                                                                            $("#dialogCte").dialog("close");
                                                                                                        } else {
                                                                                                            if (strNomMain == "SERV") {
                                                                                                                document.getElementById("FCT_ID").value = lstVal.CT_ID;
                                                                                                                document.getElementById("CT_NOM").value = lstVal.CT_RAZONSOCIAL;
                                                                                                                ObtenNomCteNCargo();
                                                                                                                $("#dialogCte").dialog("close");
                                                                                                            } else {
                                                                                                                if (strNomMain == "C_TELEM") {
                                                                                                                    //Telemarketing
                                                                                                                    document.getElementById("ED_CLIENTE").value = lstVal.CT_ID;
                                                                                                                    document.getElementById("ED_NOMBRE").value = lstVal.CT_RAZONSOCIAL;
                                                                                                                    RefreshCteEdoCta();
                                                                                                                } else {
                                                                                                                    if (strNomMain == "EVENTOS") {
                                                                                                                        document.getElementById("EV_CT_ID").value = lstVal.CT_ID;
                                                                                                                        document.getElementById("EV_CT_RSOCIAL").value = lstVal.CT_RAZONSOCIAL;
                                                                                                                        $("#dialogCte").dialog("close");
                                                                                                                    } else {
                                                                                                                        //Ventas de servicio
                                                                                                                        var boolUsaEnMain = true;
                                                                                                                        if (bolUsaOpnCte != null) {
                                                                                                                            if (!bolUsaOpnCte) {
                                                                                                                                boolUsaEnMain = false;
                                                                                                                            }
                                                                                                                        }
                                                                                                                        if (boolUsaEnMain) {
                                                                                                                            //En caso de ventas colocamos el numero y nombre en los renglones correspondientes
                                                                                                                            document.getElementById("FCT_ID").value = lstVal.CT_ID;
                                                                                                                            document.getElementById("CT_NOM").value = lstVal.CT_RAZONSOCIAL;
                                                                                                                            ObtenNomCteServ();
                                                                                                                        } else {
                                                                                                                            //Colocamos el id en la pantalla de consultas
                                                                                                                            document.getElementById("VIEW_CLIENTE").value = lstVal.CT_ID;
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }

                                                            }
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            $("#dialogCte").dialog("close");
        }
    }
}
/**Reporte de Red de mlm*/
function mlmDesc() {
    var grid = jQuery("#CLIENTES");
    if (grid.getGridParam("selrow") != null) {
        var lstVal = grid.getRowData(grid.getGridParam("selrow"));
        $('#dialogMLM').dialog('option', 'title', "Descendencia del nodo " + lstVal.CT_ID + " " + lstVal.CT_RAZONSOCIAL);
        var strHtml = "<table id=\"\">" +
                "<tr><td>Nombre:</td><td>" + lstVal.CT_RAZONSOCIAL + "</td></tr>" +
                "<tr><td>Telefono:</td><td>" + lstVal.CT_TELEFONO1 + "</td></tr>" +
                "<tr><td>Email:</td><td>" + lstVal.CT_EMAIL1 + "</td></tr>" +
                "<table id=\"mlmGrid\"><tr><td/></tr></table> " +
                "<div id=\"pager\"></div> ";
        document.getElementById("dialogMLM_inside").innerHTML = strHtml;
        //Abrimos el grid
        jQuery("#mlmGrid").jqGrid({
            treeGrid: false, //Nvo Campo
            treeGridModel: 'adjacency', //Nvo Campo
            ExpandColumn: 'Clave', //Nvo Campo
            ExpandColClick: true, //Nvo Campo
            url: 'MLM_process.jsp?id=2&NodoId=' + lstVal.CT_ID,
            datatype: 'xml',
            mtype: 'POST',
            rowNum: 100000,
            colNames: ["Id", "Clave", "Upline", "Nombre", "Email", "Telefono", "P.Puntos", "P.Negocio", "G.Puntos", "G.Negocio", "Comision"],
            colModel: [{
                    name: 'id',
                    index: 'id',
                    width: 1,
                    hidden: true,
                    key: true//Nvo Campo
                }, {
                    name: 'Clave',
                    index: 'Clave',
                    width: 200,
                    hidden: false,
                    sortable: false
                }, {
                    name: 'Upline',
                    index: 'Upline',
                    width: 100,
                    hidden: false,
                    key: true//Nvo Campo
                }, {
                    name: 'Nombre',
                    index: 'Nombre',
                    width: 220,
                    hidden: false,
                    sortable: false
                }, {
                    name: 'Email',
                    width: 100,
                    hidden: false
                }, {
                    name: 'Telefono',
                    width: 100,
                    hidden: false
                }, {
                    name: 'PPuntos',
                    width: 100,
                    formatter: 'number',
                    hidden: false
                }, {
                    name: 'PNegocio',
                    width: 100,
                    formatter: 'number',
                    hidden: false
                }, {
                    name: 'GPuntos',
                    width: 100,
                    formatter: 'number',
                    hidden: false
                }, {
                    name: 'GNegocio',
                    width: 100,
                    formatter: 'number',
                    hidden: false
                }, {
                    name: 'Comision',
                    width: 100,
                    formatter: 'number',
                    hidden: false
                }],
            height: 500,
            width: 500,
            pager: false,
            caption: ''
        });
        $("#dialogMLM").dialog("open");
    } else {
        alert("Necesita seleccionar un cliente");
    }
}
/**Obtiene el estado de cuenta del cliente*/
function CteEdo() {
//   var tabs = $("#tabsOptCLIENTES").tabs();
//var label =  "Edo de cuenta. ",
//                       id = "tabsOptCLIENTES-" + tabCounter,
//                       li = $(tabTemplate.replace(/#href/g, "#" + id).replace(/#label/g, label)),
//                       tabContentHtml =  "Tab " + tabCounter + " content.";
//               tabs.find(".ui-tabs-nav").append(li);
//               tabs.append("<div id='" + id + "'><p>" + tabContentHtml + "</p></div>");
//               tabs.tabs("refresh");
//               tabCounter++;

    var grid = jQuery("#CLIENTES");
    if (grid.getGridParam("selrow") != null) {
        var lstVal = grid.getRowData(grid.getGridParam("selrow"));
        MakeReportInside(12, "Cliente", lstVal.CT_ID, "1");
//      $("#tabsOptCLIENTES").tabs('enable', 1);
//      $("#tabsOptCLIENTES").tabs( "option","active", 1);
//      var div = document.getElementById("screenOptCLIENTES");
//      var strHtml = "<form method=post name=formUpload>";
//      strHtml += CreaHidden("CT_ID_EDO", lstVal.CT_ID);
//      strHtml += CreaDate(lstMsg[98], "FEC_INI", "", "", "", 0, "", "", "", false);
//      strHtml += CreaDate(lstMsg[99], "FEC_FIN", "", "", "", 0, "", "", "", false);
//      strHtml += CreaBoton("", "importar", lstMsg[100], "CteEdoDo();", "left", false, false);
//      strHtml += "</form>";
//      div.innerHTML = strHtml;
//      document.getElementById("VerDetaCLIENTES").innerHTML = lstMsg[101];
//      $("#FEC_INI").datepicker({
//         changeMonth: true,
//         changeYear: true
//      });
//      $("#FEC_FIN").datepicker({
//         changeMonth: true,
//         changeYear: true
//      });
//      $("#FEC_INI").datepicker('setDate', new Date());
//      $("#FEC_FIN").datepicker('setDate', new Date());
    }

}
/**Manda a generar el reporte del estado de cuenta*/
function CteEdoDo() {
    var a = confirm(lstMsg[102]);
    var strTipo = "PDF";
    if (a) {
        strTipo = "XLS";
    }
    var strHtml = CreaHidden("CT_ID_REPO", document.getElementById("CT_ID_EDO").value);
    strHtml += CreaHidden("FEC_INI", document.getElementById("FEC_INI").value);
    strHtml += CreaHidden("FEC_FIN", document.getElementById("FEC_FIN").value);
    openReport("EDO_CTA_CTE", strTipo, strHtml);
}
/**Abre el cuadro de dialogo de busqueda de cliente*/
function OpnDiagCteEdo1() {
    OpnOpt('CLIENTES', 'grid', 'dialogCte', false, false);
}
//Prosefi 3/04/2013, funcion para cargar actividad economica
function OpnDiagAc() {
    OpnOpt('AC', 'grid', 'dialog2', false, false);
}


/**SIRVE PARA OBTENER LAS DIRECCIONES DE ENTREGA**/
function  ObtenDireciones() {
    var bolExist = false;
    //Peticion por ajax para mostrar info
    $.ajax({
        type: "POST",
        data: "ct_id=" + document.getElementById("FCT_ID").value, //id de cliente seleccionado
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "ERP_DireccionesEntrega.jsp?ID=1",
        success: function (datos) {
            var lstDirecciones = datos.getElementsByTagName("Direcciones")[0];
            var lstDireccion = lstDirecciones.getElementsByTagName("direcciones");
            var objDir = document.getElementById("CT_DIRENTREGA");
            select_clear(objDir);
            for (j = 0; j < lstDireccion.length; j++) {
                bolExist = true;
                var objP = lstDireccion[j];
                if (j == 0) {
                    select_add(objDir, "<-Seleccione->", 0);
                }
                select_add(objDir, objP.getAttribute('dir_cliente'), objP.getAttribute('id_dir'));
            }
            if (bolExist == false) {
                select_add(objDir, '<--No existen mas Direcciones-->', 0);
            }

        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}

function initCt() {
    document.getElementById("BTN_SAVE").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_CANCEL").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_NEW").parentNode.parentNode.style.display = "";
    document.getElementById("BTN_DEL").parentNode.parentNode.style.display = "";
    document.getElementById("BTN_DFA_SAVE").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_DFA_CANCEL").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_DFA_NEW").parentNode.parentNode.style.display = "";
    if (document.getElementById("BTN_DFA_EDIT")) {
        document.getElementById("BTN_DFA_EDIT").parentNode.parentNode.style.display = "";
    }
    document.getElementById("BTN_DFA_DEL").parentNode.parentNode.style.display = "";
    status_Fields_CTEFinal("ocultar");
    status_Fields("ocultar");
    CambiaEstadosCte();
    showHiddeFieldContacto(1);
    showContactoCT();
}
/*INIT para monedaBlanca*/
function initCtMonedaBlanca() {
    document.getElementById("BTN_SAVE").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_CANCEL").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_NEW").parentNode.parentNode.style.display = "";
    document.getElementById("BTN_DEL").parentNode.parentNode.style.display = "";
    document.getElementById("BTN_DFA_SAVE").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_DFA_CANCEL").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_DFA_NEW").parentNode.parentNode.style.display = "";
    document.getElementById("BTN_DFA_DEL").parentNode.parentNode.style.display = "";
    status_Fields_CTEFinal_moneda("ocultar");
    status_Fields_moneda("ocultar");
    CambiaEstadosCte();
    loadSubsedes();
}
/*CASO ESPECIAL MONEDA BLANCA*/
function status_Fields_moneda(s) {

    var strCalle = document.getElementById("CDE_CALLE");
    var strColonia = document.getElementById("CDE_COLONIA");
    var strLocalidad = document.getElementById("CDE_LOCALIDAD");
    var strMunicipio = document.getElementById("CDE_MUNICIPIO");
    var strEstado = document.getElementById("CDE_ESTADO");
    var strCp = document.getElementById("CDE_CP");
    var strNumero = document.getElementById("CDE_NUMERO");
    var strNumInt = document.getElementById("CDE_NUMINT");
    var strTelefono = document.getElementById("CDE_TELEFONO1");
    var strNombre = document.getElementById("CDE_NOMBRE");
    var strEmail = document.getElementById("CDE_EMAIL");
    var strDesc = document.getElementById("CDE_DESCRIPCION");
    var strComentarios = document.getElementById("CDE_COMENTARIOS");
    var strPAIS = document.getElementById("CDE_PAIS");
    if (s == "mostrar") {
        strCalle.parentNode.parentNode.style.display = "";
        strColonia.parentNode.parentNode.style.display = "";
        strLocalidad.parentNode.parentNode.style.display = "";
        strMunicipio.parentNode.parentNode.style.display = "";
        strEstado.parentNode.parentNode.style.display = "";
        strCp.parentNode.parentNode.style.display = "";
        strNumero.parentNode.parentNode.style.display = "";
        strNumInt.parentNode.parentNode.style.display = "";
        strTelefono.parentNode.parentNode.style.display = "";
        strNombre.parentNode.parentNode.style.display = "";
        strEmail.parentNode.parentNode.style.display = "";
        strDesc.parentNode.parentNode.style.display = "";
        strComentarios.parentNode.parentNode.style.display = "";
        strPAIS.parentNode.parentNode.style.display = "";
    }
    if (s == "ocultar") {
        strCalle.parentNode.parentNode.style.display = "none";
        strColonia.parentNode.parentNode.style.display = "none";
        strLocalidad.parentNode.parentNode.style.display = "none";
        strMunicipio.parentNode.parentNode.style.display = "none";
        strEstado.parentNode.parentNode.style.display = "none";
        strCp.parentNode.parentNode.style.display = "none";
        strNumero.parentNode.parentNode.style.display = "none";
        strNumInt.parentNode.parentNode.style.display = "none";
        strTelefono.parentNode.parentNode.style.display = "none";
        strNombre.parentNode.parentNode.style.display = "none";
        strEmail.parentNode.parentNode.style.display = "none";
        strDesc.parentNode.parentNode.style.display = "none";
        strComentarios.parentNode.parentNode.style.display = "none";
        strPAIS.parentNode.parentNode.style.display = "none";
    }
    if (s == "limpiar") {
        strCalle.value = "";
        strColonia.value = "";
        strLocalidad.value = "";
        strMunicipio.value = "";
        strEstado.value = "";
        strCp.value = "";
        strNumero.value = "";
        strNumInt.value = "";
        strTelefono.value = "";
        strNombre.value = "";
        strEmail.value = "";
        strDesc.value = "";
        strComentarios.value = "";
        strPAIS.value = "";
    }

}
/*CASO ESPECIAL MONEDA BLANCA*/
function status_Fields_CTEFinal_moneda(s) {

    var strRazonS = document.getElementById("DFA_RAZONSOCIAL");
    var strRFC = document.getElementById("DFA_RFC");
    var strCalle = document.getElementById("DFA_CALLE");
    var strColonia = document.getElementById("DFA_COLONIA");
    var strLocalidad = document.getElementById("DFA_LOCALIDAD");
    var strMunicipio = document.getElementById("DFA_MUNICIPIO");
    var strEstado = document.getElementById("DFA_ESTADO");
    var strCp = document.getElementById("DFA_CP");
    var strNumero = document.getElementById("DFA_NUMERO");
    var strNumInt = document.getElementById("DFA_NUMINT");
    var strTelefono = document.getElementById("DFA_TELEFONO");
    var strPais = document.getElementById("DFA_PAIS");
    var strEmail = document.getElementById("DFA_EMAIL");
    var strEsVisible = document.getElementById("DFA_VISIBLE1");
    if (s == "mostrar") {
        strRazonS.parentNode.parentNode.style.display = "";
        strRFC.parentNode.parentNode.style.display = "";
        strCalle.parentNode.parentNode.style.display = "";
        strColonia.parentNode.parentNode.style.display = "";
        strLocalidad.parentNode.parentNode.style.display = "";
        strMunicipio.parentNode.parentNode.style.display = "";
        strEstado.parentNode.parentNode.style.display = "";
        strCp.parentNode.parentNode.style.display = "";
        strNumero.parentNode.parentNode.style.display = "";
        strNumInt.parentNode.parentNode.style.display = "";
        strTelefono.parentNode.parentNode.style.display = "";
        strPais.parentNode.parentNode.style.display = "";
        strEmail.parentNode.parentNode.style.display = "";
        strEsVisible.parentNode.parentNode.style.display = "";
    }
    if (s == "ocultar") {
        strRazonS.parentNode.parentNode.style.display = "none";
        strRFC.parentNode.parentNode.style.display = "none";
        strCalle.parentNode.parentNode.style.display = "none";
        strColonia.parentNode.parentNode.style.display = "none";
        strLocalidad.parentNode.parentNode.style.display = "none";
        strMunicipio.parentNode.parentNode.style.display = "none";
        strEstado.parentNode.parentNode.style.display = "none";
        strCp.parentNode.parentNode.style.display = "none";
        strNumero.parentNode.parentNode.style.display = "none";
        strNumInt.parentNode.parentNode.style.display = "none";
        strTelefono.parentNode.parentNode.style.display = "none";
        strPais.parentNode.parentNode.style.display = "none";
        strEmail.parentNode.parentNode.style.display = "none";
        strEsVisible.parentNode.parentNode.style.display = "none";
    }

    if (s == "limpiar") {
        strRazonS.value = "";
        strRFC.value = "";
        strNumInt.value = "";
        strCalle.value = "";
        strColonia.value = "";
        strLocalidad.value = "";
        strMunicipio.value = "";
        strEstado.value = "";
        strCp.value = "";
        strNumero.value = "";
        strTelefono.value = "";
        strPais.value = 0;
        strEmail.value = "";
        strEsVisible.value = 0;
    }
}
/*Sirve para cambiar el estado de los campos*/
function status_Fields(s) {

    var strCalle = document.getElementById("CDE_CALLE");
    var strColonia = document.getElementById("CDE_COLONIA");
    var strLocalidad = document.getElementById("CDE_LOCALIDAD");
    var strMunicipio = document.getElementById("CDE_MUNICIPIO");
    var strEstado = document.getElementById("CDE_ESTADO");
    var strCp = document.getElementById("CDE_CP");
    var strNumero = document.getElementById("CDE_NUMERO");
    var strNumInt = document.getElementById("CDE_NUMINT");
    var strTelefono = document.getElementById("CDE_TELEFONO1");
    var strNombre = document.getElementById("CDE_NOMBRE");
    var strEmail = document.getElementById("CDE_EMAIL");
    var strDesc = document.getElementById("CDE_DESCRIPCION");
    var strComentarios = document.getElementById("CDE_COMENTARIOS");
    var strPAIS = document.getElementById("CDE_PAIS");
    if (document.getElementById("CDE_EMAIL2")) {
        var strEmail2 = document.getElementById("CDE_EMAIL2");
    }
    if (document.getElementById("CDE_CONTACTO1")) {
        var strContacto1 = document.getElementById("CDE_CONTACTO1");
    }
    if (document.getElementById("CDE_CONTACTO2")) {
        var strContacto2 = document.getElementById("CDE_CONTACTO2");
    }
    if (s == "mostrar") {
        strCalle.parentNode.parentNode.style.display = "";
        strColonia.parentNode.parentNode.style.display = "";
        strLocalidad.parentNode.parentNode.style.display = "";
        strMunicipio.parentNode.parentNode.style.display = "";
        strEstado.parentNode.parentNode.style.display = "";
        strCp.parentNode.parentNode.style.display = "";
        strNumero.parentNode.parentNode.style.display = "";
        strNumInt.parentNode.parentNode.style.display = "";
        strTelefono.parentNode.parentNode.style.display = "";
        strNombre.parentNode.parentNode.style.display = "";
        strEmail.parentNode.parentNode.style.display = "";
        strDesc.parentNode.parentNode.style.display = "";
        strComentarios.parentNode.parentNode.style.display = "";
        strPAIS.parentNode.parentNode.style.display = "";
        if (document.getElementById("CDE_EMAIL2")) {
            strEmail2.parentNode.parentNode.style.display = "";
        }
        if (document.getElementById("CDE_CONTACTO1")) {
            strContacto1.parentNode.parentNode.style.display = "";
        }
        if (document.getElementById("CDE_CONTACTO2")) {
            strContacto2.parentNode.parentNode.style.display = "";
        }
    }
    if (s == "ocultar") {
        strCalle.parentNode.parentNode.style.display = "none";
        strColonia.parentNode.parentNode.style.display = "none";
        strLocalidad.parentNode.parentNode.style.display = "none";
        strMunicipio.parentNode.parentNode.style.display = "none";
        strEstado.parentNode.parentNode.style.display = "none";
        strCp.parentNode.parentNode.style.display = "none";
        strNumero.parentNode.parentNode.style.display = "none";
        strNumInt.parentNode.parentNode.style.display = "none";
        strTelefono.parentNode.parentNode.style.display = "none";
        strNombre.parentNode.parentNode.style.display = "none";
        strEmail.parentNode.parentNode.style.display = "none";
        strDesc.parentNode.parentNode.style.display = "none";
        strComentarios.parentNode.parentNode.style.display = "none";
        strPAIS.parentNode.parentNode.style.display = "none";
        if (document.getElementById("CDE_EMAIL2")) {
            strEmail2.parentNode.parentNode.style.display = "none";
        }
        if (document.getElementById("CDE_CONTACTO1")) {
            strContacto1.parentNode.parentNode.style.display = "none";
        }
        if (document.getElementById("CDE_CONTACTO2")) {
            strContacto2.parentNode.parentNode.style.display = "none";
        }
    }
    if (s == "limpiar") {
        strCalle.value = "";
        strColonia.value = "";
        strLocalidad.value = "";
        strMunicipio.value = "";
        strEstado.value = "";
        strCp.value = "";
        strNumero.value = "";
        strNumInt.value = "";
        strTelefono.value = "";
        strNombre.value = "";
        strEmail.value = "";
        strDesc.value = "";
        strComentarios.value = "";
        strPAIS.value = "";
        if (document.getElementById("CDE_EMAIL2")) {
            strEmail2.value = "";
        }
        if (document.getElementById("CDE_CONTACTO1")) {
            strContacto1.value = "";
        }
        if (document.getElementById("CDE_CONTACTO2")) {
            strContacto2.value = "";
        }
    }




}

function status_Fields_CTEFinal(s) {

    var strRazonS = document.getElementById("DFA_RAZONSOCIAL");
    var strRFC = document.getElementById("DFA_RFC");
    var strCalle = document.getElementById("DFA_CALLE");
    var strColonia = document.getElementById("DFA_COLONIA");
    var strLocalidad = document.getElementById("DFA_LOCALIDAD");
    var strMunicipio = document.getElementById("DFA_MUNICIPIO");
    var strEstado = document.getElementById("DFA_ESTADO");
    var strCp = document.getElementById("DFA_CP");
    var strNumero = document.getElementById("DFA_NUMERO");
    var strNumInt = document.getElementById("DFA_NUMINT");
    var strTelefono = document.getElementById("DFA_TELEFONO");
    var strPais = document.getElementById("DFA_PAIS");
    var strEmail = document.getElementById("DFA_EMAIL");
    var strEsVisible = document.getElementById("DFA_VISIBLE1");
    if (document.getElementById("DFA_EMAIL2")) {
        var strEmail2 = document.getElementById("DFA_EMAIL2");
    }
    if (document.getElementById("DFA_CONTACTO1")) {
        var strContacto1 = document.getElementById("DFA_CONTACTO1");
    }
    if (document.getElementById("DFA_CONTACTO2")) {
        var strContacto2 = document.getElementById("DFA_CONTACTO2");
    }




    if (s == "mostrar") {
        strRazonS.parentNode.parentNode.style.display = "";
        strRFC.parentNode.parentNode.style.display = "";
        strCalle.parentNode.parentNode.style.display = "";
        strColonia.parentNode.parentNode.style.display = "";
        strLocalidad.parentNode.parentNode.style.display = "";
        strMunicipio.parentNode.parentNode.style.display = "";
        strEstado.parentNode.parentNode.style.display = "";
        strCp.parentNode.parentNode.style.display = "";
        strNumero.parentNode.parentNode.style.display = "";
        strNumInt.parentNode.parentNode.style.display = "";
        strTelefono.parentNode.parentNode.style.display = "";
        strPais.parentNode.parentNode.style.display = "";
        strEmail.parentNode.parentNode.style.display = "";
        strEsVisible.parentNode.parentNode.style.display = "";
        if (document.getElementById("DFA_EMAIL2")) {
            strEmail2.parentNode.parentNode.style.display = "";
        }
        if (document.getElementById("DFA_CONTACTO1")) {
            strContacto1.parentNode.parentNode.style.display = "";
        }
        if (document.getElementById("DFA_CONTACTO2")) {
            strContacto2.parentNode.parentNode.style.display = "";
        }

    }
    if (s == "ocultar") {
        strRazonS.parentNode.parentNode.style.display = "none";
        strRFC.parentNode.parentNode.style.display = "none";
        strCalle.parentNode.parentNode.style.display = "none";
        strColonia.parentNode.parentNode.style.display = "none";
        strLocalidad.parentNode.parentNode.style.display = "none";
        strMunicipio.parentNode.parentNode.style.display = "none";
        strEstado.parentNode.parentNode.style.display = "none";
        strCp.parentNode.parentNode.style.display = "none";
        strNumero.parentNode.parentNode.style.display = "none";
        strNumInt.parentNode.parentNode.style.display = "none";
        strTelefono.parentNode.parentNode.style.display = "none";
        strPais.parentNode.parentNode.style.display = "none";
        strEmail.parentNode.parentNode.style.display = "none";
        strEsVisible.parentNode.parentNode.style.display = "none";
        if (document.getElementById("DFA_EMAIL2")) {
            strEmail2.parentNode.parentNode.style.display = "none";
        }
        if (document.getElementById("DFA_CONTACTO1")) {
            strContacto1.parentNode.parentNode.style.display = "none";
        }
        if (document.getElementById("DFA_CONTACTO2")) {
            strContacto2.parentNode.parentNode.style.display = "none";
        }

    }

    if (s == "limpiar") {
        strRazonS.value = "";
        strRFC.value = "";
        strNumInt.value = "";
        strCalle.value = "";
        strColonia.value = "";
        strLocalidad.value = "";
        strMunicipio.value = "";
        strEstado.value = "";
        strCp.value = "";
        strNumero.value = "";
        strTelefono.value = "";
        strPais.value = 0;
        strEmail.value = "";
        strEsVisible.value = 0;
        if (document.getElementById("DFA_EMAIL2")) {
            strEmail2.value = "";
        }
        strContacto1.value = "";
        strContacto2.value = "";
    }
}
/*Sirve para preparar los campos para una nueva direccion*/
function newDir() {
    status_Fields("mostrar");
    document.getElementById("BTN_SAVE").parentNode.parentNode.style.display = "";
    document.getElementById("BTN_CANCEL").parentNode.parentNode.style.display = "";
    document.getElementById("BTN_NEW").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_DEL").parentNode.parentNode.style.display = "none";
}
/*Sirve para preparar los campos para agregar un nuevo cliente final*/
function newDirCteFinal() {
    status_Fields_CTEFinal("mostrar");
    document.getElementById("BTN_DFA_NEW").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_DFA_DEL").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_DFA_EDIT").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_DFA_SAVE").parentNode.parentNode.style.display = "";
    document.getElementById("BTN_DFA_CANCEL").parentNode.parentNode.style.display = "";
}
/*Sirve para cancelar la operacion y oculatr los campos*/
function cancelDir() {
    status_Fields("ocultar");
    document.getElementById("BTN_SAVE").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_CANCEL").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_NEW").parentNode.parentNode.style.display = "";
    document.getElementById("BTN_DEL").parentNode.parentNode.style.display = "";
    status_Fields("limpiar");
}

/*Sirve para cancelar la operacion y oculatr los campos de Cliente Final*/
function cancelDirCteFinal() {
    status_Fields_CTEFinal("ocultar");
    document.getElementById("BTN_DFA_SAVE").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_DFA_CANCEL").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_DFA_NEW").parentNode.parentNode.style.display = "";
    document.getElementById("BTN_DFA_EDIT").parentNode.parentNode.style.display = "";
    document.getElementById("BTN_DFA_DEL").parentNode.parentNode.style.display = "";
    status_Fields_CTEFinal("limpiar");
}

/*Sirve para guardar la nueva direcion de entrega*/
function saveDir() {
    if (document.getElementById("CT_ID").value == "" || document.getElementById("CT_ID").value == "0") {
        alert("ATENCIÓN: Primero debes guardar al cliente para asignarle mas direcciones de entrega.");
    } else {
        var strPost = "";
        if (validaDatos() != 0) {
            $("#dialogWait").dialog("open");
            strPost = "&CDE_CALLE=" + document.getElementById("CDE_CALLE").value;
            strPost += "&CDE_COLONIA=" + document.getElementById("CDE_COLONIA").value;
            strPost += "&CDE_LOCALIDAD=" + document.getElementById("CDE_LOCALIDAD").value;
            strPost += "&CDE_MUNICIPIO=" + document.getElementById("CDE_MUNICIPIO").value;
            strPost += "&CDE_ESTADO=" + document.getElementById("CDE_ESTADO").value;
            strPost += "&CDE_CP=" + document.getElementById("CDE_CP").value;
            strPost += "&CDE_NUMERO=" + document.getElementById("CDE_NUMERO").value;
            strPost += "&CDE_NUMINT=" + document.getElementById("CDE_NUMINT").value;
            strPost += "&CDE_TELEFONO1=" + document.getElementById("CDE_TELEFONO1").value;
            strPost += "&CDE_NOMBRE=" + document.getElementById("CDE_NOMBRE").value;
            strPost += "&CDE_EMAIL=" + document.getElementById("CDE_EMAIL").value;
            strPost += "&CDE_DESCRIPCION=" + document.getElementById("CDE_DESCRIPCION").value;
            strPost += "&CDE_COMENTARIOS=" + document.getElementById("CDE_COMENTARIOS").value;
            strPost += "&CT_ID=" + document.getElementById("CT_ID").value;
            strPost += "&CDE_PAIS=" + document.getElementById("CDE_PAIS").value;
            strPost += "&CDE_EMAIL2=" + document.getElementById("CDE_EMAIL2").value;
            strPost += "&CDE_CONTACTO1=" + document.getElementById("CDE_CONTACTO1").value;
            strPost += "&CDE_CONTACTO2=" + document.getElementById("CDE_CONTACTO2").value;

            $.ajax({
                type: "POST",
                data: strPost,
                scriptCharset: "utf-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "html",
                url: "ERP_DireccionesEntrega.jsp?ID=2",
                success: function (datos) {
                    if (datos.substring(0, 2) == "OK") {
                        alert("Alta Exitosa!");
                        LoadDirecciones(document.getElementById("CT_ID").value);
                        status_Fields("ocultar");
                        status_Fields("limpiar");
                    } else {
                        alert(datos);
                    }
                    $("#dialogWait").dialog("close");
                },
                error: function (objeto, quepaso, otroobj) {
                    alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
                }
            });
        }
    }

}

/*Sirve para validar formulario de direcciones de entrega*/
function validaDatos() {
    var strCalle = document.getElementById("CDE_CALLE");
    var strColonia = document.getElementById("CDE_COLONIA");
    var strLocalidad = document.getElementById("CDE_LOCALIDAD");
    var strMunicipio = document.getElementById("CDE_MUNICIPIO");
    var strEstado = document.getElementById("CDE_ESTADO");
    var strCp = document.getElementById("CDE_CP");
    var strNumero = document.getElementById("CDE_NUMERO");
    var strNumInt = document.getElementById("CDE_NUMINT");
    var strTelefono = document.getElementById("CDE_TELEFONO1");
    var strNombre = document.getElementById("CDE_NOMBRE");
    var strDesc = document.getElementById("CDE_DESCRIPCION");
    var strEmail = document.getElementById("CDE_EMAIL");
    var strEmail2 = document.getElementById("CDE_EMAIL2");
    var strContacto1 = document.getElementById("CDE_CONTACTO1");
    var strContacto2 = document.getElementById("CDE_CONTACTO2");
    if (strCalle.value == "") {
        alert("Atención: Captura la calle");
        strCalle.focus();
        return 0;
    }
    if (strColonia.value == "") {
        alert("Atención: Captura la Colonia");
        strColonia.focus();
        return 0;
    }

    if (strMunicipio.value == "") {
        alert("Atención: Captura el municipio o Delegación");
        strMunicipio.focus();
        return 0;
    }
    if (strEstado.value == "") {
        alert("Atención: Captura el Estado");
        strEstado.focus();
        return 0;
    }
    if (strCp.value == "") {
        alert("Atención: Captura el Codigo Postal");
        strCp.focus();
        return 0;
    }
    if (strTelefono.value == "") {
        alert("Atención: Captura algun nÃºmero telefonico");
        strTelefono.focus();
        return 0;
    }
    if (strEmail.value == "") {
        alert("AtenciÃ³n: Captura el Email 1");
        strEmail.focus();
        return 0;
    }
    if (strEmail2.value == "") {
        alert("AtenciÃ³n: Captura el Email 2");
        strEmail2.focus();
        return 0;
    }
    if (strContacto1.value == "") {
        alert("AtenciÃ³n: Captura el Contacto 1");
        strContacto1.focus();
        return 0;
    }
    if (strContacto2.value == "") {
        alert("AtenciÃ³n: Captura el Contacto 2");
        strContacto2.focus();
        return 0;
    }
    /*
     if (strDesc.value == "") {
     alert("AtenciÃ³n: Agrega alguna referencia Ej: Entre calle 1 y 3");
     strDesc.focus();
     return 0;
     }
     */

    return 1;
}


/*Sirve para refrescar el grid de las direcciones de entrega*/
function LoadDirecciones(idx) {
    var grid = jQuery("#grd_Direcciones");
    grid.setGridParam({
        url: "CIP_TablaOp.jsp?ID=5&opnOpt=DIRENT&_search=true&CT_ID=" + idx
    });
    grid.trigger("reloadGrid");
}
function LoadDireccionesCteFinal(idx) {
    var grid = jQuery("#grd_DireccionesDFA");
    grid.setGridParam({
        url: "CIP_TablaOp.jsp?ID=5&opnOpt=DIR_CTE&_search=true&CT_ID=" + idx
    });
    grid.trigger("reloadGrid");
}

function LoadProspectacion(idx) {


    var strPOST = "";
    strPOST += "&CT_ID=" + idx;

    $.ajax({
        type: "POST",
        data: encodeURI(strPOST),
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "crm_historial_seg.jsp?id=2",
        success: function (datoVal) {


            var objXML = datoVal.getElementsByTagName("crm_historial_seguimiento")[0];
            if (objXML != null) {
                var lstTiks = objXML.getElementsByTagName("crm_historial_seguimiento1");


                for (var i = 0; i < lstTiks.length; i++) {
                    var obj = lstTiks[i];


                    var boolGenEven;
                    if (obj.getAttribute("HS_GENERAR_EVENTO") == 1) {
                        boolGenEven = "SI";
                    } else {
                        boolGenEven = "NO";
                    }
                    var mostrar = {
                        HS_FECHA: obj.getAttribute("HS_FECHA"),
                        HS_HORA: obj.getAttribute("HS_HORA"),
                        HS_COMENTARIOS: obj.getAttribute("HS_COMENTARIOS"),
                        HS_TIEMPO_ACUMULADO: obj.getAttribute("HS_TIEMPO_ACUMULADO"),
                        EP_ID: obj.getAttribute("EP_ID"),
                        HS_GENERAR_EVENTO: boolGenEven,
                        HS_FECHA_SIGUIENTE_CONTACTO: obj.getAttribute("HS_FECHA_SIGUIENTE_CONTACTO"),
                    };
                    jQuery("#GRID_HISTORIAL").addRowData(i, mostrar, "last");
                }

            } else {
                alert("LA " + strNomDoc + " " + obj.value + " NO EXISTE");
            }

            document.getElementById("HS_COMENTARIOS").value = "";
            document.getElementById("HS_GENERAR_EVENTO1").value = "";
            document.getElementById("HS_FECHA_SIGUIENTE_CONTACTO").value = "";

            $("#dialog").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });

}

//Controla el flujo de los tabs
function tabShowClientes(event, ui) {

    var index = ui.newTab.index();
    // alert(index); // This is never displayed
    //console.log("Entro tabShowClientes");
    var idx = 0;
    if (document.getElementById("CT_ID") != null) {

        idx = document.getElementById("CT_ID").value;
    }
    if (index === 5) {

        LoadDirecciones(idx);
    }
    if (index === 6) {

        LoadDireccionesCteFinal(idx);
    }
    if (index === 7) {
        if (d.getElementById("CT_ID").value != "") {
            LoadProspectacion(idx);
        }
    }

}

/**Borra el item seleccionado*/
function delDireccion() {
    var grid = jQuery("#grd_Direcciones");
    if (grid.getGridParam("selrow") != null) {
        var id = grid.getGridParam("selrow");
        var lstRow = grid.getRowData(id);
        var idDir = lstRow.CDE_ID;
        if (getUso(idDir) != true) {
            if (confirm("AtenciÃ³n: Confirma que desea eliminar la dirección")) {
                grid.delRowData(grid.getGridParam("selrow"));
                deleteDireccion(idDir);
            }
        } else {

            alert("Atención: Esta direcciÃ³n no puede ser borrada, ya se uso en algunos documentos");
        }
    } else {
        alert("Selecciona un registro el la tabla");
    }
}

/*Nos regresa true o false de si la direccion ya fue usada*/
function getUso(idDir) {

    $.ajax({
        type: "POST",
        data: "id_dir=" + idDir,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "ERP_DireccionesEntrega.jsp?ID=3",
        success: function (datos) {
            if (datos.substring(0, 2) == "OK") {
                return true;
            } else {
                return false;
                alert(datos);
            }
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}


/*SIRVE PARA BORRAR UNA DIRECCION DE ENTREGA DE LA BD*/
function deleteDireccion(idDir) {
    $.ajax({
        type: "POST",
        data: "dir_id=" + idDir,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "ERP_DireccionesEntrega.jsp?ID=4",
        success: function (datos) {
            if (datos.substring(0, 2) == "OK") {
                alert("Dirección de entrega Eliminada...");
            } else {

                alert(datos);
            }
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}


/**Borra el item seleccionado*/
function delDireccionCteFinal() {
    var grid = jQuery("#grd_DireccionesDFA");
    if (grid.getGridParam("selrow") != null) {
        var id = grid.getGridParam("selrow");
        var lstRow = grid.getRowData(id);
        var idDir = lstRow.DFA_ID;
        if (getUsoCteFinal(idDir) != true) {
            if (confirm("Atención: Confirma que desea eliminar la dirección")) {
                grid.delRowData(grid.getGridParam("selrow"));
                deleteDireccionCteFinal(idDir);
            }
        } else {
            alert("Atención: Esta dirección no puede ser borrada, ya se uso en algunos documentos");
        }
    } else {
        alert("Selecciona un registro el la tabla");
    }
}

/*Nos regresa true o false de si la direccion ya fue usada*/
function getUsoCteFinal(idDir) {

    $.ajax({
        type: "POST",
        data: "id_dir=" + idDir,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "ERP_ClienteFacturacion.jsp?ID=3",
        success: function (datos) {
            if (datos.substring(0, 2) == "OK") {
                return true;
            } else {
                return false;
                alert(datos);
            }
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}


/*SIRVE PARA BORRAR UNA DIRECCION DE ENTREGA DE LA BD*/
function deleteDireccionCteFinal(idDir) {
    $.ajax({
        type: "POST",
        data: "dir_id=" + idDir,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "ERP_ClienteFacturacion.jsp?ID=4",
        success: function (datos) {
            if (datos.substring(0, 2) == "OK") {
                alert("Dirección de entrega Eliminada...");
            } else {

                alert(datos);
            }
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}

/*Sirve para guardar la nueva direcion de entrega*/
function saveDirCteFinal() {
    if (document.getElementById("CT_ID").value == "" || document.getElementById("CT_ID").value == "0") {
        alert("ATENCIÓN: Primero debes guardar al cliente para asignarle mas direcciones de entrega.");
    } else {
        var strPost = "";
        if (validaDatosCteFinal() != 0) {
            //Valida si es edicion
            if (document.getElementById("DFA_ID").value) {
                var strDFA_VISIBLE = "0";
                if (document.getElementById("DFA_VISIBLE1").checked)
                {
                    strDFA_VISIBLE = 1;
                } else
                {
                    strDFA_VISIBLE += 0;
                }
                setDatosCteFinal({
                    DFA_ID: document.getElementById("DFA_ID").value,
                    DFA_RAZONSOCIAL: document.getElementById("DFA_RAZONSOCIAL").value,
                    DFA_RFC: document.getElementById("DFA_RFC").value,
                    DFA_CALLE: document.getElementById("DFA_CALLE").value,
                    DFA_COLONIA: document.getElementById("DFA_COLONIA").value,
                    DFA_LOCALIDAD: document.getElementById("DFA_LOCALIDAD").value,
                    DFA_MUNICIPIO: document.getElementById("DFA_MUNICIPIO").value,
                    DFA_ESTADO: document.getElementById("DFA_ESTADO").value,
                    DFA_CP: document.getElementById("DFA_CP").value,
                    DFA_NUMERO: document.getElementById("DFA_NUMERO").value,
                    DFA_NUMINT: document.getElementById("DFA_NUMINT").value,
                    DFA_TELEFONO: document.getElementById("DFA_TELEFONO").value,
                    DFA_PAIS: document.getElementById("DFA_PAIS").value,
                    DFA_EMAIL: document.getElementById("DFA_EMAIL").value,
                    DFA_VISIBLE: strDFA_VISIBLE,
                    DFA_EMAIL2: document.getElementById("DFA_EMAIL2").value,
                    DFA_CONTACTO1: document.getElementById("DFA_CONTACTO1").value,
                    DFA_CONTACTO2: document.getElementById("DFA_CONTACTO2").value,
                    CT_ID: document.getElementById("CT_ID").value
                });

            } else {
                strPost = "&DFA_CALLE=" + document.getElementById("DFA_CALLE").value;
                strPost += "&DFA_COLONIA=" + document.getElementById("DFA_COLONIA").value;
                strPost += "&DFA_LOCALIDAD=" + document.getElementById("DFA_LOCALIDAD").value;
                strPost += "&DFA_MUNICIPIO=" + document.getElementById("DFA_MUNICIPIO").value;
                strPost += "&DFA_ESTADO=" + getTextSelect("DFA_ESTADO");
                strPost += "&DFA_CP=" + document.getElementById("DFA_CP").value;
                strPost += "&DFA_NUMERO=" + document.getElementById("DFA_NUMERO").value;
                strPost += "&DFA_NUMINT=" + document.getElementById("DFA_NUMINT").value;
                strPost += "&DFA_TELEFONO=" + document.getElementById("DFA_TELEFONO").value;
                strPost += "&DFA_RAZONSOCIAL=" + document.getElementById("DFA_RAZONSOCIAL").value;
                strPost += "&DFA_RFC=" + document.getElementById("DFA_RFC").value;
                strPost += "&DFA_EMAIL=" + document.getElementById("DFA_EMAIL").value;
                strPost += "&CT_ID=" + document.getElementById("CT_ID").value;
                strPost += "&DFA_PAIS=" + getTextSelect("DFA_PAIS");
                strPost += "&DFA_EMAIL2=" + document.getElementById("DFA_EMAIL2").value;
                strPost += "&DFA_CONTACTO1=" + document.getElementById("DFA_CONTACTO1").value;
                strPost += "&DFA_CONTACTO2=" + document.getElementById("DFA_CONTACTO2").value;

                if (document.getElementById("DFA_VISIBLE1").checked)
                {
                    strPost += "&DFA_VISIBLE=" + 1;
                } else
                {
                    strPost += "&DFA_VISIBLE=" + 0;
                }

                $.ajax({
                    type: "POST",
                    data: strPost,
                    scriptCharset: "utf-8",
                    contentType: "application/x-www-form-urlencoded;charset=utf-8",
                    cache: false,
                    dataType: "html",
                    url: "ERP_ClienteFacturacion.jsp?ID=2",
                    success: function (datos) {
                        if (datos.substring(0, 2) == "OK") {
                            alert("Alta Exitosa!");
                            LoadDireccionesCteFinal(document.getElementById("CT_ID").value);
                            status_Fields_CTEFinal("ocultar");
                            cancelDirCteFinal();
                        } else {
                            alert(datos);
                        }
                        $("#dialogWait").dialog("close");
                    },
                    error: function (objeto, quepaso, otroobj) {
                        alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
                    }
                });
            }
        }
    }

}

function validaDatosCteFinal() {
    var strCalle = document.getElementById("DFA_CALLE").value;
    var strColonia = document.getElementById("DFA_COLONIA").value;
    var strMunicipio = document.getElementById("DFA_MUNICIPIO").value;
    var strEstado = document.getElementById("DFA_ESTADO").value;
    var strCp = document.getElementById("DFA_CP").value;
    var strNumero = document.getElementById("DFA_NUMERO").value;
    var strRazonS = document.getElementById("DFA_RAZONSOCIAL").value;
    var strRFC = document.getElementById("DFA_RFC").value;
    var strPais = document.getElementById("DFA_PAIS").value;
    var strMail1 = document.getElementById("DFA_EMAIL").value;
    var strMail2 = document.getElementById("DFA_EMAIL2").value;
    var strContacto1 = document.getElementById("DFA_CONTACTO1").value;
    var strContacto2 = document.getElementById("DFA_CONTACTO2").value;


    if (strCalle == "") {
        alert("Atención: Captura la calle");
        strCalle.focus();
        return 0;
    }
    if (strColonia == "") {
        alert("Atención: Captura la Colonia");
        strColonia.focus();
        return 0;
    }

    if (strMunicipio == "") {
        alert("Atención: Captura el municipio o Delegación");
        strMunicipio.focus();
        return 0;
    }
    if (strEstado == "") {
        alert("Atención: Captura el Estado");
        strEstado.focus();
        return 0;
    }
    if (strCp == "") {
        alert("Atención: Captura el Codigo Postal");
        strCp.focus();
        return 0;
    }

    if (strRazonS == "") {
        alert("Atención: Captura la Razon social");
        strRazonS.focus();
        return 0;
    }
    if (strRFC == "") {
        alert("Atención: Captura el RFC");
        strRFC.focus();
        return 0;
    }
    if (strNumero == "") {
        alert("Atención: Captura el Numero");
        strRFC.focus();
        return 0;
    }
    if (strPais == 0) {
        alert("Atención: Captura el Pais");
        strRFC.focus();
        return 0;
    }
    if (strMail1 == "") {
        alert("Atención: Captura el Mail 1");
        strMail1.focus();
        return 0;
    }
    if (strMail2 == "") {
        alert("Atención: Captura el Mail 2");
        strMail2.focus();
        return 0;
    }
    if (strContacto1 == "") {
        alert("Atención: Captura el Contacto 1");
        strContacto1.focus();
        return 0;
    }
    if (strContacto2 == "") {
        alert("Atencion: Captura el Contacto 2");
        strContacto2.focus();
        return 0;
    }

    return 1;
}

function showPosicionSel()
{
    $("#dialogWait").dialog("open");
    var strCT_ID = document.getElementById("CT_SPONZOR").value;
    if (strCT_ID == "")
    {
        alert("Se debe poner por quien esta invitado");
    } else {
        var strPOST = "CT_ID=" + strCT_ID;
        $.ajax({
            type: "POST",
            data: strPOST,
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "ERP_Descendencia.jsp?ID=1",
            success: function (datos) {
                if (datos.substring(0, 2) == "OK") {
                    var numero = parseInt(datos.substring(2, 100));
                    if (numero > 3)
                    {
                        numero = 3;
                    }
                    var objSelDest = document.getElementById("CT_NIVEL");
                    select_clear(objSelDest);
                    for (var i = 1; i <= numero; i++) {
                        select_add(objSelDest, i, i);
                    }
                    document.getElementById("CT_NIVEL").parentNode.parentNode.style.display = "";
                    $("#dialogWait").dialog("close");
                } else {
                    return false;
                    alert(datos);
                }
                $("#dialogWait").dialog("close");
            },
            error: function (objeto, quepaso, otroobj) {
                alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }
        });
    }

}
/**
 * Calcula el upline
 * */
function CalculaDebajoDeQuien()
{
    var strCT_ID = document.getElementById("CT_SPONZOR").value;
    if (strCT_ID == "")
    {
        strCT_ID = "0";
    }
    var strPOST = "CT_ID=" + strCT_ID;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: strPOST,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "ERP_Descendencia.jsp?ID=2",
        success: function (datos) {
            if (datos.substring(0, 2) == "OK") {
                var numero = parseInt(datos.substring(2, 100));
                document.getElementById("CT_UPLINE").value = numero;
            } else {
                return false;
                alert(datos);
            }
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}


function CambiaEstadosCte()
{
    var strPais = document.getElementById("PA_ID").value;
    var strPOST = "PA_ID=" + strPais;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: strPOST,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "ERP_EstadosPais.jsp?ID=1",
        success: function (datos) {
            var objsc = datos.getElementsByTagName("Estados")[0];
            var lstEst = objsc.getElementsByTagName("Estado");
            var objEst = document.getElementById("CT_ESTADO");
            var bolExist = false;
            var strDatEstado = document.getElementById("CT_ESTADO").value;
            select_clear(objEst);
            for (j = 0; j < lstEst.length; j++) {
                bolExist = true;
                var objP = lstEst[j];
                if (j == 0) {
                    select_add(objEst, "Seleccione", 0);
                }
                select_add(objEst, objP.getAttribute('ESP_NOMBRE'), objP.getAttribute('ESP_NOMBRE'));
            }
            if (bolExist == false) {
                select_add(objEst, 'Seleccione', 0);
            }
            document.getElementById("CT_ESTADO").value = strDatEstado;
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}



function tabShowClientes1() {

    document.getElementById('CT_NOMBRE_INVITO').value = "";
    var strPost = "CT_ID=" + document.getElementById('CT_ID_INVITO').value;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "ERP_ConAntCte.jsp?id=2",
        success: function (datoVal) {

            var objXML = datoVal.getElementsByTagName("clientes")[0];
            if (objXML != null) {

                var lstTiks = objXML.getElementsByTagName("cliente");
                if (lstTiks.length != 0) {
                    for (var i = 0; i < lstTiks.length; i++) {
                        var obj = lstTiks[i];
                        document.getElementById('CT_NOMBRE_INVITO').value = obj.getAttribute("CT_RAZONSOCIAL");
                    }
                } else {
                    alert("EL cliente no existe con ese ID");
                }

            } else {
                alert("LA " + strNomDoc + " " + obj.value + " NO EXISTE");
            }

        },
        error: function (objeto, quepaso, otroobj) {
            alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}

function Historial_AgregarGrid() {


    if (d.getElementById("CT_ID").value != "") {

//      if (d.getElementById("CAM_ID").value != 0) {
        if (d.getElementById("EP_ID").value != 0) {

            $("#dialog").dialog("open");

            var strPOST = "";
            strPOST += "&CT_ID=" + d.getElementById("CT_ID").value;
            strPOST += "&HS_COMENTARIOS=" + d.getElementById("HS_COMENTARIOS").value;
            strPOST += "&HS_USER=" + d.getElementById("HS_USER").value;
            strPOST += "&CAM_ID=" + d.getElementById("CAM_ID").value;
            strPOST += "&EP_ID=" + d.getElementById("EP_ID").value;
            if (d.getElementById("HS_GENERAR_EVENTO1").checked == true) {
                strPOST += "&HS_GENERAR_EVENTO=1";
            } else {
                strPOST += "&HS_GENERAR_EVENTO=0";
            }

            strPOST += "&HS_FECHA_SIGUIENTE_CONTACTO=" + d.getElementById("HS_FECHA_SIGUIENTE_CONTACTO").value;
            $.ajax({
                type: "POST",
                data: encodeURI(strPOST),
                scriptCharset: "utf-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "xml",
                url: "crm_historial_seg.jsp?id=1",
                success: function (datoVal) {


                    var objXML = datoVal.getElementsByTagName("crm_historial_seguimiento")[0];
                    if (objXML != null) {
                        var lstTiks = objXML.getElementsByTagName("crm_historial_seguimiento1");


                        for (var i = 0; i < lstTiks.length; i++) {
                            var obj = lstTiks[i];


                            var boolGenEven;
                            if (obj.getAttribute("HS_GENERAR_EVENTO") == 1) {
                                boolGenEven = "SI";
                            } else {
                                boolGenEven = "NO";
                            }
                            var mostrar = {
                                HS_FECHA: obj.getAttribute("HS_FECHA"),
                                HS_HORA: obj.getAttribute("HS_HORA"),
                                HS_COMENTARIOS: obj.getAttribute("HS_COMENTARIOS"),
                                HS_TIEMPO_ACUMULADO: obj.getAttribute("HS_TIEMPO_ACUMULADO"),
                                EP_ID: obj.getAttribute("EP_ID"),
                                HS_GENERAR_EVENTO: boolGenEven,
                                HS_FECHA_SIGUIENTE_CONTACTO: obj.getAttribute("HS_FECHA_SIGUIENTE_CONTACTO"),
                            };
                            jQuery("#GRID_HISTORIAL").addRowData(i, mostrar, "last");
                        }

                    } else {
                        alert("LA " + strNomDoc + " " + obj.value + " NO EXISTE");
                    }

                    document.getElementById("HS_COMENTARIOS").value = "";
                    document.getElementById("HS_GENERAR_EVENTO1").value = "";
                    document.getElementById("HS_FECHA_SIGUIENTE_CONTACTO").value = "";

                    $("#dialog").dialog("close");
                },
                error: function (objeto, quepaso, otroobj) {
                    alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
                    $("#dialogWait").dialog("close");
                }
            });


        } else {
            alert("Seleccione el Prospecto Estatus");
        }
//      } else {
//         alert("Seleccione la campaÃ±a");
//      }

    } else {
        alert("Solo puede agregar cuando el cliente ya este dado de alta");
    }





}
/**Obtiene el nombre del nodo padre*/
function getNameId() {

    document.getElementById('CT_NOMBRE_INVITO').value = "";
    var strPost = "CT_ID=" + document.getElementById('CT_UPLINE').value;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "ERP_ConAntCte.jsp?id=2",
        success: function (datoVal) {

            var objXML = datoVal.getElementsByTagName("clientes")[0];
            if (objXML != null) {

                var lstTiks = objXML.getElementsByTagName("cliente");
                if (lstTiks.length != 0) {
                    for (var i = 0; i < lstTiks.length; i++) {
                        var obj = lstTiks[i];
                        document.getElementById('CT_NOMBRE_INVITO').value = obj.getAttribute("CT_RAZONSOCIAL");
                    }
                } else {
                    alert("EL cliente no existe con ese ID");
                }

            } else {
                alert("LA " + strNomDoc + " " + obj.value + " NO EXISTE");
            }

        },
        error: function (objeto, quepaso, otroobj) {
            alert(":Upline cte:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}
/**Obtiene el nombre del nodo padre sponzor*/
function getNameIdSponzor() {

    document.getElementById('CT_NOMBRE_INVITO').value = "";
    var strPost = "CT_ID=" + document.getElementById('CT_SPONZOR').value;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "ERP_ConAntCte.jsp?id=2",
        success: function (datoVal) {

            var objXML = datoVal.getElementsByTagName("clientes")[0];
            if (objXML != null) {

                var lstTiks = objXML.getElementsByTagName("cliente");
                if (lstTiks.length != 0) {
                    for (var i = 0; i < lstTiks.length; i++) {
                        var obj = lstTiks[i];
                        document.getElementById('CT_NOMBRE_INVITO').value = obj.getAttribute("CT_RAZONSOCIAL");
                        //Que cÃ¡lcule debajo de quien lo ponemos
                        //CalculaDebajoDeQuien();
                    }
                } else {
                    alert("EL cliente no existe con ese ID");
                }

            } else {
                alert("LA " + strNomDoc + " " + obj.value + " NO EXISTE");
            }

        },
        error: function (objeto, quepaso, otroobj) {
            alert(":Upline cte:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}

function opnLpreciosEspecialesMak() {
    var grid = jQuery("#CLIENTES");
    var id = grid.getGridParam("selrow");
    if (grid.getGridParam("selrow") != null) {
        var objSecModiVta = objMap.getScreen("P_ESPECIAL_CT");
        if (objSecModiVta != null) {
            objSecModiVta.bolActivo = false;
            objSecModiVta.bolMain = false;
            objSecModiVta.bolInit = false;
            objSecModiVta.idOperAct = 0;
        }
        OpnOpt("P_ESPECIAL_CT", "_ed", "dialog", false, false, true);
    } else {
        alert("Selecciona una fila en el tab Precios Clientes");
    }
}

function showHiddeFieldContacto(intOpc) {
    switch (intOpc) {
        case 1:
            //Esconde los Botones y Fields Iniciales
            if (document.getElementById("BTN_ADD_CNT")) {
                document.getElementById("BTN_ADD_CNT").parentNode.parentNode.style.display = 'none';
            }
            if (document.getElementById("BTN_CANCEL_CNT")) {
                document.getElementById("BTN_CANCEL_CNT").parentNode.parentNode.style.display = 'none';
            }
            if (document.getElementById("CCO_ID")) {
                document.getElementById("CCO_ID").parentNode.parentNode.style.display = 'none';
            }
            if (document.getElementById("CCO_NOMBRE")) {
                document.getElementById("CCO_NOMBRE").parentNode.parentNode.style.display = 'none';
            }
            if (document.getElementById("CCO_APPATERNO")) {
                document.getElementById("CCO_APPATERNO").parentNode.parentNode.style.display = 'none';
            }
            if (document.getElementById("CCO_APMATERNO")) {
                document.getElementById("CCO_APMATERNO").parentNode.parentNode.style.display = 'none';
            }
            if (document.getElementById("CCO_CORREO")) {
                document.getElementById("CCO_CORREO").parentNode.parentNode.style.display = 'none';
            }
            if (document.getElementById("CCO_CORREO2")) {
                document.getElementById("CCO_CORREO2").parentNode.parentNode.style.display = 'none';
            }
            if (document.getElementById("CCO_TELEFONO")) {
                document.getElementById("CCO_TELEFONO").parentNode.parentNode.style.display = 'none';
            }
            if (document.getElementById("CT_MAILMES2")) {
                document.getElementById("CT_MAILMES2").parentNode.parentNode.style.display = 'none';
            }
            if (document.getElementById("CCO_EXTENCION")) {
                document.getElementById("CCO_EXTENCION").parentNode.parentNode.style.display = 'none';
            }
            if (document.getElementById("CCO_ALTERNO")) {
                document.getElementById("CCO_ALTERNO").parentNode.parentNode.style.display = 'none';
            }
            if (document.getElementById("CCO_TITULO")) {
                document.getElementById("CCO_TITULO").parentNode.parentNode.style.display = 'none';
            }
            if (document.getElementById("CCO_FECHA_NAC")) {
                document.getElementById("CCO_FECHA_NAC").parentNode.parentNode.style.display = 'none';
            }
            if (document.getElementById("CCO_AREA")) {
                document.getElementById("CCO_AREA").parentNode.parentNode.style.display = 'none';
            }
            if (document.getElementById("BTN_NEW_CNT")) {
                document.getElementById("BTN_NEW_CNT").parentNode.parentNode.style.display = '';
            }
            if (document.getElementById("BTN_EDIT_CNT")) {
                document.getElementById("BTN_EDIT_CNT").parentNode.parentNode.style.display = '';
            }
            if (document.getElementById("BTN_DEL_CNT")) {
                document.getElementById("BTN_DEL_CNT").parentNode.parentNode.style.display = '';
            }
            break;
        case 2:
            //Mostrar botnes y Fields para agregar nuevo Contacto
            if (document.getElementById("BTN_NEW_CNT")) {
                document.getElementById("BTN_NEW_CNT").parentNode.parentNode.style.display = 'none';
            }
            if (document.getElementById("BTN_EDIT_CNT")) {
                document.getElementById("BTN_EDIT_CNT").parentNode.parentNode.style.display = 'none';
            }
            if (document.getElementById("BTN_DEL_CNT")) {
                document.getElementById("BTN_DEL_CNT").parentNode.parentNode.style.display = 'none';
            }
            if (document.getElementById("CCO_ID")) {
                document.getElementById("CCO_ID").parentNode.parentNode.style.display = '';
            }
            if (document.getElementById("BTN_ADD_CNT")) {
                document.getElementById("BTN_ADD_CNT").parentNode.parentNode.style.display = '';
            }
            if (document.getElementById("BTN_CANCEL_CNT")) {
                document.getElementById("BTN_CANCEL_CNT").parentNode.parentNode.style.display = '';
            }
            if (document.getElementById("CCO_NOMBRE")) {
                document.getElementById("CCO_NOMBRE").parentNode.parentNode.style.display = '';
            }
            if (document.getElementById("CCO_APPATERNO")) {
                document.getElementById("CCO_APPATERNO").parentNode.parentNode.style.display = '';
            }
            if (document.getElementById("CCO_APMATERNO")) {
                document.getElementById("CCO_APMATERNO").parentNode.parentNode.style.display = '';
            }
            if (document.getElementById("CCO_CORREO")) {
                document.getElementById("CCO_CORREO").parentNode.parentNode.style.display = '';
            }
            if (document.getElementById("CCO_CORREO2")) {
                document.getElementById("CCO_CORREO2").parentNode.parentNode.style.display = '';
            }
            if (document.getElementById("CCO_TELEFONO")) {
                document.getElementById("CCO_TELEFONO").parentNode.parentNode.style.display = '';
            }
            if (document.getElementById("CT_MAILMES2")) {
                document.getElementById("CT_MAILMES2").parentNode.parentNode.style.display = '';
            }
            if (document.getElementById("CCO_EXTENCION")) {
                document.getElementById("CCO_EXTENCION").parentNode.parentNode.style.display = '';
            }
            if (document.getElementById("CCO_ALTERNO")) {
                document.getElementById("CCO_ALTERNO").parentNode.parentNode.style.display = '';
            }
            if (document.getElementById("CCO_TITULO")) {
                document.getElementById("CCO_TITULO").parentNode.parentNode.style.display = '';
            }
            if (document.getElementById("CCO_FECHA_NAC")) {
                document.getElementById("CCO_FECHA_NAC").parentNode.parentNode.style.display = '';
            }
            if (document.getElementById("CCO_AREA")) {
                document.getElementById("CCO_AREA").parentNode.parentNode.style.display = '';
            }
            break;
        default:
            alert("intOpc No Valida.");
    }
}

function cancelTransContactoCT() {
    showHiddeFieldContacto(1);
    cleanFieldContactoCT();
}

function showContactoCT() {
    var itemIdCob = 0;
    var idCliente = document.getElementById("CT_ID").value;
    var strPost = "idCliente=" + idCliente;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "ERP_ClienteFacturacion.jsp?ID=6",
        success: function (datos) {
            jQuery("#GRIDCONTACTOS_CT").clearGridData();
            var objPdom = datos.getElementsByTagName("ContactoCT")[0];
            var lstProms = objPdom.getElementsByTagName("datos");
            for (var i = 0; i < lstProms.length; i++) {
                var obj = lstProms[i];
                var dataRow = {
                    CCO_CONTADOR: i + 1,
                    CCO_ID: obj.getAttribute("CCO_ID"),
                    CCO_NOMBRE: obj.getAttribute("CCO_NOMBRE"),
                    CCO_APPATERNO: obj.getAttribute("CCO_APPATERNO"),
                    CCO_APMATERNO: obj.getAttribute("CCO_APMATERNO"),
                    CCO_CORREO: obj.getAttribute("CCO_CORREO"),
                    CCO_CORREO2: obj.getAttribute("CCO_CORREO2"),
                    CCO_MAILMES: obj.getAttribute("CCO_MAILMES"),
                    CCO_TELEFONO: obj.getAttribute("CCO_TELEFONO"),
                    CCO_EXTENCION: obj.getAttribute("CCO_EXTENCION"),
                    CCO_TITULO: obj.getAttribute("CCO_TITULO"),
                    CCO_ALTERNO: obj.getAttribute("CCO_ALTERNO"),
                    CCO_AREA: obj.getAttribute("CCO_AREA")
                };
                //Anexamos el registro al GRID
                itemIdCob++;
                jQuery("#GRIDCONTACTOS_CT").addRowData(itemIdCob, dataRow, "last");
            }
            $("#dialogWait").dialog("close");
        }, error: function (data) {
            console.log(data);
            $("#dialogWait").dialog("close");
            alert(data);
        }
    });
}

function btnSaveContactoCT() {
    var strPost = "CCO_TITULO=" + document.getElementById("CCO_TITULO").value;
    strPost += "&CCO_NOMBRE=" + document.getElementById("CCO_NOMBRE").value;
    strPost += "&CCO_APPATERNO=" + document.getElementById("CCO_APPATERNO").value;
    strPost += "&CCO_APMATERNO=" + document.getElementById("CCO_APMATERNO").value;
    strPost += "&CCO_CORREO=" + document.getElementById("CCO_CORREO").value;
    strPost += "&CCO_CORREO2=" + document.getElementById("CCO_CORREO2").value;
    strPost += "&CCO_TELEFONO=" + document.getElementById("CCO_TELEFONO").value;
    strPost += "&CCO_EXTENCION=" + document.getElementById("CCO_EXTENCION").value;
    strPost += "&CCO_TITULO=" + document.getElementById("CCO_TITULO").value;
    strPost += "&CCO_ALTERNO=" + document.getElementById("CCO_ALTERNO").value;
    strPost += "&CCO_AREA=" + document.getElementById("CCO_AREA").value;
    strPost += "&CCO_FECHA_NAC=" + document.getElementById("CCO_FECHA_NAC").value;
    strPost += "&idCliente=" + document.getElementById("CT_ID").value;
    if (document.getElementById("CCO_ID").value == "") {
        strPost += "&CCO_ID=0";
        strPost += "&Trans=1";
    } else {
        strPost += "&CCO_ID=" + document.getElementById("CCO_ID").value;
        strPost += "&Trans=2";
    }
    if (document.getElementById("CT_MAILMES2").checked) {
        strPost += "&CCO_MAILMES=" + 1;
    } else {
        strPost += "&CCO_MAILMES=" + 0;
    }

    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: encodeURI(strPost),
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "ERP_ClienteFacturacion.jsp?ID=7",
        success: function (datos) {
            $("#dialogWait").dialog("close");
            if (trim(datos) == "OK") {
                showContactoCT();
                cleanFieldContactoCT();
                showHiddeFieldContacto(1);
            } else {
                alert(datos);
            }
        }, error: function (data) {
            $("#dialogWait").dialog("close");
            alert(data);
        }
    });
}

function btnEditContactoCT() {
    var grid = jQuery("#GRIDCONTACTOS_CT");
    if (grid.getGridParam("selrow")) {
        var id = grid.getGridParam("selrow");
        var lstVal = grid.getRowData(id);
        showHiddeFieldContacto(2);
        document.getElementById("CCO_ID").value = lstVal.CCO_ID;
        document.getElementById("CCO_NOMBRE").value = lstVal.CCO_NOMBRE;
        document.getElementById("CCO_APPATERNO").value = lstVal.CCO_APPATERNO;
        document.getElementById("CCO_APMATERNO").value = lstVal.CCO_APMATERNO;
        document.getElementById("CCO_CORREO").value = lstVal.CCO_CORREO;
        document.getElementById("CCO_CORREO2").value = lstVal.CCO_CORREO2;
        document.getElementById("CCO_TELEFONO").value = lstVal.CCO_TELEFONO;
        if (lstVal.CT_MAILMES == "SI") {
            document.getElementById("CT_MAILMES2").checked = true;
        } else {
            document.getElementById("CT_MAILMES2").checked = false;
        }
        document.getElementById("CCO_EXTENCION").value = lstVal.CCO_EXTENCION;
        document.getElementById("CCO_ALTERNO").value = lstVal.CCO_ALTERNO;
        document.getElementById("CCO_TITULO").value = lstVal.CCO_TITULO;
        document.getElementById("CCO_FECHA_NAC").value = lstVal.CCO_FECHA_NAC;
        document.getElementById("CCO_AREA").value = lstVal.CCO_AREA;
    } else {
        alert("Selecciona una fila en el tab Contactos Cliente");
    }
}

function btnDeleteContactoCT() {
    var grid = jQuery("#GRIDCONTACTOS_CT");
    if (grid.getGridParam("selrow")) {
        var id = grid.getGridParam("selrow");
        var lstVal = grid.getRowData(id);
        document.getElementById("SioNO_inside").innerHTML = "";
        $("#SioNO").dialog("open");
        $("#SioNO").dialog('option', 'title', "Â¿Desea Eliminar la partida?");
        _resetSioNoCte("btnSI", "btnNO", "SioNO_inside");
        document.getElementById("btnSI").onclick = function () {
            var strPost = "Id_Contacto=" + lstVal.CCO_ID;
            $("#SioNO").dialog("close");
            $("#dialogWait").dialog("open");
            $.ajax({
                type: "POST",
                data: encodeURI(strPost),
                scriptCharset: "utf-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "html",
                url: "ERP_ClienteFacturacion.jsp?ID=8",
                success: function (datos) {
                    $("#dialogWait").dialog("close");
                    if (trim(datos) == "OK") {
                        showContactoCT();
                    } else {
                        alert(datos);
                    }
                }, error: function (data) {
                    $("#dialogWait").dialog("close");
                    alert(data);
                }
            });
        };
        document.getElementById("btnNO").onclick = function () {
            $("#SioNO").dialog("close")
        };
    } else {
        alert("Selecciona una fila en el tab Contactos Cliente");
    }
}

function cleanFieldContactoCT() {
    document.getElementById("CCO_ID").value = "";
    document.getElementById("CCO_NOMBRE").value = "";
    document.getElementById("CCO_APPATERNO").value = "";
    document.getElementById("CCO_APMATERNO").value = "";
    document.getElementById("CCO_CORREO").value = "";
    document.getElementById("CCO_CORREO2").value = "";
    document.getElementById("CCO_TELEFONO").value = "";
    document.getElementById("CT_MAILMES2").checked = false;
    document.getElementById("CCO_EXTENCION").value = "";
    document.getElementById("CCO_ALTERNO").value = "";
    document.getElementById("CCO_TITULO").value = "";
    document.getElementById("CCO_FECHA_NAC").value = "";
    document.getElementById("CCO_AREA").value = "";
}
function showIFE() {

    var miArchivo = document.getElementById("CT_CRED_ELECTOR").value;
    if (miArchivo == "") {
        alert("Este cliente no ha subido este document");
    } else {
        $("#dialogView").dialog("open");
        $("#dialogView").dialog({
            position: {my: "center", at: "center", of: window}
        });
        var path = "iCommerce/modules/mod_moneda_blanca/documentos/";
        var contenido = "";
        var tipo = miArchivo.substring((miArchivo.length - 3), (miArchivo.length));
        if (tipo.toUpperCase() == "PDF") {
            contenido = "<a href='" + path + miArchivo + "' target='_blank'>Click aqui Para abrir el PDF</a>";
        } else {
            contenido = "<img width='400' height='400' src='" + path + miArchivo + "'>";
        }
        console.log(contenido);
        $("#dialogView").html(contenido);
    }

}
function showComprobante() {
    var miArchivo = document.getElementById("CT_COMPROBANTE_DOMI").value;
    if (miArchivo == "") {
        alert("Este cliente no ha subido este documento");
    } else {
        $("#dialogView").dialog("open");
        $("#dialogView").dialog({
            position: {my: "center", at: "center", of: window}
        });
        var path = "iCommerce/modules/mod_moneda_blanca/documentos/";
        var contenido = "";
        var tipo = miArchivo.substring((miArchivo.length - 3), (miArchivo.length));
        if (tipo.toUpperCase() == "PDF") {
            contenido = "<a href='" + path + miArchivo + "' target='_blank'>Click aqui Para abrir el PDF</a>";
        } else {
            contenido = "<img width='400' height='400' src='" + path + miArchivo + "'>";
        }
        console.log(contenido);
        $("#dialogView").html(contenido);
    }


}

//**
function showComprobanteNisa() { // muestra el comprobante del select
    //iCommerce/docs/error - copia (2)
    var miArchivo = document.getElementById("DOC_FILE").value;
    //var miArchivo = "prod2.png";
    if (miArchivo == "") {
        alert("Este cliente no ha subido este documento");
    } else {
        $("#dialogView").dialog("open");
        $("#dialogView").dialog({
            position: {my: "center", at: "center", of: window}
        });
        //var path = "iCommerce/modules/mod_nisa/img/";
        var path = "iCommerce/docs/";
        var contenido = "";
        var tipo = miArchivo.substring((miArchivo.length - 3), (miArchivo.length));
        if (tipo.toUpperCase() == "PDF") {
            contenido = "<a href='" + path + miArchivo + "' target='_blank'>Click aqui Para abrir el PDF</a>";
        } else {
            contenido = "<img width='600' height='600' src='" + path + miArchivo + "'>";
            contenido = contenido + "<button value='cerrar' onclick='cierraPop()'>cerrar</button>"
        }
        //console.log(contenido);
        $("#dialogView").html(contenido);

    }
}
//***
function ObtenDocumentos() { // trae los comprobantes disponibles del cliente a un select
    var bolExist = false;
    //Peticion por ajax para mostrar info
    $.ajax({
        type: "POST",
        data: "ct_id=" + document.getElementById("CT_ID").value, //id de cliente seleccionado
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "ERP_Varios.jsp?id=7",
        success: function (datos) {
            var lstDirecciones = datos.getElementsByTagName("Direcciones")[0];
            var lstDireccion = lstDirecciones.getElementsByTagName("direcciones");
            var objDir = document.getElementById("DOC_FILE");
            select_clear(objDir);
            for (j = 0; j < lstDireccion.length; j++) {
                bolExist = true;
                var objP = lstDireccion[j];
                if (j == 0) {
                    //select_add(objDir, "<-Seleccione->", 0);
                }
                //document.getElementById("DOC_FILE").value = objP.getAttribute('doc_file');
                select_add(objDir, objP.getAttribute('doc_file'), objP.getAttribute('doc_file'));
            }
            if (bolExist == false) {
                select_add(objDir, '<--No hay documentos por mostrar-->', 0);
            }

        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}
//**
function cierraPop() {
    $('#dialogView').dialog('close');
}
//**
function showContrato() {
    var miArchivo = document.getElementById("CT_CONTRATO").value;
    if (miArchivo == "") {
        alert("Este cliente no ha subido este documento");
    } else {
        $("#dialogView").dialog("open");
        $("#dialogView").dialog({
            position: {my: "center", at: "center", of: window}
        });

        var path = "iCommerce/modules/mod_moneda_blanca/documentos/";
        var contenido = "";
        var tipo = miArchivo.substring((miArchivo.length - 3), (miArchivo.length));
        if (tipo.toUpperCase() == "PDF") {
            contenido = "<a href='" + path + miArchivo + "' target='_blank'>Click aqui Para abrir el PDF</a>";
        } else {
            contenido = "<img width='400' height='400' src='" + path + miArchivo + "'>";
        }
        console.log(contenido);
        $("#dialogView").html(contenido);
    }

}

function editDirCteFinal() {
    if (validationSelectedRowGridCte("grd_DireccionesDFA", "Selecciona un cliente en la tabla.", false)) {
        status_Fields_CTEFinal("mostrar");
        getDatosCteFinal();
        document.getElementById("BTN_DFA_NEW").parentNode.parentNode.style.display = "none";
        document.getElementById("BTN_DFA_DEL").parentNode.parentNode.style.display = "none";
        document.getElementById("BTN_DFA_EDIT").parentNode.parentNode.style.display = "none";
        document.getElementById("BTN_DFA_SAVE").parentNode.parentNode.style.display = "";
        document.getElementById("BTN_DFA_CANCEL").parentNode.parentNode.style.display = "";
    }
}//Fin editDirCteFinal


function getDatosCteFinal() {
    var strNomGrid = "grd_DireccionesDFA";
    document.getElementById("DFA_ID").value = getDataColumnCte(strNomGrid, "DFA_ID");
    document.getElementById("DFA_RAZONSOCIAL").value = getDataColumnCte(strNomGrid, "DFA_RAZONSOCIAL");
    document.getElementById("DFA_RFC").value = getDataColumnCte(strNomGrid, "DFA_RFC");
    document.getElementById("DFA_CALLE").value = getDataColumnCte(strNomGrid, "DFA_CALLE");
    document.getElementById("DFA_COLONIA").value = getDataColumnCte(strNomGrid, "DFA_COLONIA");
    document.getElementById("DFA_LOCALIDAD").value = getDataColumnCte(strNomGrid, "DFA_LOCALIDAD");
    document.getElementById("DFA_MUNICIPIO").value = getDataColumnCte(strNomGrid, "DFA_MUNICIPIO");
    document.getElementById("DFA_ESTADO").value = getDataColumnCte(strNomGrid, "DFA_ESTADO");
    document.getElementById("DFA_CP").value = getDataColumnCte(strNomGrid, "DFA_CP");
    document.getElementById("DFA_NUMERO").value = getDataColumnCte(strNomGrid, "DFA_NUMERO");
    document.getElementById("DFA_NUMINT").value = getDataColumnCte(strNomGrid, "DFA_NUMINT");
    document.getElementById("DFA_TELEFONO").value = getDataColumnCte(strNomGrid, "DFA_TELEFONO");
    document.getElementById("DFA_PAIS").value = getDataColumnCte(strNomGrid, "DFA_PAIS");
    document.getElementById("DFA_EMAIL").value = getDataColumnCte(strNomGrid, "DFA_EMAIL");
    document.getElementById("DFA_VISIBLE1").value = getDataColumnCte(strNomGrid, "DFA_VISIBLE1");
    document.getElementById("DFA_EMAIL2").value = getDataColumnCte(strNomGrid, "DFA_EMAIL2");
    document.getElementById("DFA_CONTACTO1").value = getDataColumnCte(strNomGrid, "DFA_CONTACTO1");
    document.getElementById("DFA_CONTACTO2").value = getDataColumnCte(strNomGrid, "DFA_CONTACTO2");
}//Fin getDatosCteFinal

function setDatosCteFinal(objParamsAjax) {
    if (!objParamsAjax["bolRespuesta"]) {/*Envio*/
        var strPost = "&DFA_ID=" + objParamsAjax["DFA_ID"];
        strPost += "&DFA_RAZONSOCIAL=" + objParamsAjax["DFA_RAZONSOCIAL"];
        strPost += "&DFA_RFC=" + objParamsAjax["DFA_RFC"];
        strPost += "&DFA_CALLE=" + objParamsAjax["DFA_CALLE"];
        strPost += "&DFA_COLONIA=" + objParamsAjax["DFA_COLONIA"];
        strPost += "&DFA_LOCALIDAD=" + objParamsAjax["DFA_LOCALIDAD"];
        strPost += "&DFA_MUNICIPIO=" + objParamsAjax["DFA_MUNICIPIO"];
        strPost += "&DFA_ESTADO=" + objParamsAjax["DFA_ESTADO"];
        strPost += "&DFA_CP=" + objParamsAjax["DFA_CP"];
        strPost += "&DFA_NUMERO=" + objParamsAjax["DFA_NUMERO"];
        strPost += "&DFA_NUMINT=" + objParamsAjax["DFA_NUMINT"];
        strPost += "&DFA_TELEFONO=" + objParamsAjax["DFA_TELEFONO"];
        strPost += "&DFA_PAIS=" + objParamsAjax["DFA_PAIS"];
        strPost += "&DFA_EMAIL=" + objParamsAjax["DFA_EMAIL"];
        strPost += "&DFA_VISIBLE=" + objParamsAjax["DFA_VISIBLE"];
        strPost += "&DFA_EMAIL2=" + objParamsAjax["DFA_EMAIL2"];
        strPost += "&DFA_CONTACTO1=" + objParamsAjax["DFA_CONTACTO1"];
        strPost += "&DFA_CONTACTO2=" + objParamsAjax["DFA_CONTACTO2"];
        strPost += "&CT_ID=" + objParamsAjax["CT_ID"];
        ajaxRequestCte(strPost, "ERP_ClienteFacturacion", 14, "ID", "", "", "", setDatosCteFinal, "Error al editar el Subcliente.", objParamsAjax);
    } else {/*Respuesta*/
        var objMaster = objParamsAjax["objResponse"].getElementsByTagName(objParamsAjax["strPrefixMaster"])[0];
        var objDetail = objMaster.getElementsByTagName(objParamsAjax["strPrefixDetail"]);
        for (var i = 0; i < objDetail.length; i++) {
            var obj = objDetail[i];
            if (obj.getAttribute('respuesta') != "OK") {
                alert(obj.getAttribute('respuesta'));
            } else {
                alert("Edicion Exitosa.");
                LoadDireccionesCteFinal(document.getElementById("CT_ID").value);
                status_Fields_CTEFinal("ocultar");
                cancelDirCteFinal();
            }
        }
    }
}//Fin setDatosCteFinal


function validationSelectedRowGridCte(strNameGrid, strMessage, bolIsCheckBoxGrid) {
    $("#dialogWait").dialog("open");
    var grid = jQuery("#" + strNameGrid);
    var strMsj = "";
    var selRowIds;
    if (bolIsCheckBoxGrid) {
        selRowIds = grid.jqGrid('getGridParam', 'selarrrow');
        strMsj = strMessage;
    } else {
        selRowIds = grid.getGridParam("selrow");
        strMsj = strMessage;
    }
    if (selRowIds != null) {
        if (selRowIds.length != 0 && selRowIds.length != "") {
            $("#dialogWait").dialog("close");
            return true;
        } else {
            alert(strMsj);
            $("#dialogWait").dialog("close");
            return false;
        }
    } else {
        alert(strMsj);
        $("#dialogWait").dialog("close");
        return false;
    }
}//Fin validationSelectedRowGridCte


function getDataColumnCte(strNameGrid, strNameColumn) {
    var grid = jQuery("#" + strNameGrid);
    var id = grid.getGridParam("selrow");
    if (grid.getGridParam("selrow") != null) {
        return grid.jqGrid('getCell', id, strNameColumn);
    } else {
        alert("No se pudo obtener la columna : " + strNameColumn);
        return false;
    }
}//Fin getDataColumnCte

function ajaxRequestCte(strPost, strJSP, strIdJSP, strNameOpc, strPrefixMaster, strPrefixDetail, strDataType, strNameFunction, strMessageError, objParamsAjax) {
    $("#dialogWait").dialog("open");
    var strNameOpcInterno = (strNameOpc == "" ? "id" : strNameOpc);
    var strPrefixMasterInterno = (strPrefixMaster == "" ? "Master" : strPrefixMaster);
    var strPrefixDetailInterno = (strPrefixDetail == "" ? "Detail" : strPrefixDetail);
    var strDataTypeInterno = (strDataType == "" ? "xml" : strDataType);

    strPost += "&strPrefijoMaster=" + strPrefixMasterInterno;
    strPost += "&strPrefijoDeta=" + strPrefixDetailInterno;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: strDataTypeInterno,
        url: strJSP + ".jsp?" + strNameOpcInterno + "=" + strIdJSP,
        success: function (objResponse) {
            var paramsResponse = {
                bolRespuesta: true,
                objResponse: objResponse,
                strPrefixMaster: strPrefixMasterInterno,
                strPrefixDetail: strPrefixDetailInterno
            };
            jQuery.extend(paramsResponse, objParamsAjax);
            strNameFunction(paramsResponse);
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(strMessageError + " \nError :" + objeto + " " + quepaso + " " + otroobj + " \nError : La peticion al archivo " + strJSP + " id= " + strIdJSP + " fallo.");
            $("#dialogWait").dialog("close");
        }
    });
}//Fin ajaxRequestCte


function getTextSelect(strNameSelect) {
    var idSelect = d.getElementById(strNameSelect).selectedIndex;
    var textSelect = d.getElementById(strNameSelect).options[idSelect].text;
    return textSelect;
}//Fin getTextSelect

// modena blanca
function loadSubsedes() {
    var strPost = "SM_ID=" + document.getElementById('SM_ID').value;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "json",
        url: "ERP_ClienteFacturacion.jsp?ID=15",
        success: function (json) {
            var subSedes = json.subsedes;
            var strOpciones = "<option value='0'>--Seleccione--</option>";
            for (var scont = 0; scont < subSedes.length; scont++) {
                if (subSedes[scont].id != "" && subSedes[scont].nombre != "") {
                    if (document.getElementById("SC_ID").value == subSedes[scont].id) {
                        strOpciones += "<option selected value='" + subSedes[scont].id + "'>" + subSedes[scont].nombre + "</option>";
                    } else {
                        strOpciones += "<option value='" + subSedes[scont].id + "'>" + subSedes[scont].nombre + "</option>";
                    }

                }
            }
            if (document.getElementById('SM_ID').value == "0") {
                document.getElementById("select_SC_ID").innerHTML = "<option value='0'>--Seleccione--</option>";
            } else {

                document.getElementById("select_SC_ID").innerHTML = strOpciones;

            }


        },
        error: function (objeto, quepaso, otroobj) {
            alert(" cte:" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}

// modena blanca
function fillHiddenSubsede() {
    document.getElementById("SC_ID").value = document.getElementById("select_SC_ID").value;
}
function ShowClabe() {
    if (document.getElementById("CT_CTA_CLABE1").value.length != 18) {
        alert("La clabe interbancaria debe contener solo 18 numeros");
        document.getElementById("CT_CTA_CLABE1").value = "";
        document.getElementById("CT_CTA_CLABE1").focus();
        return false;
    }
}

function _resetSioNoCte(strNameBtnSi, strNameBtnNo, strNameInside) {
    $(strNameBtnSi).unbind("");
    $(strNameBtnNo).unbind("");
    var div = document.getElementById(strNameInside);
    div.innerHTML = "";
}
/***Descarga archivo de la oficina virtual*/
function _descargaFile(idOp) {
    var idCte = document.getElementById("CT_ID").value;
    //Peticion por ajax para el nombre
    $.ajax({
        type: "POST",
        data: "CT_ID=" + idCte + "&Opcion=" + idOp,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "ERP_ClienteFacturacion.jsp?ID=16",
        success: function (datos) {
            var nombrefile = trim(datos);
            //Abrimos el archivo
            var strNomDoc = "iCommerce/docs/" + nombrefile;//Hay que revisar el path
            Abrir_Link(strNomDoc, "_new", 200, 200, 0, 0);//Con esta lÃ­nea abrimos el archivo
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":visualizar archivos:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");

        }
    });

}

function abrirImagen() {
    var idCte = document.getElementById("CT_ID").value;
}
/**Se encarga de subir archivos del cliente  al servidor*/
function guardarArchivo(strNomFile) {
    ValidaClean(strNomFile);
    if (document.getElementById(strNomFile).value == "") {
        ValidaShow(strNomFile, lstMsg[13]);
        return false;
    }
    var strExtencion = Right(document.getElementById(strNomFile).value.toUpperCase(), 4);
    if (strExtencion == ".PNG" || strExtencion == ".JPG" || strExtencion == ".PDF" || strExtencion == ".DOC" || strExtencion == "DOCX"
            || strExtencion == ".PPT" || strExtencion == "PPTX") {
        //Subimos los datos al servidor
        ajaxArchivoUpload(strNomFile, document.getElementById("CT_ID").value);
        return true;
    } else {
        ValidaShow(strNomFile, "Formato no valido.");
        return false;
    }
    return true;
}

/*Sube la imagen del producto al servidor*/
function ajaxArchivoUpload(strNomFile, intCT_ID) {
    $("#dialogWait").dialog('open');
    //Subimos el archivo con ajaxUpload
    $.ajaxFileUpload({
        url: 'ERP_UpFileCliente.jsp?CT_ID=' + intCT_ID,
        secureuri: false,
        fileElementId: strNomFile,
        dataType: 'json',
        success: function (data, status) {
            if (typeof (data.error) != 'undefined') {
                if (data.error != '') {
                    alert(data.error);
                }
                if (data.msg != '') {
                    document.getElementById(strNomFile).value = data.msg;
                }
            }
            ;
        },
        error: function (data, status, e) {
//            alert(e);
            $("#dialogWait").dialog('close');
        }
    });
    return false;
}

function verArchivoFicha(intArchivo) {
    var strNomDoc = document.getElementById(intArchivo).value;
    AbrirArchivoMB(strNomDoc, "_new", 700, 700, 0, 0);
}


function AbrirArchivoMB(e, b, d, a, f, c) {
    popupWin = window.open(e, b, "menubar=no,toolbar=no,location=no,directories=no,status=no,scrollbars,resizable,dependent=no,width=" + d + ",height=" + a + ",left=" + f + ",top=" + c)
}


function certificacion(cambio) {
    document.getElementById("DUMANI_LISTA12").checked = true;
    document.getElementById("DUMANI_LISTA22").checked = true;
    document.getElementById("DUMANI_LISTA32").checked = true;
    document.getElementById("DUMANI_LISTA42").checked = true;

    if (cambio == 2) {
        document.getElementById("DUMANI_LISTA11").checked = true;
        document.getElementById("CT_LPRECIOS").value = 2;
    }
    if (cambio == 3) {
        document.getElementById("DUMANI_LISTA21").checked = true;
        document.getElementById("CT_LPRECIOS").value = 3;
    }
    if (cambio == 4) {
        document.getElementById("DUMANI_LISTA31").checked = true;
        document.getElementById("CT_LPRECIOS").value = 4;
    }
    if (cambio == 5) {
        document.getElementById("DUMANI_LISTA41").checked = true;
        document.getElementById("CT_LPRECIOS").value = 5;
    }

}


/**Abre el cuadro de dialogo para la residencia fiscal*/
function OpnDiagPais_SAT() {
    bolUsaOpnCte = true;
    OpnOpt('PA_SAT', 'grid', 'dialogCte', false, false);
}

function agregaDato(id) {
    var grid = jQuery("#PA_SAT");
    var lstVal = grid.getRowData(id);
    document.getElementById("CT_RESIDENCIA_FISCAL").value = lstVal.PA_ABREV;
    $("#dialogCte").dialog("close");
}