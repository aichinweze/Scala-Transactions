package Solution

case class Transaction(
                        transactionId: String,
                        accountId: String,
                        transactionDay: Int,
                        category: String,
                        transactionAmount: Double)

object Transaction {
  def getDailyTransactionSums(list: List[Transaction]): Map[Int, Double] = {
    // Filter transaction columns to tuple2: (transactionDay, transactionAmount)
    list.map(x => (x.transactionDay, x.transactionAmount))
      // Group tuples into a map with transaction day as key and list of transaction amounts as value
      .groupBy(_._1)
      .map { case (k,v) => (k,v.map(_._2))}
      // Map where key is transactionDay and value is sum of transactionAmounts in that day
      .map(x => (x._1, x._2.sum))
  }

  // Create a HashMap that relates accountID (key) to a Set of Tuple2: (Category, Average Transaction Value)
  def getAccountAverages(list: List[Transaction]): Map[String, Set[(String, Double)]] = {
    val sortedAccounts = Map[String, List[(String, Double)]]() ++
      list.map(x => (x.accountId, x.category, x.transactionAmount))
        .groupBy(_._1)
        .map { case (k, v) => k -> (v.map(_._2), v.map(_._3)).zipped
          .toList}

    def createAccountAvg(list: List[(String, Double)]): Set[(String, Double)] = {
      val groupedTrans: Map[String, List[Double]] = list.groupBy(_._1)
        .map { case (k,v) => k -> v.map(_._2)}
      groupedTrans.map(x => (x._1, x._2.sum/x._2.size))
        .toSet
    }
    Map[String, Set[(String, Double)]]() ++ sortedAccounts.map { case (k, v) => k -> createAccountAvg(v)}
  }

  // Create a HashSet that stores all distinct accountIDs in the list of transactions
  def getCustomerList(list: List[Transaction]): List[String] = {
    def orderSet(set: Set[String]): List[String] = {
      val range = 1 to set.size
      range.flatMap { num =>
        set.filter(_.replace("A", "").toInt == num)
      }
      }.toList
    orderSet(list.map(x => x.accountId).toSet)
  }

  // Create a List[List[Window]] using the input list that stores (Day window represents, Map(AccountID, List[Transactions in Window])
  def getWindowStats(list: List[(Int, Map[String, List[Transaction]])]): List[List[Window]] = {
    list.map { entry =>
      entry._2.map(x => Window(entry._1,
        x._1,
        x._2.map(a => a.transactionAmount).max,
        x._2.map(a => a.transactionAmount).sum/x._2.map(a => a.transactionAmount).size,
        x._2.filter(a => a.category == "AA").map(b => b.transactionAmount).sum,
        x._2.filter(a => a.category == "CC").map(b => b.transactionAmount).sum,
        x._2.filter(a => a.category == "FF").map(b => b.transactionAmount).sum)).toList
    }.toList
  }
}
