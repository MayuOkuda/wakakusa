<?php
require_once "cookie.php";
require_once "json.php";
//require_once "main.php";

/*ログインモジュール
　入力されたID、パスワードを基に手動ログインを行う
*/

function login($id, $pass, $cookie){
 $dbh = connectDb();
 $sql = "SELECT パスワード FROM wp_student WHERE ログインID = :userid";
 $params = array(':userid' => ("$id"));
 $stmt = $dbh->prepare($sql);
 $stmt->execute($params);
 $row = $stmt->fetch(PDO::FETCH_ASSOC);

 $db_hashed_pass = $row["パスワード"];
 if(password_verify($pass, $db_hashed_pass)){
  //setup_auto_login($id);
  call_json($id, $cookie);
  return true;
 }
 else{
  //$errorMessage = "error";
  return false;
 }

return false;
}

?>
