package mongodb.timeseries.demo.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class BucketDataDto {

    private final Instant startDate;
    private final Double average;
    private final Double last;

}
