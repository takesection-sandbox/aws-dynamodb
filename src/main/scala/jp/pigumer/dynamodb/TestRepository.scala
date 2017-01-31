package jp.pigumer.dynamodb

import com.amazonaws.services.dynamodbv2.document.{DynamoDB, Item, Table}
import com.amazonaws.services.dynamodbv2.model._

trait TestRepository {

  val dynamoDB: DynamoDB

  val tableName = "test"

  def createTable: Table = {
    val request = new CreateTableRequest()
      .withAttributeDefinitions(
        new AttributeDefinition("id", ScalarAttributeType.S),
        new AttributeDefinition("subId", ScalarAttributeType.S))
      .withKeySchema(new KeySchemaElement("id", KeyType.HASH))
      .withProvisionedThroughput(new ProvisionedThroughput(1l, 1l))
      .withTableName(tableName)

    request.withGlobalSecondaryIndexes(
      Seq(new GlobalSecondaryIndex().
        withIndexName("subIdIndex").
        withProvisionedThroughput(new ProvisionedThroughput(1l, 1l)).
        withKeySchema(new KeySchemaElement("subId", KeyType.HASH)).
        withProjection(new Projection().withProjectionType(ProjectionType.ALL))): _*)

    dynamoDB.createTable(request)
  }

  def dropTable = {
    val table = dynamoDB.getTable(tableName)
    table.delete()
  }

  def save(id: String, value: String) = {
    val table = dynamoDB.getTable(tableName)
    table.putItem(new Item().
      withString("id", id).
      withString("subId", "test")
      .withString("value", value)
      )
  }

  def findBy(subId: String) = {
    val table: Table = dynamoDB.getTable(tableName)
    val index = table.getIndex("subIdIndex")
    index.query("subId", subId)
  }

}
