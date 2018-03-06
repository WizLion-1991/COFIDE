/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.prosefi;

import Tablas.cat_documentacion;
import Tablas.cat_credito;
import Tablas.cat_obligado;
import Tablas.cat_vencimiento;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author SIWEB
 */
public class Credito {

    cat_documentacion objDocumentacion;
    //tcredito objCredito;
    cat_obligado objObligado;
    cat_vencimiento objVencimiento;
    protected cat_credito objCredito;

    public Credito() {
        this.objCredito = new cat_credito();
        this.objObligado = new cat_obligado();
        this.objDocumentacion = new cat_documentacion();
        this.objVencimiento = new cat_vencimiento();
    }

    public cat_credito getObjCredito() {
        return objCredito;
    }

    public void setObjCredito(cat_credito objCredito) {
        this.objCredito = objCredito;
    }

    public cat_obligado getObjObligado() {
        return objObligado;
    }

    public void setObjObligado(cat_obligado objObligado) {
        this.objObligado = objObligado;
    }

    public cat_documentacion getObjDocumentacion() {
        return objDocumentacion;
    }

    public void setObjDocumentacion(cat_documentacion objDocumentacion) {
        this.objDocumentacion = objDocumentacion;
    }

    public cat_vencimiento getObjVencimiento() {
        return objVencimiento;
    }

    public void setObjVencimiento(cat_vencimiento objVencimiento) {
        this.objVencimiento = objVencimiento;
    }

    //metodo de agrega credito
    public String agrega(Conexion oConn) {
        String strOk = "OK";
        String strokDeta = "";
        this.objCredito.setBolGetAutonumeric(true);
        String strResult = this.objCredito.Agrega(oConn);
        //imprimimos si inserto en la tabla
        System.out.println("resultado credito  " + strResult);
        //hacemos la condicion de si, si inserto en la primer tabla inserte en la segunda 
        if (strResult.equals("OK")) {
            //obtenemos el ultimo valor insertado en la tabla anterior
            String strkeyCredito = this.objCredito.getValorKey();
            //declaramos la variable la cual va a ir insertada en la otra tabla
            int intKeyCredito = Integer.parseInt(strkeyCredito);
            //le decimos que la variable se va insertar en la otra tabla
            this.objObligado.setFieldInt("CTO_ID", intKeyCredito);
            String strOK = this.objObligado.Agrega(oConn);
            strokDeta = strOK;
        }
        return strokDeta;
    }

    public String modifica(Conexion oConn, String strIdObligado, String strRazon,
            String strRfc, String strCalle, String strNumero, String strNumeroin,
            String strColonia, String strLocalidad, String strMunicipo, String strEstado,
            String strCodigop, String strTelefono1, String strTelefono2, String strContacto1,
            String strContacto2, String strEmail1, String strEmail2, String strEdad,
            String strN_id, String strEc_id, String strRm_id, String strNconyugue,
            String strN_cid, String strCedad) {

        String strOk = "";
        String strOkdeta = "";
        strOk = this.objCredito.Modifica(oConn);
        if (strOk.equals("OK")) {
            String sqlUpdate = "UPDATE cat_obligado set OB_RAZONSOCIAL = '" + strRazon + "', OB_RFC = '" + strRfc + "', OB_CALLE = '" + strCalle + "', "
                    + "OB_NUMERO = '" + strNumero + "', OB_NUMEROIN = '" + strNumeroin + "', OB_COLONIA = '" + strColonia + "', OB_LOCALIDAD = '" + strLocalidad + "', "
                    + "OB_MUNICIPIO = '" + strMunicipo + "', OB_ESTADO = '" + strEstado + "', OB_CP = '" + strCodigop + "', OB_TELEFONO1 = '" + strTelefono1 + "', "
                    + "OB_TELEFONO2 = '" + strTelefono2 + "', OB_CONTACTO1 = '" + strContacto1 + "', OB_CONTACTO2 = '" + strContacto2 + "', OB_EMAIL1 = '" + strEmail1 + "', "
                    + "OB_EMAIL2 = '" + strEmail2 + "', OB_EDAD = '" + strEdad + "', N_ID = '" + strN_id + "', EC_ID = '" + strEc_id + "', RM_ID = '" + strRm_id + "',"
                    + "OB_NCONYUGUE = '" + strNconyugue + "', N_CID = '" + strN_cid + "', OB_CEDAD = '" + strCedad + "'"
                    + "WHERE CTO_ID = " + strIdObligado;

            oConn.runQueryLMD(sqlUpdate);
            strOkdeta = "OK";
        }
        return strOkdeta;
    }

    public String elimina(Conexion oConn, String strIdObligado) {

        String strOk = "";
        String strOkdeta = "";
        strOk = this.objCredito.Borra(oConn);
        if (strOk.equals("OK")) {
            String sqlDelete = "DELETE FROM cat_obligado WHERE CTO_ID =" + strIdObligado;
            oConn.runQueryLMD(sqlDelete);
            strOkdeta = "OK";
        }
        return strOkdeta;
    }
    
