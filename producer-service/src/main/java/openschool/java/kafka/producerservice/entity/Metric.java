package openschool.java.kafka.producerservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Metric {
    private String id;
    private String name;
    private double value;
    private LocalDateTime timestamp;
}