//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.5-2 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: PM.04.20 a las 02:25:20 PM CDT 
//


package com.mx.siweb.sat.retenciones.dividendos;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element name="DividOUtil" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="CveTipDivOUtil" use="required" type="{http://www.sat.gob.mx/esquemas/retencionpago/1/catalogos}c_TipoDividendoOUtilidadDistribuida" />
 *                 &lt;attribute name="MontISRAcredRetMexico" use="required" type="{http://www.sat.gob.mx/esquemas/retencionpago/1/dividendos}t_Importe" />
 *                 &lt;attribute name="MontISRAcredRetExtranjero" use="required" type="{http://www.sat.gob.mx/esquemas/retencionpago/1/dividendos}t_Importe" />
 *                 &lt;attribute name="MontRetExtDivExt" type="{http://www.sat.gob.mx/esquemas/retencionpago/1/dividendos}t_Importe" />
 *                 &lt;attribute name="TipoSocDistrDiv" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;enumeration value="Sociedad Nacional"/>
 *                       &lt;enumeration value="Sociedad Extranjera"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="MontISRAcredNal" type="{http://www.sat.gob.mx/esquemas/retencionpago/1/dividendos}t_Importe" />
 *                 &lt;attribute name="MontDivAcumNal" type="{http://www.sat.gob.mx/esquemas/retencionpago/1/dividendos}t_Importe" />
 *                 &lt;attribute name="MontDivAcumExt" type="{http://www.sat.gob.mx/esquemas/retencionpago/1/dividendos}t_Importe" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Remanente" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="ProporcionRem">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                       &lt;fractionDigits value="6"/>
 *                       &lt;maxInclusive value="9999999999"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Version" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" fixed="1.0" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "dividOUtil",
    "remanente"
})
@XmlRootElement(name = "Dividendos")
public class Dividendos {

