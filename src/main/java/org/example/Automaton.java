package org.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Automaton {
    Pattern[] afd = new Pattern[9];

    public Automaton() {
        this.afd[0] = Pattern.compile("\\s+");
        this.afd[1] = Pattern.compile("([A-Za-z]|_)([A-Za-z]|_|[0-9])*");
        this.afd[2] = Pattern.compile("[\\+\\-\\*/]=|=");
        this.afd[3] = Pattern.compile("[\\+\\-\\*/]");
        this.afd[8] = Pattern.compile("[0-9]+");
        this.afd[4] = Pattern.compile("[0-9]+\\.[0-9]+");
        this.afd[7] = Pattern.compile("[0-9]+\\.");
        this.afd[5] = Pattern.compile("\\(|\\)");
        this.afd[6] = Pattern.compile("\\;");
    }

    public boolean recognize(String text, int iniToken, int[] i, int noAuto) {
        Matcher m = this.afd[noAuto].matcher(text);
        if (m.find(iniToken)) {
            if (m.start() == iniToken) {
                i[0] = m.end();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
