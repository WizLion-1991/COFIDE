/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide.entidades;

import comSIWeb.Operaciones.TableMaster;

/**
 * Representa los cursos en el CRM de COFIDE
 *
 * @author ZeusSIWEB
 */
public class CofideCursos extends TableMaster {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    public CofideCursos() {
        super("cofide_cursos", "CC_CURSO_ID", "", "");
        this.Fields.put("CC_CURSO_ID", new Integer(0));
        this.Fields.put("CC_CLAVES", "");
        this.Fields.put("CC_SEDE_ID", new Integer(0));
        this.Fields.put("CC_SEDE", "");
        this.Fields.put("CC_FECHA_INICIAL", "");
        this.Fields.put("CC_FECHA_FINAL", "");
        this.Fields.put("CC_HR_EVENTO_INI", "");
        this.Fields.put("CC_HR_EVENTO_FIN", "");
        this.Fields.put("CC_DURACION_HRS", new Integer(0));
        this.Fields.put("CC_ALIMENTO", "");
        this.Fields.put("CC_DESAYUNO", new Integer(0));
        this.Fields.put("CC_COMIDA", new Integer(0));
        this.Fields.put("CC_MONTAJE", new Integer(0));
        this.Fields.put("CC_GARANTIA", "");
        this.Fields.put("CC_NOMBRE_CURSO", "");
        this.Fields.put("CC_INSTRUCTOR_ID", new Integer(0));
        this.Fields.put("CC_INSTRUCTOR", "");
        this.Fields.put("CC_PRECIO_UNIT", "");
        this.Fields.put("CC_PRECIO_IVA", "");
        this.Fields.put("CC_COSTO_HORA", "");
        this.Fields.put("CC_LIMITE", new Integer(0));
        this.Fields.put("CC_VTA_ESTIMADA", "");
        this.Fields.put("CC_SESION", "");
        this.Fields.put("CC_ALIAS_FEC", "");
        this.Fields.put("CC_ACTIVO", "");
        this.Fields.put("CC_CONFIRMAR", new Integer(0));
        this.Fields.put("CC_AUTORIZAR", new Integer(0));
        this.Fields.put("CC_ESTADOS", "");
        this.Fields.put("CC_ENCABEZADO", "");
        this.Fields.put("CC_DETALLE", "");
        this.Fields.put("CC_EXPOSITOR", "");
        this.Fields.put("CC_TEMARIO", "");
        this.Fields.put("CC_INSCRITOS", new Integer(0));
        this.Fields.put("CC_ESTATUS", new Integer(0));
        this.Fields.put("CC_N_MATERIALES", new Integer(0));
        this.Fields.put("CC_IS_PRESENCIAL", new Integer(0));
        this.Fields.put("CC_PRECIO_PRES", "");
        this.Fields.put("CC_IVA_PRES", "");
        this.Fields.put("CC_IS_ONLINE", new Integer(0));
        this.Fields.put("CC_PRECIO_ON",0);
        this.Fields.put("CC_IVA_ON",0);
        this.Fields.put("CC_IS_VIDEO", new Integer(0));
        this.Fields.put("CC_PRECIO_VID",0);
        this.Fields.put("CC_IVA_VID",0);
        this.Fields.put("CC_PRES_ONLINE", new Integer(0));
        this.Fields.put("CC_PRECIO_PREON",0);
        this.Fields.put("CC_IVA_PREON",0);
        this.Fields.put("CC_HRS_NDPC", "");
        this.Fields.put("CC_IS_DIPLOMADO", new Integer(0));
        this.Fields.put("CC_IS_SEMINARIO", new Integer(0));
        this.Fields.put("CC_TEMPLATE1", new Integer(0));
        this.Fields.put("CC_TEMPLATE2", new Integer(0));
        this.Fields.put("CC_TEMPLATE3", new Integer(0));
        this.Fields.put("CC_MASIVOS", "");
        this.Fields.put("CC_MAILGROUP", "");
        this.Fields.put("CC_CURSO_CONFIRM", new Integer(0));
        this.Fields.put("CC_ID_SEMINARIO_DIPLOMADO", new Integer(0));
        this.Fields.put("CC_TIPO_TEMPLATE", new Integer(0));
        this.Fields.put("CC_CONFIRMA_MAIL", new Integer(0));
        this.Fields.put("CC_URL_MATERIAL", "");
        this.Fields.put("CC_ID_WEB", new Integer(0));
        this.Fields.put("CC_GOOGLEMAPS", "");
        this.Fields.put("CC_MATERIAL", "");
        this.Fields.put("CC_VIDEO", "");
        this.Fields.put("CC_PRIORIDAD", new Integer(0));
        this.Fields.put("CC_REQUISITO", "");
        this.Fields.put("CC_OBJETIVO", "");
        this.Fields.put("CC_DIRIGIDO", "");
        this.Fields.put("CC_TIPO_ALIMENTO", new Integer(0));
        this.Fields.put("CC_ALIAS", "");
        this.Fields.put("CC_SALON", "");
        this.Fields.put("CC_HORA", "");
        this.Fields.put("CC_FICHA_TECNICA", new Integer(0));
        this.Fields.put("CC_CONFIRM_INSTR", new Integer(0));
        this.Fields.put("CC_PUBLICADO", new Integer(0));
        this.Fields.put("CC_PROGRAMAR", new Integer(0));
        this.Fields.put("CC_CONFIRMA_MG", new Integer(0));
        this.Fields.put("CC_FECHA_PROX", "");
        this.Fields.put("CC_IMPORTE_AJUSTADO", new Double(0));
        this.Fields.put("CC_FECHA_PAGO_INST", "");
        this.Fields.put("CC_OBSERVACIONES", "");
        this.Fields.put("CC_FACTURA_INST", "");
        this.Fields.put("CC_SEDE_ID2", new Integer(0));
        this.Fields.put("CC_FECHA_INICIAL2", "");
        this.Fields.put("CC_HR_EVENTO_INI2", "");
        this.Fields.put("CC_HR_EVENTO_FIN2", "");
        this.Fields.put("CC_DURACION_HRS2", new Integer(0));
        this.Fields.put("CC_ALIMENTO2", "");
        this.Fields.put("CC_SESION2", "");
        this.Fields.put("CC_ALIAS_FEC2", "");
        this.Fields.put("CC_TIPO_ALIMENTO2", new Integer(0));
        this.Fields.put("CC_SALON2", "");
        this.Fields.put("CC_SEDE_ID3", new Integer(0));
        this.Fields.put("CC_FECHA_INICIAL3", "");
        this.Fields.put("CC_HR_EVENTO_INI3", "");
        this.Fields.put("CC_HR_EVENTO_FIN3", "");
        this.Fields.put("CC_DURACION_HRS3", new Integer(0));
        this.Fields.put("CC_ALIMENTO3", "");
        this.Fields.put("CC_SESION3", "");
        this.Fields.put("CC_ALIAS_FEC3", "");
        this.Fields.put("CC_TIPO_ALIMENTO3", new Integer(0));
        this.Fields.put("CC_SALON3", "");
        this.Fields.put("CC_ENVIO_MAIL", new Integer(0));
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    // </editor-fold>
}
