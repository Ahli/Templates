package mongodb.timeseries.demo.persistence.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.TimeSeries;
import org.springframework.data.mongodb.core.timeseries.Granularity;

import java.time.Instant;

@Getter
@Setter
@RequiredArgsConstructor
@TimeSeries(collection = "measurements", timeField = "timestamp", metaField = "metaData",
            granularity = Granularity.MINUTES)
public class Measurement {
	
	@Field("timestamp")
	private Instant timestamp;
	
	@Field("metaData")
	private MetaData metaData;
	
	private float value;
	
	public Measurement(final Instant timestamp, final MetaData metaData, final float value) {
		this.timestamp = timestamp;
		this.metaData = metaData;
		this.value = value;
	}
}
