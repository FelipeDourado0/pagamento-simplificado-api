package br.com.dourado.pagamento.simplificado.api.infra.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggerHelper {

    private Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public void info(Class<?> clazz, String message) {
        getLogger(clazz).info(message);
    }

    public void debug(Class<?> clazz, String message) {
        getLogger(clazz).debug(message);
    }

    public void warn(Class<?> clazz, String message) {
        getLogger(clazz).warn(message);
    }

    public void error(Class<?> clazz, String message) {
        getLogger(clazz).error(message);
    }

    public void error(Class<?> clazz, String message, Throwable throwable) {
        getLogger(clazz).error(message, throwable);
    }
}
