package openschool.java.kafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openschool.java.kafka.entity.Metric;
import openschool.java.kafka.repository.MetricRepository;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetricAnalysisService {

    private final MetricRepository metricRepository;

    public double predictCpuUsage(int minutes) {
        List<Metric> recentMetrics = metricRepository.findTop10ByNameOrderByTimestampDesc("system.cpu.usage");

        if (recentMetrics.isEmpty()) {
            throw new IllegalStateException("No metrics available for prediction");
        }

        int size = recentMetrics.size();
        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumX2 = 0;

        for (int i = 0; i < size; i++) {
            double x = i + 1;
            double y = recentMetrics.get(size - 1 - i).getMeasurements().getFirst().getValue();
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
        }

        double slope = (size * sumXY - sumX * sumY) / (size * sumX2 - sumX * sumX);
        double intercept = (sumY - slope * sumX) / size;

        return intercept + slope * (size + minutes);
    }

    public double calculateAverageCpuUsage(int minutes) {
        List<Metric> recentMetrics = metricRepository.findTop10ByNameOrderByTimestampDesc("system.cpu.usage");

        if (recentMetrics.isEmpty()) {
            throw new IllegalStateException("No metrics available for calculation");
        }

        // Filter metrics for the specified time frame (last 'minutes' minutes)
        long currentTime = System.currentTimeMillis();
        long timeThreshold = currentTime - (minutes * 60 * 1000L);
        List<Metric> filteredMetrics = recentMetrics.stream()
                .filter(metric -> metric.getTimestamp().atZone(ZoneOffset.UTC).toInstant().toEpochMilli() >= timeThreshold)
                .toList();

        if (filteredMetrics.isEmpty()) {
            throw new IllegalStateException("No metrics available in the specified time frame");
        }

        return filteredMetrics.stream()
                .mapToDouble(metric -> metric.getMeasurements().getFirst().getValue())
                .average()
                .orElse(0.0);
    }
}
