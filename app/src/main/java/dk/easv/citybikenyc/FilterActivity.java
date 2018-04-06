package dk.easv.citybikenyc;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.easv.citybikenyc.DALC.BEBikeStation;
import dk.easv.citybikenyc.DALC.BikeStations;
import dk.easv.citybikenyc.DALC.BikeStationsASyncTask;
import dk.easv.citybikenyc.DALC.IBikeStations;

public class FilterActivity extends AppCompatActivity {


    IBikeStations mBikeStations;

    ListView mLvStations;
    EditText mFilterText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        mBikeStations = new BikeStationsASyncTask(new IBELoaded() {
            @Override
            public void onFinished() {
                initializeData();
            }
        });
        mBikeStations.loadAll();

        mLvStations = findViewById(R.id.lvStations);
        mFilterText = findViewById(R.id.etFilter);
        mFilterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(Global.TAG, "Text changed: " + charSequence);
                filter(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        Log.d(Global.TAG, "OnCreate - loadAll completed");

    }


    private void filter(String filter)
    {
        final ArrayList<BEBikeStation> f = mBikeStations.getAllByFilter(filter);
        ListAdapter adapter = createAdapter(f);

        mLvStations.setAdapter(adapter);


        mLvStations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                BEBikeStation s = f.get(position);

                String msg = s.getName();

                Toast.makeText(getApplicationContext(),
                        msg, Toast.LENGTH_SHORT).show();
            }
        });
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


    public void initializeData() {
        filter("");
    }


}
