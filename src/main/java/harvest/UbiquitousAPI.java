package harvest;

import entity.Client;
import entity.SensorData;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.concurrent.ExecutionException;

@Path(UbiquitousAPI.PATH)
public interface UbiquitousAPI {

    String PATH="/ubiquitous";
    String ADD_DATA="/addData";

    @Path(ADD_DATA)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    Response addData(SensorData data) throws ExecutionException, InterruptedException;



}
