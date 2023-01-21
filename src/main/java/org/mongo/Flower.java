package org.mongo;

import org.bson.types.ObjectId;
import java.util.List;

public class Flower {
    private ObjectId id;
    private String name;
    private List<String> colors;
    private Boolean isBlooming;
    private Float height;

    public Flower() { }
    public Flower(ObjectId id, String name, List<String> colors, Boolean isBlooming, Float height) {
        this.id = id;
        this.name = name;
        this.colors = colors;
        this.isBlooming = isBlooming;
        this.height = height;
    }

    public ObjectId getId() { return id; }

    public void setId(ObjectId id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Boolean getIsBlooming() {
        return isBlooming;
    }

    public void setIsBlooming(Boolean isBlooming) {
        this.isBlooming = isBlooming;
    }
    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }
    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    @Override
    public String toString() {
        return "Flower [id=" + id + ", name=" + name + ", colors=" + colors + ", isBlooming=" + isBlooming + ", height=" + height + "]";
    }

}
