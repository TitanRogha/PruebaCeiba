package com.ceiba.tiendatecnologica.dominio.servicio.garantia;


import com.ceiba.tiendatecnologica.dominio.GarantiaExtendida;
import com.ceiba.tiendatecnologica.dominio.Producto;
import com.ceiba.tiendatecnologica.dominio.excepcion.GarantiaExtendidaException;
import com.ceiba.tiendatecnologica.dominio.repositorio.RepositorioGarantiaExtendida;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

@Component
public class ServicioGenerarGarantia {

    private static final Logger logger = LoggerFactory.getLogger(ServicioGenerarGarantia.class);


    public static final String EL_PRODUCTO_TIENE_GARANTIA = "El producto ya cuenta con una garantía extendida";

    private RepositorioGarantiaExtendida repositorioGarantiaExtendida;

    public ServicioGenerarGarantia(RepositorioGarantiaExtendida repositorioGarantiaExtendida){
        this.repositorioGarantiaExtendida = repositorioGarantiaExtendida;
    }

    public boolean tieneGarantia(String codigo) {

        Producto producto = this.repositorioGarantiaExtendida.obtenerProductoConGarantiaPorCodigo(codigo);
        if(producto==null){
            return false;
        }
        return true;
    }

    public boolean tieneVocales(String codigo) {
        int contadorVocales = 0;
        codigo=codigo.toLowerCase();
        for(int x=0;x<codigo.length();x++) {
            if ((codigo.charAt(x)=='a') || (codigo.charAt(x)=='e') || (codigo.charAt(x)=='i') || (codigo.charAt(x)=='o') || (codigo.charAt(x)=='u')){
                contadorVocales++;
            }
        }
        if(contadorVocales>=3){
            return true;
        }
        return false;
    }

    public Double calcularPrecio(Double precioProducto){
        if(precioProducto>500000){
            return precioProducto*0.2;
        }
        return precioProducto*0.1;
    }
    public Date calcularFechaFinalizacion(Double precioProducto,Date fechaSolicitud) {
        Calendar fechaIni = Calendar.getInstance();
        Calendar fechaFin = Calendar.getInstance();
        fechaIni.setTime(fechaSolicitud);
        fechaFin.setTime(fechaSolicitud);
        int contadorLunes = 0;

        if (precioProducto > 500000) {
            fechaFin.add(Calendar.DAY_OF_YEAR, 199);

            for (Date date = fechaIni.getTime();fechaIni.before(fechaFin);fechaIni.add(Calendar.DATE,1),date = fechaIni.getTime() ){
                if (fechaIni.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                    contadorLunes--;
                }
            }

            fechaFin.add(Calendar.DAY_OF_YEAR, contadorLunes);
            if (fechaFin.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                fechaFin.add(Calendar.DATE,1);
            }

        }else {
            fechaFin.add(Calendar.DAY_OF_YEAR, 99);
        }
        return fechaFin.getTime();
    }

    public String ejecutar(GarantiaExtendida garantiaExtendida,String nombreCliente) {

        boolean tieneGarantia = tieneGarantia(garantiaExtendida.getProducto().getCodigo());
            if(tieneGarantia==true){
                logger.info("Hola");
                throw new GarantiaExtendidaException("El producto solicitado ya cuenta con garantia extendida");
        }

            boolean tieneVocales =tieneVocales(garantiaExtendida.getProducto().getCodigo());
            if(tieneVocales==true){

                throw new GarantiaExtendidaException("Este producto no cuenta con garantia extendida");
        }

        Double precioGarantia = calcularPrecio(garantiaExtendida.getProducto().getPrecio());
        Date fechaFinalizacion = calcularFechaFinalizacion(garantiaExtendida.getProducto().getPrecio(),garantiaExtendida.getFechaSolicitudGarantia());

        garantiaExtendida.setFechaFinGarantia(fechaFinalizacion);
        garantiaExtendida.setNombreCliente(nombreCliente);
        garantiaExtendida.setPrecio(precioGarantia);

        return this.repositorioGarantiaExtendida.agregar(garantiaExtendida);
    }

}
