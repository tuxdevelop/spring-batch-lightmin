CREATE TABLE BATCH_JOB_CONFIGURATION (
  job_configuration_id   NUMERIC IDENTITY PRIMARY KEY NOT NULL,
  application_name       UNIVARCHAR(255)              NOT NULL,
  job_name               UNIVARCHAR(255),
  job_incrementer        UNIVARCHAR(255),
  job_configuration_type INT                          NOT NULL
)
GO

CREATE TABLE BATCH_JOB_CONFIGURATION_VALUE (
  id                   NUMERIC IDENTITY PRIMARY KEY NOT NULL,
  job_configuration_id NUMERIC                      NOT NULL,
  value_key            VARCHAR(255)                 NOT NULL,
  configuration_value  VARCHAR(255),
  FOREIGN KEY (job_configuration_id) REFERENCES BATCH_JOB_CONFIGURATION (job_configuration_id)
)
GO

CREATE TABLE BATCH_JOB_CONFIGURATION_PARAMETERS (
  id                   NUMERIC IDENTITY PRIMARY KEY NOT NULL,
  job_configuration_id NUMERIC                      NOT NULL,
  parameter_name       UNIVARCHAR(255)              NOT NULL,
  parameter_value      UNIVARCHAR(255)              NOT NULL,
  parameter_type       INT                          NOT NULL,
  FOREIGN KEY (job_configuration_id) REFERENCES BATCH_JOB_CONFIGURATION (job_configuration_id)
)
GO

