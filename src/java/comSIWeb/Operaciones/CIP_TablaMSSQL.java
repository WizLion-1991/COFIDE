package comSIWeb.Operaciones;

import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Utilerias.Fechas;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *Clase que hereda las funcionalidades de CIP_TABLA para sobrecargar
 * las consultas para paginacion
 * @author zeus
 */
public class CIP_TablaMSSQL extends CIP_Tabla {
   
   private static final Logger log = LogManager.getLogger(CIP_TablaMSSQL.class.getName());

   /**
    * Constructor del  objeto que procesa las peticiones de las pantallas en automatico
    * @param NomTabla Es el nombre de la tabla
    * @param NomKey Es la llave primaria de la tabla
    * @param NomKey2 Es la llave secundaria de la tabla
    * @param NomKey3 Es la llave terciaria de la tabla
    * @param varSesiones Es el objeto con las variables de sesion
    */
   public CIP_TablaMSSQL(String NomTabla, String NomKey, String NomKey2, String NomKey3, VariableSession varSesiones) {
      super(NomTabla, NomKey, NomKey2, NomKey3, varSesiones);
   }
   //Realiza una consulta y devuelve un XML para el GRID

   @Override
   public String ConsultaXML(Conexion oConn) {
      String strXML = "";
      String strFiltro = "";
      Fechas fecha = new Fechas();
      /*************************FILTRO****************/
      String strSql = "SELECT * FROM " + this.NomTabla + " ";
      if (!this.ValorKey.trim().equals("0") && !this.ValorKey.trim().equals("")) {
         strFiltro += this.NomKey + " = '" + this.ValorKey + "'";
      }
      //Validacion para cuando la tabla de consulta contengan el campo sucursales
      //Recorremos campo por campo
      Iterator it = this.lst.iterator();
      while (it.hasNext()) {
         FormulariosDeta formDeta = (FormulariosDeta) it.next();
         String strfrmd_nombre = formDeta.getFieldString("frmd_nombre");
         String strfrmd_consulta = formDeta.getFieldString("frmd_consulta");
         if (strfrmd_consulta.equals("1") && strfrmd_nombre.equals("SC_ID") && !this.NomTabla.equals("usuarios")
                 && !this.NomTabla.equals("vta_sucursal")) {
            strFiltro += " AND  " + strfrmd_nombre + " in (" + this.varSesiones.getStrLstSucursales() + ") ";
            break;
         }
      }
      //Validacion para cuando la tabla de consulta contengan el campo Empresa
      //Recorremos campo por campo
      it = this.lst.iterator();
      while (it.hasNext()) {
         FormulariosDeta formDeta = (FormulariosDeta) it.next();
         String strfrmd_nombre = formDeta.getFieldString("frmd_nombre");
         String strfrmd_consulta = formDeta.getFieldString("frmd_consulta");
         if (strfrmd_consulta.equals("1") && strfrmd_nombre.equals("EMP_ID")
                 && !this.NomTabla.equals("usuarios")
                 && !this.NomTabla.equals("vta_foliosempresa")
                 && !this.NomTabla.equals("vta_sucursal")
                 && !this.NomTabla.equals("vta_empresas")) {
            if (this.varSesiones.getIntIdEmpresa() != 0) {
               strFiltro += " AND  " + strfrmd_nombre + " = " + this.varSesiones.getIntIdEmpresa() + " ";
            }
            break;
         }
      }
      //Validamos si nos mandaron un campo para hacer un filtro
      if (this.searchField.trim().length() > 0) {
         if (searchString.trim().length() > 0) {
            //Buscamos si el dato a buscar es numerico y si el dato es numerico
            //Validamos si nos mandaron algun otro valor
            it = this.lst.iterator();
            while (it.hasNext()) {
               FormulariosDeta formDeta = (FormulariosDeta) it.next();
               String strfrmd_nombre = formDeta.getFieldString("frmd_nombre");
               String strfrmd_consulta = formDeta.getFieldString("frmd_consulta");
               String strfrmd_dato = formDeta.getFieldString("frmd_dato");
               //Buscamos el campo
               if (strfrmd_consulta.equals("1") && !strfrmd_dato.equals("none")
                       && strfrmd_nombre.equals(this.searchField)) {
                  if (strfrmd_dato.toLowerCase().equals("integer")) {
                     //Convertimos el valor en numerico
                     int intValor = 0;
                     try {
                        intValor = Integer.valueOf(searchString);
                        String strFiltroTmp = " AND  " + this.searchField + " ";
                        if (this.searchOper.equals("eq")) {
                           strFiltroTmp += " = " + searchString + "";
                        }
                        if (this.searchOper.equals("ne")) {
                           strFiltroTmp += " <> " + searchString + "";
                        }
                        if (this.searchOper.equals("lt")) {
                           strFiltroTmp += " < " + searchString + "";
                        }
                        if (this.searchOper.equals("le")) {
                           strFiltroTmp += " <= " + searchString + "";
                        }
                        if (this.searchOper.equals("gt")) {
                           strFiltroTmp += " >" + searchString + "";
                        }
                        if (this.searchOper.equals("ge")) {
                           strFiltroTmp += " >=" + searchString + "";
                        }
                        strFiltro = strFiltroTmp + " " + strFiltro;
                     } catch (NumberFormatException ex) {
                        //El valor no es numerico entonces buscamos si es un campo de tipo text configurado para mostrar datos de otra tabla
                        if (_ObtieneInfoOtraTabla(formDeta)) {
                           //Obtenemos en la tabla externa el id a buscar
                           _BuscaDatosExternoSearch(searchString, formDeta, oConn, this.searchOper);
                           //Validamos si hubo id's externos para usarlos como parte del filtro
                           if (formDeta.getLstElements().size() > 0) {
                              String strFiltroTmp = " AND  " + this.searchField + " ";
                              strFiltroTmp += " IN (";
                              Iterator<String> it3 = formDeta.getLstElements().iterator();
                              while (it3.hasNext()) {
                                 String strValorFiltroM = it3.next();
                                 strFiltroTmp += strValorFiltroM + ",";
                              }
                              strFiltroTmp = strFiltroTmp.substring(0, strFiltroTmp.length() - 1);
                              strFiltroTmp += ")";
                              strFiltro = strFiltroTmp + " " + strFiltro;
                           }
                        } else {
                           System.out.println("Error al recuperar valor numerico..." + this.NomTabla + " strfrmd_nombre " + strfrmd_nombre + " strfrmd_valor " + searchString);
                        }
                     }
                     break;
                  } else {
                     //Debe ser texto
                     String strFiltroTmp = " AND  " + this.searchField + " ";
                     if (this.searchOper.equals("bw")) {
                        strFiltroTmp += " like'" + searchString + "%'";
                     }
                     if (this.searchOper.equals("eq")) {
                        strFiltroTmp += " = '" + searchString + "'";
                     }
                     if (this.searchOper.equals("ne")) {
                        strFiltroTmp += " <> '" + searchString + "'";
                     }
                     if (this.searchOper.equals("lt")) {
                        strFiltroTmp += " < '" + searchString + "'";
                     }
                     if (this.searchOper.equals("le")) {
                        strFiltroTmp += " <= '" + searchString + "'";
                     }
                     if (this.searchOper.equals("gt")) {
                        strFiltroTmp += " >'" + searchString + "'";
                     }
                     if (this.searchOper.equals("ge")) {
                        strFiltroTmp += " >='" + searchString + "'";
                     }
                     if (this.searchOper.equals("ew")) {
                        strFiltroTmp += " like'%" + searchString + "'";
                     }
                     if (this.searchOper.equals("cn")) {
                        strFiltroTmp += " like'%" + searchString + "%'";
                     }
                     strFiltro = strFiltroTmp + " " + strFiltro;
                     break;
                  }
               }
            }
         }
      } else {
         if (this._search.equals("true")) {
            //Validamos si nos mandaron algun otro valor
            it = this.lst.iterator();
            while (it.hasNext()) {
               FormulariosDeta formDeta = (FormulariosDeta) it.next();
               String strfrmd_nombre = formDeta.getFieldString("frmd_nombre");
               String strfrmd_consulta = formDeta.getFieldString("frmd_consulta");
               String strfrmd_dato = formDeta.getFieldString("frmd_dato");
               String strfrmd_tipo = formDeta.getFieldString("frmd_tipo");
               String strValor = String.valueOf(this.Fields.get(strfrmd_nombre));
               if (strfrmd_consulta.equals("1") && !strfrmd_dato.equals("none")) {
                  if (strfrmd_dato.equals("integer")) {
                     if (strfrmd_tipo.equals("radio")) {
                        if (!strValor.equals("999")) {
                           strFiltro += " AND  " + strfrmd_nombre + " = " + strValor + " ";
                        }
                     } else {
                        if (strfrmd_tipo.equals("num_per")) {
                           strFiltro += " AND " + strfrmd_nombre + " >= '" + strValor + "' AND " + strfrmd_nombre + " <= '" + formDeta.getStrValorAdi2() + "' ";
                        } else {
                           if (Integer.valueOf(strValor) != 0) {
                              strFiltro += " AND  " + strfrmd_nombre + " = " + strValor + " ";
                           } else {
                              //El valor no es numerico entonces buscamos si es un campo de tipo text configurado para mostrar datos de otra tabla
                              if (_ObtieneInfoOtraTabla(formDeta)) {
                                 if (formDeta.getLstElements().size() > 0) {
                                    String strFiltroTmp = " AND  " + strfrmd_nombre + " ";
                                    strFiltroTmp += " IN (";
                                    Iterator<String> it3 = formDeta.getLstElements().iterator();
                                    while (it3.hasNext()) {
                                       String strValorFiltroM = it3.next();
                                       strFiltroTmp += strValorFiltroM + ",";
                                    }
                                    strFiltroTmp = strFiltroTmp.substring(0, strFiltroTmp.length() - 1);
                                    strFiltroTmp += ")";
                                    strFiltro = strFiltroTmp + " " + strFiltro;
                                 }
                              }
                           }
                        }
                     }
                  } else {
                     if (strfrmd_dato.equals("double")) {
                        if (Double.valueOf(strValor) != 0) {
                           strFiltro += " AND  " + strfrmd_nombre + " = " + strValor + " ";
                        }
                     } else {
                        if (strfrmd_tipo.equals("date_per")) {
                           if (!strValor.equals("") && !strValor.equals("null")) {
                              strFiltro += " AND " + strfrmd_nombre + " >= '" + strValor + "' AND " + strfrmd_nombre + " <= '" + formDeta.getStrValorAdi2() + "' ";
                           }
                        } else {
                           if (strfrmd_tipo.equals("date")) {
                              if (!strValor.equals("") && !strValor.equals("null")) {
                                 strFiltro += " AND  " + strfrmd_nombre + " = '" + strValor + "' ";
                              }
                           } else {
                              if (!strValor.equals("") && !strValor.equals("null")) {
                                 strFiltro += " AND  " + strfrmd_nombre + " like '%" + strValor + "%' ";
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
      //Condicion especial
      String strfrm_gridCondicion = this.form.getFieldString("frm_gridCondicion");
      if (!strfrm_gridCondicion.equals("")) {
         strfrm_gridCondicion = strfrm_gridCondicion.replace("[no_user]", this.varSesiones.getIntNoUser() + "");
         strfrm_gridCondicion = strfrm_gridCondicion.replace("[no_cliente]", this.varSesiones.getIntClienteWork() + "");
         strfrm_gridCondicion = strfrm_gridCondicion.replace("[Empresa]", this.varSesiones.getIntClienteWork() + "");
         strfrm_gridCondicion = strfrm_gridCondicion.replace("[anio]", this.varSesiones.getIntAnioWork() + "");
         strfrm_gridCondicion = strfrm_gridCondicion.replace("[SUCURSAL]", this.varSesiones.getIntSucursalDefault() + "");
         strfrm_gridCondicion = strfrm_gridCondicion.replace("[LSTSUCURSAL]", this.varSesiones.getStrLstSucursales() + "");
         strfrm_gridCondicion = strfrm_gridCondicion.replace("[EmpresaDe                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           f]", this.varSesiones.getIntIdEmpresa() + "");
         strFiltro += strfrm_gridCondicion;
      }
      if (strFiltro.trim().length() > 0) {
         if (strFiltro.startsWith(" AND")) {
            strFiltro = " WHERE " + strFiltro.substring(4, strFiltro.length());
         } else {
            strFiltro = " WHERE " + strFiltro;
         }

      }
      strSql += strFiltro;
      /*************************FILTRO****************/
      /*************************PAGINACION***************/
      //if this is the first query - get total number of records in the query result
      String strSqlCount = "";
      double dblCount = 0;
      long inttotal_pages = 0;
      if (this.rows == 0) {
         this.rows = 1;
      }
      strSqlCount = "Select count(*) as cnt from (" + strSql + ") as tbl";
      ResultSet rs;
      try {
         rs = oConn.runQuery(strSqlCount, true);
         while (rs.next()) {
            dblCount = rs.getDouble("cnt");
         }
         rs.close();
      } catch (SQLException ex) {
         ex.fillInStackTrace();
         bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "", oConn);
      }
      if (dblCount > 0) {
         inttotal_pages = (long) this.round(dblCount / rows, 0);
      } else {
         inttotal_pages = 0;
      }
      /*if for some reasons the requested page is greater than the total
      set the requested page to total page*/
      if (this.page > inttotal_pages) {
         page = (int) inttotal_pages;
      }
      //Calculate the starting position of the rows
      int start = this.rows * this.page - this.rows;
      /*if for some reasons start position is negative set it to 0
      'typical case is that the user type 0 for the requested page*/
      if (start < 0) {
         start = 0;
      }
      //Anadimos el ordem
      String strOrden = "";
      if (!this.sidx.equals("") && !this.sord.equals("")) {
         strOrden += " ORDER BY " + this.sidx + " " + this.sord;
      }
      //add limits to query to get only rows necessary for output
      //strSql += " LIMIT " + start + "," + this.rows; Only apllyes MYSQL
      strSql = "SELECT * FROM (Select  ROW_NUMBER() OVER(" + strOrden + ") AS rn, " + strSql.substring(7, strSql.length());
      strSql += " ) AS sub WHERE rn >= " + start + " AND rn <= " + (start + this.rows) + " ";
      /*************************PAGINACION***************/
      /*************************ENCABEZADO***************/
      strXML = strXMLHEAD;
      strXML += "<rows>" + "";
      strXML += "<page>" + page + "</page>";
      strXML += "<total>" + inttotal_pages + "</total>";
      strXML += "<records>" + (int) dblCount + "</records>";
      /*************************ENCABEZADO***************/
      /*************************OBTENEMOS LISTA DE CAMPOS A MOSTRAR*************/
      try {
         rs = oConn.runQuery(strSql, true);
         int intContaRows = 0;
         while (rs.next()) {
            String strXfrm_Valorkey = rs.getString(this.NomKey);
            intContaRows++;
            strXML += "<row id='" + strXfrm_Valorkey + "'>";
            //Recorremos campo por campo
            it = this.lst.iterator();
            while (it.hasNext()) {
               FormulariosDeta formDeta = (FormulariosDeta) it.next();
               String strfrmd_nombre = formDeta.getFieldString("frmd_nombre");
               String strfrmd_dato = formDeta.getFieldString("frmd_dato");
               String strfrmd_tipo = formDeta.getFieldString("frmd_tipo");
               String strfrmd_consulta = formDeta.getFieldString("frmd_consulta");
               String strfrmd_tablaext = formDeta.getFieldString("frmd_tablaext");
               String strfrmd_tabla_envio = formDeta.getFieldString("frmd_tabla_envio");
               String strfrmd_tabla_mostrar = formDeta.getFieldString("frmd_tabla_mostrar");
               String strfrmd_tabla_pre = formDeta.getFieldString("frmd_tabla_pre");
               String strfrmd_tabla_post = formDeta.getFieldString("frmd_tabla_post");
               String strfrmd_alias_envio = formDeta.getFieldString("frmd_alias_envio");
               String strfrmd_alias_mostrar = formDeta.getFieldString("frmd_alias_mostrar");
               //Validamos nombre de los campos por enviar
               String strNomEnvio = new String(strfrmd_tabla_envio);
               String strNomMostrar = new String(strfrmd_tabla_mostrar);
               if (!strfrmd_alias_envio.trim().equals("")) {
                  strNomEnvio = new String(strfrmd_alias_envio);
               }
               if (!strfrmd_alias_mostrar.trim().equals("")) {
                  strNomMostrar = new String(strfrmd_alias_mostrar);
               }
               //Validamos si es un campo a mostrar en las consultas
               if (strfrmd_consulta.equals("1") && !strfrmd_dato.equals("none")) {
                  //Verificamos si hay que sacar la informacion de otra tabla
                  boolean bolOtraTabla = false;
                  String strValorOtra = "";
                  if (!strfrmd_tablaext.trim().equals("")
                          && !strfrmd_tabla_envio.trim().equals("")
                          && !strfrmd_tabla_mostrar.trim().equals("")) {
                     //Validamos si los datos externos los tenemos en la lista temporal
                     if (formDeta.lstData.containsKey(rs.getString(strfrmd_nombre))) {
                        strValorOtra = new String((String) formDeta.lstData.get(rs.getString(strfrmd_nombre)));
                        bolOtraTabla = true;
                     } else {
                        //Validamos si es factible hacer la busqueda
                        //evitamos buscar id's numericos igual a cero
                        //o cadenas de texto igual a vacias
                        boolean bolBusqueda = true;
                        if (strfrmd_dato.equals("integer")) {
                           if (rs.getString(strfrmd_nombre).equals("0")) {
                              bolBusqueda = false;
                           }
                        } else {
                           if (strfrmd_dato.equals("text")) {
                              if (rs.getString(strfrmd_nombre).equals("")) {
                                 bolBusqueda = false;
                              }
                           }
                        }
                        if (bolBusqueda) {
                           //Buscamos los datos externos
                           String strTBPost = new String(strfrmd_tabla_post);
                           strTBPost = strTBPost.replace("[no_user]", this.varSesiones.getIntNoUser() + "");
                           strTBPost = strTBPost.replace("[no_cliente]", this.varSesiones.getIntClienteWork() + "");
                           strTBPost = strTBPost.replace("[Empresa]", this.varSesiones.getIntClienteWork() + "");
                           strTBPost = strTBPost.replace("[anio]", this.varSesiones.getIntAnioWork() + "");
                           strTBPost = strTBPost.replace("[SUCURSAL]", this.varSesiones.getIntSucursalDefault() + "");
                           strTBPost = strTBPost.replace("[LSTSUCURSAL]", this.varSesiones.getStrLstSucursales() + "");
                           strTBPost = strTBPost.replace("[EmpresaDef]", this.varSesiones.getIntIdEmpresa() + "");
                           if (strTBPost.contains("where") || strTBPost.contains("WHERE") || strTBPost.contains("Where")) {
                              if (strTBPost.contains("order") || strTBPost.contains("ORDER") || strTBPost.contains("Order")) {
                                 int intPos = strTBPost.toLowerCase().indexOf("order");
                                 String strTBPost1 = strTBPost.substring(0, intPos);
                                 String strTBPost2 = strTBPost.substring(intPos, strTBPost.length());
                                 strTBPost = strTBPost1 + " AND " + strNomEnvio + " = '" + rs.getString(strfrmd_nombre) + "' " + strTBPost2;
                              } else {
                                 strTBPost += " AND " + strNomEnvio + " = '" + rs.getString(strfrmd_nombre) + "'";
                              }
                           } else {
                              strTBPost = " where  " + strNomEnvio + " = '" + rs.getString(strfrmd_nombre) + "'" + strTBPost;
                           }
                           String strSqlCombo = "select " + strfrmd_tabla_pre + " " + strfrmd_tabla_envio + ","
                                   + strfrmd_tabla_mostrar + " from " + strfrmd_tablaext + " " + strTBPost
                                   + "  ";
                           try {
                              ResultSet rs2 = oConn.runQuery(strSqlCombo, true);
                              while (rs2.next()) {
                                 bolOtraTabla = true;
                                 String strMostrar = rs2.getString(strNomMostrar);
                                 strValorOtra = new String(strMostrar);
                                 formDeta.lstData.put(rs.getString(strfrmd_nombre), strValorOtra);
                              }
                              rs2.close();
                           } catch (SQLException ex) {
                              ex.fillInStackTrace();
                              bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "", oConn);
                           }
                        }
                     }
                  }
                  if (strfrmd_tipo.equals("radio")) {
                     if (formDeta.lstData.size() > 0) {
                        String strxValor = this.utilXML.Sustituye(rs.getString(strfrmd_nombre));
                        strXML += "<cell>" + strxValor + "</cell>";
                     } else {
                        if (rs.getString(strfrmd_nombre).equals("1")) {
                           strXML += "<cell>SI</cell>";
                        } else {
                           strXML += "<cell>NO</cell>";
                        }
                     }
                  } else {
                     //Mostramos info de otra tabla
                     if (bolOtraTabla) {
                        String strxValor = this.utilXML.Sustituye(strValorOtra);
                        strXML += "<cell>" + strxValor + "</cell>";
                     } else {
                        String strxValor = this.utilXML.Sustituye(rs.getString(strfrmd_nombre));
                        if (strfrmd_tipo.equals("date") || strfrmd_tipo.equals("date_per")) {
                           if (strxValor.length() >= 8) {
                              strXML += "<cell>" + fecha.Formatea(strxValor, "/") + "</cell>";
                           } else {
                              strXML += "<cell>" + strxValor + "</cell>";
                           }
                        } else {
                           if (strfrmd_dato.equals("double")) {
                              try {
                                 double dblValor = Double.valueOf(strxValor);
                                 //strXML += "<cell>" + NumberString.FormatearDecimal(dblValor, this.varSesiones.getNumDecimal()) + "</cell>";
                                 strXML += "<cell>" + strxValor + "</cell>";
                              } catch (NumberFormatException ex) {
                                 strXML += "<cell>" + strxValor + "</cell>";
                              }
                           } else {
                              if (strfrmd_tipo.equals("epoch")) {
                                 String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(rs.getLong(strfrmd_nombre) * 1000));
                                 strXML += "<cell>" + date + "</cell>";
                              } else {
                                 if (strfrmd_tipo.equals("datetime")) {
                                    String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getDate(strfrmd_nombre));
                                    strXML += "<cell>" + date + "</cell>";
                                 } else {
                                    strXML += "<cell>" + strxValor + "</cell>";
                                 }

                              }
                           }
                        }
                     }
                  }

               }
            }
            strXML += " </row>";
         }
         rs.close();
      } catch (SQLException ex) {
         System.out.println(" " + ex.getLocalizedMessage());
         bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "", oConn);
      }
      strXML += "</rows>";
      /*************************OBTENEMOS LISTA DE CAMPOS A MOSTRAR*************/
      return strXML;
   }
}
