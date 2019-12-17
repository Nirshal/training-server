package com.nirshal.util.mongodb;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CountOptions;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.Getter;
import lombok.NonNull;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.mongodb.client.model.Filters.eq;

/**
 * Wrap a mongo collection to offer some standardized and common commodity operations.
 * The collection is accessible for more advanced operations using the specified getter.
 *
 * @param <T> The type of items in the collection.
 */
public class MongoRepository<T extends MongoItem> {

    @Getter
    private MongoCollection<T> collection;

    private Random random = new Random();


    /**
     * Default constructor.
     *
     * @param collection the collection to wrap.
     */
    public MongoRepository(MongoCollection<T> collection) {
        this.collection = collection;
    }

    /**
     * Insert or replace and object inside the collection. If the id of the object is null,
     * then an new id is created an the original object is mutated populating the id with
     * the generated value.
     *
     * @param item an object that extends {@link MongoItem}
     * @return the {@link UpdateResult} containing information on the outcome of the upsert.
     */
    public UpdateResult upsert(T item) {
        if (item.getId() == null) {
            item.setId(createId(item));
        }
        return collection.replaceOne
                (
                        eq("_id", item.getId()),
                        item,
                        new ReplaceOptions().upsert(true)
                );
    }

    /**
     * Update one and only one object inside the collection.
     * The first argument specify the query to select the object, the second the update to perform.
     *
     * @param query  specify the criteria to pick up the object from the db.
     * @param update the change to perform to the object.
     * @return the {@link UpdateResult} containing information on the outcome of the update.
     */
    public UpdateResult updateOne(Bson query, Bson update) {
        return collection.updateOne(query, update);
    }

    /**
     * Find and return an item from the collection.
     * Return null if not found.
     *
     * @param id the id of the item.
     * @return the item or null if not found
     */
    public T findById(String id) {
        return collection.find(eq("_id", id)).first();
    }


    /**
     * Remove an item from the collection.
     *
     * @param id the id of the item.
     * @return the {@link DeleteResult} containing information on the outcome of the upsert.
     */
    public DeleteResult deleteById(String id) {
        return collection.deleteOne(eq("_id", id));
    }


    /**
     * Find and return all items matching the provided filter.
     * Try to find usefull filters using the static members of {@link com.mongodb.client.model.Filters}
     *
     * @param filter the filter to match the items.
     * @return a FindIterable of the retrieved documents
     */
    public FindIterable<T> query(Bson filter) {
        return collection.find(filter);
    }


    /**
     * Find and return a page of items matching the provided filter.
     * Try to find usefull filters using the static members of {@link com.mongodb.client.model.Filters}
     *
     * @param filter    the Bson Document containing filter criteria
     * @param page      the Page object containing pagination infos
     * @param withCount if true the Page object is populated with total count
     * @return a FindIterable of the retrieved documents
     */
    public FindIterable<T> query(Bson filter, Page page, boolean withCount) { //@NonNull

        FindIterable<T> iterable = collection.find(filter);

        iterable = this.managePagination(iterable, page, withCount, filter);

        return iterable;
    }


    /**
     * Find and return a page of items.
     * Try to find usefull filters using the static members of {@link com.mongodb.client.model.Filters}
     *
     * @param page the Page object containing pagination infos
     * @return a FindIterable of the retrieved documents
     */
    public FindIterable<T> query(@NonNull Page page) {

        Bson sortingCriteria = null;
        if (null != page && !page.getSortBy().equalsIgnoreCase("")) {
            sortingCriteria =
                    page.getSortDirection().equalsIgnoreCase(Page.ASC) ?
                            Sorts.ascending(page.getSortBy())
                            :
                            Sorts.descending(page.getSortBy());
        }

        return collection.find()
                .sort(sortingCriteria)
                .skip(page.getItemPerPage() * page.getPage())
                .limit(page.getItemPerPage());
    }

    /**
     * Find and return a page of items matching the provided filter.
     * Try to find usefull filters using the static members of {@link com.mongodb.client.model.Filters}
     *
     * @param filter     the Bson Document containing filter criteria
     * @param page       the Page object containing pagination infos
     * @param projection the Bson Document containing projection criteria
     * @return a FindIterable of the retrieved documents
     */
    public FindIterable<T> query(Bson filter, @NonNull Page page, Bson projection) {
        return collection.find(filter)
                .limit(page.getItemPerPage())
                .skip(page.getItemPerPage() * page.getPage())
                .projection(projection);
    }


