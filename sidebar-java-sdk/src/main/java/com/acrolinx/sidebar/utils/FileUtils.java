
package com.acrolinx.sidebar.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.acrolinx.sidebar.pojo.settings.InputFormat;

@SuppressWarnings({"WeakerAccess", "unused"})
public class FileUtils
{
    /**
     * This is just a basic implementation to get the {@link InputFormat}s from file name with file extension.
     * For integration using an Acrolinx Server 5.2 or newer just set the InputFormat to AUTO.
     * @param fileName
     * @return inputFormat
     */
    public static InputFormat getInputFormat(String fileName)
    {
        final String fileExtensionPattern = "(\\.(?i)(txt|xml|dita|html|md))";
        Pattern fileExtensionExtractor = Pattern.compile(fileExtensionPattern);
        Matcher matcher = fileExtensionExtractor.matcher(fileName);
        InputFormat inputFormat;
        if (matcher.find()) {
            switch (matcher.group().toLowerCase()) {
                case ".txt":
                    inputFormat = InputFormat.TEXT;
                    break;
                case ".xml":
                    inputFormat = InputFormat.XML;
                    break;
                case ".dita":
                    inputFormat = InputFormat.XML;
                    break;
                case ".html":
                    inputFormat = InputFormat.HTML;
                    break;
                case ".md":
                    inputFormat = InputFormat.MARKDOWN;
                    break;
                default:
                    inputFormat = InputFormat.TEXT;
                    break;
            }
        } else {
            inputFormat = null;
        }
        return inputFormat;
    }

    public static boolean hasFileEnding(String fileName, String requiredFileEnding)
    {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            String fileEnding = fileName.substring(fileName.lastIndexOf(".") + 1);
            return fileEnding.equalsIgnoreCase(requiredFileEnding);
        } else
            return false;
    }
}
