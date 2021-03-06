(ns william-spooner.spoonerism
  (:require [clojure.string :as str]
            [clojure.set :refer [subset?]]))

(def abcs (set "abcdefghijklmnopqrstuvwxyz1234567890"))
(def blacklist (hash-set "a" "an" "and" "but" "nor" "in" "is" "of" "or" "some" "that" "the" "this" "to"))
(def ignore-pattern #"(?i)^[^aeiou]*$|\d")
(def pattern #"(?i)^(gu|[^aeiou]*qu|o\'[^aeiou]*|y[^aeiou1-9]*|[^aeiouy1-9]*)([^\s]*$)")


(defn ignore? [word]
  (if (contains? blacklist (str/lower-case word))
    true
    (not (nil? (re-find ignore-pattern word)))))

(defn filter-candidates [words]
  (filter #(not (ignore? %)) words))

(defn parse-phrase [phrase]
  (str/split (str/trim phrase) #"[\s,.!?-]+"))

(defn split-word [word]
  (subvec (first (re-seq pattern word)) 1))

(defn pull [i v]
  (map #(nth % i) v))

(defn is-spoonerable [phrase]
  (let [candidate-words (vec (filter-candidates (parse-phrase phrase)))]
    (if-not (> (count candidate-words) 1)
      false
      (if-not (> (count (set (pull 0 (map split-word candidate-words)))) 1)
        false
        (> (first (sort (map count candidate-words))) 2)))))

(defn potential-index-word-length [word]
  (if (> (count word) 10)
    2
    (if (< (count word) 3)
      1
      0)))

(defn potential-index-word-characters [word]
  (let [characters (set (str/lower-case word))]
    (if (subset? characters abcs) 0 1)))

(defn potential-index-word-prefix [word]
  (if (= (first (split-word word)) "") 1 0))

(defn potential-index-word [word]
  (+
   1
   (potential-index-word-length word)
   (potential-index-word-characters word)
   (potential-index-word-prefix word)))

(defn potential-index-phrase [phrase]
  (if-not (nil? (re-find #"[.,-=?!&^%$@()+'/\\\"]" phrase))
    1
    0))

(defn potential-index [phrase]
  (let [words (vec (filter-candidates (parse-phrase phrase)))]
    (+
     (if (> (count words) 3) 1 0)
     (potential-index-phrase phrase)
     (reduce + 0 (map potential-index-word words)))))

(defn rotate [n coll]
  (let [c (count coll)]
    (take c (drop (mod n c) (cycle coll)))))

(defn rotate-prefixs [pairs]
  (let [prefixes (rotate (dec (count pairs)) (pull 0 pairs))
        suffixes (pull 1 pairs)]
    (mapv #(str/capitalize (str  %1 %2)) prefixes suffixes)))

(defn hydrate-articles [words spooners]
  (if (= (count words) (count spooners))
    spooners
    (loop [i 0 result words remaining spooners]
      (if (empty? remaining)
        result
        (let [is-replacable (not (ignore? (nth result i)))]
          (recur
            (inc i)
            (if is-replacable (assoc result i (first remaining)) result)
            (if is-replacable (subvec remaining 1) remaining)))))))

(defn spoonerize [phrase]
  (let [words (parse-phrase phrase)]
    (str/join " "
      (hydrate-articles
        words
        (rotate-prefixs (map split-word (filter-candidates words)))))))
