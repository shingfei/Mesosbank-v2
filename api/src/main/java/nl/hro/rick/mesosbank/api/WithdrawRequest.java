package nl.hro.rick.mesosbank.api;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by Rick on 3-4-2017.
 */
public class WithdrawRequest
{
    @JsonProperty
    private String iban;
    @JsonProperty
    private long amount;

    public WithdrawRequest() {
    }

    public void setIBAN(String iban) {
        this.iban = iban;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getIBAN() {
        return iban;
    }

    public long getAmount() {
        return amount;
    }
}
