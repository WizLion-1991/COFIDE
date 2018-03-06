//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantaci\u00f3n de la referencia de enlace (JAXB) XML v2.2.5-2 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perder\u00e1n si se vuelve a compilar el esquema de origen. 
// Generado el: AM.02.03 a las 10:21:16 AM CST 
//


package Core.FirmasElectronicas.complementos.nominas;

import Core.FirmasElectronicas.Addendas.DateFormatterDate;
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
 *         &lt;element name="Percepciones" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Percepcion" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="TipoPercepcion" use="required">
 *                             &lt;simpleType>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *                                 &lt;minInclusive value="1"/>
 *                                 &lt;pattern value="[0-9]{3}"/>
 *                               &lt;/restriction>
 *                             &lt;/simpleType>
 *                           &lt;/attribute>
 *                           &lt;attribute name="Clave" use="required">
 *                             &lt;simpleType>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                 &lt;minLength value="3"/>
 *                                 &lt;maxLength value="15"/>
 *                                 &lt;whiteSpace value="collapse"/>
 *                               &lt;/restriction>
 *                             &lt;/simpleType>
 *                           &lt;/attribute>
 *                           &lt;attribute name="Concepto" use="required">
 *                             &lt;simpleType>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                 &lt;minLength value="1"/>
 *                                 &lt;maxLength value="100"/>
 *                               &lt;/restriction>
 *                             &lt;/simpleType>
 *                           &lt;/attribute>
 *                           &lt;attribute name="ImporteGravado" use="required" type="{http://www.sat.gob.mx/nomina}t_Importe" />
 *                           &lt;attribute name="ImporteExento" use="required" type="{http://www.sat.gob.mx/nomina}t_Importe" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="TotalGravado" use="required" type="{http://www.sat.gob.mx/nomina}t_Importe" />
 *                 &lt;attribute name="TotalExento" use="required" type="{http://www.sat.gob.mx/nomina}t_Importe" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Deducciones" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Deduccion" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="TipoDeduccion" use="required">
 *                             &lt;simpleType>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *                                 &lt;minInclusive value="1"/>
 *                                 &lt;pattern value="[0-9]{3}"/>
 *                               &lt;/restriction>
 *                             &lt;/simpleType>
 *                           &lt;/attribute>
 *                           &lt;attribute name="Clave" use="required">
 *                             &lt;simpleType>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                 &lt;minLength value="3"/>
 *                                 &lt;maxLength value="15"/>
 *                                 &lt;whiteSpace value="collapse"/>
 *                               &lt;/restriction>
 *                             &lt;/simpleType>
 *                           &lt;/attribute>
 *                           &lt;attribute name="Concepto" use="required">
 *                             &lt;simpleType>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                 &lt;minLength value="1"/>
 *                                 &lt;maxLength value="100"/>
 *                               &lt;/restriction>
 *                             &lt;/simpleType>
 *                           &lt;/attribute>
 *                           &lt;attribute name="ImporteGravado" use="required" type="{http://www.sat.gob.mx/nomina}t_Importe" />
 *                           &lt;attribute name="ImporteExento" use="required" type="{http://www.sat.gob.mx/nomina}t_Importe" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="TotalGravado" use="required" type="{http://www.sat.gob.mx/nomina}t_Importe" />
 *                 &lt;attribute name="TotalExento" use="required" type="{http://www.sat.gob.mx/nomina}t_Importe" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Incapacidad" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="DiasIncapacidad" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                       &lt;fractionDigits value="6"/>
 *                       &lt;maxInclusive value="1"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="TipoIncapacidad" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                 &lt;attribute name="Descuento" use="required" type="{http://www.sat.gob.mx/nomina}t_Importe" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="HorasExtra" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="Dias" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                 &lt;attribute name="TipoHoras" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;whiteSpace value="collapse"/>
 *                       &lt;enumeration value="Dobles"/>
 *                       &lt;enumeration value="Triples"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="HorasExtra" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                 &lt;attribute name="ImportePagado" use="required" type="{http://www.sat.gob.mx/nomina}t_Importe" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" fixed="1.1" />
 *       &lt;attribute name="RegistroPatronal" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="20"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="NumEmpleado" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="15"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="CURP" use="required" type="{http://www.sat.gob.mx/nomina}t_CURP" />
 *       &lt;attribute name="TipoRegimen" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;minInclusive value="1"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="NumSeguridadSocial" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="15"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="FechaPago" use="required" type="{http://www.w3.org/2001/XMLSchema}date" />
 *       &lt;attribute name="FechaInicialPago" use="required" type="{http://www.w3.org/2001/XMLSchema}date" />
 *       &lt;attribute name="FechaFinalPago" use="required" type="{http://www.w3.org/2001/XMLSchema}date" />
 *       &lt;attribute name="NumDiasPagados" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *             &lt;fractionDigits value="6"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="Departamento">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="100"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="CLABE">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.sat.gob.mx/nomina}t_Clabe">
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="Banco">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;minInclusive value="1"/>
 *             &lt;pattern value="[0-9]{3}"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="FechaInicioRelLaboral" use="required" type="{http://www.w3.org/2001/XMLSchema}date" />
 *       &lt;attribute name="Antiguedad" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="Puesto" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="TipoContrato" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="TipoJornada" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="PeriodicidadPago" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="100"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="SalarioBaseCotApor" use="required" type="{http://www.sat.gob.mx/nomina}t_Importe" />
 *       &lt;attribute name="RiesgoPuesto" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;minInclusive value="1"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="SalarioDiarioIntegrado" use="required" type="{http://www.sat.gob.mx/nomina}t_Importe" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "percepciones",
    "deducciones",
    "incapacidad",
    "horasExtra"
})
@XmlRootElement(name = "Nomina")
public class Nomina {

