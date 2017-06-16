package nl.hro.rick.mesosbank.server;


import java.sql.*;

/**
 * Created by Rick on 24-3-2017.
 */
public class Databaseimpl implements Database
{

    private Connection con;
    private String host;
    private String uName;
    private String uPass;
    private ResultSet rs;

    public Databaseimpl()
    {
        this.host = "jdbc:mysql://127.0.0.1:3306/MesosBank";
        this.uName = "root";
        this.uPass = "fafnirop";
        this.rs = null;
        this.connect();
    }

    private void connect()
    {
        try
        {
            System.out.println("Boven connect statement");
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(host, uName, uPass);
            System.out.println("Onder connect statement"+con.toString());
        }
        catch (Exception e)
        {
            System.out.println("Connection error "+e);
        }

    }

    @Override
    public long getBalance(String rekeningNr)
    {
        System.out.println("start getBalance");
        long saldo = 0;
        try {
            System.out.println("Boven statement Prepared statement"+con.toString());
            System.out.println("Het rekeningnummer is: "+rekeningNr);

            PreparedStatement ps = con.prepareStatement("SELECT Balans"
                    + " FROM Account"
                    + " WHERE Rekeningnmr = ?;");
            ps.setString(1, rekeningNr);
            rs = ps.executeQuery();
            System.out.println("Onder statement execute"+rs.toString());
            if (rs.first())
            {
                System.out.println("Vlak voor het krijgen vd balans");
                System.out.println(rs.toString());
                saldo = rs.getLong("Balans");
                System.out.println("Dit is het saldo" + saldo);
            }
        }
        catch (Exception e)
        {
            System.out.println("SQL error"+ e.toString());
        }

        return saldo;
    }
    @Override
    public long withdraw(String rekeningNr, long amount)
    {
        System.out.println("Start withdraw");
        long nieuweBalans = 0;
        try {
            long saldo = getBalance(rekeningNr);
                if (saldo > amount) {
                    //mogen pinnen
                    PreparedStatement ps = con.prepareStatement("UPDATE Account" + " SET Balans = ? "
                            + "WHERE Rekeningnmr = ?");
                    ps.setLong(1, (saldo - amount));
                    ps.setString(2,rekeningNr);
                    ps.execute();
                    nieuweBalans = getBalance(rekeningNr);
                    return nieuweBalans;
                }
                else
                    {
                        System.out.println("Saldo ontoerijkend!");
                        return nieuweBalans;
                    }
            }
        catch (Exception e)
        {
            System.out.println("SQL error"+ e.toString());
        }
        return nieuweBalans;
    }
    @Override
    public boolean pasAuthenticatie(String Uid, String rekeningNr)
    {
        System.out.println("authenticatie van de pas beginnen");
        try
        {
            PreparedStatement ps = con.prepareStatement("SELECT UID " + " FROM Account " + " WHERE UID = ? " + " AND Rekeningnmr = ? ");
            ps.setString(1, Uid);
            ps.setString(2, rekeningNr);

            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                return true;
            }
        }
        catch(Exception e){
            System.out.println("Authenticatie vd pas gaat fout!");
            e.printStackTrace();
        }
        return false;
    }

    public boolean pinAuthenticatie(String rekeningNr, String pin)
    {
        System.out.println("authenticatie van de pincode begint");
        try
        {

            PreparedStatement ps = con.prepareStatement("SELECT  Status " + " FROM Account " + " WHERE Rekeningnmr = ? " );
            ps.setString(1,rekeningNr);
            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                int status = rs.getInt("Status");
                System.out.println("Status is :"+status);
                if(status >= 3)
                {
                    System.out.println("Pas is geblockeerd! Neem contact op met de bank");
                    return false;
                }
                else
                    {
                        System.out.println("Begin met pincode checken");
                        PreparedStatement ps2 = con.prepareStatement("SELECT pincode " + "FROM Account " + " WHERE Rekeningnmr = ? " + " AND pincode = ?");
                        ps2.setString(1, rekeningNr);
                        ps2.setString(2, pin);
                        ResultSet rs2 = ps2.executeQuery();
                        System.out.println("Pincode is gechecked");
                        if (rs2.next())
                        {
                            PreparedStatement ps4 = con.prepareStatement("UPDATE Account "+ " SET Status = 0" + " WHERE Rekeningnmr = ?");
                            ps4.setString(1, rekeningNr);
                            ps4.execute();
                            System.out.println("Pincode klopt en status staat weer op 0 :)");
                            return true;
                        }
                        else
                            {
                                System.out.println("Boven status updaten");
                                PreparedStatement ps3 = con.prepareStatement("UPDATE Account "+ " SET Status = Status + 1" + " WHERE Rekeningnmr = ?");
                                ps3.setString(1, rekeningNr);
                                System.out.println("voor status execute");
                                ps3.execute();
                                System.out.println("Foute poging!, status is geupdate!");
                                return false;
                            }

                    }
            }
            else
                {
                    return false;
                }

        }
        catch(Exception e)
        {
            System.out.println("Authenticatie vd pincode gaat fout!");
        }
        return false;
    }
}
