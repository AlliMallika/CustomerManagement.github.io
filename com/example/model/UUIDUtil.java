package com.example.model;

import java.util.UUID;

public class UUIDUtil {
    public static String generateTestUUID() {
        return "test" + UUID.randomUUID().toString().replace("-", "").substring(0, 28);
    }
}
