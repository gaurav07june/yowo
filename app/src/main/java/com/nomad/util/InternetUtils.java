package com.nomad.util;

import java.net.InetAddress;

/**
 * Created by karan.kalsi on 4/21/2017.
 */
public class InternetUtils {

    /**
     * Returns true if internet is available else returns false.
     * @return
     */
    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }

    }

