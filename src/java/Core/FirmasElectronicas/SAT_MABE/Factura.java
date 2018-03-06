//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantaci\u00f3n de la referencia de enlace (JAXB) XML v2.2.5-2 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perder\u00e1n si se vuelve a compilar el esquema de origen. 
// Generado el: AM.10.02 a las 11:10:37 AM CDT 
//


package Core.FirmasElectronicas.SAT_MABE;

import Core.FirmasElectronicas.Addendas.ShortDateFormatterGuion;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Moneda">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="tipoMoneda" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;length value="3"/>
 *                       &lt;enumeration value="MXN"/>
 *                       &lt;enumeration value="USD"/>
 *                       &lt;enumeration value="YEN"/>
 *                       &lt;enumeration value="VEF"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="tipoCambio" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *                 &lt;attribute name="importeConLetra" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Proveedor">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="codigo" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;minLength value="1"/>
 *                       &lt;maxLength value="9"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Entrega">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="plantaEntrega" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;minLength value="3"/>
 *                       &lt;maxLength value="4"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="calle">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;minLength value="1"/>
 *                       &lt;maxLength value="35"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="noExterior">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;minLength value="1"/>
 *                       &lt;whiteSpace value="collapse"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="noInterior">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;minLength value="1"/>
 *                       &lt;whiteSpace value="collapse"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="codigoPostal">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;minLength value="1"/>
 *                       &lt;maxLength value="5"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Detalles">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Detalle" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="noLineaArticulo" use="required">
 *                             &lt;simpleType>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                                 &lt;totalDigits value="3"/>
 *                               &lt;/restriction>
 *                             &lt;/simpleType>
 *                           &lt;/attribute>
 *                           &lt;attribute name="codigoArticulo" use="required">
 *                             &lt;simpleType>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                 &lt;minLength value="1"/>
 *                                 &lt;maxLength value="47"/>
 *                               &lt;/restriction>
 *                             &lt;/simpleType>
 *                           &lt;/attribute>
 *                           &lt;attribute name="descripcion" use="required">
 *                             &lt;simpleType>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                 &lt;minLength value="1"/>
 *                                 &lt;maxLength value="50"/>
 *                               &lt;/restriction>
 *                             &lt;/simpleType>
 *                           &lt;/attribute>
 *                           &lt;attribute name="unidad" use="required">
 *                             &lt;simpleType>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                 &lt;minLength value="1"/>
 *                                 &lt;maxLength value="3"/>
 *                               &lt;/restriction>
 *                             &lt;/simpleType>
 *                           &lt;/attribute>
 *                           &lt;attribute name="cantidad" use="required" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *                           &lt;attribute name="precioSinIva" use="required" type="{http://recepcionfe.mabempresa.com/cfd/addenda/v1}t_Importe" />
 *                           &lt;attribute name="precioConIva" type="{http://recepcionfe.mabempresa.com/cfd/addenda/v1}t_Importe" />
 *                           &lt;attribute name="importeSinIva" use="required" type="{http://recepcionfe.mabempresa.com/cfd/addenda/v1}t_Importe" />
 *                           &lt;attribute name="importeConIva" type="{http://recepcionfe.mabempresa.com/cfd/addenda/v1}t_Importe" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Descuentos" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="tipo" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;minLength value="1"/>
 *                       &lt;maxLength value="10"/>
 *                       &lt;enumeration value="NA"/>
 *                       &lt;enumeration value="CARGO"/>
 *                       &lt;enumeration value="DESCUENTO"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="descripcion">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;minLength value="1"/>
 *                       &lt;maxLength value="35"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="importe" use="required" type="{http://recepcionfe.mabempresa.com/cfd/addenda/v1}t_Importe" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Subtotal">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="importe" use="required" type="{http://recepcionfe.mabempresa.com/cfd/addenda/v1}t_Importe" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Traslados" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Traslado" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="tipo" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="tasa" use="required" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *                           &lt;attribute name="importe" use="required" type="{http://recepcionfe.mabempresa.com/cfd/addenda/v1}t_Importe" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Retenciones" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Retencion" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="tipo" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="tasa" use="required" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *                           &lt;attribute name="importe" use="required" type="{http://recepcionfe.mabempresa.com/cfd/addenda/v1}t_Importe" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Total">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="importe" use="required" type="{http://recepcionfe.mabempresa.com/cfd/addenda/v1}t_Importe" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="version" use="required" fixed="1.0">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="tipoDocumento" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="15"/>
 *             &lt;enumeration value="FACTURA"/>
 *             &lt;enumeration value="NOTA CREDITO"/>
 *             &lt;enumeration value="NOTA CARGO"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="folio" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="20"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="fecha" use="required" type="{http://www.w3.org/2001/XMLSchema}date" />
 *       &lt;attribute name="ordenCompra">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="35"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="referencia1">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="35"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="referencia2">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="20"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "moneda",
    "proveedor",
    "entrega",
    "detalles",
    "descuentos",
    "subtotal",
    "traslados",
    "retenciones",
    "total"
})
@XmlRootElement(name = "Factura")
public class Factura {

