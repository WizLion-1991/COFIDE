/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mx.siweb.estadocuenta;

import java.util.ArrayList;

/**
 *Entidad que representa una lista de tasas de cambio
 * @author siwebmx5
 */
public class lstTasaCambio {
    ArrayList<TasaCambio> Lista;

    public lstTasaCambio() {    
        this.Lista = new ArrayList<TasaCambio>();
    }

    public ArrayList<TasaCambio> getLista() {
        return Lista;
    }

    public void setLista(ArrayList<TasaCambio> Lista) {
        this.Lista = Lista;
    }
    
    public TasaCambio Regresa(int i)
    {
        TasaCambio regresa;
        regresa = this.Lista.get(i);
        return regresa;
    }
    
    public void Agrega(TasaCambio obj)
    {
        this.Lista.add(obj);
    }
    
    public int Longitud()
    {
        return this.Lista.size();
    }
}
