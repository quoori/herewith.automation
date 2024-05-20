package com.qa.listeners;

import com.qa.base.BaseSetup;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int count = 0;
    private static int maxTry = 0;
    private static boolean isRetrying = false;

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (!iTestResult.isSuccess()) {                      //Check if test not succeed
            if (count < maxTry) {                            //Check if max try count is reached
                count++;                                     //Increase the maxTry count by 1
                iTestResult.setStatus(ITestResult.FAILURE);  //Mark test as failed
                isRetrying = true;
                return true;                                 //Tells TestNG to re-run the test
            } else {
                iTestResult.setStatus(ITestResult.FAILURE);  //If maxCount reached,test marked as failed
                isRetrying = false;
            }
        } else {
            iTestResult.setStatus(ITestResult.SUCCESS);      //If test passes, TestNG marks it as passed
            isRetrying = false;
        }
        return false;
    }

    public static boolean isRetrying() {
        return isRetrying;
    }
}
