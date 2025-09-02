package br.com.dourado.pagamento.simplificado.api.service;

import br.com.dourado.pagamento.simplificado.api.domain.entities.ContaCorrente;
import br.com.dourado.pagamento.simplificado.api.domain.entities.Usuario;
import br.com.dourado.pagamento.simplificado.api.domain.repositories.ContaCorrenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Service
public class ContaCorrenteService {
    private static final Random random = new Random();
    @Autowired
    ContaCorrenteRepository contaCorrenteRepository;

    public void cadastrarContaCorrente(Usuario usuario, BigDecimal saldoInicial) {
        ContaCorrente novaContaCorrente = new ContaCorrente()
                .setUsuario(usuario)
                .setContaCorrente(gerarContaCorrente())
                .setAgencia(gerarAgencia())
                .setSaldo(saldoInicial);

        salvarContaCorrente(novaContaCorrente);
    }

    private void salvarContaCorrente(ContaCorrente contaCorrente) {
        contaCorrenteRepository.save(contaCorrente);
        contaCorrenteRepository.flush();
    }

    private String gerarAgencia() {
        int numero = 1000 + random.nextInt(9000); // sempre entre 1000-9999
        return String.valueOf(numero);
    }

    private String gerarContaCorrente() {
        int numero = 10000000 + random.nextInt(90000000); // 8 dígitos
        int digito = random.nextInt(10); // dígito de 0-9
        return numero + "-" + digito;
    }
}
