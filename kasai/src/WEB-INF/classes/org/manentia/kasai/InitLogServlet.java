/*
 * SetupLogServlet.java
 *
 * Created on 28 de junio de 2006, 04:45 PM
 *
 */

package org.manentia.kasai;

import java.util.MissingResourceException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.PropertyConfigurator;

import com.manentia.commons.configuration.Configuration;

/**
 * Permite configurar el sistema de logging.
 *
 * @author norbes 
 */
public class InitLogServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2469888309483339294L;



	/** Initializa el servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        String configureLogFile = null;
        // Seteo la recarga en cinco minutos
        long delay = 1000 * 60 * 5;
        
        try {
            configureLogFile = Configuration.getInstance(org.manentia.kasai.Constants.CONFIG_PROPERTY_FILE).getString("configure.log.file");
        } catch (MissingResourceException mre) {
            System.out.println("ERROR: problema para leer la propiedad de configuracion de log");
        }

        PropertyConfigurator.configureAndWatch(configureLogFile, delay);
    }
    

    
    /** Destruye el servlet.
     */
    public void destroy() {
        super.destroy();
    }
}
