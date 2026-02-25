package com.example.proyecto.Service;

import com.example.proyecto.Model.ItemPedido;
import com.example.proyecto.Repository.ItemPedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemRepositoryService {

    private final ItemPedidoRepository itemPedidoRepository;

    public ItemRepositoryService(ItemPedidoRepository itemPedidoRepository) {
        this.itemPedidoRepository = itemPedidoRepository;
    }

    public List<ItemPedido> findAll() {
        return itemPedidoRepository.findAll();
    }

    public ItemPedido findById(Integer id) {
        return itemPedidoRepository.findById(id).orElse(null);
    }


    public ItemPedido save(ItemPedido itemPedido) {
        return itemPedidoRepository.save(itemPedido);
    }

    public void deleteById(Integer id) {
        itemPedidoRepository.deleteById(id);
    }


}
