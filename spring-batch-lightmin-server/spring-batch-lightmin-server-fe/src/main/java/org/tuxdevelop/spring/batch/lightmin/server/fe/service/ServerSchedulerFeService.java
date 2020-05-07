package org.tuxdevelop.spring.batch.lightmin.server.fe.service;

import lombok.extern.slf4j.Slf4j;
import org.tuxdevelop.spring.batch.lightmin.api.resource.admin.JobIncrementer;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.ContentPageModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.common.JobIncremeterTypeModel;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.server.scheduler.*;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.domain.ExecutionInfo;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.domain.ExecutionInfoPage;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerConfiguration;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.SchedulerExecution;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.repository.domain.ServerSchedulerStatus;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.service.ExecutionInfoService;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.service.SchedulerConfigurationService;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.service.SchedulerExecutionService;
import org.tuxdevelop.spring.batch.lightmin.server.scheduler.service.ServerSchedulerService;
import org.tuxdevelop.spring.batch.lightmin.server.support.RegistrationBean;
import org.tuxdevelop.spring.batch.lightmin.util.DomainParameterParser;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ServerSchedulerFeService extends CommonFeService {

    private final SchedulerExecutionService schedulerExecutionService;
    private final SchedulerConfigurationService schedulerConfigurationService;
    private final ExecutionInfoService executionInfoService;
    private final ServerSchedulerService serverSchedulerService;
    private final RegistrationBean registrationBean;


    public ServerSchedulerFeService(final SchedulerExecutionService schedulerExecutionService,
                                    final SchedulerConfigurationService schedulerConfigurationService,
                                    final ExecutionInfoService executionInfoService,
                                    final ServerSchedulerService serverSchedulerService, final RegistrationBean registrationBean) {
        super(registrationBean);
        this.schedulerExecutionService = schedulerExecutionService;
        this.schedulerConfigurationService = schedulerConfigurationService;
        this.executionInfoService = executionInfoService;
        this.serverSchedulerService = serverSchedulerService;
        this.registrationBean = registrationBean;
    }

    //Execution

    public ContentPageModel<ServerSchedulerInfoPageModel> getServerSchedulerInfos(final Integer state,
                                                                                  final Integer startIndex,
                                                                                  final Integer pageSize) {

        final ExecutionInfoPage executions = this.executionInfoService.findAll(state, startIndex, pageSize);
        final List<ServerSchedulerInfoModel> schedulerInfos = this.map(executions.getExecutionInfos());

        final ContentPageModel<ServerSchedulerInfoPageModel> pageModel
                = new ContentPageModel<>(executions.getStartIndex(), executions.getPageSize(), executions.getTotalCount());

        final ServerSchedulerInfoPageModel pageContent = new ServerSchedulerInfoPageModel();
        pageContent.setSchedulerInfos(schedulerInfos);
        pageContent.setFilterState(state);
        pageContent.setDisplayFilterState(this.mapStateToDisplayText(state));

        pageModel.setValue(pageContent);

        return pageModel;
    }

    public void deleteExecution(final Long executionId) {
        this.schedulerExecutionService.deleteExecution(executionId);
    }

    public void stopExecution(final Long executionId) {
        this.serverSchedulerService.stopExecution(executionId);
    }

    //Configuration

    public ServerSchedulerConfigurationsModel getServerSchedulerConfigurations() {
        final ServerSchedulerConfigurationsModel model = new ServerSchedulerConfigurationsModel();

        final Set<String> applications = this.registrationBean.getAllApplicationNames();

        final List<SchedulerConfiguration> schedulerConfigurations
                = this.schedulerConfigurationService.findAll();

        final Map<String, List<ServerSchedulerConfigurationModel>> map
                = this.mapToApplicationMap(schedulerConfigurations);

        model.setRegisteredApplications(applications);
        model.setSchedulers(map);

        return model;
    }

    public void saveSchedulerConfiguration(final ServerSchedulerConfigurationModel model) {
        final SchedulerConfiguration config = this.map(model);
        this.serverSchedulerService.saveSchedulerConfiguration(config);
    }

    public ServerSchedulerConfigurationModel findById(final Long id) {
        final SchedulerConfiguration configuration;
        configuration = this.serverSchedulerService.findSchedulerConfigurationById(id);
        return this.map(configuration);
    }

    public void disableConfiguration(final Long configurationId) {
        this.serverSchedulerService.disableServerSchedulerConfiguration(configurationId);
    }

    public void startConfiguration(final Long configurationId) {
        this.serverSchedulerService.startServerSchedulerConfiguration(configurationId);
    }

    public void deleteConfiguration(final Long configurationId) {
        this.serverSchedulerService.deleteServerSchedulerConfiguration(configurationId);
    }


    /*
     * private helpers and mappers
     */

    //Scheduler Executions

    private List<ServerSchedulerInfoModel> map(final List<ExecutionInfo> executionInfos) {
        return executionInfos.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    private ServerSchedulerInfoModel map(final ExecutionInfo executionInfo) {
        final ServerSchedulerInfoModel model = new ServerSchedulerInfoModel();
        model.setExecution(this.map(executionInfo.getExecution()));
        model.setConfig(this.map(executionInfo.getConfiguration()));
        return model;
    }

    private ServerSchedulerExecutionModel map(final SchedulerExecution execution) {
        final ServerSchedulerExecutionModel model = new ServerSchedulerExecutionModel();
        model.setId(execution.getId());
        model.setExecutionCount(execution.getExecutionCount());
        model.setNextFireTime(execution.getNextFireTime());
        model.setSchedulerConfigurationId(execution.getSchedulerConfigurationId());
        model.setStatus(execution.getState());
        model.setLastUpdate(execution.getLastUpdate());
        model.setStatusRead(new ServerSchedulerExecutionStatusModel(ServerSchedulerExecutionStatusModel.map(execution.getState())));
        return model;
    }

    private String mapStateToDisplayText(final Integer state) {
        final String text;
        if (state == null) {
            text = "all";
        } else {
            text = Arrays.stream(ServerSchedulerExecutionStatusModel.ServerSchedulerExecutionType.values())
                    .filter(status -> status.getValue().equals(state))
                    .findFirst()
                    .map(ServerSchedulerExecutionStatusModel.ServerSchedulerExecutionType::getDisplayText).orElse("unknown");
        }
        return text;
    }

    //Scheduler Configurations

    private List<ServerSchedulerConfigurationModel> mapConfigurations(final List<SchedulerConfiguration> configurations) {
        return configurations.stream()
                .map(
                        this::map
                ).collect(Collectors.toList());
    }

    private Map<String, List<ServerSchedulerConfigurationModel>> mapToApplicationMap(final List<SchedulerConfiguration> configurations) {

        final Map<String, List<ServerSchedulerConfigurationModel>> map = new HashMap<>();

        this.mapConfigurations(configurations)
                .forEach(
                        config -> {
                            final String application = config.getApplicationName();
                            if (!map.containsKey(application)) {
                                map.put(application, new ArrayList<>());
                            } else {
                                log.trace("application already in the map");
                            }
                            map.get(application).add(config);
                        }
                );
        return map;
    }

    private ServerSchedulerConfigurationModel map(final SchedulerConfiguration configuration) {
        final ServerSchedulerConfigurationModel model = new ServerSchedulerConfigurationModel();
        model.setId(configuration.getId());
        model.setApplicationName(configuration.getApplication());
        model.setCronExpression(configuration.getCronExpression());
        model.setIncrementer(configuration.getJobIncrementer().getIncrementerIdentifier());
        model.setIncrementerRead(new JobIncremeterTypeModel(JobIncremeterTypeModel.map(configuration.getJobIncrementer())));
        model.setInstanceExecutionCount(configuration.getInstanceExecutionCount());
        model.setJobName(configuration.getJobName());
        model.setParametersRead(configuration.getJobParameters());
        model.setParameters(this.mapParameters(configuration.getJobParameters()));
        model.setMaxRetries(configuration.getMaxRetries());
        model.setRetryable(configuration.getRetryable());
        model.setRetryInterval(configuration.getRetryInterval());
        model.setStatusRead(new ServerSchedulerConfigurationStatusModel(ServerSchedulerConfigurationStatusModel.map(configuration.getStatus().getValue())));
        model.setStatus(configuration.getStatus().getValue());
        return model;
    }

    private SchedulerConfiguration map(final ServerSchedulerConfigurationModel model) {
        final SchedulerConfiguration scheduler = new SchedulerConfiguration();
        scheduler.setId(model.getId());
        scheduler.setApplication(model.getApplicationName());
        scheduler.setCronExpression(model.getCronExpression());
        scheduler.setJobIncrementer(JobIncrementer.valueOf(model.getIncrementer()));
        scheduler.setInstanceExecutionCount(model.getInstanceExecutionCount());
        scheduler.setJobName(model.getJobName());
        scheduler.setJobParameters(DomainParameterParser.parseParameters(model.getParameters()));
        scheduler.setMaxRetries(model.getMaxRetries());
        scheduler.setRetryable(model.getRetryable());
        scheduler.setRetryInterval(model.getRetryInterval());
        scheduler.setStatus(ServerSchedulerStatus.getByValue(model.getStatus()));
        return scheduler;
    }

    private String mapParameters(final Map<String, Object> parametersMap) {
        return DomainParameterParser.parseParameterMapToString(parametersMap);
    }


}
