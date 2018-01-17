/*
 * The MIT License (MIT)
 * Copyright (c) 2015 xiaocong@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.uiautomator.stub;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.Until;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Use JUnit test to start the uiautomator jsonrpc server.
 *
 * @author xiaocong@gmail.com
 */
@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class Stub {
    private final String TAG = "UIAUTOMATOR";
    private static final int LAUNCH_TIMEOUT = 5000;

    int PORT = 9008;
    AutomatorHttpServer server = new AutomatorHttpServer(PORT);

    @Before
    public void setUp() throws Exception {
        server.route("/jsonrpc/0", new JsonRpcServer(new ObjectMapper(), new AutomatorServiceImpl(), AutomatorService.class));
        server.start();
    }

    private void launchService() throws RemoteException {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.wakeUp();

        // Wait for launcher
        String launcherPackage = device.getLauncherPackageName();
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        // Launch the app
        Context context = InstrumentationRegistry.getContext();
        final String packageName = "com.github.uiautomator";

//        Useless: Check if service is running
//        if (Helper.isAppRunning(context, packageName)) {
//            Log.i(TAG, "Service is running");
//            System.out.println("UiAutomator service is running");
//            return;
//        }

//        Log.i(TAG, "Launch service");
//        context.startService(new Intent("com.github.uiautomator.ACTION_START"));

//        Log.i(TAG, "Launch " + packageName);
//        final Intent intent = context.getPackageManager()
//                .getLaunchIntentForPackage(packageName);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        context.startActivity(intent);
//
//        device.wait(Until.hasObject(By.pkg(packageName).depth(0)),
//                LAUNCH_TIMEOUT);
//        device.pressHome();
    }

    @After
    public void tearDown() {
        server.stop();
        Context context = InstrumentationRegistry.getContext();
        context.startService(new Intent("com.github.uiautomator.ACTION_STOP"));
    }

    @Test
    @LargeTest
    public void testUIAutomatorStub() throws InterruptedException {
        while (server.isAlive()) {
            Thread.sleep(100);
        }
    }
}