/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mx.siweb.erp.restful.entity;

/**
 *
 * @author siweb
 */
public class SugerenciasDeta {
    
    private int sug_id;
    private String fecha;
    private String dirigido;
    private String correo;
    private String comentario;
    private String estatus;
    private String solucion;
    private int ct_id;
    private String Codigo;
    
    public String getCodigo() {
      return Codigo;
    }

    public void setCodigo(String Codigo) {
      this.Codigo = Codigo;
    }

    public int getCt_id(){
        return ct_id;
    }
    
    public void setCt_id(int Ct_id){
        this.ct_id = Ct_id;
    }
    
    
    public int getsug_id() {
        return sug_id;
    }

    public void setsug_id(int sug_id) {
        this.sug_id = sug_id;
    }

    public String getfecha() {
        return fecha;
    }

    public void setfecha(String fecha) {
        this.fecha = fecha;
    }

    public String getdirigido() {
        return dirigido;
    }

    public void setDirigido(String Dirigido) {
        this.dirigido = Dirigido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String Correo) {
        this.correo = Correo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String Comentario) {
        this.comentario = Comentario;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String Estatus) {
        this.estatus = Estatus;
    }

    public String getSolucion() {
        return solucion;
    }

    public void setSolucion(String Solucion) {
        this.solucion = Solucion;
    }

}