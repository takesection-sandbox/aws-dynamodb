package jp.pigumer.dynamodb

import com.amazonaws.services.dynamodbv2.document.{DynamoDB, Item, Table}
import com.amazonaws.services.dynamodbv2.model._

import scala.collection.JavaConverters._
import scala.collection.mutable

trait TestRepository {

  val dynamoDB: DynamoDB

  val tableName = "test"

  def createTable: Table = {

    val request = new CreateTableRequest()
      .withAttributeDefinitions(
        new AttributeDefinition("id", ScalarAttributeType.S),
        new AttributeDefinition("date", ScalarAttributeType.S))
      .withKeySchema(new KeySchemaElement("id", KeyType.HASH))
      .withKeySchema(new KeySchemaElement("date", KeyType.RANGE))
      .withProvisionedThroughput(new ProvisionedThroughput(1l, 1l))
      .withTableName(tableName)

    dynamoDB.createTable(request)
  }

  def dropTable = {
    val table = dynamoDB.getTable(tableName)
    table.delete()
  }

  def save(item: StockItems) = {
    val table = dynamoDB.getTable(tableName)
    table.putItem(new Item().
      withString("id", item.id).
      withString("date", item.date).
      withInt("count", item.count)
    )
  }

  def sum(): Map[String, Int] = {
    val table = dynamoDB.getTable("test")
    table.scan().asScala.foldLeft(new mutable.LinkedHashMap[String, Int])(
      (map, item) => {
        val id = item.getString("id")
        val sum = item.getInt("count")
        map += id -> map.get(id).map(c => c + sum).getOrElse(sum)
      }
    ).toMap
  }
}
