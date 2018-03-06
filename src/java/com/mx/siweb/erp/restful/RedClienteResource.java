/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mx.siweb.erp.restful;

import com.mx.siweb.erp.restful.entity.Hijo;
import com.mx.siweb.erp.restful.entity.Papa;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.apache.logging.log4j.LogManager;

/**
 * REST Web Service
 *
 * @author siweb
 */
@Path("RedCliente")
public class RedClienteResource {

    @Context
    private UriInfo context;
    @Context
    private HttpServletRequest servletRequest;
    @Context
    private javax.servlet.ServletContext servletContext;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(RedClienteResource.class.getName());

    /**
     * Creates a new instance of RedClienteResource
     */
    public RedClienteResource() {
    }

    /**
     * Retrieves representation of an instance of com.mx.siweb.erp.restful.RedClienteResource
     * @param strCodigo
     * @param idPapa
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public Papa getJson(@DefaultValue("") @QueryParam("Codigo") String strCodigo,
                        @DefaultValue("") @QueryParam("idPapa") String idPapa,
                        @DefaultValue("") @QueryParam("Periodo") String  strPeriodo) {
        Papa pa = new Papa();

        VariableSession varSesiones = new VariableSession(servletRequest);
        
        try {
            //Abrimos la conexión
            Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
            oConn.open();
            //Se valida si se tiene acceso 
            EvalSesion eval = new EvalSesion();
            log.debug("Validando secion");
            if (eval.evaluaSesion(strCodigo, oConn)) {
                log.debug("Secion Valida");
                varSesiones.getVars();
                if (eval.evaluaPermiso(1, oConn) || eval.isBolEsCliente()) {
                    log.debug("Permiso Valido");
                    String consultaPapa = "select * from vta_cliente where CT_ID = " + idPapa;
                    ResultSet res = oConn.runQuery(consultaPapa);
                    while(res.next()){
                        pa.setIdPapa(res.getInt("CT_ID"));
                        pa.setNombrePapa(res.getString("CT_RAZONSOCIAL"));
                    }
                    res.close();                  
                    
                    String consulta = "SELECT CT_ID,CT_UPLINE,CT_RAZONSOCIAL,CT_TELEFONO1,\n"
                       + "CT_TELEFONO2,CT_EMAIL1,CT_ARMADODEEP,CT_ARMADOINI,CT_ARMADOFIN,\n"
                       + "\n"
                       + "getMLMDataComision(vta_cliente.CT_ID," + strPeriodo + ",'CO_PUNTOS_P') as PPUNTOS,\n"
                       + "\n"
                       + "getMLMDataComision(vta_cliente.CT_ID," + strPeriodo + ",'CO_NEGOCIO_P') as PNEGOCIO,\n"
                       + "\n"
                       + "getMLMDataComision(vta_cliente.CT_ID," + strPeriodo + ",'CO_PUNTOS_G') as GPUNTOS,\n"
                       + "\n"
                       + "getMLMDataComision(vta_cliente.CT_ID," + strPeriodo + ",'CO_NEGOCIO_G') as GNEGOCIO,\n"
                       + "\n"
                       + "getMLMDataComision(vta_cliente.CT_ID," + strPeriodo + ",'CO_IMPORTE') as COMISION,\n"
                       + "\n"
                       + "getMLMDataNivelRed(vta_cliente.CT_ID," + strPeriodo + ") AS NIVELRED,	vta_empresas.EMP_PATHIMGFORM as Logo,\n"
                       + "\n"
                       + "vta_empresas.EMP_IMGCUERPO as ImagenFondo FROM vta_cliente \n"
                       + "Join vta_empresas On vta_empresas.EMP_ID = vta_cliente.EMP_ID WHERE \n"
                       + "CT_ARMADONUM>=(SELECT CT_ARMADOINI FROM vta_cliente where CT_ID =" + idPapa 
                       + " ) AND \n"
                       + "CT_ARMADONUM<=(SELECT CT_ARMADOFIN FROM vta_cliente where CT_ID = " + idPapa
                       + ")\n"
                       + "AND if(0 = 0,1 = 1 , getMLMDataComision(vta_cliente.CT_ID," + strPeriodo + ",'CO_PUNTOS_P') > 0 OR \n"
                       + "\n"
                       + "getMLMDataComision(vta_cliente.CT_ID," + strPeriodo + ",'CO_NEGOCIO_P') > 0) ORDER BY CT_ARMADONUM;";
                    ResultSet rs = oConn.runQuery(consulta);
                    while (rs.next()) {
                        Hijo hj = new Hijo();
                        hj.setIdHijo(rs.getInt("CT_ID"));
                        hj.setNombre(rs.getString("CT_RAZONSOCIAL"));
                        hj.setIdPapa(rs.getInt("CT_UPLINE"));
                        hj.setTelefono1(rs.getString("CT_TELEFONO1"));
                        hj.setTelefono2(rs.getString("CT_TELEFONO2"));
                        hj.setEmail(rs.getString("CT_EMAIL1"));
                        hj.setArmadoDeep(rs.getInt("CT_ARMADODEEP"));
                        hj.setArmadoIni(rs.getInt("CT_ARMADOINI"));
                        hj.setArmadoFin(rs.getInt("CT_ARMADOFIN"));
                        hj.setpPuntos(rs.getString("PPUNTOS"));
                        hj.setPnegocio(rs.getString("PNEGOCIO"));
                        hj.setGpuntos(rs.getString("GPUNTOS"));
                        hj.setGnegocio(rs.getString("GNEGOCIO"));
                        hj.setComision(rs.getString("COMISION"));
                        hj.setNivelRed(rs.getString("NIVELRED"));
                        
                        pa.getHijoItem().add(hj);
                    }//Fin WHILE
                    rs.close();
                    pa.setCodigo(strCodigo);
                }//Fin IF evalua Permiso
            }//Fin IF evalua Sesion
            oConn.close();
        }//Fin TRY
        catch (Exception e) {
            log.error("MobilServiceLogin1" + e.getMessage() + " " + e.getLocalizedMessage());
        }
        return pa;
    }


}
