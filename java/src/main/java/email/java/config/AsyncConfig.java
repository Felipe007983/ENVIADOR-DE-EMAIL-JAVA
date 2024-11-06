package email.java.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // Número mínimo de threads
        executor.setMaxPoolSize(10); // Número máximo de threads
        executor.setQueueCapacity(1000); // Capacidade da fila
        executor.setThreadNamePrefix("EmailSender-");
        executor.initialize();
        return executor;
    }
}
