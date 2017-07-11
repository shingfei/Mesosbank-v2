package nl.hro.rick.mesosbank.server;

import nl.hro.rick.mesosbank.api.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by Rick on 24-3-2017.
 */

@Path("/")
public class BankEndpoint
{
    /*
    * Mogelijke Http methodes
    * GET           - get something from the server
    * POST          - create something new on the server
    * PUT           - update something on the server
    * DELETE        -remove something from the server
     */




    @GET
    @Path("/balance/{rekeningNr}")
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
    public BalanceResponse getSaldo(@PathParam("rekeningNr") String rekeningNummer)
    {
        System.out.println("Get Saldo werkt!");


        Database db = Server.getDatabase();
        BalanceResponse br = new BalanceResponse();
        br.setBalans(db.getBalance(rekeningNummer));
        br.setIban(rekeningNummer);
        return br;
    }

    @POST
    @Path("/withdraw")
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-g")
    @Consumes(MediaType.APPLICATION_JSON)
    public WithdrawResponse withdraw(WithdrawRequest request){
        WithdrawResponse response = new WithdrawResponse();
        Database db = Server.getDatabase();
        boolean b = db.withdraw(request.getIBAN(), request.getAmount());
        if (b == false){
            response.setResponse(false);
            response.setNewSaldo(db.getBalance(request.getIBAN()));

            System.out.println("pinnen is mislukt "+ response.isResponse());
            return response;
        }
        else {
            response.setResponse(true);
            response.setNewSaldo(db.getBalance(request.getIBAN()));
            response.setBedragGepint(request.getAmount());
            response.setIban(request.getIBAN());
            return response;

        }
    }

    @POST
    @Path("/verifycard")
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-g")
    @Consumes(MediaType.APPLICATION_JSON)
    public AuthenticatieResponse verifycard(AuthenticatieRequest request){
        AuthenticatieResponse response = new AuthenticatieResponse();

        Database db = Server.getDatabase();

        if (db.pasAuthenticatie(request.getUid(), request.getIban()) == true){
            response.setCardExists(true);
            return response;
        }

        response.setCardExists(false);
        return response;
    }

    @POST
    @Path("/verifypin")
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-g")
    @Consumes(MediaType.APPLICATION_JSON)
    public PinAuthenticatieResponse verifypin(PinAuthenticatieRequest request){
        PinAuthenticatieResponse response = new PinAuthenticatieResponse();
        Database db = Server.getDatabase();

        if(db.pinAuthenticatie(request.getUid(), request.getPin()) == true){
            response.setPin(true);
            return response;
        }
        else {
            response.setFailedAttemps(db.getAttemps(request.getUid()));
            response.setGeblokeerdpas(db.getblokkade(request.getUid()));
            return response;
        }


    }

}
