# Job System

The Job System is a basic Java application that allows users to schedule and execute jobs of different types. This system is designed for developers who want to create and run asynchronous job definitions. It supports custom job types and provides features for scheduling, concurrency management, and job monitoring.

## Features

- Support for custom job types defined by developers.
- Tracking of a job's lifecycle, including its current state (e.g., running, failed).
- Scheduling of jobs for periodic execution.
- Immediate execution of one-time jobs.
- Concurrency management: Jobs run concurrently, with a limit on the number of concurrent jobs.
- Cancellation of jobs with a mechanism to notify them to abort and clean up.

## Usage

1. Create a `JobScheduler` instance with a maximum limit on concurrent jobs.

```java
JobScheduler jobScheduler = new JobScheduler(maxConcurrentJobs);
```

2. Define and schedule custom jobs using the `JobScheduler`. Jobs can be scheduled for periodic or one-time execution.

```java
CustomJob job1 = new CustomJob(UUID.randomUUID(), "TypeA");
jobScheduler.scheduleJob(job1, delay, period);
```

3. To monitor running jobs, you can create a `JobSchedulerMonitor` and start it. This will periodically print the running job count.

```java
JobSchedulerMonitor monitor = new JobSchedulerMonitor(jobScheduler);
monitor.startMonitoring();
```

4. To cancel a job, use the `cancelJob` method.

```java
jobScheduler.cancelJob(job.getUniqueId());
```

5. After you're done, be sure to shut down the `JobScheduler` and stop the monitor.

```java
jobScheduler.stop();
monitor.stopMonitoring();
```

## Building

The Job System is implemented in Java 8 or later. You can build and run it using your preferred Java development environment or the command line.

## Testing

The system includes unit tests for various components to ensure correct behavior. You can run these tests to verify the functionality.
