
package Core.FirmasElectronicas.SAT3_2.cancelacion;

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
@WebServiceClient(name = "CancelaCFDService", targetNamespace = "http://cancelacfd.sat.gob.mx", wsdlLocation = "https://cancelacion.facturaelectronica.sat.gob.mx/Cancelacion/CancelaCFDService.svc?singleWsdl")
public class CancelaCFDService
    extends Service
{

    private final static URL CANCELACFDSERVICE_WSDL_LOCATION;
    private final static WebServiceException CANCELACFDSERVICE_EXCEPTION;
    private final static QName CANCELACFDSERVICE_QNAME = new QName("http://cancelacfd.sat.gob.mx", "CancelaCFDService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("https://cancelacion.facturaelectronica.sat.gob.mx/Cancelacion/CancelaCFDService.svc?singleWsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        CANCELACFDSERVICE_WSDL_LOCATION = url;
        CANCELACFDSERVICE_EXCEPTION = e;
    }

    public CancelaCFDService() {
        super(__getWsdlLocation(), CANCELACFDSERVICE_QNAME);
    }

    public CancelaCFDService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    /**
     * 
     * @return
     *     returns ICancelaCFDBinding
     */
    @WebEndpoint(name = "BasicHttpBinding_ICancelaCFDBinding")
    public ICancelaCFDBinding getBasicHttpBindingICancelaCFDBinding() {
        return super.getPort(new QName("http://cancelacfd.sat.gob.mx", "BasicHttpBinding_ICancelaCFDBinding"), ICancelaCFDBinding.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ICancelaCFDBinding
     */
    @WebEndpoint(name = "BasicHttpBinding_ICancelaCFDBinding")
    public ICancelaCFDBinding getBasicHttpBindingICancelaCFDBinding(WebServiceFeature... features) {
        return super.getPort(new QName("http://cancelacfd.sat.gob.mx", "BasicHttpBinding_ICancelaCFDBinding"), ICancelaCFDBinding.class, features);
    }

    private static URL __getWsdlLocation() {
        if (CANCELACFDSERVICE_EXCEPTION!= null) {
            throw CANCELACFDSERVICE_EXCEPTION;
        }
        return CANCELACFDSERVICE_WSDL_LOCATION;
    }

}