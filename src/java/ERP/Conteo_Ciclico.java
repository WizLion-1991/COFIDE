/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import Tablas.vta_ciclico_deta;
import Tablas.vta_movproddeta;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import comSIWeb.Utilerias.UtilXml;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author siweb
 */
public class Conteo_Ciclico extends ProcesoMaster implements ProcesoInterfaz {

// <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(NCredito.class.getName());

   private String strSql;
   private int intcantidad;
   private int intmasvendido;
   private int itemsmasvendido;
   private int intsinmov;
   private int intitemsinmov;
   private int intaleatorios;
   private vta_ciclico_deta document;
   private ArrayList<DetalleConteoCiclico> detalle;
   private ArrayList<vta_ciclico_deta> deta;
   private int intSucursal;
   private int intEmpresa;
   private String strFechaInicial;
   private String strFechaFinal;

   public Conteo_Ciclico(Conexion oConn, VariableSession varSesiones) {
      super(oConn, varSesiones);
      this.document = new vta_ciclico_deta();
   }

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public Conteo_Ciclico(Conexion oConn, VariableSession varSesiones, int intSucursal, int intEmpresa, String strFechaInicial, String strFechaFinal) {
      super(oConn, varSesiones);
      this.strFechaInicial = strFechaInicial;
      this.strFechaFinal = strFechaFinal;
      this.intSucursal = intSucursal;
      this.intEmpresa = intEmpresa;
      this.document = new vta_ciclico_deta();
      detalle = new ArrayList<DetalleConteoCiclico>();

   }

   public vta_ciclico_deta getDocument() {
      return document;
   }

   public void setDocument(vta_ciclico_deta document) {
      this.document = document;
   }

   public ArrayList<vta_ciclico_deta> getDeta() {
      return deta;
   }

   public void setDeta(ArrayList<vta_ciclico_deta> deta) {
      this.deta = deta;
   }

   public String doAltaCiclicoDeta(Conexion oConn) {
      String strResult = "OK";

      //Insertamos el detalle
      Iterator<vta_ciclico_deta> it = this.deta.iterator();
      while (it.hasNext()) {
         vta_ciclico_deta deta = it.next();
//            
         String strResult2 = "";
         if (deta.getFieldInt("CCD_ID") != 0) {
            strResult2 = deta.Modifica(oConn);
         } else {
            strResult2 = deta.Agrega(oConn);
         }

         if (!strResult2.equals("OK")) {
         strResult2 = strResult;
         }
         else{
         String obtenerSucursal="SELECT * FROM vta_ciclico_master WHERE CC_ID= '"+deta.getFieldInt("CC_ID")+"'";
            try{
               ResultSet rMaster= oConn.runQuery(obtenerSucursal,true);
               while(rMaster.next()){
                  String strQuery="UPDATE vta_producto SET PR_ACTIVO='0' WHERE vta_producto.PR_CODIGO='"+deta.getFieldString("PR_CODIGO")+"'"+
                               "AND vta_producto.SC_ID='"+rMaster.getInt("CC_SUCURSAL")+"'";
                  oConn.runQueryLMD(strQuery);
               }
               rMaster.close();
            }catch(SQLException ex){strResult=""+ex;}
         }
      }

      return strResult;
   }

