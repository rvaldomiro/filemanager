package br.com.valdomiro.filemanager;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

public class FileManager {

	public static void main(final String[] args) throws Exception {
		final FileManager fileManager = new FileManager();
		fileManager.start();
	}

	private Scheduler scheduler;

	private FileManager() {
		super();
	}

	private void addJobFila(@SuppressWarnings("rawtypes") final Class clazz, final String cronExpression)
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
		scheduler = new StdSchedulerFactory().getScheduler();
		addJobFila(MoverArquivosJob.class, "0/10 * * 1/1 * ? *");
		scheduler.start();
	}

}
