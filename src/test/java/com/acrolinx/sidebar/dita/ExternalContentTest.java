/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.dita;

import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.acrolinx.sidebar.pojo.document.CheckContent;
import com.acrolinx.sidebar.pojo.document.externalContent.*;

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

        final List<DitaReference> ditaReferences = content.getDitaReferences();
        final List<Entity> entities = content.getEntities();
        final List<TextReplacement> textReplacements = content.getTextReplacements();

        assertEquals(3, ditaReferences.size());
        assertEquals(3, entities.size());
        assertEquals(3, textReplacements.size());

        assertNotNull(ditaReferences);
        assertNotNull(entities);
        assertNotNull(textReplacements);

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

        final CheckContent checkContent = new CheckContent("<xml>This \"is a sentence.</xml>", content);

        assertNotNull(checkContent);

        assertNotNull(checkContent.toString());

        assertTrue(checkContent.toString().contains("content"));
        assertTrue(checkContent.toString().contains("externalContent"));
    }

    @Test
    public void testExternalContentNotPresent()
    {
        final CheckContent checkContent = new CheckContent("This is a some sample", null);

        assertNotNull(checkContent);

        assertTrue(checkContent.toString().contains("content"));

    }

    @Test
    public void testExternalContentReferencesEmpty()
    {
        final ExternalContent externalContent = new ExternalContentBuilder().build();
        final CheckContent checkContent = new CheckContent("This is a some sample", externalContent);

        assertNotNull(checkContent);

        assertTrue(checkContent.toString().contains("content"));
        assertTrue(checkContent.toString().contains("externalContent"));
        assertTrue(checkContent.toString().contains("textReplacements"));
        assertTrue(checkContent.toString().contains("entities"));
    }
}
