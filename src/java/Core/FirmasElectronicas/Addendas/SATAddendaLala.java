/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Core.FirmasElectronicas.Addendas;

import Core.FirmasElectronicas.SAT22.Comprobante;
import Core.FirmasElectronicas.SAT22.Comprobante.Addenda;
import Core.FirmasElectronicas.SAT22.Comprobante.Conceptos;
import Core.FirmasElectronicas.SAT22.Comprobante.Conceptos.Concepto;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.AdditionalInformation;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.BaseAmount;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.Buyer;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.LineItem;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.OrderIdentification;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.PayableAmount;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.RequestForPaymentIdentification;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.Seller;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.ShipTo;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.SpecialInstruction;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.Tax;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.TotalAmount;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import comSIWeb.Utilerias.NumberString;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBElement;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.PrintStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Addenda lala bajo nuevo paradigma
 *
 * @author aleph_79
 */
public class SATAddendaLala extends SATAddenda {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private Fechas fecha;
   String strHM_SELLER_ASSIGNED_IDENTIFIER_FOR_A_PARTY = null;
   String strHM_DIV = null;
   String strHM_ON = null;
   String strHM_SOC = null;
   String strHM_REFERENCEDATE = null;
   String strName = null;
   String strStreetAddressOne = null;
   String strCity = null;
   String strPostalCode = null;
   double dblImporte;
   double dblImpuesto1;
   double dblTotal;
   double dblTasaImpuesto1;
   String strDeliveryDate = null;
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public SATAddendaLala() {
      fecha = new Fechas();
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   private void GetData(int intTransaccion, Conexion oConn) {
      String strSql = "SELECT HM_SOC,HM_ON,HM_DIV,HM_REFERENCEDATE,"
         + " HM_SELLER_ASSIGNED_IDENTIFIER_FOR_A_PARTY"
         + " ,HM_NAME,HM_STREET,HM_CITY,HM_POSTALCODE"
         + " ,FAC_IMPORTE,FAC_IMPUESTO1,FAC_TOTAL,FAC_TASA1,FAC_FECHA"
         + " from vta_facturas "
         + " where FAC_ID = " + intTransaccion;
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            this.strHM_SOC = rs.getString("HM_SOC");
            this.strHM_ON = rs.getString("HM_ON");
            this.strHM_DIV = rs.getString("HM_DIV");
            this.strHM_REFERENCEDATE = rs.getString("HM_REFERENCEDATE");
            this.strHM_SELLER_ASSIGNED_IDENTIFIER_FOR_A_PARTY = rs.getString("HM_SELLER_ASSIGNED_IDENTIFIER_FOR_A_PARTY");
            this.strName = rs.getString("HM_NAME");
            this.strStreetAddressOne = rs.getString("HM_STREET");
            this.strCity = rs.getString("HM_CITY");
            this.strPostalCode = rs.getString("HM_POSTALCODE");
            this.dblImporte = rs.getDouble("FAC_IMPORTE");
            this.dblImpuesto1 = rs.getDouble("FAC_IMPUESTO1");
            this.dblTotal = rs.getDouble("FAC_TOTAL");
            this.dblTasaImpuesto1 = rs.getDouble("FAC_TASA1");
            this.strDeliveryDate = fecha.FormateaAAAAMMDD(rs.getString("FAC_FECHA"), "-");
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         Logger.getLogger(SATAddendaLala.class.getName()).log(Level.SEVERE, null, ex);
      }

   }

