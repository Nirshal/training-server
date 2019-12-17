package com.nirshal.util.mongodb;

import lombok.Data;

/**
 * Just an implementation of {@link MongoItem}.
 * Extending a class from this one makes all the needed requirements
 * for {@link MongoRepository} be fulfilled automatically.
 */
@Data
public abstract class MongoItemClass implements MongoItem {

    private String id;

}
