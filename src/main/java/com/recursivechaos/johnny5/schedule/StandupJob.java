/**
 * Created by Andrew Bell 11/27/2015
 * www.recursivechaos.com
 * andrew@recursivechaos.com
 * Licensed under MIT License 2015. See license.txt for details.
 */

package com.recursivechaos.johnny5.schedule;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StandupJob extends QuartzJobBean {

    @Autowired
    SlackSession slackSession;

    @Autowired
    SlackChannel slackChannel;

    private String message;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        slackSession.sendMessage(slackChannel, message, null);
    }

    public void setMessage(String messsage) {
        this.message = messsage;
    }

}