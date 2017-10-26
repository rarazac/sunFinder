package ch.msengineering.sunfinder.services.webcam.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Location implements Serializable {

    private static final long serialVersionUID = 1896523113426690664L;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("region_code")
    @Expose
    private String regionCode;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("continent")
    @Expose
    private String continent;
    @SerializedName("continent_code")
    @Expose
    private String continentCode;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("timezone")
    @Expose
    private String timezone;

    /**
     * No args constructor for use in serialization
     */
    public Location() {
    }

    /**
     * @param region
     * @param regionCode
     * @param timezone
     * @param continentCode
     * @param continent
     * @param countryCode
     * @param longitude
     * @param latitude
     * @param country
     * @param city
     */
    public Location(String city, String region, String regionCode, String country, String countryCode, String continent, String continentCode, double latitude, double longitude, String timezone) {
        super();
        this.city = city;
        this.region = region;
        this.regionCode = regionCode;
        this.country = country;
        this.countryCode = countryCode;
        this.continent = continent;
        this.continentCode = continentCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = timezone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Location withCity(String city) {
        this.city = city;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Location withRegion(String region) {
        this.region = region;
        return this;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public Location withRegionCode(String regionCode) {
        this.regionCode = regionCode;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Location withCountry(String country) {
        this.country = country;
        return this;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Location withCountryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public Location withContinent(String continent) {
        this.continent = continent;
        return this;
    }

    public String getContinentCode() {
        return continentCode;
    }

    public void setContinentCode(String continentCode) {
        this.continentCode = continentCode;
    }

    public Location withContinentCode(String continentCode) {
        this.continentCode = continentCode;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Location withLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Location withLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Location withTimezone(String timezone) {
        this.timezone = timezone;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("city", city).append("region", region).append("regionCode", regionCode).append("country", country).append("countryCode", countryCode).append("continent", continent).append("continentCode", continentCode).append("latitude", latitude).append("longitude", longitude).append("timezone", timezone).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(region).append(regionCode).append(timezone).append(continentCode).append(continent).append(countryCode).append(longitude).append(latitude).append(country).append(city).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Location)) {
            return false;
        }
        Location rhs = ((Location) other);
        return new EqualsBuilder().append(region, rhs.region).append(regionCode, rhs.regionCode).append(timezone, rhs.timezone).append(continentCode, rhs.continentCode).append(continent, rhs.continent).append(countryCode, rhs.countryCode).append(longitude, rhs.longitude).append(latitude, rhs.latitude).append(country, rhs.country).append(city, rhs.city).isEquals();
    }

}
