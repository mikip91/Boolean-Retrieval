package com.buffalo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

public class TermAtTime {

	public static void TaatOr(String[] queryTerm, BufferedWriter write) throws IOException {
		int queryTermLength = queryTerm.length;
		ArrayList<LinkedList<Integer>> termPostingList = new ArrayList<LinkedList<Integer>>();

		int i = 0;
		for (i = 0; i < queryTermLength; i++) {
			termPostingList.add(IRProject2.termMap.get(queryTerm[i]));
		}

		Collections.sort(termPostingList, new Comparator<LinkedList<Integer>>() {
			@Override
			public int compare(LinkedList<Integer> o1, LinkedList<Integer> o2) {
				if (o1.size() > o2.size()) {
					return 1;
				} else if (o1.size() < o2.size()) {
					return -1;
				} else {
					return 0;
				}

			}
		});

		int count = 0;
		for (int j = 0; j < queryTermLength - 1; j++) {
			LinkedList<Integer> temp = new LinkedList<Integer>();
			int pointer1 = 0;
			int pointer2 = 0;
			while (pointer1 < termPostingList.get(j).size() && pointer2 < termPostingList.get(j + 1).size()) {
				count++;
				if (termPostingList.get(j).get(pointer1) < termPostingList.get(j + 1).get(pointer2)) {
					temp.add(termPostingList.get(j).get(pointer1));
					pointer1++;
				} else if (termPostingList.get(j).get(pointer1).equals(termPostingList.get(j + 1).get(pointer2))) {
					temp.add(termPostingList.get(j).get(pointer1));
					pointer1++;
					pointer2++;
				} else {
					temp.add(termPostingList.get(j + 1).get(pointer2));
					pointer2++;
				}
			}

			while (pointer1 < termPostingList.get(j).size()) {
				temp.add(termPostingList.get(j).get(pointer1));
				pointer1++;
			}

			while (pointer2 < termPostingList.get(j + 1).size()) {
				temp.add(termPostingList.get(j + 1).get(pointer2));
				pointer2++;
			}
			termPostingList.remove(j + 1);
			termPostingList.add(j + 1, temp);
		}
		write.write("TaatOr");
		write.newLine();
		for (int k = 0; k < queryTermLength; k++) {
			write.write(queryTerm[k] + " ");
		}
		write.newLine();
		write.write("Results: ");
		if (termPostingList.get(queryTermLength - 1) != null) {
			String queryValue = termPostingList.get(queryTermLength - 1).toString();
			String extractedQueryValue = (queryValue.substring(1, queryValue.length() - 1));
			write.write(extractedQueryValue.replace(",", ""));
			write.newLine();
			write.write("Number of documents in results: " + termPostingList.get(queryTermLength - 1).size());
			write.newLine();
			write.write("Number of comparisons: " + count);
			write.newLine();
		}
	}

	/*public static void TaatAnd(String[] queryTerm, BufferedWriter write) throws IOException {
		int queryTermLength = queryTerm.length;
		ArrayList<LinkedList<Integer>> termPostingList = new ArrayList<LinkedList<Integer>>();

		int i = 0;
		for (i = 0; i < queryTermLength; i++) {
			termPostingList.add(IRProject2.termMap.get(queryTerm[i]));
		}

		Collections.sort(termPostingList, new Comparator<LinkedList<Integer>>() {

			@Override
			public int compare(LinkedList<Integer> o1, LinkedList<Integer> o2) {
				if (o1.size() > o2.size()) {
					return 1;
				} else if (o1.size() < o2.size()) {
					return -1;
				} else {
					return 0;
				}
			}
		});

		int count = 0;
		for (int j = 0; j < queryTermLength - 1; j++) {
			LinkedList<Integer> temp = new LinkedList<Integer>();
			int pointer1 = 0;
			int pointer2 = 0;
			int skipLength1 = (int) Math.sqrt((termPostingList.get(j).size()));
			int skipLength2 = (int) Math.sqrt((termPostingList.get(j + 1).size()));
			while (pointer1 < termPostingList.get(j).size() && pointer2 < termPostingList.get(j + 1).size()) {
				count++;
				if (termPostingList.get(j).get(pointer1).equals(termPostingList.get(j + 1).get(pointer2))) {
					temp.add(termPostingList.get(j).get(pointer1));
					pointer1++;
					pointer2++;
				} else if (termPostingList.get(j).get(pointer1) < termPostingList.get(j + 1).get(pointer2)) {
					pointer1++;
				} else {
					pointer2++;
				}
			}
			termPostingList.add(j + 1, temp);
		}

		write.write("TaatAnd");
		write.newLine();
		for (int k = 0; k < queryTermLength; k++) {
			write.write(queryTerm[k] + " ");
		}
		write.newLine();
		write.write("Results: ");

		if (termPostingList.get(queryTermLength - 1) != null) {
			if (termPostingList.get(queryTermLength - 1).isEmpty()) {
				write.write("empty");
				write.newLine();
			} else if (queryTermLength == 1) {
				write.write("Single list Found");
				write.newLine();
			} else {
				String queryValue = termPostingList.get(queryTermLength - 1).toString();
				String extractedQueryValue = (queryValue.substring(1, queryValue.length() - 1));
				write.write(extractedQueryValue.replace(",", ""));
				write.newLine();
			}
			write.write("Number of documents in results: " + termPostingList.get(queryTermLength - 1).size());
			write.newLine();
			write.write("Number of comparisons: " + count);
			write.newLine();
		}
	}*/

