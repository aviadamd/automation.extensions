package org.poc.mongo.pojos;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

@Entity("ingredients")
public class Ingredient {
    @Id
    private ObjectId id;
    private String name;
    private boolean healthy;

    public Ingredient() { }

    public Ingredient(ObjectId id, String name, boolean healthy) {
        this.id = id;
        this.name = name;
        this.healthy = healthy;
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean getHealthy() { return healthy; }
    public void setHealthy(boolean healthy) { this.healthy = healthy; }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", healthy=" + healthy +
                '}';
    }
}
