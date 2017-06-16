package nl.hro.rick.mesosbank.server;

/**
 * Created by Rick on 24-3-2017.
 */
public interface Database
{
    long getBalance(String rekeningNr);
    long withdraw(String rekeningNr, long amount);
    boolean pasAuthenticatie(String Uid, String rekeningNr);
    boolean pinAuthenticatie(String rekeningNr, String pin);
}
