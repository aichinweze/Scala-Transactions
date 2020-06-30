package Solution

import java.nio.file.{Files, Paths}

import scala.collection.SortedMap
import scala.io.Source

object Demo {
  def main(args: Array[String]): Unit = {
    // The full path to the file to import
    // val fileName = "C:\\PATH_TO_TEXT_FILE\\transactions.txt"
    val fileName = "C:\\Users\\IChinweze\\IdeaProjects\\Quantexa_Transactions\\src\\Solution\\transactions.txt"

    // The lines of the CSV file (dropping the first to remove the header)
    val transactionslines = Source.fromFile(fileName).getLines().drop(1)

    // Split each line up by commas and construct Transactions
    val transactions: List[Transaction] = transactionslines.map { line =>
      val split = line.split(',')
      Transaction(split(0), split(1), split(2).toInt, split(3), split(4).toDouble)
    }.toList

    // Q1
    val listOfTransactionSums = Transaction.getDailyTransactionSums(transactions)

    // The value of numberOfDays is the number of days represented in the list of transactions
    val numberOfDays = listOfTransactionSums.size
    val rangeOfDays = 1 to numberOfDays

    // customers is a HashSet consistent of all the distinct account IDs represented in the list of transactions
    val customers: List[String] = Transaction.getCustomerList(transactions)

    // Print Q1 an ordered form of the results (listOfTransactionSums) to a .txt file
    // val q1Path = "C:\\PATH_TO_SRC_FOLDER\\src\\Solution\\Q1.txt"
    val q1Path = "C:\\Users\\IChinweze\\IdeaProjects\\Quantexa_Transactions\\src\\Solution\\Q1.txt"
    writeResultsToFile(getQ1ResultsToStrings(listOfTransactionSums, numberOfDays), q1Path)


    // Q2
    // Print an ordered form of the results (Transaction.getAccountAverages(transactions)) to a .txt file
    // val q2Path = "C:\\PATH_TO_SRC_FOLDER\\src\\Solution\\Q2.txt"
    val q2Path = "C:\\Users\\IChinweze\\IdeaProjects\\Quantexa_Transactions\\src\\Solution\\Q2.txt"
    writeResultsToFile(getQ2ResultsToStrings(Transaction.getAccountAverages(transactions)), q2Path)


    // Q3
    // windowStats is a List[List[Window]]. Each day has a List[Window] holding statistics for each account
    val windowStats = Transaction.getWindowStats(rangeOfDays
                                                 // Map each day to a Map relating each day (key) to a List[Window] (value)
                                                 .map(day => (day, Window.getWindows(transactions, day).toList))
                                                 .map(x => (x._1, x._2.groupBy(_.accountId))).toList)
    // val q3Path = "C:\\PATH_TO_SRC_FOLDER\\src\\Solution\\Q3.txt"
    val q3Path = "C:\\Users\\IChinweze\\IdeaProjects\\Quantexa_Transactions\\src\\Solution\\Q3.txt"
    writeResultsToFile(getQ3ResultsToStrings(windowStats, customers).flatten, q3Path)
  }

  // Write contents of string to a txt file on the specified path
  def writeResultsToFile(data: List[String], outPath: String): Unit = {
    Files.write(Paths.get(outPath), data.mkString("\n").getBytes)
  }

  // Get the data from Q1 into a series of strings so that it can be printed to a file
  def getQ1ResultsToStrings(map: Map[Int, Double], length: Int): List[String] = {
    val sortedMap = SortedMap[Int, Double]() ++ map
    sortedMap.map(x => "Day: " + x._1.toString + "\t Total Transaction Value: " + x._2.toString).toList
  }

  // Get the data from Q2 into a series of strings so that it can be printed to a file
  def getQ2ResultsToStrings(map: Map[String, Set[(String, Double)]]): List[String] = {
    def orderResults(mapIn: Map[String, Set[(String, Double)]]): List[Map[String, Set[(String, Double)]]] = {
      val range = 1 to mapIn.size
      range.map { num =>
        mapIn.filter(_._1.replace("A", "").toInt == num)
      }.toList
    }
    orderResults(map).map { x =>
        val categoryStrings = x.head._2
                               .toList.sortBy(_._1)
                               .map(x => "Average " + x._1 + " Value: " + x._2.toString)
        "Account ID: " + x.head._1 + "\t\t" + categoryStrings.mkString("\t\t\t")
    }
  }

  // Get the data from Q3 into a series of strings so that it can be printed to a file
  def getQ3ResultsToStrings(window: List[List[Window]], customers: List[String]): List[List[String]] = {
      // Convert List[Window] from this day to a List[String]. Each String contains statistics for a distinct account ID
      def getDailyStrings(day: Int, customers: List[String], currentWindow: List[Window]): List[String] = {
        customers.map {
          case a if currentWindow.filter(_.accountId == a) != Nil =>
            Window.formatStatStrings(currentWindow.filter(_.accountId == a).head)
          case a if currentWindow.filter(_.accountId == a) == Nil =>
            Window.formatStatStrings(Window(day, a, 0, 0, 0, 0, 0))
          case _ => "Insufficient data to provide output"
        }.toList
      }

      // Order the list of windows by accountID
      def sortByAccountID(list: List[Window]): List[Window] = {
        val range = 1 to list.size
        range.flatMap { num =>
          list.filter(_.accountId.replace("A", "").toInt == num)
        }
      }.toList

      // mapIn: Create a sortedMap where the key is the day that the List[Window] belongs to
      val mapIn = SortedMap[Int, List[Window]]() ++ window.flatMap(x => sortByAccountID(x)).groupBy(_.windowDay)
      val range = 1 to window.size
      range.map{
        case x if x > Window.windowSize  =>
          getDailyStrings(x, customers, mapIn(x))
        // If the current day is less than the value for the length of a window, there is not enough data available
        case _ => List("Insufficient Data to create a window")
      }.toList
  }
}
