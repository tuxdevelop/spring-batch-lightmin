CREATE TABLE BATCH_JOB_CONFIGURATION (
  job_configuration_id BIGINT       AUTO_INCREMENT NOT NULL,
  job_name             VARCHAR(255),
  job_incrementer      VARCHAR(255),
  PRIMARY KEY (job_configuration_id)
);

CREATE TABLE BATCH_JOB_SCHEDULER_CONFIGURATION (
  id                   BIGINT       AUTO_INCREMENT NOT NULL,
  job_configuration_id BIGINT                      NOT NULL,
  scheduler_type       INT                         NOT NULL,
  cron_expression      VARCHAR(255),
  initial_delay        BIGINT,
  fixed_delay          BIGINT,
  task_executor_type   INT                         NOT NULL,
  bean_name            VARCHAR(255)                NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (job_configuration_id) REFERENCES BATCH_JOB_CONFIGURATION(job_configuration_id)
);

CREATE TABLE BATCH_JOB_CONFIGURATION_PARAMETERS (
  id                   BIGINT        AUTO_INCREMENT NOT NULL,
  job_configuration_id BIGINT                       NOT NULL,
  parameter_name       VARCHAR(255)                 NOT NULL,
  parameter_value      VARCHAR(255)                 NOT NULL,
  parameter_type       INT                          NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (job_configuration_id) REFERENCES BATCH_JOB_CONFIGURATION(job_configuration_id)
);



