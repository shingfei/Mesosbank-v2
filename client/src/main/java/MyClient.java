

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import nl.hro.rick.mesosbank.api.*;

import org.glassfish.jersey.jackson.JacksonFeature;

import javax.swing.*;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import java.awt.*;
import java.io.*;
import java.rmi.server.UID;
import java.util.Enumeration;

/**
 * Created by rick on 24-03-17.
 */
public class MyClient {
   static private Client client;
    private WebTarget target;
    private static String ID = "MESO0915328";
    private static String validate = "/validate/";
    private static String validatePin = "/validatePin/";
    private static String pincode = "/2222";
    private static String UID = "/94 F7 D4 5F";
    private static String balance = "/balance/";
    private static String amountMoney = "/10";
    static WithdrawRequest request = new WithdrawRequest();
    static BalanceResponse balanceRequest = new BalanceResponse();
    static PinAuthenticatieResponse pinresponse = new PinAuthenticatieResponse();
    static AuthenticatieResponse validPas = new AuthenticatieResponse();
    static long s;
    private static CardLayout cardTest = new CardLayout();

    public MyClient(int port) {
        client = ClientBuilder.newClient().register(JacksonFeature.class);
        target = client.target("http://145.24.222.79:" + port);

    }

    public static void main(String[] args){

        SwingUtilities.invokeLater(new Runnable()
        {

            public void run()
            {
                new ReadCard(8025);
            }
        });
//        MyClient client = new MyClient(8025);
         //  System.out.println("Saldo op rekening nummer "+ID+ " heeft " +client.balance(request.getIBAN(),request.getAmount()).getBalance()+" euro.");
//        System.out.println("Saldo op rekening nummer "+ID+ " heeft " +client.withdraw(request.getIBAN(), request.getAmount()).getNewSaldo() + " euro.");
//        System.out.println(client.authenticatie(UID,request.getIBAN()));
//        System.out.println(client.pinAuthenticatie(request.getIBAN(),pincode));
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
