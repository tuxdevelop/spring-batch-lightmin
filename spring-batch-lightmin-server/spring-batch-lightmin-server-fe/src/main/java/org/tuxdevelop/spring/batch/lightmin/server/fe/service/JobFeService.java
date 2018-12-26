package org.tuxdevelop.spring.batch.lightmin.server.fe.service;

import org.tuxdevelop.spring.batch.lightmin.api.resource.batch.JobInfo;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.job.batch.BatchJobInfoModel;
import org.tuxdevelop.spring.batch.lightmin.server.service.JobServerService;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;

import java.util.ArrayList;
import java.util.List;

public class JobFeService extends CommonFeService {

    private final JobServerService jobServerService;
    private final RegistrationBean registrationBean;

    public JobFeService(final JobServerService jobServerService, final RegistrationBean registrationBean) {
        super(registrationBean);
        this.jobServerService = jobServerService;
        this.registrationBean = registrationBean;
    }

    public List<BatchJobInfoModel> findById(final String applicationId) {
        final LightminClientApplication lightminClientApplication = this.registrationBean.findById(applicationId);
        final List<String> registeredJobs = lightminClientApplication.getLightminClientInformation().getRegisteredJobs();

        final List<BatchJobInfoModel> batchJobInfoModels = new ArrayList<>();

        registeredJobs
                .forEach(
                        jobName -> {
                            final JobInfo jobInfo = this.jobServerService.getJobInfo(jobName, lightminClientApplication);
                            final BatchJobInfoModel batchJobInfoModel = new BatchJobInfoModel();
                            batchJobInfoModel.setInstanceCount(jobInfo.getJobInstanceCount());
                            batchJobInfoModel.setJobName(jobInfo.getJobName());
                            batchJobInfoModels.add(batchJobInfoModel);
                        }
                );

        return batchJobInfoModels;
    }

}
