package com.nirshal.repository.impl;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.nirshal.model.Training;
import com.nirshal.repository.TrainingRepository;
import com.nirshal.util.mongodb.MongoCollections;
import com.nirshal.util.mongodb.MongoRepository;
import com.nirshal.util.mongodb.MongoRepositoryCommonQueries;
import com.nirshal.util.mongodb.Page;
import lombok.Data;
import org.bson.conversions.Bson;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class TrainingRepositoryImpl implements TrainingRepository {

    @Inject
    MongoCollections collections;

    MongoRepository<Training> repo;

    @PostConstruct
    void init(){
        repo = collections.getRepositoryFrom(Training.class);
    }

    public MongoRepository<Training> getRepo() {
        return repo;
    }
    @Override
    public Boolean exists(String id){
        return repo.existsAtLeastOne(MongoRepositoryCommonQueries.hasThisId(id));
    }

    @Override
    public Training findById(String id){
        return repo.findById(id);
    }

    @Override
    public void upsert(Training training){
        repo.upsert(training);
    }
    @Override
    public List<Training> query(Page page){
        return repo
                .query(page)
                .into(new ArrayList<>());
    }
    @Override
    public void deleteById(String id){
        repo.deleteById(id);
    }

    @Override
    public List<Training> getByDates(Date from, Date to) {

        Bson fromDate = from != null ? Filters.gte(Training.CREATION_DATE_FIELD_NAME, from) : null;
        Bson toDate = to != null ? Filters.lte(Training.CREATION_DATE_FIELD_NAME, to) : null;

        Bson sorting = Sorts.ascending(Training.CREATION_DATE_FIELD_NAME);

        if (fromDate == null && toDate == null){
            return repo
                    .getCollection()
                    .find()
                    .sort(sorting).into(new ArrayList<>());
        } else {
            Bson query;
            if (fromDate != null && toDate == null){
                query = fromDate;
            } else {
                if (fromDate == null && toDate != null){
                    query = toDate;
                } else {
                    query = Filters.and
                            (
                                    fromDate,
                                    toDate
                            );
                }
            }
            return repo
                    .getCollection()
                    .find(query)
                    .sort(sorting).into(new ArrayList<>());
        }
    }
}
