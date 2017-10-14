

package ch.msengineering.sunfinder.services.rating.api;

/**
 * Created by razac on 14.10.17.
 */

public class Rating {

    private final String id;
    private final int value;

    public Rating(String id, int value){
        this.id = id;
        this.value = value;
    }

    public int getRating(){
        // return value if value is not 0 otherwise return 0
        return value != 0 ? value : 0;
    }
    public String getId(){
        // return id if id is not null otherwise return ""
        return id != null ? id : "";
    }
}
