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
        BalanceResponse br = new BalanceResponse(rekeningNummer, db.getBalance(rekeningNummer));

        return br;
    }

    @GET
    @Path("/withdraw/{rekeningNr}/{amount}")
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
    public WithdrawResponse withdraw(@PathParam("rekeningNr") String rekeningNummer,@PathParam("amount") Long amount)
    {
        System.out.println("Withdraw werkt~!");

        Database db = Server.getDatabase();
        WithdrawResponse wr = new WithdrawResponse("geslaagd", db.withdraw(rekeningNummer, amount));

        return wr;
    }

    @GET
    @Path("/validate/{rekeningNr}/{Uid}")
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
    public AuthenticatieResponse authenticatie (@PathParam("Uid") String Uid,@PathParam("rekeningNr")  String rekeningNr)
    {
        Database db = Server.getDatabase();
        AuthenticatieResponse ar = new AuthenticatieResponse( db.pasAuthenticatie(Uid, rekeningNr));

        return ar;
    }

    @GET
    @Path("/validatePin/{rekeningNr}/{pin}")
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
    public PinAuthenticatieResponse pinAuthenticatie (@PathParam("rekeningNr") String rekeningNr, @PathParam("pin") String pin)
    {
        Database db = Server.getDatabase();
        PinAuthenticatieResponse par = new PinAuthenticatieResponse( db.pinAuthenticatie(rekeningNr, pin));

        return par;
    }

}
