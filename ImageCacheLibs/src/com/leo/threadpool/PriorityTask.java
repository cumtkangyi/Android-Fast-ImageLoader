package com.leo.threadpool;

public class PriorityTask extends AbstractCommand implements Runnable {

	public IPriorityTask runnable;
	private final ITaskHandler handler;
	public String category;

	public PriorityTask(String category, boolean commandSortByLatest,
			IPriorityTask runnable, ITaskHandler handler) {
		super(commandSortByLatest);
		this.category = category;
		this.handler = handler;
		this.runnable = runnable;
	}

	public PriorityTask(String category, boolean commandSortByLatest,
			int priority, IPriorityTask runnable, ITaskHandler handler) {
		super(commandSortByLatest, priority);
		this.category = category;
		this.handler = handler;
		this.runnable = runnable;
	}

	@Override
	public void run() {
		if (runnable != null) {
			runnable.run();
		}
		if (handler != null) {
			handler.onFinish(runnable.getFlag());
		}
	}
}
