shellPrompt := { state =>
  object devnull extends ProcessLogger {
    def info (s: => String) {}
    def error (s: => String) { }
    def buffer[T] (f: => T): T = f
  }
  val currBranch = (
    ("git status -sb" lines_! devnull headOption)
      getOrElse "-" stripPrefix "## "
  )
  val p = Project.extract(state)
  val projectNameOrId = p.getOpt(sbt.Keys.name) getOrElse { p.currentProject.id }
  val projectVersion = p.getOpt(sbt.Keys.version) getOrElse { "" }
  import scala.Console.RESET
  val LYELLOW="\033[1;33m"
  val LGREEN="\033[1;32m"
  val LBLUE="\033[01;34m"
  ("%s%s%s:%s%s%s:%s%s%s> ").format(
    LGREEN, projectNameOrId, RESET,
    LBLUE, projectVersion, RESET,
    LYELLOW,currBranch, RESET)
}
