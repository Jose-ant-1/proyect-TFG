package com.example.proyecto.config;

import com.example.proyecto.Model.*;
import com.example.proyecto.Repository.*;
import com.example.proyecto.Service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioService usuarioRepo;
    private final TecnoImpreRepository tecnoRepo;
    private final MaterialesRepository matRepo;
    private final ProdPrediRepository prodRepo;
    private final PedidoRepository pedidoRepo;
    private final PagoRepository pagoRepo;
    private final SolicitudPersoRepository solRepo;
    private final ArchivoSolicitudRepository archivoRepo;
    private final ValoracionesRepository valorRepo;
    private final ItemPedidoRepository itemPedidoRepo;

    public DataLoader(UsuarioService usuarioRepo, TecnoImpreRepository tecnoRepo,
                      MaterialesRepository matRepo, ProdPrediRepository prodRepo,
                      PedidoRepository pedidoRepo, PagoRepository pagoRepo,
                      SolicitudPersoRepository solRepo, ArchivoSolicitudRepository archivoRepo,
                      ValoracionesRepository valorRepo, ItemPedidoRepository itemPedidoRepo) {
        this.usuarioRepo = usuarioRepo;
        this.tecnoRepo = tecnoRepo;
        this.matRepo = matRepo;
        this.prodRepo = prodRepo;
        this.pedidoRepo = pedidoRepo;
        this.pagoRepo = pagoRepo;
        this.solRepo = solRepo;
        this.archivoRepo = archivoRepo;
        this.valorRepo = valorRepo;
        this.itemPedidoRepo = itemPedidoRepo;

    }

    @Override
    public void run(String... args) throws Exception {

        if (usuarioRepo.findByEmail("admin@tienda3d.com").isPresent()) {
            System.out.println(">> Datos ya existentes. Saltando carga...");
            return;
        }


        // 1. USUARIO
        Usuario admin = Usuario.builder()
                .nombre("Admin")
                .apellidos("García Pérez")
                .email("admin@tienda3d.com")
                .contrasenia("1234")
                .telefono("600123456")
                .direccion("Calle Mayor 1")
                .ciudad("Madrid")
                .codigoPostal(28001)
                .rol("ADMIN")
                .estado("ACTIVO")
                .build();
        usuarioRepo.save(admin);

        // 2. TECNOLOGÍA
        TecnologiaImpresion fdm = TecnologiaImpresion.builder()
                .nombre("FDM (Filamento)")
                .descripcion("Modelado por deposición fundida")
                .especificacion("Boquilla 0.4mm, Precisión 0.1mm")
                .disponible(true)
                .build();
        tecnoRepo.save(fdm);

        // 3. MATERIAL
        Materiales pla = Materiales.builder()
                .nombreMaterial("PLA")
                .tipo("Termoplástico")
                .descripcion("Biodegradable y fácil de imprimir")
                .color("Rojo")
                .precioPorGramo(0.05)
                .stockGramo(5000.0)
                .propiedades("Rigidez alta, Resistencia térmica baja")
                .disponible(true)
                .fechaCreacion(LocalDate.now())
                .build();
        matRepo.save(pla);

        // 4. PRODUCTO PREDISEÑADO
        ProductoPredisenyado figura = ProductoPredisenyado.builder()
                .nombreProducto("Figura Dragón")
                .descripcion("Figura decorativa articulada de gran detalle")
                .precio(25.99)
                .stockDisponible(15)
                .dimensiones("15x10x5 cm")
                .pesoGramos(120.0)
                .tecnologia(fdm)
                .material(pla)
                .destacado(true)
                .disponible(true)
                .fechaCreacion(LocalDate.now())
                .imagenUrl("https://m.media-amazon.com/images/I/71nAKXhEakL._AC_SY450_.jpg")
                .build();
        prodRepo.save(figura);

        // 5. PEDIDO
        Pedido pedido1 = Pedido.builder()
                .usuario(admin)
                .numeroPedido("PED-2024-0001")
                .subtotal(25.99)
                .gastosEnvio(4.95)
                .total(30.94)
                .estado("COMPLETADO")
                .direccionEnvio("Calle Mayor 1, Madrid")
                .fechaPedido(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();
        pedidoRepo.save(pedido1);

        // 6. ITEM PEDIDO
        ItemPedido linea1 = ItemPedido.builder()
                .pedido(pedido1)
                .producto(figura)
                .cantidad(1)
                .precioUnitario(25.99)
                .build();
        itemPedidoRepo.save(linea1);

        // 7. PAGO
        Pago pago1 = Pago.builder()
                .usuario(admin)
                .pedido(pedido1)
                .importe(30.94)
                .metodoPago("TARJETA")
                .estadoPago("COMPLETADO")
                .idTransaccion("TXN-99887766")
                .fechaPago(LocalDateTime.now())
                .fechaCreacion(LocalDateTime.now())
                .build();
        pagoRepo.save(pago1);

        // 8. SOLICITUD PERSONALIZADA
        SolicitudPersonalizada sol1 = SolicitudPersonalizada.builder()
                .usuario(admin)
                .numeroSolicitud("SOL-CUSTOM-001")
                .tipoServicio("Prototipado")
                .material(pla)
                .tecnologia(fdm)
                .descripcion("Pieza mecánica para motor")
                .estado("pendiente")
                .fechaSolicitud(LocalDateTime.now())
                .build();
        solRepo.save(sol1);

        // 9. ARCHIVO SOLICITUD
        ArchivoSolicitud archivo1 = ArchivoSolicitud.builder()
                .solicitud(sol1)
                .nombreArchivo("motor_v8.stl")
                .url("http://storage.com/motor_v8.stl")
                .fechaSubida(LocalDate.now())
                .build();
        archivoRepo.save(archivo1);

        // 10. VALORACIÓN
        Valoraciones val1 = Valoraciones.builder()
                .usuario(admin)
                .producto(figura)
                .puntuacion(5)
                .comentario("Excelente acabado.")
                .fechaValoracion(LocalDate.now())
                .build();
        valorRepo.save(val1);

        System.out.println(">> DataLoader: ¡Base de datos cargada correctamente!");
    }
}