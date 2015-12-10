/*
 * Copyright 2015 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wildfly.migration.wfly10.subsystem;

import org.wildfly.migration.core.ServerMigrationContext;
import org.wildfly.migration.core.logger.ServerMigrationLogger;
import org.wildfly.migration.wfly10.standalone.WildFly10StandaloneServer;

import java.io.IOException;

/**
 * @author emmartins
 */
public class LegacyWildFlySubsystem extends BasicWildFly10Subsystem {

    public LegacyWildFlySubsystem(String name, WildFly10Extension extension) {
        super(name, extension);
    }

    @Override
    public void migrate(WildFly10StandaloneServer server, ServerMigrationContext context) throws IOException {
        super.migrate(server, context);
        server.migrateSubsystem(getName());
        postMigrate(server, context);
        // FIXME tmp workaround for legacy subsystems which do not remove itself
        if (server.getSubsystems().contains(getName())) {
            // remove itself after migration
            server.removeSubsystem(getName());
            ServerMigrationLogger.ROOT_LOGGER.infof("Legacy subsystem %s removed since still existed after migration.", getName());
        }
    }

    protected void postMigrate(WildFly10StandaloneServer server, ServerMigrationContext context) throws IOException {

    }
}