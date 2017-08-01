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
import java.util.List;

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
        void loadSuccess();

        //-1：文件夹创建失败
        //-2：assets中的数据库文件导出失败
        void loadFail(int i);
    }

    SQLiteDatabase database;

    public static void loadData(final Context context, final List<String> databaseName, final LoadDataCallback l) {


        File path = new File(pathStr);
        LogUtils.e("pathStr=" + path);
        //不存在先创建文件夹
        if (path.mkdir()) {
            Log.i("test", "创建成功");
        } else {
            Log.i("test", "创建失败");
            l.loadFail(-1);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                //循环需要导出的文件名
                for (int i = 0; i < databaseName.size(); i++) {
                    File file = new File(pathStr + "/" + databaseName.get(i));
                    if (!file.exists()) {
                        InputStream is = null;
                        FileOutputStream fos = null;
                        try {
                            //得到资源
                            AssetManager am = context.getAssets();
                            //得到数据库的输入流
                            is = am.open(databaseName.get(i));
                            LogUtils.e(is + "");
                            //用输出流写到SDcard上面
                            fos = new FileOutputStream(file);
                            LogUtils.e("fos=" + fos);
                            LogUtils.e("file=" + file);
                            //创建byte数组  用于1KB写一次
                            byte[] buffer = new byte[1024];
                            int count = 0;
                            while ((count = is.read(buffer)) > 0) {
                                LogUtils.e("得到");
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
                }
                l.loadSuccess();
            }
        }).start();
    }

    public SQLiteDatabase openDatabase(Context context) {
        System.out.println("filePath:" + filePath);
        File jhPath = new File(filePath);
        //查看数据库文件是否存在
        if (jhPath.exists()) {
            Log.i("test", "存在数据库");
            //存在则直接返回打开的数据库
            return SQLiteDatabase.openOrCreateDatabase(jhPath, null);
        } else {
            //不存在先创建文件夹
            File path = new File(pathStr);
            Log.i("test", "pathStr=" + path);
            if (path.mkdir()) {
                Log.i("test", "创建成功");
            } else {
                Log.i("test", "创建失败");
            }
            try {
                //得到资源
                AssetManager am = context.getAssets();
                //得到数据库的输入流
                InputStream is = am.open("rpg.db");
                Log.i("test", is + "");
                //用输出流写到SDcard上面
                FileOutputStream fos = new FileOutputStream(jhPath);
                Log.i("test", "fos=" + fos);
                Log.i("test", "jhPath=" + jhPath);
                //创建byte数组  用于1KB写一次
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    Log.i("test", "得到");
                    fos.write(buffer, 0, count);
                }
                //最后关闭就可以了
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
            //如果没有这个数据库  我们已经把他写到SD卡上了，然后在执行一次这个方法 就可以返回数据库了
            return openDatabase(context);
        }
    }
}