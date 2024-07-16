package openschool.java.kafka.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openschool.java.kafka.dto.MetricTO;
import openschool.java.kafka.entity.Measurement;
import openschool.java.kafka.entity.Metric;
import openschool.java.kafka.repository.MetricRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMetricListener {

    private final MetricRepository metricRepository;

    @KafkaListener(topics = "metrics-topic", groupId = "metrics-group")
    @Transactional
    public void listen(MetricTO metricTO) {
        log.info("Getting metric {}", metricTO);

        List<Measurement> measurements = metricTO.getMeasurements().stream()
                .map(x -> Measurement.builder()
                        .value(x.getValue())
                        .statistic(x.getStatistic())
                        .build())
                .toList();

        Metric metric = Metric.builder()
                .name(metricTO.getName())
                .description(metricTO.getDescription())
                .baseUnit(metricTO.getBaseUnit())
                .timestamp(metricTO.getTimestamp())
                .measurements(measurements)
                .availableTags(metricTO.getAvailableTags())
                .build();

        metricRepository.save(metric);

        log.info("Received metric: {}", metric);
    }
}
