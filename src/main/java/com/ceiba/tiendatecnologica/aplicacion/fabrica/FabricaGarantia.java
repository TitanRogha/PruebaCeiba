package com.ceiba.tiendatecnologica.aplicacion.fabrica;


import com.ceiba.tiendatecnologica.dominio.GarantiaExtendida;
import com.ceiba.tiendatecnologica.dominio.Producto;
import org.springframework.stereotype.Component;

@Component
public class FabricaGarantia {
    public GarantiaExtendida garantiaExtendidaPre(Producto producto){
        return new GarantiaExtendida(producto);
    }

    public GarantiaExtendida garantiaExtendida(GarantiaExtendida garantiaExtendida){
        return new GarantiaExtendida(garantiaExtendida.getProducto(),garantiaExtendida.getFechaSolicitudGarantia(),garantiaExtendida.getFechaFinGarantia(),garantiaExtendida.getPrecioGarantia(),garantiaExtendida.getNombreCliente());
    }
}
