package com.djay.sminqgeofence.geofence;

import android.support.annotation.NonNull;

import com.djay.sminqgeofence.utils.Constants;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Map;

/**
 * Presenter class for geo-fencing
 *
 * @author Dhananjay Kumar
 */
class GeoFencePresenter implements GeoFenceContract.Presenter, OnCompleteListener<Void> {

    private GeoFenceContract.View mView;
    private GeofencingClient mGeoFencingClient;
    private ArrayList<Geofence> mGeoFenceList;
    private PendingGeoFenceTask mPendingGeoFenceTask = PendingGeoFenceTask.NONE;

    GeoFencePresenter(GeoFenceContract.View view, GeofencingClient geofencingClient) {
        this.mView = view;
        mView.setPresenter(this);
        mGeoFenceList = new ArrayList<>();
        mView.setButtonsEnabledState();
        populateGeoFenceList();
        mGeoFencingClient = geofencingClient;
    }

    /**
     * Performs the geofencing task that was pending until location permission was granted.
     */
    @Override
    public void performPendingGeoFenceTask() {
        if (mPendingGeoFenceTask == PendingGeoFenceTask.ADD) {
            addGeoFences();
        } else if (mPendingGeoFenceTask == PendingGeoFenceTask.REMOVE) {
            removeGeoFences();
        }
    }

    @Override
    public void clearPendingGeoFenceTask() {
        mPendingGeoFenceTask = PendingGeoFenceTask.NONE;
    }

    @Override
    public void addPendingGeoFenceTask() {
        mPendingGeoFenceTask = PendingGeoFenceTask.ADD;
    }

    @Override
    public void removePendingGeoFenceTask() {
        mPendingGeoFenceTask = PendingGeoFenceTask.REMOVE;
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unSubscribe() {
    }

    /**
     * Builds and returns a GeofencingRequest. Specifies the list of geofences to be monitored.
     * Also specifies how the geofence notifications are initially triggered.
     */
    private GeofencingRequest getGeoFencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeoFenceList);
        return builder.build();
    }

    /**
     * Adds geofences. This method should be called after the user has granted the location
     * permission.
     */
    @SuppressWarnings("MissingPermission")
    public void addGeoFences() {
        mGeoFencingClient.addGeofences(getGeoFencingRequest(), mView.getGeoFencePendingIntent())
                .addOnCompleteListener(this);
    }

    /**
     * Removes geofences. This method should be called after the user has granted the location
     * permission.
     */
    @SuppressWarnings("MissingPermission")
    public void removeGeoFences() {
        if (!mView.haveSufficientPermission()) {
            return;
        }

        mGeoFencingClient.removeGeofences(mView.getGeoFencePendingIntent()).addOnCompleteListener(this);
    }

    /**
     * Runs when the result of calling {@link #addGeoFences()} and/or {@link #removeGeoFences()}
     * is available.
     *
     * @param task the resulting Task, containing either a result or error.
     */
    @Override
    public void onComplete(@NonNull Task<Void> task) {
        mPendingGeoFenceTask = PendingGeoFenceTask.NONE;
        if (task.isSuccessful()) {
            mView.updateGeoFencesAdded(!mView.getGeoFencesAdded());
            mView.setButtonsEnabledState();
            mView.showGeoFenceAddedMsg();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            mView.showGeoFenceAddError(task.getException());
        }
    }

    /**
     * This sample hard codes geofence data. A real app might dynamically create geofences based on
     * the user's location.
     */
    @SuppressWarnings("Convert2streamapi")
    private void populateGeoFenceList() {
        for (Map.Entry<String, LatLng> entry : Constants.GEO_FENCE_CO_ORDINATES.entrySet()) {

            mGeoFenceList.add(new Geofence.Builder()
                    .setRequestId(entry.getKey())
                    .setCircularRegion(
                            entry.getValue().latitude,
                            entry.getValue().longitude,
                            Constants.GEOFENCE_RADIUS_IN_METERS
                    )
                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        }
    }

    private enum PendingGeoFenceTask {
        ADD, REMOVE, NONE
    }

}
