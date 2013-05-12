package search.system.peer.search;

import common.configuration.SearchConfiguration;
import cyclon.system.peer.cyclon.CyclonSample;
import cyclon.system.peer.cyclon.CyclonSamplePort;
import cyclon.system.peer.cyclon.PeerDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;
import se.sics.kompics.network.Network;
import se.sics.kompics.timer.SchedulePeriodicTimeout;
import se.sics.kompics.timer.Timer;
import se.sics.kompics.web.Web;
import se.sics.kompics.web.WebRequest;
import se.sics.kompics.web.WebResponse;
import search.simulator.snapshot.Snapshot;
import search.system.peer.AddIndexText;
import search.system.peer.IndexPort;
import tman.system.peer.tman.TManSample;
import tman.system.peer.tman.TManSamplePort;

/**
 * Should have some comments here.
 *
 * @author jdowling
 */
public final class Search extends ComponentDefinition {

    private static final Logger logger = LoggerFactory.getLogger(Search.class);
    Positive<IndexPort> indexPort = positive(IndexPort.class);
    Positive<Network> networkPort = positive(Network.class);
    Positive<Timer> timerPort = positive(Timer.class);
    Negative<Web> webPort = negative(Web.class);
    Positive<CyclonSamplePort> cyclonSamplePort = positive(CyclonSamplePort.class);
    Positive<TManSamplePort> tmanPort = positive(TManSamplePort.class);
    ArrayList<Address> neighbours = new ArrayList<Address>();
    private Address self;
    private SearchConfiguration searchConfiguration;
    // Apache Lucene used for searching
    StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_42);
    Directory index = new RAMDirectory();
    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_42, analyzer);
    int lastMissingIndexEntry = 0;
    int maxIndexEntry = 0;
    Random random;
    // When you partition the index you need to find new nodes
    // This is a routing table maintaining a list of pairs in each partition.
    private Map<Integer, List<PeerDescriptor>> routingTable;
    Comparator<PeerDescriptor> peerAgeComparator = new Comparator<PeerDescriptor>() {
        @Override
        public int compare(PeerDescriptor t, PeerDescriptor t1) {
            if (t.getAge() > t1.getAge()) {
                return 1;
            } else {
                return -1;
            }
        }
    };

//-------------------------------------------------------------------	
    public Search() {

        subscribe(handleInit, control);
        subscribe(handleWebRequest, webPort);
        subscribe(handleCyclonSample, cyclonSamplePort);
        subscribe(handleAddIndexText, indexPort);
        subscribe(handleUpdateIndexTimeout, timerPort);
        subscribe(handleMissingIndexEntriesRequest, networkPort);
        subscribe(handleMissingIndexEntriesResponse, networkPort);
        subscribe(handleTManSample, tmanPort);
    }
