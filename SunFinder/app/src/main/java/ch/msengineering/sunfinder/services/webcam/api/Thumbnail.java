package ch.msengineering.sunfinder.services.webcam.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Thumbnail implements Serializable {

    private final static long serialVersionUID = -2816114992923301472L;
    @SerializedName("width")
    @Expose
    private long width;
    @SerializedName("height")
    @Expose
    private long height;

    /**
     * No args constructor for use in serialization
     */
    public Thumbnail() {
    }

    /**
     * @param height
     * @param width
     */
    public Thumbnail(long width, long height) {
        super();
        this.width = width;
        this.height = height;
    }

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public Thumbnail withWidth(long width) {
        this.width = width;
        return this;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public Thumbnail withHeight(long height) {
        this.height = height;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("width", width).append("height", height).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(height).append(width).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Thumbnail) == false) {
            return false;
        }
        Thumbnail rhs = ((Thumbnail) other);
        return new EqualsBuilder().append(height, rhs.height).append(width, rhs.width).isEquals();
    }

}
