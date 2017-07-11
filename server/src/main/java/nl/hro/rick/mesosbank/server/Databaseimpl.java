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
    public boolean withdraw(String rekeningNr, long amount) {
        try {
            long saldo = getBalance(rekeningNr);
            if (saldo >= amount) {
                System.out.println(("iban: {}\t saldo: {} "+rekeningNr+saldo));

                PreparedStatement ps = con.prepareStatement("UPDATE Account SET Balans = ? WHERE Rekeningnmr = ?");
                ps.setLong(1, (saldo - amount));
                ps.setString(2, rekeningNr);

                boolean rs = ps.execute();
                System.out.println("Nieuwe saldo: {}"+getBalance(rekeningNr));

                return true;
            }
            System.out.println("Saldo is ontoreikend");
            return false;
        }
        catch (SQLException e){
            System.out.println("execution of query withdraw failed"+ e);
        }
        return false;
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

    public boolean pinAuthenticatie(String uid, String pin){
        try{
            PreparedStatement ps = con.prepareStatement("SELECT blokkade FROM Account WHERE UID = ?");
            ps.setString(1, uid);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int blok = rs.getInt("blokkade");

            if(blok == 1){
                return false;
            }
            else {
                PreparedStatement ps2 = con.prepareStatement("SELECT pincode FROM Account WHERE UID = ? AND pincode = ?");
                ps2.setString(1, uid);
                ps2.setString(2, pin);

                ResultSet rs2 = ps2.executeQuery();

                if (rs2.next()){

                    PreparedStatement ps3 = con.prepareCall("UPDATE Account SET Status = 0 WHERE UID = ?");
                    ps3.setString(1,uid);
                    ps3.execute();

                    return true;
                }
                else {
                    int plus = getAttemps(uid);
                    plus+=1;

                    PreparedStatement ps4 = con.prepareCall("UPDATE Account SET Status = ? WHERE UID = ?");
                    ps4.setInt(1, plus);
                    ps4.setString(2,uid);
                    ps4.execute();




                    if (getAttemps(uid) >= 3){
                        PreparedStatement ps5 = con.prepareCall("UPDATE Account SET blokkade = 1 WHERE UID = ?");
                        ps5.setString(1,uid);
                        ps5.execute();
                        return false;
                    }
                    return false;
                }
            }


        }
        catch (SQLException e){
            System.out.println("query kon niet worden  1");
        }

        return false;
    }

    //@Override
    public int getAttemps(String uid){
        int pogingen = 0;
        try{
            PreparedStatement ps = con.prepareStatement("SELECT Status "+" FROM Account "+" WHERE UID = ?");
            ps.setString(1, uid);
            ResultSet rs = ps.executeQuery();
            rs.next();
            pogingen = rs.getInt("Status");
            return pogingen;
        }
        catch (SQLException e){
            System.out.println("query get attempts kon niet worden uitgevoerd 2");
        }

        return pogingen;
    }

    // @Override
    public int getblokkade(String uid){
        int blokkade = 0;
        try{
            PreparedStatement ps = con.prepareStatement("SELECT blokkade "+" FROM Account "+" where UID = ?");
            ps.setString(1, uid);
            ResultSet rs = ps.executeQuery();
            rs.next();
            blokkade = rs.getInt("blokkade");
            return blokkade;
        }
        catch (SQLException e){
            System.out.println("blokkade error");
        }

        return blokkade;
    }
}
