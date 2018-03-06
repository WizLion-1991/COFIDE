/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import com.mx.siweb.erp.reportes.*;
import Tablas.NOM_CuentaContableE;
import com.mx.siweb.erp.reportes.entities.RDPG_BancosE;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.UtilXml;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author siweb
 */
public class NOM_CuentaContable {

    private Conexion oConn;
    private ArrayList<NOM_CuentaContableE> entidades;
    private int idDep;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(NOM_CuentaContable.class.getName());

    public NOM_CuentaContable(Conexion oConn, int idDep) {
        this.oConn = oConn;
        this.idDep = idDep;
        entidades = new ArrayList<NOM_CuentaContableE>();
    }

    public void Alta(String descripcion, int precepcion) {
        String stmt = "INSERT INTO rhh_departamento_cuentas (DESC_CONTA_CUENTA, PERC_ID, DP_ID) "
                + "VALUES ('" + descripcion + "'," + precepcion + " ," + idDep + " )";
        log.debug(stmt);
        oConn.runQueryLMD(stmt);
    }

    public void Baja(int idCuenta) {
        String stmt = "DELETE FROM rhh_departamento_cuentas WHERE (DEC_ID=" + idCuenta + ")";
        log.debug(stmt);
        oConn.runQueryLMD(stmt);
    }

    public void Consulta() {
        try {
            String stmt = "SELECT DEC_ID,DESC_CONTA_CUENTA,PERC_ID FROM rhh_departamento_cuentas WHERE (DP_ID=" + idDep + ")";
            log.debug(stmt);
            ResultSet rs = oConn.runQuery(stmt);
            while (rs.next()) {
                NOM_CuentaContableE e = new NOM_CuentaContableE();
                e.setId(rs.getInt("DEC_ID"));
                e.setDescripcion(rs.getString("DESC_CONTA_CUENTA"));
                e.setPercepcion(rs.getInt("PERC_ID"));
                entidades.add(e);
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getLocalizedMessage());
        }
    }
    
    public String obtenPercepcion(int prId){
        String percepcion="";
        try {
            String stmt = "SELECT PERC_DESCRIPCION FROM rhh_percepciones WHERE (PERC_ID=" + prId + ")";
            log.debug(stmt);
            ResultSet rs = oConn.runQuery(stmt);
            while (rs.next()) {
                percepcion=rs.getString("PERC_DESCRIPCION");
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getLocalizedMessage());
        }
        return percepcion;
    }

    public String GeneraXml() {
        Iterator<NOM_CuentaContableE> it = entidades.iterator();
        UtilXml util = new UtilXml();
        StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        strXML.append("<CuentasContables>");
        while (it.hasNext()) {
            NOM_CuentaContableE entidad = it.next();
            strXML.append(" <CuentaContable");
            strXML.append(" Id= \"").append(entidad.getId()).append("\" ");
            strXML.append(" Descripcion= \"").append(util.Sustituye(entidad.getDescripcion())).append("\" ");
            strXML.append(" Percecion= \"").append(util.Sustituye(obtenPercepcion(entidad.getPercepcion()))).append("\" ");
            strXML.append("/>");
        }
        strXML.append("</CuentasContables>");
        return strXML.toString();
    }
}
