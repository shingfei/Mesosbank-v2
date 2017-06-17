package nl.hro.rick.mesosbank.api;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by Rick on 14-6-2017.
 */
public class AuthenticatieResponse {

        @JsonProperty
        private boolean pasExist;

        public AuthenticatieResponse (boolean pasExist)
        {
            this.pasExist = pasExist;
        }

        public boolean isPasExist() {
            return pasExist;
        }

        public boolean getPasExist() {
            return pasExist;
        }

        public AuthenticatieResponse() {
        }
}
