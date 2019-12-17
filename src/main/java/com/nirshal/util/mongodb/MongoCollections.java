package com.nirshal.util.mongodb;

import com.mongodb.client.MongoCollection;
import com.nirshal.model.Training;
import lombok.Getter;
import org.bson.conversions.Bson;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Manges all the collections defined in the project.
 * Inject this if you need a collection, and then call the
 * {@link #getRepositoryFrom(type)} to get the specific collection
 * wrapped into a {@link MongoRepository}.
 */
@ApplicationScoped
public class MongoCollections {

    Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Inject
    MongoClientHelperQuarkus mongodb;

    @Getter
    @ConfigProperty(name = "mongo.collections.trainings")
    String trainingsCollection;

    /**
     * The label that identifies a specific collection.
     */
    public enum type {
        TRAININGS(Training.class);

        Class clazz;

        type(Class clazz){
            this.clazz = clazz;
        }
    }

    /**
     * Maps the Collection type cases with the configuration properties representing the names.
     * @param type
     * @return
     */
    private String getName(MongoCollections.type type) {
        switch (type){
            case TRAININGS:
                return trainingsCollection;
                default:
                    throw new Error("FATAL ERROR: " + type.name() + " does not have associated collection name!!!");
        }
    }

    /**
     * Returns the specified repository.
     * @param type the specific repository we want.
     * @return
     */
    public MongoRepository getRepositoryFrom(MongoCollections.type type) {

        logger.info("Mongo Repository intialization for type={}.", type);
        MongoCollection collection = mongodb.getCollection(getName(type), type.clazz);

        for (Bson index : MongoCollectionsIndexesProvider.getIdexes(type)){
            try {
                String indexName = collection.createIndex(index);
                logger.info("Creating index on Mongo Repository type={}, index={}",
                        type,
                        indexName
                );
            } catch (Exception e){

                logger.error("Failed to create index={} on repository with type={}, exception={}",
                        index,
                        type,
                        e
                );
            }
        }

        return new MongoRepository(collection);

    }

}
