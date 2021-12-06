import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class Testing {
    //System.out.println("Running queries");
    static String indexPathwithoutstemwithoutlemma="src/main/resources/index";
    static String query= "src/main/resources/questions.txt";

    @Test
    public void test() throws IOException{
    System.out.println("Running Test with index with no lemma and no stemming");
    QueryProcess qBest = new QueryProcess(query,indexPathwithoutstemwithoutlemma, new BM25Similarity(), false);
    QueryProcess qBestBool = new QueryProcess(query,indexPathwithoutstemwithoutlemma, new BooleanSimilarity(),false);
    QueryProcess qBestJM = new QueryProcess(query,indexPathwithoutstemwithoutlemma, new LMJelinekMercerSimilarity((float) 0.5), false);

        System.out.println("Running Similarities");
        String res1 = qBest.evalQuery();
        String res2 = qBestBool.evalQuery();
        String res3 = qBestJM.evalQuery();


        System.out.println("RESULTS");
        System.out.println("With Default Similarity:"+res1);
        System.out.println("With Boolean Similarity:"+res2);
        System.out.println("With JelinekMercer Similarity:"+res3);
        String indexPath="src/main/resources/indexlemma";
        System.out.println("Running Test with index with lemma and no stemming");
        QueryProcess qBest1 = new QueryProcess(query,indexPath, new BM25Similarity(), false);
        QueryProcess qBestBool1 = new QueryProcess(query,indexPath, new BooleanSimilarity(), false);
        QueryProcess qBestJM1 = new QueryProcess(query,indexPath, new LMJelinekMercerSimilarity((float) 0.5),false);

        System.out.println("Running Similarities");
        String res4 = qBest1.evalQuery();
        String res5 = qBestBool1.evalQuery();
        String res6 = qBestJM1.evalQuery();


        System.out.println("RESULTS");
        System.out.println("With Default Similarity:"+res4);
        System.out.println("With Boolean Similarity:"+res5);
        System.out.println("With JelinekMercer Similarity:"+res6);

        String indexPathLemma="src/main/resources/indexstemm";
        System.out.println("Running Test with index with no lemma and stemming");
        QueryProcess qBest2 = new QueryProcess(query,indexPathLemma, new BM25Similarity(), false);
        QueryProcess qBestBool2 = new QueryProcess(query,indexPathLemma, new BooleanSimilarity(),false);
        QueryProcess qBestJM2 = new QueryProcess(query,indexPathLemma, new LMJelinekMercerSimilarity((float) 0.5),false);

        System.out.println("Running Similarities");
        String res7 = qBest2.evalQuery();
        String res8 = qBestBool2.evalQuery();
        String res9 = qBestJM2.evalQuery();


        System.out.println("RESULTS");
        System.out.println("With Default Similarity:"+res7);
        System.out.println("With Boolean Similarity:"+res8);
        System.out.println("With JelinekMercer Similarity:"+res9);




    }

}
