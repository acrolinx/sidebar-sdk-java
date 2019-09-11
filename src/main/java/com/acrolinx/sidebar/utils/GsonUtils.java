/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils
{

    public static String getJsonFormObject(Object object)
    {
        final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();

        final String jsonString = gson.toJson(object);

        return jsonString.replaceAll("\\\\n", "\n").replaceAll("\\\\t", "\t").replaceAll("\\\\b", "\b").replaceAll(
                "\\\\r", "\r").replaceAll("\\\\f", "\f").replaceAll("\\\\'", "\'");
    }
}
