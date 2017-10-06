/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private int wordlength = DEFAULT_WORD_LENGTH;
    private Random random = new Random();
    ArrayList<String> wordlist = new ArrayList<>();
    HashSet<String> wordset = new HashSet<String>();
    ArrayList<String> w1;
    HashMap<String,ArrayList<String>> letterstowords = new HashMap<String,ArrayList<String>>();
    HashMap<Integer,ArrayList<String>>sizetowords = new HashMap<Integer,ArrayList<String>>();
    ArrayList<String> sizeanagram = null;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordset.add(word);
            wordlist.add(word);
            if(sizetowords.containsKey(word.length()))
            {
                sizeanagram=sizetowords.get(word.length());
                sizeanagram.add(word);
            }
            else
            {
                sizeanagram = new ArrayList<String>();
                sizeanagram.add(word);
                sizetowords.put(word.length(),sizeanagram);
            }
            String st= sortletters(word);
            if (letterstowords.containsKey(st))
            {
                   w1=letterstowords.get(st);
                    w1.add(word);
            }
            else
            {
                w1 = new ArrayList<String>();
                w1.add(word);
                letterstowords.put(st,w1);
            }

        }

    }

    public boolean isGoodWord(String word, String base) {

        if(word.contains(base))
        {
            return false;
        }
        if(wordset.contains(word))
        {
            return true;
        }

        return false;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        //int len = targetWord.length();
        String s1= sortletters(targetWord);
         result.addAll(letterstowords.get(s1));
        return result;
    }

    public String sortletters(String targetword)
    {

        char ch[] = targetword.toCharArray();
        Arrays.sort(ch);
        String s= new String(ch);
        return s;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        //char ch ='a';
        String temp,k=null;
        ArrayList<String> x = new ArrayList<>();
        for(char i='a';i<='z';i++)
        {
            result = new ArrayList<String>();
            temp = word;
            temp+=  i;
            k =sortletters(temp);
            if(letterstowords.containsKey(k))
            {
                 x.addAll(letterstowords.get(k));
            }
            for(int j = 0;j < x.size();j++){
                if(isGoodWord(x.get(j),word))
                {
                    result.add(x.get(j));
                }
            }
        }

        return result;
    }

    public String pickGoodStarterWord() {

        String tempStartWord = null;
        //String sortTempStartWord = null;
        int numberOfAnagram = 0;

        ArrayList<String> StarterWords = new ArrayList<String>();
        do{
            StarterWords = sizetowords.get(wordlength);
            tempStartWord = StarterWords.get(random.nextInt(StarterWords.size()));
            numberOfAnagram = getAnagramsWithOneMoreLetter(tempStartWord).size();

        }while (numberOfAnagram <= MIN_NUM_ANAGRAMS && numberOfAnagram >0);
        Log.d("test", "numberofAnagram: "+numberOfAnagram);
        if(wordlength <= MAX_WORD_LENGTH){
            wordlength++;
        }
        return tempStartWord;

    }
}
