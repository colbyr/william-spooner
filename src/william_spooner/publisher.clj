(ns william-spooner.publisher
  (:require
    [william-spooner.wikipedia :as wikipedia]
    [william-spooner.twitter :as twitter]))

(defn compose-tweet [spoonerism]
  (str (:spoonerism spoonerism) " " (:link spoonerism)))

(defn -main [&]
  (let [spoonerism (wikipedia/get-spoonerism)]
    (println (System/currentTimeMillis) ": Generated new spoonerism :" (:original spoonerism) ":" (:spoonerism spoonerism))
    (let [tweet (compose-tweet spoonerism)]
      (twitter/publish tweet)
      (println "Tweeted :" tweet))))
