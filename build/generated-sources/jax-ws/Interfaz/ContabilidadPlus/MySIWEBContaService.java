
package Interfaz.ContabilidadPlus;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01 
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "MySIWEBContaService", targetNamespace = "http://ws.contabilidad.siweb.mx.com/", wsdlLocation = "http://localhost:8084/ContabilidadPlus/MySIWEBConta?wsdl")
public class MySIWEBContaService
    extends Service
{

    private final static URL MYSIWEBCONTASERVICE_WSDL_LOCATION;
    private final static WebServiceException MYSIWEBCONTASERVICE_EXCEPTION;
    private final static QName MYSIWEBCONTASERVICE_QNAME = new QName("http://ws.contabilidad.siweb.mx.com/", "MySIWEBContaService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:8084/ContabilidadPlus/MySIWEBConta?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        MYSIWEBCONTASERVICE_WSDL_LOCATION = url;
        MYSIWEBCONTASERVICE_EXCEPTION = e;
    }

    public MySIWEBContaService() {
        super(__getWsdlLocation(), MYSIWEBCONTASERVICE_QNAME);
    }

    public MySIWEBContaService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    /**
     * 
     * @return
     *     returns MySIWEBConta
     */
    @WebEndpoint(name = "MySIWEBContaPort")
    public MySIWEBConta getMySIWEBContaPort() {
        return super.getPort(new QName("http://ws.contabilidad.siweb.mx.com/", "MySIWEBContaPort"), MySIWEBConta.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns MySIWEBConta
     */
    @WebEndpoint(name = "MySIWEBContaPort")
    public MySIWEBConta getMySIWEBContaPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://ws.contabilidad.siweb.mx.com/", "MySIWEBContaPort"), MySIWEBConta.class, features);
    }

    private static URL __getWsdlLocation() {
        if (MYSIWEBCONTASERVICE_EXCEPTION!= null) {
            throw MYSIWEBCONTASERVICE_EXCEPTION;
        }
        return MYSIWEBCONTASERVICE_WSDL_LOCATION;
    }

}
