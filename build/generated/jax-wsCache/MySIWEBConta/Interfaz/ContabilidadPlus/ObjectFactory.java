
package Interfaz.ContabilidadPlus;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the Interfaz.ContabilidadPlus package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DoCleanPeriodoResponse_QNAME = new QName("http://ws.contabilidad.siweb.mx.com/", "doCleanPeriodoResponse");
    private final static QName _DoPolizaAutoCancel_QNAME = new QName("http://ws.contabilidad.siweb.mx.com/", "doPolizaAutoCancel");
    private final static QName _DoPolizaAutoResponse_QNAME = new QName("http://ws.contabilidad.siweb.mx.com/", "doPolizaAutoResponse");
    private final static QName _DoEvalUserResponse_QNAME = new QName("http://ws.contabilidad.siweb.mx.com/", "doEvalUserResponse");
    private final static QName _DoCleanPeriodo_QNAME = new QName("http://ws.contabilidad.siweb.mx.com/", "doCleanPeriodo");
    private final static QName _DoPolizaAuto_QNAME = new QName("http://ws.contabilidad.siweb.mx.com/", "doPolizaAuto");
    private final static QName _DoPolizaAutoCancelResponse_QNAME = new QName("http://ws.contabilidad.siweb.mx.com/", "doPolizaAutoCancelResponse");
    private final static QName _DoEvalUser_QNAME = new QName("http://ws.contabilidad.siweb.mx.com/", "doEvalUser");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: Interfaz.ContabilidadPlus
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DoCleanPeriodoResponse }
     * 
     */
    public DoCleanPeriodoResponse createDoCleanPeriodoResponse() {
        return new DoCleanPeriodoResponse();
    }

    /**
     * Create an instance of {@link DoPolizaAutoCancel }
     * 
     */
    public DoPolizaAutoCancel createDoPolizaAutoCancel() {
        return new DoPolizaAutoCancel();
    }

    /**
     * Create an instance of {@link DoPolizaAutoResponse }
     * 
     */
    public DoPolizaAutoResponse createDoPolizaAutoResponse() {
        return new DoPolizaAutoResponse();
    }

    /**
     * Create an instance of {@link DoEvalUserResponse }
     * 
     */
    public DoEvalUserResponse createDoEvalUserResponse() {
        return new DoEvalUserResponse();
    }

    /**
     * Create an instance of {@link DoCleanPeriodo }
     * 
     */
    public DoCleanPeriodo createDoCleanPeriodo() {
        return new DoCleanPeriodo();
    }

    /**
     * Create an instance of {@link DoPolizaAutoCancelResponse }
     * 
     */
    public DoPolizaAutoCancelResponse createDoPolizaAutoCancelResponse() {
        return new DoPolizaAutoCancelResponse();
    }

    /**
     * Create an instance of {@link DoPolizaAuto }
     * 
     */
    public DoPolizaAuto createDoPolizaAuto() {
        return new DoPolizaAuto();
    }

    /**
     * Create an instance of {@link DoEvalUser }
     * 
     */
    public DoEvalUser createDoEvalUser() {
        return new DoEvalUser();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoCleanPeriodoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.contabilidad.siweb.mx.com/", name = "doCleanPeriodoResponse")
    public JAXBElement<DoCleanPeriodoResponse> createDoCleanPeriodoResponse(DoCleanPeriodoResponse value) {
        return new JAXBElement<DoCleanPeriodoResponse>(_DoCleanPeriodoResponse_QNAME, DoCleanPeriodoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoPolizaAutoCancel }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.contabilidad.siweb.mx.com/", name = "doPolizaAutoCancel")
    public JAXBElement<DoPolizaAutoCancel> createDoPolizaAutoCancel(DoPolizaAutoCancel value) {
        return new JAXBElement<DoPolizaAutoCancel>(_DoPolizaAutoCancel_QNAME, DoPolizaAutoCancel.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoPolizaAutoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.contabilidad.siweb.mx.com/", name = "doPolizaAutoResponse")
    public JAXBElement<DoPolizaAutoResponse> createDoPolizaAutoResponse(DoPolizaAutoResponse value) {
        return new JAXBElement<DoPolizaAutoResponse>(_DoPolizaAutoResponse_QNAME, DoPolizaAutoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoEvalUserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.contabilidad.siweb.mx.com/", name = "doEvalUserResponse")
    public JAXBElement<DoEvalUserResponse> createDoEvalUserResponse(DoEvalUserResponse value) {
        return new JAXBElement<DoEvalUserResponse>(_DoEvalUserResponse_QNAME, DoEvalUserResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoCleanPeriodo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.contabilidad.siweb.mx.com/", name = "doCleanPeriodo")
    public JAXBElement<DoCleanPeriodo> createDoCleanPeriodo(DoCleanPeriodo value) {
        return new JAXBElement<DoCleanPeriodo>(_DoCleanPeriodo_QNAME, DoCleanPeriodo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoPolizaAuto }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.contabilidad.siweb.mx.com/", name = "doPolizaAuto")
    public JAXBElement<DoPolizaAuto> createDoPolizaAuto(DoPolizaAuto value) {
        return new JAXBElement<DoPolizaAuto>(_DoPolizaAuto_QNAME, DoPolizaAuto.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoPolizaAutoCancelResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.contabilidad.siweb.mx.com/", name = "doPolizaAutoCancelResponse")
    public JAXBElement<DoPolizaAutoCancelResponse> createDoPolizaAutoCancelResponse(DoPolizaAutoCancelResponse value) {
        return new JAXBElement<DoPolizaAutoCancelResponse>(_DoPolizaAutoCancelResponse_QNAME, DoPolizaAutoCancelResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoEvalUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.contabilidad.siweb.mx.com/", name = "doEvalUser")
    public JAXBElement<DoEvalUser> createDoEvalUser(DoEvalUser value) {
        return new JAXBElement<DoEvalUser>(_DoEvalUser_QNAME, DoEvalUser.class, null, value);
    }

}