    @XmlElement(name = "Percepciones")
    protected Nomina.Percepciones percepciones;
    @XmlElement(name = "Deducciones")
    protected Nomina.Deducciones deducciones;
    @XmlElement(name = "Incapacidad")
    protected List<Nomina.Incapacidad> incapacidad;
    @XmlElement(name = "HorasExtra")
    protected List<Nomina.HorasExtra> horasExtra;
    @XmlAttribute(name = "Version", required = true)
    protected String version;
    @XmlAttribute(name = "RegistroPatronal", required = true)
    protected String registroPatronal;
    @XmlAttribute(name = "NumEmpleado", required = true)
    protected String numEmpleado;
    @XmlAttribute(name = "CURP", required = true)
    protected String curp;
    @XmlAttribute(name = "TipoRegimen", required = true)
    protected int tipoRegimen;
    @XmlAttribute(name = "NumSeguridadSocial", required = true)
    protected String numSeguridadSocial;
    @XmlAttribute(name = "FechaPago", required = true)
    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(DateFormatterDate.class)
    protected Date fechaPago;
    @XmlAttribute(name = "FechaInicialPago", required = true)
    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(DateFormatterDate.class)
    protected Date fechaInicialPago;
    @XmlAttribute(name = "FechaFinalPago", required = true)
    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(DateFormatterDate.class)
    protected Date fechaFinalPago;
    @XmlAttribute(name = "NumDiasPagados", required = true)
    protected BigDecimal numDiasPagados;
    @XmlAttribute(name = "Departamento")
    protected String departamento;
    @XmlAttribute(name = "CLABE")
    protected String clabe;
    @XmlAttribute(name = "Banco")
    protected String banco;
    @XmlAttribute(name = "FechaInicioRelLaboral", required = true)
    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(DateFormatterDate.class)
    protected Date fechaInicioRelLaboral;
    @XmlAttribute(name = "Antiguedad", required = true)
    protected int antiguedad;
    @XmlAttribute(name = "Puesto", required = true)
    protected String puesto;
    @XmlAttribute(name = "TipoContrato", required = true)
    protected String tipoContrato;
    @XmlAttribute(name = "TipoJornada", required = true)
    protected String tipoJornada;
    @XmlAttribute(name = "PeriodicidadPago", required = true)
    protected String periodicidadPago;
    @XmlAttribute(name = "SalarioBaseCotApor", required = true)
    protected BigDecimal salarioBaseCotApor;
    @XmlAttribute(name = "RiesgoPuesto",required=false)
    protected Integer riesgoPuesto;
    @XmlAttribute(name = "SalarioDiarioIntegrado", required = true)
    protected BigDecimal salarioDiarioIntegrado;

    /**
     * Obtiene el valor de la propiedad percepciones.
     * 
     * @return
     *     possible object is
     *     {@link Nomina.Percepciones }
     *     
     */
    public Nomina.Percepciones getPercepciones() {
        return percepciones;
    }

