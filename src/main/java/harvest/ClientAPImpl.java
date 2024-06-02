package harvest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import entity.*;
import entity.System;
import firebase.FirebaseClass;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;
import ipma.ForecastData;
import ipma.Regions;
import ipma.WeatherForecast;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Startup;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static harvest.UbiquitousAPImpl.getPeriod;


@ApplicationScoped
public class ClientAPImpl implements ClientAPI{

     private static final String  NAME="Name";
    private static final String USERS="Users";
    private static final String MAIL="Mail";
    private static final String PASSWORD="Password";
    private static final String CROPS="Crops";
    private static final String CROP="Crop";
    private static final String SYSTEMS="Systems";
    private static final String SYSTEM="System";
    private static final String WATERING="Watering";
    private static final String CULTURE="Culture";
    private static final String LOCATION="location";
    private static final String MOST_RECENT="MostRecent";
    private static final String DONE="Done";
    private static final String Begin="Begin";
    private static final String END="End";
    private static final String IMAGE="Image";
    private static final String MAX_HUMIDITY="Max_Humidity";
    private static final String PERIOD="Period";
    private static final String MAX_TEMPERATURE="Max_Temperature";
    private static final String MIN_TEMPERATURE="Min_Temperature";
    private static final String MIN_HUMIDITY="Min_Humidity";
    private static final String DATA="Data";
    private static final String NO_DATA="There is no data";
    private static final String GOOD="Good";
    private static final String OK="Ok";
    private static final String BAD="BAD";
    private static final String CRITICAL="Critical";
    private static final String HUMIDITY_LEVEL="humidityLevel";
    private static final String LIGHT_LEVEL="lightLevel";
    private static final String TANK_LEVEL="tankLevel";
    private static final String TEMPERATURE_LEVEL="temperatureLevel";
    private static final int MAX_DAYS=3;
    private static final String MORNING="morning";
    private static final String MORNING_YESTERDAY="yesterday_morning";
    private static final String AFTERNOON="afternoon";
    private static final String AFTERNOON_YESTERDAY="yesterday_afternoon";
    private static final String EVENING="evening";
    private static final String EVENING_YESTERDAY="yesterday_evening";
    private static final String NIGHT="night";
    private static final String NIGHT_YESTERDAY="yesterday_night";
    private static final String NO_DATA_TO_PREDICT="There is not enough data to forecast watering.";
    private final FirebaseClass instance= FirebaseClass.getInstance();
    private final Map<String,Integer> userCredentials=new HashMap<>();
    private final Map<String,CropInformation> cropInformationMap=new HashMap<>();
    HashMap<Integer, HashMap<String,ForecastData>> weather=new HashMap<>(35);

    public ClientAPImpl() throws IOException {
    }

    public static String transformEnumToString(CropsEnum cropsEnum){
        String crop=cropsEnum.toString();
        return crop.replace("_"," ");
    }
    public static CropsEnum transformStringToEnum(String crop){
        return CropsEnum.valueOf(crop.replace(" ","_"));
    }

    private int calculateState(String crop, double humidity, double temperature, double tank) throws ExecutionException, InterruptedException {
        CropInformation inf= cropInformationMap.get(crop);
        if (inf==null){
            DocumentReference information= instance.db.collection(CULTURE).document(crop);
            DocumentSnapshot ds=information.get().get();
            String image= (String) ds.get(IMAGE);
            Long maxTemperature= (Long) ds.get(MAX_TEMPERATURE);
            Long minTemperature= (Long) ds.get(MIN_TEMPERATURE);
            Long maxHumidity= (Long) ds.get(MAX_HUMIDITY);
            Long minHumidity= (Long) ds.get(MIN_HUMIDITY);
            inf= new CropInformation(image,
                    maxHumidity,minHumidity,maxTemperature,minTemperature);
            cropInformationMap.put(crop,inf);
        }
        int points=0;
        if(humidity>=inf.getMinHumidity() && humidity<=inf.getMaxHumidity())
            points++;
        if(temperature>=inf.getMinTemperature() && temperature<=inf.getMaxTemperature())
            points++;
        if(tank>=50)
            points++;
        return points;
    }

