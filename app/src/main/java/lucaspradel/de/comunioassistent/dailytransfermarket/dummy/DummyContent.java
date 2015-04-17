package lucaspradel.de.comunioassistent.dailytransfermarket.dummy;

import android.support.v4.util.Pair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lucaspradel.de.comunioassistent.dailytransfermarket.helper.PlayerInfo;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<PlayerInfo> ITEMS = new ArrayList<PlayerInfo>();
    public static List<Pair<Date, Integer>> MARKETVALUES = new ArrayList<>();

    static {
        // Add 3 sample items.
        try {
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-05"),2510000));
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-06"),2440000));
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-07"),2400000));
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-08"),2510000));
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-09"),2370000));
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-10"),2270000));
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-11"),2150000));
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-12"),2510000));
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-13"),2810000));
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-14"),2910000));
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-15"),2310000));
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-16"),2210000));
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-17"),2310000));
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-18"),2610000));
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-19"),3210000));
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-20"),4010000));
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-21"),4110000));
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-22"),2510000));
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-23"),2110000));
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-24"),2010000));
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-25"),1010000));
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-26"),2510000));
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-27"),2510000));
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-28"),2510000));
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-29"),2510000));
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-30"),2510000));
            MARKETVALUES.add(new Pair<Date, Integer>(new SimpleDateFormat("yyyy-MM-dd").parse("2014-10-31"),2510000));
        } catch (Exception e) {
            //
        }
        ITEMS.add(new PlayerInfo("31982", "R. Kruse", 0, 8, 160000, 160000, "INJURED", "komplexe Bänderverletzung", "striker", MARKETVALUES));
        ITEMS.add(new PlayerInfo("27634", "R. Adler", 8, 4, 530000, 530000, "ACTIVE", "", "keeper", MARKETVALUES));
        ITEMS.add(new PlayerInfo("27634", "R. Adler", 8, 4, 530000, 530000, "ACTIVE", "", "keeper", MARKETVALUES));
        ITEMS.add(new PlayerInfo("27634", "R. Adler", 8, 4, 530000, 530000, "ACTIVE", "", "keeper", MARKETVALUES));
    }

}
