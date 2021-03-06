/**
 * Created by Andrew Bell 12/6/2015
 * www.recursivechaos.com
 * andrew@recursivechaos.com
 * Licensed under MIT License 2015. See license.txt for details.
 */

package com.recursivechaos.johnny5.config;

import com.offbytwo.jenkins.JenkinsServer;
import com.recursivechaos.johnny5.properties.JenkinsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class JenkinsConfig {

    @Autowired
    JenkinsProperties jenkinsProperties;

    @Bean
    public JenkinsServer jenkinsServer() throws URISyntaxException {
        return new JenkinsServer(new URI(jenkinsProperties.server), jenkinsProperties.username, jenkinsProperties.password);
    }

}
