//package com.qa.listeners;
//
//import org.testng.IAlterSuiteListener;
//import org.testng.collections.Maps;
//import org.testng.xml.XmlClass;
//import org.testng.xml.XmlSuite;
//import org.testng.xml.XmlTest;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class suiteListener implements IAlterSuiteListener {
//        @Override
//        public void alter(List<XmlSuite> suites) {
//            XmlSuite suite = suites.get(0);
//            //Check if there was a parameter named "browserFlavors" defined at the suite
//            String browserFlavors = suite.getParameter("browserFlavors");
//            if (browserFlavors == null || browserFlavors.trim().isEmpty()) {
//                //If no such parameter was found, then Try querying the JVM arguments to see if it contains
//                //value for it. Just to ensure we don't end up in a situation wherein there's no JVM also provided
//                //Lets add a default value for the JVM argument which in our case is "firefox"
//                browserFlavors = System.getProperty("browserFlavors", "firefox");
//            }
//            String[] browsers = browserFlavors.split(",");
//            List<XmlTest> xmlTests = new ArrayList<>();
//            for (String browser : browsers) {
//                XmlTest xmlTest = new XmlTest(suite);
//                xmlTest.setName(browser + "_test");
//                Map<String, String> parameters = Maps.newHashMap();
//                parameters.put("browser", browser);
//                xmlTest.setParameters(parameters);
//                XmlClass xmlClass = new XmlClass();
//                xmlClass.setName("Test");
//                xmlTest.getClasses().add(xmlClass);
//                xmlTests.add(xmlTest);
//            }
//            suite.setTests(xmlTests);
//        }
//    }
