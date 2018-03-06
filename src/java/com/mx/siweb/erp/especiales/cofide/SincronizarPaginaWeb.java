/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide;

import com.mx.siweb.erp.especiales.cofide.entidades.paginaweb.CatCrmMat;
import com.mx.siweb.erp.especiales.cofide.entidades.paginaweb.CatCursos;
import com.mx.siweb.erp.especiales.cofide.entidades.paginaweb.CatDiplomados;
import com.mx.siweb.erp.especiales.cofide.entidades.paginaweb.CatEventos;
import com.mx.siweb.erp.especiales.cofide.entidades.paginaweb.CatModulos;
import com.mx.siweb.erp.especiales.cofide.entidades.paginaweb.CatSeminarios;
import com.mx.siweb.erp.especiales.cofide.entidades.CofideCursos;
import com.mx.siweb.erp.especiales.cofide.entidades.CofideHrEvento;
import com.mx.siweb.erp.especiales.cofide.entidades.CofideListaNegra;
import com.mx.siweb.erp.especiales.cofide.entidades.paginaweb.CatExpositor;
import com.mx.siweb.erp.especiales.cofide.entidades.paginaweb.CatSede;
import com.mx.siweb.erp.especiales.cofide.entidades.paginaweb.DetCurso;
import com.mx.siweb.erp.especiales.cofide.entidades.paginaweb.DetDiplomado;
import com.mx.siweb.erp.especiales.cofide.entidades.paginaweb.DetSeminario;
import com.mx.siweb.erp.especiales.cofide.entidades.paginaweb.IntCrmExpositor;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Sincroniza la pagina web de COFIDE
 *
 * @author ZeusSIWEB
 */
public class SincronizarPaginaWeb {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    Conexion oConn; // conexion a la DDBB local
    Conexion oConnCOFIDE; //conexion a la base de la pagina web
    Conexion oConnCOFIDE_LN; //conexion a la base de la lista negra
    boolean bolLocal = false;
    private String strUrl;
    private String strUsuario;
    private String strPassword;

    Fechas fecha = new Fechas();

    public String getStrUrl() {
        return strUrl;
    }

    public void setStrUrl(String strUrl) {
        this.strUrl = strUrl;
    }

    public String getStrUsuario() {
        return strUsuario;
    }

    public void setStrUsuario(String strUsuario) {
        this.strUsuario = strUsuario;
    }

    public String getStrPassword() {
        return strPassword;
    }

    public void setStrPassword(String strPassword) {
        this.strPassword = strPassword;
    }

    public boolean isBolLocal() {
        return bolLocal;
    }

