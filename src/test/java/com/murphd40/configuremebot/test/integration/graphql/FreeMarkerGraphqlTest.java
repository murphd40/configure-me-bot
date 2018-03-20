package com.murphd40.configuremebot.test.integration.graphql;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.murphd40.configuremebot.Application;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class FreeMarkerGraphqlTest {

    @Autowired
    private Configuration freeMarkerConfig;

    @Test
    public void foo() throws IOException, TemplateException {
        Template template = freeMarkerConfig.getTemplate("createTargetedMessage.graphql");

        Map<String, Object> model = new HashMap<>();
        model.put("conversationId", "CONVERSATION_ID");
        model.put("targetUserId", "TARGET_USER_ID");
        model.put("targetDialogId", "TARGET_DIALOG_ID");

        String s = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

        System.out.println(s);
    }

}
