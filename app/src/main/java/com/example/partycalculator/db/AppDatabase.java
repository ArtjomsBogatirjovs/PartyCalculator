package com.example.partycalculator.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


import com.example.partycalculator.dao.HumanDao;
import com.example.partycalculator.dao.ItemDao;
import com.example.partycalculator.dao.PartyDao;
import com.example.partycalculator.convertors.BigDecimalConverter;
import com.example.partycalculator.entity.Human;
import com.example.partycalculator.entity.Item;
import com.example.partycalculator.entity.ItemConsumer;
import com.example.partycalculator.entity.Party;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {Party.class, Human.class, Item.class, ItemConsumer.class},
        version = 1
)
@TypeConverters({BigDecimalConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract PartyDao partyDao();

    public abstract HumanDao humanDao();

    public abstract ItemDao itemDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "party_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
