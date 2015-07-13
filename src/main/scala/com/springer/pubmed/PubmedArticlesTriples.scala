package com.springer.pubmed

import java.io.{FileOutputStream, File}
import com.hp.hpl.jena.rdf.model.ModelFactory
import com.hp.hpl.jena.vocabulary.RDF

case class PubmedArticlesTriples(pubmedArticles:Seq[PubmedArticle]) {

  def save(file:File, format:String): Unit = {
    addPubmedArticlesTriples()
    model.write(new FileOutputStream(file), format)
  }

  private lazy val model = ModelFactory.createDefaultModel()

  private def addPubmedArticlesTriples(): Unit = {
    //TODO: do something else when article is missing doi?
    pubmedArticles.filter(_.doi.nonEmpty).foreach{ pubmedArticle => {
      val pubmedArticleResource = model.createResource(s"http://dx.doi.org/${pubmedArticle.doi}").addProperty(RDF.`type`, FaBiO.JournalArticle)
      pubmedArticle.meshHeadings.foreach( meshHeading => {
        if (meshHeading.descriptorName.isMajor) {
          pubmedArticleResource.addProperty(FaBiO.hasSubjectTerm, model.createResource("http://id.nlm.nih.gov/mesh/" + meshHeading.descriptorName.ui))
        }
        meshHeading.qualifierNames.filter(_.isMajor).foreach(qn => {
          pubmedArticleResource.addProperty(FaBiO.hasSubjectTerm, model.createResource("http://id.nlm.nih.gov/mesh/" + meshHeading.descriptorName.ui + qn.ui))
        })
      })
    }}
  }

  object FaBiO {
    val JournalArticle = model.createResource(fabio+"JournalArticle")
    val hasSubjectTerm = model.createProperty(fabio, "hasSubjectTerm")
    private lazy val model = ModelFactory.createDefaultModel()
    private lazy val fabio = "http://purl.org/spar/fabio/"
  }
}