    @XmlElement(name = "Moneda", required = true)
    protected Factura.Moneda moneda;
    @XmlElement(name = "Proveedor", required = true)
    protected Factura.Proveedor proveedor;
    @XmlElement(name = "Entrega", required = true)
    protected Factura.Entrega entrega;
    @XmlElement(name = "Detalles", required = true)
    protected Factura.Detalles detalles;
    @XmlElement(name = "Descuentos")
    protected Factura.Descuentos descuentos;
    @XmlElement(name = "Subtotal", required = true)
    protected Factura.Subtotal subtotal;
    @XmlElement(name = "Traslados")
    protected Factura.Traslados traslados;
    @XmlElement(name = "Retenciones")
    protected Factura.Retenciones retenciones;
    @XmlElement(name = "Total", required = true)
    protected Factura.Total total;
    @XmlAttribute(name = "version", required = true)
    protected BigDecimal version;
    @XmlAttribute(name = "tipoDocumento", required = true)
    protected String tipoDocumento;
    @XmlAttribute(name = "folio", required = true)
    protected String folio;
    @XmlAttribute(name = "fecha", required = true)
    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(ShortDateFormatterGuion.class)
    protected Date fecha;
    @XmlAttribute(name = "ordenCompra")
    protected String ordenCompra;
    @XmlAttribute(name = "referencia1")
    protected String referencia1;
    @XmlAttribute(name = "referencia2")
    protected String referencia2;

    /**
     * Obtiene el valor de la propiedad moneda.
     * 
     * @return
     *     possible object is
     *     {@link Factura.Moneda }
     *     
     */
    public Factura.Moneda getMoneda() {
        return moneda;
    }

    /**
     * Define el valor de la propiedad moneda.
     * 
     * @param value
     *     allowed object is
     *     {@link Factura.Moneda }
     *     
     */
    public void setMoneda(Factura.Moneda value) {
        this.moneda = value;
    }

    /**
     * Obtiene el valor de la propiedad proveedor.
     * 
     * @return
     *     possible object is
     *     {@link Factura.Proveedor }
     *     
     */
    public Factura.Proveedor getProveedor() {
        return proveedor;
    }

    /**
     * Define el valor de la propiedad proveedor.
     * 
     * @param value
     *     allowed object is
     *     {@link Factura.Proveedor }
     *     
     */
    public void setProveedor(Factura.Proveedor value) {
        this.proveedor = value;
    }

    /**
     * Obtiene el valor de la propiedad entrega.
     * 
     * @return
     *     possible object is
     *     {@link Factura.Entrega }
     *     
     */
    public Factura.Entrega getEntrega() {
        return entrega;
    }

    /**
     * Define el valor de la propiedad entrega.
     * 
     * @param value
     *     allowed object is
     *     {@link Factura.Entrega }
     *     
     */
    public void setEntrega(Factura.Entrega value) {
        this.entrega = value;
    }

    /**
     * Obtiene el valor de la propiedad detalles.
     * 
     * @return
     *     possible object is
     *     {@link Factura.Detalles }
     *     
     */
    public Factura.Detalles getDetalles() {
        return detalles;
    }

    /**
     * Define el valor de la propiedad detalles.
     * 
     * @param value
     *     allowed object is
     *     {@link Factura.Detalles }
     *     
     */
    public void setDetalles(Factura.Detalles value) {
        this.detalles = value;
    }

