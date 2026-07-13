package com.mycompany.krs_sistem.auth;

public class Session {
    private static String currentUserId;
    
    public static void setCurrentUserId(String userId) {
        currentUserId = userId;
    }
    
    public static String getCurrentUserId() {
        return currentUserId;
    }
}
