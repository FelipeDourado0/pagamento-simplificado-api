package br.com.dourado.pagamento.simplificado.api.service;

import br.com.dourado.pagamento.simplificado.api.domain.entities.Conta;
import br.com.dourado.pagamento.simplificado.api.domain.entities.Usuario;
import br.com.dourado.pagamento.simplificado.api.domain.repositories.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Service
public class ContaService {
    private static final Random random = new Random();
    @Autowired
    ContaRepository contaRepository;

    public void cadastrarContaCorrente(Usuario usuario, BigDecimal saldoInicial) {
        Conta novaConta = new Conta()
                .setUsuario(usuario)
                .setNumeroConta(gerarContaCorrente())
                .setAgencia(gerarAgencia())
                .setSaldo(saldoInicial);

        salvarContaCorrente(novaConta);
    }

    private void salvarContaCorrente(Conta conta) {
        contaRepository.save(conta);
        contaRepository.flush();
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
