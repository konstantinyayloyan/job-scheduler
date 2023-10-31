package com.example.basic.job.system.job;

import com.example.basic.job.system.enums.JobStatus;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class CustomJob implements Job {
    private final UUID uniqueId;
    private final AtomicBoolean isCancelled;
    private JobStatus status;

    public CustomJob(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.status = JobStatus.PENDING;
        this.isCancelled = new AtomicBoolean(false);
    }

    @Override
    public void execute() {
        if (isCancelled.get()) {
            throw new IllegalStateException("Job was cancelled.");
        }
        System.out.println("Executing job with unique id " + uniqueId);
        setStatus(JobStatus.COMPLETED);
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public JobStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(JobStatus status) {
        this.status = status;
    }
}
