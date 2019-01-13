package com.bavuwe.matemaatika;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

/**
 * Class, that manages an index on content created by @see MataHTMLParser.
 */
public class ContentIndex {	
	private HashMap<String, short[]> tfidfIndex = null;
	private int numDocuments;
	private int numWords;
	/**
	 * The dummy constructor.
	 */
	public ContentIndex() {
	}
	
	private boolean isAlphaWord(String s) {
		for (int i=0 ; i<s.length() ; ++i) {
			char c = s.charAt(i);
			if (!Character.isLetter(c)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Build the TFIDF index.
	 * @param parser
	 */
	public void buildIndex(MataHTMLParser parser) {
		HashMap<String, Integer> wordDocuments = new HashMap<String, Integer>();
		HashMap<Integer, HashMap<String, Float>> tfs = new HashMap<Integer, HashMap<String, Float>>();
		HashSet<String> wordSet = new HashSet<String>();
		
		String[] subTopicTitles = parser.getSubTopicTitles();
		
		// first calculate the term frequencies in single documents and overall
		for (int docIdx=0 ; docIdx < subTopicTitles.length ; ++ docIdx) {
			HashMap<String, Float> tf = new HashMap<String, Float>();
			String[] words = getWords(parser.getSubTopicHTML(docIdx) + " " + subTopicTitles[docIdx]);
			for (String theword : words) {
				String word = theword.trim();
				if (word.length() > 20) { // ignore too long words
					continue;
				}
				if (!isAlphaWord(word)) {
					continue;
				}
				for (String prefix : getPrefixes(word)) {
					/*if (prefix.length() <= 5) { // ignore too short prefixes
						continue; 
					}*/
					wordSet.add(prefix);
					// put the prefix into wordDocuments index
					Integer idfCount = wordDocuments.get(prefix);
					if (idfCount == null) {
						idfCount = 0;
					}
					wordDocuments.put(prefix, idfCount);
					// update the tf index
					Float tfCount = tf.get(prefix);
					if (tfCount == null) {
						tfCount = 0f;
					}
					tfCount = tfCount + 1f;
					tf.put(prefix, tfCount);
				}
			}
			// normalize term frequencies
			for (String prefix : tf.keySet()) {
				Float count = tf.get(prefix);
				count /= words.length;
				tf.put(prefix, count);
			}
			//
			tfs.put(docIdx, tf);
		}
		
		// now calculate TF-IDF weights
		numDocuments = subTopicTitles.length;
		numWords = wordSet.size();
		tfidfIndex = new HashMap<String, short[]>();
		
		for (int docIdx=0 ; docIdx < subTopicTitles.length ; ++ docIdx) {
			HashMap<String, Float> tf = tfs.get(docIdx);
			for (String word : wordSet) {
				float tfreq = 0f;
				if (tf.get(word) != null) {
					tfreq = tf.get(word);
				}
				float idfreq = (float)Math.log10((float)(numDocuments) / (wordDocuments.get(word) + 1));
				float tf_idf = tfreq * idfreq;
				
				short[] weights = tfidfIndex.get(word);
				if (weights == null) {
					weights = new short[numDocuments];
					for (int j=0 ; j<numDocuments ; ++j) {
						weights[j] = 0;
					}
				}
				weights[docIdx] = (short)(tf_idf*300);
				tfidfIndex.put(word, weights);
			}
		}
	}
	
	/**
	 * Serialize the index to a string.
	 * @return
	 */
	public void save() {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(new File("mata.index"));
			DataOutputStream dos = new DataOutputStream(fos);
			dos.writeInt(numDocuments);
			dos.writeInt(numWords);
			for (String word : tfidfIndex.keySet()) {
				dos.writeUTF(word);
				short[] weights = tfidfIndex.get(word);
				for (int i=0 ; i<weights.length ; ++i) {
					if (weights[i] > 0.0001) {
						dos.writeShort(i);
						dos.writeShort(weights[i]);
					}
				}
				dos.writeShort(-1);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Load the index from a binary input stream.
	 * @param in
	 */
	public void load(InputStream in) {
		tfidfIndex = new HashMap<String, short[]>(13000);
		DataInputStream dis = new DataInputStream(in);
		try {
			numDocuments = dis.readInt();
			numWords = dis.readInt();
			System.out.println("The index has " + numWords + " words and " + numDocuments + " documents!");
			for (int wordIdx=0 ; wordIdx < numWords ; ++wordIdx) {
				String word = dis.readUTF();
				//System.out.println("Load word " + word);
				short[] weights = new short[numDocuments];
				for (int j=0 ; j<numDocuments ; ++j) {
					weights[j] = 0;
				}
				int docIdx = dis.readShort();
				while (docIdx != -1) {
					short weight = dis.readShort();
					weights[docIdx] = weight;
					//System.out.println("For document " + docIdx + " it is " + weight);
					docIdx = dis.readShort();
				}
				tfidfIndex.put(word, weights);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String[] getPrefixes(String s) {
		String[] prefixes = new String[s.length()];
		for (int i=1 ; i <= s.length() ; ++i) {
			prefixes[i-1] = s.substring(0, i);
		}
		return prefixes;
	}
	
	/**
	 * Query the index against a querystring.
	 * @param queryString
	 */
	public int[] query(String queryString) {
		String[] queries = queryString.split(" ");
		float[] docWeights = new float[numDocuments];
		for (String query : queries) {
			short[] weights = tfidfIndex.get(query);
			if (weights != null) {
				for (int i=0 ; i<weights.length ; ++i) {
					docWeights[i] += weights[i];
				}
			}
		}
		// sort the weights
		List<DocWeight> docs = new Vector<DocWeight>();
		for (int docIdx=0 ; docIdx < numDocuments ; ++docIdx) {
			if (docWeights[docIdx] > 0) {
				docs.add(new DocWeight(docIdx, docWeights[docIdx]));
			}
		}
		Collections.sort(docs);
		int[] docIdxs = new int[docs.size()];
		for (int i=0 ; i<docs.size() ; ++i) {
			docIdxs[i] = docs.get(i).docIdx;
		}
		return docIdxs;
	}
	
	private static String trimTags(String s) {
		String plain = s.replaceAll("<(.|\n)*?>", "").trim();
		return plain;
	}
	
	private static String[] getWords(String sentence) {
		String[] words = trimTags(sentence.toLowerCase()).split("[\\s,\\.;\\?!•:]+");
		return words;
	}
}

/**
 * We use this class to sort the documents according to their weight.
 */
class DocWeight implements Comparable<DocWeight> {
	public int docIdx;
	public float weight;
	
	public DocWeight(int docIdx, float weight) {
		this.docIdx = docIdx;
		this.weight = weight;
	}

	@Override
	public int compareTo(DocWeight o) {
		return Float.compare(o.weight, this.weight);
	}
}
