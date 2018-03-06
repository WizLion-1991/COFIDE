package com.mx.siweb.erp.especiales.innte;

import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Esta clase se encarga de exportar los tickets para OB10
 *
 * @author ZeusGalindo
 */
public class ExportaTickets {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final Logger log = LogManager.getLogger(ExportaTickets.class.getName());
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   public String GenerarTxt(Conexion oConn, String strFechaIni, String strFechaFin) {
      StringBuilder strTxt = new    StringBuilder();
      
      log.debug("Iniciando consulta...");
      
      strTxt.append("|FOLIO|emisor_rfc|emisor_nombre|emisor_calle|emisor_colonia|emisor_municipio|emisor_estado|emisor_pais|emisor_codigoPostal|emisor_Regimen|cliente_rfc|cliente_nombre|cliente_calle|cliente_noExterior|cliente_colonia|cliente_municipio|cliente_estado|cliente_pais|cliente_codigoPostal|FECHA|Moneda|impuesto|tasa|totalImpuestosTrasladados|subTotal|total|formaDePago|condicionesDePago|tipoDeComprobante|metodoDePago|LugarExpedicion|orden_de_compra|partida_cantidad|partida_unidad|partida_descripcion|partida_importeBruto|partida_descuentoUni|partida_valorUnitario|partida_importe|notas|\n");//
      String strSql = "select TKT_FOLIO as folio,\n"
              + "EMP_RFC as emisor_rfc,\n"
              + "EMP_RAZONSOCIAL as emisor_nombre,\n"
              + "EMP_CALLE as emisor_calle,\n"
              + "EMP_COLONIA as emisor_colonia,\n"
              + "EMP_MUNICIPIO as emisor_municipio,\n"
              + "EMP_ESTADO as emisor_estado,\n"
              + "\"Mexico\" as emisor_pais,\n"
              + "EMP_CP as emisor_codigoPostal,\n"
              + "(SELECT GROUP_CONCAT(REGF_DESCRIPCION SEPARATOR ',') FROM vta_empregfiscal,vta_regimenfiscal where vta_empregfiscal.REGF_ID = vta_regimenfiscal.REGF_ID and vta_empregfiscal.EMP_ID=vta_empresas.EMP_ID GROUP BY vta_empregfiscal.EMP_ID limit 0,1) as emisor_Regimen,\n"
              + "TKT_RFC as cliente_rfc,TKT_RAZONSOCIAL as cliente_nombre,\n"
              + "TKT_CALLE as cliente_calle,\n"
              + "TKT_NUMERO as cliente_noExterior,\n"
              + "TKT_COLONIA as cliente_colonia,\n"
              + "TKT_MUNICIPIO as cliente_municipio,\n"
              + "TKT_ESTADO as cliente_estado,\n"
              + "\"Mexico\" as cliente_pais,\n"
              + "TKT_CP as cliente_codigoPostal,\n"
              + "TKT_FECHA as FECHA,\n"
              + "TKT_NOTASPIE as notas,\n"
              + "(SELECT MON_DESCRIPCION from vta_monedas where TKT_MONEDA = MON_ID) as Moneda ,\n"
              + "TKT_IMPUESTO1 as impuesto , \n"
              + "TKT_TASA1 as tasa ,\n"
              + "TKT_IMPUESTO1 as totalImpuestosTrasladados , \n"
              + "TKT_IMPORTE as subTotal , \n"
              + "TKT_DESCUENTO as descuento , \n"
              + "TKT_TOTAL as total , \n"
              + "TKT_FORMADEPAGO as formaDePago ,  \n"
              + "TKT_CONDPAGO as condicionesDePago ,\n"
              + "TKT_TIPOCOMP as tipoDeComprobante , \n"
              + "TKT_METODODEPAGO as metodoDePago ,\n"
              + "\"Mexio D.F.\"  as LugarExpedicion , \n"
              + "TKT_REFERENCIA as orden_de_compra,\n"
              + " TKTD_CANTIDAD as partida_cantidad , TKTD_UNIDAD_MEDIDA as partida_unidad ,TKTD_DESCRIPCION  as partida_descripcion,\n"
              + "TKTD_IMPORTE - TKTD_IMPUESTO1 + TKTD_DESCUENTO as partida_importeBruto ,\n"
              + "TKTD_DESCUENTO/TKTD_CANTIDAD  as partida_descuentoUni ,"
              + "TKTD_PRECIO/(1+(TKT_TASA1/100))-TKTD_DESCUENTO/TKTD_CANTIDAD as partida_valorUnitario ,"
              + "TKTD_IMPORTE/(1+(TKT_TASA1/100))  as partida_importe \n"
              + "from vta_tickets , vta_empresas, vta_ticketsdeta\n"
              + "where  vta_tickets.EMP_ID =  vta_empresas.EMP_ID AND vta_ticketsdeta.TKT_ID = vta_tickets.TKT_ID AND \n"
              + "TKT_ANULADA = 0 AND TKT_FECHA>='" + strFechaIni + "' AND TKT_FECHA <= '" + strFechaFin + "'  order by TKT_FOLIO";
      try {
         ResultSet rs = oConn.runQuery(strSql);
         int intConta = 0;
         while(rs.next()){
            if(intConta > 0){
               strTxt.append("\n");
            }
            intConta++;
            strTxt.append("|").append(rs.getString("folio"));
            strTxt.append("|").append(rs.getString("emisor_rfc"));
            strTxt.append("|").append(rs.getString("emisor_nombre"));
            strTxt.append("|").append(rs.getString("emisor_calle"));
            strTxt.append("|").append(rs.getString("emisor_colonia"));
            strTxt.append("|").append(rs.getString("emisor_municipio"));
            strTxt.append("|").append(rs.getString("emisor_estado"));
            strTxt.append("|").append(rs.getString("emisor_pais"));
            strTxt.append("|").append(rs.getString("emisor_codigoPostal"));
            strTxt.append("|").append(rs.getString("emisor_Regimen"));
            strTxt.append("|").append(rs.getString("cliente_rfc"));
            strTxt.append("|").append(rs.getString("cliente_nombre"));
            strTxt.append("|").append(rs.getString("cliente_calle"));
            strTxt.append("|").append(rs.getString("cliente_noExterior"));
            strTxt.append("|").append(rs.getString("cliente_colonia"));
            strTxt.append("|").append(rs.getString("cliente_municipio"));
            strTxt.append("|").append(rs.getString("cliente_estado"));
            strTxt.append("|").append(rs.getString("cliente_pais"));
            strTxt.append("|").append(rs.getString("cliente_codigoPostal"));
            strTxt.append("|").append(rs.getString("FECHA").substring(0, 4) + "-" + rs.getString("FECHA").substring(4, 6)  + "-" + rs.getString("FECHA").substring(6, 8));
            strTxt.append("|").append(rs.getString("Moneda"));
            strTxt.append("|").append(rs.getString("impuesto"));
            strTxt.append("|").append(rs.getString("tasa"));
            strTxt.append("|").append(rs.getString("totalImpuestosTrasladados"));
            strTxt.append("|").append(rs.getString("subTotal"));
            strTxt.append("|").append(rs.getString("total"));
//            strTxt.append("|").append(rs.getString("descuento"));
            strTxt.append("|").append(rs.getString("formaDePago"));
            strTxt.append("|").append(rs.getString("condicionesDePago"));
            strTxt.append("|").append("380");
            strTxt.append("|").append(rs.getString("metodoDePago"));
            strTxt.append("|").append(rs.getString("LugarExpedicion"));
            strTxt.append("|").append(rs.getString("orden_de_compra"));
            strTxt.append("|").append(rs.getString("partida_cantidad"));
            strTxt.append("|").append(rs.getString("partida_unidad"));
            strTxt.append("|").append(rs.getString("partida_descripcion"));
            strTxt.append("|").append(rs.getString("partida_importeBruto"));
            strTxt.append("|").append(rs.getString("partida_descuentoUni"));
            strTxt.append("|").append(rs.getString("partida_valorUnitario"));
            strTxt.append("|").append(rs.getString("partida_importe"));
            strTxt.append("|").append(rs.getString("notas"));
            strTxt.append("|");
            log.debug("Ticket " + rs.getString("folio"));
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      log.debug("****");
      log.debug(strTxt);
      log.debug("****");
      return strTxt.toString();
   }
   // </editor-fold>
}
