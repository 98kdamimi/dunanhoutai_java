package com.junyang.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class SecondaryMongoConfig {
	
	@Value("${spring.data.mongodb.secondary.uri}")
    private String mongoUri;

    // 创建次数据源的 MongoDatabaseFactory
    @Bean(name = "secondaryMongoDatabaseFactory")
    public MongoDatabaseFactory secondaryMongoDatabaseFactory() {
        return new SimpleMongoClientDatabaseFactory(mongoUri);
    }

    // 创建次数据源的 MongoTemplate
    @Bean(name = "secondaryMongoTemplate")
    public MongoTemplate secondaryMongoTemplate() {
        return new MongoTemplate(secondaryMongoDatabaseFactory());
    }
}
