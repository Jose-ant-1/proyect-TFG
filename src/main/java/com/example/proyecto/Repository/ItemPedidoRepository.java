package com.example.proyecto.Repository;

import com.example.proyecto.Model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Integer> {
    // para buscar todos los productos de un pedido especifico
    List<ItemPedido> findByPedidoIdPedido(int idPedido);
}