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

package com.nimbits.cloudplatform.server.process.task;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.nimbits.cloudplatform.client.enums.Action;
import com.nimbits.cloudplatform.client.enums.Parameters;
import com.nimbits.cloudplatform.client.model.entity.Entity;
import com.nimbits.cloudplatform.client.model.location.Location;
import com.nimbits.cloudplatform.client.model.point.Point;
import com.nimbits.cloudplatform.client.model.timespan.Timespan;
import com.nimbits.cloudplatform.client.model.user.User;
import com.nimbits.cloudplatform.client.model.value.Value;
import com.nimbits.cloudplatform.server.gson.GsonFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;


/**
 * Created by bsautner
 * User: benjamin
 * Date: 10/7/11
 * Time: 2:12 PM
 */
@Component("TaskImpl")
public class TaskImpl   {

    private static final String IN_CONTENT = "inContent";
    private static final String QUEUE_DELETE_BLOB= "blob";

    private static final String DEFAULT = "default";
    private static final String PATH_DELETE_BLOB_TASK = "/task/deleteBlobTask";
    private static final String PATH_POINT_MAINT_TASK = "/task/pointTask";
    private static final String PATH_MOVE_TASK = "/task/moveTask";
    private static final String PATH_TASK_RECORD_VALUE = "/task/valueTask";
    private static final String PATH_TASK_DUMP_TASK= "/task/dumpTask";
    private static final String PATH_TASK_UPLOAD_TASK= "/task/uploadTask";
    private static final String PATH_TASK_PROCESS_BATCH = "/task/batchTask";
    private static final String PATH_INCOMING_MAIL_QUEUE = "/task/mailTask";
    private static final String PATH_DELETE_DATA_TASK = "/task/deleteTask";
    private static final Logger log = Logger.getLogger(TaskImpl.class.getName());
    private static final String DUMP = "dump";


    public TaskImpl() {




    }

    
    public  static void startDeleteDataTask(final Entity point,
                                    final boolean onlyExpired,
                                    final int exp) {



            final Queue queue =  QueueFactory.getQueue(DEFAULT);
            if (onlyExpired) {
                queue.add(TaskOptions.Builder.withUrl(PATH_DELETE_DATA_TASK)
                        .param(Parameters.json.getText(),  GsonFactory.getInstance().toJson(point))
                        .param(Parameters.exp.getText(), Long.toString(exp))

                );
            } else {
                queue.add(TaskOptions.Builder.withUrl(PATH_DELETE_DATA_TASK)
                        .param(Parameters.json.getText(),  GsonFactory.getInstance().toJson(point))
                );
            }



    }

    
    public static  void startDeleteBlobTask(final BlobKey key ) {

        try {
            final Queue queue =  QueueFactory.getQueue( QUEUE_DELETE_BLOB  );

            queue.add(TaskOptions.Builder.withUrl(PATH_DELETE_BLOB_TASK)
                    .param(Parameters.key.getText(), key.getKeyString())
            );
        } catch (IllegalStateException e) {
            final Queue queue =  QueueFactory.getQueue( DEFAULT  );

            queue.add(TaskOptions.Builder.withUrl(PATH_DELETE_BLOB_TASK)
                    .param(Parameters.key.getText(), key.getKeyString())
            );
        }
    }



    
    public static  void startDataDumpTask(final Entity entity, final Timespan timespan) {
        final Queue queue =  QueueFactory.getQueue(DUMP);
        final String json = GsonFactory.getInstance().toJson(entity);
        queue.add(TaskOptions.Builder.withUrl(PATH_TASK_DUMP_TASK)
                .param(Parameters.entity.getText(), json)
                .param(Parameters.sd.getText(),
                        String.valueOf(timespan.getStart().getTime()))
                .param(Parameters.ed.getText(),
                        String.valueOf(timespan.getEnd().getTime()))
        );
    }

    
    public static  void startUploadTask(final User user, final Point entity, final BlobKey blobKey) {
        final Queue queue =  QueueFactory.getQueue(DUMP);
        final String json = GsonFactory.getInstance().toJson(entity);
        final String userJson = GsonFactory.getInstance().toJson(user);
        queue.add(TaskOptions.Builder.withUrl(PATH_TASK_UPLOAD_TASK)
                .param(Parameters.entity.getText(), json)
                .param(Parameters.user.getText(), userJson)
                .param(Parameters.blobkey.getText(), blobKey.getKeyString())
        );
    }

    
    public static  void startProcessBatchTask(final User user, final HttpServletRequest req, final HttpServletResponse resp)  {


            final Queue queue =  QueueFactory.getQueue(DEFAULT);



            final String userJson = GsonFactory.getInstance().toJson(user);

            log.info(userJson);

            final TaskOptions options = TaskOptions.Builder.withUrl(PATH_TASK_PROCESS_BATCH);

            final Enumeration enumeration = req.getParameterNames();
            final Map m = req.getParameterMap();

            while (enumeration.hasMoreElements()) {
                final String param = enumeration.nextElement().toString();
                final String value = ((String[]) m.get(param))[0];
                options.param(param, value);
            }

            options.param(Parameters.pointUser.getText(), userJson);

            queue.add(options);



    }

    
    public static  void startRecordValueTask(final User u, final Entity point, final Value value) {

            if (Double.valueOf(value.getDoubleValue()).isInfinite()) {
                return;
            }
            final Queue queue =  QueueFactory.getQueue(DEFAULT);
            final String userJson = GsonFactory.getInstance().toJson(u);
            final String pointJson = GsonFactory.getInstance().toJson(point);
            final String valueJson = GsonFactory.getInstance().toJson(value);

            queue.add(TaskOptions.Builder
                    .withUrl(PATH_TASK_RECORD_VALUE).taskName(UUID.randomUUID().toString())
                    .param(Parameters.pointUser.getText(), userJson)
                    .param(Parameters.pointJson.getText(), pointJson)
                    .param(Parameters.valueJson.getText(), valueJson));

    }

    
    public static  void startIncomingMailTask(final String fromAddress, final String inContent) {

        final Queue queue =  QueueFactory.getQueue(DEFAULT);
        queue.add(TaskOptions.Builder.withUrl(PATH_INCOMING_MAIL_QUEUE)
                .param(Parameters.fromAddress.getText(), fromAddress)
                .param(IN_CONTENT, inContent));


    }

    
    public  static void startPointMaintTask(final Entity e) {

        final String json = GsonFactory.getInstance().toJson(e);

        final Queue queue =  QueueFactory.getQueue( DEFAULT);

        queue.add(TaskOptions.Builder.withUrl(PATH_POINT_MAINT_TASK)
                .param(Parameters.json.getText(), json));

    }



    public static void startMoveCachedValuesToStoreTask(final Entity point) {
        final String json = GsonFactory.getInstance().toJson(point);

        final Queue queue =  QueueFactory.getQueue(DEFAULT);

        queue.add(TaskOptions.Builder.withUrl(PATH_MOVE_TASK)
                .param(Parameters.point.getText(), json));
    }


}
