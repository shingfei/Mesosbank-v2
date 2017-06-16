

import nl.hro.rick.mesosbank.api.*;

import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import java.io.*;

/**
 * Created by rick on 24-03-17.
 */
public class MyClient {
    private Client client;
    private WebTarget target;

    private static String ID = "MESO0915328";
    private static String validate = "/validate/";
    private static String validatePin = "/validatePin/";
    private static String pincode = "/2222";
    private static String UID = "/94 F7 D4 5F";
    private static String balance = "/balance/";



    public MyClient(int port) {
        client = ClientBuilder.newClient().register(JacksonFeature.class);
        target = client.target("http://145.24.222.79:" + port);

    }

    public static void main(String[] args) throws IOException {
        MyClient client = new MyClient(8025);
        WithdrawRequest request = new WithdrawRequest();
        BalanceResponse balanceRequest = new BalanceResponse();
        request.setIBAN("MESO0915328");
        request.setAmount(10);
        PinAuthenticatieResponse pinresponse = new PinAuthenticatieResponse();
        AuthenticatieResponse validPas = new AuthenticatieResponse();
        System.out.println("Saldo op rekening nummer "+ID+ " heeft " +client.balance(balanceRequest.getRekeningNummer(), balanceRequest.getBalance()+" euro.");
        System.out.println("Saldo op rekening nummer "+ID+ " heeft " +client.withdraw(request.getIBAN(), request.getAmount()).getNewSaldo() + " euro.");
        System.out.println(client.pinAuthenticatieResponse(pinresponse.isPinCorrect(),request.getIBAN(),  );

        // System.out.println(client.authenticatieResponse(validPas.getPasExist(), UID,);
        //System.out.println(client.authenticatieResponse(request.));

        //System.out.println(client.authenticatieResponse());


    }

    public BalanceResponse balance(String IBAN) {
        BalanceResponse response = target
                .path(balance + ID)
                .request(MediaType.APPLICATION_JSON)
                .get(BalanceResponse.class);


        return response;
    }


    public WithdrawResponse withdraw(String IBAN, Long amount) {
        WithdrawResponse response = target
                .path("/withdraw/IBAN/amount")
                // .queryParam("IBAN",ID)
                .request()
                .get(WithdrawResponse.class);

        return response;
    }

    public AuthenticatieResponse authenticatieResponse(String UID, String rekeningNr) {
        AuthenticatieResponse response = target
                .path(validate + ID + UID)
                .request(MediaType.APPLICATION_JSON)
                .get(AuthenticatieResponse.class);

        return response;
    }

    public PinAuthenticatieResponse pinAuthenticatieResponse(String rekeningNr, String pincode) {
        PinAuthenticatieResponse response = target
                .path(validatePin + ID + pincode)
                .request(MediaType.APPLICATION_JSON)
                .get(PinAuthenticatieResponse.class);

        return response;
    }


}
