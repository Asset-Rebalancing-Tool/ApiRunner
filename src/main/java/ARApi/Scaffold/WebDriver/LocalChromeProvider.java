package ARApi.Scaffold.WebDriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;


public class LocalChromeProvider implements IWebDriverService {

    @Override
    public <T> Future<T> SubmitAction(WebDriverAction<T> webDriverAction){
        return exe.submit(() ->{
            var webDriver = GetDriver();
            var result = webDriverAction.PerformAction(webDriver);
            webDriverWorkerQueue.add(webDriver);
            return result;
        });
    }

    @Override
    public <T> Future<T> SubmitAction(WebDriverActionWithUrl<T> webDriverAction, String url){
        return exe.submit(() ->{
            var webDriver = GetDriver();
            var result = webDriverAction.PerformAction(webDriver, url);
            webDriverWorkerQueue.add(webDriver);
            return result;
        });
    }

    private WebDriver GetDriver(){
        var webDriver = webDriverWorkerQueue.peek();
        if(webDriver != null){
            return webDriver;
        }
        return GetChromeDriver();
    }

    // limit to 3 browsers for now, since we dont have proxy yet
    private final ExecutorService exe = Executors.newFixedThreadPool(3);

    private final BlockingQueue<WebDriver> webDriverWorkerQueue = new LinkedBlockingQueue<>();

    private static WebDriver GetChromeDriver(){
        System.setProperty("webdriver.chrome.driver", "F:\\priv\\AssetRebalancerApi\\drivers\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        //chromeOptions.addArguments("--headless");
        return new ChromeDriver(chromeOptions);
    }


}
