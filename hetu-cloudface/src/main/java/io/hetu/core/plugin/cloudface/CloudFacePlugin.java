/*
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
package io.hetu.core.plugin.cloudface;

//refer to: /opt/openlookeng/resource/hetu-core/presto-geospatial/
//             src/main/java/io/prestosql/plugin/geospatial

import com.google.common.collect.ImmutableSet;
import io.prestosql.spi.Plugin;

import java.util.Set;

public class CloudFacePlugin
        implements Plugin
{
    @Override
    public Set<Class<?>> getFunctions()
    {
        return ImmutableSet.<Class<?>>builder()
                .add(HwcFaceSetFunctions.class)
                .add(HwcFaceFunctions.class)
                .add(HwcFaceRecognitionFunctions.class)
                .add(HelpFunctions.class)
                .build();
    }
}