    /**
     * Define el valor de la propiedad percepciones.
     * 
     * @param value
     *     allowed object is
     *     {@link Nomina.Percepciones }
     *     
     */
    public void setPercepciones(Nomina.Percepciones value) {
        this.percepciones = value;
    }

    /**
     * Obtiene el valor de la propiedad deducciones.
     * 
     * @return
     *     possible object is
     *     {@link Nomina.Deducciones }
     *     
     */
    public Nomina.Deducciones getDeducciones() {
        return deducciones;
    }

    /**
     * Define el valor de la propiedad deducciones.
     * 
     * @param value
     *     allowed object is
     *     {@link Nomina.Deducciones }
     *     
     */
    public void setDeducciones(Nomina.Deducciones value) {
        this.deducciones = value;
    }

    /**
     * Gets the value of the incapacidad property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the incapacidad property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIncapacidad().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Nomina.Incapacidad }
     * 
     * 
     */
    public List<Nomina.Incapacidad> getIncapacidad() {
        if (incapacidad == null) {
            incapacidad = new ArrayList<Nomina.Incapacidad>();
        }
        return this.incapacidad;
    }

    /**
     * Gets the value of the horasExtra property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the horasExtra property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHorasExtra().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Nomina.HorasExtra }
     * 
     * 
     */
    public List<Nomina.HorasExtra> getHorasExtra() {
        if (horasExtra == null) {
            horasExtra = new ArrayList<Nomina.HorasExtra>();
        }
        return this.horasExtra;
    }

    /**
     * Obtiene el valor de la propiedad version.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        if (version == null) {
            return "1.1";
        } else {
            return version;
        }
    }

    /**
     * Define el valor de la propiedad version.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Obtiene el valor de la propiedad registroPatronal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegistroPatronal() {
        return registroPatronal;
    }

    /**
     * Define el valor de la propiedad registroPatronal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegistroPatronal(String value) {
        this.registroPatronal = value;
    }

    /**
     * Obtiene el valor de la propiedad numEmpleado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumEmpleado() {
        return numEmpleado;
    }

    /**
     * Define el valor de la propiedad numEmpleado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumEmpleado(String value) {
        this.numEmpleado = value;
    }

    /**
     * Obtiene el valor de la propiedad curp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCURP() {
        return curp;
    }

    /**
     * Define el valor de la propiedad curp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCURP(String value) {
        this.curp = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoRegimen.
     * 
     */
    public int getTipoRegimen() {
        return tipoRegimen;
    }

    /**
     * Define el valor de la propiedad tipoRegimen.
     * 
     */
    public void setTipoRegimen(int value) {
        this.tipoRegimen = value;
    }

    /**
     * Obtiene el valor de la propiedad numSeguridadSocial.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumSeguridadSocial() {
        return numSeguridadSocial;
    }

    /**
     * Define el valor de la propiedad numSeguridadSocial.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumSeguridadSocial(String value) {
        this.numSeguridadSocial = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaPago.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public Date getFechaPago() {
        return fechaPago;
    }

    /**
     * Define el valor de la propiedad fechaPago.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaPago(Date value) {
        this.fechaPago = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaInicialPago.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public Date getFechaInicialPago() {
        return fechaInicialPago;
    }

    /**
     * Define el valor de la propiedad fechaInicialPago.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaInicialPago(Date value) {
        this.fechaInicialPago = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaFinalPago.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public Date getFechaFinalPago() {
        return fechaFinalPago;
    }

    /**
     * Define el valor de la propiedad fechaFinalPago.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaFinalPago(Date value) {
        this.fechaFinalPago = value;
    }

    /**
     * Obtiene el valor de la propiedad numDiasPagados.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNumDiasPagados() {
        return numDiasPagados;
    }

    /**
     * Define el valor de la propiedad numDiasPagados.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNumDiasPagados(BigDecimal value) {
        this.numDiasPagados = value;
    }

    /**
     * Obtiene el valor de la propiedad departamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDepartamento() {
        return departamento;
    }

    /**
     * Define el valor de la propiedad departamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDepartamento(String value) {
        this.departamento = value;
    }

    /**
     * Obtiene el valor de la propiedad clabe.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public String getCLABE() {
        return clabe;
    }

    /**
     * Define el valor de la propiedad clabe.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCLABE(String value) {
        this.clabe = value;
    }

    /**
     * Obtiene el valor de la propiedad banco.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public String getBanco() {
        return banco;
    }

    /**
     * Define el valor de la propiedad banco.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBanco(String value) {
        this.banco = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaInicioRelLaboral.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public Date getFechaInicioRelLaboral() {
        return fechaInicioRelLaboral;
    }

    /**
     * Define el valor de la propiedad fechaInicioRelLaboral.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaInicioRelLaboral(Date value) {
        this.fechaInicioRelLaboral = value;
    }

    /**
     * Obtiene el valor de la propiedad antiguedad.
     * 
     */
    public int getAntiguedad() {
        return antiguedad;
    }

