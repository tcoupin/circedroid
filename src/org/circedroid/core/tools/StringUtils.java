package org.circedroid.core.tools;

import java.util.HashMap;
import java.util.logging.Logger;

import android.util.Log;

public class StringUtils {

	public static float leven(String str0, String str1) {
		if (str0 == null || str1 == null) {
			return 0f;
		}
		if (str0.length() == 0 || str1.length() == 0) {
			return 0f;
		}
		str0 = normalize(str0);
		str1 = normalize(str1);

		int[] previousLine = new int[str0.length() + 1];
		int[] currentLine = new int[str0.length() + 1];

		int cout = 0;
		for (int i = 0; i <= str0.length(); i++) {
			previousLine[i] = i;
		}
		for (int j = 1; j <= str1.length(); j++) {
			currentLine[0] = j;
			for (int i = 1; i <= str0.length(); i++) {
				cout = (str0.charAt(i - 1) == str1.charAt(j - 1) ? 0 : 1);
				currentLine[i] = Math.min(
						Math.min(previousLine[i] + 1, currentLine[i - 1] + 1),
						previousLine[i - 1] + cout);
			}
			for (int i = 0; i<currentLine.length;i++){
				previousLine[i] = currentLine[i];
			}
			
		}

		//Log.v("Leven", "rsult : " + currentLine[str0.length()]);

		float max = (float) Math.max(str0.length(), str1.length());
		//Log.v("Leven", "max : " + max);
		return (max - ((float) currentLine[str0.length()])) / max;
	}

	public static float similitude(String search, String ref) {
		if (search == null || ref == null) {
			return 0f;
		}
		if (search.length() == 0 || ref.length() == 0) {
			return 0f;
		}
		String[] refSplit = ref.split(" ");
		String[] searchSplit = search.split(" ");

		// On calcul tous les distance de levenstein mot a mot
		float[][] scores = new float[refSplit.length][searchSplit.length];
		for (int i = 0; i < refSplit.length; i++) {
			for (int j = 0; j < searchSplit.length; j++) {
				scores[i][j] = leven(refSplit[i], searchSplit[j]);
			}
		}

		float score = 0f;
		int nb = 0;
		int idelta = 0;
		int jdelta = 0;
		float max;
		boolean bottomisbigger;
		boolean rightisbigger;
		// On calcul le score en diagonal. Pour gérer l'oubli d'un mot, on peut
		// se décaler d'un mot en bas ou a droite
		for (int i = 0; i < Math.min(refSplit.length, searchSplit.length); i++) {

			if ((i + idelta) >= refSplit.length
					|| (i + jdelta) >= searchSplit.length)
				break;
			score += scores[i + idelta][i + jdelta];
			nb++;

			max = scores[i + idelta][i + jdelta];
			rightisbigger = false;
			if ((i + idelta + 1) < refSplit.length) {
				if (max < scores[i + idelta + 1][i + jdelta]) {
					max = scores[i + idelta + 1][i + jdelta];
					rightisbigger = true;
				}
			}
			bottomisbigger = false;
			if ((i + jdelta + 1) < searchSplit.length) {
				if (max < scores[i + idelta][i + jdelta + 1]) {
					max = scores[i + idelta + 1][i + jdelta + 1];
					bottomisbigger = true;
				}
			}
			if (rightisbigger && bottomisbigger) {
				// bottom is bigger
				jdelta++;
				score += scores[i + idelta][i + jdelta];
				nb++;
			} else if (rightisbigger && !bottomisbigger) {
				// right is bigger
				idelta++;
				score += scores[i + idelta][i + jdelta];
				nb++;
			}

		}

		return score / ((float) nb);
	}

	public static String normalize(String input) {
		input = input.toLowerCase();
		char[][] replaceMap = { { 'e', 'é' }, { 'e', 'è' }, { 'e', 'ê' },
				{ 'a', 'à' }, { 'c', 'ç' }, { 'i', 'î' }, { 'o', 'ô' },
				{ 'u', 'ù' }, { 'u', 'û' } };
		for (int i = 0; i < replaceMap.length; i++) {
			input = input.replace(replaceMap[i][1], replaceMap[i][0]);
		}
		return input;
	}
}
