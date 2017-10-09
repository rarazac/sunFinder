package ch.msengineering.sunfinder.services.webcam.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Result implements Serializable {

    private final static long serialVersionUID = 4476641959610140120L;
    @SerializedName("offset")
    @Expose
    private long offset;
    @SerializedName("limit")
    @Expose
    private long limit;
    @SerializedName("total")
    @Expose
    private long total;
    @SerializedName("webcams")
    @Expose
    private List<Webcam> webcams = new ArrayList<Webcam>();

    /**
     * No args constructor for use in serialization
     */
    public Result() {
    }

    /**
     * @param total
     * @param limit
     * @param webcams
     * @param offset
     */
    public Result(long offset, long limit, long total, List<Webcam> webcams) {
        super();
        this.offset = offset;
        this.limit = limit;
        this.total = total;
        this.webcams = webcams;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public Result withOffset(long offset) {
        this.offset = offset;
        return this;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public Result withLimit(long limit) {
        this.limit = limit;
        return this;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Result withTotal(long total) {
        this.total = total;
        return this;
    }

    public List<Webcam> getWebcams() {
        return webcams;
    }

    public void setWebcams(List<Webcam> webcams) {
        this.webcams = webcams;
    }

    public Result withWebcams(List<Webcam> webcams) {
        this.webcams = webcams;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("offset", offset).append("limit", limit).append("total", total).append("webcams", webcams).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(total).append(limit).append(webcams).append(offset).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Result) == false) {
            return false;
        }
        Result rhs = ((Result) other);
        return new EqualsBuilder().append(total, rhs.total).append(limit, rhs.limit).append(webcams, rhs.webcams).append(offset, rhs.offset).isEquals();
    }

}
