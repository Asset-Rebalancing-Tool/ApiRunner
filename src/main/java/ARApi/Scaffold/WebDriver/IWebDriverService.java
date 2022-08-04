package ARApi.Scaffold.WebDriver;

import java.util.concurrent.Future;

public interface IWebDriverService {
    <T> Future<T> SubmitAction(WebDriverAction<T> webDriverAction);

    <T> Future<T> SubmitAction(WebDriverActionWithUrl<T> webDriverAction, String url);
}
