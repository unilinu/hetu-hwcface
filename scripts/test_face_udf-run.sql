  select cloudface.DelFaceSet('pml-faceset');




  select cloudface.NewFaceSet('pml-faceset');




  select id, facepath FROM mysql.pml.user where name = '张三';




  select cloudface.AddFace('pml-faceset', '/home/openlkadmin/team-1269929257/data/luoxiang.jpg', 1001);




  select id, facepath FROM mysql.pml.user where name = '李四';




  select cloudface.AddFace('pml-faceset', '/home/openlkadmin/team-1269929257/data/luoyonghao.jpg', 1002); 




  select cloudface.GetFace('pml-faceset', 1001);




  select cloudface.AddFace('pml-faceset', '/home/openlkadmin/team-1269929257/data/luoyonghao.jpg', 1003); 




  select cloudface.DelFace('pml-faceset', 1003);




  select cloudface.GetAllFaceSets();




  select cloudface.GetFaceSet('pml-faceset');











  select name from mysql.pml.user where id = cloudface.SearchFace('pml-faceset', 'https://pic3.zhimg.com/v2-c3b1ad8057c14fc8cf6520702a307798_xl.jpg');




  select name from mysql.pml.user where id = cloudface.SearchFace('pml-faceset', 'https://pic4.zhimg.com/80/v2-6d6224abdb7134a78e40430bdc690c0e_400x224.jpg');




  select name from mysql.pml.user where id = cloudface.SearchFace('pml-faceset', 'https://pic3.zhimg.com/80/v2-36c2f0f917e0a47440593a2c51dc5769_qhd.jpg');




  select name from mysql.pml.user where id = cloudface.FaceScan('https://pic3.zhimg.com/v2-c3b1ad8057c14fc8cf6520702a307798_xl.jpg');




