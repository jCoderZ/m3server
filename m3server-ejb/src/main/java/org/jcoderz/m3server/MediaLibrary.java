package org.jcoderz.m3server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
import org.jaudiotagger.tag.datatype.Artwork;
import org.jcoderz.mp3.intern.MusicBrainzMetadata;
import org.jcoderz.mp3.intern.util.Environment;

/**
 * This is the main class which provides an interface to all of the media
 * library functionality.
 * 
 * @author mrumpf
 * 
 */
@Stateless
public class MediaLibrary {

	static {
		if (!Environment.M3_LIBRARY_HOME.exists()
				|| !Environment.M3_LIBRARY_HOME.isDirectory()) {
			throw new RuntimeException("M3_LIBRARY_HOME="
					+ Environment.M3_LIBRARY_HOME
					+ " does not exist or is not a directory");
		}
	}

	private static final String M3_LUCENE_ROOT = "tools/var/lib/lucene";
	public static final String M3_AUDIO_ROOT = "audio";
	private Directory lucene;
	private Analyzer analyzer;
	private IndexReader ireader;
	private IndexSearcher isearcher;

	public MediaLibrary() {
		try {
			lucene = FSDirectory.open(new File(Environment.M3_LIBRARY_HOME,
					M3_LUCENE_ROOT));
			analyzer = new StandardAnalyzer(Version.LUCENE_36);
			// in Lucene 4 the readonly flag is set to true per default
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

	public List<Item> browse(String path) {
		List<Item> content = new ArrayList<Item>();
		content.addAll(FileSystemBrowser.createItemList(path));
		return content;
	}

	public Artwork coverImage(String file) {
		File root = new File(Environment.M3_LIBRARY_HOME,
				MediaLibrary.M3_AUDIO_ROOT);
		if (file == null || file.isEmpty()) {
			// TODO throw Exception
		}

		Artwork result = null;
		File f = new File(root, file);
		if (!f.exists()) {
			// TODO throw Exception
			throw new RuntimeException("File " + file + " does not exist!");
		}
		if (f.isDirectory()) {
			String[] files = f.list();
			for (String ff : files) {
				File folderFile = new File(f, ff);
				if (folderFile.isFile()) {
					MusicBrainzMetadata mb = new MusicBrainzMetadata(folderFile);
					result = mb.getCoverImage();
				}
			}
			if (result == null) {
				result = new Artwork();
				result.setBinaryData(FileSystemBrowser.FOLDER_ICON_DEFAULT);
				result.setMimeType("image/png");
			}
		} else if (f.isFile()) {
			MusicBrainzMetadata mb = new MusicBrainzMetadata(f);
			result = mb.getCoverImage();
			// when no image has been found inside the file
			if (result == null || result.getBinaryData() == null) {
				result.setBinaryData(FileSystemBrowser.FILE_ICON_DEFAULT);
				result.setMimeType("image/png");
			}
		} else {
			// TODO throw Exception
			throw new RuntimeException("Unknown file type " + file);
		}

		return result;
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