   /**
    * Sirve para obtener los datos de un conteo ciclico detalle
    *
    * @param strId Es el id
    * @param oConn Es la conexion
    * @return
    */
   public String getDataCiclico(int strId, Conexion oConn) {
      log.debug("aquie stoy ");
      UtilXml util = new UtilXml();

      String strXMLData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
      //partidas de la cotizacion

      //recuperamos los datos
      strXMLData += "<lstDeta>";

      //Obtenemos el id de todas las partidas que conforman el detalle
      String strSqlX = "select CCD_ID,PR_CODIGO,PR_DESCRIPCION, CCD_CANT_EXIST,CCD_TEXTIL,CCD_ADERIBLE,CCD_CONTEO1,CCD_CONTEO2,CC_ID,CCD_DIFERENCIA"
              + " from vta_ciclico_deta where CC_ID = " + strId;
      try {
         ResultSet rs = oConn.runQuery(strSqlX, true);
         while (rs.next()) {
            //vta_ciclico_deta deta = new vta_ciclico_deta();
            //Recuperamos todos los campos y los anexamos al excel actual
            this.document.setFieldInt("CCD_ID", rs.getInt("CCD_ID"));
            //this.document.setValorKey(rs.getInt("CCD_ID") + "");
            this.document.ObtenDatos(oConn);
            strXMLData += "<deta ";
            strXMLData += " Codigo= \"" + rs.getString("PR_CODIGO") + "\"";
            strXMLData += " Descripcion=\"" + rs.getString("PR_DESCRIPCION") + "\"";
            strXMLData += " Existencia=\"" + rs.getDouble("CCD_CANT_EXIST") + "\" ";

            strXMLData += "Textil=\"" + rs.getInt("CCD_TEXTIL") + "\" ";
            strXMLData += "Aderible=\"" + rs.getInt("CCD_ADERIBLE") + "\" ";
            strXMLData += "Conteo_1=\"" + rs.getDouble("CCD_CONTEO1") + "\" ";
            strXMLData += "Conteo_2=\"" + rs.getDouble("CCD_CONTEO2") + "\" ";
            strXMLData += "ID=\"" + rs.getInt("CC_ID") + "\" ";
            strXMLData += "IDD=\"" + rs.getInt("CCD_ID") + "\" ";
            strXMLData += "Diferencia=\"" + rs.getInt("CCD_DIFERENCIA") + "\" ";

            strXMLData += " />";

         }
         rs.close();
      } catch (SQLException ex) {
         Logger.getLogger(Conteo_Ciclico.class.getName()).log(Level.SEVERE, null, ex);
      }

      strXMLData += "</lstDeta>";
      return strXMLData;

   }

   public ArrayList<DetalleConteoCiclico> getDetalle() {
      return detalle;
   }

   public void setDetalle(ArrayList<DetalleConteoCiclico> detalle) {
      this.detalle = detalle;
   }

   public int getIntSucursal() {
      return intSucursal;
   }

   public void setIntSucursal(int intSucursal) {
      this.intSucursal = intSucursal;
   }

   public int getIntEmpresa() {
      return intEmpresa;
   }

   public void setIntEmpresa(int intEmpresa) {
      this.intEmpresa = intEmpresa;
   }

   public String getStrFechaInicial() {
      return strFechaInicial;
   }

   public void setStrFechaInicial(String strFechaInicial) {
      this.strFechaInicial = strFechaInicial;
   }

   public String getStrFechaFinal() {
      return strFechaFinal;
   }

