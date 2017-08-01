package com.thisrpg;

/**
 * Created by Administrator on 2017/7/31 0031.
 */

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 这个类就是实现从assets目录读取数据库文件然后写入SDcard中,如果在SDcard中存在，就打开数据库，不存在就从assets目录下复制过去
 *
 * @author Big_Adamapple
 */
public class SQLdm {

    //数据库存储路径
    private static final String filePath = "data/data/com.thisrpg/rpg.db";
    //数据库存放的文件夹 data/data/com.main.jh 下面
    private static final String pathStr = "data/data/com.thisrpg";

    interface LoadDataCallback {
        void loadSuccess(SQLiteDatabase database);

        //-1：文件夹创建失败
        //-2：assets中的数据库文件导出失败
        //-3：文件删除失败
        void loadFail(int i);
    }

    public static void loadData(final Context context, final String databaseName, final LoadDataCallback l) {


        File path = new File(pathStr);
        LogUtils.e("pathStr=" + path);
        //不存在先创建文件夹
        if (!path.mkdir()) {
            l.loadFail(-1);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                //循环需要导出的文件名
                File file = new File(pathStr + "/" + databaseName);
                if (!SharedPreferencesUtils.getString(context, "version", "").equals(SystemUtils.getVersion(context))) {
                    if (!file.delete()) {
                        l.loadFail(-3);
                    }
                }

                //之后删掉这个判断
                if (file.exists()) {
                    file.delete();
                }

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
                SharedPreferencesUtils.editorPut(context, "version", SystemUtils.getVersion(context));
                l.loadSuccess(SQLiteDatabase.openOrCreateDatabase(pathStr + "/" + databaseName, null));
            }
        }).start();
    }
}