package com.ceiba.tiendatecnologica.aplicacion.manejadores.garantia;

import com.ceiba.tiendatecnologica.aplicacion.fabrica.FabricaGarantia;
import com.ceiba.tiendatecnologica.aplicacion.manejadores.producto.ManejadorObtenerProducto;
import com.ceiba.tiendatecnologica.dominio.GarantiaExtendida;
import com.ceiba.tiendatecnologica.dominio.Producto;
import com.ceiba.tiendatecnologica.dominio.excepcion.GarantiaExtendidaException;
import com.ceiba.tiendatecnologica.dominio.servicio.garantia.ServicioGenerarGarantia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component
public class ManejadorGenerarGarantia {

	private static final Logger logger = LoggerFactory.getLogger(ManejadorGenerarGarantia.class);


	private ServicioGenerarGarantia servicioGenerarGarantia;
		private FabricaGarantia fabricaGarantia;
	private ManejadorObtenerProducto manejadorObtenerProducto;

	public ManejadorGenerarGarantia(ServicioGenerarGarantia servicioGenerarGarantia, FabricaGarantia fabricaGarantia, ManejadorObtenerProducto manejadorObtenerProducto) {
		this.servicioGenerarGarantia = servicioGenerarGarantia;
		this.fabricaGarantia = fabricaGarantia;
		this.manejadorObtenerProducto = manejadorObtenerProducto;

	}


	@Transactional
	public String ejecutar(String codigoProducto,String nombreCliente) throws Exception {
		Producto producto= this.manejadorObtenerProducto.ejecutar(codigoProducto);
		GarantiaExtendida garantiaExtendida = this.fabricaGarantia.garantiaExtendidaPre(producto);
		return this.servicioGenerarGarantia.ejecutar(garantiaExtendida,nombreCliente);

	}
}
