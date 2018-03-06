/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author siwebmx5
 */
public class ConsultaAnticiposProv {
    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    Conexion oConn;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(EstadoCuentaCliente.class.getName());
    Fechas fecha = new Fechas();
    
    // </editor-fold>
    
   // <editor-fold defaultstate="collapsed" desc="Constructores">
    public ConsultaAnticiposProv(Conexion oConn) {
        this.oConn = oConn;
    }
    public Conexion getoConn() {
        return oConn;
    }

    public void setoConn(Conexion oConn) {
        this.oConn = oConn;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    
    public String getPagosAnticipos(int intMC_ID) {
        StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        strXML.append("<pagos>\n");
        String strSQL = "SELECT MP_ID," +
                        " MP_FOLIO," +
                        " MP_FECHA," +
                        " MP_MONEDA," +
                        " MP_TASAPESO," +
                        " MP_ABONO," +
                        " MPM_ID," +
                        " CXP_ID,"+
                        " (Select CXP_FOLIO From vta_cxpagar Where vta_cxpagar.CXP_ID = vta_mov_prov.CXP_ID) as cxpFOLIO," +                        
                        " (Select MON_DESCRIPCION From vta_monedas Where vta_monedas.MON_ID = vta_mov_prov.MP_MONEDA) as txtMONEDA," +
                        " (Select CXP_FECHA From vta_cxpagar Where vta_cxpagar.CXP_ID = vta_mov_prov.CXP_ID) as cxpFECHA" +
                        " FROM vta_mov_prov" +
                        " Where MP_USA_ANTICIPO = 1" +
                        " And MP_ABONO > 0" +
                        " And MP_ANULADO = 0" +
                        " And MP_ANTI_ID = " + intMC_ID;
        try {
            ResultSet rs = this.oConn.runQuery(strSQL, true);
            while (rs.next()) {
                strXML.append("<pago ");
                strXML.append(" MP_ID = \"").append(rs.getInt("MP_ID")).append("\" ");
                strXML.append(" MP_FECHA = \"").append(this.fecha.Formatea(rs.getString("MP_FECHA"),"/")).append("\" ");
                strXML.append(" MP_FOLIO = \"").append(rs.getString("MP_FOLIO")).append("\" ");
                strXML.append(" MP_MONEDA = \"").append(rs.getInt("MP_MONEDA")).append("\" ");
                strXML.append(" MP_TASAPESO = \"").append(rs.getDouble("MP_TASAPESO")).append("\" ");
                strXML.append(" MP_ABONO = \"").append(rs.getDouble("MP_ABONO")).append("\" ");
                strXML.append(" CXP_ID = \"").append(rs.getInt("CXP_ID")).append("\" ");
                strXML.append(" cxpFOLIO = \"").append(rs.getString("cxpFOLIO")).append("\" ");
                strXML.append(" txtMONEDA = \"").append(rs.getString("txtMONEDA")).append("\" ");
                strXML.append(" cxpFECHA = \"").append(this.fecha.Formatea(rs.getString("cxpFECHA"),"/")).append("\" ");
                strXML.append(" MPM_ID = \"").append(rs.getInt("MPM_ID")).append("\" ");
                strXML.append(" />\n");
            }
        } catch (SQLException ex) {
            this.log.error(ex.getMessage());
        }

        strXML.append("</pagos>\n");
        return strXML.toString();
    }
    
    public String getDatoProveedor(int intPV_ID) {
        StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        strXML.append("<proveedores>\n");

        String strSql = "select PV_ID,PV_RAZONSOCIAL,MON_ID "
                + " from vta_proveedor where PV_ID = " + intPV_ID;
        try {
            ResultSet rs = this.oConn.runQuery(strSql, true);
            while (rs.next()) {
                strXML.append("<proveedor ");
                strXML.append(" PV_ID = \"").append(rs.getInt("PV_ID")).append("\" ");
                strXML.append(" PV_RAZONSOCIAL = \"").append(rs.getString("PV_RAZONSOCIAL")).append("\" ");
                strXML.append(" MON_ID = \"").append(rs.getString("MON_ID")).append("\" ");
                strXML.append(" />\n");
            }

        } catch (SQLException ex) {
            this.log.error(ex.getMessage());
        }
        strXML.append("</proveedores>\n");
        return strXML.toString();
    }
    public String getAnticiposProveedor(int intPV_ID, int intMON_ID, int intUTILIZADOS, int intAnulado) {
        StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        
        strXML.append("<anticipos>\n");
        String strFiltro = "";
        if (intUTILIZADOS == 0) {
            strFiltro += " AND MP_SALDO_ANTICIPO > 0.0 ";
        }
        if (intAnulado == 0) {
            strFiltro += " AND MP_ANULADO = 0 ";
        }
        String strSQL = "Select *,"
                + " (Select MON_DESCRIPCION From vta_monedas Where vta_monedas.MON_ID = vta_mov_prov.MP_MONEDA) as txtMONEDA"
                + " From vta_mov_prov"
                + " Where "
                + " MP_ANTICIPO = 1"
                + " AND PV_ID =" + intPV_ID
                + " AND MP_MONEDA = " + intMON_ID
                + strFiltro;
        try {
            ResultSet rs = this.oConn.runQuery(strSQL, true);
            String strEstatus = "";
            while (rs.next()) {
                strEstatus = "";
                Double dblSALDO_ORIGINAL = rs.getDouble("MP_ANTICIPO_ORIGINAL");
                Double dblSALDO_ANTICIPO = rs.getDouble("MP_SALDO_ANTICIPO");
                if (dblSALDO_ORIGINAL.doubleValue() == dblSALDO_ANTICIPO.doubleValue()) {
                    strEstatus = "VIGENTE";
                }
                if (dblSALDO_ORIGINAL.doubleValue() > dblSALDO_ANTICIPO.doubleValue()) {
                    strEstatus = "EN USO";
                }
                if (dblSALDO_ANTICIPO.doubleValue() == 0.0) {
                    strEstatus = "UTILIZAD0";
                }
                strXML.append("<anticipo ");

                strXML.append(" MP_ID = \"").append(rs.getInt("MP_ID")).append("\" ");
                strXML.append(" MP_FOLIO = \"").append(rs.getString("MP_FOLIO")).append("\" ");
                strXML.append(" MP_FECHA = \"").append(this.fecha.Formatea(rs.getString("MP_FECHA"), "/")).append("\" ");
                strXML.append(" MP_TXTMONEDA = \"").append(rs.getString("txtMONEDA")).append("\" ");                
                strXML.append(" MP_ANTICIPO_ORIGINAL = \"").append(rs.getDouble("MP_ANTICIPO_ORIGINAL")).append("\" ");
                strXML.append(" MP_SALDO_ANTICIPO = \"").append(rs.getDouble("MP_SALDO_ANTICIPO")).append("\" ");
                strXML.append(" MP_ANULADO = \"").append(rs.getInt("MP_ANULADO")).append("\" ");
                if (rs.getInt("MP_ANULADO") == 1) {
                    strXML.append(" MP_TXTANULADO = \"").append("SI").append("\" ");
                    strEstatus = "-";
                } else {
                    strXML.append(" MP_TXTANULADO = \"").append("NO").append("\" ");                    
                }
                strXML.append(" MP_ESTATUS = \"").append(strEstatus).append("\" ");
                strXML.append(" MP_MONEDA = \"").append(rs.getInt("MP_MONEDA")).append("\" ");
                strXML.append(" />\n");


            }
            strXML.append("</anticipos>");
            rs.close();
        } catch (SQLException ex) {
            this.log.error(ex.getMessage());
        }

        return strXML.toString();
    }
    // </editor-fold>    
}
