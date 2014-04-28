package edu.sjsu.cmpe283.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Arrays;

@Configuration
@EnableMongoRepositories
public class DBConfig extends AbstractMongoConfiguration {

    private static Log logger = LogFactory.getLog(DBConfig.class);

    @Value("${mongodb.name}")
    String MONGODB_NAME;

    @Value("${mongodb.user}")
    String MONGODB_USER;
    
    @Value("${mongodb.password}")
    String MONGODB_PASSWORD;
    
    @Value("${mongodb.host}")
    String MONGODB_HOST;
    
    @Value("${mongodb.port}")
    Integer MONGODB_PORT;


    @Override
    protected String getDatabaseName() {
        return MONGODB_NAME;
    }

    @Override
    public Mongo mongo() throws Exception {
        logger.debug("Connecting to MongoDB: [" + MONGODB_USER + ":" + MONGODB_PASSWORD + "@" + MONGODB_HOST + ":" + MONGODB_PORT + "/" + MONGODB_NAME);
        MongoCredential credential = MongoCredential.createMongoCRCredential(MONGODB_USER, MONGODB_NAME, MONGODB_PASSWORD.toCharArray());
        MongoClient mongoClient = new MongoClient(new ServerAddress(MONGODB_HOST, MONGODB_PORT), Arrays.asList(credential));
        logger.debug("Connected to MongoDB: " + mongoClient.debugString());
        return mongoClient;
    }
}
