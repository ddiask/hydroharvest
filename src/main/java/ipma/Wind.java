package ipma;

public class Wind {

    public static String convertId(int id){
        switch (id){
            case 1: return "Weak";
            case 2: return "Moderate";
            case 3: return "Strong";
            case 4: return "Very strong";
            default: return "Error";
        }
    }
}
