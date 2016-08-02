package org.fundsofhope.android.config;

/**
 * Created by anip on 23/07/16.
 */
public enum Environment {
    PRODUCTION{
        @Override
        public String getBaseUrl() {
            return "http://api.fundsofhope.org";
        }

        @Override
        public String toString() {
            return "LOCAL";
        }
    };
    public abstract String getBaseUrl();
}
