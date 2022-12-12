package com.project.study.exception;

public class ServerNotFoundException extends RuntimeException {
    public ServerNotFoundException(Long serverId) {
        super(String.format("[Exception] Server with Id %d not found.", serverId));
    }
}
