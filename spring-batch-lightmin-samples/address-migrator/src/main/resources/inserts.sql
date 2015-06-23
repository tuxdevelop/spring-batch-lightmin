INSERT INTO BATCH_JOB_CONFIGURATION
(job_name, job_incrementer)
VALUES (
  'addressMigrationJob',
  'DATE_INCREMENTER'
);

INSERT INTO BATCH_JOB_SCHEDULER_CONFIGURATION
(job_configuration_id, scheduler_type, initial_delay, fixed_delay, task_executor_type, bean_name)
VALUES (
  1,
  2,
  10,
  60000,
  1,
  'addressMigrationJobSYNCHRONOUSLY1'
)
