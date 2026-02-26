/**
 * carPriceAnalytics, Sistema de analisis de precios vehiculares
 * Copyright (c) 2025 Cognoware Cia. Ltda.
 * Todos los derechos reservados.
 * <p>
 * Este archivo forma parte del carPriceAnalytics de
 * Cognoware Cia. Ltda, desarrollado conforme a los requerimientos y
 * normativas establecidos.
 */
package ec.com.cognoware.carpriceanalytics.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configuracion del pool de hilos para extraccion asincrona.
 *
 * @author JJARA
 * @version 1.0.0
 * @since 2025-02-26
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    @Value("${spring.task.execution.pool.core-size:5}")
    private int corePoolSize;

    @Value("${spring.task.execution.pool.max-size:20}")
    private int maxPoolSize;

    @Value("${spring.task.execution.pool.queue-capacity:50}")
    private int queueCapacity;

    /**
     * Configura el executor para tareas asincronas.
     *
     * @return executor con pool de hilos configurado.
     */
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor =
            new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("provider-extractor-");
        executor.initialize();
        return executor;
    }
}
