package de.lucaspradel.comunioassistent.common;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import de.lucaspradel.comunioassistent.R;

/**
 * Created by lucas on 01.04.15.
 */
public class IconMapper {

    private static final String ACTIVE = "ACTIVE";
    private static final String INJURED = "INJURED";

    public static Drawable getClubIconForClubId(Context context, int clubId) {
        Resources resources = context.getResources();
        switch (clubId) {
            case 1:
                return resources.getDrawable(R.drawable.club_1);
            case 3:
                return resources.getDrawable(R.drawable.club_3);
            case 4:
                return resources.getDrawable(R.drawable.club_4);
            case 5:
                return resources.getDrawable(R.drawable.club_5);
            case 6:
                return resources.getDrawable(R.drawable.club_6);
            case 7:
                return resources.getDrawable(R.drawable.club_7);
            case 8:
                return resources.getDrawable(R.drawable.club_8);
            case 9:
                return resources.getDrawable(R.drawable.club_9);
            case 10:
                return resources.getDrawable(R.drawable.club_10);
            case 12:
                return resources.getDrawable(R.drawable.club_12);
            case 13:
                return resources.getDrawable(R.drawable.club_13);
            case 14:
                return resources.getDrawable(R.drawable.club_14);
            case 17:
                return resources.getDrawable(R.drawable.club_17);
            case 18:
                return resources.getDrawable(R.drawable.club_18);
            case 21:
                return resources.getDrawable(R.drawable.club_21);
            case 62:
                return resources.getDrawable(R.drawable.club_62);
            case 68:
                return resources.getDrawable(R.drawable.club_68);
            case 81:
                return resources.getDrawable(R.drawable.club_81);
            default:
                return resources.getDrawable(R.drawable.question_mark);
        }
    }

    public static Drawable getStatusIconForStatusCode(Context context, String statusCode) {
        Resources resources = context.getResources();
        if (statusCode == null) {
            return resources.getDrawable(R.drawable.question_mark);
        } else if (statusCode.equalsIgnoreCase(ACTIVE)) {
            return resources.getDrawable(R.drawable.available);
        } else if (statusCode.equalsIgnoreCase(INJURED)) {
            return resources.getDrawable(R.drawable.injured);
        }
        return resources.getDrawable(R.drawable.question_mark);
    }
}
