package nl.hro.rick.mesosbank.api;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by Rick on 3-4-2017.
 */
public class WithdrawRequest
{
    @JsonProperty
    private String IBAN;
    @JsonProperty
    private long amount;

    public WithdrawRequest() {
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getIBAN() {
        return IBAN;
    }

    public long getAmount() {
        return amount;
    }
}
