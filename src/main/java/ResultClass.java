import org.apache.lucene.document.Document;

import javax.print.Doc;


public class ResultClass {

    Document docName;
    double docscore = 0;
    public ResultClass(Document doc, double score){
        this.docName=doc;
        this.docscore=score;

    }
    ResultClass(){

    }

}
