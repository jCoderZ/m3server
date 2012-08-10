package org.jcoderz.m3server;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.ReaderUtil;
import org.apache.lucene.util.Version;

public class MediaLibrary {

	private Directory lucene;
	private Analyzer analyzer;
	private IndexReader ireader;
	private IndexSearcher isearcher;

	public void init() throws IOException {
		lucene = FSDirectory.open(new File(
				"/media/0B83-851C/tools/var/lib/lucene"));
		analyzer = new StandardAnalyzer(Version.LUCENE_36);
		ireader = IndexReader.open(lucene, true);
		isearcher = new IndexSearcher(ireader);
	}

	public Collection<String> getFieldInfos() {
		return ReaderUtil.getIndexedFields(ireader);
	}

	public Playlist search(String query) {
		Playlist pl = new Playlist();
		try {
			QueryParser parser = new QueryParser(Version.LUCENE_36, "artist",
					analyzer);
			Query q = parser.parse(query);
			TopDocs td = isearcher.search(q, null, 1000);
			ScoreDoc[] hits = td.scoreDocs;
			for (int i = 0; i < hits.length; i++) {
				Document doc = isearcher.doc(hits[i].doc);
				System.out.println(doc.get("artist") + "/" + doc.get("album-title") + "/" + doc.get("title"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return pl;
	}

	public Playlist search(Query query) {
		return null;
	}

	public static void main(String[] args) {
		try {
			MediaLibrary ml = new MediaLibrary();
			ml.init();
			Collection<String> fields = ml.getFieldInfos();
			for (String field : fields) {
				// path, cover-image, cover-image-type are not indexed
				System.out.println(field);
			}
			ml.search("hosen");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
