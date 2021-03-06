/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.kaufhof.jsonhomeclient

import play.api.libs.json.JsValue
import play.api.libs.ws._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.postfixOps

/**
 * The client to load a json-home document and extract relevant information.
 * A client is responsible for a specific json-home host.
 *
 * @param host the json home host to load the home document from
 * @param ws the WSClient to use for loading the home document.
 *           In a Play 2.3.x application it can be created via `WS.client`,
 *           in other apps it can be created via
 *           `new NingWSClient(new AsyncHttpClientConfig.Builder().build())` (remember
 *           to `close()` the client when the application is stopped).
 * @param defaultHeaders possibility to put headers from app-context
 * @author <a href="mailto:martin.grotzke@inoio.de">Martin Grotzke</a>
 */

class JsonHomeClient(val host: JsonHomeHost,
                     val ws: WSClient,
                     val defaultHeaders: Map[String, String] = Map("Accept" -> "application/json-home")) {

  protected[jsonhomeclient] def jsonHome(): Future[JsValue] = {
    configure(
      ws.url(host.jsonHomeUri.toString)
        .withHeaders(defaultHeaders.toSeq: _*)
    ).get().map(_.json)
  }

  protected def configure(requestHolder: WSRequest): WSRequest = requestHolder

}

