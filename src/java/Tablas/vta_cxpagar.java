package Tablas;

import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Clase para la tabla de cuentas por pagar
 *
 * @author SIWEB
 */
public class vta_cxpagar extends TableMaster {
    //Listados para calcular el prorrateo

    private ArrayList<vta_movproddeta> lstEntradasEditada;
    private ArrayList<vta_movproddeta> lstEntradas;
    private ArrayList<vta_cxpagardetalle> lstDetalleCXP;
    private double dblImporteProrratear = 0;
    private static final  Logger log = LogManager.getLogger(vta_cxpagar.class.getName());
    
    public ArrayList<vta_movproddeta> getLstEntradas() {
        if (lstEntradas == null) {
            lstEntradas = new ArrayList<vta_movproddeta>();
        }
        return lstEntradas;
    }

    public void setLstEntradas(ArrayList<vta_movproddeta> lstEntradas) {
        this.lstEntradas = lstEntradas;
    }

    public ArrayList<vta_cxpagardetalle> getLstDetalleCXP() {
        if (lstDetalleCXP == null) {
            lstDetalleCXP = new ArrayList<vta_cxpagardetalle>();
        }
        return lstDetalleCXP;
    }

    public void setLstDetalleCXP(ArrayList<vta_cxpagardetalle> lstDetalleCXP) {
        this.lstDetalleCXP = lstDetalleCXP;
    }

    public double getDblImporteProrratear() {
        return dblImporteProrratear;
    }

    public void setDblImporteProrratear(double dblImporteProrratear) {
        this.dblImporteProrratear = dblImporteProrratear;
    }

    public vta_cxpagar() {
        super("vta_cxpagar", "CXP_ID", "", "");
        this.Fields.put("CXP_ID", new Integer(0));
        this.Fields.put("PV_ID", new Integer(0));
        this.Fields.put("SC_ID", new Integer(0));
        this.Fields.put("ODC_ID", new Integer(0));
        this.Fields.put("CXP_IMPORTE", new Double(0));
        this.Fields.put("CXP_IMPUESTO1", new Double(0));
        this.Fields.put("CXP_IMPUESTO2", new Double(0));
        this.Fields.put("CXP_IMPUESTO3", new Double(0));
        this.Fields.put("CXP_TOTAL", new Double(0));
        this.Fields.put("CXP_CREDITO", new Integer(0));
        this.Fields.put("CXP_ANULADO", new Integer(0));
        this.Fields.put("CXP_ANULFECHA", "");
        this.Fields.put("CXP_FECHA", "");
        this.Fields.put("CXP_FECHACREATE", "");
        this.Fields.put("CXP_HORA", "");
        this.Fields.put("CXP_USUARIO", new Integer(0));
        this.Fields.put("CXP_USUARIOANUL", new Integer(0));
        this.Fields.put("CXP_FOLIO", "");
        this.Fields.put("CXP_REFERENCIA", "");
        this.Fields.put("CXP_IDGASTO", new Integer(0));
        this.Fields.put("CXP_IDCENTROCOSTO", new Integer(0));
        this.Fields.put("CXP_NOTAS", "");
        this.Fields.put("CXP_MONEDA", new Integer(0));
        this.Fields.put("EMP_ID", new Integer(0));
        this.Fields.put("CXP_RAZONSOCIAL", "");
        this.Fields.put("CXP_CALLE", "");
        this.Fields.put("CXP_COLONIA", "");
        this.Fields.put("CXP_LOCALIDAD", "");
        this.Fields.put("CXP_ESTADO", "");
        this.Fields.put("CXP_MUNICIPIO", "");
        this.Fields.put("CXP_CP", new Integer(0));
        this.Fields.put("CXP_TASA1", new Double(0));
        this.Fields.put("CXP_TASA2", new Double(0));
        this.Fields.put("CXP_TASA3", new Double(0));
        this.Fields.put("CXP_DIASCREDITO", new Integer(0));
        this.Fields.put("CXP_DESCUENTO", new Double(0));
        this.Fields.put("CXP_COSTO", new Double(0));
        this.Fields.put("CXP_NUMERO", new Integer(0));
        this.Fields.put("CXP_NUMEROINT", new Integer(0));
        this.Fields.put("CXP_RETISR", new Double(0));
        this.Fields.put("CXP_RETIVA", new Double(0));
        this.Fields.put("CXP_NETO", new Double(0));
        this.Fields.put("CXP_NOTASPIE", "");
        this.Fields.put("CXP_NUMPEDI", "");
        this.Fields.put("CXP_ADUANA", "");
        this.Fields.put("CXP_CONDPAGO", "");
        this.Fields.put("CXP_USO_IEPS", new Integer(0));
        this.Fields.put("CXP_TASA_IEPS", new Integer(0));
        this.Fields.put("CXP_IMPORTE_IEPS", new Double(0));
        this.Fields.put("TI_ID", new Integer(0));
        this.Fields.put("TI_ID2", new Integer(0));
        this.Fields.put("TI_ID3", new Integer(0));
        this.Fields.put("CXP_LPRECIOS", new Integer(0));
        this.Fields.put("CXP_NUMINT", "");
        this.Fields.put("CXP_TIPOCOMP", new Integer(0));
        this.Fields.put("CXP_US_MOD", new Integer(0));
        this.Fields.put("CXP_FECHAPEDI", "");
        this.Fields.put("CXP_AUTORIZA", new Integer(0));
        this.Fields.put("PED_ID", new Integer(0));
        this.Fields.put("PED_COD", "");
        this.Fields.put("CXP_FECHA_CONFIRMA", "");
        this.Fields.put("CXP_FECHA_AUTORIZA", "");
        this.Fields.put("CXP_HORA_AUTORIZA", "");
        this.Fields.put("CXP_FECHA_PROVISION", "");
        this.Fields.put("CXP_USUARIO_AUTORIZA", new Integer(0));
        this.Fields.put("CXP_EXEC_INTER_CP", new Integer(0));
        this.Fields.put("CXP_UUID", "");
    }

