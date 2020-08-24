package com.nirshal.repository.impl.panache;

import com.nirshal.model.TrainingFile;
import com.nirshal.repository.MongoDriver;
import com.nirshal.repository.TrainingFileRepository;
import com.nirshal.util.mongodb.MongoCollectionsIndexesProvider;
import org.bson.conversions.Bson;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.Random;

@MongoDriver(MongoDriver.Type.PANACHE)
@ApplicationScoped
public class TrainingFileRepositoryImplPanache implements TrainingFileRepository {

    @Inject
    TrainingFileRepo repo;

    private final Random random = new Random();

    @PostConstruct
    void init(){
        for (Bson index : MongoCollectionsIndexesProvider.getIdexes(TrainingFile.class.getSimpleName())) {
            repo.mongoCollection().createIndex(index);
        }
    }

    @Override
    public TrainingFile findById(String id) {
        return repo.findById(id);
    }

    @Override
    public void upsert(TrainingFile trainingFile) {
        if (trainingFile.getId() == null) {
            trainingFile.setId(createId(trainingFile));
        }
        repo.persistOrUpdate(trainingFile);
    }

    @Override
    public Boolean deleteById(String id) {
        return repo.deleteById(id);
    }

    /**
     * Commodity function to generate an id when the item needs one.
     * The id is the concatenation of epoch and the abs of the object hashcode.
     *
     * @param item
     * @return
     */
    private String createId(TrainingFile item) {
        return new Date().getTime() + "_" + Math.abs(random.nextInt());//item.hashCode());
    }

}
