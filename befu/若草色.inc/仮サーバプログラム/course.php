<?php
require_once "json.php";

/* 科目情報モジュール
   該当学生に関係する科目情報を表示する */


function course_json($num, $day, $time){
$dbh = connectDb();
$sth = $dbh->prepare("SELECT wp_course.科目コード, 科目名称, 開講曜時, wp_teacher.氏名, wp_course.開講時期, 教室名称, 単位数, 単位区分,科目区分
  FROM wp_lesson, wp_room, wp_teacher, wp_lecture, wp_course
  WHERE wp_course.学籍番号='$num' AND wp_course.科目コード= wp_lecture.科目コード
  AND wp_lesson.科目コード = wp_course.科目コード AND wp_teacher.教職員番号 = wp_lesson.教員番号
  AND wp_lesson.教室コード = wp_room.教室コード
  AND (((wp_lesson.更新年月日 > '$day') OR (wp_lesson.更新年月日 = '$day' AND wp_lesson.更新日時 > '$time'))
  OR ((wp_room.更新年月日 > '$day') OR (wp_room.更新年月日 = '$day' AND wp_room.更新日時 > '$time'))
  OR ((wp_teacher.更新年月日 > '$day') OR (wp_teacher.更新年月日 = '$day' AND wp_teacher.更新日時 > '$time'))
  OR ((wp_lecture.更新年月日 > '$day') OR (wp_lecture.更新年月日 = '$day' AND wp_lecture.更新日時 > '$time'))
  OR ((wp_course.更新年月日 > '$day') OR (wp_course.更新年月日 = '$day' AND wp_course.更新日時 > '$time')))");

$sth->execute();

$userData = array();

echo "\"course\" :";

while($row = $sth->fetch(PDO::FETCH_ASSOC)){
    $userData[]=array(
    'scode'=>$row['科目コード'],
    'subject'=>$row['科目名称'],
    'daytime' =>$row['開講曜時'],
    'teacher' =>$row['氏名'],
    'period'=>$row['開講時期'],
    'room'=>$row['教室名称'],
    'sj'=>$row['単位数'],
    'sjclass'=>$row['単位区分'],
    'sjclasssub'=>$row['科目区分'],
    );
};

header('Content-type: application/json');
echo json_encode($userData);
echo ",\n";


}
?>

