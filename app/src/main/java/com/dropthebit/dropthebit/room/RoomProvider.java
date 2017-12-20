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
                .fallbackToDestructiveMigration()
                .build();
        //데이터베이스 스키마 바꾸는 과정에서 버전업때문에 migration 충돌 났는데 fallbackTo ~ 이거 넣어서 테이블 다 삭제하도록함
        // 원래대로 유지할라면 addMigration 해서 버전 업 해줘야댄대
    }

    public LocalDatabase getDatabase() {
        return database;
    }
}
