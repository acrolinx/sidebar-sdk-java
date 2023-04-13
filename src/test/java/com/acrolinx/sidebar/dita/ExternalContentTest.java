/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.dita;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.acrolinx.sidebar.pojo.document.CheckContent;
import com.acrolinx.sidebar.pojo.document.externalContent.ExternalContent;
import com.acrolinx.sidebar.pojo.document.externalContent.ExternalContentBuilder;
import com.acrolinx.sidebar.pojo.document.externalContent.ExternalContentField;

public class ExternalContentTest
{
    @Test
    public void testBuildingExternalContent()
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
    public void testIntegrationOfContentAndExternalContent()
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

        assertTrue(checkContent.toString().contains("content"));
        assertTrue(checkContent.toString().contains("externalContent"));
    }

    @Test
    public void testExternalContentNotPresent()
    {
        final CheckContent checkContent = new CheckContent("This is a some sample", null);
        assertTrue(checkContent.toString().contains("content"));
    }

    @Test
    public void testExternalContentReferencesEmpty()
    {
        final ExternalContent externalContent = new ExternalContentBuilder().build();
        final CheckContent checkContent = new CheckContent("This is a some sample", externalContent);

        String string = checkContent.toString();
        assertTrue(string.contains("content"));
        assertTrue(string.contains("externalContent"));
        assertTrue(string.contains("textReplacements"));
        assertTrue(string.contains("entities"));
    }
}
