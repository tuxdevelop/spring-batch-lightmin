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

INSERT INTO BATCH_JOB_CONFIGURATION
(job_name, job_incrementer)
VALUES (
  'addressMigrationJob',
  'DATE_INCREMENTER'
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
  'STOPPED'
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
  'STOPPED'
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
  'STOPPED'
);

INSERT INTO BATCH_JOB_LISTENER_CONFIGURATION
(job_configuration_id, listener_type, source_folder, file_pattern, poller_period, task_executor_type, bean_name, status)
VALUES (
  4,
  1,
  'src/test/resources/properties/sample/client/input',
  '*.txt',
  600,
  1,
  'addressMigrationJobSYNCHRONOUSLY4',
  'ACTIVE'
);

