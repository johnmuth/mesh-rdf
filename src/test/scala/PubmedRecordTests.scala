
import com.springer.pubmed.{MeshHeadings, PubmedArticles}
import org.scalatest.{FunSpec, Matchers}

import scala.xml.{Elem, XML}

class PubmedRecordTests extends FunSpec with Matchers {

   describe("PubmedRecord") {
     it("is instantiated from efetch xml") {
       val elem: Elem = XML.loadFile("src/test/resources/pubmed-article-set.xml")
       val pubmedRecords = PubmedArticles(elem)
       pubmedRecords.size should be(1)
       val actualArticle1 = pubmedRecords(0)
       actualArticle1.pmid should be("20336356")
       actualArticle1.title should be("The Na-K-Cl Co-transporter in astrocyte swelling.")
       actualArticle1.doi should be("10.1007/s11011-010-9180-3")
       actualArticle1.year should be("2010")
       actualArticle1.articleAbstract should be("Ion channels, exchangers and transporters are known to be involved in cell volume regulation. A disturbance in one or more of these systems may result in loss of ion homeostasis and cell swelling. In particular, activation of the Na(+)-K(+)-Cl(-) cotransporters has been shown to regulate cell volume in many conditions. The Na(+)-K(+)-Cl- cotransporters (NKCC) are a class of membrane proteins that transport Na, K, and Cl ions into and out of a wide variety of epithelial and nonepithelial cells. Studies have established the role of NKCC1 in astrocyte swelling/brain edema in ischemia and trauma. Our recent studies suggest that NKCC1 activation is also involved in astrocyte swelling induced by ammonia and in the brain edema in the thioacetamide model of acute liver failure. This review will focus on mechanisms of NKCC1 activation and its contribution to astrocyte swelling/brain edema in neurological disorders, with particular emphasis on ammonia neurotoxicity and acute liver failure.")
     }
   }

   describe("MeshHeadings") {
     it("extracts MeshHeadings from xml") {
       val elem: Elem = XML.loadString("""<MeshHeadingList>
                                         |            <MeshHeading>
                                         |                <DescriptorName MajorTopicYN="N" UI="D000641">Ammonia</DescriptorName>
                                         |                <QualifierName MajorTopicYN="N" UI="Q000378">metabolism</QualifierName>
                                         |                <QualifierName MajorTopicYN="N" UI="Q000633">toxicity</QualifierName>
                                         |            </MeshHeading>
                                         |            <MeshHeading>
                                         |                <DescriptorName MajorTopicYN="N" UI="D000818">Animals</DescriptorName>
                                         |            </MeshHeading>
                                         |            <MeshHeading>
                                         |                <DescriptorName MajorTopicYN="N" UI="D001253">Astrocytes</DescriptorName>
                                         |                <QualifierName MajorTopicYN="Y" UI="Q000378">metabolism</QualifierName>
                                         |                <QualifierName MajorTopicYN="N" UI="Q000473">pathology</QualifierName>
                                         |            </MeshHeading>
                                         |            <MeshHeading>
                                         |                <DescriptorName MajorTopicYN="N" UI="D001921">Brain</DescriptorName>
                                         |                <QualifierName MajorTopicYN="Y" UI="Q000378">metabolism</QualifierName>
                                         |                <QualifierName MajorTopicYN="N" UI="Q000473">pathology</QualifierName>
                                         |                <QualifierName MajorTopicYN="N" UI="Q000503">physiopathology</QualifierName>
                                         |            </MeshHeading>
                                         |            <MeshHeading>
                                         |                <DescriptorName MajorTopicYN="N" UI="D001929">Brain Edema</DescriptorName>
                                         |                <QualifierName MajorTopicYN="Y" UI="Q000378">metabolism</QualifierName>
                                         |                <QualifierName MajorTopicYN="N" UI="Q000503">physiopathology</QualifierName>
                                         |            </MeshHeading>
                                         |            <MeshHeading>
                                         |                <DescriptorName MajorTopicYN="N" UI="D048429">Cell Size</DescriptorName>
                                         |            </MeshHeading>
                                         |            <MeshHeading>
                                         |                <DescriptorName MajorTopicYN="N" UI="D004195">Disease Models, Animal</DescriptorName>
                                         |            </MeshHeading>
                                         |            <MeshHeading>
                                         |                <DescriptorName MajorTopicYN="N" UI="D006501">Hepatic Encephalopathy</DescriptorName>
                                         |                <QualifierName MajorTopicYN="Y" UI="Q000378">metabolism</QualifierName>
                                         |                <QualifierName MajorTopicYN="N" UI="Q000473">pathology</QualifierName>
                                         |                <QualifierName MajorTopicYN="N" UI="Q000503">physiopathology</QualifierName>
                                         |            </MeshHeading>
                                         |            <MeshHeading>
                                         |                <DescriptorName MajorTopicYN="N" UI="D006801">Humans</DescriptorName>
                                         |            </MeshHeading>
                                         |            <MeshHeading>
                                         |                <DescriptorName MajorTopicYN="N" UI="D017114">Liver Failure, Acute</DescriptorName>
                                         |                <QualifierName MajorTopicYN="N" UI="Q000139">chemically induced</QualifierName>
                                         |                <QualifierName MajorTopicYN="N" UI="Q000150">complications</QualifierName>
                                         |                <QualifierName MajorTopicYN="N" UI="Q000503">physiopathology</QualifierName>
                                         |            </MeshHeading>
                                         |            <MeshHeading>
                                         |                <DescriptorName MajorTopicYN="N" UI="D018384">Oxidative Stress</DescriptorName>
                                         |                <QualifierName MajorTopicYN="N" UI="Q000502">physiology</QualifierName>
                                         |            </MeshHeading>
                                         |            <MeshHeading>
                                         |                <DescriptorName MajorTopicYN="N" UI="D028021">Sodium-Potassium-Chloride Symporters</DescriptorName>
                                         |                <QualifierName MajorTopicYN="N" UI="Q000235">genetics</QualifierName>
                                         |                <QualifierName MajorTopicYN="Y" UI="Q000378">metabolism</QualifierName>
                                         |            </MeshHeading>
                                         |            <MeshHeading>
                                         |                <DescriptorName MajorTopicYN="N" UI="D064506">Solute Carrier Family 12, Member 2</DescriptorName>
                                         |            </MeshHeading>
                                         |        </MeshHeadingList>
                                         |""".stripMargin)
       val meshHeadings = MeshHeadings(elem)
       meshHeadings.size should be(13)
       meshHeadings(0).descriptorName.isMajor should be(false)
       meshHeadings(0).descriptorName.ui should be("D000641")
       meshHeadings(0).descriptorName.value should be("Ammonia")
       meshHeadings(0).qualifierNames.size should be(2)
       meshHeadings(0).qualifierNames(0).isMajor should be(false)
       meshHeadings(0).qualifierNames(0).ui should be("Q000378")
       meshHeadings(0).qualifierNames(0).value should be("metabolism")

       meshHeadings(2).qualifierNames(0).isMajor should be(true)

     }
   }
 }
