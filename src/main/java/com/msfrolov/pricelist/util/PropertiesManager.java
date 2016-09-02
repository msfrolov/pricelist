package com.msfrolov.pricelist.util;

import com.msfrolov.pricelist.exception.PriceException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesManager {

    private static Map<String, Properties> propertiesCache = new HashMap<>();

    public static Properties getProperties(final String fileName) {
        try {
            Properties result;
            result = propertiesCache.get(fileName);
            if (result == null) {
                result = new Properties();
                result.load(PropertiesManager.class.getClassLoader().getResourceAsStream(fileName));
                propertiesCache.put(fileName, result);
            }
            return result;
        } catch (IOException e) {
            throw new PriceException("IOException: failed to get property", e);
        }
    }
}
