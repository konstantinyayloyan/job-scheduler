package com.example.basic.job.system.scheduler;

import com.example.basic.job.system.enums.JobStatus;
import com.example.basic.job.system.helper.JobFuturePair;
import com.example.basic.job.system.job.Job;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class JobScheduler {
    private final ScheduledExecutorService scheduler;
    private final int maxConcurrentJobs;
    private final ConcurrentHashMap<UUID, JobFuturePair> jobRegistry;
    private final AtomicBoolean isShuttingDown;
    private final AtomicInteger runningJobsCount;

    public JobScheduler(int maxConcurrentJobs) {
        this.maxConcurrentJobs = maxConcurrentJobs;
        this.scheduler = Executors.newScheduledThreadPool(maxConcurrentJobs);
        this.jobRegistry = new ConcurrentHashMap<>();
        this.isShuttingDown = new AtomicBoolean(false);
        this.runningJobsCount = new AtomicInteger(0);
    }

    public void scheduleJob(final Job job, final long delay, final long period) {
        scheduleJob(job, delay, period, TimeUnit.SECONDS);
    }

    public void scheduleJob(final Job job, final long delay, final long period, final TimeUnit timeUnit) {
        if (canExecuteJob(job)) {
            Runnable jobTask = () -> {
                if (!Thread.currentThread().isInterrupted()) {
                    job.setStatus(JobStatus.RUNNING);
                    try {
                        job.execute();
                        runningJobsCount.incrementAndGet();
                        job.setStatus(JobStatus.COMPLETED);
                    } catch (Exception e) {
                        job.setStatus(JobStatus.FAILED);
                    } finally {
                        jobRegistry.remove(job.getUniqueId());
                        runningJobsCount.decrementAndGet();
                    }
                }
            };

            final ScheduledFuture<?> scheduledFuture = period > 0
                    ? scheduler.scheduleAtFixedRate(jobTask, delay, period, timeUnit)
                    : scheduler.schedule(jobTask, delay, timeUnit);

            jobRegistry.put(job.getUniqueId(), new JobFuturePair(job, scheduledFuture));
        } else {
            System.out.println("Can't execute job");
        }
    }

    public void stop() {
        if (isShuttingDown.compareAndSet(false, true)) {
            scheduler.shutdown();
        }
    }

    public void cancelJob(final UUID uniqueId) {
        JobFuturePair jobFuturePair = jobRegistry.remove(uniqueId);
        if (jobFuturePair != null) {
            System.out.println("Cancelling the job with id " + uniqueId);
            jobFuturePair.cancel();
        } else {
            System.out.println("The job with id " + uniqueId + " is already finished or cancelled");
        }
    }

    public int getRunningJobCount() {
        return runningJobsCount.get();
    }

    private boolean canExecuteJob(final Job job) {
        return !isShuttingDown.get() && job.getStatus().isPending() && runningJobsCount.get() < maxConcurrentJobs;
    }
}
