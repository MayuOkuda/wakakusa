<?php
define('DSN','mysql:host=localhost;dbname=KUTPortal');
define('DB_USER','root');
define('DB_PASSWORD','root00');
error_reporting(E_ALL & ~E_NOTICE);

function connectDb() {
/* HP上でDB接続するためのアクセスモジュール */
try {
return new PDO(DSN, DB_USER, DB_PASSWORD);
} catch (PDOException $e) {
echo $e->getMessage();
exit;
}
}

function test($subject, $test1, $test2){
/* HPからのテスト日変更をDBに反映させる */

//日時取得
$day = date('Ymd');
$time = date('His');

//変更された領域毎の処理
if($test1 > 0 && $test2 > 0){

$dbh = connectDb();
$sql = "UPDATE wp_lesson SET テスト日1 =:test1, テスト日2=:test2, 更新年月日=:day, 更新日時 = :time where 科目コード = :subject";
$params = array(':subject' => ("$subject"), ':test1' => ("$test1"), ':test2' => ("$test2"), ':day'=>("$day"), ':time' => ("$time"));
$stmt = $dbh->prepare($sql);
$stmt->execute($params);

}
else if($test1 > 0){

$dbh = connectDb();
$sql = "UPDATE wp_lesson SET テスト日1 =:test1,  更新年月日=:day, 更新日時 = :time where 科目コード = :subject";
$params = array(':subject' => ("$subject"), ':test1' => ("$test1"), ':day'=>("$day"), ':time' => ("$time"));
$stmt = $dbh->prepare($sql);
$stmt->execute($params);

}
else if($test2 > 0){

$dbh = connectDb();
$sql = "UPDATE wp_lesson SET テスト日2=:test2, 更新年月日=:day, 更新日時 = :time where 科目コード = :subject";
$params = array(':subject' => ("$subject"), ':test2' => ("$test2"), ':day'=>("$day"), ':time' => ("$time"));
$stmt = $dbh->prepare($sql);
$stmt->execute($params);

}
}


function news($title, $mail, $genre, $message, $grade, $college, $individual){
/*講義以外のお知らせを作成するHPからの入力をDB，push通知を呼び出す処理 */
$code = NULL;
$day = date('Ymd');
$time = date('His');

//お知らせコードを決定する処理
$dbh = connectDb();
$sql = "SELECT COUNT(お知らせコード) AS cnt FROM wp_news";
$stmt = $dbh->prepare($sql);
$stmt->execute();
$row = $stmt->fetch(PDO::FETCH_ASSOC);
$num1 = $row['cnt'] + 1;
$num2 = str_pad((string)$num1, 9, '0', STR_PAD_LEFT);
$num = "N".$num2;

//お知らせをDBに反映させる処理
$sql2 = "INSERT INTO wp_news VALUES (:num, :day, :address, :title, :message, :genre, :code, :today, :time)";
$params2 = array(':num' => ("$num"), ':day' => ("$day"), ':address' => ("$mail"), ':title' => ("$title"), ':message' => ("$message"), ':genre'=>("$genre"), ':code' => ($code), ':today' => ("$day"), ':time' => ("$time"));
$stmt2 = $dbh->prepare($sql2);
$stmt2->execute($params2);


//送る対象を選択する例外処理
if(!empty($individual)){
$college = 1;
$grade = 1;
}

//push通知処理を呼び出す
send($title, $genre, $grade, $college, $individual);
}

function kougi($subject, $title, $message){
/* 講義お知らせをDBに反映し，通知処理を呼び出す処理 */

$genre="講義";
$day = date('Ymd');
$time = date('His');

//お知らせコードを決定する処理
$dbh = connectDb();
$sql = "SELECT COUNT(お知らせコード) AS cnt FROM wp_news";
$stmt = $dbh->prepare($sql);
$stmt->execute();
$row = $stmt->fetch(PDO::FETCH_ASSOC);
$num1 = $row['cnt'] + 1;
$num2 = str_pad((string)$num1, 9, '0', STR_PAD_LEFT);
$num = "N".$num2;

//講義担当者のメールアドレスを取得する処理
$dbh = connectDb();
$sql = "SELECT メールアドレス FROM wp_lesson, wp_teacher WHERE 科目コード ='$subject' AND 教員番号 = 教職員番号";
$stmt = $dbh->prepare($sql);
$stmt->execute();
$row = $stmt->fetch(PDO::FETCH_ASSOC);
$mail = $row['メールアドレス'];

//お知らせをDBに挿入する処理
$sql2 = "INSERT INTO wp_news VALUES (:num, :day, :address, :title, :message, :genre, :code, :today, :time)";
$params2 = array(':num' => ("$num"), ':day' => ("$day"), ':address' => ("$mail"), ':title' => ("$title"), ':message' => ("$message"), ':genre' => ($genre), ':code'=> ($subject), ':today' => ($day), ':time' => ($time));
$stmt2 = $dbh->prepare($sql2);
$stmt2->execute($params2);

//通知処理を呼び出す処理
send($title, $genre, $subject, 0, 0);

}


function send($title, $body, $target1, $target2, $individual){
/* Androidにpush通知を送る処理 */

if($target1 == "1"){
//個人宛の時，tokenIDを呼び出す処理
$dbh = connectDb();
$sql = "SELECT tokenID FROM wp_fcm, wp_student WHERE 学籍番号 = '$individual' AND ログインID = userID";
$stmt = $dbh->prepare($sql);
$stmt->execute();
$row = $stmt->fetch(PDO::FETCH_ASSOC);
$target = $row['tokenID'];
$to = "to";
}
else if($target1 == "all" && $target2 == "all"){
//全員宛
$target = "/topics/all";
$to = "to";
}
else{
$target = "'".$target1."' in topics && '".$target2."' in topics";
$to = "condition";
}



//FCMのAPIキーとURL
$api_key ="AAAAgG51F6Y:APA91bEqZ35itQKELkRKnVX0wlIDmFKuOx9HxccVyZ-Un3DTFfOPjPzU4ApK4uf9JMAvC5abkkJ98ydD2elQyjbhB-TAQj8fP1p94mwCeSHZP4TKEq9AvaGO44Y6BGLnIkHg0ED7ApgO1LrU17aXwEysueHmY0Ojlw";
$url = "https://fcm.googleapis.com/fcm/send";

$header = [
"Authorization:key=".$api_key,
"Content-Type: application/json"
];

//送信内容
$fields = [
$to => $target,
"data" => [
"title" => $title,
"body" => $body
],
"priority" => "high"
];

//curlにoptionを設定し，送信する処理
$handle = curl_init();
curl_setopt($handle, CURLOPT_URL, $url);
curl_setopt($handle, CURLOPT_POST, true); 
curl_setopt($handle, CURLOPT_HTTPHEADER, $header);
curl_setopt($handle, CURLOPT_RETURNTRANSFER, true);
curl_setopt($handle, CURLOPT_SSL_VERIFYPEER, false);
curl_setopt($handle, CURLOPT_POSTFIELDS, json_encode($fields));
curl_exec($handle);
curl_close($handle);

}

?>
