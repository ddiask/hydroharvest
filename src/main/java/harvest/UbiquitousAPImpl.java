package harvest;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Precondition;
import entity.*;
import firebase.FirebaseClass;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.lang.System;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static harvest.ClientAPImpl.transformEnumToString;

public class UbiquitousAPImpl implements UbiquitousAPI{
    private static final String SYSTEMS="Systems";
    private static final String SYSTEM="System";
    private static final String CROPS="Crops";
    private static final String PERIOD="Period";
    private static final String DATE="Date";
    private static final String MORNING="morning";
    private static final String AFTERNOON="afternoon";
    private static final String READINGS="readings";
    private static final String EVENING="evening";
    private static final String NIGHT="night";
    private static final String DATA="Data";
    private static final String HUMIDITY_LEVEL="humidityLevel";
    private static final String LIGHT_LEVEL="lightLevel";
    private static final String TANK_LEVEL="tankLevel";
    private static final String TEMPERATURE_LEVEL="temperatureLevel";
    private static final String WATERING="Watering";
    private static final String MOST_RECENT="MostRecent";
    private static final String MAX_HUMIDITY="Max_Humidity";
    private static final String MAX_TEMPERATURE="Max_Temperature";
    private static final String MIN_TEMPERATURE="Min_Temperature";
    private static final String MIN_HUMIDITY="Min_Humidity";
    private static final String IMAGE="Image";
    private static final String CULTURE="Culture";
    private static final String Begin="Begin";
    private static final String END="End";
    private static final String DONE="Done";
    public FirebaseClass instance= FirebaseClass.getInstance();

    public UbiquitousAPImpl() throws IOException {
    }

    @Override
    public Response addData(SensorData data) throws ExecutionException, InterruptedException {
        DocumentSnapshot documentSnapshot=instance.db.collection(SYSTEMS).document(data.getIp()).get().get();
        if(!documentSnapshot.exists())
            return Response.status(409).build();
        String system= (String) documentSnapshot.get(SYSTEM);
        assert system != null;
        CollectionReference colRef = instance.db.collection(CROPS).document(system)
                .collection(SYSTEMS);
        DocumentReference docRef=colRef.document(data.getIp());
        Date date= Timestamp.now().toDate();
        DocumentReference doc=docRef.collection(DATA).document(MOST_RECENT);
        HashMap<String, Object> map= new HashMap<>();
        for(Sensor sensor: data.getSensors()){
            map.put(sensor.getName(),sensor.getValue());
        }
        map.put(PERIOD, getPeriod(date));
        doc.set(map);
        map.remove(PERIOD);
        //Update averages
        DocumentReference docRefAverage=docRef.collection(DATA).document(getPeriod(date));
        DocumentSnapshot snapshot= docRefAverage.get().get();
        if(!snapshot.exists()){
           map.put(READINGS, 1);
           docRefAverage.set(map);
        }else{
            int r=((int) (long)snapshot.get(READINGS)+1);
            map.put(READINGS, r);
            double humidity=((double)snapshot.get(HUMIDITY_LEVEL)*(r-1)
                    +(double) map.get(HUMIDITY_LEVEL))/r;
            map.put(HUMIDITY_LEVEL, humidity);
            double light=((double)snapshot.get(LIGHT_LEVEL)*(r-1)
                    +(double) map.get(LIGHT_LEVEL))/r;
            map.put(LIGHT_LEVEL, light);
            double tank=((double)snapshot.get(TANK_LEVEL)*(r-1)
                    +(double) map.get(TANK_LEVEL))/r;
            map.put(TANK_LEVEL, tank);
            double temperature=((double) snapshot.get(TEMPERATURE_LEVEL)*(r-1)
                    +(double) map.get(TEMPERATURE_LEVEL))/r;
            map.put(TEMPERATURE_LEVEL, temperature);
            docRefAverage.update(map);
        }
        Iterable<DocumentReference> d =docRef.collection(WATERING).listDocuments();
        long resStart=-1;
        long resFinal=-1;
        for (DocumentReference reference: d){
            DocumentSnapshot snap=reference.get().get();
            Timestamp timestamp= (Timestamp) snap.get(Begin);
            Timestamp endTimestamp= (Timestamp) snap.get(END);
            boolean done= (boolean) snap.get(DONE);
            long leftTime=(timestamp.getSeconds())-Timestamp.now().getSeconds();
            if(endTimestamp.getSeconds()<Timestamp.now().getSeconds() && !done){
                reference.update(DONE,true);
            }
            if(leftTime<300 && leftTime>0){
                resStart=leftTime*1000;
                Timestamp t= (Timestamp) snap.get(END);
                resFinal=(t.getSeconds()-Timestamp.now().getSeconds())*1000;
            }
        }
        return Response.accepted(new DataWatering(resStart,resFinal)).build();
    }

    @Override
    public Response getInformation(CropsEnum crop) throws ExecutionException, InterruptedException {
        String plant= transformEnumToString(crop);
        DocumentReference information= instance.db.collection(CULTURE).document(plant);
        DocumentSnapshot ds=information.get().get();
        String image= (String) ds.get(IMAGE);
        Long maxTemperature= (Long) ds.get(MAX_TEMPERATURE);
        Long minTemperature= (Long) ds.get(MIN_TEMPERATURE);
        Long maxHumidity= (Long) ds.get(MAX_HUMIDITY);
        Long minHumidity= (Long) ds.get(MIN_HUMIDITY);
        return Response.accepted(new CropInformation(image,maxHumidity,minHumidity,maxTemperature,minTemperature)).build();
    }

    public static String getPeriod(Date date){
        Calendar time= Calendar.getInstance();
        time.setTime(date);
        int hours= time.get(Calendar.HOUR_OF_DAY);
        System.out.println("As horas sao: "+hours);
        if(hours<=7)
            return NIGHT;
        else if (hours<=12)
            return MORNING;
        else if(hours>=20)
            return AFTERNOON;
        else
            return EVENING;
    }
}
