package com.murphd40.configuremebot.client.graphql;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

@Component
public class GraphQLQueryBuilder {

    @Autowired
    private Configuration freeMarkerConfig;

    @SneakyThrows
    public GraphQLQuery buildTargetedMessageQuery(TargetedMessage targetedMessage) {
        Template template = freeMarkerConfig.getTemplate("createTargetedMessage.graphql");
        return new GraphQLQuery(FreeMarkerTemplateUtils.processTemplateIntoString(template, targetedMessage));
    }

}
