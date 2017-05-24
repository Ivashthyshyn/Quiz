package com.example.key.quiz.database;

import android.app.Application;

import com.bettervectordrawable.Convention;
import com.bettervectordrawable.VectorDrawableCompat;
import com.example.key.quiz.R;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Key on 18.04.2017.
 */

public class QuizApplication extends Application {
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        int[] ids = VectorDrawableCompat.findVectorResourceIdsByConvention(getResources(), R.drawable.class, Convention.ResourceNameHasVectorSuffix);
        VectorDrawableCompat.enableResourceInterceptionFor(getResources(), ids);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "quiz-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public  DaoSession getDaoSession() {
        return daoSession;
    }
}
