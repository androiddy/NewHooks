package com.dexposedart.newhook;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by zhangzhongping on 16/12/19.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static final boolean DEBUG = true;

    private static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String FILE_NAME = "crash";

    //log文件的后缀名
    private static final String FILE_NAME_SUFFIX = ".crash";

    private static CrashHandler sInstance = null;

    //系统默认的异常处理（默认情况下，系统会终止当前的异常程序）
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;

    private Context mContext;


    //构造方法私有，防止外部构造多个实例，即采用单例模式
    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        if (sInstance == null) {
            sInstance = new CrashHandler();
        }
        return sInstance;
    }

    //这里主要完成初始化工作
    public void init(Context context) {
        //获取系统默认的异常处理器
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        //将当前实例设为系统默认的异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        //获取Context，方便内部使用
        mContext = context.getApplicationContext();
    }


    /**
     * 这个是最关键的函数，当程序中有未被捕获的异常，系统将会自动调用#uncaughtException方法
     * thread为出现未捕获异常的线程，ex为未捕获的异常，有了这个ex，我们就可以得到异常信息。
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            ex.printStackTrace();
            //导出异常信息到SD卡中
            dumpExceptionToSDCard(ex);
            //这里可以通过网络上传异常信息到服务器，便于开发人员分析日志从而解决bug
            uploadExceptionToServer(ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "哎呀，程序发生异常啦...", 0).show();
                Looper.loop();
            }
        }).start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
        mDefaultCrashHandler.uncaughtException(thread, ex);
    }

    public void dumpExceptionToSDCard(Throwable ex) throws IOException {
        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        PrintWriter pw = null;
        try {
            File file = new File(PATH + FILE_NAME + System.nanoTime() + FILE_NAME_SUFFIX);
            if (!file.exists()) {
                file.createNewFile();
            }
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println(InitCrach(ex, false));
            pw.println();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "dump crash info failed");
        } finally {
            if (pw != null) {
                pw.flush();
                pw.close();
            }
        }
    }

    private Object InitCrach(Throwable ex, boolean str) throws PackageManager.NameNotFoundException {

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.flush();
        printWriter.close();
        String result = writer.toString();
        Log.e("123",result);
        return result;
    }


    private void uploadExceptionToServer(Throwable ex) {

    }

}
