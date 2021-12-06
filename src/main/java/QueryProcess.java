import edu.stanford.nlp.parser.dvparser.DVModelReranker;
import edu.stanford.nlp.simple.Sentence;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queries.function.FunctionScoreQuery;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.tartarus.snowball.ext.PorterStemmer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import org.apache.lucene.search.QueryRescorer;

public class QueryProcess {

    String queryPath = "";
    String indexPath = "";
    private Optional<Similarity> s;
    boolean implementReRank;
    // making use of method overloading
    QueryProcess(String path, String indexPath, boolean r){
        this.queryPath = path;
        this.indexPath=indexPath;
        this.s=Optional.empty();
        this.implementReRank = r;
    }
    QueryProcess(String path, String indexPath, Similarity s, boolean r){
        this.queryPath = path;
        this.indexPath=indexPath;
        this.s=Optional.of(s);
        this.implementReRank=r;
    }

    public String evalQuery() throws IOException {
        File file = new File(queryPath);
        Scanner scanner = new Scanner(file);
        int i = 0;
        int correct = 0, incorrecct = 0;
        String cat = "", query= "", ans="";
        int total=0;
        double mmr=0;
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(i%4==0){
                cat = line.trim();
            }
            else if(i%4==1){
                query = line.trim();
            }
            else if(i%4==2){
                ans = line.trim();
            }
            else {
                List<ResultClass> res = new ArrayList<ResultClass>();
                if(implementReRank){
                    res = runReRank(query+" "+cat);
                    if(ans.toLowerCase(Locale.ROOT).contains(res.get(0).docName.get("Title"))){
                        mmr++;
                        correct++;
                    }
                    else{
                        incorrecct++;
                        for (int m=0; m<res.size();m++){
                            if(ans.toLowerCase(Locale.ROOT).contains(res.get(m).docName.get("Title"))){
                                mmr += (double)1/(m+1);
                                break;
                            }

                        }

                    }

                }
                else {
                    res = run(query + " " + cat);

                    if (ans.toLowerCase(Locale.ROOT).equals(res.get(0).docName.get("Title"))) {
                        mmr++;
                        correct++;
                    } else {
                        incorrecct++;
                        for (int m = 0; m < res.size(); m++) {
                            //System.out.println(res.get(m).docName.get("Title"));
                            if (ans.toLowerCase(Locale.ROOT).equals(res.get(m).docName.get("Title"))) {
                                mmr += (double) 1 / (m + 1);
                                break;
                            }

                        }
                        //System.out.println("------------------");
                    }
                }
                total++;
            }
            i++;
        }
        String result = "P@1: " + correct + "/" + total + " = " + (double)correct/total + " mmr score is: "+ (double) mmr;
        //System.out.println(result);
        //System.out.println("Correct: "+correct+"\tIncorrect: "+incorrecct);
        return result;
    }

    private static String RemoveSpecialCharacters(String str) {
        return str.replaceAll("\n", " ")
                .replaceAll("https?://\\S+\\s?", "")
                .replaceAll("[^ a-zA-Z\\d]", " ")
                .toLowerCase().trim();
    }

    private String convertLemma(StringBuilder b, String s) {
        if (s.isEmpty()) {
            return s;
        }
        for (String lemma : new Sentence(s.toLowerCase()).lemmas()) {
            b.append(lemma).append(" ");
        }
        return b.toString();
    }

    private String convertStem(StringBuilder b, String s) {
        for(String word: new Sentence(s.toLowerCase()).words()) {
            b.append(getStem(word) + " ");
        }
        return b.toString();
    }

    private String getStem(String term) {
        PorterStemmer stemmer = new PorterStemmer();
        stemmer.setCurrent(term);
        stemmer.stem();
        return stemmer.getCurrent();
    }


    public List<ResultClass> run(String query) throws IOException{
        List<ResultClass> ans = new ArrayList<ResultClass>();
        StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
        //WhitespaceAnalyzer analyzer = new WhitespaceAnalyzer();
        StringBuilder stem = new StringBuilder();
        StringBuilder lemma = new StringBuilder();

        try {
            query= RemoveSpecialCharacters(query);
            query = convertLemma(lemma,query);
            query = convertStem(stem,query);
            Directory index = FSDirectory.open(new File(indexPath).toPath());
            Query query1 = new QueryParser("Content", standardAnalyzer).parse(query);
            DoubleValuesSource boostByField = DoubleValuesSource.fromLongField("sum");
            FunctionScoreQuery modifiedQuery = new FunctionScoreQuery(query1, boostByField);
            IndexReader indexReader = DirectoryReader.open(index);
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            //Query query1 = new QueryParser("Content",analyzer).parse(query);
            //Query query2 = new QueryParser("categories" , standardAnalyzer).parse(cat);
            if(s.isPresent()) {
                indexSearcher.setSimilarity(s.get());
            }
            int hitsperpage = 10;
            reRanking re = new reRanking();

            TopDocs docs = indexSearcher.search(query1, hitsperpage);
            if(implementReRank) {
                ans = re.reRank(query, docs, indexSearcher);
                return ans;
            }
            //docs = queryRescorer.rescore(indexSearcher, docs, 5);

            ScoreDoc[] hits = docs.scoreDocs;

            for (int j=0;j< hits.length;j++){
                ResultClass r = new ResultClass();
                int docId = hits[j].doc;
                Document d = indexSearcher.doc(docId);
                r.docName = d;
                r.docscore = hits[j].score;
                ans.add(r);
            }
            indexReader.close();
            return ans;
        }catch (Exception e){
            e.printStackTrace();
        };
        return ans;
    }
    public List<ResultClass> runReRank(String query) throws IOException{
        List<ResultClass> ans = new ArrayList<ResultClass>();
        StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
        //WhitespaceAnalyzer analyzer = new WhitespaceAnalyzer();
        StringBuilder stem = new StringBuilder();
        StringBuilder lemma = new StringBuilder();

        try {
            query= RemoveSpecialCharacters(query);
            query = convertLemma(lemma,query);
            Directory index = FSDirectory.open(new File(indexPath).toPath());
            Query query1 = new QueryParser("Content", standardAnalyzer).parse(query);
            DoubleValuesSource boostByField = DoubleValuesSource.fromLongField("sum");
            FunctionScoreQuery modifiedQuery = new FunctionScoreQuery(query1, boostByField);
            IndexReader indexReader = DirectoryReader.open(index);
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            //Query query1 = new QueryParser("Content",analyzer).parse(query);
            //Query query2 = new QueryParser("categories" , standardAnalyzer).parse(cat);
            if(s.isPresent()) {
                indexSearcher.setSimilarity(s.get());
            }
            int hitsperpage = 10;
            reRanking re = new reRanking();

            TopDocs docs = indexSearcher.search(query1, hitsperpage);
            if(implementReRank) {
                ans = re.reRank(query, docs, indexSearcher);
                return ans;
            }
            //docs = queryRescorer.rescore(indexSearcher, docs, 5);

//            ScoreDoc[] hits = docs.scoreDocs;
//
//            for (int j=0;j< hits.length;j++){
//                ResultClass r = new ResultClass();
//                int docId = hits[j].doc;
//                Document d = indexSearcher.doc(docId);
//                r.docName = d;
//                r.docscore = hits[j].score;
//                ans.add(r);
//            }
//            indexReader.close();
//            return ans;
        }catch (Exception e){
            e.printStackTrace();
        };
        return ans;
    }
}