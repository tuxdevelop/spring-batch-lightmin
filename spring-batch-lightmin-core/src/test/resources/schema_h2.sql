CREATE TABLE BATCH_JOB_CONFIGURATION (
  job_configuration_id NUMERIC IDENTITY PRIMARY KEY NOT NULL,
  job_name             VARCHAR(255),
  job_incrementer       VARCHAR(255)
);

CREATE TABLE BATCH_JOB_SCHEDULER_CONFIGURATION (
  id                   NUMERIC IDENTITY PRIMARY KEY NOT NULL,
  job_configuration_id NUMERIC                      NOT NULL,
  scheduler_type       INT                          NOT NULL,
  cron_expression      VARCHAR(255),
  initial_delay        NUMERIC,
  fixed_delay          NUMERIC,
  task_executor_type   INT                          NOT NULL,
  bean_name            VARCHAR(255)                 NOT NULL,
  status               VARCHAR(255)                 NOT NULL
);

CREATE TABLE BATCH_JOB_CONFIGURATION_PARAMETERS (
  id                   NUMERIC IDENTITY PRIMARY KEY NOT NULL,
  job_configuration_id NUMERIC                      NOT NULL,
  parameter_name       VARCHAR(255)                 NOT NULL,
  parameter_value      VARCHAR(255)                 NOT NULL,
  parameter_type       INT                          NOT NULL
);

