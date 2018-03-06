/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mx.siweb.erp.restful;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author ZeusGalindo
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

   @Override
   public Set<Class<?>> getClasses() {
      Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
      addRestResourceClasses(resources);
      return resources;
   }

   /**
    * Do not modify addRestResourceClasses() method.
    * It is automatically populated with
    * all resources defined in the project.
    * If required, comment out calling this method in getClasses().
    */
   private void addRestResourceClasses(Set<Class<?>> resources) {
      resources.add(com.mx.siweb.erp.restful.ArticulosResource.class);
      resources.add(com.mx.siweb.erp.restful.CambioPasswordResource.class);
      resources.add(com.mx.siweb.erp.restful.ClientesResource.class);
      resources.add(com.mx.siweb.erp.restful.DatProveedoresResource.class);
      resources.add(com.mx.siweb.erp.restful.MobilserviceResource.class);
      resources.add(com.mx.siweb.erp.restful.MobilserviceExit.class);
      resources.add(com.mx.siweb.erp.restful.ObtenerCategoriasProducto.class);
      resources.add(com.mx.siweb.erp.restful.ObtenerParidadResource.class);
      resources.add(com.mx.siweb.erp.especiales.cofide.restful.PedidosResource.class);
      resources.add(com.mx.siweb.erp.restful.ProductosCategorias.class);
      resources.add(com.mx.siweb.erp.restful.ProductosEcommResource.class);
      resources.add(com.mx.siweb.erp.restful.Reports.class);
      resources.add(com.mx.siweb.erp.restful.ServiceComprasResourceResource.class);
      resources.add(com.mx.siweb.erp.restful.ServiceclienteResource.class);
      resources.add(com.mx.siweb.erp.restful.SugerenciasResource.class);
      resources.add(com.mx.siweb.erp.restful.VentasResource.class);
      resources.add(com.mx.siweb.erp.restful.EtiquetasResource.class);
      resources.add(com.mx.siweb.erp.especiales.cofide.restful.LlamadaResource.class);
//      resources.add(com.mx.siweb.erp.especiales.cofide.restful.DatosFacturacionResource.class);
//      resources.add(com.mx.siweb.erp.especiales.cofide.restful.PedidosResource.class);
   }

}