    /**
     * Define si lo ejecutamos en local
     *
     * @param bolLocal
     */
    public void setBolLocal(boolean bolLocal) {
        this.bolLocal = bolLocal;
    }
    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(SincronizarPaginaWeb.class.getName());

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    public SincronizarPaginaWeb(Conexion oConn) {
        this.oConn = oConn;
        oConnCOFIDE = new Conexion();
        oConnCOFIDE_LN = new Conexion();
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    /**
     * Realiza la conexion a la base de COFIDE
     */
    private void connCOFIDE() {
        oConnCOFIDE.setStrNomPool("jdbc/COFIDE"); //comentar para hacer el testclases
        if (this.bolLocal) {
            String[] ConexionURL = new String[4];

            ConexionURL[0] = this.strUrl;
            ConexionURL[1] = this.strUsuario;
            ConexionURL[2] = this.strPassword;
            ConexionURL[3] = "mysql";

            try {
                oConnCOFIDE = new Conexion(ConexionURL, null);
            } catch (Exception ex) {
                LOG.error(ex.getMessage());
            }
            oConnCOFIDE.open();
        } else {
            oConnCOFIDE.open();
        }
    }

//    private void connCOFIDE_LN() { //ver la base de la lista negra directo del sitio web
//        oConnCOFIDE_LN.setStrNomPool("jdbc/COFIDE_LN"); //comentar para hacer el testclases
//        if (this.bolLocal) {
//            String[] ConexionURL = new String[4];
//
//            ConexionURL[0] = this.strUrl;
//            ConexionURL[1] = this.strUsuario;
//            ConexionURL[2] = this.strPassword;
//            ConexionURL[3] = "mysql";
//
//            try {
//                oConnCOFIDE_LN = new Conexion(ConexionURL, null);
//            } catch (Exception ex) {
//                LOG.error(ex.getMessage());
//            }
//            oConnCOFIDE_LN.open();
//        } else {
//            oConnCOFIDE_LN.open();
//        }
//    }
    /**
     * @param intIdCurso
     * @throws java.sql.SQLException
     */
    public void actualizaPaginaWeb(int intIdCurso) throws SQLException { //agrega el curso a la pagin web
        connCOFIDE(); // inicializa la conexion

        int intIdEvento = 1; //curso
        String strSqlDel = ""; //eliminar registro
        //Cargamos los datos del curso maestro
        CofideCursos curso = new CofideCursos();
        curso.ObtenDatos(intIdCurso, oConn);
        int intDuracion = curso.getFieldInt("CC_DURACION_HRS");
        int intDuracion2 = curso.getFieldInt("CC_DURACION_HRS2");
        int intDuracion3 = curso.getFieldInt("CC_DURACION_HRS3");
        int intDuracionTotal = intDuracion + intDuracion2 + intDuracion3;
        int intPrecioOnline = 0;
        int intPrecioVid = 0;

        intPrecioOnline = (int) Double.parseDouble(curso.getFieldString("CC_PRECIO_ON"));
        intPrecioVid = (int) Double.parseDouble(curso.getFieldString("CC_PRECIO_VID"));

        //Validamos si es curso
        if (curso.getFieldInt("CC_IS_SEMINARIO") == 0 && curso.getFieldInt("CC_IS_DIPLOMADO") == 0) { //curso
            //cat_cursos Maestro de cursos
            //elimina el registro
            strSqlDel = "delete from cat_cursos where id_curso = " + intIdCurso;
            oConnCOFIDE.runQueryLMD(strSqlDel);
            strSqlDel = "delete from det_curso where id_curso = " + intIdCurso;
            oConnCOFIDE.runQueryLMD(strSqlDel);
            //alta de curso

            CatCursos cursoPagina = new CatCursos();
            cursoPagina.setBolGetAutonumeric(true);
            cursoPagina.setFieldInt("id_curso", intIdCurso);
            cursoPagina.setFieldString("nombre", curso.getFieldString("CC_NOMBRE_CURSO"));
            cursoPagina.setFieldString("fch_alta", fecha.getFechaActualAAAAMMDDguion() + " " + fecha.getHoraActual());
            cursoPagina.setFieldString("fch_ini", fecha.FormateaAAAAMMDD(curso.getFieldString("CC_FECHA_INICIAL"), "-") + " " + curso.getFieldString("CC_HR_EVENTO_INI"));
            cursoPagina.setFieldString("hra_ini", curso.getFieldString("CC_HR_EVENTO_INI"));
            cursoPagina.setFieldString("hra_fin", curso.getFieldString("CC_HR_EVENTO_FIN"));
            cursoPagina.setFieldString("id_status", curso.getFieldString("CC_ESTATUS"));
            cursoPagina.setFieldString("alias_fch_ini", curso.getFieldString("CC_ALIAS_FEC"));
            cursoPagina.setFieldString("nombre_retrans", curso.getFieldString("CC_VIDEO"));
            cursoPagina.Agrega(oConnCOFIDE); //guarda en cat_curso
            //det_curso Es el detalle del curso
            DetCurso detalle = new DetCurso();
            detalle.setFieldInt("id_curso", intIdCurso);
            detalle.setFieldInt("duracion", intDuracionTotal);
            detalle.setFieldDouble("precio", curso.getFieldDouble("CC_PRECIO_PRES"));
            detalle.setFieldInt("p_online", intPrecioOnline);
            detalle.setFieldInt("p_video", intPrecioVid);

            detalle.setFieldInt("id_sede", curso.getFieldInt("CC_SEDE_ID"));
            detalle.setFieldString("objetivo", curso.getFieldString("CC_OBJETIVO"));
            detalle.setFieldString("dirigido", curso.getFieldString("CC_DIRIGIDO"));
            detalle.setFieldString("temario", curso.getFieldString("CC_TEMARIO"));

            detalle.setFieldInt("id_alimento", curso.getFieldInt("CC_TIPO_ALIMENTO"));
            detalle.setFieldString("hra_alimento", curso.getFieldString("CC_ALIMENTO"));
            detalle.setFieldString("requisitos", curso.getFieldString("CC_REQUISITO"));
            detalle.setFieldString("incluye", curso.getFieldString("CC_DETALLE"));
            detalle.Agrega(oConnCOFIDE); //guarda en los detalles del curso
            //interfaz de expositores
            IntCrmExpositor IntCrmEx = new IntCrmExpositor();

            strSqlDel = "delete from interface_expositores where id_evento = " + intIdCurso;
            oConnCOFIDE.runQueryLMD(strSqlDel);
            //Agrega N numero de Instructores
            String strSql = "select * from cofide_instructor_imparte where CC_CURSO_ID = " + intIdCurso;
            try {
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    //interfaz de expositores
                    IntCrmEx.setFieldInt("id_evento", intIdCurso);
                    IntCrmEx.setFieldInt("id_tipo_evento", intIdEvento);
                    IntCrmEx.setFieldInt("id_expositor", rs.getInt("CI_INSTRUCTOR_ID"));
                    IntCrmEx.Agrega(oConnCOFIDE);
                }
                rs.close();
            } catch (SQLException ex) {
                System.out.println("Obten Instructors del curso " + intIdCurso + " ERROR[" + ex.getLocalizedMessage() + "]");
            }

        }
        //Validamos si es seminario
        if (curso.getFieldInt("CC_IS_SEMINARIO") == 1) {
            //cat_seminarios Aqui van los cursos catalogados como seminarios
            //elimina el registro
            strSqlDel = "delete from cat_seminarios where id_seminario = " + intIdCurso;
            oConnCOFIDE.runQueryLMD(strSqlDel);
            strSqlDel = "delete from det_seminario where id_seminario = " + intIdCurso;
            oConnCOFIDE.runQueryLMD(strSqlDel);
            strSqlDel = "delete from cat_modulos where id_dip_sem = " + intIdCurso; //id detalle modulo eliminar
            oConnCOFIDE.runQueryLMD(strSqlDel);
            //alta de curso

            CatSeminarios seminario = new CatSeminarios();
//            seminario.setFieldInt("id_seminario", curso.getFieldInt("CC_ID_SEMINARIO_DIPLOMADO"));
            seminario.setFieldInt("id_seminario", intIdCurso);
            seminario.setFieldString("nombre", curso.getFieldString("CC_NOMBRE_CURSO"));
            seminario.setFieldString("fch_ini", fecha.FormateaAAAAMMDD(curso.getFieldString("CC_FECHA_INICIAL"), "-") + " " + curso.getFieldString("CC_HR_EVENTO_INI"));
            seminario.setFieldString("id_status", curso.getFieldString("CC_ESTATUS"));
            seminario.setFieldString("fch_alta", fecha.getFechaActualAAAAMMDDguion() + " " + fecha.getHoraActual());
            seminario.Agrega(oConnCOFIDE); //agrega los seminarios
            //det_seminario Aqui vael detalle de los cursos catalogados como seminarios
            DetSeminario detSeminario = new DetSeminario();
            detSeminario.setFieldInt("id_seminario", intIdCurso);
            detSeminario.setFieldInt("duracion", intDuracionTotal);
            detSeminario.setFieldDouble("precio", curso.getFieldDouble("CC_PRECIO_PRES"));
            detSeminario.setFieldInt("p_online", intPrecioOnline);
            detSeminario.setFieldInt("p_video", intPrecioVid);

            detSeminario.setFieldInt("id_sede", curso.getFieldInt("CC_SEDE_ID"));
            detSeminario.setFieldString("objetivo", curso.getFieldString("CC_OBJETIVO"));
            detSeminario.setFieldString("dirigido", curso.getFieldString("CC_DIRIGIDO"));
            detSeminario.setFieldString("temario", curso.getFieldString("CC_TEMARIO"));

            detSeminario.setFieldInt("id_alimento", curso.getFieldInt("CC_TIPO_ALIMENTO"));
            detSeminario.setFieldString("hra_alimento", curso.getFieldString("CC_ALIMENTO"));
            detSeminario.setFieldString("requisitos", curso.getFieldString("CC_REQUISITO"));
            detSeminario.setFieldString("incluye", curso.getFieldString("CC_DETALLE"));
            detSeminario.Agrega(oConnCOFIDE); //guardamos los detalles del seminario
            //cat_modulos Aqui van los modulos de cada seminario o diplomado
            String strSql = "Select * from cofide_modulo_curso where CC_CURSO_IDD = " + intIdCurso; //id detalle modulo
            int intID = 0;
            int intIDD = 0;
            intIdEvento = 3;
            int intModulo = 1;
            String strModulo = "";
            try {
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    intID = rs.getInt("CC_CURSO_ID");
                    intIDD = rs.getInt("CC_CURSO_IDD");
                    strModulo = rs.getString("CC_NOMBRE_CURSO");
                    CatModulos modulo = new CatModulos();
                    modulo.setFieldInt("id_curso", intID);
                    modulo.setFieldInt("id_dip_sem", intIDD);
                    modulo.setFieldInt("id_tipo_evento", 3);
                    System.out.println("seminario: " + intIdEvento);
                    modulo.setFieldString("descripcion", strModulo);
                    modulo.setFieldInt("modulo", intModulo);
                    modulo.Agrega(oConnCOFIDE); // agrega modulos del seminario
                    intModulo++;
                }
                rs.close();
            } catch (SQLException ex) {
                LOG.error(ex.getMessage());
            }
            //interfaz de expositores
            IntCrmExpositor IntCrmEx = new IntCrmExpositor();
            strSqlDel = "delete from interface_expositores where id_evento = " + intIdCurso;
            oConnCOFIDE.runQueryLMD(strSqlDel);
            //Agrega N numero de Instructores
            strSql = "select * from cofide_instructor_imparte where CC_CURSO_ID = " + intIdCurso;
            try {
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    //interfaz de expositores

                    IntCrmEx.setFieldInt("id_evento", intIdCurso);
                    IntCrmEx.setFieldInt("id_tipo_evento", 3);
                    IntCrmEx.setFieldInt("id_expositor", rs.getInt("CI_INSTRUCTOR_ID"));
                    IntCrmEx.Agrega(oConnCOFIDE);
                }
                rs.close();
            } catch (SQLException ex) {
                System.out.println("Obten Instructors del curso " + intIdCurso + " ERROR[" + ex.getLocalizedMessage() + "]");
            }

        }
        //Validamos si es diplomado
        if (curso.getFieldInt("CC_IS_DIPLOMADO") == 1) {
            //cat_diplomados Aqui van los cursos catalogados como diplomados
            //elimina el registro
            strSqlDel = "delete from cat_diplomados where id_diplomado = " + intIdCurso;
            oConnCOFIDE.runQueryLMD(strSqlDel);
            strSqlDel = "delete from det_diplomado where id_diplomado = " + intIdCurso;
            oConnCOFIDE.runQueryLMD(strSqlDel);
            strSqlDel = "delete from cat_modulos where id_dip_sem = " + intIdCurso; //id detalle modulo eliminar
            oConnCOFIDE.runQueryLMD(strSqlDel);
            //alta de curso

            CatDiplomados diplomado = new CatDiplomados();
            diplomado.setFieldInt("id_diplomado", intIdCurso);
            diplomado.setFieldString("nombre", curso.getFieldString("CC_NOMBRE_CURSO"));
            diplomado.setFieldString("fch_ini", fecha.FormateaAAAAMMDD(curso.getFieldString("CC_FECHA_INICIAL"), "-") + " " + curso.getFieldString("CC_HR_EVENTO_INI"));
            diplomado.setFieldString("id_status", curso.getFieldString("CC_ESTATUS"));
            diplomado.setFieldString("fch_alta", fecha.getFechaActualAAAAMMDDguion() + " " + fecha.getHoraActual());
            diplomado.Agrega(oConnCOFIDE); //llenar diplomados
            //det_diplomado Aqui va el detalle de  los cursos catalogados como diplomados 
            DetDiplomado detDiplomado = new DetDiplomado();
            detDiplomado.setFieldInt("id_diplomado", intIdCurso);
            detDiplomado.setFieldInt("duracion", intDuracionTotal);
            detDiplomado.setFieldDouble("precio", curso.getFieldDouble("CC_PRECIO_PRES"));
            detDiplomado.setFieldInt("p_online", intPrecioOnline);
            detDiplomado.setFieldInt("p_video", intPrecioVid);

            detDiplomado.setFieldInt("id_sede", curso.getFieldInt("CC_SEDE_ID"));
            detDiplomado.setFieldString("objetivo", curso.getFieldString("CC_OBJETIVO"));
            detDiplomado.setFieldString("dirigido", curso.getFieldString("CC_DIRIGIDO"));
            detDiplomado.setFieldString("temario", curso.getFieldString("CC_TEMARIO"));

            detDiplomado.setFieldInt("id_alimento", curso.getFieldInt("CC_TIPO_ALIMENTO"));
            detDiplomado.setFieldString("hra_alimento", curso.getFieldString("CC_ALIMENTO"));
            detDiplomado.setFieldString("requisitos", curso.getFieldString("CC_REQUISITO"));
            detDiplomado.setFieldString("incluye", curso.getFieldString("CC_DETALLE"));
            detDiplomado.Agrega(oConnCOFIDE); //agrega detalles dle diplomado
            //modulos
            String strSql = "Select * from cofide_modulo_curso where CC_CURSO_IDD = " + intIdCurso;
            int intID = 0;
            int intIDD = 0;
            intIdEvento = 2;
            String strModulo = "";
            int intModulo = 1;
            try {
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    intID = rs.getInt("CC_CURSO_ID");
                    intIDD = rs.getInt("CC_CURSO_IDD");
                    strModulo = rs.getString("CC_NOMBRE_CURSO");
                    CatModulos modulo = new CatModulos();
                    modulo.setFieldInt("id_curso", intID);
                    modulo.setFieldInt("id_dip_sem", intIDD);
                    modulo.setFieldInt("id_tipo_evento", 2);
                    System.out.println("diplomado: " + intIdEvento);
                    modulo.setFieldString("descripcion", strModulo);
                    modulo.setFieldInt("modulo", intModulo);
                    modulo.Agrega(oConnCOFIDE); //agrega modulos del diplomado
                    intModulo++;
                }
                rs.close();
            } catch (SQLException ex) {
                LOG.error(ex.getMessage());
            }

            //Elimina Expositores para reinsertar los nuevos
            strSqlDel = "delete from interface_expositores where id_evento = " + intIdCurso;
            oConnCOFIDE.runQueryLMD(strSqlDel);
            //Agrega N numero de Instructores
            strSql = "select * from cofide_instructor_imparte where CC_CURSO_ID = " + intIdCurso;
            try {
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    //interfaz de expositores
                    IntCrmExpositor IntCrmEx = new IntCrmExpositor();
                    IntCrmEx.setFieldInt("id_evento", intIdCurso);
                    IntCrmEx.setFieldInt("id_tipo_evento", 2);
                    IntCrmEx.setFieldInt("id_expositor", rs.getInt("CI_INSTRUCTOR_ID"));
                    IntCrmEx.Agrega(oConnCOFIDE);
                }
                rs.close();
            } catch (SQLException ex) {
                System.out.println("Obten Instructors del curso " + intIdCurso + " ERROR[" + ex.getLocalizedMessage() + "]");
            }

        }
        //cat_eventos Aqui van todos los cursos, seminarios y diplomados
        CatEventos eventos = new CatEventos();

        strSqlDel = "delete from cat_eventos where id_evento = " + intIdCurso;
        oConnCOFIDE.runQueryLMD(strSqlDel);
        String strIsOnline = "0";
        String strRetrans = "0";
        if (curso.getFieldInt("CC_IS_ONLINE") == 1) {
            strIsOnline = "1";
            System.out.println("online");
        }
        if (curso.getFieldInt("CC_IS_VIDEO") == 1) {
            strIsOnline = "2";
            strRetrans = "1";
            System.out.println("video / retransmision");
        }
        eventos.setFieldInt("id_evento", intIdCurso);
        System.out.println("tipo de eventi: " + intIdEvento);
        eventos.setFieldInt("id_tipo_evento", intIdEvento);
        eventos.setFieldString("fch_ini", fecha.FormateaAAAAMMDD(curso.getFieldString("CC_FECHA_INICIAL"), "-") + " " + curso.getFieldString("CC_HR_EVENTO_INI"));
        eventos.setFieldString("id_status", curso.getFieldString("CC_ESTATUS"));
        eventos.setFieldString("prioridad", String.valueOf(curso.getFieldInt("CC_PRIORIDAD")));
        eventos.setFieldString("id_mod", strIsOnline);
        eventos.setFieldString("retransmision", strRetrans);
        eventos.setFieldString("retrans_status_w", strRetrans);
        eventos.Agrega(oConnCOFIDE);
        //SEDE
        CofideSedeH cs = new CofideSedeH();
        cs.ObtenDatos(curso.getFieldInt("CC_SEDE_ID"), oConn);
        CatSede sede = new CatSede();
        //elimina el registro
        strSqlDel = "delete from cat_sedes where id_sede = " + curso.getFieldInt("CC_SEDE_ID");
        oConnCOFIDE.runQueryLMD(strSqlDel);

        //cat_sedes Aqui va toda la información de la sede del curso
        String strCalle = cs.getFieldString("CSH_CALLE") + " " + cs.getFieldString("CSH_NUM_EXT") + " " + cs.getFieldString("CSH_NUM_INT");
        sede.setFieldInt("id_sede", cs.getFieldInt("CSH_ID"));
        sede.setFieldString("nombre", cs.getFieldString("CSH_SEDE"));
        sede.setFieldString("tel", cs.getFieldString("CSH_TELEFONO"));
        sede.setFieldString("calle_num", strCalle);
        sede.setFieldString("col", cs.getFieldString("CSH_COLONIA"));

        sede.setFieldString("id_estado", cs.getFieldString("CSH_ID_ESTADO"));
        sede.setFieldInt("cp", cs.getFieldInt("CSH_CP"));

        sede.setFieldInt("id_status", cs.getFieldInt("CSH_ESTATUS"));
        sede.setFieldString("url_map", cs.getFieldString("CSH_URLMAP"));
        sede.setFieldString("alias", cs.getFieldString("CSH_ALIAS"));
        sede.Agrega(oConnCOFIDE);

        //INSTRUCTOR
        CofideInstructor ci = new CofideInstructor();
        String strSqlE = "select * from cofide_instructor_imparte where CC_CURSO_ID = " + intIdCurso;
        ResultSet rsE = oConn.runQuery(strSqlE, true);
        while (rsE.next()) {

            ci.ObtenDatos(rsE.getInt("CI_INSTRUCTOR_ID"), oConn);
            CatExpositor expo = new CatExpositor();
            //elimina el registro
            strSqlDel = "delete from cat_expositores where id_expositor = " + rsE.getInt("CI_INSTRUCTOR_ID");
            oConnCOFIDE.runQueryLMD(strSqlDel);

            String strSql = "select CTI_ABRV from cofide_titulos where CTI_ID = " + ci.getFieldString("CI_TITULO");
            ResultSet rs = oConn.runQuery(strSql, true);
            String strTitulo = "";
            while (rs.next()) {
                strTitulo = rs.getString("CTI_ABRV");
            }
            rs.close();
            //cat_expositores Aqui va toda la información del indstructor del curso
            expo.setFieldInt("id_expositor", ci.getFieldInt("CI_INSTRUCTOR_ID"));
            expo.setFieldString("titulo", strTitulo);
            expo.setFieldString("nombre", ci.getFieldString("CI_NOMBRE"));
            expo.setFieldString("apellidos", ci.getFieldString("CI_APELLIDO"));
            expo.setFieldString("cv", ci.getFieldString("CI_CV"));
            expo.setFieldInt("id_status", ci.getFieldInt("CI_ESTATUS"));
            expo.setFieldString("foto", ci.getFieldString("CI_FOTO"));
            expo.Agrega(oConnCOFIDE);
        }
        rsE.close();
        EventoHorario(intIdCurso); //cursos con mas de una fecha
        oConnCOFIDE.close();
    }

    /**
     * actualiza la informacion ingresada de los instructores
     *
     * @param stridExp
     * @throws SQLException
     */
    public String ActualizaExpocitor(String stridExp) throws SQLException {
        connCOFIDE();
        CofideInstructor ci = new CofideInstructor();
        int intIdExpocitor = Integer.parseInt(stridExp);
        ci.ObtenDatos(intIdExpocitor, oConn);
        CatExpositor expo = new CatExpositor();
        String strResp = "";
        //elimina el registro
        String strSqlDel = "delete from cat_expositores where id_expositor = " + intIdExpocitor;
        oConnCOFIDE.runQueryLMD(strSqlDel);
        //obtener el titulo del instructor
        String strSql = "select CTI_ABRV from cofide_titulos where CTI_ID = " + ci.getFieldString("CI_TITULO");
        ResultSet rs = oConn.runQuery(strSql, true);
        String strTitulo = "";
        while (rs.next()) {
            strTitulo = rs.getString("CTI_ABRV");
        }
        rs.close();
        //cat_expositores Aqui va toda la información del indstructor del curso
        expo.setFieldInt("id_expositor", ci.getFieldInt("CI_INSTRUCTOR_ID"));
        expo.setFieldString("titulo", strTitulo);
        expo.setFieldString("nombre", ci.getFieldString("CI_NOMBRE"));
        expo.setFieldString("apellidos", ci.getFieldString("CI_APELLIDO"));
        expo.setFieldString("cv", ci.getFieldString("CI_CV"));
        expo.setFieldInt("id_status", ci.getFieldInt("CI_ESTATUS"));
        expo.setFieldString("foto", ci.getFieldString("CI_FOTO"));
        strResp = expo.Agrega(oConnCOFIDE);
        oConnCOFIDE.close();
        return strResp;
    }

    public void CancelaCurso(String strIdCurso) {
        connCOFIDE();
        //elimina el registro
        String strSqlDel = "UPDATE cat_eventos SET id_status = 0 WHERE id_evento = " + strIdCurso;
        oConnCOFIDE.runQueryLMD(strSqlDel);
    }

