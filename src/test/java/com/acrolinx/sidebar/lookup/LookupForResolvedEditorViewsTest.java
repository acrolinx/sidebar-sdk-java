/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.lookup;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.acrolinx.sidebar.pojo.document.AbstractMatch;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatch;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatchWithReplacement;
import com.acrolinx.sidebar.pojo.document.IntRange;
import com.acrolinx.sidebar.pojo.document.externalContent.ExternalContentMatch;

class LookupForResolvedEditorViewsTest
{
    private final static String textApple = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<!DOCTYPE task PUBLIC \"-//OASIS//DTD DITA General Task//EN\" \"generalTask.dtd\">\n"
            + "<task id=\"task_tvt_rr5_t1b\">\n" + "    <title></title>\n" + "    <shortdesc></shortdesc>\n"
            + "    <taskbody>\n" + "        <context>\n" + "            <p><b>an</b> <u>apple</u></p>\n"
            + "        </context>\n" + "    </taskbody>\n" + "</task>";

    private final static String appleEditorContent = "a apple";

    private final static String textAutumnFlowers = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<!DOCTYPE concept PUBLIC \"-//OASIS//DTD DITA Concept//EN\" \"concept.dtd\">\n"
            + "<concept id=\"autumnFlowers\">\n" + "    <title>Autumn Flowers</title>\n" + "    <conbody>\n"
            + "        <p>Autumn is the season of the primary harvest. Autumn falls during September - November in\n"
            + "            the Northern hemisphere, and during March - June in the Southern hemisphere. Crops are\n"
            + "            harvested during Autumn. Leaves change color are at their beautiful best.</p>\n"
            + "        <p>Some of the flowers blooming in\n"
            + "                autumn<indexterm>flowers<indexterm>autumn</indexterm></indexterm> are: Acashia,\n"
            + "            Allium, Alstromeria, Amaranthus, Anemone, Baby's Breath, Bittersweet, Carnation, China\n"
            + "            berry, Chrysanthemum, Cockscomb, Cosmos, Echinops, Freesia, Gerbera Daisy, Gladiolus,\n"
            + "            Hypericum, Iris, Juniper, Kangaroo paw, Kalancheo, Liatrus, Lily, Asiatic, Lily,\n"
            + "            Gloriosa, Misty Blue, Orchid, Pepper berry, Protea, Queen Ann's Lace, Quince, Rover,\n"
            + "            Roses, Rowen berry, Salvia, Solidago, Statice, Star of Bethlehem, Sunflower, Yarrow,\n"
            + "            Zinnia.</p>\n" + "    </conbody>\n" + "</concept>";

    private final static String autumnFlowersEditor = "\n" + "Autumn Flowers\n"
            + "Autumn is the season of the primary harvest. Autumn falls during September - November in the Northern hemisphere, and during March - June in the Southern hemisphere. Crops are harvested during Autumn. Leaves change color are at their beautiful best.\n"
            + "Some of the flowers blooming in autumnflowersautumn are: Acashia, Allium, Alstromeria, Amaranthus, Anemone, Baby's Breath, Bittersweet, Carnation, China berry, Chrysanthemum, Cockscomb, Cosmos, Echinops, Freesia, Gerbera Daisy, Gladiolus, Hypericum, Iris, Juniper, Kangaroo paw, Kalancheo, Liatrus, Lily, Asiatic, Lily, Gloriosa, Misty Blue, Orchid, Pepper berry, Protea, Queen Ann's Lace, Quince, Rover, Roses, Rowen berry, Salvia, Solidago, Statice, Star of Bethlehem, Sunflower, Yarrow, Zinnia.";

    private final static String restDitaText = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
            + "<!DOCTYPE glossentry PUBLIC \"-//OASIS//DTD DITA Glossary//EN\" \"glossary.dtd\">\n"
            + "<glossentry id=\"glossentry_s2k_tlv_t1b\">\n" + "    <glossterm/>\n"
            + "    <?oxy_comment_start author=\"shroti_kapartiwar\" timestamp=\"20170804T161332+0530\" comment=\"this is the camment texxt\"?>\n"
            + "    <glossdef><?oxy_comment_end?> new data new line starts in div this mastake <sub>test</sub> page\n"
            + "            <sup>spalling.</sup>\n"
            + "        <table frame=\"all\" rowsep=\"1\" colsep=\"1\" id=\"table_lvy_4pv_t1b\">\n"
            + "            <title>tablee</title>\n" + "            <tgroup cols=\"2\">\n"
            + "                <colspec colname=\"c1\" colnum=\"1\" colwidth=\"1.44*\"/>\n"
            + "                <colspec colname=\"c2\" colnum=\"2\" colwidth=\"1*\"/>\n" + "                <thead>\n"
            + "                    <row>\n" + "                        <entry>wastee</entry>\n"
            + "                        <entry>wastee</entry>\n" + "                    </row>\n"
            + "                </thead>\n" + "                <tbody>\n" + "                    <row>\n"
            + "                        <entry>wastee</entry>\n" + "                        <entry>wastee</entry>\n"
            + "                    </row>\n" + "                    <row>\n"
            + "                        <entry>wastee</entry>\n" + "                        <entry>wastee</entry>\n"
            + "                    </row>\n" + "                    <row>\n"
            + "                        <entry>wastee</entry>\n" + "                        <entry>wastee</entry>\n"
            + "                    </row>\n" + "                </tbody>\n" + "            </tgroup>\n"
            + "        </table></glossdef>\n" + "    <glossBody>\n" + "        <glossScopeNote>\n"
            + "            <table frame=\"all\" rowsep=\"1\" colsep=\"1\" id=\"table_hfx_lkq_51b\">\n"
            + "                <title>tast</title>\n" + "                <tgroup cols=\"1\">\n"
            + "                    <colspec colname=\"c1\" colnum=\"1\" colwidth=\"1*\"/>\n"
            + "                    <thead>\n" + "                        <row>\n"
            + "                            <entry/>\n" + "                        </row>\n"
            + "                    </thead>\n" + "                    <tbody>\n" + "                        <row>\n"
            + "                            <entry/>\n" + "                        </row>\n"
            + "                        <row>\n" + "                            <entry/>\n"
            + "                        </row>\n" + "                    </tbody>\n" + "                </tgroup>\n"
            + "            </table>\n" + "        </glossScopeNote>\n" + "    </glossBody>\n" + "    <related-links/>\n"
            + "</glossentry>\n";

