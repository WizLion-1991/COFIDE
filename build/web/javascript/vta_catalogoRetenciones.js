function vta_catalogoRetenciones(){}function dblClickRete(id){var strNomMain=objMap.getNomMain();var grid=jQuery("#CATAL_RETEN");var lstVal=grid.getRowData(id);if(strNomMain=="CATAL_RETEN"){OpnEdit(document.getElementById("Ed"+strNomMain));}else{if(strNomMain=="CAPTURA"){document.getElementById("RET_CVERETENC").value=lstVal.RET_CLAVE;document.getElementById("RET_DESCRETENC").value=lstVal.RET_CONCEPTO;$("#dialogView").dialog("close");}$("#dialogCte").dialog("close");}}