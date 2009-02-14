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
 * This is a test set for <a href="http://jira.codehaus.org/browse/MNG-95">MNG-95</a>.
 * 
 * @author John Casey
 * @version $Id$
 */
public class MavenITmng0095ReactorFailureBehaviorTest
    extends AbstractMavenIntegrationTestCase
{
    protected MavenITmng0095ReactorFailureBehaviorTest()
    {
        super( ALL_MAVEN_VERSIONS );
    }

    /**
     * Test fail-fast reactor behavior. Forces an exception to be thrown in
     * the first module and checks that the second module is not built and the overall build fails, too.
     */
    public void testitFailFast()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-0095" );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.deleteDirectory( "subproject/target" );
        verifier.deleteDirectory( "subproject2/target" );
        verifier.getCliOptions().add( "--fail-fast" );
        verifier.setLogFileName( "log-ff.txt" );
        try
        {
            verifier.executeGoal( "org.apache.maven.its.plugins:maven-it-plugin-touch:touch" );
            verifier.verifyErrorFreeLog();
        }
        catch ( VerificationException e )
        {
            // expected
        }
        verifier.resetStreams();

        verifier.assertFilePresent( "target/touch.txt" );
        verifier.assertFileNotPresent( "subproject/target/touch.txt" );
        verifier.assertFileNotPresent( "subproject2/target/touch.txt" );
    }

    /**
     * Test fail-never reactor behavior. Forces an exception to be thrown in
     * the first module, but checks that the second module is built and the overall build succeeds.
     */
    public void testitFailNever()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-0095" );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.deleteDirectory( "subproject/target" );
        verifier.deleteDirectory( "subproject2/target" );
        verifier.getCliOptions().add( "--fail-never" );
        verifier.setLogFileName( "log-fn.txt" );
        verifier.executeGoal( "org.apache.maven.its.plugins:maven-it-plugin-touch:touch" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        verifier.assertFilePresent( "target/touch.txt" );
        verifier.assertFileNotPresent( "subproject/target/touch.txt" );
        verifier.assertFilePresent( "subproject2/target/touch.txt" );
    }

    /**
     * Test fail-at-end reactor behavior. Forces an exception to be thrown in
     * the first module and checks that the second module is still built but the overall build finally fails.
     */
    public void testitFailAtEnd()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-0095" );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.deleteDirectory( "subproject/target" );
        verifier.deleteDirectory( "subproject2/target" );
        verifier.getCliOptions().add( "--fail-at-end" );
        verifier.setLogFileName( "log-fae.txt" );
        try
        {
            verifier.executeGoal( "org.apache.maven.its.plugins:maven-it-plugin-touch:touch" );
            verifier.verifyErrorFreeLog();
        }
        catch ( VerificationException e )
        {
            // expected
        }
        verifier.resetStreams();

        verifier.assertFilePresent( "target/touch.txt" );
        verifier.assertFileNotPresent( "subproject/target/touch.txt" );
        verifier.assertFilePresent( "subproject2/target/touch.txt" );
    }

}
