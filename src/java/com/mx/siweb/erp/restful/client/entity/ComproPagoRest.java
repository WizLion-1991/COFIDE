/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mx.siweb.erp.restful.client.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *Entidad de respuesta de compro pago
 * @author ZeusGalindo
 */
@XmlRootElement
public class ComproPagoRest {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   String payment_id;
   String short_payment_id;
   String payment_status;
   String payment_instructions;
   String step_1;
   String step_2;
   String step_3;
   String description;
   String note_extra_comition;
   String note_expiration_date;
   String note_confirmation;
      public String getPayment_id() {
      return payment_id;
   }

   public void setPayment_id(String payment_id) {
      this.payment_id = payment_id;
   }

   public String getShort_payment_id() {
      return short_payment_id;
   }

   public void setShort_payment_id(String short_payment_id) {
      this.short_payment_id = short_payment_id;
   }

   public String getPayment_status() {
      return payment_status;
   }

   public void setPayment_status(String payment_status) {
      this.payment_status = payment_status;
   }

   public String getPayment_instructions() {
      return payment_instructions;
   }

   public void setPayment_instructions(String payment_instructions) {
      this.payment_instructions = payment_instructions;
   }

   public String getStep_1() {
      return step_1;
   }

   public void setStep_1(String step_1) {
      this.step_1 = step_1;
   }

   public String getStep_2() {
      return step_2;
   }

   public void setStep_2(String step_2) {
      this.step_2 = step_2;
   }

   public String getStep_3() {
      return step_3;
   }

   public void setStep_3(String step_3) {
      this.step_3 = step_3;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getNote_extra_comition() {
      return note_extra_comition;
   }

   public void setNote_extra_comition(String note_extra_comition) {
      this.note_extra_comition = note_extra_comition;
   }

   public String getNote_expiration_date() {
      return note_expiration_date;
   }

   public void setNote_expiration_date(String note_expiration_date) {
      this.note_expiration_date = note_expiration_date;
   }

   public String getNote_confirmation() {
      return note_confirmation;
   }

   public void setNote_confirmation(String note_confirmation) {
      this.note_confirmation = note_confirmation;
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>


}
