package org.fuwt.examples;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.regex.Pattern;

/**
 * User: chris
 * Date: 5/15/11
 * Time: 1:16 PM
 */
public class RegExMatcher extends BaseMatcher<String>
{

    private final String regexPattern;
    private final Pattern pattern;

    public RegExMatcher(final String regexPattern)
    {
        this.regexPattern = regexPattern;
        this.pattern = Pattern.compile(regexPattern);
    }

    public RegExMatcher(final String regexPattern, int regexFlags)
    {
        this.regexPattern = regexPattern;
        this.pattern = Pattern.compile(regexPattern,regexFlags);
    }

    public boolean matches(final Object item)
    {
        return pattern.matcher(String.valueOf(item)).find();
    }

    public void describeTo(final Description description)
    {
        description.appendText("matches regex=" + regexPattern);
    }

    public static RegExMatcher matches(String regex){
        return new RegExMatcher(regex);
    }

    public static RegExMatcher matches(String regex, int regexFlags){
        return new RegExMatcher(regex,regexFlags);
    }
}
