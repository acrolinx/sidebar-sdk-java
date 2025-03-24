/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import com.acrolinx.sidebar.pojo.settings.InputFormat;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileUtils {
  private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

  public static void deleteDirectory(Path directory) throws IOException {
    Validate.exists(directory, "directory");
    Validate.isDirectory(directory, "directory");

    try (Stream<Path> stream = Files.walk(directory)) {
      stream.sorted(Comparator.reverseOrder()).forEach(FileUtils::deleteFile);
    }
  }

  public static void extractZipFile(Path zipFilePath, Path destinationDirectoryPath)
      throws IOException {
    Validate.isRegularFile(zipFilePath, "zipFilePath");
    Validate.isDirectory(destinationDirectoryPath, "destinationDirectoryPath");

    try (ZipInputStream zipInputStream =
        new ZipInputStream(new FileInputStream(zipFilePath.toFile()))) {
      ZipEntry zipEntry;

      while ((zipEntry = zipInputStream.getNextEntry()) != null) {
        final Path filePath = destinationDirectoryPath.resolve(zipEntry.getName());

        if (zipEntry.isDirectory()) {
          Files.createDirectories(filePath);
        } else {
          extractFile(zipInputStream, filePath);
        }
      }
    }
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

  private static void deleteFile(Path filePath) {
    try {
      Files.delete(filePath);
    } catch (IOException e) {
      logger.error("Error while deleting file: {}", filePath, e);
    }
  }

  private static void extractFile(InputStream inputStream, Path filePath) throws IOException {
    try (OutputStream outputStream = Files.newOutputStream(filePath)) {
      inputStream.transferTo(outputStream);
    }
  }

  private FileUtils() {
    throw new IllegalStateException();
  }
}
