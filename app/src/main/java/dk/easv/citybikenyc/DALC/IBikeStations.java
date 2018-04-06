package dk.easv.citybikenyc.DALC;

import java.util.ArrayList;

/**
 * Created by oe on 05/04/2018.
 */

public interface IBikeStations {


    void loadAll();

    ArrayList<BEBikeStation> getAll();

    ArrayList<BEBikeStation> getAllByFilter(String filter);
}
