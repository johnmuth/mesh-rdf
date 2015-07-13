package com.springer.pubmed

import java.io.File

import org.rogach.scallop.{ScallopConf, Subcommand}

object PubmedClientApp {

  def main(args: Array[String]) {

    object Conf extends ScallopConf(args) {
      val fetchArticles = new Subcommand("fetch-articles") {
        val issns = opt[String]("issns", required=true)
        val maxRecords = opt[Int]("max-records", required=false, default = Option(100000))
        val start = opt[Int]("start", required=false, default = Option(0))
        val format = opt[String]("format", required=true)
        val outputFile = opt[String]("output-file", required=true)
      }
    }

    Conf.subcommand match {
      case Some(Conf.fetchArticles) => {
        val outputFormat = Conf.fetchArticles.format.get.get
        if (!ValidFormats.contains(outputFormat)) {
          println(s"Valid formats: ${ValidFormats}")
          System.exit(1)
        }

        val pubmedClient = new PubmedClient
        val articles: Seq[PubmedArticle] = pubmedClient.getPubmedArticles(Conf.fetchArticles.issns.get.get.split(","), Conf.fetchArticles.maxRecords.get.get, Conf.fetchArticles.start.get.get)
        println(s"Got ${articles.size} articles")
        PubmedArticlesTriples(articles).save(new File(Conf.fetchArticles.outputFile.get.get), outputFormat)
      }
      case _ =>
        Conf.printHelp()
        System.exit(1)
    }
  }
  private val ValidFormats = Seq("TURTLE","TTL","Turtle","N-TRIPLES","N-TRIPLE","NT","RDF/XML-ABBREV","RDF/XML","N3","JSON-LD","RDF/JSON","RDF/JSON")

}

