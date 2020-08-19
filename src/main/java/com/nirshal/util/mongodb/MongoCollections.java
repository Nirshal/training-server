package com.nirshal.util.mongodb;

import com.mongodb.client.MongoCollection;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * A Map holding instances of repositories in order to avoid instanciating multiple identical collections.
     */
    private Map<String, MongoRepository> collections = new HashMap<>();

    /**
     * Returns the specified repository.
     * @param type the specific repository we want.
     * @return
     */
    public MongoRepository getRepositoryFrom(Class type) {

        if (
                collections.get(type.getSimpleName())!= null
        ){
            logger.info("Mongo Repository for type={} has been already instantiated: returning the existing instance.", type);
            return collections.get(type.getSimpleName());
        } else {
            logger.info("Mongo Repository initialization for type={}.", type.getSimpleName());
            MongoCollection collection = mongodb.getCollection(type.getSimpleName(), type);

            for (Bson index : MongoCollectionsIndexesProvider.getIdexes(type.getSimpleName())) {
                try {
                    String indexName = collection.createIndex(index);
                    logger.info("Creating index on Mongo Repository type={}, index={}",
                            type,
                            indexName
                    );
                } catch (Exception e) {

                    logger.error("Failed to create index={} on repository with type={}, exception={}",
                            index,
                            type,
                            e
                    );
                }
            }
            MongoRepository repo = new MongoRepository(collection);
            // We save the new repo reference for future reuse, if configured so.
            collections.put(type.getSimpleName(), repo);
            return repo;
        }
    }

}
