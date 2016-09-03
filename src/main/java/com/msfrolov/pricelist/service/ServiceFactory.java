package com.msfrolov.pricelist.service;

import com.msfrolov.pricelist.exception.PriceException;
import com.msfrolov.pricelist.util.PropertiesManager;

import java.util.Properties;

public class ServiceFactory {

    /**
     * Method for implementing flexibility with injection interface implementation
     *
     * @return an instance of service implementation
     */
    public <T> T getService(String beanName, Class beanClass) {
        T service;
        Properties properties = PropertiesManager.getProperties("properties/bean-context.properties");
        String s = properties.getProperty(beanName);
        try {
            service = (T) Class.forName(s).newInstance();
        } catch (ClassNotFoundException e) {
            throw new PriceException("failed to get service class", e);
        } catch (InstantiationException e) {
            throw new PriceException("failed to get service implementation", e);
        } catch (IllegalAccessException e) {
            throw new PriceException("failed to get access on service class", e);
        }
        return service;
    }
}
