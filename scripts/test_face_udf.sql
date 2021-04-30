-- #! /opt/openlookeng/bin/openlk-cli

-- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- !!RUN ONLY BY COPING AND PASTING!!
-- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

-- # test cloudface
select cloudface.Test('return length of this string');


-- # new face set
select cloudface.NewFaceSet('pml-test-faceset');


-- # add faces
-- select * FROM mysql.pml.user;

-- ## add zhangsan's face
select id, facepath FROM mysql.pml.user where name = '张三';
-- filling MANUALLY
select cloudface.AddFace('pml-test-faceset', '/home/openlkadmin/team-1269929257/data/luoxiang.jpg', 1001);

-- ## add zhangsan's face
select id, facepath FROM mysql.pml.user where name = '李四';
-- need filling MANUALLY
select cloudface.AddFace('pml-test-faceset', '/home/openlkadmin/team-1269929257/data/luoyonghao.jpg', 1002);

-- ## DECLARE IS NOT SUPPORTED!
-- delimiter $$
-- create procedure add_faces()
-- begin
-- declare user_id int;
-- declare face_path varchar(255);
-- select id, facepath INTO user_id, face_path FROM mysql.pml.user where name = '李四';
-- select cloudface.AddFace('pml-test-faceset', face_path, user_id);
-- select id, facepath INTO user_id, face_path FROM mysql.pml.user where name = '张三';
-- select cloudface.AddFace('pml-test-faceset', face_path, user_id);
-- end $$
-- delimiter ;
-- call add_faces();


-- # search who
select name from mysql.pml.user where id = cloudface.SearchFace('pml-test-faceset', 'https://pic3.zhimg.com/v2-c3b1ad8057c14fc8cf6520702a307798_xl.jpg'); -- luoxiang -> zhangsan
select name from mysql.pml.user where id = cloudface.SearchFace('pml-test-faceset', 'https://pic4.zhimg.com/80/v2-6d6224abdb7134a78e40430bdc690c0e_400x224.jpg'); -- luoyonghao -> lisi
select name from mysql.pml.user where id = cloudface.SearchFace('pml-test-faceset', 'https://pic3.zhimg.com/80/v2-36c2f0f917e0a47440593a2c51dc5769_qhd.jpg'); -- Trump -> none


-- # scan who
select name from mysql.pml.user where id = cloudface.FaceScan('https://pic3.zhimg.com/v2-c3b1ad8057c14fc8cf6520702a307798_xl.jpg'); -- luoxiang -> zhangsan


-- # delete face
select cloudface.DelFace('pml-test-faceset', 1001);

--# delete face set
select cloudface.DelFaceSet('pml-test-faceset');
