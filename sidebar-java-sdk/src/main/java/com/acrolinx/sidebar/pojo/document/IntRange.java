
package com.acrolinx.sidebar.pojo.document;

import java.io.Serializable;

import org.apache.commons.lang.text.StrBuilder;

@SuppressWarnings("WeakerAccess")
public class IntRange implements Serializable
{
    final int min;
    final int max;

    public IntRange(int number1, int number2)
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

    public String toString()
    {
        StrBuilder buf = new StrBuilder(32);
        buf.append("Range[");
        buf.append(getMinimumInteger());
        buf.append(',');
        buf.append(getMaximumInteger());
        buf.append(']');
        return buf.toString();
    }

}
