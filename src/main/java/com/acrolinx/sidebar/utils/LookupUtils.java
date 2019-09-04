/* Copyright (c) 2016-present Acrolinx GmbH */

package com.acrolinx.sidebar.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acrolinx.sidebar.pojo.document.AbstractMatch;

@SuppressWarnings("unused")
public class LookupUtils
{

    private static final Logger logger = LoggerFactory.getLogger(SidebarUtils.class);

    public static List<? extends AbstractMatch> filterDangerousMatches(List<? extends AbstractMatch> list,
            String lastCheckedContent)
    {
        return list.stream().filter(m -> {
            String matchContent = lastCheckedContent.substring(m.getRange().getMinimumInteger(),
                    m.getRange().getMaximumInteger());
            logger.debug("Checking if match is a tag");
            boolean isTag = matchContent.matches("</?\\w+.*?>");
            logger.debug("Is match a tag: " + isTag);
            return !isTag;

        }).collect(Collectors.toList());
    }
}