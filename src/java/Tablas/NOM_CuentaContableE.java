/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Tablas;

/**
 *
 * @author siweb
 */
public class NOM_CuentaContableE {
    
    private int Id;
    private String descripcion;
    private int percepcion;
    private int deduccion;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPercepcion() {
        return percepcion;
    }

    public void setPercepcion(int percepcion) {
        this.percepcion = percepcion;
    }

    public int getDeduccion() {
        return deduccion;
    }

    public void setDeduccion(int deduccion) {
        this.deduccion = deduccion;
    }
    
    public int getId() {
        return Id;
    }
    
    public void setId(int Id) {
        this.Id = Id;
    }

}
