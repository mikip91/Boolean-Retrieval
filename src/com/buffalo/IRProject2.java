package com.buffalo;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class IRProject2 {

	static HashMap<String, LinkedList> termMap = new HashMap<>();
	static String path = "./resources/inputFiles/";
	static IndexReader reader;

	// static String path;

	public static void main(String[] args) throws IOException {

		/*
		 * path = args[0]; String output = args[1]; String input = args[2];
		 */
		reader = getReader();

		String input = "./resources/indexedFiles/input.txt";
		String output = "./resources/outputFiles/output.txt";

		BufferedWriter write = null;
		try {
			InputStreamReader fileReader = new InputStreamReader(new FileInputStream(input), "UTF-8");
			Scanner scanner = new Scanner(fileReader);
			write = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), StandardCharsets.UTF_8));

			Fields fields = MultiFields.getFields(reader);
			String[] languages = new String[] { "text_en", "text_fr", "text_es" };
			int totalLang = languages.length;
			int count = 0;
			for (int i = 0; i < totalLang; i++) {
				Terms terms = fields.terms(languages[i]);
				TermsEnum termIterator = terms.iterator();
				BytesRef term;
				while ((term = termIterator.next()) != null) {
					// System.out.println(count++);
					PostingsEnum postings = MultiFields.getTermDocsEnum(reader, languages[i], term);
					LinkedList<Integer> docIDlist = new LinkedList<Integer>();
					int j;
					while ((j = postings.nextDoc()) != PostingsEnum.NO_MORE_DOCS) {
						docIDlist.add(j);
					}
					termMap.put(term.utf8ToString(), docIDlist);
				}
			}

			for (Map.Entry<String, LinkedList> entry : termMap.entrySet()) {
				String key = entry.getKey().toString();
				LinkedList value = entry.getValue();
				//out.println(key + value);
				System.out.println(termMap.size());
			}

			while (scanner.hasNextLine()) {
				String value = scanner.nextLine();
				String[] queryTerm = value.split(" ");
				getPostingsLists(queryTerm, write);

				TermAtTime.TaatAnd(queryTerm, write);
				TermAtTime.TaatOr(queryTerm, write);
				// TermAtTime.TaatSkip(queryTerm, write);
				DocumentAtTime.DaatAnd(queryTerm, write);
				DocumentAtTime.DaatOr(queryTerm, write);
				// DocumentAtTime.DaatSkip(queryTerm, write);

			}

		} catch (IOException e) {
			e.printStackTrace();

			System.out.println("Exception is" + e);
		}
		write.close();
	}

	private static IndexReader getReader() {
		Path dataDir = Paths.get(path);
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(FSDirectory.open(dataDir));
		} catch (IOException e) {
			System.out.println("Reader not found");
			e.printStackTrace();
		}
		return reader;
	}

	private static void getPostingsLists(String queryTerm[], BufferedWriter out) throws IOException {
		LinkedList<Integer> queryTermList = new LinkedList<>();
		int queryTermLength = queryTerm.length;

		for (int i = 0; i < queryTermLength; i++) {
			out.write("GetPostings");
			out.newLine();
			out.write(queryTerm[i]);
			out.newLine();
			out.write("Postings list: ");
			queryTermList = termMap.get(queryTerm[i]);
			if (queryTermList != null) {
				String queryValue = queryTermList.toString();
				String extractedQueryValue = (queryValue.substring(1, queryValue.length() - 1));
				out.write(extractedQueryValue.replace(",", ""));
				out.newLine();
			} else {
				out.write("empty");
				out.newLine();
			}
		}
	}

}
