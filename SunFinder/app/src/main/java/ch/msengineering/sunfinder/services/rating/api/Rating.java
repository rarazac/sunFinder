package ch.msengineering.sunfinder.services.rating.api;

/**
 * Created by razac on 14.10.17.
 */

public class Rating {

    public String id; // contains the id of the webcam which was rated
    public int value; // contains the value of the rating, high rating = good

    public Rating(String id, int value){
        this.id = id;
        this.value = value;
    }

    // return value if value is not 0 otherwise return 0
    public int getRating(){
        return value != 0 ? value : 0;
    }

    // return id if id is not null otherwise return ""
    public String getId(){
        return id != null ? id : "";
    }
}