    /**
     * Obtiene el valor de la propiedad descuentos.
     * 
     * @return
     *     possible object is
     *     {@link Factura.Descuentos }
     *     
     */
    public Factura.Descuentos getDescuentos() {
        return descuentos;
    }

    /**
     * Define el valor de la propiedad descuentos.
     * 
     * @param value
     *     allowed object is
     *     {@link Factura.Descuentos }
     *     
     */
    public void setDescuentos(Factura.Descuentos value) {
        this.descuentos = value;
    }

    /**
     * Obtiene el valor de la propiedad subtotal.
     * 
     * @return
     *     possible object is
     *     {@link Factura.Subtotal }
     *     
     */
    public Factura.Subtotal getSubtotal() {
        return subtotal;
    }

    /**
     * Define el valor de la propiedad subtotal.
     * 
     * @param value
     *     allowed object is
     *     {@link Factura.Subtotal }
     *     
     */
    public void setSubtotal(Factura.Subtotal value) {
        this.subtotal = value;
    }

    /**
     * Obtiene el valor de la propiedad traslados.
     * 
     * @return
     *     possible object is
     *     {@link Factura.Traslados }
     *     
     */
    public Factura.Traslados getTraslados() {
        return traslados;
    }

    /**
     * Define el valor de la propiedad traslados.
     * 
     * @param value
     *     allowed object is
     *     {@link Factura.Traslados }
     *     
     */
    public void setTraslados(Factura.Traslados value) {
        this.traslados = value;
    }

    /**
     * Obtiene el valor de la propiedad retenciones.
     * 
     * @return
     *     possible object is
     *     {@link Factura.Retenciones }
     *     
     */
    public Factura.Retenciones getRetenciones() {
        return retenciones;
    }

    /**
     * Define el valor de la propiedad retenciones.
     * 
     * @param value
     *     allowed object is
     *     {@link Factura.Retenciones }
     *     
     */
    public void setRetenciones(Factura.Retenciones value) {
        this.retenciones = value;
    }

    /**
     * Obtiene el valor de la propiedad total.
     * 
     * @return
     *     possible object is
     *     {@link Factura.Total }
     *     
     */
    public Factura.Total getTotal() {
        return total;
    }

    /**
     * Define el valor de la propiedad total.
     * 
     * @param value
     *     allowed object is
     *     {@link Factura.Total }
     *     
     */
    public void setTotal(Factura.Total value) {
        this.total = value;
    }

