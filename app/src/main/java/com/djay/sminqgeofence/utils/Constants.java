/*
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.djay.sminqgeofence.utils;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Constants used in this sample.
 */

public final class Constants {

    private Constants() {
    }

    private static final String PACKAGE_NAME = "com.google.android.gms.location.Geofence";

    public static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";

    /**
     * Used to set an expiration time for a geofence. After this amount of time Location Services
     * stops tracking the geofence.
     */
    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;

    /**
     * For this sample, geofences expire after twelve hours.
     */
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;
    public static final float GEOFENCE_RADIUS_IN_METERS = 1609; // 1 mile, 1.6 km

    /**
     * Map for storing information about airports in the San Francisco bay area.
     */
    public static final HashMap<String, LatLng> GEO_FENCE_CO_ORDINATES = new HashMap<>();

    static {
        // BHUSARI_COL.
        GEO_FENCE_CO_ORDINATES.put("BHUSARI_COL", new LatLng(18.508212, 73.790440));

        // KALYANI_NAGAR.
        GEO_FENCE_CO_ORDINATES.put("KALYANI_NAGAR", new LatLng(18.544609, 73.910590));

        // R. BHUSARI_COL.
        GEO_FENCE_CO_ORDINATES.put("R_BHUSARI", new LatLng(18.511006, 73.789657));

        // BAVDHAN
        GEO_FENCE_CO_ORDINATES.put("BAVDHAN", new LatLng(18.515604, 73.781905));
    }
}
