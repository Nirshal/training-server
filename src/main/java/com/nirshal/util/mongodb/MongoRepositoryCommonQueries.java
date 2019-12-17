package com.nirshal.util.mongodb;

import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;

import java.util.List;

public class MongoRepositoryCommonQueries {

    public static Bson isInThisListOfIds(List<String> idList){
        return Filters.in
                (
                        FieldConstants.MONGO_ID,
                        idList
                );
    }
    public static Bson hasThisId(String id){
        return Filters.eq
                (
                        FieldConstants.MONGO_ID,
                        id
                );
    }
}
