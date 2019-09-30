import sbt._
import sbt.Keys._
import scala.sys.process._
import scala.util.Try

shellPrompt := { state =>
  object devnull extends ProcessLogger {
    def err(s: => String): Unit = ()
    def out(s: => String): Unit = ()
    def buffer[T] (f: => T): T = f
  }

  val currBranch: String =
    Try("git status -sb" lineStream_! devnull headOption).toOption match {
    case Some(line) => line.getOrElse("-").stripPrefix("## ")
    case None => "-"
  }

  val p = Project.extract(state)
  val projectNameOrId = p.getOpt(sbt.Keys.name).getOrElse(p.currentProject.id)
  val projectVersion = p.getOpt(sbt.Keys.version).getOrElse("")
  
  import scala.io.AnsiColor._

  "%s%s%s:%s%s%s:%s%s%s> ".format(
    GREEN, projectNameOrId, RESET,
    BLUE, projectVersion, RESET,
    YELLOW, currBranch, RESET)
}
