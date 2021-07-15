package ccs.perform.util;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonProperties {
    /** ロガー */
    private static final Logger log = LoggerFactory.getLogger(CommonProperties.class);

    static Properties prop  = new Properties();
    static {
        try {
            prop.load(CommonProperties.class.getClassLoader().getResourceAsStream("common.properties"));
        } catch (Exception e) {
            log.warn("common.properties can't load.");
        }
    }

    static public String get(String key) {
        return get(key, null);
    }

    static public String get(String key, String def) {
        return prop.getProperty(key, def);
    }

}
