package com.djay.sminqgeofence.geofence;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.djay.sminqgeofence.BaseActivity;
import com.djay.sminqgeofence.R;
import com.djay.sminqgeofence.utils.Constants;
import com.djay.sminqgeofence.utils.GeoFenceErrorMessages;
import com.djay.sminqgeofence.utils.helpers.PermissionHelper;
import com.google.android.gms.location.LocationServices;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity class for starting GeoFence extends @{@link AppCompatActivity}
 *
 * @author Dhananjay Kumar
 */
public class GeoFenceActivity extends BaseActivity implements GeoFenceContract.View {

    private static final String TAG = GeoFenceActivity.class.getSimpleName();
    @BindView(R.id.btn_add_geofences)
    Button mAddGeoFencesButton;
    @BindView(R.id.btn_remove_geofences)
    Button mRemoveGeoFencesButton;
    private GeoFenceContract.Presenter mPresenter;
    private PermissionHelper mPermissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_fence);
        ButterKnife.bind(this);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mPermissionHelper = new PermissionHelper(this);
        mPresenter = new GeoFencePresenter(this, LocationServices.getGeofencingClient(this));
    }

    @Override
    public void setPresenter(GeoFenceContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mPermissionHelper.checkPermissions()) {
            mPermissionHelper.requestPermissions();
        } else {
            mPresenter.performPendingGeoFenceTask();
        }
    }

    /**
     * Ensures that only one button is enabled at any time. The Add Geofences button is enabled
     * if the user hasn't yet added geofences. The Remove Geofences button is enabled if the
     * user has added geofences.
     */
    public void setButtonsEnabledState() {
        if (getGeoFencesAdded()) {
            mAddGeoFencesButton.setEnabled(false);
            mRemoveGeoFencesButton.setEnabled(true);
        } else {
            mAddGeoFencesButton.setEnabled(true);
            mRemoveGeoFencesButton.setEnabled(false);
        }
    }

    /**
     * Returns true if geofences were added, otherwise false.
     */
    public boolean getGeoFencesAdded() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
                Constants.GEOFENCES_ADDED_KEY, false);
    }

    /**
     * Stores whether geofences were added ore removed in {@link SharedPreferences};
     *
     * @param added Whether geofences were added or removed.
     */
    @Override
    public void updateGeoFencesAdded(boolean added) {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putBoolean(Constants.GEOFENCES_ADDED_KEY, added)
                .apply();
    }

    /**
     * Gets a PendingIntent to send with the request to add or remove Geofences. Location Services
     * issues the Intent inside this PendingIntent whenever a geofence transition occurs for the
     * current list of geofences.
     *
     * @return A PendingIntent for the IntentService that handles geofence transitions.
     */
    public PendingIntent getGeoFencePendingIntent() {
        Intent intent = new Intent(this, GeoFenceResultIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Adds geofences, which sets alerts to be notified when the device enters or exits one of the
     * specified geofences. Handles the success or failure results returned by addGeoFences().
     */
    @OnClick(R.id.btn_add_geofences)
    public void addGeoFencesButtonHandler() {
        if (!mPermissionHelper.checkPermissions()) {
            mPresenter.addPendingGeoFenceTask();
            mPermissionHelper.requestPermissions();
            return;
        }
        mPresenter.addGeoFences();
    }

    /**
     * Removes geofences, which stops further notifications when the device enters or exits
     * previously registered geofences.
     */
    @OnClick(R.id.btn_remove_geofences)
    public void removeGeoFencesButtonHandler() {
        if (!mPermissionHelper.checkPermissions()) {
            mPresenter.removePendingGeoFenceTask();
            mPermissionHelper.requestPermissions();
            return;
        }
        mPresenter.removeGeoFences();
    }

    @Override
    public boolean haveSufficientPermission() {
        if (!mPermissionHelper.checkPermissions()) {
            showSnackBar(getString(R.string.insufficient_permissions));
            return false;
        }
        return true;
    }

    @Override
    public void showGeoFenceAddedMsg() {
        int messageId = getGeoFencesAdded() ? R.string.geofences_added :
                R.string.geofences_removed;
        Toast.makeText(this, getString(messageId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showGeoFenceAddError(Exception e) {
        String errorMessage = GeoFenceErrorMessages.getErrorString(this, e);
        Log.w(TAG, errorMessage);
    }

    @Override
    public void permissionGrantedAction() {
        mPresenter.performPendingGeoFenceTask();
    }

    @Override
    public void permissionGrantFailAction() {
        mPresenter.clearPendingGeoFenceTask();
    }
}
