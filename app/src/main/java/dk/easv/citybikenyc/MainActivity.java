package dk.easv.citybikenyc;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import dk.easv.citybikenyc.DALC.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ListActivity {


    private IBikeStations pd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        pd = new BikeStationsASyncTask(new IBELoaded() {
            @Override
            public void onFinished() {
                initializeData(pd.getAll());
            }
        });
        pd.loadAll();
        
        Log.d(Global.TAG, "OnCreate - loadAll completed");

    }


    private List<Map<String, String>> asListMap(List<BEBikeStation> src)
    {
        List<Map<String, String>> res = new ArrayList<Map<String, String>>();
        for (BEBikeStation s : src)
        {
            Map<String, String> e = new HashMap<String, String>();
            e.put("number", "" + s.getNumber());
            e.put("name", s.getName());
            res.add(e);
        }
        return res;
    }

    public SimpleAdapter createAdapter(ArrayList<BEBikeStation> ms) {
        SimpleAdapter adapter = new SimpleAdapter(this,
                asListMap(ms),
                R.layout.cell,
                new String[] { "number",       "name" },
                new int[]    { R.id.txtNumber, R.id.txtName });

        return adapter;
    }


    public void initializeData(final ArrayList<BEBikeStation> pd)
    {
        ListAdapter adapter = createAdapter(pd);

        setListAdapter(adapter);
        Log.d(Global.TAG, "Adapter attached");

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                BEBikeStation s = pd.get(position);

                String msg = s.getName();

                Toast.makeText(getApplicationContext(),
                        msg, Toast.LENGTH_SHORT).show();
            }
        });
    }



}
