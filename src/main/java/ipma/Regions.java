package ipma;

public class Regions {
    private static final String AVEIRO="Aveiro";
    private static final String BEJA="Beja";
    private static final String BRAGA="Braga";
    private static final String GUIMARAES="Guimarães";
    private static final String BRAGANCA="Bragança";
    private static final String CASTELO_BRANCO= "Castelo Branco";
    private static final String COIMBRA="Coimbra";
    private static final String EVORA= "Évora";
    private static final String FARO="Faro";
    private static final String SAGRES="Sagres";
    private static final String PORTIMAO="Portimão";
    private static final String LOULE="Loulé";
    private static final String GUARDA="Guarda";
    private static final String PENHAS_DOURADAS="Penhas Douradas";
    private static final String LEIRIA= "Leiria";
    private static final String LISBOA= "Lisboa";
    private static final String PORTALEGRE= "Portalegre";
    private static final String PORTO="Porto";
    private static final String SANTAREM="Santarém";
    private static final String SETUBAL="Setúbal";
    private static final String SINES="Sines";
    private static final String VIANA_DO_CASTELO="Viana do Castelo";
    private static final String VILA_REAL="Vila Real";
    private static final String VISEU= "Viseu";
    private static final String FUNCHAL="Funchal";
    private static final String PORTO_SANTO="Porto Santo";
    private static final String VILA_DO_PORTO="Vila do Porto";
    private static final String PONTA_DELGADA="Ponta Delgada";
    private static final String ANGRA_DO_HEROISMO="Angra do Heroísmo";
    private static final String SANTA_CRUZ_DA_GRACIOSA= "Santa Cruz da Graciosa";
    private static final String VELAS="Velas";
    private static final String MADALENA="Madalena";
    private static final String HORTA="Horta";
    private static final String SANTA_CRUZ_DAS_FLORES="Santa Cruz das Flores";
    private static final String VILA_DO_CORVO="Vila do Corvo";

    public static int converter(String regionsEnum){
        switch (regionsEnum){
            case AVEIRO -> {return 1010500;}
            case BEJA -> {return 1020500;}
            case BRAGA -> {return 1030300;}
            case GUIMARAES -> {return 1030800;}
            case BRAGANCA -> {return 1040200;}
            case CASTELO_BRANCO -> {return 1050200;}
            case COIMBRA -> {return 1060300;}
            case EVORA ->{return 1070500;}
            case FARO ->{return 1080500;}
            case SAGRES -> {return 1081505;}
            case PORTIMAO-> {return 1081100;}
            case LOULE-> {return 1080800;}
            case GUARDA-> {return 1090700;}
            case PENHAS_DOURADAS -> {return 1090821;}
            case LEIRIA -> {return 1100900;}
            case LISBOA -> {return 1110600;}
            case PORTALEGRE -> {return 1121400;}
            case PORTO -> {return 1131200;}
            case SANTAREM -> {return 1141600;}
            case SETUBAL -> {return 1151200;}
            case SINES -> {return 1151300;}
            case VIANA_DO_CASTELO -> {return 1160900;}
            case VILA_REAL -> {return 1171400;}
            case VISEU -> {return 1182300;}
            case FUNCHAL -> {return 2310300;}
            case PORTO_SANTO -> {return 2320100;}
            case VILA_DO_PORTO -> {return 3410100;}
            case PONTA_DELGADA -> {return 3420300;}
            case ANGRA_DO_HEROISMO -> {return 3430100;}
            case SANTA_CRUZ_DA_GRACIOSA -> {return 3440100;}
            case VELAS -> {return 3450200;}
            case MADALENA -> {return 3460200;}
            case HORTA -> {return 3470100;}
            case SANTA_CRUZ_DAS_FLORES -> {return 3480200;}
            case VILA_DO_CORVO -> {return 3490100;}
            default -> {return -1;}
        }
    }

}