    private final static String restDitaEditor = "     new data new line starts in div this mastake  test  page  spalling.    tablee         wastee  wastee      wastee  wastee    wastee  wastee    wastee  wastee          tast                           ";

    @Test
    void testLookupIgnoreWhiteSpace()
    {
        List<AcrolinxMatch> matches = new ArrayList<>();
        matches.add(new AcrolinxMatch(new IntRange(246, 247), "a"));
        matches.add(new AcrolinxMatch(new IntRange(251, 252), " "));
        matches.add(new AcrolinxMatch(new IntRange(255, 260), "apple"));
        LookupForResolvedEditorViews lookup = new LookupForResolvedEditorViews();
        Optional<List<? extends AbstractMatch>> abstractMatches = lookup.matchRangesForResolvedEditorView(matches,
                textApple, appleEditorContent, offset -> new ContentNode() {
                    @Override
                    public int getStartOffset()
                    {
                        return 0;
                    }

                    @Override
                    public int getEndOffset()
                    {
                        return 8;
                    }

                    @Override
                    public String getContent()
                    {
                        return "a apple";
                    }

                    @Override
                    public String getAsXMLFragment()
                    {
                        return "<p><b>a</b> <u>apple</u></p>";
                    }
                });
        List<? extends AbstractMatch> matchesNew = abstractMatches.get();
        matchesNew.stream().forEach(
                match -> Assertions.assertEquals(appleEditorContent.substring(match.getRange().getMinimumInteger(),
                        match.getRange().getMaximumInteger()), match.getContent()));
    }

    @Test
    void testLookupIgnoreWhiteSpaceIgnoreWhitespaces()
    {
        List<AcrolinxMatch> matches = new ArrayList<>();
        String replacement = "blooming in\n" + "                autumn is";

        matches.add(new AcrolinxMatchWithReplacement("blooming", new IntRange(509, 517), replacement));
        matches.add(new AcrolinxMatchWithReplacement(" ", new IntRange(517, 518), ""));
        matches.add(new AcrolinxMatchWithReplacement("in", new IntRange(518, 520), ""));
        matches.add(new AcrolinxMatchWithReplacement(" ", new IntRange(520, 521), ""));
        matches.add(new AcrolinxMatchWithReplacement(" ", new IntRange(521, 522), ""));
        matches.add(new AcrolinxMatchWithReplacement(" ", new IntRange(522, 523), ""));
        matches.add(new AcrolinxMatchWithReplacement(" ", new IntRange(524, 525), ""));
        matches.add(new AcrolinxMatchWithReplacement(" ", new IntRange(525, 526), ""));
        matches.add(new AcrolinxMatchWithReplacement(" ", new IntRange(526, 527), ""));
        matches.add(new AcrolinxMatchWithReplacement(" ", new IntRange(527, 528), ""));

        matches.add(new AcrolinxMatchWithReplacement(" ", new IntRange(528, 529), ""));
        matches.add(new AcrolinxMatchWithReplacement(" ", new IntRange(529, 530), ""));
        matches.add(new AcrolinxMatchWithReplacement(" ", new IntRange(530, 531), ""));
        matches.add(new AcrolinxMatchWithReplacement(" ", new IntRange(531, 532), ""));
        matches.add(new AcrolinxMatchWithReplacement(" ", new IntRange(532, 533), ""));
        matches.add(new AcrolinxMatchWithReplacement(" ", new IntRange(533, 534), ""));
        matches.add(new AcrolinxMatchWithReplacement(" ", new IntRange(534, 535), ""));
        matches.add(new AcrolinxMatchWithReplacement(" ", new IntRange(535, 536), ""));
        matches.add(new AcrolinxMatchWithReplacement(" ", new IntRange(536, 537), ""));
        matches.add(new AcrolinxMatchWithReplacement("autumn", new IntRange(537, 543), ""));

        matches.add(new AcrolinxMatchWithReplacement(" ", new IntRange(602, 603), ""));
        matches.add(new AcrolinxMatchWithReplacement("are", new IntRange(603, 606), ""));

        LookupForResolvedEditorViews lookup = new LookupForResolvedEditorViews();
        Optional<List<? extends AbstractMatch>> abstractMatches = lookup.matchRangesForResolvedEditorView(matches,
                textAutumnFlowers, autumnFlowersEditor, offset -> new ContentNode() {
                    @Override
                    public int getStartOffset()
                    {
                        return 266;
                    }

                    @Override
                    public int getEndOffset()
                    {
                        return 763;
                    }

                    @Override
                    public String getContent()
                    {
                        return "Some of the flowers blooming in autumnflowersautumn are: Acashia, Allium, Alstromeria, Amaranthus, Anemone, Baby's Breath, Bittersweet, Carnation, China berry, Chrysanthemum, Cockscomb, Cosmos, Echinops, Freesia, Gerbera Daisy, Gladiolus, Hypericum, Iris, Juniper, Kangaroo paw, Kalancheo, Liatrus, Lily, Asiatic, Lily, Gloriosa, Misty Blue, Orchid, Pepper berry, Protea, Queen Ann's Lace, Quince, Rover, Roses, Rowen berry, Salvia, Solidago, Statice, Star of Bethlehem, Sunflower, Yarrow, Zinnia.";
                    }

                    @Override
                    public String getAsXMLFragment()
                    {
                        return "";
                    }
                });

        List<? extends AbstractMatch> matchesNew = abstractMatches.get();
        Assertions.assertEquals(7, matchesNew.size());
        matches.sort(new MatchComparator());
        matchesNew.stream().sorted(new MatchComparator()).forEach(
                match -> Assertions.assertEquals(autumnFlowersEditor.substring(match.getRange().getMinimumInteger(),
                        match.getRange().getMaximumInteger()), match.getContent()));

        AbstractMatch abstractMatch = matchesNew.get(0);
        Assertions.assertEquals("blooming", abstractMatch.getContent());
        Assertions.assertEquals("blooming in autumn is",
                ((AcrolinxMatchWithReplacement) abstractMatch).getReplacement());
    }

