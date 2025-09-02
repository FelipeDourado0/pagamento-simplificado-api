package br.com.dourado.pagamento.simplificado.api.controller;

import br.com.dourado.pagamento.simplificado.api.domain.dtos.transferencia.ExtratoTransferenciaDTO;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.transferencia.TransferenciaRequestDTO;
import br.com.dourado.pagamento.simplificado.api.service.TransferenciaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("transferencia")
public class TransferenciaController {
    @Autowired
    TransferenciaService transferenciaService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ExtratoTransferenciaDTO> fazerTransferencia(@RequestBody @Valid TransferenciaRequestDTO data) {
        ExtratoTransferenciaDTO resultado = transferenciaService.realizarTransacao(data);

        return ResponseEntity.ok(resultado);
    }
}
