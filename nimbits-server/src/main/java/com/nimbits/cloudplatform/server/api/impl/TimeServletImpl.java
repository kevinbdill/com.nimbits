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

package com.nimbits.cloudplatform.server.api.impl;

import com.nimbits.cloudplatform.client.common.Utils;
import com.nimbits.cloudplatform.client.constants.Const;
import com.nimbits.cloudplatform.client.enums.ClientType;
import com.nimbits.cloudplatform.client.enums.DateFormatType;
import com.nimbits.cloudplatform.client.enums.Parameters;
import com.nimbits.cloudplatform.server.api.ApiServlet;
import com.nimbits.cloudplatform.server.gson.GsonFactory;
import org.springframework.stereotype.Service;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by Benjamin Sautner
 * User: bsautner
 * Date: 3/20/12
 * Time: 9:14 AM
 */

@Service("time")
public class TimeServletImpl extends ApiServlet  implements org.springframework.web.HttpRequestHandler {


    private static final long serialVersionUID = 6160961337851138572L;

    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {




            final PrintWriter out = resp.getWriter();
            DateFormatType type;
            final String format = req.getParameter(Parameters.format.getText());
            final String clientTypeParam = req.getParameter(Parameters.client.getText());
            final ClientType clientType;

            if (! Utils.isEmptyString(clientTypeParam)) {
                clientType = ClientType.valueOf(clientTypeParam);
            }
            else {
                clientType = ClientType.other;
            }

            if (Utils.isEmptyString(format)) {
                type = DateFormatType.unixEpoch;

            }
            else {
                type = DateFormatType.get(format);
            }
            if (type == null) {
                type = DateFormatType.unixEpoch;
            }

            if (clientType.equals(ClientType.arduino)) {

                out.print(Const.CONST_ARDUINO_DATA_SEPARATOR);
            }
            switch (type) {

                case unixEpoch:
                    if (clientType.equals(ClientType.arduino)) {
                        out.print(new Date().getTime() / 1000);
                    }
                    else {
                    out.print(new Date().getTime());
                    }
                    break;

                case json:
                    out.print(GsonFactory.getInstance().toJson(new Date()));
                    break;
            }

        if (clientType.equals(ClientType.arduino)) {

            out.print(Const.CONST_ARDUINO_DATA_SEPARATOR);
        }
        resp.setStatus(HttpServletResponse.SC_OK);

    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
