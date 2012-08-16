package org.jcoderz.m3server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;

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

@Stateless
public class MediaLibrary {

	// TODO: hard-coded until the Environment class from m3util is available
	// here
	private static final String M3_LIBRARY_HOME = "/media/0B83-851C";

	private static final File M3_LIBRARY_HOME_FOLDER;
	static {
		M3_LIBRARY_HOME_FOLDER = new File(M3_LIBRARY_HOME);
		if (!M3_LIBRARY_HOME_FOLDER.exists()
				|| !M3_LIBRARY_HOME_FOLDER.isDirectory()) {
			throw new RuntimeException("M3_LIBRARY_HOME=" + M3_LIBRARY_HOME
					+ " does not exist or is not a directory");
		}
	}

	private static final String M3_LUCENE_ROOT = "tools/var/lib/lucene";
	private static final String M3_AUDIO_ROOT = "audio";
	private Directory lucene;
	private Analyzer analyzer;
	private IndexReader ireader;
	private IndexSearcher isearcher;

	public MediaLibrary() {
		try {
			lucene = FSDirectory.open(new File(M3_LIBRARY_HOME_FOLDER,
					M3_LUCENE_ROOT));
			analyzer = new StandardAnalyzer(Version.LUCENE_36);
			ireader = IndexReader.open(lucene, true);
			isearcher = new IndexSearcher(ireader);
		} catch (IOException ex) {
			throw new RuntimeException("Initialization failed", ex);
		}
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
				Mp3Info mp3 = new Mp3Info();
				mp3.setAlbum(doc.get("album-title"));
				mp3.setArtist(doc.get("artist"));
				mp3.setPath(doc.get("path"));
				mp3.setTitle(doc.get("title"));
				mp3.setReleased(doc.get("released"));
				String size = doc.get("size");
				mp3.setSize(size != null ? Long.valueOf(size) : 0L);
				pl.getMp3().add(mp3);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return pl;
	}

	public Playlist search(Query query) {
		return null;
	}

	public Playlist getAllArtists() {
		return null;
	}

	public List<String> browse(String path) {
		List<String> content = new ArrayList<String>();
		if (path == null || path.isEmpty()) {
			// TODO: generate from TagQuality once the m3util project is available
			content.add("01-gold");
			content.add("02-silver");
			content.add("03-bronze");
		} else {
			try {
				File root = new File(M3_LIBRARY_HOME_FOLDER, M3_AUDIO_ROOT);
				File subpath = new File(root, path);
				if (subpath.exists() && subpath.isDirectory()) {
					content.addAll(Arrays.asList(subpath.list()));
				}
				else {
					// TODO 
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return content;
	}

	public static void main(String[] args) {
		try {
			MediaLibrary ml = new MediaLibrary();

			Collection<String> fields = ml.getFieldInfos();
			for (String field : fields) {
				// path, cover-image, cover-image-type are not indexed
				System.out.println(field);
			}
			ml.search("ärzte");
			ml.getAllArtists();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
