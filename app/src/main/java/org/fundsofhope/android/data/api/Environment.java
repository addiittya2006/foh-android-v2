package org.fundsofhope.android.data.api;

/**
 * Created by anip on 23/07/16.
 */
public enum Environment {

    PRODUCTION {
        @Override
        public String getBaseUrl() {
            return "http://api.fundsofhope.org";
        }
    };

    public abstract String getBaseUrl();

}
