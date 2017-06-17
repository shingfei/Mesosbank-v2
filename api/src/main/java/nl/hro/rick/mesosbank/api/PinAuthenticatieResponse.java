package nl.hro.rick.mesosbank.api;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by Rick on 15-6-2017.
 */
public class PinAuthenticatieResponse {
    @JsonProperty
    private boolean pinCorrect;


    public PinAuthenticatieResponse(boolean pinCorrect) {
        this.pinCorrect = pinCorrect;
    }

    public boolean isPinCorrect() {
        return pinCorrect;
    }

    public PinAuthenticatieResponse() {
    }
}
