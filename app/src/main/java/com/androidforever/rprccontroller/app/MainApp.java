package com.androidforever.rprccontroller.app;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by pedja on 6.6.15..
 */
public class MainApp extends Application
{
    private DaoSession daoSession;
    private static MainApp mThis;

    @Override
    public void onCreate()
    {
        super.onCreate();
        getDaoSession();//make database initiate(update if needed) before anything else happens
        mThis = this;
    }

    /**
     * Get dao session instance
     * If daoSession instance is null new will be created, else it will just return existing instance*/
    public DaoSession getDaoSession()
    {
        if (daoSession == null)
        {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "hosts", null);
            SQLiteDatabase db = helper.getWritableDatabase();
            DaoMaster daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            if (BuildConfig.DEBUG)
            {
                QueryBuilder.LOG_SQL = true;
                QueryBuilder.LOG_VALUES = true;
            }
        }
        return daoSession;
    }

    public static MainApp get()
    {
        return mThis;
    }
}
