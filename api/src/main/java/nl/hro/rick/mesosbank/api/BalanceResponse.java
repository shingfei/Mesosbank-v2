package nl.hro.rick.mesosbank.api;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by Rick on 4-4-2017.
 */
public class BalanceResponse
{
    @JsonProperty
    private long balans;
    @JsonProperty
    private String iban;


    public void setBalans(long balans) {
        this.balans = balans;
    }

    public long getBalans() {
        return balans;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }
}

