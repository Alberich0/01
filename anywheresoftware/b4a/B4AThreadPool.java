package anywheresoftware.b4a;

import java.util.Iterator;
import java.util.WeakHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class B4AThreadPool {
    private static final int THREADS_SPARE = 5;
    private final WeakHashMap<Object, ConcurrentHashMap<Integer, Future<?>>> futures;
    private ThreadPoolExecutor pool;
    private final ConcurrentLinkedQueue<QueuedTask> queueOfTasks;

    /* renamed from: anywheresoftware.b4a.B4AThreadPool.1 */
    class C00021 extends ThreadPoolExecutor {
        C00021(int $anonymous0, int $anonymous1, long $anonymous2, TimeUnit $anonymous3, BlockingQueue $anonymous4) {
            super($anonymous0, $anonymous1, $anonymous2, $anonymous3, $anonymous4);
        }

        protected void afterExecute(Runnable r, Throwable t) {
            for (int i = 0; i < 1; i++) {
                QueuedTask qt = (QueuedTask) B4AThreadPool.this.queueOfTasks.poll();
                if (qt != null) {
                    BA.handler.post(qt);
                }
            }
        }
    }

    private static class MyThreadFactory implements ThreadFactory {
        private final ThreadFactory defaultFactory;

        private MyThreadFactory() {
            this.defaultFactory = Executors.defaultThreadFactory();
        }

        public Thread newThread(Runnable r) {
            Thread t = this.defaultFactory.newThread(r);
            t.setDaemon(true);
            return t;
        }
    }

    class QueuedTask implements Runnable {
        final Object container;
        final Runnable task;
        final int taskId;

        public QueuedTask(Runnable task, Object container, int taskId) {
            this.task = task;
            this.container = container;
            this.taskId = taskId;
        }

        public void run() {
            if (B4AThreadPool.this.pool.getActiveCount() > B4AThreadPool.this.pool.getMaximumPoolSize() - B4AThreadPool.THREADS_SPARE) {
                BA.handler.postDelayed(this, 50);
            } else {
                B4AThreadPool.this.submitToPool(this.task, this.container, this.taskId);
            }
        }
    }

    public B4AThreadPool() {
        this.futures = new WeakHashMap();
        this.queueOfTasks = new ConcurrentLinkedQueue();
        this.pool = new C00021(0, 50, 60, TimeUnit.SECONDS, new SynchronousQueue());
        this.pool.setThreadFactory(new MyThreadFactory());
    }

    public void submit(Runnable task, Object container, int taskId) {
        if (this.pool.getActiveCount() > this.pool.getMaximumPoolSize() - THREADS_SPARE) {
            this.queueOfTasks.add(new QueuedTask(task, container, taskId));
        } else {
            submitToPool(task, container, taskId);
        }
    }

    private void submitToPool(Runnable task, Object container, int taskId) {
        try {
            ConcurrentHashMap<Integer, Future<?>> map;
            Future<?> f = this.pool.submit(task);
            synchronized (this.futures) {
                map = (ConcurrentHashMap) this.futures.get(container);
                if (map == null) {
                    map = new ConcurrentHashMap();
                    this.futures.put(container, map);
                }
            }
            Iterator<Future<?>> it = map.values().iterator();
            while (it.hasNext()) {
                if (((Future) it.next()).isDone()) {
                    it.remove();
                }
            }
            map.put(Integer.valueOf(taskId), f);
        } catch (RejectedExecutionException e) {
            RejectedExecutionException ree = e;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
            submitToPool(task, container, taskId);
        }
    }

    public boolean isRunning(Object container, int taskId) {
        ConcurrentHashMap<Integer, Future<?>> map = (ConcurrentHashMap) this.futures.get(container);
        if (map == null) {
            return false;
        }
        Future<?> f = (Future) map.get(Integer.valueOf(taskId));
        if (f == null) {
            return false;
        }
        return !f.isDone();
    }

    public void markTaskAsFinished(Object container, int taskId) {
        ConcurrentHashMap<Integer, Future<?>> map = (ConcurrentHashMap) this.futures.get(container);
        if (map != null) {
            map.remove(Integer.valueOf(taskId));
        }
    }
}
