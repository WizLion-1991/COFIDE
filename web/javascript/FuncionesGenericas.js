/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function FuncionesGenericas() {
}

/****************************************************Functiones para Grid*/

/*Convierte una columna del grid en editable*/
function editColumnGridFG(strNameGrid, strNameColumn, bolEdit) {
    resetSelGridFG(strNameGrid);
    var grid = jQuery("#" + strNameGrid);
    var arr = grid.getDataIDs();
    for (var i = 0; i < arr.length; i++) {
        var id = arr[i];
        grid.saveRow(id);
    }
    jQuery("#" + strNameGrid).jqGrid('setColProp', strNameColumn, {editable: bolEdit});
}

/*Verifica que el Grid tenga seleccionado almenos una fila o el minimo de filas establecidas
 * Se puede mandar a llamar la funcion sin los ultimos 2 parametros*/
function selectedRowGridFG(strNameGrid, strMessage, bolIsCheckBoxGrid, bolstrMinimumRowsSelected, strMinimumRowsSelected) {
    if (bolstrMinimumRowsSelected == null) {
        bolstrMinimumRowsSelected = false;
    }
    var grid = jQuery("#" + strNameGrid);
    var strMsj = strMessage;
    var selRowIds;
    if (bolIsCheckBoxGrid) {
        selRowIds = grid.jqGrid('getGridParam', 'selarrrow');
    } else {
        selRowIds = grid.getGridParam("selrow");
    }
    if (selRowIds != null) {
        if (selRowIds.length != 0 && selRowIds.length != "") {
            if (bolstrMinimumRowsSelected) {
                if (strMinimumRowsSelected > selRowIds.length) {
                    strMsj = "Seleccione minimo " + strMinimumRowsSelected + " filas en la tabla.";
                    alert(strMsj);
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else {
            alert(strMsj);
            return false;
        }
    } else {
        alert(strMsj);
        return false;
    }
}

/*Verifica que el grid contenga datos*/
function gridContainsDataFG(strNameGrid, strMessage) {
    var grid = jQuery("#" + strNameGrid);
    var arr = grid.getDataIDs();
    if (arr.length == 0) {
        if (strMessage != "") {
            alert(strMessage);
        }
        return false;
    }
    return true;
}

/*Oculta y Muestra Columnas de un Grid*/
function hideShowColumnGridFG(strNameGrid, strNameColumn, strAccion) {
    if (strAccion == 'hide') {
        $("#" + strNameGrid).hideCol(strNameColumn);
    }
    if (strAccion == 'show') {
        $("#" + strNameGrid).showCol(strNameColumn);
    }
}

/*Alinea una columna left,right,center*/
function alignColumnFG(strNameGrid, strNameColumn, strAlign) {
    var grid = jQuery("#" + strNameGrid);
    var arr = grid.getDataIDs();
    for (var i = 0; i < arr.length; i++) {
        var id = arr[i];
        var lstRow = grid.getRowData(id);
        grid.jqGrid('setCell', id, strNameColumn, "", {"text-align": strAlign});
        grid.setRowData(id, lstRow);
    }
    grid.trigger("reloadGrid");
}

/*Cambia el titulo de una columna*/
function setTitleColumnFG(strNameGrid, strNameColumn, strTittle) {
    jQuery("#" + strNameGrid).setLabel(strNameColumn, strTittle);
}

/*Reseta la seleccion de un Grid*/
function resetSelGridFG(strNameGrid) {
    var grid = jQuery("#" + strNameGrid);
    grid.jqGrid('resetSelection');
    grid.trigger("reloadGrid");
}

/*Permite que la informacion de un grid se pueda ordenar con la informacion local
 * Ponerlo en el ONcomplete del grid*/
function sortGridFG(strNameGrid) {
    $("#" + strNameGrid).setGridParam({datatype: 'local'});
}

/*Pinta una fila de un color dependiendo del id 
 * los colores debe de ser con codigo Hexadecimal*/
function setColorBackGroundRowFG(strNameGrid, intIdRow, strColorFont, strColorBackground) {
    $("#" + strNameGrid).jqGrid('setRowData', intIdRow, false, {color: '#' + strColorFont, weightfont: 'bold', background: '#' + strColorBackground});
}


/*Obtiene el valor de una fila y columna seleccionada
 *Se puede llamar sin el ultimo parametro */
function getDataColumnFG(strNameGrid, strNameColumn, bolIsMultiple) {
    var grid = jQuery("#" + strNameGrid);
    var id = "";
    if (bolIsMultiple == null) {
        bolIsMultiple = false;
    }
    if (typeof bolIsMultiple == "undefined") {
        bolIsMultiple = false;
    }
    if (bolIsMultiple) {
        id = grid.jqGrid('getGridParam', 'selarrrow');
    } else {
        id = grid.getGridParam("selrow");
    }
    if (id != null && id != "" && id != 0) {
        return grid.jqGrid('getCell', id, strNameColumn);
    } else {
        alert("No se pudo obtener la columna : " + strNameColumn);
        return false;
    }
}

/*Obtiene el valor de una fila y columna dependiendo del Id*/
function getDataColumnIdFG(strNameGrid, strNameColumn, id) {
    var grid = jQuery("#" + strNameGrid);
    if (id != null && id != 0) {
        return grid.jqGrid('getCell', id, strNameColumn);
    } else {
        alert("No se pudo obtener la columna : " + strNameColumn);
        return false;
    }
}

/*Asigna un valor a una fila seleccionada y columna */
function setDataColFG(strNameGrid, strNameColumn, data) {
    var grid = jQuery("#" + strNameGrid);
    if (grid.getGridParam("selrow") != null) {
        var id = grid.getGridParam("selrow");
        grid.jqGrid('setCell', id, strNameColumn, data);
        grid.trigger("reloadGrid");
    } else {
        alert("No se pudo obtener la columna : " + strNameColumn);
        return false;
    }
}

/*Asigna un valor a una fila y columna dependiendo del id*/
function setDataColIdFG(strNameGrid, strNameColumn, data, id) {
    var grid = jQuery("#" + strNameGrid);
    var lstRow = grid.getRowData(id);
    grid.jqGrid('setCell', lstRow, strNameColumn, data);
//Si est getDataIDs
//grid.setCell(id, "DET_TIPO", "", "advertencia", "", false);    
    grid.setRowData(id, lstRow);
}




/****************************************************Funciones para Campos*/

/*Oculta o muestra un campo*/
function hideShowFieldFG(strNameField, strAction, bolIsRadioButton, bolIsPanelRadioButton, bolUseParentNode) {
    var strHideShow = "";
    var strField = "";
    if (strAction == "hide") {
        strHideShow = "none";
    }
    if (bolIsRadioButton) {
        for (var i = 1; i <= 2; i++) {
            strField = document.getElementById(strNameField + i);
            if (bolUseParentNode) {
                strField.parentNode.parentNode.style.display = strHideShow;
            } else {
                strField.style.display = strHideShow;
            }
        }
    } else {
        if (bolIsPanelRadioButton) {
            for (var i = 0; i <= 2; i++) {
                strField = document.getElementById(strNameField + i);
                if (bolUseParentNode) {
                    strField.parentNode.parentNode.style.display = strHideShow;
                } else {
                    strField.style.display = strHideShow;
                }
            }
        } else {
            strField = document.getElementById(strNameField);
            if (bolUseParentNode) {
                strField.parentNode.parentNode.style.display = strHideShow;
            } else {
                strField.style.display = strHideShow;
            }
        }
    }
}

/*Cambia un campo a solo lectura*/
function blockFieldFG(strNameField, bolBlock) {
    var field = document.getElementById(strNameField);
    field.disabled = bolBlock;
    if (bolBlock) {
        field.style.backgroundColor = "#BDBDBD";
    } else {
        field.style.backgroundColor = "";
    }
}

/*Obtiene el Texto de un Select*/
function getTextSelectFG(strNameSelect) {
    var idSelect = d.getElementById(strNameSelect).selectedIndex;
    var textSelect = d.getElementById(strNameSelect).options[idSelect].text;
    return textSelect;
}

/*Pone el foco en un campo*/
function focusOnFieldFG(strNameField) {
    document.getElementById(strNameField).focus();
}

/*Valida si un campo esta vacio*/
function valRequiredFieldsFG(strNameField) {
    var fieldData = document.getElementById(strNameField).value;
    if (fieldData == "") {
        return false;
    }
    return true;
}

/*Formatea un campo que contiene comas o signo de pesos y lo regresa como double*/
function getFieldFormatFG(strNameField) {
    var fieldData = document.getElementById(strNameField).value.split(',').join('');
    fieldData = fieldData.replace("$", "");
    return parseFloat(fieldData);
}

/*Alinea la informacion de una caja de texto*/
function alignTextFG(strNameField, strPosition) {
    document.getElementById(strNameField).style.textAlign = strPosition;
}

/****************************************************Funciones HTML*/

/*Crea el codigo html para agregar botones a un Div*/
function addIconHtmlFG(strNameClass, strNameFunction, strNameIcon, strSizeIcon, strTitle, bolShowTitle, strSizeTitulo, bolSpaceEnd) {
    var strTituloExt = "";
    if (strSizeTitulo != "0" && strSizeTitulo != "") {
        strTituloExt = "<td style=\"font-size:" + strSizeTitulo + "px\" class=\"" + strNameClass + "\"> " + strTitle + "</>";
    } else {
        strTituloExt = strTitle;
    }
    var strEspacioHtml = "&nbsp;&nbsp;<td style=\"visibility:hidden\"id=\"22\"><i class = \"fa fa-hdd-o\" style=\"font-size:40px\" ></i></td>";
    var strHtml = "<td " + (strNameClass != "" ? "class=\"" + strNameClass + "\"" : "") + ">";
    strHtml += "<a href=\"javascript:" + strNameFunction + ";\" class=\"sf-with-ul\" title=\"" + strTitle + "\">";
    strHtml += "<i class = \"" + strNameIcon + "\" style=\"font-size:" + strSizeIcon + "px\" ></i>";
    strHtml += (bolShowTitle == true ? strTituloExt : "") + "</td>" + (bolSpaceEnd == true ? strEspacioHtml : "");
    return strHtml;
}

/*Asigna el HTML a un Div*/
function drawIconHtmlFG(strHtmlIcons, strDiv) {
    var strHtml = "<table><tr>";
    strHtml += strHtmlIcons;
    strHtml += "</tr></table>";
    document.getElementById(strDiv).innerHTML = strHtml;
}

/****************************************************Funciones Pantalla*/

/*Oculta el Menu superior*/
function hideTopMenuFG() {
    myLayout.close("west");
    myLayout.close("east");
    myLayout.close("south");
    myLayout.close("north");
    if (document.getElementById("btn1") != null) {
        document.getElementById("btn1").parentNode.parentNode.style.display = 'none';
    }
}

/*Cierra y limpia totalmente la pantalla actual*/
function closeScreenFG() {
    myLayout.open("west");
    myLayout.open("east");
    myLayout.open("south");
    myLayout.open("north");
    document.getElementById("MainPanel").innerHTML = "";
    //Limpiamos el objeto en el framework para que nos deje cargarlo enseguida
    var objMainFacPedi = objMap.getScreen(objMap.getNomMain());
    objMainFacPedi.bolActivo = false;
    objMainFacPedi.bolMain = false;
    objMainFacPedi.bolInit = false;
    objMainFacPedi.idOperAct = 0;
}

/****************************************************Funciones Pop-Up*/

/*Abre un Pop-Up y lo limpia antes de abrir*/
function openPopUpFG(strNomPant, strNomDialog, bolCleanWindow) {
    if (bolCleanWindow) {
        var objSecModiVta = objMap.getScreen(strNomPant);
        if (objSecModiVta != null) {
            objSecModiVta.bolActivo = false;
            objSecModiVta.bolMain = false;
            objSecModiVta.bolInit = false;
            objSecModiVta.idOperAct = 0;
        }
    }
    OpnOpt(strNomPant, '_ed', strNomDialog, false, false, true);
}

/*Cierra un Pop-Up*/
function closePopUpFG(NomDialog) {
    $("#" + NomDialog).dialog("close");
}

/*Limpia Las acciones de un dialog Si o No*/
function _resetSioNoFG(strNameBtnSi, strNameBtnNo, strNameInside) {
    $(strNameBtnSi).unbind("");
    $(strNameBtnNo).unbind("");
    var div = document.getElementById(strNameInside);
    div.innerHTML = "";
}

/*Genera un Dialog Si o no con las acciones que se mandan como parametros*/
function sioNoPopUpFG(objParamsAjax) {
    _resetSioNoFuncGen(objParamsAjax["NamebtnSI"], objParamsAjax["NamebtnNo"], objParamsAjax["NameInside"]);
    $("#" + objParamsAjax["NameDialog"]).dialog('option', 'title', objParamsAjax["strMessage"]);
    dialogSioNoEventFuncGen(objParamsAjax);
    $("#" + objParamsAjax["NameDialog"]).dialog("open");
}

function dialogSioNoEventFuncGen(objParamsAjax) {
    if (objParamsAjax["functionEditIns"] != "") {
        objParamsAjax["functionEditIns"]();
    }
    document.getElementById(objParamsAjax["NamebtnSI"]).onclick = function () {
        confirmSioNoFuncGen(objParamsAjax["objMetodoSi"]);
        $("#" + objParamsAjax["NameDialog"]).dialog("close");
    };
    document.getElementById(objParamsAjax["NamebtnNo"]).onclick = function () {
        confirmSioNoFuncGen(objParamsAjax["objMetodoNO"]);
        $("#" + objParamsAjax["NameDialog"]).dialog("close");
    };
}

function confirmSioNoFuncGen(strNomMetodo) {
    if (strNomMetodo != null && strNomMetodo != "") {
        strNomMetodo();
    }
}

/****************************************************Funciones Ajax*/
/*Realiza una peticion Ajax*/
function ajaxRequestFG(strPost, strJSP, strIdJSP, strNameOpc, strPrefixMaster, strPrefixDetail, strDataType, strNameFunction, strMessageError, objParamsAjax) {
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
}