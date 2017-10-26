package ch.msengineering.sunfinder.services.rating.api;

/**
 * Created by razac on 14.10.17.
 */

public class Rating {

    private String id; // contains the id of the webcam which was rated
    private int ratingValue; // contains the value of the rating, high rating = good
    private int timeStamp;

    public Rating() {
        // Default constructor required for calls to DataSnapshot.getValue(Rating.class)
    }

    public Rating(String id, int ratingValue, int timeStamp) {
        this.id = id;
        this.ratingValue = ratingValue;
        this.timeStamp = timeStamp;
    }

    // return value if value is not 0 otherwise return 0
    public int getRatingValue() {
        return ratingValue != 0 ? ratingValue : 0;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public int getTimeStamp() {
        return timeStamp != 0 ? timeStamp : 0;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    // return id if id is not null otherwise return ""
    public String getId() {
        return id != null ? id : "";
    }

    public void setId(String id) {
        this.id = id;
    }
}
