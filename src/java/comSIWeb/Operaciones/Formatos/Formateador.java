package comSIWeb.Operaciones.Formatos;

import comSIWeb.Operaciones.Bitacora;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.Fechas;
import comSIWeb.Utilerias.NumberString;
import comSIWeb.Utilerias.StringofNumber;
import generated.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.logging.log4j.LogManager;

/**
 * Esta clase Crear los formatos de impresion en Xml
 *
 * @author zeus
 */
public class Formateador {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">

   protected formatos formato;
   protected ArrayList<TableMaster> lstDeta;
   protected ArrayList<TableMaster> lstSQL;
   //Lista de comodines personalizados
   protected ArrayList<formatoComodin> lstComodinesPers;
   protected ObjectFactory xmlFact;
   protected Formato fmXML;
   protected int intTypeOut;
   protected String strPath;
   protected Bitacora bit;
   protected formatosql tbMaster;
   protected formatosql tbFooter;
   protected int intNumDecimales;
   protected Fechas fecha;
   protected String strSimboloMoney;
   protected String strNomMoney;
   protected String strTitulo;
   protected String strNomLlave;
   protected int intLastYBody = 0;
   protected String strNomFile;
   protected boolean bolSecondPage = false;
   protected boolean bolRightPage = false;
   protected boolean bolThirdPage = false;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(Formateador.class.getName());
   /**
    * Constante para indicar que el tipo de salida es por archivo
    */
   public static final int FILE = 0;
   /**
    * Constante para indicar que el tipo de salida es por objeto
    */
   public static final int OBJECT = 1;
   /**
    * Constante para indicar que el tipo de salida es por out hacia el browser
    */
   public static final int OUT = 2;
   protected String strPathImage;
   protected String strPathFonts;
   protected boolean bolDosxHoja;
   protected boolean bolCuatroxHoja;
   protected boolean bolSeisxHoja;

   /**
    * Nos regresa el tipo de salida
    *
    * @return Regresa una constante FILE, OUT, OBJECT
    */
   public int getIntTypeOut() {
      return intTypeOut;
   }

   /**
    * Define el tipo de salida
    *
    * @param intTypeOut Definimos la constante FILE, OUT, OBJECT
    */
   public void setIntTypeOut(int intTypeOut) {
      this.intTypeOut = intTypeOut;
   }

   /**
    * Nos regresa el path donde se guardara el formato en xml
    *
    * @return Es el path
    */
   public String getStrPath() {
      return strPath;
   }

   /**
    * Definimos el path donde se guardara el formato en xml
    *
    * @param strPath Es el path donde se almacenara el formato xml
    */
   public void setStrPath(String strPath) {
      this.strPath = strPath;
   }

   /**
    * Regresa el objeto del formato XML
    *
    * @return Nos regresa el objeto de formato XML
    */
   public Formato getFmXML() {
      return fmXML;
   }

   /**
    * Nos regresa el numero de decimales por default para los numero
    *
    * @return Nos regresa el numero de decimales
    */
   public int getIntNumDecimales() {
      return intNumDecimales;
   }

   /**
    * Define el numero de decimales por default para los numero
    *
    * @param intNumDecimales Es el numero de decimales
    */
   public void setIntNumDecimales(int intNumDecimales) {
      this.intNumDecimales = intNumDecimales;
   }

   /**
    * Regresa el simbolo para dinero
    *
    * @return Nos regresa el simbolo para el dinero $
    */
   public String getStrSimboloMoney() {
      return strSimboloMoney;
   }

   /**
    * Define el simbolo para dinero
    *
    * @param strSimboloMoney Es el simbolo para el dinero $
    */
   public void setStrSimboloMoney(String strSimboloMoney) {
      this.strSimboloMoney = strSimboloMoney;
   }

   /**
    * Regresa el titulo del reporte
    *
    * @return Es una cadena con el titulo del reporte
    */
   public String getStrTitulo() {
      return strTitulo;
   }

   /**
    * Nos regresa el nombre del campo llave con el que recuperaremos el id del
    * formato
    *
    * @return Es el valor del campo llave a imprimir
    */
   public String getStrNomLlave() {
      return strNomLlave;
   }

   /**
    * Nos regresa el path de la fuentes
    *
    * @return Es un cadena
    */
   public String getStrPathFonts() {
      return strPathFonts;
   }

   /**
    * Define el path de la fuentes
    *
    * @param strPathFonts Es un cadena
    */
   public void setStrPathFonts(String strPathFonts) {
      this.strPathFonts = strPathFonts;
   }

   /**
    * Nos regresa el path de las imagenes
    *
    * @return Es un cadena
    */
   public String getStrPathImage() {
      return strPathImage;
   }

   /**
    * Define el path de las imagenes
    *
    * @param strPathImage Es un cadena
    */
   public void setStrPathImage(String strPathImage) {
      this.strPathImage = strPathImage;
   }

   /**
    * Nos Indica que se imprimiran dos formatos por hoja
    *
    * @return Es un valor boolean
    */
   public boolean isBolDosxHoja() {
      return bolDosxHoja;
   }

   /**
    * Indica que se imprimiran dos formatos por hoja
    *
    * @param bolDosxHoja Es un valor boolean
    */
   public void setBolDosxHoja(boolean bolDosxHoja) {
      this.bolDosxHoja = bolDosxHoja;
   }

   /**
    * Nos Indica que se imprimiran cuatro formatos por hoja
    *
    * @return Es un valor boolean
    */
   public boolean isBolCuatroxHoja() {
      return bolCuatroxHoja;
   }

   /**
    * Indica que se imprimiran cuatro formatos por hoja
    *
    * @param bolCuatroxHoja Es un valor boolean
    */
   public void setBolCuatroxHoja(boolean bolCuatroxHoja) {
      this.bolCuatroxHoja = bolCuatroxHoja;
   }

   /**
    * Nos Indica que se imprimiran seis formatos por hoja
    *
    * @return Es un valor boolean
    */
   public boolean isBolSeisxHoja() {
      return bolSeisxHoja;
   }

   /**
    * Indica que se imprimiran seis formatos por hoja
    *
    * @param bolSeisxHoja Es un valor boolean
    */
   public void setBolSeisxHoja(boolean bolSeisxHoja) {
      this.bolSeisxHoja = bolSeisxHoja;
   }

