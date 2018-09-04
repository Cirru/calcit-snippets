
(ns app.comp.workspace
  (:require [respo.macros :refer [defcomp <> div input list-> button span pre]]
            [respo.comp.space :refer [=<]]
            [respo.comp.inspect :refer [comp-inspect]]
            [respo-ui.core :as ui]
            [app.schema :as schema]
            [app.style :as style]
            [app.config :as config]
            [respo.util.list :refer [map-val]]
            ["copy-text-to-clipboard" :as copy!]
            [respo-alerts.comp.alerts :refer [comp-alert]]))

(defcomp
 comp-workspace
 (snippets)
 (div
  {:style {:padding 16}}
  (list->
   {}
   (->> snippets
        (map-val
         (fn [snippet]
           (pre
            {:innerHTML (:text snippet),
             :style {:border "1px solid #ddd", :cursor :pointer, :padding 8},
             :on-click (fn [e d! m!] (copy! (:text snippet)))})))))))
