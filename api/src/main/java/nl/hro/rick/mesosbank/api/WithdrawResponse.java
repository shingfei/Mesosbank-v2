package nl.hro.rick.mesosbank.api;
import org.codehaus.jackson.annotate.JsonProperty;


/**
 * Created by Rick on 3-4-2017.
 */
public class WithdrawResponse
{
    @JsonProperty
    private String response;
    @JsonProperty
    private long newSaldo;

    public WithdrawResponse() {
    }

    public WithdrawResponse(String response, long newSaldo) {
        this.response = response;
        this.newSaldo = newSaldo;

    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public long getNewSaldo() {
        return newSaldo;
    }

    public void setNewSaldo(long newSaldo) {
        this.newSaldo = newSaldo;
    }
}
