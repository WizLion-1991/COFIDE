package com.siweb.test;

import ERP.MovProveedor;
import ERP.PagosMasivosCtas;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *Clase para probar pagos
 * @author ZeusGalindo
 */
public class TestPagos {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   public void TestPagoProveedor(Conexion oConn, VariableSession varSesiones,
           double dblMontoPagoTot, int intBc_Id, String strFecha, int intMoneda, 
           double dblParidad, double dblMontoPagoTotMoneda, String strNotas, int intContaPagos, 
           String stridTrx, String strTipoDoc, String strMONTOPAGO, String strMONTOPAGOAMONEDA, String strMONENDAORIGINAL) {

      //Recuperamos el numero de banco
      PagosMasivosCtas masivo = new PagosMasivosCtas(oConn, varSesiones, null);
      //Inicializamos objeto
      masivo.Init();
      masivo.getMasivo().setFieldString("MPM_FECHA", strFecha);
      masivo.setIntMoneda(intMoneda);
      masivo.setDblParidad(dblParidad);
      masivo.setDblMontoPagado(dblMontoPagoTotMoneda);
      //Recuperamos los pagos que se van a guardar
      String[] lstTrx = stridTrx.split(",");
      String[] lstTipoDoc = strTipoDoc.split(",");
      String[] lstMontoPay = strMONTOPAGO.split(",");
      String[] lstMontoPagoAMoneda = strMONTOPAGOAMONEDA.split(",");
      String[] lstMONEDAS = strMONENDAORIGINAL.split(",");
      for (int i = 0; i < lstTrx.length; i++) {
         //Recuperamos el id de la transaccion
         int intIdTrx = Integer.valueOf(lstTrx[i]);
         //Recuperamos el tipo de transaccion
         int intTipoDoc = Integer.valueOf(lstTipoDoc[i]);
         //Recuperamos el monto del pago
         double dblMontoPago = Double.valueOf(lstMontoPay[i]);
         //Recuperamos el monto del pago ya con la tasa de cambio
         double dblMontoPagoAMoneda = Double.valueOf(lstMontoPagoAMoneda[i]);
         //Instanciamos el objeto que nos trae las listas de precios
         MovProveedor movProv = new MovProveedor(oConn, varSesiones, null);
         //Inicializamos objeto
         movProv.Init();
         if (intBc_Id != 0) {
            movProv.setIntBc_Id(intBc_Id);
         } else {
            movProv.setBolCaja(true);
         }
         //Recibimos datos para el encabezado

         movProv.getCta_prov().setFieldString("MP_FECHA", strFecha);
         movProv.getCta_prov().setFieldString("MP_NOTAS", strNotas);
         movProv.getCta_prov().setFieldInt("MP_MONEDA", Integer.valueOf(lstMONEDAS[i]));
         movProv.getCta_prov().setFieldDouble("MP_TASAPESO", dblParidad);
         movProv.getCta_prov().setFieldInt("CXP_ID", intIdTrx);
         movProv.getCta_prov().setFieldInt("MP_ESPAGO", 1);
         movProv.getCta_prov().setFieldDouble("MP_ABONO", dblMontoPago);
         //Calculamos el factor de proporcion
         double dblFactor = (dblMontoPago / dblMontoPagoTot);
         //Recibimos los pagos
         int intCountPagos = intContaPagos;
//         for (int j = 1; j <= intCountPagos; j++) {
//            if (Double.valueOf(request.getParameter("MPD_IMPORTE" + j)) > 0) {
//               vta_mov_prov_deta detaPago = new vta_mov_prov_deta();
//               double dblImporte = Double.valueOf(request.getParameter("MPD_IMPORTE" + j));
//               double dblCambio = Double.valueOf(request.getParameter("MPD_CAMBIO" + j));
//               double dblImporteTrx = dblImporte * dblFactor;
//               double dblImporteCambio = dblCambio * dblFactor;
//               //Calculamos proporcion de la forma de pago
//               detaPago.setFieldInt("PV_ID", 0);
//               detaPago.setFieldInt("SC_ID", 0);
//               detaPago.setFieldInt("MPD_MONEDA", Integer.valueOf(request.getParameter("MPD_MONEDA" + j)));
//               detaPago.setFieldString("MPD_FOLIO", request.getParameter("MPD_FOLIO" + j));
//               detaPago.setFieldString("MPD_FORMAPAGO", request.getParameter("MPD_FORMAPAGO" + j));
//               detaPago.setFieldString("MPD_NOCHEQUE", request.getParameter("MPD_NOCHEQUE" + j));
//               detaPago.setFieldString("MPD_BANCO", request.getParameter("MPD_BANCO" + j));
//               detaPago.setFieldString("MPD_NOTARJETA", request.getParameter("MPD_NOTARJETA" + j));
//               detaPago.setFieldString("MPD_TIPOTARJETA", request.getParameter("MPD_TIPOTARJETA" + j));
//               detaPago.setFieldDouble("MPD_IMPORTE", dblImporteTrx);
//               detaPago.setFieldDouble("MPD_TASAPESO", Double.valueOf(request.getParameter("MPD_TASAPESO" + j)));
//               detaPago.setFieldDouble("MPD_CAMBIO", dblImporteCambio);
//               movProv.AddDetalle(detaPago);
//            }
//         }
         //Anadimos el pago
         masivo.AddDetalle(movProv);
      }

      //Generamos transaccion
      masivo.doTrx();
      String strRes = "";
      if (masivo.getStrResultLast().equals("OK")) {
         strRes = "OK." + masivo.getMasivo().getValorKey();
      } else {
         strRes = masivo.getStrResultLast();
      }
   }
   // </editor-fold>
}
