package ARApi.Scaffold.Shared.Enums;

public enum Currency {
    EUR, USD, UNSUPPORTED;
    public static Currency parse(String text) {
        for (Currency b : Currency.values()) {
            if (b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return Currency.UNSUPPORTED;
    }
}
