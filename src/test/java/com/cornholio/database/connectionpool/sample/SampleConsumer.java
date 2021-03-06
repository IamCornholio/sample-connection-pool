package com.cornholio.database.connectionpool.sample;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This is a sample consumer thread. Used for testing only. Each instance of this thread will do the following:
 * <ol>
 * <li>Get a connection</li>
 * <li>Wait for a given threadWaitTime milliseconds</li>
 * <li>Close the connection</li>
 * </ol>
 *
 * @author nikhilagarwal
 */
public class SampleConsumer implements Runnable {

	private static final String LOG_THREAD_END = "Ending thread: ";
	private static final String LOG_THREAD_START = "Starting thread: ";
	private static Logger logger;
	private Connection connection;
	private Long threadWaitTime;

	public SampleConsumer(final Connection connection, final Long threadWaitTime) {
		super();
		this.connection = connection;
		this.threadWaitTime = threadWaitTime;
	}

	private static Logger getLogger() {
		if (SampleConsumer.logger == null) {
			SampleConsumer.logger = Logger.getLogger(SampleConsumer.class.getSimpleName());
		}
		return SampleConsumer.logger;
	}

	// a main method to test the Sample Consumer
	public static void main(final String... args) {
		SampleConsumer consumer;
		try {
			consumer = new SampleConsumer(SampleConnectionUtil.getConnection(), 20000L);
			consumer.run();
		} catch (final SQLException e) {
			SampleConsumer.getLogger().log(Level.ERROR, e.getMessage(), e);
		}
	}

	/**
	 * Simulated connection processing. It actually just waits around for threadWaitTime.
	 *
	 * @throws InterruptedException
	 */
	private void doSomething() throws InterruptedException {
		Thread.sleep(this.getThreadWaitTime());
	}

	public Connection getConnection() {
		return this.connection;
	}

	public void setConnection(final Connection connection) {
		this.connection = connection;
	}

	public Long getThreadWaitTime() {
		return this.threadWaitTime;
	}

	public void setThreadWaitTime(final Long threadWaitTime) {
		this.threadWaitTime = threadWaitTime;
	}

	@Override
	public void run() {
		try {
			if (SampleConsumer.getLogger().isInfoEnabled()) {
				SampleConsumer.getLogger().log(Level.INFO, SampleConsumer.LOG_THREAD_START + this.hashCode());
			}

			this.doSomething();

			this.getConnection().close();

			if (SampleConsumer.getLogger().isInfoEnabled()) {
				SampleConsumer.getLogger().log(Level.INFO, SampleConsumer.LOG_THREAD_END + this.hashCode());
			}
		} catch (final Exception e) {
			SampleConsumer.getLogger().log(Level.ERROR, e.getMessage(), e);
		}
	}
}