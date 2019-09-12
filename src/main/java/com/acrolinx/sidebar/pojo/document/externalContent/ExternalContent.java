/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.pojo.document.externalContent;

import java.util.List;
import java.util.stream.Collectors;

public class ExternalContent
{

    private List<TextReplacement> textReplacements;
    private List<Entity> entities;
    private List<DitaReference> ditaReferences;

    public ExternalContent(List<TextReplacement> textReplacements, List<Entity> entities,
            List<DitaReference> ditaReferences)
    {
        this.textReplacements = textReplacements;
        this.entities = entities;
        this.ditaReferences = ditaReferences;
    }

    public List<TextReplacement> getTextReplacements()
    {
        return textReplacements;
    }

    public List<Entity> getEntities()
    {
        return entities;
    }

    public List<DitaReference> getDitaReferences()
    {
        return ditaReferences;
    }

    @Override
    public String toString()
    {

        return "{\"textReplacements\" : ["
                + textReplacements.stream().map(a -> String.valueOf(a.toString())).collect(Collectors.joining(","))
                + "] " + ", \"entities\" : ["
                + entities.stream().map(a -> String.valueOf(a.toString())).collect(Collectors.joining(","))
                + "], \"ditaReferences\" : ["
                + ditaReferences.stream().map(a -> String.valueOf(a.toString())).collect(Collectors.joining(","))
                + "]}";
    }
}
