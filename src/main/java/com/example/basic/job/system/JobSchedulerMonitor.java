package com.example.basic.job.system;

import com.example.basic.job.system.job.CustomJob;
import com.example.basic.job.system.scheduler.JobScheduler;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JobSchedulerMonitor {
    private final JobScheduler jobScheduler;
    private final ScheduledExecutorService monitorExecutor;

    public JobSchedulerMonitor(JobScheduler jobScheduler) {
        this.jobScheduler = jobScheduler;
        this.monitorExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    private void startMonitoring() {
        monitorExecutor.scheduleAtFixedRate(this::printRunningJobs, 0, 1, TimeUnit.SECONDS);
    }

    private void printRunningJobs() {
        int runningJobsCount = jobScheduler.getRunningJobCount();
        System.out.println("Running job count: " + runningJobsCount);
    }

    public static void main(String[] args) {
        JobScheduler jobScheduler = new JobScheduler(5);
        JobSchedulerMonitor monitor = new JobSchedulerMonitor(jobScheduler);

        // Start monitoring
        monitor.startMonitoring();

        for (int i = 0; i < 100; ++i) {
            jobScheduler.scheduleJob(new CustomJob(UUID.randomUUID()), 10, 1); // Every 1 hour
        }
    }
}
