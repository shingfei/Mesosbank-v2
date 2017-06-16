package nl.hro.rick.mesosbank.server;

/**
 * Created by elvira on 13-03-17.
 */
public class MockDatabase implements Database
{
    @Override
    public long getBalance(String rekeningNr)
    {
        return 123456789;
    }

    @Override
    public long withdraw(String rekeningNr, long amount)
    {
        return 0;
    }

    @Override
    public boolean pasAuthenticatie(String Uid, String rekeningNr) {
        return false;
    }

    @Override
    public boolean pinAuthenticatie(String rekeningNr, String pin) {
        return false;
    }
}
