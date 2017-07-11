package nl.hro.rick.mesosbank.api;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by SF on 8-7-2017.
 */
public class AuthenticatieRequest
{
    @JsonProperty
    private String uid;

    @JsonProperty
    private String iban;
    public String getIban() {
        return iban;
    }

    public String getUid() {
        return uid;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
