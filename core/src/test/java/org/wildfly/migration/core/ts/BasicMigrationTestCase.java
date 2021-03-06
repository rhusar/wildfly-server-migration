/*
 * Copyright 2016 Red Hat, Inc.
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
package org.wildfly.migration.core.ts;

import org.junit.Test;
import org.wildfly.migration.core.ServerMigration;

import java.io.IOException;

/**
 * @author emmartins
 */
public class BasicMigrationTestCase {

    @Test
    public void testSupportedMigration() throws IOException {
        new ServerMigration().from(TestSourceServerProvider.SERVER.getBaseDir()).to(TestTargetServerProvider.SERVER.getBaseDir()).run();
    }

    @Test
    public void testUnsupportedMigration() throws IOException {
        try {
            new ServerMigration().to(TestSourceServerProvider.SERVER.getBaseDir()).from(TestTargetServerProvider.SERVER.getBaseDir()).run();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}
