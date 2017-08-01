package com.thisrpg;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.thisrpg.db.DaoMaster;
import com.thisrpg.db.DaoSession;
import com.thisrpg.db.Players;
import com.thisrpg.db.PlayersDao;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String dbNameList = "rpg.db";
        SQLdm.loadData(this, dbNameList, new SQLdm.LoadDataCallback() {
            @Override
            public void loadSuccess(SQLiteDatabase database) {
                DaoMaster daoMaster = new DaoMaster(database);
                DaoSession daoSession = daoMaster.newSession();
                PlayersDao userDao = daoSession.getPlayersDao();
                List<Players> user = userDao.queryBuilder().where(PlayersDao.Properties.Uid.eq(1)).build().list();
                LogUtils.e(user.get(0).getName());
            }

            @Override
            public void loadFail(int i) {
                LogUtils.e("File");
            }
        });
    }
}
