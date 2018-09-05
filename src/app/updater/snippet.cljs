
(ns app.updater.snippet (:require [app.schema :as schema]))

(defn create [db op-data sid op-id op-time]
  (assoc-in
   db
   [:snippets op-id]
   (merge schema/snippet {:id op-id, :time op-time, :tree op-data})))

(defn remove-one [db op-data sid op-id op-time]
  (update db :snippets (fn [snippets] (dissoc snippets op-data))))
