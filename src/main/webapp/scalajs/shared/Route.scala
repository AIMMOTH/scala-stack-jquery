package shared

import shared.html._
import org.slf4j.LoggerFactory
import org.slf4j.Logger
import scalatags.Text.TypedTag
import org.glassfish.jersey.uri.UriTemplate

object Route {
  
  def apply(uri: String)(implicit logger : Logger = LoggerFactory.getLogger(getClass)) : Either[String, TypedTag[String]] = {
    
    val paths = scala.collection.mutable.Seq()
    val template = new UriTemplate("/{language}/{page}");
    
    import scala.collection.JavaConversions._
    template.`match`(uri, paths)
    
    logger.info(s"mapping $paths")
    
    uri match {
      case "/" => Left(s"/${Languages.default.code}/")
      case route if route.startsWith("""/\s\s-\s\s/""") => Right(Index(new Stylisch, minified = route.endsWith(".min.html")))
      case "/404" => Right(ClientError.NotFound())
      case "/5xx" => Right(ServerError.InternalServerError())
      case _ => Left("/404")
    }
  }
}