   public void setStrFechaFinal(String strFechaFinal) {
      this.strFechaFinal = strFechaFinal;
   }

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   public void generaProductosMasVendidos() {
      int numeroAleatorio = (int) (Math.random() * 10 + 1);
      strSql = "SELECT CC_REGISTRO AS CANTIDAD ,CC_MASVEND,CC_ITEMSVEND AS NUMV,CC_SINMOV,CC_ITEMSINMOV AS NUMSIN,CC_ALEATORIOS  "
              + "FROM vta_sucursal WHERE EMP_ID=" + intEmpresa + " AND SC_ID=" + intSucursal;
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            intcantidad = rs.getInt("CANTIDAD");
            log.debug("cantidad " + intcantidad);
            intmasvendido = rs.getInt("CC_MASVEND");
            log.debug("% de productos vendidos " + intmasvendido);
            itemsmasvendido = rs.getInt("NUMV");
            intsinmov = rs.getInt("CC_SINMOV");
            log.debug("% de productos sin mov " + intsinmov);
            intitemsinmov = rs.getInt("NUMSIN");
            intaleatorios = rs.getInt("CC_ALEATORIOS");
            log.debug("% de productos aleatorios " + intaleatorios);
         }
         rs.close();
      } catch (SQLException ex) {
         this.strResultLast = "ERROR:" + ex.getMessage();
         ex.fillInStackTrace();
      }
      //ver cuantos items mas vendidos vamos a sacar la cantidad /% de productos mas vendidos

      int itemsvend = (int) (intcantidad * ((double) intmasvendido / (double) 100));
      System.out.println(" cuantos items vendidos " + itemsvend);
      int count = 1; //contador de items

      //SACAMOS LOS PRODUCTOS mas vendidos
      strSql = "Select  p.PR_CODIGO as codigo,p.PR_DESCRIPCION as descripcion , P.PR_EXISTENCIA AS EXISTENCIA ,sum(d.FACD_CANTIDAD) as cantidad_vendida  "
              + "from vta_facturas f, vta_facturasdeta d, vta_producto p where f.FAC_ID = d.FAC_ID AND d.PR_ID = p.PR_ID "
              + "and f.FAC_ANULADA = 0 AND f.EMP_ID = " + intEmpresa + " AND  f.SC_ID =" + intSucursal
              + " GROUP BY "
              + "p.PR_CODIGO ,p.PR_DESCRIPCION "
              + " ,(SELECT p1.PC_DESCRIPCION FRom vta_prodcat1 p1 where p1.PC_ID = p.PR_CATEGORIA1)"
              + " ,d.FACD_PRECIO ,p.PR_COSTOPROM "
              + "ORDER BY "
              + " cantidad_vendida DESC  limit " + itemsmasvendido;
      try {
         ResultSet rs = oConn.runQuery(strSql, true);

         while (rs.next() && count <= itemsvend) {
            DetalleConteoCiclico deta = new DetalleConteoCiclico();
            if (numeroAleatorio % 2 == 1) {
               deta.setStrCodigo(rs.getString("codigo"));
               deta.setStrDescripcion(rs.getString("descripcion"));
               deta.setDbCantidadVendida(rs.getDouble("cantidad_vendida"));
               deta.setDbExistencia(rs.getDouble("EXISTENCIA"));
               numeroAleatorio = (int) (Math.random() * 2 + 1);
               log.debug("Systemarticulo " + deta.getStrCodigo() + " " + deta.getStrDescripcion() + " " + deta.getDbExistencia());
               count++;
               detalle.add(deta);
               // System.out.println("si me agregue mas vendidos "+count);

            } else {
               numeroAleatorio = (int) (Math.random() * 2 + 1);
               continue;

            }

         }
         getSinMovimientos();
         getAleatorios();
         rs.close();
      } catch (SQLException ex) {
         this.strResultLast = "ERROR:" + ex.getMessage();
         ex.fillInStackTrace();
      }

   }

   public void getAleatorios() {

      int numeroAleatorio = (int) (Math.random() * 2 + 1);//generando numero aaleatorio entre 0 y 1
      // System.out.println("numero aleatorio en aleatorios "+ numeroAleatorio);
      //ver cuantos items mas vendidos vamos a sacar la cantidad /% de productos aleatorios
      int itemsaleatorios = (int) (intcantidad * ((double) intaleatorios / (double) 100));
      log.debug("cuantos items aleatorios " + itemsaleatorios);
      int count2 = 1; //contador de items

      //SACAMOS LOS PRODUCTOS aleatorios
      strSql = "Select  p.PR_CODIGO as codigo,p.PR_DESCRIPCION as descripcion , P.PR_EXISTENCIA AS EXISTENCIA ,sum(d.FACD_CANTIDAD) as cantidad_vendida  "
              + "from vta_facturas f, vta_facturasdeta d, vta_producto p where f.FAC_ID = d.FAC_ID AND d.PR_ID = p.PR_ID "
              + "and f.FAC_ANULADA = 0 AND f.EMP_ID = " + intEmpresa + " AND  f.SC_ID =" + intSucursal
              + " GROUP BY \n"
              + "p.PR_CODIGO ,p.PR_DESCRIPCION "
              + " ,(SELECT p1.PC_DESCRIPCION FRom vta_prodcat1 p1 where p1.PC_ID = p.PR_CATEGORIA1)"
              + " ,d.FACD_PRECIO ,p.PR_COSTOPROM "
              + "ORDER BY "
              + " cantidad_vendida  limit " + itemsmasvendido;
      try {
         ResultSet rs = oConn.runQuery(strSql, true);

         while (rs.next() && count2 <= itemsaleatorios) {
            DetalleConteoCiclico deta = new DetalleConteoCiclico();
            if (numeroAleatorio == 1) {
               deta.setStrCodigo(rs.getString("codigo"));

               deta.setStrDescripcion(rs.getString("descripcion"));

               deta.setDbCantidadVendida(rs.getDouble("cantidad_vendida"));

               deta.setDbCantidadVendida(rs.getDouble("EXISTENCIA"));
               log.debug("Systemarticulo " + deta.getStrCodigo() + " " + deta.getStrDescripcion() + " " + deta.getDbExistencia());
               numeroAleatorio = (int) (Math.random() * 2 + 1);

               count2++;
               detalle.add(deta);
               // System.out.println("si me aguegue aleatorios"+count2);

            } else {
               numeroAleatorio = (int) (Math.random() * 2 + 1);
               continue;

            }

         }
         rs.close();
      } catch (SQLException ex) {
         this.strResultLast = "ERROR:" + ex.getMessage();
         ex.fillInStackTrace();
      }

   }

   public void getSinMovimientos() {

      int numeroAleatorio = (int) (Math.random() * 10 + 1); //generando numero aaleatorio entre 0 y 1

      //ver cuantos items mas vendidos vamos a sacar la cantidad /% de productos aleatorios
      int itemsin = (int) (intcantidad * ((double) intsinmov / (double) 100));
      log.debug("items sin movib " + itemsin);
      int count2 = 1; //contador de items

      //SACAMOS LOS PRODUCTOS aleatorios
      strSql = "select PR_ID,PR_CODIGO AS codigo ,PR_DESCRIPCION  as descripcion,PR_EXISTENCIA as EXISTENCIA  from vta_producto where \n"
              + "(\n"
              + "select count(vta_facturasdeta.FACD_ID) from vta_facturas, vta_facturasdeta where "
              + "vta_facturas.FAC_ID =  vta_facturasdeta.FAC_ID "
              + " AND vta_facturas.FAC_ANULADA = 0 AND vta_facturas.SC_ID =" + intSucursal + " AND vta_facturas.EMP_ID = " + intEmpresa
              + " AND vta_facturas.EMP_ID = 2 AND vta_facturasdeta.PR_ID = vta_producto.PR_ID"
              + " AND vta_facturas.FAC_FECHA>= '" + strFechaInicial + "' AND vta_facturas.FAC_FECHA>= '" + strFechaFinal + "') = 0 and"
              + " ( select count(vta_ticketsdeta.TKTD_ID) from vta_tickets, vta_ticketsdeta where "
              + " vta_tickets.TKT_ID =  vta_ticketsdeta.TKT_ID "
              + "AND vta_tickets.TKT_ANULADA = 0 AND vta_tickets.SC_ID =  " + intSucursal
              + " AND vta_tickets.EMP_ID =" + intEmpresa
              + " AND vta_ticketsdeta.PR_ID = vta_producto.PR_ID"
              + " AND vta_tickets.TKT_FECHA>= '" + strFechaInicial + "'AND vta_tickets.TKT_FECHA>= '" + strFechaFinal + "'"
              + ") =0  "
              + "ORDER BY"
              + " PR_ID  limit " + intitemsinmov;
      try {
         ResultSet rs = oConn.runQuery(strSql, true);

         while (rs.next() && count2 <= itemsin) {
            DetalleConteoCiclico deta = new DetalleConteoCiclico();
            if (numeroAleatorio % 2 == 1) {
               deta.setStrCodigo(rs.getString("codigo"));
               deta.setStrDescripcion(rs.getString("descripcion"));
               //deta.setDbCantidadVendida(rs.getDouble("cantidad_vendida"));
               deta.setDbCantidadVendida(rs.getDouble("EXISTENCIA"));
               numeroAleatorio = (int) (Math.random() * 2 + 1);
               log.debug("Systemarticulo " + deta.getStrCodigo() + " " + deta.getStrDescripcion() + " " + deta.getDbExistencia());
               count2++;
               detalle.add(deta);

            } else {
               numeroAleatorio = (int) (Math.random() * 2 + 1);
               continue;

            }

         }
         rs.close();
      } catch (SQLException ex) {
         this.strResultLast = "ERROR:" + ex.getMessage();
         ex.fillInStackTrace();
      }

   }

   public void doTrxAnul() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   public void doTrxRevive() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   public void doTrxSaldo() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   public void doTrxMod() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   public void Init() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   public void doTrx() {

   }

   public String GeneraXml() {
      Iterator<DetalleConteoCiclico> it = detalle.iterator();

      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
      strXML.append("<productos>");

      while (it.hasNext()) {
         DetalleConteoCiclico deta = it.next();
         strXML.append(" <Docs");
         UtilXml algo = new UtilXml();

         strXML.append(" Codigo= \"").append(deta.getStrCodigo()).append("\" ");
         String cadena = algo.Sustituye(deta.getStrDescripcion());
         strXML.append(" Descripcion=\"").append(cadena).append("\" ");
         strXML.append(" cantidad_vendida=\"").append(deta.getDbCantidadVendida()).append("\" ");
         strXML.append(" Existencia=\"").append(deta.getDbExistencia()).append("\" ");

         strXML.append("/>");
      }
      strXML.append("</productos>");
      return strXML.toString();

   }
