/*
 * Copyright (c) 2017 Acrolinx GmbH
 */

package com.acrolinx.sidebar;

import java.util.List;
import java.util.Optional;

import com.acrolinx.sidebar.pojo.document.AbstractMatch;

public abstract class LookupRanges
{

    /**
     * Abstract method used to get the current location of matches.
     * As the location within the current document might have changed since the last check.
     *
     * @param checkedText The current text as it was when a check was performed on it.
     * @param changedText The current text within the editor.
     * @param matches The matches as given for the checked text.
     * @return An Optional with a list with the corrected ranges will be returned. In case the text within these ranges
     * is not present anymore an empty Optional will be returned.
     */
    public abstract Optional<List<? extends AbstractMatch>> getMatchesWithCorrectedRanges(String checkedText,
            String changedText, List<? extends AbstractMatch> matches);

}