    @XmlElement(name = "DividOUtil")
    protected Dividendos.DividOUtil dividOUtil;
    @XmlElement(name = "Remanente")
    protected Dividendos.Remanente remanente;
    @XmlAttribute(name = "Version", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String version;

    /**
     * Obtiene el valor de la propiedad dividOUtil.
     * 
     * @return
     *     possible object is
     *     {@link Dividendos.DividOUtil }
     *     
     */
    public Dividendos.DividOUtil getDividOUtil() {
        return dividOUtil;
    }

    /**
     * Define el valor de la propiedad dividOUtil.
     * 
     * @param value
     *     allowed object is
     *     {@link Dividendos.DividOUtil }
     *     
     */
    public void setDividOUtil(Dividendos.DividOUtil value) {
        this.dividOUtil = value;
    }

    /**
     * Obtiene el valor de la propiedad remanente.
     * 
     * @return
     *     possible object is
     *     {@link Dividendos.Remanente }
     *     
     */
    public Dividendos.Remanente getRemanente() {
        return remanente;
    }

    /**
     * Define el valor de la propiedad remanente.
     * 
     * @param value
     *     allowed object is
     *     {@link Dividendos.Remanente }
     *     
     */
    public void setRemanente(Dividendos.Remanente value) {
        this.remanente = value;
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
            return "1.0";
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
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="CveTipDivOUtil" use="required" type="{http://www.sat.gob.mx/esquemas/retencionpago/1/catalogos}c_TipoDividendoOUtilidadDistribuida" />
     *       &lt;attribute name="MontISRAcredRetMexico" use="required" type="{http://www.sat.gob.mx/esquemas/retencionpago/1/dividendos}t_Importe" />
     *       &lt;attribute name="MontISRAcredRetExtranjero" use="required" type="{http://www.sat.gob.mx/esquemas/retencionpago/1/dividendos}t_Importe" />
     *       &lt;attribute name="MontRetExtDivExt" type="{http://www.sat.gob.mx/esquemas/retencionpago/1/dividendos}t_Importe" />
     *       &lt;attribute name="TipoSocDistrDiv" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;enumeration value="Sociedad Nacional"/>
     *             &lt;enumeration value="Sociedad Extranjera"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="MontISRAcredNal" type="{http://www.sat.gob.mx/esquemas/retencionpago/1/dividendos}t_Importe" />
     *       &lt;attribute name="MontDivAcumNal" type="{http://www.sat.gob.mx/esquemas/retencionpago/1/dividendos}t_Importe" />
     *       &lt;attribute name="MontDivAcumExt" type="{http://www.sat.gob.mx/esquemas/retencionpago/1/dividendos}t_Importe" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class DividOUtil {

        @XmlAttribute(name = "CveTipDivOUtil", required = true)
        protected String cveTipDivOUtil;
        @XmlAttribute(name = "MontISRAcredRetMexico", required = true)
        protected BigDecimal montISRAcredRetMexico;
        @XmlAttribute(name = "MontISRAcredRetExtranjero", required = true)
        protected BigDecimal montISRAcredRetExtranjero;
        @XmlAttribute(name = "MontRetExtDivExt")
        protected BigDecimal montRetExtDivExt;
        @XmlAttribute(name = "TipoSocDistrDiv", required = true)
        protected String tipoSocDistrDiv;
        @XmlAttribute(name = "MontISRAcredNal")
        protected BigDecimal montISRAcredNal;
        @XmlAttribute(name = "MontDivAcumNal")
        protected BigDecimal montDivAcumNal;
        @XmlAttribute(name = "MontDivAcumExt")
        protected BigDecimal montDivAcumExt;

        /**
         * Obtiene el valor de la propiedad cveTipDivOUtil.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCveTipDivOUtil() {
            return cveTipDivOUtil;
        }

        /**
         * Define el valor de la propiedad cveTipDivOUtil.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCveTipDivOUtil(String value) {
            this.cveTipDivOUtil = value;
        }

        /**
         * Obtiene el valor de la propiedad montISRAcredRetMexico.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getMontISRAcredRetMexico() {
            return montISRAcredRetMexico;
        }

        /**
         * Define el valor de la propiedad montISRAcredRetMexico.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setMontISRAcredRetMexico(BigDecimal value) {
            this.montISRAcredRetMexico = value;
        }

        /**
         * Obtiene el valor de la propiedad montISRAcredRetExtranjero.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getMontISRAcredRetExtranjero() {
            return montISRAcredRetExtranjero;
        }

        /**
         * Define el valor de la propiedad montISRAcredRetExtranjero.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setMontISRAcredRetExtranjero(BigDecimal value) {
            this.montISRAcredRetExtranjero = value;
        }

        /**
         * Obtiene el valor de la propiedad montRetExtDivExt.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getMontRetExtDivExt() {
            return montRetExtDivExt;
        }

        /**
         * Define el valor de la propiedad montRetExtDivExt.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setMontRetExtDivExt(BigDecimal value) {
            this.montRetExtDivExt = value;
        }

        /**
         * Obtiene el valor de la propiedad tipoSocDistrDiv.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTipoSocDistrDiv() {
            return tipoSocDistrDiv;
        }

        /**
         * Define el valor de la propiedad tipoSocDistrDiv.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTipoSocDistrDiv(String value) {
            this.tipoSocDistrDiv = value;
        }

        /**
         * Obtiene el valor de la propiedad montISRAcredNal.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getMontISRAcredNal() {
            return montISRAcredNal;
        }

        /**
         * Define el valor de la propiedad montISRAcredNal.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setMontISRAcredNal(BigDecimal value) {
            this.montISRAcredNal = value;
        }

        /**
         * Obtiene el valor de la propiedad montDivAcumNal.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getMontDivAcumNal() {
            return montDivAcumNal;
        }

        /**
         * Define el valor de la propiedad montDivAcumNal.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setMontDivAcumNal(BigDecimal value) {
            this.montDivAcumNal = value;
        }

        /**
         * Obtiene el valor de la propiedad montDivAcumExt.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getMontDivAcumExt() {
            return montDivAcumExt;
        }

        /**
         * Define el valor de la propiedad montDivAcumExt.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setMontDivAcumExt(BigDecimal value) {
            this.montDivAcumExt = value;
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
     *       &lt;attribute name="ProporcionRem">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *             &lt;fractionDigits value="6"/>
     *             &lt;maxInclusive value="9999999999"/>
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
    public static class Remanente {

        @XmlAttribute(name = "ProporcionRem")
        protected BigDecimal proporcionRem;

        /**
         * Obtiene el valor de la propiedad proporcionRem.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getProporcionRem() {
            return proporcionRem;
        }

        /**
         * Define el valor de la propiedad proporcionRem.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setProporcionRem(BigDecimal value) {
            this.proporcionRem = value;
        }

    }

}
