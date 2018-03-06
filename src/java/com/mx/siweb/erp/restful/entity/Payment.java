/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mx.siweb.erp.restful.entity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author siweb
 */
public class Payment {
    
    public String codigo;
    private List<PaymentDeta> PAYMENTItem;
    
    public String getCodigo(){
        return codigo;
    }
    
    public void setCodigo(String codigo){
        this.codigo = codigo;
    }
    
    public List<PaymentDeta> getPaymentDeta(){
        return PAYMENTItem;
    }
    
    public void setPaymentDeta(List<PaymentDeta> PAYMENTItem){
        this.PAYMENTItem = PAYMENTItem;
    }
    
    public Payment(){
        PAYMENTItem = new ArrayList<PaymentDeta>();
    }
    
}
