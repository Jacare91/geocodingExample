package jacare.geocodingexample;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.Bind;
import butterknife.ButterKnife;
import jacare.geocodingexample.model.LocHelper;
import jacare.geocodingexample.model.LocTracker;
import rx.Observable;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMapClickListener {
    public static final String TAG = "Trnspt.Maps";

    @Bind(R.id.address_output) protected EditText destAddressContainer;

    private LocTracker locTracker;
    private LocHelper locHelper;
    private GoogleMap map;

    private Marker destPosMarker;
    private Marker currPosMarker;

    private boolean started;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

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

    private LocTracker startTracker(){
        LocTracker tracker = new LocTracker(this);
        tracker.getStartedLocUpdatesObs().filter(bool -> !started).subscribe(bool -> {});
        tracker.getUpdatedLocObs().subscribe(latLng -> updateCurrPos(latLng));
        return tracker;
    }

    private GoogleMap startMap() {
        GoogleMap map = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        map.setOnMapClickListener(this);
        return map;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.i(TAG, "OnMapClick");
        Observable.just(latLng).map(loc -> updateDestPos(latLng))
                .flatMap(loc -> locHelper.convertLatLngToAddress(latLng))
                .filter(addresses -> addresses != null || addresses.size() > 0)
                .map(addresses -> addresses.get(0).getAddressLine(0))
                .doOnError(e -> Log.d(TAG, "Fuckup!"))
                .subscribe(address -> destAddressContainer.setText(address));
    }

    protected LatLng updateDestPos(LatLng latLng){
        if(destPosMarker != null)
            destPosMarker.remove();
        destPosMarker = map.addMarker(new MarkerOptions().position(latLng));
        return latLng;
    }

    private LatLng updateCurrPos(LatLng latLng){
        if(currPosMarker != null)
            currPosMarker.remove();
        currPosMarker = map.addMarker(new MarkerOptions().position(latLng));
        return latLng;
    }

}
