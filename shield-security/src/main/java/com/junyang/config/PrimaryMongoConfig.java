package com.junyang.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class PrimaryMongoConfig {
	
	@Value("${spring.data.mongodb.primary.uri}")
    private String mongoUri;
	
	// 创建主数据源的 MongoDatabaseFactory
    @Bean(name = "primaryMongoDatabaseFactory")
    @Primary
    public MongoDatabaseFactory primaryMongoDatabaseFactory() {
        return new SimpleMongoClientDatabaseFactory(mongoUri);
    }

    // 创建主数据源的 MongoTemplate
    @Bean(name = "primaryMongoTemplate")
    @Primary
    public MongoTemplate primaryMongoTemplate() {
        return new MongoTemplate(primaryMongoDatabaseFactory());
    }
}
