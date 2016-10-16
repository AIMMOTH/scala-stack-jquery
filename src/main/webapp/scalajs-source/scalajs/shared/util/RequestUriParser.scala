package scalajs.shared.util

import scala.util.parsing.combinator.RegexParsers

case class RequestUriTokens(path : Option[String], query : Option[String])

/**
 * Useful parser when using HttpServletRequest.getRequestUri()
 * 
 * @see javax.servlet.http.HttpServletRequest.getRequestUri()
 */
class RequestUriParser extends RegexParsers {

  private val requestUriParser = someRequest

  val notQuestionmark = """[^\?]*""".r
  val any = """.*""".r
  
  def someRequest = "/" ~> opt(notQuestionmark) ~ opt("?" ~> any) ^^ {
    case path ~ query => new RequestUriTokens(path, query)
  }

  def applyOnUri(uri : String) = parseAll(requestUriParser, uri) match {
      case Success(result, _)  => Right(result)
      case Failure(failure, _) => Left(failure)
      case Error(error, _)     => Left(error)
    }
}

object RequestUriParser {
  lazy val requestUriParser = new RequestUriParser

  def apply(uri : String) = requestUriParser.applyOnUri(uri)
}