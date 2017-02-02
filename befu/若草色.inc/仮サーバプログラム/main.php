<?php

require_once "user_author.php";
require_once "login.php";
require_once "json.php";
require_once "cookie.php";

header('Content-Type: text/html; charset=utf-8');

/*メインモジュール*/

$auto_login = $_COOKIE["auto_login"]; /* クッキー */
$userid = $_POST["userid"];           /* ユーザID */
$pass = $_POST["password"];           /* パスワード */
$tokenID = $_POST["token"];           /* tokenID */
$reday = $_POST["reday"];             /* 更新日時 */

$len = strlen($reday);
//更新日時が空の場合
if(empty($reday) || $len < 14){
 $reday = '00000000000000';
}

//更新日と時間に分割
$day = substr($reday, 0, 8);
$time= substr($reday, 8, 14);

//自動ログイン
if(isset($auto_login)){
 if(user_author($auto_login)){
  exit;
 }
 else{
  login($userid, $pass, $auto_login);
  exit;
 }
}

//手動ログイン
else{
 if(login($userid, $pass, $auto_login)){
  exit;
 }
}


/*JSON呼び出しモジュール
　入力されたユーザIDから学籍番号を取得し、JSON取得モジュールを呼び出す
*/

function call_json($id, $cookie){

 // 学籍番号取得
 $dbh = connectDb();
 $sql = "SELECT 学籍番号 FROM wp_student WHERE ログインID = :userid";
 $params = array(':userid' => ("$id"));
 $stmt = $dbh->prepare($sql);
 $stmt->execute($params);
 $row = $stmt->fetch(PDO::FETCH_ASSOC);
 $userid = $row["学籍番号"];

 global $tokenID, $day, $time;

if(!empty($tokenID) && !$tokenID.equals("NoToken")){
 //tokenID更新処理
 $dbh = connectDb();
 $sql = "DELETE FROM wp_fcm WHERE userID = :userid";
 $params = array(':userid' => ("$id"));
 $stmt = $dbh->prepare($sql);
 $stmt->execute($params);

 $dbh = connectDb();
 $sql = "INSERT INTO wp_fcm (userID, tokenID) VALUES (:userID, :tokenID)";
 $stmt = $dbh->prepare($sql);
 $params = array(':userID' => ("$id"), ':tokenID' => ("$tokenID"));
 $stmt->execute($params);
}

 //Cookie設定と該当データ表示
 delete_auto_login($id, $cookie);
 setup_auto_login($id);
 json($userid, $day, $time);

 exit;
}

?>

<!docutype html>
<html>
<head>
  <meta charset="UTF-8">
  <title>サンプルアプリケーション</title>
  </head>
  <body>
  <h1>ログイン機能　サンプルアプリケーション</h1>
  <!-- $_SERVER['PHP_SELF']はXSSの危険性があるので、actionは空にしておく -->
  <!--<form id="loginForm" name="loginForm" action="<?php print($_SERVER['PHP_SELF']) ?>" method="POST">-->
  <form id="loginForm" name="loginForm" action="" method="POST">
  <fieldset>
  <legend>ログインフォーム</legend>
  <div><?php echo $errorMessage ?></div>
  <label for="userid">ユーザID</label><input type="text" id="userid" name="userid" value="<?php echo htmlspecialchars($_POST["userid"], ENT_QUOTES); ?>">
  <br>
  <label for="password">パスワード</label><input type="password" id="password" name="password" value="">
  <br>
  <label for="day">更新日時</label><input type="reday" id="reday" name="reday" value="">
  <br>
  <label for="token">Token</label><input type="token" id="token" name="token" value="">
  <input type="submit" id="login" name="login" value="ログイン">
  </fieldset>
  </form>
  </body>
</html>

