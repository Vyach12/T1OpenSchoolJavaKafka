package openschool.java.kafka.producerservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import openschool.java.kafka.producerservice.entity.Metric;

@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
public class MetricsController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping
    public ResponseEntity<?> sendMetric(@RequestBody Metric metric) {
        try {
            String message = new ObjectMapper().writeValueAsString(metric);
            kafkaTemplate.send("metrics-topic", message);
            return ResponseEntity.ok().build();
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing metric");
        }
    }
}
