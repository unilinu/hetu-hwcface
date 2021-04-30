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
import com.huaweicloud.frs.client.result.GetFaceResult;
import com.huaweicloud.frs.client.result.common.Face;
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

public class HwcFaceFunctions
        extends HwcAuthInfo
{
    private HwcFaceFunctions() {}

    @ScalarFunction("AddFace")
    @Description("Add face in <para1: face_set> by <para2: face_path> and <para3: who_id>")
    @SqlType(StandardTypes.INTEGER)
    public static long addFace(@SqlType(StandardTypes.VARCHAR) Slice faceset,
                               @SqlType(StandardTypes.VARCHAR) Slice face, @SqlType(StandardTypes.INTEGER) long who)
    {
        AuthInfo authInfo = new AuthInfo(endpoint, region, ak, sk);
        FrsClient frsClient = new FrsClient(authInfo, projectId);
        //Step.2 Request
        try {
            frsClient.getV2().getFaceService().addFaceByFile(faceset.toStringUtf8(), Long.toString(who), face.toStringUtf8());
            return 0;
        }
        catch (FrsException | IOException e) { //While http status code is not http_ok
//            e.printStackTrace();
            return -1;
        }
    }

    @ScalarFunction("GetFace")
    @Description("Get face in <para1: face_set> by <para2: who_id>")
    @SqlType(StandardTypes.VARCHAR)
    public static Slice getFace(@SqlType(StandardTypes.VARCHAR) Slice faceset, @SqlType(StandardTypes.INTEGER) long who)
    {
        AuthInfo authInfo = new AuthInfo(endpoint, region, ak, sk);
        FrsClient frsClient = new FrsClient(authInfo, projectId);
        //Step.2 Request
        try {
            GetFaceResult getFaceResult = frsClient.getV2().getFaceService().getFaces(faceset.toStringUtf8(), 0, 100);
            DynamicSliceOutput output = new DynamicSliceOutput(200);
            for (Face face : getFaceResult.getFaces()) {
                if (face.getExternalImageId().equals(Long.toString(who))) {
                    output.appendBytes(face.toString().getBytes(StandardCharsets.UTF_8));
                    return output.slice();
                }
            }
            output.appendBytes(String.format("face id %d does not exist!", who).getBytes(StandardCharsets.UTF_8));
            return output.slice();
        }
        catch (FrsException | IOException e) { //While http status code is not http_ok
//            e.printStackTrace();
            DynamicSliceOutput output = new DynamicSliceOutput(200);
            output.appendBytes(e.getMessage().getBytes(StandardCharsets.UTF_8));
            return output.slice();
        }
    }

    @ScalarFunction("DelFace")
    @Description("Delete face in <para1: face_set> by <para2: who_id>")
    @SqlType(StandardTypes.INTEGER)
    public static long delFace(@SqlType(StandardTypes.VARCHAR) Slice faceset, @SqlType(StandardTypes.INTEGER) long who)
    {
        AuthInfo authInfo = new AuthInfo(endpoint, region, ak, sk);
        FrsClient frsClient = new FrsClient(authInfo, projectId);
        //Step.2 Request
        try {
            frsClient.getV2().getFaceService().deleteFaceByExternalImageId(faceset.toStringUtf8(), Long.toString(who));
            return 0;
        }
        catch (FrsException | IOException e) { //While http status code is not http_ok
//            e.printStackTrace();
            return -1;
        }
    }
}
