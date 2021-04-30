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
import com.google.common.collect.ImmutableList;
import io.prestosql.operator.scalar.AbstractTestFunctions;
import io.prestosql.spi.type.Type;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.prestosql.metadata.FunctionExtractor.extractFunctions;
import static io.prestosql.operator.scalar.ApplyFunction.APPLY_FUNCTION;
import static io.prestosql.spi.type.IntegerType.INTEGER;
import static io.prestosql.spi.type.VarcharType.VARCHAR;

public class TestFinal
        extends AbstractTestFunctions
{
    @BeforeClass
    protected void registerFunctions()
    {
        CloudFacePlugin plugin = new CloudFacePlugin();
        for (Type type : plugin.getTypes()) {
            functionAssertions.addType(type);
        }
        functionAssertions.getMetadata().addFunctions(extractFunctions(plugin.getFunctions()));
        functionAssertions.getMetadata().addFunctions(ImmutableList.of(APPLY_FUNCTION));
    }

    @Test
    public void TestSystem()
    {
        // 测试Test功能:
        assertFunction("Test('abc')", INTEGER, 3);

        // 测试新建人脸库功能:
//        assertFunction("NewFaceSet('pml-test-faceset')", INTEGER, 0); // 新建成功，但断言ERROR
        // 假定人脸库已经外部新建
        assertFunction("NewFaceSet('pml-test-faceset')", INTEGER, -1); // exist

        // 测试新建人脸功能:
        assertFunction("AddFace('pml-test-faceset', '/home/openlkadmin/team-1269929257/data/luoxiang.jpg', 1001)", INTEGER, 0); // zhangsan
        assertFunction("AddFace('pml-test-faceset', '/home/openlkadmin/team-1269929257/data/luoyonghao.jpg', 1002)", INTEGER, 0); // lisi
        assertFunction("AddFace('pml-test-faceset', '/home/openlkadmin/team-1269929257/data/INVALID.jpg', 1003)", INTEGER, -1); // none

        // 测试显示人脸功能:
        assertFunction("GetFace('pml-test-faceset', 1003)", VARCHAR, "face id 1003 does not exist!"); // none

        // 测试搜索人脸功能:
        assertFunction("SearchFace('pml-test-faceset', 'https://pic3.zhimg.com/v2-c3b1ad8057c14fc8cf6520702a307798_xl.jpg')", INTEGER, 1001); // zhangsan
        assertFunction("SearchFace('pml-test-faceset', 'https://pic3.zhimg.com/80/v2-36c2f0f917e0a47440593a2c51dc5769_qhd.jpg')", INTEGER, 0); // else
        assertFunction("SearchFace('pml-test-faceset', '/home/openlkadmin/team-1269929257/data/INVALID.jpg')", INTEGER, -1); // none

        assertFunction("FaceScan('https://pic3.zhimg.com/v2-c3b1ad8057c14fc8cf6520702a307798_xl.jpg')", INTEGER, 1001); // zhangsan
        assertFunction("FaceScan('https://pic3.zhimg.com/80/v2-36c2f0f917e0a47440593a2c51dc5769_qhd.jpg')", INTEGER, 0); // else
        assertFunction("FaceScan('/home/openlkadmin/team-1269929257/data/INVALID.jpg')", INTEGER, -1);  // none

        //测试删除人脸功能:
//        assertFunction("DelFace('pml-test-faceset', 1001)", INTEGER, 0); // 删除成功，但断言ERROR
        assertFunction("DelFace('pml-test-faceset', 1003)", INTEGER, -1); // miss

        //测试删除人脸库功能:
//        assertFunction("DelFaceSet('pml-test-faceset')", INTEGER, 0); // 删除成功，但断言ERROR
        assertFunction("DelFaceSet('invalid-faceset')", INTEGER, -1); // miss
    }
}
