package org.txudevelop.spring.batch.lightmin.server.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.BDDAssertions;
import org.junit.Before;
import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.client.api.LightminClientApplication;
import org.tuxdevelop.spring.batch.lightmin.server.repository.LightminApplicationRepository;

import java.util.Collection;

@Slf4j
public abstract class LightminApplicationRepositoryTest {

    @Test
    public void testSave() {
        final LightminClientApplication app1 = ServerDomainHelper.createLightminClientApplication("app1");
        final LightminClientApplication app2 = ServerDomainHelper.createLightminClientApplication("app2");
        final LightminClientApplication savedApp1 = this.getLightminApplicationRepository().save(app1);
        final LightminClientApplication savedApp2 = this.getLightminApplicationRepository().save(app2);

        final Collection<LightminClientApplication> allApps = this.getLightminApplicationRepository().findAll();
        BDDAssertions.then(allApps).hasSize(2);
        BDDAssertions.then(allApps).contains(savedApp1);
        BDDAssertions.then(allApps).contains(savedApp2);
    }

    @Test
    public void testFind() {
        final LightminClientApplication app1 = ServerDomainHelper.createLightminClientApplication("app1");
        final LightminClientApplication savedApp1 = this.getLightminApplicationRepository().save(app1);
        final LightminClientApplication foundApp = this.getLightminApplicationRepository().find(savedApp1.getId());
        BDDAssertions.then(foundApp).isEqualTo(savedApp1);
    }

    @Test
    public void testFindByApplicationName() {
        final LightminClientApplication app1Instance1 = ServerDomainHelper.createLightminClientApplication("app1");
        final LightminClientApplication app2 = ServerDomainHelper.createLightminClientApplication("app2");
        final LightminClientApplication app1Instance2 = ServerDomainHelper.createLightminClientApplication("app1");
        final LightminClientApplication savedApp1Instance1 = this.getLightminApplicationRepository().save(app1Instance1);
        final LightminClientApplication savedApp2 = this.getLightminApplicationRepository().save(app2);
        final LightminClientApplication savedApp1Instance2 = this.getLightminApplicationRepository().save(app1Instance2);

        final Collection<LightminClientApplication> app1Instances =
                this.getLightminApplicationRepository().findByApplicationName("app1");
        BDDAssertions.then(app1Instances).hasSize(2);
        BDDAssertions.then(app1Instances).contains(savedApp1Instance1);
        BDDAssertions.then(app1Instances).contains(savedApp1Instance2);
    }

    @Test
    public void testDelete() {
        final LightminClientApplication app1 = ServerDomainHelper.createLightminClientApplication("app1");
        final LightminClientApplication savedApp1 = this.getLightminApplicationRepository().save(app1);
        final LightminClientApplication foundApp = this.getLightminApplicationRepository().find(savedApp1.getId());
        BDDAssertions.then(foundApp).isEqualTo(savedApp1);
        this.getLightminApplicationRepository().delete(foundApp.getId());
        final LightminClientApplication result = this.getLightminApplicationRepository().find(savedApp1.getId());
        BDDAssertions.then(result).isNull();
    }

    protected abstract LightminApplicationRepository getLightminApplicationRepository();

    @Before
    public void init() {
        this.getLightminApplicationRepository().clear();
    }
}
