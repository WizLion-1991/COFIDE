/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.nominas;

/**
 *
 * @author ZeusSIWEB
 */
public enum CamposExcelIncidencias {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   CLAVE(0), NOMBRE(1),
   DIAS_TRABAJADOS(2),
   DIAS_DOMINGO(3),
   HORAS_EXTRAS(4),
   DIAS_DE_VACACIONES(5),
   PRIMA_VACACIONAL(6),
   RETROACTIVO_DIAS(7),
   NUMERO_RETARDOS(8),
   BONO_PUNTUALIDAD(9),
   BONO_ASISTENCIA(10),
   BONO_CUMPLIMIENTO(11), FALTAS_JUSTIFICADAS(12),
   FALTAS_INJUSTIFICADAS(13),
   FALTAS_SANCION(14),
   INCAPACIDAD_ENFERMEDAD(15),
   INCAPACIDAD_MATERNA(16), ACCIDENTE_TRABAJO(17),
   TIPO_DICTAMEN(18),
   TIPO_CASO(19),
   CAJA_DE_AHORROS(20),
   DIA_FESTIVO(21), DETALLE_HORAS_EXTRAS(22),
   DETALLE_FALTAS_JUSTIFICADAS(23),
   DETALLE_FALTAS_INJUSTIFICADAS(24),
   DETALLE_FALTAS_SANCION(25), DETALLE_INCAPACIDAD_ENFERMEDAD(26),
   DETALLE_INCAPACIDAD_MATERNA(27), DETALLE_ACCIDENTE_TRABAJO(28);
   //Campos tipo constante   

   private final int posicion; //Peso específico de la madera

   public int getPosicion() {
      return posicion;
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">

   CamposExcelIncidencias(int posicion) {

      this.posicion = posicion;

   }
   // </editor-fold>

}
