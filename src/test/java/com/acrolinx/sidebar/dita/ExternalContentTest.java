/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.dita;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.acrolinx.sidebar.pojo.document.CheckContent;
import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContent;
import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContentBuilder;
import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContentField;

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

        final ExternalContent externalContent = externalContentBuilder.build();

        final List<ExternalContentField> ditaReferences = externalContent.getDitaReferences();
        final List<ExternalContentField> entities = externalContent.getEntities();
        final List<ExternalContentField> textReplacements = externalContent.getTextReplacements();

        assertEquals(3, ditaReferences.size());
        assertEquals(3, entities.size());
        assertEquals(3, textReplacements.size());

        assertNotNull(externalContent.toString());
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

        final ExternalContent externalContent = externalContentBuilder.build();

        final CheckContent checkContent = new CheckContent("<xml>This \"is a sentence.</xml>\n<t>tests</t>",
                externalContent);

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