    @Test
    void testLookupFindMatchInNode()
    {
        List<AcrolinxMatch> matches = new ArrayList<>();

        matches.add(new AcrolinxMatch(new IntRange(1021, 1027), "wastee"));

        LookupForResolvedEditorViews lookup = new LookupForResolvedEditorViews();
        Optional<List<? extends AbstractMatch>> abstractMatches = lookup.matchRangesForResolvedEditorView(matches,
                restDitaText, restDitaEditor, offset -> new ContentNode() {
                    @Override
                    public int getStartOffset()
                    {
                        return 119;
                    }

                    @Override
                    public int getEndOffset()
                    {
                        return 126;
                    }

                    @Override
                    public String getContent()
                    {
                        return "wastee";
                    }

                    @Override
                    public String getAsXMLFragment()
                    {
                        return "";
                    }
                });

        List<? extends AbstractMatch> matchesNew = abstractMatches.get();

        Assertions.assertEquals(1, matchesNew.size());

        matchesNew.stream().forEach(match -> Assertions.assertEquals(
                restDitaEditor.substring(match.getRange().getMinimumInteger(), match.getRange().getMaximumInteger()),
                match.getContent()));
    }

    @Test
    void testLookupFindMatchInNode2()
    {
        List<AcrolinxMatch> matches = new ArrayList<>();

        matches.add(new AcrolinxMatch(new IntRange(1021, 1027), "tast"));

        LookupForResolvedEditorViews lookup = new LookupForResolvedEditorViews();
        Optional<List<? extends AbstractMatch>> abstractMatches = lookup.matchRangesForResolvedEditorView(matches,
                restDitaText, restDitaEditor, offset -> new ContentNode() {
                    @Override
                    public int getStartOffset()
                    {
                        return 171;
                    }

                    @Override
                    public int getEndOffset()
                    {
                        return 176;
                    }

                    @Override
                    public String getContent()
                    {
                        return "tast";
                    }

                    @Override
                    public String getAsXMLFragment()
                    {
                        return "";
                    }
                });

        List<? extends AbstractMatch> matchesNew = abstractMatches.get();

        Assertions.assertEquals(1, matchesNew.size());

        matchesNew.stream().forEach(match -> Assertions.assertEquals(
                restDitaEditor.substring(match.getRange().getMinimumInteger(), match.getRange().getMaximumInteger()),
                match.getContent()));
    }

