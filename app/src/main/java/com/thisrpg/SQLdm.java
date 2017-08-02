package com.thisrpg;

/**
 * Created by Administrator on 2017/7/31 0031.
 */

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.thisrpg.db.DaoMaster;
import com.thisrpg.db.DaoSession;

import org.greenrobot.greendao.database.Database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 这个类就是实现从assets目录读取数据库文件然后写入SDcard中,如果在SDcard中存在，就打开数据库，不存在就从assets目录下复制过去
 */
public class SQLdm {


    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    public static DaoMaster getDaoMaster() {
        return daoMaster;
    }

    public static void setDaoMaster(DaoMaster daoMaster) {
        SQLdm.daoMaster = daoMaster;
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    public static void setDaoSession(DaoSession daoSession) {
        SQLdm.daoSession = daoSession;
    }

    //数据库存放的文件夹 data/data/com.main.jh 下面
    private static final String pathStr = "data/data/com.thisrpg/databases";


    public interface LoadDataCallback {
        void loadSuccess();

        //-1：文件夹创建失败
        //-2：assets中的数据库文件导出失败
        //-3：文件删除失败
        void loadFail(int i);
    }

    public static void loadData(final Context context, final String databaseName, final LoadDataCallback l) {

        File path = new File(pathStr);
        LogUtils.e("pathStr=" + path);
        //不存在先创建文件夹
        if (!path.exists()) {
            if (!path.mkdir()) {
                l.loadFail(-1);
                return;
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                //判断是否需要更新数据库
                File file = new File(pathStr + "/" + databaseName);
                //判断文件如果存在
                if (file.exists()) {
                    //那么检测版本号是否一致
                    if (!SharedPreferencesUtils.getString(context, "version", "").equals(SystemUtils.getVersion(context))) {
                        //版本号不一致则需要更新，将上一个版本的数据库删除之后再重新初始化
                        if (!file.delete()) {
                            l.loadFail(-3);
                            return;
                        }
                    }
                }


                //之后删掉这个判断
                if (file.exists()) {
                    file.delete();
                }

                //检测完版本号之后再判定数据库文件是否存在
                //如果不存在，则重新从assets文件夹中将数据库文件导出本地
                if (!file.exists()) {
                    InputStream is = null;
                    FileOutputStream fos = null;
                    try {
                        //得到资源
                        AssetManager am = context.getAssets();
                        //得到数据库的输入流
                        is = am.open(databaseName);
                        //用输出流写到SDcard上面
                        fos = new FileOutputStream(file);
                        //创建byte数组  用于1KB写一次
                        byte[] buffer = new byte[1024];
                        int count = 0;
                        while ((count = is.read(buffer)) > 0) {
                            fos.write(buffer, 0, count);
                        }
                        fos.flush();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        l.loadFail(-2);
                        return;
                    } finally {
                        //最后关闭就可以了
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                //将版本号保存到本地
                SharedPreferencesUtils.editorPut(context, "version", SystemUtils.getVersion(context));
                //实例化DaoMaster，DaoSession
                if (daoMaster == null) {
                    daoMaster = new DaoMaster(new DaoMaster.DevOpenHelper(context, databaseName).getReadableDatabase());
                    daoSession = daoMaster.newSession();
                }
                l.loadSuccess();
            }
        }).start();
    }

    private PackageInfo getAppInfo(Context context) {
        PackageInfo packageInfo = null;
        // 获取packageManager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }
}