package openschool.java.kafka.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openschool.java.kafka.dto.MetricTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
public class MetricsController {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping
    public void sendMetric(@RequestBody MetricTO metric) {
        log.info("sending metric {}", metric);
        kafkaTemplate.send("metrics-topic", metric);
    }
}