    /**
     * Define el valor de la propiedad antiguedad.
     * 
     */
    public void setAntiguedad(int value) {
        this.antiguedad = value;
    }

    /**
     * Obtiene el valor de la propiedad puesto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPuesto() {
        return puesto;
    }

    /**
     * Define el valor de la propiedad puesto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPuesto(String value) {
        this.puesto = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoContrato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoContrato() {
        return tipoContrato;
    }

    /**
     * Define el valor de la propiedad tipoContrato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoContrato(String value) {
        this.tipoContrato = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoJornada.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoJornada() {
        return tipoJornada;
    }

    /**
     * Define el valor de la propiedad tipoJornada.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoJornada(String value) {
        this.tipoJornada = value;
    }

    /**
     * Obtiene el valor de la propiedad periodicidadPago.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPeriodicidadPago() {
        return periodicidadPago;
    }

    /**
     * Define el valor de la propiedad periodicidadPago.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPeriodicidadPago(String value) {
        this.periodicidadPago = value;
    }

    /**
     * Obtiene el valor de la propiedad salarioBaseCotApor.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSalarioBaseCotApor() {
        return salarioBaseCotApor;
    }

    /**
     * Define el valor de la propiedad salarioBaseCotApor.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSalarioBaseCotApor(BigDecimal value) {
        this.salarioBaseCotApor = value;
    }

    /**
     * Obtiene el valor de la propiedad riesgoPuesto.
     * 
     */
    public int getRiesgoPuesto() {
        return riesgoPuesto;
    }

    /**
     * Define el valor de la propiedad riesgoPuesto.
     * 
     */
    public void setRiesgoPuesto(int value) {
        this.riesgoPuesto = value;
    }

    /**
     * Obtiene el valor de la propiedad salarioDiarioIntegrado.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSalarioDiarioIntegrado() {
        return salarioDiarioIntegrado;
    }

    /**
     * Define el valor de la propiedad salarioDiarioIntegrado.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSalarioDiarioIntegrado(BigDecimal value) {
        this.salarioDiarioIntegrado = value;
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
     *         &lt;element name="Deduccion" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="TipoDeduccion" use="required">
     *                   &lt;simpleType>
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
     *                       &lt;minInclusive value="1"/>
     *                       &lt;pattern value="[0-9]{3}"/>
     *                     &lt;/restriction>
     *                   &lt;/simpleType>
     *                 &lt;/attribute>
     *                 &lt;attribute name="Clave" use="required">
     *                   &lt;simpleType>
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                       &lt;minLength value="3"/>
     *                       &lt;maxLength value="15"/>
     *                       &lt;whiteSpace value="collapse"/>
     *                     &lt;/restriction>
     *                   &lt;/simpleType>
     *                 &lt;/attribute>
     *                 &lt;attribute name="Concepto" use="required">
     *                   &lt;simpleType>
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                       &lt;minLength value="1"/>
     *                       &lt;maxLength value="100"/>
     *                     &lt;/restriction>
     *                   &lt;/simpleType>
     *                 &lt;/attribute>
     *                 &lt;attribute name="ImporteGravado" use="required" type="{http://www.sat.gob.mx/nomina}t_Importe" />
     *                 &lt;attribute name="ImporteExento" use="required" type="{http://www.sat.gob.mx/nomina}t_Importe" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="TotalGravado" use="required" type="{http://www.sat.gob.mx/nomina}t_Importe" />
     *       &lt;attribute name="TotalExento" use="required" type="{http://www.sat.gob.mx/nomina}t_Importe" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "deduccion"
    })
    public static class Deducciones {

        @XmlElement(name = "Deduccion", required = true)
        protected List<Nomina.Deducciones.Deduccion> deduccion;
        @XmlAttribute(name = "TotalGravado", required = true)
        protected BigDecimal totalGravado;
        @XmlAttribute(name = "TotalExento", required = true)
        protected BigDecimal totalExento;

        /**
         * Gets the value of the deduccion property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the deduccion property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDeduccion().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Nomina.Deducciones.Deduccion }
         * 
         * 
         */
        public List<Nomina.Deducciones.Deduccion> getDeduccion() {
            if (deduccion == null) {
                deduccion = new ArrayList<Nomina.Deducciones.Deduccion>();
            }
            return this.deduccion;
        }

