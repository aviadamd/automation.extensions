package org.poc.mongo;

import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.mongo.legacy.DocumentAdapter;

public class CountriesDtoAdapter extends BasicDBObject {
    private CountriesDtoAdapter() {}
    public static Document toDocument(Countries countries) {
        return DocumentAdapter.toDocument(countries
                .append("_id", countries.getId())
                .append("name", countries.getName())
                .append("population", countries.getPopulation())
                .append("countryCode", countries.getCountryCode()));
    }
}
