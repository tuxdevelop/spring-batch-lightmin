package org.txudevelop.spring.batch.lightmin.controller;

import static org.assertj.core.api.Fail.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.txudevelop.spring.batch.lightmin.ITConfigurationApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest({ "server.port=0", "management.port=0" })
@SpringApplicationConfiguration(classes = ITConfigurationApplication.class)
public class AdminControllerIT {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Test
	public void redirectToIndexIT() {
		try {
			this.mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"));
		} catch (final Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void initIndexIT() {
		try {
			this.mockMvc.perform(get("/index")).andExpect(status().isOk()).andExpect(view().name("index"));
		} catch (final Exception e) {
			fail(e.getMessage());
		}
	}

	@Before
	public void init() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

}
