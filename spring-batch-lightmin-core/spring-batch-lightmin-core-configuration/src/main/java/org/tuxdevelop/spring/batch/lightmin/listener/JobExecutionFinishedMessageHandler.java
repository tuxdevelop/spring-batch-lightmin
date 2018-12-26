package org.tuxdevelop.spring.batch.lightmin.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

/**
 * @author Marcel Becker
 * @since 0.5
 */
@Slf4j
public class JobExecutionFinishedMessageHandler implements MessageHandler {

    @Override
    public void handleMessage(final Message<?> message) throws MessagingException {
        final JobExecution jobExecution = (JobExecution) message.getPayload();
        log.debug("Executed: {} ", jobExecution);
    }
}
