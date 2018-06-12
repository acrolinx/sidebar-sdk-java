
package com.acrolinx.sidebar.pojo.document;

import java.io.Serializable;

import org.apache.commons.lang.text.StrBuilder;

@SuppressWarnings("WeakerAccess")
public class IntRange implements Serializable
{
    private static final long serialVersionUID = -2808883234003742075L;
    final int min;
    final int max;

    public IntRange(final int number1, final int number2)
    {
        if (number2 < number1) {
            this.min = number2;
            this.max = number1;
        } else {
            this.min = number1;
            this.max = number2;
        }
    }

    public int getMinimumInteger()
    {
        return min;
    }

    public int getMaximumInteger()
    {
        return max;
    }

    @Override
    public String toString()
    {
        final StrBuilder buf = new StrBuilder(32);
        buf.append("Range[");
        buf.append(getMinimumInteger());
        buf.append(',');
        buf.append(getMaximumInteger());
        buf.append(']');
        return buf.toString();
    }

}