    @Test
    void testLookupSpecialCases1()
    {
        final String rest_lookUpEditor = "    "
                + " The flowers are very seasobable. Flower blooming in winter are very brigth and fresh. New data new line starts in div this mastake  test  page  spalling.   tablee         waste  waste      waste  waste    waste  waste    waste  waste     "
                + "tast\n" + "client customer textt is written in cald and italicc formattingg\n"
                + "listt 1listt 2listt 3listt 4\n" + "orderr 1orderr 2orderr 3orderr 4orderr 5";

        final String getRest_lookUpText = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<!DOCTYPE glossentry PUBLIC \"-//OASIS//DTD DITA Glossary//EN\" \"glossary.dtd\">\n"
                + "<glossentry id=\"glossentry_s2k_tlv_t1b\">\n" + "    <glossterm/>\n"
                + "    <?oxy_comment_start author=\"shroti_kapartiwar\" timestamp=\"20170804T161332+0530\" comment=\"this is the camment texxt\"?>\n"
                + "    <glossdef><?oxy_comment_end?> The flowers are very seasobable. Flower blooming in\n"
                + "        <?oxy_comment_start author=\"shroti_kapartiwar\" timestamp=\"20170906T162356+0530\" comment=\"new line written\"?>winter<?oxy_comment_end?>\n"
                + "        are very brigth and fresh. new data new line starts in div this mastake <sub>test</sub> page\n"
                + "            <sup>spalling.</sup><table frame=\"all\" rowsep=\"1\" colsep=\"1\" id=\"table_lvy_4pv_t1b\">\n"
                + "            <title>tablee</title>\n" + "            <tgroup cols=\"2\">\n"
                + "                <colspec colname=\"c1\" colnum=\"1\" colwidth=\"1.44*\"/>\n"
                + "                <colspec colname=\"c2\" colnum=\"2\" colwidth=\"1*\"/>\n"
                + "                <thead>\n" + "                    <row>\n"
                + "                        <entry>wastee</entry>\n" + "                        <entry>wastee</entry>\n"
                + "                    </row>\n" + "                </thead>\n" + "                <tbody>\n"
                + "                    <row>\n" + "                        <entry>wastee</entry>\n"
                + "                        <entry>wastee</entry>\n" + "                    </row>\n"
                + "                    <row>\n" + "                        <entry>wastee</entry>\n"
                + "                        <entry>wastee</entry>\n" + "                    </row>\n"
                + "                    <row>\n" + "                        <entry>wastee</entry>\n"
                + "                        <entry>wastee</entry>\n" + "                    </row>\n"
                + "                </tbody>\n" + "            </tgroup>\n" + "        </table></glossdef>\n"
                + "    <glossBody>\n"
                + "        <glossScopeNote><table frame=\"all\" rowsep=\"1\" colsep=\"1\" id=\"table_hfx_lkq_51b\">\n"
                + "                <title>tast</title>\n" + "                <tgroup cols=\"1\">\n"
                + "                    <colspec colname=\"c1\" colnum=\"1\" colwidth=\"1*\"/>\n"
                + "                    <thead>\n" + "                        <row>\n"
                + "                            <entry/>\n" + "                        </row>\n"
                + "                    </thead>\n" + "                    <tbody>\n" + "                        <row>\n"
                + "                            <entry/>\n" + "                        </row>\n"
                + "                        <row>\n" + "                            <entry/>\n"
                + "                        </row>\n" + "                    </tbody>\n" + "                </tgroup>\n"
                + "            </table>client customer textt is written in <b>cald</b> and <i>italicc</i>\n"
                + "            <u>formattingg</u><ul id=\"ul_rp3_dsq_51b\">\n" + "                <li>listt 1</li>\n"
                + "                <li>listt 2</li>\n" + "                <li>listt 3</li>\n"
                + "                <li>listt 4</li>\n" + "            </ul><ol id=\"ol_it4_fsq_51b\">\n"
                + "                <li>orderr 1</li>\n" + "                <li>orderr 2</li>\n"
                + "                <li>orderr 3</li>\n" + "                <li>orderr 4</li>\n"
                + "                <li>orderr 5</li>\n" + "            </ol></glossScopeNote>\n" + "    </glossBody>\n"
                + "    <related-links/>\n" + "</glossentry>\n";

        List<AcrolinxMatch> matches = new ArrayList<>();

        matches.add(new AcrolinxMatch(new IntRange(347, 351), "very"));
        matches.add(new AcrolinxMatch(new IntRange(351, 352), " "));
        matches.add(new AcrolinxMatch(new IntRange(352, 362), "seasobable"));

        matches.stream().forEach(
                match -> Assertions.assertEquals(getRest_lookUpText.substring(match.getRange().getMinimumInteger(),
                        match.getRange().getMaximumInteger()), match.getContent()));

        ContentNode contentNode = new ContentNode() {
            @Override
            public int getStartOffset()
            {
                return 4;
            }

            @Override
            public int getEndOffset()
            {
                return 243;
            }

            @Override
            public String getContent()
            {
                return " The flowers are very seasobable. Flower blooming in winter are very brigth and fresh. New data new line starts in div this mastake  test  page  spalling.   tablee         waste  waste      waste  waste    waste  waste    waste  waste     ";
            }

            @Override
            public String getAsXMLFragment()
            {
                return "<glossdef><?oxy_comment_end?> The flowers are very seasobable. Flower blooming in\n"
                        + "        <?oxy_comment_start author=\"shroti_kapartiwar\" timestamp=\"20170906T162356+0530\" comment=\"new line written\"?>winter<?oxy_comment_end?>\n"
                        + "        are very brigth and fresh. new data new line starts in div this mastake <sub>test</sub> page\n"
                        + "            <sup>spalling.</sup><table frame=\"all\" rowsep=\"1\" colsep=\"1\" id=\"table_lvy_4pv_t1b\">\n"
                        + "            <title>tablee</title>\n" + "            <tgroup cols=\"2\">\n"
                        + "                <colspec colname=\"c1\" colnum=\"1\" colwidth=\"1.44*\"/>\n"
                        + "                <colspec colname=\"c2\" colnum=\"2\" colwidth=\"1*\"/>\n"
                        + "                <thead>\n" + "                    <row>\n"
                        + "                        <entry>wastee</entry>\n"
                        + "                        <entry>wastee</entry>\n" + "                    </row>\n"
                        + "                </thead>\n" + "                <tbody>\n" + "                    <row>\n"
                        + "                        <entry>wastee</entry>\n"
                        + "                        <entry>wastee</entry>\n" + "                    </row>\n"
                        + "                    <row>\n" + "                        <entry>wastee</entry>\n"
                        + "                        <entry>wastee</entry>\n" + "                    </row>\n"
                        + "                    <row>\n" + "                        <entry>wastee</entry>\n"
                        + "                        <entry>wastee</entry>\n" + "                    </row>\n"
                        + "                </tbody>\n" + "            </tgroup>\n" + "        </table></glossdef>";
            }
        };

        Assertions.assertEquals(rest_lookUpEditor.substring(contentNode.getStartOffset(), contentNode.getEndOffset()),
                contentNode.getContent());

        LookupForResolvedEditorViews lookup = new LookupForResolvedEditorViews();
        Optional<List<? extends AbstractMatch>> abstractMatches = lookup.matchRangesForResolvedEditorView(matches,
                getRest_lookUpText, rest_lookUpEditor, offset -> contentNode);

        List<? extends AbstractMatch> matchesNew = abstractMatches.get();

        Assertions.assertEquals(3, matchesNew.size());

        matchesNew.stream().forEach(match -> {
            Assertions.assertEquals(rest_lookUpEditor.substring(match.getRange().getMinimumInteger(),
                    match.getRange().getMaximumInteger()), match.getContent());
        });
    }

