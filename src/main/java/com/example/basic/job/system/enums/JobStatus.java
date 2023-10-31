package com.example.basic.job.system.enums;

public enum JobStatus {
    PENDING, RUNNING, COMPLETED, CANCELLED, FAILED;

    public boolean isPending() {
        return this == PENDING;
    }
    public boolean isCancelled() {
        return this == CANCELLED;
    }
    public boolean isCompleted() {
        return this == COMPLETED;
    }
}
