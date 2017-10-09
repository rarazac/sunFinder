package ch.msengineering.sunfinder.services.webcam.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Webcam implements Serializable {

    private final static long serialVersionUID = 2558334477494528796L;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("image")
    @Expose
    private Image image;
    @SerializedName("location")
    @Expose
    private Location location;

    /**
     * No args constructor for use in serialization
     */
    public Webcam() {
    }

    /**
     * @param id
     * @param title
     * @param location
     * @param status
     * @param image
     */
    public Webcam(String id, String status, String title, Image image, Location location) {
        super();
        this.id = id;
        this.status = status;
        this.title = title;
        this.image = image;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Webcam withId(String id) {
        this.id = id;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Webcam withStatus(String status) {
        this.status = status;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Webcam withTitle(String title) {
        this.title = title;
        return this;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Webcam withImage(Image image) {
        this.image = image;
        return this;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Webcam withLocation(Location location) {
        this.location = location;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("status", status).append("title", title).append("image", image).append("location", location).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(title).append(location).append(status).append(image).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Webcam) == false) {
            return false;
        }
        Webcam rhs = ((Webcam) other);
        return new EqualsBuilder().append(id, rhs.id).append(title, rhs.title).append(location, rhs.location).append(status, rhs.status).append(image, rhs.image).isEquals();
    }

}