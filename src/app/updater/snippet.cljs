
(ns app.updater.snippet (:require [app.schema :as schema]))

(defn create [db op-data sid op-id op-time]
  (assoc-in db [:snippets op-id] (merge schema/snippet op-data {:id op-id, :time op-time})))

(defn remove-one [db op-data sid op-id op-time]
  (update db :snippets (fn [snippets] (dissoc snippets op-data))))

(defn update-title [db op-data sid op-id op-time]
  (assoc-in db [:snippets (:id op-data) :name] (:name op-data)))

(defn update-tree [db op-data sid op-id op-time]
  (assoc-in db [:snippets (:id op-data) :tree] (:tree op-data)))