   @Override
   public void FillAddenda(Addenda addenda, Comprobante objComp,
      String strPath, int intTransaccion, Conexion oConn) {
      //Obtenemos los datos de la addenda
      GetData(intTransaccion, oConn);
      Core.FirmasElectronicas.SAT_AMECE.ObjectFactory objFactory = new Core.FirmasElectronicas.SAT_AMECE.ObjectFactory();
      //requestForPayment
      RequestForPayment request = objFactory.createComprobanteAddendaRequestForPayment();
      request.setType("SimpleInvoiceType");
      request.setContentVersion("1.3.1");
      request.setDocumentStatus("ORIGINAL");
      request.setDocumentStructureVersion("AMC7.1");
//      request.setDeliveryDate(objComp.getFecha());

      //requestForPaymentIdentification
      RequestForPaymentIdentification identification = objFactory.createComprobanteAddendaRequestForPaymentRequestForPaymentIdentification();
      identification.setEntityType("INVOICE");
      identification.setUniqueCreatorIdentification(objComp.getSerie() + objComp.getFolio());
      request.setRequestForPaymentIdentification(identification);
      //specialInstruction
      SpecialInstruction specialInstruction = objFactory.createComprobanteAddendaRequestForPaymentSpecialInstruction();
      specialInstruction.setCode("SOC");
      specialInstruction.getContent().add(objFactory.createComprobanteAddendaRequestForPaymentSpecialInstructionText(strHM_SOC));
      request.getSpecialInstruction().add(specialInstruction);
      //orderIdentification
      OrderIdentification orderIdentification = objFactory.createComprobanteAddendaRequestForPaymentOrderIdentification();
      OrderIdentification.ReferenceIdentification ref = objFactory.createComprobanteAddendaRequestForPaymentOrderIdentificationReferenceIdentification();
      ref.setType("ON");
      ref.setValue(strHM_ON);
      orderIdentification.getReferenceIdentification().add(ref);
//      orderIdentification.setReferenceDate(fecha.RegresaXMLGregorianCalendar(fecha.FormateaBD(strHM_REFERENCEDATE, "/"), "00:00").toGregorianCalendar().getTime());
      request.setOrderIdentification(orderIdentification);
      //AdditionalInformation
      AdditionalInformation adition = objFactory.createComprobanteAddendaRequestForPaymentAdditionalInformation();
      AdditionalInformation.ReferenceIdentification referenceidentification = objFactory.createComprobanteAddendaRequestForPaymentAdditionalInformationReferenceIdentification();
      referenceidentification.setType("DIV");
      referenceidentification.setValue(strHM_DIV);
//      adition.setReferenceIdentification(referenceidentification);
      request.setAdditionalInformation(adition);
      //DeliveryNote
      //buyer
      Buyer provedor = objFactory.createComprobanteAddendaRequestForPaymentBuyer();
      request.setBuyer(provedor);
      //seller
      Seller sel = objFactory.createComprobanteAddendaRequestForPaymentSeller();
      Seller.AlternatePartyIdentification alternatepartyidentification = objFactory.createComprobanteAddendaRequestForPaymentSellerAlternatePartyIdentification();
      sel.setGln("7507003100001");
      alternatepartyidentification.setType("SELLER_ASSIGNED_IDENTIFIER_FOR_A_PARTY");
      alternatepartyidentification.setValue(strHM_SELLER_ASSIGNED_IDENTIFIER_FOR_A_PARTY);
      sel.setAlternatePartyIdentification(alternatepartyidentification);
      request.setSeller(sel);
      //ShipTo
      ShipTo ship = objFactory.createComprobanteAddendaRequestForPaymentShipTo();
      ShipTo.NameAndAddress nameandadddress = objFactory.createComprobanteAddendaRequestForPaymentShipToNameAndAddress();
      ship.setGln("7507003123772");
      JAXBElement<String> street = objFactory.createComprobanteAddendaRequestForPaymentShipToNameAndAddressStreetAddressOne(objComp.getReceptor().getDomicilio().getCalle());
      JAXBElement<String> city = objFactory.createComprobanteAddendaRequestForPaymentShipToNameAndAddressCity(objComp.getReceptor().getDomicilio().getPais());
      JAXBElement<String> name = objFactory.createComprobanteAddendaRequestForPaymentShipToNameAndAddressName(objComp.getReceptor().getNombre());
      if (name.getValue().length() > 35) {
         String steee = name.getValue();
         steee = steee.substring(0, 34);
         name.setValue(steee);
      }
      JAXBElement<String> code = objFactory.createComprobanteAddendaRequestForPaymentShipToNameAndAddressPostalCode(objComp.getReceptor().getDomicilio().getCodigoPostal());
      nameandadddress.getNameAndStreetAddressOneAndCity().add(name);
      nameandadddress.getNameAndStreetAddressOneAndCity().add(street);
      nameandadddress.getNameAndStreetAddressOneAndCity().add(city);
      nameandadddress.getNameAndStreetAddressOneAndCity().add(code);
      ship.setNameAndAddress(nameandadddress);
      request.setShipTo(ship);
      //InvoiceCreator
      //Customs
      //currency
      RequestForPayment.Currency currency = objFactory.createComprobanteAddendaRequestForPaymentCurrency();
      currency.setCurrencyISOCode("MXN");
      currency.getCurrencyFunction().add("BILLING_CURRENCY");
      request.getCurrency().add(currency);
      //paymentTerms
      //shipmentDetail
      //allowanceCharge

      //lineItem
      Conceptos conceptos = objComp.getConceptos();
      Iterator<Concepto> it = conceptos.getConcepto().iterator();
      long lngConta = 0;
      double totLineTot = 0;
      while (it.hasNext()) {
         Concepto concepto = it.next();
         lngConta++;

         LineItem line = objFactory.createComprobanteAddendaRequestForPaymentLineItem();
         line.setType("SimpleInvoiceLineItemType");
         line.setNumber(BigInteger.valueOf(lngConta));
         LineItem.TradeItemIdentification tradeitemidentification = objFactory.createComprobanteAddendaRequestForPaymentLineItemTradeItemIdentification();
         line.setTradeItemIdentification(tradeitemidentification);
         LineItem.AlternateTradeItemIdentification alternatetradeitemidentification = objFactory.createComprobanteAddendaRequestForPaymentLineItemAlternateTradeItemIdentification();
         alternatetradeitemidentification.setType("BUYER_ASSIGNED");
         alternatetradeitemidentification.setValue(concepto.getNoIdentificacion());
         line.getAlternateTradeItemIdentification().add(alternatetradeitemidentification);
         LineItem.TradeItemDescriptionInformation tradeitemdescriptioninformation = objFactory.createComprobanteAddendaRequestForPaymentLineItemTradeItemDescriptionInformation();
         tradeitemdescriptioninformation.setLanguage("ES");
         tradeitemdescriptioninformation.setLongText(concepto.getDescripcion());
         line.setTradeItemDescriptionInformation(tradeitemdescriptioninformation);
         LineItem.InvoicedQuantity invoicedquantity = objFactory.createComprobanteAddendaRequestForPaymentLineItemInvoicedQuantity();
         invoicedquantity.setUnitOfMeasure("MIL");
         invoicedquantity.setValue(new BigDecimal(NumberString.FormatearDecimal(concepto.getCantidad().doubleValue(), 2).replace(",", "")));
         line.setInvoicedQuantity(invoicedquantity);
         LineItem.GrossPrice grossprice = objFactory.createComprobanteAddendaRequestForPaymentLineItemGrossPrice();
         grossprice.setAmount(new BigDecimal(NumberString.FormatearDecimal(concepto.getImporte().doubleValue(), 2).replace(",", "")));
         line.setGrossPrice(grossprice);
         LineItem.NetPrice netprice = objFactory.createComprobanteAddendaRequestForPaymentLineItemNetPrice();
         netprice.setAmount(new BigDecimal(NumberString.FormatearDecimal(concepto.getValorUnitario().doubleValue(), 2).replace(",", "")));
         line.setNetPrice(netprice);
         LineItem.TradeItemTaxInformation tradeitemtaxinformation = objFactory.createComprobanteAddendaRequestForPaymentLineItemTradeItemTaxInformation();
         tradeitemtaxinformation.setTaxTypeDescription("VAT");
         tradeitemtaxinformation.setReferenceNumber("V0");
         Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.LineItem.TradeItemTaxInformation.TradeItemTaxAmount tradeitem = objFactory.createComprobanteAddendaRequestForPaymentLineItemTradeItemTaxInformationTradeItemTaxAmount();
         tradeitem.setTaxPercentage(new BigDecimal(NumberString.FormatearDecimal(objComp.getImpuestos().getTraslados().getTraslado().get(0).getTasa().doubleValue(), 2).replace(",", "")));
         double dblImporteIVA = (objComp.getImpuestos().getTraslados().getTraslado().get(0).getTasa().doubleValue() / 100) * concepto.getImporte().doubleValue();
         tradeitem.setTaxAmount(new BigDecimal(NumberString.FormatearDecimal(BigDecimal.valueOf(dblImporteIVA).doubleValue(), 2).replace(",", "")));
         tradeitemtaxinformation.setTradeItemTaxAmount(tradeitem);
         line.getTradeItemTaxInformation().add(tradeitemtaxinformation);
         LineItem.TotalLineAmount totallineamount = objFactory.createComprobanteAddendaRequestForPaymentLineItemTotalLineAmount();
         double totLine = dblImporteIVA + concepto.getImporte().doubleValue();
         totLineTot += totLine;

         line.setTotalLineAmount(totallineamount);
         LineItem.TotalLineAmount.GrossAmount grossamount = objFactory.createComprobanteAddendaRequestForPaymentLineItemTotalLineAmountGrossAmount();
         grossamount.setAmount(new BigDecimal(NumberString.FormatearDecimal(BigDecimal.valueOf(totLine).doubleValue(), 2).replace(",", "")));
         totallineamount.setGrossAmount(grossamount);
         LineItem.TotalLineAmount.NetAmount netamount = objFactory.createComprobanteAddendaRequestForPaymentLineItemTotalLineAmountNetAmount();
         double totbrut = (concepto.getCantidad().doubleValue() * concepto.getValorUnitario().doubleValue());
         netamount.setAmount(new BigDecimal(NumberString.FormatearDecimal(BigDecimal.valueOf(totbrut).doubleValue(), 2).replace(",", "")));
         totallineamount.setNetAmount(netamount);
         line.setTotalLineAmount(totallineamount);
         request.getLineItem().add(line);
         //totalAmount
         TotalAmount totalamount = objFactory.createComprobanteAddendaRequestForPaymentTotalAmount();
         totalamount.setAmount(new BigDecimal(NumberString.FormatearDecimal(BigDecimal.valueOf(totLine).doubleValue(), 2).replace(",", "")));
         request.setTotalAmount(totalamount);
         //BaseAmount
         BaseAmount baseamount = objFactory.createComprobanteAddendaRequestForPaymentBaseAmount();
         baseamount.setAmount(new BigDecimal(NumberString.FormatearDecimal(objComp.getSubTotal().doubleValue(), 2).replace(",", "")));
         request.setBaseAmount(baseamount);
         //tax
         Tax tax = objFactory.createComprobanteAddendaRequestForPaymentTax();
         tax.setType("VAT");
         tax.setTaxPercentage(new BigDecimal(NumberString.FormatearDecimal(objComp.getImpuestos().getTraslados().getTraslado().get(0).getTasa().doubleValue(), 2).replace(",", "")));
         tax.setTaxAmount(new BigDecimal(NumberString.FormatearDecimal(objComp.getImpuestos().getTraslados().getTraslado().get(0).getImporte().doubleValue(), 2).replace(",", "")));
         tax.setTaxCategory("TRANSFERIDO");
         request.getTax().add(tax);
         //payableAmount
         PayableAmount payableamount = objFactory.createComprobanteAddendaRequestForPaymentPayableAmount();
         payableamount.setAmount(new BigDecimal(NumberString.FormatearDecimal(concepto.getImporte().doubleValue(), 2).replace(",", "")));
         request.setPayableAmount(payableamount);

      }

      //Asignamos a la addenda
      addenda.getAny().add(request);
   }

