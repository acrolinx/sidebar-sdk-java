/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.pojo.document.externalContent;

import java.util.List;

import com.acrolinx.sidebar.utils.GsonUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
        return GsonUtils.getJsonFormObject(this);
    }
}
