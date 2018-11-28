package com.buffalo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class DocumentAtTime {
	static LinkedList<Integer> temp;

	public static void DaatOr(String[] queryTerm, BufferedWriter write) throws IOException {
		int queryLength = queryTerm.length;
		int count = 0;
		ArrayList<LinkedList<Integer>> termPostingList = new ArrayList<LinkedList<Integer>>();
		int pointer[] = new int[queryTerm.length];

		for (int i = 0; i < queryLength; i++) {
			termPostingList.add(IRProject2.termMap.get(queryTerm[i]));
			pointer[i] = 0;
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

		int minValue = 0;
		temp = new LinkedList<Integer>();
		while (true) {
			int counter = 0;
			minValue = Integer.MAX_VALUE;
			for (int i = 0; i < queryLength; i++) {
				if (!(pointer[i] >= termPostingList.get(i).size())) {
					if (termPostingList.get(i).get(pointer[i]) < minValue) {
						minValue = termPostingList.get(i).get(pointer[i]);
						count++;
					}
				} else {
					counter++;
				}
			}

			if (counter == queryLength) {
				break;
			}

			for (int i = 0; i < queryLength; i++) {
				if (!(pointer[i] >= termPostingList.get(i).size())) {
					if (termPostingList.get(i).get(pointer[i]) == minValue) {
						pointer[i]++;
					}
				} else {
					counter++;
				}
			}

			temp.add(minValue);

		}

		write.write("DaatOr");
		write.newLine();
		for (int i = 0; i < queryLength; i++) {
			write.write(queryTerm[i] + " ");
		}
		write.newLine();
		write.write("Results: ");
		String s = temp.toString();
		write.write((s.substring(1, s.length() - 1)).replace(",", ""));
		write.newLine();
		write.write("Number of documents in results : " + temp.size());
		write.newLine();
		write.write("Number of comparisons: " + count);
		write.newLine();

	}

	public static void DaatAnd(String[] queryTerm, BufferedWriter write) throws IOException {
		int queryLength = queryTerm.length;
		int count = 0;
		ArrayList<LinkedList<Integer>> termPostingList = new ArrayList<LinkedList<Integer>>();
		int pointer[] = new int[queryTerm.length];

		for (int i = 0; i < queryLength; i++) {
			termPostingList.add(IRProject2.termMap.get(queryTerm[i]));
			pointer[i] = 0;
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
		int minValue = 0;
		int flag = 0;
		temp = new LinkedList<Integer>();
		while (true) {
			minValue = Integer.MAX_VALUE;
			for (int i = 0; i < queryLength; i++) {
				if (pointer[i] >= termPostingList.get(i).size()) {
					flag = 1;
					break;
				}

				if (termPostingList.get(i) == null) {
					flag = 1;
					break;
				}

				if (termPostingList.get(i).get(pointer[i]) < minValue) {
					minValue = termPostingList.get(i).get(pointer[i]);
					count++;
				}
			}

			if (flag == 1) {
				break;
			}

			int compare = 0;
			for (int i = 0; i < queryLength; i++) {
				if (termPostingList.get(i).get(pointer[i]) == minValue) {
					pointer[i]++;
					compare++;
					count++;
				}
			}

			if (compare == queryLength) {
				temp.add(minValue);
			}

		}

		write.write("DaatAnd");
		write.newLine();
		for (int i = 0; i < queryLength; i++) {
			write.write(queryTerm[i] + " ");
		}
		write.newLine();
		write.write("Results: ");

		if (temp.size() >= 0) {
			if (temp.size() == 0) {
				write.write("empty");
				write.newLine();
			} else if (queryLength == 1) {
				write.write("Single list Found");
				write.newLine();
			} else {
				String s = temp.toString();
				write.write((s.substring(1, s.length() - 1)).replace(",", ""));
				write.newLine();
			}
			write.write("Number of documents in results : " + temp.size());
			write.newLine();
			write.write("Number of comparisons: " + count);
			write.newLine();

		}

	}

	/*
	 * public static void DaatSkip(String[] queryTerm, BufferedWriter write) throws
	 * IOException { int queryLength = queryTerm.length; int count = 0;
	 * ArrayList<LinkedList<Integer>> termPostingList = new
	 * ArrayList<LinkedList<Integer>>(); int pointer[] = new int[queryLength]; int
	 * skipLength[] = new int[queryLength];
	 * 
	 * for (int i = 0; i < queryLength; i++) {
	 * termPostingList.add(IRProject2.termMap.get(queryTerm[i])); pointer[i] = 0;
	 * skipLength[i] = (int) Math.ceil(Math.sqrt((termPostingList.get(i).size())));
	 * }
	 * 
	 * Collections.sort(termPostingList, new Comparator<LinkedList<Integer>>() {
	 * 
	 * @Override public int compare(LinkedList<Integer> o1, LinkedList<Integer> o2)
	 * { if (o1.size() > o2.size()) { return 1; } else if (o1.size() < o2.size()) {
	 * return -1; } else { return 0; }
	 * 
	 * } });
	 * 
	 * int minValue = 0; int flag = 0; temp = new LinkedList<Integer>(); while
	 * (true) { minValue = Integer.MAX_VALUE; for (int i = 0; i < queryLength; i++)
	 * { if (pointer[i] >= termPostingList.get(i).size()) { flag = 1; break; }
	 * 
	 * if (termPostingList.get(i) == null) { flag = 1; break; }
	 * 
	 * if (termPostingList.get(i).get(pointer[i]) < minValue) { count++; minValue =
	 * termPostingList.get(i).get(pointer[i]);
	 * 
	 * int listSize = termPostingList.get(i).size(); int skipPointerIndex =
	 * pointer[i]+skipLength[i]; while (hasSkip(pointer[i], listSize) &&
	 * skipPointerIndex < listSize && termPostingList.get(i).get(skipPointerIndex)
	 * <= minValue) { minValue = termPostingList.get(i).get(skipPointerIndex);
	 * pointer[i] = pointer[i] + skipPointerIndex; }
	 * 
	 * } }
	 * 
	 * if (flag == 1) { break; }
	 * 
	 * int compare = 0; for (int i = 0; i < queryLength; i++) { if
	 * (termPostingList.get(i).get(pointer[i]) == minValue) {
	 * 
	 * ArrayList<LinkedList<Integer>> otherPostingList = new
	 * ArrayList<LinkedList<Integer>>(); otherPostingList.addAll(termPostingList);
	 * otherPostingList.remove(i); int listSize = termPostingList.get(i).size();
	 * 
	 * boolean skip = false; int skipCounter = 0; int j; while (hasSkip(pointer[i],
	 * listSize) && pointer[i] + skipLength[i] < listSize) { for (j = 0; j <
	 * otherPostingList.size(); j++) { if (termPostingList.get(i).get(pointer[i] +
	 * skipLength[i]) < listSize && termPostingList.get(i).get(pointer[i] +
	 * skipLength[i]) <= otherPostingList.get(j) .get(pointer[i])) { pointer[i] =
	 * pointer[i] + skipLength[i]; skipCounter++; } }
	 * 
	 * if (j == skipCounter) { pointer[i] = pointer[i] + skipLength[i]; break; }
	 * else { pointer[i]++; } }
	 * 
	 * pointer[i]++; compare++; } else if (termPostingList.get(i).get(pointer[i]) >
	 * minValue) { int listSize = termPostingList.get(i).size(); while
	 * (hasSkip(pointer[i], listSize) && pointer[i] + skipLength[i] < listSize &&
	 * termPostingList.get(i).get(pointer[i] + skipLength[i]) <= posting[i]) {
	 * pointer2 = pointer2 + skipLength[i]; } count++; pointer2++; } else if
	 * (termPostingList.get(i).get(pointer[i]) < minValue) {
	 * 
	 * }
	 * 
	 * }
	 * 
	 * if (compare == queryLength) { temp.add(minValue); }
	 * 
	 * }
	 * 
	 * write.write("DaatSkip"); write.newLine(); for (int i = 0; i < queryLength;
	 * i++) { write.write(queryTerm[i] + " "); } write.newLine();
	 * write.write("Results: ");
	 * 
	 * if (temp.size() >= 0) { if (temp.size() == 0) { write.write("empty");
	 * write.newLine(); } else if (queryLength == 1) {
	 * write.write("Single list Found"); write.newLine(); } else { String s =
	 * temp.toString(); write.write((s.substring(1, s.length() - 1)).replace(",",
	 * "")); write.newLine(); } write.write("Number of documents in results : " +
	 * temp.size()); write.newLine(); write.write("Number of comparisons: " +
	 * count); write.newLine();
	 * 
	 * }
	 * 
	 * }
	 */

	private static boolean hasSkip(int p, int size) {
		int skipLength = (int) Math.ceil(Math.sqrt(size));
		return p % skipLength == 0;
	}

}
