package ch.msengineering.sunfinder.services.webcam.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class WebCamNearby implements Serializable {

    private static final long serialVersionUID = -5083114106067000958L;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("result")
    @Expose
    private Result result;

    /**
     * No args constructor for use in serialization
     */
    public WebCamNearby() {
    }

    /**
     * @param result
     * @param status
     */
    public WebCamNearby(String status, Result result) {
        super();
        this.status = status;
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public WebCamNearby withStatus(String status) {
        this.status = status;
        return this;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public WebCamNearby withResult(Result result) {
        this.result = result;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("status", status).append("result", result).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(result).append(status).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof WebCamNearby)) {
            return false;
        }
        WebCamNearby rhs = ((WebCamNearby) other);
        return new EqualsBuilder().append(result, rhs.result).append(status, rhs.status).isEquals();
    }

}
