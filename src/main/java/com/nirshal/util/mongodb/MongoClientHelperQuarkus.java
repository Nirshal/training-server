package com.nirshal.util.mongodb;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.JsonbConfig;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * Helps in configuring and connect to mongodb.
 * Also includes configuration in order to use the automatic pojo codec.
 * Needs the following underlying properties in application.properties:
 *
 * # Quarkus-MongoDb:
 * mongo.db_name = ssb
 * quarkus.mongodb.connection-string=mongodb://127.0.0.1:27017/ssb
 * quarkus.mongodb.write-concern.journal=false
 * quarkus.mongodb.max-pool-size= 10
 * quarkus.mongodb.min-pool-size= 5
 * quarkus.mongodb.max-connection-idle-time= 300000
 * quarkus.mongodb.max-connection-life-time= 3600000
 * quarkus.mongodb.wait-queue-timeout= 10000
 * quarkus.mongodb.maintenance-frequency= 2000
 * quarkus.mongodb.maintenance-initial-delay= 500
 * quarkus.mongodb.wait-queue-multiple= 10
 * quarkus.mongodb.credentials.username = root
 * quarkus.mongodb.credentials.password = password
 * quarkus.mongodb.credentials.auth-source = admin
 *
 */
@ApplicationScoped
public class MongoClientHelperQuarkus {

    @ConfigProperty(name="mongo.db_name")
    String mongoDatabaseName;

    @Inject
    MongoClient client;

    /**
     * Returns the specified collection, configured with the pojo codec for the specified class.
     * @param name the name of the collection.
     * @param clazz the pojo class of the items in the collection
     * @return
     */
    public MongoCollection getCollection(String name, Class clazz){

        JsonbConfig config = new JsonbConfig().withNullValues(true);

        CodecRegistry pojoCodecRegistry =
                fromRegistries
                        (
                                MongoClientSettings.getDefaultCodecRegistry(),
                                fromProviders(PojoCodecProvider.builder().automatic(true).build())
                        );

        return client.getDatabase(mongoDatabaseName).withCodecRegistry(pojoCodecRegistry).getCollection(name, clazz);
    }

}