    public String getDatos(Conexion oConn, String strId) throws SQLException {
        Fechas fecha = new Fechas();
        String strXMLData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
        boolean bolExist = false;
        String strCredito = "SELECT * FROM cat_credito where CTO_ID = " + strId;
        //String strConsulta = "SELECT * FROM cat_credito c, cat_obligado o where c.CTO_ID = o.CTO_ID AND o.CTO_ID = " + strId;          
        ResultSet rs = oConn.runQuery(strCredito);

        strXMLData += "<DatosCredito>";
        while (rs.next()) {

            strXMLData += "<Credito ";

            strXMLData += "CTO_ID =\"" + rs.getInt("CTO_ID") + "\" ";
            strXMLData += "CTO_NREFERENCIA =\"" + rs.getString("CTO_NREFERENCIA") + "\" ";
            strXMLData += "SS_ID =\"" + rs.getString("SS_ID") + "\" ";
            strXMLData += "F_ID =\"" + rs.getString("F_ID") + "\" ";
            strXMLData += "CT_ID =\"" + rs.getString("CT_ID") + "\" ";
            strXMLData += "CTO_FECHA =\"" + rs.getString("CTO_FECHA") + "\" ";
            strXMLData += "CTO_NCUENTA =\"" + rs.getString("CTO_NCUENTA") + "\" ";
            strXMLData += "TO_ID =\"" + rs.getString("TO_ID") + "\" ";
            strXMLData += "IM_ID =\"" + rs.getString("IM_ID") + "\" ";
            strXMLData += "MTO_ID =\"" + rs.getString("MTO_ID") + "\" ";
            strXMLData += "M_ID =\"" + rs.getString("M_ID") + "\" ";
            strXMLData += "CTO_NIC =\"" + rs.getString("CTO_NIC") + "\" ";
            strXMLData += "CTO_PCREDITO =\"" + rs.getString("CTO_PCREDITO") + "\" ";
            strXMLData += "CT0_FAPERTURA =\"" + fecha.FormateaDDMMAAAA(rs.getString("CT0_FAPERTURA"), "/") + "\" ";
            strXMLData += "CT0_FVENCIMIENTO =\"" + fecha.FormateaDDMMAAAA(rs.getString("CT0_FVENCIMIENTO"), "/") + "\" ";
            strXMLData += "CT0_PPPERIODO =\"" + rs.getString("CT0_PPPERIODO") + "\" ";
            strXMLData += "CT_NOM =\"" + rs.getString("CT_NOM") + "\" ";
            strXMLData += "F_NOM =\"" + rs.getString("F_NOM") + "\" ";
            strXMLData += "TC_ID =\"" + rs.getString("TC_ID") + "\" ";


        }
        //if(rs.getStatement() != null )rs.getStatement().close(); 
        rs.close();

        String strObligado = "SELECT * FROM  cat_obligado WHERE CTO_ID = " + strId;
        ResultSet rs1 = oConn.runQuery(strObligado);


        while (rs1.next()) {
            System.out.println("obligado");

            strXMLData += "OB_ID =\"" + rs1.getString("OB_ID") + "\" ";
            strXMLData += "OB_RAZONSOCIAL =\"" + rs1.getString("OB_RAZONSOCIAL") + "\" ";
            strXMLData += "OB_RFC =\"" + rs1.getString("OB_RFC") + "\" ";
            strXMLData += "OB_CALLE =\"" + rs1.getString("OB_CALLE") + "\" ";
            strXMLData += "OB_NUMERO =\"" + rs1.getString("OB_NUMERO") + "\" ";
            strXMLData += "OB_NUMEROIN =\"" + rs1.getString("OB_NUMEROIN") + "\" ";
            strXMLData += "OB_COLONIA =\"" + rs1.getString("OB_COLONIA") + "\" ";
            strXMLData += "OB_LOCALIDAD =\"" + rs1.getString("OB_LOCALIDAD") + "\" ";
            strXMLData += "OB_MUNICIPIO =\"" + rs1.getString("OB_MUNICIPIO") + "\" ";
            strXMLData += "OB_ESTADO =\"" + rs1.getString("OB_ESTADO") + "\" ";
            strXMLData += "OB_CP =\"" + rs1.getString("OB_CP") + "\" ";
            strXMLData += "OB_TELEFONO1 =\"" + rs1.getString("OB_TELEFONO1") + "\" ";
            strXMLData += "OB_TELEFONO2 =\"" + rs1.getString("OB_TELEFONO2") + "\" ";
            strXMLData += "OB_CONTACTO1 =\"" + rs1.getString("OB_CONTACTO1") + "\" ";
            strXMLData += "OB_CONTACTO2 =\"" + rs1.getString("OB_CONTACTO2") + "\" ";
            strXMLData += "OB_EMAIL1 =\"" + rs1.getString("OB_EMAIL1") + "\" ";
            strXMLData += "OB_EMAIL2 =\"" + rs1.getString("OB_EMAIL2") + "\" ";
            strXMLData += "OB_EDAD =\"" + rs1.getString("OB_EDAD") + "\" ";
            strXMLData += "N_ID =\"" + rs1.getString("N_ID") + "\" ";
            strXMLData += "EC_ID =\"" + rs1.getString("EC_ID") + "\" ";
            strXMLData += "RM_ID =\"" + rs1.getString("RM_ID") + "\" ";
            strXMLData += "OB_NCONYUGUE =\"" + rs1.getString("OB_NCONYUGUE") + "\" ";
            strXMLData += "N_CID =\"" + rs1.getString("N_CID") + "\" ";
            strXMLData += "OB_CEDAD =\"" + rs1.getString("OB_CEDAD") + "\" ";
            //strXMLData += "CTO_ID =\"" + rs.getString("CTO_ID") + "\" ";

        }
        strXMLData += ">";
        strXMLData += "</Credito>";
//        if(rs1.getStatement() != null )rs1.getStatement().close(); 
        rs1.close();
        strXMLData += "</DatosCredito>";
        return strXMLData;
    }

    public String deleteDatos(Conexion oConn) {
        String strRes = "";
        strRes = this.objDocumentacion.Borra(oConn);
        return strRes;
    }

    public String autorizado(Conexion oConn) {
        String strRes = "";

        strRes = this.objVencimiento.Agrega(oConn);
        return strRes;
    }
}