    private String convertPointsToStatus(int points){
        if(points==3)
            return GOOD;
        else if (points==2) {
            return OK;
        } else if (points==1) {
            return BAD;
        }else
            return CRITICAL;
    }

    @Override
    public Response addClient(Client client) throws ExecutionException, InterruptedException {
        DocumentReference docRef = instance.db.collection(USERS).document(client.getId());
        ApiFuture<DocumentSnapshot> future =docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists())
            return Response.status(409).build();
        Map<String, Object> data = new HashMap<>();
        data.put(NAME, client.getName());
        data.put(MAIL, client.getMail());
        data.put(PASSWORD, client.getPassword().hashCode());
        ApiFuture<WriteResult> result = docRef.set(data);
        return Response.accepted(result).build();
    }

    @Override
    public Response addCrop(Crop crop) throws ExecutionException, InterruptedException {
        //Verify password and if user exists
        DocumentReference docRef = instance.db.collection(USERS).document(crop.getIdUser());
        ApiFuture<DocumentSnapshot> future =docRef.get();
        DocumentSnapshot document = future.get();
        if (!document.exists())
            return Response.status(409).build();
        Integer password= userCredentials.get(crop.getIdUser());
        if(password==null) {
            password= (int) (long) document.get(PASSWORD);
            userCredentials.put(crop.getIdUser(), password);
        }
        if(password!=crop.getPassword().hashCode())
            return Response.status(403).build();
        DocumentReference docRefCrops= instance.db.collection(CROPS).document();
        //Add crop to user
        docRef.collection(CROPS).document(docRefCrops.getId()).set(new HashMap<>());
        //Add user tp crop
        docRefCrops.collection(USERS).document(crop.getIdUser()).set(new HashMap<>());
        //Add Systems
        for (System system:crop.getSystems()){
            DocumentReference r=docRefCrops.collection(SYSTEMS).document(system.getIp());
            Map<String, Object> data = new HashMap<>();
            GeoPoint point = new GeoPoint(system.getLatitude(),system.getLongitude());
            data.put(LOCATION, point);
            data.put(NAME, system.getName());
            Map<String,Object> systemData= new HashMap<>();
            systemData.put(SYSTEM,docRefCrops.getId());
            instance.db.collection(SYSTEMS).document(system.getIp()).set(systemData);
            r.set(data);
        }
        //add crop information
        Map<String, Object> data = new HashMap<>();
        data.put(CROP, crop.getCrop());
        data.put(LOCATION, crop.getLocation());
        data.put(NAME, crop.getName());
        docRefCrops.set(data);
        return Response.accepted(docRefCrops.getId()).build();
    }

    @Override
    public Response addUserToCrop(String systemId, String userIdToAdd,String userId, String password) throws ExecutionException, InterruptedException {
        //Verify if user to add exists
        DocumentReference docRef = instance.db.collection(USERS).document(userIdToAdd);
        ApiFuture<DocumentSnapshot> future =docRef.get();
        DocumentSnapshot document = future.get();
        if (!document.exists())
            return Response.status(409).build();
        //Verify if user exists and password is correct
        DocumentReference ref =instance.db.collection(CROPS).document(systemId).collection(USERS).document(userId);
        if(!ref.get().get().exists())
            return Response.status(409).build();
        DocumentReference docRefUser = instance.db.collection(USERS).document(userId);
        ApiFuture<DocumentSnapshot> futureUser =docRefUser.get();
        DocumentSnapshot documentUser = futureUser.get();
        Integer pwd= userCredentials.get(userId);
        if(pwd==null) {
            pwd= (int) (long) documentUser.get(PASSWORD);
            userCredentials.put(userId, pwd);
        }
        if(pwd!=password.hashCode())
            return Response.status(403).build();
        //Verify crop exists
        DocumentReference docRefCrops=instance.db.collection(CROPS).document(systemId);
        ApiFuture<DocumentSnapshot> futureCrops =docRefCrops.get();
        DocumentSnapshot documentCrops = futureCrops.get();
        if (!documentCrops.exists())
            return Response.status(409).build();
        //Add crop to user
        docRef.collection(CROPS).document(docRefCrops.getId()).set(new HashMap<>());
        //Add user tp crop
        docRefCrops.collection(USERS).document(userIdToAdd).set(new HashMap<>());
        return Response.accepted().build();
    }

    @Override
    public Response changeName(String systemId, String userId, String password, String name) throws ExecutionException, InterruptedException {
        //Verify password and if user exists
        DocumentReference docRef = instance.db.collection(USERS).document(userId);
        ApiFuture<DocumentSnapshot> future =docRef.get();
        DocumentSnapshot document = future.get();
        if (!document.exists())
            return Response.status(409).build();
        Integer pwd= userCredentials.get(userId);
        if(pwd==null) {
            pwd= (int) (long) document.get(PASSWORD);
            userCredentials.put(userId, pwd);
        }
        if(pwd!=password.hashCode())
            return Response.status(403).build();
        //Verify crop exists
        DocumentReference docRefCrops=instance.db.collection(CROPS).document(systemId);
        ApiFuture<DocumentSnapshot> futureCrops =docRefCrops.get();
        DocumentSnapshot documentCrops = futureCrops.get();
        if (!documentCrops.exists())
            return Response.status(409).build();
        //Verify if user belongs to crop
        DocumentReference docRefCropsUsers = docRefCrops.collection(USERS).document(userId);
        ApiFuture<DocumentSnapshot> futureCropsUsers =docRefCropsUsers.get();
        DocumentSnapshot documentCropsUsers = futureCropsUsers.get();
        if (!documentCropsUsers.exists())
            return Response.status(409).build();
        Map<String, Object> data = new HashMap<>();
        data.put(NAME, name);
        docRefCrops.update(data);
        return Response.accepted().build();
    }

    @Override
    public Response changeSystemName(String systemId, String userId, String password, String ip, String name) throws ExecutionException, InterruptedException {
        CollectionReference ref =instance.db.collection(CROPS);
        DocumentReference userRef=ref.document(systemId).collection(USERS).document(userId);
        if(!userRef.get().get().exists())
            return Response.status(409).build();
        DocumentReference docRef = instance.db.collection(USERS).document(userId);
        ApiFuture<DocumentSnapshot> future =docRef.get();
        DocumentSnapshot document = future.get();
        if (!document.exists())
            return Response.status(409).build();
        Integer pwd= userCredentials.get(userId);
        if(pwd==null) {
            pwd= (int) (long) document.get(PASSWORD);
            userCredentials.put(userId, pwd);
        }
        if(pwd!=password.hashCode())
            return Response.status(403).build();
        DocumentReference system=ref.document(systemId).collection(SYSTEMS).document(ip);
        if(!system.get().get().exists())
            return Response.status(409).build();
        HashMap<String, Object> data= new HashMap<>();
        data.put(NAME,name);
        system.update(data);
        return Response.accepted().build();
    }

    @Override
    public Response addWater(String systemId, String userId, String password, Water water) throws ExecutionException, InterruptedException {
        CollectionReference ref =instance.db.collection(CROPS);
        DocumentReference userRef=ref.document(systemId).collection(USERS).document(userId);
        if(!userRef.get().get().exists())
            return Response.status(409).build();
        DocumentReference docRef = instance.db.collection(USERS).document(userId);
        ApiFuture<DocumentSnapshot> future =docRef.get();
        DocumentSnapshot document = future.get();
        if (!document.exists())
            return Response.status(409).build();
        Integer pwd= userCredentials.get(userId);
        if(pwd==null) {
            pwd= (int) (long) document.get(PASSWORD);
            userCredentials.put(userId, pwd);
        }
        if(pwd!=password.hashCode())
            return Response.status(403).build();
        CollectionReference systems=ref.document(systemId).collection(SYSTEMS);
        for(String ip: water.getSystems()){
            DocumentReference refWatering=systems.document(ip).collection(WATERING).document(water.getStartDate().getTime().toString());
            HashMap<String, Object> data= new HashMap<>();
            data.put(Begin, water.getStartDate().getTime());
            data.put(END, water.getEndDate().getTime());
            data.put(DONE, false);
            refWatering.set(data);
        }
        return Response.accepted().build();
    }

    @Override
    public Response getInformation(CropsEnum crop, String userId, String password) throws ExecutionException, InterruptedException {
        DocumentReference docRef = instance.db.collection(USERS).document(userId);
        ApiFuture<DocumentSnapshot> future =docRef.get();
        DocumentSnapshot document = future.get();
        if (!document.exists())
            return Response.status(409).build();
        Integer pwd= userCredentials.get(userId);
        if(pwd==null) {
            pwd= (int) (long) document.get(PASSWORD);
            userCredentials.put(userId, pwd);
        }
        if(pwd!=password.hashCode())
            return Response.status(403).build();
        String plant= transformEnumToString(crop);
        CropInformation inf= cropInformationMap.get(plant);
        if (inf==null){
            DocumentReference information= instance.db.collection(CULTURE).document(plant);
            DocumentSnapshot ds=information.get().get();
            String image= (String) ds.get(IMAGE);
            Long maxTemperature= (Long) ds.get(MAX_TEMPERATURE);
            Long minTemperature= (Long) ds.get(MIN_TEMPERATURE);
            Long maxHumidity= (Long) ds.get(MAX_HUMIDITY);
            Long minHumidity= (Long) ds.get(MIN_HUMIDITY);
            inf= new CropInformation(image,
            maxHumidity,minHumidity,maxTemperature,minTemperature);
            cropInformationMap.put(plant,inf);
        }
        return Response.accepted(inf).build();
    }

    @Override
    public Response getWater(String systemId, String userId, String password, String ip) throws ExecutionException, InterruptedException {
        CollectionReference ref =instance.db.collection(CROPS);
        DocumentReference userRef=ref.document(systemId).collection(USERS).document(userId);
        if(!userRef.get().get().exists())
            return Response.status(409).build();
        DocumentReference docRef = instance.db.collection(USERS).document(userId);
        ApiFuture<DocumentSnapshot> future =docRef.get();
        DocumentSnapshot document = future.get();
        if (!document.exists())
            return Response.status(409).build();
        Integer pwd= userCredentials.get(userId);
        if(pwd==null) {
            pwd= (int) (long) document.get(PASSWORD);
            userCredentials.put(userId, pwd);
        }
        if(pwd!=password.hashCode())
            return Response.status(403).build();
        DocumentReference system=ref.document(systemId).collection(SYSTEMS).document(ip);
        if(!system.get().get().exists())
            return Response.status(409).build();
        Iterable<DocumentReference> iterator= system.collection(WATERING).listDocuments();
        List<WaterClient> watering= new ArrayList<>();
        for (DocumentReference doc: iterator){
            DocumentSnapshot ds=doc.get().get();
            Timestamp begin= (Timestamp) ds.get(Begin);
            Timestamp end= (Timestamp) ds.get(END);
            boolean done= (boolean) ds.get(DONE);
            Calendar calendarEnd=Calendar.getInstance();
            calendarEnd.setTime(end.toDate());
            Calendar calendarStart=Calendar.getInstance();
            calendarEnd.setTime(begin.toDate());;
            WaterClient water= new WaterClient(calendarStart,calendarEnd,done);
            watering.add(water);
        }
        return Response.accepted(watering).build();
    }

    @Override
    public Response getAllCrops(String userId, String password) throws ExecutionException, InterruptedException {
        DocumentReference docRef = instance.db.collection(USERS).document(userId);
        ApiFuture<DocumentSnapshot> future =docRef.get();
        DocumentSnapshot document = future.get();
        if (!document.exists())
            return Response.status(409).build();
        Integer pwd= userCredentials.get(userId);
        if(pwd==null) {
            pwd= (int) (long) document.get(PASSWORD);
            userCredentials.put(userId, pwd);
        }
        if(pwd!=password.hashCode())
            return Response.status(403).build();
        if(docRef.collection(CROPS).get().get().isEmpty()){
            return Response.ok(null).build();
        }
        Iterable<DocumentReference> iterable=docRef.collection(CROPS).listDocuments();
        List<CropDetailed> cropsDetails=new ArrayList<>();
        for(DocumentReference ref: iterable){
            String cropId=ref.getId(); //isto
            DocumentReference crop=instance.db.collection(CROPS).document(cropId);
            DocumentSnapshot infCrop=crop.get().get();
            String cropString=(String) infCrop.get(CROP);
            CropsEnum cropType=transformStringToEnum(cropString);//isto
            String name= (String) infCrop.get(NAME);//isto
            String location= (String) infCrop.get(LOCATION);//isto
            CropInformation inf= cropInformationMap.get(cropString);
            String image; //isto
            if (inf==null) {
                DocumentReference information = instance.db.collection(CULTURE).document(cropString);
                DocumentSnapshot ds = information.get().get();
                image = (String) ds.get(IMAGE);
            }else{
                image=inf.getImage();
            }
            List<SystemDetailed> systemsList= new ArrayList<>();
            Iterable<DocumentReference> systems= crop.collection(SYSTEMS).listDocuments();
            int totalPoints=0;
            for (DocumentReference system: systems){
                String id=system.getId();
                DocumentSnapshot generalInformation= system.get().get();
                String systemName= (String) generalInformation.get(NAME);
                GeoPoint systemLocation= (GeoPoint)  generalInformation.get(LOCATION);
                DocumentReference systemData=system.collection(DATA).document(MOST_RECENT);
                DocumentSnapshot dataSnapshot= systemData.get().get();
                SystemDetailed sd;
                if(!dataSnapshot.exists()){
                     sd= new SystemDetailed(systemLocation.getLatitude(),
                            systemLocation.getLongitude(),id,systemName,-1,
                            -1,-1,-1,NO_DATA);
                }else{
                   double humidity= (double)dataSnapshot.get(HUMIDITY_LEVEL);
                   double light= (double)dataSnapshot.get(LIGHT_LEVEL);
                   double tank= (double)dataSnapshot.get(TANK_LEVEL);
                   double temperature= (double)dataSnapshot.get(TEMPERATURE_LEVEL);
                   int points=calculateState(cropString, humidity, temperature,tank);
                   String status= convertPointsToStatus(points);
                    sd= new SystemDetailed(systemLocation.getLatitude(),
                            systemLocation.getLongitude(),id,systemName,humidity,
                            light,tank,temperature,status);
                    totalPoints+=points;
                }
                systemsList.add(sd);
            }
            String cropStatus= (systemsList.size()!=0)
                    ?convertPointsToStatus(totalPoints / systemsList.size())
                    : OK;
            CropDetailed entity= new CropDetailed(systemsList,name,location,cropType,cropStatus,cropId,image);
            cropsDetails.add(entity);
        }
        CropsGeneralInformation information= new CropsGeneralInformation(cropsDetails);

        return Response.accepted(information).build();
    }

    @Override
    public Response getWeather(String region) throws IOException, UnirestException, InterruptedException {
        int regionId= Regions.converter(region);
        HashMap<String, ForecastData> data=new HashMap<>(MAX_DAYS);
        if(weather.get(regionId)==null)
            getWeatherAux();
        return Response.ok(weather.get(regionId)).build();
    }

    void onStart(@Observes StartupEvent ev) throws UnirestException, JsonProcessingException {
        getWeatherAux();
    }

    private void getWeatherAux() throws UnirestException, JsonProcessingException {
        com.fasterxml.jackson.databind.ObjectMapper objectMapper= new com.fasterxml.jackson.databind.ObjectMapper();
        for(int i=0; i<MAX_DAYS; i++){
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.get("https://api.ipma.pt/open-data/forecast/meteorology/cities/daily/hp-daily-forecast-day"+i+".json")
                    .asString();
            WeatherForecast forecast = objectMapper.readValue(response.getBody(), WeatherForecast.class);
            for(ForecastData d: forecast.getData()){
                if(weather.get(d.getGlobalIdLocal())==null){
                    HashMap<String, ForecastData> data= new HashMap<>(3);
                    data.put(forecast.getForecastDate(), d);
                    weather.put(d.getGlobalIdLocal(), data);
                }else{
                    HashMap<String, ForecastData> data= weather.get(d.getGlobalIdLocal());
                    data.put(forecast.getForecastDate(), d);
                }
            }
        }
    }

    @Override
    public Response wateringForecast(String systemId, String userId, String password, String ip) throws ExecutionException, InterruptedException {
        CollectionReference ref =instance.db.collection(CROPS);
        DocumentReference userRef=ref.document(systemId).collection(USERS).document(userId);
        if(!userRef.get().get().exists())
            return Response.status(409).build();
        DocumentReference docRef = instance.db.collection(USERS).document(userId);
        ApiFuture<DocumentSnapshot> future =docRef.get();
        DocumentSnapshot document = future.get();
        if (!document.exists())
            return Response.status(409).build();
        Integer pwd= userCredentials.get(userId);
        if(pwd==null) {
            pwd= (int) (long) document.get(PASSWORD);
            userCredentials.put(userId, pwd);
        }
        if(pwd!=password.hashCode())
            return Response.status(403).build();
        DocumentReference system=ref.document(systemId).collection(SYSTEMS).document(ip);
        if(!system.get().get().exists())
            return Response.status(409).build();
        DocumentReference data= system.collection(DATA).document(MOST_RECENT);
        DocumentSnapshot snapshot=data.get().get();
        Calendar forecast= Calendar.getInstance();
        if(!snapshot.exists())
            return Response.accepted(NO_DATA_TO_PREDICT).build();
        else{
            DocumentSnapshot systemSnapshot=ref.document(systemId).get().get();
            String cropString= (String) systemSnapshot.get(CROP);
            double humidity= (double)snapshot.get(HUMIDITY_LEVEL);
            double light= (double)snapshot.get(LIGHT_LEVEL);
            double tank= (double)snapshot.get(TANK_LEVEL);
            double temperature= (double)snapshot.get(TEMPERATURE_LEVEL);
            int points=calculateState(cropString, humidity, temperature,tank);
            String status= convertPointsToStatus(points);
            Date date= Timestamp.now().toDate();
            String period=getPeriod(date);
            if(status.equals(GOOD)){
                forecast.add(Calendar.DATE,1);
                forecast.add(Calendar.HOUR_OF_DAY, 21);
            }else if(period.equals(NIGHT)){
                forecast.add(Calendar.HOUR_OF_DAY, 7);
            }else{
                forecast.add(Calendar.HOUR_OF_DAY, 21);
            }
        }
        return Response.ok(forecast).build();
    }

    @Override
    public Response userExists(String userId) throws ExecutionException, InterruptedException {
        if(userCredentials.get(userId)!=null)
            return Response.accepted(true).build();
        DocumentReference docRef = instance.db.collection(USERS).document(userId);
        ApiFuture<DocumentSnapshot> future =docRef.get();
        DocumentSnapshot document = future.get();
        return Response.accepted(document.exists()).build();

    }

    @Scheduled(cron="0 1 0 * * ?")
    void cronJob(ScheduledExecution execution) throws ExecutionException, InterruptedException, UnirestException, JsonProcessingException {
        Iterable<DocumentReference> iterable = instance.db.collection(CROPS).listDocuments();
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        for (DocumentReference ref : iterable) {
            executorService.execute(() -> {
                Iterable<DocumentReference> it = ref.collection(SYSTEMS).listDocuments();
                for (DocumentReference system : it) {
                    DocumentReference mostRecent = system.collection(DATA).document(MOST_RECENT);
                    try {
                        if (existsDocument(mostRecent))
                            mostRecent.delete();
                        DocumentReference morning = system.collection(DATA).document(MORNING);
                        downSample(morning, MORNING, MORNING_YESTERDAY);
                        DocumentReference afternoon = system.collection(DATA).document(AFTERNOON);
                        downSample(afternoon, AFTERNOON,AFTERNOON_YESTERDAY);
                        DocumentReference evening = system.collection(DATA).document(EVENING);
                        downSample(evening, EVENING,EVENING_YESTERDAY);
                        DocumentReference night = system.collection(DATA).document(NIGHT);
                        downSample(night, NIGHT,NIGHT_YESTERDAY);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        weather= new HashMap<>(35);
        getWeatherAux();

    }

    private boolean existsDocument(DocumentReference doc) throws ExecutionException, InterruptedException {
        return doc.get().get().exists();
    }

    private void downSample(DocumentReference ref, String period, String yesterday_period) throws ExecutionException, InterruptedException {
        if(existsDocument(ref)) {
            DocumentSnapshot snapshot = ref.get().get();
            ref.getParent().document(yesterday_period).set(Objects.requireNonNull(snapshot.getData()));
            ref.delete();
        }
    }


}
