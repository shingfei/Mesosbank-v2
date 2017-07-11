package nl.hro.rick.mesosbank.api;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by SF on 8-7-2017.
 */
public class PinAuthenticatieRequest
{
    @JsonProperty
    private String pin;
    @JsonProperty
    private String uid;

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getUid() {
        return uid;
    }

    public String getPin() {
        return pin;
    }
}

