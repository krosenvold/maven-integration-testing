package org.apache.maven.it;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

import java.io.File;

/**
 * This is a test set for <a href="http://jira.codehaus.org/browse/MNG-2771">MNG-2771</a>
 * 
 * @author Mark Hobson
 * @version $Id$
 */
public class MavenITmng2771PomExtensionComponentOverrideTest
    extends AbstractMavenIntegrationTestCase
{
    protected MavenITmng2771PomExtensionComponentOverrideTest()
    {
        super( ALL_MAVEN_VERSIONS );
    }

    /**
     * Test that ensures the POM extensions can override default component implementations.
     */
    public void testitMNG2771()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-2771/extension" );
        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        verifier.executeGoal( "install" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-2771/plugin" );
        verifier = new Verifier( testDir.getAbsolutePath() );
        verifier.executeGoal( "install" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-2771/project" );
        verifier = new Verifier( testDir.getAbsolutePath() );
        verifier.executeGoal( "verify" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}
