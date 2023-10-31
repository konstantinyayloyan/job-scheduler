package com.example.basic.job.system.job;

import com.example.basic.job.system.enums.JobStatus;

import java.util.UUID;

public interface Job {
    void execute();
    UUID getUniqueId();
    JobStatus getStatus();
    void setStatus(JobStatus status);
}
