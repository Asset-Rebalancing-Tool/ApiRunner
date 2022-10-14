package ARApi.Scaffold.WebDriver;

import org.openqa.selenium.WebDriver;

public interface WebDriverActionWithUrl<T> {

    T PerformAction(WebDriver webDriver, String url);
}
