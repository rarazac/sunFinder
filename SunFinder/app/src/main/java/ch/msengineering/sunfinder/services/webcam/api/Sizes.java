package ch.msengineering.sunfinder.services.webcam.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Sizes implements Serializable {

    private final static long serialVersionUID = -6252459329507585918L;
    @SerializedName("icon")
    @Expose
    private Icon icon;
    @SerializedName("thumbnail")
    @Expose
    private Thumbnail thumbnail;
    @SerializedName("preview")
    @Expose
    private Preview preview;
    @SerializedName("toenail")
    @Expose
    private Toenail toenail;

    /**
     * No args constructor for use in serialization
     */
    public Sizes() {
    }

    /**
     * @param icon
     * @param thumbnail
     * @param preview
     * @param toenail
     */
    public Sizes(Icon icon, Thumbnail thumbnail, Preview preview, Toenail toenail) {
        super();
        this.icon = icon;
        this.thumbnail = thumbnail;
        this.preview = preview;
        this.toenail = toenail;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public Sizes withIcon(Icon icon) {
        this.icon = icon;
        return this;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Sizes withThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public Preview getPreview() {
        return preview;
    }

    public void setPreview(Preview preview) {
        this.preview = preview;
    }

    public Sizes withPreview(Preview preview) {
        this.preview = preview;
        return this;
    }

    public Toenail getToenail() {
        return toenail;
    }

    public void setToenail(Toenail toenail) {
        this.toenail = toenail;
    }

    public Sizes withToenail(Toenail toenail) {
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
        if ((other instanceof Sizes) == false) {
            return false;
        }
        Sizes rhs = ((Sizes) other);
        return new EqualsBuilder().append(icon, rhs.icon).append(thumbnail, rhs.thumbnail).append(preview, rhs.preview).append(toenail, rhs.toenail).isEquals();
    }

}
