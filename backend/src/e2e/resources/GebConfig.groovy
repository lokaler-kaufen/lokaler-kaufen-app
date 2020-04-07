import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver

waiting {
    timeout = 5
}

environments {
    // run via “./gradlew chromeTest”
    chrome {
        driver = { new ChromeDriver() }
    }

    // run via “./gradlew chromeHeadlessTest”
    chromeHeadless {
        driver = {
            ChromeOptions o = new ChromeOptions()
            o.addArguments('headless')
            new ChromeDriver(o)
        }
    }

    // run via “./gradlew firefoxTest”
    firefox {
        atCheckWaiting = 1
        driver = { new FirefoxDriver() }
    }
}

// specify base URL via -Dgeb.build.baseUrl=http://localhost:4200
//