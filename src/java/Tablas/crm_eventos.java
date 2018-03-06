/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author juliocesar
 */
public class crm_eventos extends TableMaster {
public crm_eventos() {
super("crm_eventos", "CAMPOLLAVE","","");
this.Fields.put("EV_ID", new Integer(0));
this.Fields.put("EV_ASUNTO", "");
this.Fields.put("EV_ASIGNADO_A", new Integer(0));
this.Fields.put("EV_FECHA_INICIO", "");
this.Fields.put("EV_HORA_INICIO", "");
this.Fields.put("EV_FECHA_VENCIMIENTO", "");
this.Fields.put("EV_HORA_VENCIMIENTO", "");
this.Fields.put("EV_ESTADO", new Integer(0));
this.Fields.put("EV_ENVIAR_NOTIFICACION", new Integer(0));
this.Fields.put("EV_TIPO_ACTIVIDAD", new Integer(0));
this.Fields.put("EV_LUGAR", "");
this.Fields.put("EV_PRIORIDAD", new Integer(0));
this.Fields.put("EV_VISIBILIDAD", new Integer(0));
this.Fields.put("EV_RECORDATORIO", new Integer(0));
this.Fields.put("EV_RECURRENTE", new Integer(0));
this.Fields.put("EV_REL_TO", "");
this.Fields.put("EV_CT_ID", new Integer(0));
this.Fields.put("EV_DESCRIPCION", "");
this.Fields.put("EV_INVITADOS_LIST", "");
this.Fields.put("CAM_ID", new Integer(0));
this.Fields.put("EMP_ID", new Integer(0));
this.Fields.put("EV_TERMINAR_ANT", "");
this.Fields.put("EV_COMENTARIO", "");
}
}