(ns william-spooner.twitter
  (:require [environ.core :refer [env]])
  (:use
    [twitter.oauth]
    [twitter.callbacks]
    [twitter.callbacks.handlers]
    [twitter.api.restful]))

(def credentials
  (make-oauth-creds (System/getenv "TWITTER_CONSUMER_KEY")
                    (System/getenv "TWITTER_CONSUMER_SECRET")
                    (System/getenv "TWITTER_USER_ACCESS_TOKEN")
                    (System/getenv "TWITTER_USER_ACCESS_TOKEN_SECRET")))

(defn publish [status]
  (statuses-update :oauth-creds credentials
                   :params {:status status}))

(defn url-length []
  (:short_url_length (help-configuration :oauth-creds credentials)))
