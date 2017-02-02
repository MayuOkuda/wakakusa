<?php
require_once "json.php";

/* テスト日情報モジュール
   該当学生に関係するテスト日を表示する */

function test_json($num, $day, $time){
$dbh = connectDb();

$year = date(Y);
$month = date(n);
if($month >= 1 && $month <= 3){
$year = $year -1;
}

$sth = $dbh->prepare("SELECT wp_course.科目コード, テスト日1, テスト日2
  FROM wp_course, wp_lesson
  WHERE wp_course.学籍番号='$num' AND wp_lesson.科目コード = wp_course.科目コード
  AND wp_course.開講年 = $year
  AND  (((wp_course.更新年月日 > '$day') OR (wp_course.更新年月日 = '$day' AND wp_course.更新日時 > '$time'))
  OR  ((wp_lesson.更新年月日 > '$day') OR (wp_lesson.更新年月日 = '$day' AND wp_lesson.更新日時 > '$time')))");
$sth->execute();

$userData = array();
echo "\"test\" :";

while($row = $sth->fetch(PDO::FETCH_ASSOC)){
$userData[]=array(
'scode'=>$row['科目コード'],
'test1'=>$row['テスト日1'],
'test2'=>$row['テスト日2'],
);
}

header('Content-type: application/json');
echo json_encode($userData);
echo ",\n";
}
?>
