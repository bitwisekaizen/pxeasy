package com.thegrayfiles.exception;

public class SessionNotFound extends Exception {
    public SessionNotFound(String uuid) {
        super("Session with uuid " + uuid + " was not found.");
    }
}
