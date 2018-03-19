package com.murphd40.configuremebot.controller.request.webhook

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test

class WebhookEventTests {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()

    private static final String ACTION_SELECTED_EVENT = '''
{
    "spaceName": "App Test",
    "spaceId": "5aa26de8e4b092ef0b999d63",
    "annotationPayload": "{\\"type\\":\\"actionSelected\\",\\"annotationId\\":\\"5aadbf84e4b0418f70c3bbf1\\",\\"type\\":\\"actionSelected\\",\\"version\\":\\"1.0\\",\\"created\\":1521336196807,\\"createdBy\\":\\"cb549f40-eeeb-1034-9cd4-db109f3014ac\\",\\"updated\\":1521336196807,\\"updatedBy\\":\\"cb549f40-eeeb-1034-9cd4-db109f3014ac\\",\\"tokenClientId\\":\\"toscana-web-client-id\\",\\"conversationId\\":\\"5aa26de8e4b092ef0b999d63\\",\\"targetDialogId\\":\\"d19157f0-2a4a-11e8-b1f9-f96c4c41d433\\",\\"referralMessageId\\":\\"d19157f0-2a4a-11e8-b1f9-f96c4c41d433\\",\\"actionId\\":\\"/triggers\\",\\"targetAppId\\":\\"76815b03-6cf9-4a18-b5b6-63e13035c80e\\"}",
    "messageId": "5aadbf84e4b0418f70c3bbf0",
    "annotationType": "actionSelected",
    "annotationId": "5aadbf84e4b0418f70c3bbf1",
    "time": 1521336196807,
    "type": "message-annotation-added",
    "userName": "DAVID MURPHY",
    "userId": "cb549f40-eeeb-1034-9cd4-db109f3014ac"
}
'''

    private static final String UNSUPPORTED_EVENT = '''
{
    "spaceName": "App Test",
    "spaceId": "5aa26de8e4b092ef0b999d63",
    "annotationPayload": "{\\"hidden\\":false,\\"spaceId\\":\\"5aa26de8e4b092ef0b999d63\\",\\"momentSummary\\":{\\"phrases\\":[{\\"label\\":\\"World\\",\\"source\\":\\"keyword\\",\\"score\\":0.916502,\\"count\\":null,\\"category\\":null}],\\"mostRelevantMessage\\":{\\"messageId\\":\\"5aaf139de4b0b698c7b4dd6f\\",\\"published\\":1521423261236}},\\"momentId\\":\\"2c8a31d4e33c6008788ee47d70d30379a6f32d77\\",\\"algorithm\\":\\"timegap\\",\\"participants\\":[{\\"authorId\\":\\"cb549f40-eeeb-1034-9cd4-db109f3014ac\\",\\"messageCount\\":1}],\\"startMessage\\":{\\"messageId\\":\\"5aaf139de4b0b698c7b4dd6f\\",\\"published\\":1521423261236},\\"lastUpdatedMessage\\":{\\"messageId\\":\\"5aaf139de4b0b698c7b4dd6f\\",\\"published\\":1521423261236},\\"momentVersion\\":6}",
    "messageId": "5aaf139de4b0b698c7b4dd6f",
    "annotationType": "conversation-moment",
    "annotationId": "5aaf139de4b08779b7e15c9c",
    "time": 1521423261633,
    "type": "message-annotation-added",
    "userId": "toscana-service-moment-identification-client-id"
}
'''

    @Test
    void parseActionSelected() {
        AnnotationAddedEvent event = OBJECT_MAPPER.readValue(ACTION_SELECTED_EVENT, AnnotationAddedEvent.class)

        println event
    }

    @Test
    void parseUnsupported() {
        AnnotationAddedEvent event = OBJECT_MAPPER.readValue(UNSUPPORTED_EVENT, AnnotationAddedEvent.class)

        println event
    }

}
