/*
 * Copyright Webfunny Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webfunny.rum;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanId;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.sdk.testing.junit5.OpenTelemetryExtension;
import io.opentelemetry.sdk.trace.data.SpanData;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

class WorkflowTest {

    @RegisterExtension final OpenTelemetryExtension otelTesting = OpenTelemetryExtension.create();
    private Tracer tracer;

    @BeforeEach
    void setup() {
        tracer = otelTesting.getOpenTelemetry().getTracer("testTracer");
    }

    @Test
    void createAndEnd() {
        Span workflowTimer =
                tracer.spanBuilder("workflow")
                        .setAttribute(WebfunnyRum.WORKFLOW_NAME_KEY, "workflow")
                        .startSpan();
        Span inner = tracer.spanBuilder("foo").startSpan();
        try (Scope scope = inner.makeCurrent()) {
            // do nothing
        } finally {
            inner.end();
        }
        workflowTimer.end();

        List<SpanData> spans = otelTesting.getSpans();
        assertEquals(2, spans.size());
        // verify we're not trying to do any propagation of the context here.
        assertEquals(spans.get(0).getParentSpanId(), SpanId.getInvalid());
        assertEquals(spans.get(0).getName(), "foo");
        assertEquals(spans.get(1).getName(), "workflow");
    }
}
