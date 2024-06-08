package ipma;

public class Precipitation {

    public static String convertId(int id){
        switch (id){
            case 0: return  "No precipitation";
            case 1: return "Weak";
            case 2: return "Moderate";
            case 3: return "Strong";
            default: return "Error";

        }

    }
}
