package com.ceiba.tiendatecnologica.dominio.unitaria;

import com.ceiba.tiendatecnologica.dominio.GarantiaExtendida;
import com.ceiba.tiendatecnologica.dominio.Producto;
import com.ceiba.tiendatecnologica.dominio.excepcion.GarantiaExtendidaException;
import com.ceiba.tiendatecnologica.dominio.repositorio.RepositorioGarantiaExtendida;
import com.ceiba.tiendatecnologica.dominio.servicio.garantia.ServicioGenerarGarantia;
import com.ceiba.tiendatecnologica.dominio.servicio.vendedor.ServicioVendedor;
import com.ceiba.tiendatecnologica.testdatabuilder.GarantiaTestDataBuilder;
import com.ceiba.tiendatecnologica.testdatabuilder.ProductoTestDataBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.AdditionalMatchers.gt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioGenerarGarantiaTest {

    private static final Logger logger = LoggerFactory.getLogger(ServicioGenerarGarantia.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void ExcepcionAlGenerarGarantiaProductoConTresVocales() {
        GarantiaTestDataBuilder garantiaTestDataBuilder = new GarantiaTestDataBuilder();
        GarantiaExtendida garantiaExtendida = garantiaTestDataBuilder.build();
        RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);

        ServicioGenerarGarantia servicioGenerarGarantiaMock = mock(ServicioGenerarGarantia.class);

        when(servicioGenerarGarantiaMock.tieneVocales(garantiaExtendida.getProducto().getCodigo())).thenReturn(true);

        ServicioGenerarGarantia servicioGenerarGarantia = new ServicioGenerarGarantia(repositorioGarantia);
        try{
            servicioGenerarGarantia.ejecutar(garantiaExtendida,garantiaExtendida.getNombreCliente());
        }catch (Exception ex){
            assertEquals("Este producto no cuenta con garantia extendida",ex.getMessage());
        }

    }

    @Test
    public void ExcepcionAlGenerarGarantiaProductoConGarantiaExistente() {
        GarantiaTestDataBuilder garantiaTestDataBuilder = new GarantiaTestDataBuilder();
        GarantiaExtendida garantiaExtendida = garantiaTestDataBuilder.build();
        RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);

        ServicioGenerarGarantia servicioGenerarGarantiaMock = mock(ServicioGenerarGarantia.class);

        when(servicioGenerarGarantiaMock.tieneGarantia(garantiaExtendida.getProducto().getCodigo())).thenReturn(true);

        ServicioGenerarGarantia servicioGenerarGarantia = new ServicioGenerarGarantia(repositorioGarantia);
        try{
            servicioGenerarGarantia.ejecutar(garantiaExtendida,garantiaExtendida.getNombreCliente());
        }catch (Exception ex){
            assertEquals("El producto solicitado ya cuenta con garantia extendida",ex.getMessage());
        }

    }
    @Test
    public void PrecioYFechaFinalizacionGarantiaPrecioInferior(){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        GarantiaTestDataBuilder garantiaTestDataBuilder = new GarantiaTestDataBuilder();
        GarantiaExtendida garantiaExtendida = garantiaTestDataBuilder.build();
        RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
        ServicioGenerarGarantia servicioGenerarGarantia = new ServicioGenerarGarantia(repositorioGarantia);
        String fechaSolicitud=format.format(garantiaExtendida.getFechaSolicitudGarantia());

        if(garantiaExtendida.getProducto().getPrecio()>500000) {
            Double precioGarantia=servicioGenerarGarantia.calcularPrecio(garantiaExtendida.getProducto().getPrecio());
            Date fechaFinalizacion=servicioGenerarGarantia.calcularFechaFinalizacion(garantiaExtendida.getProducto().getPrecio(),garantiaExtendida.getFechaSolicitudGarantia());
            String fechaFin=format.format(fechaFinalizacion);

            assertEquals((garantiaExtendida.getProducto().getPrecio()*0.2),precioGarantia,0.0);
            assertEquals(fechaFin,"2021-01-11");
        }
    }


}
