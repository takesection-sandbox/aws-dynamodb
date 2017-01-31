package jp.pigumer.dynamodb

import java.util.UUID

import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.{AmazonDynamoDB, AmazonDynamoDBClientBuilder}
import org.specs2.mutable.Specification

import scala.collection.JavaConverters._

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

        repo.save(UUID.randomUUID().toString, "foo")
        repo.save(UUID.randomUUID().toString, "bar")

        val rec = repo.findBy("test").iterator().asScala
        rec.foreach(i => System.out.println(i.toJSON))
      }
      finally {
        repo.dropTable
      }
      success
    }
  }

}
