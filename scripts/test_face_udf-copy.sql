-- #! /opt/openlookeng/bin/openlk-cli


-- # test function
select cloudface.Test('return length of this string'); -- 28


-- # get all face sets
select cloudface.GetAllFaceSets();


-- # new face set
select cloudface.NewFaceSet('pml-faceset');


-- # add faces
-- select * FROM mysql.pml.user;

-- ## add zhangsan's face
select id, facepath FROM mysql.pml.user where name = '张三';
select cloudface.AddFace('pml-faceset', '/home/openlkadmin/team-1269929257/data/luoxiang.jpg', 1001); -- filling MANUALLY

-- ## add zhangsan's face
select id, facepath FROM mysql.pml.user where name = '李四';
select cloudface.AddFace('pml-faceset', '/home/openlkadmin/team-1269929257/data/luoyonghao.jpg', 1002); -- need filling MANUALLY


-- # get face
select cloudface.GetFace("pml-faceset", 1001);


-- # get face set
select cloudface.GetFaceSet("pml-faceset");


-- # search who
select name from mysql.pml.user where id = cloudface.SearchFace('pml-faceset', 'https://pic3.zhimg.com/v2-c3b1ad8057c14fc8cf6520702a307798_xl.jpg'); -- luoxiang -> zhangsan
select name from mysql.pml.user where id = cloudface.SearchFace('pml-faceset', 'https://pic4.zhimg.com/80/v2-6d6224abdb7134a78e40430bdc690c0e_400x224.jpg'); -- luoyonghao -> lisi
select name from mysql.pml.user where id = cloudface.SearchFace('pml-faceset', 'https://pic3.zhimg.com/80/v2-36c2f0f917e0a47440593a2c51dc5769_qhd.jpg'); -- Trump -> none


-- # scan who
select name from mysql.pml.user where id = cloudface.FaceScan('https://pic3.zhimg.com/v2-c3b1ad8057c14fc8cf6520702a307798_xl.jpg'); -- luoxiang -> zhangsan


-- # delete face
select cloudface.DelFace('pml-faceset', 1001);


--# delete face set
select cloudface.DelFaceSet('pml-faceset');


-- # delete all face sets
-- select cloudface.DelAllFaceSets();