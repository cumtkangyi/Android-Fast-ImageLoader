package com.sunny.threadpool;

public class AbstractCommand implements Comparable<AbstractCommand> {

	private boolean commandSortByLatest = true;
	int priority;
	long time;

	AbstractCommand(boolean commandSortByLatest) {
		this.commandSortByLatest = commandSortByLatest;
		this.time = System.currentTimeMillis();
	}

	AbstractCommand(boolean commandSortByLatest, int priority) {
		this(commandSortByLatest);
		this.priority = priority;
	}

	@Override
	public int compareTo(AbstractCommand another) {
		int result = another.priority - priority;
		if (result == 0) {
			result = (int) (commandSortByLatest ? (another.time - time)
					: (time - another.time));
		}
		return result;
	}

}