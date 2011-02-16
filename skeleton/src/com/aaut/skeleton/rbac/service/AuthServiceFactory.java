package com.aaut.skeleton.rbac.service;

import org.apache.log4j.Logger;

import com.aaut.skeleton.rbac.exception.ServiceNotAvailableException;

/**
 * 
 * @author James
 */
public class AuthServiceFactory {

	private static Logger logger = Logger.getLogger(AuthServiceFactory.class);

	public static AuthService getAuthService(String serviceClass)
			throws ServiceNotAvailableException {
		AuthService service = null;

		try {
			service = (AuthService) Class.forName(serviceClass).newInstance();
		} catch (ClassNotFoundException e) {
			logger.error("Class not found");
			throw new ServiceNotAvailableException(AuthServiceFactory.class
					.getName()
					+ ".classNotFound", e);
		} catch (InstantiationException e) {
			logger
					.error("Error initializing service, it doesn't have a correct constructor");
			throw new ServiceNotAvailableException(AuthServiceFactory.class
					.getName()
					+ ".instantiationError", e);
		} catch (IllegalAccessException e) {
			logger
					.error("Error initializing service, it doesn't have a public constructor");
			throw new ServiceNotAvailableException(AuthServiceFactory.class
					.getName()
					+ ".instantiationError", e);
		}

		return service;
	}

}
