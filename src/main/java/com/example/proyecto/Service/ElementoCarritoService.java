package com.example.proyecto.Service;

import com.example.proyecto.Model.*;
import com.example.proyecto.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ElementoCarritoService {

    private final ElementoCarritoRepository elementoRepository;
    private final CarritoService carritoService;

    @Transactional
    public ElementoCarrito agregarItem(Usuario usuario, ProductoPredisenyado producto, int cantidad) {
        Carrito carrito = carritoService.obtenerCarritoPorUsuario(usuario);

        // BUSCAR si el producto ya está en el carrito de este usuario
        Optional<ElementoCarrito> itemExistente = carrito.getElementos().stream()
                .filter(e -> e.getProducto() != null && e.getProducto().getId().equals(producto.getId()))
                .findFirst();

        if (itemExistente.isPresent()) {
            // SI EXISTE: Actualizamos la cantidad (sumamos la nueva cantidad, que puede ser 1 o -1)
            ElementoCarrito item = itemExistente.get();
            int nuevaCantidad = item.getCantidad() + cantidad;

            if (nuevaCantidad <= 0) {
                // Si al restar llegamos a 0 o menos, lo borramos
                carrito.getElementos().remove(item);
                elementoRepository.delete(item);
                return null;
            }

            item.setCantidad(nuevaCantidad);
            return elementoRepository.save(item);
        } else {
            // SI NO EXISTE: Lo creamos de cero (solo si la cantidad es positiva)
            if (cantidad <= 0) return null;

            ElementoCarrito nuevoItem = ElementoCarrito.builder()
                    .usuario(usuario)
                    .cantidad(cantidad)
                    .producto(producto)
                    .precioUnitario(producto.getPrecio())
                    .build();

            carrito.getElementos().add(nuevoItem);
            return elementoRepository.save(nuevoItem);
        }
    }

    @Transactional
    public void eliminarItem(Long id) {
        // para ver si el ID llega bien a la consola de IntelliJ
        System.out.println("Intentando eliminar elemento de carrito con ID: " + id);
        elementoRepository.deleteById(id);
    }
}