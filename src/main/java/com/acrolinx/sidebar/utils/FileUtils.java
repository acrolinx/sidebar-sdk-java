/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import com.acrolinx.sidebar.pojo.settings.InputFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FileUtils
{
    private FileUtils()
    {
        throw new IllegalStateException();
    }

    /**
     * This is just a basic implementation to get the {@link InputFormat}s from file name with file
     * extension. For integration using an Acrolinx Server 5.2 or newer just set the InputFormat to
     * AUTO.
     */
    public static InputFormat getInputFormat(String fileName)
    {
        Pattern fileExtensionPatternPattern = Pattern.compile("(\\.(?i)(txt|xml|dita|html|md))");
        Matcher matcher = fileExtensionPatternPattern.matcher(fileName);

        if (matcher.find()) {
            switch (matcher.group().toLowerCase()) {
                case ".txt":
                    return InputFormat.TEXT;
                case ".xml":
                    return InputFormat.XML;
                case ".dita":
                    return InputFormat.XML;
                case ".html":
                    return InputFormat.HTML;
                case ".md":
                    return InputFormat.MARKDOWN;
                default:
                    return InputFormat.TEXT;
            }
        }

        return null;
    }

    public static boolean hasFileEnding(String fileName, String requiredFileEnding)
    {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            String fileEnding = fileName.substring(fileName.lastIndexOf(".") + 1);
            return fileEnding.equalsIgnoreCase(requiredFileEnding);
        }

        return false;
    }
}
