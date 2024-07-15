package openschool.java.kafka.consumerservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Metric {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "metric_id")
    private List<Measurement> measurements;

/*    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "metric_id")
    private List<Tag> availableTags;*/

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime timestamp;

    @Transient
    public double getValue() {
        return measurements.stream()
                .filter(m -> "VALUE".equals(m.getStatistic()))
                .findFirst()
                .map(Measurement::getValue)
                .orElse(0.0);
    }
}

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String statistic;
    private double value;
}

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ElementCollection
    private List<String> values;
}