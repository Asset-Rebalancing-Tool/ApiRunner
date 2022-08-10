package ARApi.Scaffold.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Modified from stackoverflow.
 * Process strings, compare them / get their fuzzy matching score
 */
@Service
public class StringProcessingService {

    private final Locale locale;

    @Autowired
    public StringProcessingService(LocaleProvider localeProvider) {
        this.locale = localeProvider.GetLocale();
    }

    public boolean Same(final String term, final String query){
        return Process(term).equals(Process(query));
    }

    public String Process(String toBeProcessed){
        return toBeProcessed.trim().toLowerCase(locale);
    }

    public boolean Contains(final String term, final String query){
        var termProcessed = Process(term);
        var queryProcessed = Process(query);

        return termProcessed.contains(queryProcessed) || queryProcessed.contains(termProcessed);
    }

    public Integer fuzzyScore(final CharSequence term, final CharSequence query) {

        if (term == null || query == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

        // fuzzy logic is case insensitive. We normalize the Strings to lower
        // case right from the start. Turning characters to lower case
        // via Character.toLowerCase(char) is unfortunately insufficient
        // as it does not accept a locale.
        final String termLowerCase = term.toString().toLowerCase(locale).trim();
        final String queryLowerCase = query.toString().toLowerCase(locale).trim();

        // the resulting score
        int score = 0;

        // the position in the term which will be scanned next for potential
        // query character matches
        int termIndex = 0;

        // index of the previously matched character in the term
        int previousMatchingCharacterIndex = Integer.MIN_VALUE;

        for (int queryIndex = 0; queryIndex < queryLowerCase.length(); queryIndex++) {
            final char queryChar = queryLowerCase.charAt(queryIndex);

            boolean termCharacterMatchFound = false;
            for (; termIndex < termLowerCase.length()
                    && !termCharacterMatchFound; termIndex++) {
                final char termChar = termLowerCase.charAt(termIndex);

                if (queryChar == termChar) {
                    // simple character matches result in one point
                    score++;

                    // subsequent character matches further improve
                    // the score.
                    if (previousMatchingCharacterIndex + 1 == termIndex) {
                        score += 2;
                    }

                    previousMatchingCharacterIndex = termIndex;

                    // we can leave the nested loop. Every character in the
                    // query can match at most one character in the term.
                    termCharacterMatchFound = true;
                }
            }
        }

        return score;
    }

    public Locale getLocale() {
        return locale;
    }

}