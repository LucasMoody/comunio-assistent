package lucaspradel.de.comunioassistent.dailytransfermarket.view;

import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;

/**
 * Created by lucas on 03.04.15.
 */
public class ExtendedAxisValueFormatter extends SimpleAxisValueFormatter {

    @Override
    public int formatValueForAutoGeneratedAxis(char[] formattedValue, float value, int autoDecimalDigits) {
        return super.formatValueForAutoGeneratedAxis(formattedValue, value / 1000f, autoDecimalDigits);
    }
}
