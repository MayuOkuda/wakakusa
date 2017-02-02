<?php
require_once "json.php";
require_once "main.php";

/*自動認証モジュール
　受け取ったCookieの値を基に自動ログインを行う
*/

function user_author($auto_login){

 //ユーザID取得
 $dbh = connectDb();
 $sql = "SELECT user_name FROM wp_auto_login WHERE auto_login_key = :auto_login";
 $params = array(':auto_login' => ("$auto_login"));
 $stmt = $dbh->prepare($sql);
 $stmt->execute($params);
 $row = $stmt->fetch(PDO::FETCH_ASSOC);

 if(!empty($row["user_name"])){
  call_json($row["user_name"], $auto_login);
  return true;
 }
 else{
  return false;
 }
}



?>
