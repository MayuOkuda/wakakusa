<?php
require_once "json.php";

/* 学生情報モジュール
   該当学生の学生情報を取得する */

function student_json($num, $day, $time){
 $dbh= connectDb();
 $sth= $dbh->prepare("SELECT wp_student.学籍番号, wp_student.氏名, 生年月日, 入学年月日, 学部名称, 学年, 学科名称, a.専攻名称 AS 専攻名称1, b.専攻名称 AS 専攻名称2, c.専攻名称 AS 専攻名称3, wp_teacher.氏名 AS 教師, 住所,wp_student.メールアドレス
                     FROM wp_student, wp_privacy, wp_undergraduate, wp_major a, wp_major b, wp_major c, wp_teacher, wp_department
                     WHERE wp_student.学籍番号='$num' AND wp_student.学籍番号= wp_privacy.学籍番号
                     AND wp_student.学部コード=wp_undergraduate.学部コード
                     AND wp_student.学科コード=wp_department.学科コード
                     AND wp_student.専攻コード=a.専攻コード
                     AND wp_student.副専攻コード1=b.専攻コード
                     AND wp_student.副専攻コード2=c.専攻コード
                     AND 教職員番号=主指導教員番号
                     AND (((wp_student.更新年月日 > '$day') OR (wp_student.更新年月日 = '$day' AND wp_student.更新日時 > '$time'))
                     OR  ((wp_privacy.更新年月日 > '$day') OR (wp_privacy.更新年月日 = '$day' AND wp_privacy.更新日時 > '$time'))
                     OR  ((wp_undergraduate.更新年月日 > '$day') OR (wp_undergraduate.更新年月日 = '$day' AND wp_undergraduate.更新日時 > '$time'))
                     OR  ((a.更新年月日 > '$day') OR (a.更新年月日 = '$day' AND a.更新日時 > '$time'))
                     OR  ((b.更新年月日 > '$day') OR (b.更新年月日 = '$day' AND b.更新日時 > '$time'))
                     OR  ((c.更新年月日 > '$day') OR (c.更新年月日 = '$day' AND c.更新日時 > '$time'))
                     OR  ((wp_teacher.更新年月日 > '$day') OR (wp_teacher.更新年月日 = '$day' AND wp_teacher.更新日時 > '$time'))
                     OR  ((wp_department.更新年月日 > '$day') OR (wp_department.更新年月日 = '$day' AND wp_department.更新日時 > '$time')))");
$sth->execute();
$userData = array();

echo "{";
echo "\"student\" :";
while($row = $sth->fetch(PDO::FETCH_ASSOC)){
    $userData[]=array(
    'id'=>$row['学籍番号'],
    'name'=>$row['氏名'],
    'birth' =>$row['生年月日'],
    'adm'=>$row['入学年月日'],
    'ug' =>$row['学部名称'],
    'dpm' =>$row['学科名称'],
    'grade' =>$row['学年'],
    'mjr'=>$row['専攻名称1'],
    'sub1'=>$row['専攻名称2'],
    'sub2'=>$row['専攻名称3'],
    'teacher'=>$row['教師'],
    'address'=>$row['住所'],
    'mailaddress'=>$row['メールアドレス'],
    );
}

header('Content-type: application/json');
echo json_encode($userData);
echo ",\n";
}


?>
