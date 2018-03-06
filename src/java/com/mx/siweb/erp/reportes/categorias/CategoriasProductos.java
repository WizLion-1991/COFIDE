/*
 * Sirve para obtener las clasificaciones de los productos.
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.reportes.categorias;

import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 *
 * @author N4v1d4d3s
 */
public class CategoriasProductos {

    /*SON LAS PROPIEDADES DE LAS CLASIFICACIONES*/
    HashMap<Integer, String> CAT1 = new HashMap<Integer, String>();
    HashMap<Integer, String> CAT2 = new HashMap<Integer, String>();
    HashMap<Integer, String> CAT3 = new HashMap<Integer, String>();
    HashMap<Integer, String> CAT4 = new HashMap<Integer, String>();
    HashMap<Integer, String> CAT5 = new HashMap<Integer, String>();
    HashMap<Integer, String> CAT6 = new HashMap<Integer, String>();
    HashMap<Integer, String> CAT7 = new HashMap<Integer, String>();
    HashMap<Integer, String> CAT8 = new HashMap<Integer, String>();
    HashMap<Integer, String> CAT9 = new HashMap<Integer, String>();
    HashMap<Integer, String> CAT10 = new HashMap<Integer, String>();

    /*ES EL CONSTRUCTOR*/
    public CategoriasProductos(Conexion oConn) {
        this.getValuesCategoria1(oConn);
        this.getValuesCategoria2(oConn);
        this.getValuesCategoria3(oConn);
        this.getValuesCategoria4(oConn);
        this.getValuesCategoria5(oConn);
        this.getValuesCategoria6(oConn);
        this.getValuesCategoria7(oConn);
        this.getValuesCategoria8(oConn);
        this.getValuesCategoria9(oConn);
        this.getValuesCategoria10(oConn);
    }

    /*SET AN GET DE LAS PROPIEDADES(CATEGORIAS)*/
    public HashMap<Integer, String> getCAT1() {
        return CAT1;
    }

    public void setCAT1(HashMap<Integer, String> CAT1) {
        this.CAT1 = CAT1;
    }

    public HashMap<Integer, String> getCAT2() {
        return CAT2;
    }

    public void setCAT2(HashMap<Integer, String> CAT2) {
        this.CAT2 = CAT2;
    }

    public HashMap<Integer, String> getCAT3() {
        return CAT3;
    }

    public void setCAT3(HashMap<Integer, String> CAT3) {
        this.CAT3 = CAT3;
    }

    public HashMap<Integer, String> getCAT4() {
        return CAT4;
    }

    public void setCAT4(HashMap<Integer, String> CAT4) {
        this.CAT4 = CAT4;
    }

    public HashMap<Integer, String> getCAT5() {
        return CAT5;
    }

    public void setCAT5(HashMap<Integer, String> CAT5) {
        this.CAT5 = CAT5;
    }

    public HashMap<Integer, String> getCAT6() {
        return CAT6;
    }

    public void setCAT6(HashMap<Integer, String> CAT6) {
        this.CAT6 = CAT6;
    }

    public HashMap<Integer, String> getCAT7() {
        return CAT7;
    }

    public void setCAT7(HashMap<Integer, String> CAT7) {
        this.CAT7 = CAT7;
    }

    public HashMap<Integer, String> getCAT8() {
        return CAT8;
    }

    public void setCAT8(HashMap<Integer, String> CAT8) {
        this.CAT8 = CAT8;
    }

    public HashMap<Integer, String> getCAT9() {
        return CAT9;
    }

    public void setCAT9(HashMap<Integer, String> CAT9) {
        this.CAT9 = CAT9;
    }

    public HashMap<Integer, String> getCAT10() {
        return CAT10;
    }

    public void setCAT10(HashMap<Integer, String> CAT10) {
        this.CAT10 = CAT10;
    }

