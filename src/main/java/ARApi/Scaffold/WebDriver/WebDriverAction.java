package ARApi.Scaffold.WebDriver;

import org.openqa.selenium.WebDriver;

public interface WebDriverAction<T> {

    T PerformAction(WebDriver webDriver);
}
