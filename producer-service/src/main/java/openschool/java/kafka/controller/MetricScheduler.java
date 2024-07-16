package openschool.java.kafka.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openschool.java.kafka.dto.MetricTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetricScheduler {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 60000)
    @Async
    public void sendActuatorMetrics() {
        log.info("Sending actuator metrics");
        String actuatorMetricsUrl = "http://localhost:8080/actuator/metrics/system.cpu.usage";
        try {
            String rawJson = restTemplate.getForObject(actuatorMetricsUrl, String.class);
            log.info("Raw JSON from Actuator: {}", rawJson);

            MetricTO metricTO = new ObjectMapper().readValue(rawJson, MetricTO.class);

            if (metricTO != null) {
                metricTO.setTimestamp(LocalDateTime.now());
                kafkaTemplate.send("metrics-topic", metricTO);
                log.info("Automatically sent actuator metric: {}", metricTO);
            } else {
                log.warn("Received null metric data from actuator endpoint");
            }
        } catch (Exception e) {
            log.error("Error while fetching or sending metric {}", e.getMessage());
        }
    }
}