    /*
     *Sirve para obtener los valores de la categoria 1 y 
     * almacenarlos en la propiedad CAT1
     */
    public void getValuesCategoria1(Conexion oConn) {

        try {

            String strConsulta = "SELECT * FROM vta_prodcat1";

            ResultSet rs = oConn.runQuery(strConsulta);

            while (rs.next()) {

                CAT1.put(rs.getInt("PC_ID"), rs.getString("PC_DESCRIPCION"));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }

    }
    /*
     *Sirve para obtener los valores de la categoria 2 y 
     * almacenarlos en la propiedad CAT2
     */

    public void getValuesCategoria2(Conexion oConn) {

        try {

            String strConsulta = "SELECT * FROM vta_prodcat2";

            ResultSet rs = oConn.runQuery(strConsulta);

            while (rs.next()) {
                CAT2.put(rs.getInt("PC2_ID"), rs.getString("PC2_DESCRIPCION"));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }

    }
    /*
     *Sirve para obtener los valores de la categoria 3 y 
     * almacenarlos en la propiedad CAT3
     */

    public void getValuesCategoria3(Conexion oConn) {

        try {

            String strConsulta = "SELECT * FROM vta_prodcat3";

            ResultSet rs = oConn.runQuery(strConsulta);

            while (rs.next()) {

                CAT3.put(rs.getInt("PC3_ID"), rs.getString("PC3_DESCRIPCION"));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }

    }
    /*
     *Sirve para obtener los valores de la categoria 4 y 
     * almacenarlos en la propiedad CAT4
     */

    public void getValuesCategoria4(Conexion oConn) {

        try {

            String strConsulta = "SELECT * FROM vta_prodcat4";

            ResultSet rs = oConn.runQuery(strConsulta);

            while (rs.next()) {

                CAT4.put(rs.getInt("PC4_ID"), rs.getString("PC4_DESCRIPCION"));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }

    }
    /*
     *Sirve para obtener los valores de la categoria 5 y 
     * almacenarlos en la propiedad CAT5
     */

    public void getValuesCategoria5(Conexion oConn) {

        try {

            String strConsulta = "SELECT * FROM vta_prodcat5";

            ResultSet rs = oConn.runQuery(strConsulta);

            while (rs.next()) {

                CAT5.put(rs.getInt("PC5_ID"), rs.getString("PC5_DESCRIPCION"));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }

    }
    /*
     *Sirve para obtener los valores de la categoria 6 y 
     * almacenarlos en la propiedad CAT6
     */

    public void getValuesCategoria6(Conexion oConn) {

        try {

            String strConsulta = "SELECT * FROM vta_prodcat6";

            ResultSet rs = oConn.runQuery(strConsulta);

            while (rs.next()) {

                CAT6.put(rs.getInt("PC6_ID"), rs.getString("PC6_DESCRIPCION"));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }

    }
    /*
     *Sirve para obtener los valores de la categoria 7 y 
     * almacenarlos en la propiedad CAT7
     */

    public void getValuesCategoria7(Conexion oConn) {

        try {

            String strConsulta = "SELECT * FROM vta_prodcat7";

            ResultSet rs = oConn.runQuery(strConsulta);

            while (rs.next()) {

                CAT7.put(rs.getInt("PC7_ID"), rs.getString("PC7_DESCRIPCION"));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }

    }
    /*
     *Sirve para obtener los valores de la categoria 8 y 
     * almacenarlos en la propiedad CAT8
     */

    public void getValuesCategoria8(Conexion oConn) {

        try {

            String strConsulta = "SELECT * FROM vta_prodcat8";

            ResultSet rs = oConn.runQuery(strConsulta);

            while (rs.next()) {

                CAT8.put(rs.getInt("PC8_ID"), rs.getString("PC8_DESCRIPCION"));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }

    }
    /*
     *Sirve para obtener los valores de la categoria 9 y 
     * almacenarlos en la propiedad CAT9
     */

    public void getValuesCategoria9(Conexion oConn) {

        try {

            String strConsulta = "SELECT * FROM vta_prodcat9";

            ResultSet rs = oConn.runQuery(strConsulta);

            while (rs.next()) {

                CAT9.put(rs.getInt("PC9_ID"), rs.getString("PC9_DESCRIPCION"));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }

    }
    /*
     *Sirve para obtener los valores de la categoria 10 y 
     * almacenarlos en la propiedad CAT10
     */

    public void getValuesCategoria10(Conexion oConn) {

        try {

            String strConsulta = "SELECT * FROM vta_prodcat10";

            ResultSet rs = oConn.runQuery(strConsulta);

            while (rs.next()) {

                CAT10.put(rs.getInt("PC10_ID"), rs.getString("PC10_DESCRIPCION"));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }

    }
}
