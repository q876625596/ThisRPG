package com.thisrpg.scene;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.thisrpg.LogUtils;
import com.thisrpg.R;
import com.thisrpg.SQLdm;
import com.thisrpg.db.Players;
import com.thisrpg.db.PlayersDao;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

/**
 * Created by Administrator on 2017/8/2 0002.
 */

public class LoadingActivity extends AppCompatActivity {

    private ImageView backGroundImage;
    private AVLoadingIndicatorView avi;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        backGroundImage = (ImageView) findViewById(R.id.backGroundImage);
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.show();
        handler.sendEmptyMessageDelayed(1, 1000L);

        String dbNameList = "rpg.db";
        SQLdm.loadData(this, dbNameList, new SQLdm.LoadDataCallback() {
            @Override
            public void loadSuccess() {

                avi.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //avi.hide();
                    }
                }, 3000L);
                PlayersDao userDao = SQLdm.getDaoSession().getPlayersDao();
                List<Players> user = userDao.queryBuilder().where(PlayersDao.Properties.Uid.eq(1)).build().list();
                LogUtils.e(user.get(0).getName());
                List<Players> user1 = userDao.queryBuilder().where(PlayersDao.Properties.Uid.eq(2)).build().list();
                LogUtils.e(user1.get(0).getName());

            }

            @Override
            public void loadFail(int i) {
                LogUtils.e("File:" + i);
            }
        });
    }

    int[] colors = {R.color.kikyo, R.color.tintRed, R.color.shion, R.color.sora, R.color.tintGreen, R.color.momo};
    int colorIndex = 1;
    Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            avi.setIndicatorColor(ContextCompat.getColor(LoadingActivity.this, colors[colorIndex++]));
            if (colorIndex == 3) {
                colorIndex = 0;
            }
            handler.sendEmptyMessageDelayed(1, 1000L);
            return false;
        }
    });

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
