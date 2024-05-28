package firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseClass {

    private static FirebaseClass INSTANCE;
    public static String projectId;
    public FileInputStream serviceAccount;

    public FirebaseOptions options;

    public FirebaseApp firebaseApp;
    public Firestore db;
    public FirebaseClass() throws IOException {
        projectId="hydroharvest-163f1";
        serviceAccount= new FileInputStream("hydroharvest.json");
        options= new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://hydroharvest-163f1-default-rtdb.europe-west1.firebasedatabase.app")
                .build();
        firebaseApp=FirebaseApp.initializeApp(options);
        db=FirestoreClient.getFirestore();
    };
    public static FirebaseClass getInstance() throws IOException {
        if(INSTANCE == null) {
            INSTANCE = new FirebaseClass();
        }

        return INSTANCE;
    }
}
