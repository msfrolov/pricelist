package com.msfrolov.pricelist.action;

import com.msfrolov.pricelist.exception.PriceException;
import com.msfrolov.pricelist.util.PropertiesManager;

import java.util.Map;
import java.util.Properties;

public class ActionFactory {

    private Map<String, Action> actions;

    public Action getAction(String actionName) {
        Action service;
        Properties properties = PropertiesManager.getProperties("properties/action-mapping.properties");
        String s = properties.getProperty(actionName);
        try {
            service = (Action) Class.forName(s).newInstance();
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
