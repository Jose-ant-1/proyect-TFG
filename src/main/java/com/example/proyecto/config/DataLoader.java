package com.example.proyecto.config;

import com.example.proyecto.Model.*;
import com.example.proyecto.Repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepo;
    private final TecnoImpreRepository tecnoRepo;
    private final MaterialesRepository matRepo;
    private final ProdPrediRepository prodRepo;
    private final PedidoRepository pedidoRepo;
    private final PagoRepository pagoRepo;
    private final SolicitudPersoRepository solRepo;
    private final ArchivoSolicitudRepository archivoRepo;
    private final ValoracionesRepository valorRepo;
    // NUEVO: Necesitamos el repositorio para los ítems
    private final ItemPedidoRepository itemPedidoRepo;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UsuarioRepository usuarioRepo, TecnoImpreRepository tecnoRepo,
                      MaterialesRepository matRepo, ProdPrediRepository prodRepo,
                      PedidoRepository pedidoRepo, PagoRepository pagoRepo,
                      SolicitudPersoRepository solRepo, ArchivoSolicitudRepository archivoRepo,
                      ValoracionesRepository valorRepo, ItemPedidoRepository itemPedidoRepo, PasswordEncoder passwordEncoder) {
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
        this.passwordEncoder = passwordEncoder;
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
                .contrasenia(passwordEncoder.encode("1234")) // Nota: En producción esto iría encriptado
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
                .stockGramo(5000)
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
                .pesoGramos(120)
                .tecnologia(fdm)
                .material(pla)
                .destacado(true)
                .disponible(true)
                .fechaCreacion(LocalDate.now())
                .build();
        prodRepo.save(figura);

        // 5. PEDIDO (Cabecera)
        Pedido pedido1 = Pedido.builder()
                .usuario(admin)
                .numeroPedido("PED-2024-0001")
                .subtotal(25.99)
                .gastosEnvio(4.95)
                .total(30.94)
                .estado("COMPLETADO")
                .direccionEnvio("Calle Mayor 1, Madrid")
                .fechaPedido(LocalDate.now())
                .fecha_actualizacion(LocalDate.now())
                .build();
        pedidoRepo.save(pedido1);

        // 6. ITEM PEDIDO (¡IMPORTANTE! Vincula el producto con el pedido)
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
                .fechaPago(LocalDate.now())
                // .fechaCreacion(LocalDate.now()) // Revisa si Pago.java tiene este campo, si no, bórralo
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
                .fechaSolicitud(LocalDate.now())
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