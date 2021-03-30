package mflix.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoDriverInformation;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.SslSettings;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Configuration
@Service
public class MongoDBConfiguration {

  @Bean
  @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
  public MongoClient mongoClient(@Value("${spring.mongodb.uri}") String connectionString) {

    ConnectionString connString = new ConnectionString(connectionString);

    //TODO> Ticket: Handling Timeouts - configure the expected
    // WriteConcern `wtimeout` and `connectTimeoutMS` values
    
    WriteConcern writeConcern = new WriteConcern(2);
    writeConcern = writeConcern.withWTimeout(2500, TimeUnit.MILLISECONDS);
    
    MongoClientSettings options = MongoClientSettings
    	     .builder()
    	     .applyConnectionString(connString)
    	     .writeConcern(writeConcern)
    	     .build();
   
    
    SslSettings sslSettings = options.getSslSettings();
    sslSettings.isEnabled();
    
   System.out.println(options.getWriteConcern().asDocument().toString());
   System.out.println(options.getReadConcern().asDocument().toString());
   System.out.println(options.getReadPreference().toString());
    
    
    MongoClient mongoClient = MongoClients.create(options);

    return mongoClient;
  }
}
