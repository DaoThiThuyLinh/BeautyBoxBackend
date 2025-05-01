package org.beautybox.common;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

public class NanoId {

    public String gen(){
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        int size = 9;

        return NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, alphabet, size);
    }
}
