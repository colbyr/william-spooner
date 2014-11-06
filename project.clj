(defproject william-spooner "0.1.0-SNAPSHOT"
  :description "The spoonerizing twitter bot."
  :url "http://twitter.com/artofspoonerism"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clj-http "1.0.1"]
                 [compojure "1.1.8"]
                 [ring/ring-jetty-adapter "1.2.2"]
                 [environ "0.5.0"]]
  :main ^:skip-aot william-spooner.web
  :min-lein-version "2.0.0"
  :plugins [[environ/environ.lein "0.2.1"]]
  :hooks [environ.leiningen.hooks]
  :uberjar-name "william-spooner-standalone.jar"
  :profiles {:production {:env {:production true}}})
