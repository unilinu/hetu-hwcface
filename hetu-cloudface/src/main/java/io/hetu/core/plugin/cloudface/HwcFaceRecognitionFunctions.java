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

public class HwcFaceRecognitionFunctions
        extends HwcAuthInfo
{
    private HwcFaceRecognitionFunctions() {}

    @ScalarFunction("Test")
    @Description("Return length of input string")
    @SqlType(StandardTypes.INTEGER)
    public static long test(@SqlType(StandardTypes.VARCHAR) Slice slice)
    { return slice.toStringUtf8().length(); }

    @ScalarFunction("SearchFace")
    @Description("Search face in <para1: face_set> by <para2: face_url>, return face_external_id or 0 for none")
    @SqlType(StandardTypes.INTEGER)
    public static long searchFace(@SqlType(StandardTypes.VARCHAR) Slice faceset, @SqlType(StandardTypes.VARCHAR) Slice face)
    {
        AuthInfo authInfo = new AuthInfo(endpoint, region, ak, sk);
        FrsClient frsClient = new FrsClient(authInfo, projectId);
        try {
            SearchFaceResult searchFaceResult = frsClient.getV2().getSearchService().searchFaceByObsUrl(faceset.toStringUtf8(), face.toStringUtf8());
            for (ComplexFace complexFace : searchFaceResult.getFaces()) {
                if (complexFace.getSimilarity() > (double) 0.95) {
                    return Long.parseLong(complexFace.getExternalImageId());
                }
            }
            return 0;
        }
        catch (FrsException | IOException e) { //While http status code is not http_ok
//            e.printStackTrace();
            return -1;
        }
    }

    @ScalarFunction("FaceScan")
    @Description("Scan <para1: face_url> in <implicit_para2: first_face_set>")
    @SqlType(StandardTypes.INTEGER)
    public static long faceScan(@SqlType(StandardTypes.VARCHAR) Slice face)
    {
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
                    return 0;
                }
                catch (FrsException | IOException e) { //While http status code is not http_ok
//                    e.printStackTrace();
                    return -1;
                }
            }
            catch (Exception e) {
//                e.printStackTrace();
                return -1;
            }
        }
        catch (FrsException | IOException e) { //While http status code is not http_ok
//            e.printStackTrace();
            return -1;
        }
    }
}
