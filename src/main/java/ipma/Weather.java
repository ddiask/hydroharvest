package ipma;

public class Weather {
    public static String convertId(int id){
        switch (id){
            case 0: return "No information";
            case 1: return "Clear sky";
            case 2, 25: return "Partly cloudy";
            case 3: return "Sunny intervals";
            case 4: return "Cloudy ";
            case 5: return "Cloudy (High cloud)";
            case 6: return "Showers/rain";
            case 7: return "Light showers/rain";
            case 8: return "Heavy showers/rain";
            case 9: return "Rain/showers";
            case 10: return "Light rain";
            case 11: return "Heavy rain/showers";
            case 12: return "Intermittent rain";
            case 13: return "Intermittent light rain";
            case 14: return "Intermittent heavy rain";
            case 15: return "Drizzle";
            case 16: return "Mist";
            case 17,26: return "Fog";
            case 18: return "Snow";
            case 19: return "Thunderstorms";
            case 20: return "Showers and thunderstorms";
            case 21: return "Hail";
            case 22: return "Frost";
            case 23: return "Rain and thunderstorms";
            case 24: return "Convective clouds";
            case 27: return "Cloudy";
            case 28: return "Snow showers";
            case 29,30: return "Rain and snow";
            default : return "Error";
        }
    }
}