//    public void peticionDescargaMaterial(int intIdTicket, int intIdFactura) {
//        connCOFIDE();
//        String strQuery = "";
//        int intId_Curso = 0;
//        int intVenta = 0;
//        String strEmail = "";
//        int intTipo = 0;
//        if (intIdTicket != 0) {
//            strQuery = " CP_TKT_ID = " + intIdTicket;
//        }
//        if (intIdFactura != 0) {
//            strQuery = " CP_FAC_ID = " + intIdTicket;
//        }
//        String strParticipanteSQL = "select * from  cofide_participantes where" + strQuery;
//        ResultSet rs;
//        try {
//            rs = oConn.runQuery(strParticipanteSQL, true);
//            while (rs.next()) {
//                if (rs.getInt("CP_TKT_ID") != 0) {
//                    intVenta = rs.getInt("CP_TKT_ID");
//                }
//                if (rs.getInt("CP_FAC_ID") != 0) {
//                    intVenta = rs.getInt("CP_FAC_ID");
//                }
//                intId_Curso = rs.getInt("CP_ID_CURSO");
//                strEmail = rs.getString("CP_CORREO");
//                intTipo = rs.getInt("CP_TIPO_CURSO");
//            }
//            rs.close();
//        } catch (SQLException ex) {
//            Logger.getLogger(SincronizarPaginaWeb.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        //cat_crm_mat
//        CatCrmMat interfaz = new CatCrmMat();
//        interfaz.setFieldInt("id_venta", intVenta);
//        interfaz.setFieldInt("id_evento", intId_Curso);
//        interfaz.setFieldInt("id_tipo_evento", intTipo);
//        interfaz.setFieldString("email", strEmail);
//        interfaz.Agrega(oConnCOFIDE); //guardar en eventos crm
//        oConnCOFIDE.close();
//    }
    /**
     * Sincroniza la lista negra de la pagina web con la del CRM
     */
