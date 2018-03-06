/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.nominas.Entidades;

/**
 *
 * @author CasaJosefa
 */
public class CalculoAnualISRBachE {

    private int mes = 0;//MES INICIAL
    private int mesFinal = 0;//MES FINAL
    private String rfc = "";//R.F.C.
    private String curp = "";//C.U.R.P.
    private String apellidoPaterno = "";//APELLIDO PATERNO
    private String apellidoMaterno = "";//APELLIDO MATERNO
    private String nombres = "";//NOMBRE (S)
    private int areaGeo = 0;//AREA GEO.
    private int calAnual = 0;//CAL. ANUAL
    private int tarifa = 0;//TARIFA
    private int tarifa91 = 0;//TARIFA '91
    private double proporc = 0;//PROPORC.
    private int sindicato = 0;//SINDICATO
    private int asimilado = 0;//ASIMILADO
    private int entidad = 0;//ENTIDAD
    private String rfc1Pat = "";//RFC 1 PAT
    private String rfc2pat = "";//RFC 2 PAT
    private String rfc3pat = "";//RFC 3 PAT
    private String rfc4pat = "";//RFC 4 PAT
    private String rfc5pat = "";//RFC 5 PAT
    private String rfc6pat = "";//RFC 6 PAT
    private String rfc7pat = "";//RFC 7 PAT
    private String rfc8pat = "";//RFC 8 PAT
    private String rfc9pat = "";//RFC 9 PAT
    private String rfc10pat = "";//RFC 10 PAT
    private double montoAportacionesVoluntarias = 0.0; //MONTO DE LAS APORTACIONES VOLUNTARIAS EFECTUADAS
    private String aplicoMontoAportVoluntarioas = "";//INDIQUE SI EL PATRÓN APLICÓ EL MONTO DE LAS APORTACIONES VOLUNTARIAS EN EL CÁLCULO DEL IMPUESTO
    private double montoAportDeduciblesTrabDecl = 0.0;//MONTO DE LAS APORTACIONES VOLUNTARIAS DEDUCIBLES PARA TRABAJADORES QUE REALIZARÁN SU DECLARACIÓN
    private double montoAportVoluDeduPatron = 0.0;//MONTO DE LAS APORTACIONES VOLUNTARIAS DEDUCIBLES APLICADAS POR EL PATRÓN
    private int pagosSeparacion = 0;//Pagos por separación
    private int asimiladosSalarios = 0;//Asimilados a salarios (excepto fracción I del Art. 110 de la LISR)
    private int pagosEfectuadosTrabajadores = 0;//Pagos del patrón efectuados a sus trabajadores (incluyendo fracción I del Art. 110 de la LISR)
    private double totalesPagosParciales = 0.0;//ING TOTALES POR PAGOS PARCIALES
    private double montoDiarioJubilaciones = 0.0;//MONTO DIARIO JUBILACIONES, RETIRO, EN PARCIALIDADES
    private double cantidadPercibidaPeriodo = 0.0;//CANTIDAD PERCIBIDA EN EL PERIODO JUB PENS ...
    private double montoTotalPagoUnaExhibi = 0.0;//MONTO TOTAL DEL PAGO EN UNA SOLA EXHIBICION
    private int numeroDias = 0;//NUMERO DE DIAS
    private double ingresosExcentos = 0.0;//INGRESOS EXCENTOS
    private double ingresosGravables = 0.0;//INGRESOS GRAVABLES
    private double ingresosAcomulables = 0.0;//INGRESOS ACUMULABLES
    private double ingresosNoAcomulables = 0.0;//INGRESOS NO ACUMULABLES
    private double impRetenido = 0.0;//IMP RETENIDO
    private double montoTotalOtrosPagosSep = 0.0;//MONTO TOTAL OTROS PAGOS POR SEPARACION
    private int numeroAniosServicio = 0;//NUMERO DE AÑOS DE SERVICIO
    private double ingresosExcentos1 = 0.0;//INGRESOS EXCENTOS
    private double ingresosGravados = 0.0;//INGRESOS GRAVADOS
    private double acomulablesUltimoSdoMensual = 0.0;//ING ACUMULABLES ULTIMO SDO MENSUAL
    private double correspUltimoSdoMensual = 0.0;//IMP CORRESP AL ULTIMO SDO MENSUAL
    private double noAcomulables = 0.0;//ING NO ACUMULABLES
    private double impRetenido1 = 0.0;//IMP RETENIDO
    private double ingresosAsimiladosSalarios = 0.0;//Ingresos asimilados a salarios
    private double impuestoRetenidoEjercicio = 0.0;//IMPUESTO RETENIDO DURANTE EL EJERCICIO
    private int opcEmpleadorAdquirirTitulos = 0;//INDIQUE SI EJERCICO LA OPCION DEL EMPLEADOR PARA ADQUIRIR TITULOS VALOR 1= SI 2 = NO
    private double valorMercadoAcionesAddTitulos = 0.0;//VALOR DE MERCADO DE LAS ACCIONES AL ADQUIRIR TITULOS VALOR
    private double precEstablIngreTitulos = 0.0; //PRECIO ESTABLECIDO AL OTORGARSE LA OPCION DE INGRESOS EN OPCIONES DE TITULOS VALOR
    private double ingresoAcomulable = 0.0;//INGRESO ACUMULABLE
    private double impuestoRetenidoEjercicio1 = 0.0;//IMPUESTO RETENDIDO DURANTE EL EJERCICIO
    private double sueldoSalariosRayasJornales = 0.0;//Sueldos, salarios, rayas y jornales
    private double sueldoExcento = 0.0;//SUELDOS EXCENTO
    private double gratificacionAnual = 0.0;//Gratificación anual
    private double aguinaldoExento = 0.0;//AGUINALDO EXENTO $1,577.70
    private double viaticosGastosViaje = 0.0;//Viáticos y gastos de viaje
    private double viaticosGastosViaje1 = 0.0;//Viáticos y gastos de viaje
    private double tiempoExtraOrdinario = 0.0;//Tiempo extraordinario
    private double tiempoExtraExento = 0.0;//TIEMPO EXTRA EXENTO
    private double primaVacacional = 0.0;//Prima vacacional
    private double primaVacaExento = 0.0;//PRIMA VAC. EXENTO $788.85
    private double primaDominical = 0.0;//Prima dominical
    private double primaDominical1 = 0.0;//Prima dominical
    private double participacionTrabajUtilidades = 0.0;//Participación de los trabajadores en las utilidades (PTU)
    private double ptuExenta = 0.0;//PTU EXENTA
    private double reembolsoGastMedDentHosp = 0.0;//Reembolso de gastos médicos, dentales y hospitalarios
    private double reembolsoGastMedDentHosp1 = 0.0;//Reembolso de gastos médicos, dentales y hospitalarios
    private double fondoAhorro = 0.0;//Fondo de ahorro
    private double fondoAhorro1 = 0.0;//Fondo de ahorro
    private double cajaAhorro = 0.0;//Caja de ahorro
    private double cajaAhorro1 = 0.0;//Caja de ahorro
    private double valesDespensa = 0.0;//Vales para despensa
    private double valesDespensa1 = 0.0;//Vales para despensa
    private double ayudaGastosFuneral = 0.0;//Ayuda para gastos de funeral
    private double ayudaGastosFuneral1 = 0.0;//Ayuda para gastos de funeral
    private double contibuCargoTrabajPagPatron = 0.0;//Contribuciones a cargo del trabajador pagadas por el patrón
    private double contibuCargoTrabajPagPatron1 = 0.0;//Contribuciones a cargo del trabajador pagadas por el patrón
    private double premioPuntualidad = 0.0;//Premios por puntualidad
    private double premioPuntualidad1 = 0.0;//Premios por puntualidad
    private double primaSeguroVida = 0.0;//Prima de seguro de vida
    private double primaSeguroVida1 = 0.0;//Prima de seguro de vida
    private double seguroGastosMedicosMayores = 0.0;//Seguro de gastos médicos mayores
    private double seguroGastosMedicosMayores1 = 0.0;//Seguro de gastos médicos mayores
    private double valesRestaurante = 0.0;//Vales para restaurante
    private double valesRestaurante1 = 0.0;//Vales para restaurante
    private double valesGasolina = 0.0;//Vales para gasolina
    private double valesGasolina1 = 0.0;//Vales para gasolina
    private double valesRopa = 0.0;//Vales para ropa
    private double valesRopa1 = 0.0;//Vales para ropa
    private double ayudaRenta = 0.0;//Ayuda para renta
    private double ayudaRenta1 = 0.0;//Ayuda para renta
    private double ayudaArticulosEscolares = 0.0;//Ayuda para artículos escolares
    private double ayudaArticulosEscolares1 = 0.0;//Ayuda para artículos escolares
    private double ayudaAnteojos = 0.0;//Dotación o ayuda para anteojos
    private double ayudaAnteojos1 = 0.0;//Dotación o ayuda para anteojos
    private double ayudaTransporte = 0.0;//Ayuda para transporte
    private double ayudaTransporte1 = 0.0;//Ayuda para transporte
    private double cuotasSindicalesPagadasPatron = 0.0;//Cuotas sindicales pagadas por el patrón
    private double cuotasSindicalesPagadasPatron1 = 0.0;//Cuotas sindicales pagadas por el patrón
    private double subsidioIncapacidad = 0.0;//Subsidios por incapacidad
    private double subsidioIncapacidad1 = 0.0;//Subsidios por incapacidad
    private double becasTrabajadoresHijos = 0.0;//Becas para trabajadores y/o sus hijos
    private double becasTrabajadoresHijos1 = 0.0;//Becas para trabajadores y/o sus hijos
    private double pagoOtroEmpleador = 0.0;//Pagos efectuados por otros empleadores (sólo si el patrón que declara realizó cálculo anual)
    private double pagoOtroEmpleador1 = 0.0;//Pagos efectuados por otros empleadores (sólo si el patrón que declara realizó cálculo anual)
    private double otrosIngresosSalarios = 0.0;//Otros ingresos por salarios
    private double otrosIngresosSalarios1 = 0.0;//Otros ingresos por salarios
    private double sumaIngresoGravadiSuelSal = 0.0;//Suma del ingreso GRAVADO por sueldos y salarios
    private double sumaIngresoExentoSuieldSal = 0.0;//Suma del ingreso EXENTO por sueldos y salariosING EXC
    private double impuestoRetenEjercicio = 0.0;//Impuesto retenido durante el ejercicio que declara
    private double impuestoRetenOtrosPatronEjer = 0.0;//Impuesto retenido por otro(s) patrón(es) durante el ejercicio que declara
    private double saldoFavorEjercPatrCompSiguiEjer = 0.0;//Saldo a favor determinado en el ejercicio que declara, que el patrón compensará durante el siguiente ejercicio o solicitará su devolución
    private double saldoFavorEjercAntNoComp = 0.0;//Saldo a favor del ejercicio anterior no compensado durante el ejercicio que declara
    private double sumaCantConcCredit = 0.0;//Suma de las cantidades que por concepto de crédito al salario le correspondió al trabajador
    private double creditoSalarioEntrEfecTrabEje = 0.0;//Crédito al salario entregado en efectivo al trabajador durante el ejercicio que declara
    private double montoTotalIngrObtenPrevisionSocial = 0.0;//Monto total de ingresos obtenidos por concepto de prestaciones de previsión social
    private double sumaIngreExcentosConceptoPresta = 0.0;//Suma de ingresos exentos por concepto de prestaciones de previsión social
    private double sumaIngresosSueldosSalarios = 0.0;//Suma de ingresos por sueldos y salarios
    private double montoImpuestoLocPrestServSocial = 0.0;//Monto del impuesto local a los ingresos por sueldos y salarios y en general por la prestación de un servicio personal subordinado retenido
    private double montoSubEmplEfectTrabEjer = 0.0;//Monto del subsidio para el empleo entregado en efectivo al trabajador durante el ejercicio que declara
    private double totalAportVoluntDedu = 0.0;//TOTAL DE LAS APORTACIONES VOLUNTARIAS DEDUCIBLES
    private double isrTarifAnual = 0.0;//ISR conforme a la tarifa anual
    private double subsAcreditable = 0.0;//Subsidio acreditable
    private double subsNoAcreditable = 0.0;//Subsidio no acreditable
    private double impuestoIngresosAcomulables = 0.0;//Impuesto sobre ingresos acumulables
    private double impuestoIngresosNoAcomulables = 0.0;//Impuesto sobre ingresos no acumulables
    private double subsidioEntregadoTrabajador = 0.0;//Subsidio para el empleo entregado al trabajador
    private double subsidioNivelacionIngresoEntregado = 0.0;//Subsidio para la nivelación del ingreso entregado al trabajador

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getMesFinal() {
        return mesFinal;
    }

