package comSIWeb.Operaciones.Formatos;

import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *Representa un objeto para generar formatos de manera masiva
 * @author zeus
 */
public class FormateadorMasivo extends Formateador {

   protected int intNumRows = 0;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(FormateadorMasivo.class.getName());

   public FormateadorMasivo() {
      super();
   }

   /**
    * Genera el formato en formato Xml
    * @param oConn Es la conexion
    * @param strFiltro Es el filtro para la consulta
    * @return Regresa una caden con OK si se pudo generar el formato
    * Cuando es dos por hoja el primer formato es 0 el segundo es 1
    * Cuando es cuatro por hoja el primer formato es 0 el segundo es 1 el tercero es 2 y el cuarto es 3
    */
   public String DoFormat(Conexion oConn, String strFiltro) {
      String strRes = "OK";
      this.strNomFile = this.strNomLlave + "Masivo" + ".xml";
      //Ejecutamos los SQL
      int intIdMaster = 0;
      Iterator<TableMaster> it = this.lstSQL.iterator();
      while (it.hasNext()) {
         formatosql tb = (formatosql) it.next();
         if (tb.getFieldInt("FS_ESMASTER") == 1) {
            String strRes2 = tb.ExecuteSQLMasterMasivo(oConn, strFiltro);
            if (strRes2.equals("OK")) {
               intIdMaster = tb.getFieldInt("FS_ID");
               log.debug("intIdMaster:" + intIdMaster);
               tbMaster = tb;
            } else {
               strRes = strRes2;
            }
            break;
         }
      }
      //Validamos si existe un sql Master
      int intRows = 0;
      if (intIdMaster != 0) {
         try {
            int intPar = -1;
            /*Recorremos renglon por renglon*/
            if (tbMaster.getRs() != null) {
               while (tbMaster.getRs().next()) {
                  intRows++;
                  intPar++;
                  // <editor-fold defaultstate="collapsed" desc="Ejecutamos los sql secundarios">
                  it = this.lstSQL.iterator();
                  while (it.hasNext()) {
                     formatosql tb = (formatosql) it.next();
                     if (tb.getFieldInt("FS_ESMASTER") == 0) {
                        log.debug("ejecutando detalle:" + tb.getFieldInt("FS_ID"));
                        String strRes2 = tb.ExecuteSQL(oConn, tbMaster);
                        if (strRes2.equals("OK")) {
                        } else {
                           strRes = strRes2;
                        }
                        if (tb.getFieldInt("FS_ESFOOT") == 1) {
                           this.tbFooter = tb;
                        }
                     }
                  }
                  //Termina ejecucion de sql detalle y foot
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="Si todos los SQL resultaron exitosos">
                  if (strRes.equals("OK")) {
                     if (intPar == 1 && this.bolDosxHoja) {
                        this.bolSecondPage = true;
                     } else {
                        this.bolSecondPage = false;
                     }
                     // <editor-fold defaultstate="collapsed" desc="Reglas 4 por hoja">
                     if (this.bolCuatroxHoja) {
                        if (intPar == 0) {
                           this.bolRightPage = false;
                           this.bolSecondPage = false;
                        }
                        if (intPar == 1) {
                           this.bolRightPage = true;
                           this.bolSecondPage = false;
                        }
                        if (intPar == 2) {
                           this.bolRightPage = false;
                           this.bolSecondPage = true;
                        }
                        if (intPar == 3) {
                           this.bolRightPage = true;
                           this.bolSecondPage = true;
                        }
                     }
                     // </editor-fold>
                     // <editor-fold defaultstate="collapsed" desc="Reglas 6 por hoja">
                     if (this.bolSeisxHoja) {
                        if (intPar == 0) {
                           this.bolRightPage = false;
                           this.bolSecondPage = false;
                           this.bolThirdPage = false;
                        }
                        if (intPar == 1) {
                           this.bolRightPage = true;
                           this.bolSecondPage = false;
                           this.bolThirdPage = false;
                        }
                        if (intPar == 2) {
                           this.bolRightPage = false;
                           this.bolSecondPage = false;
                           this.bolThirdPage = true;
                        }
                        if (intPar == 3) {
                           this.bolRightPage = false;
                           this.bolSecondPage = true;
                           this.bolThirdPage = false;
                        }
                        if (intPar == 4) {
                           this.bolRightPage = true;
                           this.bolSecondPage = true;
                           this.bolThirdPage = false;
                        }
                        if (intPar == 5) {
                           this.bolRightPage = true;
                           this.bolSecondPage = true;
                           this.bolThirdPage = true;
                        }
                     }
                     // </editor-fold>
                     //Creamos el formato
                     CrearHead(oConn, true);
                     CrearBody(oConn);
                     CrearFoot(oConn, true);
                     if (this.bolDosxHoja && intPar == 1) {
                        intPar = -1;
                     }
                     if (this.bolCuatroxHoja && intPar == 3) {
                        intPar = -1;
                     }
                     if (this.bolSeisxHoja && intPar == 5) {
                        intPar = -1;
                     }
                  }
                  // </editor-fold>
               }
               /*Cerramos la tabla maestra*/
               tbMaster.getRs().close();
            }
         } catch (SQLException ex) {
            Logger.getLogger(FormateadorMasivo.class.getName()).log(Level.SEVERE, null, ex);
         }
         intNumRows = intRows;
         //Generamos el formato con los datos
         int intTotalPages = 0;
         CreaConfig(intTotalPages);
         //Generamos el formato dependiendo de la opcion usada FILE OUT OBJECT
         CreaFormato(oConn);
         //Cerramos las conexiones
         CierraRs(oConn);
      } else {
         strRes = "ERROR:El formato no contiene un sql Maestro";
      }
      return strRes;
   }

   /**
    * Regresa el total de paginas generadas
    * @return regresa el total de paginas generadas
    */
   public int getIntNumRows() {
      return intNumRows;
   }
}
