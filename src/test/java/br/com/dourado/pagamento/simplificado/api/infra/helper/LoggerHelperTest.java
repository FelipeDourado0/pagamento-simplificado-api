package br.com.dourado.pagamento.simplificado.api.infra.helper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class LoggerHelperTest {

    private LoggerHelper loggerHelper;
    private Logger mockLogger;

    @BeforeEach
    void setUp() {
        mockLogger = Mockito.mock(Logger.class);

        // sobrescrevendo o método getLogger para sempre devolver o mock
        loggerHelper = new LoggerHelper() {
            @Override
            protected Logger getLogger(Class<?> clazz) {
                return mockLogger;
            }
        };
    }

    @Test
    void deveChamarLoggerInfo() {
        loggerHelper.info(LoggerHelperTest.class, "Mensagem INFO");
        verify(mockLogger, times(1)).info("Mensagem INFO");
    }

    @Test
    void deveChamarLoggerDebug() {
        loggerHelper.debug(LoggerHelperTest.class, "Mensagem DEBUG");
        verify(mockLogger, times(1)).debug("Mensagem DEBUG");
    }

    @Test
    void deveChamarLoggerWarn() {
        loggerHelper.warn(LoggerHelperTest.class, "Mensagem WARN");
        verify(mockLogger, times(1)).warn("Mensagem WARN");
    }

    @Test
    void deveChamarLoggerErrorSemThrowable() {
        loggerHelper.error(LoggerHelperTest.class, "Mensagem ERROR");
        verify(mockLogger, times(1)).error("Mensagem ERROR");
    }

    @Test
    void deveChamarLoggerErrorComThrowable() {
        Exception ex = new RuntimeException("Erro de teste");
        loggerHelper.error(LoggerHelperTest.class, "Mensagem ERROR", ex);
        verify(mockLogger, times(1)).error("Mensagem ERROR", ex);
    }
}