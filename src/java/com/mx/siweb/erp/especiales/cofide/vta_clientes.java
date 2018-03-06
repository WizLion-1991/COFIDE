package com.mx.siweb.erp.especiales.cofide;
// Generated 14/02/2010 11:10:21 AM by Hibernate Tools 3.2.1.GA

import comSIWeb.Operaciones.TableMaster;

/**
 * Representa un Cliente
 */
public class vta_clientes extends TableMaster {

    public vta_clientes() {
        super("vta_cliente", "CT_ID", "", "");
        this.Fields.put("CT_ID", new Integer(0));
        this.Fields.put("CT_RAZONSOCIAL", "");
        this.Fields.put("CT_RFC", "");
        this.Fields.put("CT_CALLE", "");
        this.Fields.put("CT_COLONIA", "");
        this.Fields.put("CT_LOCALIDAD", "");
        this.Fields.put("CT_TXTIVA", "");
        this.Fields.put("CT_TXTIVAAGUA", "");
        this.Fields.put("CT_MUNICIPIO", "");
        this.Fields.put("CT_ESTADO", "");
        this.Fields.put("CT_CP", "");
        this.Fields.put("CT_TELEFONO1", "");
        this.Fields.put("CT_TELEFONO2", "");
        this.Fields.put("CT_CONTACTO1", "");
        this.Fields.put("CT_CONTACTO2", "");
        this.Fields.put("CT_FOLIO", new Integer(0));
        this.Fields.put("CT_NUMCEROS", new Integer(0));
        this.Fields.put("CT_SALDO", new Double(0));
        this.Fields.put("CT_EMAIL1", "");
        this.Fields.put("CT_EMAIL2", "");
        this.Fields.put("CT_EMAIL3", "");
        this.Fields.put("CT_EMAIL4", "");
        this.Fields.put("CT_EMAIL5", "");
        this.Fields.put("CT_NUMERO", "");
        this.Fields.put("CT_NUMINT", "");
        this.Fields.put("CT_LPRECIOS", new Integer(0));
        this.Fields.put("CT_DIASCREDITO", new Integer(0));
        this.Fields.put("CT_MONTOCRED", new Double(0));
        this.Fields.put("CT_FECHAREG", "");
        this.Fields.put("CT_IDIOMA", new Integer(0));
        this.Fields.put("SC_ID", new Integer(0));
        this.Fields.put("CT_PASSWORD", "");
        this.Fields.put("CT_DESCUENTO", new Double(0));
        this.Fields.put("CT_VENDEDOR", "");
        this.Fields.put("CT_CONTAVTA", "");
        this.Fields.put("CT_CONTAPAG", "");
        this.Fields.put("CT_CONTANC", "");
        this.Fields.put("CT_FECHAULTINT", "");
        this.Fields.put("CT_HORAULTINT", "");
        this.Fields.put("CT_FECHAEXIT", "");
        this.Fields.put("CT_HORAEXIT", "");
        this.Fields.put("CT_FALLIDOS", new Integer(0));
        this.Fields.put("CT_NOTAS", "");
        this.Fields.put("CT_EXITOSOS", new Integer(0));
        this.Fields.put("CT_CATEGORIA1", new Integer(0));
        this.Fields.put("CT_CATEGORIA2", new Integer(0));
        this.Fields.put("CT_CATEGORIA3", new Integer(0));
        this.Fields.put("CT_CATEGORIA4", new Integer(0));
        this.Fields.put("CT_CATEGORIA5", new Integer(0));
        this.Fields.put("CT_TIPOPERS", new Integer(0));
        this.Fields.put("CT_USOIMBUEBLE", "");
        this.Fields.put("CT_TIPOFAC", new Integer(0));
        this.Fields.put("CT_TIT_CONT1", "");
        this.Fields.put("CT_TIT_CONT2", "");
        this.Fields.put("CT_CONT_AP1", "");
        this.Fields.put("CT_CONT_AP2", "");
        this.Fields.put("CT_CONT_AM1", "");
        this.Fields.put("CT_CONT_AM2", "");
        this.Fields.put("EMP_ID", new Integer(0));
        this.Fields.put("CT_CONTACTE", "");
        this.Fields.put("CT_CUENTAVTACRED", "");
        this.Fields.put("CT_UPLINE", new Integer(0));
        this.Fields.put("CT_CONTACTO", "");
        this.Fields.put("CT_FECHAULTIMOCONTACTO", "");
        this.Fields.put("CT_ARMADOINI", new Integer(0));
        this.Fields.put("CT_ARMADOFIN", new Integer(0));
        this.Fields.put("CT_ARMADONUM", new Integer(0));
        this.Fields.put("CT_ARMADODEEP", new Integer(0));
        this.Fields.put("CT_SPONZOR", new Integer(0));
        this.Fields.put("CT_LADO", "");
        this.Fields.put("CT_IS_LOGGED", new Integer(0));
        this.Fields.put("CT_LAST_ACT", "");
        this.Fields.put("CT_LASTSESSIONID", "");
        this.Fields.put("CT_LASTIPADDRESS", "");
        this.Fields.put("CT_LAST_TIME", "");
        this.Fields.put("CT_LAST_TIME_FAIL", "");
        this.Fields.put("CT_IS_DISABLED", new Integer(0));
        this.Fields.put("CT_CTABANCO1", "");
        this.Fields.put("CT_CTABANCO2", "");
        this.Fields.put("CT_CTATARJETA", "");
        this.Fields.put("CT_NUMPREDIAL", "");
        this.Fields.put("PA_ID", new Integer(0));
        this.Fields.put("CT_ACTIVO", new Integer(0));
        this.Fields.put("CT_RAZONCOMERCIAL", "");
        this.Fields.put("CT_CATEGORIA6", new Integer(0));
        this.Fields.put("CT_CATEGORIA7", new Integer(0));
        this.Fields.put("CT_CATEGORIA8", new Integer(0));
        this.Fields.put("CT_CATEGORIA9", new Integer(0));
        this.Fields.put("CT_CATEGORIA10", new Integer(0));
        this.Fields.put("MON_ID", new Integer(0));
        this.Fields.put("TI_ID", new Integer(0));
        this.Fields.put("TTC_ID", new Integer(0));
        this.Fields.put("CT_RBANCARIA1", "");
        this.Fields.put("CT_RBANCARIA2", "");
        this.Fields.put("CT_RBANCARIA3", "");
        this.Fields.put("CT_BANCO1", new Integer(0));
        this.Fields.put("CT_BANCO2", new Integer(0));
        this.Fields.put("CT_BANCO3", new Integer(0));
        this.Fields.put("CT_METODODEPAGO", "");
        this.Fields.put("CT_FORMADEPAGO", "");
        this.Fields.put("CT_FECHA_NAC", "");
        this.Fields.put("CT_NOMBRE", "");
        this.Fields.put("CT_APATERNO", "");
        this.Fields.put("CT_AMATERNO", "");
        this.Fields.put("CT_PPUNTOS", new Double(0));
        this.Fields.put("CT_PNEGOCIO", new Double(0));
        this.Fields.put("CT_GPUNTOS", new Double(0));
        this.Fields.put("CT_GNEGOCIO", new Double(0));
        this.Fields.put("CT_COMISION", new Double(0));
        this.Fields.put("CT_NIVELRED", new Integer(0));
        this.Fields.put("MPE_ID", new Integer(0));
        this.Fields.put("CT_CONTEO_HIJOS", new Integer(0));
        this.Fields.put("CT_CONTEO_HIJOS_ACTIVOS", new Integer(0));
        this.Fields.put("CT_CONTEO_INGRESOS", new Integer(0));
        this.Fields.put("CT_RLEGAL", "");
        this.Fields.put("CT_FIADOR", "");
        this.Fields.put("CT_F1DIRECCION", "");
        this.Fields.put("CT_F1IFE", "");
        this.Fields.put("CT_FIADOR2", "");
        this.Fields.put("CT_F2DIRECCION", "");
        this.Fields.put("CT_F2IFE", "");
        this.Fields.put("CT_FIADOR3", "");
        this.Fields.put("CT_F3DIRECCION", "");
        this.Fields.put("CT_F3IFE", "");
        this.Fields.put("CT_CHANGE_PASSWRD", new Integer(0));
        this.Fields.put("CT_CTA_BANCO1", "");
        this.Fields.put("CT_CTA_BANCO2", "");
        this.Fields.put("CT_CTA_SUCURSAL1", "");
        this.Fields.put("CT_CTA_SUCURSAL2", "");
        this.Fields.put("CT_CTA_CLABE1", "");
        this.Fields.put("CT_CTA_CLABE2", "");
        this.Fields.put("CT_CONTACTE_COMPL", "");
        this.Fields.put("CT_CTA_ANTICIPO", "");
        this.Fields.put("CT_CTACTE_COMPL_ANTI", "");
        this.Fields.put("CT_CONTA_RET_ISR", "");
        this.Fields.put("CT_CONTA_RET_IVA", "");
        this.Fields.put("TI2_ID", new Integer(0));
        this.Fields.put("TI3_ID", new Integer(0));
        this.Fields.put("CT_BANCO_CTA1", "");
        this.Fields.put("CT_BANCO_CTA2", "");
        this.Fields.put("CT_CLABE1", "");
        this.Fields.put("CT_CLABE2", "");
        this.Fields.put("CT_CRED_ELECTOR", "");
        this.Fields.put("CT_ES_PROSPECTO", new Integer(0));
        this.Fields.put("CT_FECHA_CONTACTO", "");
        this.Fields.put("CAT_MED_CONT_ID", new Integer(0));
        this.Fields.put("CT_UBICACION_GOOGLE", "");
        this.Fields.put("CT_FACEBOOK", "");
        this.Fields.put("CT_PAGINA_WEB", "");
        this.Fields.put("CT_POR_CIERRE", new Integer(0));
        this.Fields.put("EP_ID", new Integer(0));
        this.Fields.put("CAM_ID", new Integer(0));
        this.Fields.put("CT_CURP", "");
        this.Fields.put("CT_ID_INVITO", new Integer(0));
        this.Fields.put("CT_NOMBRE_INVITO", "");
        this.Fields.put("CT_FECHA_ACTIVA", "");
        this.Fields.put("CT_ENVIO_FACTURA", new Integer(0));
        this.Fields.put("CT_BCO_CTA_BNCA", new Integer(0));
        this.Fields.put("CT_EMAIL6", "");
        this.Fields.put("CT_EMAIL7", "");
        this.Fields.put("CT_EMAIL8", "");
        this.Fields.put("CT_EMAIL9", "");
        this.Fields.put("CT_EMAIL10", "");
        this.Fields.put("CT_CIUDAD", "");
        this.Fields.put("COFIDE_BASE", new Integer(0));
        this.Fields.put("CT_SEDE", "");
        this.Fields.put("CT_ID_CTE", new Integer(0));
        this.Fields.put("CT_PROCESO_COMPAQ", new Integer(0));
        this.Fields.put("CT_CLAVE_DDBB", "");
        this.Fields.put("CT_GIRO", "");
        this.Fields.put("CT_AREA", "");
        this.Fields.put("CT_MAILMES", new Integer(0));
        this.Fields.put("CT_CONMUTADOR", "");
        this.Fields.put("CT_EMAGISTER", new Integer(0));
        this.Fields.put("CT_AAA", new Integer(0));
        this.Fields.put("CT_MKT", new Integer(0));
        this.setBolGetAutonumeric(true);
    }
}
