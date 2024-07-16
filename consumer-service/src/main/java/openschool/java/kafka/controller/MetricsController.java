package openschool.java.kafka.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "MetricsController", description = "Контроллер, предоставляющий доступ к метрикам и их анализу")
public class MetricsController {

    private final MetricRepository repository;
    private final MetricAnalysisService metricAnalysisService;

    @GetMapping
    @Operation(summary = "Получить все метрики", description = "Возвращает список всех метрик")
    public List<Metric> getAllMetrics() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить метрику по ID", description = "Возвращает метрику по её идентификатору")
    public ResponseEntity<Metric> getMetricById(
            @Parameter(description = "UUID метрики") @PathVariable UUID id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/system.cpu.usage/predict")
    @Operation(summary = "Предсказать использование CPU", description = "Предсказывает будущие значения использования CPU на основе линейной регрессии")
    public double predictCpuUsage(
            @Parameter(description = "Количество минут для предсказания") @RequestParam int minutes) {
        return metricAnalysisService.predictCpuUsage(minutes);
    }

    @GetMapping("/system.cpu.usage/average")
    @Operation(summary = "Среднее использование CPU", description = "Возвращает среднее значение использования CPU за указанное количество минут")
    public double getAverageCpuUsage(
            @Parameter(description = "Количество минут для расчета среднего значения") @RequestParam int minutes) {
        return metricAnalysisService.calculateAverageCpuUsage(minutes);
    }
}
