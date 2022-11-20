package mongodb.timeseries.demo.persistence.config;

import mongodb.timeseries.demo.persistence.model.Measurement;
import org.bson.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;

@Configuration
public class PersistenceConfig {
	
	@Bean
	@Primary
	public MongoTemplate mongoTemplate(
			final MongoDatabaseFactory mongoDatabaseFactory, final MappingMongoConverter mappingMongoConverter) {
		
		// this is to avoid saving _class to db
		mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
		
		final MongoTemplate mongoTemplate = new MongoTemplate(mongoDatabaseFactory, mappingMongoConverter);
		init(mongoTemplate);
		return mongoTemplate;
	}
	
	public void init(final MongoTemplate mongoTemplate) {
		ensureCollectionExists(Measurement.class, mongoTemplate);
		
		ensureMetaDataTimestampIndex(Measurement.class, mongoTemplate);
	}
	
	private void ensureCollectionExists(final Class<?> collectionClass, final MongoTemplate mongoTemplate) {
		if (!mongoTemplate.collectionExists(collectionClass)) {
			mongoTemplate.createCollection(collectionClass);
		}
	}
	
	private void ensureMetaDataTimestampIndex(final Class<?> collectionClass, final MongoTemplate mongoTemplate) {
		mongoTemplate.indexOps(collectionClass)
				.ensureIndex(new CompoundIndexDefinition(new Document().append("metaData.deviceId", 1)
						.append("metaData.dataType", 1)
						.append("timestamp", 1)));
	}
	
}
