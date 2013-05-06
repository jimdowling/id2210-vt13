/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package search.system.peer.search;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jdowling
 */
public class SearchTest {

    public SearchTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testRangeQuery() throws Exception {

        RAMDirectory directory = new RAMDirectory();

        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_42,
                new WhitespaceAnalyzer(Version.LUCENE_42));
        IndexWriter writer = new IndexWriter(directory, config);

        try {

            Document doc = new Document();
            doc.add(new IntField("id", 100, Field.Store.YES));
            writer.addDocument(doc); 
            doc = new Document();
            doc.add(new IntField("id", 999, Field.Store.YES));
            writer.addDocument(doc);

        } finally {

            writer.close();

        }


        NumericRangeQuery<Integer> rangeQuery =
                NumericRangeQuery.newIntRange("id", 99, 1000, true, true);

        IndexReader reader = DirectoryReader.open(directory);
        try {

            IndexSearcher searcher = new IndexSearcher(reader);

            TopDocs docs = searcher.search(rangeQuery, 1000);

            assert(2 == docs.totalHits);

        } finally {

            reader.close();

        }


    }
}