        /**
         * Obtiene el valor de la propiedad totalGravado.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getTotalGravado() {
            return totalGravado;
        }

        /**
         * Define el valor de la propiedad totalGravado.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setTotalGravado(BigDecimal value) {
            this.totalGravado = value;
        }

        /**
         * Obtiene el valor de la propiedad totalExento.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getTotalExento() {
            return totalExento;
        }

        /**
         * Define el valor de la propiedad totalExento.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setTotalExento(BigDecimal value) {
            this.totalExento = value;
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
         *       &lt;attribute name="TipoDeduccion" use="required">
         *         &lt;simpleType>
         *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
         *             &lt;minInclusive value="1"/>
         *             &lt;pattern value="[0-9]{3}"/>
         *           &lt;/restriction>
         *         &lt;/simpleType>
         *       &lt;/attribute>
         *       &lt;attribute name="Clave" use="required">
         *         &lt;simpleType>
         *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *             &lt;minLength value="3"/>
         *             &lt;maxLength value="15"/>
         *             &lt;whiteSpace value="collapse"/>
         *           &lt;/restriction>
         *         &lt;/simpleType>
         *       &lt;/attribute>
         *       &lt;attribute name="Concepto" use="required">
         *         &lt;simpleType>
         *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *             &lt;minLength value="1"/>
         *             &lt;maxLength value="100"/>
         *           &lt;/restriction>
         *         &lt;/simpleType>
         *       &lt;/attribute>
         *       &lt;attribute name="ImporteGravado" use="required" type="{http://www.sat.gob.mx/nomina}t_Importe" />
         *       &lt;attribute name="ImporteExento" use="required" type="{http://www.sat.gob.mx/nomina}t_Importe" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Deduccion {

            @XmlAttribute(name = "TipoDeduccion", required = true)
            protected String tipoDeduccion;
            @XmlAttribute(name = "Clave", required = true)
            protected String clave;
            @XmlAttribute(name = "Concepto", required = true)
            protected String concepto;
            @XmlAttribute(name = "ImporteGravado", required = true)
            protected BigDecimal importeGravado;
            @XmlAttribute(name = "ImporteExento", required = true)
            protected BigDecimal importeExento;

            /**
             * Obtiene el valor de la propiedad tipoDeduccion.
             * 
             */
            public String getTipoDeduccion() {
                return tipoDeduccion;
            }

            /**
             * Define el valor de la propiedad tipoDeduccion.
             * 
             */
            public void setTipoDeduccion(String value) {
                this.tipoDeduccion = value;
            }

            /**
             * Obtiene el valor de la propiedad clave.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getClave() {
                return clave;
            }

            /**
             * Define el valor de la propiedad clave.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setClave(String value) {
                this.clave = value;
            }

            /**
             * Obtiene el valor de la propiedad concepto.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getConcepto() {
                return concepto;
            }

            /**
             * Define el valor de la propiedad concepto.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setConcepto(String value) {
                this.concepto = value;
            }

            /**
             * Obtiene el valor de la propiedad importeGravado.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getImporteGravado() {
                return importeGravado;
            }

            /**
             * Define el valor de la propiedad importeGravado.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setImporteGravado(BigDecimal value) {
                this.importeGravado = value;
            }

            /**
             * Obtiene el valor de la propiedad importeExento.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getImporteExento() {
                return importeExento;
            }

            /**
             * Define el valor de la propiedad importeExento.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setImporteExento(BigDecimal value) {
                this.importeExento = value;
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
     *       &lt;attribute name="Dias" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
     *       &lt;attribute name="TipoHoras" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;whiteSpace value="collapse"/>
     *             &lt;enumeration value="Dobles"/>
     *             &lt;enumeration value="Triples"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="HorasExtra" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
     *       &lt;attribute name="ImportePagado" use="required" type="{http://www.sat.gob.mx/nomina}t_Importe" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class HorasExtra {

        @XmlAttribute(name = "Dias", required = true)
        protected int dias;
        @XmlAttribute(name = "TipoHoras", required = true)
        protected String tipoHoras;
        @XmlAttribute(name = "HorasExtra", required = true)
        protected int horasExtra;
        @XmlAttribute(name = "ImportePagado", required = true)
        protected BigDecimal importePagado;

        /**
         * Obtiene el valor de la propiedad dias.
         * 
         */
        public int getDias() {
            return dias;
        }

