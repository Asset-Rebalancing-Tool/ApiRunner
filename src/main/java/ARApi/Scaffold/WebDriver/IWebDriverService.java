package ARApi.Scaffold.WebDriver;

import java.util.concurrent.Future;

/**
 * The idea is to not expose the webdriver directly to the outside world,
 * but to take tasks for the drivers, execute them and return the results.
 */
public interface IWebDriverService {

    <T> Future<T> SubmitAction(WebDriverAction<T> webDriverAction);

    <T> Future<T> SubmitAction(WebDriverActionWithUrl<T> webDriverAction, String url);
}
