package Solution

case class Window(
                   windowDay: Int,
                   accountId: String,
                   max: Double,
                   average: Double,
                   aaTotal: Double,
                   ccTotal: Double,
                   ffTotal: Double)

object Window {
  val windowSize: Int = 5

  // Return data contained within a window object as a string
  def formatStatStrings(window: Window): String = {
    "Day: " + window.windowDay.toString +
      "\t\tAccount ID: " + window.accountId +
      "\t\tMaximum: " + window.max.toString +
      "\t\tAverage: " + window.average.toString +
      "\t\tAA Total Value: " + window.aaTotal.toString +
      "\t\tCC Total Value: " + window.ccTotal.toString +
      "\t\tFF Total Value: " + window.ffTotal.toString
  }

  // Return a list of transactions for a day. From this list, statistics will be calculated for each account
  def getWindows(list: List[Transaction], currDay: Int): List[Transaction] =
    currDay match {
      case x if x <= windowSize => Nil
      case _ => list.filter(x => x.transactionDay <= currDay - 1 && x.transactionDay >= currDay - 5)
    }
}
