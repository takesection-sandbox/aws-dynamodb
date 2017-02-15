package jp.pigumer.dynamodb

object Data {

  def list = Seq(
    StockItems("0001", "2017-02-01", 100),
    StockItems("0001", "2017-02-02", 9),
    StockItems("0002", "2017-02-03", 5),
    StockItems("0003", "2017-02-01", 20),
    StockItems("0001", "2017-02-03", 30),
    StockItems("0002", "2017-02-02", 4),
    StockItems("0003", "2017-02-02", 5),
    StockItems("0001", "2017-02-04", 7),
    StockItems("0002", "2017-02-01", 89),
    StockItems("0001", "2017-02-05", 23)
  )
}
