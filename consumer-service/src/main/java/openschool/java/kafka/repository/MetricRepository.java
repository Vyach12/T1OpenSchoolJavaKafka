package openschool.java.kafka.repository;

import openschool.java.kafka.entity.Metric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MetricRepository extends JpaRepository<Metric, UUID> {
    List<Metric> findTop10ByNameOrderByTimestampDesc(String name);
}
