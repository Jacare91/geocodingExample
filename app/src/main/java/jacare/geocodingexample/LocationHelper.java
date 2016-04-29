/**
 * Created by: Tomek Spędzia
 * Date: 4/28/2016
 * Email: tomek.milosz.spedzia@gmail.com
 */

package jacare.geocodingexample;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class LocationHelper {
    public static final String TAG = "LocHelp";

    private Context context;

    public LocationHelper(Context context) {
        this.context = context;
    }

    public void convertLatLngToAddress(final LatLng location, final AddressListener listener) {
        Log.i(TAG, "Converting input to address!");

        new AsyncTask<Void, Void, Address>() {
            @Override
            protected Address doInBackground(Void... params) {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
                    if (addresses.size() > 0) {
                        Address address = addresses.get(0);
                        return address;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Something fucked up with geocoder!");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Address address) {
                if (listener != null && address != null)
                    listener.onConversionFinished(address);
            }
        }.execute();

    }

    public interface AddressListener {
        void onConversionFinished(Address address);
    }
}