var lstMsgPass=new Array();lstMsgPass[0]="NECESITA LLENAR TODOS LOS CAMPOS";lstMsgPass[1]="ERROR: PASSWORD DEBE CONTENER SEIS CARACTERES!";lstMsgPass[2]="ERROR: PASSWORD DEBE CONTENER UN NUMERO (0-9)!";lstMsgPass[3]="ERROR: PASSWORD DEBE CONTENER UNA LETRA MINUSCULA (A-Z)!";lstMsgPass[4]="ERROR: PASSWORD DEBE CONTENER UNA LETRA MAYUSCULA (A-Z)!";lstMsgPass[5]="ERROR: EL VALOR DEL PASSWORD NO COINCIDE!";lstMsgPass[6]="ERROR: EL PASSWORD ANTERIOR NO ES CORRECTO!";lstMsgPass[7]="CAMBIO REALIZADO!";lstMsgPass[8]="MAIL ENVIADO!";function getSCChange(){$("#dialogWait").dialog("open");$.ajax({type:"POST",data:"",scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"html",url:"Contrasenia.do?Opt=getScChange",success:function(datos){var div=document.getElementById("MainPanel");div.innerHTML=datos;var divRight=document.getElementById("rightPanel");if(divRight!=undefined){divRight.innerHTML="";}$("#dialogWait").dialog("close");},error:function(objeto,quepaso,otroobj){alert(":pto:"+objeto+" "+quepaso+" "+otroobj);$("#dialogWait").dialog("close");}});}function doChangePass(){var oldpass=document.getElementById("oldpass");var nvopass=document.getElementById("nvopass");var nvopass2=document.getElementById("nvopass");var passback=document.getElementById("passback");var strAnswer=document.getElementById("answer").value;if(oldpass.value==""){alert(lstMsgPass[0]);oldpass.focus();return false;}if(nvopass.value==""){alert(lstMsgPass[0]);nvopass.focus();return false;}if(nvopass2.value==""){alert(lstMsgPass[0]);nvopass2.focus();return false;}if(nvopass.value!=""&&nvopass.value==nvopass2.value){if(nvopass.value.length<6){alert(lstMsgPass[1]);nvopass.focus();return false;}re=/[0-9]/;if(!re.test(nvopass.value)){alert(lstMsgPass[2]);nvopass.focus();return false;}re=/[a-z]/;if(!re.test(nvopass.value)){alert(lstMsgPass[3]);nvopass.focus();return false;}re=/[A-Z]/;if(!re.test(nvopass.value)){alert(lstMsgPass[4]);nvopass.focus();return false;}}else{alert(lstMsgPass[5]);nvopass.focus();return false;}if(trim(passback.value)!=trim(oldpass.value)){alert(lstMsgPass[6]);return false;}$("#dialogWait").dialog("open");document.getElementById("saveme").disabled=true;$.ajax({type:"POST",data:"nvopass="+nvopass.value+"&answer="+strAnswer,scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"html",url:"Contrasenia.do?Opt=doChangePass",success:function(datos){if(trim(datos)=="OK"){var div=document.getElementById("MainPanel");div.innerHTML="<div class='form_head'>"+lstMsgPass[7]+"</div>";}else{alert(datos);document.getElementById("saveme").disabled=false;}$("#dialogWait").dialog("close");},error:function(objeto,quepaso,otroobj){$("#dialogWait").dialog("close");alert(":pto:"+objeto+" "+quepaso+" "+otroobj);document.getElementById("saveme").disabled=false;}});return true;}function doLosePass(){var strMail=document.getElementById("mimail").value;var strAnswer=document.getElementById("answer").value;document.getElementById("saveme").disabled=true;$.ajax({type:"POST",data:"mail="+strMail+"&answer="+strAnswer,scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"html",url:"Contrasenia.do?Opt=doGetLosePass",success:function(datos){if(trim(datos)=="OK"){var div=document.getElementById("contentform");div.innerHTML="<div class='form_head'>"+lstMsgPass[8]+"...</div>";}else{alert(datos);document.getElementById("saveme").disabled=false;}},error:function(objeto,quepaso,otroobj){alert(":pto:"+objeto+" "+quepaso+" "+otroobj);}});}