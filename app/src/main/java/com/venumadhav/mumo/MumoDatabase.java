package com.venumadhav.mumo;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Single source of truth for Mumo's Firebase Realtime Database reference.
 * Every screen previously hardcoded the same database URL separately.
 */
public final class MumoDatabase {

    private static final String DATABASE_URL = "https://mumo-91024-default-rtdb.firebaseio.com/";
    private static volatile DatabaseReference instance;

    private MumoDatabase() {
    }

    public static DatabaseReference getReference() {
        if (instance == null) {
            synchronized (MumoDatabase.class) {
                if (instance == null) {
                    instance = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URL);
                }
            }
        }
        return instance;
    }
}
