package com.djay.sminqgeofence.geofence;


import android.app.PendingIntent;

import com.djay.sminqgeofence.BasePresenter;
import com.djay.sminqgeofence.BaseView;


/**
 * Interface acting as a contract between geo-fence view and presenter
 *
 * @author Dhananjay Kumar
 */
interface GeoFenceContract {

    interface Presenter extends BasePresenter {
        void addGeoFences();

        void removeGeoFences();

        void addPendingGeoFenceTask();

        void removePendingGeoFenceTask();

        void clearPendingGeoFenceTask();

        void performPendingGeoFenceTask();
    }

    interface View extends BaseView<Presenter> {
        boolean haveSufficientPermission();

        boolean getGeoFencesAdded();

        void setButtonsEnabledState();

        void updateGeoFencesAdded(boolean added);

        void showGeoFenceAddedMsg();

        void showGeoFenceAddError(Exception e);

        PendingIntent getGeoFencePendingIntent();
    }
}
