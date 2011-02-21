/**
 * Added by James
 * on 2011-2-21
 */
package com.aaut.skeleton.web;

import java.util.ResourceBundle;

import javax.servlet.ServletContext;

import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import com.aaut.skeleton.commons.util.Validators;

public class ModularityLoaderListener extends ContextLoaderListener {

	public static final String MODULE_SWITCH_ON = "on";
	public static final String MODULE_SWITCH_OFF = "off";

	public static final String MODULE_RBAC_PATH = "classpath*:config/module/rbacConfig.xml";
	public static final String MODULE_NOTIFICATION_PATH = "classpath*:config/module/notificationConfig.xml";

	@Override
	protected WebApplicationContext createWebApplicationContext(
			ServletContext sc, ApplicationContext parent) {
		Class<?> contextClass = determineContextClass(sc);
		if (!ConfigurableWebApplicationContext.class
				.isAssignableFrom(contextClass)) {
			throw new ApplicationContextException("Custom context class ["
					+ contextClass.getName() + "] is not of type ["
					+ ConfigurableWebApplicationContext.class.getName() + "]");
		}
		ConfigurableWebApplicationContext wac = (ConfigurableWebApplicationContext) BeanUtils
				.instantiateClass(contextClass);

		// Assign the best possible id value.
		if (sc.getMajorVersion() == 2 && sc.getMinorVersion() < 5) {
			// Servlet <= 2.4: resort to name specified in web.xml, if any.
			String servletContextName = sc.getServletContextName();
			wac
					.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX
							+ ObjectUtils.getDisplayString(servletContextName));
		} else {
			// Servlet 2.5's getContextPath available!
			try {
				String contextPath = (String) ServletContext.class.getMethod(
						"getContextPath").invoke(sc);
				wac
						.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX
								+ ObjectUtils.getDisplayString(contextPath));
			} catch (Exception ex) {
				throw new IllegalStateException(
						"Failed to invoke Servlet 2.5 getContextPath method",
						ex);
			}
		}

		wac.setParent(parent);
		wac.setServletContext(sc);
		String configLocationParams = sc
				.getInitParameter(CONFIG_LOCATION_PARAM);
		ResourceBundle bundle = ResourceBundle.getBundle("skeleton");
		if (MODULE_SWITCH_ON.equalsIgnoreCase(bundle
				.getString("skeleton.module.rbac"))) {
			if (!Validators.isEmpty(configLocationParams)) {
				configLocationParams += ",";
			}
			configLocationParams += MODULE_RBAC_PATH;
		}

		if (MODULE_SWITCH_ON.equalsIgnoreCase(bundle
				.getString("skeleton.module.notification"))) {
			if (!Validators.isEmpty(configLocationParams)) {
				configLocationParams += ",";
			}
			configLocationParams += MODULE_NOTIFICATION_PATH;
		}

		wac.setConfigLocation(configLocationParams);
		customizeContext(sc, wac);
		wac.refresh();
		return wac;
	}
}