	public static void TaatAnd(String[] queryTerm, BufferedWriter write) throws IOException {
		int queryTermLength = queryTerm.length;
		ArrayList<LinkedList<Integer>> termPostingList = new ArrayList<LinkedList<Integer>>();

		int i = 0;
		for (i = 0; i < queryTermLength; i++) {
			termPostingList.add(IRProject2.termMap.get(queryTerm[i]));
		}

		Collections.sort(termPostingList, new Comparator<LinkedList<Integer>>() {

			@Override
			public int compare(LinkedList<Integer> o1, LinkedList<Integer> o2) {
				if (o1.size() > o2.size()) {
					return 1;
				} else if (o1.size() < o2.size()) {
					return -1;
				} else {
					return 0;
				}
			}
		});

		int count = 0;
		for (int j = 0; j < queryTermLength - 1; j++) {
			LinkedList<Integer> temp = new LinkedList<Integer>();
			int pointer1 = 0;
			int pointer2 = 0;
			int skipLength1 = (int) Math.floor(Math.sqrt((termPostingList.get(j).size())));
			int skipLength2 = (int) Math.floor(Math.sqrt((termPostingList.get(j + 1).size())));
			int list1Size = termPostingList.get(j).size();
			int list2Size = termPostingList.get(j + 1).size();
			while (pointer1 < termPostingList.get(j).size() && pointer2 < termPostingList.get(j + 1).size()) {
				//count++;
				int posting1 = termPostingList.get(j).get(pointer1);
				int posting2 = termPostingList.get(j + 1).get(pointer2);
				if (posting1 == posting2) {
					count++;
					temp.add(posting1);
					pointer1++;
					pointer2++;
				} else if (posting1 < posting2) {
					while (hasSkip(pointer1, list1Size) && pointer1 + skipLength1 < list1Size
							&& termPostingList.get(j).get(pointer1 + skipLength1) <= posting2) {
						pointer1 = pointer1 + skipLength1;
					}
					count++;
					pointer1++;
				} else {
					while (hasSkip(pointer2, list2Size) && pointer2 + skipLength2 < list2Size
							&& termPostingList.get(j + 1).get(pointer2 + skipLength2) <= posting1) {
						pointer2 = pointer2 + skipLength2;
					}
					count++;
					pointer2++;
				}

			}
			termPostingList.remove(j + 1);
			termPostingList.add(j + 1, temp);
		}

		write.write("TaatAnd");
		write.newLine();
		for (int k = 0; k < queryTermLength; k++) {
			write.write(queryTerm[k] + " ");
		}
		write.newLine();
		write.write("Results: ");

		if (termPostingList.get(queryTermLength - 1) != null) {
			if (termPostingList.get(queryTermLength - 1).isEmpty()) {
				write.write("empty");
				write.newLine();
			} else if (queryTermLength == 1) {
				write.write("Single list Found");
				write.newLine();
			} else {
				String queryValue = termPostingList.get(queryTermLength - 1).toString();
				String extractedQueryValue = (queryValue.substring(1, queryValue.length() - 1));
				write.write(extractedQueryValue.replace(",", ""));
				write.newLine();
			}
			write.write("Number of documents in results: " + termPostingList.get(queryTermLength - 1).size());
			write.newLine();
			write.write("Number of comparisons: " + count);
			write.newLine();
		}

	}

	private static boolean hasSkip(int p, int size) {
		int skipLength = (int) Math.floor(Math.sqrt(size));
		return p % skipLength == 0;
	}
}