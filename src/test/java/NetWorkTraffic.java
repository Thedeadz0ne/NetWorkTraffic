import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;

import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetWorkTraffic {

    public WebDriver driver;

    @Test
    public void NetWorkTraffic() {
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/src/test/java/resources/chromedriver.exe");

        ChromeOptions caps = new ChromeOptions();
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.INFO);
        caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
        driver = new ChromeDriver(caps);

        driver.manage().window().maximize();
        driver.get("https://www.google.com/");

        List<LogEntry> entries = driver.manage().logs().get(LogType.PERFORMANCE).getAll();
        System.out.println(entries.size() + " " + LogType.PERFORMANCE + " log entries found");
        int size = 0;
        int count = 0;
        for (LogEntry entry : entries) {
            if (entry.getMessage().contains("Network.loadingFinished")) {
                String pattern = "encodedDataLength\":(\\d+)";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(entry.getMessage());
                count++;
                if (m.find()) {
                    size = Integer.parseInt(m.group(1)) + size;
                }
            }
        }
        System.out.println(size / 1024);
        System.out.println(count);

        driver.quit();


    }
}
