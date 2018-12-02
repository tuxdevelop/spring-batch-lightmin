package org.tuxdevelop.spring.batch.lightmin.server.fe.model.application;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@EqualsAndHashCode
public class ApplicationClusterModel {

    @Getter
    @Setter
    private String name;

    @Getter
    private final Set<ApplicationInstanceModel> applicationInstances;

    public ApplicationClusterModel() {
        this.applicationInstances = new HashSet<>();
    }

    public void add(final ApplicationInstanceModel instance) {
        this.applicationInstances.add(instance);
    }

    public void addAll(final Set<ApplicationInstanceModel> instances) {
        if (instances != null) {
            for (final ApplicationInstanceModel instance : instances) {
                this.add(instance);
            }
        } else {
            log.debug("instances null, nothing to add");
        }
    }

    public Integer getSize() {
        return this.applicationInstances.size();
    }

    public Boolean hasMultiInstances(final String name) {
        return this.applicationInstances.size() > 1;
    }


}
