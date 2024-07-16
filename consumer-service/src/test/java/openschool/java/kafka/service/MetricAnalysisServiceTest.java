package openschool.java.kafka.service;

import openschool.java.kafka.entity.Measurement;
import openschool.java.kafka.entity.Metric;
import openschool.java.kafka.repository.MetricRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MetricAnalysisServiceTest {

    @Mock
    private MetricRepository metricRepository;

    @InjectMocks
    private MetricAnalysisService metricAnalysisService;

    @Test
    void testPredictCpuUsage() {
        List<Metric> mockMetrics = Arrays.asList(
                createMetric(LocalDateTime.now().minusMinutes(10), 20.0),
                createMetric(LocalDateTime.now().minusMinutes(9), 22.0),
                createMetric(LocalDateTime.now().minusMinutes(8), 24.0),
                createMetric(LocalDateTime.now().minusMinutes(7), 26.0),
                createMetric(LocalDateTime.now().minusMinutes(6), 28.0)
        );

        when(metricRepository.findTop10ByNameOrderByTimestampDesc("system.cpu.usage")).thenReturn(mockMetrics);

        double predictedUsage = metricAnalysisService.predictCpuUsage(5);
        assertTrue(predictedUsage > 0, "Predicted CPU usage should be greater than 0");
    }

    @Test
    void testCalculateAverageCpuUsage() {
        List<Metric> mockMetrics = Arrays.asList(
                createMetric(LocalDateTime.now().minusMinutes(10), 20.0),
                createMetric(LocalDateTime.now().minusMinutes(9), 22.0),
                createMetric(LocalDateTime.now().minusMinutes(8), 24.0),
                createMetric(LocalDateTime.now().minusMinutes(7), 26.0),
                createMetric(LocalDateTime.now().minusMinutes(6), 28.0)
        );

        when(metricRepository.findTop10ByNameOrderByTimestampDesc("system.cpu.usage")).thenReturn(mockMetrics);

        double averageUsage = metricAnalysisService.calculateAverageCpuUsage(10);
        assertEquals(24.0, averageUsage, 0.1, "Average CPU usage should be 24.0");
    }

    private Metric createMetric(LocalDateTime timestamp, double value) {
        Measurement measurement = Measurement.builder()
                .id(UUID.randomUUID())
                .statistic("VALUE")
                .value(value)
                .build();

        return Metric.builder()
                .id(UUID.randomUUID())
                .name("system.cpu.usage")
                .description("CPU usage metric")
                .baseUnit("percent")
                .measurements(Collections.singletonList(measurement))
                .timestamp(timestamp)
                .build();
    }
}