/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.acrolinx.sidebar.pojo.settings.AcrolinxSidebarInitParameter;
import com.acrolinx.sidebar.pojo.settings.AcrolinxSidebarInitParameter.AcrolinxSidebarInitParameterBuilder;
import java.util.List;
import org.junit.jupiter.api.Test;

class StartPageInstallerTest {
  @Test
  void prepareSidebarUrlWithStartPage() throws Exception {
    final AcrolinxSidebarInitParameterBuilder builder =
        new AcrolinxSidebarInitParameterBuilder("123", List.of());

    final AcrolinxSidebarInitParameter acrolinxSidebarInitParameter = builder.build();

    assertEquals(
        StartPageInstaller.prepareSidebarUrl(acrolinxSidebarInitParameter),
        StartPageInstaller.getStartPageUrl());
  }

  @Test
  void prepareSidebarUrlWithSidebar() {
    final AcrolinxSidebarInitParameterBuilder builder =
        new AcrolinxSidebarInitParameterBuilder("123", List.of());
    builder.withSidebarUrl("https://127.0.0.1");
    builder.withShowServerSelector(false);

    final AcrolinxSidebarInitParameter acrolinxSidebarInitParameter = builder.build();

    assertEquals(
        "https://127.0.0.1", StartPageInstaller.prepareSidebarUrl(acrolinxSidebarInitParameter));
  }

  @Test
  void prepareSidebarUrlWithStartPageIfSidebarUrlIsSetBut() throws Exception {
    final AcrolinxSidebarInitParameterBuilder builder =
        new AcrolinxSidebarInitParameterBuilder("123", List.of());
    builder.withSidebarUrl("https://127.0.0.1");
    builder.withShowServerSelector(true);

    final AcrolinxSidebarInitParameter acrolinxSidebarInitParameter = builder.build();

    assertEquals(
        StartPageInstaller.prepareSidebarUrl(acrolinxSidebarInitParameter),
        StartPageInstaller.getStartPageUrl());
  }

  @Test
  void prepareSidebarUrlWithStartPageIfServerUrlIsSetButServerSelector() throws Exception {
    final AcrolinxSidebarInitParameterBuilder builder =
        new AcrolinxSidebarInitParameterBuilder("123", List.of());
    builder.withServerAddress("https://127.0.0.1");
    builder.withShowServerSelector(true);

    final AcrolinxSidebarInitParameter acrolinxSidebarInitParameter = builder.build();

    assertEquals(
        StartPageInstaller.prepareSidebarUrl(acrolinxSidebarInitParameter),
        StartPageInstaller.getStartPageUrl());
  }

  @Test
  void prepareSidebarUrlWithStartPageIfServerUrlIsSet() throws Exception {
    final AcrolinxSidebarInitParameterBuilder builder =
        new AcrolinxSidebarInitParameterBuilder("123", List.of());
    builder.withServerAddress("https://127.0.0.1");
    builder.withShowServerSelector(false);

    final AcrolinxSidebarInitParameter acrolinxSidebarInitParameter = builder.build();

    assertEquals(
        StartPageInstaller.prepareSidebarUrl(acrolinxSidebarInitParameter),
        StartPageInstaller.getStartPageUrl());
  }
}
