package com.example.basic.job.system;

import com.example.basic.job.system.job.CustomJob;
import com.example.basic.job.system.scheduler.JobScheduler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JobSchedulerTest {
    private static JobScheduler jobScheduler;

    @BeforeAll
    public static void setUp() {
        jobScheduler = new JobScheduler(2);
    }

    @AfterAll
    public static void tearDown() {
        jobScheduler.stop();
    }

    @Test
    public void testJobScheduling() throws InterruptedException {
        CustomJob job1 = new CustomJob(UUID.randomUUID());
        CustomJob job2 = new CustomJob(UUID.randomUUID());

        jobScheduler.scheduleJob(job1, 0, 1); // Every 1 hour with no delay
        jobScheduler.scheduleJob(job2, 0, 2); // Every 2 hours with no delay

        // Wait for a sufficient time to ensure jobs have been executed
        TimeUnit.SECONDS.sleep(5);

        assertTrue(job1.getStatus().isCompleted());
        assertTrue(job2.getStatus().isCompleted());
    }

    @Test
    public void testJobCancellation() {
        CustomJob job1 = new CustomJob(UUID.randomUUID());
        CustomJob job2 = new CustomJob(UUID.randomUUID());

        jobScheduler.scheduleJob(job1, 10, 1, TimeUnit.SECONDS); // Every 1 second after 10 seconds delay
        jobScheduler.scheduleJob(job2, 10, 1, TimeUnit.SECONDS); // Every 1 second after 10 seconds delay
        jobScheduler.cancelJob(job1.getUniqueId());

        assertTrue(job1.getStatus().isCancelled());
        assertTrue(job2.getStatus().isPending());
    }
}
