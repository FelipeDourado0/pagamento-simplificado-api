package br.com.dourado.pagamento.simplificado.api.controller;

import br.com.dourado.pagamento.simplificado.api.controller.interfce.IContaController;
import br.com.dourado.pagamento.simplificado.api.domain.dtos.conta.DadosContaResponseDTO;
import br.com.dourado.pagamento.simplificado.api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("conta")
public class ContaController implements IContaController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'LOJISTA')")
    public ResponseEntity<DadosContaResponseDTO> buscarDadosPorEmail(@RequestParam String cpfCnpj) {
        DadosContaResponseDTO response = usuarioService.obterDadosConta(cpfCnpj);

        return ResponseEntity.ok(response);
    }
}
