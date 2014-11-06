(ns william-spooner.web
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [ring.adapter.jetty :as jetty]
            [environ.core :refer [env]]
            [william-spooner.twitter :as twitter]))

(defn splash []
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str "<DOCTYPE html><html><body>The name's Wooner. <a href=\"http://en.wikipedia.org/wiki/William_Archibald_Spooner\">Spilliam Wooner</a>.</body></html>")})

(defroutes app
  (GET "/" []
       (splash))
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))

;; For interactive development:
;; (.stop server)
;; (def server (-main))
