import java.io.IOException;

import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class bestTest {
    static String indexPathWithStemLemma="src/main/resources/indexwithstemlemma";
    static String query = "src/main/resources/questions.txt";
    @Test
    public void test()throws IOException{

        System.out.println("Running Test with index with lemma and stemming");
        QueryProcess qBest = new QueryProcess(query,indexPathWithStemLemma, new BM25Similarity(), false);

        QueryProcess qBestBool = new QueryProcess(query,indexPathWithStemLemma, new BooleanSimilarity(), false);
        QueryProcess qBestJM = new QueryProcess(query,indexPathWithStemLemma, new LMJelinekMercerSimilarity((float) 0.4), false);

        System.out.println("Running Similarities");
        String res1 = qBest.evalQuery();
        String res2 = qBestBool.evalQuery();
        String res3 = qBestJM.evalQuery();

        System.out.println("RESULTS");
        System.out.println("With Default Similarity:"+res1);
        System.out.println("With Boolean Similarity:"+res2);
        System.out.println("With JelinekMercer Similarity:"+res3);

        System.out.println("------------Using Reranking--------------------------");
        QueryProcess qBest1 = new QueryProcess(query,indexPathWithStemLemma, new BM25Similarity(), true);

        QueryProcess qBestBool1 = new QueryProcess(query,indexPathWithStemLemma, new BooleanSimilarity(), true);
        QueryProcess qBestJM1 = new QueryProcess(query,indexPathWithStemLemma, new LMJelinekMercerSimilarity((float) 0.4), true);

        System.out.println("Running Similarities");
        String res4 = qBest1.evalQuery();
        String res5 = qBestBool1.evalQuery();
        String res6 = qBestJM1.evalQuery();
        System.out.println("RESULTS");
        System.out.println("With Default Similarity:"+res4);
        System.out.println("With Boolean Similarity:"+res5);
        System.out.println("With JelinekMercer Similarity:"+res6);




    }
}
