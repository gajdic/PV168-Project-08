package cz.muni.fi.pv168.cashflowevidence.app;

import javax.swing.text.NumberFormatter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Objects;

/**
 * Formatter used for input fields meant for numbers.
 */
public class InputNumberFormatter extends NumberFormatter {

    public InputNumberFormatter(NumberFormat numberFormat) {
        super(numberFormat);
    }

    @Override
    public Object stringToValue(String text) throws ParseException {
        if (Objects.equals(text, "")) {
            return null;
        }

        return super.stringToValue(text);
    }

    @Override
    public void setAllowsInvalid(boolean allowsInvalid) {
        super.setAllowsInvalid(false);
    }

    @Override
    public void setValueClass(Class<?> valueClass) {
        super.setValueClass(Double.class);
    }

    public static NumberFormat getDecimalFormat() {
        NumberFormat decimalFormat = DecimalFormat.getInstance();
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setMaximumFractionDigits(2);

        return decimalFormat;
    }
}