   @Override
   public void FillAddenda(Core.FirmasElectronicas.SAT3_2.Comprobante.Addenda addenda, Core.FirmasElectronicas.SAT3_2.Comprobante objComp,
      String strPath, int intTransaccion, Conexion oConn) {
      //Obtenemos los datos de la addenda
      GetData(intTransaccion, oConn);

      Core.FirmasElectronicas.SAT_AMECE.ObjectFactory objFactory = new Core.FirmasElectronicas.SAT_AMECE.ObjectFactory();
      //requestForPayment
      RequestForPayment request = objFactory.createComprobanteAddendaRequestForPayment();
      request.setType("SimpleInvoiceType");
      request.setContentVersion("1.3.1");
      request.setDocumentStatus("ORIGINAL");
      request.setDocumentStructureVersion("AMC7.1");
//request.setDeliveryDate(objComp.getFecha());

      //requestForPaymentIdentification
      RequestForPaymentIdentification identification = objFactory.createComprobanteAddendaRequestForPaymentRequestForPaymentIdentification();
      identification.setEntityType("INVOICE");
      identification.setUniqueCreatorIdentification(objComp.getSerie() + objComp.getFolio());
      request.setRequestForPaymentIdentification(identification);
      //specialInstruction
      SpecialInstruction specialInstruction = objFactory.createComprobanteAddendaRequestForPaymentSpecialInstruction();
      specialInstruction.setCode("SOC");
      specialInstruction.getContent().add(objFactory.createComprobanteAddendaRequestForPaymentSpecialInstructionText(strHM_SOC));
      request.getSpecialInstruction().add(specialInstruction);
      //orderIdentification
      OrderIdentification orderIdentification = objFactory.createComprobanteAddendaRequestForPaymentOrderIdentification();
      OrderIdentification.ReferenceIdentification ref = objFactory.createComprobanteAddendaRequestForPaymentOrderIdentificationReferenceIdentification();
      ref.setType("ON");
      ref.setValue(strHM_ON);
      orderIdentification.getReferenceIdentification().add(ref);
//orderIdentification.setReferenceDate(fecha.RegresaXMLGregorianCalendar(fecha.FormateaBD(strHM_REFERENCEDATE, "/"), "00:00").toGregorianCalendar().getTime());
      request.setOrderIdentification(orderIdentification);
      //AdditionalInformation
      AdditionalInformation adition = objFactory.createComprobanteAddendaRequestForPaymentAdditionalInformation();
      AdditionalInformation.ReferenceIdentification referenceidentification = objFactory.createComprobanteAddendaRequestForPaymentAdditionalInformationReferenceIdentification();
      referenceidentification.setType("DIV");
      referenceidentification.setValue(strHM_DIV);
//adition.setReferenceIdentification(referenceidentification);
      request.setAdditionalInformation(adition);
      //DeliveryNote
      //buyer
      Buyer provedor = objFactory.createComprobanteAddendaRequestForPaymentBuyer();
      request.setBuyer(provedor);
      //seller
      Seller sel = objFactory.createComprobanteAddendaRequestForPaymentSeller();
      Seller.AlternatePartyIdentification alternatepartyidentification = objFactory.createComprobanteAddendaRequestForPaymentSellerAlternatePartyIdentification();
      sel.setGln("7507003100001");
      alternatepartyidentification.setType("SELLER_ASSIGNED_IDENTIFIER_FOR_A_PARTY");
      alternatepartyidentification.setValue(strHM_SELLER_ASSIGNED_IDENTIFIER_FOR_A_PARTY);
      sel.setAlternatePartyIdentification(alternatepartyidentification);
      request.setSeller(sel);
      //ShipTo
      ShipTo ship = objFactory.createComprobanteAddendaRequestForPaymentShipTo();
      ShipTo.NameAndAddress nameandadddress = objFactory.createComprobanteAddendaRequestForPaymentShipToNameAndAddress();
      ship.setGln("7507003123772");
      JAXBElement<String> street = objFactory.createComprobanteAddendaRequestForPaymentShipToNameAndAddressStreetAddressOne(objComp.getReceptor().getDomicilio().getCalle());
      JAXBElement<String> city = objFactory.createComprobanteAddendaRequestForPaymentShipToNameAndAddressCity(objComp.getReceptor().getDomicilio().getPais());
      JAXBElement<String> name = objFactory.createComprobanteAddendaRequestForPaymentShipToNameAndAddressName(objComp.getReceptor().getNombre());
      if (name.getValue().length() > 35) {
         String steee = name.getValue();
         steee = steee.substring(0, 34);
         name.setValue(steee);
      }
      JAXBElement<String> code = objFactory.createComprobanteAddendaRequestForPaymentShipToNameAndAddressPostalCode(objComp.getReceptor().getDomicilio().getCodigoPostal());
      nameandadddress.getNameAndStreetAddressOneAndCity().add(name);
      nameandadddress.getNameAndStreetAddressOneAndCity().add(street);
      nameandadddress.getNameAndStreetAddressOneAndCity().add(city);
      nameandadddress.getNameAndStreetAddressOneAndCity().add(code);
      ship.setNameAndAddress(nameandadddress);
      request.setShipTo(ship);
      //InvoiceCreator
      //Customs
      //currency
      RequestForPayment.Currency currency = objFactory.createComprobanteAddendaRequestForPaymentCurrency();
      currency.setCurrencyISOCode("MXN");
      currency.getCurrencyFunction().add("BILLING_CURRENCY");
      request.getCurrency().add(currency);
      //paymentTerms
      //shipmentDetail
      //allowanceCharge

      //lineItem
      Core.FirmasElectronicas.SAT3_2.Comprobante.Conceptos conceptos = objComp.getConceptos();
      Iterator<Core.FirmasElectronicas.SAT3_2.Comprobante.Conceptos.Concepto> it = conceptos.getConcepto().iterator();
      long lngConta = 0;
      double totLineTot = 0;
      while (it.hasNext()) {
         Core.FirmasElectronicas.SAT3_2.Comprobante.Conceptos.Concepto concepto = it.next();
         lngConta++;

         LineItem line = objFactory.createComprobanteAddendaRequestForPaymentLineItem();
         line.setType("SimpleInvoiceLineItemType");
         line.setNumber(BigInteger.valueOf(lngConta));
         LineItem.TradeItemIdentification tradeitemidentification = objFactory.createComprobanteAddendaRequestForPaymentLineItemTradeItemIdentification();
         line.setTradeItemIdentification(tradeitemidentification);
         LineItem.AlternateTradeItemIdentification alternatetradeitemidentification = objFactory.createComprobanteAddendaRequestForPaymentLineItemAlternateTradeItemIdentification();
         alternatetradeitemidentification.setType("BUYER_ASSIGNED");
         alternatetradeitemidentification.setValue(concepto.getNoIdentificacion());
         line.getAlternateTradeItemIdentification().add(alternatetradeitemidentification);
         LineItem.TradeItemDescriptionInformation tradeitemdescriptioninformation = objFactory.createComprobanteAddendaRequestForPaymentLineItemTradeItemDescriptionInformation();
         tradeitemdescriptioninformation.setLanguage("ES");
         tradeitemdescriptioninformation.setLongText(concepto.getDescripcion());
         line.setTradeItemDescriptionInformation(tradeitemdescriptioninformation);
         LineItem.InvoicedQuantity invoicedquantity = objFactory.createComprobanteAddendaRequestForPaymentLineItemInvoicedQuantity();
         invoicedquantity.setUnitOfMeasure("MIL");
         invoicedquantity.setValue(new BigDecimal(NumberString.FormatearDecimal(concepto.getCantidad().doubleValue(), 2).replace(",", "")));
         line.setInvoicedQuantity(invoicedquantity);
         LineItem.GrossPrice grossprice = objFactory.createComprobanteAddendaRequestForPaymentLineItemGrossPrice();
         grossprice.setAmount(new BigDecimal(NumberString.FormatearDecimal(concepto.getImporte().doubleValue(), 2).replace(",", "")));
         line.setGrossPrice(grossprice);
         LineItem.NetPrice netprice = objFactory.createComprobanteAddendaRequestForPaymentLineItemNetPrice();
         netprice.setAmount(new BigDecimal(NumberString.FormatearDecimal(concepto.getValorUnitario().doubleValue(), 2).replace(",", "")));
         line.setNetPrice(netprice);
         LineItem.TradeItemTaxInformation tradeitemtaxinformation = objFactory.createComprobanteAddendaRequestForPaymentLineItemTradeItemTaxInformation();
         tradeitemtaxinformation.setTaxTypeDescription("VAT");
         tradeitemtaxinformation.setReferenceNumber("V0");

         Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.LineItem.TradeItemTaxInformation.TradeItemTaxAmount tradeitem = objFactory.createComprobanteAddendaRequestForPaymentLineItemTradeItemTaxInformationTradeItemTaxAmount();
         tradeitem.setTaxPercentage(new BigDecimal(NumberString.FormatearDecimal(objComp.getImpuestos().getTraslados().getTraslado().get(0).getTasa().doubleValue(), 2).replace(",", "")));
         double dblImporteIVA = (objComp.getImpuestos().getTraslados().getTraslado().get(0).getTasa().doubleValue() / 100) * concepto.getImporte().doubleValue();
         tradeitem.setTaxAmount(new BigDecimal(NumberString.FormatearDecimal(BigDecimal.valueOf(dblImporteIVA).doubleValue(), 2).replace(",", "")));
         tradeitemtaxinformation.setTradeItemTaxAmount(tradeitem);
         line.getTradeItemTaxInformation().add(tradeitemtaxinformation);
         LineItem.TotalLineAmount totallineamount = objFactory.createComprobanteAddendaRequestForPaymentLineItemTotalLineAmount();
         double totLine = dblImporteIVA + concepto.getImporte().doubleValue();
         totLineTot += totLine;

         line.setTotalLineAmount(totallineamount);
         LineItem.TotalLineAmount.GrossAmount grossamount = objFactory.createComprobanteAddendaRequestForPaymentLineItemTotalLineAmountGrossAmount();
         grossamount.setAmount(new BigDecimal(NumberString.FormatearDecimal(BigDecimal.valueOf(totLine).doubleValue(), 2).replace(",", "")));
         totallineamount.setGrossAmount(grossamount);
         LineItem.TotalLineAmount.NetAmount netamount = objFactory.createComprobanteAddendaRequestForPaymentLineItemTotalLineAmountNetAmount();
         double totbrut = (concepto.getCantidad().doubleValue() * concepto.getValorUnitario().doubleValue());
         netamount.setAmount(new BigDecimal(NumberString.FormatearDecimal(BigDecimal.valueOf(totbrut).doubleValue(), 2).replace(",", "")));
         totallineamount.setNetAmount(netamount);
         line.setTotalLineAmount(totallineamount);
         request.getLineItem().add(line);
         //totalAmount
         TotalAmount totalamount = objFactory.createComprobanteAddendaRequestForPaymentTotalAmount();
         totalamount.setAmount(new BigDecimal(NumberString.FormatearDecimal(BigDecimal.valueOf(totLine).doubleValue(), 2).replace(",", "")));
         request.setTotalAmount(totalamount);
         //BaseAmount
         BaseAmount baseamount = objFactory.createComprobanteAddendaRequestForPaymentBaseAmount();
         baseamount.setAmount(new BigDecimal(NumberString.FormatearDecimal(objComp.getSubTotal().doubleValue(), 2).replace(",", "")));
         request.setBaseAmount(baseamount);
         //tax
         Tax tax = objFactory.createComprobanteAddendaRequestForPaymentTax();
         tax.setType("VAT");
         tax.setTaxPercentage(new BigDecimal(NumberString.FormatearDecimal(objComp.getImpuestos().getTraslados().getTraslado().get(0).getTasa().doubleValue(), 2).replace(",", "")));
         tax.setTaxAmount(new BigDecimal(NumberString.FormatearDecimal(objComp.getImpuestos().getTraslados().getTraslado().get(0).getImporte().doubleValue(), 2).replace(",", "")));
         tax.setTaxCategory("TRANSFERIDO");
         request.getTax().add(tax);
         //payableAmount
         PayableAmount payableamount = objFactory.createComprobanteAddendaRequestForPaymentPayableAmount();
         payableamount.setAmount(new BigDecimal(NumberString.FormatearDecimal(concepto.getImporte().doubleValue(), 2).replace(",", "")));
         request.setPayableAmount(payableamount);

      }

      //Asignamos a la addenda
      addenda.getAny().add(request);
   }

