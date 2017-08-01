package com.thisrpg;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.thisrpg.db.DaoMaster;

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.AbstractDaoSession;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        List<String> dbNameList = new ArrayList<>();
        dbNameList.add("rpg.db");
        SQLdm.loadData(this, dbNameList, new SQLdm.LoadDataCallback() {
            @Override
            public void loadSuccess() {

            }

            @Override
            public void loadFail(int i) {
                LogUtils.e("File");
            }
        });
    }
}