//    public void sincronizaListaNegra() {
//        connCOFIDE();
//        String strCorreo = "";
//        //cofide_no_deseados
//        String strSql = "select * from users";
//        ResultSet rs;
//        try {
//            rs = oConnCOFIDE.runQuery(strSql, true);
//            while (rs.next()) {
//                strCorreo = rs.getString("email");
//                //Buscamos si existe en la tabla de CofideListaNegra
//                CofideListaNegra item = new CofideListaNegra();
//                item.setFieldString("CB_CT_CORREO", strCorreo);
//                item.Agrega(oConn); //agregar a lista negra en erp
//            }
//            rs.close();
//        } catch (SQLException ex) {
//            LOG.error(ex.getMessage());
//        }
//        oConnCOFIDE.close();
//    }
    public void EventoHorario(int intIdCurso) {
        connCOFIDE();

        CofideCursos cc = new CofideCursos();
        cc.ObtenDatos(intIdCurso, oConn);
        CofideHrEvento ce = new CofideHrEvento();

        //elimina el registro
        String strSqlDel = "delete from horario_eventos where id_evento = " + intIdCurso;
        oConnCOFIDE.runQueryLMD(strSqlDel);
        //alimenta la tabla de eventos
        ce.setFieldInt("id_evento", intIdCurso);
        ce.setFieldString("fch_ini", fecha.FormateaAAAAMMDD(cc.getFieldString("CC_FECHA_INICIAL"), "-") + " " + cc.getFieldString("CC_HR_EVENTO_INI"));
        ce.setFieldString("fch_fin", fecha.FormateaAAAAMMDD(cc.getFieldString("CC_FECHA_INICIAL"), "-") + " " + cc.getFieldString("CC_HR_EVENTO_FIN"));
        ce.Agrega(oConnCOFIDE);
        if (!cc.getFieldString("CC_FECHA_INICIAL2").equals("")) {
            ce.setFieldInt("id_evento", intIdCurso);
            ce.setFieldString("fch_ini", fecha.FormateaAAAAMMDD(cc.getFieldString("CC_FECHA_INICIAL2"), "-") + " " + cc.getFieldString("CC_HR_EVENTO_INI2"));
            ce.setFieldString("fch_fin", fecha.FormateaAAAAMMDD(cc.getFieldString("CC_FECHA_INICIAL2"), "-") + " " + cc.getFieldString("CC_HR_EVENTO_FIN2"));
            ce.Agrega(oConnCOFIDE);
        }
        if (!cc.getFieldString("CC_FECHA_INICIAL3").equals("")) {
            ce.setFieldInt("id_evento", intIdCurso);
            ce.setFieldString("fch_ini", fecha.FormateaAAAAMMDD(cc.getFieldString("CC_FECHA_INICIAL3"), "-") + " " + cc.getFieldString("CC_HR_EVENTO_INI3"));
            ce.setFieldString("fch_fin", fecha.FormateaAAAAMMDD(cc.getFieldString("CC_FECHA_INICIAL3"), "-") + " " + cc.getFieldString("CC_HR_EVENTO_FIN3"));
            ce.Agrega(oConnCOFIDE);
        }
        oConnCOFIDE.close();
    }
}
