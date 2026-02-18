package com.example.proyecto.config;

import com.example.proyecto.Model.*;
import com.example.proyecto.Repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.sql.Date;

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

    public DataLoader(UsuarioRepository usuarioRepo, TecnoImpreRepository tecnoRepo,
                      MaterialesRepository matRepo, ProdPrediRepository prodRepo,
                      PedidoRepository pedidoRepo, PagoRepository pagoRepo,
                      SolicitudPersoRepository solRepo, ArchivoSolicitudRepository archivoRepo,
                      ValoracionesRepository valorRepo) {
        this.usuarioRepo = usuarioRepo;
        this.tecnoRepo = tecnoRepo;
        this.matRepo = matRepo;
        this.prodRepo = prodRepo;
        this.pedidoRepo = pedidoRepo;
        this.pagoRepo = pagoRepo;
        this.solRepo = solRepo;
        this.archivoRepo = archivoRepo;
        this.valorRepo = valorRepo;
    }

    @Override
    public void run(String... args) throws Exception {

        // Limpiamos o comprobamos para evitar el error de "Duplicate Entry"
        if (usuarioRepo.findByEmail("admin@tienda3d.com").isPresent()) {
            System.out.println(">> Datos ya existentes. Saltando carga...");
            return;
        }

        // 1. USUARIO (Rellenando TODO)
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
                .fechaRegistro(LocalDate.now())
                .fechaActualizacion(LocalDate.now())
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
                .tiempoImpresionMinutos(480)
                .caracteristicas("Articulado, No requiere soportes")
                .destacado(true)
                .disponible(true)
                .idMaterial(pla.getId())
                .idTecnologia(fdm.getId())
                .fechaCreacion(LocalDate.now())
                .build();
        prodRepo.save(figura);

        // 5. PEDIDO (Importante: numeroPedido no puede ser null)
        Pedido pedido1 = Pedido.builder()
                .idUsuario(admin.getId())
                .numeroPedido("PED-2024-0001")
                .subtotal(25.99)
                .gastosEnvio(4.95)
                .total(30.94)
                .estado("COMPLETADO")
                .direccionEnvio("Calle Mayor 1, Madrid")
                .notaCliente("Entregar por la tarde")
                .fechaPedido(LocalDate.now())
                .fecha_actualizacion(LocalDate.now())
                .build();
        pedidoRepo.save(pedido1);

        // 6. PAGO
        Pago pago1 = Pago.builder()
                .idUsuario(admin.getId())
                .idPedido(pedido1.getIdPedido())
                .importe(30.94)
                .metodoPago("TARJETA")
                .estadoPago("COMPLETADO")
                .idTransaccion("TXN-99887766") // único
                .detalles("Pago procesado por Stripe")
                .fechaPago(LocalDate.now())
                .fechaCreacion(LocalDate.now())
                .build();
        pagoRepo.save(pago1);

        // 7. SOLICITUD PERSONALIZADA
        SolicitudPersonalizada sol1 = SolicitudPersonalizada.builder()
                .idUsuario(admin.getId())
                .numeroSolicitud("SOL-CUSTOM-001")
                .tipoServicio("Prototipado")
                .idMaterial(pla.getId())
                .idTecnologia(fdm.getId())
                .descripcion("Pieza mecánica para motor")
                .requisitosEspeciales("Resistencia a vibraciones")
                .acabado("Lijado suave")
                .urgente(true)
                .estado("pendiente")
                .fechaSolicitud(LocalDate.now())
                .fechaActualizacion(LocalDate.now())
                .build();
        solRepo.save(sol1);

        // 8. VALORACIÓN
        Valoraciones val1 = Valoraciones.builder()
                .idUsuario(admin.getId())
                .idProducto(figura.getId())
                .puntuacion(5)
                .comentario("Excelente acabado, el color rojo es muy vibrante.")
                .fechaValoracion(LocalDate.now())
                .build();
        valorRepo.save(val1);

        System.out.println(">> DataLoader: ¡Base de datos cargada al 100% sin nulos!");
    }
}