    /**
     * Obtiene todas las cuentas por pagar de un pedimento
     *
     * @param intPED_ID Es el id del pedimento
     * @param oConn Es la conexión
     * @return Regresa un ArrayList con las cuentas por pagar
     */
    public ArrayList<vta_cxpagar> getMovCXP(int intPED_ID, Conexion oConn) {
        ArrayList<vta_cxpagar> lstCXPagar = new ArrayList<vta_cxpagar>();
        String strSql = "select CXP_ID from vta_cxpagar where PED_ID =  " + intPED_ID;
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                int intCXP_IDTmp = rs.getInt("CXP_ID");
                vta_cxpagar cxpagarTmp = new vta_cxpagar();
                cxpagarTmp.ObtenDatos(intCXP_IDTmp, oConn);
                lstCXPagar.add(cxpagarTmp);
                //Inicializamos listado de detalle
                cxpagarTmp.getLstDetalleCXP();
                //Validamos si es una cuenta por pagar por entrada de mercancia
                //para obtener su detalle
                if (cxpagarTmp.getFieldInt("ODC_ID") != 0) {
                    cxpagarTmp.getMovAlmacen(oConn);
                }
                //Obtenemos el detalle de la cuenta por pagar
                strSql = "select CXPD_ID from vta_cxpagardetalle where CXP_ID =  " + intCXP_IDTmp;
                ResultSet rs2 = oConn.runQuery(strSql, true);
                while (rs2.next()) {
                    int intCXPD_ID = rs2.getInt("CXPD_ID");
                    vta_cxpagardetalle deta = new vta_cxpagardetalle();
                    deta.ObtenDatos(intCXPD_ID, oConn);
                    cxpagarTmp.lstDetalleCXP.add(deta);
                }
//                if(rs2.getStatement() != null )rs2.getStatement().close(); 
                rs2.close();
            }
            //if(rs.getStatement() != null )rs.getStatement().close(); 
            rs.close();
        } catch (SQLException ex) {
           log.error(ex.getMessage());
        }
        return lstCXPagar;
    }

    /**
     * Carga en la cuenta por pagar una lista con los movimientos de almacen que
     * le dieron origen
     *
     * @param oConn Es la conexion
     */
    public void getMovAlmacen(Conexion oConn) {
        getLstEntradas();
        //Consultamos todos los movimientos de entrada vinculados a esta cuenta por pagar
        String strSql = "select vta_movproddeta.MPD_ID from vta_cxpagardetalle,vta_movproddeta "
                + " where vta_cxpagardetalle.MPD_ID=vta_movproddeta.MPD_ID and vta_cxpagardetalle.CXP_ID = " + this.getFieldInt("CXP_ID");
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                int intMPD_IDTmp = rs.getInt("MPD_ID");
                vta_movproddeta movAlmTmp = new vta_movproddeta();
                movAlmTmp.ObtenDatos(intMPD_IDTmp, oConn);
                this.lstEntradas.add(movAlmTmp);
            }
            //if(rs.getStatement() != null )rs.getStatement().close(); 
            rs.close();
        } catch (SQLException ex) {
           log.error(ex.getMessage());
        }
    }

    /**
     * Nos regresa el nombre del proveedor
     *
     * @param intPV_ID Es el id del proveedor
     * @param oConn Es la conexion
     * @return Regresa el nombre
     */
    public String getNomProveedor(int intPV_ID, Conexion oConn) {
        String strNomProv = "";
        //Consultamos todos los movimientos de entrada vinculados a esta cuenta por pagar
        String strSql = "select PV_RAZONSOCIAL from vta_proveedor "
                + " where PV_ID = " + intPV_ID;
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strNomProv = rs.getString("PV_RAZONSOCIAL");
            }
            //if(rs.getStatement() != null )rs.getStatement().close(); 
            rs.close();
        } catch (SQLException ex) {
           log.error(ex.getMessage());
        }
        return strNomProv;
    }
    //Lista Agregada Para mostrar en Simulacion

    public ArrayList<vta_movproddeta> getlstEntradasEditada() {
        if (lstEntradasEditada == null) {
            lstEntradasEditada = new ArrayList<vta_movproddeta>();
        }
        return lstEntradasEditada;
    }

    public ArrayList<vta_movproddeta> CalculaEntradaEditada(ArrayList<vta_movproddeta> Entrada) {
//        System.out.println("Entre al metodo Para editar la informacion");
        this.lstEntradas = Entrada;
        this.lstEntradasEditada = new ArrayList<vta_movproddeta>();
        
        Double numero = 0.0;
        Double dblAcumuladoCantidad = 0.0;
        //Double dblAcumuladoImporte = 0.0;
        Double dblAcumuladoProrrateo = 0.0;
        String ID_nuevo = "";
        String ID_anterior = "";
        String PR_ID_Anterior = "";
        String PR_ID_Nuevo ="";
        boolean i = true;
        Iterator<vta_movproddeta> eEA = this.lstEntradas.iterator();
        vta_movproddeta Anterior = new vta_movproddeta();
//        System.out.println("Entramos al While");
        while (eEA.hasNext()) {
//            System.out.println("Empezamos el ciclo");
            vta_movproddeta deta = eEA.next();
            ID_nuevo = deta.getFieldString("MP_ID");
            numero = deta.getFieldDouble("MPD_ENTRADAS");
            //Importe = deta.getFieldDouble("MPD_COSTO");
            PR_ID_Nuevo = deta.getFieldString("PR_ID");
//            System.out.println("Checamos si es la primera vez que entramos");
            
            if (i) {
                Anterior = deta;
                ID_anterior = ID_nuevo;
                PR_ID_Anterior = PR_ID_Nuevo;
                
                i = false;
            }
//            System.out.println("Checamos si el ID sin similares");
            //!ID_nuevo.equals(ID_anterior) || 
            if (!PR_ID_Nuevo.equals(PR_ID_Anterior)) {
//                System.out.println("Si no lo son hacemos el agregado a la lista");
                Anterior.setFieldDouble("MPD_ENTRADAS", dblAcumuladoCantidad);
                //Anterior.setFieldDouble("MPD_COSTO", dblAcumuladoImporte);

                //dblAcumuladoProrrateo = dblAcumuladoCantidad * dblAcumuladoImporte;
                Anterior.setFieldDouble("MPD_COSTO_PRORRATEO", dblAcumuladoProrrateo);
                
//                System.out.println("Agregamos a la lista");
                this.lstEntradasEditada.add(Anterior);
//                System.out.println("Terminamos de agrega a la lista");
                
                ID_anterior = ID_nuevo;
                dblAcumuladoCantidad = numero;
                //dblAcumuladoImporte = Importe;
                Anterior = deta;
                PR_ID_Anterior = PR_ID_Nuevo;
                
                log.debug("Terminamos de cargar los datoss");
            } else {
//                System.out.println("Si son iguales, solamente hacemos una suma");
                dblAcumuladoCantidad += numero;
                //dblAcumuladoImporte += Importe;
            }

        }
//        System.out.println("Terminamos el While");
        this.lstEntradasEditada.add(Anterior);
        return this.lstEntradasEditada;
    }

}
