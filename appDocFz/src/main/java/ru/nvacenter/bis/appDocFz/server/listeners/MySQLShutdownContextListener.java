package ru.nvacenter.bis.appDocFz.server.listeners;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;

public class MySQLShutdownContextListener implements ServletContextListener {

	private final Logger logger = Logger.getLogger(MySQLShutdownContextListener.class.getName());
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		try {
			AbandonedConnectionCleanupThread.shutdown();
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, "", e);
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		//Nothing to do
	}

}
