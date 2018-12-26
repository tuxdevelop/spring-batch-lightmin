CREATE TABLE BATCH_JOB_CONFIGURATION (
  job_configuration_id   SERIAL                NOT NULL,
  application_name       VARCHAR(255)          NOT NULL,
  job_name               VARCHAR(255),
  job_incrementer        VARCHAR(255),
  job_configuration_type INT                   NOT NULL,
  PRIMARY KEY (job_configuration_id)
);

CREATE TABLE BATCH_JOB_CONFIGURATION_VALUE (
  id                   SERIAL                NOT NULL,
  job_configuration_id BIGINT                NOT NULL,
  value_key            VARCHAR(255)          NOT NULL,
  configuration_value  VARCHAR(255),
  PRIMARY KEY (id),
  FOREIGN KEY (job_configuration_id) REFERENCES BATCH_JOB_CONFIGURATION (job_configuration_id)
);

CREATE TABLE BATCH_JOB_CONFIGURATION_PARAMETERS (
  id                   SERIAL NOT NULL,
  job_configuration_id BIGINT                NOT NULL,
  parameter_name       VARCHAR(255)          NOT NULL,
  parameter_value      VARCHAR(255)          NOT NULL,
  parameter_type       INT                   NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (job_configuration_id) REFERENCES BATCH_JOB_CONFIGURATION (job_configuration_id)
);



