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

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMapClickListener {
    private EditText destAddressContainer;

    private LocationHelper locationHelper;
    private GoogleMap map;

    private Marker destMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        destAddressContainer = (EditText)findViewById(R.id.address);

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
        locationHelper.convertLatLngToAddress(latLng, new LocationHelper.AddressListener() {
            @Override
            public void onConversionFinished(Address address) {
                if(address != null) {
                    String addressLine = address.getAddressLine(0);
                    destAddressContainer.setText(addressLine);
                }
            }
        });
    }
}
