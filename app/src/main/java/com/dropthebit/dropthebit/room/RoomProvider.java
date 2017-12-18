package com.dropthebit.dropthebit.room;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * Created by mason-hong on 2017. 12. 18..
 */
public class RoomProvider {
    private static RoomProvider instance;
    private LocalDatabase database;

    public static RoomProvider getInstance(Context context) {
        if (instance == null) {
            synchronized (RoomProvider.class) {
                if (instance == null) {
                    instance = new RoomProvider(context);
                }
            }
        }
        return instance;
    }

    private RoomProvider(Context context) {
        database = Room.databaseBuilder(
                context,
                LocalDatabase.class,
                "local-database")
                .build();
    }

    public LocalDatabase getDatabase() {
        return database;
    }
}
