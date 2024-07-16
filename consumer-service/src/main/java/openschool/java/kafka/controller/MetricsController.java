package openschool.java.kafka.controller;

import lombok.RequiredArgsConstructor;
import openschool.java.kafka.entity.Metric;
import openschool.java.kafka.repository.MetricRepository;
import openschool.java.kafka.service.MetricAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
public class MetricsController {

    private final MetricRepository repository;
    private final MetricAnalysisService metricAnalysisService;

    @GetMapping
    public List<Metric> getAllMetrics() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Metric> getMetricById(@PathVariable UUID id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * возможность предсказания будущих значений метрик на основе линейной регрессии
     * @param minutes
     * @return
     */
    @GetMapping("/system.cpu.usage/predict")
    public double predictCpuUsage(@RequestParam int minutes) {
        return metricAnalysisService.predictCpuUsage(minutes);
    }

    @GetMapping("/system.cpu.usage/average")
    public double getAverageCpuUsage(@RequestParam int minutes) {
        return metricAnalysisService.calculateAverageCpuUsage(minutes);
    }
}