package com.xander.minitasker;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * 执行耗时任务后，回调到 UI 线程
 * Created by xander on 2018/4/11.
 */

public abstract class UiTasker<R> {

  /**
   * 回调消息
   */
  private static final int MESSAGE_POST_RESULT = 100;

  /**
   * 和 UI 线程绑定的 Handler
   */
  private static InternalHandler sHandler;

  /**
   * 获取和 UI 线程绑定的 Handler
   * @return 和 UI 线程绑定的 Handler
   */
  private static Handler getMainHandler() {
    synchronized (AsyncTask.class) {
      if (sHandler == null) {
        sHandler = new InternalHandler(Looper.getMainLooper());
      }
      return sHandler;
    }
  }

  /**
   * 接收到回调消息后处理回调消息
   */
  private static class InternalHandler extends Handler {
    public InternalHandler(Looper looper) {
      super(looper);
    }

    @SuppressWarnings({ "unchecked", "RawUseOfParameterizedType" }) @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case MESSAGE_POST_RESULT:
          // There is only one result
          UiTasker uiTasker = (UiTasker) msg.obj;
          uiTasker.callback(uiTasker.getResult());
          break;
        default:
          break;
      }
    }
  }

  /**
   * 耗时任务执行结果
   */
  private R result;

  /**
   * 耗时任务
   * @return
   */
  public abstract R run();

  /**
   * 回调方法
   * @param result
   */
  public abstract void callback(R result);

  /**
   * 设置回调结果
   * @param iResult
   */
  public void setResult(R iResult) {
    result = iResult;
  }

  /**
   * 获取回调结果
   * @return
   */
  public R getResult() {
    return result;
  }

  /**
   * 发送回调消息
   */
  public void postResult() {
    Message message = Message.obtain();
    message.what = MESSAGE_POST_RESULT;
    message.obj = this;
    UiTasker.getMainHandler().sendMessage(message);
  }
}
