/*
 * Copyright (c) 2010 Nimbits Inc.
 *
 * http://www.nimbits.com
 *
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/gpl.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the license is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.nimbits.cloudplatform.server.gson;

import com.google.gson.*;
import com.nimbits.cloudplatform.client.model.user.User;
import com.nimbits.cloudplatform.client.model.user.UserModel;

import java.lang.reflect.Type;

/**
 * Created by bsautner
 * User: benjamin
 * Date: 11/10/11
 * Time: 7:11 PM
 */
public class UserDeserializer implements JsonDeserializer<User> {
    @Override
    public User deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        final JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonElement;
        final String json = jsonPrimitive.getAsString();
        return GsonFactory.getInstance().fromJson(json, UserModel.class);


    }
}
