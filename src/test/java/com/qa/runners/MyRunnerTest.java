package com.qa.runners;

import com.qa.base.BaseSetup;
import com.qa.base.ServerManager;
import com.qa.listeners.RetryAnalyzer;
import com.qa.utils.JsonUtils;
import io.cucumber.testng.*;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.*;
import org.testng.xml.XmlTest;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;

@Test
@CucumberOptions(glue = {"com.qa.stepdefinitions"}, features = {"src/test/java/com/qa/features"},
//          dryRun = true,
        tags = "@testing",
        monochrome = true, plugin = {"io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"})

public class MyRunnerTest implements ITest {
    private TestNGCucumberRunner testNGCucumberRunner;
    private ThreadLocal<String> testName = new ThreadLocal<>();

    @BeforeClass
    public void initial(ITestContext context) throws IOException {
        XmlTest currentXmlTest = context.getCurrentXmlTest();
        Objects.requireNonNull(currentXmlTest);
        CucumberPropertiesProvider properties = currentXmlTest::getParameter;
        this.testNGCucumberRunner = new TestNGCucumberRunner(this.getClass(), properties);
        BaseSetup baseSetup = new BaseSetup();
        baseSetup.initializeGlobalParams();
        //log4j2 create folders for each device
        ThreadContext.put("ROUTINGKEY", baseSetup.getPlatformName() + "_" + baseSetup.getDeviceName());
        new ServerManager().startServer();
    }

    @BeforeMethod(alwaysRun = true)
    public void setTestName(Method method, Object[] row) {
        String name = row[0].toString();
        testName.set(name);
    }

    @Override
    public String getTestName() {
        return testName.get();
    }

    @Test(groups = {"cucumber"}, dataProvider = "scenarios", retryAnalyzer = RetryAnalyzer.class)
    public void runScenario(PickleWrapper pickleWrapper, FeatureWrapper featureWrapper) {
        AllureLifecycle lifecycle = Allure.getLifecycle();
        lifecycle.updateTestCase(testResult -> testResult.setName(pickleWrapper.toString()));
        if (!pickleWrapper.getPickle().getTags().contains("@ignore")) {
            this.testNGCucumberRunner.runScenario(pickleWrapper.getPickle());
        } else {
            throw new SkipException("Skipped Test");
        }
    }

    @DataProvider
    public Object[][] scenarios() {
        return this.testNGCucumberRunner == null ? new Object[0][0] : this.testNGCucumberRunner.provideScenarios();
    }

    @AfterClass
    public void afterClass(){
        ServerManager serverManager = new ServerManager();
        if (serverManager.getServer() != null) {
            serverManager.getServer().stop();
        }
        if (this.testNGCucumberRunner != null) {
            this.testNGCucumberRunner.finish();
        }
    }

}