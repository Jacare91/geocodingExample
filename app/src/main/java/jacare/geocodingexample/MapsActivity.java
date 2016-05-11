package jacare.geocodingexample;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.OnEditorAction;
import jacare.geocodingexample.model.LocHelper;
import jacare.geocodingexample.model.LocTracker;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMapClickListener {
    public static final String TAG = "Trnspt.Maps";

    @Bind(R.id.address) protected EditText destAddressContainer;

    private LocTracker locTracker;
    private LocHelper locHelper;
    private GoogleMap map;

    private Observable<LatLng> mapClickObs;

    private Marker currLocMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        locTracker = startTracker();
        locHelper = new LocHelper(this);
        if(map == null)
            map = startMap();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startMap();
    }

    @Override
    public void onMapClick(LatLng latLng) {

            marker.remove();
        marker = mMap.addMarker(new MarkerOptions().position(latLng));
        locHelper.convertLatLngToAddress(latLng).subscribe(addresses -> {
            if (addresses != null || addresses.size() > 0) {
                String addressLine = addresses.get(0).getAddressLine(0);
                destAddressContainer.setText(addressLine);
            } else
                handleLatLngToAddressError();
        });
    private LocTracker startTracker(){
        LocTracker tracker = new LocTracker(this);
        tracker.getStartedLocUpdatesObs();
        tracker.getUpdatedLocObs().subscribe(latLng -> {
        });
        return tracker;
    }

    private GoogleMap startMap() {
        GoogleMap map = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        map.setOnMapClickListener(this);
        return map;
    }

    private void updatedPosition(){

    }
    }
}