        /**
         * Define el valor de la propiedad dias.
         * 
         */
        public void setDias(int value) {
            this.dias = value;
        }

        /**
         * Obtiene el valor de la propiedad tipoHoras.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTipoHoras() {
            return tipoHoras;
        }

        /**
         * Define el valor de la propiedad tipoHoras.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTipoHoras(String value) {
            this.tipoHoras = value;
        }

        /**
         * Obtiene el valor de la propiedad horasExtra.
         * 
         */
        public int getHorasExtra() {
            return horasExtra;
        }

        /**
         * Define el valor de la propiedad horasExtra.
         * 
         */
        public void setHorasExtra(int value) {
            this.horasExtra = value;
        }

        /**
         * Obtiene el valor de la propiedad importePagado.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getImportePagado() {
            return importePagado;
        }

        /**
         * Define el valor de la propiedad importePagado.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setImportePagado(BigDecimal value) {
            this.importePagado = value;
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
     *       &lt;attribute name="DiasIncapacidad" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *             &lt;fractionDigits value="6"/>
     *             &lt;maxInclusive value="1"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="TipoIncapacidad" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
     *       &lt;attribute name="Descuento" use="required" type="{http://www.sat.gob.mx/nomina}t_Importe" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Incapacidad {

        @XmlAttribute(name = "DiasIncapacidad", required = true)
        protected BigDecimal diasIncapacidad;
        @XmlAttribute(name = "TipoIncapacidad", required = true)
        protected int tipoIncapacidad;
        @XmlAttribute(name = "Descuento", required = true)
        protected BigDecimal descuento;

        /**
         * Obtiene el valor de la propiedad diasIncapacidad.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getDiasIncapacidad() {
            return diasIncapacidad;
        }

        /**
         * Define el valor de la propiedad diasIncapacidad.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setDiasIncapacidad(BigDecimal value) {
            this.diasIncapacidad = value;
        }

        /**
         * Obtiene el valor de la propiedad tipoIncapacidad.
         * 
         */
        public int getTipoIncapacidad() {
            return tipoIncapacidad;
        }

        /**
         * Define el valor de la propiedad tipoIncapacidad.
         * 
         */
        public void setTipoIncapacidad(int value) {
            this.tipoIncapacidad = value;
        }

        /**
         * Obtiene el valor de la propiedad descuento.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getDescuento() {
            return descuento;
        }

        /**
         * Define el valor de la propiedad descuento.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setDescuento(BigDecimal value) {
            this.descuento = value;
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
     *         &lt;element name="Percepcion" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="TipoPercepcion" use="required">
     *                   &lt;simpleType>
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
     *                       &lt;minInclusive value="1"/>
     *                       &lt;pattern value="[0-9]{3}"/>
     *                     &lt;/restriction>
     *                   &lt;/simpleType>
     *                 &lt;/attribute>
     *                 &lt;attribute name="Clave" use="required">
     *                   &lt;simpleType>
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                       &lt;minLength value="3"/>
     *                       &lt;maxLength value="15"/>
     *                       &lt;whiteSpace value="collapse"/>
     *                     &lt;/restriction>
     *                   &lt;/simpleType>
     *                 &lt;/attribute>
     *                 &lt;attribute name="Concepto" use="required">
     *                   &lt;simpleType>
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                       &lt;minLength value="1"/>
     *                       &lt;maxLength value="100"/>
     *                     &lt;/restriction>
     *                   &lt;/simpleType>
     *                 &lt;/attribute>
     *                 &lt;attribute name="ImporteGravado" use="required" type="{http://www.sat.gob.mx/nomina}t_Importe" />
     *                 &lt;attribute name="ImporteExento" use="required" type="{http://www.sat.gob.mx/nomina}t_Importe" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="TotalGravado" use="required" type="{http://www.sat.gob.mx/nomina}t_Importe" />
     *       &lt;attribute name="TotalExento" use="required" type="{http://www.sat.gob.mx/nomina}t_Importe" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "percepcion"
    })
    public static class Percepciones {

        @XmlElement(name = "Percepcion", required = true)
        protected List<Nomina.Percepciones.Percepcion> percepcion;
        @XmlAttribute(name = "TotalGravado", required = true)
        protected BigDecimal totalGravado;
        @XmlAttribute(name = "TotalExento", required = true)
        protected BigDecimal totalExento;

        /**
         * Gets the value of the percepcion property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the percepcion property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPercepcion().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Nomina.Percepciones.Percepcion }
         * 
         * 
         */
        public List<Nomina.Percepciones.Percepcion> getPercepcion() {
            if (percepcion == null) {
                percepcion = new ArrayList<Nomina.Percepciones.Percepcion>();
            }
            return this.percepcion;
        }

