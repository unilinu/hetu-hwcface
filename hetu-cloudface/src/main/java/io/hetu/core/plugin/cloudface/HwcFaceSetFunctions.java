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

import com.huaweicloud.frs.client.param.AuthInfo;
import com.huaweicloud.frs.client.result.GetAllFaceSetsResult;
import com.huaweicloud.frs.client.result.GetFaceSetResult;
import com.huaweicloud.frs.client.result.common.FaceSet;
import com.huaweicloud.frs.client.service.FrsClient;
import com.huaweicloud.frs.common.FrsException;
import io.airlift.slice.DynamicSliceOutput;
import io.airlift.slice.Slice;
import io.prestosql.spi.function.Description;
import io.prestosql.spi.function.ScalarFunction;
import io.prestosql.spi.function.SqlType;
import io.prestosql.spi.type.StandardTypes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HwcFaceSetFunctions
        extends HwcAuthInfo
{
    private HwcFaceSetFunctions() {}

    @ScalarFunction("NewFaceSet")
    @Description("New a <para1: face_set>")
    @SqlType(StandardTypes.INTEGER)
    public static long newFaceSet(@SqlType(StandardTypes.VARCHAR) Slice faceset)
    {
        AuthInfo authInfo = new AuthInfo(endpoint, region, ak, sk);
        FrsClient frsClient = new FrsClient(authInfo, projectId);
        try {
            com.huaweicloud.frs.client.result.CreateFaceSetResult res = frsClient.getV2().getFaceSetService().createFaceSet(faceset.toStringUtf8());
            return 0;
        }
        catch (FrsException | IOException e) { //While http status code is not http_ok
//            e.printStackTrace();
            return -1;
        }
    }

    @ScalarFunction("GetFaceSet")
    @Description("Get a <para1: face_set>")
    @SqlType(StandardTypes.VARCHAR)
    public static Slice getFaceSet(@SqlType(StandardTypes.VARCHAR) Slice faceset)
    {
        AuthInfo authInfo = new AuthInfo(endpoint, region, ak, sk);
        FrsClient frsClient = new FrsClient(authInfo, projectId);
        try {
            GetFaceSetResult getFaceSetResult = frsClient.getV2().getFaceSetService().getFaceSet(faceset.toStringUtf8());
            DynamicSliceOutput output = new DynamicSliceOutput(200);
            output.appendBytes(getFaceSetResult.toString().getBytes(StandardCharsets.UTF_8));
            return output.slice();
        }
        catch (FrsException | IOException e) { //While http status code is not http_ok
//            e.printStackTrace();
            DynamicSliceOutput output = new DynamicSliceOutput(200);
            output.appendBytes(e.getMessage().getBytes(StandardCharsets.UTF_8));
            return output.slice();
        }
    }

    @ScalarFunction("DelFaceSet")
    @Description("Delete a <para1: face_set>")
    @SqlType(StandardTypes.INTEGER)
    public static long delFaceSet(@SqlType(StandardTypes.VARCHAR) Slice faceset)
    {
        AuthInfo authInfo = new AuthInfo(endpoint, region, ak, sk);
        FrsClient frsClient = new FrsClient(authInfo, projectId);
        //Step.2 Request
        try {
            frsClient.getV2().getFaceSetService().deleteFaceSet(faceset.toStringUtf8());
            return 0;
        }
        catch (FrsException | IOException e) { //While http status code is not http_ok
//            e.printStackTrace();
            return -1;
        }
    }

    @ScalarFunction("GetAllFaceSets")
    @Description("Get all face sets")
    @SqlType(StandardTypes.VARCHAR)
    public static Slice getAllFaceSets()
    {
        AuthInfo authInfo = new AuthInfo(endpoint, region, ak, sk);
        FrsClient frsClient = new FrsClient(authInfo, projectId);
        try {
            GetAllFaceSetsResult getAllFaceSetResult = frsClient.getV2().getFaceSetService().getAllFaceSets();
            DynamicSliceOutput output = new DynamicSliceOutput(200);
            output.appendBytes(getAllFaceSetResult.toString().getBytes(StandardCharsets.UTF_8));
            return output.slice();
        }
        catch (FrsException | IOException e) { //While http status code is not http_ok
//            e.printStackTrace();
            DynamicSliceOutput output = new DynamicSliceOutput(200);
            output.appendBytes(e.getMessage().getBytes(StandardCharsets.UTF_8));
            return output.slice();
        }
    }

    @ScalarFunction("DelAllFaceSets")
    @Description("Delete all face sets")
    @SqlType(StandardTypes.INTEGER)
    public static long delAllFaceSets()
    {
        AuthInfo authInfo = new AuthInfo(endpoint, region, ak, sk);
        FrsClient frsClient = new FrsClient(authInfo, projectId);
        try {
            GetAllFaceSetsResult getAllFaceSetResult = frsClient.getV2().getFaceSetService().getAllFaceSets();
            for (FaceSet faceset : getAllFaceSetResult.getFaceSetsInfo()) {
                frsClient.getV2().getFaceSetService().deleteFaceSet(faceset.getFaceSetName());
            }
            return 0;
        }
        catch (FrsException | IOException e) { //While http status code is not http_ok
//            e.printStackTrace();
            return -1;
        }
    }
}
