package spring.hugme.infra.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {
  @Bean(name = "LLMApiExecutor")
  @Primary
  public Executor LLMApiExecutor(){
    ThreadPoolTaskExecutor es = new ThreadPoolTaskExecutor();

    es.setCorePoolSize(5);
    es.setMaxPoolSize(15);
    es.setQueueCapacity(50);

    es.initialize();
    return es;
  }

}
