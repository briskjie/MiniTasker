package com.xander.minitasker;

import android.os.AsyncTask;
import java.util.concurrent.Executor;

/**
 * MiniTasker 是一个方便切换线程去执行耗时任务，
 * 并且可以在需要回调到 UI 线程的时候能简便切换到 UI 线程的开源库。
 * Created by xander on 2018/4/11.
 */

public class MiniTasker {

  /**
   * 线程池，直接使用 AsyncTask 的线程池
   */
  private static Executor THREAD_POOL_EXECUTOR = AsyncTask.THREAD_POOL_EXECUTOR;

  /**
   * 自定义线程池
   * @param threadPool
   */
  public static void setThreadPoolExecutor(Executor threadPool) {
    THREAD_POOL_EXECUTOR = threadPool;
  }

  /**
   * 在非 UI 线程执行耗时任务
   * @param tasker
   */
  public static void run(final Tasker tasker) {
    THREAD_POOL_EXECUTOR.execute(new Runnable() {
      @Override public void run() {
        tasker.run();
      }
    });
  }

  /**
   * 在非 UI 线程执行耗时任务，执行完后将结果回调到 UI 线程
   * @param uiTasker
   */
  public static void runAndCallback(final UiTasker uiTasker) {
    THREAD_POOL_EXECUTOR.execute(new Runnable() {
      @Override public void run() {
        uiTasker.setResult(uiTasker.run());
        uiTasker.postResult();
      }
    });
  }
}
