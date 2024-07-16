package openschool.java.kafka.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MetricTO {
    private String name;
    private String description;
    private String baseUnit;
    private LocalDateTime timestamp;
    private List<Measurement> measurements;
    private List<String> availableTags;

    @Data
    public static class Measurement {
        private String statistic;
        private double value;
    }
}
