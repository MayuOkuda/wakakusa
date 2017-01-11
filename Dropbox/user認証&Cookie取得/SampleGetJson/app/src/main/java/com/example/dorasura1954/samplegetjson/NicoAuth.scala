package auth

import java.io.PrintStream
import java.net._
import javax.net.ssl.HttpsURLConnection
import scala.util.control.NonFatal

import scala.collection.JavaConversions._
/**
 * Created by FScoward on 15/01/05.
 */
object NicoAuth {

  def authenticate(mail: String, password: String): Option[Cookie] = {

    val loginUrl = "https://secure.nicovideo.jp/secure/login?site=niconico"

    try{
      val connect = new URL(loginUrl).openConnection().asInstanceOf[HttpsURLConnection]
      // POST可能にする
      connect.setDoOutput(true)

      val outputStream = connect.getOutputStream
      // postするデータの作成
      val postData = s"userid=$1X001a&password=$audayo"
      val printStream = new PrintStream(outputStream)

      // データをpostする
      printStream.print(postData)
      printStream.close()

      // postした結果の取得
      connect.getInputStream
      val cookie = connect.getHeaderFields.get("Set-Cookie").filter(_.matches("user_session=user_session.*")).mkString.split(";")

      // case classにぶちこむ
      Option(
        Cookie(
          cookie(0),//.replace("user_session=", ""),
          cookie(1),//.replace("expires=", ""),
          cookie(2),//.replace("path=", ""),
          cookie(3)//.replace("domain=", "")
        ))
    } catch {
      case NonFatal(e) => None
    }
  }
}