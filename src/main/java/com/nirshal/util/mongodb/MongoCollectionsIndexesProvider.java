package com.nirshal.util.mongodb;
import com.mongodb.client.model.Indexes;
import com.nirshal.model.Training;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class MongoCollectionsIndexesProvider {

    /**
     * Maps the Collection type cases with the Indexes that must be present for them.
     * @param type
     * @return
     */
    public static List<Bson> getIdexes(String type) {

        List<Bson> indexes = new ArrayList<>();

        switch (type){
            case "Training":
                indexes.add(
                        Indexes.compoundIndex(
                                Indexes.ascending(Training.CREATION_DATE_FIELD_NAME)
                        )
                );
                break;
        }

        return indexes;
    }

}
