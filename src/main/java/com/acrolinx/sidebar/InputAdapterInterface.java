/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar;

import com.acrolinx.sidebar.pojo.document.AcrolinxMatch;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatchWithReplacement;
import com.acrolinx.sidebar.pojo.document.IntRange;
import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContent;
import com.acrolinx.sidebar.pojo.settings.InputFormat;
import java.util.List;

/** This interface serves to interact with the current editor. */
public interface InputAdapterInterface {
  /**
   * Receives the current input format. If this method returns null, the check will be canceled by
   * the sidebar.
   *
   * @return Returns the current input format.
   * @see InputFormat
   */
  InputFormat getInputFormat();

  /**
   * Receives the current text from the editor.
   *
   * @return Returns the text to be checked.
   */
  String getContent();

  /**
   * Receives the external text from the editor. Only supported with Acrolinx Platform 2019.10
   * (Sidebar version 14.16) and newer. Return null if not used.
   *
   * @return Returns the object for external content.
   */
  ExternalContent getExternalContent();

  /**
   * The path or filename of the document to check. In a CMS, it can be the id that is used to look
   * up the document.
   */
  String getDocumentReference();

  /**
   * Is called by the Acrolinx Integration to highlight current issues in the text editor.
   *
   * <p>Note that the matches contain the ranges as found in the originally checked text. It might
   * be necessary to implement some lookup algorithm here to map these ranges to the current
   * document (which might have changed in the meantime).
   *
   * @param checkId The current check id.
   * @param acrolinxMatches The ranges to be highlighted as sent by the AcrolinxServer.
   */
  void selectRanges(String checkId, List<AcrolinxMatch> acrolinxMatches);

  /**
   * Is called by the Acrolinx Integration to replace found issues with suggestions from the
   * Acrolinx Sidebar. If the Acrolinx Sidebar is configured as read only, this method won't be
   * called.
   *
   * <p>Note that the matches contain the ranges as found in the originally checked text. It might
   * be necessary to implement some lookup algorithm here to map these ranges to the current
   * document (which might have changed in the meantime).
   *
   * @param checkId The current check id.
   * @param matchesWithReplacement The ranges to be replaced.
   */
  void replaceRanges(String checkId, List<AcrolinxMatchWithReplacement> matchesWithReplacement);

  /**
   * Is called when checkSelection is enabled to get the current selected intRange for checking.
   *
   * @return currently selected range.
   */
  List<IntRange> getCurrentSelection();
}
