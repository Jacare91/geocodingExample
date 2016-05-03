package jacare.geocodingexample;

import android.location.Address;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMapClickListener {
    @Bind(R.id.address) protected EditText destAddressContainer;

    private LocationHelper locationHelper;
    private GoogleMap map;

    private Marker destMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        setUpMapIfNeeded();
        locationHelper = new LocationHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (map == null)
            map = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
        map.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(destMarker != null)
            destMarker.remove();
        destMarker = map.addMarker(new MarkerOptions().position(latLng));

        locationHelper.convertLatLngToAddress(latLng)
                .subscribe(addresses -> {
                    if(addresses != null || addresses.size() > 0){
                        String addressLine = addresses.get(0).getAddressLine(0);
                        destAddressContainer.setText(addressLine);
                    }else
                        handleLatLngToAddressError();
                });
    }



    protected void handleLatLngToAddressError(){
        //TODO: add some way to display addresses or whatever.
    }
}