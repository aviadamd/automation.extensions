package org.poc.mySql;

import com.mongodb.BasicDBObject;

public class Countries extends BasicDBObject {
    private String id;
    private String name;
    private String countryCode;
    private String District;
    private String Population;

    public Countries() {}

    public Countries(String id, String name, String countryCode, String district, String population) {
        this.id = id;
        this.name = name;
        this.countryCode = countryCode;
        District = district;
        Population = population;
    }

    public String getId() {
        return id;
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

    public void setId(String id) {
        this.id = id;
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
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", District='" + District + '\'' +
                ", Population='" + Population + '\'' +
                '}';
    }
}