    /**
     * Find and return a page of items matching the provided filter.
     * Try to find usefull filters using the static members of {@link com.mongodb.client.model.Filters}
     *
     * @param filter              the Bson Document containing filter criteria
     * @param projectionFieldList the String array containing fields to keep
     * @return a FindIterable of the retrieved documents
     */
    public FindIterable<T> query(Bson filter, String[] projectionFieldList) {
        return collection.find(filter)
                .projection(Projections.include(projectionFieldList));
    }


    /**
     * Find and return the FIRST item matching the provided filter.
     * Try to find usefull filters using the static members of {@link com.mongodb.client.model.Filters}
     *
     * @param filter the filter to match the item.
     * @return the first item corresponding to the received filters
     */
    public T findOne(Bson filter) {
        return collection.find(filter).first();
    }


    /**
     * Find and return the FIRST item matching the provided filter.
     * Try to find usefull filters using the static members of {@link com.mongodb.client.model.Filters}
     *
     * @param filter     the filter to match the item.
     * @param projection the Bson Document containing projection criteria
     * @return the first item corresponding to the received filters
     */
    public T findOne(Bson filter, Bson projection) {
        return collection.find(filter).projection(projection).first();
    }


    /**
     * Find and return a page of items matching the provided filter.
     * Try to find usefull filters using the static members of {@link com.mongodb.client.model.Filters}
     *
     * @param aggregations the List of Bson Document containing aggregation criteria
     * @return a FindIterable of the retrieved documents
     */
    public AggregateIterable<T> aggregate(List<Bson> aggregations) {
        return collection.aggregate(aggregations);
    }


    /**
     * Commodity function to generate an id when the item needs one.
     * The id is the concatenation of epoch and the abs of the object hashcode.
     *
     * @param item
     * @return
     */
    private String createId(T item) {
        return new Date().getTime() + "_" + Math.abs(random.nextInt());//item.hashCode());
    }


    public FindIterable<T> selectSortLimit(Bson filter, String sortField, int limit, Boolean ascending) {

        if (ascending) {
            return collection.find(filter).sort(Sorts.ascending(sortField)).limit(limit);
        } else {
            return collection.find(filter).sort(Sorts.descending(sortField)).limit(limit);
        }
    }

    /**
     * Delete all documents from the collection
     */
    public void deleteAll() {
        collection.deleteMany(new Document());
    }

    /**
     * Insert a List of T in the collection
     *
     * @param list the List of element to insert
     */
    public void insertMany(List<T> list) {
        collection.insertMany(list);
    }

    /**
     * delete many documents responding to the Document containing selection criteria
     */
    public DeleteResult deleteMany(Bson filter) {
        return collection.deleteMany(filter);
    }

    /**
     * Check if a related query returns at least one document.
     *
     * @param criteria the query to check.
     * @return true if at least one document matches the query
     */
    public boolean existsAtLeastOne(Bson criteria) {
        return collection.countDocuments(criteria, new CountOptions().limit(1)) == 1L;
    }


    /**
     * gets the count corresponding to the selected filters, populates the received page object and returns it
     *
     * @param filters the bson filters to make the count
     * @param page    the page to populate with data
     * @return the populated page object
     */
    public Page count(Bson filters, Page page) {

        int docCount = Math.toIntExact(collection.countDocuments(filters));
        return page.setPagesNumber((int) Math.ceil((double) docCount / page.getItemPerPage()));
    }


    /**
     * manages pagination in the query
     * @param iterable
     * @param page
     * @return
     */
    private FindIterable<T> managePagination(FindIterable iterable, Page page, boolean withCount, Bson filters) {

        if (null != page) {

            if (page.getSortBy().equalsIgnoreCase("")) {
                Bson sortingCriteria =
                        page.getSortDirection().equalsIgnoreCase(Page.ASC) ?
                                Sorts.ascending(page.getSortBy())
                                :
                                Sorts.descending(page.getSortBy());

                iterable = iterable.sort(sortingCriteria);
            }

            iterable = iterable
                    .skip(page.getItemPerPage() * page.getPage())
                    .limit(page.getItemPerPage());

            if (withCount) {
                this.count(filters, page);
            }
        }

        return iterable;
    }
}
