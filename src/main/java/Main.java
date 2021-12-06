import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;
import java.io.FileNotFoundException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.tartarus.snowball.ext.PorterStemmer;

import edu.stanford.nlp.simple.Sentence;

import org.apache.lucene.document.Document;
public class Main {
    static String path;
    static String indexPath = "src/main/resources/index111";
    //static String indexPath = "index/lemmaNoTpl";

    static {
        path = "src/main/resources/wiki-subset-20140602";
    }


    public static void main(String[] args)throws FileNotFoundException, IOException {
        IndexCreator ic = new IndexCreator(indexPath);
        String query= "src/main/resources/questions.txt";
        String indexPathWithStemLemma="src/main/resources/indexwithstemlemma";
        //ic.parseFiles(path);
        //parseFiles(path);
        QueryProcess qp = new QueryProcess(query,indexPathWithStemLemma, new BM25Similarity(), false);
        String answer = qp.evalQuery();
        System.out.println(answer);
        System.out.println("Implement Reranking");
        QueryProcess qp1 = new QueryProcess(query,indexPathWithStemLemma, new BM25Similarity(), true);
        String answer1 = qp1.evalQuery();
        System.out.println(answer1);


//        String indexPathwithoutstemwithoutlemma="src/main/resources/index";
//        String indexPathWithStemLemma="src/main/resources/indexwithstemlemma";
//        String indexPathLemma="src/main/resources/indexlemma";
//        String indexPathStem="src/main/resources/indexstemm";

//        System.out.println("Running queries on index without lemma and without stemming");
//        System.out.println("With BM25 Similarity");
//        QueryProcess qp = new QueryProcess(query,indexPathwithoutstemwithoutlemma,new BM25Similarity());
//        qp.evalQuery();
//        System.out.println("With Boolean Similarity");
//        QueryProcess qpwithbool = new QueryProcess(query,indexPathWithStemLemma, new BooleanSimilarity());
//        qpwithbool.evalQuery();
//        System.out.println("With Jelenick Mercer Similarity");
//        QueryProcess qpwithJLM = new QueryProcess(query,indexPathWithStemLemma,new LMJelinekMercerSimilarity((float) 0.5));
//        qpwithJLM.evalQuery();
//        System.out.println("*****************************************************************");
//        System.out.println("Running queries on index with lemma and with stemming");
//        System.out.println("With Boolean Similarity");
//        QueryProcess qp1withbool = new QueryProcess(query,indexPathWithStemLemma, new BooleanSimilarity());
//        qp1withbool.evalQuery();
//        System.out.println("With BM25 Similarity");
//        QueryProcess qp1withBm25 = new QueryProcess(query,indexPathWithStemLemma, new BM25Similarity());
//        qp1withBm25.evalQuery();
//        System.out.println("With Jelenick Mercer Similarity");
//        QueryProcess qp1withJLM = new QueryProcess(query,indexPathWithStemLemma,new LMJelinekMercerSimilarity((float) 0.5));
//        qp1withJLM.evalQuery();
//        System.out.println("*****************************************************************");
//        System.out.println("Running queries on index with lemma and no stemming");
//        System.out.println("With Boolean Similarity");
//        QueryProcess qp2withbool = new QueryProcess(query,indexPathLemma, new BooleanSimilarity());
//        qp2withbool.evalQuery();
//        System.out.println("With BM25 Similarity");
//        QueryProcess qp2withBm25 = new QueryProcess(query,indexPathLemma, new BM25Similarity());
//        qp2withBm25.evalQuery();
//        System.out.println("With Jelenick Mercer Similarity");
//        QueryProcess qp2withJLM = new QueryProcess(query,indexPathLemma,new LMJelinekMercerSimilarity((float) 0.5));
//        qp2withJLM.evalQuery();
//        System.out.println("*****************************************************************");
//        System.out.println("Running queries on index with no lemma and stemming");
//        System.out.println("With Boolean Similarity");
//        QueryProcess qp3withbool = new QueryProcess(query,indexPathStem, new BooleanSimilarity());
//        qp3withbool.evalQuery();
//        System.out.println("With BM25 Similarity");
//        QueryProcess qp3withBm25 = new QueryProcess(query,indexPathStem);
//        qp3withBm25.evalQuery();
//        System.out.println("With Jelenick Mercer Similarity");
//        QueryProcess qp3withJLM = new QueryProcess(query,indexPathStem,new LMJelinekMercerSimilarity((float) 0.5));
//        qp3withJLM.evalQuery();


    }


}
