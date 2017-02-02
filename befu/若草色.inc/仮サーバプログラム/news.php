<?php
require_once "json.php";

/* お知らせ情報モジュール
   該当学生に関係するお知らせ情報を表示する */


function news_json($num, $day, $time){

$dbh = connectDb();
$sth = $dbh->prepare("SELECT DISTINCT お知らせコード, 送信日, wp_teacher.氏名, 送信アドレス, タイトル, 内容, お知らせ区分
  FROM wp_news, wp_course, wp_teacher
  WHERE (ISNULL(wp_news.科目コード) OR (wp_course.科目コード = wp_news.科目コード AND wp_course.学籍番号='$num' AND wp_news.お知らせ区分 LIKE N'講義'))
  AND wp_teacher.メールアドレス = 送信アドレス
  AND (((wp_news.更新年月日 > '$day') OR (wp_news.更新年月日 = '$day' AND wp_news.更新日時 > '$time'))
  OR  ((wp_course.更新年月日 > '$day') OR (wp_course.更新年月日 = '$day' AND wp_course.更新日時 > '$time'))
  OR  ((wp_teacher.更新年月日 > '$day') OR (wp_teacher.更新年月日 = '$day' AND wp_teacher.更新日時 > '$time')))
  ORDER BY お知らせコード DESC limit 100");
$sth->execute();

$userData = array();


echo "\"news\" :";

while($row = $sth->fetch(PDO::FETCH_ASSOC)){
    $userData[]=array(
    'newscode'=>$row['お知らせコード'],
    'day'=>$row['送信日'],
    'adduser'=>$row['氏名'],
    'address'=>$row['送信アドレス'],
    'title'=>$row['タイトル'],
    'content'=>$row['内容'],
    'neclass'=>$row['お知らせ区分'],
    );
}

header('Content-type: application/json');
echo json_encode($userData);
echo "}";


}
?>
