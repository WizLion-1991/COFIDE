/*
 * Esta clase se encarga de crear nuevos sistemas
 */
package com.mx.siweb.erp.especiales.siweb;

import ERP.ProcesoInterfaz;
import ERP.ProcesoMaster;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.generateData;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author ZeusGalindo
 */
public class InstaladorSIWEB extends ProcesoMaster implements ProcesoInterfaz {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private String strPathBase;
   private String strNombreSistema;
   private String strPathOrigen;
   private String strPathRepositorioXML;
   private String strTipoSistema;
   private String strNombreBaseDatos;
   private String strNombreCarpetaXML;
   private String strPathCarpetaXMLOrigen;
   private String strPathFileContextOrigen;
   private String strPathFileBaseContext;
   private String strPasswordBd;
   private String strPuertoBd;

   private boolean bolValidaDuplicidad;
   private boolean bolGeneraTablas;
   private boolean bolCopiaSitio;

   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(InstaladorSIWEB.class.getName());

   public String getStrPuertoBd() {
      return strPuertoBd;
   }

   public void setStrPuertoBd(String strPuertoBd) {
      this.strPuertoBd = strPuertoBd;
   }

   public String getStrPathFileBaseContext() {
      return strPathFileBaseContext;
   }

   public void setStrPathFileBaseContext(String strPathFileBaseContext) {
      this.strPathFileBaseContext = strPathFileBaseContext;
   }

   public boolean isBolValidaDuplicidad() {
      return bolValidaDuplicidad;
   }

   public void setBolValidaDuplicidad(boolean bolValidaDuplicidad) {
      this.bolValidaDuplicidad = bolValidaDuplicidad;
   }

   public boolean isBolGeneraTablas() {
      return bolGeneraTablas;
   }

   public void setBolGeneraTablas(boolean bolGeneraTablas) {
      this.bolGeneraTablas = bolGeneraTablas;
   }

   public boolean isBolCopiaSitio() {
      return bolCopiaSitio;
   }

   public void setBolCopiaSitio(boolean bolCopiaSitio) {
      this.bolCopiaSitio = bolCopiaSitio;
   }

   public String getStrPathCarpetaXMLOrigen() {
      return strPathCarpetaXMLOrigen;
   }

   public void setStrPathCarpetaXMLOrigen(String strPathCarpetaXMLOrigen) {
      this.strPathCarpetaXMLOrigen = strPathCarpetaXMLOrigen;
   }

   public String getStrPathRepositorioXML() {
      return strPathRepositorioXML;
   }

   public void setStrPathRepositorioXML(String strPathRepositorioXML) {
      this.strPathRepositorioXML = strPathRepositorioXML;
   }

   public String getStrNombreSistema() {
      return strNombreSistema;
   }

   public void setStrNombreSistema(String strNombreSistema) {
      this.strNombreSistema = strNombreSistema;
   }

   public String getStrTipoSistema() {
      return strTipoSistema;
   }

   public String getStrPathBase() {
      return strPathBase;
   }

   public void setStrPathBase(String strPathBase) {
      this.strPathBase = strPathBase;
   }

   public String getStrPathOrigen() {
      return strPathOrigen;
   }

   public void setStrPathOrigen(String strPathOrigen) {
      this.strPathOrigen = strPathOrigen;
   }

   public void setStrTipoSistema(String strTipoSistema) {
      this.strTipoSistema = strTipoSistema;
   }

   public String getStrNombreBaseDatos() {
      return strNombreBaseDatos;
   }

   public void setStrNombreBaseDatos(String strNombreBaseDatos) {
      this.strNombreBaseDatos = strNombreBaseDatos;
   }

   public String getStrNombreCarpetaXML() {
      return strNombreCarpetaXML;
   }

   public void setStrNombreCarpetaXML(String strNombreCarpetaXML) {
      this.strNombreCarpetaXML = strNombreCarpetaXML;
   }

