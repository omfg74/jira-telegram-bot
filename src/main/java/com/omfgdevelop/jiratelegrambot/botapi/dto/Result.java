
package com.omfgdevelop.jiratelegrambot.botapi.dto;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "url",
    "has_custom_certificate",
    "pending_update_count",
    "last_error_date",
    "last_error_message",
    "max_connections",
    "ip_address"
})
@Generated("jsonschema2pojo")
public class Result {

    @JsonProperty("url")
    private String url;
    @JsonProperty("has_custom_certificate")
    private Boolean hasCustomCertificate;
    @JsonProperty("pending_update_count")
    private Integer pendingUpdateCount;
    @JsonProperty("last_error_date")
    private Integer lastErrorDate;
    @JsonProperty("last_error_message")
    private String lastErrorMessage;
    @JsonProperty("max_connections")
    private Integer maxConnections;
    @JsonProperty("ip_address")
    private String ipAddress;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("has_custom_certificate")
    public Boolean getHasCustomCertificate() {
        return hasCustomCertificate;
    }

    @JsonProperty("has_custom_certificate")
    public void setHasCustomCertificate(Boolean hasCustomCertificate) {
        this.hasCustomCertificate = hasCustomCertificate;
    }

    @JsonProperty("pending_update_count")
    public Integer getPendingUpdateCount() {
        return pendingUpdateCount;
    }

    @JsonProperty("pending_update_count")
    public void setPendingUpdateCount(Integer pendingUpdateCount) {
        this.pendingUpdateCount = pendingUpdateCount;
    }

    @JsonProperty("last_error_date")
    public Integer getLastErrorDate() {
        return lastErrorDate;
    }

    @JsonProperty("last_error_date")
    public void setLastErrorDate(Integer lastErrorDate) {
        this.lastErrorDate = lastErrorDate;
    }

    @JsonProperty("last_error_message")
    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    @JsonProperty("last_error_message")
    public void setLastErrorMessage(String lastErrorMessage) {
        this.lastErrorMessage = lastErrorMessage;
    }

    @JsonProperty("max_connections")
    public Integer getMaxConnections() {
        return maxConnections;
    }

    @JsonProperty("max_connections")
    public void setMaxConnections(Integer maxConnections) {
        this.maxConnections = maxConnections;
    }

    @JsonProperty("ip_address")
    public String getIpAddress() {
        return ipAddress;
    }

    @JsonProperty("ip_address")
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
