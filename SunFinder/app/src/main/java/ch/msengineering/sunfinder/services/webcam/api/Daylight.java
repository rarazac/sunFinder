package ch.msengineering.sunfinder.services.webcam.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Daylight implements Serializable {

    private final static long serialVersionUID = -5979141066603028297L;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("preview")
    @Expose
    private String preview;
    @SerializedName("toenail")
    @Expose
    private String toenail;

    /**
     * No args constructor for use in serialization
     */
    public Daylight() {
    }

    /**
     * @param icon
     * @param thumbnail
     * @param preview
     * @param toenail
     */
    public Daylight(String icon, String thumbnail, String preview, String toenail) {
        super();
        this.icon = icon;
        this.thumbnail = thumbnail;
        this.preview = preview;
        this.toenail = toenail;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Daylight withIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Daylight withThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public Daylight withPreview(String preview) {
        this.preview = preview;
        return this;
    }

    public String getToenail() {
        return toenail;
    }

    public void setToenail(String toenail) {
        this.toenail = toenail;
    }

    public Daylight withToenail(String toenail) {
        this.toenail = toenail;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("icon", icon).append("thumbnail", thumbnail).append("preview", preview).append("toenail", toenail).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(icon).append(thumbnail).append(preview).append(toenail).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Daylight) == false) {
            return false;
        }
        Daylight rhs = ((Daylight) other);
        return new EqualsBuilder().append(icon, rhs.icon).append(thumbnail, rhs.thumbnail).append(preview, rhs.preview).append(toenail, rhs.toenail).isEquals();
    }

}