    public void setMesFinal(int mesFinal) {
        this.mesFinal = mesFinal;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public int getAreaGeo() {
        return areaGeo;
    }

    public void setAreaGeo(int areaGeo) {
        this.areaGeo = areaGeo;
    }

    public int getCalAnual() {
        return calAnual;
    }

    public void setCalAnual(int calAnual) {
        this.calAnual = calAnual;
    }

    public int getTarifa() {
        return tarifa;
    }

    public void setTarifa(int tarifa) {
        this.tarifa = tarifa;
    }

    public int getTarifa91() {
        return tarifa91;
    }

    public void setTarifa91(int tarifa91) {
        this.tarifa91 = tarifa91;
    }

    public double getProporc() {
        return proporc;
    }

    public void setProporc(double proporc) {
        this.proporc = proporc;
    }

    public int getSindicato() {
        return sindicato;
    }

    public void setSindicato(int sindicato) {
        this.sindicato = sindicato;
    }

    public int getAsimilado() {
        return asimilado;
    }

    public void setAsimilado(int asimilado) {
        this.asimilado = asimilado;
    }

    public int getEntidad() {
        return entidad;
    }

    public void setEntidad(int entidad) {
        this.entidad = entidad;
    }

    public String getRfc1Pat() {
        return rfc1Pat;
    }

    public void setRfc1Pat(String rfc1Pat) {
        this.rfc1Pat = rfc1Pat;
    }

    public String getRfc2pat() {
        return rfc2pat;
    }

    public void setRfc2pat(String rfc2pat) {
        this.rfc2pat = rfc2pat;
    }

    public String getRfc3pat() {
        return rfc3pat;
    }

    public void setRfc3pat(String rfc3pat) {
        this.rfc3pat = rfc3pat;
    }

    public String getRfc4pat() {
        return rfc4pat;
    }

    public void setRfc4pat(String rfc4pat) {
        this.rfc4pat = rfc4pat;
    }

    public String getRfc5pat() {
        return rfc5pat;
    }

    public void setRfc5pat(String rfc5pat) {
        this.rfc5pat = rfc5pat;
    }

    public String getRfc6pat() {
        return rfc6pat;
    }

    public void setRfc6pat(String rfc6pat) {
        this.rfc6pat = rfc6pat;
    }

    public String getRfc7pat() {
        return rfc7pat;
    }

    public void setRfc7pat(String rfc7pat) {
        this.rfc7pat = rfc7pat;
    }

    public String getRfc8pat() {
        return rfc8pat;
    }

    public void setRfc8pat(String rfc8pat) {
        this.rfc8pat = rfc8pat;
    }

    public String getRfc9pat() {
        return rfc9pat;
    }

    public void setRfc9pat(String rfc9pat) {
        this.rfc9pat = rfc9pat;
    }

    public String getRfc10pat() {
        return rfc10pat;
    }

    public void setRfc10pat(String rfc10pat) {
        this.rfc10pat = rfc10pat;
    }

    public double getMontoAportacionesVoluntarias() {
        return montoAportacionesVoluntarias;
    }

    public void setMontoAportacionesVoluntarias(double montoAportacionesVoluntarias) {
        this.montoAportacionesVoluntarias = montoAportacionesVoluntarias;
    }

    public String getAplicoMontoAportVoluntarioas() {
        return aplicoMontoAportVoluntarioas;
    }

    public void setAplicoMontoAportVoluntarioas(String aplicoMontoAportVoluntarioas) {
        this.aplicoMontoAportVoluntarioas = aplicoMontoAportVoluntarioas;
    }

    public double getMontoAportDeduciblesTrabDecl() {
        return montoAportDeduciblesTrabDecl;
    }

    public void setMontoAportDeduciblesTrabDecl(double montoAportDeduciblesTrabDecl) {
        this.montoAportDeduciblesTrabDecl = montoAportDeduciblesTrabDecl;
    }

    public double getMontoAportVoluDeduPatron() {
        return montoAportVoluDeduPatron;
    }

    public void setMontoAportVoluDeduPatron(double montoAportVoluDeduPatron) {
        this.montoAportVoluDeduPatron = montoAportVoluDeduPatron;
    }

    public int getPagosSeparacion() {
        return pagosSeparacion;
    }

    public void setPagosSeparacion(int pagosSeparacion) {
        this.pagosSeparacion = pagosSeparacion;
    }

    public int getAsimiladosSalarios() {
        return asimiladosSalarios;
    }

    public void setAsimiladosSalarios(int asimiladosSalarios) {
        this.asimiladosSalarios = asimiladosSalarios;
    }

    public int getPagosEfectuadosTrabajadores() {
        return pagosEfectuadosTrabajadores;
    }

    public void setPagosEfectuadosTrabajadores(int pagosEfectuadosTrabajadores) {
        this.pagosEfectuadosTrabajadores = pagosEfectuadosTrabajadores;
    }

    public double getTotalesPagosParciales() {
        return totalesPagosParciales;
    }

    public void setTotalesPagosParciales(double totalesPagosParciales) {
        this.totalesPagosParciales = totalesPagosParciales;
    }

    public double getMontoDiarioJubilaciones() {
        return montoDiarioJubilaciones;
    }

    public void setMontoDiarioJubilaciones(double montoDiarioJubilaciones) {
        this.montoDiarioJubilaciones = montoDiarioJubilaciones;
    }

    public double getCantidadPercibidaPeriodo() {
        return cantidadPercibidaPeriodo;
    }

    public void setCantidadPercibidaPeriodo(double cantidadPercibidaPeriodo) {
        this.cantidadPercibidaPeriodo = cantidadPercibidaPeriodo;
    }

    public double getMontoTotalPagoUnaExhibi() {
        return montoTotalPagoUnaExhibi;
    }

    public void setMontoTotalPagoUnaExhibi(double montoTotalPagoUnaExhibi) {
        this.montoTotalPagoUnaExhibi = montoTotalPagoUnaExhibi;
    }

    public int getNumeroDias() {
        return numeroDias;
    }

    public void setNumeroDias(int numeroDias) {
        this.numeroDias = numeroDias;
    }

    public double getIngresosExcentos() {
        return ingresosExcentos;
    }

    public void setIngresosExcentos(double ingresosExcentos) {
        this.ingresosExcentos = ingresosExcentos;
    }

    public double getIngresosGravables() {
        return ingresosGravables;
    }

    public void setIngresosGravables(double ingresosGravables) {
        this.ingresosGravables = ingresosGravables;
    }

    public double getIngresosAcomulables() {
        return ingresosAcomulables;
    }

    public void setIngresosAcomulables(double ingresosAcomulables) {
        this.ingresosAcomulables = ingresosAcomulables;
    }

    public double getIngresosNoAcomulables() {
        return ingresosNoAcomulables;
    }

    public void setIngresosNoAcomulables(double ingresosNoAcomulables) {
        this.ingresosNoAcomulables = ingresosNoAcomulables;
    }

    public double getImpRetenido() {
        return impRetenido;
    }

    public void setImpRetenido(double impRetenido) {
        this.impRetenido = impRetenido;
    }

    public double getMontoTotalOtrosPagosSep() {
        return montoTotalOtrosPagosSep;
    }

    public void setMontoTotalOtrosPagosSep(double montoTotalOtrosPagosSep) {
        this.montoTotalOtrosPagosSep = montoTotalOtrosPagosSep;
    }

    public int getNumeroAniosServicio() {
        return numeroAniosServicio;
    }

    public void setNumeroAniosServicio(int numeroAniosServicio) {
        this.numeroAniosServicio = numeroAniosServicio;
    }

    public double getIngresosExcentos1() {
        return ingresosExcentos1;
    }

    public void setIngresosExcentos1(double ingresosExcentos1) {
        this.ingresosExcentos1 = ingresosExcentos1;
    }

    public double getIngresosGravados() {
        return ingresosGravados;
    }

    public void setIngresosGravados(double ingresosGravados) {
        this.ingresosGravados = ingresosGravados;
    }

    public double getAcomulablesUltimoSdoMensual() {
        return acomulablesUltimoSdoMensual;
    }

    public void setAcomulablesUltimoSdoMensual(double acomulablesUltimoSdoMensual) {
        this.acomulablesUltimoSdoMensual = acomulablesUltimoSdoMensual;
    }

    public double getCorrespUltimoSdoMensual() {
        return correspUltimoSdoMensual;
    }

    public void setCorrespUltimoSdoMensual(double correspUltimoSdoMensual) {
        this.correspUltimoSdoMensual = correspUltimoSdoMensual;
    }

    public double getNoAcomulables() {
        return noAcomulables;
    }

    public void setNoAcomulables(double noAcomulables) {
        this.noAcomulables = noAcomulables;
    }

    public double getImpRetenido1() {
        return impRetenido1;
    }

    public void setImpRetenido1(double impRetenido1) {
        this.impRetenido1 = impRetenido1;
    }

    public double getIngresosAsimiladosSalarios() {
        return ingresosAsimiladosSalarios;
    }

    public void setIngresosAsimiladosSalarios(double ingresosAsimiladosSalarios) {
        this.ingresosAsimiladosSalarios = ingresosAsimiladosSalarios;
    }

    public double getImpuestoRetenidoEjercicio() {
        return impuestoRetenidoEjercicio;
    }

    public void setImpuestoRetenidoEjercicio(double impuestoRetenidoEjercicio) {
        this.impuestoRetenidoEjercicio = impuestoRetenidoEjercicio;
    }

    public int getOpcEmpleadorAdquirirTitulos() {
        return opcEmpleadorAdquirirTitulos;
    }

    public void setOpcEmpleadorAdquirirTitulos(int opcEmpleadorAdquirirTitulos) {
        this.opcEmpleadorAdquirirTitulos = opcEmpleadorAdquirirTitulos;
    }

    public double getValorMercadoAcionesAddTitulos() {
        return valorMercadoAcionesAddTitulos;
    }

    public void setValorMercadoAcionesAddTitulos(double valorMercadoAcionesAddTitulos) {
        this.valorMercadoAcionesAddTitulos = valorMercadoAcionesAddTitulos;
    }

    public double getPrecEstablIngreTitulos() {
        return precEstablIngreTitulos;
    }

    public void setPrecEstablIngreTitulos(double precEstablIngreTitulos) {
        this.precEstablIngreTitulos = precEstablIngreTitulos;
    }

    public double getIngresoAcomulable() {
        return ingresoAcomulable;
    }

    public void setIngresoAcomulable(double ingresoAcomulable) {
        this.ingresoAcomulable = ingresoAcomulable;
    }

    public double getImpuestoRetenidoEjercicio1() {
        return impuestoRetenidoEjercicio1;
    }

    public void setImpuestoRetenidoEjercicio1(double impuestoRetenidoEjercicio1) {
        this.impuestoRetenidoEjercicio1 = impuestoRetenidoEjercicio1;
    }

    public double getSueldoSalariosRayasJornales() {
        return sueldoSalariosRayasJornales;
    }

    public void setSueldoSalariosRayasJornales(double sueldoSalariosRayasJornales) {
        this.sueldoSalariosRayasJornales = sueldoSalariosRayasJornales;
    }

    public double getSueldoExcento() {
        return sueldoExcento;
    }

    public void setSueldoExcento(double sueldoExcento) {
        this.sueldoExcento = sueldoExcento;
    }

    public double getGratificacionAnual() {
        return gratificacionAnual;
    }

    public void setGratificacionAnual(double gratificacionAnual) {
        this.gratificacionAnual = gratificacionAnual;
    }

    public double getAguinaldoExento() {
        return aguinaldoExento;
    }

    public void setAguinaldoExento(double aguinaldoExento) {
        this.aguinaldoExento = aguinaldoExento;
    }

    public double getViaticosGastosViaje() {
        return viaticosGastosViaje;
    }

    public void setViaticosGastosViaje(double viaticosGastosViaje) {
        this.viaticosGastosViaje = viaticosGastosViaje;
    }

    public double getViaticosGastosViaje1() {
        return viaticosGastosViaje1;
    }

    public void setViaticosGastosViaje1(double viaticosGastosViaje1) {
        this.viaticosGastosViaje1 = viaticosGastosViaje1;
    }

    public double getTiempoExtraOrdinario() {
        return tiempoExtraOrdinario;
    }

    public void setTiempoExtraOrdinario(double tiempoExtraOrdinario) {
        this.tiempoExtraOrdinario = tiempoExtraOrdinario;
    }

    public double getTiempoExtraExento() {
        return tiempoExtraExento;
    }

    public void setTiempoExtraExento(double tiempoExtraExento) {
        this.tiempoExtraExento = tiempoExtraExento;
    }

    public double getPrimaVacacional() {
        return primaVacacional;
    }

    public void setPrimaVacacional(double primaVacacional) {
        this.primaVacacional = primaVacacional;
    }

    public double getPrimaVacaExento() {
        return primaVacaExento;
    }

    public void setPrimaVacaExento(double primaVacaExento) {
        this.primaVacaExento = primaVacaExento;
    }

    public double getPrimaDominical() {
        return primaDominical;
    }

    public void setPrimaDominical(double primaDominical) {
        this.primaDominical = primaDominical;
    }

    public double getPrimaDominical1() {
        return primaDominical1;
    }

    public void setPrimaDominical1(double primaDominical1) {
        this.primaDominical1 = primaDominical1;
    }

    public double getParticipacionTrabajUtilidades() {
        return participacionTrabajUtilidades;
    }

    public void setParticipacionTrabajUtilidades(double participacionTrabajUtilidades) {
        this.participacionTrabajUtilidades = participacionTrabajUtilidades;
    }

    public double getPtuExenta() {
        return ptuExenta;
    }

    public void setPtuExenta(double ptuExenta) {
        this.ptuExenta = ptuExenta;
    }

    public double getReembolsoGastMedDentHosp() {
        return reembolsoGastMedDentHosp;
    }

    public void setReembolsoGastMedDentHosp(double reembolsoGastMedDentHosp) {
        this.reembolsoGastMedDentHosp = reembolsoGastMedDentHosp;
    }

    public double getReembolsoGastMedDentHosp1() {
        return reembolsoGastMedDentHosp1;
    }

    public void setReembolsoGastMedDentHosp1(double reembolsoGastMedDentHosp1) {
        this.reembolsoGastMedDentHosp1 = reembolsoGastMedDentHosp1;
    }

    public double getFondoAhorro() {
        return fondoAhorro;
    }

    public void setFondoAhorro(double fondoAhorro) {
        this.fondoAhorro = fondoAhorro;
    }

    public double getFondoAhorro1() {
        return fondoAhorro1;
    }

    public void setFondoAhorro1(double fondoAhorro1) {
        this.fondoAhorro1 = fondoAhorro1;
    }

    public double getCajaAhorro() {
        return cajaAhorro;
    }

    public void setCajaAhorro(double cajaAhorro) {
        this.cajaAhorro = cajaAhorro;
    }

    public double getCajaAhorro1() {
        return cajaAhorro1;
    }

    public void setCajaAhorro1(double cajaAhorro1) {
        this.cajaAhorro1 = cajaAhorro1;
    }

    public double getValesDespensa() {
        return valesDespensa;
    }

    public void setValesDespensa(double valesDespensa) {
        this.valesDespensa = valesDespensa;
    }

    public double getValesDespensa1() {
        return valesDespensa1;
    }

    public void setValesDespensa1(double valesDespensa1) {
        this.valesDespensa1 = valesDespensa1;
    }

    public double getAyudaGastosFuneral() {
        return ayudaGastosFuneral;
    }

    public void setAyudaGastosFuneral(double ayudaGastosFuneral) {
        this.ayudaGastosFuneral = ayudaGastosFuneral;
    }

    public double getAyudaGastosFuneral1() {
        return ayudaGastosFuneral1;
    }

    public void setAyudaGastosFuneral1(double ayudaGastosFuneral1) {
        this.ayudaGastosFuneral1 = ayudaGastosFuneral1;
    }

    public double getContibuCargoTrabajPagPatron() {
        return contibuCargoTrabajPagPatron;
    }

    public void setContibuCargoTrabajPagPatron(double contibuCargoTrabajPagPatron) {
        this.contibuCargoTrabajPagPatron = contibuCargoTrabajPagPatron;
    }

    public double getContibuCargoTrabajPagPatron1() {
        return contibuCargoTrabajPagPatron1;
    }

    public void setContibuCargoTrabajPagPatron1(double contibuCargoTrabajPagPatron1) {
        this.contibuCargoTrabajPagPatron1 = contibuCargoTrabajPagPatron1;
    }

    public double getPremioPuntualidad() {
        return premioPuntualidad;
    }

    public void setPremioPuntualidad(double premioPuntualidad) {
        this.premioPuntualidad = premioPuntualidad;
    }

    public double getPremioPuntualidad1() {
        return premioPuntualidad1;
    }

    public void setPremioPuntualidad1(double premioPuntualidad1) {
        this.premioPuntualidad1 = premioPuntualidad1;
    }

    public double getPrimaSeguroVida() {
        return primaSeguroVida;
    }

    public void setPrimaSeguroVida(double primaSeguroVida) {
        this.primaSeguroVida = primaSeguroVida;
    }

    public double getPrimaSeguroVida1() {
        return primaSeguroVida1;
    }

    public void setPrimaSeguroVida1(double primaSeguroVida1) {
        this.primaSeguroVida1 = primaSeguroVida1;
    }

    public double getSeguroGastosMedicosMayores() {
        return seguroGastosMedicosMayores;
    }

    public void setSeguroGastosMedicosMayores(double seguroGastosMedicosMayores) {
        this.seguroGastosMedicosMayores = seguroGastosMedicosMayores;
    }

    public double getSeguroGastosMedicosMayores1() {
        return seguroGastosMedicosMayores1;
    }

    public void setSeguroGastosMedicosMayores1(double seguroGastosMedicosMayores1) {
        this.seguroGastosMedicosMayores1 = seguroGastosMedicosMayores1;
    }

    public double getValesRestaurante() {
        return valesRestaurante;
    }

    public void setValesRestaurante(double valesRestaurante) {
        this.valesRestaurante = valesRestaurante;
    }

    public double getValesRestaurante1() {
        return valesRestaurante1;
    }

    public void setValesRestaurante1(double valesRestaurante1) {
        this.valesRestaurante1 = valesRestaurante1;
    }

    public double getValesGasolina() {
        return valesGasolina;
    }

    public void setValesGasolina(double valesGasolina) {
        this.valesGasolina = valesGasolina;
    }

    public double getValesGasolina1() {
        return valesGasolina1;
    }

    public void setValesGasolina1(double valesGasolina1) {
        this.valesGasolina1 = valesGasolina1;
    }

    public double getValesRopa() {
        return valesRopa;
    }

    public void setValesRopa(double valesRopa) {
        this.valesRopa = valesRopa;
    }

    public double getValesRopa1() {
        return valesRopa1;
    }

    public void setValesRopa1(double valesRopa1) {
        this.valesRopa1 = valesRopa1;
    }

    public double getAyudaRenta() {
        return ayudaRenta;
    }

    public void setAyudaRenta(double ayudaRenta) {
        this.ayudaRenta = ayudaRenta;
    }

    public double getAyudaRenta1() {
        return ayudaRenta1;
    }

    public void setAyudaRenta1(double ayudaRenta1) {
        this.ayudaRenta1 = ayudaRenta1;
    }

    public double getAyudaArticulosEscolares() {
        return ayudaArticulosEscolares;
    }

    public void setAyudaArticulosEscolares(double ayudaArticulosEscolares) {
        this.ayudaArticulosEscolares = ayudaArticulosEscolares;
    }

    public double getAyudaArticulosEscolares1() {
        return ayudaArticulosEscolares1;
    }

    public void setAyudaArticulosEscolares1(double ayudaArticulosEscolares1) {
        this.ayudaArticulosEscolares1 = ayudaArticulosEscolares1;
    }

    public double getAyudaAnteojos() {
        return ayudaAnteojos;
    }

    public void setAyudaAnteojos(double ayudaAnteojos) {
        this.ayudaAnteojos = ayudaAnteojos;
    }

    public double getAyudaAnteojos1() {
        return ayudaAnteojos1;
    }

    public void setAyudaAnteojos1(double ayudaAnteojos1) {
        this.ayudaAnteojos1 = ayudaAnteojos1;
    }

    public double getAyudaTransporte() {
        return ayudaTransporte;
    }

    public void setAyudaTransporte(double ayudaTransporte) {
        this.ayudaTransporte = ayudaTransporte;
    }

    public double getAyudaTransporte1() {
        return ayudaTransporte1;
    }

    public void setAyudaTransporte1(double ayudaTransporte1) {
        this.ayudaTransporte1 = ayudaTransporte1;
    }

    public double getCuotasSindicalesPagadasPatron() {
        return cuotasSindicalesPagadasPatron;
    }

    public void setCuotasSindicalesPagadasPatron(double cuotasSindicalesPagadasPatron) {
        this.cuotasSindicalesPagadasPatron = cuotasSindicalesPagadasPatron;
    }

    public double getCuotasSindicalesPagadasPatron1() {
        return cuotasSindicalesPagadasPatron1;
    }

    public void setCuotasSindicalesPagadasPatron1(double cuotasSindicalesPagadasPatron1) {
        this.cuotasSindicalesPagadasPatron1 = cuotasSindicalesPagadasPatron1;
    }

    public double getSubsidioIncapacidad() {
        return subsidioIncapacidad;
    }

    public void setSubsidioIncapacidad(double subsidioIncapacidad) {
        this.subsidioIncapacidad = subsidioIncapacidad;
    }

    public double getSubsidioIncapacidad1() {
        return subsidioIncapacidad1;
    }

    public void setSubsidioIncapacidad1(double subsidioIncapacidad1) {
        this.subsidioIncapacidad1 = subsidioIncapacidad1;
    }

    public double getBecasTrabajadoresHijos() {
        return becasTrabajadoresHijos;
    }

    public void setBecasTrabajadoresHijos(double becasTrabajadoresHijos) {
        this.becasTrabajadoresHijos = becasTrabajadoresHijos;
    }

    public double getBecasTrabajadoresHijos1() {
        return becasTrabajadoresHijos1;
    }

    public void setBecasTrabajadoresHijos1(double becasTrabajadoresHijos1) {
        this.becasTrabajadoresHijos1 = becasTrabajadoresHijos1;
    }

    public double getPagoOtroEmpleador() {
        return pagoOtroEmpleador;
    }

    public void setPagoOtroEmpleador(double pagoOtroEmpleador) {
        this.pagoOtroEmpleador = pagoOtroEmpleador;
    }

    public double getPagoOtroEmpleador1() {
        return pagoOtroEmpleador1;
    }

    public void setPagoOtroEmpleador1(double pagoOtroEmpleador1) {
        this.pagoOtroEmpleador1 = pagoOtroEmpleador1;
    }

    public double getOtrosIngresosSalarios() {
        return otrosIngresosSalarios;
    }

    public void setOtrosIngresosSalarios(double otrosIngresosSalarios) {
        this.otrosIngresosSalarios = otrosIngresosSalarios;
    }

    public double getOtrosIngresosSalarios1() {
        return otrosIngresosSalarios1;
    }

    public void setOtrosIngresosSalarios1(double otrosIngresosSalarios1) {
        this.otrosIngresosSalarios1 = otrosIngresosSalarios1;
    }

    public double getSumaIngresoGravadiSuelSal() {
        return sumaIngresoGravadiSuelSal;
    }

    public void setSumaIngresoGravadiSuelSal(double sumaIngresoGravadiSuelSal) {
        this.sumaIngresoGravadiSuelSal = sumaIngresoGravadiSuelSal;
    }

    public double getSumaIngresoExentoSuieldSal() {
        return sumaIngresoExentoSuieldSal;
    }

    public void setSumaIngresoExentoSuieldSal(double sumaIngresoExentoSuieldSal) {
        this.sumaIngresoExentoSuieldSal = sumaIngresoExentoSuieldSal;
    }

    public double getImpuestoRetenEjercicio() {
        return impuestoRetenEjercicio;
    }

    public void setImpuestoRetenEjercicio(double impuestoRetenEjercicio) {
        this.impuestoRetenEjercicio = impuestoRetenEjercicio;
    }

    public double getImpuestoRetenOtrosPatronEjer() {
        return impuestoRetenOtrosPatronEjer;
    }

    public void setImpuestoRetenOtrosPatronEjer(double impuestoRetenOtrosPatronEjer) {
        this.impuestoRetenOtrosPatronEjer = impuestoRetenOtrosPatronEjer;
    }

    public double getSaldoFavorEjercPatrCompSiguiEjer() {
        return saldoFavorEjercPatrCompSiguiEjer;
    }

    public void setSaldoFavorEjercPatrCompSiguiEjer(double saldoFavorEjercPatrCompSiguiEjer) {
        this.saldoFavorEjercPatrCompSiguiEjer = saldoFavorEjercPatrCompSiguiEjer;
    }

    public double getSaldoFavorEjercAntNoComp() {
        return saldoFavorEjercAntNoComp;
    }

    public void setSaldoFavorEjercAntNoComp(double saldoFavorEjercAntNoComp) {
        this.saldoFavorEjercAntNoComp = saldoFavorEjercAntNoComp;
    }

    public double getSumaCantConcCredit() {
        return sumaCantConcCredit;
    }

    public void setSumaCantConcCredit(double sumaCantConcCredit) {
        this.sumaCantConcCredit = sumaCantConcCredit;
    }

    public double getCreditoSalarioEntrEfecTrabEje() {
        return creditoSalarioEntrEfecTrabEje;
    }

    public void setCreditoSalarioEntrEfecTrabEje(double creditoSalarioEntrEfecTrabEje) {
        this.creditoSalarioEntrEfecTrabEje = creditoSalarioEntrEfecTrabEje;
    }

    public double getMontoTotalIngrObtenPrevisionSocial() {
        return montoTotalIngrObtenPrevisionSocial;
    }

    public void setMontoTotalIngrObtenPrevisionSocial(double montoTotalIngrObtenPrevisionSocial) {
        this.montoTotalIngrObtenPrevisionSocial = montoTotalIngrObtenPrevisionSocial;
    }

    public double getSumaIngreExcentosConceptoPresta() {
        return sumaIngreExcentosConceptoPresta;
    }

    public void setSumaIngreExcentosConceptoPresta(double sumaIngreExcentosConceptoPresta) {
        this.sumaIngreExcentosConceptoPresta = sumaIngreExcentosConceptoPresta;
    }

    public double getSumaIngresosSueldosSalarios() {
        return sumaIngresosSueldosSalarios;
    }

    public void setSumaIngresosSueldosSalarios(double sumaIngresosSueldosSalarios) {
        this.sumaIngresosSueldosSalarios = sumaIngresosSueldosSalarios;
    }

    public double getMontoImpuestoLocPrestServSocial() {
        return montoImpuestoLocPrestServSocial;
    }

    public void setMontoImpuestoLocPrestServSocial(double montoImpuestoLocPrestServSocial) {
        this.montoImpuestoLocPrestServSocial = montoImpuestoLocPrestServSocial;
    }

    public double getMontoSubEmplEfectTrabEjer() {
        return montoSubEmplEfectTrabEjer;
    }

    public void setMontoSubEmplEfectTrabEjer(double montoSubEmplEfectTrabEjer) {
        this.montoSubEmplEfectTrabEjer = montoSubEmplEfectTrabEjer;
    }

    public double getTotalAportVoluntDedu() {
        return totalAportVoluntDedu;
    }

    public void setTotalAportVoluntDedu(double totalAportVoluntDedu) {
        this.totalAportVoluntDedu = totalAportVoluntDedu;
    }

    public double getIsrTarifAnual() {
        return isrTarifAnual;
    }

    public void setIsrTarifAnual(double isrTarifAnual) {
        this.isrTarifAnual = isrTarifAnual;
    }

    public double getSubsAcreditable() {
        return subsAcreditable;
    }

    public void setSubsAcreditable(double subsAcreditable) {
        this.subsAcreditable = subsAcreditable;
    }

    public double getSubsNoAcreditable() {
        return subsNoAcreditable;
    }

    public void setSubsNoAcreditable(double subsNoAcreditable) {
        this.subsNoAcreditable = subsNoAcreditable;
    }

    public double getImpuestoIngresosAcomulables() {
        return impuestoIngresosAcomulables;
    }

    public void setImpuestoIngresosAcomulables(double impuestoIngresosAcomulables) {
        this.impuestoIngresosAcomulables = impuestoIngresosAcomulables;
    }

    public double getImpuestoIngresosNoAcomulables() {
        return impuestoIngresosNoAcomulables;
    }

    public void setImpuestoIngresosNoAcomulables(double impuestoIngresosNoAcomulables) {
        this.impuestoIngresosNoAcomulables = impuestoIngresosNoAcomulables;
    }

    public double getSubsidioEntregadoTrabajador() {
        return subsidioEntregadoTrabajador;
    }

    public void setSubsidioEntregadoTrabajador(double subsidioEntregadoTrabajador) {
        this.subsidioEntregadoTrabajador = subsidioEntregadoTrabajador;
    }

    public double getSubsidioNivelacionIngresoEntregado() {
        return subsidioNivelacionIngresoEntregado;
    }

    public void setSubsidioNivelacionIngresoEntregado(double subsidioNivelacionIngresoEntregado) {
        this.subsidioNivelacionIngresoEntregado = subsidioNivelacionIngresoEntregado;
    }
}
