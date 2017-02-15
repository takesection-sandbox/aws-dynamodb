package jp.pigumer.dynamodb

import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.{AmazonDynamoDB, AmazonDynamoDBClientBuilder}
import org.specs2.mutable.Specification

class TestRepositorySpec extends Specification {

  "TestRepository" should {

    val client: AmazonDynamoDB = {
      val builder = AmazonDynamoDBClientBuilder.standard()
      builder.build()
    }

    "test" in {
      val repo = new TestRepository {
        override val dynamoDB = new DynamoDB(client)
      }
      try {
        repo.createTable.waitForActive

        // Data 投入
        Data.list.foreach(item => repo.save(item))

        // サマリー
        repo.sum().
          foreach(t => {
            val t1 = t._1
            val t2 = t._2
            println(s"$t1: $t2")
          })

      }
      finally {
        repo.dropTable
      }
      success
    }
  }

}
