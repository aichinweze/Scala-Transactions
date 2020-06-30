Scala Transactions
==================

This project uses functional programming (in Scala) to perform processing functions on a set of fictional bank transcation data. 
An executable file can be compiled from the code stored within `src/Solution/Demo.scala`. Absolute paths need to be given for the 
transaction data stored in `src/Solution/transactions.txt`.

Data
----

The data provided is stored within `transactions.txt` and consists of 991 transactions presented in a comma separated format and 
spread over a month. The transactions are for multiple accounts and there are multiple types of transaction. The file has the 
following columns:

    * transactionId: String representing the id of a transaction
    * accountId: String representing the id of the account which made the transaction transaction
    * day: Integer representing the day the transaction was made on (time information was removed)
    * category: String representing the type of category of the transaction 
    * transactionAmount: Double representing the value of the transaction


Output
------

`Demo.scala` seeks to output its results to three text files. The absolute path of these files on the computer must be added to the 
appropriate String variables in the file itself before compiling and running this program. 

1) Output 1: Calculates the total transaction value for all transactions performed each day over the month. The output contains one 
line for each day and each line includes the day and the total value.
2) Output 2: Calculates the average value of transactions per account for each type of transaction (there are seven in total). The output 
presents one line per account and each line includes the account id and the average value for each transaction type (ie 7 fields containing the average values).
3) Output 3: For each day, each account has transaction statistics calculated for five days prior (not including transactions from the day
itself). The output has one line per day per account id and each line has each of the calculated statistics. The statistics are:
      * The maximum transaction value in the previous 5 days of transactions per account
      * The average transaction value of the previous 5 days of transactions per account
      * The total transaction value of transactions types “AA”, “CC” and “FF” in the previous 5 days per account
      