    @Test
    void testLookupSpecialCasesDeletedNewLine()
    {
        final String rest_lookUpEditor = "    "
                + " The flowers are very seasobable. Flower blooming in winter are very brigth and fresh. New data new line starts in div this mastake  test  page  spalling.   tablee         waste  waste      waste  waste    waste  waste    waste  waste     "
                + "tast\n" + "client customer textt is written in cald and italicc formattingg\n"
                + "listt 1listt 2listt 3listt 4\n" + "orderr 1orderr 2orderr 3orderr 4orderr 5";

        final String getRest_lookUpText = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<!DOCTYPE glossentry PUBLIC \"-//OASIS//DTD DITA Glossary//EN\" \"glossary.dtd\">\n"
                + "<glossentry id=\"glossentry_s2k_tlv_t1b\">\n" + "    <glossterm/>\n"
                + "    <?oxy_comment_start author=\"shroti_kapartiwar\" timestamp=\"20170804T161332+0530\" comment=\"this is the camment texxt\"?>\n"
                + "    <glossdef><?oxy_comment_end?> The flowers are very seasobable. Flower blooming in\n"
                + "        <?oxy_comment_start author=\"shroti_kapartiwar\" timestamp=\"20170906T162356+0530\" comment=\"new line written\"?>winter<?oxy_comment_end?>\n"
                + "        are very brigth and fresh. new data new line starts in div this mastake <sub>test</sub> page\n"
                + "            <sup>spalling.</sup><table frame=\"all\" rowsep=\"1\" colsep=\"1\" id=\"table_lvy_4pv_t1b\">\n"
                + "            <title>tablee</title>\n" + "            <tgroup cols=\"2\">\n"
                + "                <colspec colname=\"c1\" colnum=\"1\" colwidth=\"1.44*\"/>\n"
                + "                <colspec colname=\"c2\" colnum=\"2\" colwidth=\"1*\"/>\n"
                + "                <thead>\n" + "                    <row>\n"
                + "                        <entry>wastee</entry>\n" + "                        <entry>wastee</entry>\n"
                + "                    </row>\n" + "                </thead>\n" + "                <tbody>\n"
                + "                    <row>\n" + "                        <entry>wastee</entry>\n"
                + "                        <entry>wastee</entry>\n" + "                    </row>\n"
                + "                    <row>\n" + "                        <entry>wastee</entry>\n"
                + "                        <entry>wastee</entry>\n" + "                    </row>\n"
                + "                    <row>\n" + "                        <entry>wastee</entry>\n"
                + "                        <entry>wastee</entry>\n" + "                    </row>\n"
                + "                </tbody>\n" + "            </tgroup>\n" + "        </table></glossdef>\n"
                + "    <glossBody>\n"
                + "        <glossScopeNote><table frame=\"all\" rowsep=\"1\" colsep=\"1\" id=\"table_hfx_lkq_51b\">\n"
                + "                <title>tast</title>\n" + "                <tgroup cols=\"1\">\n"
                + "                    <colspec colname=\"c1\" colnum=\"1\" colwidth=\"1*\"/>\n"
                + "                    <thead>\n" + "                        <row>\n"
                + "                            <entry/>\n" + "                        </row>\n"
                + "                    </thead>\n" + "                    <tbody>\n" + "                        <row>\n"
                + "                            <entry/>\n" + "                        </row>\n"
                + "                        <row>\n" + "                            <entry/>\n"
                + "                        </row>\n" + "                    </tbody>\n" + "                </tgroup>\n"
                + "            </table>client customer textt is written in <b>cald</b> and <i>italicc</i>\n"
                + "            <u>formattingg</u><ul id=\"ul_rp3_dsq_51b\">\n" + "                <li>listt 1</li>\n"
                + "                <li>listt 2</li>\n" + "                <li>listt 3</li>\n"
                + "                <li>listt 4</li>\n" + "            </ul><ol id=\"ol_it4_fsq_51b\">\n"
                + "                <li>orderr 1</li>\n" + "                <li>orderr 2</li>\n"
                + "                <li>orderr 3</li>\n" + "                <li>orderr 4</li>\n"
                + "                <li>orderr 5</li>\n" + "            </ol></glossScopeNote>\n" + "    </glossBody>\n"
                + "    <related-links/>\n" + "</glossentry>\n";

        List<AcrolinxMatch> matches = new ArrayList<>();

        matches.add(new AcrolinxMatch(new IntRange(371, 379), "blooming"));
        matches.add(new AcrolinxMatch(new IntRange(379, 380), " "));
        matches.add(new AcrolinxMatch(new IntRange(380, 382), "in"));

        matches.add(new AcrolinxMatch(new IntRange(382, 383), "\n"));
        matches.add(new AcrolinxMatch(new IntRange(383, 384), " "));
        matches.add(new AcrolinxMatch(new IntRange(384, 385), " "));
        matches.add(new AcrolinxMatch(new IntRange(385, 386), " "));
        matches.add(new AcrolinxMatch(new IntRange(386, 387), " "));
        matches.add(new AcrolinxMatch(new IntRange(387, 388), " "));
        matches.add(new AcrolinxMatch(new IntRange(388, 389), " "));
        matches.add(new AcrolinxMatch(new IntRange(389, 390), " "));
        matches.add(new AcrolinxMatch(new IntRange(390, 391), " "));

        matches.add(new AcrolinxMatch(new IntRange(499, 505), "winter"));

        matches.add(new AcrolinxMatch(new IntRange(524, 525), " "));
        matches.add(new AcrolinxMatch(new IntRange(525, 526), " "));
        matches.add(new AcrolinxMatch(new IntRange(526, 527), " "));
        matches.add(new AcrolinxMatch(new IntRange(527, 528), " "));
        matches.add(new AcrolinxMatch(new IntRange(528, 529), " "));
        matches.add(new AcrolinxMatch(new IntRange(529, 530), " "));

        matches.add(new AcrolinxMatch(new IntRange(530, 531), " "));
        matches.add(new AcrolinxMatch(new IntRange(531, 532), " "));
        matches.add(new AcrolinxMatch(new IntRange(532, 533), " "));

        matches.add(new AcrolinxMatch(new IntRange(533, 536), "are"));

        ContentNode contentNode = new ContentNode() {
            @Override
            public int getStartOffset()
            {
                return 4;
            }

            @Override
            public int getEndOffset()
            {
                return 243;
            }

            @Override
            public String getContent()
            {
                return " The flowers are very seasobable. Flower blooming in winter are very brigth and fresh. New data new line starts in div this mastake  test  page  spalling.   tablee         waste  waste      waste  waste    waste  waste    waste  waste     ";
            }

            @Override
            public String getAsXMLFragment()
            {
                return "<glossdef><?oxy_comment_end?> The flowers are very seasobable. Flower blooming in\n"
                        + "        <?oxy_comment_start author=\"shroti_kapartiwar\" timestamp=\"20170906T162356+0530\" comment=\"new line written\"?>winter<?oxy_comment_end?>\n"
                        + "        are very brigth and fresh. new data new line starts in div this mastake <sub>test</sub> page\n"
                        + "            <sup>spalling.</sup><table frame=\"all\" rowsep=\"1\" colsep=\"1\" id=\"table_lvy_4pv_t1b\">\n"
                        + "            <title>tablee</title>\n" + "            <tgroup cols=\"2\">\n"
                        + "                <colspec colname=\"c1\" colnum=\"1\" colwidth=\"1.44*\"/>\n"
                        + "                <colspec colname=\"c2\" colnum=\"2\" colwidth=\"1*\"/>\n"
                        + "                <thead>\n" + "                    <row>\n"
                        + "                        <entry>wastee</entry>\n"
                        + "                        <entry>wastee</entry>\n" + "                    </row>\n"
                        + "                </thead>\n" + "                <tbody>\n" + "                    <row>\n"
                        + "                        <entry>wastee</entry>\n"
                        + "                        <entry>wastee</entry>\n" + "                    </row>\n"
                        + "                    <row>\n" + "                        <entry>wastee</entry>\n"
                        + "                        <entry>wastee</entry>\n" + "                    </row>\n"
                        + "                    <row>\n" + "                        <entry>wastee</entry>\n"
                        + "                        <entry>wastee</entry>\n" + "                    </row>\n"
                        + "                </tbody>\n" + "            </tgroup>\n" + "        </table></glossdef>";
            }
        };

        Assertions.assertEquals(rest_lookUpEditor.substring(contentNode.getStartOffset(), contentNode.getEndOffset()),
                contentNode.getContent());

        LookupForResolvedEditorViews lookup = new LookupForResolvedEditorViews();
        Optional<List<? extends AbstractMatch>> abstractMatches = lookup.matchRangesForResolvedEditorView(matches,
                getRest_lookUpText, rest_lookUpEditor, offset -> contentNode);

        List<? extends AbstractMatch> matchesNew = abstractMatches.get();

        Assertions.assertEquals(7, matchesNew.size());

        matchesNew.stream().forEach(match -> Assertions.assertEquals(
                rest_lookUpEditor.substring(match.getRange().getMinimumInteger(), match.getRange().getMaximumInteger()),
                match.getContent()));
    }

