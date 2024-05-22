/* Copyright (c) 2024 Acrolinx GmbH */
package com.acrolinx.sidebar.lookup;

import static org.junit.jupiter.api.Assertions.*;

import com.acrolinx.sidebar.pojo.document.AbstractMatch;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatch;
import com.acrolinx.sidebar.pojo.document.IntRange;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LookupRangesDiffTest {
  private LookupRangesDiff lookupRangesDiff;

  @BeforeEach
  public void setUp() {
    lookupRangesDiff = new LookupRangesDiff();
  }

  @Test
  void whenWordAddedBeforeMatch() {
    String checkedText = "This is a car.";
    String changedText = "This is a yellow car.";

    final AcrolinxMatch acrolinxMatch = new AcrolinxMatch(new IntRange(10, 13), "car");
    final List<AcrolinxMatch> acrolinxMatches = List.of(acrolinxMatch);

    final Optional<List<? extends AbstractMatch>> matchesWithCorrectedRanges =
        lookupRangesDiff.getMatchesWithCorrectedRanges(checkedText, changedText, acrolinxMatches);

    assertTrue(matchesWithCorrectedRanges.isPresent());
    final List<? extends AbstractMatch> abstractCorrectedMatches = matchesWithCorrectedRanges.get();
    assertEquals(1, abstractCorrectedMatches.size());
    assertEquals(17, abstractCorrectedMatches.get(0).getRange().getMinimumInteger());
    assertEquals(20, abstractCorrectedMatches.get(0).getRange().getMaximumInteger());
  }

  @Test
  void whenChangesInWhiteSpacesScenario1() {
    String checkedText =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<!DOCTYPE topic PUBLIC \"-//OASIS//DTD DITA Topic//EN\" \"topic.dtd\">\n"
            + "<topic id=\"test_topic\">\n"
            + "    <title>An apple</title>\n"
            + "    <body>\n"
            + "        <p>An apple is a round, edible fruit produced by an apple tree. Malus spp. among them is a\n"
            + "            apple thousands of years old.</p>\n"
            + "    </body>\n"
            + "</topic>\n"
            + "</topic>\n";
    String changedText =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<!DOCTYPE topic PUBLIC \"-//OASIS//DTD DITA Topic//EN\" \"topic.dtd\">\n"
            + "<topic id=\"test_topic\">\n"
            + "    <title>An apple</title>\n"
            + "    <body>\n"
            + "        <p>An apple is a random round, edible fruit produced by an apple tree. Malus spp. among them\n"
            + "            is a apple thousands of years old.</p>\n"
            + "    </body>\n"
            + "</topic>\n"
            + "</topic>\n";

    final AcrolinxMatch acrolinxMatch1 = new AcrolinxMatch(new IntRange(266, 267), "a");
    final List<AcrolinxMatch> acrolinxMatches = List.of(acrolinxMatch1);

    final Optional<List<? extends AbstractMatch>> matchesWithCorrectedRanges =
        lookupRangesDiff.getMatchesWithCorrectedRanges(checkedText, changedText, acrolinxMatches);

    assertTrue(matchesWithCorrectedRanges.isPresent());
  }

  @Test
  void whenChangesInWhiteSpacesScenario2() {
    String checkedText =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<!DOCTYPE topic PUBLIC \"-//OASIS//DTD DITA Topic//EN\" \"topic.dtd\">\n"
            + "<topic id=\"test_topic\">\n"
            + "    <title>An apple</title>\n"
            + "    <body>\n"
            + "        <p><b>Iris</b><indexterm>flowers<indexterm>spring<indexterm>iris</indexterm></indexterm></indexterm>\n"
            + "            is a of\n"
            + "            between 200-300 species of flowering plants with showy flowers. It takes its name from\n"
            + "            the Greek word for a <i>rainbow</i>, referring to the wide variety of flower colors\n"
            + "            found among the many species. As well as being the scientific name, iris is also a\n"
            + "            apple used as a common name; for one thing, it refers to all <b>Iris</b> species, but\n"
            + "            some plants called thus belong to closely related genera. In North America, a common\n"
            + "            name for irises is <b>flags</b>, while the subgenus <b>Scorpiris</b> is widely known as\n"
            + "            <b>junos</b>, particularly in horticulture.The iris flower is of special interest as an example of the relation between flowering\n"
            + "            plants and pollinating insects. </p>\n"
            + "    </body>\n"
            + "</topic>\n";
    String changedText =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<!DOCTYPE topic PUBLIC \"-//OASIS//DTD DITA Topic//EN\" \"topic.dtd\">\n"
            + "<topic id=\"test_topic\">\n"
            + "    <title>An apple</title>\n"
            + "    <body>\n"
            + "        <p><b>Iris</b><indexterm>flowers<indexterm>spring<indexterm>iris</indexterm></indexterm></indexterm>\n"
            + "            is a of between 200-300 species of flowering plants with showy flowers. The name of this\n"
            + "            flower comes from the Greek word for a rainbow. This is because there are many different\n"
            + "            colors of flowers in the various species. <i/>  As well as being the scientific name,\n"
            + "            iris is also a apple used as a common name; for one thing, it refers to all <b>Iris</b>\n"
            + "            species, but some plants called thus belong to closely related genera. In North America,\n"
            + "            a common name for irises is <b>flags</b>, while the subgenus <b>Scorpiris</b> is widely\n"
            + "            known as <b>junos</b>, particularly in horticulture.The iris flower is of special\n"
            + "            interest as an example of the relation between flowering plants and pollinating insects. </p>\n"
            + "    </body>\n"
            + "</topic>\n";

    final AcrolinxMatch acrolinxMatch1 = new AcrolinxMatch(new IntRange(587, 600), " ");
    final List<AcrolinxMatch> acrolinxMatches = List.of(acrolinxMatch1);

    final Optional<List<? extends AbstractMatch>> matchesWithCorrectedRanges =
        lookupRangesDiff.getMatchesWithCorrectedRanges(checkedText, changedText, acrolinxMatches);

    assertTrue(matchesWithCorrectedRanges.isPresent());
  }
}