        /**
         * Obtiene el valor de la propiedad totalGravado.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getTotalGravado() {
            return totalGravado;
        }

        /**
         * Define el valor de la propiedad totalGravado.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setTotalGravado(BigDecimal value) {
            this.totalGravado = value;
        }

        /**
         * Obtiene el valor de la propiedad totalExento.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getTotalExento() {
            return totalExento;
        }

        /**
         * Define el valor de la propiedad totalExento.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setTotalExento(BigDecimal value) {
            this.totalExento = value;
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
         *       &lt;attribute name="TipoPercepcion" use="required">
         *         &lt;simpleType>
         *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
         *             &lt;minInclusive value="1"/>
         *             &lt;pattern value="[0-9]{3}"/>
         *           &lt;/restriction>
         *         &lt;/simpleType>
         *       &lt;/attribute>
         *       &lt;attribute name="Clave" use="required">
         *         &lt;simpleType>
         *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *             &lt;minLength value="3"/>
         *             &lt;maxLength value="15"/>
         *             &lt;whiteSpace value="collapse"/>
         *           &lt;/restriction>
         *         &lt;/simpleType>
         *       &lt;/attribute>
         *       &lt;attribute name="Concepto" use="required">
         *         &lt;simpleType>
         *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *             &lt;minLength value="1"/>
         *             &lt;maxLength value="100"/>
         *           &lt;/restriction>
         *         &lt;/simpleType>
         *       &lt;/attribute>
         *       &lt;attribute name="ImporteGravado" use="required" type="{http://www.sat.gob.mx/nomina}t_Importe" />
         *       &lt;attribute name="ImporteExento" use="required" type="{http://www.sat.gob.mx/nomina}t_Importe" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Percepcion {

            @XmlAttribute(name = "TipoPercepcion", required = true)
            protected String tipoPercepcion;
            @XmlAttribute(name = "Clave", required = true)
            protected String clave;
            @XmlAttribute(name = "Concepto", required = true)
            protected String concepto;
            @XmlAttribute(name = "ImporteGravado", required = true)
            protected BigDecimal importeGravado;
            @XmlAttribute(name = "ImporteExento", required = true)
            protected BigDecimal importeExento;

            /**
             * Obtiene el valor de la propiedad tipoPercepcion.
             * 
             */
            public String getTipoPercepcion() {
                return tipoPercepcion;
            }

            /**
             * Define el valor de la propiedad tipoPercepcion.
             * 
             */
            public void setTipoPercepcion(String value) {
                this.tipoPercepcion = value;
            }

            /**
             * Obtiene el valor de la propiedad clave.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getClave() {
                return clave;
            }

            /**
             * Define el valor de la propiedad clave.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setClave(String value) {
                this.clave = value;
            }

            /**
             * Obtiene el valor de la propiedad concepto.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getConcepto() {
                return concepto;
            }

            /**
             * Define el valor de la propiedad concepto.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setConcepto(String value) {
                this.concepto = value;
            }

            /**
             * Obtiene el valor de la propiedad importeGravado.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getImporteGravado() {
                return importeGravado;
            }

            /**
             * Define el valor de la propiedad importeGravado.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setImporteGravado(BigDecimal value) {
                this.importeGravado = value;
            }

            /**
             * Obtiene el valor de la propiedad importeExento.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getImporteExento() {
                return importeExento;
            }

            /**
             * Define el valor de la propiedad importeExento.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setImporteExento(BigDecimal value) {
                this.importeExento = value;
            }

        }

    }

}
