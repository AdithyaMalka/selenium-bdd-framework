package core.driver;

public enum Browser {
    CHROME("chrome"),
    EDGE("edge");

    private final String browserName;

    Browser(String browserName) {
        this.browserName = browserName;
    }

    public String browserName() {
        return browserName;
    }

    public boolean is(String browserType) {
        return this.browserName.equalsIgnoreCase(browserType);
    }
}
