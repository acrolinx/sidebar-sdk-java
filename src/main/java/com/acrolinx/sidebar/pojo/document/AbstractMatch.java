/* Copyright (c) 2016-present Acrolinx GmbH */

package com.acrolinx.sidebar.pojo.document;

public abstract class AbstractMatch
{

    public abstract IntRange getRange();

    public abstract String getContent();

    public abstract AbstractMatch setRange(IntRange range);

    public abstract AbstractMatch copy();
}