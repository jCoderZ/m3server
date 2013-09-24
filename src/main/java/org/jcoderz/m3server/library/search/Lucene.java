package org.jcoderz.m3server.library.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.jcoderz.m3server.library.Item;
import org.jcoderz.m3server.library.Library;
import org.jcoderz.m3server.library.LibraryException;
import org.jcoderz.m3server.library.Path;
import org.jcoderz.m3server.library.filesystem.AudioFileItem;
import org.jcoderz.m3server.util.Logging;
import org.jcoderz.m3util.intern.util.Environment;

/**
 * This is the Lucene search provider implementation.
 *
 * @author mrumpf
 *
 */
public class Lucene implements Searchable {

    private static final Logger logger = Logging.getLogger(Lucene.class);

    private Directory lucene;
    private Analyzer analyzer;
    private IndexReader ireader;
    private IndexSearcher isearcher;

    public Lucene() {
        try {
            lucene = FSDirectory.open(Environment.getLuceneFolder());
            analyzer = new StandardAnalyzer(Version.LUCENE_36);
            // in Lucene 4 the readonly flag is set to true per default
            ireader = IndexReader.open(lucene, true);
            isearcher = new IndexSearcher(ireader);
        } catch (IOException ex) {
            throw new RuntimeException("Initialization failed", ex);
        }
    }

    @Override
    public List<Item> search(String query) {
        List<Item> l = new ArrayList<>();
        try {
            QueryParser parser = new QueryParser(Version.LUCENE_36, "artist",
                    analyzer);
            Query q = parser.parse(query);
            TopDocs td = isearcher.search(q, null, 1000);
            ScoreDoc[] hits = td.scoreDocs;
            for (int i = 0; i < hits.length; i++) {
                Document doc = isearcher.doc(hits[i].doc);
                Item item = Library.LIBRARY.item(new Path(doc.get("path")));
                AudioFileItem mp3 = new AudioFileItem(doc.get("title"), item.getParent(), null);
                mp3.setAlbum(doc.get("album-title"));
                mp3.setArtist(doc.get("artist"));
                mp3.setTitle(doc.get("title"));
                String size = doc.get("size");
                // FIXME: mp3.setSize(size != null ? Long.valueOf(size) : 0L);
                l.add(mp3);
            }
        } catch (LibraryException | ParseException | IOException | NumberFormatException ex) {
            ex.printStackTrace();
        }
        return l;
    }
    
    
}
