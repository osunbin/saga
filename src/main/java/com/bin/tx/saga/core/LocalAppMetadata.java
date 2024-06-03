package com.bin.tx.saga.core;

public interface LocalAppMetadata {

    String getLocalAppName();

    String getRegisterUrl();




      LocalAppMetadata EMPTY = new LocalAppMetadata() {
        @Override
        public String getLocalAppName() {
            return "";
        }

        @Override
        public String getRegisterUrl() {
            return "";
        }

    };

}
