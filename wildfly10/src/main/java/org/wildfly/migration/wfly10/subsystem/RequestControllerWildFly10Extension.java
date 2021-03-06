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

import org.jboss.as.controller.operations.common.Util;
import org.jboss.dmr.ModelNode;
import org.wildfly.migration.core.ServerMigrationContext;
import org.wildfly.migration.core.logger.ServerMigrationLogger;
import org.wildfly.migration.wfly10.standalone.WildFly10StandaloneServer;

import java.io.IOException;

import static org.jboss.as.controller.PathAddress.pathAddress;
import static org.jboss.as.controller.PathElement.pathElement;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.*;

/**
 * @author emmartins
 */
public class RequestControllerWildFly10Extension extends WildFly10Extension {

    private static final String EXTENSION_NAME = "org.wildfly.extension.request-controller";
    public static final RequestControllerWildFly10Extension INSTANCE = new RequestControllerWildFly10Extension();

    private RequestControllerWildFly10Extension() {
        super(EXTENSION_NAME);
        subsystems.add(new RequestControllerWildFly10Subsystem(this));
    }

    private static class RequestControllerWildFly10Subsystem extends BasicWildFly10Subsystem {
        private static final String SUBSYSTEM_NAME = "request-controller";

        private RequestControllerWildFly10Subsystem(RequestControllerWildFly10Extension extension) {
            super(SUBSYSTEM_NAME, extension);
        }

        @Override
        public void migrate(WildFly10StandaloneServer server, ServerMigrationContext context) throws IOException {
            super.migrate(server, context);
            migrateConfig(server.getSubsystem(getName()), server, context);
        }

        protected void migrateConfig(ModelNode config, WildFly10StandaloneServer server, ServerMigrationContext context) throws IOException {
            if (config != null) {
                return;
            }
            final String extensionName = getExtension().getName();
            if (!server.getExtensions().contains(extensionName)) {
                ServerMigrationLogger.ROOT_LOGGER.debugf("Adding Extension %s...", extensionName);
                final ModelNode op = Util.createAddOperation(pathAddress(pathElement(EXTENSION, extensionName)));
                op.get(MODULE).set(extensionName);
                server.executeManagementOperation(op);
                ServerMigrationLogger.ROOT_LOGGER.infof("Extension %s added.",extensionName);
            }
            ServerMigrationLogger.ROOT_LOGGER.debugf("Adding subsystem %s...", getName());
            final ModelNode op = Util.createAddOperation(pathAddress(pathElement(SUBSYSTEM, getName())));
            server.executeManagementOperation(op);
            ServerMigrationLogger.ROOT_LOGGER.infof("Subsystem %s added.", getName());
        }
    }
}