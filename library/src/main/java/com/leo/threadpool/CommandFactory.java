package com.leo.threadpool;

public class CommandFactory {

    boolean sortByLatest = false;

    public CommandFactory(boolean sortByLatest) {
        this.sortByLatest = sortByLatest;
    }

    public CommandFactory() {
        this.sortByLatest = false;
    }

    public PriorityTaskImpl getTask(String category, IPriorityTask runnable,
                                int priority, ITaskHandler handler) {
        PriorityTaskImpl task = new PriorityTaskImpl(category, sortByLatest, priority,
                runnable, handler);
        return task;
    }

    public PriorityTaskImpl getTask(String category, IPriorityTask runnable,
                                ITaskHandler handler) {
        PriorityTaskImpl task = new PriorityTaskImpl(category, sortByLatest, runnable,
                handler);
        return task;
    }
}
