package com.leo.threadpool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DThreadPool implements IDThreadPool {
    /**
     * Tyrant
     */
    TyrantExecutor tyrantExecuter;
    /**
     * Coward
     */
    CowardExecutor cowardExecuter;

    CommandFactory tyrantCommandFactory;
    CommandFactory cowardCommandFactory;

    PriorityBlockingQueue<Runnable> tyrantQueue;
    PriorityBlockingQueue<Runnable> cowradQueue;

    ConcurrentHashMap<String, IPriorityTask> taskManager = new ConcurrentHashMap<String, IPriorityTask>();

    /**
     * Lock used for all public operations
     */
    private final ReentrantLock lock;

    public static IDThreadPool newThreadPool(int cowardSize, int tyrantSize,
                                             int despoticLimit, boolean sortByLatest) {
        return new DThreadPool(cowardSize, tyrantSize, despoticLimit, 0,
                sortByLatest);
    }

    private DThreadPool(int cowardSize, int tyrantSize, int despoticLimit,
                           long keepAliveTime, boolean sortByLatest) {
        ReentrantLock cowardPauseLock = new ReentrantLock();
        Condition unpaused = cowardPauseLock.newCondition();
        tyrantQueue = new PriorityBlockingQueue<Runnable>();
        cowradQueue = new PriorityBlockingQueue<Runnable>();
        PriorityThreadFactory threadFactory = new PriorityThreadFactory(
                "thread-pool", 10);
        tyrantExecuter = new TyrantExecutor(tyrantSize, despoticLimit, tyrantQueue,
                threadFactory, cowardPauseLock, unpaused);
        cowardExecuter = new CowardExecutor(cowardSize,
                tyrantExecuter.getChain(), cowradQueue, threadFactory,
                cowardPauseLock, unpaused);
        tyrantCommandFactory = new CommandFactory(true);
        cowardCommandFactory = new CommandFactory(false);
        lock = new ReentrantLock();
    }

    ITaskHandler th = new ITaskHandler() {

        @Override
        public void onFinish(String flag) {
            lock.lock();
            try {
                if (taskManager.containsKey(flag)) {
                    taskManager.remove(flag);
                }
            } finally {
                lock.unlock();
            }
        }
    };

    private void execute(String category, IPriorityTask runnable,
                         TaskPriority priority) {
        if (runnable != null) {
            if (priority.ordinal() > TaskPriority.BACK_MAX.ordinal()) {
                tyrantExecuter.execute(tyrantCommandFactory.getTask(category, runnable,
                        priority.ordinal(), th));
            } else {
                cowardExecuter.execute(cowardCommandFactory.getTask(category, runnable,
                        priority.ordinal(), th));
            }
        }
    }

    @Override
    public void shutdownNow() {
        tyrantExecuter.shutdownNow();
        cowardExecuter.shutdownNow();
    }

    // public void put(String category, TaskPriority runnable) {
    // put(category, runnable, IPriorityTask.PRIORITY_MEDIUM);
    //
    // }

    public void put(String category, IPriorityTask runnable,
                    TaskPriority priority) throws NullPointerException {
        if (runnable == null) {
            throw new NullPointerException();
        }
        String key = runnable.getFlag();
        lock.lock();
        try {
            if (taskManager.containsKey(key)) {
                if (!taskManager.get(key).onRepeatPut(runnable)) {
                    runnable.isolateFlag();
                    taskManager.put(runnable.getFlag(), runnable);
                    execute(category, runnable, priority);
                }
            } else {
                taskManager.put(runnable.getFlag(), runnable);
                execute(category, runnable, priority);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void cancelQueueByCategory(String category) {
        ArrayList<Runnable> keys = new ArrayList<Runnable>();
        ArrayList<Runnable> buffer = new ArrayList<Runnable>();
        lock.lock();
        try {
            tyrantQueue.drainTo(keys);
            for (Runnable cmd : keys) {
                if (((PriorityTask) cmd).category.equals(category)) {
                    buffer.add(cmd);
                }
            }
            keys.removeAll(buffer);
            tyrantQueue.addAll(keys);
            keys.clear();
            cowradQueue.drainTo(keys);
            for (Runnable cmd : keys) {
                if (((PriorityTask) cmd).category.equals(category)) {
                    buffer.add(cmd);
                }
            }
            keys.removeAll(buffer);
            cowradQueue.addAll(keys);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void cancelQueueByTaskID(int taskId) {
        lock.lock();
        try {
            Collection<IPriorityTask> ipts = taskManager.values();
            ArrayList<IPriorityTask> needClean = new ArrayList<IPriorityTask>();
            for (IPriorityTask task : ipts) {
                if (task.unregisterListener(taskId)) {
                    needClean.add(task);
                }
            }
            // clean in Queue.
            if (needClean.size() > 0) {
                ArrayList<Runnable> keys = new ArrayList<Runnable>();
                ArrayList<Runnable> buffer = new ArrayList<Runnable>();
                tyrantQueue.drainTo(keys);
                PriorityTask pt;
                for (Runnable cmd : keys) {
                    pt = (PriorityTask) cmd;
                    if (needClean.contains(pt.runnable)) {
                        buffer.add(cmd);
                    }
                }
                keys.removeAll(buffer);
                tyrantQueue.addAll(keys);
            }
        } finally {
            lock.unlock();
        }
    }

    private final class CowardExecutor extends ThreadPoolExecutor {
        private boolean isPaused;
        private ReentrantLock pauseLock;
        private Condition unpaused;
        private Chain chain;

        public CowardExecutor(int poolSize, Chain _chain,
                              BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
                              ReentrantLock _pauseLock, Condition _unpaused) {
            super(poolSize, poolSize, 0, TimeUnit.MILLISECONDS, workQueue,
                    threadFactory);
            this.pauseLock = _pauseLock;
            this.unpaused = _unpaused;
            this.chain = _chain;
        }

        protected void beforeExecute(Thread t, Runnable r) {
            super.beforeExecute(t, r);
            pauseLock.lock();
            try {
                isPaused = !chain.allowBreath();
                while (isPaused) {
                    unpaused.await();
                    isPaused = !chain.allowBreath();
                }
            } catch (InterruptedException ie) {
                t.interrupt();
            } finally {
                pauseLock.unlock();
            }
        }
    }

    private final class TyrantExecutor extends ThreadPoolExecutor {
        private ReentrantLock pauseLock;
        private Condition unpaused;
        private volatile Chain chain;
        private volatile int despoticLimit;

        public TyrantExecutor(int poolSize, int _despoticLimit,
                              BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
                              ReentrantLock _pauseLock, Condition _unpaused) {
            super(poolSize, poolSize, 0, TimeUnit.MILLISECONDS, workQueue,
                    threadFactory);
            this.despoticLimit = _despoticLimit;
            this.pauseLock = _pauseLock;
            this.unpaused = _unpaused;
            chain = new Chain() {

                @Override
                public boolean allowBreath() {
                    return getActiveCount() < despoticLimit;
                }
            };
        }

        public Chain getChain() {
            return chain;
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
            pauseLock.lock();
            try {
                if (chain.allowBreath()) {
                    unpaused.signalAll();
                }
            } finally {
                pauseLock.unlock();
            }
        }

    }

    private interface Chain {
        boolean allowBreath();
    }

    @Override
    public int getTaskCount() {
        return tyrantExecuter.getActiveCount() + cowardExecuter.getActiveCount();
    }
}
