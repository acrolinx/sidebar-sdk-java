/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.dita;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.acrolinx.sidebar.pojo.document.CheckContent;
import com.acrolinx.sidebar.pojo.document.externalContent.ExternalContent;
import com.acrolinx.sidebar.pojo.document.externalContent.ExternalContentBuilder;
import com.acrolinx.sidebar.pojo.document.externalContent.ExternalContentField;

class ExternalContentTest
{
    @Test
    void testBuildingExternalContent()
    {
        final ExternalContentBuilder externalContentBuilder = new ExternalContentBuilder();

        externalContentBuilder.addDitaReference(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        externalContentBuilder.addDitaReference(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        externalContentBuilder.addDitaReference(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        externalContentBuilder.addEntity(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        externalContentBuilder.addEntity(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        externalContentBuilder.addEntity(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        externalContentBuilder.addTextReplacement(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        externalContentBuilder.addTextReplacement(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        externalContentBuilder.addTextReplacement(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        final ExternalContent content = externalContentBuilder.build();

        final List<ExternalContentField> ditaReferences = content.getDitaReferences();
        final List<ExternalContentField> entities = content.getEntities();
        final List<ExternalContentField> textReplacements = content.getTextReplacements();

        assertEquals(3, ditaReferences.size());
        assertEquals(3, entities.size());
        assertEquals(3, textReplacements.size());

        assertNotNull(content.toString());
    }

    @Test
    void testIntegrationOfContentAndExternalContent()
    {
        final ExternalContentBuilder externalContentBuilder = new ExternalContentBuilder();

        externalContentBuilder.addDitaReference(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        externalContentBuilder.addDitaReference(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        externalContentBuilder.addDitaReference(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        externalContentBuilder.addEntity(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        externalContentBuilder.addEntity(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        externalContentBuilder.addEntity(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        externalContentBuilder.addTextReplacement(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        externalContentBuilder.addTextReplacement(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        externalContentBuilder.addTextReplacement(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        final ExternalContent content = externalContentBuilder.build();

        final CheckContent checkContent = new CheckContent("<xml>This \"is a sentence.</xml>\n<t>tests</t>", content);

        String jsonString = checkContent.toString();
        assertTrue(jsonString.contains("content"));
        assertTrue(jsonString.contains("externalContent"));
    }

    @Test
    void testExternalContentNotPresent()
    {
        final CheckContent checkContent = new CheckContent("This is a some sample", null);
        assertTrue(checkContent.toString().contains("content"));
    }

    @Test
    void testExternalContentReferencesEmpty()
    {
        final ExternalContent externalContent = new ExternalContentBuilder().build();
        final CheckContent checkContent = new CheckContent("This is a some sample", externalContent);

        String jsonString = checkContent.toString();
        assertTrue(jsonString.contains("content"));
        assertTrue(jsonString.contains("externalContent"));
        assertTrue(jsonString.contains("textReplacements"));
        assertTrue(jsonString.contains("entities"));
    }
}