    /**
     * Obtiene el valor de la propiedad version.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVersion() {
        if (version == null) {
            return new BigDecimal("1.0");
        } else {
            return version;
        }
    }

    /**
     * Define el valor de la propiedad version.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVersion(BigDecimal value) {
        this.version = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoDocumento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * Define el valor de la propiedad tipoDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoDocumento(String value) {
        this.tipoDocumento = value;
    }

    /**
     * Obtiene el valor de la propiedad folio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFolio() {
        return folio;
    }

    /**
     * Define el valor de la propiedad folio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFolio(String value) {
        this.folio = value;
    }

    /**
     * Obtiene el valor de la propiedad fecha.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Define el valor de la propiedad fecha.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFecha(Date value) {
        this.fecha = value;
    }

    /**
     * Obtiene el valor de la propiedad ordenCompra.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrdenCompra() {
        return ordenCompra;
    }

    /**
     * Define el valor de la propiedad ordenCompra.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrdenCompra(String value) {
        this.ordenCompra = value;
    }

    /**
     * Obtiene el valor de la propiedad referencia1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferencia1() {
        return referencia1;
    }

    /**
     * Define el valor de la propiedad referencia1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferencia1(String value) {
        this.referencia1 = value;
    }

    /**
     * Obtiene el valor de la propiedad referencia2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferencia2() {
        return referencia2;
    }

    /**
     * Define el valor de la propiedad referencia2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferencia2(String value) {
        this.referencia2 = value;
    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="tipo" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;minLength value="1"/>
     *             &lt;maxLength value="10"/>
     *             &lt;enumeration value="NA"/>
     *             &lt;enumeration value="CARGO"/>
     *             &lt;enumeration value="DESCUENTO"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="descripcion">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;minLength value="1"/>
     *             &lt;maxLength value="35"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="importe" use="required" type="{http://recepcionfe.mabempresa.com/cfd/addenda/v1}t_Importe" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Descuentos {

        @XmlAttribute(name = "tipo", required = true)
        protected String tipo;
        @XmlAttribute(name = "descripcion")
        protected String descripcion;
        @XmlAttribute(name = "importe", required = true)
        protected BigDecimal importe;

        /**
         * Obtiene el valor de la propiedad tipo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTipo() {
            return tipo;
        }

        /**
         * Define el valor de la propiedad tipo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTipo(String value) {
            this.tipo = value;
        }

        /**
         * Obtiene el valor de la propiedad descripcion.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescripcion() {
            return descripcion;
        }

        /**
         * Define el valor de la propiedad descripcion.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescripcion(String value) {
            this.descripcion = value;
        }

        /**
         * Obtiene el valor de la propiedad importe.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getImporte() {
            return importe;
        }

        /**
         * Define el valor de la propiedad importe.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setImporte(BigDecimal value) {
            this.importe = value;
        }

    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Detalle" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="noLineaArticulo" use="required">
     *                   &lt;simpleType>
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *                       &lt;totalDigits value="3"/>
     *                     &lt;/restriction>
     *                   &lt;/simpleType>
     *                 &lt;/attribute>
     *                 &lt;attribute name="codigoArticulo" use="required">
     *                   &lt;simpleType>
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                       &lt;minLength value="1"/>
     *                       &lt;maxLength value="47"/>
     *                     &lt;/restriction>
     *                   &lt;/simpleType>
     *                 &lt;/attribute>
     *                 &lt;attribute name="descripcion" use="required">
     *                   &lt;simpleType>
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                       &lt;minLength value="1"/>
     *                       &lt;maxLength value="50"/>
     *                     &lt;/restriction>
     *                   &lt;/simpleType>
     *                 &lt;/attribute>
     *                 &lt;attribute name="unidad" use="required">
     *                   &lt;simpleType>
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                       &lt;minLength value="1"/>
     *                       &lt;maxLength value="3"/>
     *                     &lt;/restriction>
     *                   &lt;/simpleType>
     *                 &lt;/attribute>
     *                 &lt;attribute name="cantidad" use="required" type="{http://www.w3.org/2001/XMLSchema}decimal" />
     *                 &lt;attribute name="precioSinIva" use="required" type="{http://recepcionfe.mabempresa.com/cfd/addenda/v1}t_Importe" />
     *                 &lt;attribute name="precioConIva" type="{http://recepcionfe.mabempresa.com/cfd/addenda/v1}t_Importe" />
     *                 &lt;attribute name="importeSinIva" use="required" type="{http://recepcionfe.mabempresa.com/cfd/addenda/v1}t_Importe" />
     *                 &lt;attribute name="importeConIva" type="{http://recepcionfe.mabempresa.com/cfd/addenda/v1}t_Importe" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "detalle"
    })
    public static class Detalles {

        @XmlElement(name = "Detalle", required = true)
        protected List<Factura.Detalles.Detalle> detalle;

        /**
         * Gets the value of the detalle property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the detalle property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDetalle().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Factura.Detalles.Detalle }
         * 
         * 
         */
        public List<Factura.Detalles.Detalle> getDetalle() {
            if (detalle == null) {
                detalle = new ArrayList<Factura.Detalles.Detalle>();
            }
            return this.detalle;
        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="noLineaArticulo" use="required">
         *         &lt;simpleType>
         *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
         *             &lt;totalDigits value="3"/>
         *           &lt;/restriction>
         *         &lt;/simpleType>
         *       &lt;/attribute>
         *       &lt;attribute name="codigoArticulo" use="required">
         *         &lt;simpleType>
         *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *             &lt;minLength value="1"/>
         *             &lt;maxLength value="47"/>
         *           &lt;/restriction>
         *         &lt;/simpleType>
         *       &lt;/attribute>
         *       &lt;attribute name="descripcion" use="required">
         *         &lt;simpleType>
         *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *             &lt;minLength value="1"/>
         *             &lt;maxLength value="50"/>
         *           &lt;/restriction>
         *         &lt;/simpleType>
         *       &lt;/attribute>
         *       &lt;attribute name="unidad" use="required">
         *         &lt;simpleType>
         *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *             &lt;minLength value="1"/>
         *             &lt;maxLength value="3"/>
         *           &lt;/restriction>
         *         &lt;/simpleType>
         *       &lt;/attribute>
         *       &lt;attribute name="cantidad" use="required" type="{http://www.w3.org/2001/XMLSchema}decimal" />
         *       &lt;attribute name="precioSinIva" use="required" type="{http://recepcionfe.mabempresa.com/cfd/addenda/v1}t_Importe" />
         *       &lt;attribute name="precioConIva" type="{http://recepcionfe.mabempresa.com/cfd/addenda/v1}t_Importe" />
         *       &lt;attribute name="importeSinIva" use="required" type="{http://recepcionfe.mabempresa.com/cfd/addenda/v1}t_Importe" />
         *       &lt;attribute name="importeConIva" type="{http://recepcionfe.mabempresa.com/cfd/addenda/v1}t_Importe" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Detalle {

            @XmlAttribute(name = "noLineaArticulo", required = true)
            protected BigInteger noLineaArticulo;
            @XmlAttribute(name = "codigoArticulo", required = true)
            protected String codigoArticulo;
            @XmlAttribute(name = "descripcion", required = true)
            protected String descripcion;
            @XmlAttribute(name = "unidad", required = true)
            protected String unidad;
            @XmlAttribute(name = "cantidad", required = true)
            protected BigDecimal cantidad;
            @XmlAttribute(name = "precioSinIva", required = true)
            protected BigDecimal precioSinIva;
            @XmlAttribute(name = "precioConIva")
            protected BigDecimal precioConIva;
            @XmlAttribute(name = "importeSinIva", required = true)
            protected BigDecimal importeSinIva;
            @XmlAttribute(name = "importeConIva")
            protected BigDecimal importeConIva;

            /**
             * Obtiene el valor de la propiedad noLineaArticulo.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getNoLineaArticulo() {
                return noLineaArticulo;
            }

            /**
             * Define el valor de la propiedad noLineaArticulo.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setNoLineaArticulo(BigInteger value) {
                this.noLineaArticulo = value;
            }

            /**
             * Obtiene el valor de la propiedad codigoArticulo.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCodigoArticulo() {
                return codigoArticulo;
            }

            /**
             * Define el valor de la propiedad codigoArticulo.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCodigoArticulo(String value) {
                this.codigoArticulo = value;
            }

            /**
             * Obtiene el valor de la propiedad descripcion.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDescripcion() {
                return descripcion;
            }

            /**
             * Define el valor de la propiedad descripcion.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDescripcion(String value) {
                this.descripcion = value;
            }

            /**
             * Obtiene el valor de la propiedad unidad.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getUnidad() {
                return unidad;
            }

            /**
             * Define el valor de la propiedad unidad.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setUnidad(String value) {
                this.unidad = value;
            }

            /**
             * Obtiene el valor de la propiedad cantidad.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getCantidad() {
                return cantidad;
            }

            /**
             * Define el valor de la propiedad cantidad.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setCantidad(BigDecimal value) {
                this.cantidad = value;
            }

            /**
             * Obtiene el valor de la propiedad precioSinIva.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getPrecioSinIva() {
                return precioSinIva;
            }

            /**
             * Define el valor de la propiedad precioSinIva.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setPrecioSinIva(BigDecimal value) {
                this.precioSinIva = value;
            }

            /**
             * Obtiene el valor de la propiedad precioConIva.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getPrecioConIva() {
                return precioConIva;
            }

            /**
             * Define el valor de la propiedad precioConIva.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setPrecioConIva(BigDecimal value) {
                this.precioConIva = value;
            }

            /**
             * Obtiene el valor de la propiedad importeSinIva.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getImporteSinIva() {
                return importeSinIva;
            }

            /**
             * Define el valor de la propiedad importeSinIva.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setImporteSinIva(BigDecimal value) {
                this.importeSinIva = value;
            }

            /**
             * Obtiene el valor de la propiedad importeConIva.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getImporteConIva() {
                return importeConIva;
            }

            /**
             * Define el valor de la propiedad importeConIva.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setImporteConIva(BigDecimal value) {
                this.importeConIva = value;
            }

        }

    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="plantaEntrega" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;minLength value="3"/>
     *             &lt;maxLength value="4"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="calle">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;minLength value="1"/>
     *             &lt;maxLength value="35"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="noExterior">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;minLength value="1"/>
     *             &lt;whiteSpace value="collapse"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="noInterior">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;minLength value="1"/>
     *             &lt;whiteSpace value="collapse"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="codigoPostal">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;minLength value="1"/>
     *             &lt;maxLength value="5"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Entrega {

        @XmlAttribute(name = "plantaEntrega", required = true)
        protected String plantaEntrega;
        @XmlAttribute(name = "calle")
        protected String calle;
        @XmlAttribute(name = "noExterior")
        protected String noExterior;
        @XmlAttribute(name = "noInterior")
        protected String noInterior;
        @XmlAttribute(name = "codigoPostal")
        protected String codigoPostal;

        /**
         * Obtiene el valor de la propiedad plantaEntrega.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPlantaEntrega() {
            return plantaEntrega;
        }

        /**
         * Define el valor de la propiedad plantaEntrega.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPlantaEntrega(String value) {
            this.plantaEntrega = value;
        }

        /**
         * Obtiene el valor de la propiedad calle.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCalle() {
            return calle;
        }

        /**
         * Define el valor de la propiedad calle.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCalle(String value) {
            this.calle = value;
        }

        /**
         * Obtiene el valor de la propiedad noExterior.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNoExterior() {
            return noExterior;
        }

        /**
         * Define el valor de la propiedad noExterior.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNoExterior(String value) {
            this.noExterior = value;
        }

        /**
         * Obtiene el valor de la propiedad noInterior.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNoInterior() {
            return noInterior;
        }

        /**
         * Define el valor de la propiedad noInterior.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNoInterior(String value) {
            this.noInterior = value;
        }

        /**
         * Obtiene el valor de la propiedad codigoPostal.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodigoPostal() {
            return codigoPostal;
        }

        /**
         * Define el valor de la propiedad codigoPostal.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodigoPostal(String value) {
            this.codigoPostal = value;
        }

    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="tipoMoneda" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;length value="3"/>
     *             &lt;enumeration value="MXN"/>
     *             &lt;enumeration value="USD"/>
     *             &lt;enumeration value="YEN"/>
     *             &lt;enumeration value="VEF"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="tipoCambio" type="{http://www.w3.org/2001/XMLSchema}decimal" />
     *       &lt;attribute name="importeConLetra" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Moneda {

        @XmlAttribute(name = "tipoMoneda", required = true)
        protected String tipoMoneda;
        @XmlAttribute(name = "tipoCambio")
        protected BigDecimal tipoCambio;
        @XmlAttribute(name = "importeConLetra")
        protected String importeConLetra;

        /**
         * Obtiene el valor de la propiedad tipoMoneda.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTipoMoneda() {
            return tipoMoneda;
        }

        /**
         * Define el valor de la propiedad tipoMoneda.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTipoMoneda(String value) {
            this.tipoMoneda = value;
        }

        /**
         * Obtiene el valor de la propiedad tipoCambio.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getTipoCambio() {
            return tipoCambio;
        }

        /**
         * Define el valor de la propiedad tipoCambio.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setTipoCambio(BigDecimal value) {
            this.tipoCambio = value;
        }

        /**
         * Obtiene el valor de la propiedad importeConLetra.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getImporteConLetra() {
            return importeConLetra;
        }

        /**
         * Define el valor de la propiedad importeConLetra.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setImporteConLetra(String value) {
            this.importeConLetra = value;
        }

    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="codigo" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;minLength value="1"/>
     *             &lt;maxLength value="9"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Proveedor {

        @XmlAttribute(name = "codigo", required = true)
        protected String codigo;

        /**
         * Obtiene el valor de la propiedad codigo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodigo() {
            return codigo;
        }

        /**
         * Define el valor de la propiedad codigo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodigo(String value) {
            this.codigo = value;
        }

    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Retencion" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="tipo" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="tasa" use="required" type="{http://www.w3.org/2001/XMLSchema}decimal" />
     *                 &lt;attribute name="importe" use="required" type="{http://recepcionfe.mabempresa.com/cfd/addenda/v1}t_Importe" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "retencion"
    })
    public static class Retenciones {

        @XmlElement(name = "Retencion", required = true)
        protected List<Factura.Retenciones.Retencion> retencion;

        /**
         * Gets the value of the retencion property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the retencion property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getRetencion().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Factura.Retenciones.Retencion }
         * 
         * 
         */
        public List<Factura.Retenciones.Retencion> getRetencion() {
            if (retencion == null) {
                retencion = new ArrayList<Factura.Retenciones.Retencion>();
            }
            return this.retencion;
        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="tipo" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="tasa" use="required" type="{http://www.w3.org/2001/XMLSchema}decimal" />
         *       &lt;attribute name="importe" use="required" type="{http://recepcionfe.mabempresa.com/cfd/addenda/v1}t_Importe" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Retencion {

            @XmlAttribute(name = "tipo", required = true)
            protected String tipo;
            @XmlAttribute(name = "tasa", required = true)
            protected BigDecimal tasa;
            @XmlAttribute(name = "importe", required = true)
            protected BigDecimal importe;

            /**
             * Obtiene el valor de la propiedad tipo.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTipo() {
                return tipo;
            }

            /**
             * Define el valor de la propiedad tipo.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTipo(String value) {
                this.tipo = value;
            }

            /**
             * Obtiene el valor de la propiedad tasa.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getTasa() {
                return tasa;
            }

            /**
             * Define el valor de la propiedad tasa.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setTasa(BigDecimal value) {
                this.tasa = value;
            }

            /**
             * Obtiene el valor de la propiedad importe.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getImporte() {
                return importe;
            }

            /**
             * Define el valor de la propiedad importe.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setImporte(BigDecimal value) {
                this.importe = value;
            }

        }

    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="importe" use="required" type="{http://recepcionfe.mabempresa.com/cfd/addenda/v1}t_Importe" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Subtotal {

        @XmlAttribute(name = "importe", required = true)
        protected BigDecimal importe;

        /**
         * Obtiene el valor de la propiedad importe.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getImporte() {
            return importe;
        }

        /**
         * Define el valor de la propiedad importe.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setImporte(BigDecimal value) {
            this.importe = value;
        }

    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="importe" use="required" type="{http://recepcionfe.mabempresa.com/cfd/addenda/v1}t_Importe" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Total {

        @XmlAttribute(name = "importe", required = true)
        protected BigDecimal importe;

        /**
         * Obtiene el valor de la propiedad importe.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getImporte() {
            return importe;
        }

        /**
         * Define el valor de la propiedad importe.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setImporte(BigDecimal value) {
            this.importe = value;
        }

    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Traslado" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="tipo" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="tasa" use="required" type="{http://www.w3.org/2001/XMLSchema}decimal" />
     *                 &lt;attribute name="importe" use="required" type="{http://recepcionfe.mabempresa.com/cfd/addenda/v1}t_Importe" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "traslado"
    })
    public static class Traslados {

        @XmlElement(name = "Traslado", required = true)
        protected List<Factura.Traslados.Traslado> traslado;

        /**
         * Gets the value of the traslado property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the traslado property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTraslado().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Factura.Traslados.Traslado }
         * 
         * 
         */
        public List<Factura.Traslados.Traslado> getTraslado() {
            if (traslado == null) {
                traslado = new ArrayList<Factura.Traslados.Traslado>();
            }
            return this.traslado;
        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="tipo" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="tasa" use="required" type="{http://www.w3.org/2001/XMLSchema}decimal" />
         *       &lt;attribute name="importe" use="required" type="{http://recepcionfe.mabempresa.com/cfd/addenda/v1}t_Importe" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Traslado {

            @XmlAttribute(name = "tipo", required = true)
            protected String tipo;
            @XmlAttribute(name = "tasa", required = true)
            protected BigDecimal tasa;
            @XmlAttribute(name = "importe", required = true)
            protected BigDecimal importe;

            /**
             * Obtiene el valor de la propiedad tipo.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTipo() {
                return tipo;
            }

            /**
             * Define el valor de la propiedad tipo.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTipo(String value) {
                this.tipo = value;
            }

            /**
             * Obtiene el valor de la propiedad tasa.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getTasa() {
                return tasa;
            }

            /**
             * Define el valor de la propiedad tasa.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setTasa(BigDecimal value) {
                this.tasa = value;
            }

            /**
             * Obtiene el valor de la propiedad importe.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getImporte() {
                return importe;
            }

            /**
             * Define el valor de la propiedad importe.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setImporte(BigDecimal value) {
                this.importe = value;
            }

        }

    }

}