/////Codigo Andrew

   public String actualizarStatus(int id, int status, Conexion oConn) {
      String strListo = "OK";

      String strStatus = "UPDATE vta_ciclico_master SET CC_STATUS='" + status + "' WHERE CC_ID='" + id + "'";
      oConn.runQueryLMD(strStatus);

      return strListo;
   }

   public String aplicarInventario(Conexion oConn,int id,String strfolio_GLOBAL,String strSist_Costos ) {
      String strRes = "OK";
      String strMaster= "SELECT * FROM vta_ciclico_master WHERE CC_ID='"+id+"'";
      Fechas fecha = new Fechas();
      Inventario inv = new Inventario(oConn, varSesiones, request);
      int intTipoMov=inv.getIntTipoOperacion();
      if(intTipoMov == 2){
         inv.getMovProd().setFieldInt("TIN_ID", 2);
         inv.setIntTipoOperacion(2);
      }
      else if(intTipoMov == 1){
         inv.getMovProd().setFieldInt("TIN_ID", 1);
         inv.setIntTipoOperacion(1);
      }
      try{
      ResultSet rMaster= oConn.runQuery(strMaster,true);
      if(rMaster.next()){
         inv.getMovProd().setFieldInt("SC_ID",rMaster.getInt("CC_SUCURSAL"));
         inv.setIntEMP_ID(rMaster.getInt("EMP_ID"));
      }
         inv.getMovProd().setFieldInt("SC_ID2", 0);
         inv.getMovProd().setFieldString("MP_FECHA", fecha.getFechaActual());
         inv.getMovProd().setFieldString("MP_FOLIO", "");
         inv.getMovProd().setFieldString("MP_NOTAS","Ajuste por conteo ciclico"+id);
         inv.getMovProd().setFieldInt("MP_IDORIGEN", 0);
         inv.setIntNumIdTraspasoSalida(0);
      }catch(Exception ex){
      strRes="Excepcion "+ex;}
      if (strfolio_GLOBAL.equals("SI")) {
         inv.setBolFolioGlobal(true);
      }
//      Definimos el sistema de costos
      try {
        inv.setIntTipoCosteo(Integer.valueOf(strSist_Costos));
      } catch (NumberFormatException ex) {
         System.out.println("No hay sistema de costos definido");
      }
      inv.getMovProd().setFieldInt("MP_TURNO", 1);
      inv.getMovProd().setFieldInt("TMP_ID", 7);
      
      //Validamos si tenemos un empresa seleccionada
      if (varSesiones.getIntIdEmpresa() != 0) {
         //Asignamos la empresa seleccionada
         inv.getMovProd().setFieldInt("EMP_ID", varSesiones.getIntIdEmpresa()); 
      }
      vta_movproddeta movDeta = new vta_movproddeta();
      //Obtenemos el detalle
         try{
         ResultSet rCiclico= oConn.runQuery(strMaster,true);
         if(rCiclico.next()){    
            String strCCdetalle= "SELECT * FROM vta_ciclico_deta WHERE CC_ID='"+id+"'";     
            ResultSet rCCdetalle= oConn.runQuery(strCCdetalle,true);
            if(rCCdetalle.next()){
               String strProducto ="SELECT * FROM vta_Producto WHERE SC_ID='"+rCiclico.getInt("CC_SUCURSAL")+"' AND PR_CODIGO='"+rCCdetalle.getString("PR_CODIGO")+"'";
               ResultSet rProducto= oConn.runQuery(strProducto,true);
               if(rProducto.next()){
                  movDeta.setFieldInt("PR_ID",rProducto.getInt("PR_ID"));
                  movDeta.setFieldDouble("MPD_COSTO",rProducto.getDouble("PR_COSTOREPOSICION"));
                  }
               movDeta.setFieldInt("SC_ID",rCiclico.getInt("CC_SUCURSAL"));
               movDeta.setFieldString("PR_CODIGO",rCCdetalle.getString("PR_CODIGO"));
               double dblCantidad = rCCdetalle.getDouble("CCD_DIFERENCIA");
               if (intTipoMov == Inventario.ENTRADA || intTipoMov == Inventario.TRASPASO_ENTRADA) {
                  movDeta.setFieldDouble("MPD_ENTRADAS", dblCantidad);
                  } 
                  else {
                  movDeta.setFieldDouble("MPD_SALIDAS", dblCantidad);
                  } 
               }

            }
            rCiclico.close();
         }catch(Exception ex){strRes="Excepcion"+ex;}
         movDeta.setFieldInt("MPD_IDORIGEN", 0);
         movDeta.setFieldString("MPD_FECHA", fecha.getFechaActual());
         movDeta.setFieldString("MPD_NOTAS","Ajuste por conteo ciclico"+id);
         movDeta.setFieldInt("ID_USUARIOS", varSesiones.getIntNoUser());
         inv.AddDetalle(movDeta);
      
      //Inicializamos objeto
      inv.Init();
      //Almacenamos la operacion
      inv.doTrx();

      if (inv.getStrResultLast().equals("OK")) {
         strRes = "OK." + inv.getMovProd().getValorKey();
      } else {
         strRes = inv.getStrResultLast();
      }

      return strRes;
   }
   
   
   public String compararExistencia(Conexion oConn,int id) throws SQLException{
      String strListo="OK";
      String strCompCont="SELECT CCD_CONTEO1,PR_CODIGO  FROM vta_ciclico_deta WHERE CC_ID = '"+id+"'";
      String strSucursal="SELECT CC_SUCURSAL FROM vta_ciclico_master WHERE CC_ID= '"+id+"'";
      
      try{
         
         ResultSet rSucursal=oConn.runQuery(strSucursal, true);
         if(rSucursal.next()){
         ResultSet rConteo= oConn.runQuery(strCompCont,true);
         while(rConteo.next()){
         String strCompExt="SELECT PR_EXISTENCIA FROM vta_producto WHERE PR_CODIGO = '"+rConteo.getString("PR_CODIGO")+
                 "' AND SC_ID='"+rSucursal.getInt("CC_SUCURSAL")+"'";
         ResultSet rExistencia= oConn.runQuery(strCompExt,true);
            while(rExistencia.next()){
               if(rConteo.getDouble("CCD_CONTEO1")== rExistencia.getDouble("PR_EXISTENCIA")){
                  String pasarExistencia="UPDATE vta_ciclico_deta SET CCD_CONTEO2= '"+rConteo.getInt("CCD_CONTEO1")+"' WHERE PR_CODIGO = '"+
                                         rConteo.getString("PR_CODIGO")+"' AND CC_ID='"+id+"';";
                  oConn.runQueryLMD(pasarExistencia);
               }
            }
            
         rExistencia.close();
         }
         rConteo.close();
      }
         
      }catch(SQLException ex){
         strListo="Escepcio SQL "+ex;
      }
      catch(Exception exp){
         strListo="Otra Excepcion "+exp;
      }
      return strListo;
   }
   
   public String calcularDiferencia(Conexion oConn, int id){
      String strListo="ok";
      String strSucursal="SELECT CC_SUCURSAL FROM vta_ciclico_master WHERE CC_ID= '"+id+"'";
      double dblDiferencia=0;
      try {
         ResultSet rSucursal=oConn.runQuery(strSucursal, true);
         if(rSucursal.next()){
            String strconteoD= "SELECT PR_CODIGO,CCD_CONTEO2 FROM vta_ciclico_deta WHERE CC_ID='"+id+"'";
            ResultSet rCont= oConn.runQuery(strconteoD,true);
         while(rCont.next()){
            String compExt="SELECT PR_EXISTENCIA FROM vta_producto WHERE PR_CODIGO = '"+rCont.getString("PR_CODIGO")+
                    "' AND SC_ID='"+rSucursal.getInt("CC_SUCURSAL")+"'";
            ResultSet rExistencia= oConn.runQuery(compExt,true);
            if(rExistencia.next()){
            dblDiferencia= rExistencia.getDouble("PR_EXISTENCIA") - rCont.getDouble("CCD_CONTEO2");
            String strActualizarDif= "UPDATE vta_ciclico_deta SET CCD_DIFERENCIA='"+dblDiferencia+"' WHERE PR_CODIGO='"+rCont.getString("PR_CODIGO")+"'";
            oConn.runQueryLMD(strActualizarDif);
            }  
         } 
         rCont.close();
         }
      } 
      catch (SQLException ex) {
         strListo="Excepcion SQL "+ex;
      }
      catch(Exception exp){
         strListo="Otra excepcion "+exp;
      }
      return strListo;
   }
   
   


}
