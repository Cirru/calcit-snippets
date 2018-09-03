
(ns app.updater.snippet (:require [app.schema :as schema]))

(defn create [db op-data sid op-id op-time]
  (assoc-in
   db
   [:snippets op-id]
   (merge schema/snippet {:id op-id, :time op-time, :text op-data})))
