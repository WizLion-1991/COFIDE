function ClickPermiso(obj) {
    var strTxt = new String(obj.nextSibling.data);
    var bolchecked = obj.checked;
    if (strTxt.indexOf("[-]") != -1) {
        var bolFind = false;
        var obNext = obj.nextSibling;
        while (!bolFind) {
            obNext = obNext.nextSibling;
            if (obNext.type == "checkbox") {
                strTxt = new String(obNext.nextSibling.data);
                if (strTxt.indexOf("[-]") != -1) {
                    bolFind = true;
                } else {
                    obNext.checked = bolchecked;
                }
            }
            if (obNext == null || obNext.type == "hidden") {
                bolFind = true;
            }
        }
    } else {
        if (obj.checked) {
            var bolFindB = false;
            var obNextB = obj.previousSibling;
            while (!bolFindB) {
                obNextB = obNextB.previousSibling;
                if (obNextB.type == "checkbox") {
                    strTxt = new String(obNextB.nextSibling.data);
                    if (strTxt.indexOf("[-]") != -1) {
                        bolFindB = true;
                        obNextB.checked = bolchecked;
                    }
                }
                if (obNextB == null) {
                    bolFindB = true;
                }
            }
        }
    }
}
function Permisos() {}
function InactivaPermisosXD() {
    var objPerfil = document.getElementById("PF_ID");
    if (objPerfil.value == 1) {
        var lstCheck = document.getElementsByName("PERM_LST");
        for (var i = 0; i < lstCheck.length; i++) {
            if (parseInt(lstCheck[i].value) == 6 || parseInt(lstCheck[i].value) == 21 || parseInt(lstCheck[i].value) == 22 || parseInt(lstCheck[i].value) == 23 || parseInt(lstCheck[i].value) == 24 || parseInt(lstCheck[i].value) == 25) {
                lstCheck[i].style.display = "none";
            }
        }
    }
}