    @Test
    void testLookupIncludesHTMLEntities()
    {
        final String authorViewContent = "Instructions for Authors.\n"
                + "Replace the values in < > appropriate values.\n" + "The <caar> is <nicce>.";
        final String documentContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<!DOCTYPE topic PUBLIC \"-//OASIS//DTD DITA Topic//EN\" \"topic.dtd\">\n"
                + "<topic id=\"topic_icd_txy_3db\">\n" + "    <title>Instructions for Authors.</title>\n"
                + "    <body>\n" + "        <p>Replace the values in &lt; > appropriate values.</p>\n"
                + "        <p>The &lt;caar&gt; is &lt;nicce&gt;.</p>\n" + "    </body>\n" + "</topic>\n";

        ContentNode contentNode = new ContentNode() {
            @Override
            public int getStartOffset()
            {
                return 72;
            }

            @Override
            public int getEndOffset()
            {
                return 94;
            }

            @Override
            public String getContent()
            {
                return "The <caar> is <nicce>.";
            }

            @Override
            public String getAsXMLFragment()
            {
                return "<p>The &lt;caar&gt; is &lt;nicce&gt;.</p>";
            }
        };

        Assertions.assertEquals(authorViewContent.substring(contentNode.getStartOffset(), contentNode.getEndOffset()),
                contentNode.getContent());

        List<AcrolinxMatch> matches = new ArrayList<>();

        matches.add(new AcrolinxMatch(new IntRange(272, 276), "<"));
        matches.add(new AcrolinxMatch(new IntRange(276, 280), "caar"));
        matches.add(new AcrolinxMatch(new IntRange(280, 284), ">"));

        matches.add(new AcrolinxMatch(new IntRange(288, 292), "<"));
        matches.add(new AcrolinxMatch(new IntRange(292, 297), "nicce"));
        matches.add(new AcrolinxMatch(new IntRange(297, 301), ">"));

        LookupForResolvedEditorViews lookup = new LookupForResolvedEditorViews();
        Optional<List<? extends AbstractMatch>> abstractMatches = lookup.matchRangesForResolvedEditorView(matches,
                documentContent, authorViewContent, offset -> contentNode);

        abstractMatches.get().stream().forEach(match -> Assertions.assertEquals(
                authorViewContent.substring(match.getRange().getMinimumInteger(), match.getRange().getMaximumInteger()),
                match.getContent()));
    }

    /***
     * This test covers the rare edge case where the contentnode's content appears twice in the document
     * and the matchcontent also appears once in referenced content.
     */
    @Test
    void testLookupOnFileWithRepeatedSameContentAndReferences()
    {
        final String matchContent = "mistaake";
        final String contentNodeString = " " + matchContent + " " + matchContent;
        final String contentNodeXMLString = "<p>" + contentNodeString + "</p>";

        final String authorViewContentBeforeContentNodeString = "Instructions for Authors.\n" + " " + matchContent
                + "\n" + contentNodeString + "\n";
        final String authorViewContent = authorViewContentBeforeContentNodeString + contentNodeString;

        final String documentContentBeforeContentNode = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<!DOCTYPE topic PUBLIC \"-//OASIS//DTD DITA Topic//EN\" \"topic.dtd\">\n"
                + "<topic id=\"topic_icd_txy_3db\">\n" + "<title>Instructions for Authors.</title>\n"
                + "    <body>\n<p conref=\"mistake-conref\"/>" + contentNodeXMLString;
        final String documentContentAfterContentNode = "    </body>\n" + "</topic>\n";
        final String documentContent = documentContentBeforeContentNode + contentNodeXMLString
                + documentContentAfterContentNode;
        ContentNode contentNode = new ContentNode() {
            @Override
            public int getStartOffset()
            {
                return authorViewContentBeforeContentNodeString.length();
            }

            @Override
            public int getEndOffset()
            {
                return authorViewContentBeforeContentNodeString.length() + contentNodeString.length();
            }

            @Override
            public String getContent()
            {
                return contentNodeString;
            }

            @Override
            public String getAsXMLFragment()
            {
                return contentNodeXMLString;
            }
        };

        Assertions.assertEquals(authorViewContent.substring(contentNode.getStartOffset(), contentNode.getEndOffset()),
                contentNode.getContent());

        List<AcrolinxMatch> matches = new ArrayList<>();

        int pTagAndWhiteSpace = 4;
        matches.add(new AcrolinxMatch(
                new IntRange(documentContentBeforeContentNode.length() + pTagAndWhiteSpace,
                        documentContentBeforeContentNode.length() + pTagAndWhiteSpace + matchContent.length()),
                matchContent));

        LookupForResolvedEditorViews lookup = new LookupForResolvedEditorViews();
        Optional<List<? extends AbstractMatch>> abstractMatches = lookup.matchRangesForResolvedEditorView(matches,
                documentContent, authorViewContent, offset -> contentNode);

        AbstractMatch onlyMatch = abstractMatches.get().get(0);
        Assertions.assertEquals(onlyMatch.getContent(), authorViewContent.substring(
                onlyMatch.getRange().getMinimumInteger(), onlyMatch.getRange().getMaximumInteger()));
        Assertions.assertEquals(authorViewContentBeforeContentNodeString.length() + 1,
                onlyMatch.getRange().getMinimumInteger());
        Assertions.assertEquals(authorViewContentBeforeContentNodeString.length() + 1 + matchContent.length(),
                onlyMatch.getRange().getMaximumInteger());
    }

