package org.poc.mySql;

import org.bson.types.ObjectId;

public class Country {

    private int _id;
    private String name;
    private String countryCode;
    private String district;
    private String population;

    public Country() {}

    public Country(int _id, String name, String countryCode, String district, String population) {
        this._id = _id;
        this.name = name;
        this.countryCode = countryCode;
        this.district = district;
        this.population = population;
    }

    public int getId() {
        return _id;
    }
    public String getName() {
        return name;
    }
    public String getCountryCode() {
        return countryCode;
    }
    public String getDistrict() {
        return district;
    }
    public String getPopulation() {
        return population;
    }

    public void setId(int  _id) {
        this._id = _id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public void setPopulation(String population) {
        this.population = population;
    }

    @Override
    public String toString() {
        return "Countries{" +
                "id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", District='" + district + '\'' +
                ", Population='" + population + '\'' +
                '}';
    }
}
