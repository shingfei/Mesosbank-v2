package nl.hro.rick.mesosbank.api;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by Rick on 4-4-2017.
 */
public class BalanceResponse
{
    @JsonProperty
    private long balance;
    @JsonProperty
    private String rekeningNummer;

    public BalanceResponse() {
    }

    public BalanceResponse(String rekeningNummer, long balance) {
        this.balance = balance;
        this.rekeningNummer = rekeningNummer;
    }

    public long getBalance() {
        return balance;
    }

    public String getRekeningNummer() {
        return rekeningNummer;
    }
}

