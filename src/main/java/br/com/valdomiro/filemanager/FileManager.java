package br.com.valdomiro.filemanager;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

public class FileManager {

    private final Scheduler scheduler;

    private FileManager() throws Exception {
        super();
        scheduler = new StdSchedulerFactory().getScheduler();
    }

    public static void main(final String[] args) throws Exception {
        new FileManager().start();
    }

    private void addJobFila(final Class clazz, final String cronExpression)
            throws Exception {
        if (cronExpression == null || "0".equals(cronExpression)) {
            return;
        }

        final JobDetail job = new JobDetail();
        job.setName(clazz.getSimpleName().concat("_JOB_").concat(String.valueOf(Math.random())));
        job.setJobClass(clazz);

        final CronTrigger trigger = new CronTrigger();
        trigger.setName(clazz.getSimpleName().concat("_Trigger_").concat(String.valueOf(Math.random())));
        trigger.setCronExpression(cronExpression);

        scheduler.scheduleJob(job, trigger);
    }

    private void start() throws Exception {
        addJobFila(MoverArquivosJob.class, "0/10 * * 1/1 * ? *");
        scheduler.start();
    }

}