    @Test
    void testLookupInReferencedContent()
    {
        final String matchContent = "mistaake";
        final String contentNodeString = " " + matchContent + " ";
        final String contentNodeResolvedXMLString = "<p>" + contentNodeString + "</p>";

        final String authorViewContentBeforeContentNodeString = "Instructions for Authors.\n";
        final String authorViewContent = authorViewContentBeforeContentNodeString + contentNodeString + "\n";
        final String referencedContentTag = "<p conref=\"mistake-conref\"/>";

        final String documentContentBeforeContentNode = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<!DOCTYPE topic PUBLIC \"-//OASIS//DTD DITA Topic//EN\" \"topic.dtd\">\n"
                + "<topic id=\"topic_icd_txy_3db\">\n" + "<title>Instructions for Authors.</title>\n" + "    <body>\n";
        final String documentContentAfterContentNode = "\n    </body>\n" + "</topic>\n";
        final String documentContent = documentContentBeforeContentNode + referencedContentTag
                + documentContentAfterContentNode;
        ContentNode contentNode = new ExternalContentNode() {
            @Override
            public ReferenceTreeNode getReferenceTree()
            {
                ReferenceTreeNode r = new ReferenceTreeNode();
                r.setUnresolvedContent(referencedContentTag);
                r.setResolvedContent(contentNodeResolvedXMLString);

                ReferenceTreeNode externalContentNode = new ReferenceTreeNode();
                externalContentNode.setResolvedContent(contentNodeResolvedXMLString);
                externalContentNode.setUnresolvedContent(contentNodeResolvedXMLString);
                externalContentNode.setStartOffsetInParent(0);
                externalContentNode.setReferencingTag(referencedContentTag);
                r.referenceChildren.add(externalContentNode);
                return r;
            }

            @Override
            public int getStartOffset()
            {
                return authorViewContentBeforeContentNodeString.length();
            }

            @Override
            public int getEndOffset()
            {
                return authorViewContentBeforeContentNodeString.length() + contentNodeString.length();
            }

            @Override
            public String getContent()
            {
                return contentNodeString;
            }

            @Override
            public String getAsXMLFragment()
            {
                return referencedContentTag;
            }
        };

        Assertions.assertEquals(authorViewContent.substring(contentNode.getStartOffset(), contentNode.getEndOffset()),
                contentNode.getContent());

        List<AcrolinxMatch> matches = new ArrayList<>();

        int pTagAndWhiteSpace = 4;
        List<ExternalContentMatch> externalContentMatches = new ArrayList<>();
        ExternalContentMatch externalContentMatch = new ExternalContentMatch("id", "dita", pTagAndWhiteSpace,
                pTagAndWhiteSpace + matchContent.length());
        externalContentMatches.add(externalContentMatch);
        matches.add(new AcrolinxMatch(
                new IntRange(documentContentBeforeContentNode.length(),
                        documentContentBeforeContentNode.length() + referencedContentTag.length()),
                matchContent, externalContentMatches));

        LookupForResolvedEditorViews lookup = new LookupForResolvedEditorViews();
        Optional<List<? extends AbstractMatch>> abstractMatches = lookup.matchRangesForResolvedEditorView(matches,
                documentContent, authorViewContent, offset -> contentNode);

        AbstractMatch onlyMatch = abstractMatches.get().get(0);
        IntRange matchRange = onlyMatch.getRange();
        Assertions.assertEquals(onlyMatch.getContent(),
                authorViewContent.substring(matchRange.getMinimumInteger(), matchRange.getMaximumInteger()));
        Assertions.assertEquals(authorViewContentBeforeContentNodeString.length() + 1, matchRange.getMinimumInteger());
        Assertions.assertEquals(authorViewContentBeforeContentNodeString.length() + 1 + matchContent.length(),
                matchRange.getMaximumInteger());
    }

    @Test
    void testLookupDuplicationInReferencedContent()
    {
        final String matchContent = "mistaake";
        final String contentNodeString = " " + matchContent + " " + matchContent;
        final String contentNodeResolvedXMLString = "<p>" + contentNodeString + "</p>";

        final String authorViewContentBeforeContentNodeString = "Instructions for Authors.\n";
        final String authorViewContent = authorViewContentBeforeContentNodeString + contentNodeString + "\n";
        final String referencedContentTag = "<p conref=\"mistake-conref\"/>";

        final String documentContentBeforeContentNode = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<!DOCTYPE topic PUBLIC \"-//OASIS//DTD DITA Topic//EN\" \"topic.dtd\">\n"
                + "<topic id=\"topic_icd_txy_3db\">\n" + "<title>Instructions for Authors.</title>\n" + "    <body>\n";
        final String documentContentAfterContentNode = "\n    </body>\n" + "</topic>\n";
        final String documentContent = documentContentBeforeContentNode + referencedContentTag
                + documentContentAfterContentNode;
        ContentNode contentNode = new ExternalContentNode() {
            @Override
            public ReferenceTreeNode getReferenceTree()
            {
                ReferenceTreeNode r = new ReferenceTreeNode();
                r.setUnresolvedContent(referencedContentTag);
                r.setResolvedContent(contentNodeResolvedXMLString);

                ReferenceTreeNode externalContentNode = new ReferenceTreeNode();
                externalContentNode.setResolvedContent(contentNodeResolvedXMLString);
                externalContentNode.setUnresolvedContent(contentNodeResolvedXMLString);
                externalContentNode.setStartOffsetInParent(0);
                externalContentNode.setReferencingTag(referencedContentTag);
                r.referenceChildren.add(externalContentNode);
                return r;
            }

            @Override
            public int getStartOffset()
            {
                return authorViewContentBeforeContentNodeString.length();
            }

            @Override
            public int getEndOffset()
            {
                return authorViewContentBeforeContentNodeString.length() + contentNodeString.length();
            }

            @Override
            public String getContent()
            {
                return contentNodeString;
            }

            @Override
            public String getAsXMLFragment()
            {
                return referencedContentTag;
            }
        };

        Assertions.assertEquals(authorViewContent.substring(contentNode.getStartOffset(), contentNode.getEndOffset()),
                contentNode.getContent());

        List<AcrolinxMatch> matches = new ArrayList<>();

        int pTagAndWhiteSpace = 4;
        List<ExternalContentMatch> externalContentMatches = new ArrayList<>();
        ExternalContentMatch externalContentMatch = new ExternalContentMatch("id", "dita", pTagAndWhiteSpace,
                pTagAndWhiteSpace + matchContent.length());
        externalContentMatches.add(externalContentMatch);
        matches.add(new AcrolinxMatch(
                new IntRange(documentContentBeforeContentNode.length(),
                        documentContentBeforeContentNode.length() + referencedContentTag.length()),
                matchContent, externalContentMatches));

        LookupForResolvedEditorViews lookup = new LookupForResolvedEditorViews();
        Optional<List<? extends AbstractMatch>> abstractMatches = lookup.matchRangesForResolvedEditorView(matches,
                documentContent, authorViewContent, offset -> contentNode);

        AbstractMatch onlyMatch = abstractMatches.get().get(0);
        IntRange matchRange = onlyMatch.getRange();
        Assertions.assertEquals(onlyMatch.getContent(),
                authorViewContent.substring(matchRange.getMinimumInteger(), matchRange.getMaximumInteger()));
        Assertions.assertEquals(authorViewContentBeforeContentNodeString.length() + 1, matchRange.getMinimumInteger());
        Assertions.assertEquals(authorViewContentBeforeContentNodeString.length() + 1 + matchContent.length(),
                matchRange.getMaximumInteger());
    }

