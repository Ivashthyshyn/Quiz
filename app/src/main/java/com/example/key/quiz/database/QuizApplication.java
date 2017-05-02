package com.example.key.quiz.database;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Key on 18.04.2017.
 */

public class QuizApplication extends Application {
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "quiz-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public  DaoSession getDaoSession() {
        return daoSession;
    }
}