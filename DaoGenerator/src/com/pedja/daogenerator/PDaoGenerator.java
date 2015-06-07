/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pedja.daogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class PDaoGenerator
{

    public static void main(String[] args) throws Exception
    {
        Schema schema = new Schema(1, "com.androidforever.rprccontroller.app");

        addHosts(schema);
        new DaoGenerator().generateAll(schema, "./app/src/main/java");
    }

    private static void addHosts(Schema schema)
    {
        Entity config = schema.addEntity("Host");
        config.setHasKeepSections(true);
        config.addStringProperty("host").primaryKey();
    }

}