   /**
    * Agregamos un nuevo comodin personalizado al formato
    *
    * @param strComodin Es el comodin personalizado
    * @param strValorComodin Es el valor del comodin
    */
   public void addComodinPersonalizado(String strComodin, String strValorComodin) {
      this.lstComodinesPers.add(new formatoComodin(strComodin, strValorComodin));
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   /**
    * Constructor del formateador
    */
   public Formateador() {
      this.formato = new formatos();
      this.lstDeta = new ArrayList<TableMaster>();
      this.lstSQL = new ArrayList<TableMaster>();
      this.lstComodinesPers = new ArrayList<formatoComodin>();
      this.strTitulo = "";
      this.xmlFact = new ObjectFactory();
      this.fmXML = xmlFact.createFormato();
      this.intTypeOut = 0;
      this.strPath = "";
      this.bit = new Bitacora();
      this.tbMaster = null;
      this.tbFooter = null;
      this.intNumDecimales = 2;
      this.fecha = new Fechas();
      this.strSimboloMoney = "$";
      this.strNomLlave = "";
      this.strNomFile = "";
      this.strPathImage = "";
      this.strPathFonts = "";
      this.strNomMoney = "";
      this.bolDosxHoja = false;
      this.bolSeisxHoja = false;
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    * Inicializa los valores para el formato
    *
    * @param oConn Es la conexion
    * @param strAbrev abreviatura del nombre del formato
    * @return Regresa una caden con OK si se pudo generar el formato
    */
   public String InitFormat(Conexion oConn, String strAbrev) {
      String strRes = "OK";
      //Obtenemos el formato
      this.formato.ObtenDatos(strAbrev, oConn);
      if (!this.formato.getFieldString("FM_ID").equals("0")) {
         this.strTitulo = this.formato.getFieldString("FM_NOMBRE");
         this.strNomLlave = this.formato.getFieldString("FM_LLAVE");
         //Recuperamos el detalle del formato
         formatodeta fdeta = new formatodeta();
         formatosql fSQL = new formatosql();
         this.lstDeta = fdeta.ObtenDatosVarios(" FM_ID = " + this.formato.getFieldString("FM_ID") + " ORDER BY FMD_HEAD_FOOT_BOD,FMD_ORDEN ", oConn);
         this.lstSQL = fSQL.ObtenDatosVarios(" FM_ID = " + this.formato.getFieldString("FM_ID") + " ", oConn);
      } else {
         strRes = "ERROR:El formato no existe";
      }
      return strRes;
   }

   /**
    * Inicializa los parametros de los reportes sql con valores adicionales
    *
    * @param request Es el objeto HttpServletRequest con las peticiones web
    */
   public void InitParamsSql(HttpServletRequest request) {
      Iterator<TableMaster> it = this.lstSQL.iterator();
      while (it.hasNext()) {
         TableMaster tbn = it.next();
         //Evaluamos si tienen parametros adicionales que tomar
         if (tbn.getFieldString("FS_PARAM1").contains("{") && tbn.getFieldString("FS_PARAM1").contains("}")) {
            String strValorParam = request.getParameter(tbn.getFieldString("FS_PARAM1").replace("{", "").replace("}", ""));
            tbn.setFieldString("FS_SQL", tbn.getFieldString("FS_SQL").replace(tbn.getFieldString("FS_PARAM1"), strValorParam));
         }
         if (tbn.getFieldString("FS_PARAM2").contains("{") && tbn.getFieldString("FS_PARAM2").contains("}")) {
            String strValorParam = request.getParameter(tbn.getFieldString("FS_PARAM2").replace("{", "").replace("}", ""));
            tbn.setFieldString("FS_SQL", tbn.getFieldString("FS_SQL").replace(tbn.getFieldString("FS_PARAM2"), strValorParam));
         }
         if (tbn.getFieldString("FS_PARAM3").contains("{") && tbn.getFieldString("FS_PARAM3").contains("}")) {
            String strValorParam = request.getParameter(tbn.getFieldString("FS_PARAM3").replace("{", "").replace("}", ""));
            tbn.setFieldString("FS_SQL", tbn.getFieldString("FS_SQL").replace(tbn.getFieldString("FS_PARAM3"), strValorParam));
         }
         if (tbn.getFieldString("FS_PARAM4").contains("{") && tbn.getFieldString("FS_PARAM4").contains("}")) {
            String strValorParam = request.getParameter(tbn.getFieldString("FS_PARAM4").replace("{", "").replace("}", ""));
            tbn.setFieldString("FS_SQL", tbn.getFieldString("FS_SQL").replace(tbn.getFieldString("FS_PARAM4"), strValorParam));
         }
         if (tbn.getFieldString("FS_PARAM5").contains("{") && tbn.getFieldString("FS_PARAM5").contains("}")) {
            String strValorParam = request.getParameter(tbn.getFieldString("FS_PARAM5").replace("{", "").replace("}", ""));
            tbn.setFieldString("FS_SQL", tbn.getFieldString("FS_SQL").replace(tbn.getFieldString("FS_PARAM5"), strValorParam));
         }
      }
   }

   /**
    * Genera el formato en formato Xml
    *
    * @param oConn Es la conexion
    * @param intIdKey Es el Id o llave del registro inicial a imprimir en el
    * formato
    * @return Regresa una caden con OK si se pudo generar el formato
    */
   public String DoFormat(Conexion oConn, int intIdKey) {
      String strRes = "OK";
      this.strNomFile = this.strNomLlave + intIdKey + ".xml";
      //Ejecutamos los SQL
      int intIdMaster = 0;
      Iterator<TableMaster> it = this.lstSQL.iterator();
      while (it.hasNext()) {
         formatosql tb = (formatosql) it.next();
         if (tb.getFieldInt("FS_ESMASTER") == 1) {
            String strRes2 = tb.ExecuteSQLMaster(oConn, intIdKey);
            if (strRes2.equals("OK")) {
               intIdMaster = tb.getFieldInt("FS_ESMASTER");
               tbMaster = tb;
            } else {
               strRes = strRes2;
            }
            break;
         }
      }
      //Validamos si existe un sql Master
      if (intIdMaster != 0) {
         //Ejecutamos los sql secundarios
         it = this.lstSQL.iterator();
         while (it.hasNext()) {
            formatosql tb = (formatosql) it.next();
            if (tb.getFieldInt("FS_ESMASTER") == 0) {
               String strRes2 = tb.ExecuteSQL(oConn, intIdKey, tbMaster);
               if (strRes2.equals("OK")) {
               } else {
                  strRes = strRes2;
               }
               if (tb.getFieldInt("FS_ESFOOT") == 1) {
                  this.tbFooter = tb;
               }
            }
         }
         //Si todos los SQL resultaron exitosos
         if (strRes.equals("OK")) {
            //Creamos el formato
            CrearHead(oConn, false);
            CrearBody(oConn);
            CrearFoot(oConn, false);
            //Generamos el formato con los datos
            int intTotalPages = 0;
            CreaConfig(intTotalPages);
            //Generamos el formato dependiendo de la opcion usada FILE OUT OBJECT
            CreaFormato(oConn);
            //Cerramos las conexiones
            CierraRs(oConn);
         }
      } else {
         strRes = "ERROR:El formato no contiene un sql Maestro";
      }
      return strRes;
   }

   /**
    * Obtiene el nombre de la moneda en el formato
    *
    * @param bolEsMasivo Indica si es una impresion masiva
    * @param oConn Es la conexion a la base de datos
    */
   protected void getMonedaFormat(boolean bolEsMasivo, Conexion oConn) {
      //Validamos si existe un campo para identificar el nombre de la moneda
      if (!this.formato.getFieldString("FM_MONEDA").equals("")) {
         if (tbMaster != null) {
            try {
               String strValMoneda = "";
               if (!bolEsMasivo) {
                  tbMaster.getRs().beforeFirst();
                  while (tbMaster.getRs().next()) {
                     strValMoneda = tbMaster.getRs().getString(this.formato.getFieldString("FM_MONEDA"));
                  }
               } else {
                  strValMoneda = tbMaster.getRs().getString(this.formato.getFieldString("FM_MONEDA"));
               }
               String strSql = "select * from vta_monedas where MON_ID = " + strValMoneda;
               ResultSet rs = oConn.runQuery(strSql, true);
               //Buscamos el nombre del archivo
               while (rs.next()) {
                  this.strSimboloMoney = rs.getString("MON_SIMBOLO");
                  this.strNomMoney = rs.getString("MON_DESCRIPCION");
               }
               //if(rs.getStatement() != null )rs.getStatement().close(); 
               rs.close();
            } catch (SQLException ex) {
               Logger.getLogger(Formateador.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
      }
   }

   /**
    * Crea los tags de Head
    *
    * @param oConn Es la conexion a la bd
    * @param bolEsMasivo Indica si el head es masivo o no
    */
   protected void CrearHead(Conexion oConn, boolean bolEsMasivo) {
      getMonedaFormat(bolEsMasivo, oConn);
      //Header
      Header head = this.xmlFact.createHeader();
      if (tbMaster != null) {
         //Recorremos los campos y buscamos los campos de tipo Head
         Iterator<TableMaster> it = lstDeta.iterator();
         while (it.hasNext()) {
            formatodeta fmd = (formatodeta) it.next();
            if (fmd.getFieldInt("FMD_HEAD_FOOT_BOD") == 0) {
               String strNom = fmd.getFieldString("FMD_NOM");
               String strValor = "";
               if (strNom.equals("none")) {
                  strValor = fmd.getFieldString("FMD_VALCONST");
               } else {
                  try {
                     if (!bolEsMasivo) {
                        tbMaster.getRs().beforeFirst();
                        while (tbMaster.getRs().next()) {
                           strValor = tbMaster.getRs().getString(strNom);
                        }
                     } else {
                        strValor = tbMaster.getRs().getString(strNom);
                     }
                  } catch (SQLException ex) {
                     if (ex.getLocalizedMessage() != null) {
                        log.error("ERROR EN HEAD " + ex.getLocalizedMessage());
                        log.error("ERROR EN HEAD  " + ex.getMessage());
                        this.bit.GeneraBitacora(ex.getLocalizedMessage(), "system", "formato Head " + strNom, oConn);
                     } else {
                        ex.printStackTrace();
                     }
                  }
               }
               //Aqui se parsea el dato a mostrar
               CrearItemHead(oConn, head, fmd, strValor);

            }
         }
      }
      //Anadimso el encabezado al formato xml
      this.fmXML.getFooterOrBodyOrHeaderOrConfig().add(head);
   }

   /**
    * Formatea el dato para el encabezado
    *
    * @param oConn Es la conexion
    * @param head Es el encabezado
    * @param fmd Son los parametros del campo
    * @param strValor Es el valor del campo
    */
   protected void CrearItemHead(Conexion oConn, Header head, formatodeta fmd, String strValor) {
      int intFactorMinus = 0;
      int intFactorMinusX = 0;
      if (this.bolDosxHoja && this.bolSecondPage) {
         intFactorMinus = this.formato.getFieldInt("FM_INIMIDDLE");
      }
      if (this.bolCuatroxHoja) {
         if (this.bolSecondPage) {
            intFactorMinus = this.formato.getFieldInt("FM_INIMIDDLE");
         }
         if (this.bolRightPage) {
            intFactorMinusX = this.formato.getFieldInt("FM_INIMIDDLEY");
         }
      }
      if (this.bolSeisxHoja) {
         if (this.bolSecondPage) {
            intFactorMinus = this.formato.getFieldInt("FM_INIMIDDLE");
         }
         if (this.bolRightPage) {
            intFactorMinusX = this.formato.getFieldInt("FM_INIMIDDLEY");
         }
         if (this.bolThirdPage) {
            intFactorMinusX = this.formato.getFieldInt("FM_INIMIDDLEY") * 2;
         }
      }
      int intAncho = 0;
      try {
         intAncho = fmd.getFieldInt("FMD_ANCHO");
      } catch (NumberFormatException ex) {
         log.error(ex.getMessage());
      }
      strValor = this.FormatValor(strValor, fmd.getFieldString("FMD_FORMATO"), oConn);

      //Validamos si es el texto tiene que ajustarse(conceptos en multiples lineas)
      if (fmd.getFieldString("FMD_FORMATO").startsWith("textAuto") && strValor.length() > intAncho) {
         // <editor-fold defaultstate="collapsed" desc="Objetos con textAuto">
         int intContRow = -1;
         //Separamos la cadena en varias partes
         String[] lstFrases = this.ObtenFrases(strValor, intAncho, fmd.getFieldString("FMD_FORMATO"));
         //Recorremos cada frase
         for (int yh = 0; yh < lstFrases.length; yh++) {
            if (lstFrases[yh] != null) {
               int intPosYCalc = 0;
               //Si es la primera linea
               if (yh > 0) {
                  intContRow++;
               }
               if (yh == 0) {
                  intPosYCalc = this.formato.getFieldInt("FM_COMFACTOR") * (intContRow + 1) + intFactorMinus;
               } else {
                  intPosYCalc = this.formato.getFieldInt("FM_COMFACTOR") * (intContRow + 1) + intFactorMinus;
               }
               intPosYCalc = fmd.getFieldInt("FMD_POSY") - intPosYCalc;
               this.intLastYBody = intPosYCalc;//guardamos la ultima posicion en Y

               //Creamos objeto a representar
               HP rot = this.xmlFact.createHP();
               int intPosX = fmd.getFieldInt("FMD_POSX") + intFactorMinusX;
               rot.setPX(intPosX + "");
               rot.setPY(intPosYCalc + "");
               rot.setPalign(fmd.getFieldString("FMD_ALIGN"));
               rot.setPcolor(fmd.getFieldString("FMD_COLOR"));
               rot.setPcolorbak(fmd.getFieldString("FMD_COLORFONDO"));
               rot.setPstyle(fmd.getFieldString("FMD_ESTILO"));
               rot.setPheight(fmd.getFieldString("FMD_ALTO"));
               rot.setPsize(fmd.getFieldString("FMD_SIZEFONT"));
               rot.setPwidth(fmd.getFieldString("FMD_ANCHO"));
               rot.setPwidthCanvas(fmd.getFieldString("FMD_WIDTH_CANVAS"));
               String strUrlImg = fmd.getFieldString("FMD_TITULO");
               String strFont = fmd.getFieldString("FMD_LETRA");
               if (strUrlImg.contains("[URL_IMG]")) {
                  strUrlImg = strUrlImg.replace("[URL_IMG]", this.strPathImage);
               }
               if (strFont.contains("[URL_FONT]")) {
                  strFont = strFont.replace("[URL_FONT]", this.strPathFonts);
               }
               rot.setUrlimg(strUrlImg);
               rot.setPfont(strFont);
               rot.setValor(lstFrases[yh]);
               rot.setPformato(fmd.getFieldString("FMD_FORMATO"));
               head.getHP().add(rot);
            }
         }
         // </editor-fold>
      } else {
         // <editor-fold defaultstate="collapsed" desc="Creamos objeto a representar">
         HP rot = this.xmlFact.createHP();
         int intPosYCalc = fmd.getFieldInt("FMD_POSY") + intFactorMinus;
         int intPosX = fmd.getFieldInt("FMD_POSX") + intFactorMinusX;
         rot.setPX(intPosX + "");
         rot.setPY(intPosYCalc + "");
         rot.setPalign(fmd.getFieldString("FMD_ALIGN"));
         rot.setPcolor(fmd.getFieldString("FMD_COLOR"));
         rot.setPcolorbak(fmd.getFieldString("FMD_COLORFONDO"));
         rot.setPstyle(fmd.getFieldString("FMD_ESTILO"));
         int intFMD_ALTO = fmd.getFieldInt("FMD_ALTO") + intFactorMinus;
         rot.setPheight(intFMD_ALTO + "");
         rot.setPsize(fmd.getFieldString("FMD_SIZEFONT"));
         int intFMD_ANCHO = fmd.getFieldInt("FMD_ANCHO") + intFactorMinusX;
         rot.setPwidth(intFMD_ANCHO + "");
         rot.setPwidthCanvas(fmd.getFieldString("FMD_WIDTH_CANVAS"));
         String strUrlImg = fmd.getFieldString("FMD_TITULO");
         String strFont = fmd.getFieldString("FMD_LETRA");
         if (strUrlImg.contains("[URL_IMG]")) {
            strUrlImg = strUrlImg.replace("[URL_IMG]", this.strPathImage);
         }
         if (strFont.contains("[URL_FONT]")) {
            strFont = strFont.replace("[URL_FONT]", this.strPathFonts);
         }
         if (strUrlImg.equals("") && fmd.getFieldString("FMD_TIPODATO").equals("image")) {
            if (!strValor.equals("")) {
               strUrlImg = strValor;
            }
         }
         rot.setUrlimg(strUrlImg);
         rot.setPfont(strFont);
         rot.setValor(strValor);
         rot.setPformato(fmd.getFieldString("FMD_FORMATO"));
         head.getHP().add(rot);
         // </editor-fold>
      }
   }

   /**
    * Formatea el valor dependiendo de la eleccion
    *
    * @param strValor Es el valor a formatear
    * @param strFormat Es el formato
    * @param oConn Es la conexion a la bd
    * @return Regresa un valor formateado
    */
   protected String FormatValor(String strValor, String strFormat, Conexion oConn) {
      if (strValor == null) {
         strValor = "";
      }
      String strValueReturn = new String(strValor);
      // <editor-fold defaultstate="collapsed" desc="Validamos si hay comodines">
      if (!this.lstComodinesPers.isEmpty()) {
         //Validamos si contiene un comodin
         if (strValueReturn.contains("[") && strValueReturn.contains("]")) {
            Iterator<formatoComodin> it = this.lstComodinesPers.iterator();
            while (it.hasNext()) {
               formatoComodin comodin = it.next();
               strValueReturn = strValueReturn.replace(comodin.getStrNomComodin(), comodin.getStrValorComodin());
            }
         }
      }
      // </editor-fold>
      if (!strFormat.trim().equals("")) {
         // <editor-fold defaultstate="collapsed" desc="Formato fecha">
         if (strFormat.toLowerCase().equals("date")) {
            if (strValor.length() == 8) {
               strValueReturn = this.fecha.FormateaDDMMAAAA(strValor, "/");
            }
         }
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="formato decimal">
         if (strFormat.toLowerCase().startsWith("decimal") && !strValor.equals("")) {
            int intNumDecimal = this.intNumDecimales;
            strFormat = strFormat.replace("decimal", "");
            strFormat = strFormat.replace("(", "");
            strFormat = strFormat.replace(")", "");
            if (!strFormat.equals("")) {
               try {
                  intNumDecimal = Integer.valueOf(strFormat);
               } catch (NumberFormatException ex) {
                  this.bit.GeneraBitacora(ex.getMessage(), "system", "format decimal", oConn);
                  log.error("[Formateador] ex " + ex.getLocalizedMessage() + " ");
               }
            }
            double dblValor = 0;
            try {
               dblValor = Double.valueOf(strValor);
               strValueReturn = NumberString.FormatearDecimal(dblValor, intNumDecimal);
            } catch (NumberFormatException ex) {
               this.bit.GeneraBitacora(ex.getMessage(), "system", "format decimal_2", oConn);
               log.error("[Formateador] ex " + ex.getLocalizedMessage() + " ");
            }
         }
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="formato integer">
         if (strFormat.toLowerCase().startsWith("integer") && !strValor.equals("")) {
            double dblValor = 0;
            try {
               dblValor = Double.valueOf(strValor);
               strValueReturn = NumberString.FormatearDecimal(dblValor, 0);
            } catch (NumberFormatException ex) {
               this.bit.GeneraBitacora(ex.getMessage(), "system", "format decimal_2", oConn);
               log.error("[Formateador] ex " + ex.getLocalizedMessage() + " ");
            }
         }
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="formato money">
         if (strFormat.toLowerCase().startsWith("money") && !strValor.equals("")) {
            int intNumDecimal = this.intNumDecimales;
            strFormat = strFormat.replace("money", "");
            strFormat = strFormat.replace("(", "");
            strFormat = strFormat.replace(")", "");
            if (!strFormat.equals("")) {
               try {
                  intNumDecimal = Integer.valueOf(strFormat);
               } catch (NumberFormatException ex) {
                  this.bit.GeneraBitacora(ex.getMessage(), "system", "format money", oConn);
               }
            }
            double dblValor = 0;
            try {
               dblValor = Double.valueOf(strValor);
               strValueReturn = this.strSimboloMoney + NumberString.FormatearDecimal(dblValor, intNumDecimal);
            } catch (NumberFormatException ex) {
               this.bit.GeneraBitacora(ex.getMessage(), "system", "format money_2", oConn);
            }
         }
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="formato en letra">
         if (strFormat.toLowerCase().startsWith("enletra") && !strValor.equals("")) {
            StringofNumber enletra = new StringofNumber();
            double dblValor = 0;
            try {
               dblValor = Double.valueOf(strValor);
               if (!this.strNomMoney.equals("")) {
                  enletra.setNombreMoneda(this.strNomMoney);
                  enletra.setStrSimbolo(this.strSimboloMoney);
               }
               strValueReturn = enletra.getStringOfNumber(dblValor);
            } catch (NumberFormatException ex) {
               this.bit.GeneraBitacora(ex.getMessage(), "system", "format letra_2", oConn);
               log.error("[Formateador] ex " + ex.getLocalizedMessage() + " ");
            }
         }
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="formato en letra con parentesis">
         if (strFormat.toLowerCase().startsWith("enletrapar") && !strValor.equals("")) {
            StringofNumber enletra = new StringofNumber();
            double dblValor = 0;
            try {
               dblValor = Double.valueOf(strValor);
               strValueReturn = "(" + enletra.getStringOfNumber(dblValor) + ")";
            } catch (NumberFormatException ex) {
               this.bit.GeneraBitacora(ex.getMessage(), "system", "format letra_2", oConn);
               log.error("[Formateador] ex " + ex.getLocalizedMessage() + " ");
            }
         }
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="formato de fecha en letras">
         if (strFormat.toLowerCase().startsWith("fechaletra") && !strValor.equals("")) {
            strValueReturn = this.fecha.DameFechaenLetra(strValor);
         }
         // </editor-fold>
      }
      return strValueReturn;
   }

   /**
    * Cerramos los resultset de los formatos
    *
    * @param oConn Es la conexion a la base de datos
    */
   protected void CierraRs(Conexion oConn) {
      //Imprimimos los datos del detalle
      Iterator it = this.lstSQL.iterator();
      while (it.hasNext()) {
         formatosql tb = (formatosql) it.next();
         if (tb.getRs() != null) {
            try {
               //if (!tb.getRs().isClosed()) {
               try {
                  tb.getRs().close();
               } catch (SQLException ex) {
                  this.bit.GeneraBitacora(ex.getLocalizedMessage(), "system", "formato close 1", oConn);
               }
               //}

            } catch (java.lang.AbstractMethodError ex) {
               Logger.getLogger(Formateador.class.getName()).log(Level.SEVERE, null, ex);
               this.bit.GeneraBitacora(ex.getLocalizedMessage(), "system", "formato close 2", oConn);
            } catch (Exception ex2) {
               Logger.getLogger(Formateador.class.getName()).log(Level.SEVERE, null, ex2);
               this.bit.GeneraBitacora(ex2.getLocalizedMessage(), "system", "formato close 2", oConn);
            }
         }
      }
   }

   /**
    * Genera la impresion del detalle del formato
    *
    * @param oConn Es la conexion a la bd
    */
   protected void CrearBody(Conexion oConn) {
      int intFactorMinus = 0;
      int intFactorMinusX = 0;
      int intFirstYBody = 0;//guardamos la primera posicion en Y
      if (this.bolDosxHoja && this.bolSecondPage) {
         intFactorMinus = this.formato.getFieldInt("FM_INIMIDDLE");
      }
      if (this.bolCuatroxHoja) {
         if (this.bolSecondPage) {
            intFactorMinus = this.formato.getFieldInt("FM_INIMIDDLE");
         }
         if (this.bolRightPage) {
            intFactorMinusX = this.formato.getFieldInt("FM_INIMIDDLEY");
         }
      }
      if (this.bolSeisxHoja) {
         if (this.bolSecondPage) {
            intFactorMinus = this.formato.getFieldInt("FM_INIMIDDLE");
         }
         if (this.bolRightPage) {
            intFactorMinusX = this.formato.getFieldInt("FM_INIMIDDLEY");
         }
         if (this.bolThirdPage) {
            intFactorMinusX = this.formato.getFieldInt("FM_INIMIDDLEY") * 2;
         }
      }
      Body body = this.xmlFact.createBody();
      int intNumPages = 1;
      log.debug("intLastYBody Before rows:" + intLastYBody);
      //Imprimimos los datos del detalle
      Iterator it = this.lstSQL.iterator();
      int intNumRows = this.formato.getFieldInt("FM_NUMITEMS");//Numero de partidas a imprimir por hoja
      while (it.hasNext()) {
         formatosql tb = (formatosql) it.next();
         log.debug("id sql:" + tb.getFieldInt("FS_ID"));
         if (tb.getFieldInt("FS_ESMASTER") == 0
                 && tb.getFieldInt("FS_ESFOOT") == 0) {//Solo procesamos los objetos detalle
            Page page = this.xmlFact.createPage();
            try {
                tb.getRs().beforeFirst();
               int intContRowTmp1 = 0;
               // <editor-fold defaultstate="collapsed" desc="Ciclo para determinar el numero de filas">
               while (tb.getRs().next()) {
                  intContRowTmp1++;
               }
               // </editor-fold>
               tb.getRs().beforeFirst();
               int intContRow = 0;
               // <editor-fold defaultstate="collapsed" desc="Ciclo para las partidas">
               while (tb.getRs().next()) {
                  intContRow++;
                  //Sacamos la lista de los objetos detalle del formato
                  Iterator<TableMaster> it2 = lstDeta.iterator();
                  while (it2.hasNext()) {
                     formatodeta fmd = (formatodeta) it2.next();
                     //Validamos si es un campo detalle
                     if (fmd.getFieldInt("FMD_HEAD_FOOT_BOD") == 1
                             && fmd.getFieldInt("FS_ID") == tb.getFieldInt("FS_ID")) {
                        String strNom = fmd.getFieldString("FMD_NOM");
                        String strValor = "";
                        if (strNom.equals("none")) {
                           strValor = fmd.getFieldString("FMD_VALCONST");
                           if (strValor.equals("[NEW_PAGE]")) {
                              log.debug("*****************" + strValor + "*************");
                              //Anadimos el detalle al formato
                              body.getPage().add(page);
                              this.fmXML.getFooterOrBodyOrHeaderOrConfig().add(body);
                              this.CrearFoot(oConn, false);
                              this.CrearHead(oConn, false);
                              body = this.xmlFact.createBody();
                              page = this.xmlFact.createPage();
                              intContRow = 0;
                              strValor = "";
                           }
                        } else {
                           try {
                              strValor = tb.getRs().getString(strNom);
                              if (strValor == null) {
                                 strValor = "";
                              }
                           } catch (SQLException ex) {
                              if (ex.getLocalizedMessage() != null) {
                                 this.bit.GeneraBitacora(ex.getLocalizedMessage(), "system", "formato Body 1", oConn);
                              } else {
                                 log.error(ex.getMessage());
                              }
                           }
                        }
                        strValor = this.FormatValor(strValor, fmd.getFieldString("FMD_FORMATO"), oConn);
                        log.debug(strValor);
                        int intAncho = 0;
                        try {
                           intAncho = fmd.getFieldInt("FMD_ANCHO");
                        } catch (NumberFormatException ex) {
                           log.error(ex.getMessage());
                        }
                        //Validamos si es el texto tiene que ajustarse(conceptos en multiples lineas)
                        if (fmd.getFieldString("FMD_FORMATO").startsWith("textAuto") && strValor.length() > intAncho) {
                           // <editor-fold defaultstate="collapsed" desc="Ajuste con textAuto">
                           //Separamos la cadena en varias partes
                           String[] lstFrases = this.ObtenFrases(strValor, intAncho, fmd.getFieldString("FMD_FORMATO"));
                           //Recorremos cada frase
                           int intPosYCalc = this.formato.getFieldInt("FM_DETAFACTOR") * (intContRow - 1) + intFactorMinus;
                           int intPosYCalcMasLast = intPosYCalc;
                           // <editor-fold defaultstate="collapsed" desc="Ciclo para generar las frases">
                           for (int yh = 0; yh < lstFrases.length; yh++) {
                              if (lstFrases[yh] != null) {

                                 //Si es la primera linea
                                 if (yh > 0) {
                                    intContRow++;
                                 }
                                 if (yh == 0) {
                                 } else {
                                    intPosYCalc = intPosYCalc + this.formato.getFieldInt("FM_COMFACTOR") + intFactorMinus;
                                 }
                                 int intPosYCalcMas = fmd.getFieldInt("FMD_POSY") - intPosYCalc;
                                 if (intFirstYBody == 0) {
                                    intFirstYBody = this.intLastYBody;//Primera posicion de Y
                                 }
                                 this.intLastYBody = intPosYCalc;//guardamos la ultima posicion en Y
                                 intPosYCalcMasLast = intPosYCalcMas;
                                 //Creamos objeto a representar
                                 HP rot = this.xmlFact.createHP();
                                 int intPosX = fmd.getFieldInt("FMD_POSX") + intFactorMinusX;
                                 rot.setPX(intPosX + "");
                                 rot.setPY(intPosYCalcMas + "");
                                 rot.setPalign(fmd.getFieldString("FMD_ALIGN"));
                                 rot.setPcolor(fmd.getFieldString("FMD_COLOR"));
                                 rot.setPcolorbak(fmd.getFieldString("FMD_COLORFONDO"));
                                 rot.setPstyle(fmd.getFieldString("FMD_ESTILO"));
                                 rot.setPheight(fmd.getFieldString("FMD_ALTO"));
                                 rot.setPsize(fmd.getFieldString("FMD_SIZEFONT"));
                                 rot.setPwidth(fmd.getFieldString("FMD_ANCHO"));
                                 rot.setPwidthCanvas(fmd.getFieldString("FMD_WIDTH_CANVAS"));
                                 String strUrlImg = fmd.getFieldString("FMD_TITULO");
                                 String strFont = fmd.getFieldString("FMD_LETRA");
                                 if (strUrlImg.contains("[URL_IMG]")) {
                                    strUrlImg = strUrlImg.replace("[URL_IMG]", this.strPathImage);
                                 }
                                 if (strFont.contains("[URL_FONT]")) {
                                    strFont = strFont.replace("[URL_FONT]", this.strPathFonts);
                                 }
                                 rot.setUrlimg(strUrlImg);
                                 rot.setPfont(strFont);
                                 rot.setValor(lstFrases[yh]);
                                 rot.setPformato(fmd.getFieldString("FMD_FORMATO"));
                                 page.getHP().add(rot);
                                 //Si la coordeanda Y es menor a cero hacemos salto de hoja
                                 //Solamente si el formato esta configurado para ello
                                 if (this.formato.getFieldInt("FM_GEN_PAG_DETA") == 1) {
                                    if (intPosYCalcMas <= this.formato.getFieldInt("FM_POS_MIN_IN_Y")) {
                                       //Validamos si aun hay mas datos
                                       if (yh + 1 < lstFrases.length) {
                                          //Anadimos el detalle al formato
                                          body.getPage().add(page);
                                          page = this.xmlFact.createPage();
                                          log.debug("**********************Salto de hoja Y menor a cero **********************");
                                          intContRow = 0;
                                          strValor = "";
                                          intPosYCalc = this.formato.getFieldInt("FM_POS_INI_Y_PAG_DETA");
                                          intPosYCalcMasLast = intPosYCalc;
                                       }
                                    }
                                 }
                              }
                           }
                           // </editor-fold>
                           this.intLastYBody = intPosYCalcMasLast;
                           if (intFirstYBody == 0) {
                              intFirstYBody = this.intLastYBody;//Primera posicion de Y
                           }
                           // </editor-fold>
                        } else {
                           // <editor-fold defaultstate="collapsed" desc="Objeto Normal">
                           int intPosYCalc = this.formato.getFieldInt("FM_DETAFACTOR") * (intContRow - 1) + intFactorMinus;
                           intPosYCalc = fmd.getFieldInt("FMD_POSY") - intPosYCalc;
                           this.intLastYBody = intPosYCalc;//guardamos la ultima posicion en Y
                           if (intFirstYBody == 0) {
                              intFirstYBody = this.intLastYBody;//Primera posicion de Y
                           }                           //Creamos objeto a representar
                           HP rot = this.xmlFact.createHP();
                           int intPosX = fmd.getFieldInt("FMD_POSX") + intFactorMinusX;
                           rot.setPX(intPosX + "");
                           rot.setPY(intPosYCalc + "");
                           rot.setPalign(fmd.getFieldString("FMD_ALIGN"));
                           rot.setPcolor(fmd.getFieldString("FMD_COLOR"));
                           rot.setPcolorbak(fmd.getFieldString("FMD_COLORFONDO"));
                           rot.setPstyle(fmd.getFieldString("FMD_ESTILO"));
                           rot.setPheight(fmd.getFieldString("FMD_ALTO"));
                           rot.setPsize(fmd.getFieldString("FMD_SIZEFONT"));
                           int intFMD_ANCHO = fmd.getFieldInt("FMD_ANCHO") + intFactorMinusX;
                           rot.setPwidth(intFMD_ANCHO + "");
                           rot.setPwidthCanvas(fmd.getFieldString("FMD_WIDTH_CANVAS"));
                           String strUrlImg = fmd.getFieldString("FMD_TITULO");
                           String strFont = fmd.getFieldString("FMD_LETRA");
                           if (strUrlImg.contains("[URL_IMG]")) {
                              strUrlImg = strUrlImg.replace("[URL_IMG]", this.strPathImage);
                           }
                           if (strFont.contains("[URL_FONT]")) {
                              strFont = strFont.replace("[URL_FONT]", this.strPathFonts);
                           }
                           rot.setUrlimg(strUrlImg);
                           rot.setPfont(strFont);
                           rot.setValor(strValor);
                           rot.setPformato(fmd.getFieldString("FMD_FORMATO"));
                           page.getHP().add(rot);
                           // </editor-fold>
                        }
                     }
                  }
                  //Generamos paginas adicionales solo en caso de que falten items
                  if (this.formato.getFieldInt("FM_NOEVALMAXROWS") == 0) {
                     log.debug("Validamos i metemos una hoja adicional al final ");
                     log.debug("intContRow:" + intContRow);
                     log.debug("intNumRows:" + intNumRows);
                     log.debug("intContRowTmp1:" + intContRowTmp1);
                     // <editor-fold defaultstate="collapsed" desc="Validate Max rows">
                     if (intContRow + 1 > intNumRows /*&& intContRowTmp1 > intNumRows*/) {
                        body.getPage().add(page);
                        log.debug("**********************Salto de hoja Validate Max rows **********************");
                        page = this.xmlFact.createPage();
                        intContRow = 0;
                        intNumPages++;
                        this.intLastYBody = intFirstYBody;
                     }
                     // </editor-fold>
                  }
               }
               // </editor-fold>
               //end while

               // <editor-fold defaultstate="collapsed" desc="Validamos si faltan filas por pintar">
               if (this.formato.getFieldInt("FM_NOEVALMAXROWS") == 0) {
                  //Validamos si faltan filas las rellenamos con datos vacios
                  log.debug("Validamos si faltan filas por pintar");
                  log.debug("intContRow:" + intContRow);
                  log.debug("intNumRows:" + intNumRows);
                  log.debug("intNumPages:" + intNumPages);
                  //Evaluamos si no hubo filas para calcular el primer y
                  if (intContRow == 0) {
                     //Sacamos la lista de los objetos detalle del formato
                     Iterator<TableMaster> it2 = lstDeta.iterator();
                     while (it2.hasNext()) {
                        formatodeta fmd = (formatodeta) it2.next();
                        //Calculamos el primer y
                        if (fmd.getFieldInt("FMD_HEAD_FOOT_BOD") == 1
                                && fmd.getFieldInt("FS_ID") == tb.getFieldInt("FS_ID")) {

                           int intPosYCalc = this.formato.getFieldInt("FM_DETAFACTOR") * (intContRow -  1) + intFactorMinus;
                           intPosYCalc = fmd.getFieldInt("FMD_POSY") - intPosYCalc;
                           this.intLastYBody = intPosYCalc;//guardamos la ultima posicion en Y
                        }
                     }

                  }
                  if (intContRow < intNumRows) {
                     //Validamos si estamos en la segunda pagina y no hubo filas
                     /*if (intContRow == 0 && intNumPages > 1) {
                        log.debug("No hace nada...¿?");
                     } else {*/
                        // <editor-fold defaultstate="collapsed" desc="for para acompletar filas">
                        log.debug("intLastYBody ini:" + intLastYBody);
                        for (int h = intContRow; h < intNumRows; h++) {
                           int intPosYCalc = this.intLastYBody - this.formato.getFieldInt("FM_DETAFACTOR") + intFactorMinus;
                           this.intLastYBody = intPosYCalc;//guardamos la ultima posicion en Y
                           log.debug(h + " intPosYCalc " + intPosYCalc);
                           //Creamos objeto a representar
                           //Sacamos la lista de los objetos detalle del formato
                           Iterator<TableMaster> it2 = lstDeta.iterator();
                           while (it2.hasNext()) {
                              formatodeta fmd = (formatodeta) it2.next();
                              //Validamos si es un campo detalle
                              if (fmd.getFieldInt("FMD_HEAD_FOOT_BOD") == 1
                                      && fmd.getFieldInt("FS_ID") == tb.getFieldInt("FS_ID")) {
                                 String strNom = fmd.getFieldString("FMD_NOM");
                                 String strValor = "";
                                 if (strNom.equals("none")) {
                                    strValor = fmd.getFieldString("FMD_VALCONST");
                                 } else {
                                 }
                                 strValor = this.FormatValor(strValor, fmd.getFieldString("FMD_FORMATO"), oConn);
                                 //Creamos objeto a representar
                                 HP rot = this.xmlFact.createHP();
                                 int intPosX = fmd.getFieldInt("FMD_POSX") + intFactorMinusX;
                                 rot.setPX(intPosX + "");
                                 rot.setPY(intPosYCalc + "");
                                 rot.setPalign(fmd.getFieldString("FMD_ALIGN"));
                                 rot.setPcolor(fmd.getFieldString("FMD_COLOR"));
                                 rot.setPcolorbak(fmd.getFieldString("FMD_COLORFONDO"));
                                 rot.setPstyle(fmd.getFieldString("FMD_ESTILO"));
                                 rot.setPheight(fmd.getFieldString("FMD_ALTO"));
                                 rot.setPsize(fmd.getFieldString("FMD_SIZEFONT"));
                                 int intFMD_ANCHO = fmd.getFieldInt("FMD_ANCHO") + intFactorMinusX;
                                 rot.setPwidth(intFMD_ANCHO + "");
                                 rot.setPwidthCanvas(fmd.getFieldString("FMD_WIDTH_CANVAS"));
                                 String strUrlImg = fmd.getFieldString("FMD_TITULO");
                                 String strFont = fmd.getFieldString("FMD_LETRA");
                                 if (strUrlImg.contains("[URL_IMG]")) {
                                    strUrlImg = strUrlImg.replace("[URL_IMG]", this.strPathImage);
                                 }
                                 if (strFont.contains("[URL_FONT]")) {
                                    strFont = strFont.replace("[URL_FONT]", this.strPathFonts);
                                 }
                                 rot.setUrlimg(strUrlImg);
                                 rot.setPfont(strFont);
                                 rot.setValor(strValor);
                                 rot.setPformato(fmd.getFieldString("FMD_FORMATO"));
                                 page.getHP().add(rot);
                              }
                           }
                        }
                        // </editor-fold>
                     //}
                  }
               }
               // </editor-fold>
            } catch (SQLException ex) {
               if (ex.getLocalizedMessage() != null) {
                  this.bit.GeneraBitacora(ex.getLocalizedMessage(), "system", "formato Body 2", oConn);
               } else {
                  ex.printStackTrace();
               }
            }
            body.getPage().add(page);
            log.debug("**********************Agrega la ultima pagina al body**********************");
         } else {
            //Tabla maestra
         }
      }
      //Anadimos el detalle al formato
      this.fmXML.getFooterOrBodyOrHeaderOrConfig().add(body);
   }

   protected void CrearFoot(Conexion oConn, boolean bolEsMasivo) {
      int intFactorMinus = 0;
      int intFactorMinusX = 0;
      int intLasYFoot = 0;

      // <editor-fold defaultstate="collapsed" desc="Validamos numero de paginas x hoja">
      if (this.bolDosxHoja && this.bolSecondPage) {
         intFactorMinus = this.formato.getFieldInt("FM_INIMIDDLE");
      }
      if (this.bolCuatroxHoja) {
         if (this.bolSecondPage) {
            intFactorMinus = this.formato.getFieldInt("FM_INIMIDDLE");
         }
         if (this.bolRightPage) {
            intFactorMinusX = this.formato.getFieldInt("FM_INIMIDDLEY");
         }
      }
      if (this.bolSeisxHoja) {
         if (this.bolSecondPage) {
            intFactorMinus = this.formato.getFieldInt("FM_INIMIDDLE");
         }
         if (this.bolRightPage) {
            intFactorMinusX = this.formato.getFieldInt("FM_INIMIDDLEY");
         }
         if (this.bolThirdPage) {
            intFactorMinusX = this.formato.getFieldInt("FM_INIMIDDLEY") * 2;
         }
      }
      // </editor-fold>
      ArrayList<formatosql> lstHeaders = new ArrayList<formatosql>();
      if (this.tbMaster != null) {
         lstHeaders.add(tbMaster);
      }
      if (this.tbFooter != null) {
         lstHeaders.add(tbFooter);
      }
      log.debug("this.intLastYBody :" + this.intLastYBody);
      //Footer
      Footer foot = this.xmlFact.createFooter();
      //Barremos los sql
      Iterator<formatosql> itsql = lstHeaders.iterator();
      while (itsql.hasNext()) {
         formatosql objSql = itsql.next();
         //Recorremos los campos y buscamos los campos de tipo Head
         Iterator<TableMaster> it = lstDeta.iterator();
         while (it.hasNext()) {
            formatodeta fmd = (formatodeta) it.next();
            if ((fmd.getFieldInt("FMD_HEAD_FOOT_BOD") == 2)
                    && fmd.getFieldInt("FS_ID") == objSql.getFieldInt("FS_ID")) {
               String strNom = fmd.getFieldString("FMD_NOM");
               String strValor = "";
               if (strNom.equals("none")) {
                  strValor = fmd.getFieldString("FMD_VALCONST");
               } else {
                  try {
                     //Validamos si estamos procesando el objeto maestro
                     if (this.tbMaster.getFieldInt("FS_ID") == objSql.getFieldInt("FS_ID")) {
                        if (!bolEsMasivo) {
                           objSql.getRs().beforeFirst();
                           while (objSql.getRs().next()) {
                              strValor = objSql.getRs().getString(strNom);
                           }
                        } else {
                           strValor = objSql.getRs().getString(strNom);
                        }
                     } else {
                        objSql.getRs().beforeFirst();
                        while (objSql.getRs().next()) {
                           strValor = objSql.getRs().getString(strNom);
                        }
                     }
                  } catch (SQLException ex) {
                     if (ex.getLocalizedMessage() != null) {
                        log.error("ERROR EN FOOT " + ex.getLocalizedMessage());
                        log.error("ERROR EN FOOT  " + ex.getMessage());
                        this.bit.GeneraBitacora(ex.getLocalizedMessage(), "system", "formato Foot 1", oConn);
                     } else {
                        ex.printStackTrace();
                     }
                  }
               }
               strValor = this.FormatValor(strValor, fmd.getFieldString("FMD_FORMATO"), oConn);
               //Calculamos la posicion en Y en base al detalle
               int intPosYCalc = fmd.getFieldInt("FMD_POSY");
               int intHeight = fmd.getFieldInt("FMD_ALTO");
               if (this.intLastYBody != 0) {
                  intPosYCalc = this.intLastYBody - intPosYCalc + intFactorMinus;
                  if (fmd.getFieldString("FMD_FORMATO").equals("Line")) {
                     intHeight = this.intLastYBody - intHeight;
                  }
               }
               //Obtenemos el ancho
               int intAncho = 0;
               try {
                  intAncho = fmd.getFieldInt("FMD_ANCHO");
               } catch (NumberFormatException ex) {
                  log.error(ex.getMessage());
               }
               // <editor-fold defaultstate="collapsed" desc="Validamos si es el texto tiene que ajustarse(conceptos en multiples lineas)">
               if (fmd.getFieldString("FMD_FORMATO").startsWith("textAuto") && strValor.length() > intAncho) {
                  // <editor-fold defaultstate="collapsed" desc="Objetos con textAuto">
                  int intContRow = -1;
                  //Separamos la cadena en varias partes
                  String[] lstFrases = this.ObtenFrases(strValor, intAncho, fmd.getFieldString("FMD_FORMATO"));
                  // <editor-fold defaultstate="collapsed" desc="Recorremos cada frase">
                  for (int yh = 0; yh < lstFrases.length; yh++) {
                     if (lstFrases[yh] != null) {
                        intPosYCalc = 0;
                        //Si es la primera linea
                        if (yh > 0) {
                           intContRow++;
                        }
                        if (yh == 0) {
                           intPosYCalc = this.formato.getFieldInt("FM_COMFACTOR") * (intContRow + 1) + intFactorMinus;
                        } else {
                           intPosYCalc = this.formato.getFieldInt("FM_COMFACTOR") * (intContRow + 1) + intFactorMinus;
                        }
                        //intPosYCalc = fmd.getFieldInt("FMD_POSY") - intPosYCalc;
                        if (this.intLastYBody != 0) {
                           intPosYCalc = this.intLastYBody - (intPosYCalc + fmd.getFieldInt("FMD_POSY")) + intFactorMinus;
                           if (fmd.getFieldString("FMD_FORMATO").equals("Line")) {
                              intHeight = this.intLastYBody - intHeight;
                           }
                        }
                        //this.intLastYBody = intPosYCalc;//guardamos la ultima posicion en Y

                        //Creamos objeto a representar
                        HP rot = this.xmlFact.createHP();
                        int intPosX = fmd.getFieldInt("FMD_POSX") + intFactorMinusX;
                        rot.setPX(intPosX + "");
                        rot.setPY(intPosYCalc + "");
                        rot.setPalign(fmd.getFieldString("FMD_ALIGN"));
                        rot.setPcolor(fmd.getFieldString("FMD_COLOR"));
                        rot.setPcolorbak(fmd.getFieldString("FMD_COLORFONDO"));
                        rot.setPstyle(fmd.getFieldString("FMD_ESTILO"));
                        rot.setPheight(fmd.getFieldString("FMD_ALTO"));
                        rot.setPsize(fmd.getFieldString("FMD_SIZEFONT"));
                        rot.setPwidth(fmd.getFieldString("FMD_ANCHO"));
                        rot.setPwidthCanvas(fmd.getFieldString("FMD_WIDTH_CANVAS"));
                        String strUrlImg = fmd.getFieldString("FMD_TITULO");
                        String strFont = fmd.getFieldString("FMD_LETRA");
                        if (strUrlImg.contains("[URL_IMG]")) {
                           strUrlImg = strUrlImg.replace("[URL_IMG]", this.strPathImage);
                        }
                        if (strFont.contains("[URL_FONT]")) {
                           strFont = strFont.replace("[URL_FONT]", this.strPathFonts);
                        }
                        rot.setUrlimg(strUrlImg);
                        rot.setPfont(strFont);
                        rot.setValor(lstFrases[yh]);
                        rot.setPformato(fmd.getFieldString("FMD_FORMATO"));
                        foot.getHP().add(rot);
                     }
                  }
                  // </editor-fold>
                  // </editor-fold>
               } else {
                  // <editor-fold defaultstate="collapsed" desc="Objeto normal">
                  //Creamos objeto a representar
                  HP rot = this.xmlFact.createHP();
                  int intPosX = fmd.getFieldInt("FMD_POSX") + intFactorMinusX;
                  rot.setPX(intPosX + "");
                  rot.setPY(intPosYCalc + "");
                  rot.setPalign(fmd.getFieldString("FMD_ALIGN"));
                  rot.setPcolor(fmd.getFieldString("FMD_COLOR"));
                  rot.setPcolorbak(fmd.getFieldString("FMD_COLORFONDO"));
                  rot.setPstyle(fmd.getFieldString("FMD_ESTILO"));
                  rot.setPheight(intHeight + "");
                  rot.setPsize(fmd.getFieldString("FMD_SIZEFONT"));
                  int intFMD_ANCHO = fmd.getFieldInt("FMD_ANCHO") + intFactorMinusX;
                  rot.setPwidth(intFMD_ANCHO + "");
                  rot.setPwidthCanvas(fmd.getFieldString("FMD_WIDTH_CANVAS"));
                  String strUrlImg = fmd.getFieldString("FMD_TITULO");
                  String strFont = fmd.getFieldString("FMD_LETRA");
                  if (strUrlImg.contains("[URL_IMG]")) {
                     strUrlImg = strUrlImg.replace("[URL_IMG]", this.strPathImage);
                  }
                  if (strFont.contains("[URL_FONT]")) {
                     strFont = strFont.replace("[URL_FONT]", this.strPathFonts);
                  }
                  rot.setUrlimg(strUrlImg);
                  rot.setPfont(strFont);
                  rot.setValor(strValor);
                  rot.setPformato(fmd.getFieldString("FMD_FORMATO"));
                  foot.getHP().add(rot);
                  // </editor-fold>
               }
               // </editor-fold>
            }
         }
      }

      //Anadimos el pie al formato xml
      this.fmXML.getFooterOrBodyOrHeaderOrConfig().add(foot);
   }

   /**
    * Crea los tags de configuracion del xml
    *
    * @param intTotalPages Es el total de paginas
    */
   protected void CreaConfig(int intTotalPages) {
      //Config
      Title tit = this.xmlFact.createTitle();
      tit.setvalue(this.formato.getFieldString("FM_NOMBRE"));
      TypePage tpage = this.xmlFact.createTypePage();
      tpage.setvalue(this.formato.getFieldString("FM_TIPOHOJA"));
      Orientation ori = this.xmlFact.createOrientation();
      if (this.formato.getFieldInt("FM_ORIENTACION") == 0) {
         ori.setvalue("horizontal");
      } else {
         ori.setvalue("vertical");
      }
      LeftMargin left = this.xmlFact.createLeftMargin();
      left.setvalue(this.formato.getFieldString("FM_LEFT"));
      RightMargin right = this.xmlFact.createRightMargin();
      right.setvalue(this.formato.getFieldString("FM_RIGHT"));
      TopMargin top = this.xmlFact.createTopMargin();
      top.setvalue(this.formato.getFieldString("FM_TOP"));
      BottomMargin bot = this.xmlFact.createBottomMargin();
      bot.setvalue(this.formato.getFieldString("FM_BOTTOM"));
      Width width = this.xmlFact.createWidth();
      width.setvalue(this.formato.getFieldString("FM_WIDTH"));
      Height height = this.xmlFact.createHeight();
      height.setvalue(this.formato.getFieldString("FM_HEIGHT"));
      TotalPages tot = this.xmlFact.createTotalPages();
      tot.setvalue(intTotalPages + "");
      //
      Config cnf = this.xmlFact.createConfig();
      cnf.getTotalPagesOrHeightOrWidthOrBottomMarginOrTopMarginOrRightMarginOrLeftMarginOrOrientationOrTypePageOrTitle().add(tit);
      cnf.getTotalPagesOrHeightOrWidthOrBottomMarginOrTopMarginOrRightMarginOrLeftMarginOrOrientationOrTypePageOrTitle().add(tpage);
      cnf.getTotalPagesOrHeightOrWidthOrBottomMarginOrTopMarginOrRightMarginOrLeftMarginOrOrientationOrTypePageOrTitle().add(ori);
      cnf.getTotalPagesOrHeightOrWidthOrBottomMarginOrTopMarginOrRightMarginOrLeftMarginOrOrientationOrTypePageOrTitle().add(left);
      cnf.getTotalPagesOrHeightOrWidthOrBottomMarginOrTopMarginOrRightMarginOrLeftMarginOrOrientationOrTypePageOrTitle().add(right);
      cnf.getTotalPagesOrHeightOrWidthOrBottomMarginOrTopMarginOrRightMarginOrLeftMarginOrOrientationOrTypePageOrTitle().add(top);
      cnf.getTotalPagesOrHeightOrWidthOrBottomMarginOrTopMarginOrRightMarginOrLeftMarginOrOrientationOrTypePageOrTitle().add(bot);
      cnf.getTotalPagesOrHeightOrWidthOrBottomMarginOrTopMarginOrRightMarginOrLeftMarginOrOrientationOrTypePageOrTitle().add(width);
      cnf.getTotalPagesOrHeightOrWidthOrBottomMarginOrTopMarginOrRightMarginOrLeftMarginOrOrientationOrTypePageOrTitle().add(height);
      this.fmXML.getFooterOrBodyOrHeaderOrConfig().add(cnf);
   }
   /*Crea el xml del formato*/

   protected void CreaFormato(Conexion oConn) {
      try {
         JAXBContext jaxbContext = JAXBContext.newInstance("generated");

         Marshaller marshaller = jaxbContext.createMarshaller();
         //Dependiendo de la opcion del usuario generamos la salida
         if (this.intTypeOut == Formateador.FILE) {
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            try {
               marshaller.marshal(this.fmXML, new FileOutputStream(this.strPath + this.strNomFile));
            } catch (FileNotFoundException ex) {
               bit.GeneraBitacora(ex.getMessage(), "system", "formato", oConn);
            }
         }
         if (this.intTypeOut == Formateador.OBJECT) {
         }
         if (this.intTypeOut == Formateador.OUT) {
         }

      } catch (JAXBException ex) {
         bit.GeneraBitacora(ex.getMessage(), "system", "formato", oConn);
      }

   }
   // </editor-fold>

   /**
    * Calcula las frases del texto con formato textAuto
    *
    * @param strTextoOriginal Es el valor del texto a pintar
    * @param intAncho Es el ancho maximo de la cadena
    * @param strFormato Es el tipo de formato o ajuste solicitado
    * @return Regresa una cadena con el texto separado en frases
    */
   protected String[] ObtenFrases(String strTextoOriginal, int intAncho, String strFormato) {
      // <editor-fold defaultstate="collapsed" desc="Valores iniciales">
      String[] lstFrases = null;
      //Contiene el texto separado por espacios
      ArrayList<String> lstColaNuevaTmp = new ArrayList<String>();
      char c10 = 10;
      String[] lstColaSaltos10 = null;
      // </editor-fold>

      //Validamos el tipo de ajuste
      if (strFormato.equals("textAuto")) {
         //Ajuste de texto que contiene estrofas de una oración
         // <editor-fold defaultstate="collapsed" desc="Calculamos la cola de textos por ajustar">
         if (strTextoOriginal.contains(c10 + "")) {
            //Generamos una cola en base a los saltos de linea
            lstColaSaltos10 = strTextoOriginal.split(c10 + "");
         } else {
            //Generamos una cola con 1 solo elemento que el texto original
            lstColaSaltos10 = new String[]{strTextoOriginal};
         }
         // </editor-fold>

         // <editor-fold defaultstate="collapsed" desc="Ciclo para recorrer todos los textos por revisar">
         for (int y = 0; y < lstColaSaltos10.length; y++) {
            String[] lstColaActual = lstColaActual = lstColaSaltos10[y].split(" ");

            // <editor-fold defaultstate="collapsed" desc="Ciclo para calcular las frases del texto en base al ancho">
            String strFraseAct = "";
            String strFraseAnt = "";
            for (int i = 0; i < lstColaActual.length; i++) {
               String strPalabraNva = lstColaActual[i];
               //Validamos que no este vacia la frase
               if (!strPalabraNva.isEmpty()) {
                  strFraseAnt = new String(strFraseAct);
                  if (i == 0) {
                     strFraseAct += strPalabraNva;
                  } else {
                     strFraseAct += " " + strPalabraNva;
                  }
                  // <editor-fold defaultstate="collapsed" desc="Hacemos el corte si la frase no tiene espacios">
                  if (strFraseAct.length() > intAncho) {
                     lstColaNuevaTmp.add(strFraseAnt);
                     strFraseAct = new String(strPalabraNva);
                  } else {
                     //Hacemos nada
                  }
                  // </editor-fold>
               }
            }
            if (strFraseAct.length() > 0) {
               //Si la frase es mayor al ancho hacemos el ajuste en base a las letras
               if (strFraseAct.length() > intAncho) {
                  // <editor-fold defaultstate="collapsed" desc="Ajuste en base al tamanio del texto sin considerar espacios">
                  int intPartes = strFraseAct.length() / intAncho;
                  String[] lstFrasesTmp = new String[intPartes + 1];
                  for (int bq = 0; bq <= intPartes; bq++) {
                     if (((bq * intAncho) + intAncho) > strFraseAct.length()) {
                        lstFrasesTmp[bq] = strFraseAct.substring(bq * intAncho, strFraseAct.length());
                     } else {
                        lstFrasesTmp[bq] = strFraseAct.substring(bq * intAncho, (bq * intAncho) + intAncho);
                     }
                  }
                  lstColaNuevaTmp.addAll(Arrays.asList(lstFrasesTmp));
                  // </editor-fold>
               } else {
                  lstColaNuevaTmp.add(strFraseAct);
               }
            }
            // </editor-fold>
         }

         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="Convertimos arraylist a un arreglo">
         lstFrases = new String[lstColaNuevaTmp.size()];
         Iterator<String> it4 = lstColaNuevaTmp.iterator();
         int intContaNva = -1;
         while (it4.hasNext()) {
            String strFraseNva = it4.next();
            intContaNva++;
            lstFrases[intContaNva] = strFraseNva;
         }
         // </editor-fold>
      } else {
         if (strFormato.equals("textAutoText")) {
            //Ajuste de texto que puede separarse y que contiene solo texto como una clave
            // <editor-fold defaultstate="collapsed" desc="Ajuste en base al tamanio del texto sin considerar espacios">
            int intPartes = strTextoOriginal.length() / intAncho;
            lstFrases = new String[intPartes + 1];
            for (int bq = 0; bq <= intPartes; bq++) {
               if (((bq * intAncho) + intAncho) > strTextoOriginal.length()) {
                  lstFrases[bq] = strTextoOriginal.substring(bq * intAncho, strTextoOriginal.length());
               } else {
                  lstFrases[bq] = strTextoOriginal.substring(bq * intAncho, (bq * intAncho) + intAncho);
               }
            }
            // </editor-fold>
         } else {
            //Default copia el valor tal cual
            lstFrases = new String[]{strTextoOriginal};
         }
      }

      return lstFrases;
   }
}
