package com.nirshal.util.mongodb;

/**
 * Model the object managed into a mongodb collection.
 * The provided methods are needed by {@link MongoRepository}
 * in order to implement the basic database operations.
 */
public interface MongoItem {

    String getId();
    void setId(String id);
}