    @Test
    void testLookupDuplicationInSecondReferencedContentLevel()
    {
        final String matchContent = "mistaake";
        final String contentNodeString = " " + matchContent + " " + matchContent;
        final String contentNodeResolvedXMLString = "<div><p>" + contentNodeString + "</p></div>";

        final String authorViewContentBeforeContentNodeString = "Instructions for Authors.\n";
        final String authorViewContent = authorViewContentBeforeContentNodeString + contentNodeString + "\n";
        final String referencedContentTag = "<div conref=\"mistake-conref\"/>";
        final String secondReferencedContentTag = "<p conref=\"mistake-2-conref\"/>";

        final String documentContentBeforeContentNode = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<!DOCTYPE topic PUBLIC \"-//OASIS//DTD DITA Topic//EN\" \"topic.dtd\">\n"
                + "<topic id=\"topic_icd_txy_3db\">\n" + "<title>Instructions for Authors.</title>\n" + "    <body>\n";
        final String documentContentAfterContentNode = "\n    </body>\n" + "</topic>\n";
        final String documentContent = documentContentBeforeContentNode + referencedContentTag
                + documentContentAfterContentNode;
        ContentNode contentNode = new ExternalContentNode() {
            @Override
            public ReferenceTreeNode getReferenceTree()
            {
                ReferenceTreeNode r = new ReferenceTreeNode();
                r.setUnresolvedContent(referencedContentTag);
                r.setResolvedContent(contentNodeResolvedXMLString);

                ReferenceTreeNode externalContentNode = new ReferenceTreeNode();
                externalContentNode.setResolvedContent(contentNodeResolvedXMLString);
                externalContentNode.setUnresolvedContent("<div>" + secondReferencedContentTag + "</div>");
                externalContentNode.setStartOffsetInParent(0);
                externalContentNode.setReferencingTag(referencedContentTag);

                ReferenceTreeNode externalContentChildNode = new ReferenceTreeNode();
                String firstLevelSubstring = contentNodeResolvedXMLString.substring(5,
                        contentNodeResolvedXMLString.length() - 6);
                externalContentChildNode.setResolvedContent(firstLevelSubstring);
                externalContentChildNode.setUnresolvedContent(firstLevelSubstring);
                externalContentChildNode.setReferencingTag(secondReferencedContentTag);
                externalContentChildNode.setStartOffsetInParent(5);

                externalContentNode.referenceChildren.add(externalContentChildNode);
                r.referenceChildren.add(externalContentNode);
                return r;
            }

            @Override
            public int getStartOffset()
            {
                return authorViewContentBeforeContentNodeString.length();
            }

            @Override
            public int getEndOffset()
            {
                return authorViewContentBeforeContentNodeString.length() + contentNodeString.length();
            }

            @Override
            public String getContent()
            {
                return contentNodeString;
            }

            @Override
            public String getAsXMLFragment()
            {
                return referencedContentTag;
            }
        };

        Assertions.assertEquals(authorViewContent.substring(contentNode.getStartOffset(), contentNode.getEndOffset()),
                contentNode.getContent());

        List<AcrolinxMatch> matches = new ArrayList<>();

        int divTag = 5;
        int pTag = 3;
        int pTagAndWhiteSpace = pTag + 1;
        List<ExternalContentMatch> nestedExternalContentMatches = new ArrayList<>();
        ExternalContentMatch nestedContentMatch = new ExternalContentMatch("id2", "dita", pTagAndWhiteSpace,
                pTagAndWhiteSpace + matchContent.length());
        nestedExternalContentMatches.add(nestedContentMatch);

        List<ExternalContentMatch> externalContentMatches = new ArrayList<>();
        ExternalContentMatch externalContentMatch = new ExternalContentMatch("id", "dita", divTag,
                divTag + secondReferencedContentTag.length(), nestedExternalContentMatches);

        externalContentMatches.add(externalContentMatch);
        matches.add(new AcrolinxMatch(
                new IntRange(documentContentBeforeContentNode.length(),
                        documentContentBeforeContentNode.length() + referencedContentTag.length()),
                matchContent, externalContentMatches));

        LookupForResolvedEditorViews lookup = new LookupForResolvedEditorViews();
        Optional<List<? extends AbstractMatch>> abstractMatches = lookup.matchRangesForResolvedEditorView(matches,
                documentContent, authorViewContent, offset -> contentNode);

        AbstractMatch onlyMatch = abstractMatches.get().get(0);
        IntRange matchRange = onlyMatch.getRange();
        Assertions.assertEquals(onlyMatch.getContent(),
                authorViewContent.substring(matchRange.getMinimumInteger(), matchRange.getMaximumInteger()));
        Assertions.assertEquals(authorViewContentBeforeContentNodeString.length() + 1, matchRange.getMinimumInteger());
        Assertions.assertEquals(authorViewContentBeforeContentNodeString.length() + 1 + matchContent.length(),
                matchRange.getMaximumInteger());
    }
}
