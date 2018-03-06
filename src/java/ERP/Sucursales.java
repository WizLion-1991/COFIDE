/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import Tablas.vta_sucursal_master_as;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;



/**
 *
 * @author siwebmx5
 */
public class Sucursales {
    vta_sucursal_master_as SMA;

    public Sucursales(VariableSession varSesiones) {
        this.SMA = new vta_sucursal_master_as();
    }

    public vta_sucursal_master_as getSMA() {
        return SMA;
    }

    public void setSMA(vta_sucursal_master_as SMA) {
        this.SMA = SMA;
    }
    
    public String AltaDatos(Conexion oConn)
    {
        String strRes = "";
        strRes = this.SMA.Agrega(oConn);
        return strRes;
    }    
    
    public String updateDatos(Conexion oConn)
    {
        String strRes="";
        strRes=this.SMA.Modifica(oConn);
        return strRes;
    }
    public String deleteDatos(Conexion oConn)
    {
        String strRes="";
        strRes=this.SMA.Borra(oConn);
        return strRes;
    }
    
    
}
