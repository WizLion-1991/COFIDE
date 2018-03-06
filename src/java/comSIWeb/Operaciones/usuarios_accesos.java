/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comSIWeb.Operaciones;

/**
 * Representa la informacion de acceso de un usuario
 *
 * @author aleph_79
 */
public class usuarios_accesos extends TableMaster {

    public usuarios_accesos() {
        super("usuarios_accesos", "seg_id", "", "");
        this.Fields.put("seg_id", new Integer(0));
        this.Fields.put("id_usuario", new Integer(0));
        this.Fields.put("seg_ip_remote", "");
        this.Fields.put("seg_computer_name", "");
        this.Fields.put("seg_fecha", "");
        this.Fields.put("seg_hora", "");
        this.Fields.put("seg_tipo_ini_fin", new Integer(0));
        this.Fields.put("seg_date", new Long(0));
        this.Fields.put("seg_total_time", new Integer(0));
        this.Fields.put("seg_nombre_user", "");
        this.Fields.put("seg_notas", "");
        this.Fields.put("seg_session_id", "");
        this.Fields.put("seg_is_proveedor", new Integer(0));
        this.Fields.put("seg_is_empleado", new Integer(0));

    }
   // <editor-fold defaultstate="collapsed" desc="Propiedades">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    // </editor-fold>
}
