function vta_combo(){}function initCombo(){hidePR();document.getElementById("TOTAL_ACUMULADO").value=0;document.getElementById("TOTAL_DOLARES").value=0;document.getElementById("TOTAL_EUR").value=0;document.getElementById("TOTAL_CAN").value=0;document.getElementById("TOTAL_LSD").value=0;TasaCambioPtoVta();}function hidePR(){document.getElementById("LABEL_PR").parentNode.parentNode.style.display="none";document.getElementById("PR_ID").parentNode.parentNode.style.display="none";document.getElementById("PR_CODIGO").parentNode.parentNode.style.display="none";document.getElementById("PR_DESCRIPCION").parentNode.parentNode.style.display="none";document.getElementById("PR_CANTIDAD").parentNode.parentNode.style.display="none";document.getElementById("PR_NUMERO").parentNode.parentNode.style.display="none";document.getElementById("PR_PRECIO").parentNode.parentNode.style.display="none";document.getElementById("BTN_CONFIRMA_PR").parentNode.parentNode.style.display="none";}function nvo_Producto(){document.getElementById("LABEL_PR").parentNode.parentNode.style.display="";document.getElementById("PR_ID").parentNode.parentNode.style.display="";document.getElementById("PR_CODIGO").parentNode.parentNode.style.display="";document.getElementById("PR_DESCRIPCION").parentNode.parentNode.style.display="";document.getElementById("PR_CANTIDAD").parentNode.parentNode.style.display="";document.getElementById("PR_NUMERO").parentNode.parentNode.style.display="";document.getElementById("PR_PRECIO").parentNode.parentNode.style.display="";document.getElementById("BTN_CONFIRMA_PR").parentNode.parentNode.style.display="";}function del_Producto1(){var grid1=jQuery("#GR_PROD1");var idCmb1=grid1.getGridParam("selrow");if(idCmb1!=null){var lstRow=grid1.getRowData(idCmb1);document.getElementById("TOTAL_ACUMULADO").value=document.getElementById("TOTAL_ACUMULADO").value-parseFloat(lstRow.CMBD_PR_PRECIO);grid1.delRowData(idCmb1);initTasas();}else{alert("Debe seleccionar un producto");}}function del_Producto2(){var grid2=jQuery("#GR_PROD2");var idCmb2=grid2.getGridParam("selrow");if(idCmb2!=null){var lstRow=grid2.getRowData(idCmb2);document.getElementById("TOTAL_ACUMULADO").value=document.getElementById("TOTAL_ACUMULADO").value-parseFloat(lstRow.CMBD_PR_PRECIO);grid2.delRowData(idCmb2);initTasas();}else{alert("Debe seleccionar un producto");}}function del_Producto3(){var grid3=jQuery("#GR_PROD3");var idCmb3=grid3.getGridParam("selrow");if(idCmb3!=null){var lstRow=grid3.getRowData(idCmb3);document.getElementById("TOTAL_ACUMULADO").value=document.getElementById("TOTAL_ACUMULADO").value-parseFloat(lstRow.CMBD_PR_PRECIO);grid3.delRowData(idCmb3);initTasas();}else{alert("Debe seleccionar un producto");}}function del_Producto4(){var grid4=jQuery("#GR_PROD4");var idCmb4=grid4.getGridParam("selrow");if(idCmb4!=null){var lstRow=grid4.getRowData(idCmb4);document.getElementById("TOTAL_ACUMULADO").value=document.getElementById("TOTAL_ACUMULADO").value-parseFloat(lstRow.CMBD_PR_PRECIO);grid4.delRowData(idCmb4);initTasas();}else{alert("Debe seleccionar un producto");}}function del_Producto5(){var grid5=jQuery("#GR_PROD5");var idCmb5=grid5.getGridParam("selrow");if(idCmb5!=null){var lstRow=grid5.getRowData(idCmb5);document.getElementById("TOTAL_ACUMULADO").value=document.getElementById("TOTAL_ACUMULADO").value-parseFloat(lstRow.CMBD_PR_PRECIO);grid5.delRowData(idCmb5);initTasas();}else{alert("Debe seleccionar un producto");}}function del_Producto6(){var grid6=jQuery("#GR_PROD6");var idCmb6=grid6.getGridParam("selrow");if(idCmb6!=null){var lstRow=grid6.getRowData(idCmb6);document.getElementById("TOTAL_ACUMULADO").value=document.getElementById("TOTAL_ACUMULADO").value-parseFloat(lstRow.CMBD_PR_PRECIO);grid6.delRowData(idCmb6);initTasas();}else{alert("Debe seleccionar un producto");}}function conf_Producto(){var itemIdCob=0;var strID_PR=document.getElementById("PR_ID").value;var strCodigoPR=document.getElementById("PR_CODIGO").value;var strDescripcionPR=document.getElementById("PR_DESCRIPCION").value;var strCantidadPR=document.getElementById("PR_CANTIDAD").value;var strNumeroPR=document.getElementById("PR_NUMERO").value;var strPrecioPR=document.getElementById("PR_PRECIO").value;strPrecioPR=strPrecioPR*strCantidadPR;var strRes=validaTotal(parseFloat(strPrecioPR));var strResID=validaPR_ID();if(strCantidadPR<0.1){alert("Ingrese una catidad de productos.");document.getElementById("PR_CANTIDAD").focus();}else{if(strNumeroPR<1){alert("Seleccione el numero del Producto");document.getElementById("PR_NUMERO").focus();}else{if(strResID=="OK"){if(strRes=="OK"){if(strNumeroPR==1){var dataRow={CMBD_PR_ID:strID_PR,CMBD_PR_CODIGO:strCodigoPR,CMBD_PR_DESCRIPCION:strDescripcionPR,CMBD_PR_CANTIDAD:strCantidadPR,CMBD_CONTADOR:strCantidadPR,CMBD_PR_PRECIO:parseFloat(strPrecioPR),CMBD_PR_NUM:strNumeroPR};itemIdCob=jQuery("#GR_PROD1").length+1;jQuery("#GR_PROD1").addRowData(itemIdCob,dataRow,"last");}if(strNumeroPR==2){var dataRow={CMBD_PR_ID:strID_PR,CMBD_PR_CODIGO:strCodigoPR,CMBD_PR_DESCRIPCION:strDescripcionPR,CMBD_PR_CANTIDAD:strCantidadPR,CMBD_CONTADOR:strCantidadPR,CMBD_PR_PRECIO:parseFloat(strPrecioPR),CMBD_PR_NUM:strNumeroPR};itemIdCob=jQuery("#GR_PROD2").length+1;jQuery("#GR_PROD2").addRowData(itemIdCob,dataRow,"last");}if(strNumeroPR==3){var dataRow={CMBD_PR_ID:strID_PR,CMBD_PR_CODIGO:strCodigoPR,CMBD_PR_DESCRIPCION:strDescripcionPR,CMBD_PR_CANTIDAD:strCantidadPR,CMBD_CONTADOR:strCantidadPR,CMBD_PR_PRECIO:parseFloat(strPrecioPR),CMBD_PR_NUM:strNumeroPR};itemIdCob=jQuery("#GR_PROD3").length+1;jQuery("#GR_PROD3").addRowData(itemIdCob,dataRow,"last");}if(strNumeroPR==4){var dataRow={CMBD_PR_ID:strID_PR,CMBD_PR_CODIGO:strCodigoPR,CMBD_PR_DESCRIPCION:strDescripcionPR,CMBD_PR_CANTIDAD:strCantidadPR,CMBD_CONTADOR:strCantidadPR,CMBD_PR_PRECIO:parseFloat(strPrecioPR),CMBD_PR_NUM:strNumeroPR};itemIdCob=jQuery("#GR_PROD4").length+1;jQuery("#GR_PROD4").addRowData(itemIdCob,dataRow,"last");}if(strNumeroPR==5){var dataRow={CMBD_PR_ID:strID_PR,CMBD_PR_CODIGO:strCodigoPR,CMBD_PR_DESCRIPCION:strDescripcionPR,CMBD_PR_CANTIDAD:strCantidadPR,CMBD_CONTADOR:strCantidadPR,CMBD_PR_PRECIO:parseFloat(strPrecioPR),CMBD_PR_NUM:strNumeroPR};itemIdCob=jQuery("#GR_PROD5").length+1;jQuery("#GR_PROD5").addRowData(itemIdCob,dataRow,"last");}if(strNumeroPR==6){var dataRow={CMBD_PR_ID:strID_PR,CMBD_PR_CODIGO:strCodigoPR,CMBD_PR_DESCRIPCION:strDescripcionPR,CMBD_PR_CANTIDAD:strCantidadPR,CMBD_CONTADOR:strCantidadPR,CMBD_PR_PRECIO:parseFloat(strPrecioPR),CMBD_PR_NUM:strNumeroPR};itemIdCob=jQuery("#GR_PROD6").length+1;jQuery("#GR_PROD6").addRowData(itemIdCob,dataRow,"last");}cleanData();}else{alert(strRes);document.getElementById("TOTAL_ACUMULADO").value=document.getElementById("TOTAL_ACUMULADO").value-parseFloat(strPrecioPR);initTasas();document.getElementById("PR_CANTIDAD").focus();}}else{alert(strResID);document.getElementById("TOTAL_ACUMULADO").value=document.getElementById("TOTAL_ACUMULADO").value-parseFloat(strPrecioPR);initTasas();document.getElementById("PR_CANTIDAD").focus();}}}}function opnProductos(){OpnOpt("PROD_MAK","grid","dialogProd",false,false);}function cleanData(){document.getElementById("PR_ID").value=0;document.getElementById("PR_CODIGO").value="";document.getElementById("PR_DESCRIPCION").value="";document.getElementById("PR_CANTIDAD").value="";document.getElementById("PR_NUMERO").value=0;document.getElementById("PR_PRECIO").value=0;}function editCombo(){document.getElementById("TOTAL_ACUMULADO").value=0;hidePR();var itemIdCob=0;var strPost="IdCmb="+document.getElementById("CMB_ID").value;$("#dialogWait").dialog("open");$.ajax({type:"POST",data:strPost,scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"xml",url:"ERP_Combo.jsp?id=1",success:function(datos){TasaCambioPtoVta();var dblAcumulado=0;jQuery("#GR_PROD1").clearGridData();jQuery("#GR_PROD2").clearGridData();jQuery("#GR_PROD3").clearGridData();jQuery("#GR_PROD4").clearGridData();jQuery("#GR_PROD5").clearGridData();jQuery("#GR_PROD6").clearGridData();
var objofrt=datos.getElementsByTagName("Combo_deta")[0];var lstProms=objofrt.getElementsByTagName("datos");for(var i=0;i<lstProms.length;i++){var obj=lstProms[i];if(obj.getAttribute("CMBD_PR_NUM")==1){var dataRow1={CMBD_ID:obj.getAttribute("CMBD_ID"),CMBD_PR_ID:obj.getAttribute("CMBD_PR_ID"),CMBD_PR_CODIGO:obj.getAttribute("CMBD_PR_CODIGO"),CMBD_PR_DESCRIPCION:obj.getAttribute("CMBD_PR_DESCRIPCION"),CMBD_PR_CANTIDAD:obj.getAttribute("CMBD_PR_CANTIDAD"),CMBD_CONTADOR:obj.getAttribute("CMBD_CONTADOR"),CMBD_PR_PRECIO:parseFloat(obj.getAttribute("CMBD_PR_PRECIO")),CMBD_PR_NUM:obj.getAttribute("CMBD_PR_NUM")};itemIdCob++;jQuery("#GR_PROD1").addRowData(itemIdCob,dataRow1,"last");dblAcumulado=dblAcumulado+parseFloat(obj.getAttribute("CMBD_PR_PRECIO"));}if(obj.getAttribute("CMBD_PR_NUM")==2){var dataRow2={CMBD_ID:obj.getAttribute("CMBD_ID"),CMBD_PR_ID:obj.getAttribute("CMBD_PR_ID"),CMBD_PR_CODIGO:obj.getAttribute("CMBD_PR_CODIGO"),CMBD_PR_DESCRIPCION:obj.getAttribute("CMBD_PR_DESCRIPCION"),CMBD_PR_CANTIDAD:obj.getAttribute("CMBD_PR_CANTIDAD"),CMBD_CONTADOR:obj.getAttribute("CMBD_CONTADOR"),CMBD_PR_PRECIO:parseFloat(obj.getAttribute("CMBD_PR_PRECIO")),CMBD_PR_NUM:obj.getAttribute("CMBD_PR_NUM")};itemIdCob++;jQuery("#GR_PROD2").addRowData(itemIdCob,dataRow2,"last");dblAcumulado=dblAcumulado+parseFloat(obj.getAttribute("CMBD_PR_PRECIO"));}if(obj.getAttribute("CMBD_PR_NUM")==3){var dataRow3={CMBD_ID:obj.getAttribute("CMBD_ID"),CMBD_PR_ID:obj.getAttribute("CMBD_PR_ID"),CMBD_PR_CODIGO:obj.getAttribute("CMBD_PR_CODIGO"),CMBD_PR_DESCRIPCION:obj.getAttribute("CMBD_PR_DESCRIPCION"),CMBD_PR_CANTIDAD:obj.getAttribute("CMBD_PR_CANTIDAD"),CMBD_CONTADOR:obj.getAttribute("CMBD_CONTADOR"),CMBD_PR_PRECIO:parseFloat(obj.getAttribute("CMBD_PR_PRECIO")),CMBD_PR_NUM:obj.getAttribute("CMBD_PR_NUM")};itemIdCob++;jQuery("#GR_PROD3").addRowData(itemIdCob,dataRow3,"last");dblAcumulado=dblAcumulado+parseFloat(obj.getAttribute("CMBD_PR_PRECIO"));}if(obj.getAttribute("CMBD_PR_NUM")==4){var dataRow4={CMBD_ID:obj.getAttribute("CMBD_ID"),CMBD_PR_ID:obj.getAttribute("CMBD_PR_ID"),CMBD_PR_CODIGO:obj.getAttribute("CMBD_PR_CODIGO"),CMBD_PR_DESCRIPCION:obj.getAttribute("CMBD_PR_DESCRIPCION"),CMBD_PR_CANTIDAD:obj.getAttribute("CMBD_PR_CANTIDAD"),CMBD_CONTADOR:obj.getAttribute("CMBD_CONTADOR"),CMBD_PR_PRECIO:parseFloat(obj.getAttribute("CMBD_PR_PRECIO")),CMBD_PR_NUM:obj.getAttribute("CMBD_PR_NUM")};itemIdCob++;jQuery("#GR_PROD4").addRowData(itemIdCob,dataRow4,"last");dblAcumulado=dblAcumulado+parseFloat(obj.getAttribute("CMBD_PR_PRECIO"));}if(obj.getAttribute("CMBD_PR_NUM")==5){var dataRow5={CMBD_ID:obj.getAttribute("CMBD_ID"),CMBD_PR_ID:obj.getAttribute("CMBD_PR_ID"),CMBD_PR_CODIGO:obj.getAttribute("CMBD_PR_CODIGO"),CMBD_PR_DESCRIPCION:obj.getAttribute("CMBD_PR_DESCRIPCION"),CMBD_PR_CANTIDAD:obj.getAttribute("CMBD_PR_CANTIDAD"),CMBD_CONTADOR:obj.getAttribute("CMBD_CONTADOR"),CMBD_PR_PRECIO:parseFloat(obj.getAttribute("CMBD_PR_PRECIO")),CMBD_PR_NUM:obj.getAttribute("CMBD_PR_NUM")};itemIdCob++;jQuery("#GR_PROD5").addRowData(itemIdCob,dataRow5,"last");dblAcumulado=dblAcumulado+parseFloat(obj.getAttribute("CMBD_PR_PRECIO"));}if(obj.getAttribute("CMBD_PR_NUM")==6){var dataRow6={CMBD_ID:obj.getAttribute("CMBD_ID"),CMBD_PR_ID:obj.getAttribute("CMBD_PR_ID"),CMBD_PR_CODIGO:obj.getAttribute("CMBD_PR_CODIGO"),CMBD_PR_DESCRIPCION:obj.getAttribute("CMBD_PR_DESCRIPCION"),CMBD_PR_CANTIDAD:obj.getAttribute("CMBD_PR_CANTIDAD"),CMBD_CONTADOR:obj.getAttribute("CMBD_CONTADOR"),CMBD_PR_PRECIO:parseFloat(obj.getAttribute("CMBD_PR_PRECIO")),CMBD_PR_NUM:obj.getAttribute("CMBD_PR_NUM")};itemIdCob++;jQuery("#GR_PROD6").addRowData(itemIdCob,dataRow6,"last");dblAcumulado=dblAcumulado+parseFloat(obj.getAttribute("CMBD_PR_PRECIO"));}}document.getElementById("TOTAL_ACUMULADO").value=dblAcumulado;$("#dialogWait").dialog("close");}});}function addCombo(datos){$("#dialogWait").dialog("open");var blAlta=true;var intIdSave=0;if(datos!=null){intIdSave=trim(datos).replace("OK","");}else{intIdSave=document.getElementById("CMB_ID").value;}var strPost="";var grid1=jQuery("#GR_PROD1");var idArr1=grid1.getDataIDs();if(idArr1.length==0){}else{for(var i=0;i<idArr1.length;i++){var id=idArr1[i];var lstRow=grid1.getRowData(id);strPost+="&PR_ID1_"+i+"="+lstRow.CMBD_PR_ID+"";strPost+="&PR_CODIGO1_"+i+"="+lstRow.CMBD_PR_CODIGO+"";strPost+="&PR_DESCRIPCION1_"+i+"="+lstRow.CMBD_PR_DESCRIPCION+"";strPost+="&PR_CANTIDAD1_"+i+"="+lstRow.CMBD_PR_CANTIDAD+"";strPost+="&PR_PRECIO1_"+i+"="+lstRow.CMBD_PR_PRECIO+"";}}var grid2=jQuery("#GR_PROD2");var idArr2=grid2.getDataIDs();if(idArr2.length==0){}else{for(var i=0;i<idArr2.length;i++){var id=idArr2[i];var lstRow=grid2.getRowData(id);strPost+="&PR_ID2_"+i+"="+lstRow.CMBD_PR_ID+"";strPost+="&PR_CODIGO2_"+i+"="+lstRow.CMBD_PR_CODIGO+"";strPost+="&PR_DESCRIPCION2_"+i+"="+lstRow.CMBD_PR_DESCRIPCION+"";strPost+="&PR_CANTIDAD2_"+i+"="+lstRow.CMBD_PR_CANTIDAD+"";strPost+="&PR_PRECIO2_"+i+"="+lstRow.CMBD_PR_PRECIO+"";}}var grid3=jQuery("#GR_PROD3");var idArr3=grid3.getDataIDs();if(idArr3.length==0){}else{for(var i=0;i<idArr3.length;i++){var id=idArr3[i];var lstRow=grid3.getRowData(id);strPost+="&PR_ID3_"+i+"="+lstRow.CMBD_PR_ID+"";strPost+="&PR_CODIGO3_"+i+"="+lstRow.CMBD_PR_CODIGO+"";strPost+="&PR_DESCRIPCION3_"+i+"="+lstRow.CMBD_PR_DESCRIPCION+"";strPost+="&PR_CANTIDAD3_"+i+"="+lstRow.CMBD_PR_CANTIDAD+"";strPost+="&PR_PRECIO3_"+i+"="+lstRow.CMBD_PR_PRECIO+"";}}var grid4=jQuery("#GR_PROD4");var idArr4=grid4.getDataIDs();if(idArr4.length==0){}else{for(var i=0;i<idArr4.length;i++){var id=idArr4[i];var lstRow=grid4.getRowData(id);strPost+="&PR_ID4_"+i+"="+lstRow.CMBD_PR_ID+"";strPost+="&PR_CODIGO4_"+i+"="+lstRow.CMBD_PR_CODIGO+"";strPost+="&PR_DESCRIPCION4_"+i+"="+lstRow.CMBD_PR_DESCRIPCION+"";strPost+="&PR_CANTIDAD4_"+i+"="+lstRow.CMBD_PR_CANTIDAD+"";strPost+="&PR_PRECIO4_"+i+"="+lstRow.CMBD_PR_PRECIO+"";}}var grid5=jQuery("#GR_PROD5");var idArr5=grid5.getDataIDs();if(idArr5.length==0){}else{for(var i=0;i<idArr5.length;i++){var id=idArr5[i];var lstRow=grid5.getRowData(id);strPost+="&PR_ID5_"+i+"="+lstRow.CMBD_PR_ID+"";strPost+="&PR_CODIGO5_"+i+"="+lstRow.CMBD_PR_CODIGO+"";strPost+="&PR_DESCRIPCION5_"+i+"="+lstRow.CMBD_PR_DESCRIPCION+"";strPost+="&PR_CANTIDAD5_"+i+"="+lstRow.CMBD_PR_CANTIDAD+"";strPost+="&PR_PRECIO5_"+i+"="+lstRow.CMBD_PR_PRECIO+"";}}var grid6=jQuery("#GR_PROD6");var idArr6=grid6.getDataIDs();if(idArr6.length==0){}else{for(var i=0;i<idArr6.length;i++){var id=idArr6[i];var lstRow=grid6.getRowData(id);strPost+="&PR_ID6_"+i+"="+lstRow.CMBD_PR_ID+"";strPost+="&PR_CODIGO6_"+i+"="+lstRow.CMBD_PR_CODIGO+"";strPost+="&PR_DESCRIPCION6_"+i+"="+lstRow.CMBD_PR_DESCRIPCION+"";strPost+="&PR_CANTIDAD6_"+i+"="+lstRow.CMBD_PR_CANTIDAD+"";strPost+="&PR_PRECIO6_"+i+"="+lstRow.CMBD_PR_PRECIO+"";}}strPost+="&intlenghtArr1="+idArr1.length;strPost+="&intlenghtArr2="+idArr2.length;strPost+="&intlenghtArr3="+idArr3.length;strPost+="&intlenghtArr4="+idArr4.length;strPost+="&intlenghtArr5="+idArr5.length;strPost+="&intlenghtArr6="+idArr6.length;strPost+="&intIdMaster="+intIdSave;strPost+="&blAlta="+blAlta;$.ajax({type:"POST",data:strPost,scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"html",url:"ERP_Combo.jsp?id=2",success:function(datos){if(datos.substring(0,2)=="OK"){_objSc.RestoreSave();_objSc=null;}else{alert(datos);}$("#dialogWait").dialog("close");},error:function(objeto,quepaso,otroobj){alert(":pto:"+objeto+" "+quepaso+" "+otroobj);}});}function EditCombo(datos){$("#dialogWait").dialog("open");var intIdSave=0;if(datos!=null){intIdSave=trim(datos).replace("OK","");}else{intIdSave=document.getElementById("CMB_ID").value;}var strPost="";var grid1=jQuery("#GR_PROD1");var idArr1=grid1.getDataIDs();if(idArr1.length==0){}else{for(var i=0;i<idArr1.length;i++){var id=idArr1[i];var lstRow=grid1.getRowData(id);strPost+="&PR_ID1_"+i+"="+lstRow.CMBD_PR_ID+"";strPost+="&PR_CODIGO1_"+i+"="+lstRow.CMBD_PR_CODIGO+"";strPost+="&PR_DESCRIPCION1_"+i+"="+lstRow.CMBD_PR_DESCRIPCION+"";
strPost+="&PR_CANTIDAD1_"+i+"="+lstRow.CMBD_PR_CANTIDAD+"";strPost+="&PR_PRECIO1_"+i+"="+lstRow.CMBD_PR_PRECIO+"";}}var grid2=jQuery("#GR_PROD2");var idArr2=grid2.getDataIDs();if(idArr2.length==0){}else{for(var i=0;i<idArr2.length;i++){var id=idArr2[i];var lstRow=grid2.getRowData(id);strPost+="&PR_ID2_"+i+"="+lstRow.CMBD_PR_ID+"";strPost+="&PR_CODIGO2_"+i+"="+lstRow.CMBD_PR_CODIGO+"";strPost+="&PR_DESCRIPCION2_"+i+"="+lstRow.CMBD_PR_DESCRIPCION+"";strPost+="&PR_CANTIDAD2_"+i+"="+lstRow.CMBD_PR_CANTIDAD+"";strPost+="&PR_PRECIO2_"+i+"="+lstRow.CMBD_PR_PRECIO+"";}}var grid3=jQuery("#GR_PROD3");var idArr3=grid3.getDataIDs();if(idArr3.length==0){}else{for(var i=0;i<idArr3.length;i++){var id=idArr3[i];var lstRow=grid3.getRowData(id);strPost+="&PR_ID3_"+i+"="+lstRow.CMBD_PR_ID+"";strPost+="&PR_CODIGO3_"+i+"="+lstRow.CMBD_PR_CODIGO+"";strPost+="&PR_DESCRIPCION3_"+i+"="+lstRow.CMBD_PR_DESCRIPCION+"";strPost+="&PR_CANTIDAD3_"+i+"="+lstRow.CMBD_PR_CANTIDAD+"";strPost+="&PR_PRECIO3_"+i+"="+lstRow.CMBD_PR_PRECIO+"";}}var grid4=jQuery("#GR_PROD4");var idArr4=grid4.getDataIDs();if(idArr4.length==0){}else{for(var i=0;i<idArr4.length;i++){var id=idArr4[i];var lstRow=grid4.getRowData(id);strPost+="&PR_ID4_"+i+"="+lstRow.CMBD_PR_ID+"";strPost+="&PR_CODIGO4_"+i+"="+lstRow.CMBD_PR_CODIGO+"";strPost+="&PR_DESCRIPCION4_"+i+"="+lstRow.CMBD_PR_DESCRIPCION+"";strPost+="&PR_CANTIDAD4_"+i+"="+lstRow.CMBD_PR_CANTIDAD+"";strPost+="&PR_PRECIO4_"+i+"="+lstRow.CMBD_PR_PRECIO+"";}}var grid5=jQuery("#GR_PROD5");var idArr5=grid5.getDataIDs();if(idArr5.length==0){}else{for(var i=0;i<idArr5.length;i++){var id=idArr5[i];var lstRow=grid5.getRowData(id);strPost+="&PR_ID5_"+i+"="+lstRow.CMBD_PR_ID+"";strPost+="&PR_CODIGO5_"+i+"="+lstRow.CMBD_PR_CODIGO+"";strPost+="&PR_DESCRIPCION5_"+i+"="+lstRow.CMBD_PR_DESCRIPCION+"";strPost+="&PR_CANTIDAD5_"+i+"="+lstRow.CMBD_PR_CANTIDAD+"";strPost+="&PR_PRECIO5_"+i+"="+lstRow.CMBD_PR_PRECIO+"";}}var grid6=jQuery("#GR_PROD6");var idArr6=grid6.getDataIDs();if(idArr6.length==0){}else{for(var i=0;i<idArr6.length;i++){var id=idArr6[i];var lstRow=grid6.getRowData(id);strPost+="&PR_ID6_"+i+"="+lstRow.CMBD_PR_ID+"";strPost+="&PR_CODIGO6_"+i+"="+lstRow.CMBD_PR_CODIGO+"";strPost+="&PR_DESCRIPCION6_"+i+"="+lstRow.CMBD_PR_DESCRIPCION+"";strPost+="&PR_CANTIDAD6_"+i+"="+lstRow.CMBD_PR_CANTIDAD+"";strPost+="&PR_PRECIO6_"+i+"="+lstRow.CMBD_PR_PRECIO+"";}}strPost+="&intlenghtArr1="+idArr1.length;strPost+="&intlenghtArr2="+idArr2.length;strPost+="&intlenghtArr3="+idArr3.length;strPost+="&intlenghtArr4="+idArr4.length;strPost+="&intlenghtArr5="+idArr5.length;strPost+="&intlenghtArr6="+idArr6.length;strPost+="&intIdMaster="+intIdSave;$.ajax({type:"POST",data:strPost,scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"html",url:"ERP_Combo.jsp?id=3",success:function(datos){if(datos.substring(0,2)=="OK"){_objSc.RestoreSave();_objSc=null;}else{alert(datos);}$("#dialogWait").dialog("close");},error:function(objeto,quepaso,otroobj){alert(":pto:"+objeto+" "+quepaso+" "+otroobj);}});}function EditAfterAddCombo(datos){$("#dialogWait").dialog("open");var blAlta=false;var intIdSave=0;if(datos!=null){intIdSave=trim(datos).replace("OK","");}else{intIdSave=document.getElementById("CMB_ID").value;}var strPost="";var grid1=jQuery("#GR_PROD1");var idArr1=grid1.getDataIDs();if(idArr1.length==0){}else{for(var i=0;i<idArr1.length;i++){var id=idArr1[i];var lstRow=grid1.getRowData(id);strPost+="&PR_ID1_"+i+"="+lstRow.CMBD_PR_ID+"";strPost+="&PR_CODIGO1_"+i+"="+lstRow.CMBD_PR_CODIGO+"";strPost+="&PR_DESCRIPCION1_"+i+"="+lstRow.CMBD_PR_DESCRIPCION+"";strPost+="&PR_CANTIDAD1_"+i+"="+lstRow.CMBD_PR_CANTIDAD+"";strPost+="&PR_PRECIO1_"+i+"="+lstRow.CMBD_PR_PRECIO+"";}}var grid2=jQuery("#GR_PROD2");var idArr2=grid2.getDataIDs();if(idArr2.length==0){}else{for(var i=0;i<idArr2.length;i++){var id=idArr2[i];var lstRow=grid2.getRowData(id);strPost+="&PR_ID2_"+i+"="+lstRow.CMBD_PR_ID+"";strPost+="&PR_CODIGO2_"+i+"="+lstRow.CMBD_PR_CODIGO+"";strPost+="&PR_DESCRIPCION2_"+i+"="+lstRow.CMBD_PR_DESCRIPCION+"";strPost+="&PR_CANTIDAD2_"+i+"="+lstRow.CMBD_PR_CANTIDAD+"";strPost+="&PR_PRECIO2_"+i+"="+lstRow.CMBD_PR_PRECIO+"";}}var grid3=jQuery("#GR_PROD3");var idArr3=grid3.getDataIDs();if(idArr3.length==0){}else{for(var i=0;i<idArr3.length;i++){var id=idArr3[i];var lstRow=grid3.getRowData(id);strPost+="&PR_ID3_"+i+"="+lstRow.CMBD_PR_ID+"";strPost+="&PR_CODIGO3_"+i+"="+lstRow.CMBD_PR_CODIGO+"";strPost+="&PR_DESCRIPCION3_"+i+"="+lstRow.CMBD_PR_DESCRIPCION+"";strPost+="&PR_CANTIDAD3_"+i+"="+lstRow.CMBD_PR_CANTIDAD+"";strPost+="&PR_PRECIO3_"+i+"="+lstRow.CMBD_PR_PRECIO+"";}}var grid4=jQuery("#GR_PROD4");var idArr4=grid4.getDataIDs();if(idArr4.length==0){}else{for(var i=0;i<idArr4.length;i++){var id=idArr4[i];var lstRow=grid4.getRowData(id);strPost+="&PR_ID4_"+i+"="+lstRow.CMBD_PR_ID+"";strPost+="&PR_CODIGO4_"+i+"="+lstRow.CMBD_PR_CODIGO+"";strPost+="&PR_DESCRIPCION4_"+i+"="+lstRow.CMBD_PR_DESCRIPCION+"";strPost+="&PR_CANTIDAD4_"+i+"="+lstRow.CMBD_PR_CANTIDAD+"";strPost+="&PR_PRECIO4_"+i+"="+lstRow.CMBD_PR_PRECIO+"";}}var grid5=jQuery("#GR_PROD5");var idArr5=grid5.getDataIDs();if(idArr5.length==0){}else{for(var i=0;i<idArr5.length;i++){var id=idArr5[i];var lstRow=grid5.getRowData(id);strPost+="&PR_ID5_"+i+"="+lstRow.CMBD_PR_ID+"";strPost+="&PR_CODIGO5_"+i+"="+lstRow.CMBD_PR_CODIGO+"";strPost+="&PR_DESCRIPCION5_"+i+"="+lstRow.CMBD_PR_DESCRIPCION+"";strPost+="&PR_CANTIDAD5_"+i+"="+lstRow.CMBD_PR_CANTIDAD+"";strPost+="&PR_PRECIO5_"+i+"="+lstRow.CMBD_PR_PRECIO+"";}}var grid6=jQuery("#GR_PROD6");var idArr6=grid6.getDataIDs();if(idArr6.length==0){}else{for(var i=0;i<idArr6.length;i++){var id=idArr6[i];var lstRow=grid6.getRowData(id);strPost+="&PR_ID6_"+i+"="+lstRow.CMBD_PR_ID+"";strPost+="&PR_CODIGO6_"+i+"="+lstRow.CMBD_PR_CODIGO+"";strPost+="&PR_DESCRIPCION6_"+i+"="+lstRow.CMBD_PR_DESCRIPCION+"";strPost+="&PR_CANTIDAD6_"+i+"="+lstRow.CMBD_PR_CANTIDAD+"";strPost+="&PR_PRECIO6_"+i+"="+lstRow.CMBD_PR_PRECIO+"";}}strPost+="&intlenghtArr1="+idArr1.length;strPost+="&intlenghtArr2="+idArr2.length;strPost+="&intlenghtArr3="+idArr3.length;strPost+="&intlenghtArr4="+idArr4.length;strPost+="&intlenghtArr5="+idArr5.length;strPost+="&intlenghtArr6="+idArr6.length;strPost+="&intIdMaster="+intIdSave;strPost+="&blAlta="+blAlta;$.ajax({type:"POST",data:strPost,scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"html",url:"ERP_Combo.jsp?id=2",success:function(datos){if(datos.substring(0,2)=="OK"){_objSc.RestoreSave();_objSc=null;}else{alert(datos);}$("#dialogWait").dialog("close");},error:function(objeto,quepaso,otroobj){alert(":pto:"+objeto+" "+quepaso+" "+otroobj);}});}function validaTotal(strPrecioPR){var strMsg="";var dblTotal=parseFloat(strPrecioPR);var tmpTotal=parseFloat(document.getElementById("TOTAL_ACUMULADO").value);var nvoTotal=tmpTotal+dblTotal;document.getElementById("TOTAL_ACUMULADO").value=nvoTotal;initTasas();strMsg=checkPTienda();if(strMsg=="OK"){strMsg=checkPMayoreo();if(strMsg=="OK"){strMsg=checkPDistribuidor();if(strMsg=="OK"){return strMsg;}else{return strMsg;}}else{return strMsg;}}else{return strMsg;}}function checkPTienda(){var strRespuesta="OK";initTasas();var tmpMPX=parseFloat(document.getElementById("TOTAL_ACUMULADO").value);var tmpUSD=parseFloat(document.getElementById("TOTAL_DOLARES").value);var tmpEUR=parseFloat(document.getElementById("TOTAL_EUR").value);var tmpCAN=parseFloat(document.getElementById("TOTAL_CAN").value);var tmpLSD=parseFloat(document.getElementById("TOTAL_LSD").value);var precTienda=parseFloat(document.getElementById("CMB_PREC_TIENDA").value);var idMon=document.getElementById("MON_ID_PREC_TIENDA").value;switch(idMon){case"1":if(tmpMPX>=precTienda){strRespuesta="Precio Tienda afecta costo.";}break;case"2":if(tmpUSD>=precTienda){strRespuesta="Precio Tienda afecta costo.";}break;case"3":if(tmpEUR>=precTienda){strRespuesta="Precio Tienda afecta costo.";}break;case"4":if(tmpCAN>=precTienda){strRespuesta="Precio Tienda afecta costo.";
}break;case"5":if(tmpLSD>=precTienda){strRespuesta="Precio Tienda afecta costo.";}break;}return strRespuesta;}function checkPMayoreo(){var strRespuesta="OK";initTasas();var tmpMPX=parseFloat(document.getElementById("TOTAL_ACUMULADO").value);var tmpUSD=parseFloat(document.getElementById("TOTAL_DOLARES").value);var tmpEUR=parseFloat(document.getElementById("TOTAL_EUR").value);var tmpCAN=parseFloat(document.getElementById("TOTAL_CAN").value);var tmpLSD=parseFloat(document.getElementById("TOTAL_LSD").value);var precMayoreo=parseFloat(document.getElementById("CMB_PREC_MAYOREO").value);var idMon=document.getElementById("MON_ID_PREC_MAYOREO").value;switch(idMon){case"1":if(tmpMPX>=precMayoreo){strRespuesta="Precio Mayoreo afecta costo.";}break;case"2":if(tmpUSD>=precMayoreo){strRespuesta="Precio Mayoreo afecta costo.";}break;case"3":if(tmpEUR>=precMayoreo){strRespuesta="Precio Mayoreo afecta costo.";}break;case"4":if(tmpCAN>=precMayoreo){strRespuesta="Precio Mayoreo afecta costo.";}break;case"5":if(tmpLSD>=precMayoreo){strRespuesta="Precio Mayoreo afecta costo.";}break;}return strRespuesta;}function initTasas(){var tmpMPX=parseFloat(document.getElementById("TOTAL_ACUMULADO").value);var tmpUSD=parseFloat(document.getElementById("TASA_USD").value);var tmpEUR=parseFloat(document.getElementById("TASA_EUR").value);var tmpCAN=parseFloat(document.getElementById("TASA_CAN").value);var tmpLSD=parseFloat(document.getElementById("TASA_LSD").value);tmpUSD=tmpMPX/tmpUSD;tmpEUR=tmpMPX/tmpEUR;tmpCAN=tmpMPX/tmpCAN;tmpLSD=tmpMPX/tmpLSD;document.getElementById("TOTAL_DOLARES").value=tmpUSD;document.getElementById("TOTAL_EUR").value=tmpEUR;document.getElementById("TOTAL_CAN").value=tmpCAN;document.getElementById("TOTAL_LSD").value=tmpLSD;}function checkPDistribuidor(){var strRespuesta="OK";initTasas();var tmpMPX=parseFloat(document.getElementById("TOTAL_ACUMULADO").value);var tmpUSD=parseFloat(document.getElementById("TOTAL_DOLARES").value);var tmpEUR=parseFloat(document.getElementById("TOTAL_EUR").value);var tmpCAN=parseFloat(document.getElementById("TOTAL_CAN").value);var tmpLSD=parseFloat(document.getElementById("TOTAL_LSD").value);var precDistribuidor=parseFloat(document.getElementById("CMB_PREC_DISTRIBUIDOR").value);var idMon=document.getElementById("MON_ID_PREC_DISTRIBUIDOR").value;switch(idMon){case"1":if(tmpMPX>=precDistribuidor){strRespuesta="Precio Distribuidor afecta costo.";}break;case"2":if(tmpUSD>=precDistribuidor){strRespuesta="Precio Distribuidor afecta costo.";}break;case"3":if(tmpEUR>=precDistribuidor){strRespuesta="Precio Distribuidor afecta costo.";}break;case"4":if(tmpCAN>=precDistribuidor){strRespuesta="Precio Distribuidor afecta costo.";}break;case"5":if(tmpLSD>=precDistribuidor){strRespuesta="Precio Distribuidor afecta costo.";}break;}return strRespuesta;}function TasaCambioPtoVta(){$("#dialogWait").dialog("open");var intMonedaBanco=1;var intMonedaSeleccionada=document.getElementById("MON_ID_PREC_TIENDA").value;var strPOST="&Moneda_1="+intMonedaBanco;strPOST+="&Moneda_2="+intMonedaSeleccionada;$.ajax({type:"POST",data:strPOST,scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"xml",url:"ERP_Combo.jsp?id=3",success:function(datos){var objsc=datos.getElementsByTagName("TasaCambio")[0];var lstTiks=objsc.getElementsByTagName("TasaCambios");var obj=lstTiks[0];var dblUSD=parseFloat(obj.getAttribute("USD"));var dblEUR=parseFloat(obj.getAttribute("EUR"));var dblCAN=parseFloat(obj.getAttribute("CAN"));var dblLSD=parseFloat(obj.getAttribute("LSD"));document.getElementById("TASA_USD").value=dblUSD;document.getElementById("TASA_EUR").value=dblEUR;document.getElementById("TASA_CAN").value=dblCAN;document.getElementById("TASA_LSD").value=dblLSD;initTasas();$("#dialogWait").dialog("close");},error:function(objeto,quepaso,otroobj){alert(":ptoExist:"+objeto+" "+quepaso+" "+otroobj);$("#dialogWait").dialog("close");}});}function validaPR_ID(){var strMsgID="OK";var grid1=jQuery("#GR_PROD1");var idArr1=grid1.getDataIDs();if(idArr1.length==0){}else{for(var i=0;i<idArr1.length;i++){var id=idArr1[i];var lstRow=grid1.getRowData(id);if(lstRow.CMBD_PR_ID==document.getElementById("PR_ID").value){strMsgID="el producto ya existe en el paquete";}}}if(strMsgID=="OK"){var grid2=jQuery("#GR_PROD2");var idArr2=grid2.getDataIDs();if(idArr2.length==0){}else{for(var i=0;i<idArr2.length;i++){var id=idArr2[i];var lstRow=grid2.getRowData(id);if(lstRow.CMBD_PR_ID==document.getElementById("PR_ID").value){strMsgID="el producto ya existe en el paquete";}}}}if(strMsgID=="OK"){var grid3=jQuery("#GR_PROD3");var idArr3=grid3.getDataIDs();if(idArr3.length==0){}else{for(var i=0;i<idArr3.length;i++){var id=idArr3[i];var lstRow=grid3.getRowData(id);if(lstRow.CMBD_PR_ID==document.getElementById("PR_ID").value){strMsgID="el producto ya existe en el paquete";}}}}if(strMsgID=="OK"){var grid4=jQuery("#GR_PROD4");var idArr4=grid4.getDataIDs();if(idArr4.length==0){}else{for(var i=0;i<idArr4.length;i++){var id=idArr4[i];var lstRow=grid4.getRowData(id);if(lstRow.CMBD_PR_ID==document.getElementById("PR_ID").value){strMsgID="el producto ya existe en el paquete";}}}}if(strMsgID=="OK"){var grid5=jQuery("#GR_PROD5");var idArr5=grid5.getDataIDs();if(idArr5.length==0){}else{for(var i=0;i<idArr5.length;i++){var id=idArr5[i];var lstRow=grid5.getRowData(id);if(lstRow.CMBD_PR_ID==document.getElementById("PR_ID").value){strMsgID="el producto ya existe en el paquete";}}}}if(strMsgID=="OK"){var grid6=jQuery("#GR_PROD6");var idArr6=grid6.getDataIDs();if(idArr6.length==0){}else{for(var i=0;i<idArr6.length;i++){var id=idArr6[i];var lstRow=grid6.getRowData(id);if(lstRow.CMBD_PR_ID==document.getElementById("PR_ID").value){strMsgID="el producto ya existe en el paquete";}}}}return strMsgID;}function selectPrCombo(event,obj){if(event.keyCode==13){openProdsMak();}}function openProdsMak(){var objSecModiVta=objMap.getScreen("PRODUCTOS_MAK");if(objSecModiVta!=null){objSecModiVta.bolActivo=false;objSecModiVta.bolMain=false;objSecModiVta.bolInit=false;objSecModiVta.idOperAct=0;}OpnOpt("PRODUCTOS_MAK","_ed","dialog",false,false,true);}