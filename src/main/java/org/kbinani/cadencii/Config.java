package org.kbinani.cadencii;

import java.util.*;


public class Config {
    private static TreeMap<String, Boolean> mDirectives = new TreeMap<String, Boolean>();

    static {
        mDirectives.put("aquestone", false);
        mDirectives.put("script", false);
        mDirectives.put("midi", true);
        mDirectives.put("vocaloid", true);
        mDirectives.put("debug", false);
        mDirectives.put("property", true);
    }

    public static String getWineVersion() {
        return "1.1.2";
    }

    public static TreeMap<String, Boolean> getDirectives() {
        TreeMap<String, Boolean> ret = new TreeMap<String, Boolean>();

        for (Iterator<String> itr = mDirectives.keySet().iterator();
                itr.hasNext();) {
            String key = itr.next();
            ret.put(key, mDirectives.get(key));
        }

        return ret;
    }
}
