package com.study.config;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.DBCollection;
import com.mongodb.MongoClientURI;

/**
 * mongo模板
 * 
 * @author fk
 *
 */
@Configuration
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
public class MgTemplate {

	@Value("${spring.data.mongodb.uri}")
	private String url;

	@Autowired
	private MongoTemplate mongoTemplate;

	public DBCollection getCollection(String collectionName) {
		return mongoTemplate.getCollection(collectionName);
	}

	@Bean
	public MongoDbFactory mongoDbFactory() throws UnknownHostException {
		MongoClientURI mongoClientURI = new MongoClientURI(url);
		System.out.println(mongoClientURI.getURI());
		MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongoClientURI);
		return mongoDbFactory;
	}

}
