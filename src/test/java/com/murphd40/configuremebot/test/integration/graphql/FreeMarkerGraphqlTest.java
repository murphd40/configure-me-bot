package com.murphd40.configuremebot.test.integration.graphql;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.murphd40.configuremebot.Application;
import com.murphd40.configuremebot.client.graphql.GraphQLQuery;
import com.murphd40.configuremebot.client.graphql.GraphQLQueryBuilder;
import com.murphd40.configuremebot.client.graphql.request.Attachment;
import com.murphd40.configuremebot.client.graphql.request.Card;
import com.murphd40.configuremebot.client.graphql.request.InformationCard;
import com.murphd40.configuremebot.client.graphql.request.InformationCard.Button.ButtonStyle;
import com.murphd40.configuremebot.client.graphql.request.TargetedMessage;
import com.murphd40.configuremebot.client.graphql.response.Person;
import com.murphd40.configuremebot.service.WatsonWorkService;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class FreeMarkerGraphqlTest {

    @Autowired
    private GraphQLQueryBuilder queryBuilder;

    @Autowired
    private WatsonWorkService watsonWorkService;

    @Test
    public void targetedMessage() throws IOException, TemplateException {
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
            .buttons(Arrays.asList(button1, button2))
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

        GraphQLQuery query = queryBuilder.buildTargetedMessageQuery(targetedMessage);

        System.out.println(query);
    }

    @Test
    public void getPeople() throws IOException {
        List<String> personIds = Arrays.asList("694510c0-0037-1035-8902-badfe61c59a0", "cb549f40-eeeb-1034-9cd4-db109f3014ac");
        List<Person> people = watsonWorkService.getPeople(personIds);

        System.out.println(people);
    }
}