package openschool.java.kafka.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "MetricsSenderController", description = "Контроллер для отправки метрик в Kafka")
public class MetricsSenderController {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping
    @Operation(summary = "Отправить метрику", description = "Отправляет метрику в Kafka")
    public void sendMetric(@RequestBody MetricTO metric) {
        log.info("sending metric {}", metric);
        kafkaTemplate.send("metrics-topic", metric);
    }
}
