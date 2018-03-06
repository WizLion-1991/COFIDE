/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.casajosefa;

import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author siwebmx5
 */
public class CalculoUPLINE {
    // <editor-fold defaultstate="collapsed" desc="Propiedades">

    private int intMaxNivel;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(CalculoUPLINE.class.getName());

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    public CalculoUPLINE() {
        this.intMaxNivel = 0;
    }

    public int getIntMaxNivel() {
        return intMaxNivel;
    }

    public void setIntMaxNivel(int intMaxNivel) {
        this.intMaxNivel = intMaxNivel;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    /*Metodo Padre
     * Parametros:
     *    intActual ---Indica el cliente actual a evaluar
     *    oConn     ---Conexion a la base de datos
     * Funcion del metodo:
     *   Busca el padre del nodo actual, regresa el valor del reclutador, 0 si no encuentra alguno
     */
    public int Padre(int intActual, Conexion oConn) {
        String strSQL = "Select CT_UPLINE From vta_cliente Where CT_ID = " + intActual;
        try {
            ResultSet rs = oConn.runQuery(strSQL, true);
            while (rs.next()) {
                 //if(rs.getStatement() != null )rs.getStatement().close(); 
                 int intUplineTmp = rs.getInt("CT_UPLINE");
                 rs.close();
                return intUplineTmp;
            }
             //if(rs.getStatement() != null )rs.getStatement().close(); 
             rs.close();
        } catch (SQLException ex) {
            this.log.error(ex.getMessage());
        }

        return 0;
    }
    /*Metodo TieneHijos
     * Parametros:
     *    intActual ---Indica el cliente actual a evaluar
     *    oConn     ---Conexion a la base de datos
     * Funcion del metodo:
     *   Buscar si tiene hijos o no si los tiene regresa verdadero, si no, regresa falso
     */
    public boolean TieneHijos(int intActual, Conexion oConn) {
        String strSQL = "Select CT_ID From vta_cliente Where CT_UPLINE = " + intActual;
        try {
            ResultSet rs = oConn.runQuery(strSQL, true);
            while (rs.next()) {
                 //if(rs.getStatement() != null )rs.getStatement().close(); 
                 rs.close();
                return true;
            }
             //if(rs.getStatement() != null )rs.getStatement().close(); 
             rs.close();
        } catch (SQLException ex) {
            this.log.error(ex.getMessage());
        }
        return false;
    }
    /*Metodo NivelMaximo
     * Parametros:
     *    intActual ---Indica el cliente actual a evaluar
     *    oConn     ---Conexion a la base de datos
     *    intNivelActual --- Nos indica el nivel en que nos encontramos
     * Funcion del metodo:
     *   Regresa el nivel maximo al que se puede agregar un nuevo reclutado
     */

    private int NivelMaximo(int intActual, int intNivelActual, Conexion oConn) {        
        if (!TieneHijos(intActual, oConn) || intNivelActual == this.intMaxNivel) {
            return intNivelActual+1;
        }
        String strSQL = "Select CT_ID From vta_cliente Where CT_UPLINE = " + intActual;
        try {
            ResultSet rs = oConn.runQuery(strSQL, true);
            ArrayList<Integer> list = new ArrayList<Integer>();
            int intprof= 0;
            while (rs.next()) {
                intprof = NivelMaximo(rs.getInt("CT_ID"), (intNivelActual + 1), oConn);
                list.add(intprof);
                if (intprof==this.intMaxNivel)
                {
                    break;
                }
            }
             //if(rs.getStatement() != null )rs.getStatement().close(); 
             rs.close();
            Iterator<Integer> it = list.iterator();
            int intMax = 0;
            while (it.hasNext()) {
                Integer Actual = it.next();
                if (intMax < Actual.intValue()) {
                    intMax = Actual.intValue();
                }
            }
            return intMax;

        } catch (SQLException ex) {
            this.log.error(ex.getMessage());

        }
        return intNivelActual;
    }

    public int EncontrarNivelMaximo(int intCliente, Conexion oConn) {
        return NivelMaximo(intCliente, 0, oConn);
    }
    /*Metodo DebajoDeQuien
     * Parametros:
     *    intActual ---Indica el cliente actual a evaluar
     *    oConn     ---Conexion a la base de datos
     * Funcion del metodo:
     *   Regresa de quien estara debajo el nuevo reclutado
     */

    public int DebajoDeQuien(int intActual, int intNivelDestino, Conexion oConn) {
        if (intNivelDestino == 1) {
            return intActual;
        }
        int intNumCiclos = intNivelDestino-1;
        int intNumHijos = 1;
        Queue<Integer> stkQueue = new LinkedList<Integer>();
        String strSQL="";
        stkQueue.add(intActual);
        //Obtenemos todos los clientes/nodos que se encuentren en dicho nivel
          //Primero Obtenemos a que nivel debemos de llegar, por lo tanto profundizaremos tantos como niveles solicitados
        for (int i = 0; i < intNumCiclos; i++) {
            //Por cada Nivel Hay un numero de Clientes, de los cuales obtendremos los que se encuentran debajo de ellos
            for (int j = 0; j < intNumHijos; j++) {
                strSQL = "Select CT_ID From vta_cliente Where CT_UPLINE = " + stkQueue.poll().intValue();
                try {
                    ResultSet rs = oConn.runQuery(strSQL, true);                    
                    while (rs.next()) {
                        stkQueue.add(rs.getInt("CT_ID"));
                    }
                     //if(rs.getStatement() != null )rs.getStatement().close(); 
                     rs.close();
                } catch (SQLException ex) {
                    this.log.error(ex.getMessage());

                }
            }
            intNumHijos = stkQueue.size();
        }
        //Una vez obtenidos todos los clientes que se encuentran en dicho nivel Se obtiene cual de todos ellos tienen el valor mas bajo
        //Y es donde se debera poner el siguiente Cliente.
        int intClienteMenor = stkQueue.poll().intValue();
        int intNumeroMenor= this.NumeroDeHijos(intClienteMenor, oConn);
        int intNumActual =0;
        int intClienteActual =0;
        while(!stkQueue.isEmpty())
        {
            intClienteActual = stkQueue.poll().intValue();
            intNumActual = this.NumeroDeHijos(intClienteActual, oConn);
            if(intNumActual < intNumeroMenor)
            {
                intClienteMenor = intClienteActual;
                intNumeroMenor = intNumActual;
            }
        }
        
        return intClienteMenor;
    }
    /*Metodo NumeroDeHijos
     * Parametros:
     *    intCliente ---Indica el cliente se va a evaluar
     *    oConn     ---Conexion a la base de datos
     * Funcion del metodo:
     *   El numero de clientes debajo directo del cliente actual
     */
    private int NumeroDeHijos(int intCliente, Conexion oConn) {
        String strSQL = "Select Count(CT_ID) as numHijos From vta_cliente Where CT_UPLINE = " + intCliente;
        int intCont = 0;
        try {
            ResultSet rs = oConn.runQuery(strSQL, true);

            while (rs.next()) {
                intCont = rs.getInt("numHijos");
            }
             //if(rs.getStatement() != null )rs.getStatement().close(); 
             rs.close();

        } catch (SQLException ex) {
            this.log.error(ex.getMessage());

        }
        return intCont;
    }
    // </editor-fold>
}
