INSERT INTO BATCH_JOB_CONFIGURATION
(job_name, job_incrementer)
VALUES (
  'addressMigrationJob',
  'DATE_INCREMENTER'
);

INSERT INTO BATCH_JOB_CONFIGURATION
(job_name, job_incrementer)
VALUES (
  'UnknownJob',
  'DATE_INCREMENTER'
);

INSERT INTO BATCH_JOB_CONFIGURATION
(job_name, job_incrementer)
VALUES (
  'addressPrinterJob',
  'NONE'
);


INSERT INTO BATCH_JOB_SCHEDULER_CONFIGURATION
(job_configuration_id, scheduler_type, initial_delay, fixed_delay, task_executor_type, bean_name, status)
VALUES (
  1,
  2,
  10,
  60000,
  1,
  'addressMigrationJobSYNCHRONOUSLY1',
  'RUNNING'
);

INSERT INTO BATCH_JOB_SCHEDULER_CONFIGURATION
(job_configuration_id, scheduler_type, initial_delay, fixed_delay, task_executor_type, bean_name, status)
VALUES (
  2,
  2,
  10,
  10,
  1,
  'UnknownJobSYNCHRONOUSLY1',
  'RUNNING'
);


INSERT INTO BATCH_JOB_SCHEDULER_CONFIGURATION
(job_configuration_id, scheduler_type, initial_delay, fixed_delay, task_executor_type, bean_name, status)
VALUES (
  3,
  2,
  10,
  6000,
  1,
  'addressPrinterJobSYNCHRONOUSLY1',
  'RUNNING'
);

