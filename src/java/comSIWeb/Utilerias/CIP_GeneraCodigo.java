package comSIWeb.Utilerias;

import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.Formularios;
import comSIWeb.Operaciones.FormulariosDeta;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zeus
 */
public class CIP_GeneraCodigo {

   /*Generara los formularios para las tablas que necesitemos*/
   public static void GeneraPantallasAuto(ArrayList<String> lstTablas, Conexion oConn) {
      ArrayList<FormulariosDeta> lstDeta = new ArrayList<FormulariosDeta>();//Representa los campos del formulario
      Iterator<String> it = lstTablas.iterator();
      while (it.hasNext()) {
         String strNameTable = it.next();
         Formularios form = new Formularios();
         String strfrmKEY = "";
         form.setFieldString("frm_title", strNameTable);
         //El campo solo admite 10 caracteres
         if (strNameTable.length() > 10) {
            form.setFieldString("frm_abrv", strNameTable.substring(0, 10));
         } else {
            form.setFieldString("frm_abrv", strNameTable);
         }
         form.setFieldString("frm_table", strNameTable);
         form.setFieldString("frm_scriptBeforeAdd", "");
         form.setFieldString("frm_scriptAfterAdd", "");
         form.setFieldString("frm_scriptBeforeMod", "");
         form.setFieldString("frm_scriptAfterMod", "");
         form.setFieldString("frm_scriptBeforeDel", "");
         form.setFieldString("frm_key", strfrmKEY);
         form.setFieldString("frm_urlgrid", "CIP_TablaOp.jsp?ID=5");
         form.setFieldString("frm_urlNew", "CIP_TablaOp.jsp?ID=1");
         form.setFieldString("frm_urlEd", "CIP_TablaOp.jsp?ID=1");
         form.setFieldString("frm_urlDel", "CIP_TablaOp.jsp?ID=3");
         form.setFieldString("frm_urlData", "CIP_TablaOp.jsp?ID=4");
         form.setFieldString("frm_urlDataPrint", "CIP_TablaOp.jsp?ID=2");
         form.setFieldString("frm_xmlNodoIni", strNameTable);
         form.setFieldString("frm_xmlNodoSec", strNameTable + "s");
         form.setFieldString("frm_tipoorden", "asc");
         form.setFieldInt("frm_usaAjax", 1);
         form.setFieldInt("frm_gridrows", 30);
         try {
            String strSql = "describe " + strNameTable;
            ResultSet rs = oConn.runQuery(strSql, true);
            int intOrden = -1;
            while (rs.next()) {
               String strName = new String(rs.getString("Field"));
               String strType = new String(rs.getString("Type"));
               String strKey = new String(rs.getString("Key"));
               String strExtra = new String(rs.getString("Extra"));
               intOrden++;
               String strTipoValorDefa = "";
               String strTipo = "";
               String strTipoDato = "";
               String strfrmd_val_num = "0";
               int intAncho = 0;
               FormulariosDeta formdeta = new FormulariosDeta();
               formdeta.setFieldString("frmd_nombre", strName);
               formdeta.setFieldString("frmd_titulo", strName);
               formdeta.setFieldString("frmd_titcorto", strName);
               formdeta.setFieldInt("frmd_orden", intOrden);
               formdeta.setFieldString("frmd_align", "left");
               formdeta.setFieldInt("frmd_nuevo", 1);
               formdeta.setFieldInt("frmd_modif", 1);
               formdeta.setFieldInt("frmd_consulta", 1);
               formdeta.setFieldInt("frmd_impresion", 1);
               formdeta.setFieldString("frmd_tipo", "text");
               //DETECTAMOS CAMPO LLAVE
               if (strExtra.equals("auto_increment") && strKey.equals("PRI")) {
                  strfrmKEY = new String(strName);
                  formdeta.setFieldInt("frmd_nuevo", 0);
                  formdeta.setFieldInt("frmd_modif", 1);
                  formdeta.setFieldInt("frmd_consulta", 1);
                  formdeta.setFieldInt("frmd_impresion", 0);
                  formdeta.setFieldString("frmd_tipo", "hidden");
               }
               if (strType.equals("text")) {
                  strTipoValorDefa = "\"\"";
                  strTipoDato = "text";
                  intAncho = 30;
                  formdeta.setFieldInt("frmd_renglon", 5);
               } else {
                  if (strType.substring(0, 7).equals("varchar")) {
                     strTipoValorDefa = "";
                     strTipoDato = "text";
                     intAncho = Integer.valueOf(strType.replace("varchar(", "").replace(")", ""));
                  } else {
                     if (strType.substring(0, 3).equals("int")) {
                        strTipoValorDefa = "0";
                        strTipoDato = "integer";
                        strfrmd_val_num = "1";
                        intAncho = 10;
                     } else {
                        if (strType.substring(0, 8).equals("smallint")) {
                           //System.out.println(" es numerico");
                           strTipoValorDefa = "0";
                           strTipoDato = "integer";
                           strfrmd_val_num = "1";
                           intAncho = 10;
                        } else {
                           if (strType.substring(0, 7).equals("decimal")) {
                              strTipoValorDefa = "0.0";
                              strTipoDato = "double";
                              strfrmd_val_num = "1";
                              intAncho = 15;
                           }
                        }
                     }
                  }
               }
               formdeta.setFieldInt("frmd_ancho", intAncho);
               formdeta.setFieldInt("frmd_maxlen", intAncho);
               formdeta.setFieldString("frmd_valor", strTipoValorDefa);
               formdeta.setFieldString("frmd_dato", strTipoDato);
               formdeta.setFieldString("frmd_val_num", strfrmd_val_num);
               formdeta.setFieldString("frmd_tablaext", "");
               formdeta.setFieldString("frmd_tabla_envio", "");
               formdeta.setFieldString("frmd_tabla_mostrar", "");
               lstDeta.add(formdeta);
            }
         } catch (SQLException ex) {
            Logger.getLogger(CIP_GeneraCodigo.class.getName()).log(Level.SEVERE, null, ex);
         }
         form.setFieldString("frm_key", strfrmKEY);
         form.setFieldString("frm_orden", strfrmKEY);
         //Agregamos la pantalla
         form.setBolGetAutonumeric(true);
         String strResp = form.Agrega(oConn);
         if (strResp.equals("OK")) {
            String strValorKey = form.getValorKey();
            int intValorKey = Integer.valueOf(strValorKey);
            //Recorremos las partidas del detalle
            Iterator<FormulariosDeta> it2 = lstDeta.iterator();
            while (it2.hasNext()) {
               FormulariosDeta form_deta = it2.next();
               form_deta.setFieldInt("frm_id", intValorKey);
               String strResp2 = form_deta.Agrega(oConn);
            }
            lstDeta.clear();
         }
      }

   }

