/**
 * Created by: Tomek Spędzia
 * Date: 5/10/2016
 * Email: tomek.milosz.spedzia@gmail.com
 */

package jacare.geocodingexample.model;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import rx.Observable;

public class LocTracker implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String TAG = "Trnspt.LocTracker";

    protected GoogleApiClient apiClient;
    protected Context context;

    protected Observable<Boolean> startedLocUpdatesObs;
    protected Observable<LatLng> updatedLocObs;

    public LocTracker(Context context) {
        this.context = context;

        apiClient = startApi();
        updatedLocObs = Observable.create(latLng -> Log.d(TAG, "First layer called!"));
        startLocUpdates();
    }

    public Observable<LatLng> getUpdatedLocObs() {
        return updatedLocObs;
    }

    public Observable<Boolean> getStartedLocUpdatesObs() {
        return startedLocUpdatesObs;
    }

    @Override
    public void onConnected(Bundle bundle) {
        updatedLocObs.filter(latLng -> startedLocUpdatesObs == null).subscribe(latLng -> start());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        updatedLocObs.subscribe(latLng -> Log.d(TAG, "Updating location with value " + latLng.toString()));
    }

    protected void start(){
        startLocUpdates();
        startedLocUpdatesObs.subscribe(bool -> Log.i(TAG, "Connected to loc service. Waiting for updates"));
    }

    protected LocationRequest buildLocationRequest() {
        return new LocationRequest().setInterval(10000).setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected GoogleApiClient startApi() {
        GoogleApiClient apiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        apiClient.registerConnectionCallbacks(this);
        apiClient.registerConnectionFailedListener(this);
        apiClient.connect();
        return apiClient;
    }

    protected void startLocUpdates() {
        LocationRequest request = buildLocationRequest();
        if(apiClient.isConnected())
            LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, request, this);
    }
}
