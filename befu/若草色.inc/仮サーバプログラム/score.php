<?php
require_once "json.php";

/* 成績情報モジュール
   該当学生の履修科目関係する成績を表示する */

function score_json($num, $day, $time){
$dbh = connectDb();
$sth = $dbh->prepare("SELECT 科目コード, 成績, 開講年, 開講時期
  FROM wp_course
  WHERE wp_course.学籍番号='$num'
  AND  ((wp_course.更新年月日 > '$day') OR (wp_course.更新年月日 = '$day' AND wp_course.更新日時 > '$time'))");
$sth->execute();

$userData = array();

echo "\"score\" :";

while($row = $sth->fetch(PDO::FETCH_ASSOC)){
    $userData[]=array(
    'scode'=>$row['科目コード'],
    'score'=>$row['成績'],
    'year'=>$row['開講年'],
    'period'=>$row['開講時期'],
    );
}

header('Content-type: application/json');
echo json_encode($userData);
echo ",\n";

}
?>
