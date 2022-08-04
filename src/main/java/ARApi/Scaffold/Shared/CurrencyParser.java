package ARApi.Scaffold.Shared;

public class CurrencyParser {
    public static Currency parse(String text) {
        for (Currency b : Currency.values()) {
            if (b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return Currency.UNSUPPORTED;
    }
}
