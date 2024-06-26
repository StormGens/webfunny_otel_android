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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import io.opentelemetry.sdk.trace.data.SpanData;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class DefaultMemorySpanBufferTest {

    DefaultMemorySpanBuffer backlogProvider = new DefaultMemorySpanBuffer();

    @Test
    void fillFromBacklog_shouldEmptiesBacklog() {
        List<SpanData> spans = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            spans.add(mock(SpanData.class));
        }

        backlogProvider.addAll(spans);
        backlogProvider.drain();

        assertTrue(backlogProvider.isEmpty());
    }
}
