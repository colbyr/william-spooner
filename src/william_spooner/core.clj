(ns william-spooner.core
  (:require [william-spooner.spoonerism :as sp])
  (:require [clj-http.client :as client])
  (:require [clojure.string :as str])
  (:gen-class))

(def random-uri "http://en.wikipedia.org/w/api.php?action=query&generator=random&grnnamespace=0&prop=info&inprop=url&format=json&grnlimit=10&continue=")

(defn query []
  (client/get random-uri {:as :json}))

(defn titles-and-urls []
  (map
   #(select-keys % [:title :fullurl])
   (vals (:pages (:query (:body (query)))))))

(defn clean [phrase]
  (first (str/split phrase #" \(")))

(defn best-candidate []
  (first
    (filterv #(sp/is-spoonerable (clean (:title %))) (titles-and-urls))))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [phrase (clean (:title (best-candidate)))]
    (println (str "Spoonerizing: " phrase " -> " (sp/spoonerize phrase)))))
