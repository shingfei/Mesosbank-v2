
import nl.hro.rick.mesosbank.api.*;
import nl.hro.rick.mesosbank.server.Databaseimpl;
import org.glassfish.jersey.jackson.JacksonFeature;
import javax.swing.*;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

/**
 * Created by rick on 24-03-17.
 */
public class MyClient {

    static private Client client;
    private WebTarget target;
    private static String ID = "";
    private static String validate = "/validate/";
    private static String validatePin = "/validatePin/";
    private static String pincode = "/2222";
    private static String UID = "/ 94 f7 d4 5f";
    private static String balance = "/balance/";
    private static String amountMoney = "/10";
    static WithdrawRequest request = new WithdrawRequest();
    static BalanceResponse balanceRequest = new BalanceResponse();
    static PinAuthenticatieResponse pinresponse = new PinAuthenticatieResponse();
    static AuthenticatieResponse validPas = new AuthenticatieResponse();


    static long s;

    public MyClient(int port) {
        client = ClientBuilder.newClient().register(JacksonFeature.class);
        target = client.target("http://145.24.222.79:" + port);

    }

    public static void main(String[] args){
<<<<<<< HEAD

        SwingUtilities.invokeLater(new Runnable()
=======
       /* SwingUtilities.invokeLater(new Runnable()
>>>>>>> origin/master
        {

            public void run()
            {
               new ReadCard(8025);
            }
        });
<<<<<<< HEAD
          //MyClient client = new MyClient(8025);
//        System.out.println("Saldo op rekening nummer "+ID+ " heeft " +client.balance(request.getIBAN(),request.getAmount()).getBalance()+" euro.");
=======
          MyClient client = new MyClient(8025);*/
//           System.out.println("Saldo op rekening nummer "+ID+ " heeft " +client.balance(request.getIBAN(),request.getAmount()).getBalance()+" euro.");
>>>>>>> origin/master
//        System.out.println("Saldo op rekening nummer "+ID+ " heeft " +client.withdraw(request.getIBAN(), request.getAmount()).getNewSaldo() + " euro.");
//        System.out.println(client.balance(request.getIBAN(), request.getAmount()).getRekeningNummer());
//        System.out.println(client.authenticatie(UID,balanceRequest.getRekeningNummer()))
//  System.out.println(client.balance(request.getIBAN(),request.getAmount()).getRekeningNummer());
        //System.out.println("Saldo op rekening nummer "+ID+ " heeft " +client.balance(request.getIBAN(),request.getAmount()).getBalance()+" euro.");
//        System.out.println(client.pinAuthenticatie(balanceRequest.getRekeningNummer(),pincode).isPinCorrect());
    }


    public BalanceResponse balance(String IBAN, long amount) {
        BalanceResponse response = target
                .path(balance + ID)
                .request(MediaType.APPLICATION_JSON)
                .get(BalanceResponse.class);


        return response;
    }

    public WithdrawResponse withdraw(String IBAN, Long amount) {
        WithdrawResponse response = target
                .path("/withdraw/"+ID+ amountMoney)
                .request()
                .get(WithdrawResponse.class);

        return response;
    }

    public AuthenticatieResponse authenticatie(String UID, String rekeningNr) {
        AuthenticatieResponse response = target
                .path(validate + ID + UID)
                .request(MediaType.APPLICATION_JSON)
                .get(AuthenticatieResponse.class);

        return response;
    }

    public PinAuthenticatieResponse pinAuthenticatie(String rekeningNr, String pincode) {
        PinAuthenticatieResponse response = target
                .path(validatePin + ID + pincode)
                .request(MediaType.APPLICATION_JSON)
                .get(PinAuthenticatieResponse.class);

        return response;
    }



}
