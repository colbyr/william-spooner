(ns william-spooner.wikipedia
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

(defn filter-spoonerable [candidates]
  (filterv #(sp/is-spoonerable (clean (:title %))) candidates))


(defn best-candidate []
  (first
   (sort-by
    #(sp/potential-index (:title %))
    (filter-spoonerable (titles-and-urls)))))

(:title (best-candidate))

(defn get-spoonerism []
  (let [candidate (best-candidate)]
    {:link (candidate :fullurl)
     :spoonerism (sp/spoonerize (clean (:title candidate)))
     :original (clean (:title candidate))}))

(let [sp (get-spoonerism)]
  (str (:original sp) " => " (:spoonerism sp)))
