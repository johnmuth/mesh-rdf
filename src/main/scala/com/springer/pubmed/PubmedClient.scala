package com.springer.pubmed

import java.net.URLEncoder
import org.apache.http.client.methods.HttpGet
import org.apache.http.config.SocketConfig
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.http.util.EntityUtils
import org.slf4j.LoggerFactory
import scala.xml.XML

class PubmedClient {

  def getPubmedArticles(issns:Seq[String], maxRecords:Int, start:Int): Seq[PubmedArticle] = {
    val matchingPubmedIds: Seq[String] = findMatchIds(issns, maxRecords, start)
    matchingPubmedIds.grouped(100).map(batch => {
      fetchPubmedArticles(batch)
    }) reduce (_ ++ _)
  }

  private def findMatchIds(issns:Seq[String], maxRecords:Int, start:Int) : Seq[String] = {
    val url = PubmedSearchUrl(issns, maxRecords, start).url
    logger.debug(s"findMatchIds: url=$url")
    try {
      logger.debug("About to do httpClient.execute")
      val response = httpClient.execute(new HttpGet(url))
      if (response.getStatusLine.getStatusCode != 200) {
        logger.debug(s"Non-200 response from ${url}: ${response}")
        EntityUtils.consumeQuietly(response.getEntity)
        Seq()
      } else {
        logger.debug(s"200 response from ${url}")
        val responseString = EntityUtils.toString(response.getEntity, "UTF-8")
        logger.trace(s"responseString : $responseString")
        val responseElem = XML.loadString(responseString)
        val ids = (responseElem \\ "IdList" \ "Id").map(_.text)
        logger.debug(s"pmids: $ids")
        return ids
      }
    } catch {
      case t: Throwable => {
        logger.error(s"Error requesting or parsing response from ${url}", t)
        Seq()
      }
    }
  }

  private def fetchPubmedArticles(ids:Seq[String]) : Seq[PubmedArticle] = {
    val url = PubmedFetchUrl(ids).url
    logger.debug(s"getRecords: url=$url")
    try {
      logger.debug("About to do httpClient.execute")
      val response = httpClient.execute(new HttpGet(url))
      if (response.getStatusLine.getStatusCode != 200) {
        logger.debug(s"Non-200 response from ${url}: ${response}")
        EntityUtils.consumeQuietly(response.getEntity)
        Seq()
      } else {
        logger.debug(s"200 response from ${url}")
        val responseString = EntityUtils.toString(response.getEntity, "UTF-8")
        logger.trace(s"responseString : $responseString")
        PubmedArticles(XML.loadString(responseString))
      }
    } catch {
      case t: Throwable => {
        logger.error(s"Error requesting or parsing response from ${url} : ${t.getMessage}", t)
        Seq()
      }
    }
  }

  private def httpClient = {
    try {
      val httpClient = clientBuilder.build()
      httpClient
    } catch {
      case t:Throwable => {
        logger.error("Error creating httpClient", t)
        throw t
      }
    }
  }

  private lazy val clientBuilder = {
    val cb = HttpClientBuilder.create()
    val cxMgr = new PoolingHttpClientConnectionManager()
    cxMgr.setMaxTotal(20)
    cxMgr.setDefaultMaxPerRoute(20)
    cxMgr.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(30000).build())
    cb.setConnectionManager(cxMgr)
    cb
  }

  private lazy val logger = LoggerFactory.getLogger(getClass)

  case class PubmedSearchUrl(issns:Seq[String], maxToReturn:Int, offset:Int) {
    val url = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&retmode=xml&retmax=" + maxToReturn + "&retstart=" + offset + "&term=(" + issns.map(issn => URLEncoder.encode(issn, "UTF-8")).mkString(")+OR+(") + ")&field=ISSN"
  }

  case class PubmedFetchUrl(ids:Seq[String]) {
    val url = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&retmode=xml&id=" + ids.mkString(",") + "&retmax=" + ids.size
  }

}

