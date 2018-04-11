package com.xander.minitasker;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by xander on 2018/4/11.
 */

public abstract class UiTasker<R> {

  private static final int MESSAGE_POST_RESULT = 100;

  private static InternalHandler sHandler;

  private static Handler getMainHandler() {
    synchronized (AsyncTask.class) {
      if (sHandler == null) {
        sHandler = new InternalHandler(Looper.getMainLooper());
      }
      return sHandler;
    }
  }

  private static class InternalHandler extends Handler {
    public InternalHandler(Looper looper) {
      super(looper);
    }

    @SuppressWarnings({ "unchecked", "RawUseOfParameterizedType" })
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case MESSAGE_POST_RESULT:
          // There is only one result
          if (null != msg.getCallback()) {
            msg.getCallback().run();
          }
          break;
      }
    }
  }

  private R result;

  public abstract R run();

  public abstract void callback(R result);

  public void setResult(R iResult) {
    result = iResult;
  }

  public void postResult() {
    Message message = Message.obtain(getMainHandler(), new Runnable() {
      @Override public void run() {
        callback(result);
      }
    });
    message.what = MESSAGE_POST_RESULT;
    getMainHandler().sendMessage(message);
  }
}
