package nl.hro.rick.mesosbank.api;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by Rick on 14-6-2017.
 */
public class AuthenticatieResponse {

    @JsonProperty
    private boolean cardExists;

    public boolean isCardExists() {
        return cardExists;
    }

    public void setCardExists(boolean cardExists) {
        this.cardExists = cardExists;
    }
}
