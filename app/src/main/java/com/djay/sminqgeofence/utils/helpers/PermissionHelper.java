package com.djay.sminqgeofence.utils.helpers;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.djay.sminqgeofence.BaseActivity;
import com.djay.sminqgeofence.R;


/**
 * Helper class for checking and granting permissions
 *
 * @author Dhananjay Kumar
 */
public class PermissionHelper {
    private static final String TAG = PermissionHelper.class.getSimpleName();
    private BaseActivity mActivity;
    public static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;


    public PermissionHelper(BaseActivity activity) {
        mActivity = activity;
    }

    /**
     * Return the current state of the permissions needed.
     */
    public boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(mActivity,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            mActivity.showSnackBar(R.string.permission_rationale, android.R.string.ok,
                    view -> {
                        // Request permission
                        ActivityCompat.requestPermissions(mActivity,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_PERMISSIONS_REQUEST_CODE);
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}
