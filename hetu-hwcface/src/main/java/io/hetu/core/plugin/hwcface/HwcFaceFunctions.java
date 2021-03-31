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
package io.hetu.core.plugin.hwcface;

import com.huaweicloud.frs.client.param.AuthInfo;
import com.huaweicloud.frs.client.result.GetAllFaceSetsResult;
import com.huaweicloud.frs.client.result.SearchFaceResult;
import com.huaweicloud.frs.client.result.common.ComplexFace;
import com.huaweicloud.frs.client.service.FrsClient;
import com.huaweicloud.frs.common.FrsException;
import io.airlift.slice.Slice;
import io.prestosql.spi.function.Description;
import io.prestosql.spi.function.ScalarFunction;
import io.prestosql.spi.function.SqlType;
import io.prestosql.spi.type.StandardTypes;

import java.io.IOException;

public class HwcFaceFunctions
{
    private static final String ak = "VE2H20KTER2SQ72TCODJ";
    private static final String sk = "NyvM6TxlNRmZMJ5wqTuQw3Y1RCnK3ZEMFcc4gSdh";
    private static final String endpoint = "https://face.cn-east-3.myhuaweicloud.com";
    private static final String region = "cn-east-3";
    private static final String projectId = "0bdae2fb9c8026b02f47c01cf0b958be";

    private HwcFaceFunctions() {}

    @ScalarFunction("Test")
    @Description("Return length of input string")
    @SqlType(StandardTypes.INTEGER)
    public static long Test(@SqlType(StandardTypes.VARCHAR) Slice slice)
    { return slice.toStringUtf8().length(); }

    @ScalarFunction("NewFaceSet")
    @Description("New a <para1: face_set>")
    @SqlType(StandardTypes.INTEGER)
    public static long NewFaceSet(@SqlType(StandardTypes.VARCHAR) Slice faceset)
    {
        //Step.1 Create frs client
        AuthInfo authInfo = new AuthInfo(endpoint, region, ak, sk);
        FrsClient frsClient = new FrsClient(authInfo, projectId);
        try {
            frsClient.getV2().getFaceSetService().createFaceSet(faceset.toStringUtf8());
            return (long) 0;
        }
        catch (FrsException | IOException e) { //While http status code is not http_ok
            e.printStackTrace();
            return (long) -1;
        }
    }

    @ScalarFunction("AddFace")
    @Description("Add face in <para1: face_set> by <para2: face_path> and <para3: who_id>")
    @SqlType(StandardTypes.INTEGER)
    public static long AddFace(@SqlType(StandardTypes.VARCHAR) Slice faceset,
                               @SqlType(StandardTypes.VARCHAR) Slice face, @SqlType(StandardTypes.INTEGER) long who)
    {
        //Step.1 Create frs client
        AuthInfo authInfo = new AuthInfo(endpoint, region, ak, sk);
        FrsClient frsClient = new FrsClient(authInfo, projectId);
        //Step.2 Request
        try {
//            AddFaceResult addFaceResult =
            frsClient.getV2().getFaceService().addFaceByFile(faceset.toStringUtf8(), Long.toString(who), face.toStringUtf8());
            return (long) 0;
        }
        catch (FrsException | IOException e) { //While http status code is not http_ok
            e.printStackTrace();
            return (long) -1;
        }
    }

    @ScalarFunction("SearchFace")
    @Description("Search face in <para1: face_set> by <para2: face_url>, return face_external_id or 0 for none")
    @SqlType(StandardTypes.INTEGER)
    public static long SearchFace(@SqlType(StandardTypes.VARCHAR) Slice faceset, @SqlType(StandardTypes.VARCHAR) Slice face)
    {
        //Step.1 Create frs client
        AuthInfo authInfo = new AuthInfo(endpoint, region, ak, sk);
        FrsClient frsClient = new FrsClient(authInfo, projectId);
        try {
            SearchFaceResult searchFaceResult = frsClient.getV2().getSearchService().searchFaceByObsUrl(faceset.toStringUtf8(), face.toStringUtf8());
            for (ComplexFace complexFace : searchFaceResult.getFaces()) {
                if (complexFace.getSimilarity() > (double) 0.95) {
                    return Long.parseLong(complexFace.getExternalImageId());
                }
            }
            return (long) 0;
        }
        catch (FrsException | IOException e) { //While http status code is not http_ok
            e.printStackTrace();
            return (long) -1;
        }
    }

    @ScalarFunction("DelFace")
    @Description("Delete face in <para1: face_set> by <para2: who_id>")
    @SqlType(StandardTypes.INTEGER)
    public static long DelFace(@SqlType(StandardTypes.VARCHAR) Slice faceset, @SqlType(StandardTypes.INTEGER) long who)
    {
        //Step.1 Create frs client
        AuthInfo authInfo = new AuthInfo(endpoint, region, ak, sk);
        FrsClient frsClient = new FrsClient(authInfo, projectId);
        //Step.2 Request
        try {
            frsClient.getV2().getFaceService().deleteFaceByExternalImageId(faceset.toStringUtf8(), Long.toString(who));
            return (long) 0;
        }
        catch (FrsException | IOException e) { //While http status code is not http_ok
            e.printStackTrace();
            return (long) -1;
        }
    }

    @ScalarFunction("DelFaceSet")
    @Description("Delete a <para1: face_set>")
    @SqlType(StandardTypes.INTEGER)
    public static long DelFaceSet(@SqlType(StandardTypes.VARCHAR) Slice faceset)
    {
        //Step.1 Create frs client
        AuthInfo authInfo = new AuthInfo(endpoint, region, ak, sk);
        FrsClient frsClient = new FrsClient(authInfo, projectId);
        //Step.2 Request
        try {
            frsClient.getV2().getFaceSetService().deleteFaceSet(faceset.toStringUtf8());
            return (long) 0;
        }
        catch (FrsException | IOException e) { //While http status code is not http_ok
            e.printStackTrace();
            return (long) -1;
        }
    }

    @ScalarFunction("FaceScan")
    @Description("Scan <para1: face_url> in <implicit_para2: first_face_set>")
    @SqlType(StandardTypes.INTEGER)
    public static long FaceScan(@SqlType(StandardTypes.VARCHAR) Slice face)
    {
        //Step.1 Create frs client
        AuthInfo authInfo = new AuthInfo(endpoint, region, ak, sk);
        FrsClient frsClient = new FrsClient(authInfo, projectId);
        //Step.2 Request
        try {
            GetAllFaceSetsResult getAllFaceSetsResult = frsClient.getV2().getFaceSetService().getAllFaceSets();
            try {
                String faceset = getAllFaceSetsResult.getFaceSetsInfo().get(0).getFaceSetName();
                try {
                    SearchFaceResult searchFaceResult = frsClient.getV2().getSearchService().searchFaceByObsUrl(faceset, face.toStringUtf8());
                    for (ComplexFace complexFace : searchFaceResult.getFaces()) {
                        if (complexFace.getSimilarity() > (double) 0.95) {
                            return Long.parseLong(complexFace.getExternalImageId());
                        }
                    }
                    return (long) 0;
                }
                catch (FrsException | IOException e) { //While http status code is not http_ok
                    e.printStackTrace();
                    return (long) -1;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                return (long) -1;
            }
        }
        catch (FrsException | IOException e) { //While http status code is not http_ok
            e.printStackTrace();
            return (long) -1;
        }
    }
}
