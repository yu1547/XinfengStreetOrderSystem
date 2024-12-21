package ntou.cs.XinfengStreetOrderSystem.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "menuItems")
public class MenuItem {
    @Id
    private String id;
    private String name;
    private String description="";
    private String image="";
    private double price;
    private String category="";
    private boolean isAvailable=true;
    private String setContents = "";

    public MenuItem() {
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.category = category;
        this.isAvailable = isAvailable;
        this.setContents= setContents;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getImage(){
        return image;
    }

    public void setImage(String image){
        this.image = image;
    }

    public double getPrice(){
        return price;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public Boolean getIsAvailable(){
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable){
        this.isAvailable = isAvailable;
    }

    public String getSetContents(){
        return setContents;
    }

    public void setSetContents(String setContents){
        this.setContents = setContents;
    }
}