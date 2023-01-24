package org.poc.mongo;

import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;

public class Countries extends BasicDBObject {
    private ObjectId _id;
    private String name;
    private String countryCode;
    private String District;
    private String Population;
    public Countries() {}

    public Countries(ObjectId _id, String name, String countryCode, String district, String population) {
        this._id = _id;
        this.name = name;
        this.countryCode = countryCode;
        District = district;
        Population = population;
    }
    public ObjectId  getId() {
        return _id;
    }
    public String getName() {
        return name;
    }
    public String getCountryCode() {
        return countryCode;
    }
    public String getDistrict() {
        return District;
    }
    public String getPopulation() {
        return Population;
    }
    public void setId(ObjectId  _id) {
        this._id = _id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    public void setDistrict(String district) {
        District = district;
    }
    public void setPopulation(String population) {
        Population = population;
    }
    @Override
    public String toString() {
        return "Countries{" +
                "id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", District='" + District + '\'' +
                ", Population='" + Population + '\'' +
                '}';
    }
}
