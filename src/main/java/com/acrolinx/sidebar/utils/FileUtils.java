/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import com.acrolinx.sidebar.pojo.settings.InputFormat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class FileUtils {
  private FileUtils() {
    throw new IllegalStateException();
  }

  /**
   * This is just a basic implementation to get the {@link InputFormat}s from file name with file
   * extension. For integration using an Acrolinx Server 5.2 or newer just set the InputFormat to
   * AUTO.
   */
  public static InputFormat getInputFormat(String fileName) {
    Pattern fileExtensionPatternPattern = Pattern.compile("(\\.(?i)(txt|xml|dita|html|md))");
    Matcher matcher = fileExtensionPatternPattern.matcher(fileName);

    if (matcher.find()) {
      switch (matcher.group().toLowerCase()) {
        case ".xml":
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

  public static boolean hasFileEnding(String fileName, String requiredFileEnding) {
    if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
      String fileEnding = fileName.substring(fileName.lastIndexOf(".") + 1);
      return fileEnding.equalsIgnoreCase(requiredFileEnding);
    }

    return false;
  }

  public static void extractZipFile(Path zipFilePath, Path destinationDirectoryPath)
      throws IOException {
    Validate.isRegularFile(zipFilePath, "zipFilePath");
    Validate.isDirectory(destinationDirectoryPath, "destinationDirectoryPath");

    try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath.toFile()))) {
      ZipEntry zipEntry = zipIn.getNextEntry();

      while (zipEntry != null) {
        String filePath = destinationDirectoryPath + File.separator + zipEntry.getName();

        if (!zipEntry.isDirectory()) {
          extractFile(zipIn, filePath);
        } else {
          File file = new File(filePath);
          if (file.mkdirs()) {
            throw new IOException("Failed to create directory " + filePath);
          }
        }

        zipIn.closeEntry();
        zipEntry = zipIn.getNextEntry();
      }
    }
  }

  private static void extractFile(ZipInputStream zipInputStream, String filePath)
      throws IOException {
    try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
      zipInputStream.transferTo(fileOutputStream);
    }
  }
}
