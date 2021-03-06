/*
 * Copyright (c) 2013 Nimbits Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either expressed or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.nimbits.cloudplatform;


import com.nimbits.cloudplatform.client.android.AndroidControl;
import com.nimbits.cloudplatform.client.android.AndroidControlFactory;
import com.nimbits.cloudplatform.client.model.entity.Entity;
import com.nimbits.cloudplatform.client.model.location.Location;
import com.nimbits.cloudplatform.client.model.user.User;
import com.nimbits.cloudplatform.http.UrlContainer;
import com.nimbits.cloudplatform.transaction.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: Benjamin Sautner
 * Date: 1/16/13
 * Time: 3:57 PM
 */
public class Nimbits {


    public static UrlContainer base;
    public static User session;


    public static List<Entity> tree;
    public static Location location;

    public static LoginListener listener;
    public static List<String> authKey;
    private static AndroidControl control;

    private static String apiKey;

    static {

        authKey = Collections.emptyList();
    }

    public static String email;

    public static AndroidControl getControl() {
        return control == null ? AndroidControlFactory.getConservativeInstance() : control;
    }

    public static void setControl(AndroidControl control) {
        Nimbits.control = control;
    }

    public static void setLoginListener(LoginListener aListener) {
        listener = aListener;
    }

    public static void setApiKey(String apiKey) {
        Nimbits.apiKey = apiKey;
    }

    public static String getApiKey() {
        return apiKey;
    }

    public interface LoginListener {
        void loginSuccess(User session);

        void loginFail(String reason);

    }




    public static void loginWithKey(final String instanceUrl, final String email, final String key) {
        new Thread(new Runnable() {
            public void run() {

                base = (UrlContainer.getInstance(instanceUrl));


                List<User> sample;

                sample = Transaction.getSession(email, key);
                if (!sample.isEmpty()) {
                    User user = sample.get(0);
                    session = (user);
                    authKey = new ArrayList<String>(1);
                    authKey.add(key);
                    listener.loginSuccess(user);
                } else {
                    listener.loginFail("You authenticated ok, but we couldn't get your session");
                }


            }

        }).start();


    }


}
