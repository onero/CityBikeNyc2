package dk.easv.citybikenyc.DALC;

import java.util.ArrayList;

/**
 * Created by oe on 06/04/2018.
 */

public abstract class ABikeStations implements IBikeStations {

    @Override
    public ArrayList<BEBikeStation> getAllByFilter(String filter)
    {
        ArrayList<BEBikeStation> bs = new ArrayList<>();
        for (BEBikeStation b : getAll())
            if (b.getName().contains(filter))
                bs.add(b);

        return bs;
    }
}
