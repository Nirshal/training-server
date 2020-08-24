package com.nirshal.repository.impl.panache;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.nirshal.model.Training;
import com.nirshal.repository.MongoDriver;
import com.nirshal.repository.TrainingRepository;
import com.nirshal.util.mongodb.MongoCollectionsIndexesProvider;
import com.nirshal.util.mongodb.Page;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import org.bson.conversions.Bson;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


@MongoDriver(MongoDriver.Type.PANACHE)
@ApplicationScoped
public class TrainingRepositoryImplPanache implements TrainingRepository {

    @Inject
    TrainingRepo repo;

    private final Random random = new Random();

    @PostConstruct
    void init(){
        for (Bson index : MongoCollectionsIndexesProvider.getIdexes(Training.class.getSimpleName())) {
            repo.mongoCollection().createIndex(index);
        }
    }

    @Override
    public Boolean exists(String id) {
        return repo.findByIdOptional(id).isPresent();
    }

    @Override
    public Training findById(String id) {
        return repo.findById(id);
    }

    @Override
    public void upsert(Training training) {
        if (training.getId() == null) {
            training.setId(createId(training));
        }
        repo.persistOrUpdate(training);
    }

    @Override
    public List<Training> query(Page page) {
        // create a query for all living persons
        PanacheQuery<Training> query =
                repo.findAll(
                        page.getSortDirection().equals(Page.ASC) ?
                                Sort.ascending(page.getSortBy())
                                :
                                Sort.descending(page.getSortBy())
                );

        query
                .page(
                        io.quarkus.panache.common.Page
                                .of(
                                        page.getPage(),
                                        page.getItemPerPage()
                                )
                );
        page.setPagesNumber(query.pageCount());
        return query.list();

    }

    @Override
    public Boolean deleteById(String id) {
        return repo.deleteById(id);
    }

    @Override
    public List<Training> getByDates(Date from, Date to) {
        Bson fromDate = from != null ? Filters.gte(Training.CREATION_DATE_FIELD_NAME, from) : null;
        Bson toDate = to != null ? Filters.lte(Training.CREATION_DATE_FIELD_NAME, to) : null;

        Bson sorting = Sorts.ascending(Training.CREATION_DATE_FIELD_NAME);

        if (fromDate == null && toDate == null){
            return repo
                    .mongoCollection()
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
                    .mongoCollection()
                    .find(query)
                    .sort(sorting).into(new ArrayList<>());
        }
    }

    /**
     * Commodity function to generate an id when the item needs one.
     * The id is the concatenation of epoch and the abs of the object hashcode.
     *
     * @param item
     * @return
     */
    private String createId(Training item) {
        return new Date().getTime() + "_" + Math.abs(random.nextInt());//item.hashCode());
    }
}