   /**
    * Genera las entidades de las tablas de la base de datos
    * @param oConn Es la conexion
    */
   public static void GeneraEntidades(Conexion oConn) {
      String strSql = "show tables";
      ResultSet rs;
      ResultSet rs2;
      char chr10 = 10;
      oConn.setBolMostrarQuerys(true);
      try {
         rs2 = oConn.runQuery(strSql);
         while (rs2.next()) {
            String strNameTable = rs2.getString(1);
            System.out.println("strNameTable " + strNameTable);
            String strTxtClase = "public class " + strNameTable + " extends TableMaster {" + chr10;
            strTxtClase += "public " + strNameTable + "() {" + chr10;
            strTxtClase += "super(\"" + strNameTable + "\", \"CAMPOLLAVE\",\"\",\"\");" + chr10;
            //Obtenemos estructura

            strSql = "describe " + strNameTable;
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               String strName = rs.getString("Field");
               String strType = rs.getString("Type");
               String strTipoValorDefa = "";
               if (strType.equals("text")) {
                  strTipoValorDefa = "\"\"";
               } else {
                  if (strType.equals("blob")) {
                     strTipoValorDefa = "\"\"";
                  } else {
                     if (strType.startsWith("varchar")) {
                        strTipoValorDefa = "\"\"";
                     } else {
                        if (strType.startsWith("int")) {
                           strTipoValorDefa = "0";
                        } else {
                           if (strType.startsWith("smallint")) {
                              //System.out.println(" es numerico");
                              strTipoValorDefa = "0";
                           } else {
                              if (strType.startsWith("decimal")) {
                                 strTipoValorDefa = "0.0";
                              }
                           }

                        }

                     }
                  }
               }

               strTxtClase += "this.Fields.put(\"" + strName + "\", " + strTipoValorDefa + ");" + chr10;

            }
            strTxtClase += "}" + chr10;
            strTxtClase += "}" + chr10;
            System.out.println(strTxtClase);
         }

      } catch (SQLException ex) {
         ex.fillInStackTrace();
         System.out.println("hubo ubn erroir..");
      }
   }
}
