package openschool.java.kafka.consumerservice.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openschool.java.kafka.consumerservice.entity.Metric;
import openschool.java.kafka.consumerservice.repository.MetricRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final MetricRepository repository;

    @KafkaListener(topics = "metrics-topic", groupId = "metrics-group")
    public void listen(String message) {
        log.info("Received metric {}", message);
        try {
            Metric metric = new ObjectMapper().readValue(message, Metric.class);
            repository.save(metric);
            log.info("Received metric: {}", metric.getName());
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }
}
