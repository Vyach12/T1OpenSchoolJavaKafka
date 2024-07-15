package openschool.java.kafka.producerservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetricScheduler {
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final RestTemplate restTemplate = new RestTemplate();

    // Список метрик для отправки
    private final String[] metricNames = {"system.cpu.usage", "jvm.threads.states"};

    @Scheduled(fixedRate = 60000) // Отправка метрик каждую минуту
    @Async
    public void sendActuatorMetrics() {
        log.info("Sending actuator metrics");
        for (String metricName : metricNames) {
            try {
                // Получаем метрику из Actuator
                // URL для доступа к Actuator Metrics
                String actuatorMetricsUrl = "http://localhost:8080/actuator/metrics/";
                String fullUrl = actuatorMetricsUrl + metricName;
                String metricData = restTemplate.getForObject(fullUrl, String.class);

                // Отправляем метрику в Kafka
                kafkaTemplate.send("metrics-topic", metricData);
                log.info("Automatically sent actuator metric: {}", metricData);
            } catch (Exception e) {
                log.error("Error while fetching or sending metric {}", metricName);
            }
        }
    }
}
