package hu.u_szeged.inf.fog.observer.service;

import hu.u_szeged.inf.fog.simulator.executor.service.SimulatorJobExecutorService;
import hu.u_szeged.inf.fog.simulator.executor.dao.SimulatorJobDao;
import hu.u_szeged.inf.fog.simulator.executor.model.SimulatorJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class JobObserverService {

    private static final Logger logger = LoggerFactory.getLogger(JobObserverService.class);

    private final SimulatorJobExecutorService jobExecutorService;
    private final SimulatorJobDao jobDao;
    private ScheduledExecutorService scheduler;
    private ExecutorService jobExecutor;

    @Value("${job.observer.check.interval}")
    private long checkInterval;

    @Value("${job.executor.thread.pool.size}")
    private int jobExecutorThreadPoolSize;

    @Value("${job.scheduler.pool.size}")
    private int jobSchedulerPoolSize;

    @Autowired
    public JobObserverService(SimulatorJobExecutorService jobExecutorService, SimulatorJobDao jobDao) {
        this.jobExecutorService = jobExecutorService;
        this.jobDao = jobDao;
    }

    @PostConstruct
    public void init() {
        if (jobSchedulerPoolSize <= 0) {
            throw new IllegalArgumentException("Job scheduler pool size must be greater than zero");
        }

        if (jobExecutorThreadPoolSize <= 0) {
            throw new IllegalArgumentException("Job executor thread pool size must be greater than zero");
        }

        this.scheduler = Executors.newScheduledThreadPool(jobSchedulerPoolSize);
        this.jobExecutor = Executors.newFixedThreadPool(jobExecutorThreadPoolSize);

        scheduleJobChecking();
    }

    @PreDestroy
    public void shutdown() {
        logger.info("Shutting down JobObserverService...");
        scheduler.shutdown();
        jobExecutor.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
            if (!jobExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                jobExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            jobExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void scheduleJobChecking() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                checkForJobs();
            } catch (Exception e) {
                logger.error("Error while checking for jobs", e);
            }
        }, 0, checkInterval, TimeUnit.SECONDS);
    }

    private void checkForJobs() {
        logger.info("Checking for new jobs...");
        SimulatorJob job = jobDao.retrieveNextAvailableJob();
        if (job != null) {
            logger.info("Submitting job {} for execution", job.getId());
            Future<?> future = jobExecutor.submit(() -> {
                try {
                    jobExecutorService.runSimulationForJob(job);
                } catch (Exception e) {
                    logger.error("Error executing job {}", job.getId(), e);
                }
            });

            // Schedule a timeout for the jobw
            scheduler.schedule(() -> {
                if (!future.isDone()) {
                    logger.warn("Job {} is taking too long and will be cancelled", job.getId());
                    future.cancel(true);
                }
            }, 30, TimeUnit.MINUTES); // 30 minutes timeout, adjust as needed
        } else {
            logger.info("No new jobs found");
        }
    }
}
