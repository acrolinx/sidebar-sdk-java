/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.pojo.document.externalContent;

import java.util.ArrayList;
import java.util.List;

/**
 * This serves as a builder to create an object of External content
 */
public class ExternalContentBuilder
{

    private List<TextReplacement> textReplacements = new ArrayList<>();
    private List<Entity> entities = new ArrayList<>();
    private List<DitaReference> ditaReferences = new ArrayList<>();

    public ExternalContentBuilder()
    {
    }

    /**
     * External content which doesn't require parsing.
     *
     * @param id Key of referenced entity
     * @param content Resolved content of entity
     * @return ExternalContentBuilder
     */
    public ExternalContentBuilder addTextReplacement(String id, String content)
    {
        this.textReplacements.add(new TextReplacement(id, content));
        return this;
    }

    /**
     * External content which requires parsing.
     *
     * @param id Key of referenced entity
     * @param content Resolved content of entity
     * @return ExternalContentBuilder
     */
    public ExternalContentBuilder addEntity(String id, String content)
    {
        this.entities.add(new Entity(id, content));
        return this;
    }

    /**
     * Dita references like conref, keyref, conkeyref which represent a placeholder for external
     * content. Content will be parsed.
     *
     * @param id Key of referenced entity
     * @param content Resolved content of entity
     * @return ExternalContentBuilder
     */
    public ExternalContentBuilder addDitaReference(String id, String content)
    {
        this.ditaReferences.add(new DitaReference(id, content));
        return this;
    }

    /**
     * Get the external content object.
     *
     * @return ExternalContent
     */
    public ExternalContent build()
    {
        return new ExternalContent(textReplacements, entities, ditaReferences);

    }

}
