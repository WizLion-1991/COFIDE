function vta_ncview_prov(){}var bolAnularVta=false;var bolSoyMain=false;var strNomMain=false;function InitViewVNCCXP(){try{document.getElementById("btn1").style.display="none";}catch(err){}strNomMain=objMap.getNomMain();if(strNomMain=="NC_VIEW2"){bolSoyMain=true;}ActivaButtonsNCCXP(false,false,false,!bolSoyMain);$("#dialogWait").dialog("open");$.ajax({type:"POST",data:"keys=98",scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"xml",url:"Acceso.do",success:function(datos){var objsc=datos.getElementsByTagName("Access")[0];var lstKeys=objsc.getElementsByTagName("key");for(i=0;i<lstKeys.length;i++){var obj=lstKeys[i];if(obj.getAttribute("id")==98&&obj.getAttribute("enabled")=="true"){bolAnularVta=true;}}$("#dialogWait").dialog("close");},error:function(objeto,quepaso,otroobj){alert(":pto:"+objeto+" "+quepaso+" "+otroobj);}});}var strNomFormView="";var strKeyView="";var strNomFormatPrint="";var strTipoVtaView="";var strNomOrderView="";function ViewDoNCCXP(){bolSoyMain=false;strNomMain=objMap.getNomMain();if(strNomMain=="NC_VIEW2"){bolSoyMain=true;}ActivaButtonsNCCXP(bolAnularVta,true,true,!bolSoyMain);var strPrefijoMaster="NC";strNomOrderView="NC_ID";strNomFormView="NCVIEW_PROV";strKeyView="NC_ID";strNomFormatPrint="NCREDITO";strTipoVtaView="2";var strParams="&"+strPrefijoMaster+"_ANULADA=999";var strFecha1=document.getElementById("VIEW_FECHA1").value;var strFecha2=document.getElementById("VIEW_FECHA2").value;var strId=document.getElementById("VIEW_ID").value;if(strId!="0"&&strId!=""){strParams+="&"+strKeyView+"="+strId+"";}else{if(strFecha1!=""&&strFecha2!=""){strParams+="&"+strPrefijoMaster+"_FECHA1="+strFecha1+"&"+strPrefijoMaster+"_FECHA2="+strFecha2+"";}if(document.getElementById("VIEW_SUCURSAL").value!="0"){strParams+="&SC_ID="+document.getElementById("VIEW_SUCURSAL").value+"";}if(document.getElementById("VIEW_CLIENTE").value!="0"){strParams+="&PV_ID="+document.getElementById("VIEW_CLIENTE").value+"";}if(document.getElementById("VIEW_FOLIO").value!=""){strParams+="&"+strPrefijoMaster+"_FOLIO="+document.getElementById("VIEW_FOLIO").value+"";}if(document.getElementById("VIEW_EMP").value!="0"){strParams+="&EMP_ID="+document.getElementById("VIEW_EMP").value+"";}}var grid=jQuery("#VIEW_GRIDNC1");grid.setGridParam({url:"CIP_TablaOp.jsp?ID=5&opnOpt="+strNomFormView+"&_search=true"+strParams});grid.setGridParam({sortname:strNomOrderView}).trigger("reloadGrid");}function ValidaCleanNCCXP(strNomField){var objDivErr=document.getElementById("err_"+strNomField);if(objDivErr!=null){objDivErr.innerHTML="";objDivErr.setAttribute("class","");objDivErr.setAttribute("className","");}}function ValidaShowNCCXP(strNomField,strMsg){var objDivErr=document.getElementById("err_"+strNomField);objDivErr.setAttribute("class","");objDivErr.setAttribute("class","inError");objDivErr.setAttribute("className","inError");objDivErr.innerHTML="<img src='images/layout/report3_del.gif' border='0'>&nbsp;"+strMsg;}function VtaViewPrintNCCXP(){var grid=jQuery("#VIEW_GRIDNC1");if(grid.getGridParam("selrow")!=null){var lstRow=grid.getRowData(grid.getGridParam("selrow"));var strHtml=CreaHidden("NC_ID",lstRow.NC_ID);openWhereverFormat("ERP_SendNC?id="+lstRow.NC_ID,strNomFormatPrint,"PDF",strHtml);}}function VtaViewAnulNCCXP(){var grid=jQuery("#VIEW_GRIDNC1");if(grid.getGridParam("selrow")!=null&&bolAnularVta){document.getElementById("SioNO_inside").innerHTML="";$("#SioNO").dialog("open");$("#SioNO").dialog("option","title",lstMsg[46]);document.getElementById("btnSI").onclick=function(){$("#SioNO").dialog("close");VtaViewAnulDoNCCXP();};document.getElementById("btnNO").onclick=function(){$("#SioNO").dialog("close");};}}function VtaViewAnulDoNCCXP(){var grid=jQuery("#VIEW_GRIDNC1");if(grid.getGridParam("selrow")!=null){$("#dialogWait").dialog("open");var lstRow=grid.getRowData(grid.getGridParam("selrow"));$.ajax({type:"POST",data:encodeURI("idAnul="+lstRow.NC_ID),scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"html",url:"NCMovp.do?id=2",success:function(dato){dato=trim(dato);if(dato!="OK"){alert(dato);}grid.setGridParam({sortname:strNomOrderView}).trigger("reloadGrid");$("#dialogWait").dialog("close");},error:function(objeto,quepaso,otroobj){alert(":pto9:"+objeto+" "+quepaso+" "+otroobj);$("#dialogWait").dialog("close");}});}}function resetGridViewNCCXP(){var grid=jQuery("#VIEW_GRIDNC1");grid.clearGridData();}function VtaViewSalirNCCXP(){strNomMain=objMap.getNomMain();if(strNomMain!="VTAS_VIEW2"){$("#dialogView").dialog("close");}}function VtaViewXMLNCCXP(){var grid=jQuery("#VIEW_GRIDNC1");if(grid.getGridParam("selrow")!=null){var lstRow=grid.getRowData(grid.getGridParam("selrow"));openXMLNC(lstRow.NC_ID);}}function VtaViewEditNCCXP(){strNomMain=objMap.getNomMain();var grid=jQuery("#VIEW_GRIDNC1");var idSel=grid.getGridParam("selrow");if(idSel!=null){if(document.getElementById("VIEW_TIPO").value=="3"){var lstRow=grid.getRowData(idSel);if(lstRow.NC_ANULADA=="NO"){if(strNomMain=="VENTAS"){getPedidoenVenta(idSel,"PEDIDO");}else{if(strNomMain=="SERVICIOS"){getPedidoenVentaSrv(idSel,"PEDIDO");}}}}}}function ActivaButtonsNCCXP(bolAnular,bolPrint,bolXML,bolExit){if(bolAnular){document.getElementById("vv_btnCancel1").style.display="block";}else{document.getElementById("vv_btnCancel1").style.display="none";}if(bolPrint){document.getElementById("vv_btnPrint1").style.display="block";}else{document.getElementById("vv_btnPrint1").style.display="none";}if(bolXML){document.getElementById("vv_btnXML1").style.display="block";}else{document.getElementById("vv_btnXML1").style.display="none";}if(bolExit){document.getElementById("vv_btnExit1").style.display="block";}else{document.getElementById("vv_btnExit1").style.display="none";}}function OpnDiagCteCXP(){OpnOpt("PROVEEDOR","grid","dialogProv",false,false);}function openXMLNC(strNCId){var strHtml='<form action="ERP_XML_Download.jsp" method="post" target="_blank" id="formSend">';strHtml+=CreaHidden("NC_ID",strNCId);strHtml+="</form>";document.getElementById("formHidden").innerHTML=strHtml;document.getElementById("formSend").submit();}