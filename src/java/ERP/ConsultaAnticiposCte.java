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
public class ConsultaAnticiposCte {
    // <editor-fold defaultstate="collapsed" desc="Propiedades">

    Conexion oConn;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(EstadoCuentaCliente.class.getName());
    Fechas fecha = new Fechas();
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">

    public Conexion getoConn() {
        return oConn;
    }

    public void setoConn(Conexion oConn) {
        this.oConn = oConn;
    }

    public ConsultaAnticiposCte(Conexion oConn) {
        this.oConn = oConn;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">

    public String getPagosAnticipos(int intMC_ID) {
        StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        strXML.append("<pagos>\n");
        String strSQL = "SELECT MC_ID," +
                        " MC_FOLIO," +
                        " MC_FECHA," +
                        " MC_MONEDA," +
                        " MC_TASAPESO," +
                        " MC_ABONO," +
                        " FAC_ID,"+
                        " MCM_ID," +
                        " (Select FAC_FOLIO From vta_facturas Where vta_facturas.FAC_ID = vta_mov_cte.FAC_ID) as facFOLIO," +                        
                        " (Select MON_DESCRIPCION From vta_monedas Where vta_monedas.MON_ID = vta_mov_cte.MC_MONEDA) as txtMONEDA," +
                        " (Select FAC_FECHA From vta_facturas Where vta_facturas.FAC_ID = vta_mov_cte.FAC_ID) as facFECHA" +
                        " FROM vta_mov_cte" +
                        " Where MC_USA_ANTICIPO = 1" +
                        " And MC_ABONO > 0" +
                        " And MC_ANULADO = 0" +
                        " And MC_ANTI_ID = " + intMC_ID;
        try {
            ResultSet rs = this.oConn.runQuery(strSQL, true);
            while (rs.next()) {
                strXML.append("<pago ");
                strXML.append(" MC_ID = \"").append(rs.getInt("MC_ID")).append("\" ");
                strXML.append(" MC_FECHA = \"").append(this.fecha.Formatea(rs.getString("MC_FECHA"),"/")).append("\" ");
                strXML.append(" MC_FOLIO = \"").append(rs.getString("MC_FOLIO")).append("\" ");
                strXML.append(" MC_MONEDA = \"").append(rs.getInt("MC_MONEDA")).append("\" ");
                strXML.append(" MC_TASAPESO = \"").append(rs.getDouble("MC_TASAPESO")).append("\" ");
                strXML.append(" MC_ABONO = \"").append(rs.getDouble("MC_ABONO")).append("\" ");
                strXML.append(" FAC_ID = \"").append(rs.getInt("FAC_ID")).append("\" ");
                strXML.append(" facFOLIO = \"").append(rs.getString("facFOLIO")).append("\" ");
                strXML.append(" txtMONEDA = \"").append(rs.getString("txtMONEDA")).append("\" ");
                strXML.append(" facFECHA = \"").append(this.fecha.Formatea(rs.getString("facFECHA"),"/")).append("\" ");
                strXML.append(" MCM_ID = \"").append(rs.getInt("MCM_ID")).append("\" ");
                strXML.append(" />\n");
            }
        } catch (SQLException ex) {
            this.log.error(ex.getMessage());
        }

        strXML.append("</pagos>\n");
        return strXML.toString();
    }

    public String getDatoCliente(int intCT_ID) {
        StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        strXML.append("<clientes>\n");

        String strSql = "select CT_ID,CT_RAZONSOCIAL,MON_ID "
                + " from vta_cliente where CT_ID = " + intCT_ID;
        try {
            ResultSet rs = this.oConn.runQuery(strSql, true);
            while (rs.next()) {
                strXML.append("<cliente ");
                strXML.append(" CT_ID = \"").append(rs.getInt("CT_ID")).append("\" ");
                strXML.append(" CT_RAZONSOCIAL = \"").append(rs.getString("CT_RAZONSOCIAL")).append("\" ");
                strXML.append(" MON_ID = \"").append(rs.getString("MON_ID")).append("\" ");
                strXML.append(" />\n");
            }

        } catch (SQLException ex) {
            this.log.error(ex.getMessage());
        }
        strXML.append("</clientes>\n");
        return strXML.toString();
    }

    public String getAnticiposCliente(int intCT_ID, int intMON_ID, int intUTILIZADOS, int intAnulado) {
        StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        
        strXML.append("<anticipos>\n");
        String strFiltro = "";
        if (intUTILIZADOS == 0) {
            strFiltro += " AND MC_SALDO_ANTICIPO > 0.0 ";
        }
        if (intAnulado == 0) {
            strFiltro += " AND MC_ANULADO = 0 ";
        }
        String strSQL = "Select *,"
                + " (Select MON_DESCRIPCION From vta_monedas Where vta_monedas.MON_ID = vta_mov_cte.MC_MONEDA) as txtMONEDA"
                + " From vta_mov_cte"
                + " Where "
                + " MC_ANTICIPO = 1"
                + " AND CT_ID =" + intCT_ID
                + " AND MC_MONEDA = " + intMON_ID
                + strFiltro;
        try {
            ResultSet rs = this.oConn.runQuery(strSQL, true);
            String strEstatus = "";
            while (rs.next()) {
                strEstatus = "";
                Double dblSALDO_ORIGINAL = rs.getDouble("MC_ANTICIPO_ORIGINAL");
                Double dblSALDO_ANTICIPO = rs.getDouble("MC_SALDO_ANTICIPO");
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

                strXML.append(" MC_ID = \"").append(rs.getInt("MC_ID")).append("\" ");
                strXML.append(" MC_FOLIO = \"").append(rs.getString("MC_FOLIO")).append("\" ");
                strXML.append(" MC_FECHA = \"").append(this.fecha.Formatea(rs.getString("MC_FECHA"), "/")).append("\" ");
                strXML.append(" MC_TXTMONEDA = \"").append(rs.getString("txtMONEDA")).append("\" ");                
                strXML.append(" MC_ANTICIPO_ORIGINAL = \"").append(rs.getDouble("MC_ANTICIPO_ORIGINAL")).append("\" ");
                strXML.append(" MC_SALDO_ANTICIPO = \"").append(rs.getDouble("MC_SALDO_ANTICIPO")).append("\" ");
                strXML.append(" MC_ANULADO = \"").append(rs.getInt("MC_ANULADO")).append("\" ");
                if (rs.getInt("MC_ANULADO") == 1) {
                    strXML.append(" MC_TXTANULADO = \"").append("SI").append("\" ");
                    strEstatus = "-";
                } else {
                    strXML.append(" MC_TXTANULADO = \"").append("NO").append("\" ");                    
                }
                strXML.append(" MC_ESTATUS = \"").append(strEstatus).append("\" ");
                strXML.append(" MC_MONEDA = \"").append(rs.getInt("MC_MONEDA")).append("\" ");
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
