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
import java.util.Collection;

/**
 * This is a test set for <a href="http://jira.codehaus.org/browse/MNG-820">MNG-820</a>.
 * 
 * @author Brett Porter
 * @version $Id$
 */
public class MavenITmng0820ConflictResolutionTest
    extends AbstractMavenIntegrationTestCase
{
    protected MavenITmng0820ConflictResolutionTest()
    {
        super( ALL_MAVEN_VERSIONS );
    }

    /**
     * Verify that the collector selecting a particular version gets the correct subtree
     */
    public void testitMNG0820()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-0820" );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.deleteArtifacts( "org.apache.maven.its.mng0820" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        Collection artifacts = verifier.loadLines( "target/artifacts.txt", "UTF-8" );
        assertEquals( 3, artifacts.size() );
        assertTrue( artifacts.toString(), artifacts.contains( "org.apache.maven.its.mng0820:d:jar:2.0" ) );
        assertTrue( artifacts.toString(), artifacts.contains( "org.apache.maven.its.mng0820:c:jar:1.4" ) );
        assertTrue( artifacts.toString(), artifacts.contains( "org.apache.maven.its.mng0820:a:jar:1.0" ) );
        assertFalse( artifacts.toString(), artifacts.contains( "org.apache.maven.its.mng0505:b:jar:1.0" ) );
    }

}
