package openschool.java.kafka.consumerservice.controller;

import lombok.RequiredArgsConstructor;
import openschool.java.kafka.consumerservice.entity.Metric;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import openschool.java.kafka.consumerservice.repository.MetricRepository;
import java.util.List;

@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
public class MetricsController {

    private final MetricRepository repository;

    @GetMapping
    public List<Metric> getAllMetrics() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Metric> getMetricById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
