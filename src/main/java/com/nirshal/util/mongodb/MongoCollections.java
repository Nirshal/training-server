package com.nirshal.util.mongodb;

import com.mongodb.client.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Manges all the collections defined in the project.
 * Inject this if you need a collection, and then call the
 * {@link #getRepositoryFrom(Class)} to get the specific collection
 * wrapped into a {@link MongoRepository}.
 */
@ApplicationScoped
public class MongoCollections {

    Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Inject
    MongoClientHelperQuarkus mongodb;

    /**
     * Returns the specified repository.
     * @param type the specific repository we want.
     * @return
     */
    public MongoRepository getRepositoryFrom(Class type) {

        logger.info("Mongo Repository intialization for type={}.", type.getSimpleName());
        MongoCollection collection = mongodb.getCollection(type.getSimpleName(), type);

        return new MongoRepository(collection);
    }

}
