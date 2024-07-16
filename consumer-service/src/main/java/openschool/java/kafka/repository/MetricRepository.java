package openschool.java.kafka.repository;

import openschool.java.kafka.entity.Metric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MetricRepository extends JpaRepository<Metric, UUID> {
}
