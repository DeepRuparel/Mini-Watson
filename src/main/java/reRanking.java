import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;
import java.util.*;

public class reRanking {
    public List<ResultClass> reRank(String queryExpr, TopDocs topDocs, IndexSearcher searcher) throws IOException {
        List<ResultClass> res = new ArrayList<>();
        String[] queryArr = queryExpr.split(" ");
        Map<Integer, List<String>> docMap = new HashMap<>();
        for(ScoreDoc scoreDoc:topDocs.scoreDocs){
            List<String> content = Arrays.asList(searcher.doc(scoreDoc.doc).get("Content").split(" "));
            docMap.put(scoreDoc.doc, content);
        }
        for(ScoreDoc scoreDoc : topDocs.scoreDocs){
            int docID = scoreDoc.doc;
            Document doc = searcher.doc(docID);
            List<String> tokens = docMap.get(scoreDoc.doc);
            double score = 1;
            for(String c : queryArr){
                int num = tokens.size();
                int tf = Collections.frequency(tokens, c);
                double total = 0, occurances = 0;
                for(int docid : docMap.keySet()){
                    occurances = findoccurence(docid,c,docMap);
                    total += docMap.get(docid).size();
                }

                double param = calculate(occurances, total, num, tf);
                score *= param;
            }
            res.add(new ResultClass(doc, score));
        }

        return res;
    }
    public int findoccurence(int docid , String c, Map<Integer, List<String>> docMap){
        int o=0;
        o += Collections.frequency(docMap.get(docid), c);
        return o;
    }
    public double calculate(double o, double t, int num, int tf){

        double PtMc = o / t;
        double param = (tf + 0.5 * PtMc) / (num + 0.5);
        return param;
    }

}
