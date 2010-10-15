/*
 * Copyright 2010 the original author or authors.
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
package org.gradle.api.tasks.diagnostics.internal;

import org.gradle.api.Project;
import org.gradle.logging.internal.StreamingStyledTextOutput;
import org.gradle.logging.internal.TestStyledTextOutput;
import org.gradle.util.TemporaryFolder;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

import static org.gradle.util.Matchers.containsLine;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(JMock.class)
public class TextReportRendererTest {
    private final JUnit4Mockery context = new JUnit4Mockery();
    @Rule
    public final TemporaryFolder testDir = new TemporaryFolder();
    private final TextReportRenderer renderer = new TextReportRenderer();

    @Test
    public void writesReportToAFile() throws IOException {
        File outFile = new File(testDir.getDir(), "report.txt");
        renderer.setOutputFile(outFile);
        assertThat(renderer.getTextOutput(), instanceOf(StreamingStyledTextOutput.class));

        renderer.complete();

        assertTrue(outFile.isFile());
        assertThat(renderer.getTextOutput(), nullValue());
    }

    @Test
    public void writeRootProjectHeader() throws IOException {
        final Project project = context.mock(Project.class);
        TestStyledTextOutput textOutput = new TestStyledTextOutput();

        context.checking(new Expectations() {{
            allowing(project).getRootProject();
            will(returnValue(project));
        }});

        renderer.setOutput(textOutput);
        renderer.startProject(project);
        renderer.completeProject(project);
        renderer.complete();

        assertThat(textOutput.toString(), containsLine("Root Project"));
    }
    
    @Test
    public void writeSubProjectHeader() throws IOException {
        final Project project = context.mock(Project.class);
        TestStyledTextOutput textOutput = new TestStyledTextOutput();

        context.checking(new Expectations() {{
            allowing(project).getRootProject();
            will(returnValue(context.mock(Project.class, "root")));
            allowing(project).getPath();
            will(returnValue("<path>"));
        }});

        renderer.setOutput(textOutput);
        renderer.startProject(project);
        renderer.completeProject(project);
        renderer.complete();

        assertThat(textOutput.toString(), containsLine("Project <path>"));
    }
}
