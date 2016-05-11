package jacare.geocodingexample.model;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LocHelper {
    public static final String TAG = "Trnspt.LocHelp";

    private Geocoder geocoder;

    public LocHelper(Context context) {
        geocoder = new Geocoder(context, Locale.getDefault());
    }

    public Observable<List<Address>> convertLatLngToAddress(final LatLng loc) {
        Log.i(TAG, "Converting input to address!");

        return Observable.just(loc)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .map(latLng -> {
                    List<Address> addresses = new ArrayList<Address>();
                    try {
                        addresses = geocoder.getFromLocation(loc.latitude, loc.longitude, 5);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d(TAG, "Something fucked up with geocoder!");
                    }

                    return addresses;
                });
    }
}