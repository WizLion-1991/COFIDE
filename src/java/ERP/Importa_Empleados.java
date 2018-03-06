/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import java.io.*;
import Tablas.rhh_empleados;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author siwebmx5
 */
public class Importa_Empleados {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(Importa_Empleados.class.getName());
    private String strResultLast;
    private boolean bolLimpiarInfo = false;

    public String getStrResultLast() {
        return strResultLast;
    }

    public void setStrResultLast(String strResultLast) {
        this.strResultLast = strResultLast;
    }

    public boolean isBolLimpiarInfo() {
        return bolLimpiarInfo;
    }

    public void setBolLimpiarInfo(boolean bolLimpiarInfo) {
        this.bolLimpiarInfo = bolLimpiarInfo;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    public Importa_Empleados() {
        this.strResultLast = "";
        this.bolLimpiarInfo = true;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    public void CargaEmpleados(String strPathExcel, Conexion oConn, VariableSession varSesiones) {
        this.strResultLast = "OK";
        if (ValidaExcel(strPathExcel, oConn)) {

            //Si tiene activa la bandera de limpiar que borre la info
            if (this.bolLimpiarInfo) {

                String strSql = "delete from rhh_empleados where EMP_ID = " + varSesiones.getIntIdEmpresa();
                oConn.runQueryLMD(strSql);

                //Regeneramos el folio
                int intFolio = 1;
                strSql = "select EMP_NUM as num_folio from rhh_empleados ";
                try {
                    ResultSet rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        intFolio = rs.getInt("num_folio");
                    }
                    rs.close();
                    if (intFolio == 0) {
                        intFolio = 1;
                    }
                } catch (SQLException ex) {
                    log.error("Error (6):" + ex.getMessage());
                }
                //Alteramos el autoincremental
                strSql = "alter table rhh_empleados AUTO_INCREMENT = " + (intFolio++);
                oConn.runQueryLMD(strSql);

            }
            try {
                InputStream myxls = null;
                myxls = new FileInputStream(strPathExcel);

                HSSFWorkbook archivoExcel = new HSSFWorkbook(myxls);
                rhh_empleados RRHE_Empleado;
                SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");
                Fechas fecha = new Fechas();
                //Cell algo;
                Cell strFechaIngreso;
                Cell strFechaNombre;
                Cell strRFC;
                Cell strCURP;
                Cell strNoSeguro;
                Cell strCalle;
                Cell strNumero;
                Cell strNumeroInt;
                Cell strLocalidad;
                Cell strColonia;
                Cell strMunicipio;
                Cell strEstado;
                Cell strCP;
                Cell strRegimenCont;
                Cell strRiesgoPuesto;
                Cell strPerfilPuesto;
                Cell strDepartamento;
                Cell strSalarioDiario;
                Cell strSalarioIntegrado;
                Cell strClabeEmpresa;
                Cell strBanco;
                Cell strFechaInicioLaboral;
                Cell strTipoContrato;
                Cell strTipoJornada;
                Cell strPeriodicidadPAgo;
                Cell strMetodoPago;
                Cell strNumCta;
                Cell strCondiciones;
                Cell strFormaPago;
                Cell strEmail1;
                Cell strEmail2;
                Cell strNota1;
                Cell strNota2;
                Cell strNota3;
                Cell strRegPatronal;
                Cell strPais;
                Cell strClave;
                Cell strTipoSalario;
                Cell strHorario;
                Cell strActivo;

                Iterator<Row> it = archivoExcel.getSheetAt(0).rowIterator();
                int intContador = 0;
                it.next();
                while (it.hasNext()) {
                    Row row = it.next();
                    intContador++;
                    strFechaIngreso = row.getCell(0);
                    strFechaNombre = row.getCell(1);
                    strRFC = row.getCell(2);
                    strCURP = row.getCell(3);
                    strNoSeguro = row.getCell(4);
                    strCalle = row.getCell(5);
                    strNumero = row.getCell(6);
                    strNumeroInt = row.getCell(7);
                    strLocalidad = row.getCell(8);
                    strColonia = row.getCell(9);
                    strMunicipio = row.getCell(10);
                    strEstado = row.getCell(11);
                    strCP = row.getCell(12);
                    strRegimenCont = row.getCell(13);
                    strRiesgoPuesto = row.getCell(14);
                    //strPerfilPuesto = row.getCell(15);
                    strDepartamento = row.getCell(15);
                    strSalarioDiario = row.getCell(16);
                    strSalarioIntegrado = row.getCell(17);
                    strClabeEmpresa = row.getCell(18);
                    strBanco = row.getCell(19);
                    strFechaInicioLaboral = row.getCell(20);
                    strTipoContrato = row.getCell(21);
                    strTipoJornada = row.getCell(22);
                    strPeriodicidadPAgo = row.getCell(23);
                    strMetodoPago = row.getCell(24);
                    strNumCta = row.getCell(25);
                    strCondiciones = row.getCell(26);
                    strFormaPago = row.getCell(27);
                    strEmail1 = row.getCell(28);
                    strEmail2 = row.getCell(29);

                    strNota1 = row.getCell(30);

                    strNota2 = row.getCell(31);
                    strNota3 = row.getCell(32);
                    strPais = row.getCell(33);
                    strTipoSalario = row.getCell(34);
                    strHorario = row.getCell(35);
                    strRegPatronal = row.getCell(36);
                    strClave = row.getCell(37);
                    strActivo = row.getCell(38);
                    BigDecimal big;
                    //Validamos que el nombre este lleno sino lo saltamos
                    if (strFechaNombre != null) {
                        log.debug("Nombre empleado:" + strFechaNombre.getStringCellValue());
                        int idEmp = 0;
                        boolean bolAgrega = true;
                        RRHE_Empleado = new rhh_empleados();
                        //consuta
                        idEmp = existsCURP(strCURP.getStringCellValue(), oConn, varSesiones);
                        if (idEmp != 0) {
                            bolAgrega = false;
                            RRHE_Empleado.ObtenDatos(idEmp, oConn);
                        }
                        String str = "";
                        if (strNoSeguro != null) {
                            if (strNoSeguro.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                big = new BigDecimal(strNoSeguro.getNumericCellValue());
                                str = big.toString();
                            }
                            if (strNoSeguro.getCellType() == Cell.CELL_TYPE_STRING) {
                                str = strNoSeguro.getStringCellValue();
                            }
                        }
                        if (strFechaIngreso != null) {
                            if (strFechaIngreso.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                RRHE_Empleado.setFieldString("EMP_INGRESO", SDF.format(strFechaIngreso.getDateCellValue()));
                            }
                            if (strFechaIngreso.getCellType() == Cell.CELL_TYPE_STRING) {
                                RRHE_Empleado.setFieldString("EMP_INGRESO", fecha.FormateaBD(strFechaIngreso.getStringCellValue(), "/"));
                            }
                        }

                        RRHE_Empleado.setFieldString("EMP_NOMBRE", strFechaNombre.getStringCellValue());
                        RRHE_Empleado.setFieldString("EMP_RFC", strRFC.getStringCellValue());
                        RRHE_Empleado.setFieldString("EMP_CURP", strCURP.getStringCellValue());
                        RRHE_Empleado.setFieldString("EMP_NO_SEG", str);
                        if (strCalle != null) {
                            if (strCalle.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                BigDecimal bigC = new BigDecimal(strCalle.getNumericCellValue());
                                String strCalles = bigC.toString();
                                RRHE_Empleado.setFieldString("EMP_CALLE", strCalles);
                            } else {
                                RRHE_Empleado.setFieldString("EMP_CALLE", strCalle.getStringCellValue());
                            }
                        }
                        RRHE_Empleado.setFieldString("EMP_METODO_PAGO", strMetodoPago.getStringCellValue());

                        String strCta = "";
                        if (strNumCta != null) {
                            if (strNumCta.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                big = new BigDecimal(strNumCta.getNumericCellValue());
                                strCta = big.toString();
                            }
                            if (strNumCta.getCellType() == Cell.CELL_TYPE_STRING) {
                                strCta = strNumCta.getStringCellValue();
                            }
                        }
                        log.debug("EMP_NUM_CTA:" + strCta);
                        RRHE_Empleado.setFieldString("EMP_NUM_CTA", strCta);
                        if (strCondiciones != null) {
                            RRHE_Empleado.setFieldString("EMP_CONDICIONES", strCondiciones.getStringCellValue());
                        }
                        RRHE_Empleado.setFieldString("EMP_FORMA_DE_PAGO", strFormaPago.getStringCellValue());

                        if (strEmail1 != null) {
                            RRHE_Empleado.setFieldString("EMP_EMAIL1", strEmail1.getStringCellValue());
                        }
                        if (strEmail2 != null) {
                            RRHE_Empleado.setFieldString("EMP_EMAIL2", strEmail2.getStringCellValue());
                        }
                        if (strNota1 != null && strNota1.getCellType() == Cell.CELL_TYPE_STRING) {
                            RRHE_Empleado.setFieldString("EMP_NOTA1", strNota1.getStringCellValue());
                        }
                        if (strNota2 != null && strNota1.getCellType() == Cell.CELL_TYPE_STRING) {
                            RRHE_Empleado.setFieldString("EMP_NOTA2", strNota2.getStringCellValue());
                        }
                        if (strNota3 != null && strNota1.getCellType() == Cell.CELL_TYPE_STRING) {
                            RRHE_Empleado.setFieldString("EMP_NOTA3", strNota3.getStringCellValue());
                        }
                        if (strRegPatronal != null && strRegPatronal.getCellType() == Cell.CELL_TYPE_STRING) {
                            RRHE_Empleado.setFieldString("EMP_REGISTRO_PATRONAL", strRegPatronal.getStringCellValue());
                        }
                        if (strPais != null && strPais.getCellType() == Cell.CELL_TYPE_STRING) {
                            RRHE_Empleado.setFieldString("EMP_PAIS", strPais.getStringCellValue());
                        }
                        if (strTipoSalario != null && strTipoSalario.getCellType() == Cell.CELL_TYPE_STRING) {
                            RRHE_Empleado.setFieldString("EMP_TIPO_SALARIO", strPais.getStringCellValue());
                        }
                        if (strClave != null && strClave.getCellType() == Cell.CELL_TYPE_STRING) {
                            RRHE_Empleado.setFieldString("EMP_CLAVE", strClave.getStringCellValue());
                        } else {
                            if (strClave != null && strClave.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                RRHE_Empleado.setFieldString("EMP_CLAVE", "" + (int) strClave.getNumericCellValue());
                            }
                        }

                        double dblActivo = strActivo.getNumericCellValue();
                        int intActivo = (int) dblActivo;
                        RRHE_Empleado.setFieldString("EMP_ACTIVO", "" + intActivo);

                        str = "";
                        if (strClabeEmpresa != null) {
                            if (strClabeEmpresa.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                big = new BigDecimal(strClabeEmpresa.getNumericCellValue());
                                str = big.toString();
                            }
                            if (strClabeEmpresa.getCellType() == Cell.CELL_TYPE_STRING) {
                                str = strClabeEmpresa.getStringCellValue();
                            }
                        }

                        RRHE_Empleado.setFieldString("EMP_CLABE", str);
                        if (strLocalidad != null) {
                            RRHE_Empleado.setFieldString("EMP_LOCALIDAD", strLocalidad.getStringCellValue());
                        }
                        if (strColonia != null) {
                            RRHE_Empleado.setFieldString("EMP_COLONIA", strColonia.getStringCellValue());
                        }
                        if (strMunicipio != null) {
                            RRHE_Empleado.setFieldString("EMP_MUNICIPIO", strMunicipio.getStringCellValue());
                        }
                        if (strEstado != null) {
                            RRHE_Empleado.setFieldString("EMP_ESTADO", strEstado.getStringCellValue());
                        }
                        if (strCP != null) {
                            //Validar si el excel lo puso como numerico o texto
                            if (strCP.getCellType() == Cell.CELL_TYPE_STRING) {
                                RRHE_Empleado.setFieldString("EMP_CP", strCP.getStringCellValue());
                            } else {
                                Integer intCP = (int) strCP.getNumericCellValue();
                                if (intCP != 0) {
                                    RRHE_Empleado.setFieldString("EMP_CP", intCP.toString());
                                }
                            }
                        }
                        RRHE_Empleado.setFieldDouble("EMP_SALARIO_DIARIO", strSalarioDiario.getNumericCellValue());
                        if (strSalarioIntegrado != null && strSalarioIntegrado.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            RRHE_Empleado.setFieldDouble("EMP_SALARIO_INTEGRADO", strSalarioIntegrado.getNumericCellValue());
                        }

                        RRHE_Empleado.setFieldInt("EMP_ID", varSesiones.getIntIdEmpresa());

                        str = "";
                        if (strNumero != null) {
                            if (strNumero.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                big = new BigDecimal(strNumero.getNumericCellValue());
                                str = big.toString();
                            }
                            if (strNumero.getCellType() == Cell.CELL_TYPE_STRING) {
                                str = strNumero.getStringCellValue();
                            }
                        }
                        RRHE_Empleado.setFieldString("EMP_NUMERO", str);

                        str = "";
                        if (strNumeroInt != null) {
                            if (strNumeroInt.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                big = new BigDecimal(strNumeroInt.getNumericCellValue());
                                str = big.toString();
                            }
                            if (strNumeroInt.getCellType() == Cell.CELL_TYPE_STRING) {
                                str = strNumeroInt.getStringCellValue();
                            }
                        }

                        RRHE_Empleado.setFieldString("EMP_NUMINT", str);

                        if (strFechaInicioLaboral.getCellType() != Cell.CELL_TYPE_STRING) {
                            RRHE_Empleado.setFieldString("EMP_FECHA_INICIO_REL_LABORAL", SDF.format(strFechaInicioLaboral.getDateCellValue()));
                        }

                        RRHE_Empleado.setFieldString("EMP_TIPO_CONTRATO", strTipoContrato.getStringCellValue());
                        RRHE_Empleado.setFieldString("EMP_TIPO_JORNADA", strTipoJornada.getStringCellValue());
                        RRHE_Empleado.setFieldString("EMP_PERIODICIDAD_PAGO", strPeriodicidadPAgo.getStringCellValue());
                        RRHE_Empleado.setFieldInt("SC_ID", varSesiones.getIntSucursalDefault());
                        if (strBanco != null) {
                            RRHE_Empleado.setFieldString("EMP_BANCO", strBanco.getStringCellValue());
                        }
                        try {
                            String strSQL = null;
                            if (strRegimenCont.getCellType() == Cell.CELL_TYPE_STRING) {
                                strSQL = "Select RC_ID From rhh_regimen_contratacion Where RC_DESCRIPCION = '" + strRegimenCont.getStringCellValue() + "' ";
                            } else {
                                strSQL = "Select RC_ID From rhh_regimen_contratacion Where RC_ID = " + (int) strRegimenCont.getNumericCellValue() + " ";
                            }

                            ResultSet rs = oConn.runQuery(strSQL);
                            boolean boolHayInformacion = false;
                            while (rs.next()) {
                                RRHE_Empleado.setFieldInt("RC_ID", rs.getInt("RC_ID"));
                                boolHayInformacion = true;
                            }
                            if (!boolHayInformacion) {
                                RRHE_Empleado.setFieldInt("RC_ID", 0);
                            }
                            rs.close();

                            boolHayInformacion = false;

                            if (strRiesgoPuesto.getCellType() == Cell.CELL_TYPE_STRING) {
                                strSQL = "Select RP_ID From rhh_riesgo_puesto Where RP_DESCRIPCION = '" + strRiesgoPuesto.getStringCellValue() + "'";
                            } else {
                                strSQL = "Select RP_ID From rhh_riesgo_puesto Where RP_ID = '" + (int) strRiesgoPuesto.getNumericCellValue() + "'";
                            }
                            rs = oConn.runQuery(strSQL);
                            while (rs.next()) {
                                RRHE_Empleado.setFieldInt("RP_ID", rs.getInt("RP_ID"));
                                boolHayInformacion = true;
                            }
                            if (!boolHayInformacion) {
                                RRHE_Empleado.setFieldInt("RP_ID", 0);
                            }
                            rs.close();

                            boolHayInformacion = false;

                            String strDato = "";
//                     if (strPerfilPuesto != null) {
//                        strDato = strPerfilPuesto.getStringCellValue();
//                     }
                            strSQL = "Select RHP_ID From rhh_perfil_puesto Where RHP_NOMBRE = '" + strDato.toUpperCase() + "'";
                            rs = oConn.runQuery(strSQL);
                            while (rs.next()) {
                                RRHE_Empleado.setFieldInt("RHP_ID", rs.getInt("RHP_ID"));
                                boolHayInformacion = true;
                            }
                            if (!boolHayInformacion) {
                                RRHE_Empleado.setFieldInt("RHP_ID", 0);
                            }
                            rs.close();

                            strDato = "";
                            if (strDepartamento != null) {
                                if (strDepartamento.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                    strDato = "" + (int) strDepartamento.getNumericCellValue();
                                } else {
                                    strDato = strDepartamento.getStringCellValue();
                                }

                            }

                            boolHayInformacion = false;
                            strSQL = "Select DP_ID From rhh_departamento Where DP_DESCRIPCION = '" + strDato.toUpperCase() + "'";
                            rs = oConn.runQuery(strSQL);
                            while (rs.next()) {
                                RRHE_Empleado.setFieldInt("DP_ID", rs.getInt("DP_ID"));
                                boolHayInformacion = true;
                            }
                            if (!boolHayInformacion) {
                                RRHE_Empleado.setFieldInt("DP_ID", 0);
                            }
                            rs.close();

                            boolHayInformacion = false;
                            String strHorarioName = "";
                            if (strHorario != null) {
                                try {
                                    if (strHorario.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                        strHorarioName = "" + (int) strHorario.getNumericCellValue();
                                    } else {
                                        if (strHorario.getCellType() == Cell.CELL_TYPE_STRING) {
                                            strHorarioName = strHorario.getStringCellValue();
                                        } else {
                                            if (strHorario.getCellType() == Cell.CELL_TYPE_ERROR) {
                                                strHorarioName = "";
                                                this.strResultLast = "Error:Hay una formula erronea en la fila " + intContador + " CURP:" + strCURP;
                                            }
                                        }
                                    }
                                } catch (Exception ex) {
                                    log.error("Error(7):" + ex.getMessage());
                                    this.strResultLast = "Error:" + ex.getMessage() + ". Número de línea: " + intContador;
                                }
                            }
                            strSQL = "Select HR_ID From rhh_horarios Where HR_DESCRIPCION = '" + strHorarioName + "'";
                            rs = oConn.runQuery(strSQL);
                            while (rs.next()) {
                                RRHE_Empleado.setFieldInt("EMP_HORARIO", rs.getInt("HR_ID"));
                                boolHayInformacion = true;
                            }
                            if (!boolHayInformacion) {
                                RRHE_Empleado.setFieldInt("EMP_HORARIO", 0);
                            }
                            rs.close();

//                    strDato="";
//                    if(strBanco != null)
//                        strDato = strBanco.getStringCellValue();
//                    
//                    boolHayInformacion=false;
//                    strSQL = "Select RBK_CVE From rhh_bancos Where RBK_NOMBRE_CORTO = '"+strDato+"'";
//                    rs = oConn.runQuery(strSQL);
//                    while (rs.next()) {
//                        RRHE_Empleado.setFieldString("EMP_BANCO", rs.getString("RBK_CVE"));
//                        boolHayInformacion = true;
//                    }
//                    if(!boolHayInformacion)
//                    {
//                        RRHE_Empleado.setFieldString("EMP_BANCO", "");
//                    }
//                    rs.close();
                        } catch (SQLException ex) {
                            log.error(ex.getMessage() + " " + ex.getSQLState());
                            this.strResultLast = "Error:" + ex.getMessage() + " " + ex.getSQLState() + ". Número de línea: " + intContador + " CURP:" + strCURP;
                        }
                        if (this.strResultLast.equals("OK")) {
                            String strRes = "";
                            if (bolAgrega) {
                                strRes = RRHE_Empleado.Agrega(oConn);
                            } else {
                                strRes = RRHE_Empleado.Modifica(oConn);
                            }

                            if (!strRes.equals("OK")) {
                                this.strResultLast = strRes;
                                break;
                            }
                        } else {
                            break;
                        }

                    }

                }
                myxls.close();
            } catch (IOException ex) {
                log.error("Error(1):" + ex.getMessage());
            }
        } else {
            log.debug("No hice nada");
        }
    }

    public int existsCURP(String Emp_Curp, Conexion oConn, VariableSession varSesiones) {
        int intEMP_NUM = 0;
        String strQuery = "select EMP_NUM,EMP_ACTIVO from rhh_empleados where EMP_ID = '" + varSesiones.getIntIdEmpresa() + "'"
                + " and SC_ID = '" + varSesiones.getIntSucursalDefault() + "'"
                + " and EMP_CURP = '" + Emp_Curp + "';";
        ResultSet rs;
        try {
            rs = oConn.runQuery(strQuery, true);
            while (rs.next()) {
                if (rs.getInt("EMP_ACTIVO") == 1) {
                    intEMP_NUM = rs.getInt("EMP_NUM");
                }

            }
            rs.close();
        } catch (SQLException ex) {
            log.error("Error(2):" + ex.getMessage());
        }

        return intEMP_NUM;
    }

    public boolean ValidaExcel(String strPathExcel, Conexion oConn) {
        boolean boolValido = true;
        try {
            InputStream myxls = null;
            myxls = new FileInputStream(strPathExcel);
            HSSFWorkbook archivoExcel = new HSSFWorkbook(myxls);

            Cell strNombre;
            Cell strRFC;
            Cell strCURP;
            Cell strCalle;
            Cell strNumero;
            Cell strNumeroInt;
            Cell strColonia;
            Cell strMunicipio;
            Cell strEstado;
            Cell strCP;

            boolean boolNombre;
            boolean boolRFC;
            boolean boolCURP;
            boolean boolCalle;
            boolean boolNumero;
            boolean boolNumeroInt;
            boolean boolColonia;
            boolean boolMunicipio;
            boolean boolEstado;
            boolean boolCP;
            boolean boolMetodoPago;
            boolean boolNumCta;
            boolean boolFormaPago;
            boolean boolBanco;
            boolean boolTipoContrato;
            boolean boolTipoJornada;
            boolean boolPeriodicidadPAgo;

            Iterator<Row> it = archivoExcel.getSheetAt(0).rowIterator();
            it.next();
            while (it.hasNext()) {
                Row row = it.next();

                strNombre = row.getCell(1);
                strRFC = row.getCell(2);
                strCURP = row.getCell(3);
                strCalle = row.getCell(5);
                strNumero = row.getCell(6);
                strNumeroInt = row.getCell(7);
                strColonia = row.getCell(9);
                strMunicipio = row.getCell(10);
                strEstado = row.getCell(11);
                strCP = row.getCell(12);
                Cell strMetodoPago = row.getCell(24);
                Cell strNumCta = row.getCell(25);
                Cell strFormaPago = row.getCell(27);
                Cell strBanco = row.getCell(19);
                Cell strTipoContrato = row.getCell(21);
                Cell strTipoJornada = row.getCell(22);
                Cell strPeriodicidadPAgo = row.getCell(23);

                boolNombre = strNombre == null;
                boolRFC = strRFC == null;
                boolCURP = strCURP == null;
                boolCalle = strCalle == null;
                boolNumero = strNumero == null;
                boolColonia = strColonia == null;
                boolMunicipio = strMunicipio == null;
                boolEstado = strEstado == null;
                boolCP = strCP == null;
                boolMetodoPago = strMetodoPago == null;
                boolNumCta = strNumCta == null;
                boolFormaPago = strFormaPago == null;
                boolTipoContrato = strTipoContrato == null;
                boolTipoJornada = strTipoJornada == null;
                boolPeriodicidadPAgo = strPeriodicidadPAgo == null;

                boolean valNom = true;
                boolean valRFC = true;
                boolean valCURP = true;
                boolean vaLCalle = true;
                boolean vaLNumero = true;
                boolean valColonia = true;
                boolean valMunicipio = true;
                boolean valEstado = true;
                boolean valBanco = true;
                boolean valTipoContrato = true;
                boolean valTipoJornada = true;
                boolean valPeriodicidadPAgo = true;
                boolean valMetodoPago = true;
                //Validamos que el nombre este lleno sino lo saltamos
                if (strNombre != null) {
                    if (!boolCURP) {
                        log.debug("Validando CURP:" + strCURP.getStringCellValue());
                        Pattern pat = Pattern.compile("[A-Z][A,E,I,O,U,X][A-Z]{2}[0-9]{2}[0-1][0-9][0-3][0-9][M,H][A-Z]{2}[B,C,D,F,G,H,J,K,L,M,N,��,P,Q,R,S,T,V,W,X,Y,Z]{3}[0-9,A-Z][0-9]");
                        Matcher mat = pat.matcher(strCURP.getStringCellValue());
                        if (mat.matches()) {
//                     boolCURP = false;
                        } else {
                            log.debug("No es valido");
                            boolValido = false;
                            this.strResultLast = "ERROR: La CURP " + strCURP.getStringCellValue() + " no es valida en el renglon " + (row.getRowNum() + 1);
                            break;
                        }
                    }
                    //vamos a validar si cumple cada registro del empleado con el tama�o de la bd
                    if (strNombre != null) {
                        valNom = (strNombre.getStringCellValue().length()) <= 90;
                    }
                    if (strRFC != null) {
                        valRFC = (strRFC.getStringCellValue().length()) <= 13 && (strRFC.getStringCellValue().length()) >= 11;
                    }
                    if (strCURP != null) {
                        valCURP = (strCURP.getStringCellValue().length()) == 18;
                    }
                    if (strCalle != null) {
                        vaLCalle = (strCalle.getStringCellValue().length()) <= 80;
                    }
                    if (strNumero != null) {
                        String strNumeroValue = "";
                        if (strNumero.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            BigDecimal big = new BigDecimal(strNumero.getNumericCellValue());
                            strNumeroValue = big.toString();
                        }
                        if (strNumero.getCellType() == Cell.CELL_TYPE_STRING) {
                            strNumeroValue = strNumero.getStringCellValue();
                        }
                        vaLNumero = (strNumeroValue.length()) <= 25;
                    }
                    if (strColonia != null) {
                        valColonia = (strColonia.getStringCellValue().length()) <= 80;
                    }
                    if (strMunicipio != null) {
                        valMunicipio = (strMunicipio.getStringCellValue().length()) <= 45;
                    }
                    if (strEstado != null) {
                        valEstado = (strEstado.getStringCellValue().length()) <= 30;
                    }
                    if (strBanco != null) {
                        valBanco = (strBanco.getStringCellValue().length()) <= 3;
                    }
                    if (strTipoContrato != null) {
                        valTipoContrato = (strTipoContrato.getStringCellValue().length()) <= 20;
                    }
                    if (strTipoJornada != null) {
                        valTipoJornada = (strTipoJornada.getStringCellValue().length()) <= 20;
                    }
                    if (strPeriodicidadPAgo != null) {
                        valPeriodicidadPAgo = (strPeriodicidadPAgo.getStringCellValue().length()) <= 50;
                    }
                    if (strMetodoPago != null) {
                        valMetodoPago = (strMetodoPago.getStringCellValue().length()) <= 50;
                    }

                    if (boolNombre || boolRFC || boolMetodoPago || boolFormaPago || boolTipoContrato || boolTipoJornada || boolPeriodicidadPAgo) {
                        boolValido = false;
                        this.strResultLast = "ERROR: Se requiere el campo   ";
                        if (boolNombre) {
                            this.strResultLast += "nombre,";
                        }
                        if (boolRFC) {
                            this.strResultLast += "RFC,";
                        }
                        if (boolCURP) {
                            this.strResultLast += "CURP,";
                        }/*
                         if (boolCalle) {
                         this.strResultLast += "calle,";
                         }
                         if (boolNumero) {
                         this.strResultLast += "numero,";
                         }

                         if (boolColonia) {
                         this.strResultLast += "colonia,";
                         }
                         if (boolMunicipio) {
                         this.strResultLast += "municipio,";
                         }
                         if (boolEstado) {
                         this.strResultLast += "estado,";
                         }
                         if (boolCP) {
                         this.strResultLast += "cp";
                         }*/

                        if (boolMetodoPago) {
                            this.strResultLast += "metodo de pago,";
                        }

                        if (boolFormaPago) {
                            this.strResultLast += "forma de pago,";
                        }

                        if (boolTipoContrato) {
                            this.strResultLast += "tipo de contrato,";
                        }
                        if (boolTipoJornada) {
                            this.strResultLast += "tipo de jornada,";
                        }
                        if (boolPeriodicidadPAgo) {
                            this.strResultLast += "periodicidad de pago,";
                        }

                        //...Hay que a��adir los otros campos
                        break;
                    }

                    if (!valNom || !valRFC || !valCURP || !vaLCalle || !vaLNumero || !valColonia || !valMunicipio || !valEstado || !valBanco || !valTipoContrato || !valTipoJornada || !valPeriodicidadPAgo) {

                        boolValido = false;
                        this.strResultLast = "ERROR: El tama�o del campo es incorrecto ";
                        if (!valNom) {
                            this.strResultLast += "nombre,";
                        }
                        if (!valRFC) {
                            this.strResultLast += "RFC,";
                        }
                        if (!valCURP) {
                            this.strResultLast += "CURP,";
                        }
                        if (!vaLCalle) {
                            this.strResultLast += "calle,";
                        }
                        if (!vaLNumero) {
                            this.strResultLast += "numero,";
                        }
                        if (!valColonia) {
                            this.strResultLast += "colonia,";
                        }
                        if (!valMunicipio) {
                            this.strResultLast += "municipio,";
                        }
                        if (!valEstado) {
                            this.strResultLast += "estado,";
                        }
                        if (!valBanco) {
                            this.strResultLast += "banco,";
                        }

                        if (!valTipoContrato) {
                            this.strResultLast += "tipo de contrato,";
                        }
                        if (!valTipoJornada) {
                            this.strResultLast += "tipo de jornada,";
                        }
                        if (!valPeriodicidadPAgo) {
                            this.strResultLast += "periodicidad de pago,";
                        }

                        break;
                    }

                    //Validamos el metodo de pago
                    if (strMetodoPago != null && valMetodoPago) {
                        if (strMetodoPago.getStringCellValue().isEmpty()) {
                            this.strResultLast = "ERROR: Se requiere del metodo de pago en el renglon " + (row.getRowNum() + 1);
                        }
                        if (!strMetodoPago.getStringCellValue().isEmpty()) {
                            if (!strMetodoPago.getStringCellValue().equals("EFECTIVO")
                                    && !strMetodoPago.getStringCellValue().equals("NO IDENTIFICADO")) {
                                String strNumeroCta = "";

                                if (strNumCta != null) {
                                    if (strNumCta.getCellType() == Cell.CELL_TYPE_STRING) {
                                        strNumeroCta = strNumCta.getStringCellValue();
                                    } else {
                                        BigDecimal big = new BigDecimal(strNumCta.getNumericCellValue());
                                        strNumeroCta = big.toString();
                                    }
                                    if (strNumeroCta.isEmpty()) {
                                        this.strResultLast = "ERROR: Se requiere del numero de cuenta de 4 digitos en el renglon " + (row.getRowNum() + 1);
                                    } else {
                                        if (strNumeroCta.length() < 4) {
                                            this.strResultLast = "ERROR: Se requiere del numero de cuenta de 4 digitos en el renglon " + (row.getRowNum() + 1);
                                        }
                                    }
                                }
                            }
                        }

                    } else {
                        this.strResultLast = "ERROR: Se requiere del metodo de pago en el renglon " + (row.getRowNum() + 1);
                    }
                    //Forma de pago
                    if (strFormaPago != null) {
                        if (strFormaPago.getStringCellValue().isEmpty()) {
                            this.strResultLast = "ERROR: Se requiere de la forma de pago en el renglon " + (row.getRowNum() + 1);
                        }

                    } else {
                        this.strResultLast = "ERROR: Se requiere de la forma de pago en el renglon " + (row.getRowNum() + 1);
                    }
                }
                //Ciclo de rows

            }

        } catch (IOException ex) {
            log.error("Error(3):" + ex.getMessage());
            this.strResultLast = "ERROR: " + ex.getMessage();
            boolValido = false;
        } catch (Exception ex) {
            log.error("Error(4):" + ex.getMessage());
            this.strResultLast = "ERROR: " + ex.getMessage();
            boolValido = false;
        }

        if (this.bolLimpiarInfo) {
            //Validamos si los empleados tienen movimientos de nominas
            int intCuantosExisten = 0;
            String strSql = "SELECT count( rhh_nominas.NOM_ID) as cuantos\n"
                    + " FROM rhh_nominas INNER JOIN rhh_empleados "
                    + " ON rhh_nominas.EMP_NUM = rhh_empleados.EMP_NUM "
                    + " where rhh_nominas.NOM_ANULADA = 0";
            try {
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    intCuantosExisten = rs.getInt("cuantos");
                }
                rs.close();

            } catch (SQLException ex) {
                log.error("Error(5):" + ex.getMessage());
            }
            //Solo aplica si no hay empleados con nominas aplicadas
            if (intCuantosExisten > 0) {
                boolValido = false;
                this.strResultLast = "ERROR: No se pueden borrar los empleados porque ya existen nominas...... ";
            }
        }

        return boolValido;
    }
// </editor-fold>
}
