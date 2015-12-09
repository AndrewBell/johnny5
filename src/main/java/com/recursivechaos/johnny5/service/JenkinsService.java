/**
 * Created by Andrew Bell 12/6/2015
 * www.recursivechaos.com
 * andrew@recursivechaos.com
 * Licensed under MIT License 2015. See license.txt for details.
 */

package com.recursivechaos.johnny5.service;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.Job;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class JenkinsService {

    private static final Logger logger = LoggerFactory.getLogger(JenkinsService.class);

    @Autowired
    JenkinsServer jenkinsServer;

    @Autowired
    SlackSession slackSession;

    @Autowired
    SlackChannel slackChannel;

    public void sendMessage(String myMessage, SlackChannel slackChannel) {
        slackSession.sendMessage(slackChannel, myMessage, null);
    }

    public void sendJobStatus(SlackChannel slackChannel) {
        try {
            Map<String, String> jobStatuses = getJobStatuses();
            for (Map.Entry<String, String> job : jobStatuses.entrySet()) {
                if (!job.getValue().equals("SUCCESS")) {
                    sendMessage(getEmoticon(job.getValue()) + " " + job.getKey() + ": " + job.getValue(), slackChannel);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            sendMessage("Malfunction! Could not fetch job statuses from Jenkins.", slackChannel);
        }
    }

    private String getEmoticon(String buildResult) {
        String emoticon = ":question:";
        BuildResult result = BuildResult.valueOf(buildResult);
        switch (result) {
            case FAILURE:
                emoticon = ":rage:";
                break;
            case ABORTED:
                emoticon = ":skull:";
                break;
            case UNSTABLE:
                emoticon = ":scream:";
                break;
            default:
                break;
        }
        return emoticon;
    }

    private Map<String, String> getJobStatuses() throws IOException {
        Map<String, String> statuses = new HashMap<>();
        Map<String, Job> jobs = jenkinsServer.getJobs();

        for (Job job : jobs.values()) {
            logger.debug("Checking job name : {}", job.getName());
            // TODO: We're still failing on parsing some jobs, refine this
            try {
                if (null != job.details().getLastBuild()) {
                    statuses.put(job.details().getDisplayName(), job.details().getLastBuild().details().getResult().name());
                } else {
                    logger.info("Job {} has not been built yet", job.getName());
                }
            } catch (Exception e) {
                logger.error("Failed to parse job data.", e);
            }
        }

        return statuses;
    }

}
