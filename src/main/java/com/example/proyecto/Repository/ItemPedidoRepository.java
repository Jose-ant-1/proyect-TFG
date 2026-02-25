package com.example.proyecto.Repository;

import com.example.proyecto.Model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Integer> {
    // Útil para buscar todos los productos de un pedido específico
    List<ItemPedido> findByPedidoIdPedido(int idPedido);
}