   public String getStrPathFileContextOrigen() {
      return strPathFileContextOrigen;
   }

   public void setStrPathFileContextOrigen(String strPathFileContextOrigen) {
      this.strPathFileContextOrigen = strPathFileContextOrigen;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public InstaladorSIWEB(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
      super(oConn, varSesiones, request);
   }

   public InstaladorSIWEB(Conexion oConn, VariableSession varSesiones) {
      super(oConn, varSesiones);
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   @Override
   public void Init() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void doTrx() {
      this.strResultLast = "OK";
      //Validamos campos obligatorios
      if (this.strNombreSistema.isEmpty()) {
         this.strResultLast = "Error: falta el nombre del sistema";
      }
      if (this.strPathOrigen.isEmpty()) {
         this.strResultLast = "Error: falta el nombre del sistema origen para el copiado";
      }
      if (this.strTipoSistema.isEmpty()) {
         this.strResultLast = "Error: falta el tipo del sistema";
      }
      if (this.strNombreBaseDatos.isEmpty()) {
         this.strResultLast = "Error: falta el nombre de la base de datos del sistema";
      }
      if (this.strNombreCarpetaXML.isEmpty()) {
         this.strResultLast = "Error: falta el nombre de la carpeta xml del sistema";
      }
      //Si pasamos las validaciones comenzamos el proceso
      if (this.strResultLast.equals("OK")) {

         boolean bolContinua1 = true;
         File fileNew = new File(this.strPathBase + System.getProperty("file.separator") + this.strNombreSistema);
         File fileOrigen = new File(this.strPathOrigen);
         if (!fileNew.exists()) {
            fileNew.mkdir();
         } else {
            if (bolValidaDuplicidad) {
               bolContinua1 = false;
            }
         }
         //Solo si continua y paso la primer validacion
         if (bolContinua1) {
            if (this.bolCopiaSitio) {
               // <editor-fold defaultstate="collapsed" desc="Copiado de archivos del sistema">
               log.debug("Copiado de archivos....");
               copiarSubDirectorios(fileOrigen, fileOrigen, fileNew, "--");
               // </editor-fold>
            }

            // <editor-fold defaultstate="collapsed" desc="Generacion carpeta para XML">
            log.debug("Creacion de carpeta....");
            String strPathXmlBase = this.strPathRepositorioXML + "/" + this.strNombreCarpetaXML;
            //No hay carpeta cuando es un sistema conable
            if (!this.strTipoSistema.toLowerCase().startsWith("contabilidad")) {
               File fileNewXml = new File(strPathXmlBase);
               if (!fileNewXml.exists()) {
                  fileNewXml.mkdir();
               }
               File fileXmlOrigen = new File(this.strPathCarpetaXMLOrigen);
               copiarSubDirectorios(fileXmlOrigen, fileXmlOrigen, fileNewXml, "--");
            }
         // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Creacion de base de datos">
            log.debug("Creacion de base de datos....");

            //Copiamos las tablas
            if (this.bolGeneraTablas) {
               try {
                  String strSqlCreate = "CREATE DATABASE " + this.strNombreBaseDatos + " CHARACTER SET utf8 COLLATE utf8_general_ci;\n";
                  this.oConn.runQueryLMD(strSqlCreate);

               } catch (Exception ex) {
                  this.strResultLast = "Error:" + ex.getMessage();
               }
               try {
                  String strSqlList = "show tables";
                  ResultSet rs = oConn.runQuery(strSqlList);
                  while (rs.next()) {
                     String strNomTabla = rs.getString(1);
                     log.debug("Nombre tabla" + strNomTabla);
                     String strSqlCreate = "CREATE TABLE " + this.strNombreBaseDatos + "." + strNomTabla + "  LIKE  " + strNomTabla + "";
                     this.oConn.runQueryLMD(strSqlCreate);
                     String strSqlInsert = "INSERT " + this.strNombreBaseDatos + "." + strNomTabla + " SELECT * FROM  " + strNomTabla + " ";
                     this.oConn.runQueryLMD(strSqlInsert);
  
                  }
                  rs.close();

                  // <editor-fold defaultstate="collapsed" desc="Ponemos el nombre de la carpeta default">
                  String strSqlUpdata1 = "update " + this.strNombreBaseDatos + ".cuenta_contratada  set pathbase =  '" + this.strNombreSistema + "'; ";
                  this.oConn.runQueryLMD(strSqlUpdata1);
                  // </editor-fold>

                  // <editor-fold defaultstate="collapsed" desc="Limpiamos la base de datos">
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="Actualizamos los datos de la empresa y la bodega">
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="Generamos usuario default">
                  // </editor-fold>
               } catch (Exception ex) {
                  this.strResultLast = "Error:" + ex.getMessage();
                  log.error("show tables..." + ex.getMessage());
               }
            }

            //;
            // </editor-fold>
            
            // <editor-fold defaultstate="collapsed" desc="Creacion de usuario y permisos">
            log.debug("Creacion de usuario y permisos....");
            try {
               strPasswordBd = generateData.getPassword(12);
               String strSqlCreate = "CREATE USER " + this.strNombreBaseDatos + "@localhost IDENTIFIED BY '" + strPasswordBd + "';";
               this.oConn.runQueryLMD(strSqlCreate);
               strSqlCreate = "GRANT DELETE ON " + this.strNombreBaseDatos + ".*  TO " + this.strNombreBaseDatos + "@localhost ;";
               this.oConn.runQueryLMD(strSqlCreate);
               strSqlCreate = "GRANT INSERT ON " + this.strNombreBaseDatos + ".*  TO " + this.strNombreBaseDatos + "@localhost ;";
               this.oConn.runQueryLMD(strSqlCreate);
               strSqlCreate = "GRANT SELECT ON " + this.strNombreBaseDatos + ".*  TO " + this.strNombreBaseDatos + "@localhost ;";
               this.oConn.runQueryLMD(strSqlCreate);
               strSqlCreate = "GRANT UPDATE ON " + this.strNombreBaseDatos + ".*  TO " + this.strNombreBaseDatos + "@localhost ;";
               this.oConn.runQueryLMD(strSqlCreate);
               strSqlCreate = "GRANT CREATE ON " + this.strNombreBaseDatos + ".*  TO " + this.strNombreBaseDatos + "@localhost ;";
               this.oConn.runQueryLMD(strSqlCreate);
               strSqlCreate = "GRANT EVENT ON " + this.strNombreBaseDatos + ".*  TO " + this.strNombreBaseDatos + "@localhost ;";
               this.oConn.runQueryLMD(strSqlCreate);
               strSqlCreate = "GRANT ALTER ON " + this.strNombreBaseDatos + ".*  TO " + this.strNombreBaseDatos + "@localhost ;";
               this.oConn.runQueryLMD(strSqlCreate);
               strSqlCreate = "GRANT INDEX ON " + this.strNombreBaseDatos + ".*  TO " + this.strNombreBaseDatos + "@localhost ;";
               this.oConn.runQueryLMD(strSqlCreate);
               strSqlCreate = "GRANT TRIGGER ON " + this.strNombreBaseDatos + ".*  TO " + this.strNombreBaseDatos + "@localhost ;";
               this.oConn.runQueryLMD(strSqlCreate);
               strSqlCreate = "GRANT SHOW VIEW ON " + this.strNombreBaseDatos + ".*  TO " + this.strNombreBaseDatos + "@localhost ;";
               this.oConn.runQueryLMD(strSqlCreate);
               strSqlCreate = "GRANT CREATE TEMPORARY TABLES ON " + this.strNombreBaseDatos + ".*  TO " + this.strNombreBaseDatos + "@localhost ;";
               this.oConn.runQueryLMD(strSqlCreate);
               strSqlCreate = "GRANT LOCK TABLES ON " + this.strNombreBaseDatos + ".*  TO " + this.strNombreBaseDatos + "@localhost ;";
               this.oConn.runQueryLMD(strSqlCreate);
               strSqlCreate = "GRANT EXECUTE ON " + this.strNombreBaseDatos + ".*  TO " + this.strNombreBaseDatos + "@localhost ;";
               this.oConn.runQueryLMD(strSqlCreate);
               
               //Acceso usuario soporte
               strSqlCreate = "GRANT DELETE ON " + this.strNombreBaseDatos + ".*  TO soporte@localhost ;";
               this.oConn.runQueryLMD(strSqlCreate);
               strSqlCreate = "GRANT INSERT ON " + this.strNombreBaseDatos + ".*  TO soporte@localhost ;";
               this.oConn.runQueryLMD(strSqlCreate);
               strSqlCreate = "GRANT SELECT ON " + this.strNombreBaseDatos + ".*  TO soporte@localhost ;";
               this.oConn.runQueryLMD(strSqlCreate);
               strSqlCreate = "GRANT UPDATE ON " + this.strNombreBaseDatos + ".*  TO soporte@localhost ;";
               this.oConn.runQueryLMD(strSqlCreate);
               strSqlCreate = "GRANT CREATE ON " + this.strNombreBaseDatos + ".*  TO soporte@localhost ;";
               this.oConn.runQueryLMD(strSqlCreate);
               strSqlCreate = "GRANT EVENT ON " + this.strNombreBaseDatos + ".*  TO soporte@localhost ;";
               this.oConn.runQueryLMD(strSqlCreate);
               strSqlCreate = "GRANT ALTER ON " + this.strNombreBaseDatos + ".*  TO soporte@localhost ;";
               this.oConn.runQueryLMD(strSqlCreate);
               strSqlCreate = "GRANT INDEX ON " + this.strNombreBaseDatos + ".*  TO soporte@localhost ;";
               this.oConn.runQueryLMD(strSqlCreate);
               strSqlCreate = "GRANT TRIGGER ON " + this.strNombreBaseDatos + ".*  TO soporte@localhost ;";
               this.oConn.runQueryLMD(strSqlCreate);
               strSqlCreate = "GRANT SHOW VIEW ON " + this.strNombreBaseDatos + ".*  TO soporte@localhost ;";
               this.oConn.runQueryLMD(strSqlCreate);
               strSqlCreate = "GRANT CREATE TEMPORARY TABLES ON " + this.strNombreBaseDatos + ".*  TO soporte@localhost ;";
               this.oConn.runQueryLMD(strSqlCreate);
               strSqlCreate = "GRANT LOCK TABLES ON " + this.strNombreBaseDatos + ".*  TO soporte@localhost ;";
               this.oConn.runQueryLMD(strSqlCreate);
               strSqlCreate = "GRANT EXECUTE ON " + this.strNombreBaseDatos + ".*  TO soporte@localhost ;";
               this.oConn.runQueryLMD(strSqlCreate);

            } catch (Exception ex) {
               this.strResultLast = "Error:" + ex.getMessage();
            }
         //"GRANT ALL ON `mydb`.* TO `username`@localhost IDENTIFIED BY 'password'; "
            // </editor-fold>

            //No hay carpeta cuando es un sistema conable
            if (!this.strTipoSistema.toLowerCase().startsWith("contabilidad")) {
               
               // <editor-fold defaultstate="collapsed" desc="Editamos el web.xml">
               log.debug("Editamos el web.xml....");
               String strFileWebXml = this.strPathBase + System.getProperty("file.separator")
                       + this.strNombreSistema + System.getProperty("file.separator") + "WEB-INF"
                       + System.getProperty("file.separator") + "web.xml";
               try {
                  File fXmlFile = new File(strFileWebXml);
                  DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                  dbFactory.setNamespaceAware(true);
                  DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                  Document doc = dBuilder.parse(fXmlFile);
                  doc.setStrictErrorChecking(false);
                  NodeList nList = doc.getDocumentElement().getChildNodes();
                  if (nList != null) {
                     for (int temp = 0; temp < nList.getLength(); temp++) {
                        Node nNode = nList.item(temp);
//                  log.debug("nombre:" + nNode.getNodeName() + " " + nNode.getNodeValue());
                        if (nNode.getNodeName().equals("context-param")) {
//                     log.debug("Mostramos contenido....");
                           boolean bolUserBd = false;
                           boolean bolPassrBd = false;
                           boolean bolPathXml1 = false;
                           boolean bolPathXml2 = false;
                           for (int temp2 = 0; temp2 < nNode.getChildNodes().getLength(); temp2++) {
                              Node nNodeChild = nNode.getChildNodes().item(temp2);
//                        log.debug("Contenido:" + nNodeChild.getNodeName() + " " + nNodeChild.getTextContent());
                              //Detectamos usuarios bd
                              if (bolUserBd && nNodeChild.getNodeName().equals("param-value")) {
                                 log.debug("Aqui cambiamos usuario...");
                                 nNodeChild.setTextContent(this.strNombreBaseDatos);
                                 bolUserBd = false;
                              }
                              //Detectamos password bd
                              if (bolPassrBd && nNodeChild.getNodeName().equals("param-value")) {
                                 log.debug("Aqui cambiamos password...");
                                 nNodeChild.setTextContent(this.strPasswordBd);
                                 bolPassrBd = false;
                              }
                              //Detectamos path xml 1
                              if (bolPathXml1 && nNodeChild.getNodeName().equals("param-value")) {
                                 log.debug("Aqui cambiamos path xml1...");
                                 nNodeChild.setTextContent(strPathXmlBase + "/");
                                 bolPathXml1 = false;
                              }
                              //Detectamos path xml 1
                              if (bolPathXml2 && nNodeChild.getNodeName().equals("param-value")) {
                                 log.debug("Aqui cambiamos path xml2...");
                                 nNodeChild.setTextContent(strPathXmlBase + "/" + "Cer_Sello" + "/");
                                 bolPathXml2 = false;
                              }
                              //Detectamos el tipo de nodo
                              if (nNodeChild.getTextContent().equals("UserBd")) {
                                 bolUserBd = true;
                              }
                              if (nNodeChild.getTextContent().equals("PasswordBd")) {
                                 bolPassrBd = true;
                              }
                              if (nNodeChild.getTextContent().equals("PathXml")) {
                                 bolPathXml1 = true;
                              }
                              if (nNodeChild.getTextContent().equals("PathPrivateKey")) {
                                 bolPathXml2 = true;
                              }
                           }

                        }
                     }//Fin FOR
                  }//Fin IF nList != null
                  //Save XML
                  DOMSource source = new DOMSource(doc);
               // PrintStream will be responsible for writing
                  // the text data to the file
                  PrintStream ps = new PrintStream(strFileWebXml);
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
            // </editor-fold>            

               // <editor-fold defaultstate="collapsed" desc="Creamos y editamos el context.xml">
               log.debug("Editamos el context.xml....");
               String strContextNew = this.strPathFileBaseContext + System.getProperty("file.separator") + this.strNombreSistema + ".xml";
               FileCopy(this.strPathFileContextOrigen, strContextNew);
               try {
                  File fXmlFile = new File(strContextNew);
                  DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                  dbFactory.setNamespaceAware(true);
                  DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                  Document doc = dBuilder.parse(fXmlFile);
                  doc.setStrictErrorChecking(false);
                  NodeList nList = doc.getDocumentElement().getChildNodes();
                  if (nList != null) {
                     for (int temp = 0; temp < nList.getLength(); temp++) {
                        Node nNode = nList.item(temp);
//                  log.debug("nombre:" + nNode.getNodeName() + " " + nNode.getNodeValue());
                        if (nNode.getNodeName().equals("Resource")) {
                           NamedNodeMap lstAttr = nNode.getAttributes();
                           //Recorremos los atributos
                           for (int temp2 = 0; temp2 < lstAttr.getLength(); temp2++) {
                              Node nodo = lstAttr.item(temp2);
                              log.debug("nodo: " + nodo.getNodeName());
                              if (nodo.getNodeName().equals("username")) {
                                 nodo.setNodeValue(this.strNombreBaseDatos);
                              }
                              if (nodo.getNodeName().equals("password")) {
                                 nodo.setNodeValue(this.strPasswordBd);
                              }
                              if (nodo.getNodeName().equals("url")) {
                                 nodo.setNodeValue("jdbc:mysql://localhost:" + this.strPuertoBd + "/" + this.strNombreBaseDatos);
                              }

                           }

                        }
                     }//Fin FOR
                  }//Fin IF nList != null
                  //Save XML
                  DOMSource source = new DOMSource(doc);
               // PrintStream will be responsible for writing
                  // the text data to the file
                  PrintStream ps = new PrintStream(strContextNew);
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
            // </editor-fold>

            }

         }

      }
   }

   @Override
   public void doTrxAnul() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void doTrxRevive() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void doTrxSaldo() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void doTrxMod() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   public void copiarSubDirectorios(File fBase, File f, File f2, String separador) {
      File[] ficheros = f.listFiles();
      for (int x = 0; x < ficheros.length; x++) {
         if (!ficheros[x].getName().equals(".DS_Store")
                 && !ficheros[x].getName().equals("ReadMe.txt")
                 && !ficheros[x].getName().equals("sqledit.txt")) {
            log.debug(separador + ficheros[x].getName());

            if (ficheros[x].isDirectory()) {
               String nuevo_separador;
               nuevo_separador = separador + " ";
               //Nombre del nuevo subdirectorio
               String strPathNewDir = ficheros[x].getAbsolutePath().replace(fBase.getAbsolutePath(), f2.getAbsolutePath());
               File fnewDir = new File(strPathNewDir);
               if (!fnewDir.exists()) {
                  try {
//                     log.debug("ficheros[x].getAbsolutePath():" + ficheros[x].getAbsolutePath());
                     log.debug("strPathNewDir:" + strPathNewDir);
                     fnewDir.mkdirs();
                  } catch (Exception ex) {
                     log.error("Error crear un subdirectorio " + strPathNewDir + " " + ex.getMessage());
                  }
               }
               copiarSubDirectorios(fBase, ficheros[x], f2, nuevo_separador);
            } else {
               //Nombre del nuevo archivo
               String strPathNewFile = ficheros[x].getAbsolutePath().replace(fBase.getAbsolutePath(), f2.getAbsolutePath());

               String strPathEE = ficheros[x].getAbsolutePath();
//               log.debug("copy from " + strPathEE + " to " + strPathNewFile);
               //Copiamos el archivo
               FileCopy(ficheros[x].getAbsolutePath(),
                       strPathNewFile);
            }

         }
      }
   }

   public void FileCopy(String sourceFile, String destinationFile) {
//      System.out.println("Desde: " + sourceFile);
//      System.out.println("Hacia: " + destinationFile);

      try {
         File inFile = new File(sourceFile);
         File outFile = new File(destinationFile);

         FileInputStream in = new FileInputStream(inFile);
         FileOutputStream out = new FileOutputStream(outFile);

         int c;
         while ((c = in.read()) != -1) {
            out.write(c);
         }

         in.close();
         out.close();
      } catch (IOException e) {
         log.error("Error al copiar el archivo desde " + sourceFile + " hacia " + destinationFile + " " + e.getMessage());
         System.err.println("Hubo un error de entrada/salida!!!");
      }
   }

   // </editor-fold>
}
