/*
 * Copyright 2018 Google LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package hack.chat.data;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class DataService {

  private static final Gson gson = new Gson();

  public static void main(String[] args) {
    DataService dataService = new DataService();

    get("/get", (request, response) -> {
      List<Message> messages = dataService.messageStore.get();

      return ImmutableMap.of("messages", messages);
    }, gson::toJson);

    post("/add", (request, response) -> {
      class RequestTemplate {

        private String name;
        private String msg;
      }
      RequestTemplate requestTemplate = gson.fromJson(request.body(), RequestTemplate.class);
      if (requestTemplate == null) {
        return ImmutableMap.of("error", "invalid request : " + request.body());
      }

      dataService.messageStore.add(new Message(requestTemplate.name, requestTemplate.msg));

      return new Object();
    }, gson::toJson);

    System.out.println("I am running");
  }

  private final MessageStore messageStore = new MessageStore();

  private DataService() {}
}
