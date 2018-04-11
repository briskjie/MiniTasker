package com.xander.minitasker;

import android.os.AsyncTask;
import java.util.concurrent.Executor;

/**
 * 方便为没有接入 RxJava 的项目切换线程而开发的一个库
 * 利用 AsyncTask 已有的线程池作为基础，在此之上执行一些
 * 耗时任务，同时
 * Created by xander on 2018/4/11.
 */

public class MiniTasker {

  private static Executor THREAD_POOL_EXECUTOR = AsyncTask.THREAD_POOL_EXECUTOR;

  public static void setThreadPoolExecutor(Executor threadPool) {
    THREAD_POOL_EXECUTOR = threadPool;
  }

  public static void run(final Tasker tasker) {
    THREAD_POOL_EXECUTOR.execute(new Runnable() {
      @Override public void run() {
        tasker.run();
      }
    });
  }

  public static void runAndCallback(final UiTasker uiTasker) {
    THREAD_POOL_EXECUTOR.execute(new Runnable() {
      @Override public void run() {
        uiTasker.setResult(uiTasker.run());
        uiTasker.postResult();
      }
    });
  }
}
