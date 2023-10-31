package com.example.basic.job.system.helper;

import com.example.basic.job.system.enums.JobStatus;
import com.example.basic.job.system.job.Job;

import java.util.concurrent.ScheduledFuture;

public class JobFuturePair {
    private final Job job;
    private final ScheduledFuture<?> scheduledFuture;

    public JobFuturePair(Job job, ScheduledFuture<?> scheduledFuture) {
        this.job = job;
        this.scheduledFuture = scheduledFuture;
    }


    public ScheduledFuture<?> getScheduledFuture() {
        return scheduledFuture;
    }

    public Job getJob() {
        return job;
    }

    public void cancel() {
        job.setStatus(JobStatus.CANCELLED);
        scheduledFuture.cancel(true);
    }
}
