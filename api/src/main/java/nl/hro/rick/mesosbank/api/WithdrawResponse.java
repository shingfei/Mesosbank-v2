package nl.hro.rick.mesosbank.api;
import org.codehaus.jackson.annotate.JsonProperty;


/**
 * Created by Rick on 3-4-2017.
 */
public class WithdrawResponse
{
    @JsonProperty
    private boolean response;
    @JsonProperty
    private long newSaldo;
    @JsonProperty
    private long bedragGepint;
    @JsonProperty
    private String iban;


    public void setResponse(boolean response) {
        this.response = response;
    }

    public boolean isResponse() {
        return response;
    }

    public long getNewSaldo() {
        return newSaldo;
    }

    public void setNewSaldo(long newSaldo) {
        this.newSaldo = newSaldo;
    }

    public void setBedragGepint(long bedragGepint) {
        this.bedragGepint = bedragGepint;
    }

    public long getBedragGepint() {
        return bedragGepint;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getIban() {
        return iban;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
