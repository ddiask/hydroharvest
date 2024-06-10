package harvest;

import entity.Client;
import entity.CropsEnum;
import entity.SensorData;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.concurrent.ExecutionException;

@Path(UbiquitousAPI.PATH)
public interface UbiquitousAPI {

    String PATH="/ubiquitous";
    String ADD_DATA="/addData";
    String GET_INFORMATION="/getInformation";
    String PLANT="plant";


    @Path(ADD_DATA)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response addData(SensorData data) throws ExecutionException, InterruptedException;



    @Path(GET_INFORMATION)
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response getInformation(@QueryParam(PLANT) CropsEnum crop) throws ExecutionException, InterruptedException;



}
