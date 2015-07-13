package com.springer.pubmed

import org.slf4j.LoggerFactory
import scala.xml.Node

case class PubmedArticle(
                          pmid: String,
                          title: String,
                          journal: String,
                          volume: String,
                          issue: String,
                          pages: String,
                          year: String,
                          articleAbstract: String,
                          doi: String,
                          meshHeadings: Seq[MeshHeading]
                          )

object PubmedArticles {

    def apply(node: Node): Seq[PubmedArticle] = {

    (node \\ "MedlineCitation").map(citElem => {
      val pmid = (citElem \ "PMID").text
      new PubmedArticle(
        pmid,
        (citElem \ "Article" \ "ArticleTitle").text,
        (citElem \ "Article" \ "Journal" \ "Title").text,
        (citElem \ "Article" \ "Journal" \ "JournalIssue" \ "Volume").text,
        (citElem \ "Article" \ "Journal" \ "JournalIssue" \ "Issue").text,
        (citElem \ "Article" \ "Pagination" \ "MedlinePgn").text,
        (citElem \ "Article" \ "Journal" \ "JournalIssue" \ "PubDate" \ "Year").text,
        getAbstract(citElem),
        getDoi(citElem),
        MeshHeadings(citElem)
      )
    })
  }

  private def getDoi(node: Node): String = (node \ "Article" \ "ELocationID").find(_.attribute("EIdType").find(_.text == "doi").isDefined).map(_.text).getOrElse("")

  private def getAbstract(node: Node): String = {
    (node \ "Article" \ "Abstract" \ "AbstractText").map(at => {
      val abstractSectionLabel = (at \ "@Label").text
      val asbtractSectionPrefix = if (abstractSectionLabel.nonEmpty) s"$abstractSectionLabel: " else ""
      asbtractSectionPrefix + at.text
    }).mkString(" ")
  }

  private lazy val logger = LoggerFactory.getLogger(getClass)

}

case class MeshHeading(descriptorName:DescriptorName, qualifierNames:Seq[QualifierName])
case class DescriptorName(isMajor:Boolean, ui:String, value:String)
case class QualifierName(isMajor:Boolean, ui:String, value:String)

object MeshHeadings {

  def apply(node: Node): Seq[MeshHeading] = {
    (node \\ "MeshHeading").map(mhNode => {
      val descriptorNameIsMajor = (mhNode \ "DescriptorName" \ "@MajorTopicYN").text match {
        case "Y" => true
        case _ => false
      }
      val descriptorNameUi = (mhNode \ "DescriptorName" \ "@UI").text
      val descriptorNameValue = (mhNode \ "DescriptorName" ).text
      val qualifierNames : Seq[QualifierName] = (mhNode \ "QualifierName").map(qnNode => {
        val qualifierNameIsMajor = (qnNode \ "@MajorTopicYN").text match {
          case "Y" => true
          case _ => false
        }
        val qualifierNameUi = (qnNode \ "@UI").text
        val qualifierNameValue = (qnNode).text
        QualifierName(qualifierNameIsMajor, qualifierNameUi, qualifierNameValue)
      })
      MeshHeading(DescriptorName(descriptorNameIsMajor, descriptorNameUi, descriptorNameValue), qualifierNames)
    })
  }

}
