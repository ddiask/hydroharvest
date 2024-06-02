package harvest;

import com.mashape.unirest.http.exceptions.UnirestException;
import entity.Client;
import entity.Crop;
import entity.CropsEnum;
import entity.System;
import entity.Water;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Path(ClientAPI.PATH)
public interface ClientAPI {

    String PATH = "/mobile";
    String ADD_CLIENT="/addClient";
    String ADD_CROP="/addCrop";
    String ADD_USER_CROP="/addUserCrop";
    String CHANGE_NAME="/changeName";
    String CHANGE_NAME_SYSTEM="/changeSystemName";
    String ADD_WATER="/addWater";
    String GET_WATER="/getWater";
    String GET_ALL_CROPS="/getAllCrops";
    String GET_INFORMATION="/getInformation";
    String GET_WEATHER="/getWeather";
    String WATERING_FORECAST="/wateringForecast";
    String USER_EXISTS="/userExists";
    String ADD_SYSTEM="/addSystem";
    String CROP_ID="cropId";
    String SYSTEM_IP="systemIp";
    String NAME="name";
    String USER_ID="userId";
    String USER_ID_TO_ADD="userIdToAdd";
    String PASSWORD="password";
    String PLANT="plant";
    String REGION="region";

    @Path(ADD_CLIENT)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    Response addClient(Client client) throws ExecutionException, InterruptedException;

    @Path(ADD_CROP)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    Response addCrop(Crop crop) throws ExecutionException, InterruptedException;

    @Path(ADD_USER_CROP+"/{"+CROP_ID+"}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    Response addUserToCrop(@PathParam(CROP_ID) String systemId,
                           @QueryParam(USER_ID_TO_ADD)String userIdToAdd,
                           @QueryParam(USER_ID) String userId,
                           @QueryParam(PASSWORD) String password) throws ExecutionException, InterruptedException;

    @Path(CHANGE_NAME+"/{"+CROP_ID+"}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    Response changeName(@PathParam(CROP_ID) String systemId,
                        @QueryParam(USER_ID)String userId,
                        @QueryParam(PASSWORD) String password,
                        @QueryParam(NAME)String name) throws ExecutionException, InterruptedException;

    @Path(CHANGE_NAME_SYSTEM+"/{"+CROP_ID+"}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    Response changeSystemName(@PathParam(CROP_ID) String systemId,
                        @QueryParam(USER_ID)String userId,
                        @QueryParam(PASSWORD) String password,
                        @QueryParam(SYSTEM_IP) String ip,
                        @QueryParam(NAME)String name) throws ExecutionException, InterruptedException;

    @Path(ADD_WATER+"/{"+CROP_ID+"}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    Response addWater(@PathParam(CROP_ID) String systemId,
                      @QueryParam(USER_ID)String userId,
                      @QueryParam(PASSWORD) String password,
                      Water water) throws ExecutionException, InterruptedException;

    @Path(GET_INFORMATION)
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response getInformation(@QueryParam(PLANT) CropsEnum crop,
                      @QueryParam(USER_ID)String userId,
                      @QueryParam(PASSWORD) String password
                      ) throws ExecutionException, InterruptedException;

    @Path(GET_WATER+"/{"+CROP_ID+"}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response getWater(@PathParam(CROP_ID) String systemId,
                      @QueryParam(USER_ID)String userId,
                      @QueryParam(PASSWORD) String password,
                      @QueryParam(SYSTEM_IP) String ip
                      ) throws ExecutionException, InterruptedException;

    @Path(GET_ALL_CROPS)
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response getAllCrops(@QueryParam(USER_ID)String userId,
                         @QueryParam(PASSWORD) String password
                    ) throws ExecutionException, InterruptedException;

    @Path(GET_WEATHER)
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response getWeather(@QueryParam(REGION)String region
    ) throws ExecutionException, InterruptedException, IOException, UnirestException;

    @Path(WATERING_FORECAST+"/{"+CROP_ID+"}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response wateringForecast(@PathParam(CROP_ID) String systemId,
                              @QueryParam(USER_ID)String userId,
                              @QueryParam(PASSWORD) String password,
                              @QueryParam(SYSTEM_IP) String ip) throws ExecutionException, InterruptedException;


    @Path(USER_EXISTS)
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response userExists(@QueryParam(USER_ID)String userId) throws ExecutionException, InterruptedException;

    @Path(ADD_SYSTEM+"/{"+CROP_ID+"}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response addSystem(@PathParam(CROP_ID) String systemId,
                       @QueryParam(USER_ID)String userId,
                       @QueryParam(PASSWORD) String password,
                       System system) throws ExecutionException, InterruptedException;
}
