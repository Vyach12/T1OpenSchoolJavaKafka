package openschool.java.kafka.consumerservice.repository;

import openschool.java.kafka.consumerservice.entity.Metric;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetricRepository extends JpaRepository<Metric, Long> {
}