package com.murphd40.configuremebot.test.integration.graphql;

import java.io.IOException;
import java.util.Arrays;

import com.murphd40.configuremebot.Application;
import com.murphd40.configuremebot.client.graphql.Attachment;
import com.murphd40.configuremebot.client.graphql.Card;
import com.murphd40.configuremebot.client.graphql.InformationCard;
import com.murphd40.configuremebot.client.graphql.InformationCard.Button.ButtonStyle;
import com.murphd40.configuremebot.client.graphql.TargetedMessage;
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

        InformationCard.Button button1 = new InformationCard.Button(ButtonStyle.PRIMARY, "button1 payload", "button1 text");
        InformationCard.Button button2 = new InformationCard.Button(ButtonStyle.SECONDARY, "button2 payload", "button2 text");

        InformationCard infoCard1 = InformationCard.builder()
            .buttons(Arrays.asList(button1, button2))
            .date(System.currentTimeMillis())
            .title("card1 title")
            .text("card1 text")
            .subtitle("card1 subtitle")
            .build();

        InformationCard infoCard2 = InformationCard.builder()
//            .buttons(Arrays.asList(button1, button2))
            .date(System.currentTimeMillis())
            .title("card2 title")
            .text("card2 text")
            .subtitle("card2 subtitle")
            .build();

        Card cardInput1 = new Card(Card.CardType.INFORMATION, infoCard1);
        Card cardInput2 = new Card(Card.CardType.INFORMATION, infoCard2);

        Attachment attachment1 = new Attachment(Attachment.AttachmentType.CARD, cardInput1);
        Attachment attachment2 = new Attachment(Attachment.AttachmentType.CARD, cardInput2);

        TargetedMessage targetedMessage = TargetedMessage.builder()
            .conversationId("conversation-id")
            .targetDialogId("target-dialog-id")
            .targetUserId("target-user-id")
            .attachments(Arrays.asList(attachment1, attachment2))
            .build();

        String s = FreeMarkerTemplateUtils.processTemplateIntoString(template, targetedMessage);

        System.out.println(s);
    }

}