package com.infeez.ignite;

import java.util.HashSet;
import java.util.Set;

public class IgniteFinder {

    private final Set<String> addresses = new HashSet<>();

    public IgniteFinder(String envAddress, String envPort) {
        boolean addPort = envPort != null && !envPort.trim().isEmpty();
        if (envAddress != null && !envAddress.trim().isEmpty()) {
            String[] addressAndMask = envAddress.split("/");
            switch (addressAndMask.length) {
                case (2): {
                    String address = addressAndMask[0];
                    String cutAddress = address.substring(0, address.lastIndexOf(".") + 1);
                    int exp = 32 - Integer.valueOf(addressAndMask[1]);
                    if (exp > 1) {
                        int left = (int) Math.pow(2, exp) - 2;
                        for (int i = left; i > 2; i--) {
                            addresses.add(cutAddress + i + (addPort ? (":" + envPort) : ""));
                        }
                    }
                    else {
                        addresses.add("127.0.0.1");
                    }
                    break;
                }
                case (1): {
                    for (String address : addressAndMask[0].split(";")) {
                        addresses.add(address + (addPort ? (":" + envPort) : ""));
                    }
                    break;
                }
                default: {
                    addresses.add("127.0.0.1");
                }
            }
        }
    }

    public Set<String> getAddresses() {
        return addresses;
    }
}