   @Override
   public void makeNameSpaceDeclaration(String strPath, int intTransaccion, Conexion oConn) {
      //Obtenemos datos
      GetData(intTransaccion, oConn);
      //Parametros

      try {
         File fXmlFile = new File(strPath);
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         dbFactory.setNamespaceAware(true);
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(fXmlFile);
         doc.setStrictErrorChecking(false);
         NodeList nList = doc.getDocumentElement().getChildNodes();
         if (nList != null) {
            for (int temp = 0; temp < nList.getLength(); temp++) {
               Node nNode = nList.item(temp);
               if (nNode.getNodeName().equals("cfdi:Complemento")) {
                  Node nodeParent = nNode.getParentNode();//Obtenemos nodo padre del complemento
                  Node addendaNode = doc.createElement("cfdi:Addenda");
                  //Generamos el detalle de la addenda
                  nodeParent.appendChild(addendaNode);
                  //requestForPayment
                  Element requestForPaymentNode = doc.createElement("requestForPayment");
                  //Atributos
                  requestForPaymentNode.setAttribute("type", "SimpleInvoiceType");
                  requestForPaymentNode.setAttribute("contentVersion", "1.3.1");
                  requestForPaymentNode.setAttribute("documentStructureVersion", "AMC7.1");
                  requestForPaymentNode.setAttribute("documentStatus", "ORIGINAL");
                  requestForPaymentNode.setAttribute("DeliveryDate", strDeliveryDate);
                  //requestForPaymentIdentification
                  Element requestForPaymentIdentificationNode = doc.createElement("requestForPaymentIdentification");
                  requestForPaymentNode.appendChild(requestForPaymentIdentificationNode);
                  //entityType
                  Element entityTypeNode = doc.createElement("entityType");
                  entityTypeNode.appendChild(doc.createTextNode("INVOICE"));
                  requestForPaymentIdentificationNode.appendChild(entityTypeNode);
                  //uniqueCreatorIdentification
                  Element uniqueCreatorIdentificationNode = doc.createElement("uniqueCreatorIdentification");
                  uniqueCreatorIdentificationNode.appendChild(doc.createTextNode("ABC99999999"));
                  requestForPaymentIdentificationNode.appendChild(uniqueCreatorIdentificationNode);

                  //specialInstruction
                  Element specialInstruction = doc.createElement("specialInstruction");
                  requestForPaymentNode.appendChild(specialInstruction);
                  specialInstruction.setAttribute("code", "SOC");
                  //text
                  Element entitytext = doc.createElement("text");
                  entitytext.appendChild(doc.createTextNode(this.strHM_SOC));
                  specialInstruction.appendChild(entitytext);

                  //orderIdentification
                  Element orderIdentification = doc.createElement("orderIdentification");
                  requestForPaymentNode.appendChild(orderIdentification);
                  Element referenceIdentification = doc.createElement("referenceIdentification");
                  orderIdentification.appendChild(referenceIdentification);
                  referenceIdentification.setAttribute("type", "ON");
                  referenceIdentification.appendChild(doc.createTextNode(this.strHM_ON));
                  Element ReferenceDate = doc.createElement("ReferenceDate");
                  orderIdentification.appendChild(ReferenceDate);
                  ReferenceDate.appendChild(doc.createTextNode(this.strHM_REFERENCEDATE));

                  //AdditionalInformation
                  Element AdditionalInformation = doc.createElement("AdditionalInformation");
                  requestForPaymentNode.appendChild(AdditionalInformation);
                  Element referenceIdentification2 = doc.createElement("referenceIdentification");
                  AdditionalInformation.appendChild(referenceIdentification2);
                  referenceIdentification2.setAttribute("type", "DIV");
                  referenceIdentification2.appendChild(doc.createTextNode(this.strHM_DIV));

                  //buyer
                  Element buyer = doc.createElement("buyer");
                  requestForPaymentNode.appendChild(buyer);

                  //seller
                  Element seller = doc.createElement("seller");
                  requestForPaymentNode.appendChild(seller);
                  Element gln1 = doc.createElement("gln");
                  seller.appendChild(gln1);
                  gln1.appendChild(doc.createTextNode("7507003100001"));
                  Element alternatePartyIdentification = doc.createElement("referenceIdentification");
                  seller.appendChild(alternatePartyIdentification);
                  alternatePartyIdentification.setAttribute("type", "SELLER_ASSIGNED_IDENTIFIER_FOR_A_PARTY");
                  alternatePartyIdentification.appendChild(doc.createTextNode(this.strHM_SELLER_ASSIGNED_IDENTIFIER_FOR_A_PARTY));

                  //Ship to
                  Element shipTo = doc.createElement("shipTo");
                  requestForPaymentNode.appendChild(shipTo);
                  Element gln2 = doc.createElement("gln");
                  shipTo.appendChild(gln2);
                  gln2.appendChild(doc.createTextNode("7507003123772"));
                  Element nameAndAddress = doc.createElement("nameAndAddress");
                  shipTo.appendChild(nameAndAddress);

                  Element name = doc.createElement("name");
                  nameAndAddress.appendChild(name);
                  Element streetAddressOne = doc.createElement("streetAddressOne");
                  nameAndAddress.appendChild(streetAddressOne);
                  Element city = doc.createElement("city");
                  nameAndAddress.appendChild(city);
                  Element postalCode = doc.createElement("postalCode");
                  nameAndAddress.appendChild(postalCode);
                  name.appendChild(doc.createTextNode(strName));
                  streetAddressOne.appendChild(doc.createTextNode(strStreetAddressOne));
                  city.appendChild(doc.createTextNode(strCity));
                  postalCode.appendChild(doc.createTextNode(strPostalCode));

                  //currency
                  Element currency = doc.createElement("currency");
                  requestForPaymentNode.appendChild(currency);
                  currency.setAttribute("currencyISOCode", "MXN");
                  Element currencyFunction = doc.createElement("currencyFunction");
                  currency.appendChild(currencyFunction);
                  currencyFunction.appendChild(doc.createTextNode("BILLING_CURRENCY"));

                  //lineItem
                  String strSql = "select * from vta_facturasdeta where FAC_ID = " + intTransaccion;
                  ResultSet rs = oConn.runQuery(strSql, true);
                  long lngConta = 0;
                  while (rs.next()) {
                     lngConta++;

                     //lineItem
                     Element lineItem = doc.createElement("lineItem");
                     requestForPaymentNode.appendChild(lineItem);
                     lineItem.setAttribute("type", "SimpleInvoiceLineItemType");

                     lineItem.setAttribute("number", lngConta + "");
                     Element tradeItemIdentification = doc.createElement("tradeItemIdentification");
                     lineItem.appendChild(tradeItemIdentification);
                     //Codigo EAN del articulo
//                     Element gtin = doc.createElement("gtin");
//                     tradeItemIdentification.appendChild(gtin);
                     Element alternateTradeItemIdentification = doc.createElement("alternateTradeItemIdentification");
                     lineItem.appendChild(alternateTradeItemIdentification);
                     alternateTradeItemIdentification.setAttribute("type", "BUYER_ASSIGNED");
                     alternateTradeItemIdentification.appendChild(doc.createTextNode(rs.getString("FACD_CVE")));

                     Element tradeItemDescriptionInformation = doc.createElement("tradeItemDescriptionInformation");
                     lineItem.appendChild(tradeItemDescriptionInformation);
                     tradeItemDescriptionInformation.setAttribute("language", "ES");
                     Element longText = doc.createElement("longText");
                     tradeItemDescriptionInformation.appendChild(longText);
                     longText.appendChild(doc.createTextNode(rs.getString("FACD_DESCRIPCION")));

                     Element invoicedQuantity = doc.createElement("invoicedQuantity");
                     lineItem.appendChild(invoicedQuantity);
                     String strUnidadMedida = rs.getString("FACD_UNIDAD_MEDIDA");
                     if (strUnidadMedida.isEmpty()) {
                        strUnidadMedida = "NO APLICA";
                     }
                     invoicedQuantity.setAttribute("unitOfMeasure", strUnidadMedida);
                     invoicedQuantity.appendChild(doc.createTextNode(rs.getString("FACD_CANTIDAD")));

                     //Importes
                     double dblImporteCIVA = rs.getDouble("FACD_IMPORTE");
                     double dblIVA = rs.getDouble("FACD_IMPUESTO1");
                     double dblTasaIVA = rs.getDouble("FACD_TASAIVA1");
                     double dblImporteLine = dblImporteCIVA - dblIVA;
                     Element grossPrice = doc.createElement("grossPrice");
                     lineItem.appendChild(grossPrice);
                     Element amount = doc.createElement("Amount");
                     grossPrice.appendChild(amount);
                     amount.appendChild(doc.createTextNode(NumberString.FormatearDecimal(dblImporteLine, 2).replace(",", "")));
                     Element netPrice = doc.createElement("grossPrice");
                     lineItem.appendChild(netPrice);
                     Element amount2 = doc.createElement("Amount");
                     netPrice.appendChild(amount2);
                     amount2.appendChild(doc.createTextNode(NumberString.FormatearDecimal(dblImporteLine, 2).replace(",", "")));

                     Element tradeItemTaxInformation = doc.createElement("tradeItemTaxInformation");
                     lineItem.appendChild(tradeItemTaxInformation);
                     Element taxTypeDescription = doc.createElement("taxTypeDescription");
                     tradeItemTaxInformation.appendChild(taxTypeDescription);
                     taxTypeDescription.appendChild(doc.createTextNode("VAT"));
                     Element referenceNumber = doc.createElement("referenceNumber");
                     tradeItemTaxInformation.appendChild(referenceNumber);
                     referenceNumber.appendChild(doc.createTextNode("V0"));
                     Element tradeItemTaxAmount = doc.createElement("tradeItemTaxAmount");
                     tradeItemTaxInformation.appendChild(tradeItemTaxAmount);
                     Element taxPercentage = doc.createElement("taxPercentage");
                     tradeItemTaxAmount.appendChild(taxPercentage);
                     taxPercentage.appendChild(doc.createTextNode(NumberString.FormatearDecimal(dblTasaIVA, 2).replace(",", "")));
                     Element taxAmount = doc.createElement("taxAmount");
                     tradeItemTaxAmount.appendChild(taxAmount);
                     taxAmount.appendChild(doc.createTextNode(NumberString.FormatearDecimal(dblIVA, 2).replace(",", "")));
                     Element taxCategory = doc.createElement("taxCategory");
                     tradeItemTaxInformation.appendChild(taxCategory);
                     taxCategory.appendChild(doc.createTextNode("TRANSFERIDO"));

                     Element totalLineAmount = doc.createElement("totalLineAmount");
                     lineItem.appendChild(totalLineAmount);

                     Element grossAmount = doc.createElement("grossAmount");
                     totalLineAmount.appendChild(grossAmount);
                     Element Amount = doc.createElement("Amount");
                     grossAmount.appendChild(Amount);
                     Amount.appendChild(doc.createTextNode(NumberString.FormatearDecimal(dblImporteLine, 2).replace(",", "")));

                     Element netAmount = doc.createElement("netAmount");
                     totalLineAmount.appendChild(netAmount);
                     Element Amount2 = doc.createElement("Amount");
                     netAmount.appendChild(Amount2);
                     Amount2.appendChild(doc.createTextNode(NumberString.FormatearDecimal(dblImporteLine, 2).replace(",", "")));
                     //End line item
                  }
                  //totalAmount
                  Element totalAmount = doc.createElement("totalAmount");
                  requestForPaymentNode.appendChild(totalAmount);
                  Element Amount4 = doc.createElement("Amount");
                  totalAmount.appendChild(Amount4);
                  Amount4.appendChild(doc.createTextNode(NumberString.FormatearDecimal(this.dblImporte, 2).replace(",", "")));

                  //baseAmount
                  Element baseAmount = doc.createElement("baseAmount");
                  requestForPaymentNode.appendChild(baseAmount);
                  Element Amount5 = doc.createElement("Amount");
                  baseAmount.appendChild(Amount5);
                  Amount5.appendChild(doc.createTextNode(NumberString.FormatearDecimal(this.dblImporte, 2).replace(",", "")));

                  //tax
                  Element tax = doc.createElement("tax");
                  requestForPaymentNode.appendChild(tax);
                  tax.setAttribute("type", "VAT");
                  Element taxPercentage = doc.createElement("taxPercentage");
                  tax.appendChild(taxPercentage);
                  taxPercentage.appendChild(doc.createTextNode(NumberString.FormatearDecimal(dblTasaImpuesto1, 2).replace(",", "")));
                  Element taxAmount = doc.createElement("taxAmount");
                  tax.appendChild(taxAmount);
                  taxAmount.appendChild(doc.createTextNode(NumberString.FormatearDecimal(this.dblImpuesto1, 2).replace(",", "")));
                  Element taxCategory = doc.createElement("taxCategory");
                  tax.appendChild(taxCategory);
                  taxCategory.appendChild(doc.createTextNode("TRANSFERIDO"));

                  //payableAmount
                  Element payableAmount = doc.createElement("payableAmount");
                  requestForPaymentNode.appendChild(payableAmount);
                  Element Amount6 = doc.createElement("Amount");
                  payableAmount.appendChild(Amount6);
                  Amount6.appendChild(doc.createTextNode(NumberString.FormatearDecimal(this.dblTotal, 2).replace(",", "")));

                  //Agregamos nodo raiz de la addenda
                  addendaNode.appendChild(requestForPaymentNode);

               }
            }
         }
         //Save XML
         DOMSource source = new DOMSource(doc);
         // PrintStream will be responsible for writing
         // the text data to the file
         PrintStream ps = new PrintStream(strPath);
         StreamResult result = new StreamResult(ps);
         // Once again we are using a factory of some sort,
         // this time for getting a Transformer instance,
         // which we use to output the XML
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
         // The actual output to a file goes here
         transformer.transform(source, result);
      } catch (Exception e) {
         System.out.println("error " + e.getMessage());
      }
   }
   // </editor-fold>
}
