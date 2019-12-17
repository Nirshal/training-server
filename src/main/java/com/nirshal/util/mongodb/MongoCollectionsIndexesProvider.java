package com.nirshal.util.mongodb;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class MongoCollectionsIndexesProvider {

    /**
     * Maps the Collection type cases with the Indexes that must be present for them.
     * @param type
     * @return
     */
    public static List<Bson> getIdexes(MongoCollections.type type) {

        List<Bson> indexes = new ArrayList<>();

        switch (type){
            case TRAININGS:
//                indexes.add(
//                        Indexes.compoundIndex(
//                                Indexes.ascending(FieldConstants.MONGO_ID),
//                                Indexes.ascending(RestConstants.USER_LEVEL)
//                        )
//                );
                break;
        }

        return indexes;
    }

}