//-------------------------------------------------------------------	
    Handler<SearchInit> handleInit = new Handler<SearchInit>() {
        @Override
        public void handle(SearchInit init) {
            self = init.getSelf();
            searchConfiguration = init.getConfiguration();
            routingTable = new HashMap<Integer, List<PeerDescriptor>>(searchConfiguration.getNumPartitions());
            random = new Random(init.getConfiguration().getSeed());
            long period = searchConfiguration.getPeriod();
            SchedulePeriodicTimeout rst = new SchedulePeriodicTimeout(period, period);
            rst.setTimeoutEvent(new UpdateIndexTimeout(rst));
            trigger(rst, timerPort);

            // TODO super ugly workaround...
            IndexWriter writer;
            try {
                writer = new IndexWriter(index, config);
                writer.commit();
                writer.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    };
    Handler<WebRequest> handleWebRequest = new Handler<WebRequest>() {
        @Override
        public void handle(WebRequest event) {

            String[] args = event.getTarget().split("-");

            logger.debug("Handling Webpage Request");
            WebResponse response;
            if (args[0].compareToIgnoreCase("search") == 0) {
                response = new WebResponse(searchPageHtml(args[1]), event, 1, 1);
            } else if (args[0].compareToIgnoreCase("add") == 0) {
                response = new WebResponse(addEntryHtml(args[1], Integer.parseInt(args[2])), event, 1, 1);
            } else {
                response = new WebResponse(searchPageHtml(event
                        .getTarget()), event, 1, 1);
            }
            trigger(response, webPort);
        }
    };

    private String searchPageHtml(String title) {
        StringBuilder sb = new StringBuilder("<!DOCTYPE html PUBLIC \"-//W3C");
        sb.append("//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR");
        sb.append("/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http:");
        sb.append("//www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Conten");
        sb.append("t-Type\" content=\"text/html; charset=utf-8\" />");
        sb.append("<title>Kompics P2P Bootstrap Server</title>");
        sb.append("<style type=\"text/css\"><!--.style2 {font-family: ");
        sb.append("Arial, Helvetica, sans-serif; color: #0099FF;}--></style>");
        sb.append("</head><body><h2 align=\"center\" class=\"style2\">");
        sb.append("ID2210 (Decentralized Search for Piratebay)</h2><br>");
        try {
            query(sb, title);
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
            sb.append(ex.getMessage());
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
            sb.append(ex.getMessage());
        }
        sb.append("</body></html>");
        return sb.toString();
    }

    private String addEntryHtml(String title, int id) {
        StringBuilder sb = new StringBuilder("<!DOCTYPE html PUBLIC \"-//W3C");
        sb.append("//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR");
        sb.append("/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http:");
        sb.append("//www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Conten");
        sb.append("t-Type\" content=\"text/html; charset=utf-8\" />");
        sb.append("<title>Adding an Entry</title>");
        sb.append("<style type=\"text/css\"><!--.style2 {font-family: ");
        sb.append("Arial, Helvetica, sans-serif; color: #0099FF;}--></style>");
        sb.append("</head><body><h2 align=\"center\" class=\"style2\">");
        sb.append("ID2210 Uploaded Entry</h2><br>");
        try {
            addEntry(title, id);
            sb.append("Entry: ").append(title).append(" - ").append(id);
        } catch (IOException ex) {
            sb.append(ex.getMessage());
            java.util.logging.Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
        }
        sb.append("</body></html>");
        return sb.toString();
    }

    private void addEntry(String title, int id) throws IOException {
        IndexWriter w = new IndexWriter(index, config);
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
        // Use a NumericRangeQuery to find missing index entries:
//    http://lucene.apache.org/core/4_2_0/core/org/apache/lucene/search/NumericRangeQuery.html
        // http://lucene.apache.org/core/4_2_0/core/org/apache/lucene/document/IntField.html
        doc.add(new IntField("id", id, Field.Store.YES));
        w.addDocument(doc);
        w.close();
        Snapshot.incNumIndexEntries(self);
    }

    private String query(StringBuilder sb, String querystr) throws ParseException, IOException {

        // the "title" arg specifies the default field to use when no field is explicitly specified in the query.
        Query q = new QueryParser(Version.LUCENE_42, "title", analyzer).parse(querystr);
        IndexSearcher searcher = null;
        IndexReader reader = null;
        try {
            reader = DirectoryReader.open(index);
            searcher = new IndexSearcher(reader);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }

        int hitsPerPage = 10;
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        // display results
        sb.append("Found ").append(hits.length).append(" entries.<ul>");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            sb.append("<li>").append(i + 1).append(". ").append(d.get("id")).append("\t").append(d.get("title")).append("</li>");
        }
        sb.append("</ul>");

        // reader can only be closed when there
        // is no need to access the documents any more.
        reader.close();
        return sb.toString();
    }
    Handler<UpdateIndexTimeout> handleUpdateIndexTimeout = new Handler<UpdateIndexTimeout>() {
        @Override
        public void handle(UpdateIndexTimeout event) {

            // pick a random neighbour to ask for index updates from. 
            // You can change this policy if you want to.
            // Maybe a gradient neighbour who is closer to the leader?
            if (neighbours.isEmpty()) {
                return;
            }
            Address dest = neighbours.get(random.nextInt(neighbours.size()));

            // find all missing index entries (ranges) between lastMissingIndexValue
            // and the maxIndexValue
            List<Range> missingIndexEntries = getMissingRanges();

            // Send a MissingIndexEntries.Request for the missing index entries to dest
            MissingIndexEntries.Request req = new MissingIndexEntries.Request(self, dest,
                    missingIndexEntries);
            trigger(req, networkPort);
        }
    };

    ScoreDoc[] getExistingDocsInRange(int min, int max, IndexReader reader,
            IndexSearcher searcher) throws IOException {
        reader = DirectoryReader.open(index);
        searcher = new IndexSearcher(reader);
        // The line below is dangerous - we should bound the number of entries returned
        // so that it doesn't consume too much memory.
        int hitsPerPage = max - min > 0 ? max - min : 1;
        Query query = NumericRangeQuery.newIntRange("id", min, max, true, true);
        TopDocs topDocs = searcher.search(query, hitsPerPage, new Sort(new SortField("id", Type.INT)));
        return topDocs.scoreDocs;
    }

    List<Range> getMissingRanges() {
        List<Range> res = new ArrayList<Range>();
        IndexReader reader = null;
        IndexSearcher searcher = null;
        try {
            reader = DirectoryReader.open(index);
            searcher = new IndexSearcher(reader);
            ScoreDoc[] hits = getExistingDocsInRange(lastMissingIndexEntry, maxIndexEntry,
                    reader, searcher);
            if (hits != null) {
                int startRange = lastMissingIndexEntry;
                // This should terminate by finding the last entry at position maxIndexValue
                for (int id = lastMissingIndexEntry + 1; id <= maxIndexEntry; id++) {
                    // We can skip the for-loop if the hits are returned in order, with lowest id first
                    boolean found = false;
                    for (int i = 0; i < hits.length; ++i) {
                        int docId = hits[i].doc;
                        Document d;
                        try {
                            d = searcher.doc(docId);
                            int indexId = Integer.parseInt(d.get("id"));
                            if (id == indexId) {
                                found = true;
                            }
                        } catch (IOException ex) {
                            java.util.logging.Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (found) {
                        if (id != startRange) {
                            res.add(new Range(startRange, id - 1));
                        }
                        startRange = (id == Integer.MAX_VALUE) ? Integer.MAX_VALUE : id + 1;
                    }
                }
                // Add all entries > maxIndexEntry as a range of interest.
                res.add(new Range(maxIndexEntry + 1, Integer.MAX_VALUE));

            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }


        return res;
    }

    List<IndexEntry> getMissingIndexEntries(Range range) {
        List<IndexEntry> res = new ArrayList<IndexEntry>();
        IndexSearcher searcher = null;
        IndexReader reader = null;
        try {
            reader = DirectoryReader.open(index);
            searcher = new IndexSearcher(reader);
            ScoreDoc[] hits = getExistingDocsInRange(range.getLower(),
                    range.getUpper(), reader, searcher);
            if (hits != null) {
                for (int i = 0; i < hits.length; ++i) {
                    int docId = hits[i].doc;
                    Document d;
                    try {
                        d = searcher.doc(docId);
                        int indexId = Integer.parseInt(d.get("id"));
                        String text = d.get("text");
                        res.add(new IndexEntry(indexId, text));
                    } catch (IOException ex) {
                        java.util.logging.Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

        return res;
    }

    /**
     * Called by null null     {@link #handleMissingIndexEntriesRequest(MissingIndexEntries.Request) 
     * handleMissingIndexEntriesRequest}
     *
     * @return List of IndexEntries at this node great than max
     */
    List<IndexEntry> getEntriesGreaterThan(int max) {
        List<IndexEntry> res = new ArrayList<IndexEntry>();

        IndexSearcher searcher = null;
        IndexReader reader = null;
        try {
            ScoreDoc[] hits = getExistingDocsInRange(max, maxIndexEntry,
                    reader, searcher);

            if (hits != null) {
                for (int i = 0; i < hits.length; ++i) {
                    int docId = hits[i].doc;
                    Document d;
                    try {
                        reader = DirectoryReader.open(index);
                        searcher = new IndexSearcher(reader);
                        d = searcher.doc(docId);
                        int indexId = Integer.parseInt(d.get("id"));
                        String text = d.get("text");
                        res.add(new IndexEntry(indexId, text));
                    } catch (IOException ex) {
                        java.util.logging.Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        return res;
    }
    Handler<MissingIndexEntries.Request> handleMissingIndexEntriesRequest = new Handler<MissingIndexEntries.Request>() {
        @Override
        public void handle(MissingIndexEntries.Request event) {

            List<IndexEntry> res = new ArrayList<IndexEntry>();
            for (Range r : event.getMissingRanges()) {
                res.addAll(getMissingIndexEntries(r));
            }

            // TODO send missing index entries back to requester
        }
    };
    Handler<MissingIndexEntries.Response> handleMissingIndexEntriesResponse = new Handler<MissingIndexEntries.Response>() {
        @Override
        public void handle(MissingIndexEntries.Response event) {
            // TODO merge the missing index entries in your lucene index 
        }
    };
    Handler<CyclonSample> handleCyclonSample = new Handler<CyclonSample>() {
        @Override
        public void handle(CyclonSample event) {
            // receive a new list of neighbours
            neighbours.clear();
            neighbours.addAll(event.getSample());

            // update routing tables
            for (Address p : neighbours) {
                int partition = p.getId() % searchConfiguration.getNumPartitions();
                List<PeerDescriptor> nodes = routingTable.get(partition);
                if (nodes == null) {
                    nodes = new ArrayList<PeerDescriptor>();
                    routingTable.put(partition, nodes);
                }
                // Note - this might replace an existing entry in Lucene
                nodes.add(new PeerDescriptor(p));
                // keep the freshest descriptors in this partition
                Collections.sort(nodes, peerAgeComparator);
                List<PeerDescriptor> nodesToRemove = new ArrayList<PeerDescriptor>();
                for (int i = nodes.size(); i > searchConfiguration.getMaxNumRoutingEntries(); i--) {
                    nodesToRemove.add(nodes.get(i - 1));
                }
                nodes.removeAll(nodesToRemove);
            }
        }
    };
//-------------------------------------------------------------------	
    Handler<AddIndexText> handleAddIndexText = new Handler<AddIndexText>() {
        @Override
        public void handle(AddIndexText event) {
            int id = LeaderEmulator.incIndexId();
            updateIndexPointers(id);
            logger.info(self.getId()
                    + " - adding index entry: {} Id={}", event.getText(), id);
            try {
                addEntry(event.getText(), id);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
                throw new IllegalArgumentException(ex.getMessage());
            }
        }
    };
    Handler<TManSample> handleTManSample = new Handler<TManSample>() {
        @Override
        public void handle(TManSample event) {
        }
    };

    private void updateIndexPointers(int id) {
        if (id == lastMissingIndexEntry + 1) {
            lastMissingIndexEntry++;
        }
        if (id > maxIndexEntry) {
            maxIndexEntry = id;
        }
    }
}