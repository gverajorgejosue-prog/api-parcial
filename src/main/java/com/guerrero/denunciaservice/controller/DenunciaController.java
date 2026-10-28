package com.guerrero.denunciaservice.controller;

import com.guerrero.denunciaservice.entity.Denuncia;
import com.guerrero.denunciaservice.service.DenunciaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controlador REST de denuncias.
 *
 * Endpoints:
 *  - POST /denuncias                 -> registra una denuncia
 *  - GET  /denuncias?offset=0&limit=5 -> listado paginado
 *  - GET  /denuncias/{id}             -> denuncia por ID
 *  - GET  /denuncias/ciudadano/{dni}  -> denuncias por DNI del ciudadano
 *  - PUT  /denuncias/{id}             -> marca la denuncia como atendida
 */
@RestController
@RequestMapping("/denuncias")
@CrossOrigin(origins = "*")
public class DenunciaController {

    private final DenunciaService denunciaService;

    public DenunciaController(DenunciaService denunciaService) {
        this.denunciaService = denunciaService;
    }

    /** Registra una nueva denuncia y devuelve 201 Created con la ubicación del recurso. */
    @PostMapping
    public ResponseEntity<Denuncia> registrar(@RequestBody Denuncia denuncia) {
        Denuncia creada = denunciaService.registrarDenuncia(denuncia);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creada.getId())
                .toUri();
        return ResponseEntity.created(ubicacion).body(creada);
    }

    /**
     * Lista denuncias de forma paginada.
     * Ejemplo: GET /denuncias?offset=0&limit=5
     *
     * Se registran ambas variantes de ruta ("" y "/") para que también
     * responda GET /denuncias/ con barra final: en Spring Framework 7 el
     * trailing slash matching fue eliminado y, sin esto, /denuncias/ caería
     * en /denuncias/{id} y devolvería un error 500.
     *
     * Con la base de datos vacía devuelve 200 OK y content = [] (sin NPE).
     */
    @GetMapping({ "", "/" })
    public ResponseEntity<Page<Denuncia>> listarPaginado(
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "5") int limit) {
        if (offset < 0 || limit < 1) {
            throw new IllegalArgumentException("El offset debe ser >= 0 y el limit >= 1");
        }
        Page<Denuncia> pagina = denunciaService.listarPaginado(PageRequest.of(offset, limit));
        // Page nunca es null; si no hay registros, content queda vacío ([]) y se responde 200 OK
        return ResponseEntity.ok(pagina);
    }

    /** Obtiene una denuncia por su ID. */
    @GetMapping("/{id}")
    public ResponseEntity<Denuncia> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(denunciaService.obtenerPorId(id));
    }

    /** Obtiene todas las denuncias registradas por el DNI de un ciudadano. */
    @GetMapping("/ciudadano/{dni}")
    public ResponseEntity<List<Denuncia>> obtenerPorDni(@PathVariable String dni) {
        return ResponseEntity.ok(denunciaService.obtenerPorDni(dni));
    }

    /** Marca una denuncia como atendida (estado 1 -> 0). */
    @PutMapping("/{id}")
    public ResponseEntity<Denuncia> atender(@PathVariable Long id) {
        return ResponseEntity.ok(denunciaService.atenderDenuncia(id));
    }
}
