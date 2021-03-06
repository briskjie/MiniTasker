# MiniTasker

MiniTasker 是一个方便切换线程去执行耗时任务，并且可以在需要回调到 UI 线程的时候能简便切换到 UI 线程的开源库。在实际的开发中，由于项目里面并没有用到 RxJava ，所以做一些耗时的事情，切换线程还是比较麻烦的，要写不少代码。在没有 RxJava 的情况下，切换进程执行耗时任务，一般通过 `AsyncTask` 来完成，但是编写 AsyncTask 代码还是稍微有些费劲的。有时候仅仅只是做耗时任务不需要回调的时候，新开一个线程来做耗时任务很明显是很浪费资源的，为了方便解决上面提到的问题，写了这个开源库。

如果看过 `AsyncTask` 的源码，就会发现 `AsyncTask` 已经维护了一个线程池，所以可以利用 `AsyncTask` 的线程池来做些事情。毕竟这些线程池已经存在。不用白不用。

在实际写这个开源库过程中，主要是解决以下 2 种场景，一种是 `不需要回调，直接执行耗时任务` ，另外一种是 `耗时任务执行完后需要回调结果给 UI 线程`。后面对这两种场景详细讨论下。

## 不需要回调，直接执行任务
其实这个场景实现很简单。因为不需要回调，所以我们找一个非 UI 线程去执行某个代码片段就好了。
刚刚上面讲过了， MiniTasker 这个库用到了   `AsyncTask` 的线程池。所以我们直接丢一个 *Runnable* 给这个线程池，然后在这个 *Runnable* 的 *run* 方法里面执行需要执行的代码片段，这样差不多就完事了。

## 耗时任务执行完后需要回调结果给 UI 线程
这个相对上面的场景要稍微复杂些。但是我们可以对这个场景拆分下。
1. 需要做一个耗时任务，这个任务不能在 UI 线程
2. 需要找到 UI 线程，方便任务的执行结果回调到 UI 线程

对于第一条，上面的那个场景正好可以满足要求。对于第二条，执行结果需要回调到 UI 线程，说明我们需要切换线程，而线程切换我们很自然想到 `Handler`。如果某个 **Handler** 是和 UI 线程绑定的，那这个 **Handler** 的 *dispatchMessage* 方法会在 UI 线程执行，所以切换线程的思路大概有了。

和 UI 线程绑定的 Handler 可以通过传入 UI 线程的 Looper 来构造。而 UI 线程的 Looper 可以通过 `Looper.getMainLooper()` 方法获取。至此，我们的方法大概就确定了。

先利用 `AsyncTask` 的线程池来执行耗时任务，同时将结果缓存起来，然后通过 `Looper.getMainLooper()` 方法获取的 UI 线程的 Looper 构造一个 Handler ，然后给这个 Handler 发送一个消息，Handler 接受到这个消息后，在 `handleMessage` 方法里面执行回调方法，这样结果就回调给了 UI 进程。

## 接入方法

首先在 project 的 build.gradle 脚本添加

```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

然后在 module 的  build.gradle 脚本添加

```gradle
dependencies {
	compile 'com.github.XanderWang:MiniTasker:1.0.0'
}
```


