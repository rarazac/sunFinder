package ch.msengineering.sunfinder.services.webcam.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Image implements Serializable {

    private final static long serialVersionUID = -4775850294462157118L;
    @SerializedName("current")
    @Expose
    private Current current;
    @SerializedName("daylight")
    @Expose
    private Daylight daylight;
    @SerializedName("sizes")
    @Expose
    private Sizes sizes;
    @SerializedName("update")
    @Expose
    private long update;

    /**
     * No args constructor for use in serialization
     */
    public Image() {
    }

    /**
     * @param update
     * @param sizes
     * @param current
     * @param daylight
     */
    public Image(Current current, Daylight daylight, Sizes sizes, long update) {
        super();
        this.current = current;
        this.daylight = daylight;
        this.sizes = sizes;
        this.update = update;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public Image withCurrent(Current current) {
        this.current = current;
        return this;
    }

    public Daylight getDaylight() {
        return daylight;
    }

    public void setDaylight(Daylight daylight) {
        this.daylight = daylight;
    }

    public Image withDaylight(Daylight daylight) {
        this.daylight = daylight;
        return this;
    }

    public Sizes getSizes() {
        return sizes;
    }

    public void setSizes(Sizes sizes) {
        this.sizes = sizes;
    }

    public Image withSizes(Sizes sizes) {
        this.sizes = sizes;
        return this;
    }

    public long getUpdate() {
        return update;
    }

    public void setUpdate(long update) {
        this.update = update;
    }

    public Image withUpdate(long update) {
        this.update = update;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("current", current).append("daylight", daylight).append("sizes", sizes).append("update", update).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(update).append(sizes).append(current).append(daylight).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Image) == false) {
            return false;
        }
        Image rhs = ((Image) other);
        return new EqualsBuilder().append(update, rhs.update).append(sizes, rhs.sizes).append(current, rhs.current).append(daylight, rhs.daylight).isEquals();
    }

}
