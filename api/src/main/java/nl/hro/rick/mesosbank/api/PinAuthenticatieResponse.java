package nl.hro.rick.mesosbank.api;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by Rick on 15-6-2017.
 */
public class PinAuthenticatieResponse {
    @JsonProperty
    private boolean pin;

    @JsonProperty
    private int failedAttemps;

    @JsonProperty
    private int geblokeerdpas;

    public int getFailedAttemps() {
        return failedAttemps;
    }

    public void setFailedAttemps(int failedAttemps) {
        this.failedAttemps = failedAttemps;
    }

    public int getGeblokeerdpas() {
        return geblokeerdpas;
    }

    public void setGeblokeerdpas(int geblokeerdpas) {
        this.geblokeerdpas = geblokeerdpas;
    }

    public boolean isPin() {
        return pin;
    }

    public void setPin(boolean pin) {
        this.pin = pin;
    }
}
